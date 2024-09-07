package org.example.ingredient;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.RestClient;

import static io.restassured.RestAssured.given;

public class IngredientClient extends RestClient {

    @Step
    public ValidatableResponse getAll() {

        return given().spec(getBaseSpec()).when().get("ingredients").then();

    }

}
