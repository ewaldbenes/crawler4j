package edu.uci.ics.crawler4j.examples.basic;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.filter.UrlFilter;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

final class BasicUrlFilter implements UrlFilter {
    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");

    private final AtomicInteger numSeenImages;

    BasicUrlFilter(AtomicInteger numSeenImages) {
        this.numSeenImages = numSeenImages;
    }

    @Override
    public boolean accept(Page referringPage, WebURL url) {
        String href = url.getURL().toString();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            numSeenImages.incrementAndGet();
            return false;
        }

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return href.startsWith("https://www.ics.uci.edu/");
    }
}
