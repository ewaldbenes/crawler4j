package edu.uci.ics.crawler4j.crawler.filter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class UrlFilters {
    public static final class ChainedUrlFilter implements UrlFilter {
        private final UrlFilter first;
        private final UrlFilter second;

        public ChainedUrlFilter(UrlFilter first, UrlFilter second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean accept(Page referringPage, WebURL url) {
            if(!first.accept(referringPage, url)) return false;
            return second.accept(referringPage, url);
        }
    }

    /**
     * Filter that allows to crawl all urls except those with a nofollow flag.
     * @param respectNoFollow
     */
    public record NoFollowUrlFilter(boolean respectNoFollow) implements UrlFilter {
        @Override
        public boolean accept(Page referringPage, WebURL url) {
            if (respectNoFollow) {
                return !((referringPage != null &&
                        referringPage.getContentType() != null &&
                        referringPage.getContentType().contains("html") &&
                        ((HtmlParseData) referringPage.getParseData())
                                .getMetaTagValue("robots")
                                .contains("nofollow")) ||
                        url.getAttribute("rel").contains("nofollow"));
            }
            return true;
        }
    }

    public record RegexPattern(Pattern pattern) implements UrlFilter {
        @Override
        public boolean accept(Page referringPage, WebURL url) {
            String href = url.getURL().toLowerCase(Locale.ROOT);
            if (pattern.matcher(href).matches()) {
                return false;
            }
            return true;
        }
    }

    public record UrlStartsWithFromList(List<String> domains) implements UrlFilter {
        @Override
        public boolean accept(Page referringPage, WebURL url) {
            String href = url.getURL().toLowerCase(Locale.ROOT);
            for (String crawlDomain : domains) {
                if (href.startsWith(crawlDomain)) {
                    return true;
                }
            }

            return false;
        }
    }
}
