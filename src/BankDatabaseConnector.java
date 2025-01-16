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
        String sql = "INSERT INTO Customers (Name,Address,Email,Phone,Username,Password) VALUES (?,?,?,?,?,?);";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.retrieveFullName());
            pstmt.setString(2, customer.retrieveAddress());
            pstmt.setString(3, customer.retrieveEmail());
            pstmt.setString(4, customer.retrievePhone());
            pstmt.setString(5, customer.retriveUserName());
            pstmt.setString(6, customer.retrievePassword());
            pstmt.executeUpdate();
            System.out.println(
                    "Customer inserted: " + customer.retrieveFullName() + " " + customer.retrieveAddress() + " "
                            + customer.retrieveEmail() + " " + customer.retrievePhone() + " "
                            + customer.retriveUserName() + " " + customer.retrievePassword());
        } catch (SQLException ex) {
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
            System.out.println("Bank account inserted: " + bankAccount.retrieveAccountNumber() + " "
                    + retrieveCustomerID(bankAccount) + " "
                    + bankAccount.retrieveFullName() + " " + bankAccount.retrieveBalance() + " "
                    + bankAccount.retrieveAccountType() + " " + bankAccount.retrieveCreationDate());
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
    }

    public boolean findUser(String username, String password) {
        String sql = "SELECT * FROM Customers WHERE Username = ? AND Password = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
        return false;
    }

    public BankAccount getUserDetails(String username) {
        String sql = "SELECT c.Name, a.AccountNumber, a.Balance, a.AccountType, c.Phone, c.Email, c.customerID FROM Customers c JOIN Accounts a ON c.customerID = a.customerID WHERE c.Username = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                BankAccount bankAccount = new BankAccount();
                UserSession userSession = UserSession.getInstance();
                bankAccount.setFullName(rs.getString("Name"));
                bankAccount.setAccountNumber(rs.getLong("AccountNumber"));
                bankAccount.setBalance(rs.getDouble("Balance"));
                bankAccount.setAccountType(rs.getString("AccountType"));
                userSession.setPhoneNumber(rs.getString("Phone"));
                userSession.setEmail(rs.getString("Email"));
                userSession.setCustomerID(rs.getInt("customerID"));

                return bankAccount;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
        return null;
    }

    public void updateCustomerDetails(String username, String newEmail, String newPassword, String newPhoneNumber) {
        try (Connection conn = getConnection()) {

            if (newEmail != null && !newEmail.isEmpty()) {
                updateField(conn, "Email", newEmail, username);
            }

            if (newPassword != null && !newPassword.isEmpty()) {
                updateField(conn, "Password", newPassword, username);
            }

            if (newPhoneNumber != null && !newPhoneNumber.isEmpty()) {
                updateField(conn, "Phone", newPhoneNumber, username);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BankDatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL Exception: " + ex.getMessage());
        }
    }

    private static void updateField(Connection conn, String fieldName, String newValue, String username)
            throws SQLException {
        String sql = "UPDATE Customers SET " + fieldName + " = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println(fieldName + " updated for username: " + username);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Creating SQL connection");
    }
}
