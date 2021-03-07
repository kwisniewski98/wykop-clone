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

import javax.persistence.EntityExistsException;
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
    @PostMapping("/subvykop/subscribe")
    ResponseEntity subscribe(@RequestBody String id, Principal principal){
        try {
            String result = subVykopService.subscribe(principal.getName(), Integer.parseInt(id));
            return ResponseEntity.ok().body(result);
        }catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/sub_vykop")
    ResponseEntity newSub_vykop(@RequestParam("banner") MultipartFile banner, @RequestParam("avatar") MultipartFile avatar,
                                @RequestParam("name") String name, @RequestParam("description") String description,
                                Principal principal ) {
        try {
            subVykopService.createSubVykop(banner, avatar,  name, description, principal.getName());
            return ResponseEntity.ok().build();

        }
        catch (EntityExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/sub_vykop/search")
    ResponseEntity findSubVykops(@RequestParam String match) {
        return ResponseEntity.ok().body(subVykopService.subVykopsMatching(match));
    }
}
