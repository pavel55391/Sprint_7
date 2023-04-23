import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.junit.jupiter.api.*;
import java.time.Duration;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginCourierTest {


    private static String correctLogin = "\"login\": \"hashirama\"";
    private static String correctPassword = "\"password\": \"1488_228\"";
    private static String correctFirstName = "\"firstName\": \"madara\"";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        String json = String.format("{%s, %s, %s}", correctLogin, correctPassword, correctFirstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @Order(1)
    @DisplayName("Check login courier")
    public void loginCourier(){

        String json = "{\"login\": \"hashirama\",\n" +
                "\"password\": \"1488_228\"}";

        Response response =
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().statusCode(200);
    }

    @Test
    @Order(2)
    @DisplayName("Check login courier with required fields")
    public void passRequiredFieldsForAuthorization(){
        Gson gson = new Gson();
        String json = String.format("{%s, %s}", correctLogin, correctPassword);
        Courier courier = gson.fromJson(json, Courier.class);
        boolean hasEmptyFields = Utils.isEmptyString(courier.getLogin())
                || Utils.isEmptyString(courier.getPassword());

        assertFalse(hasEmptyFields);
    }

    @Test
    @Order(3)
    @DisplayName("Check login courier with incorrect password")
    public void systemReturnErrorIfPasswordIncorrect() {
        String json = "{  \"login\": \"hashirama\",\n" +
                "    \"password\": \"tobirama\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().statusCode(404);
    }

    @Test
    @Order(4)
    @DisplayName("Check login courier without one field")
    public void recuestWithoutOneField(){
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            while (true);
        });
        String json = "{  \"login\": \"ninja\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().statusCode(400);
    }

    @Test
    @Order(5)
    @DisplayName("Check login courier with non exist credential")
    public void logInUnderNonExistUser(){
        String json = "{  \"login\": \"test\",\n" +
                "    \"password\": \"test\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().statusCode(404);
    }

    @Test
    @Order(6)
    @DisplayName("Check successful ID request")
    public void successfulRequestReturnsId(){
        String json = "{  \"login\": \"hashirama\",\n" +
                "    \"password\": \"1488_228\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().assertThat().body("id" , notNullValue());
    }

}
