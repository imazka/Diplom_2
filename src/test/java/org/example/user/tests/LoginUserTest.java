package org.example.user.tests;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginUserTest extends BaseTest {

    @Test
    @Description("авторизация с позитивным сценарием")
    public void loginRegisteredUserTest() {

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

    }

    @Test
    @Description("авторизация с неверным логином или паролем")
    public void loginWithIncorrectNameTest() {

        //логин зарегистрированного юзера
        ValidatableResponse loginResponse = userClient.loginUser(user);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, loginResponse.extract().statusCode());
        assertEquals("email or password are incorrect", loginResponse.extract().path("message"));

    }

}
