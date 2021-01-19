package forum.com.Vykop.Repositories;

import forum.com.Vykop.Models.Sub_vykop_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sub_vykop_listRepository")
public interface Sub_vykop_listRepository extends JpaRepository<Sub_vykop_list, Integer> {

    @Query("select sub_vykop_listRepository from Sub_vykop_list sub_vykop_listRepository " +
            "where sub_vykop_listRepository.sub_vykop in :ids")
    List<Sub_vykop_list> findAllByUser_id(List<Integer> ids);
}