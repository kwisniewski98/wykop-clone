package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.*;
import forum.com.Vykop.Repositories.CommentRepository;
import forum.com.Vykop.Repositories.PostRepository;
import forum.com.Vykop.Repositories.UserRepository;
import forum.com.Vykop.Service.UserService;
import forum.com.Vykop.Storage.StorageService;
import forum.com.Vykop.security.JwtTokenProvider;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.security.Principal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
class UserController {

    private final UserRepository repository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Qualifier("postRepository")
    @Autowired
    private PostRepository postRepository;

    @Qualifier("commentRepository")
    @Autowired
    private CommentRepository commentRepository;


    UserController(@Qualifier("userRepository") UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users/me")
    ResponseEntity me(Principal principal) {
        User user = repository.findByUsername(principal.getName());
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().body(user);
    }
    @GetMapping("/users/stats")
    ResponseEntity userStats(Principal principal) {
        try {
            return ResponseEntity.ok().body(userService.userStats(principal.getName()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/users/posts")
    ResponseEntity userPosts(Principal principal) {
        return ResponseEntity.ok().body(postRepository.findByAuthor_Username(principal.getName()));
    }

    @GetMapping("/users/comments")
    ResponseEntity userComments(Principal principal) {
        return ResponseEntity.ok().body(commentRepository.findByAuthor_Username(principal.getName()));
    }

    @GetMapping("/users/{id}")
    ResponseEntity byId(@PathVariable int id) {
        HashMap<String, String> userMap = new HashMap<String , String >();
        Optional<User> userO = repository.findById(id);
        if (!userO.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = userO.get();
        userMap.put("id", String.valueOf(user.getId()));
        userMap.put("username", user.getUsername());
        userMap.put("password", user.getPassword());
        userMap.put("email", user.getEmail());
        userMap.put("registrationDate", user.getRegistrationDate().toString());
        userMap.put("avatar", user.getAvatar());

        return ResponseEntity.ok().body(userMap);
    }

    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/users/login", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity login(@RequestBody UserForm userForm) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        User user = repository.findByUsername(userForm.getUsername());
        if (user == null || !user.getPassword().equals(userForm.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = tokenProvider.createToken(user, repository.findAll());
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("user",user);
        resultMap.put("token", token);
        return ResponseEntity.ok().headers(headers).body(resultMap);
    }

    @RequestMapping(value = "/users/signup", method = {RequestMethod.POST})
    public ResponseEntity register(@RequestBody @Valid UserRegisterForm userForm) {
        String resp = userService.registerUser(userForm);
        if (resp.equals("ok")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
    }

    @PostMapping("/users")
    ResponseEntity newUser(@RequestBody UserCreateForm newUser) {
        try {
            return ResponseEntity.ok().body(userService.createUser(newUser));
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/users/avatar")
    ResponseEntity setAvatar(@RequestParam("file") MultipartFile file, Principal principal) {
        userService.uploadAvatar(file, principal);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}")
    ResponseEntity replaceUser(@RequestBody UserEdit userEdit, @PathVariable int id) {
        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(repository.findById(id)
                .map(u -> {
                    u.setPassword(userEdit.getPassword());
                    if(userEdit.getRole() != null)
                        u.setRole(userEdit.getRole());
                    return repository.save(u);
                }));
    }

    @GetMapping("u/{name}")
    User getUserByUsername(@PathVariable String name){
        return repository.findByUsername(name);
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable int id) {
        repository.deleteById(id);
    }

}
