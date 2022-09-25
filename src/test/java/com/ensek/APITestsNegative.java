package com.ensek;

import com.ensek.databinding.Energy;
import com.ensek.databinding.Orders;
import com.ensek.helpers.Helpers;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class APITestsNegative {

    Energy energyObj;

    @BeforeClass
    public void getEnergyList() {
        baseURI = "https://ensekapicandidatetest.azurewebsites.net";
        Response res = given().
                accept(ContentType.JSON).
                when().
                get("/energy");
        res.prettyPeek().then().statusCode(200);
        energyObj = res.body().jsonPath().getObject("$", Energy.class);
    }

    @Test()
    public void wrongEnergyIdentifier() {
        Response buyNuclearRes = given().
                accept(ContentType.JSON).
                when().
                put("/buy/5/120");
        buyNuclearRes.prettyPeek().then().statusCode(400);
        String actResponseMessage = buyNuclearRes.body().jsonPath().get("message");
        String expMessage = "Bad request";
        Assert.assertEquals(actResponseMessage, expMessage);
    }

    @Test()
    public void wrongEnergyQuantity() {
        Response buyNuclearRes = given().
                accept(ContentType.JSON).
                when().
                put("/buy/5/-12");
        buyNuclearRes.prettyPeek().then().statusCode(404);
        String actResponseMessage = buyNuclearRes.body().jsonPath().get("message");
        String expMessage = "Not Found";
        Assert.assertEquals(actResponseMessage, expMessage);
    }

    @Test()
    public void strEnergyIdentifier() {
        Response buyNuclearRes = given().
                accept(ContentType.JSON).
                when().
                put("/buy/test/12");
        buyNuclearRes.prettyPeek().then().statusCode(404);
        String actResponseMessage = buyNuclearRes.body().jsonPath().get("message");
        String expMessage = "Not Found";
        Assert.assertEquals(actResponseMessage, expMessage);
    }

    @Test()
    public void wrongResourcePath() {
        Response buyNuclearRes = given().
                accept(ContentType.JSON).
                when().
                put("/buyy/test/12");
        buyNuclearRes.prettyPeek().then().statusCode(404);
        String actResponseMessage = buyNuclearRes.body().jsonPath().get("message");
        String expMessage = "Not Found";
        Assert.assertEquals(actResponseMessage, expMessage);
    }
}
