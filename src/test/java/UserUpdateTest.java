import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static constants.Data.BASE_URL;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static user.UserCreds.from;

public class UserUpdateTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        User user = new User("Yohoho@yandex.ru", "123456", "Yohoho");
        UserClient.create(user);
        Response loginResponse = UserClient.login(from(user));
        accessToken = loginResponse.path("accessToken");
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void checkUserUpdateDataWithAuth() {
        User userUpdate = new User("ducati@yandex.ru", "999999", "dukati");
        UserClient.authUserWithToken(accessToken, userUpdate)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void checkUserUpdateDataWithoutAuth() {
        User userUpdate = new User("ducati@yandex.ru", "999999", "dukati");
        UserClient.authUserWithoutToken(userUpdate)
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
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
