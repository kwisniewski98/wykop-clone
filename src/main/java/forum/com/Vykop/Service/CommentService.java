package forum.com.Vykop.Service;

import forum.com.Vykop.Models.Comment;
import forum.com.Vykop.Models.Post;
import forum.com.Vykop.Models.User;
import forum.com.Vykop.Repositories.CommentRepository;
import forum.com.Vykop.Repositories.PostRepository;
import forum.com.Vykop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class CommentService {
    @Qualifier("commentRepository")
    @Autowired
    private CommentRepository commentRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("postRepository")
    @Autowired
    private PostRepository postRepository;

    public String upvote(int commentId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()){
            return "comment not found";
        }
        Comment c = comment.get();
        if (user.getUpvotedComments().contains(c)) {
            c.setVotes(c.getVotes()-1);
            c.getUpvotedUsers().remove(user);
            user.getUpvotedComments().remove(c);
            userRepository.save(user);
            commentRepository.save(c);
            return "downvote";
        }

        c.setVotes(c.getVotes()+1);
        c.getUpvotedUsers().add(user);
        user.getUpvotedComments().add(c);
        userRepository.save(user);
        commentRepository.save(c);
        return "upvote";
    }
    public Comment createComment(String text, int postId, String username) {
        User user = userRepository.findByUsername(username);
        Post post = postRepository.findById(postId).get();
        Comment comment = new Comment(0, post, text, 0 );
        comment.setAuthor(user);
        comment = commentRepository.saveAndFlush(comment);
        post.getComments().add(comment);
        postRepository.saveAndFlush(post);
        return comment;
    }
}
