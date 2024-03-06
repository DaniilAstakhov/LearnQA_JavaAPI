package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import  lib.BaseTestCase;

import java.util.HashMap;
import java.util.Map;
public class UserDeleteTest extends BaseTestCase{

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("A test for trying to delete a user by ID 2.")
    @DisplayName("Delete user with id=2 test")
    public void UserDeleteTest(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String token = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/" + 2, token, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("A test for trying to delete a new user")
    @DisplayName("Delete new user")
    public void UserDeleteJustCreatedTest(){
        //CREATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //LOGIN USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String token = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        //DELETE
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, token, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //CHECK
        Response responseUserGet = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, token, cookie);

        Assertions.assertResponseCodeEquals(responseUserGet, 404);
        Assertions.assertResponseTextEquals(responseUserGet, "User not found");
    }

    @Test
    @Description("A test for trying to delete a user, being authorized as other user")
    @DisplayName("Delete new user, being authorized as other user")
    public void UserDeleteAsOtherUserTest(){
        //CREATE 2 USERs
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> userData2 = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData);
        JsonPath responseCreateAuth2 = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData2);
        String userId = responseCreateAuth.getString("id");

        //LOGIN USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData2.get("email"));
        authData.put("password", userData2.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String token = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        //DELETE
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, token, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);

        //CHECK
        Response responseUserGet = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, token, cookie);

        Assertions.assertResponseCodeEquals(responseUserGet, 404);
        Assertions.assertResponseTextEquals(responseUserGet, "User not found");
    }
}
