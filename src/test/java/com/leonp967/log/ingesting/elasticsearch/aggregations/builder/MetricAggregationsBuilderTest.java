package com.leonp967.log.ingesting.elasticsearch.aggregations.builder;

import com.leonp967.log.ingesting.model.TimeTypeEnum;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class MetricAggregationsBuilderTest {

    private static MetricAggregationsBuilder builder;

    @BeforeAll
    public static void setUp() {
        builder = new MetricAggregationsBuilder();
    }

    @Test
    public void shouldReturnCorrectTopUrlsAggregation() throws JSONException {
        String expectedJson = "{\"top_urls\":{\"terms\":{\"field\":\"url.keyword\",\"size\":3}}}";

        String resultJson = builder.buildTopUrlsAggregation().toString();

        JSONAssert.assertEquals(expectedJson, resultJson, false);
    }

    @Test
    public void shouldReturnCorrectTopUrlsByRegionAggregation() throws JSONException {
        String expectedJson = "{\"top_regions\":{\"terms\":{\"field\":\"region.keyword\"},\"aggregations\":{\"top_urls\":{\"terms\":{\"field\":\"url.keyword\",\"size\":3}}}}}";

        String resultJson = builder.buildTopUrlsByRegionAggregation().toString();

        JSONAssert.assertEquals(expectedJson, resultJson, false);
    }

    @Test
    public void shouldReturnCorrectBottomurlAggregation() throws JSONException {
        String expectedJson = "{\"bottom_url\":{\"terms\":{\"field\":\"url.keyword\",\"size\":1,\"order\":[{\"_count\":\"asc\"},{\"_key\":\"asc\"}]}}}";

        String resultJson = builder.buildBottomUrlAggregation().toString();

        JSONAssert.assertEquals(expectedJson, resultJson, false);
    }

    @Test
    public void shouldReturnCorrectTopMinuteAggregation() throws JSONException {
        String expectedJson = "{\"top_minute\":{\"terms\":{\"field\":\"minute.keyword\",\"size\":1}}}";

        String resultJson = builder.buildMostAccessedMinuteAggregation().toString();

        JSONAssert.assertEquals(expectedJson, resultJson, false);
    }

    @Test
    public void shouldReturnTopUrlsByWeekAggregation() throws JSONException {
        String expectedJson = "{\"time\":{\"terms\":{\"field\":\"week.keyword\",\"include\":\"25\"},\"aggregations\":{\"top_urls\":{\"terms\":{\"field\":\"url.keyword\",\"size\":3}}}}}";

        String resultJson = builder.buildTopUrlsByTimeAggregation("25", TimeTypeEnum.WEEK).toString();

        JSONAssert.assertEquals(expectedJson, resultJson, false);
    }

    @Test
    public void shouldReturnTopUrlsByDayAggregation() throws JSONException {
        String expectedJson = "{\"time\":{\"terms\":{\"field\":\"day.keyword\",\"include\":\"10\"},\"aggregations\":{\"top_urls\":{\"terms\":{\"field\":\"url.keyword\",\"size\":3}}}}}";

        String resultJson = builder.buildTopUrlsByTimeAggregation("10", TimeTypeEnum.DAY).toString();

        JSONAssert.assertEquals(expectedJson, resultJson, false);
    }

    @Test
    public void shouldReturnTopUrlsByYearAggregation() throws JSONException {
        String expectedJson = "{\"time\":{\"terms\":{\"field\":\"year.keyword\",\"include\":\"2011\"},\"aggregations\":{\"top_urls\":{\"terms\":{\"field\":\"url.keyword\",\"size\":3}}}}}";

        String resultJson = builder.buildTopUrlsByTimeAggregation("2011", TimeTypeEnum.YEAR).toString();

        JSONAssert.assertEquals(expectedJson, resultJson, false);
    }

}