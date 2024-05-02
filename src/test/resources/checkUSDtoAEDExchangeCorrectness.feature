

Feature: Verify USD to AED Exchange Rate

  Background:
    Given the base API URL is "https://open.er-api.com/v6/latest/USD"

  Scenario: USD to AED exchange rate should be within the specified range
    When I fetch the exchange rate for "USD" to "AED"
    Then the exchange rate should be between 3.6 and 3.7

