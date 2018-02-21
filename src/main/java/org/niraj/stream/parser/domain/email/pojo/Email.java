package org.niraj.stream.parser.domain.email.pojo;

import java.util.List;

public class Email {
    private Message originalMessage;
    private Employee originalSender;
    private List<RecipientEmployee> emailRecipient;
    private List<LinkForwardedMessage> forwardedMessages;

    public Message getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(Message originalMessage) {
        this.originalMessage = originalMessage;
    }

    public Employee getOriginalSender() {
        return originalSender;
    }

    public void setOriginalSender(Employee originalSender) {
        this.originalSender = originalSender;
    }

    public List<RecipientEmployee> getEmailRecipient() {
        return emailRecipient;
    }

    public void setEmailRecipient(List<RecipientEmployee> emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public List<LinkForwardedMessage> getForwardedMessages() {
        return forwardedMessages;
    }

    public void setForwardedMessages(List<LinkForwardedMessage> forwardedMessages) {
        this.forwardedMessages = forwardedMessages;
    }

    @Override
    public String toString() {
        return "Email{" +
                "originalMessage=" + originalMessage +
                ", originalSender=" + originalSender +
                ", emailRecipient=" + emailRecipient +
                ", forwardedMessages=" + forwardedMessages +
                '}';
    }
}
