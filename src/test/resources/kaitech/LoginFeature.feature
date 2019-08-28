Feature: Login feature.
    Some scenarios involving login.
    
    Scenario: Managerial login
        Given The user attempts to access a feature restricted to management personnel
        When The user inputs the correct pin into the popup
        Then The user is logged in as a manager and can perform executive tasks such as uploading new data