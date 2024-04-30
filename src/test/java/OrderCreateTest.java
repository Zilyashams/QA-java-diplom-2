import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;

import java.util.List;

import static constants.Data.BASE_URL;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static user.UserCreds.from;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final List<String> ingredients;
    private final int statusCode;
    private String accessToken;

    public OrderCreateTest(List<String> ingredients, int statusCode) {
        this.ingredients = ingredients;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa73"), SC_OK},
                {List.of("6565656", "2121212"), SC_INTERNAL_SERVER_ERROR},
                {List.of(), SC_BAD_REQUEST}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        User user = new User("Yohoho@yandex.ru", "123456", "Yohoho");
        UserClient.create(user);
        Response loginResponse = UserClient.login(from(user));
        accessToken = loginResponse.path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void checkOrderCreateWithAuth() {
        Order order= new Order(ingredients);
        OrderClient.createOrderByAuthorization(accessToken, order).then().assertThat()
                .statusCode(statusCode);
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void checkOrderCreateWithoutAuth() {
        Order order = new Order(ingredients);
        OrderClient.createOrderWithoutAuthorization(order).then().assertThat()
                .statusCode(statusCode);
    }

    @Test
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
