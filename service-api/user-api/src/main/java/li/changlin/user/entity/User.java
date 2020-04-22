package li.changlin.user.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    @NotNull(message = "名前が必要")
    @Size(min = 2, max = 20, message = "二文字以上二十文字以下で入力してください")
    private String username;

    @NotNull(message = "皆が君の正体を知りたいよ")
    private String sex;

    private String tend;

    @NotNull(message = "年齢もできれば教えてちょうだい")
    @Min(18)
    @Max(99)
    private int age;

    @NotNull(message = "メールアドレスが必要")
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "メールパターンに従ってください")
    private String email;

    @NotNull(message = "パスワードが必要")
    @Size(max = 100)
    private String password; // 登录时密码

    private String avatar;

    private int authorityid;
    private int lovers;

    protected User() {
    }


    public User(String username, int age, String tend, String email, String sex, String password,int authorityid) {
        this.username = username;
        this.age = age;
        this.tend = tend;
        this.email = email;
        this.sex = sex;
        this.password = password;
        this.authorityid = authorityid;
    }

    public int getLovers() {
        return lovers;
    }

    public void setLovers(int lovers) {
        this.lovers = lovers;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAuthorityid() {
        return authorityid;
    }

    public void setAuthorityid(int authorityid) {
        this.authorityid = authorityid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTend() {
        return tend;
    }

    public void setTend(String tend) {
        this.tend = tend;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEncodePassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(password);
        this.password = encodePasswd;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", tend='" + tend + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", authorityid=" + authorityid +
                ", lovers=" + lovers +
                '}';
    }
}
