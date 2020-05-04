package com.leonp967.log.ingesting.resource;

import com.leonp967.log.ingesting.builder.LogEntryBuilder;
import com.leonp967.log.ingesting.dto.MetricsDTO;
import com.leonp967.log.ingesting.model.LogEntry;
import com.leonp967.log.ingesting.model.MetricsFilter;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class LogAnalyticsResourceTest {

    @ConfigProperty(name = "logstash.port")
    private Integer logstashPort;

    @ConfigProperty(name = "elasticsearch.port")
    private Integer elasticsearchPort;

    @ConfigProperty(name = "quarkus.http.test-port")
    private Integer quarkusPort;

    @BeforeEach
    public void cleanLogs() {
        given()
            .port(elasticsearchPort)
            .when().delete("/_all")
            .then()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    private void givenLogstashRunning() {
        given()
            .port(logstashPort)
            .when().get()
            .then()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    private void givenElasticsearchRunning() {
        given()
            .port(elasticsearchPort)
            .when().get("_cluster/health?pretty")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
        .body("$", hasKey("status"))
        .body("status", anyOf(is("yellow"), is("green")));
    }

    @Test
    public void shouldCheckHealth() {
        givenLogstashRunning();
        givenElasticsearchRunning();

        given()
            .port(quarkusPort)
            .when().get("/laa/health")
            .then()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void shouldIngestLog() {
        givenLogstashRunning();
        givenElasticsearchRunning();

        LogEntry logEntry = LogEntryBuilder.buildModel();

        given()
            .port(quarkusPort)
            .body(logEntry)
            .contentType(ContentType.JSON)
            .when().post("/laa/ingest")
            .then()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    private void insertLog(LogEntry logEntry) {
        given()
                .port(quarkusPort)
                .body(logEntry)
                .contentType(ContentType.JSON)
            .when()
                .post("/laa/ingest")
            .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    private void populateLogs() {
        LogEntry logEntry = LogEntryBuilder.buildModel();
        LogEntry logEntry2 = LogEntryBuilder.buildModel();
        logEntry2.setUrl("https://www.facebook.com");
        LogEntry logEntry3 = LogEntryBuilder.buildModel();
        logEntry3.setUrl("https://www.metacritic.com");
        logEntry3.setRegion(3);
        logEntry3.setAccessTimestamp(1591267474000L);

        insertLog(logEntry);
        insertLog(logEntry);
        insertLog(logEntry);
        insertLog(logEntry2);
        insertLog(logEntry2);
        insertLog(logEntry3);
    }

    @Test
    public void shouldGetMetrics() throws InterruptedException {
        givenLogstashRunning();
        givenElasticsearchRunning();

        populateLogs();
        Thread.sleep(1000L);

        MetricsFilter filter = new MetricsFilter();
        filter.setTimeValue("04");
        filter.setTimeType("d");

        MetricsDTO metricsDTO = given()
                    .port(quarkusPort)
                    .contentType(ContentType.JSON)
                    .body(filter)
                .when()
                    .get("/laa/metrics")
                .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                .and()
                    .contentType(ContentType.JSON)
                .and()
                    .extract()
                    .as(MetricsDTO.class);

        assertThat(metricsDTO.getBottomUrl(), notNullValue());
        assertThat(metricsDTO.getMostAccessedMinute(), notNullValue());
        assertThat(metricsDTO.getTopUrls(), notNullValue());
        assertThat(metricsDTO.getTopUrlsByRegion(), notNullValue());
        assertThat(metricsDTO.getTopUrlsByTime(), notNullValue());
        assertThat(metricsDTO.getTopUrls().get(0).getKey(), is("https://google.com/?q=megamen"));
        assertThat(metricsDTO.getTopUrls().get(1).getKey(), is("https://www.facebook.com"));
        assertThat(metricsDTO.getTopUrls().get(2).getKey(), is("https://www.metacritic.com"));
        assertThat(metricsDTO.getTopUrlsByRegion().get(0).getKey(), is("us-east-1"));
        assertThat(metricsDTO.getTopUrlsByRegion().get(0).getValues().get(0).getKey(), is("https://google.com/?q=megamen"));
        assertThat(metricsDTO.getTopUrlsByTime().get(0).getKey(), is("04"));
        assertThat(metricsDTO.getTopUrlsByTime().get(0).getValues().get(0).getKey(), is("https://google.com/?q=megamen"));
        assertThat(metricsDTO.getMostAccessedMinute().getKey(), is("48"));
        assertThat(metricsDTO.getMostAccessedMinute().getCount(), is(5L));
        assertThat(metricsDTO.getBottomUrl().getKey(), is("https://www.metacritic.com"));
        assertThat(metricsDTO.getBottomUrl().getCount(), is(1L));
    }

}