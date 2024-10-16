package Milestone_3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RetrieveProductDetailTest {

    @Test
    public void retrieveProductDetail()
    {

        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        String randomEmail = RandomEmailGenerator.generateRandomEmail();
        String requestbody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response1 = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestbody)
                .when().post("/api/auth/signup");

        assertThat(response1.getStatusCode(), equalTo(201));

        JsonPath jsonpath1 = response1.jsonPath();
        String accessToken = jsonpath1.getString("data.session.access_token");

        Response response2 = RestAssured.given()
                .header("Authorization", "Bearer "+accessToken)
                .when().get("/api/products/");

        assertThat(response2.getStatusCode(), equalTo(200));
        JsonPath jsonPath2 = response2.jsonPath();
        String product_id = jsonPath2.getString("products[0].id");

        Response response3 = RestAssured.given()
                .header("Authorization", "Bearer "+accessToken)
                .when().get("/api/products/"+product_id);

        assertThat(response3.getStatusCode(), equalTo(200));
        JsonPath jsonpath3 = response3.jsonPath();
        System.out.println(response3.asString());
        assertThat(jsonpath3.getString("product.id"), equalTo(product_id));


    }
}
