package edu.uci.ics.crawler4j.crawler.filter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public interface UrlFilter {
    /**
     * Tells the crawler whether the given url should be crawled or not.
     *
     * @param url           the url which we are interested to know whether it should be
     *                      included in the crawl or not.
     * @param referringPage The Page in which this url was found.
     * @return if the url should be included in the crawl it returns true,
     * otherwise false is returned.
     */
    boolean accept(Page referringPage, WebURL url);
}
