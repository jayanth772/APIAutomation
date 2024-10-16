package Milestone_3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class CartOperationsTest {

    public void cartOperationsCreationAndDeletion()
    {
        String randomEmail = RandomEmailGenerator.generateRandomEmail();
        System.out.println(randomEmail);
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");
        String requestbody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response1 =RestAssured.given()
                .contentType(ContentType.JSON).body(requestbody)
                .when().post("/api/auth/signup");

        assertThat(response1.getStatusCode(), Matchers.equalTo(201));
        JsonPath jsonpath = response1.jsonPath();
        String accessToken = jsonpath.getString("data.session.access_token");


        Response response2 = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .when()
                .post("/api/cart");

       JsonPath jsonPath2 = response2.jsonPath();
        System.out.println(response2.getStatusCode());
       String cart_id = jsonPath2.getString("cart_id");

       Response response3 = RestAssured.given()
               .header("Authorization", "Bearer "+accessToken)
               .when()
               .delete("/api/cart/"+cart_id);

       System.out.println(response3.getStatusCode());
        //As these request returning cart_id(which is deleted), message so it is giving 200 code
       assertThat(response3.getStatusCode(), Matchers.equalTo(200));
    }
}
