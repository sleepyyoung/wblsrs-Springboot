package com.philpy.controller;

import com.alibaba.fastjson.JSON;
import com.philpy.config.SpiderHeaderConfig;
import com.philpy.pojo.Wblsrs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.pipeline.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "Elasticsearch?????????????????????????????????",
        description = "???????????????????????????Elasticsearch????????????API?????????/api/?????????")
@RequestMapping("/api/")
@RestController
public class ElasticsearchController {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Value("${wblsrs.map.cities}")
    private List<String> cities;

    @Value("${wblsrs.map.provinces}")
    private List<String> provinces;

    @Value("${wblsrs.url.realtimehot}")
    private String realtimehot;

    @Value("${wblsrs.url.socialevent}")
    private String socialevent;

    @Autowired
    private SpiderHeaderConfig headerConfig;

    /*
     * GET /wblsrs/_search
     * {
     *      "query": {
     *      "match_phrase": {
     *      "time": "XXXX-XX-XXTXX:XX:XX"
     *      }
     * },
     *      "size": 100
     * }
     */
    @ApiOperation(value = "???????????????")
    @GetMapping("/query-by-time/{time}")
    public String queryByTime(@PathVariable("time") String time) throws IOException {
        String timeQ = time.replace(" ", "T").substring(0, 16);
        SearchRequest request = new SearchRequest("wblsrs");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchPhraseQuery("time", timeQ));
        sourceBuilder.timeout(TimeValue.timeValueSeconds(1L));
        sourceBuilder.size(100);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> hitSourceAsMap = hit.getSourceAsMap();
            hitSourceAsMap.put("time", timeQ.replace("T", " "));
            list.add(hitSourceAsMap);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0");
        map.put("data", list);
        return JSON.toJSONString(map);
    }

    private int getAggregationNum(String content) throws IOException {
        SearchRequest request = new SearchRequest("wblsrs");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchSourceBuilder sourceBuilder = searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("title", content));
        sourceBuilder.size(0);
        TermsAggregationBuilder agg = AggregationBuilders
                .terms("result")
                .field("title.keyword")
                .order(BucketOrder.aggregation("first", true))
                .size(99999999)
                .subAggregation(AggregationBuilders.min("first").field("time"))
                .subAggregation(AggregationBuilders.max("last").field("time"));
        sourceBuilder.aggregation(agg);
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Terms result = response.getAggregations().get("result");
        List<? extends Terms.Bucket> buckets = result.getBuckets();
        return buckets.size();
    }

    /*
     * GET /wblsrs/_search
     * {
     *   "query": {
     *     "match_phrase": {
     *       "title": ????????????
     *     }
     *   },
     *   "size": 0,
     *   "aggs": {
     *     "result": {
     *       "terms": {
     *         "field": "title.keyword",
     *         "size":999999999
     *       },
     *       "aggs": {
     *         "first": {
     *           "min": {
     *             "field": "time"
     *           }
     *         },
     *         "last": {
     *           "max": {
     *             "field": "time"
     *           }
     *         },
     *         "r_bucket_sort": {
     *           "bucket_sort": {
     *             "sort": [
     *               {
     *                 "first": "asc"
     *               }
     *             ],
     *             "from": (page - 1) * limit,
     *             "size": limit
     *           }
     *         }
     *       }
     *     }
     *   }
     * }
     */
    @ApiOperation(value = "?????????????????????")
    @GetMapping("/fuzzy-query-by-content/{content}")
    public String fuzzyQueryByContent(@PathVariable("content") String content,
                                      @RequestParam("page") int page,
                                      @RequestParam("limit") int limit) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequest request = new SearchRequest("wblsrs");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchSourceBuilder sourceBuilder = searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("title", content));
        sourceBuilder.size(0);
        List<FieldSortBuilder> fieldSorts = new ArrayList<>();
        fieldSorts.add(new FieldSortBuilder("first").order(SortOrder.DESC));
        TermsAggregationBuilder agg = AggregationBuilders
                .terms("result")
                .field("title.keyword")
                .order(BucketOrder.aggregation("first", true))
                .size(999999999)
                .subAggregation(AggregationBuilders.min("first").field("time"))
                .subAggregation(AggregationBuilders.max("last").field("time"))
                .subAggregation(new BucketSortPipelineAggregationBuilder("bucket_field", fieldSorts)
                        .from((page - 1) * limit)
                        .size(limit));
        sourceBuilder.aggregation(agg);
        sourceBuilder.fetchSource(new String[]{"title"}, null);
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Terms result = response.getAggregations().get("result");
        List<? extends Terms.Bucket> buckets = result.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            Map<String, Object> map = new HashMap<>();
            Aggregations bucketAggregations = bucket.getAggregations();
            Min first = bucketAggregations.get("first");
            Max last = bucketAggregations.get("last");
            map.put("title", bucket.getKeyAsString().replaceAll(content, "<span style='color:red'>" + content + "</span>"));
            map.put("first", first.getValueAsString().replace("T", " ").substring(0, 16));
            map.put("last", last.getValueAsString().replace("T", " ").substring(0, 16));
            list.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0");
        map.put("data", list);
        map.put("count", getAggregationNum(content));
        return JSON.toJSONString(map);
    }

    /*
     * GET /wblsrs/_search
     * {
     *   "query": {
     *     "term": {
     *       "title.keyword": ????????????
     *     }
     *   },
     *   "size": 10000
     * }
     */
    @ApiOperation(value = "?????????????????????")
    @GetMapping("/precise-query-by-content/{content}")
    public String preciseQueryByContent(@PathVariable("content") String content) throws IOException {
        System.out.println(content);
        SearchRequest request = new SearchRequest("wblsrs");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder matchPhraseQueryBuilder = QueryBuilders.termQuery("title.keyword", content);
        SearchSourceBuilder sourceBuilder = searchSourceBuilder.query(matchPhraseQueryBuilder);
        sourceBuilder.timeout(TimeValue.timeValueSeconds(10L));
        sourceBuilder.size(10000);
        sourceBuilder.sort("time");
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<Integer> rankList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        List<Integer> hotList = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            rankList.add((Integer) map.get("rank"));
            timeList.add(map.get("time").toString().replace("T", " ").substring(0, 16));
            hotList.add((Integer) map.get("hot"));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("title", content);
        result.put("rankList", rankList);
        result.put("timeList", timeList);
        result.put("hotList", hotList);
        return JSON.toJSONString(result);
    }

    /*
     * GET /wblsrs/_search
     * {
     *   "query": {
     *     "bool": {
     *       "filter": {
     *         "range": {
     *           "time": {
     *             "gte": ????????????,
     *             "lte": now
     *           }
     *         }
     *       }
     *     }
     *   },
     *   "size": 0,
     *   "aggs": {
     *     "result": {
     *       "terms": {
     *         "field": "title.keyword",
     *         "size": 99999999
     *       },
     *       "aggs": {
     *         "sum": {
     *           "sum": {
     *             "field": "hot"
     *           }
     *         }
     *       }
     *     }
     *   }
     * }
     */
    @ApiOperation(value = "???/wblsrs/??????????????????API", notes = "??????00:00?????????????????????????????????")
    @GetMapping("/today-hot-wc")
    public String todayHotWordCloud() throws IOException {
        SearchRequest request = new SearchRequest("wblsrs");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("time")
                .from(LocalDateTime.now().toString().split("T")[0] + "T00:00")
                .to(LocalDateTime.now().toString().split("\\.")[0]);
        boolQueryBuilder.filter(rangeQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10L));
        searchSourceBuilder.size(0);
        TermsAggregationBuilder agg = AggregationBuilders
                .terms("result")
                .field("title.keyword")
                .size(99999999)
                .subAggregation(AggregationBuilders.sum("sum").field("hot"));
        searchSourceBuilder.aggregation(agg);
        request.source(searchSourceBuilder);
        Terms result = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            result = restHighLevelClient.search(request, RequestOptions.DEFAULT).getAggregations().get("result");
            list = new ArrayList<>();
            for (Terms.Bucket bucket : result.getBuckets()) {
                Map<String, Object> map = new HashMap<>();
                Aggregations bucketAggregations = bucket.getAggregations();
                Sum sum = bucketAggregations.get("sum");
                double s = sum.getValue();
                if (s != 0) {
                    map.put("name", bucket.getKeyAsString());
                    map.put("value", BigDecimal.valueOf(s).toPlainString().split("\\.")[0]);
                    list.add(map);
                }
            }
        } catch (ElasticsearchStatusException ignored) {
        }
        return JSON.toJSONString(list);
    }

    /*
     * ??????????????????/????????????????????????????????????????????????
     *
     * GET /wblsrs/_search
     * {
     *   "query": {
     *     "bool": {
     *       "must": [
     *         {
     *           "match_phrase": {
     *             "title": ?????????/?????????
     *           }
     *         }
     *       ],
     *       "filter": {
     *         "range": {
     *           "time": {
     *             "gte": ?????????,
     *             "lte": now
     *           }
     *         }
     *       }
     *     }
     *   },
     *   "size": 0,
     *   "aggs": {
     *     "result": {
     *       "terms": {
     *         "field": "title.keyword",
     *         "size": 999999999
     *       }
     *     }
     *   }
     * }
     */
    private int recentWeekFrequency(String city) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        SearchRequest request = new SearchRequest("wblsrs");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("title", city));
        boolQueryBuilder.filter(QueryBuilders
                .rangeQuery("time")
                .from(now.plusDays(-7L).toString().split("\\.")[0])
                .to(now.toString().split("\\.")[0]));
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(AggregationBuilders
                .terms("result")
                .field("title.keyword")
                .size(999999999));
        request.source(searchSourceBuilder);
        try {
            Terms result = restHighLevelClient.search(request, RequestOptions.DEFAULT).getAggregations().get("result");
            return result.getBuckets().size();
        } catch (ElasticsearchStatusException ignored) {
            return 0;
        }
    }


    @ApiOperation(value = "??????????????????????????????API", notes = "????????????????????????????????????????????????")
    @GetMapping("/recent-week-cities-map")
    public String recentWeekCitiesMap() throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (String city : cities) {
            Map<String, Object> map = new HashMap<>();
            int value = recentWeekFrequency(city);
            if (value != 0) {
                map.put("name", city);
                map.put("value", value);
                list.add(map);
            }
        }
        return JSON.toJSONString(list);
    }

    @ApiOperation(value = "??????????????????????????????API", notes = "????????????????????????????????????????????????")
    @GetMapping("/recent-week-provinces-map")
    public String recentWeekProvincesMap() throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (String province : provinces) {
            Map<String, Object> map = new HashMap<>();
            int value = recentWeekFrequency(province);
            if (value != 0) {
                map.put("name", province);
                map.put("value", value);
                list.add(map);
            }
        }
        return JSON.toJSONString(list);
    }

    @ApiOperation(value = "???????????????API", notes = "??????????????????")
    @GetMapping("/now-realtimehot")
    public String nowRealtimehot() throws IOException {
        ArrayList<Wblsrs> list = new ArrayList<>();
        Document document = Jsoup.connect(realtimehot).headers(headerConfig.getHeader()).get();
        Element id = document.getElementById("pl_top_realtimehot");
        for (Element element : id.getElementsByTag("tr")) {
            if (!"thead_tr".equals(element.className())) {
                Elements tds = element.getElementsByTag("td");
                if (!"???".equals(tds.get(2).text())) {
                    int rank;
                    try {
                        rank = Integer.parseInt(tds.get(0).text());
                    } catch (Exception e) {
                        rank = 0;
                    }
                    String title = tds.get(1).getElementsByTag("a").get(0).text();
                    int hot;
                    try {
                        hot = Integer.parseInt(tds.get(1).getElementsByTag("span").get(0).text());
                    } catch (Exception e) {
                        hot = 0;
                    }
                    list.add(new Wblsrs(rank, title, hot));
                }
            }
        }
        return JSON.toJSONString(list);
    }

    @ApiOperation(value = "???????????????API", notes = "??????????????????")
    @GetMapping("/now-socialevent")
    public String nowSocialevent() throws IOException {
        ArrayList<Wblsrs> list = new ArrayList<>();
        Document document = Jsoup.connect(socialevent).headers(headerConfig.getHeader()).get();
        Element id = document.getElementById("pl_top_realtimehot");
        for (Element element : id.getElementsByTag("tr")) {
            if (!"thead_tr".equals(element.className())) {
                String title = element.getElementsByTag("td").get(1).getElementsByTag("a").get(0).text();
                list.add(new Wblsrs(title));
            }
        }
        return JSON.toJSONString(list);
    }
}
