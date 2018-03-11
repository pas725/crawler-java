package com.bi.crawler.crawler;

import com.bi.crawler.crawler.business.Crawler;
import com.bi.crawler.crawler.entity.CrawlEntity;
import com.bi.crawler.crawler.entity.WebPageInfo;
import com.bi.crawler.crawler.entity.UrlType;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class CrawlerTest {

    @InjectMocks
    private Crawler crawler = new Crawler();

    @Test
    public void shouldGetInternalUrlType() throws MalformedURLException {
        URL url1 = new URL("https://wiprodigital.com");
        CrawlEntity crawlEntity = new CrawlEntity(url1, 5);
        UrlType urlType = crawler.getUrlType(crawlEntity, "https://wiprodigital.com/who-we-are#wdteam_meetus");
        Assert.assertEquals(UrlType.INTERNAL, urlType);
    }

    @Test
    public void shouldGetExternalUrlType() throws MalformedURLException {
        URL url1 = new URL("https://wiprodigital.com");
        CrawlEntity crawlEntity = new CrawlEntity(url1, 5);
        UrlType urlType = crawler.getUrlType(crawlEntity, "https://www.google.com/who-we-arr");
        Assert.assertEquals(UrlType.EXTERNAL, urlType);
    }

    @Test
    public void shouldGetStaticUrlType() throws MalformedURLException {
        URL url1 = new URL("https://wiprodigital.com");
        CrawlEntity crawlEntity = new CrawlEntity(url1, 5);
        UrlType urlType = crawler.getUrlType(crawlEntity, "https://www.google.com/my-image.jpg");
        Assert.assertEquals(UrlType.STATIC_FILE, urlType);
    }

    @Test
    public void shouldGetInvalidUrlType() throws MalformedURLException {
        URL url1 = new URL("https://wiprodigital.com");
        CrawlEntity crawlEntity = new CrawlEntity(url1, 5);
        UrlType urlType = crawler.getUrlType(crawlEntity, "jklsaijlsxlj");
        Assert.assertEquals(UrlType.INVALID, urlType);
    }

    @Test
    public void shouldGetInvalidUrlTypeTestWithNullUrl() throws MalformedURLException {
        URL url1 = new URL("https://wiprodigital.com");
        CrawlEntity crawlEntity = new CrawlEntity(url1, 5);
        UrlType urlType = crawler.getUrlType(crawlEntity, null);
        Assert.assertEquals(UrlType.INVALID, urlType);
    }

    @Test
    public void shouldGetInvalidUrlTypeTestWithNullDomain() throws MalformedURLException {
        URL url1 = new URL("https://wiprodigital.com");
        CrawlEntity crawlEntity = new CrawlEntity(url1, 5);
        UrlType urlType = crawler.getUrlType(null, "hjkhk");
        Assert.assertEquals(UrlType.INVALID, urlType);
    }

    @Test
    public void processPageShouldFetchNull() throws MalformedURLException {
        WebPageInfo webPageInfo = crawler.crawl(null, null, null);
        Assert.assertNull(webPageInfo);
    }

    @Test
    public void processPageShouldFetchValidData() throws MalformedURLException {
        String url = "https://wiprodigital.com";
        URL url1 = new URL(url);
        CrawlEntity crawlEntity = new CrawlEntity(url1, 2);
        Set<String> visited = new HashSet<>();

        List<String> emptyList = new ArrayList<>();
        Crawler crawlerSpy = Mockito.spy(crawler);
        List<String> hrefs = new ArrayList<>();
        hrefs.add("https://www.facebook.com/WiproDigital/");
        hrefs.add("https://wiprodigital.com/who-we-are#wdteam_meetus");
        hrefs.add("https://wiprodigital.com/myimage.jpg");
        Mockito.doReturn(hrefs, emptyList).when(crawlerSpy).connectAndGetHrefFromPage(Mockito.anyString());
        WebPageInfo webPageInfo = crawlerSpy.crawl(crawlEntity, url, visited);
        Assert.assertEquals(1, webPageInfo.getExternalUrls().size());
        Assert.assertEquals(1, webPageInfo.getInternalUrls().size());
        Assert.assertEquals(1, webPageInfo.getStaticUrls().size());
    }

}
