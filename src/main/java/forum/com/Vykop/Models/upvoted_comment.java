package forum.com.Vykop.Models;

import javax.persistence.*;
import java.sql.Date;
@Entity
@Table(name = "upvoted_comment")
public class upvoted_comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer user_id;
    private Integer comment_id;

    public upvoted_comment(Integer id, Integer user_id, Integer comment_id) {
        this.id = id;
        this.user_id = user_id;
        this.comment_id = comment_id;
    }


    public upvoted_comment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }
}
