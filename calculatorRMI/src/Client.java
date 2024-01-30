import java.rmi.*;
import java.util.*;
public class Client {
    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n1.Addition\n2.Subtraction\n3.Multiplication\n4.Division\n5.Exit");
            System.out.println("Enter option");
            int opt = sc.nextInt();
            if (opt==5) {
            break;
            }
            System.out.println("Enter first number");
            int a = sc.nextInt();

            System.out.println("Enter second number");
            int b = sc.nextInt();

            int n;
            switch (opt){
                case 1:
                    //Lookup method to find reference of remote object ADD
                    AddInterface obj = (AddInterface)Naming.lookup("ADD");
                    n = obj.add(a, b);
                    System.out.println("Addition" +n);
                    break;

                case 2:
                    SubInterface obj1 = (SubInterface)Naming.lookup("ADD");
                    n = obj1.sub(a,b);
                    System.out.println("Subtraction" +n);
                    break;

                case 3:
                    MulInterface obj2 = (MulInterface)Naming.lookup("ADD");
                    n = obj2.mul(a,b);
                    System.out.println("Multiplication" +n);
                    break;

                case 4:
                    DivInterface obj3 =(DivInterface) Naming.lookup("ADD");
                    n = obj3.div(a,b);
                    System.out.println("Division" +n);
                    break;

            }
        }
    }
}
