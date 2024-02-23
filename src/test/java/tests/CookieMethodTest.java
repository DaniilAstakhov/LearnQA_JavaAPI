package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookieMethodTest {
    @Test
    public void CookieMethodTest(){
            Response Response = RestAssured
                    .get("https://playground.learnqa.ru/api/homework_cookie")
                    .andReturn();

        Map<String, String> cookies = Response.getCookies();
        assertEquals(null, cookies, "There are some cookies -> " + cookies.toString());
    }
}
