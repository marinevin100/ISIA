package ejb.sessions;

public class ChangementEtatNonAutoriseException extends Exception{

    public ChangementEtatNonAutoriseException() {
    }

    public ChangementEtatNonAutoriseException(String message) {
        super(message);
    }

}
