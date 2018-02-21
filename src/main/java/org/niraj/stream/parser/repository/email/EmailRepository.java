package org.niraj.stream.parser.repository.email;

import org.niraj.stream.parser.domain.email.pojo.LinkForwardedMessage;
import org.niraj.stream.parser.domain.email.pojo.Message;
import org.niraj.stream.parser.repository.EmailDBConnection;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailRepository {
    Connection connection;

    public EmailRepository() throws FileNotFoundException, SQLException {
        connection = EmailDBConnection.getConnection();
    }

    public List<Message> getAllOriginalMessages() throws SQLException {

        String query = "SELECT m.mid, m.sender, m.message_id, m.subject, m.date " +
                "FROM message m " +
                "WHERE m.subject NOT LIKE \"FW:%\" " +
                "AND m.subject NOT LIKE \"fw:%\" " +
                "AND m.subject NOT LIKE \"Fw:%\" " +
                "AND m.subject NOT LIKE \"Re:%\" " +
                "AND m.subject NOT LIKE \"RE:%\" " +
                "AND m.subject NOT LIKE \"re:%\"";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        List<Message> emails = new ArrayList<>();
        while (result.next()) {
            Message email = new Message();
            email.setMid(result.getInt(1));
            email.setSender(result.getString(2));
            email.setMessageId(result.getString(3));
            email.setSubject(result.getString(4));
            email.setDatetime(result.getString(5));
            emails.add(email);
        }
        return emails;
    }

    public List<Message> findEmailByMessageId(String messageId) throws SQLException {
        String query = "SELECT m.mid, m.message_id, m.sender, m.date, m.subject " +
                "FROM message m WHERE m.message_id = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, messageId);
        ResultSet result = statement.executeQuery();

        List<Message> messages = new ArrayList<>();
        while (result.next()) {
            Message message = new Message();
            message.setMid(result.getInt(1));
            message.setMessageId(result.getString(2));
            message.setSender(result.getString(3));
            message.setDatetime(result.getString(4));
            message.setSubject(result.getString(5));
            messages.add(message);
        }

        return messages;
    }

    public List<LinkForwardedMessage> findForwardedMessageById(int mid) throws SQLException {
        String query = "SELECT l.* " +
                "FROM link_forwarded_message l " +
                "WHERE l.original_mid=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, mid);

        ResultSet result = statement.executeQuery();
        List<LinkForwardedMessage> forwardedMessages = new ArrayList<>();

        while (result.next()) {
            LinkForwardedMessage forwardedMessage = new LinkForwardedMessage();
            forwardedMessage.setId(result.getInt(1));
            forwardedMessage.setOriginal_sender(result.getString(2));
            forwardedMessage.setForwarded_by(result.getString(3));
            forwardedMessage.setMid(result.getInt(4));
            forwardedMessage.setOriginal_mid(result.getInt(5));
            forwardedMessages.add(forwardedMessage);
        }

        return forwardedMessages;
    }

    public Message findMessageById(int mid) throws SQLException {
        String query = "SELECT m.* FROM message m WHERE m.mid=? LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, mid);
        ResultSet result = statement.executeQuery();

        Message message = null;
        if (result.next()) {
            message = new Message();
            message.setMid(result.getInt(1));
            message.setSender(result.getString(2));
            message.setDatetime(result.getString(3));
            message.setMessageId(result.getString(4));
            message.setSubject(result.getString(4));
            message.setBody(result.getString(5));
            message.setFolder(result.getString(6));
        }

        return message;
    }


}
