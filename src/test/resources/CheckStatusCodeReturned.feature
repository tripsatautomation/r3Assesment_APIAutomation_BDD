
Feature: Verify API Response Status Codes and Application Statuses

  Scenario Outline: API call returns various application-specific statuses
    Given the base API URL is "https://open.er-api.com/v6/latest/USD"
    When GET request
    Then the HTTP response status should be <http_status>
    And the API response should contain a "result" field with the value "<app_status>"

    Examples: 
      | http_status | app_status |
      |         200 | success    |
     # |         404 | failure      |
      #|         400 | bad request  |
      #|         500 | server error |
