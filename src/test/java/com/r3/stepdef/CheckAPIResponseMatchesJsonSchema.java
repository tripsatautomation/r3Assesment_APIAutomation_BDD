package com.r3.stepdef;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class CheckAPIResponseMatchesJsonSchema {

	 private CloseableHttpResponse response;
	    private JSONObject responseData;

	    private Hooks hooks; // Instance of Hooks to access httpClient and baseUrl

	    private CloseableHttpClient httpClient;
	    private String baseUrl;

	    public CheckAPIResponseMatchesJsonSchema(Hooks hooks) {
	        this.hooks = hooks;
	        this.httpClient = hooks.getHttpClient(); // Use httpClient from Hooks
	        this.baseUrl = hooks.getBaseUrl(); // Use baseUrl from Hooks
	    }

	    @Given("I have the base URL {string}")
	    public void i_have_the_base_url(String string) {
	    	this.baseUrl = hooks.getBaseUrl();
	    }

	  

	    @Then("I validate the API response matches the expected JSON schema")
	    public void i_validate_the_api_response_matches_the_expected_json_schema() throws ClientProtocolException, IOException {
	    	String schemaAsString = "{"
	    		    + "\"$schema\": \"http://json-schema.org/draft-07/schema#\","
	    		    + "\"type\": \"object\","
	    		    + "\"required\": ["
	    		    + "    \"result\","
	    		    + "    \"provider\","
	    		    + "    \"documentation\","
	    		    + "    \"terms_of_use\","
	    		    + "    \"time_last_update_unix\","
	    		    + "    \"time_last_update_utc\","
	    		    + "    \"time_next_update_unix\","
	    		    + "    \"time_next_update_utc\","
	    		    + "    \"time_eol_unix\","
	    		    + "    \"base_code\","
	    		    + "    \"rates\""
	    		    + "],"
	    		    + "\"properties\": {"
	    		    + "    \"result\": {\"type\": \"string\"},"
	    		    + "    \"provider\": {\"type\": \"string\"},"
	    		    + "    \"documentation\": {\"type\": \"string\"},"
	    		    + "    \"terms_of_use\": {\"type\": \"string\"},"
	    		    + "    \"time_last_update_unix\": {\"type\": \"integer\"},"
	    		    + "    \"time_last_update_utc\": {\"type\": \"string\", \"format\": \"date-time\"},"
	    		    + "    \"time_next_update_unix\": {\"type\": \"integer\"},"
	    		    + "    \"time_next_update_utc\": {\"type\": \"string\", \"format\": \"date-time\"},"
	    		    + "    \"time_eol_unix\": {\"type\": \"integer\"},"
	    		    + "    \"base_code\": {\"type\": \"string\"},"
	    		    + "    \"rates\": {"
	    		    + "        \"type\": \"object\","
	    		    + "        \"additionalProperties\": { \"type\": \"number\" }"
	    		    + "    }"
	    		    + "}"
	    		+ "}";

	    	
	    	 try {
	    		  HttpGet request = new HttpGet(baseUrl); // Ensure baseUrl is the complete URL
	              response = httpClient.execute(request);
	              String json = EntityUtils.toString(response.getEntity());
	              responseData = new JSONObject(json);
	              
	              JSONObject jsonSchema = new JSONObject(schemaAsString);
	              JSONObject jsonResponse = new JSONObject(responseData); // your API response as a JSON object

	              Schema schema = SchemaLoader.load(jsonSchema);
	              schema.validate(jsonResponse);  // Validates the response against the schema
	    	    } catch (JSONException e) {
	    	        throw new RuntimeException("JSON parsing error: " + e.getMessage(), e);
	    	    } catch (ValidationException e) {
	    	    	  // Log detailed error messages
	    	        e.getCausingExceptions().stream()
	    	            .map(ValidationException::getMessage)
	    	            .forEach(System.out::println);
	    	        throw new RuntimeException("Schema validation error: " + e.getMessage(), e);
	    	    }
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
