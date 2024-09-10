package org.example.user.tests;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationUserTest extends BaseTest {

    @Test
    @Description("тест с позитивным сценарием")
    public void positiveUserRegistrationTest() {

        //регистрация юзера с позитивным сценарием
        registrationResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, registrationResponse.extract().statusCode());
        assertEquals(user.getName(), registrationResponse.extract().path("user.name"));
        assertNotNull(registrationResponse.extract().path("accessToken"));
        assertNotNull(registrationResponse.extract().path("refreshToken"));

    }

    @Test
    @Description("повторная регистрация юзера")
    public void secondUserRegistrationTest() {

        //регистрация юзера с позитивным сценарием
        registrationResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, registrationResponse.extract().statusCode());
        assertEquals(user.getName(), registrationResponse.extract().path("user.name"));
        assertNotNull(registrationResponse.extract().path("accessToken"));
        assertNotNull(registrationResponse.extract().path("refreshToken"));

        //повторная регистрация юзера
        ValidatableResponse validatableResponse2 = userClient.createUser(user);
        assertEquals(HttpStatus.SC_FORBIDDEN, validatableResponse2.extract().statusCode());
        assertEquals("User already exists", validatableResponse2.extract().path("message"));

    }

    @Test
    @Description("регистрация эзера без одного обязательного поля")
    public void userRegistrationWithoutOneFieldTest() {

        //регистрация юзера без одного обязательного поля
        user.setName(null);
        ValidatableResponse validatableResponse2 = userClient.createUser(user);
        assertEquals(HttpStatus.SC_FORBIDDEN, validatableResponse2.extract().statusCode());
        assertEquals("Email, password and name are required fields", validatableResponse2.extract().path("message"));

    }

}
