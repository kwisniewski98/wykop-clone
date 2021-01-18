package forum.com.Vykop.Models;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

@Entity
@Table(name = "users")
public class User {
    private static ArrayList<String> roles = new ArrayList<>(Arrays.asList(new String[]{"user", "admin"}));
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Date registration_date;
    private String role;

    public User() {
    }

    public User(int id, String username, String password, Date registration_date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registration_date = registration_date;
    }

    public User(int id, String username, String password, Date registration_date, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registration_date = registration_date;
        this.role = role;
    }

    ;

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

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }
}
