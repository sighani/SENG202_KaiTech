Feature: Data feature.
    Some scenarios involving data upload, visualisation, and analysis.

    Scenario: Upload XML file of data (AT-09)
        Given The user has an XML file containing specific data
        When The user chooses to upload a specific type of data and selects the file
        Then The data is uploaded and displayed on screen for confirmation

    Scenario: Add new supplier (AT-11)
        Given The user is on the main menu and has details of a new supplier
        When The user chooses to manually enter data and selects "Add supplier" from the list
        Then The user is prompted to enter details of a new supplier