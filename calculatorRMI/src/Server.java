import java.rmi.*;
import java.rmi.registry.*;

public class Server {
    public static void main(String[] args) throws Exception {
// Create an object of the interface implementation class
        impl obj = new impl();
// Binds the remote object by the name ADD
        Naming.rebind("ADD", obj);
        System.out.println("Server Started Successfully");
    }
}