import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.*;
import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreatingCourierTest {

    private static Courier courier;

    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier();
        Random random = new Random();
        courier.setLogin("naruto" + random.nextInt());
        courier.setPassword("konoha" + random.nextInt());
        courier.setFirstName("nani" + random.nextInt());
    }


    @Test
    @Order(1)
    @DisplayName("Check creating courier / status code / body")
    public void createCourierReturnsCorrectCodeAndBody(){
        Response response =
                given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().statusCode(201);
        response.then().assertThat().body("ok", is(true));
    }

    @Test
    @Order(2)
    @DisplayName("Creating two identical couriers")
    public void createCourierDuplicate() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat().statusCode(409);
    }

    @Test
    @Order(3)
    @DisplayName("Checking required fields when creating courier")
    public void createCourierWithRequiredFields(){
        boolean hasEmptyFields = Utils.isEmptyString(courier.getLogin())
                || Utils.isEmptyString(courier.getPassword())
                || Utils.isEmptyString(courier.getFirstName());
        assertFalse(hasEmptyFields);
    }

    @Test
    @Order(4)
    @DisplayName("Check creating courier without required field")
    public void requestWithoutOneField(){  //+
        String json = "{ \"login\": \"ninja\",\n" +
                "\"password\": \"1234\"\n}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat().statusCode(409);
    }

    @Test
    @Order(5)
    @DisplayName("Check creating courier with an existing username")
    public void createUserWithLoginThatAlreadyExistsReturnError(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

    response.then().assertThat().statusCode(409);
    }
}
