package com.leonp967.log.ingesting.service;

import com.leonp967.log.ingesting.bo.LogEntryBO;
import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.builder.LogEntryBuilder;
import com.leonp967.log.ingesting.builder.MetricsBuilder;
import com.leonp967.log.ingesting.model.MetricsFilter;
import com.leonp967.log.ingesting.model.TimeTypeEnum;
import com.leonp967.log.ingesting.repository.LogAnalyticsRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.nio.Buffer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class LogAnalyticsServiceTest {

    private static LogAnalyticsService service;
    private static WebClient webClient;
    private static LogAnalyticsRepository repository;

    private static final String DAY_TIME_VALUE = "13";
    private static final String DAY_TIME_TYPE = "D";
    private static final String WEEK_TIME_VALUE = "37";
    private static final String WEEK_TIME_TYPE = "w";
    private static final String YEAR_TIME_VALUE = "2015";
    private static final String YEAR_TIME_TYPE = "Y";

    @BeforeAll
    public static void setUp() throws IllegalAccessException {
        Vertx vertx = mock(Vertx.class);
        repository = mock(LogAnalyticsRepository.class);
        service = new LogAnalyticsService(vertx, repository);
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        FieldUtils.writeField(service, "webClient", webClient, true);
    }

    @Test
    public void shouldIngestLog() {
        LogEntryBO logEntryBO = LogEntryBuilder.buildBO();
        HttpResponse<Buffer> buffer = mock(HttpResponse.class);


        given(buffer.statusCode()).willReturn(200);
        given(webClient.post("").sendJson(logEntryBO))
                .will(invocationOnMock -> Uni.createFrom().item(buffer));

        Uni<Response.Status> statusUni = service.ingestLog(logEntryBO);

        statusUni.subscribe()
                .with(status -> assertThat(status, is(Response.Status.OK)),
                        Throwable::printStackTrace);
    }

    @Test
    public void shouldEvaluateMetricsWithDayFilter() {
        MetricsFilter filter = new MetricsFilter();
        filter.setTimeType(DAY_TIME_TYPE);
        filter.setTimeValue(DAY_TIME_VALUE);
        MetricsBO expected = MetricsBuilder.buildBO();

        given(repository.evaluateAllMetrics(DAY_TIME_VALUE, TimeTypeEnum.DAY))
                .willReturn(Uni.createFrom().item(MetricsBuilder.buildBO()));

        Uni<MetricsBO> metricsUni = service.evaluateMetrics(filter);

        verify(repository).evaluateAllMetrics(DAY_TIME_VALUE, TimeTypeEnum.DAY);

        metricsUni.subscribe()
                .with(result -> assertThat(result, is(expected)),
                        Throwable::printStackTrace);
    }

    @Test
    public void shouldEvaluateMetricsWithWeekFilter() {
        MetricsFilter filter = new MetricsFilter();
        filter.setTimeType(WEEK_TIME_TYPE);
        filter.setTimeValue(WEEK_TIME_VALUE);
        MetricsBO expected = MetricsBuilder.buildBO();

        given(repository.evaluateAllMetrics(WEEK_TIME_VALUE, TimeTypeEnum.WEEK))
                .willReturn(Uni.createFrom().item(MetricsBuilder.buildBO()));

        Uni<MetricsBO> metricsUni = service.evaluateMetrics(filter);

        verify(repository).evaluateAllMetrics(WEEK_TIME_VALUE, TimeTypeEnum.WEEK);

        metricsUni.subscribe()
                .with(result -> assertThat(result, is(expected)),
                        Throwable::printStackTrace);
    }

    @Test
    public void shouldEvaluateMetricsWithYearFilter() {
        MetricsFilter filter = new MetricsFilter();
        filter.setTimeType(YEAR_TIME_TYPE);
        filter.setTimeValue(YEAR_TIME_VALUE);
        MetricsBO expected = MetricsBuilder.buildBO();

        given(repository.evaluateAllMetrics(YEAR_TIME_VALUE, TimeTypeEnum.YEAR))
                .willReturn(Uni.createFrom().item(MetricsBuilder.buildBO()));

        Uni<MetricsBO> metricsUni = service.evaluateMetrics(filter);

        verify(repository).evaluateAllMetrics(YEAR_TIME_VALUE, TimeTypeEnum.YEAR);

        metricsUni.subscribe()
                .with(result -> assertThat(result, is(expected)),
                        Throwable::printStackTrace);
    }

    @Test
    public void shouldReturnPositiveHealthCheck() {
        HttpResponse<Buffer> buffer = mock(HttpResponse.class);

        given(buffer.statusCode()).willReturn(200);
        given(webClient.get("").send())
                .will(invocationOnMock -> Uni.createFrom().item(buffer));
        given(repository.healthCheck())
                .willReturn(Uni.createFrom().item(Response.Status.OK));

        Uni<Response.Status> statusUni = service.healthCheck();

        statusUni.subscribe()
                .with(status -> assertThat(status, is(Response.Status.OK)),
                        Throwable::printStackTrace);
    }

    @Test
    public void shouldReturnNegativeHealthCheck() {
        HttpResponse<Buffer> buffer = mock(HttpResponse.class);

        given(buffer.statusCode()).willReturn(200);
        given(webClient.get("").send())
                .will(invocationOnMock -> Uni.createFrom().item(buffer));
        given(repository.healthCheck())
                .willReturn(Uni.createFrom().item(Response.Status.SERVICE_UNAVAILABLE));

        Uni<Response.Status> statusUni = service.healthCheck();

        statusUni.subscribe()
                .with(status -> assertThat(status, is(Response.Status.INTERNAL_SERVER_ERROR)),
                        Throwable::printStackTrace);
    }

}