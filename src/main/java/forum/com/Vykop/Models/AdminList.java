package forum.com.Vykop.Models;

import javax.persistence.*;

@Entity
@Embeddable
public class AdminList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "sub_vykop")
    private Integer subVykop;
    @Column(name = "user_id")
    private Integer userId;

    public AdminList() {
    }

    public AdminList(Integer id, Integer subVykop, Integer userId) {
        this.id = id;
        this.subVykop = subVykop;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubVykop() {
        return subVykop;
    }

    public void setSubVykop(Integer subVykop) {
        this.subVykop = subVykop;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
