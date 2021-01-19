package forum.com.Vykop.Repositories;

import forum.com.Vykop.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("commentRepository")
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPost(int id);
}