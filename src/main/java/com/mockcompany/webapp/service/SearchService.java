package com.mockcompany.webapp.service;

import com.mockcompany.webapp.api.SearchReportResponse;
import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SearchService {
    private final ProductItemRepository productItemRepository;

    @Autowired
    SearchService(ProductItemRepository productItemRepository){
        this.productItemRepository = productItemRepository;
    }

    public Collection<ProductItem> doSearch(String query) {

        Iterable<ProductItem> allItems = this.productItemRepository.findAll();
        List<ProductItem> itemList = new ArrayList<>();
        boolean exactMatch = false;
        if (query.startsWith("\"") || query.endsWith("\"")){
            exactMatch = true;
            query = query.substring(1,query.length() - 1);
        }else {
            query = query.toLowerCase();
        }

        for (ProductItem item : allItems) {
            boolean nameMatch;
            boolean descMatch;
            if (exactMatch){
                nameMatch = item.getName().equals(query);
                descMatch = item.getDescription().equals(query);
            }else {
                nameMatch = item.getName().toLowerCase().contains(query);
                descMatch = item.getDescription().toLowerCase().contains(query);
            }
            if (nameMatch || descMatch) {
                itemList.add(item);
            }

        }
        return Collections.emptyList();
    }
}
