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


        return app;  
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
    
        // Attempt to register the account using the AccountService
        Account registeredAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
    
        // Check if registration was successful
        if (registeredAccount != null) {
            
            ctx.json(registeredAccount);
            ctx.status(200);
        } else {
            
            ctx.status(400);
            
        }
    }
    
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
    
        // Parse the request body into an Account object (without account_id)
        Account credentials = mapper.readValue(ctx.body(), Account.class);
    
        // Attempt to login using AccountService
        Account loggedInAccount = accountService.login(credentials.getUsername(), credentials.getPassword());
    
        if (loggedInAccount != null) {
            // Login successful
            ctx.json(loggedInAccount);
            ctx.status(200); // Optional since 200 is the default
        } else {
            // Login failed
            ctx.status(401); // Unauthorized
            ctx.result("");  // Ensure response body is empty
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
                ctx.result(""); // Ensure response body is empty
            }
        } catch (JsonProcessingException e) {
            ctx.status(400);
            ctx.result(""); // Ensure response body is empty
        } catch (Exception e) {
            ctx.status(500);
            ctx.result(""); // Ensure response body is empty
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
            // Message found, return it as JSON
            ctx.json(message);
            ctx.status(200); // Optional, 200 is default
        } else {
            // Message not found, return 200 with empty body
            ctx.result("");
            ctx.status(200);
        }
    }
    
}
