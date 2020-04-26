package li.changlin.oauth2.controller;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import li.changlin.common.exception.ConstraintViolationExceptionHandler;
import li.changlin.common.response.Response;
import li.changlin.common.vo.Fiction;
import li.changlin.user.entity.User;
import li.changlin.oauth2.service.AuthorityService;
import li.changlin.oauth2.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class AdminCon {
    @Autowired
    private UsersService us;
    @Autowired
    private AuthorityService as;

    @GetMapping("/admin")
    public String admin(Model model){
        List<Fiction> fics = new ArrayList<>();
        fics.add(new Fiction("ユーザー追加","/user/add"));
        fics.add(new Fiction("ユーザー削除","/user/delete"));
        fics.add(new Fiction("ユーザー編集","/user/modify"));
        model.addAttribute("fics",fics);
        return "admin";
    }
    @GetMapping("/user")
    @ResponseBody
    public Map<String, Object> getlist(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Page<User> users= us.listAllWithPage();
        Map<String, Object> map = new HashMap<>();
        map.put("total",users.getTotal());
        map.put("rows", users.getResult());
        return map;
    }
    @GetMapping("/getUser")
    @ResponseBody
    public User getUser(@RequestParam(value = "username")String username){
        User user = us.getUserByName(username);
        return user;
    }
    @GetMapping("/user/add")
    public String adduser(Model model) {
        model.addAttribute("user", new User(null,0,null,null,null,null,0));
        return "user/add";
    }
    @DeleteMapping(value = "/user/{id}")
    @ResponseBody
    public ResponseEntity<Response> delete(@PathVariable("id") Integer id) {
        try {
            us.removeUser(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "操作完了"));
    }
    @GetMapping("/user/edit/{id}")
    public String edituser(@PathVariable("id") Integer id, Model model) {
        User user = us.getUserById(id);
        model.addAttribute("foruser", user);
        return "user/edit";
    }
    @PostMapping("/user")
    public ResponseEntity<Response> addOrEdituser(User user) {
        int userid = user.getId();
        if (userid != 0){
            User originalUser= us.getUserById(user.getId());
            originalUser.setUsername(user.getUsername());
            originalUser.setSex(user.getSex());
            originalUser.setEmail(user.getEmail());
            originalUser.setAge(user.getAge());
            originalUser.setTend(user.getTend());
            originalUser.setAuthorityid(user.getAuthorityid());
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePasswd = encoder.encode(user.getPassword());
            originalUser.setPassword(encodePasswd);
            try {
                us.updateById(originalUser);
            }  catch (ConstraintViolationException e)  {
                return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
            }
            return ResponseEntity.ok().body(new Response(true, "操作完了"));
        }else {
            user.setEncodePassword(user.getPassword());
            try {
                us.addUser(user);
            } catch (ConstraintViolationException e) {
                return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
            }
            return ResponseEntity.ok().body(new Response(true, "操作完了"));
        }
    }
}