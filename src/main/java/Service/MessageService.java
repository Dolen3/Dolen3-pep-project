package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import Model.Account;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message createMessage(Message message) {
        // Validate that message_text is not blank and under 255 characters
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() >= 255) {
            return null; // Validation failed
        }
    
        // Check that posted_by refers to an existing user
        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            return null; // User does not exist
        }
    
        // Use the DAO to insert the message into the database
        return messageDAO.insertMessage(message); // Returns the new message (including message_id)
    }
}
