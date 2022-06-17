package com.softserveinc.ita.pageobjects.admin;

import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

@Getter
public class SubjectsPage extends MainMenu<SubjectsPage> {

    private final EntityTable table = new EntityTable();

    @Step("Subjects page: Opened adding subject form")
    public AddingSubjectModal openAddingSubjectForm() {
        $x("//button[contains(@class, 'addSubject')]").click();
        waitUntilModalVisible();

        return new AddingSubjectModal();
    }

    public boolean hasSubject(String subject) {
        return $$x("//td[contains(@class, 'mat-column-subject_name')]")
                .shouldHave(sizeGreaterThanOrEqual(0))
                .texts()
                .contains(subject);
    }

    @Step("Subjects page: Set search value")
    public SubjectsPage setSearchValue(String subject) {
        var searchField = "//mat-form-field[contains(@class, 'filter')]//input";

        $x(searchField).clear();
        $x(searchField).sendKeys(subject);

        return this;
    }

    @Step("Subjects page: Deleted subject")
    public SubjectsPage deleteSubjectByName(String subject) {
        pickIconMenu(subject, "delete");

        return this;
    }

    @Step("Subjects page: Confirmed deleting subject")
    public SubjectsPage confirmDeletingSubject() {
        $x("//app-confirm//button[1]")
                .shouldBe(visible)
                .click();

        return this;
    }

    @Step("Subjects page: Edited subject")
    public AddingSubjectModal editSubject(String subject) {
        pickIconMenu(subject, "edit");
        waitUntilModalVisible();

        return new AddingSubjectModal();
    }

    @Step("Subjects page: Opened timetable of subject")
    public TimetablePage openTimetablePage(String subject) {
        pickIconMenu(subject, "date_range");

        return new TimetablePage();
    }

    private void waitUntilModalVisible() {
        $x("//app-subjects-create-modal").shouldBe(visible);
    }

    private void pickIconMenu(String subject, String menuItem) {
        $$x("//tbody//tr//td")
                .findBy(exactText(subject))
                .parent()
                .$x(format(".//mat-icon[contains(@class, '%s')]", menuItem))
                .click();
    }
}
