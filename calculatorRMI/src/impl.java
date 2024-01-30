//Java program to implement the four interfaces
import java.math.BigInteger;
import java.rmi.server.*;
public class impl extends UnicastRemoteObject
        implements AddInterface, SubInterface, MulInterface,DivInterface{
    //Public constructor to throw remote exception
    //from parent constructor
    public impl() throws Exception{super();}

    //Implementation of +-*/ interfaces
    public int add(int x, int y){return x + y;}
    public int sub(int x, int y)  {return x - y;}
    public int mul(int x, int y)  {return x * y;}
    public int div(int x, int y) {return x / y;}


}
