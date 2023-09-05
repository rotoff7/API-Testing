package apiTest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class RestApiTest {

    @Test
    void getPage() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080"),
                SpecFile.responseSpecification(200));
        given()
                .basePath("/food")
                .when()
                .get()
                .then()
                .log().all();
    }

    @Test
    void checkTable() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080"),
                SpecFile.responseSpecification(200));
        Response response = given()
                .basePath("/food")
                .when()
                .get(); //?
        response.then()
                .log().all()
                .body(Matchers.not(Matchers.empty()));
//                .body("table.size()", equalTo(4));
    }

    @Test
    void addGood() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080/api"), //?
                SpecFile.responseSpecification(200));

        given()
                .body("{\n" +
                        "                \"name\":\"New Fruit\",\n" +
                        "                \"type\":\"FRUIT\",\n" +
                        "                \"exotic\":true\n" +
                        "                }")
                .basePath("/food")
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .log().all();
//                .extract()
//                .jsonPath()
    }

    @Test
    void checkAddedGood() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080/api"),
                SpecFile.responseSpecification(200));
        Response response = given()
                .basePath("/food")
                .when()
                .get(); //?
        response.then()
                .log().all()
                .assertThat()
                // before id
                .body("name", hasItem("New Fruit"))
                .body("type", hasItem("fruit"))
                .body("exotic", hasItem("true"));
    }

    @Test
    void deleteGood() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080/api"),
                SpecFile.responseSpecification(200));
        Response response = given()
                .basePath("/food")
                .when()
                .delete(); //????????
        response.then()
                .log().all();

    }
}
