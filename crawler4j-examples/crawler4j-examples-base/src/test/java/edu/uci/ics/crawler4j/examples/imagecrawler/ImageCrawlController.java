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
package edu.uci.ics.crawler4j.examples.imagecrawler;

import java.io.File;
import java.util.List;

import crawlercommons.filters.basic.BasicURLNormalizer;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.examples.Files;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.FrontierConfiguration;
import edu.uci.ics.crawler4j.frontier.SleepycatFrontierConfiguration;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.SleepycatWebURLFactory;

public class ImageCrawlController {

    public static void main(String[] args) throws Exception {
        File storageDir = Files.createTmpDir("crawler4j");

        CrawlConfig config = new CrawlConfig();

        // Set the folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        config.setCrawlStorageFolder(storageDir);

        // Number of threads to use during crawling. Increasing this typically makes crawling faster. But crawling
        // speed depends on many other factors as well. You can experiment with this to figure out what number of
        // threads works best for you.
        int numberOfCrawlers = 8;

        // Where should the downloaded images be stored?
        File storageFolder = storageDir;

        // Since images are binary content, we need to set this parameter to
        // true to make sure they are included in the crawl.
        config.setIncludeBinaryContentInCrawling(true);

        List<String> crawlDomains = List.of("https://uci.edu/");

        BasicURLNormalizer normalizer = BasicURLNormalizer.newBuilder().idnNormalization(BasicURLNormalizer.IdnNormalization.NONE).build();
        PageFetcher pageFetcher = new PageFetcher(config, normalizer);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        FrontierConfiguration frontierConfiguration = new SleepycatFrontierConfiguration(config);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher, new SleepycatWebURLFactory());
        CrawlController controller = new CrawlController(config, normalizer, pageFetcher, robotstxtServer, frontierConfiguration);
        for (String domain : crawlDomains) {
            controller.addSeed(domain);
        }

        CrawlController.WebCrawlerFactory<WebCrawler> factory = () -> new ImageCrawler(storageFolder)
                .setUrlFilter(new ImageCrawler.ImageUrlFilter(crawlDomains));
        controller.start(factory, numberOfCrawlers);
    }
}
