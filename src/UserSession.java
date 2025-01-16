public class UserSession {

    private static UserSession instance = new UserSession();

    private String username;
    private String fullName;
    private Long accountNumber;
    private double balance;
    private String accountType;
    private String phoneNumber;
    private String password;
    private String email;
    private int userID;

    private BankDatabaseConnector bankDatabaseConnector = new BankDatabaseConnector();

    private UserSession() {
    }

    public static UserSession getInstance() {
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getfullName() {
        return fullName;
    }

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCustomerID() {
        return userID;
    }

    public void setCustomerID(int userID) {
        this.userID = userID;
    }

    public void resetSession() {
        username = null;
        fullName = null;
        accountNumber = null;
        balance = 0.0;
        accountType = null;
        phoneNumber = null;
        email = null;
        password = null;
    }
}
