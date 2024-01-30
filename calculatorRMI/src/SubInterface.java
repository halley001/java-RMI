import java.rmi.*;
public interface SubInterface extends java.rmi.Remote{
    //Declaring method prototype
    public int sub(int x, int y) throws java.rmi.RemoteException;
}
