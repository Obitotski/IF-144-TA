package com.softserveinc.ita;

import com.softserveinc.ita.pageobjects.LoginPage;
import com.softserveinc.ita.pageobjects.admin.SpecialitiesPage;
import com.softserveinc.ita.pageobjects.util.RandomUtil;
import com.softserveinc.ita.pageobjects.util.TestRunner;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.softserveinc.ita.pageobjects.util.DataProvider.*;
import static com.softserveinc.ita.pageobjects.util.RandomUtil.getRandomNumber;
import static com.softserveinc.ita.pageobjects.util.WindowTabHelper.getCurrentUrl;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecialitiesTest extends TestRunner {

    private SpecialitiesPage specialitiesPage;

    @BeforeMethod(groups = {"positive", "negative"})
    public void openSpecialitiesPage() {
        specialitiesPage = new LoginPage()
                .login(ADMIN_LOGIN, ADMIN_PASSWORD)
                .openSpecialitiesPage();
    }

    @Test(groups = "positive")
    @Description("Test to verify Specialities page opening")
    public void verifySpecialitiesPageOpening() {

        var expectedUrl = SPECIALITIES_PAGE_URL;
        var currentUrl = getCurrentUrl();

        assertThat(currentUrl)
                .as("Page url should be " + expectedUrl)
                .isEqualTo(expectedUrl);
    }

    @Test(groups = "positive")
    @Description("Test to verify new speciality is added")
    public void verifyAddingNewSpeciality() {

        var randCode = getRandomNumber(5);
        var specialityCode = Integer.toString(randCode); // only numbers, no more than 5 symbols
        var specialityName = "test" + randCode;

        var messageText = specialitiesPage
                .addNewSpeciality(specialityCode, specialityName)
                .waitForProgressBarToAppear()
                .getMessageText();

        assertThat(messageText)
                .as("Message after adding should contain added speciality name")
                .contains(specialityName);

        var lastSpecialityCode = specialitiesPage
                .waitForProgressBarToDisappear()
                .getLastSpecialityCode();

        assertThat(lastSpecialityCode)
                .as("After adding new speciality speciality with added code " +
                        "should be last in the table")
                .isEqualTo(specialityCode);

        specialitiesPage.deleteSpecialityByCode(specialityCode);
    }

    @Test(groups = "positive")
    @Description("Test to verify speciality is deleted")
    public void verifyDeletingLastSpeciality() {

        var randCode = getRandomNumber(5);
        var specialityCode = Integer.toString(randCode); // only numbers, no more than 5 symbols
        var specialityName = "test" + randCode;

        var lastSpecialityCode = specialitiesPage
                .addNewSpeciality(specialityCode, specialityName)
                .waitForProgressBarToAppear()
                .waitForProgressBarToDisappear()
                .getLastSpecialityCode();

        assertThat(lastSpecialityCode)
                .as("After adding new speciality speciality with added code " +
                        "should be last in the table")
                .isEqualTo(specialityCode);

        var messageText = specialitiesPage
                .deleteSpecialityByCode(lastSpecialityCode)
                .waitForProgressBarToAppear()
                .getMessageText();

        assertThat(messageText)
                .as("Message after deleting should contain deleted speciality name")
                .contains(specialityName);

        lastSpecialityCode = specialitiesPage
                .waitForProgressBarToDisappear()
                .getLastSpecialityCode();

        assertThat(lastSpecialityCode)
                .as("After deleting speciality last speciality in the table " +
                        "should not have deleted code")
                .isNotEqualTo(specialityCode);
    }
}
