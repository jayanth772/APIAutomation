package Milestone_2;

import io.restassured.RestAssured;
import org.example.utilities.PropertyUtils;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class ValidateProductsNoAuthTest {

    @Test
    public void testGetProductsWithoutAuthHeader()
    {
        //RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        RestAssured.given()
                .baseUri(PropertyUtils.getProperty("base.url"))
                .when().get("/api/products")
                .then().statusCode(400)
                .body("message", equalTo("Authorization header is missing."));
    }
}
