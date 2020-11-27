package forum.com.Vykop.Models;

import javax.persistence.*;
import java.sql.Date;
@Entity
@Table(name = "upvoted_list")
public class upvoted_list {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer user_id;
    private Integer post_id;

    public upvoted_list(Integer id, Integer user_id, Integer post_id) {
        this.id = id;
        this.user_id = user_id;
        this.post_id = post_id;
    }


    public upvoted_list() {
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

    public Integer getpost_id() {
        return post_id;
    }

    public void setpost_id(Integer post_id) {
        this.post_id = post_id;
    }
}
