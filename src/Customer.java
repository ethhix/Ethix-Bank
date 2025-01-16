public class Customer {

    private createAccountPageController createAccountPageController;

    public Customer(createAccountPageController createAccountPageController) {
        this.createAccountPageController = createAccountPageController;
    }

    public String retriveUserName() {
        return createAccountPageController.getUserName();
    }

    public String retrievePassword() {
        return createAccountPageController.getPassword();
    }

    public String retrieveFullName() {
        return createAccountPageController.getFirstName() + " " + createAccountPageController.getLastName();
    }

    public String retrieveAddress() {
        return createAccountPageController.getAddress();
    }

    public String retrieveEmail() {
        return createAccountPageController.getEmail();
    }

    public String retrievePhone() {
        return filterPhoneNumber(createAccountPageController.getPhone());
    }

    public String filterPhoneNumber(String phone) {
        String phoneNumber = createAccountPageController.getPhone();
        phoneNumber = phoneNumber.replaceAll("[^0-9]+", "");
        return phoneNumber;
    }

    public boolean isValid() {
        return !retrieveFullName().isEmpty() && !retrieveAddress().isEmpty() && !retrieveEmail().isEmpty()
                && !retrievePhone().isEmpty()
                && !retriveUserName().isEmpty() && !retrievePassword().isEmpty();
    }
}
