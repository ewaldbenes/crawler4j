package edu.uci.ics.crawler4j.examples.hsqldb.basic;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.filter.UrlFilter;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

final class BasicWikipediaFilter implements UrlFilter {
    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
    private final AtomicInteger numSeenImages;

    BasicWikipediaFilter(AtomicInteger numSeenImages) {
        this.numSeenImages = numSeenImages;
    }

    @Override
    public boolean accept(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase(Locale.ROOT);
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            numSeenImages.incrementAndGet();
            return false;
        }

        // Only accept the url if it is in the "de.wikipedia.org" domain and protocol is "https".
        return href.startsWith("https://de.wikipedia.org");
    }
}
