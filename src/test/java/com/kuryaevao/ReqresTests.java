package com.kuryaevao;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTests {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void listUsers() {

        String listNumber = "1";

        String response =
                get("/api/users?page=" + listNumber)
                        .then()
                        .statusCode(200)
                        .extract().response().path("page").toString();

        assertThat(response).isEqualTo(listNumber);
    }

    @Test
    void createUser() {

        String userName = "Tyler",
                userJob = "The Creator";

        String data = "{ \"name\": \"" + userName + "\", \"job\": \"" + userJob + "\" }";
        System.out.println(data);

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is(userName), "job", is(userJob));
    }

    @Test
    void updateUser() {

        String userName = "Tyler",
                userJob = "The CupcakeMaker";

        String data = "{ \"name\": \"" + userName + "\", \"job\": \"" + userJob + "\" }";
        System.out.println(data);

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is(userName), "job", is(userJob), "updatedAt", notNullValue());
    }

    @Test
    void deleteUser() {

        given()
                .contentType(JSON)
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void negativeRegisterUser() {

        String data = "{\n" +
                "  \"email\": \"dragon.punch@kungfu.com\",\n" +
                "  \"password\": \"flyingKick1337\"\n" +
                "}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }
}