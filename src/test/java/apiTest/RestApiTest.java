package apiTest;

import apiTest.JdbcFolder.JDBC;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import javax.net.ssl.SSLPeerUnverifiedException;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static org.hamcrest.Matchers.*;

public class RestApiTest {

    private static String session;
    private static String productName = "NewFruitTest";
    private static String productType = "FRUIT";

    @Test
    @DisplayName("Отображение страницы 'Список товаров'")
    void pageCheck() {
        getPage();
        checkTable();
    }

    @Test
    @DisplayName("Добавление товара")
    void addingGood() {
        addGood();
        checkAddedGood(session);
        deleteRow(productName,productType);
        checkAddedGood(session);
    }

    void getPage() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080"),
                SpecFile.responseSpecification(200));
        given()
                .basePath("/food")
                .when()
                .log().all()
                .get()
                .then();
    }
    void checkTable() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080/api"),
                SpecFile.responseSpecification(200));
        Response response = given()
                .basePath("/food")
                .when()
                .get();
        response.then()
                .log().all()
                .body(Matchers.not(Matchers.empty()))
                .body("table.size()", equalTo(4));
    }

    void addGood() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080/api"),
                SpecFile.responseSpecification(200));

        Response response = given()
                .body("{\n" +
                        "                \"name\":\"NewFruitTest\",\n" +
                        "                \"type\":\"FRUIT\",\n" +
                        "                \"exotic\":true\n" +
                        "                }")
                .basePath("/food")
                .contentType(ContentType.JSON)
                .when()
                .post();
        response.then()
                .log().all();
        session = response.getDetailedCookies().getValue("JSESSIONID");

    }

    void checkAddedGood(String session) {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080/api"),
                SpecFile.responseSpecification(200));
        List<String> list = given()
                .basePath("/food")
                .sessionId(session)
                .when()
                .get() //?
                .then()
                .log().all()
                .extract()
                .jsonPath().getList("name");
        Assertions.assertEquals(productName, list.get(list.size()-1), "Product was not found");
    }

    void deleteRow(String pName, String pType){
        JDBC.deleteRow(pName, pType);
    }

    void pageElementsCheck() {
        SpecFile.installSpecification(SpecFile.requestSpecification("http://localhost:8080"),
                SpecFile.responseSpecification(200));
        Response response = given()
                .basePath("/food")
                .when()
                .get();
        response.then()
                .log().all();
//                .body(hasXPath("//h5[text()='Список товаров']"));
//                .body(hasXPath("//th[text()='#']"));
//                .body(hasXPath("//th[text()='Наименование']"))
//                .body(hasXPath("//th[text()='Тип']"))
//                .body(hasXPath("//th[text()='Экзотический']"))
//                .body(hasXPath("//button[text()='Добавить']"));
    }
}
