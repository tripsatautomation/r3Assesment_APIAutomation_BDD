package com.r3.stepdef;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Check162CurrencyPairsReturned {

    private CloseableHttpResponse response;
    private JSONObject responseData;

    private Hooks hooks; // Instance of Hooks to access httpClient and baseUrl

    private CloseableHttpClient httpClient;
    private String baseUrl;

    public Check162CurrencyPairsReturned(Hooks hooks) {
        this.hooks = hooks;
        this.httpClient = hooks.getHttpClient(); // Use httpClient from Hooks
        this.baseUrl = hooks.getBaseUrl(); // Use baseUrl from Hooks
    }

 

    @When("I request the currency pairs from the API")
    public void i_request_the_currency_pairs_from_the_api() throws Exception {
        try {
            HttpGet request = new HttpGet(baseUrl); // Ensure baseUrl is the complete URL
            response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            responseData = new JSONObject(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to make API request or parse response", e);
        }
    }

    @Then("the API should return exactly {int} currency pairs")
    public void the_api_should_return_exactly_currency_pairs(Integer expectedCount) {
        if (responseData == null) {
            throw new IllegalStateException("Response data is null, cannot perform check on currency pairs.");
        }
        JSONObject rates = responseData.getJSONObject("rates");
        int count = rates.length(); // Assuming each key in 'rates' represents a currency pair
        Assert.assertEquals(count, expectedCount.intValue(), "The number of currency pairs returned does not match the expected count.");
    }

    @After
    public void tearDown() {
        if (response != null) {
            try {
                response.close();
            } catch (Exception e) {
                System.err.println("Error closing the HTTP response: " + e.getMessage());
            }
        }
    }
}
