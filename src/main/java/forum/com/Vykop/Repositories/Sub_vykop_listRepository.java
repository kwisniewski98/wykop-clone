package forum.com.Vykop.Repositories;
import forum.com.Vykop.Models.Comment;
import forum.com.Vykop.Models.Sub_vykop_list;
import forum.com.Vykop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sub_vykop_listRepository")
public interface Sub_vykop_listRepository extends JpaRepository<Sub_vykop_list, Integer>{

}