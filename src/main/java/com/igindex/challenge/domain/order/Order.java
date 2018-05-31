package com.igindex.challenge.domain.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Order")
public class Order {
/**
 *     note: there is no request in the specification,
 *     but it could be proper to make it immutable since we want to guarantee
 *     no one tampers with the order once created
 */
    private String accont; //TODO this is possibly a typo
    private String market;
    private String submittedAt;
    private String receivedAt;
    private String action;
    private long size;

    public Order() {
    }

    @JsonProperty("account")
    public String getAccont() {
        return accont;
    }

    @JsonProperty("accont")
    public void setAccont(String accont) {
        this.accont = accont;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    @JsonProperty("submittedAt")
    public String getSubmittedAt() {
        return submittedAt;
    }

    @JsonProperty("SubmittedAt")
    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    @JsonProperty("receivedAt")
    public String getReceivedAt() {
        return receivedAt;
    }

    @JsonProperty("ReceivedAt")
    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
