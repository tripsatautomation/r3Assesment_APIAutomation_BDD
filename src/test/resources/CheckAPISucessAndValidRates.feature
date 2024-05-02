
 
 Feature: Verify API response and data validity

  Background:
    Given the base API URL is "https://open.er-api.com/v6/latest/USD"

  Scenario: API call is successful and returns valid exchange rate data
    When I make a GET request to the endpoint
    Then the response status should be 200
    And the response should contain a valid "rates" field
    And the "rates" field should include at least one valid "USD" exchange rate
 