import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCreds;

import static constants.Data.BASE_URL;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        User user = new User("Yohoho@yandex.ru", "123456", "Yohoho");
        UserClient.create(user);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void checkUserLogin() {
        User user = new User("Yohoho@yandex.ru", "123456", "Yohoho");
        Response loginResponse = UserClient.login(UserCreds.from(user));
        accessToken = loginResponse.path("accessToken");

        Assert.assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void checkUserBadLoginPassword() {
        User user = new User("Yoho@yandex.ru", "12345", "Yohoho");
        Response response = UserClient.login(UserCreds.from(user));
        accessToken = response.path("accessToken");
       response.then().assertThat().statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            UserClient.delete(accessToken).then().assertThat().body("success", equalTo(true))
                    .and()
                    .statusCode(SC_ACCEPTED)
                    .and()
                    .body("message", equalTo("User successfully removed"));
        }
    }
}
