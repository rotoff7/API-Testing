package apiTest;

import apiTest.JdbcFolder.JDBC;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestApiTest {

    private static String session;
    private static String productName = "NewFruitTest";
    private static String productType = "FRUIT";
    private static int productExotic = 1;

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
        JDBC.checkDB(productName, productType, productExotic);
        JDBC.deleteRow(productName, productType);
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
                .body("table.size()", equalTo(4))
                .assertThat()
                .body("[0]", hasKey("name"))
                .body("[0]", hasKey("type"))
                .body("[0]", hasKey("exotic"));
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
                .get()
                .then()
                .assertThat()
                .body("[4]", hasValue(productName))
                .body("[4]", hasValue(productType))
                .body("[4]", hasValue(true))
                .log().all()
                // Изначальный вариант:
                .extract()
                .jsonPath().getList("name");
        Assertions.assertEquals(productName, list.get(list.size() - 1), "Product was not found");
    }
}
