package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.Post;
import forum.com.Vykop.Repositories.PostRepository;
import forum.com.Vykop.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
class PostController {

    private final PostRepository repository;
    private final PostService postService;

    PostController(@Qualifier("postRepository") PostRepository repository,
                   PostService postService ) {
        this.repository = repository;
        this.postService = postService;
    }

    @GetMapping("/posts")
    List<Post> all() {
        return repository.findAll();
    }

    @PostMapping("/posts")
    Post newPost(@RequestBody Post newPost) {
        return repository.save(newPost);
    }

    @GetMapping("/userposts")
    Set<Post> userPosts(Principal principal) {
        return postService.getFeedPosts(principal);
    }

    @GetMapping("/posts/{subvykop}")
    ResponseEntity subredditPosts(@PathVariable String  subvykop) {
        Set<Post> posts = repository.findBySubVykop_Name(subvykop);
        if (posts.size() == 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().body(posts);
    }

    @PutMapping("/posts/{id}")
    Post replacePost(@RequestBody Post newPost, @PathVariable int id) {
        return repository.findById(id)
                .map(Post -> {
                    Post.setAuthor(newPost.getAuthor());
                    Post.setContent(newPost.getContent());
                    Post.setCreationDate(newPost.getCreationDate());
                    Post.setSubVykop(newPost.getSubVykop());
                    Post.setVotes(newPost.getVotes());
                    return repository.save(Post);
                })
                .orElseGet(() -> {
                    newPost.setId(id);
                    return repository.save(newPost);
                });
    }

    @PostMapping("/posts/upvote/{id}")
    ResponseEntity upvote(@PathVariable int id, Principal principal) {
        String result = postService.upvote(id, principal);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable int id) {
        repository.deleteById(id);
    }
}
