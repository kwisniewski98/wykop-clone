package forum.com.Vykop.Repositories;

import forum.com.Vykop.Models.Sub_vykop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sub_vykopRepository")
public interface Sub_vykopRepository extends JpaRepository<Sub_vykop, Integer> {

}