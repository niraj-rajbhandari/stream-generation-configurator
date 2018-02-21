package org.niraj.stream.parser.domain.email;

import org.niraj.stream.parser.GraphGenerator;
import org.niraj.stream.parser.configuration.DataStreamConfigurator;
import org.niraj.stream.parser.domain.email.pojo.Email;
import org.niraj.stream.parser.domain.email.pojo.Employee;
import org.niraj.stream.parser.domain.email.pojo.Message;
import org.niraj.stream.parser.message.queue.QueueService;
import org.niraj.stream.parser.repository.email.EmailRepository;
import org.niraj.stream.parser.repository.email.EmployeeRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class EnronEmailStreamConfigurator extends DataStreamConfigurator<Email> {
    private EmailRepository emailRepo;
    private EmployeeRepository employeeRepository;

    public EnronEmailStreamConfigurator(String outputFile) throws IOException {
        super(null, outputFile);
        try {
            emailRepo = new EmailRepository();
            employeeRepository = new EmployeeRepository();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void createDataStreamConfiguration() throws IOException, IllegalArgumentException, IllegalAccessException, TimeoutException {
        try {
            EmailGraphGenerator graphGenerator = new EmailGraphGenerator();
            List<Message> messages = emailRepo.getAllOriginalMessages();
            for (Message message : messages) {
                Employee sender = employeeRepository.getEmployeeByEmail(message.getSender());
                if (sender == null) {
                    sender = new Employee();
                    sender.setEmailId(message.getSender());
                }
//                if(sender != null){
                Email email = new Email();
                email.setOriginalMessage(message);
                email.setOriginalSender(sender);
                email.setEmailRecipient(employeeRepository.getRecipientEmployeeByMessageId(message.getMid()));
                if(!email.getEmailRecipient().isEmpty()){
                    email.setForwardedMessages(emailRepo.findForwardedMessageById(message.getMid()));
                    graphGenerator.createGraphStream(email);
                    if (helper.isDebugMode(config) && graphGenerator.getProcessedEmailCount() > Integer.parseInt(config.getProperty("graph-size")))
                        break;

                }
//                }

            }
            this.getConfiguration().setPatterns(graphGenerator.getGraphStream());

            QueueService queueService = QueueService.getInstance();
            queueService.publishToQueue(PUBLISHING_COMPLETED);
            QueueService.closeQueue();
            this.createJson();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}