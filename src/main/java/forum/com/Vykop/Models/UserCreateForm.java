package forum.com.Vykop.Models;

public class UserCreateForm extends UserRegisterForm {
    private String role;

    public UserCreateForm() {
    }

    public UserCreateForm(String username, String password, String email, String role) {
        super(username, password, email);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
