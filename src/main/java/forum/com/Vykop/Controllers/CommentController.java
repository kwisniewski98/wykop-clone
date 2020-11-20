package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.Comment;
import forum.com.Vykop.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class CommentController {

    private final CommentRepository repository;

    CommentController(@Qualifier("commentRepository")CommentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/comments")
    List<Comment> all() {
        return repository.findAll();
    }

    @PostMapping("/comments")
    Comment newComment(@RequestBody Comment newComment) {
        return repository.save(newComment);
    }

    @PutMapping("/comments/{id}")
    Comment replaceComment(@RequestBody Comment newComment, @PathVariable int id) {
        return repository.findById(id)
                .map(comment -> {
                    comment.setPost(newComment.getPost());
                    comment.setText(newComment.getText());
                    comment.setVotes(newComment.getVotes());
                    return repository.save(comment);
                })
                .orElseGet(() -> {
                    newComment.setId(id);
                    return repository.save(newComment);
                });
    }

    @DeleteMapping("/comments/{id}")
    void deleteComment(@PathVariable int id) {
        repository.deleteById(id);
    }
}
