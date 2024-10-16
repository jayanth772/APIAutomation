package Milestone_2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserSignUpTest {

    @Test
    public void successfullySignUp()
    {
        // Read base URL from the property file
        String baseURL = PropertyUtils.getProperty("base.url");

        // Set the base URI for REST Assured
        RestAssured.baseURI = baseURL;

        // Create the request body
        // Note: We may get a 401 status code if the email is already registered.
        // For this example, use a random unique email.
        String randomEmail = RandomEmailGenerator.generateRandomEmail();
       // String requestbody = "{\"email\": \"abc772@gmail.com\", \"password\": \"12345678\"}";
        String requestbody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        // Send a POST request and get the response
        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(requestbody).when().
                post("/api/auth/signup");

        // Get and validate the status code from the response
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201, "Status code is not as expected");

        // Validate response body properties
        Assert.assertNotNull(response.jsonPath().get("data"));        // Assert 'data' field is not null
        Assert.assertNotNull(response.jsonPath().get("data.user"));   // Assert 'user' field inside 'data' is not null
        Assert.assertNotNull(response.jsonPath().get("data.session")); // Assert 'session' field inside 'data' is not null

        //Validate the response using Hamcrest assertions
        assertThat(response.getStatusCode(), Matchers.is(201));
        assertThat(response.jsonPath().get("data"),Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.user"), Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.session"), Matchers.notNullValue());


    }
}
