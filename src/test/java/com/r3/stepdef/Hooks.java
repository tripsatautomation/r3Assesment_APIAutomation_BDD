package com.r3.stepdef;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    private CloseableHttpClient httpClient;
    private CloseableHttpResponse response;
    private String baseUrl; // To store the base URL

    @Before
    public void setUpApi() {
        // Initialize HTTP Client
        httpClient = HttpClients.createDefault();
        System.out.println("HTTP Client initialized for API testing");

        // Setup base URL
        this.baseUrl = "https://open.er-api.com/v6/latest/USD"; // Assuming this is the base URL for API testing
        System.out.println("Base URL set: " + baseUrl);
    }

    @After
    public void tearDownApi() {
        // Ensure response is not null before attempting to close it
        if (response != null) {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Ensure httpClient is not null before attempting to close it
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("HTTP Client and Response closed for API testing");
    }


    // Getter for the HTTP client to be used in the step definitions
    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    // Getter for the base URL
    public String getBaseUrl() {
        return baseUrl;
    }

    // Setter for the response to be used in the Hooks to close it properly
    public void setResponse(CloseableHttpResponse response) {
        this.response = response;
    }
  
   
}
