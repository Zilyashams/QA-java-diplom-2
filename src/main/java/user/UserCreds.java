package user;

public class UserCreds {
    private final String email;
    private final String password;

    public UserCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCreds from(User user) {
        return new UserCreds(user.getEmail(), user.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CourierCreds {" +
                "login='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
