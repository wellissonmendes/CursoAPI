package rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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


    @Test
    public void naoDeveAcessarSemSenha() {
        given()
                .log().all()
                .when()
                .get("https://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(401)
        ;

    }


    @Test
    public void deveAcessarComSenha() {
        given()
                .log().all()
                .when()
                .get("https://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;

    }

    @Test
    public void deveAcessarComSenha2() {
        given()
                .log().all()
                .auth().basic("admin", "senha")
                .when()
                .get("https://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;

    }

    @Test
    public void deveAcessarComSenhaChallenge() {
        given()
                .log().all()
                .auth().preemptive().basic("admin", "senha")
                .when()
                .get("https://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;

    }


    @Test
    public void deveFazerAutenticacaoComTokenJMT() {
        Map<String, String> login = new HashMap<String, String>();

        login.put("email", "wellisson.mendes@gmail.com");
        login.put("senha", "123456");
        String token = given()
                .body(login)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("https://barrigarest.wcaquino.me/signin")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("token")

        ;
//Obter contas
        given()

                .log().all()
                .header("Authorization", "JWT " + token)
                .when()
                .get("https://barrigarest.wcaquino.me/contas")
                .then()
                .log().all()
                .statusCode(200)

                ;
    }
}
