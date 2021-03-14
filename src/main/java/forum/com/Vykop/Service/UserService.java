package forum.com.Vykop.Service;

import forum.com.Vykop.Models.User;
import forum.com.Vykop.Models.UserCreateForm;
import forum.com.Vykop.Models.UserRegisterForm;
import forum.com.Vykop.Repositories.UserRepository;
import forum.com.Vykop.Storage.StorageService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.security.Principal;
import java.sql.Date;
import java.util.HashMap;

@Component
public class UserService {
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    private static final String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*" +
            "\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]" +
            "|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08" +
            "\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private static final String usernamePattern = "[a-zA-z0-9]{5,}";
    private static final String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    public String  registerUser(UserRegisterForm userForm) {
        if (userRepository.findByUsername(userForm.getUsername()) != null ) return "username already exists";
        if (userRepository.findByEmail(userForm.getEmail()) != null) return "email already exists";

        User user = new User(0,
                userForm.getUsername(),
                userForm.getPassword(),
                userForm.getEmail(),
                new Date(System.currentTimeMillis()),
                "user");
        userRepository.save(user);
        return "ok";
    }

    public User createUser(UserCreateForm new_user) {
        if (userRepository.findByUsername(new_user.getUsername()) != null ) throw new EntityExistsException();
        if (userRepository.findByEmail(new_user.getEmail()) != null) throw new EntityExistsException();
        User user = new User(0,
                new_user.getUsername(),
                new_user.getPassword(),
                new_user.getEmail(),
                new Date(System.currentTimeMillis()),
                new_user.getRole());
       return userRepository.saveAndFlush(user);
    }

    public String uploadAvatar(MultipartFile file, Principal principal){
        String fileURL = "http://localhost:8080/files/" + file.getOriginalFilename();
        User user = userRepository.findByUsername(principal.getName());
        storageService.store(file);
        user.setAvatar(fileURL);
        userRepository.save(user);
        return fileURL;
    }
    public HashMap<String, Integer> userStats(String username) throws NotFoundException {
        HashMap<String , Integer> stats = new HashMap<>();
        User user = userRepository.findByUsername(username);
        if (user == null) throw new NotFoundException("user not found");
        stats.put("postUpvotes", user.getPosts().stream().reduce(
                0, (sum, post) -> sum + post.getVotes(), Integer::sum)
        );
        stats.put("commentUpvotes", user.getComments().stream().reduce(
                0, (sum, post) -> sum + post.getVotes(), Integer::sum)
        );
        stats.put("totalUpvotes", stats.get("postUpvotes") +stats.get("commentUpvotes"));
        stats.put("comments", user.getComments().size());
        stats.put("posts", user.getPosts().size());
        return stats;
    }
}
