package li.changlin.video.controller;

import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import li.changlin.common.utils.VideoAnalysis;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
public class VideoConSub {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FastFileStorageClient storageClient;

    @PostMapping("/video/upload")
    @ResponseBody
    public Map<String,Object> uploadfile(@RequestPart("file") MultipartFile[] files)throws IOException{
        String picturePath="";
        String videoPath="";
        Map<String,Object> map= new HashMap<>();
        Map<String,Object> nullmap= new HashMap<>();
        for (MultipartFile file : files){
            if (Pattern.matches("image/.+",file.getContentType())){
                String imageext = FilenameUtils.getExtension(file.getOriginalFilename());
                StorePath uploadFile = storageClient.uploadFile("group1", file.getInputStream(), file.getSize(), imageext);
                picturePath = "http://192.168.30.164:8888/"+uploadFile.getFullPath();
                map.put("imageUrl",picturePath);
                map.put("videoUrl",videoPath);
                map.put("videoMsg",nullmap);
            }if (Pattern.matches("video/.+",file.getContentType())){
                String filename = file.getOriginalFilename();
                File file2 = File.createTempFile(FilenameUtils.getBaseName(filename),"tmp");
                file.transferTo(file2);
                file2.deleteOnExit();
                String videoext = FilenameUtils.getExtension(filename);
                StorePath uploadFile = storageClient.uploadFile("group1", file.getInputStream(), file.getSize(), videoext);
                String sql = "insert into video_path (videoname,groupname,videopath) values (?,?,?)";
                Object[] obj = new Object[]{filename,uploadFile.getGroup(),uploadFile.getPath()};
                jdbcTemplate.update(sql,obj);
                videoPath = "http://192.168.30.164:8888/"+uploadFile.getFullPath();
                map.put("videoUrl",videoPath);
                map.put("videoMsg", VideoAnalysis.getVideoMsg(file2));
                map.put("imageUrl",picturePath);
            }
        }
        return map;
    }
    @PostMapping("/video/coverImage")
    @ResponseBody
    public String uploadCover (MultipartFile file)throws IOException{
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        StorePath uploadFile = storageClient.uploadFile("group1", file.getInputStream(), file.getSize(), extension);
        String sql = "insert into video_cover (covername,groupname,coverpath) values (?,?,?)";
        Object[] obj = new Object[]{file.getOriginalFilename(),uploadFile.getGroup(),uploadFile.getPath()};
        jdbcTemplate.update(sql,obj);
        return "http://192.168.30.164:8888/"+uploadFile.getFullPath();

    }
}
