package li.changlin.oauth2.controller;

import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import li.changlin.user.entity.User;
import li.changlin.oauth2.service.AuthorityService;
import li.changlin.oauth2.service.UsersService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserCon {
    @Autowired
    private UsersService us;
    @Autowired
    private AuthorityService as;
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }
    @PostMapping("/register")
//    @AdviceAnnotation
    public String postRegister(User user,Model model) {
        user.setEncodePassword(user.getPassword());
        try {
            us.addUser(user);
        }catch (ConstraintViolationException e){
            List<String> msgList = new ArrayList<>();
            for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                msgList.add(constraintViolation.getMessage());
            }
            String messages = StringUtils.join(msgList.toArray(), "、");
            model.addAttribute("failMsg",messages);
            return "userAddFail";
        }
        return "redirect:http://localhost:8765/";
    }
    @PostMapping("/userIcon")
    @ResponseBody
    public String uploadIcon(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        StorePath uploadFile = storageClient.uploadFile("group1", file.getInputStream(), file.getSize(), extension);
        String sql = "insert into user_icon (filename,groupname,filepath) values (?,?,?)";
        Object[] obj = new Object[]{file.getOriginalFilename(),uploadFile.getGroup(),uploadFile.getPath()};
        jdbcTemplate.update(sql,obj);
        return "http://192.168.30.164:8888/"+uploadFile.getFullPath();
    }
    @GetMapping("/userEdit")
    //@PreAuthorize("authentication.name.equals(#username)")
    public String getUserEdit(@RequestParam(value = "name") String username, Model model) {
        User user = us.getUserByName(username);
        model.addAttribute("user", user);
        return "user/userEdit";
    }
    @PostMapping("/userEdit")
    public String userEdit(User user) {
        User originalUser = us.getUserById(user.getId());
        originalUser.setAvatar(user.getAvatar());
        originalUser.setTend(user.getTend());
        originalUser.setAge(user.getAge());
        originalUser.setEmail(user.getEmail());
        originalUser.setSex(user.getSex());
        originalUser.setEncodePassword(user.getPassword());
        us.updateById(originalUser);
       // SecurityContextHolder.clearContext();
        return "redirect:http://localhost:8765/";
    }

}
