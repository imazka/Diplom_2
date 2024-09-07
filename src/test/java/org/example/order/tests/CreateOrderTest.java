package org.example.order.tests;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateOrderTest extends BaseTest {

    //создание заказа автирозованным юзером с ингредиентами
    @Test
    public void createOrderWithAuthorizationTest() {

        //список ингредиентов
        ValidatableResponse getIngredientsResponse = ingredientClient.getAll();
        String[] arr = getIngredientsResponse.extract().path("data._id").toString().replaceAll(",", "").split("\\s+");

        //регистрация юзера с позитивным сценарием
        ValidatableResponse registrationResponse = userClient.createUser(user);
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

        //создание заказа
        Random random = new Random();
        ingredient.setIngredients(new String[]{arr[random.nextInt(arr.length - 1)], arr[random.nextInt(arr.length - 1)]});
        ValidatableResponse createOrderResponse = orderClient.create(ingredient, loginResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_OK, createOrderResponse.extract().statusCode());
        assertEquals(true, createOrderResponse.extract().path("success"));

    }

    //создание заказа автирозованным юзером без ингредиентов
    @Test
    public void createOrderWithoutIngredientsTest() {

        //регистрация юзера с позитивным сценарием
        ValidatableResponse registrationResponse = userClient.createUser(user);
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

        //создание заказа
        ValidatableResponse createOrderResponse = orderClient.create(ingredient, loginResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderResponse.extract().statusCode());
        assertEquals(false, createOrderResponse.extract().path("success"));
        assertEquals("Ingredient ids must be provided", createOrderResponse.extract().path("message"));

    }

    //создание заказа автирозованным юзером с неверным хэшем ингредиентов
    @Test
    public void createOrderWithIncorrectIngredientsHashTest() {

        //список ингредиентов
        ValidatableResponse getIngredientsResponse = ingredientClient.getAll();
        String[] arr = getIngredientsResponse.extract().path("data._id").toString().replaceAll(",", "").split("\\s+");

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

        //создание заказа
        Random random = new Random();
        ingredient.setIngredients(new String[]{
                arr[random.nextInt(arr.length - 1)].substring(0, arr.length - 4) + "qwe",
                arr[random.nextInt(arr.length - 1)].substring(0, arr.length - 4) + "qwe"
        });
        ValidatableResponse createOrderResponse = orderClient.create(ingredient, loginResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, createOrderResponse.extract().statusCode());

    }

    //создание заказа без авторизации
    @Test
    public void createOrderWithoutAuthorizationTest() {

        //список ингредиентов
        ValidatableResponse getIngredientsResponse = ingredientClient.getAll();
        String[] arr = getIngredientsResponse.extract().path("data._id").toString().replaceAll(",", "").split("\\s+");

        //регистрация юзера с позитивным сценарием
        registrationResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, registrationResponse.extract().statusCode());
        assertEquals(user.getName(), registrationResponse.extract().path("user.name"));
        assertNotNull(registrationResponse.extract().path("accessToken"));
        assertNotNull(registrationResponse.extract().path("refreshToken"));

        //создание заказа
        Random random = new Random();
        ingredient.setIngredients(new String[]{arr[random.nextInt(arr.length - 1)], arr[random.nextInt(arr.length - 1)]});
        ValidatableResponse createOrderResponse = orderClient.create(ingredient, registrationResponse.extract().path("accessToken"));
        assertEquals(HttpStatus.SC_OK, createOrderResponse.extract().statusCode());
        assertEquals(true, createOrderResponse.extract().path("success"));

    }

}
