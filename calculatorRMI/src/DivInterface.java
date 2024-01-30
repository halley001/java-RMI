import java.rmi.*;
public interface DivInterface extends java.rmi.Remote {
    // Declaring method prototype
    public int div(int x, int y) throws  java.rmi.RemoteException;
}
