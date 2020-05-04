package com.leonp967.log.ingesting.repository;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.elasticsearch.aggregations.builder.MetricAggregationsBuilder;
import io.smallrye.mutiny.Uni;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.leonp967.log.ingesting.elasticsearch.aggregations.builder.MetricAggregationsBuilder.TOP_MINUTE_AGGREGATION_NAME;
import static com.leonp967.log.ingesting.elasticsearch.aggregations.builder.MetricAggregationsBuilder.TOP_URLS_AGGREGATION_NAME;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class LogAnalyticsRepositoryTest {

    private static LogAnalyticsRepository repository;
    private static MetricAggregationsBuilder builder;
    private static final String URL_BUCKET_KEY = "https://google.com";
    private static final String REGION_BUCKET_KEY = "us-west-2";
    private static final Long BUCKET_DOC_COUNT = 20L;
    private static final String MINUTE_BUCKET_KEY = "37";

    @BeforeAll
    public static void setUp() {
        builder = new MetricAggregationsBuilder();
        repository = spy(new LogAnalyticsRepository(builder));
    }

    @Test
    public void shouldHandleSimpleAggregation() {
        Terms.Bucket bucket = mock(StringTerms.Bucket.class);

        given(bucket.getKeyAsString()).willReturn(URL_BUCKET_KEY);
        given(bucket.getDocCount()).willReturn(BUCKET_DOC_COUNT);

        List<MetricEntryBO> metricEntryBOs = repository.handleSimpleAggregation(singletonList(bucket));

        assertThat(metricEntryBOs.get(0).getKey(), is(URL_BUCKET_KEY));
        assertThat(metricEntryBOs.get(0).getCount(), is(BUCKET_DOC_COUNT));
    }

    @Test
    public void shouldHandleCompositeAggregation() {
        Terms.Bucket bucket = mock(StringTerms.Bucket.class);
        Terms.Bucket bucketRegion = mock(StringTerms.Bucket.class);
        Terms aggregationTerms = mock(StringTerms.class);
        List<Aggregation> aggregationList = new ArrayList<>();
        aggregationList.add(aggregationTerms);
        Aggregations aggregations = new Aggregations(aggregationList);

        given(bucketRegion.getKeyAsString()).willReturn(REGION_BUCKET_KEY);
        given(bucketRegion.getAggregations()).willReturn(aggregations);
        given(bucket.getKeyAsString()).willReturn(URL_BUCKET_KEY);
        given(bucket.getDocCount()).willReturn(BUCKET_DOC_COUNT);
        given(aggregationTerms.getBuckets()).will(invocationOnMock -> singletonList(bucket));
        given(aggregationTerms.getName()).willReturn(TOP_URLS_AGGREGATION_NAME);

        List<CompositeMetricBO> compositeMetricBOs = repository.handleCompositeAggregation(singletonList(bucketRegion));

        assertThat(compositeMetricBOs.get(0).getKey(), is(REGION_BUCKET_KEY));
        assertThat(compositeMetricBOs.get(0).getValues().get(0).getKey(), is(URL_BUCKET_KEY));
        assertThat(compositeMetricBOs.get(0).getValues().get(0).getCount(), is(BUCKET_DOC_COUNT));
    }

    @Test
    public void shouldGetMostAccessedMinute() {
        MockitoAnnotations.initMocks(this);
        SearchResponse searchResponse = mock(SearchResponse.class);
        Terms topMinuteAggregation = mock(StringTerms.class);

        Aggregations aggregations = new Aggregations(singletonList(topMinuteAggregation));
        Terms.Bucket bucket = mock(StringTerms.Bucket.class);
        ArgumentCaptor<ActionListener<SearchResponse>> captor = ArgumentCaptor.forClass(ActionListener.class);

        given(bucket.getKeyAsString()).willReturn(MINUTE_BUCKET_KEY);
        given(bucket.getDocCount()).willReturn(BUCKET_DOC_COUNT);
        given(searchResponse.getAggregations()).willReturn(aggregations);
        given(topMinuteAggregation.getBuckets()).will(invocationOnMock -> singletonList(bucket));
        given(topMinuteAggregation.getName()).willReturn(TOP_MINUTE_AGGREGATION_NAME);

        Uni<MetricEntryBO> responseUni = repository.getMostAccessedMinute();

        verify(repository).search(any(), captor.capture());
        ActionListener<SearchResponse> listener = captor.getValue();
        listener.onResponse(searchResponse);

        responseUni.subscribe().with(mostAccessedMinute -> {
            assertThat(mostAccessedMinute.getKey(), is(MINUTE_BUCKET_KEY));
            assertThat(mostAccessedMinute.getCount(), is(BUCKET_DOC_COUNT));
        }, Throwable::printStackTrace);
    }

    @Test
    public void shouldReturnHealth() {
        ArgumentCaptor<ActionListener<ClusterHealthResponse>> captor = ArgumentCaptor.forClass(ActionListener.class);

        //given(repository.)

        Uni<Response.Status> statusUni = repository.healthCheck();

        verify(repository, times(1)).health(captor.capture());
        ActionListener<ClusterHealthResponse> listener = captor.getValue();
        listener.onResponse(mock(ClusterHealthResponse.class));
    }
}