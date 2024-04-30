import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import java.util.List;

import static constants.Data.BASE_URL;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static user.UserCreds.from;

public class OrderGetListTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        User user = new User("Yohoho@yandex.ru", "123456", "Yohoho");
        UserClient.create(user);
        Response loginResponse = UserClient.login(from(user));
        accessToken = loginResponse.path("accessToken");
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa73"));
        OrderClient.createOrderByAuthorization(accessToken,order).then().assertThat()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void checkOrderGetListWithAuth() {
        OrderClient.getListOrders(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void checkOrderGetListWithoutAuth() {
        OrderClient.getListOrders("").then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            UserClient.delete(accessToken).then().assertThat().body("success", equalTo(true))
                    .and()
                    .body("message", equalTo("User successfully removed"))
                    .and()
                    .statusCode(SC_ACCEPTED);
        }
    }
}
