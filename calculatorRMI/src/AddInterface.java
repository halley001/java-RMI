import java.rmi.*;
public interface AddInterface extends Remote{
    //Declaring method prototype
    public int add(int x, int y) throws RemoteException;
}
