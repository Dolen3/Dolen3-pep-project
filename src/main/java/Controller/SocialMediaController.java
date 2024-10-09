package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the endpoints and handlers for the Social Media API, including user registration.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    // Constructor to initialize the AccountService
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * Starts the Javalin server and returns the configured Javalin app object.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);  
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler); 
        app.get("/messages/{message_id}", this::getMessageByIdHandler); 
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);


        return app;  
    }

    private void getMessagesByAccountIdHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
    
        ctx.json(messages);
        ctx.status(200); 
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
    
        Message messageFromRequest = mapper.readValue(ctx.body(), Message.class);
        String newMessageText = messageFromRequest.getMessage_text();
    
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            
            ctx.status(400);
            ctx.result(""); 
            return;
        }
    
        Message updatedMessage = messageService.updateMessageById(messageId, newMessageText);
    
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
            ctx.status(200); 
        } else {
            ctx.status(400);
            ctx.result(""); 
        }
    }

    /**
     * Handler for registering a new account.
     * If registration is successful, returns the registered account with account_id as JSON.
     * If unsuccessful, returns a 400 status with a failure message.
     * @param ctx the context object handling the HTTP request/response.
     * @throws JsonProcessingException in case of JSON processing errors.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
    
        // Convert the JSON request body into an Account object
        Account account = mapper.readValue(ctx.body(), Account.class);
    
        Account registeredAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
    
        if (registeredAccount != null) {
            
            ctx.json(registeredAccount);
            ctx.status(200);
        } else {
            
            ctx.status(400);
            
        }
    }
    
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
    
        Account credentials = mapper.readValue(ctx.body(), Account.class);
    
        Account loggedInAccount = accountService.login(credentials.getUsername(), credentials.getPassword());
    
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
            ctx.status(200); 
        } else {
            
            ctx.status(401); 
            ctx.result("");  
        }
    }
    
    private void postMessageHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
    
            if (createdMessage != null) {
                ctx.json(createdMessage);
                ctx.status(200);
            } else {
                ctx.status(400);
                ctx.result(""); 
            }
        } catch (JsonProcessingException e) {
            ctx.status(400);
            ctx.result(""); 
        } catch (Exception e) {
            ctx.status(500);
            ctx.result(""); 
        }
    }
    

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200); 
    }
    
    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
    
        if (message != null) {
            ctx.json(message);
            ctx.status(200); 
        } else {
            ctx.result("");
            ctx.status(200);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage != null) {
            ctx.json(deletedMessage);
            ctx.status(200);
        } else {
            
            ctx.result("");
            ctx.status(200);
        }
    }
    
}
