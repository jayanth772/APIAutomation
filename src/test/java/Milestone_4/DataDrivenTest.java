package Milestone_4;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.utilities.DataLoader;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class DataDrivenTest {

    @Test
    public void testProfileOperations()
    {
        // Read base URL from the property file
        String baseUrl = PropertyUtils.getProperty("base.url");
        RestAssured.baseURI = baseUrl;

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Signup to get an access token
        Response signupResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Aliasing the profileData fixture
        String firstName = DataLoader.getData("profileData", "first_name");
        String lastName = DataLoader.getData("profileData", "last_name");
        String address = DataLoader.getData("profileData", "address");
        String mobileNumber = DataLoader.getData("profileData", "mobile_number");

        // Create profile
        Response createProfileResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("{\"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\", \"address\": \"" + address + "\", \"mobile_number\": \"" + mobileNumber + "\"}")
                .post("/api/profile");

        //System.out.println(createProfileResponse.asString());
        assertEquals(createProfileResponse.getStatusCode(), 201);
        assertEquals(createProfileResponse.jsonPath().getString("first_name"), firstName);
        assertEquals(createProfileResponse.jsonPath().getString("last_name"), lastName);
        assertEquals(createProfileResponse.jsonPath().getString("mobile_number"), mobileNumber);

        // Update profile
        Response updateProfileResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("{\"first_name\": \"Kane\", \"last_name\": \"Jennier\"}")
                .patch("/api/profile");

        //System.out.println(updateProfileResponse.asString());
        assertEquals(updateProfileResponse.getStatusCode(), 200);
        assertEquals(updateProfileResponse.jsonPath().getString("data.first_name"), "Kane");
        assertEquals(updateProfileResponse.jsonPath().getString("data.last_name"), "Jennier");
        assertEquals(updateProfileResponse.jsonPath().getString("data.mobile_number"), mobileNumber);
    }
}
