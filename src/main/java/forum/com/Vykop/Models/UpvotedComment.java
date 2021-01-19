package forum.com.Vykop.Models;

import javax.persistence.*;

@Entity
@Embeddable
public class UpvotedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "comment_id")
    private Integer commentId;

    public UpvotedComment(Integer id, Integer userId, Integer commentId) {
        this.id = id;
        this.userId = userId;
        this.commentId = commentId;
    }


    public UpvotedComment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer comment_id) {
        this.commentId = comment_id;
    }
}
