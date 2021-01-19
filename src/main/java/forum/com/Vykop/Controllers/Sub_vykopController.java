package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.SubVykop;
import forum.com.Vykop.Repositories.Sub_vykopRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class Sub_vykopController {

    private final Sub_vykopRepository repository;

    Sub_vykopController(@Qualifier("sub_vykopRepository") Sub_vykopRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/sub_vykop")
    List<SubVykop> all() {
        return repository.findAll();
    }

    @PostMapping("/sub_vykop")
    SubVykop newSub_vykop(@RequestBody SubVykop newSub_vykop) {
        return repository.save(newSub_vykop);
    }

    @PutMapping("/sub_vykop/{id}")
    SubVykop replaceSub_vykop(@RequestBody SubVykop newSub_vykop, @PathVariable int id) {
        return repository.findById(id)
                .map(Sub_vykop -> {
                    Sub_vykop.setDescription(newSub_vykop.getDescription());
                    Sub_vykop.setName(newSub_vykop.getName());
                    return repository.save(Sub_vykop);
                })
                .orElseGet(() -> {
                    newSub_vykop.setId(id);
                    return repository.save(newSub_vykop);
                });
    }

    @DeleteMapping("/sub_vykop/{id}")
    void deleteSub_vykop(@PathVariable int id) {
        repository.deleteById(id);
    }
}
