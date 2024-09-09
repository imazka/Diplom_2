package org.example.order.tests;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetUserOrders extends BaseTest {

    @Test
    @Description("заказы авторизованного юзера")
    public void getUserOrdersWithAuthorizationTest() {

        //регистрация юзера с позитивным сценарием
        registrationResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, registrationResponse.extract().statusCode());
        assertEquals(user.getName(), registrationResponse.extract().path("user.name"));
        assertNotNull(registrationResponse.extract().path("accessToken"));
        assertNotNull(registrationResponse.extract().path("refreshToken"));

        //логин зарегистрированного юзера
        ValidatableResponse loginResponse = userClient.loginUser(user);
        assertEquals(HttpStatus.SC_OK, loginResponse.extract().statusCode());
        assertEquals(user.getName(), loginResponse.extract().path("user.name"));
        assertNotNull(loginResponse.extract().path("accessToken"));
        assertNotNull(loginResponse.extract().path("refreshToken"));

        //получение заказов юзера
        ValidatableResponse getUserOrdersResponse = orderClient.get(loginResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_OK, getUserOrdersResponse.extract().statusCode());
        assertEquals(true, getUserOrdersResponse.extract().path("success"));

    }

    @Test
    @Description("заказы не авторизованного юзера")
    public void getUserOrdersWithoutAuthorizationTest() {

        //регистрация юзера с позитивным сценарием
        registrationResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, registrationResponse.extract().statusCode());
        assertEquals(user.getName(), registrationResponse.extract().path("user.name"));
        assertNotNull(registrationResponse.extract().path("accessToken"));
        assertNotNull(registrationResponse.extract().path("refreshToken"));

        //получение заказов юзера
        ValidatableResponse getUserOrdersResponse = orderClient.get(registrationResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_UNAUTHORIZED, getUserOrdersResponse.extract().statusCode());
        assertEquals(true, getUserOrdersResponse.extract().path("success"));

    }

}
