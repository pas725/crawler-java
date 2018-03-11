package com.bi.crawler.crawler.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class contains data discovered by crawling given Url identified by "pageUrl" field.
 *      staticUrls      - It contains list of static files Url
 *      externalUrls    - It contains list of external Urls (Out of domain)
 *      internalUrls    - It contains list of internal Urls (within same domain)
 *
 *      pages           - It contains list of "WebPageInfo" objects, for each internal url found within page.
 */
@Data
public class WebPageInfo {
    private String pageUrl;
    private Set<String> staticUrls = new HashSet<>();
    private Set<String> externalUrls = new HashSet<>();
    private Set<String> internalUrls = new HashSet<>();
    private List<WebPageInfo> pages = new ArrayList<>();

    public boolean checkEmpty() {
        return staticUrls.isEmpty() && externalUrls.isEmpty() && internalUrls.isEmpty();
    }
}
