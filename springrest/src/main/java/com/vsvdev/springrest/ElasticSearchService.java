package com.vsvdev.springrest;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.tasks.ElasticsearchException;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ElasticSearchService {
private final static String INDEX_NAME = "articles";
private final ObjectMapper mapper = new ObjectMapper();
private final RestHighLevelClient restHighLevelClient;

@Autowired
    public ElasticSearchService(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public void updateArticle(String id, String title, String text) throws Exception {
Article article = new Article();
article.setTitle(title);
article.setText(text);

    IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
    indexRequest.id(id);
    indexRequest.source(mapper.writeValueAsString(article), XContentType.JSON);

    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
}

    public List<Article> search(String searchString) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      //  searchSourceBuilder.query(QueryBuilders.matchQuery("text", searchString));
             // only phrase
       // searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("text", searchString));
            //to find in text and title
       searchSourceBuilder.query(QueryBuilders.multiMatchQuery(searchString, "title", "text"));


        /*

        //match must and mustNot exclude from result
        searchSourceBuilder.query(QueryBuilders.boolQuery()
        .must(QueryBuilders.multiMatchQuery(searchString, "title", "text"))
        .mustNot(QueryBuilders.multiMatchQuery("sport", "title", "text")));




         */


        //not correct word(with mistake)
        //  searchSourceBuilder.query(QueryBuilders.multiMatchQuery(searchString, "title", "text").fuzziness("AUTO"));

        // from position 1 this mean that 0 not be displayed
       // searchSourceBuilder.from(1);
        //displayed 10 element
       // searchSourceBuilder.size(10);

HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field(new HighlightBuilder.Field("title"))
                .field(new HighlightBuilder.Field("text"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Article> articles = new ArrayList<>();
          for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String title = (String) sourceAsMap.get("title");
            String text = (String) sourceAsMap.get("text");

            HighlightField highlightFieldText = hit.getHighlightFields().get("text");
            if (highlightFieldText != null && highlightFieldText.fragments().length > 0) {
                text = highlightFieldText.fragments()[0].toString();
            }

            Article article = new Article();
            article.setTitle(title);
            article.setText(text);
            articles.add(article);
        }

        return articles;

    }

public long getCount() throws IOException {
    CountRequest countRequest = new CountRequest();
CountResponse count = restHighLevelClient.count(countRequest,RequestOptions.DEFAULT);
    return count.getCount();
}

    public static class Article{
    private String title;
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


}
