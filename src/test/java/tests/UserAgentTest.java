package tests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserAgentTest {
    static Stream<String[]> provideArrays(){
        return Stream.of(
                new String[] {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30", "Mobile", "No", "Android"},
                new String[] {"Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1", "Mobile", "Chrome", "iOS"},
                new String[] {"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)", "Googlebot", "Unknown", "Unknown"},
                new String[] {"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0", "Web", "Chrome", "No"},
                new String[] {"Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1", "Mobile", "No", "iPhone"}
        );
    }
    @ParameterizedTest
    @MethodSource("provideArrays")
    public void testUserAgent(String userAgent, String expectedPlatform, String expectedBrowser, String expectedDevice){
        Response response = RestAssured
                .given()
                .header("user-agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();
        System.out.println("Cheking -> " + userAgent);

        assertEquals(expectedPlatform, response.jsonPath().getString("platform"), "The platform is incorrect. Expected = " + expectedPlatform + "\nThe received parameter = " + response.jsonPath().getString("platform"));
        assertEquals(expectedBrowser, response.jsonPath().getString("browser"), "The browser is incorrect. Expected = " + expectedBrowser + "\nThe received parameter = " + response.jsonPath().getString("browser"));
        assertEquals(expectedDevice, response.jsonPath().getString("device"), "The device is incorrect. Expected = " + expectedDevice + "\nThe received parameter = " + response.jsonPath().getString("device"));

        System.out.println("\n");
    }
}