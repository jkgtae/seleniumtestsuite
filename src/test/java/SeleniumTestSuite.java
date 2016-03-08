import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.interactions.Actions;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
@FixMethodOrder(MethodSorters.JVM)

public class SeleniumTestSuite {
    public static FirefoxDriver driver;
    public static String listName1;
    public static String listName2;
    public static String prospectEmail;
    public static String emailName;

    // Helper methods
    public static void createList(String listName) {
        // Click on 'Add List' button
        driver.findElement(By.id("listxistx_link_create")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // Fill in random list name
        driver.findElement(By.id("name")).sendKeys(listName);

        // Click on 'Create List' button
        driver.findElement(By.id("save_information")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public static void createProspect(String email, String campaign, String profile) {
        // Click on 'Add Prospect' button
        driver.findElement(By.id("pr_link_create")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // Fill in email field
        driver.findElement(By.id("email")).sendKeys(email);

        // Fill in campaign field
        Select campaignDropDown = new Select(driver.findElement(By.id("campaign_id")));
        campaignDropDown.selectByVisibleText(campaign);

        // Fill in profile field
        Select profileDropDown = new Select(driver.findElement(By.id("profile_id")));
        profileDropDown.selectByVisibleText(profile);

        // Click on 'Create prospect' button
        driver.findElement(By.name("commit")).submit();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public static void navigateToListsPage() {
        // Save reference to current page (to be used later to ensure new page has loaded)
        WebElement old_page = driver.findElementByTagName("html");

        // Navigate to Marketing > Segmentation > Lists
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Move to the Marketing menu and hover
        actions.moveToElement(driver.findElement(By.xpath("//*[@id=\"sidebar-navigation\"]/ul/li[1]"))).perform();

        // Move to the Segmentation menu and hover
        WebElement segmentation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Segmentation")));
        actions.moveToElement(segmentation).perform();

        // Move to the Lists item and click
        WebElement lists = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Lists")));
        actions.moveToElement(lists).click().perform();

        // Wait for page load
        wait.until(ExpectedConditions.stalenessOf(old_page));
    }

    public static void navigateToProspectsPage() {
        // Save reference to current page (to be used later to ensure new page has loaded)
        WebElement old_page = driver.findElementByTagName("html");

        // Navigate to Prospects > Prospect List
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Move to the Prospects menu and hover
        actions.moveToElement(driver.findElement(By.xpath("//*[@id=\"sidebar-navigation\"]/ul/li[2]"))).perform();

        // Move to the Prospect Lists item and click
        WebElement lists = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Prospect List")));
        actions.moveToElement(lists).click().perform();

        // Wait for page load
        wait.until(ExpectedConditions.stalenessOf(old_page));
    }

    public static void navigateToNewEmailPage() {
        // Save reference to current page (to be used later to ensure new page has loaded)
        WebElement old_page = driver.findElementByTagName("html");

        // Navigate to Marketing > Segmentation > Lists
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Move to the Marketing menu and hover
        actions.moveToElement(driver.findElement(By.xpath("//*[@id=\"sidebar-navigation\"]/ul/li[1]"))).perform();

        // Move to the Segmentation menu and hover
        WebElement segmentation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Emails")));
        actions.moveToElement(segmentation).perform();

        // Move to the Lists item and click
        WebElement lists = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("New Email")));
        actions.moveToElement(lists).click().perform();

        // Wait for page load
        wait.until(ExpectedConditions.stalenessOf(old_page));
    }

    @BeforeClass
    public static void setUpTest() {
        // Create driver
        driver = new FirefoxDriver();

        // Navigate to login URL
        driver.get("https://pi.pardot.com");

        // Enter email address and password
        driver.findElement(By.id("email_address")).sendKeys("pardot.applicant@pardot.com");
        driver.findElement(By.id("password")).sendKeys("Applicant2012");

        // Click on 'Log in' button
        driver.findElement(By.name("commit")).click();

        // Generate random names for lists, prospects, etc. (e.g., "jk_List_0723")
        Random rand = new Random();
        listName1 = "jk_List_" + String.format("%04d", rand.nextInt(1001));
        listName2 = "jk_List_" + String.format("%04d", rand.nextInt(1001));
        prospectEmail = "jk_Prospect_" + String.format("%04d", rand.nextInt(1001)) + "@gmail.com";
        emailName = "jk_Email_" + String.format("%04d", rand.nextInt(1001));
    }

    @Test
    public void createListTest() {
        // Create list
        navigateToListsPage();
        createList(listName1);

        // Ensure list has been created
        navigateToListsPage();
        assertTrue(driver.getPageSource().contains(listName1)); // depends on list displaying the most recently added lists first (current default behavior)

        // Attempt to create another list with same name
        createList(listName1);

        // Ensure the system correctly gives a validation failure
        assertTrue(driver.getPageSource().contains("Please correct the errors below and re-submit"));
        driver.findElement(By.linkText("Cancel")).click(); // click on 'Cancel' button
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // Click on original list
        WebElement old_page = driver.findElementByTagName("html"); // save reference to current page
        driver.findElement(By.linkText(listName1)).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(old_page)); // wait for new page to load

        // Update list name
        driver.findElement(By.linkText("Edit")).click(); // click on 'Edit' button
        WebElement nameField = driver.findElement(By.id("name"));
        nameField.clear();
        nameField.sendKeys(listName2);
        driver.findElement(By.id("save_information")).click(); // click on 'Update List' button

        // Ensure list has been renamed
        navigateToListsPage();
        assertTrue(driver.getPageSource().contains(listName2));

        // Attempt to create another list with the original name
        createList(listName1);

        // Ensure list has been created
        navigateToListsPage();
        assertTrue(driver.getPageSource().contains(listName1));
    }

    @Test
    public void createProspectTest() {
        // Create prospect
        navigateToProspectsPage();
        createProspect(prospectEmail, "Email Template test", "Default");

        // Navigate to prospect's 'Lists' page
        WebElement old_page = driver.findElementByTagName("html"); // save reference to current page
        driver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/div/div/div/ul/li[2]/a")).click(); // click on 'Lists' link
        new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(old_page)); // wait for new page to load

        // Add prospect to second list
        driver.findElement(By.className("chzn-container")).click(); // click on 'Select list to add...'
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement listFilter = driver.findElement(By.cssSelector(".chzn-search > input:nth-child(1)"));
        listFilter.sendKeys(listName2);
        listFilter.sendKeys(Keys.RETURN);
        driver.findElement(By.name("commit")).submit();

        // Ensure prospect is successfully added to list
        old_page = driver.findElementByTagName("html"); // save reference to current page
        driver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/div/div/div/ul/li[2]/a")).click(); // click on 'Lists' link
        new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(old_page)); // wait for new page to load
        assertTrue(driver.getPageSource().contains(listName2));
    }

