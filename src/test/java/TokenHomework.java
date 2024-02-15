import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class TokenHomework {
    @Test
    public void TokenHomeworkTest() throws InterruptedException {
        JsonPath createTask = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String token = createTask.get("token");
        int remainingTime = createTask.get("seconds");
        System.out.println("The task is running!" + "\nRemaining time: " + remainingTime + "\nToken: " + token);

        JsonPath taskStatusCheck = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        if(!taskStatusCheck.get("status").equals("Job is NOT ready")){
            System.out.println("Something went wrong (Incorrect status)");
        }else {
            System.out.println("\nWaiting...");
            Thread.sleep(remainingTime * 1000);

            JsonPath taskStatusCheck2 = RestAssured
                    .given()
                    .queryParam("token", token)
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();
            if(taskStatusCheck2.get("status").equals("Job is ready")){
                System.out.println("\nCorrect status!");
                if(taskStatusCheck2.get("result") != null){
                    String result = taskStatusCheck2.get("result");
                    System.out.println("The task is completed!\nResult: " + result);
                }else {
                    System.out.println("Something went wrong (there is no result)");
                }
            }else {
                System.out.println("Something went wrong (Incorrect status)");
            }
        }
    }
}
