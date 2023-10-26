package rest;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import static io.restassured.RestAssured.*;

public class SchemaTest {

    @Test
    public void deveValidarSchemaXML() {
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/usersXML")
                .then()
                .log().all()
                .statusCode(200)
                .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))

        ;
    }

    @Test(expected = SAXParseException.class)
    public void naoDeveValidarSchemaXMLInvalido() {
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/invalidUsersXML")
                .then()
                .log().all()
                .statusCode(200)
                .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))

        ;
    }



    @Test
    public void deveValidarSchemaJson() {
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))

        ;
    }

}
