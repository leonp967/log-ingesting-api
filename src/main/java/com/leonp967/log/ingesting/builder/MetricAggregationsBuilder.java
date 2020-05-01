package com.leonp967.log.ingesting.builder;

import com.leonp967.log.ingesting.model.TimeTypeEnum;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MetricAggregationsBuilder {

    public AggregationBuilder buildTopUrlsAggregation() {
        return AggregationBuilders.terms("top_urls")
                .field("url.keyword")
                .size(3);
    }

    public AggregationBuilder buildTopUrlsByRegionAggregation() {
        org.elasticsearch.search.aggregations.AggregationBuilder topUrl = AggregationBuilders.terms("top_urls")
                .field("url.keyword")
                .size(3);

        return AggregationBuilders.terms("regions")
                .field("region.keyword")
                .subAggregation(topUrl);
    }

    public AggregationBuilder buildBottomUrlAggregation() {
        return AggregationBuilders.terms("bottom_url")
                .field("url.keyword")
                .order(BucketOrder.count(true))
                .size(1);
    }

    public AggregationBuilder buildTopUrlsByTimeAggregation(String timeValue, TimeTypeEnum timeType) {
        AggregationBuilder topUrl = AggregationBuilders.terms("top_urls")
                .field("url.keyword")
                .size(3);

        String fieldName;
        if (timeType == TimeTypeEnum.DAY) {
            fieldName = "day.keyword";
        } else if (timeType == TimeTypeEnum.WEEK) {
            fieldName = "week.keyword";
        } else if (timeType == TimeTypeEnum.YEAR) {
            fieldName = "year.keyword";
        } else {
            throw new IllegalArgumentException("The timeType argument is invalid!");
        }

        return AggregationBuilders.terms("time")
                .field(fieldName)
                .includeExclude(new IncludeExclude(timeValue.toString(), ""))
                .subAggregation(topUrl);
    }

    public AggregationBuilder buildMostAccessedMinuteAggregation() {
        return AggregationBuilders.terms("top_minute")
                .field("minute.keyword")
                .size(1);
    }
}
