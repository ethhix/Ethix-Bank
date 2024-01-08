import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankDatabaseConnector {
    private static String url = "jdbc:mysql://localhost:3306/bankDB";
    private static String user = "root";
    private static String password = "Ethix46778$";

    createAccountPageController createAccountPageController = new createAccountPageController();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void insertCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (Name,Address,Email,Phone) VALUES (?,?,?,?);";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.retrieveFullName());
            pstmt.setString(2, customer.retrieveAddress());
            pstmt.setString(3, customer.retrieveEmail());
            pstmt.setString(4, customer.retrievePhone());
            pstmt.executeUpdate();
            System.out.println("Customer inserted: " + customer.retrieveFullName() + " " + customer.retrieveAddress() + " " 
            + customer.retrieveEmail() + " " + customer.retrievePhone());
        } catch(SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
    }

    public static void insertBankAccount(BankAccount bankAccount) {
        String sql = "INSERT INTO Accounts (AccountNumber,CustomerID,fullName,Balance,AccountType,CreationDate) VALUES (?,?,?,?,?,?);";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bankAccount.createAccountNumber());
            pstmt.setInt(2, retrieveCustomerID(bankAccount));
            pstmt.setString(3, bankAccount.retrieveFullName());
            pstmt.setDouble(4, bankAccount.retrieveBalance());
            pstmt.setString(5, bankAccount.retrieveAccountType());
            pstmt.setString(6, bankAccount.retrieveCreationDate());
            pstmt.executeUpdate();
            System.out.println("Bank account inserted: " + bankAccount.retrieveAccountNumber() + " " + retrieveCustomerID(bankAccount) + " " 
            + bankAccount.retrieveFullName() + " " + bankAccount.retrieveBalance() + " " + bankAccount.retrieveAccountType() + " " + bankAccount.retrieveCreationDate());
        } catch(SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
    }

    public static int retrieveCustomerID(BankAccount bankAccount) {
        String sql = "SELECT CustomerID FROM Customers WHERE Name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bankAccount.retrieveFullName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("CustomerID");
            }
        } catch(SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
        return 0;
    }

    public static void deleteAllCustomers() {
        String sql = "DELETE FROM Customers";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("All customers deleted");
        } catch(SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
    }

    public static void deleteAllBankAccounts() {
        String sql = "DELETE FROM Accounts";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("All Accounts deleted");
        } catch(SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Creating SQL connection");
    }
}
