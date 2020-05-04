package com.leonp967.log.ingesting.elasticsearch.aggregations.builder;

import com.leonp967.log.ingesting.model.TimeTypeEnum;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;

import javax.enterprise.context.ApplicationScoped;

import static org.elasticsearch.search.aggregations.BucketOrder.count;

@ApplicationScoped
public class MetricAggregationsBuilder {

    private static final String URL_KEYWORD_FIELD_NAME = "url.keyword";
    private static final String REGION_KEYWORD_FIELD_NAME = "region.keyword";
    private static final String DAY_KEYWORD_FIELD_NAME = "day.keyword";
    private static final String WEEK_KEYWORD_FIELD_NAME = "week.keyword";
    private static final String YEAR_KEYWORD_FIELD_NAME = "year.keyword";
    private static final String MINUTE_KEYWORD_FIELD_NAME = "minute.keyword";

    public static final String TOP_URLS_AGGREGATION_NAME = "top_urls";
    public static final String TOP_REGIONS_AGGREGATION_NAME = "top_regions";
    public static final String BOTTOM_URL_AGGREGATION_NAME = "bottom_url";
    public static final String TOP_MINUTE_AGGREGATION_NAME = "top_minute";
    public static final String TIME_AGGREGATION_NAME = "time";

    public AggregationBuilder buildTopUrlsAggregation() {
        return AggregationBuilders.terms(TOP_URLS_AGGREGATION_NAME)
                .field(URL_KEYWORD_FIELD_NAME)
                .size(3);
    }

    public AggregationBuilder buildTopUrlsByRegionAggregation() {
        AggregationBuilder topUrl = AggregationBuilders.terms(TOP_URLS_AGGREGATION_NAME)
                .field(URL_KEYWORD_FIELD_NAME)
                .size(3);

        return AggregationBuilders.terms(TOP_REGIONS_AGGREGATION_NAME)
                .field(REGION_KEYWORD_FIELD_NAME)
                .subAggregation(topUrl);
    }

    public AggregationBuilder buildBottomUrlAggregation() {
        return AggregationBuilders.terms(BOTTOM_URL_AGGREGATION_NAME)
                .field(URL_KEYWORD_FIELD_NAME)
                .order(count(true))
                .size(1);
    }

    public AggregationBuilder buildTopUrlsByTimeAggregation(String timeValue, TimeTypeEnum timeType) {
        AggregationBuilder topUrl = AggregationBuilders.terms(TOP_URLS_AGGREGATION_NAME)
                .field(URL_KEYWORD_FIELD_NAME)
                .size(3);

        String fieldName;
        switch (timeType) {
            case DAY:
                fieldName = DAY_KEYWORD_FIELD_NAME;
                break;
            case WEEK:
                fieldName = WEEK_KEYWORD_FIELD_NAME;
                break;
            case YEAR:
                fieldName = YEAR_KEYWORD_FIELD_NAME;
                break;
            default:
                throw new IllegalArgumentException("The timeType argument is invalid!");
        }

        return AggregationBuilders.terms(TIME_AGGREGATION_NAME)
                .field(fieldName)
                .includeExclude(new IncludeExclude(timeValue, ""))
                .subAggregation(topUrl);
    }

    public AggregationBuilder buildMostAccessedMinuteAggregation() {
        return AggregationBuilders.terms(TOP_MINUTE_AGGREGATION_NAME)
                .field(MINUTE_KEYWORD_FIELD_NAME)
                .size(1);
    }
}
