package forum.com.Vykop.Repositories;
import forum.com.Vykop.Models.Comment;
import forum.com.Vykop.Models.Post;
import forum.com.Vykop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("postRepository")
public interface PostRepository extends JpaRepository<Post, Integer>{

}