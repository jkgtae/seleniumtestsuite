
## Programming Exercise

Build a Selenium test suite in Java using Selenium WebDriver to complete the following tasks:

1. Log in to Pardot
2. Create a list with a random name (Marketing > Segmentation > Lists)
3. Attempt to create another list with that same name and ensure the system correctly gives a validation failure
4. Rename the original list
5. Ensure the system allows the creation of another list with the original name now that the original list is renamed
6. Create a new prospect (Prospect > Prospect List)
7. Add your new prospect to the newly created list
8. Ensure the new prospect is successfully added to the list upon save
9. Send a text only email to the list (Marketing > Emails)
10. Log out


## Running the Test Suite

Built in Java 1.7 and Maven 2.2.1. Import into your favorite IDE as a Maven project and run SeleniumTestSuite.java.


## Included Tests

|Test Name|Addresses These Steps of Exercise|
|---|---|
|`createListTest()`|2. Create a list with a random name (Marketing > Segmentation > Lists)<br />3. Attempt to create another list with that same name and ensure the system correctly gives a validation failure<br />4. Rename the original list<br />5. Ensure the system allows the creation of another list with the original name now that the original list is renamed|
|`createProspectTest()`|6. Create a new prospect (Prospect > Prospect List)<br />7. Add your new prospect to the newly created list<br />8. Ensure the new prospect is successfully added to the list upon save|
|`sendEmailToListTest()`|9. Send a text only email to the list (Marketing > Emails)|

Step 1 (log in) and Step 10 (log out) are performed in methods denoted by the `@BeforeClass` and `@AfterClass` annotations. Tests must be run in the order shown above, which is accomplished via the `@FixMethodOrder` annotation.


## Helper Methods

Several helper methods are included to DRY up the code for tasks that must be performed more than once (e.g., `createList()`), or to improve test readability by separating out a series of actions related to a single task (e.g., `navigateToNewEmailPage()`).