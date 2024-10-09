package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account registerAccount(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return null; 
        }

        if (password == null || password.length() < 4) {
            return null;
        }

        if (accountDAO.doesUsernameExist(username)) {
            return null; 
        }

        Account newAccount = new Account(username, password);
        return accountDAO.insertAccount(newAccount); 
    }

    public Account login(String username, String password) { 
        Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
        return account;
    }
    
}
