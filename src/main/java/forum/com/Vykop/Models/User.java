package forum.com.Vykop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    private static ArrayList<String> roles = new ArrayList<>(Arrays.asList(new String[]{"user", "admin"}));
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Date registrationDate;
    private String role;
    private String avatar;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY,  cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private Set<Post> posts;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY,  cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private Set<Comment> comments;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "adminList",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_vykop")
    )
    @JsonIgnore
    Set<Post> adminList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "subVykopList",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_vykop")
    )
    @JsonIgnore
    Set<SubVykop> subVykopList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "upvotedComment",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    @JsonIgnore
    Set<Comment> upvotedComments;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "upvotedPost",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    @JsonIgnore
    Set<Post> upvotedPosts;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Comment> getUpvotedComments() {
        return upvotedComments;
    }

    public void setUpvotedComments(Set<Comment> upvotedComments) {
        this.upvotedComments = upvotedComments;
    }

    public Set<Post> getUpvotedPosts() {
        return upvotedPosts;
    }

    public void setUpvotedPosts(Set<Post> upvotedPosts) {
        this.upvotedPosts = upvotedPosts;
    }

    public Set<SubVykop> getSubVykopList() {
        return subVykopList;
    }

    public void setSubVykopList(Set<SubVykop> sub_vykop_list) {
        this.subVykopList = sub_vykop_list;
    }

    public Set<Post> getAdminList() {
        return adminList;
    }

    public void setAdminList(Set<Post> admin_list) {
        this.adminList = admin_list;
    }

    public User() {
    }

    public User(int id, String username, String password, Date registrationDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    public User(int id, String username, String password, Date registrationDate, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    public User(Integer id, String username, String password, String email, Date registrationDate, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    public static ArrayList<String> getRoles() {
        return roles;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registration_date) {
        this.registrationDate = registration_date;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
