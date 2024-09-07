package org.example.user.tests;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationUserTest extends BaseTest {

    //тест с позитивным сценарием
    @Test
    public void positiveUserRegistrationTest() {

        //регистрация юзера с позитивным сценарием
        registrationResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, registrationResponse.extract().statusCode());
        assertEquals(user.getName(), registrationResponse.extract().path("user.name"));
        assertNotNull(registrationResponse.extract().path("accessToken"));
        assertNotNull(registrationResponse.extract().path("refreshToken"));

    }

    //повторная регистрация юзера
    @Test
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

    //регистрация эзера без одного обязательного поля
    @Test
    public void userRegistrationWithoutOneFieldTest() {

        //регистрация юзера без одного обязательного поля
        user.setName(null);
        ValidatableResponse validatableResponse2 = userClient.createUser(user);
        assertEquals(HttpStatus.SC_FORBIDDEN, validatableResponse2.extract().statusCode());
        assertEquals("Email, password and name are required fields", validatableResponse2.extract().path("message"));

    }

}
