package com.leonp967.log.ingesting.repository;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.elasticsearch.aggregations.builder.MetricAggregationsBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.leonp967.log.ingesting.elasticsearch.aggregations.builder.MetricAggregationsBuilder.TOP_URLS_AGGREGATION_NAME;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class LogAnalyticsRepositoryTest {

    private static LogAnalyticsRepository repository;
    private static MetricAggregationsBuilder builder;
    private static final String URL_BUCKET_KEY = "https://google.com";
    private static final String REGION_BUCKET_KEY = "us-west-2";
    private static final Long BUCKET_DOC_COUNT = 20L;

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
}