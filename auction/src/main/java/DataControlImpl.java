/**
 * DataControl.java
 *
 * @author Pat Troy
 */

import java.io.*;  		// IO class stuff
import java.util.*;

import java.util.Map;           // Superclass of TreeMap
import java.util.TreeMap;       // A container that supports lookup of values
// using keys
import java.util.Iterator;      // Used to traverse the TreeMap



public class DataControlImpl
        extends 	java.rmi.server.UnicastRemoteObject
        implements	DataControl
{
    private SortedMap<Integer, Bidder> bidders;
    private SortedMap<String, Item> items;
    private int cashierNumber;

    public DataControlImpl ()
            throws java.rmi.RemoteException
    {
        super();

        bidders = Collections.synchronizedSortedMap
                (new TreeMap<Integer, Bidder> () );
        items   = Collections.synchronizedSortedMap
                (new TreeMap<String, Item> () );
        cashierNumber = 0;
    }

    public void readBidders (String filename)
            throws java.rmi.RemoteException
    {
        try
        {
            String s1;
            Bidder b1;

            BufferedReader br = new BufferedReader (new FileReader (filename));

            s1 = br.readLine();

            while (s1 != null)
            {
                b1 = new Bidder();
                b1.inFromCVS(s1);

                bidders.put ( b1.getBidNumber(), b1);

                s1 = br.readLine();
            }

            br.close();
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void writeBidders (String filename)
            throws java.rmi.RemoteException
    {
        try
        {
            // Set up the output file
            FileWriter fw = new FileWriter (filename);
            BufferedWriter bw = new BufferedWriter (fw);
            PrintWriter pw = new PrintWriter (bw);

            // for all items in the TreeMap
            for (Map.Entry<Integer, Bidder> e : bidders.entrySet())
                pw.println(e.getValue().outAsCVS() );

            pw.close();
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    public void readItems (String filename)
            throws java.rmi.RemoteException
    {
        String s1;
        Item i1;

        try
        {
            BufferedReader br = new BufferedReader (new FileReader (filename));

            s1 = br.readLine();

            while (s1 != null)
            {
                i1 = new Item();
                i1.inFromCVS(s1);

                items.put ( i1.getItemNumber(), i1);

                s1 = br.readLine();
            }

            br.close();
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void writeItems (String filename)
            throws java.rmi.RemoteException
    {
        try
        {
            // Set up the output file
            FileWriter fw = new FileWriter (filename);
            BufferedWriter bw = new BufferedWriter (fw);
            PrintWriter pw = new PrintWriter (bw);

            // for all items in the TreeMap
            for (Map.Entry<String, Item> e : items.entrySet())
                pw.println(e.getValue().outAsCVS() );

            pw.close();
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public Item makeBid (int bnum, String inum, int bamount, boolean noCheck)
            throws java.rmi.RemoteException
    {
        Item returnItem = new Item();
        Item bidItem;

        // verify item exists
        if (!items.containsKey (inum))
        {
            returnItem.setCode (-2);   // no such item exists
            return returnItem;
        }

        // verify bidder exists
        if (!bidders.containsKey (bnum))
        {
            returnItem.setCode (-4);   // no such bidder exists
            return returnItem;
        }

        // get item from Item Map
        bidItem = items.get (inum );

        // check if a bid has been made
        if (noCheck == false && bidItem.getBidder() != -1)
        {
            returnItem.setCode (-3); // item already has bid
            returnItem.makeBid (bidItem.getBidder(), bidItem.getBidAmount());
            return returnItem;
        }

        // store the bid
        bidItem.makeBid (bnum, bamount);
        returnItem.setCode (1);	// successful bid
        return returnItem;
    }

    public boolean validateItemNumber (String inum)
            throws java.rmi.RemoteException
    {
        // verify item exists
        if (items.containsKey (inum))
            return true;
        else
            return false;
    }

    public boolean validateBidderNumber (int bnum)
            throws java.rmi.RemoteException
    {
        //System.out.println ("In validateBidderNumber: " + bnum);
        // verify item exists
        if (bidders.containsKey (bnum))
            return true;
        else
            return false;
    }

    public Vector<Item> getItemsForBidder (int bidderNum)
            throws java.rmi.RemoteException
    {
        Vector<Item> itemVector = new Vector<Item> ();

        // for all items in the Item Map
        for (Map.Entry<String, Item> e : items.entrySet())
        {
            if (e.getValue().getBidder() == bidderNum)
            {
                itemVector.addElement (e.getValue() );
                //System.out.println(e.getValue().outAsCVS() );
            }
        }
        return itemVector;
    }

    public Vector<Bidder> findBidderNumber (String s)
            throws java.rmi.RemoteException
    {
        Vector<Bidder> bidderVector = new Vector<Bidder> ();

        for (Map.Entry<Integer, Bidder> e : bidders.entrySet())
        {
            // fix make lower case!!
            //String name = new String (e.getValue().getName());
            //name.toLowerCase();
            //String s2 = new String (s);
            //s2.toLowerCase();
            //System.out.println (name + ", " + s2);
            //if (name.startsWith(s2))
            if (e.getValue().getName().toLowerCase().startsWith(s.toLowerCase()))
            {
                bidderVector.addElement(e.getValue());
            }
        }
        return bidderVector;
    }
    public Bidder getBidderRecord (int bnum)
            throws java.rmi.RemoteException
    {
        if (validateBidderNumber(bnum))
            return bidders.get(bnum);
        else
            return null;
    }

    public boolean updateBidderRecord (Bidder newBidder)
            throws java.rmi.RemoteException
    {
        if (validateBidderNumber(newBidder.getBidNumber()))
        {
            bidders.put (newBidder.getBidNumber(), newBidder);
            return true;
        }
        else
            return false;
    }

    public int getNextCashierNum ()
            throws java.rmi.RemoteException
    {
        cashierNumber ++;

        return cashierNumber;
    }

    static public void main (String[] args)
    {
        try
        {
            DataControlImpl dc = new DataControlImpl ();

            dc.readBidders ("Bidders.csv");

            dc.writeBidders ("outBid.txt");

            dc.readItems ("Items.csv");

            dc.writeItems ("outItem.txt");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}