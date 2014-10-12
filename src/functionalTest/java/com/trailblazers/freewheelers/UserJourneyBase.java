package com.trailblazers.freewheelers;

import com.trailblazers.freewheelers.apis.AdminApi;
import com.trailblazers.freewheelers.apis.ScreenApi;
import com.trailblazers.freewheelers.apis.UserApi;
import com.trailblazers.freewheelers.pages.NetPromoterScoreReportPage;
import com.trailblazers.freewheelers.pages.NetPromoterScoreSurveyForm;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class UserJourneyBase {

    private static WebDriver driver;

    protected static AdminApi admin;
    protected static UserApi user;
    protected static ScreenApi screen;

    // Specific for NPS Flow
    protected static NetPromoterScoreSurveyForm npsSurveyForm;
    protected static NetPromoterScoreReportPage npsReportPage;

    @BeforeClass
    public static void before() {
        driver = new FirefoxDriver();

        admin = new AdminApi();
        user = new UserApi(driver);
        screen = new ScreenApi(driver);

        // NPS Pages
        npsSurveyForm = new NetPromoterScoreSurveyForm(driver);
        npsReportPage = new NetPromoterScoreReportPage(driver);
    }

    @AfterClass
    public static void after() {
        driver.close();
    }

}
