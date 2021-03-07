package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.Comment;
import forum.com.Vykop.Repositories.CommentRepository;
import forum.com.Vykop.Service.CommentService;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.List;

@RestController
class CommentController {

    @Autowired
    private CommentService commentService;

    private final CommentRepository repository;

    CommentController(@Qualifier("commentRepository") CommentRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/comments/{id}")
    void deleteComment(@PathVariable int id) {
        repository.deleteById(id);
    }

    @PostMapping("/comments/upvote/{id}")
    ResponseEntity upvote(@PathVariable int id, Principal principal) {
        commentService.upvote(id, principal);
        return ResponseEntity.ok().body(id);
    }
}
