package com.r3.stepdef;

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

public class CheckAPISucessAndValidRates {

    private CloseableHttpClient httpClient;
    private CloseableHttpResponse response;
    private JSONObject responseData;
    private String baseUrl;
    private Hooks hooks; // Instance of Hooks to access httpClient and baseUrl
    
    public CheckAPISucessAndValidRates(Hooks hooks) {
    	 this.hooks = hooks;
    }
    
   
  
    @When("I make a GET request to the endpoint")
    public void i_make_a_get_request_to_the_endpoint() throws Exception {
        System.out.println("Making GET request to URL: " +hooks.getBaseUrl() );
        HttpGet request = new HttpGet(hooks.getBaseUrl());
        response = hooks.getHttpClient().execute(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        responseData = new JSONObject(jsonResponse);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatus) {
        Assert.assertEquals(response.getStatusLine().getStatusCode(), expectedStatus.intValue(), "The response status is incorrect.");
    }

    @Then("the response should contain a valid {string} field")
    public void the_response_should_contain_a_valid_field(String field) {
        Assert.assertTrue(responseData.has(field), "The response does not contain the expected field: " + field);
        Assert.assertNotNull(responseData.get(field), "The field " + field + " should not be null.");
    }

  
    @Then("the {string} field should include at least one valid {string} exchange rate")
    public void the_field_should_include_at_least_one_valid_usd_exchange_rate(String field, String currency) {
        JSONObject rates = responseData.getJSONObject(field);
        Assert.assertTrue(rates.length() > 0, "The rates object is empty.");
        Assert.assertTrue(rates.has(currency), "The rates object does not contain " + currency + ".");
        Assert.assertTrue(rates.getDouble(currency) > 0, "The exchange rate for " + currency + " is not positive.");
    }


    @After
    public void tearDown() {
        hooks.setResponse(response); // Set response in Hooks for proper closure
        hooks.tearDownApi(); // Use the tearDown method from Hooks to close resources
    }
}
