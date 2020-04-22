
package li.changlin.search.controller;

import li.changlin.search.entity.EsVideo;
import li.changlin.search.feignclient.UserFeignClient;
import li.changlin.search.mapper.SearchMapper;
import li.changlin.search.service.SearchService;
import li.changlin.search.vo.Comment;
import li.changlin.user.entity.User;
import li.changlin.video.entity.Video;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
public class EsVideoCon {
    @Autowired
    SearchMapper sm;
    @Autowired
    SearchService ss;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserFeignClient ufc;

    @GetMapping("/")
    public String listVideo(
            @RequestParam(value="nagasa",required=false,defaultValue="") String nagasa,
            @RequestParam(value="object",required=false,defaultValue="") String object,
            @RequestParam(value="gashitsu",required=false,defaultValue="") String gashitsu,
            @RequestParam(value="tags",required=false,defaultValue="") String tags,
            @RequestParam(value="keyword",required=false,defaultValue="") String keyword,
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") Integer pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="8") Integer pageSize,
            Model model) {
        //Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        //FieldSortBuilder order = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(pageIndex,pageSize, Sort.Direction.DESC,"createTime");
        Page<EsVideo> esVideoPage=null;
        if (!keyword.isEmpty()){
            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
            builder.withPageable(pageable);
            builder.withQuery(
                    QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title",keyword))
                            .should(QueryBuilders.matchQuery("tags",keyword))
                            .should(QueryBuilders.matchQuery("object",keyword))
            );
            esVideoPage = sm.search(builder.build());
        } else{
            esVideoPage = ss.findEsVideo(nagasa, object, gashitsu, tags, pageable);
        }
        List<EsVideo> esVideoList = esVideoPage.getContent();
        for (EsVideo esVideo : esVideoList) {
            String videoID = esVideo.getVideoID().toString();
            String commnum = stringRedisTemplate.hasKey("user:comm_"+videoID)?stringRedisTemplate.opsForHash().size("user:comm_"+videoID).toString():"0";
            String votenum = stringRedisTemplate.hasKey("videoid:"+videoID)?stringRedisTemplate.opsForSet().size("videoid:"+videoID).toString():"0";
            esVideo.setCommentSize(Integer.valueOf(commnum));
            esVideo.setVoteSize(Integer.valueOf(votenum));
            ss.saveEsVideo(esVideo);
        }
        model.addAttribute("videoList", esVideoList);
        model.addAttribute("page", esVideoPage);
        return (async==true?"ero/eroHontai :: #videoReplace":"ero/eroHontai");
    }
    @GetMapping("/userIcon")
    @ResponseBody
    public String getUserIcon(String username){
        String sql = "select avatar from user where username=?;";
        String icon = jdbcTemplate.queryForObject(sql,String.class,username);
        return icon;
    }
    @GetMapping("/user/{username}")
    public String userProfile(@PathVariable("username") String username,@RequestParam(value="videoID",required=false)Integer videoID, Model model) {
        User user = ufc.getUser(username);
        if (videoID != null){
            sm.deleteById(videoID);
        }
        List<EsVideo> esVideos = ss.findByUserName(username);
        model.addAttribute("videoList", esVideos);
        model.addAttribute("user", user);
        return "/user/userProfile";
    }
    @GetMapping("/video/{videoID}")
    public String getvideo(@PathVariable("videoID")Integer videoID,@RequestParam(value="async",required=false) boolean async,
                           Model model, Principal principal){
        Boolean isVoted = false;
        if (principal != null){
            String loginUser = principal.getName();
            isVoted = stringRedisTemplate.opsForSet().isMember("videoid:" + videoID, loginUser);
        }
        EsVideo video = ss.getEsVideoByVideoid(videoID).get();
        String commnum = stringRedisTemplate.hasKey("user:comm_"+videoID)?stringRedisTemplate.opsForHash().size("user:comm_"+videoID).toString():"0";
        String votenum = stringRedisTemplate.hasKey("videoid:"+videoID)?stringRedisTemplate.opsForSet().size("videoid:"+videoID).toString():"0";
        video.setCommentSize(Integer.valueOf(commnum));
        video.setVoteSize(Integer.valueOf(votenum));
        String userName = video.getUserName();
        User user = ufc.getUser(userName);
        if (!async) {
            video.setReadSize(video.getReadSize()+1);
        }
        ss.saveEsVideo(video);
        model.addAttribute("video",video);
        model.addAttribute("user",user);
        model.addAttribute("isVoted",isVoted);
        return (async==true?"video/video :: #def":"video/video");
    }
    @GetMapping("/listcomments")
    public String listComments(Integer videoId, Model model,Principal principal) {
        String currentUser = "";
        if (principal != null){
            currentUser = principal.getName();
        }
        List<Comment> comments = new ArrayList<>();
        if (stringRedisTemplate.hasKey("user:comm_" + videoId)) {
            try {
                Cursor<Map.Entry<Object, Object>> cursor = stringRedisTemplate.opsForHash().scan("user:comm_" + videoId, ScanOptions.NONE);
                while (cursor.hasNext()) {
                    Map.Entry<Object, Object> entry = cursor.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    String[] userandicon = key.split("&&");
                    String username = userandicon[0];
                    String usericon = userandicon[1];
                    String[] split = value.split("&&");
                    String content = split[0];
                    String timestamp = split[1];
                    comments.add(new Comment(content,username,usericon,timestamp));
                }
                cursor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("comments", comments);
        return "video/video :: #mainContainerRepleace";
    }
}

