package rest;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FileTest {

    @Test
    public void deveObrigarEnvioArquivo() {

        given()
                .log().all()
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .statusCode(404)
                .body("error", is("Arquivo não enviado"))
        ;
    }


    @Test
    public void deveFazerUploadDoArquivo() {

        given()
                .log().all()
                .multiPart("arquivo", new File("src/test/resources/users.pdf"))
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("users.pdf"))
        ;
    }


    @Test
    public void naoDeveFazerUploadDoArquivoGrande() {

        given()
                .log().all()
                .multiPart("arquivo", new File("src/test/resources/maiorQue5.pdf"))
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .time(lessThan(5000L))
                .statusCode(413)
        ;
    }


    @Test
    public void deveBaixarArquivo() throws IOException {

        byte[] image = given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/download")
                .then()
                .log().all()
                .statusCode(200)
                .extract().asByteArray();

        File imagem = new File("src/test/resources/file.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        System.out.println(imagem.length());
        Assert.assertThat(imagem.length(), lessThan(100000L));
    }
}
