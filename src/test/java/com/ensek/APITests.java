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

import static io.restassured.RestAssured.*;

public class APITests {

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

    @Test
    public void buyElectric() {
        int electricQuantity = 12;
        Response buyElectricRes = given().
                accept(ContentType.JSON).
                when().
                put("/buy/" + energyObj.getElectric().getEnergy_id() + "/" + electricQuantity);
        buyElectricRes.prettyPeek().then().statusCode(200);
        String actResponseMessage = buyElectricRes.body().jsonPath().get("message");
        actResponseMessage = actResponseMessage.replace("\u00a0", " ");
        String expOrderId = Helpers.extractOrderID(actResponseMessage);
        //Get Orders Response
        Response ordersRes = given().
                accept(ContentType.JSON).
                when().
                get("/orders");
        ordersRes.then().statusCode(200);
        List<Orders> ordersList = ordersRes.jsonPath().getList("$", Orders.class);
        //Validations against Orders Response
        Orders expOrder = null;
        for (Orders order : ordersList) {
            String orderId = order.getId() == null ? order.getid() : order.getId();
            if (orderId.equals(expOrderId)) {
                expOrder = order;
                break;
            }
        }
        Assert.assertNotNull(expOrder, "Order with id " + expOrderId + " not found in the Orders Response");
        double cost = energyObj.getElectric().getPrice_per_unit() * electricQuantity;
        int unitsRemaining = energyObj.getElectric().getQuantity_of_units() - electricQuantity;
        String expMessage = "You have purchased " + expOrder.getQuantity() + " kWh at a cost of " + cost + " there are " + unitsRemaining + " units remaining. Your order id is " + expOrderId + ".";
        Assert.assertEquals(actResponseMessage, expMessage);
    }

    @Test()
    public void buyNuclear() {
        int oilQuantity = 10;
        Response buyNuclearRes = given().
                accept(ContentType.JSON).
                when().
                put("/buy/" + energyObj.getNuclear().getEnergy_id() + "/" + oilQuantity);
        buyNuclearRes.prettyPeek().then().statusCode(200);
        String actResponseMessage = buyNuclearRes.body().jsonPath().get("message");
        String expMessage = "There is no nuclear fuel to purchase!";
        Assert.assertEquals(actResponseMessage, expMessage);
    }

    @Test
    public void buyGas() {
        int oilQuantity = 10;
        Response buyGasRes = given().
                accept(ContentType.JSON).
                when().
                put("/buy/" + energyObj.getGas().getEnergy_id() + "/" + oilQuantity);
        buyGasRes.prettyPeek().then().statusCode(200);
        String actResponseMessage = buyGasRes.body().jsonPath().get("message");
        String expOrderId = Helpers.extractOrderID(actResponseMessage);
        //Get Orders Response
        Response ordersRes = given().
                accept(ContentType.JSON).
                when().
                get("/orders");
        ordersRes.then().statusCode(200);
        List<Orders> ordersList = ordersRes.jsonPath().getList("$", Orders.class);
        //Validations against Orders Response
        Orders expOrder = null;
        for (Orders order : ordersList) {
            String orderId = order.getId() == null ? order.getid() : order.getId();
            if (orderId.equals(expOrderId)) {
                expOrder = order;
                break;
            }
        }
        Assert.assertNotNull(expOrder, "Order with id " + expOrderId + " not found in the Orders Response");
        double cost = energyObj.getGas().getPrice_per_unit() * oilQuantity;
        int unitsRemaining = energyObj.getGas().getQuantity_of_units() - oilQuantity;
        String expMessage = "You have purchased " + expOrder.getQuantity() + " m³ at a cost of " + cost + " there are " + unitsRemaining + " units remaining. Your orderid is " + expOrderId + ".";
        Assert.assertEquals(actResponseMessage, expMessage);
    }

    @Test
    public void buyOil() {
        int oilQuantity = 10;
        Response buyElectricRes = given().
                accept(ContentType.JSON).
                when().
                put("/buy/" + energyObj.getOil().getEnergy_id() + "/" + oilQuantity);
        buyElectricRes.prettyPeek().then().statusCode(200);
        String actResponseMessage = buyElectricRes.body().jsonPath().get("message");
        String expOrderId = Helpers.extractOrderID(actResponseMessage);
        //Get Orders Response
        Response ordersRes = given().
                accept(ContentType.JSON).
                when().
                get("/orders");
        ordersRes.then().statusCode(200);
        List<Orders> ordersList = ordersRes.jsonPath().getList("$", Orders.class);
        //Validations against Orders Response
        Orders expOrder = null;
        for (Orders order : ordersList) {
            String orderId = order.getId() == null ? order.getid() : order.getId();
            if (orderId.equals(expOrderId)) {
                expOrder = order;
                break;
            }
        }
        Assert.assertNotNull(expOrder, "Order with id " + expOrderId + " not found in the Orders Response");
        double cost = energyObj.getOil().getPrice_per_unit() * oilQuantity;
        int unitsRemaining = energyObj.getOil().getQuantity_of_units() - oilQuantity;
        String expMessage = "You have purchased " + expOrder.getQuantity() + " Litres at a cost of " + cost + " there are " + unitsRemaining + " units remaining. Your orderid is " + expOrderId + ".";
        Assert.assertEquals(actResponseMessage, expMessage);
    }

    @Test
    public void verifyFebOrders() {
        //Get Orders Response
        Response ordersRes = given().
                accept(ContentType.JSON).
                when().
                get("/orders");
        ordersRes.then().statusCode(200);
        List<Orders> ordersList = ordersRes.jsonPath().getList("$", Orders.class);
        int febOrderCount = 0;
        for (Orders order : ordersList) {
            if (order.getTime().contains("Feb")) {
                febOrderCount = febOrderCount + 1;
            }
        }
        Assert.assertEquals(febOrderCount, 2, "Feb orders count mismatched");
    }
}
