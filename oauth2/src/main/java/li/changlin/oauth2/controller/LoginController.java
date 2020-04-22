package li.changlin.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	/**
	 * 自定义登录页面
	 * @return
	 */
	@GetMapping("/login")
	public String getlogin(){
		return "login";
	}
	@GetMapping("/login-error")
	public String loginerror(Model model){
		model.addAttribute("ifError",true);
		model.addAttribute("message","登録名またはパスワードが誤っている");
		return "login";
	}

}
