import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static constants.Data.BASE_URL;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void checkUserCreate() {
        User user = new User("yoyo111@yandex.ru", "1234", "yoyo111");
        Response response = UserClient.create(user);
        accessToken = response.path("accessToken");
        Assert.assertEquals("Неверный статус код", SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void checkDoubleUserCreate() {
        User user = new User("yoyo111@yandex.ru", "1234", "yoyo111");
        Response response = UserClient.create(user);
        accessToken = response.path("accessToken");
        Assert.assertEquals("Неверный статус код", SC_OK, response.statusCode());
        Response responseDoubleUser = UserClient.create(user);
        responseDoubleUser.then().assertThat().statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void checkUserWithoutPasswordCreate() {
        User user = new User("yoyo111@yandex.ru", "", "yoyo111");
        Response response = UserClient.create(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
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
