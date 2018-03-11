package com.bi.crawler.crawler.entity;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represents metadata about url to be crawl.
 * By default it can crawl only 10 links.
 */
public class CrawlEntity {
    private String protocol;
    private String host;
    private int MAX_LINK_TO_CRAWL = 10;

    public CrawlEntity(URL url) {
        this.protocol = url.getProtocol();
        this.host = url.getHost();
    }

    public CrawlEntity(URL url, int maxLink) {
        this.protocol = url.getProtocol();
        this.host = url.getHost();
        if (maxLink > 0) {
            this.MAX_LINK_TO_CRAWL = maxLink;
        }
    }

    public int getMAX_LINK_TO_CRAWL() {
        return MAX_LINK_TO_CRAWL;
    }

    /**
     * Builds absolute url by giving relative url as parameter.
     * @param url
     * @return
     */
    public String builWithRelativeUrl(String url) {
        return this.protocol + "://" + this.host + url;
    }

    /**
     * Checks whether given url is whithin same domain or not.
     * @param url
     * @return
     */
    public boolean withinSameDomain(String url) {
        try {
            URL url1 = new URL(url);
            return this.host.equals(url1.getHost());
        } catch (MalformedURLException e) {
        }
        return false;
    }
}
