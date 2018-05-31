package com.igindex.challenge;

public final class Data {
    public static final String ACCOUNT = "AX001";
    public static final String ORDER_LINE = "<Order><accont>" + ACCOUNT + "</accont><SubmittedAt>1507060723641</SubmittedAt><ReceivedAt>1507060723642</ReceivedAt><market>VOD.L</market><action>BUY</action><size>100</size></Order>\n";
    public static final String TEST_MALFORMED_XML_1 = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n" +
            ORDER_LINE;
    public static final String TEST_WELL_FORMED_XML_1 = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n" +
                    "<root>\n" +
                    ORDER_LINE +
                    "</root>";
    public static final String ACCOUNT_2 = "AX002";
    public static final String ORDER_LINE_2 = "<Order><accont>" + ACCOUNT_2 + "</accont><SubmittedAt>1507060723643</SubmittedAt><ReceivedAt>1507060723645</ReceivedAt><market>VOD.L</market><action>SELL</action><size>200</size></Order>\n";
    public static final String TEST_WELL_FORMED_XML_2 = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n" +
            "<root>\n" +
            ORDER_LINE +
            ORDER_LINE_2 +
            "</root>";
}
