package com.trailblazers.freewheelers.apis;

import com.google.common.base.Function;
import com.trailblazers.freewheelers.helpers.HomeTable;
import com.trailblazers.freewheelers.helpers.ManageItemTable;
import com.trailblazers.freewheelers.helpers.OrderTable;
import com.trailblazers.freewheelers.helpers.URLs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static com.trailblazers.freewheelers.helpers.Controls.*;

public class UserApi {

    private final String mainPageWindowHandle;
    private WebDriver driver;

    public UserApi(WebDriver driver) {
        this.driver = driver;
        this.mainPageWindowHandle = driver.getWindowHandle();
    }

    public UserApi is_logged_out() {
        driver.get(URLs.logout());
        driver.findElement(By.linkText("Logout")).click();
        return this;
    }

    public UserApi logs_in_with(String userName, String userPassword) {
        driver.get(URLs.login());

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.name("j_username")));
        driver.findElement(By.name("j_username")).sendKeys(userName);
        driver.findElement(By.name("j_password")).sendKeys(userPassword);

        driver.findElement(By.name("submit")).click();

        return this;
    }

    public UserApi creates_an_account(String name, String email, String password, String phoneNumber) {
        driver.get(URLs.home());
        driver.findElement(By.linkText("Create Account")).click();

        fillField(driver.findElement(By.id("fld_email")), email);
        fillField(driver.findElement(By.id("fld_password")), password);
        fillField(driver.findElement(By.id("fld_name")), name);
        fillField(driver.findElement(By.id("fld_phoneNumber")), phoneNumber);

        driver.findElement(By.id("createAccount")).click();

        return this;
    }

    public UserApi creates_an_item(String name, String type, String quantity, String price, String description) {
        fillField(driver.findElement(By.id("name")), name);
        fillField(driver.findElement(By.id("price")), price);

        Select select = new Select(driver.findElement(By.id("type")));
        select.selectByVisibleText(type);

        fillField(driver.findElement(By.id("description")), description);
        fillField(driver.findElement(By.id("quantity")), quantity);

        driver.findElement(By.id("createItem")).click();

        return this;
    }

    public UserApi visits_his_profile() {
        driver.findElement(By.linkText("User Profile")).click();
        return this;
    }

    public UserApi visits_admin_profile() {
        driver.findElement(By.linkText("Admin Profile")).click();
        return this;
    }

    public UserApi visits_nps_report_page() {
        driver.findElement(By.linkText("NPS Report")).click();
        return this;
    }

    public UserApi visits_nps_report_page_by_link() {
        driver.get(URLs.surveyReport());
        return this;
    }

    public UserApi visits_profile_for(String name) {
        driver.get(URLs.userProfile() + "/" + encoded(name));
        return this;
    }

    public UserApi visits_home_page() {
        driver.get(URLs.home());
        return this;
    }

    public UserApi wants_to_manage_items() {
        driver.get(URLs.admin());
        driver.findElement(By.id("manageItems")).click();
        return this;
    }

    public UserApi changes_item_name(String from, String to) {
        check(driver.findElement(ManageItemTable.toggleAll()));

        WebElement input = driver.findElement(ManageItemTable.nameFieldFor(from));
        fillField(input, to);

        driver.findElement(By.name("update")).click();

        return this;
    }

    public UserApi delete_item(String itemName) {
        check(driver.findElement(ManageItemTable.checkBoxFor(itemName)));
        driver.findElement(By.name("delete")).click();

        return this;
    }

    public UserApi reserves_item(String name) {
        driver.findElement(HomeTable.reserveButtonFor(name)).click();
        return this;
    }

    public UserApi changes_order_status(String item, String toState) {
        select(toState, driver.findElement(OrderTable.selectFor(item)));
        driver.findElement(OrderTable.saveButtonFor(item)).click();

        return this;
    }

    private String encoded(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public UserApi waits_for_survey_popup() throws InterruptedException {
        waitForSurveyToShow();
        focusOnPopUpWindow();
        return this;
    }

    private void focusOnPopUpWindow() {
        Iterator<String> handleIterator = driver.getWindowHandles().iterator();
        String popupHandle = handleIterator.next();
        popupHandle = popupHandle.equals(mainPageWindowHandle) ? handleIterator.next() : popupHandle;
        driver.switchTo().window(popupHandle);
    }

    private void waitForSurveyToShow() {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(5, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);

        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver)  {
                return driver.getWindowHandles().size() > 1;
            }
        });
    }
}
