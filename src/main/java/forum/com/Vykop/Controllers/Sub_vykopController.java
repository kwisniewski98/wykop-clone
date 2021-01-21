package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.SubVykop;
import forum.com.Vykop.Repositories.Sub_vykopRepository;
import forum.com.Vykop.Service.SubVykopService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
class Sub_vykopController {

    private final Sub_vykopRepository repository;

    @Autowired
    private SubVykopService subVykopService;

    Sub_vykopController(@Qualifier("sub_vykopRepository") Sub_vykopRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/sub_vykop/{id}")
    ResponseEntity getSub_vykop(@PathVariable int id){
        Optional<SubVykop> subVykop = repository.findById(id);
        if (subVykop.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else return ResponseEntity.ok().body(subVykop.get());
    }

    @GetMapping("/subvykop/{id}/isSubscribed")
    ResponseEntity isSubscribed(@PathVariable int id, Principal principal){
        try {
            return ResponseEntity.ok().body(subVykopService.isSubscribed(principal.getName(), id));
        }catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/subvykop/{id}/subscribe")
    ResponseEntity subscribe(@PathVariable int id, Principal principal){
        try {
            String result = subVykopService.subscribe(principal.getName(), id);
            return ResponseEntity.ok().body(result);
        }catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/subykop/{id}/banner")
    ResponseEntity changeBanner(@PathVariable int id, @RequestParam("file") MultipartFile file){
        String response = subVykopService.uploadBanner(file, id);
        if (response.equals("subVykop not found")) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().body(response);
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
                    Sub_vykop.setBanner(newSub_vykop.getBanner());
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
    @GetMapping("/sub_vykop/search")
    ResponseEntity findSubVykops(@RequestBody String match) {
        return ResponseEntity.ok().body(subVykopService.subVykopsMatching(match));
    }
}
