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

public class EnhancedUserSignupTest {

    @Test
    public void successfullySignupUser(){
        
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        String randomEmail = RandomEmailGenerator.generateRandomEmail();
        String requestbody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestbody)
                .when().post("/api/auth/signup");

        int responseCode = response.getStatusCode();
        JsonPath jsonPath = response.jsonPath();

        assertThat(jsonPath.getString("data.user.email"), Matchers.is(randomEmail));
        assertThat(jsonPath.getString("data.session.token_type"), Matchers.is("bearer"));
        assertThat(jsonPath.getString("data.session.refresh_token"), Matchers.notNullValue());
        assertThat(jsonPath.getString("data.user.id"), Matchers.equalTo(jsonPath.getString("data.session.user.id")));
        assertThat(jsonPath.getList("data.user.app_metadata.providers"), Matchers.contains("email"));
        assertThat(response.jsonPath().getString("data.user.aud"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.role"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.created_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));
        assertThat(response.jsonPath().getString("data.user.updated_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));

    }
}
