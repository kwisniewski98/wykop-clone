package forum.com.Vykop.Repositories;

import forum.com.Vykop.Models.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("contentRepository")
public interface ContentRepository extends JpaRepository<Content, Integer> {

}