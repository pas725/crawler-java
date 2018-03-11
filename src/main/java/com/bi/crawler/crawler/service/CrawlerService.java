package com.bi.crawler.crawler.service;

import com.bi.crawler.crawler.business.Crawler;
import com.bi.crawler.crawler.entity.CrawlEntity;
import com.bi.crawler.crawler.entity.WebPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Service
public class CrawlerService {

    @Autowired
    private Crawler crawler;

    /**
     * Entry point to crawl given Url.
     * @param url
     * @param maxLink - Crawler will crawl only this many links.
     * @return
     */
    public WebPageInfo process(String url, int maxLink) {
        try {
            URL url1 = new URL(url);
            CrawlEntity crawlEntity = new CrawlEntity(url1, maxLink);
            Set<String> visited = new HashSet<>();
            return crawler.crawl(crawlEntity,url, visited);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Provide valid URL to crawl.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
