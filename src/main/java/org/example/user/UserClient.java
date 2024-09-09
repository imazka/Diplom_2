package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.RestClient;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    private final String USER_REGISTRATION_PATH = "auth/register";
    private final String USER_LOGIN_PATH = "auth/login";
    private final String USER_DELETE_AND_PATCH_PATH = "auth/user";

    @Step("create user")
    public ValidatableResponse createUser(User user) {

        return given().spec(getBaseSpec()).body(user).when().post(USER_REGISTRATION_PATH).then();

    }

    @Step("login user")
    public ValidatableResponse loginUser(User user) {

        return given().spec(getBaseSpec()).body(user).when().post(USER_LOGIN_PATH).then();

    }

    @Step("delete user")
    public ValidatableResponse deleteUser(String jwt) {

        return given().spec(getBaseSpec(jwt)).when().delete(USER_DELETE_AND_PATCH_PATH).then();

    }

    @Step("update user")
    public ValidatableResponse updateUser(User user, String jwt) {

        return given().spec(getBaseSpec(jwt)).body(user).when().patch(USER_DELETE_AND_PATCH_PATH).then();

    }

}
