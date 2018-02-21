package org.niraj.stream.parser.domain.email.pojo;

public class LinkForwardedMessage {

    private int id;
    private String original_sender;
    private String forwarded_by;
    private int mid;
    private int original_mid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_sender() {
        return original_sender;
    }

    public void setOriginal_sender(String original_sender) {
        this.original_sender = original_sender;
    }

    public String getForwarded_by() {
        return forwarded_by;
    }

    public void setForwarded_by(String forwarded_by) {
        this.forwarded_by = forwarded_by;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getOriginal_mid() {
        return original_mid;
    }

    public void setOriginal_mid(int original_mid) {
        this.original_mid = original_mid;
    }

    @Override
    public String toString() {
        return "LinkForwardedMessage{" +
                "id=" + id +
                ", original_sender='" + original_sender + '\'' +
                ", forwarded_by='" + forwarded_by + '\'' +
                ", mid=" + mid +
                ", original_mid=" + original_mid +
                '}';
    }
}