    @Test
    public void sendEmailToListTest() {
        // Create new email
        navigateToNewEmailPage();
        driver.findElement(By.id("name")).sendKeys(emailName); // add email Name

        // Choose arbitrary campaign
        driver.findElement(By.className("object-name")).click(); // click to pop up list of email campaigns
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("asset-chooser-app-modal")));
        Actions actions = new Actions(driver);
        WebElement container = driver.findElement(By.id("asset-chooser-app-modal"));
        actions.moveToElement(container, 475, 325).click().build().perform(); // select arbitrary campaign
        driver.findElement(By.id("select-asset")).click(); // click 'Choose Selected' button

        // Finish creating text-only email
        driver.findElement(By.id("email_type_text_only")).click(); // select text-only radio button
        driver.findElement(By.id("save_information")).click(); // click 'Save' button
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.id("cancel_template")).click(); // click 'Cancel' button (to avoid selecting a template)
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Send email to second list
        driver.findElement(By.id("flow_sending")).click(); // click on 'Sending'
        driver.findElement(By.className("chzn-container")).click(); // click on 'Select list to add...'
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement emailListFilter = driver.findElement(By.cssSelector(".chzn-search > input:nth-child(1)")); // search for second list
        emailListFilter.sendKeys(listName2); // select second list
        emailListFilter.sendKeys(Keys.RETURN);
        driver.findElement(By.name("a_sender[]")).click(); // click on Sender dropdown
        Select senderDropDown = new Select(driver.findElement(By.name("a_sender[]")));
        senderDropDown.selectByVisibleText("General User"); // select 'General User' for Sender
        driver.findElement(By.id("subject_a")).sendKeys("Test"); // fill in Subject Line field
        driver.findElement(By.id("save_footer")).click(); // click on 'Save' button (if email was enabled, would click on 'Send Now' button instead)
    }

    @AfterClass
    public static void logOut() {
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Move to the Account icon and hover
        actions.moveToElement(driver.findElement(By.id("acct-tog"))).perform();

        // Move to the Sign Out item and click
        WebElement lists = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Sign Out")));
        actions.moveToElement(lists).click().perform();

        // Quit Firefox
//        driver.quit();
    }
}