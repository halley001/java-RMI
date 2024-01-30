import java.rmi.*;
import java.math.BigInteger;

public interface Factorial extends Remote{

    // Declaring the method
    public int fact(int y) throws RemoteException;
}
