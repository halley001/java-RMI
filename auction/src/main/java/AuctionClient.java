// imports for GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

// imports for RMI
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class AuctionClient extends JFrame
{
    private DataControl dc;

    private JTabbedPane jTabbedPane = null;
    private JPanel jContentPane = null;

    private Container container;
    private FlowLayout layout;

    private int cashierNumber;


    public AuctionClient (DataControl dcParm)
    {
        super ("Auction Interface");

        // set up data
        dc = dcParm;
        //dc.readBidders ("Bidders.csv");
        //dc.readItems ("Items.csv");

        try{
            cashierNumber = dc.getNextCashierNum();
        } catch (Exception e)
        { e.printStackTrace(); }

        layout = new FlowLayout();

        // get content pane and set its layout
        container = getContentPane();
        container.setLayout( layout );

        jTabbedPane = new JTabbedPane();
        //jTabbedPane.add("Users", getJUsersPane());
        //jTabbedPane.add("Items", getJItemsPane());
        jTabbedPane.add("Bids", new BidPanel(dc));
        jTabbedPane.add("Check Out", new CheckOutPanel(dc, cashierNumber));

        container.add (jTabbedPane);

        setSize( 700, 500 );
        setVisible( true );

    }



    public static void main( String[] args )
    {

        try
        {
            String hostName;
            InputStreamReader is =
                    new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);
            System.out.println(
                    "Enter the RMIRegistry host namer:");
            hostName = br.readLine();

            DataControl dc = (DataControl) Naming.lookup(
                    "rmi://" + hostName + "/AuctionServer");

            AuctionClient application = new AuctionClient(dc);
            application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        }
        catch (MalformedURLException murle)
        {
            System.out.println();
            System.out.println( "MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re)
        {
            System.out.println();
            System.out.println( "RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe)
        {
            System.out.println();
            System.out.println( "NotBoundException");
            System.out.println(nbe);
        }
        catch (Exception e)
        {
            System.out.println();
            System.out.println( "Exception");
            System.out.println(e);
        }
    }

}