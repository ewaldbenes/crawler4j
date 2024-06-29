/*-
 * #%L
 * de.hs-heilbronn.mi:crawler4j-frontier-sleepycat
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
package edu.uci.ics.crawler4j.frontier;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.url.WebURLImpl;

import java.net.URI;

/**
 * @author Yasser Ganjisaffar
 */
public class WebURLTupleBinding extends TupleBinding<WebURL> {

    @Override
    public WebURL entryToObject(TupleInput input) {
        WebURLImpl webURL = new WebURLImpl();
        webURL.setURL(URI.create(input.readString()));
        webURL.setDocid(input.readInt());
        webURL.setParentDocid(input.readInt());
        String parentUrlStr = input.readString();
        webURL.setParentUrl(parentUrlStr != null ? URI.create(parentUrlStr) : null);
        webURL.setDepth(input.readShort());
        webURL.setPriority(input.readByte());
        webURL.setAnchor(input.readString());
        return webURL;
    }

    @Override
    public void objectToEntry(WebURL url, TupleOutput output) {
        output.writeString(url.getURL().toString());
        output.writeInt(url.getDocid());
        output.writeInt(url.getParentDocid());
        output.writeString(url.getParentUrl() == null ? null : url.getParentUrl().toString());
        output.writeShort(url.getDepth());
        output.writeByte(url.getPriority());
        output.writeString(url.getAnchor());
    }
}
