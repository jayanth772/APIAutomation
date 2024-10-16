package Milestone_3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class CartDeletionIdempotencyTest {

    @Test
    public void verifyCartDeletionAndIdempotency()
    {
        String randomEmail = RandomEmailGenerator.generateRandomEmail();
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        //Signup call
        Response response1 = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when().
                post("/api/auth/signup");

        JsonPath jsonPath1 = response1.jsonPath();
        String accessToken = jsonPath1.getString("data.session.access_token");

        //Creating the cart
        Response response2 = RestAssured.given()
                .contentType("application/json")
                .header("Authorization","Bearer "+accessToken)
                .when().post("/api/cart");

        JsonPath jsonPath2 = response2.jsonPath();
        assertThat(response2.getStatusCode(),Matchers.equalTo(201));
        String cartId = jsonPath2.getString("cart_id");
        System.out.println(cartId);

        //Deleting the cart first time
        Response response3 = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer "+accessToken)
                .when().delete("/api/cart/" + cartId);

        assertThat(response3.getStatusCode(), Matchers.equalTo(200));
        JsonPath jsonPath3 = response3.jsonPath();
        String cartId_deleted = jsonPath3.getString("cart_id");
        assertThat(cartId_deleted, Matchers.equalTo(cartId));
        assertThat(jsonPath3.getString("message"), Matchers.equalTo("Cart deleted"));

        //Reattempting to delete the cart again
        for(int i=0;i<2;i++) {
            Response response4 = RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .when().delete("/api/cart/" + cartId);

            assertThat(response4.getStatusCode(), Matchers.equalTo(200));
            assertThat(response4.jsonPath().getString("message"), Matchers.equalTo("Cart already deleted"));

        }

    }
}
