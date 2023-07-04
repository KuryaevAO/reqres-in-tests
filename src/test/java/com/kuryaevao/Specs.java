package com.kuryaevao;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

public class Specs {
    public static RequestSpecification request = with()
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);

    /*
    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody(containsString("success"))
            .build();
            */
}