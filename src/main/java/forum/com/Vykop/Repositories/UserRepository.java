package forum.com.Vykop.Repositories;
import forum.com.Vykop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer>{
    boolean existsByUsername(String username);
    User findByUsername(String username);
    @Transactional
    void deleteByUsername(String username);
}