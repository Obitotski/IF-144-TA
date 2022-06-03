package com.softserveinc.ita;

import com.softserveinc.ita.pageobjects.LoginPage;
import com.softserveinc.ita.pageobjects.admin.FacultiesPage;
import com.softserveinc.ita.util.TestRunner;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.softserveinc.ita.util.DataProvider.*;
import static com.softserveinc.ita.util.WindowTabHelper.getCurrentUrl;
import static org.assertj.core.api.Assertions.assertThat;

public class FacultiesTest extends TestRunner {

    private FacultiesPage facultiesPage;

    @BeforeMethod (groups = {"positive", "negative"})
    public void openFacultiesPage() {
        facultiesPage = new LoginPage()
                .login(ADMIN_LOGIN, ADMIN_PASSWORD)
                .openFacultiesPage();
    }

    @Test (groups = "positive")
    @Description("Test to verify Faculties page opening")
    public void verifyFacultiesPageOpening() {

        var expectedUrl = FACULTIES_PAGE_URL;
        var currentUrl = getCurrentUrl();

        assertThat(currentUrl)
                .as("Page url should be " + expectedUrl)
                .isEqualTo(expectedUrl);
    }

    @Test(groups = "positive", dataProvider = "searchValues")
    @Description("Test to verify search field on the faculties page should work with valid input")
    public void verifySearchFieldWorksWithValidInput(String value, String expectedValue) {
        var listOfFaculties = facultiesPage
                .performSearch(value)
                .getFaculties();

        assertThat(listOfFaculties)
                .as("Search of the faculty performs if valid faculty name or description is entered")
                .allMatch(it -> it.contains(expectedValue));
    }

    @Test (groups = "negative")
    @Description("Test to verify search field on the faculties page should not work with invalid input")
    public void verifySearchFieldDoesntWorkWithInvalidInput() {
        var numberOfFaculties = facultiesPage
                .performSearch(INVALID)
                .getFaculties()
                .size();

        assertThat(numberOfFaculties)
                .as("Search of the faculty doesn't perform if invalid data is entered")
                .isEqualTo(0);
    }

    @DataProvider(name = "searchValues")
    public static Object[][] inputData() {
        return new Object[][]{{DESCRIPTION, "Інститут інформаційних технологій"},
                {FACULTY, "Інститут інформаційних технологій"}};
    }
}