package com.igindex.challenge;

import com.igindex.challenge.application.ConnectionProperties;
import com.igindex.challenge.infrastructure.configuration.DefaultDestinationProperties;
import com.igindex.challenge.io.UploadController;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("TEST")
public class ChallengeRaffaeleApplicationTests {
    public static final MockMultipartFile MULTIPART_FILE = new MockMultipartFile("file", "orders.xml", "text/xml", Data.TEST_MALFORMED_XML_1.getBytes());
    @ClassRule
    public static EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();
    @Autowired
    private JmsTemplate template;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private DefaultDestinationProperties properties;

    @Test
    public void contextLoads() {
    }

    @Test
    public void givenDefaultDestination_whenFileUploadOccurs_AMessageIsSent() throws Exception {
        final ConnectionProperties connection = properties.getConnection();
        this.mvc
                .perform(multipart(UploadController.ORDER + UploadController.UPLOAD)
                        .file(MULTIPART_FILE)
                        .param("connection.brokerUrl", connection.getBrokerUrl())
                        .param("connection.username", connection.getUsername())
                        .param("connection.password", connection.getPassword())
                        .param("name", properties.getName())
                        .param("type", properties.getType().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", Matchers.not(Matchers.isEmptyString())))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        final Object message = template.receiveAndConvert(properties.getName());
                        Assert.notNull(message, "Message is null");
                        //other json assertions here
                    }
                })
                .andReturn();


    }
    @Test
    public void givenMissingDestination_whenFileUploadOccurs_AnErrorIsReturned() throws Exception {
        final ConnectionProperties connection = properties.getConnection();
        this.mvc
                .perform(multipart(UploadController.ORDER + UploadController.UPLOAD)
                        .file(MULTIPART_FILE)
                        .param("connection.brokerUrl", "invalid")
                        .param("connection.username", "invalid")
                        .param("connection.password", "invalid")
                        .param("name", properties.getName())
                        .param("type", properties.getType().toString())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attribute("failure", true))
                .andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.not(Matchers.isEmptyString())))
                .andReturn();


    }
}
