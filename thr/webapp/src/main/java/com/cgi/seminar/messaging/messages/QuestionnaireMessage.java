package com.cgi.seminar.messaging.messages;

public class QuestionnaireMessage {
    private String name;
    private String questions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}
