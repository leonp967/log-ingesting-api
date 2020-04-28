package com.leonp967.log.ingesting.repository;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.model.TimeTypeEnum;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LogAnalyticsRepository {

    private RestHighLevelClient elasticClient;
    private final Logger LOGGER = LoggerFactory.getLogger(LogAnalyticsRepository.class);

    @PostConstruct
    public void initialize() {
        elasticClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
    }

    private AggregationBuilder buildTopUrlsAggregation() {
        return AggregationBuilders.terms("top_urls")
                .field("url.keyword")
                .size(3);
    }

    private List<MetricEntryBO> handleSimpleAggregation(List<? extends Terms.Bucket> buckets) {
        return buckets.stream()
                .map(bucket -> MetricEntryBO.builder()
                        .key(bucket.getKeyAsString())
                        .count(bucket.getDocCount())
                        .build())
                .collect(Collectors.toList());
    }

    private AggregationBuilder buildTopUrlsByRegionAggregation() {
        AggregationBuilder topUrl = AggregationBuilders.terms("top_urls")
                .field("url.keyword")
                .size(3);

        return AggregationBuilders.terms("regions")
                .field("region.keyword")
                .subAggregation(topUrl);
    }

    private List<CompositeMetricBO> handleCompositeAggregation(List<? extends Terms.Bucket> buckets) {
        return buckets.stream()
                .map(bucket -> {
                    Terms topUrlTerms = bucket.getAggregations().get("top_urls");
                    return CompositeMetricBO.builder()
                            .key(bucket.getKeyAsString())
                            .values(handleSimpleAggregation(topUrlTerms.getBuckets()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private AggregationBuilder buildBottomUrlAggregation() {
        return AggregationBuilders.terms("bottom_url")
                .field("url.keyword")
                .order(BucketOrder.count(true))
                .size(1);
    }

    private AggregationBuilder buildTopUrlsByTimeAggregation(Integer timeValue, TimeTypeEnum timeType) {
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

    private AggregationBuilder buildMostAccessedMinuteAggregation() {
        return AggregationBuilders.terms("top_minute")
                .field("minute.keyword")
                .size(1);
    }

    public Uni<MetricsBO> evaluateAllMetrics(Integer timeValue, TimeTypeEnum timeType) throws IOException {
        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(buildTopUrlsAggregation());
        searchSourceBuilder.aggregation(buildTopUrlsByRegionAggregation());
        searchSourceBuilder.aggregation(buildBottomUrlAggregation());
        searchSourceBuilder.aggregation(buildTopUrlsByTimeAggregation(timeValue, timeType));
        searchSourceBuilder.aggregation(buildMostAccessedMinuteAggregation());

        searchRequest.source(searchSourceBuilder);
        try {
            return Uni.createFrom().item(elasticClient.search(searchRequest, RequestOptions.DEFAULT))
                    .subscribeOn(Infrastructure.getDefaultWorkerPool())
                    .onItem().apply(response -> {
                        Terms topUrlsAggregation = response.getAggregations().get("top_urls");
                        List<MetricEntryBO> topUrls = handleSimpleAggregation(topUrlsAggregation.getBuckets());

                        Terms topUrlsByRegionAggregation = response.getAggregations().get("regions");
                        List<CompositeMetricBO> topUrlsByRegion = handleCompositeAggregation(topUrlsByRegionAggregation.getBuckets());

                        Terms bottomUrlAggregation = response.getAggregations().get("bottom_url");
                        MetricEntryBO bottomUrl = MetricEntryBO.builder()
                                .key(bottomUrlAggregation.getBuckets().get(0).getKeyAsString())
                                .count(bottomUrlAggregation.getBuckets().get(0).getDocCount())
                                .build();

                        Terms topUrlsByTimeAggregation = response.getAggregations().get("time");
                        List<CompositeMetricBO> topUrlsByTime = handleCompositeAggregation(topUrlsByTimeAggregation.getBuckets());

                        Terms mostAccessedMinuteAggregation = response.getAggregations().get("top_minute");
                        MetricEntryBO mostAccessedMinute = MetricEntryBO.builder()
                                .key(mostAccessedMinuteAggregation.getBuckets().get(0).getKeyAsString())
                                .count(mostAccessedMinuteAggregation.getBuckets().get(0).getDocCount())
                                .build();

                        return MetricsBO.builder()
                                .topUrls(topUrls)
                                .topUrlsByRegion(topUrlsByRegion)
                                .bottomUrl(bottomUrl)
                                .topUrlsByTime(topUrlsByTime)
                                .mostAccessedMinute(mostAccessedMinute)
                                .build();
                    });
        } catch (IOException e) {
            LOGGER.error("Error while evaluating metrics!", e);
            throw new IOException("Error while evaluating metrics!");
        }
    }
}
