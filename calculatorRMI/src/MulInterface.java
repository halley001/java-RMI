import java.rmi.*;
public interface MulInterface extends java.rmi.Remote{
    //Declaring method prototype
    public int mul(int x, int y) throws java.rmi.RemoteException;
}
