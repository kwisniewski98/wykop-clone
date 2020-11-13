package forum.com.Vykop.Repositories;
import forum.com.Vykop.Models.Admin_list;
import forum.com.Vykop.Models.Comment;
import forum.com.Vykop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("admin_listRepository")
public interface Admin_listRepository extends JpaRepository<Admin_list, Integer>{

}