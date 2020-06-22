//package com.nr.bookstore.model.elasticsearch;
//
//import org.elasticsearch.action.search.SearchResponse;
//import org.springframework.data.elasticsearch.core.ResultsExtractor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ESSearchResult<Doc> implements ResultsExtractor<ESSearchResult<Doc>> {
//
//    private long totalHits;
//    private List<Doc> docs = new ArrayList<>();
//
//    @Override
//    public ESSearchResult extract(SearchResponse searchResponse) {
//        this.totalHits = searchResponse.getHits().getTotalHits();
//        searchResponse.getHits().forEach(hit -> this.docs.add(hit.getS));
//    }
//}
