package com.philpy.Utils;

import com.alibaba.fastjson.JSON;
import com.philpy.pojo.Wblsrs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Spider {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void doSpider() throws IOException {
        String url = "https://s.weibo.com/top/summary?cate=realtimehot";
        Document document = Jsoup.parse(new URL(url), 30000);
        Element id = document.getElementById("pl_top_realtimehot");
        Elements elementsByTag = id.getElementsByTag("tr");
        ArrayList<Wblsrs> list = new ArrayList<>();
        BulkRequest request = new BulkRequest();
        request.timeout(TimeValue.timeValueSeconds(30L));
        for (Element element : elementsByTag) {
            if (!element.className().equals("thead_tr")) {
                Elements tds = element.getElementsByTag("td");
                if (!tds.get(2).text().equals("荐")) {
                    int rank;
                    try {
                        rank = Integer.parseInt(tds.get(0).text());
                    } catch (Exception e) {
                        rank = 0;
                    }
                    String title = tds.get(1).getElementsByTag("a").get(0).text();
                    int hot;
                    LocalDateTime time = LocalDateTime.now();
                    String tag = tds.get(2).text();
                    try {
                        hot = Integer.parseInt(tds.get(1).getElementsByTag("span").get(0).text());
                    } catch (Exception e) {
                        hot = 0;
                    }
                    list.add(new Wblsrs(rank, title, hot, tag, time));
                }
            }
        }
        for (Wblsrs wblsrs : list) {
            request.add(new IndexRequest("wblsrs").source(JSON.toJSONString(wblsrs), XContentType.JSON));
        }
        restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    }
}
