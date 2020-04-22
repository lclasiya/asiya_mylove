package li.changlin.oauth2.config.service;

import li.changlin.user.entity.Authority;
import li.changlin.user.entity.User;
import li.changlin.oauth2.service.AuthorityService;
import li.changlin.oauth2.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersService us;
    @Autowired
    private AuthorityService as;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = us.getUserByName(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            // 获取用户授权
            Authority authority = as.getauthoritybyid(user.getAuthorityid());
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
