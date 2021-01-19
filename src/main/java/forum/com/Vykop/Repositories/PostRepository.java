package forum.com.Vykop.Repositories;

import forum.com.Vykop.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("postRepository")
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select post from Post post inner join " +
            "Sub_vykop sub_vykop on sub_vykop.id = post.sub_vykopid " +
            "inner join Sub_vykop_list sub_vykop_list on sub_vykop.id = sub_vykop_list.sub_vykop " +
            "where sub_vykop_list.user_id = ?1")
    List<Post> findByUser(int id);
}