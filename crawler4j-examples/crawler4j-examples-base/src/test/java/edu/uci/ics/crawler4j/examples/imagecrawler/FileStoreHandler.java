package edu.uci.ics.crawler4j.examples.imagecrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.ResourceHandler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * This class shows how you can crawl images on the web and store them in a
 * folder. This is just for demonstration purposes and doesn't scale for large
 * number of images. For crawling millions of images you would need to store
 * downloaded images in a hierarchy of folders
 */
record FileStoreHandler(File storageFolder) implements ResourceHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FileStoreHandler.class);

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        // We are only interested in processing images which are bigger than 10k
        if (!((page.getParseData() instanceof BinaryParseData) ||
                (page.getContentData().length < (10 * 1024)))) {
            return;
        }

        // Get a unique name for storing this image
        String extension = url.substring(url.lastIndexOf('.'));
        String hashedName = UUID.randomUUID() + extension;

        // Store image
        String filename = storageFolder.getAbsolutePath() + '/' + hashedName;
        try {
            Files.write(Paths.get(filename), page.getContentData(), StandardOpenOption.CREATE_NEW);
            LOG.info("Stored: {}", url);
        } catch (IOException iox) {
            LOG.error("Failed to write file: {}", filename, iox);
        }
    }
}
