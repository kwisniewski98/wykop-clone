package forum.com.Vykop.Repositories;
import forum.com.Vykop.Models.Comment;
import forum.com.Vykop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("commentRepository")
public interface CommentRepository extends JpaRepository<Comment, Integer>{

}