package com.cgi.seminar.messaging;

public interface MessagingConfiguration {
    public static final String WORKORDER_EXCHANGE_NAME = "workorder-exchange";

    public static final String QUESTIONNAIRE_EXCHANGE_NAME = "questionnaire-exchange";
    public static final String QUESTIONNAIRE_QUEUE_NAME = "questionnaire-queue";

    public static final String NURSE_EXCHANGE_NAME = "nurse-exchange";
    public static final String NURSE_QUEUE_NAME = "nurse-queue";

    public static final String HOMEADDRESS_EXCHANGE_NAME = "homeaddress-exchange";
    public static final String HOMEADDRESS_QUEUE_NAME = "homeaddress-queue";

    // see also messaging.consumer.*Listener for incoming queue names
}
