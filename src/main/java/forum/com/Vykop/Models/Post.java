package forum.com.Vykop.Models;

import javax.persistence.*;
import java.sql.Date;
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer author;
    private Integer votes;
    private Integer content;
    private Date creation_date;
    private Integer sub_vykopid;

    public Post() {
    }

    public Post(Integer id, Integer author, Integer votes, Integer content, Date creation_date, Integer sub_vykopid) {
        this.id = id;
        this.author = author;
        this.votes = votes;
        this.content = content;
        this.creation_date = creation_date;
        this.sub_vykopid = sub_vykopid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getContent() {
        return content;
    }

    public void setContent(Integer content) {
        this.content = content;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getSub_vykopid() {
        return sub_vykopid;
    }

    public void setSub_vykopid(Integer sub_vykopid) {
        this.sub_vykopid = sub_vykopid;
    }
}
