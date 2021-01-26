package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.Post;
import forum.com.Vykop.Repositories.PostRepository;
import forum.com.Vykop.Service.CommentService;
import forum.com.Vykop.Service.PostService;
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
import java.util.Set;

@RestController
class PostController {

    @Autowired
    private CommentService commentService;

    @Qualifier("postRepository")
    @Autowired
    private PostRepository postRepository;

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
    ResponseEntity newPost(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("title") String title,
                 @RequestParam("text") String text, @RequestParam("subVykop") String subVykop, Principal principal) {
        return ResponseEntity.ok().body(postService.createPost(file, title, text, principal.getName(), subVykop));

    }

    @GetMapping("/userposts")
    ResponseEntity userPosts(Principal principal,
                        @RequestParam(name = "page") int page) {
        try {
            return ResponseEntity.ok().body(postService.getFeedPosts(principal, page));
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/posts/{subvykop}")
    ResponseEntity subredditPosts(@PathVariable String  subvykop, @RequestParam("page") int page, Principal principal) {
        try {
            return ResponseEntity.ok().body(postService.getPostsBySubvykop(subvykop, page, principal.getName()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/posts/{postId}/comment")
    ResponseEntity comment(@PathVariable int postId, @RequestBody String text, Principal principal) {
        return ResponseEntity.ok().body(commentService.createComment(text, postId, principal.getName()));
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
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/post")
    ResponseEntity getPost(@RequestParam int id, Principal principal){
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else return ResponseEntity.ok().body(postService.postWithCommentUpvoted(post.get(), principal.getName()));
    }

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable int id) {
        repository.deleteById(id);
    }
    @GetMapping("/posts/{subVykop}/search")
    ResponseEntity findSubVykops(@RequestBody String match, @PathVariable String subVykop) {
        return ResponseEntity.ok().body(postService.postsMatching(match, subVykop));
    }
}
