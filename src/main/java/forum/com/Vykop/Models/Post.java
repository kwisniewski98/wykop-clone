package forum.com.Vykop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "author")
    private User author;
    private Integer votes;
    private String title;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "content")
    private Content content;
    private Date creationDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "sub_vykopid")
    private SubVykop subVykop;

    @ManyToMany(mappedBy = "upvotedPosts")
    @JsonIgnore
    private Set<User> upvotedUsers;

    public Set<User> getUpvotedUsers() {
        return upvotedUsers;
    }

    @OneToMany(mappedBy = "post")
    Set<Comment> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setUpvotedUsers(Set<User> upvoted_users) {
        this.upvotedUsers = upvoted_users;
    }

    public Post() {
    }

    public Post(Integer id, User author, Integer votes, Content content, Date creationDate, SubVykop subVykop) {
        this.id = id;
        this.author = author;
        this.votes = votes;
        this.content = content;
        this.creationDate = creationDate;
        this.subVykop = subVykop;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creation_date) {
        this.creationDate = creation_date;
    }

    public SubVykop getSubVykop() {
        return subVykop;
    }

    public void setSubVykop(SubVykop sub_vykop) {
        this.subVykop = sub_vykop;
    }

}
