package com.igindex.challenge.io;

import com.igindex.challenge.Data;
import com.igindex.challenge.infrastructure.configuration.ParserConfiguration;
import com.igindex.challenge.domain.order.Order;
import com.igindex.challenge.error.ParserException;
import com.igindex.challenge.infrastructure.XmlParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ParserConfiguration.class)
public class FileParserIntegrationTest {
    @Autowired
    private XmlParser fileParser;
    @Autowired
    private XmlSanitizer sanitizer;

    @Test
    public void parseFile_givenFileContainingWellFormed1Order_then1OrderIsParsed() throws Exception {
        final List<Order> orders = parseString(Data.TEST_WELL_FORMED_XML_1);
        assertEquals(1L, orders.size());
        assertEquals(Data.ACCOUNT, orders.get(0).getAccont());
    }

    private List<Order> parseString(String testWellFormedXml1) throws IOException {
        return fileParser.parseString(testWellFormedXml1);
    }

    @Test
    public void parseFile_givenFileContainingWellFormed2Order_then2DifferentOrdersAreParsed() throws Exception {
        final List<Order> orders = parseString(Data.TEST_WELL_FORMED_XML_2);
        assertEquals(2L, orders.size());
        assertEquals(Data.ACCOUNT, orders.get(0).getAccont());
        assertEquals(Data.ACCOUNT_2, orders.get(1).getAccont());
    }

    @Test(expected = ParserException.class)
    public void parseFile_givenFileContainingMalFormed1Order_thenThrowsException() throws Exception {
        final List<Order> orders = parseString(Data.TEST_MALFORMED_XML_1);
        assertEquals(1L, orders.size());
        assertEquals(Data.ACCOUNT, orders.get(0).getAccont());
    }

    @Test()
    public void parseFile_givenFileContainingMalFormed1OrderSanitized_then1OrderIsReturned() throws Exception {
        final String sanitized = sanitizer.sanitize(new ByteArrayInputStream(Data.TEST_MALFORMED_XML_1.getBytes(Charset.defaultCharset())));
        final List<Order> orders = parseString(sanitized);
        assertEquals(1L, orders.size());
        assertEquals(Data.ACCOUNT, orders.get(0).getAccont());
    }
}