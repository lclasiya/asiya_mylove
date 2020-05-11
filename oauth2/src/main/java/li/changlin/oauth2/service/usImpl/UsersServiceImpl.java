package li.changlin.oauth2.service.usImpl;

import com.github.pagehelper.Page;
import li.changlin.user.entity.User;
import li.changlin.oauth2.mapper.UsersMapper;
import li.changlin.oauth2.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    UsersMapper um;

    @Override
    public int addUser(User user) {
        return um.addUser(user);
    }

    @Override
    public int updateById(User user) {
        return um.updateById(user);
    }

    @Override
    public int removeUser(Integer id) {
        return um.removeUser(id);
    }

    @Override
    public User getUserById(Integer id) {
        return um.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return um.getUserByName(name);
    }

    @Override
    public List<User> listAll() {
        return um.listAll();
    }

    @Override
    public Page<User> listAllWithPage() {
        return um.listAllWithPage();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addLoversByPessimisticLock(int id) {
     //   User user = um.selectForLock(id);
        um.addLovers(id);
    }


}
