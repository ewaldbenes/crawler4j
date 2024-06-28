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
package edu.uci.ics.crawler4j.examples.multiple;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import crawlercommons.filters.basic.BasicURLNormalizer;
import edu.uci.ics.crawler4j.crawler.filter.UrlFilters;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.examples.Files;
import edu.uci.ics.crawler4j.frontier.FrontierConfiguration;
import edu.uci.ics.crawler4j.frontier.SleepycatFrontierConfiguration;
import edu.uci.ics.crawler4j.url.SleepycatWebURLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MultipleCrawlerController {
    private static final Logger logger = LoggerFactory.getLogger(MultipleCrawlerController.class);

    public static void main(String[] args) throws Exception {
        // The folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        File storageDir = Files.createTmpDir("crawler4j");

        CrawlConfig config1 = new CrawlConfig();
        CrawlConfig config2 = new CrawlConfig();

        // The two crawlers should have different storage folders for their intermediate data.
        config1.setCrawlStorageFolder(new File(storageDir, "crawler1"));
        config2.setCrawlStorageFolder(new File(storageDir, "crawler2"));

        config1.setPolitenessDelay(1000);
        config2.setPolitenessDelay(2000);

        config1.setMaxPagesToFetch(50);
        config2.setMaxPagesToFetch(100);

        // We will use different PageFetchers for the two crawlers.
        BasicURLNormalizer normalizer1 = BasicURLNormalizer.newBuilder().idnNormalization(BasicURLNormalizer.IdnNormalization.NONE).build();
        BasicURLNormalizer normalizer2 = BasicURLNormalizer.newBuilder().idnNormalization(BasicURLNormalizer.IdnNormalization.NONE).build();
        PageFetcher pageFetcher1 = new PageFetcher(config1, normalizer1);
        PageFetcher pageFetcher2 = new PageFetcher(config2, normalizer2);

        // We will use the same RobotstxtServer for both of the crawlers.
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();

        FrontierConfiguration frontierConfiguration = new SleepycatFrontierConfiguration(config1);
        FrontierConfiguration frontierConfiguration2 = new SleepycatFrontierConfiguration(config2);

        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1, new SleepycatWebURLFactory());

        CrawlController controller1 = new CrawlController(config1, normalizer1, pageFetcher1, robotstxtServer, frontierConfiguration);
        CrawlController controller2 = new CrawlController(config2, normalizer2, pageFetcher2, robotstxtServer, frontierConfiguration2);

        List<String> crawler1Domains = List.of("https://www.ics.uci.edu/", "https://www.cnn.com/");
        List<String> crawler2Domains = List.of("https://en.wikipedia.org/");

        controller1.addSeed("https://www.ics.uci.edu/");
        controller1.addSeed("https://www.cnn.com/");
        controller1.addSeed("https://www.ics.uci.edu/~lopes/");
        controller1.addSeed("https://www.cnn.com/POLITICS/");

        controller2.addSeed("https://en.wikipedia.org/wiki/Main_Page");
        controller2.addSeed("https://en.wikipedia.org/wiki/Obama");
        controller2.addSeed("https://en.wikipedia.org/wiki/Bing");

        UrlFilters.RegexPattern regexPatternFilter = new UrlFilters.RegexPattern(Pattern.compile(
                ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$"));
        CrawlController.WebCrawlerFactory<WebCrawler> factory1 = () -> new BasicCrawler()
                .setUrlFilter(new UrlFilters.ChainedUrlFilter(regexPatternFilter, new UrlFilters.UrlStartsWithFromList(crawler1Domains)));
        CrawlController.WebCrawlerFactory<WebCrawler> factory2 = () -> new BasicCrawler()
                .setUrlFilter(new UrlFilters.ChainedUrlFilter(regexPatternFilter, new UrlFilters.UrlStartsWithFromList(crawler2Domains)));;

        // The first crawler will have 5 concurrent threads and the second crawler will have 7 threads.
        controller1.startNonBlocking(factory1, 5);
        controller2.startNonBlocking(factory2, 7);

        controller1.waitUntilFinish();
        logger.info("Crawler 1 is finished.");

        controller2.waitUntilFinish();
        logger.info("Crawler 2 is finished.");
    }
}
