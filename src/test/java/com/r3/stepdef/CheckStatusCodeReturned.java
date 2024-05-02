package com.r3.stepdef;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CheckStatusCodeReturned {

    private CloseableHttpClient httpClient;
    private CloseableHttpResponse response;
    private JSONObject responseData;
    private String baseUrl;
    private Hooks hooks; // Instance of Hooks to access httpClient and baseUrl
    
    public CheckStatusCodeReturned(Hooks hooks) {
        this.hooks = hooks;
    }
    
  

    @Given("the base API URL is {string}")
    public void the_base_api_url_is(String url) {
        this.baseUrl = url; // Update the base URL with the one provided by the feature file scenario
    }

    @When("GET request")
    public void get_Request() throws Exception {
    	try {
            HttpGet request = new HttpGet(baseUrl);  // Ensure baseUrl is the correct URL
            response = hooks.getHttpClient().execute(request);
        } catch (UnknownHostException e) {
            System.err.println("Error: Host cannot be resolved - " + baseUrl);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Error during HTTP execution");
            e.printStackTrace();
        }
    }

    @Then("the HTTP response status should be {int}")
    public void the_http_response_status_should_be(Integer expectedStatus) {
        Assert.assertEquals(response.getStatusLine().getStatusCode(), expectedStatus.intValue(),
            "The expected HTTP status does not match the actual status.");
    }

    @Then("the API response should contain a {string} field with the value {string}")
    public void the_api_response_should_contain_a_field_with_the_value(String fieldName, String expectedValue) throws ClientProtocolException, IOException {
    	HttpGet request = new HttpGet(baseUrl);  // Ensure baseUrl is the correct URL
        response = hooks.getHttpClient().execute(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        responseData = new JSONObject(jsonResponse);
     
    	Assert.assertTrue(responseData.has(fieldName),
            "The response does not contain the expected field: " + fieldName);
        Assert.assertEquals(responseData.getString(fieldName), expectedValue,
            "The value of the '" + fieldName + "' field is not as expected.");
    }
    
    @After
    public void tearDown() {
        hooks.setResponse(response); // Set response in Hooks for proper closure
        hooks.tearDownApi(); // Use the tearDown method from Hooks to close resources
    }
}
