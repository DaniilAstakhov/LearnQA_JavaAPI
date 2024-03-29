package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import  lib.BaseTestCase;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Epic("Editing users")
    @Severity(SeverityLevel.NORMAL)
    public void EditJustCreatedTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Epic("Editing users")
    @Description("Trying to change the user's data, being unauthorized")
    @DisplayName("User editing test without authorization")
    @Severity(SeverityLevel.NORMAL)

    public void EditWithoutAuthorizationTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                "empty",
                "empty",
                editData);
        //Check
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Epic("Editing users")
    @Description("Trying to change the user's data, being authorized as other user")
    @DisplayName("The test of editing a user being logged in as another user")
    @Severity(SeverityLevel.NORMAL)

    public void EditAsOtherUserTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData);
        JsonPath responseCreateAuth2 = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData2);
        String userId = responseCreateAuth.getString("id");

        //LOGIN as other user
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData2.get("email"));
        authData.put("password", userData2.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);
        //Check
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "You are not logged in as the user you want to edit");
    }

    @Test
    @Epic("Editing users")
    @Description("Trying to change the user's data, with wrong email, being authorized as same user")
    @DisplayName("The test of editing a user with wrong email, being logged in")
    @Severity(SeverityLevel.NORMAL)

    public void EditAsSameUserWithWrongEmailTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newEmail = "newWrongEmail.test";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);
        //Check
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
    }

    @Test
    @Epic("Editing users")
    @Description("Trying to change the user's data, with too short firstname, being authorized as same user")
    @DisplayName("The test of editing a user with too short firstname, being logged in")
    @Severity(SeverityLevel.NORMAL)

    public void EditAsSameUserWithTooShortFirstnameTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makeJsonPathPostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String shortFirstName = "a";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", shortFirstName);

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);
        //Check
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Too short value for field firstName");
    }
}
