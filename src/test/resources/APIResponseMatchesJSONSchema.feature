

Feature: Currency Exchange Rate API Validation

  Scenario: Validate USD to AED exchange rate is within the specified range
    Given I have the base URL "https://open.er-api.com/v6/latest/USD"
    Then I validate the API response matches the expected JSON schema

