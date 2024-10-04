package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account registerAccount(String username, String password) {
        // Check if username is blank
        if (username == null || username.trim().isEmpty()) {
            return null; // Registration fails due to blank username
        }

        // Check if password is at least 4 characters long
        if (password == null || password.length() < 4) {
            return null; // Registration fails due to short password
        }

        // Check if username already exists
        if (accountDAO.doesUsernameExist(username)) {
            return null; // Registration fails due to username already existing
        }

        // Create a new account and insert it into the database
        Account newAccount = new Account(username, password);
        return accountDAO.insertAccount(newAccount); // Returns the new account (including account_id)
    }

    public Account login(String username, String password) {
        // Optional: Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null; // Invalid input
        }
    
        // Use the DAO to get the account
        Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
    
        // Return the account if found
        return account;
    }
    
}
