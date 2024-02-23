package garbage;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectHomework {
    @Test
    public void LongRedirectTest1() {
        Response response = RestAssured
                .given()
                .redirects().follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect");

        int statusCode = response.getStatusCode();
        //System.out.println("Status code: " + statusCode);
        String redirectLocation = response.getHeader("Location");
        System.out.println(redirectLocation);

        while (statusCode >= 300 && statusCode < 400){
            Response newRedirectResponse = RestAssured
                    .given()
                    .redirects().follow(false)
                    .when()
                    .get(redirectLocation);
            statusCode = newRedirectResponse.getStatusCode();
            //System.out.println("Status code: " + statusCode);
            redirectLocation = newRedirectResponse.getHeader("Location");
            if (redirectLocation != null)
            System.out.println(redirectLocation);
        }
    }
}