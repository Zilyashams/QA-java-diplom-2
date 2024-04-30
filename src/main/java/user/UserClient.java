package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String USER_ENDPOINT = "/api/auth/user";


    @Step("Создание пользователя")
    public static Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER_ENDPOINT);
    }

    @Step("Авторизация пользователя")
    public static Response login(UserCreds userCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCreds)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public static Response delete(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .delete(USER_ENDPOINT);
    }
    @Step("Авторизация пользователя с токеном")
    public static Response authUserWithToken(String accessToken, User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(USER_ENDPOINT);
    }
    @Step("Авторизация пользователя без токена")
    public static Response authUserWithoutToken(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(USER_ENDPOINT);
    }
}
