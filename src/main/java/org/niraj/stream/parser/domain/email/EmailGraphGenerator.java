package org.niraj.stream.parser.domain.email;

import org.niraj.stream.parser.GraphGenerator;
import org.niraj.stream.parser.domain.email.pojo.*;
import org.niraj.stream.parser.enumerator.EdgeType;
import org.niraj.stream.parser.enumerator.EmailStateTypeEnum;
import org.niraj.stream.parser.helper.Helper;
import org.niraj.stream.parser.pojo.Edge;
import org.niraj.stream.parser.pojo.Vertex;
import org.niraj.stream.parser.repository.email.EmailRepository;
import org.niraj.stream.parser.repository.email.EmployeeRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class EmailGraphGenerator extends GraphGenerator<Email> {

    private Map<Integer, Map<Integer, Vertex>> recipientVertexLink;
    private EmployeeRepository employeeRepository;
    private EmailRepository emailRepository;
    private int processedEmailCount;

    public EmailGraphGenerator() throws IOException, TimeoutException, SQLException {
        super();
        recipientVertexLink = new HashMap<>();
        employeeRepository = new EmployeeRepository();
        emailRepository = new EmailRepository();
        processedEmailCount = 0;
    }

    public int getProcessedEmailCount() {
        return processedEmailCount;
    }

    public void createGraphStream(Email email) throws IOException {

        try {
            String graphId = "1";

            Vertex messageStateVertex = _createMessageStateVertex(EmailStateTypeEnum.ORIGINAL, graphId);

            Vertex senderVertex = _createOriginalSenderVertex(email.getOriginalSender(), graphId);
            List<RecipientEmployee> recipients = email.getEmailRecipient();

            _createRecipientGraph(recipients, graphId, senderVertex,
                    email.getOriginalMessage(), messageStateVertex);

            _createForwardedMessageGraph(email, graphId);
            processedEmailCount++;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private void _createRecipientGraph(List<RecipientEmployee> recipients, String graphId, Vertex senderVertex,
                                       Message message, Vertex messageStateVertex) {
        Map<String, String> timeVertexAttributes = getAttribute(Helper.getInstance().getTimeBucket(message.getDatetime()), graphId);
        Vertex timeVertex = createVertex(timeVertexAttributes);


        Map<String, String> dayVertexAttributes = getAttribute(Helper.getInstance().getDayBucket(message.getDatetime()), graphId);
        Vertex dayVertex = createVertex(dayVertexAttributes);
        Map<Integer, Vertex> messageRecipientVertexLink = new HashMap<>();
        for (RecipientEmployee recipient : recipients) {
            String comment = "//Recipient Message ID: " + message.getMessageId() + "-- id: " + message.getMid();
            Vertex messageVertex = _createMessageVertex(graphId,recipient.getReceptionMethod(),comment);

            Edge senderMessageEdge = createEdge(senderVertex, messageVertex, EdgeType.DIRECTED, "sends", graphId);

            String recipientStatus = recipient.getStatus() == null ? "Not Employee" : recipient.getStatus();
            Map<String, String> recipientStatusVertexAttributes = getAttribute(recipientStatus.toUpperCase(), graphId);
            Vertex recipientStatusVertex = createVertex(recipientStatusVertexAttributes);

            Vertex recipientVertex = _createSenderRecipientVertex(recipientStatusVertex,"RECEIVER",graphId);

            messageRecipientVertexLink.put(recipient.getId(), recipientVertex);
            recipientVertexLink.put(message.getMid(), messageRecipientVertexLink);

            Edge receiverMessageEdge = createEdge(messageVertex, recipientVertex, EdgeType.DIRECTED, "receives", graphId);

            Edge messageTimeEdge = createEdge(messageVertex, timeVertex, EdgeType.UNDIRECTED, "during", graphId);

            Edge messageDayEdge = createEdge(messageVertex, dayVertex, EdgeType.UNDIRECTED, "on", graphId);
            Edge messageStateEdge = createEdge(messageVertex, messageStateVertex, EdgeType.UNDIRECTED, "is", graphId);
        }
    }

    private void _createForwardedMessageGraph(Email email, String graphId) throws SQLException {
        for (LinkForwardedMessage forwardedMessageLink : email.getForwardedMessages()) {
            if (recipientVertexLink.containsKey(forwardedMessageLink.getOriginal_mid())) {
                Map<Integer, Vertex> recipientVertexLinker = recipientVertexLink.get(forwardedMessageLink.getOriginal_mid());

                Employee forwardingEmployee = employeeRepository.getEmployeeByEmail(forwardedMessageLink.getForwarded_by());
                if (forwardingEmployee != null && recipientVertexLinker.keySet().contains(forwardingEmployee.getId())) {
                    Vertex forwardingEmployeeVertex = recipientVertexLinker.get(forwardingEmployee.getId());
                    Message forwardedMessage = emailRepository.findMessageById(forwardedMessageLink.getMid());
                    List<RecipientEmployee> forwardedMessageRecipient =
                            employeeRepository.getRecipientEmployeeByMessageId(forwardedMessageLink.getMid());

                    Vertex messageStateVertex = _createMessageStateVertex(EmailStateTypeEnum.FORWARDED, graphId);

                    _createRecipientGraph(forwardedMessageRecipient, graphId, forwardingEmployeeVertex,
                            forwardedMessage, messageStateVertex);
                }
            }
        }
    }

    private Vertex _createMessageStateVertex(EmailStateTypeEnum stateType, String graphId) {
        Map<String, String> messageStateVertexAttribute = getAttribute(stateType.getValue(), graphId);
        Vertex messageStateVertex = createVertex(messageStateVertexAttribute);

        return messageStateVertex;
    }

    private Vertex _createMessageVertex(String graphId, String receptionMethod, String comment){
        Map<String,String> messageVertexAttribute = getAttribute("MESSAGE",graphId);
        messageVertexAttribute.put(COMMENT_ATTRIBUTE_INDEX, comment);
        Vertex messageVertex = createVertex(messageVertexAttribute);

        Map<String,String> messageReceptionMethodAttribute = getAttribute(receptionMethod,graphId);
        Vertex messageReceptionMethodVertex = createVertex(messageReceptionMethodAttribute);

        Edge messageStateEdge = createEdge(messageVertex,messageReceptionMethodVertex,EdgeType.UNDIRECTED,"using", graphId);

        return messageVertex;
    }

    private Vertex _createSenderRecipientVertex(Vertex senderStatusVertex, String label, String graphId) {
        Map<String, String> senderVertexAttributes = getAttribute(label, graphId);
        Vertex senderVertex = createVertex(senderVertexAttributes);
        Edge senderVertexStatusEdge = createEdge(senderVertex, senderStatusVertex, EdgeType.UNDIRECTED, "is", graphId);

        return senderVertex;
    }

    private Vertex _createOriginalSenderVertex(Employee employee, String graphId) {
        String employeeStatus = (employee.getStatus() == null) ? "Not Employee" : employee.getStatus();
        Map<String, String> originalSenderStatusAttributes = getAttribute(employeeStatus.toUpperCase(), graphId);
        originalSenderStatusAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//Original Employee ID: " + employee.getId() + "-- email: " +
                employee.getEmailId());
        Vertex originalSenderStatusVertex = createVertex(originalSenderStatusAttributes);

        return _createSenderRecipientVertex(originalSenderStatusVertex, "SENDER", graphId);
    }

}
