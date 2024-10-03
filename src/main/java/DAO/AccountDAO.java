package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL statement to insert into the account table
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            // Execute the insert statement
            preparedStatement.executeUpdate();

            // Get the generated primary key (account_id)
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generatedAccountId = pkeyResultSet.getInt(1);

                // Return the newly created account object with the generated account_id
                return new Account(generatedAccountId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean doesUsernameExist(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Return true if the username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}