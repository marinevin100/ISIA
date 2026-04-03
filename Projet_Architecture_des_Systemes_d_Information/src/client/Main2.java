package client;




import javax.naming.InitialContext;
import javax.naming.NamingException;



public class Main2 {

  /**
   * @param args
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.err.println("Erreur 3 paramètres attendus: email, no emprunt et nouvel état");
      return ;
    }
    try {
      InitialContext ctx = new InitialContext();
      System.out.println("Accès au service distant");
    } catch (NamingException e1) {
      System.out.println(e1.getMessage());
    }
  }
}
