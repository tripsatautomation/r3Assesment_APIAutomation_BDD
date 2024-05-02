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

public class CheckUSDPriceAgainstAEDAndPriceRangeCorrectness {

 

	private Hooks hooks; 

	private CloseableHttpClient httpClient = HttpClients.createDefault();
	 
	private double exchangeRate;

	public CheckUSDPriceAgainstAEDAndPriceRangeCorrectness(Hooks hooks) {
		this.hooks = hooks;
	}

 

	@When("I fetch the exchange rate for {string} to {string}")
	public void i_fetch_the_exchange_rate_for_to(String fromCurrency, String toCurrency) throws Exception {
		String fullUrl = hooks.getBaseUrl()  + "?base=" + fromCurrency + "&symbols=" + toCurrency;
		System.out.println("-------------------------------");
		System.out.println(fullUrl);
		HttpGet request = new HttpGet(fullUrl);
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject responseData = new JSONObject(jsonResponse);
			JSONObject rates = responseData.getJSONObject("rates");
			this.exchangeRate = rates.getDouble(toCurrency);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch or parse exchange rate data.");
		}
	}

	@Then("the exchange rate should be between {double} and {double}")
	public void the_exchange_rate_should_be_between_and(Double lowerBound, Double upperBound) {
		Assert.assertTrue(exchangeRate >= lowerBound && exchangeRate <= upperBound, "The exchange rate of "
				+ exchangeRate + " is not within the range " + lowerBound + " to " + upperBound);
	}

}
