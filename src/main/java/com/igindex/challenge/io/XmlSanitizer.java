package com.igindex.challenge.io;

import com.igindex.challenge.error.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XmlSanitizer {
    private static final Logger log = LoggerFactory.getLogger(XmlSanitizer.class);

    public String sanitize(InputStream stream) {
        try (InputStreamReader is = new InputStreamReader(stream)) {
            BufferedReader bis = new BufferedReader(is);
            StringBuilder sb = new StringBuilder();
            sb.append(bis.readLine());
            sb.append("<root>\n");
            while (bis.ready()) {
                sb.append(bis.readLine());
            }
            sb.append("</root>\n");
            return sb.toString();
        } catch (Exception e) {
            throw new ParserException("Error sanitising input stream", e);
        }
    }
}
