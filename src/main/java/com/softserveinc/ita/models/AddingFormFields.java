package com.softserveinc.ita.models;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.codeborne.selenide.Selenide.$x;

@RequiredArgsConstructor
public enum AddingFormFields {
    FACULTY_NAME($x("//input[@formcontrolname='faculty_name']")),
    FACULTY_DESCRIPTION($x("//textarea[@formcontrolname='faculty_description']")),
    SUBJECT_NAME($x("//input[@formcontrolname='subject_name']")),
    SUBJECT_DESCRIPTION($x("//textarea[@formcontrolname='subject_description']")),
    SPECIALTY_CODE($x("//input[@formcontrolname='speciality_code']")),
    SPECIALTY_NAME($x("//input[@formcontrolname='speciality_name']")),
    GROUP_NAME($x("//input[@formcontrolname='group_name']")),
    GROUP_SPECIALTY_ID($x("//mat-select[@formcontrolname='speciality_id']")),
    GROUP_FACULTY_ID($x("//mat-select[@formcontrolname='faculty_id']")),
    ADMIN_USERNAME($x("//input[@formcontrolname='username']")),
    ADMIN_EMAIL($x("//input[@formcontrolname='email']")),
    ADMIN_PASSWORD($x("//input[@formcontrolname='password']")),
    ADMIN_PASSWORD_CONFIRM($x("//input[@formcontrolname='password_confirm']"));

    @Getter
    private final SelenideElement name;
}