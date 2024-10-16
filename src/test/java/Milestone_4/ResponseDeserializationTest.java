package Milestone_4;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.SignupRequestModel;
import org.example.SignupResponseModel;
import org.example.utilities.ApiResponseDeserializer;
import org.example.utilities.EndpointConfig;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ResponseDeserializationTest {

    @Test
    public void testSignupWithResponseDeserialization(){
        // Reading the base URL from the property file
        String baseUrl = PropertyUtils.getProperty("base.url");
        RestAssured.baseURI = baseUrl;

        // Generating a random email for the test
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Fetching the signup endpoint from the endpoints.json file
        String signUpEndpoint = EndpointConfig.getEndpoint("auth", "signUp");

        // Using Lombok's builder pattern to construct the Signup request model
        SignupRequestModel signupRequestModel = SignupRequestModel.builder()
                .email(randomEmail)
                .password("123456")
                .build();

        // Performing the signup operation
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupRequestModel)
                .post(signUpEndpoint);

        System.out.println("Response Body: " + response.getBody().asString());
        // Deserialize the response to the SignupResponseModel object
        SignupResponseModel signupResponseModel = ApiResponseDeserializer.deserializeResponse(response, SignupResponseModel.class);

        // Verifying that the deserialized status code is as expected and existence of other fields
        assertEquals(signupResponseModel.getStatusCode(), 201);
        //System.out.println(signupResponseModel.getResponseData());
        assertNotNull(signupResponseModel, "SignupResponseModel is null");
        assertNotNull(signupResponseModel.getData(), "ResponseData is null");
        assertNotNull(signupResponseModel.getData().getSession().getAccessToken());

    }
}
