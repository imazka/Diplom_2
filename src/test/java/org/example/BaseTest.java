package org.example;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.response.ValidatableResponse;
import org.example.ingredient.Ingredient;
import org.example.ingredient.IngredientClient;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.example.user.User;
import org.example.user.UserClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    protected ValidatableResponse registrationResponse;
    protected UserClient userClient;
    protected User user;
    protected Ingredient ingredient;
    protected IngredientClient ingredientClient;
    protected Order order;
    protected OrderClient orderClient;
    protected Gson gson;
    @BeforeEach
    public void setUp() {

        Faker faker = new Faker();
        user = new User();
        user.setName(faker.name().username());
        user.setPassword(faker.internet().password(10, 15));
        user.setEmail(faker.internet().emailAddress(user.getName().toLowerCase()));

        userClient = new UserClient();
        order = new Order();
        orderClient = new OrderClient();
        ingredient = new Ingredient();
        ingredientClient = new IngredientClient();
        gson = new Gson();
        registrationResponse = null;

    }

    @AfterEach
    public void deleteUser() {

        //удаление зарегистрированного юзера
        try {

            userClient.deleteUser(registrationResponse.extract().path("accessToken"));

        } catch (Exception ignored) {}

    }

}
