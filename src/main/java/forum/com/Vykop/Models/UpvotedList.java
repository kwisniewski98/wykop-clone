package forum.com.Vykop.Models;

import javax.persistence.*;

@Entity
@Embeddable
public class UpvotedList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "post_id")
    private Integer postId;

    public UpvotedList(Integer id, Integer userId, Integer postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }


    public UpvotedList() {
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

    public Integer getpost_id() {
        return postId;
    }

    public void setpost_id(Integer post_id) {
        this.postId = post_id;
    }
}
