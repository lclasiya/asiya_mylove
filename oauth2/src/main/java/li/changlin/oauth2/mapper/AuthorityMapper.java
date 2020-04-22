package li.changlin.oauth2.mapper;

import li.changlin.user.entity.Authority;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityMapper {
    Authority getauthoritybyid(Integer id);
}
