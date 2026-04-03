package ejb.sessions;

public class CreditsInsuffisantsException extends Exception{

    public CreditsInsuffisantsException() {
    }

    public CreditsInsuffisantsException(String message) {
        super(message);
    }

}
