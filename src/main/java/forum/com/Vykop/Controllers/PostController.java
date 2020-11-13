package forum.com.Vykop.Controllers;
import java.util.List;

import forum.com.Vykop.Models.Post;
import forum.com.Vykop.Models.User;
import forum.com.Vykop.Repositories.PostRepository;
import forum.com.Vykop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
class PostController {

    private final PostRepository repository;

    PostController(@Qualifier("postRepository")PostRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/posts")
    List<Post> all() {
        return repository.findAll();
    }

    @PostMapping("/posts")
    Post newPost(@RequestBody Post newPost) {
        return repository.save(newPost);
    }

    @PutMapping("/posts/{id}")
    Post replacePost(@RequestBody Post newPost, @PathVariable int id) {
        return repository.findById(id)
                .map(Post -> {
                    Post.setAuthor(newPost.getAuthor());
                    Post.setContent(newPost.getContent());
                    Post.setCreation_date(newPost.getCreation_date());
                    Post.setSub_vykopid(newPost.getSub_vykopid());
                    Post.setVotes(newPost.getVotes());
                    return repository.save(Post);
                })
                .orElseGet(() -> {
                    newPost.setId(id);
                    return repository.save(newPost);
                });
    }

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable int id) {
        repository.deleteById(id);
    }
}
