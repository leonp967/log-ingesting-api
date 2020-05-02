package com.leonp967.log.ingesting.repository;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.builder.MetricAggregationsBuilder;
import com.leonp967.log.ingesting.model.TimeTypeEnum;
import io.smallrye.mutiny.Uni;
import org.apache.http.HttpHost;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class LogAnalyticsRepository {

    private RestHighLevelClient elasticClient;
    private final MetricAggregationsBuilder aggregationsBuilder;
    private final Logger LOGGER = LoggerFactory.getLogger(LogAnalyticsRepository.class);

    @ConfigProperty(name = "elasticsearch.url")
    String elasticSearchUrl;

    @ConfigProperty(name = "elasticsearch.scheme")
    String elasticSearchScheme;

    @ConfigProperty(name = "elasticsearch.port")
    Integer elasticSearchPort;

    public LogAnalyticsRepository(MetricAggregationsBuilder aggregationsBuilder) {
        this.aggregationsBuilder = aggregationsBuilder;
    }

    @PostConstruct
    public void initialize() {
        elasticClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticSearchUrl, elasticSearchPort, elasticSearchScheme)));
    }

    private List<MetricEntryBO> handleSimpleAggregation(List<? extends Terms.Bucket> buckets) {
        return buckets.stream()
                .map(bucket -> MetricEntryBO.builder()
                        .key(bucket.getKeyAsString())
                        .count(bucket.getDocCount())
                        .build())
                .collect(Collectors.toList());
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

    private SearchRequest buildSearchRequest(AggregationBuilder topUrlsAggregation) {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(topUrlsAggregation);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    public Uni<List<MetricEntryBO>> getTopUrls() {
        return Uni.createFrom().item(aggregationsBuilder.buildTopUrlsAggregation())
                .onItem().apply(this::buildSearchRequest)
                .onItem().produceUni((request, uniEmitter) ->
                        elasticClient.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                    @Override
                    public void onResponse(SearchResponse response) {
                        Terms topUrlsTerms = response.getAggregations().get("top_urls");
                        uniEmitter.complete(handleSimpleAggregation(topUrlsTerms.getBuckets()));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        LOGGER.error("Error while fetching top URLs metrics!", e);
                        uniEmitter.complete(new ArrayList<>());
                    }
                }));
    }

    public Uni<List<CompositeMetricBO>> getTopUrlsByRegion() {
        return Uni.createFrom().item(aggregationsBuilder.buildTopUrlsByRegionAggregation())
                .onItem().apply(this::buildSearchRequest)
                .onItem().produceUni((request, uniEmitter) ->
                        elasticClient.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                            @Override
                            public void onResponse(SearchResponse response) {
                                Terms topUrlsByRegionTerms = response.getAggregations().get("regions");
                                uniEmitter.complete(handleCompositeAggregation(topUrlsByRegionTerms.getBuckets()));
                            }

                            @Override
                            public void onFailure(Exception e) {
                                LOGGER.error("Error while fetching top URLs by region metrics!", e);
                                uniEmitter.complete(new ArrayList<>());
                            }
                        }));
    }

    public Uni<MetricEntryBO> getBottomUrl() {
        return Uni.createFrom().item(aggregationsBuilder.buildBottomUrlAggregation())
                .onItem().apply(this::buildSearchRequest)
                .onItem().produceUni((request, uniEmitter) ->
                        elasticClient.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                            @Override
                            public void onResponse(SearchResponse response) {
                                Terms bottomUrlTerms = response.getAggregations().get("bottom_url");
                                MetricEntryBO metricEntryBO = MetricEntryBO.builder()
                                        .key(bottomUrlTerms.getBuckets().get(0).getKeyAsString())
                                        .count(bottomUrlTerms.getBuckets().get(0).getDocCount())
                                        .build();
                                uniEmitter.complete(metricEntryBO);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                LOGGER.error("Error while fetching bottom URL metrics!", e);
                                uniEmitter.complete(MetricEntryBO.builder().build());
                            }
                        }));
    }

    public Uni<List<CompositeMetricBO>> getTopUrlsByTime(String timeValue, TimeTypeEnum timeType) {
        return Uni.createFrom().item(aggregationsBuilder.buildTopUrlsByTimeAggregation(timeValue, timeType))
                .onItem().apply(this::buildSearchRequest)
                .onItem().produceUni((request, uniEmitter) ->
                        elasticClient.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                            @Override
                            public void onResponse(SearchResponse response) {
                                Terms topUrlsByTimeTerms = response.getAggregations().get("time");
                                uniEmitter.complete(handleCompositeAggregation(topUrlsByTimeTerms.getBuckets()));
                            }

                            @Override
                            public void onFailure(Exception e) {
                                LOGGER.error("Error while fetching top URLs by time metrics!", e);
                                uniEmitter.complete(new ArrayList<>());
                            }
                        }));
    }

    public Uni<MetricEntryBO> getMostAccessedMinute() {
        return Uni.createFrom().item(aggregationsBuilder.buildMostAccessedMinuteAggregation())
                .onItem().apply(this::buildSearchRequest)
                .onItem().produceUni((request, uniEmitter) ->
                        elasticClient.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                    @Override
                    public void onResponse(SearchResponse response) {
                        Terms bottomUrlTerms = response.getAggregations().get("top_minute");
                        MetricEntryBO metricEntryBO = MetricEntryBO.builder()
                                .key(bottomUrlTerms.getBuckets().get(0).getKeyAsString())
                                .count(bottomUrlTerms.getBuckets().get(0).getDocCount())
                                .build();
                        uniEmitter.complete(metricEntryBO);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        LOGGER.error("Error while fetching most accessed minute metrics!", e);
                        uniEmitter.complete(MetricEntryBO.builder().build());
                    }
                }));
    }

    public Uni<MetricsBO> evaluateAllMetrics(String timeValue, TimeTypeEnum timeType) {
        return Uni.combine().all().unis(getBottomUrl(), getMostAccessedMinute(),
                getTopUrlsByRegion(), getTopUrls(), getTopUrlsByTime(timeValue, timeType))
                .asTuple().onItem()
                .apply(tuple -> MetricsBO.builder()
                        .bottomUrl(tuple.getItem1())
                        .mostAccessedMinute(tuple.getItem2())
                        .topUrlsByRegion(tuple.getItem3())
                        .topUrls(tuple.getItem4())
                        .topUrlsByTime(tuple.getItem5())
                        .build());
    }

    public Uni<Response.Status> healthCheck() {
        return Uni.createFrom().emitter(uniEmitter ->
            elasticClient.cluster().healthAsync(new ClusterHealthRequest(), RequestOptions.DEFAULT,
                    new ActionListener<ClusterHealthResponse>() {
                        @Override
                        public void onResponse(ClusterHealthResponse clusterHealthResponse) {
                            if (clusterHealthResponse.isTimedOut()) {
                                uniEmitter.complete(Response.Status.REQUEST_TIMEOUT);
                            } else {
                                LOGGER.info("Elasticsearch health check status [{}]", clusterHealthResponse.getStatus());
                                uniEmitter.complete(clusterHealthResponse.getStatus() != ClusterHealthStatus.RED ?
                                        Response.Status.OK : Response.Status.SERVICE_UNAVAILABLE);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            LOGGER.error("Elastic Search health check failed!", e);
                            uniEmitter.complete(Response.Status.INTERNAL_SERVER_ERROR);
                        }
                    })
        );
    }
}
