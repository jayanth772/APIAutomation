package Milestone_4;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserProfileUpdateWithPATCH {

    @Test
    public void testUserProfileCreationAndPartialUpdate()
    {
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        Response signUpresponse = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\""+randomEmail+"\", \"password\":\"123456789\"}")
                .when().post("/api/auth/signup");

        assertThat(signUpresponse.getStatusCode(), Matchers.equalTo(201));
        String accessToken  = signUpresponse.jsonPath().getString("data.session.access_token");

        Response profileCreationresponse = given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .body("{\"first_name\": \"James\" , \"last_name\":\"Jenny\", \"address\":\"1st cross, chruch street, London\", \"mobile_number\":\"1234567890\"}")
                .when().post("/api/profile");

        assertThat(profileCreationresponse.getStatusCode(), Matchers.equalTo(201));
        assertThat(profileCreationresponse.jsonPath().getString("first_name"), Matchers.equalTo("James"));

        Response profileUpdatedresponse = given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType("application/json")
                .body("{\"first_name\": \"Jayanth\"}")
                .when().patch("/api/profile");

        assertThat(profileUpdatedresponse.getStatusCode(), Matchers.equalTo(200));
        assertThat(profileUpdatedresponse.jsonPath().getString("message"), Matchers.equalTo("Profile updated successfully"));
        System.out.println(profileUpdatedresponse.jsonPath().getString("field_updated[0]"));


    }
}
