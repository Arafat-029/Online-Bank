package dal;

import models.Account;
import models.User;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DALAccount {

    public void addAccount(Account account) {
        String query = "INSERT INTO accounts (account_number, balance, account_type, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, account.getAccountNumber());
            statement.setDouble(2, account.getBalance());
            statement.setString(3, account.getAccountType());
            statement.setInt(4, account.getOwner().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccountByNumber(String accountNumber) {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(
                    resultSet.getString("account_number"),
                    resultSet.getDouble("balance"),
                    new User(resultSet.getInt("user_id"), null, null, null, null, null), // On ne charge pas tout l'utilisateur ici
                    resultSet.getString("account_type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAccountsByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(new Account(
                    resultSet.getString("account_number"),
                    resultSet.getDouble("balance"),
                    new User(userId, null, null, null, null, null), // On ne charge pas tout l'utilisateur ici
                    resultSet.getString("account_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public void updateAccount(Account account) {
        String query = "UPDATE accounts SET balance = ?, account_type = ? WHERE account_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getAccountType());
            statement.setString(3, account.getAccountNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(String accountNumber) {
        String query = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}