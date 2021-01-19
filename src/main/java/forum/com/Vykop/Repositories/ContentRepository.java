package forum.com.Vykop.Repositories;

import forum.com.Vykop.Models.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository("contentRepository")
public interface ContentRepository extends JpaRepository<Content, Integer> {

    @Query("select content from Content content inner join " +
            "Post post on post.content = content.id where content.id = ?1")
    Optional<Content> findByPost(int id);

}