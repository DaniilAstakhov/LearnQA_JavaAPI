import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectHomework01 {
    @Test
    public void LongRedirectTest() {
        Response response = RestAssured
                .given()
                .redirects().follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect");

        System.out.println(response.getHeader("Location"));
    }
}
