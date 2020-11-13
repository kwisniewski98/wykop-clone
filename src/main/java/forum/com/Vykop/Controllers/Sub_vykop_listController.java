package forum.com.Vykop.Controllers;
import java.util.List;

import forum.com.Vykop.Models.Sub_vykop_list;
import forum.com.Vykop.Models.User;
import forum.com.Vykop.Repositories.Sub_vykop_listRepository;
import forum.com.Vykop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
class Sub_vykop_listController {

    private final Sub_vykop_listRepository repository;

    Sub_vykop_listController(@Qualifier("sub_vykop_listRepository")Sub_vykop_listRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/sub_vykop_lists")
    List<Sub_vykop_list> all() {
        return repository.findAll();
    }

    @PostMapping("/sub_vykop_lists")
    Sub_vykop_list newSub_vykop_list(@RequestBody Sub_vykop_list newSub_vykop_list) {
        return repository.save(newSub_vykop_list);
    }

    @PutMapping("/sub_vykop_lists/{id}")
    Sub_vykop_list replaceSub_vykop_list(@RequestBody Sub_vykop_list newSub_vykop_list, @PathVariable int id) {
        return repository.findById(id)
                .map(Sub_vykop_list -> {
                    Sub_vykop_list.setSub_vykop(newSub_vykop_list.getSub_vykop());
                    Sub_vykop_list.setUser_id(newSub_vykop_list.getUser_id());
                    return repository.save(Sub_vykop_list);
                })
                .orElseGet(() -> {
                    newSub_vykop_list.setId(id);
                    return repository.save(newSub_vykop_list);
                });
    }

    @DeleteMapping("/sub_vykop_lists/{id}")
    void deleteSub_vykop_list(@PathVariable int id) {
        repository.deleteById(id);
    }
}
