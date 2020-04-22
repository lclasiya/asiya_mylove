package li.changlin.oauth2.service.usImpl;

import li.changlin.user.entity.Authority;
import li.changlin.oauth2.mapper.AuthorityMapper;
import li.changlin.oauth2.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService
{
    @Autowired
    AuthorityMapper am;
    @Override
    public Authority getauthoritybyid(Integer id) {
        return am.getauthoritybyid(id);
    }
}
