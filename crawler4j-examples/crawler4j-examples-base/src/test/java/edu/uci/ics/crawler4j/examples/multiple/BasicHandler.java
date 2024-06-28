package edu.uci.ics.crawler4j.examples.multiple;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.ResourceHandler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Set;

record BasicHandler() implements ResourceHandler {
    private static final Logger logger = LoggerFactory.getLogger(BasicHandler.class);

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
