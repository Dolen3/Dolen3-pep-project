package Controller;

import Model.Account;
import Service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * This class defines the endpoints and handlers for the Social Media API, including user registration.
 */
public class SocialMediaController {

    private AccountService accountService;

    // Constructor to initialize the AccountService
    public SocialMediaController() {
        this.accountService = new AccountService();
    }

    /**
     * Starts the Javalin server and returns the configured Javalin app object.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);  
        app.post("/login", this::postLoginHandler);
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
    
    
    
}
