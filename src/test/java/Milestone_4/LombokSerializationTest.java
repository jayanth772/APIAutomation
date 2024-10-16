package Milestone_4;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.utilities.EndpointConfig;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.example.SignupRequestModel;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class LombokSerializationTest {

    @Test
    public void testSignupWithLombokSerialization()
    {
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        String signUpEndPoint = EndpointConfig.getEndpoint("auth", "signUp");

        SignupRequestModel signupRequestModel = SignupRequestModel.builder()
                .email(randomEmail)
                .password("123456")
                .build();

        Response signUpResponse = given()
                .contentType("application/json")
                .body(signupRequestModel)
                .when().post(signUpEndPoint);

        assertThat(signUpResponse.getStatusCode(), Matchers.equalTo(201));

    }
}
