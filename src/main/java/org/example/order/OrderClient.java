package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.RestClient;
import org.example.ingredient.Ingredient;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    @Step("create order")
    public ValidatableResponse create(Ingredient ingredient, String jwt) {

        return given().spec(getBaseSpec(jwt)).body(ingredient).when().post("orders").then();

    }

    @Step("get list of user orders")
    public ValidatableResponse get(String jwt) {

        return given().spec(getBaseSpec(jwt)).when().get("orders").then();

    }

}
