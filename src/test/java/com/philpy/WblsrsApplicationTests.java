package com.philpy;

import com.philpy.config.SpiderHeaderConfig;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.unit.TimeValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@SpringBootTest
class WblsrsApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //判断索引是否存在
    @Test
    void indexExists() throws IOException {
//        System.out.println(restHighLevelClient.indices().exists(new GetIndexRequest().indices("wblsrs"), RequestOptions.DEFAULT));
    }

    //删除索引
    @Test
    void deleteIndex() throws IOException {
//        DeleteIndexRequest indexRequest = new DeleteIndexRequest("wblsrs");
//        AcknowledgedResponse response = restHighLevelClient.indices().delete(indexRequest, RequestOptions.DEFAULT);
//        System.out.println(response);
//        System.out.println(response.isAcknowledged());
    }

    //创建索引
    @Test
    void createIndex() throws IOException {
//        CreateIndexRequest index = new CreateIndexRequest("wblsrs");
//        CreateIndexResponse response = restHighLevelClient.indices().create(index, RequestOptions.DEFAULT);
//        System.out.println(response);
//        System.out.println(response.index());
    }




    //获取索引
    @Test
    void queryIndex() throws IOException {
//        GetIndexRequest indexRequest = new GetIndexRequest("philpy_index");
//        boolean exists = restHighLevelClient.indices().exists(indexRequest, RequestOptions.DEFAULT);
//        System.out.println(exists);
//
//        System.out.println(restHighLevelClient.indices().exists(new GetIndexRequest("000"), RequestOptions.DEFAULT));
    }


    //添加文档
    @Test
    void insertDoc() throws IOException {
//        User user = new User("李子烨", 3);
//        IndexRequest request = new IndexRequest("philpy_index");
//        // PUT /philpy_index/_doc/1
//        request.id("1");
//        request.timeout(TimeValue.timeValueSeconds(1L));
//        request.source(JSON.toJSONString(user), XContentType.JSON);
//        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
//        System.out.println(response);
//        System.out.println(response.getIndex());
//        System.out.println(response.status());
//        System.out.println(response.toString());
    }

    //获取文档
    @Test
    void getDoc() throws IOException {
        // GET /philpy_index/_doc/1
//        GetRequest request = new GetRequest("philpy_index", "1");
//        //不获取返回的 _source 上下文
////        request.fetchSourceContext(new FetchSourceContext(false));
//        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
//        System.out.println(exists);
//        if (exists) {
//            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
//            System.out.println(response.getSourceAsString());
//            System.out.println(response);
//        }
    }

    //更新文档
    @Test
    void updateDoc() throws IOException {
        // GET /philpy_index/_doc/1
//        UpdateRequest request = new UpdateRequest("philpy_index", "1");
//        request.timeout(TimeValue.timeValueSeconds(1L));
////        User user = new User("李沛洋2", 20);
////        request.doc(JSON.toJSONString(user), XContentType.JSON);
//        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
//        System.out.println(response);
//        System.out.println(response.status());
//        System.out.println(response.toString());
    }

    //删除文档
    @Test
    void deleteDoc() throws IOException {
//        DeleteRequest request = new DeleteRequest("philpy_index", "1");
//        request.timeout(TimeValue.timeValueSeconds(1L));
//        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
//        System.out.println(response.status());
//        System.out.println(response);
    }

    //批处理
    //批量插入
    @Test
    void insertManyDoc() throws IOException {
//        BulkRequest request = new BulkRequest();
//        request.timeout(TimeValue.timeValueSeconds(1L));
//        ArrayList<User> users = new ArrayList<>();
//        users.add(new User("李沛洋", 27));
//        users.add(new User("李沛洋2", 25));
//        users.add(new User("李沛洋2", 20));
//        users.add(new User("李沛洋4", 21));
//        users.add(new User("李沛洋5", 22));
//        for (int i = 0; i < users.size(); i++) {
//            request.add(new IndexRequest("philpy_index")
//                    .id(i + "") //id相同会覆盖，不指定id是随机id
//                    .source(JSON.toJSONString(users.get(i)), XContentType.JSON));
//        }
//        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
//        System.out.println(response);
//        System.out.println(response.status());
//        System.out.println(response.hasFailures());
    }

    //查询
    @Test
    void queryDoc() throws IOException {
//        SearchRequest request = new SearchRequest("wblsrs");
//        //构建搜索条件
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        //精确查询
////        TermQueryBuilder matchPhraseQueryBuilder = QueryBuilders.termQuery("title.keyword", "广州疫情");
//        //模糊查询
//        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("title", "人");
////        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("time", "2021-06-02T22:31");
//        SearchSourceBuilder sourceBuilder = searchSourceBuilder.query(matchPhraseQueryBuilder);
//        sourceBuilder.timeout(TimeValue.timeValueSeconds(1L));
//        sourceBuilder.size(100);
//        sourceBuilder.sort("time");
//        request.source(sourceBuilder);
//        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
//        System.out.println(response);
//        SearchHit[] hits = response.getHits().getHits();
//        for (SearchHit hit : hits) {
//            System.out.println(hit.getSourceAsMap());
//            System.out.println("*************");
//        }
//        System.out.println("首次上榜时间： " + hits[0].getSourceAsMap().get("time"));
//        System.out.println("最后在榜时间： " + hits[hits.length - 1].getSourceAsMap().get("time"));
    }

    //聚合查询
    @Test
    void aggDoc() throws IOException {
        //构建搜索条件
//        List<Map<String, Object>> list = new ArrayList<>();
//        SearchRequest request = new SearchRequest("wblsrs");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        SearchSourceBuilder sourceBuilder = searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("title", "人"));
//        sourceBuilder.size(0);
//        List<FieldSortBuilder> fieldSorts = new ArrayList<>();
//        fieldSorts.add(new FieldSortBuilder("first").order(SortOrder.DESC));
//        TermsAggregationBuilder agg = AggregationBuilders
//                .terms("result")
//                .field("title.keyword")
//                .order(BucketOrder.aggregation("first", true))
//                .size(99999999)
//                .subAggregation(AggregationBuilders.min("first").field("time"))
//                .subAggregation(AggregationBuilders.max("last").field("time"))
//                .subAggregation(new BucketSortPipelineAggregationBuilder("bucket_field", fieldSorts).from(10).size(10));
//        sourceBuilder.aggregation(agg);
//        sourceBuilder.fetchSource(new String[]{"title"}, null);
//        request.source(searchSourceBuilder);
//        Terms result = restHighLevelClient.search(request, RequestOptions.DEFAULT).getAggregations().get("result");
//        for (Bucket bucket : result.getBuckets()) {
//            Map<String, Object> map = new HashMap<>();
//            Aggregations bucketAggregations = bucket.getAggregations();
//            Min first = bucketAggregations.get("first");
//            Max last = bucketAggregations.get("last");
//            map.put("title", bucket.getKeyAsString());
//            map.put("first", first.getValueAsString());
//            map.put("last", last.getValueAsString());
//            list.add(map);
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", "0");
//        map.put("data", list);
//        System.out.println(JSON.toJSONString(map));
    }

    @Test
    void preciseQueryDoc() throws IOException {
//        //查询
//        SearchRequest request = new SearchRequest("wblsrs");
//        //构建搜索条件
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        //精确查询
//        TermQueryBuilder matchPhraseQueryBuilder = QueryBuilders.termQuery("title.keyword", "广州疫情");
//        //模糊查询
////        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("title", "人");
////        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("time", "2021-06-02T22:31");
//        SearchSourceBuilder sourceBuilder = searchSourceBuilder.query(matchPhraseQueryBuilder);
//        sourceBuilder.timeout(TimeValue.timeValueSeconds(1L));
//        sourceBuilder.size(100);
//        sourceBuilder.sort("time");
//        request.source(sourceBuilder);
//        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
////        System.out.println(response);
//        SearchHit[] hits = response.getHits().getHits();
//        List<Integer> rankList = new ArrayList<>();
//        List<String> timeList = new ArrayList<>();
//        List<Integer> hotList = new ArrayList<>();
//        for (SearchHit hit : hits) {
//            Map<String, Object> map = hit.getSourceAsMap();
////            System.out.println(hit.getSourceAsMap());
//            rankList.add((Integer) map.get("rank"));
//            timeList.add(map.get("time").toString().replace("T", " ").substring(0, 16));
//            hotList.add((Integer) map.get("hot"));
//        }
//        Map<String, Object> result = new HashMap<>();
//        result.put("title", "广州疫情");
//        result.put("rankList", rankList);
//        result.put("timeList", timeList);
//        result.put("hotList", hotList);
//        System.out.println(JSON.toJSONString(result));
    }
}
