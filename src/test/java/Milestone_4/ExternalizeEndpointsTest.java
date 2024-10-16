package Milestone_4;

import io.restassured.RestAssured;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.utilities.EndpointConfig.getEndpoint;

public class ExternalizeEndpointsTest {

    @Test
    public void testExternalizeEndpoints()
    {
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        given()
                .contentType("application/json")
                .body("{\"email\":\""+randomEmail+"\", \"password\":\"123456789\"}")
                .when().post(getEndpoint("auth","signUp"))
                .then()
                .statusCode(201);
    }
}
