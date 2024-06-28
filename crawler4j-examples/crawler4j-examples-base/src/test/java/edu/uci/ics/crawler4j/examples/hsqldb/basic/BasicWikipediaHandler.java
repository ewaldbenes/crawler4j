package edu.uci.ics.crawler4j.examples.hsqldb.basic;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.ResourceHandler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.hc.core5.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

record BasicWikipediaHandler() implements ResourceHandler {
    private static final Logger LOG = LoggerFactory.getLogger(BasicWikipediaHandler.class);
    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        LOG.debug("Docid: {}", docid);
        LOG.info("URL: {}", url);
        LOG.debug("Domain: '{}'", domain);
        LOG.debug("Sub-domain: '{}'", subDomain);
        LOG.debug("Path: '{}'", path);
        LOG.debug("Parent page: {}", parentUrl);
        LOG.debug("Anchor text: {}", anchor);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            LOG.debug("Text length: {}", text.length());
            LOG.debug("Html length: {}", html.length());
            LOG.debug("Number of outgoing links: {}", links.size());
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
            LOG.debug("Response headers:");
            for (Header header : responseHeaders) {
                LOG.debug("\t{}: {}", header.getName(), header.getValue());
            }
        }

        LOG.debug("=============");
    }
}
