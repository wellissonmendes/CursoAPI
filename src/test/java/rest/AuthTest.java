package rest;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AuthTest {


    @Test
    public void deveAcessarSWAPI() {

        given()
                .log().all()
                .when()
                .get("https://swapi.dev/api/people/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Luke Skywalker"))


        ;

    }


    @Test
    public void deveObterClima() {

        given()
                .log().all()
                .queryParam("q", "Fortaleza,BR")
                .queryParam("appid", "e730c84f6dcbdcbdebeb176ce85fdd77")
                .queryParam("units", "metric")
                .when()
                .get("https://api.openweathermap.org/data/2.5/weather")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Fortaleza"))
                .body("sys.country", is("BR"))
                .body("main.temp", greaterThan(25f))


//https://api.openweathermap.org/data/2.5/weather?q=Fortaleza&appid=e730c84f6dcbdcbdebeb176ce85fdd77
        ;

    }



}
