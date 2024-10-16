package Milestone_3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.utilities.PropertyUtils;
import org.example.utilities.RandomEmailGenerator;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CartOperationsAddAndUpdateCartItemTest {

    @Test
    public void cartOperationsAddAndUpdateCartItemTest()
    {
        // Read base URL from the property file
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // User Signup
        Response signupResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Step 2: List Products
        Response productsResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/products/");

        // Step 3: Create Cart
        Response cartCreationResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .post("/api/cart");

        String cartId = cartCreationResponse.jsonPath().getString("cart_id");

        // Step 4: Add Item to Cart and Update It
        String product1Id = productsResponse.jsonPath().getString("[0].id");
        String product2Id = productsResponse.jsonPath().getString("[1].id");

        Response addItemResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"product_id\": \"" + product1Id + "\", \"quantity\": 10}")
                .post("/api/cart/" + cartId + "/items");

        String cartItemId = addItemResponse.jsonPath().getString("cart_item_id");

        Response updateItemResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"product_id\": \"" + product2Id +"\", \"quantity\": 20}")
                .put("/api/cart/" + cartId + "/items/" + cartItemId);

        System.out.println(updateItemResponse.asString());
        assertThat(updateItemResponse.jsonPath().getString("product_id"), equalTo(product2Id));
        //assertThat(updateItemResponse.jsonPath().getString("quantity"), equalTo("20"));








    }
}
