
Feature: Verify Number of Currency Pairs Returned by API

  Background:
    Given the base API URL is "https://open.er-api.com/v6/latest/USD"

  Scenario: API returns exactly 162 currency pairs
    When I request the currency pairs from the API
    Then the API should return exactly 162 currency pairs
