package Milestone_2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductsPaginatedTest {

    @Test
    public void testGetPaginatedProducts()
    {
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        RestAssured.baseURI = PropertyUtils.getProperty("base.url");
        String requestbody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response =RestAssured.given()
                .contentType(ContentType.JSON).body(requestbody)
                .when().post("/api/auth/signup");

        JsonPath jsonpath = response.jsonPath();
        String accessToken = jsonpath.getString("data.session.access_token");

        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("limit",2)
                .queryParam("page",1)
                .when().get("/api/products/")
                .then()
                .statusCode(200)
                .body("products.size()", Matchers.equalTo(2));

//        // Make a GET request with pagination
//        Response productsResponse = RestAssured.given()
//                .header("Authorization", "Bearer " + accessToken)
//                .queryParam("limit",2)
//                .queryParam("page", 1)
//                .get("/api/products/");
//
//        // Validate the response
//        JsonPath jsonPath = productsResponse.jsonPath();
//        int productCount = jsonPath.getList("products").size();
//
//        // Validating the status code
//        assertThat(productsResponse.getStatusCode(), Matchers.equalTo(200));
//        // Validating the length of returned products array with limited value
//        assertThat(productCount, Matchers.equalTo(2));




    }
}
