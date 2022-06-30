package com.softserveinc.ita.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.softserveinc.ita.models.SubjectEntity;
import com.softserveinc.ita.models.TestEntity;
import com.softserveinc.ita.models.TimeTableEntity;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Cookie;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.google.gson.JsonParser.parseString;
import static com.softserveinc.ita.util.DataProvider.API_BASE_URI;
import static com.softserveinc.ita.util.DataProvider.API_ENTITY_GET_RECORDS_PATH;
import static com.softserveinc.ita.util.DataProvider.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static java.lang.String.format;
import static java.util.Map.of;

@UtilityClass
public class ApiUtil {

    public static Response performPostRequestWithBody(Map<String, String> bodyContent, String basePath) {
        setUpApiSpecifications();

        return given()
                .body(bodyContent)
                .post(basePath);
    }

    public static Response performGetRequest(Cookie cookie, String basePath) {
        setUpApiSpecifications();

        return given()
                .cookie(cookie)
                .get(basePath);
    }

    public static List<SubjectEntity> getSubjectsListByAPI(Cookie sessionId) {
        var path = format(API_ENTITY_GET_RECORDS_PATH, "Subject");
        var response = performGetRequest(sessionId, path);

        return extractFromJson(response).getList("", SubjectEntity.class);
    }

    public static List<SubjectEntity> postSubject(SubjectEntity subject) {
        var path = format(API_ENTITY_POST_RECORDS_PATH, "Subject");
        var response = performPostRequestWithBody(setUpSubjectBody(subject), path);

        return extractFromJson(response).getList("", SubjectEntity.class);
    }

    public static Response deleteSubject(Cookie sessionId, SubjectEntity subject) {
        var path = format(API_ENTITY_DELETE_RECORDS_PATH, "Subject", subject.getId());

        return performGetRequest(sessionId, path);
    }

    public static List<TimeTableEntity> getTimeTablesListByAPI(Cookie sessionId) {
        var path = format(API_ENTITY_GET_RECORDS_PATH, "TimeTable");
        var response = performGetRequest(sessionId, path);

        return extractFromJson(response).getList("", TimeTableEntity.class);
    }

    public static List<TimeTableEntity> postTimeTable(TimeTableEntity timeTable) {
        var path = format(API_ENTITY_POST_RECORDS_PATH, "TimeTable");
        var response = performPostRequestWithBody(setUpTimeTableBody(timeTable), path);

        return extractFromJson(response).getList("", TimeTableEntity.class);
    }

    public static List<TimeTableEntity> updateTimeTable(TimeTableEntity timeTable) {
        var path = format(API_ENTITY_UPDATE_RECORDS_PATH, "TimeTable", timeTable.getId());
        var response = performPostRequestWithBody(setUpTimeTableBody(timeTable), path);

        return extractFromJson(response).getList("", TimeTableEntity.class);
    }

    public static Response deleteTimeTable(Cookie sessionId, TimeTableEntity timeTable) {
        var path = format(API_ENTITY_DELETE_RECORDS_PATH, "TimeTable", timeTable.getId());

        return performGetRequest(sessionId, path);
    }

    public static List<TestEntity> getTestsListByAPI(Cookie sessionId) {
        var path = format(API_ENTITY_GET_RECORDS_PATH, "test");
        var response = getJsonArrayFromGetRequest(sessionId, path);
        var list = new LinkedList<TestEntity>();

        response.forEach(item -> list.add(new Gson().fromJson(item, TestEntity.class)));

        return list;
    }

    public static void verifySchemaRecords(String entity, String filePath) {
        given()
                .filter(new AllureRestAssured())
                .get(API_BASE_URI+(format(API_ENTITY_GET_RECORDS_PATH, entity)))
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(filePath));
    }

    private JsonArray getJsonArrayFromGetRequest(Cookie sessionId, String path) {
        return parseString(performGetRequest(sessionId, path)
                .then()
                .extract()
                .body()
                .asString())
                .getAsJsonArray();
    }

    private Map<String, String> setUpSubjectBody(SubjectEntity subject) {
        return of("subject_name", subject.getName(), "subject_description", subject.getDescription());
    }

    private Map<String, String> setUpTimeTableBody(TimeTableEntity timeTable) {
        return of("group_id", timeTable.getGroupId(), "subject_id", timeTable.getSubjectId(),
                "start_date", timeTable.getStartDate(), "start_time", timeTable.getStartTime(),
                "end_date", timeTable.getEndDate(), "end_time", timeTable.getEndTime());
    }

    private JsonPath extractFromJson(Response response) {
        return response
                .then()
                .extract()
                .jsonPath();
    }

    private void setUpApiSpecifications() {
        requestSpecification = getRequestSpecification();
        responseSpecification = getResponseSpecification();
    }

    private RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(API_BASE_URI)
                .setContentType(JSON)
                .setContentType(JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();
    }

    private ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(JSON)
                .build();
    }
}
