package rest;

import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class VerbosTest {

    @Test
    public void deveSalvarUsuario() {

        given()
                .log().all()
                .contentType("application/json")
                .body("{ \"name\": \"Jose\",\"age\":50 }")
                .when()
                .post("https://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.is(Matchers.notNullValue()))
                .body("name", Matchers.is("Jose"))
                .body("age", Matchers.is(50))
                ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){

        given()
                .log().all()
                .contentType("application/json")
                .body("{ \"age\":50 }")
                .when()
                .post("https://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(400)
                .body("id", Matchers.is(Matchers.nullValue()))
                .body("error",Matchers.is("Name é um atributo obrigatório"))

;
    }
}


