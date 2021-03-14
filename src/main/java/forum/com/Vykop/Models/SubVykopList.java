package forum.com.Vykop.Models;


import javax.persistence.*;

@Embeddable
public class SubVykopList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "sub_vykop")
    private Integer subVykop;
    @Column(name = "user_id")
    private Integer userId;

    public SubVykopList() {
    }

    public SubVykopList(Integer id, Integer subVykop, Integer userId) {
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

    public void setSubVykop(Integer sub_vykop) {
        this.subVykop = sub_vykop;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }
}
