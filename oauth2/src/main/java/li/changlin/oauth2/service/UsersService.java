package li.changlin.oauth2.service;

import com.github.pagehelper.Page;
import li.changlin.user.entity.User;

import java.util.List;

public interface UsersService {
    int addUser(User user);
    int updateById(User user);

    int removeUser(Integer id);

    User getUserById(Integer id);

    User getUserByName(String name);

    List<User> listAll();
    Page<User> listAllWithPage();

    void addLoversByPessimisticLock(int id);

}
