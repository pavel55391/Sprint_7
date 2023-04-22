import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.example.OrderData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateOrderDataTest {

    private static OrderData order;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        order = new OrderData();
        order.setFirstName("Naruto");
        order.setLastName("Uchiha");
        order.setAddress("Konoha, 142 apt.");
        order.setMetroStation("4");
        order.setPhone("+7 800 355 35 35");
        order.setRentTime(5);
        order.setDeliveryDate("2020-06-06");
        order.setComment("Saske, come back to Konoha");
        //order.setColor(colorData());

    }



    String json = "{\"firstName\": \"Naruto\",\n" +
            "            \"lastName\": \"Uchiha\",\n" +
            "            \"address\": \"Konoha, 142 apt.\",\n" +
            "            \"metroStation\": 4,\n" +
            "            \"phone\": \"+7 800 355 35 35\",\n" +
            "            \"rentTime\": 5,\n" +
            "            \"deliveryDate\": \"2020-06-06\",\n" +
            "            \"comment\": \"Saske, come back to Konoha\"}";

    static Object[][] colorData(){
        return new Object[][]{
                {List.of("GREY"), true},
                {List.of("BLACK"), true},
                {List.of("GREY", "BLACK"), true},
                {List.of(), true},
                {List.of("RED"), false}
        };
    }

//    @Order(1)
//    @ParameterizedTest
//    @MethodSource("colorData")
//    public void canSelectColorBlack(List<String> colors, boolean expected) {
//        List<String> allowedColors = List.of("GREY", "BLACK");
//        boolean actual = true;
//        for (String color : colors) {
//            if (!allowedColors.contains(color)) {
//                actual = false;
//                break;
//            }
//        }
//        assertEquals(expected, actual);
//    }

    @Order(1)
   @ParameterizedTest
   @MethodSource("colorData")
    public void canSelectColorBlack(List<String> colors, boolean expected) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");

        //response.then().assertThat().statusCode(409);
        List<String> allowedColors = List.of("GREY", "BLACK");
        boolean actual = true;
        for (String color : colors) {
            if (!allowedColors.contains(color)) {
                actual = false;
                break;
            }
        }
        response.then().assertThat().statusCode(201).and().body("track" , notNullValue());
    }
}











//    public class OrderTest{
//        private String firstName;
//        private String lastName;
//        private String address;
//        private String metroStation;
//        private String phone;
//        private int rentTime;
//
//        public OrderTest() {
//
//        }
//
//        private String deliveryDate;
//        private String comment;
//        private List<String> color;
//    }