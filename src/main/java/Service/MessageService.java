package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import Model.Account;
import java.util.ArrayList;
import java.util.List;


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
            return null; 
        }
    
        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            return null; 
        }  
        return messageDAO.insertMessage(message); 
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    public Message updateMessageById(int messageId, String newMessageText) {
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage != null) {
            // Update the message_text
            existingMessage.setMessage_text(newMessageText);
            // Perform the update in the database
            boolean isUpdated = messageDAO.updateMessage(existingMessage);
            if (isUpdated) {
                return existingMessage; 
            } else {
                return null; 
            }
        } else {
            return null; 
        }
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}
