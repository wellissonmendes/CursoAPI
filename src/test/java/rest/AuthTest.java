package rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
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
                .extract().path("token");
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

    @Test
    public void deveAcessarAplicacaoWeb() {

        String cookie = given()
                .log().all()
                .formParam("email", "wellisson.mendes@gmail.com")
                .formParam("senha", "123456")
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .when()
                .post("http://seubarriga.wcaquino.me/logar")
                .then()
                .log().all()
                .statusCode(200)
                .extract().header("set-cookie");
        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);



     String body =    given()
                .log().all()
                .cookie("connect.sid",cookie)
                .when()
                .get("http://seubarriga.wcaquino.me/contas")
                .then()
                .log().all()
                .statusCode(200)
             //   .body("html.body.table.tbody.tr[0].td[0]", is("Conta de teste"))
                .extract().body().asString();
       // path("html.body.table.tbody.tr[0].td[0]")
        ;
        System.out.println("----------------");
        XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
    }


}
