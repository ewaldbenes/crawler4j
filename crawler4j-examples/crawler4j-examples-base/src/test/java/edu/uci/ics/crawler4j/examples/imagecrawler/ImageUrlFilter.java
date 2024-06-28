package edu.uci.ics.crawler4j.examples.imagecrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.filter.UrlFilter;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

record ImageUrlFilter(List<String> allowedDomains) implements UrlFilter {
    private static final Pattern filters = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                    "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

    @Override
    public boolean accept(Page referringPage, WebURL url) {
        String href = url.getURL().toString();
        if (filters.matcher(href).matches()) {
            return false;
        }

        if (imgPatterns.matcher(href).matches()) {
            return true;
        }

        for (String domain : allowedDomains) {
            if (href.startsWith(domain)) {
                return true;
            }
        }
        return false;
    }
}
