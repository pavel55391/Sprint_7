import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.example.OrderData;
import org.example.OrderData.Color;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.example.OrderData.Color.BLACK;
import static org.example.OrderData.Color.GREY;
import static org.hamcrest.Matchers.notNullValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateOrderDataTest {

    private static OrderData order;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        order = new OrderData();
        order.setFirstName("Naruto");
        order.setLastName("Uchiha");
        order.setAddress("Konoha, 106 apt.");
        order.setMetroStation("4");
        order.setPhone("+7 800 555 35 35");
        order.setRentTime(5);
        order.setDeliveryDate("2020-06-06");
        order.setComment("Saske, come back to Konoha");
       }

    static Object[][] colorData(){
        return new Object[][]{
                {List.of(GREY)},
                {List.of(BLACK)},
                {List.of(GREY, BLACK)},
                {List.of()}
        };
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("colorData")
    @DisplayName("Check successful select color")
    @Description("Check GREY and BLACK color")
    public void canSelectColorBlack(List<Color> colors) {
        order.setColor(colors);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().statusCode(201).and().body("track" , notNullValue());
    }
}











