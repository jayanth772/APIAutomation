package Milestone_3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class ValidateAPIResponseAttributesTest {

    @Test
    public void validateAPIResponseAttributes()
    {
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        RestAssured.baseURI = PropertyUtils.getProperty("base.url");
        String requestbody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response =RestAssured.given()
                .contentType(ContentType.JSON).body(requestbody)
                .when().post("/api/auth/signup");

        JsonPath jsonpath = response.jsonPath();
        String accessToken = jsonpath.getString("data.session.access_token");

        Response response1 = RestAssured.given()
                .header("Authorization", "Bearer "+accessToken)
                .when().get("/api/products");

        Headers responseHeaders = response1.getHeaders();
        JsonPath jsonPath = response1.jsonPath();

        //Validate the Content-type header in the response
        assertThat(responseHeaders.getValue("Content-Type"), Matchers.equalTo("application/json; charset=utf-8"));


        //Validate response time
        assertThat(response1.getTime(), Matchers.lessThan(3000L));


        //Validate the status and statusText
        assertThat(response1.getStatusCode(),Matchers.equalTo(200));
        assertThat(response1.getStatusLine(),Matchers.containsString("OK"));



    }
}
