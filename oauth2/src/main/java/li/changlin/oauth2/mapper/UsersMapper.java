package li.changlin.oauth2.mapper;

import com.github.pagehelper.Page;
import li.changlin.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersMapper {
    int addUser(User user);
    int updateById(User user);

    int removeUser(Integer id);

    User getUserById(Integer id);

    User getUserByName(String name);

    List<User> listAll();
    Page<User> listAllWithPage();

}
