package com.bi.crawler.crawler.controller;

import com.bi.crawler.crawler.entity.WebPageInfo;
import com.bi.crawler.crawler.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by psagar on 3/9/2018.
 */
@RestController
@RequestMapping(value = "/crawler")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @RequestMapping("/")
    public String greet() {
        return "Welcome to Crawler!!";
    }

    @RequestMapping("/start")
    public WebPageInfo crawl(@RequestParam("url") String url, @RequestParam(value = "maxLink", defaultValue = "10") Integer maxLink) throws IOException {
        int ml = 0;
        if (maxLink != null) {
            ml = maxLink;
        }
        return crawlerService.process(url, ml);
    }
}
