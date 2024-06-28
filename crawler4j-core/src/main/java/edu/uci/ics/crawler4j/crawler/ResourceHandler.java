package edu.uci.ics.crawler4j.crawler;

public interface ResourceHandler {
    /**
     * Process the content of the fetched and parsed page.
     *
     * @param page the page object that is just fetched and parsed.
     */
    void visit(Page page);
}
