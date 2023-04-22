import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredGetAllureTest {

    String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2M2NhZjkxZDQ0YzhkNTAwM2QwMjI3NTYiLCJpYXQiOjE2ODIxOTU3MzQsImV4cCI6MTY4MjgwMDUzNH0.xSNx3lrV5mtsmT71TPHuKoEj1eKMRl9bB3I8SMxN_i0";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    @Test
    public void getMyInfoStatusCode() {
        given()
                .auth().oauth2(bearerToken)
                .get("/api/users/me")
                .then().statusCode(200);
    }

    @Test
    public void checkUserName() {
        given()
                .auth().oauth2(bearerToken)
                .get("/api/users/me")
                .then().assertThat().body("data.name",equalTo("pavelz_16@gmail.com"));
    }

    @Test
    public void checkUserNameAndPrintResponseBody() {

        Response response =given().auth().oauth2(bearerToken).get("/api/users/me");
        // отправили запрос и сохранили ответ в переменную response - экземпляр класса Response

        response.then().assertThat().body("data.name",equalTo("pavelz_16@gmail.com"));
        // проверили, что в теле ответа ключу name соответствует нужное имя пользователя

        System.out.println(response.body().asString()); // вывели тело ответа на экран

    }

}