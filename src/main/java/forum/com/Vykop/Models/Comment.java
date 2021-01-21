package forum.com.Vykop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY,  cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "post")
    @JsonIgnore
    private Post post;
    private String text;
    private Integer votes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "author")
    private User author;
    @ManyToMany(mappedBy = "upvotedComments")
    @JsonIgnore
    private Set<User> upvotedUsers;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private boolean upvoted;

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<User> getUpvotedUsers() {
        return upvotedUsers;
    }

    public void setUpvotedUsers(Set<User> upvoted_users) {
        this.upvotedUsers = upvoted_users;
    }

    public Comment() {
    }

    public Comment(Integer id, Post post, String text, Integer votes) {
        this.id = id;
        this.post = post;
        this.text = text;
        this.votes = votes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
