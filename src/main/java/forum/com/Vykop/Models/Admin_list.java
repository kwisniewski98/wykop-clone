package forum.com.Vykop.Models;
import java.util.List;
import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
@Entity
@Table(name = "admin_list")
public class Admin_list {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer sub_vykop;
    private Integer user_id;

    public Admin_list() {
    }

    public Admin_list(Integer id, Integer sub_vykop, Integer user_id) {
        this.id = id;
        this.sub_vykop = sub_vykop;
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSub_vykop() {
        return sub_vykop;
    }

    public void setSub_vykop(Integer sub_vykop) {
        this.sub_vykop = sub_vykop;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
