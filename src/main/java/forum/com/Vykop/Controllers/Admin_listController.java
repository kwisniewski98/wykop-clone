package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.Admin_list;
import forum.com.Vykop.Repositories.Admin_listRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class Admin_listController {

    private final Admin_listRepository repository;

    Admin_listController(@Qualifier("admin_listRepository")Admin_listRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/admin_lists")
    List<Admin_list> all() {
        return repository.findAll();
    }

    @PostMapping("/admin_lists")
    Admin_list newAdmin_list(@RequestBody Admin_list newAdmin_list) {
        return repository.save(newAdmin_list);
    }

    @PutMapping("/admin_lists/{id}")
    Admin_list replaceAdmin_list(@RequestBody Admin_list newAdmin_list, @PathVariable int id) {
        return repository.findById(id)
                .map(Admin_list -> {
                    Admin_list.setSub_vykop(newAdmin_list.getSub_vykop());
                    Admin_list.setUser_id(newAdmin_list.getUser_id());
                    return repository.save(Admin_list);
                })
                .orElseGet(() -> {
                    newAdmin_list.setId(id);
                    return repository.save(newAdmin_list);
                });
    }

    @DeleteMapping("/admin_lists/{id}")
    void deleteAdmin_list(@PathVariable int id) {
        repository.deleteById(id);
    }
}
