/*-
 * #%L
 * de.hs-heilbronn.mi:crawler4j-examples-base
 * %%
 * Copyright (C) 2010 - 2021 crawler4j-fork (pre-fork: Yasser Ganjisaffar)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.uci.ics.crawler4j.examples.shutdown;

import java.net.URI;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.ResourceHandler;
import edu.uci.ics.crawler4j.crawler.filter.UrlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yasser Ganjisaffar
 */

public class BasicCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(BasicCrawler.class);

    public BasicCrawler() {
        setUrlFilter(new BasicUrlFilter());
        setResourceHandler(new Handler());
    }

    record Handler() implements ResourceHandler {
        @Override
        public void visit(Page page) {
            int docid = page.getWebURL().getDocid();
            URI url = page.getWebURL().getURL();
            int parentDocid = page.getWebURL().getParentDocid();

            logger.debug("Docid: {}", docid);
            logger.info("URL: {}", url);
            logger.debug("Docid of parent page: {}", parentDocid);

            if (page.getParseData() instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                String text = htmlParseData.getText();
                String html = htmlParseData.getHtml();
                Set<WebURL> links = htmlParseData.getOutgoingUrls();

                logger.debug("Text length: {}", text.length());
                logger.debug("Html length: {}", html.length());
                logger.debug("Number of outgoing links: {}", links.size());
            }

            logger.debug("=============");
        }
    }

    record BasicUrlFilter() implements UrlFilter {
        private static final Pattern FILTERS = Pattern.compile(
                ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                        "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

        private static final String DOMAIN = "https://www.ics.uci.edu/";
        @Override
        public boolean accept(Page referringPage, WebURL url) {
            String href = url.getURL().toString();
            return !FILTERS.matcher(href).matches() && href.startsWith(DOMAIN);
        }
    }
}
