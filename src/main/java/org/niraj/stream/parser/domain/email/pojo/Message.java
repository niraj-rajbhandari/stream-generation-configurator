package org.niraj.stream.parser.domain.email.pojo;

public class Message {
    private int mid;
    private String sender;
    private String datetime;
    private String messageId;
    private String subject;
    private String body;
    private String folder;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mid=" + mid +
                ", sender='" + sender + '\'' +
                ", messageId='" + messageId + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
