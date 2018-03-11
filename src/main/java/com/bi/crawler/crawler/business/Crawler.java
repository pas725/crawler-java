package com.bi.crawler.crawler.business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bi.crawler.crawler.entity.CrawlEntity;
import com.bi.crawler.crawler.entity.WebPageInfo;
import com.bi.crawler.crawler.entity.UrlType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class Crawler {

    /**
     * Connects to given web url and returns all href elements within that page.
     * @param url
     * @return
     */
    public List<String> connectAndGetHrefFromPage(String url) {
        Document page = null;
        try {
            page = Jsoup.connect(url).get();
            Elements links = page.select("a[href]");
            return links.stream().map(l -> l.attr("href")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Crawler engine recursively crawls given url.
     *      - It will crawl until the MAX_CRAWL limit is hit.
     * @param domain
     * @param url
     * @param visited
     * @return
     */
    public WebPageInfo crawl(CrawlEntity domain, String url, Set<String> visited) {

        if (domain == null || url == null || visited == null) {
            return null;
        }

        if (!visited.contains(url) && visited.size() <= domain.getMAX_LINK_TO_CRAWL()) {
            visited.add(url);
            WebPageInfo webPageInfo = new WebPageInfo();
            webPageInfo.setPageUrl(url);
            List<String> pageHrefs = connectAndGetHrefFromPage(url);
            for (String linkUrl : pageHrefs) {
                System.out.println(linkUrl);
                UrlType urlType = UrlType.INTERNAL;
                if (linkUrl.startsWith("/") && !linkUrl.startsWith("//")) {
                    linkUrl = domain.builWithRelativeUrl(linkUrl);
                } else {
                    urlType = getUrlType(domain, linkUrl);
                }

                if (urlType.equals(UrlType.INTERNAL)) {
                    webPageInfo.getInternalUrls().add(linkUrl);
                    WebPageInfo nestedPage = crawl(domain, linkUrl, visited);
                    if (nestedPage != null) {
                        webPageInfo.getPages().add(nestedPage);
                    }
                } else if (urlType.equals(UrlType.EXTERNAL)) {
                    webPageInfo.getExternalUrls().add(linkUrl);
                } else if (urlType.equals(UrlType.STATIC_FILE)) {
                    webPageInfo.getStaticUrls().add(linkUrl);
                }
            }
            if (webPageInfo.checkEmpty()) {
                return null;
            }
            return webPageInfo;
        }
        return null;
    }

    /**
     * Determine the type of given Url.
     * @param domain
     * @param url
     * @return
     */
    public UrlType getUrlType(CrawlEntity domain, String url) {

        if (domain == null || url == null || url.length() == 0) {
            return UrlType.INVALID;
        }

        if (url.endsWith(".txt") || url.endsWith(".pdf")
                || url.endsWith(".jpg") || url.endsWith(".xls")
                || url.endsWith(".ppt") || url.endsWith(".mpeg")
                || url.endsWith(".mp3") || url.endsWith(".mp4")) { // Need to add more extensions
            return UrlType.STATIC_FILE;
        } else if (domain.withinSameDomain(url)) {
             return UrlType.INTERNAL;
         } else if (!url.startsWith("#") && isValidUrl(url)) {
             return UrlType.EXTERNAL;
         }
        return UrlType.INVALID;
    }

    /**
     * Validates given Url.
     * @param
     * @return
     */
    public boolean isValidUrl(String url) {
        try {
            URL url1 = new URL(url);
            return true;
        } catch (MalformedURLException e) {
        }
        return false;
    }

}
