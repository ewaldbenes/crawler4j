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
package edu.uci.ics.crawler4j.examples.statushandler;

import java.util.Locale;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.ResourceHandler;
import edu.uci.ics.crawler4j.crawler.filter.UrlFilter;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yasser Ganjisaffar
 */
public class StatusHandlerCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(StatusHandlerCrawler.class);

    public StatusHandlerCrawler() {
        setUrlFilter(new Filter());
        setResourceHandler(page -> {});
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {

        if (statusCode != HttpStatus.SC_OK) {

            if (statusCode == HttpStatus.SC_NOT_FOUND) {
                logger.warn("Broken link: {}, this link was found in page: {}", webUrl.getURL(),
                            webUrl.getParentUrl());
            } else {
                logger.warn("Non success status for link: {} status code: {}, description: {} ",
                            webUrl.getURL(), statusCode, statusDescription);
            }
        }
    }

    record Filter() implements UrlFilter {
        private static final Pattern FILTERS = Pattern.compile(
                ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                        "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
        @Override
        public boolean accept(Page referringPage, WebURL url) {
            String href = url.getURL().toString();
            return !FILTERS.matcher(href).matches() && href.startsWith("https://www.ics.uci.edu/");
        }
    }
}
