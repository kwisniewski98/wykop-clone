package forum.com.Vykop.Repositories;

import forum.com.Vykop.Models.Post;
import forum.com.Vykop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("postRepository")
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findBySubVykop_Name(String name);
}