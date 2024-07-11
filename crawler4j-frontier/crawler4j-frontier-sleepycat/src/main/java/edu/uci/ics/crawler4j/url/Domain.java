package edu.uci.ics.crawler4j.url;

import java.net.URI;

public record Domain(String topLevelDomain, String subDomain) {

    public static Domain fromUrlAndDomainList(URI url, TLDList domainList) {
        String host = url.getHost();
        if (domainList != null) {
            String candidate = null;
            String rd = null;
            StringBuilder sd = null;
            String[] parts = host.split("\\.");
            for (int i = parts.length - 1; i >= 0; i--) {
                if (rd == null) {
                    if (candidate == null) {
                        candidate = parts[i];
                    } else {
                        candidate = parts[i] + "." + candidate;
                    }
                    if (domainList.isRegisteredDomain(candidate)) {
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
            if (rd != null && sd != null) {
                return new Domain(rd, sd.toString());
            }
            if (rd != null && sd == null) {
                return new Domain(rd, "");
            }
            if (rd == null) {
                return new Domain(host, "");
            }

            throw new RuntimeException("A sub-domain without a top-level domain does not exist! Programmer error! Check the code!");
        }
        return new Domain(host, "");
    }
}