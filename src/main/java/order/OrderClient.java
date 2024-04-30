package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_ENDPOINT = "/api/orders";

    @Step("Создание заказа авторизованным пользователем")
    public static Response createOrderByAuthorization(String accessToken, Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }
    @Step("Создание заказа неавторизованным пользователем")
    public static Response createOrderWithoutAuthorization(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }
    @Step("Получение списка заказов")
    public static Response getListOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get(ORDER_ENDPOINT);
    }

}
