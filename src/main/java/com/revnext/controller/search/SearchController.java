package com.revnext.controller.search;

import com.revnext.controller.BaseController;
import com.revnext.controller.search.response.GlobalSearchResponse;
import com.revnext.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController extends BaseController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<GlobalSearchResponse> globalSearch(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("Global search: q='{}' page={} size={}", q, page, size);
        return getResponse(() -> searchService.globalSearch(q, page, size));
    }
}
