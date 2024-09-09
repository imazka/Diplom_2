package org.example.user.tests;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateUserTest extends BaseTest {

    @Test
    @Description("изменение данных зарегистрированного пользователя без авторизации")
    public void updateUnAuthorizedUserTest() {

        //регистрация юзера с позитивным сценарием
        registrationResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, registrationResponse.extract().statusCode());
        assertEquals(user.getName(), registrationResponse.extract().path("user.name"));
        assertNotNull(registrationResponse.extract().path("accessToken"));
        assertNotNull(registrationResponse.extract().path("refreshToken"));

        //изменение данных пользователя
        user.setName(user.getName() + 1);
        user.setPassword(user.getPassword() + 1);
        user.setEmail(user.getEmail().replace("@", "a@"));
        ValidatableResponse updateResponse = userClient.updateUser(user, registrationResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_UNAUTHORIZED, updateResponse.extract().statusCode());

    }

    @Test
    @Description("изменение данных зарегистрированного пользователя с авторизацией")
    public void updateAuthorizedUserTest() {

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

        //изменение данных пользователя
        user.setName(user.getName() + 1);
        user.setPassword(user.getPassword() + 1);
        user.setEmail(user.getEmail().replace("@", "a@"));
        ValidatableResponse updateResponse = userClient.updateUser(user, loginResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_OK, updateResponse.extract().statusCode());
        assertEquals(user.getName(), updateResponse.extract().path("user.name"));

    }

}
