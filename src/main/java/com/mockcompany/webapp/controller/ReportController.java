package com.mockcompany.webapp.controller;

import com.mockcompany.webapp.api.SearchReportResponse;
import com.mockcompany.webapp.model.ProductItem;
import com.mockcompany.webapp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Management decided it is super important that we have lots of products that match the following terms.
 * So much so, that they would like a daily report of the number of products for each term along with the total
 * product count.
 */
@RestController
public class ReportController {

    /**
     * The people that wrote this code didn't know about JPA Spring Repository interfaces!
     */

    private static final String[] importantTerms = new String[] {
            "Cool",
            "Amazing",
            "Perfect",
            "Kids"
    };
    private final SearchService searchService;
    private final EntityManager entityManager;

    @Autowired
    public ReportController(SearchService searchService,EntityManager entityManager) {
        this.searchService = searchService;
        this.entityManager = entityManager;
    }


    @GetMapping("/api/products/report")
    public SearchReportResponse runReport() {
        Number count = (Number) this.entityManager.createQuery("SELECT count(item) FROM ProductItem item").getSingleResult();

        Map<String, Integer> hits = new HashMap<>();
        for (String term: importantTerms){
            hits.put(term,searchService.doSearch(term).size());
        }
        SearchReportResponse response = new SearchReportResponse();
        response.setProductCount(count.intValue());
        response.setSearchTermHits(hits);
        return response;
    }
}
