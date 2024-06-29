/*-
 * #%L
 * de.hs-heilbronn.mi:crawler4j-commons
 * %%
 * Copyright (C) 2010 - 2022 crawler4j-fork (pre-fork: Yasser Ganjisaffar)
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
package edu.uci.ics.crawler4j.url;

import java.net.URI;
import java.util.Map;

public abstract class AbstractWebURL implements WebURL {

    private URI url;

    private int docid;
    private int parentDocid;
    private URI parentUrl;
    private short depth;
    private String registeredDomain;
    private String subDomain;
    private String path;
    private String anchor;
    private byte priority;
    private String tag;
    private Map<String, String> attributes;
    private TLDList tldList;

    /**
     * Set the TLDList if you want {@linkplain #getDomain()} and
     * {@link #getSubDomain()} to properly identify effective top level registeredDomain as
     * defined at <a href="https://publicsuffix.org">publicsuffix.org</a>
     */
    public void setTldList(TLDList tldList) {
        this.tldList = tldList;
    }

    /**
     * @return unique document id assigned to this Url.
     */
    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    /**
     * @return Url string
     */
    public URI getURL() {
        return url;
    }

    public void setURL(URI url) {
        this.url = url;

        String domain = url.getHost();
        registeredDomain = domain;
        subDomain = "";
        if (tldList != null) {
            String candidate = null;
            String rd = null;
            StringBuilder sd = null;
            String[] parts = domain.split("\\.");
            for (int i = parts.length - 1; i >= 0; i--) {
                if (rd == null) {
                    if (candidate == null) {
                        candidate = parts[i];
                    } else {
                        candidate = parts[i] + "." + candidate;
                    }
                    if (tldList.isRegisteredDomain(candidate)) {
                        rd = candidate;
                    }
                } else {
                    if (sd == null) {
                        sd = new StringBuilder(parts[i]);
                    } else {
                        sd.insert(0, parts[i] + ".");
                    }
                }
            }
            if (rd != null) {
                registeredDomain = rd;
            }
            if (sd != null) {
                subDomain = sd.toString();
            }
        }
    }

    /**
     * @return
     *      unique document id of the parent page. The parent page is the
     *      page in which the Url of this page is first observed.
     */
    public int getParentDocid() {
        return parentDocid;
    }

    public void setParentDocid(int parentDocid) {
        this.parentDocid = parentDocid;
    }

    /**
     * @return
     *      url of the parent page. The parent page is the page in which
     *      the Url of this page is first observed.
     */
    public URI getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(URI parentUrl) {
        this.parentUrl = parentUrl;
    }

    /**
     * @return
     *      crawl depth at which this Url is first observed. Seed Urls
     *      are at depth 0. Urls that are extracted from seed Urls are at depth 1, etc.
     */
    public short getDepth() {
        return depth;
    }

    public void setDepth(short depth) {
        this.depth = depth;
    }

    /**
     * If {@link AbstractWebURL} was provided with a {@link TLDList} then domain will be the
     * privately registered domain which is an immediate child of an effective top
     * level domain as defined at
     * <a href="https://publicsuffix.org">publicsuffix.org</a>. Otherwise it will be
     * the entire domain.
     *
     * @return Domain of this Url. For 'http://www.example.com/sample.htm',
     *         effective top level domain is 'example.com'. For
     *         'http://www.my.company.co.uk' the domain is 'company.co.uk'.
     */
    public String getDomain() {
        return registeredDomain;
    }

    /**
     * If {@link AbstractWebURL} was provided with a {@link TLDList} then subDomain will be
     * the private portion of the entire domain which is a child of the identified
     * registered domain. Otherwise it will be empty. e.g. in
     * "http://www.example.com" the subdomain is "www". In
     * "http://www.my.company.co.uk" the subdomain would be "www.my".
     */
    public String getSubDomain() {
        return subDomain;
    }

    /**
     * @return
     *      path of this Url. For 'http://www.example.com/sample.htm', registeredDomain will be 'sample.htm'
     */
    public String getPath() {
        return url.getPath();
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return
     *      anchor string. For example, in <a href="example.com">A sample anchor</a>
     *      the anchor string is 'A sample anchor'
     */
    public String getAnchor() {
        return url.getFragment();
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    /**
     * @return priority for crawling this URL. A lower number results in higher priority.
     */
    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    /**
     * @return tag in which this URL is found
     * */
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getAttribute(String name) {
        if (attributes == null) {
            return "";
        }
        return attributes.getOrDefault(name, "");
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        AbstractWebURL otherUrl = (AbstractWebURL) o;
        return (url != null) && url.equals(otherUrl.getURL());

    }

    @Override
    public String toString() {
        return url.toString();
    }
}
