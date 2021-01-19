package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.User;
import forum.com.Vykop.Models.UserForm;
import forum.com.Vykop.Models.UserRegisterForm;
import forum.com.Vykop.Repositories.UserRepository;
import forum.com.Vykop.Service.PostService;
import forum.com.Vykop.Service.UserService;
import forum.com.Vykop.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
class UserController {

    private final UserRepository repository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    UserController(@Qualifier("userRepository") UserRepository repository) {
        this.repository = repository;
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
        userService.registerUser(userForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable int id) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    user.setRegistration_date(newUser.getRegistration_date());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable int id) {
        repository.deleteById(id);
    }

}
