package com.kuryaevao;

import com.kuryaevao.lombok.CreatedUser;
import com.kuryaevao.lombok.LombokUserData;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.kuryaevao.Specs.request;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void listUsers() {

        Integer listNumber = 1;

        Integer response = given()
                .spec(request)
                .when()
                .get("/users?page=" + listNumber)
                .then()
                .log().body()
                .statusCode(200)
                .extract().response().path("page");

        assertThat(response).isEqualTo(listNumber);
    }

    @Test
    void listUsersWithGroovy() {

        given()
                .spec(request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.findAll{it.avatar =~/.*image\\.jpg$/}.avatar.flatten()",
                        hasItem("https://reqres.in/img/faces/2-image.jpg"))
                .extract().response().path("page");
        //.body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
        //                        hasItem("eve.holt@reqres.in"));
    }

    @Test
    void singleUserUsingLombok() {

        Integer singleUserId = 2;

        LombokUserData data = given()
                .spec(request)
                .when()
                .get("/users/" + singleUserId)
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(LombokUserData.class);

        assertEquals(singleUserId, data.getUser().getId());
    }

    @Test
    void createUserUsingLombok() {

        String userName = "Tyler",
                userJob = "The Creator";

        String data = "{ \"name\": \"" + userName + "\", \"job\": \"" + userJob + "\" }";
        System.out.println(data);

        Response response = given()
                .spec(request)
                .body(data)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract()
                .response();

        CreatedUser createdUser = response.getBody().as(CreatedUser.class);

        assertEquals(userName, createdUser.getName());
        assertEquals(userJob, createdUser.getJob());
    }

    @Test
    void updateUser() {

        String userName = "Tyler",
                userJob = "The CupcakeMaker";

        String data = "{ \"name\": \"" + userName + "\", \"job\": \"" + userJob + "\" }";
        System.out.println(data);

        given()
                .spec(request)
                .body(data)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .body("name", is(userName), "job", is(userJob), "updatedAt", notNullValue());
    }

    @Test
    void deleteUser() {

        given()
                .spec(request)
                .when()
                .delete("/users/2")
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test
    void negativeRegisterUser() {

        String data = "{\n" +
                "  \"email\": \"dragon.punch@kungfu.com\",\n" +
                "  \"password\": \"flyingKick1337\"\n" +
                "}";

        given()
                .spec(request)
                .body(data)
                .when()
                .post("/register")
                .then()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }
}