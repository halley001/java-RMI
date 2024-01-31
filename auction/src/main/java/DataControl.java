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

import java.rmi.*;

public interface DataControl extends Remote
{

    public void readBidders (String filename)
            throws RemoteException;

    public void writeBidders (String filename)
            throws RemoteException;

    public void readItems (String filename)
            throws RemoteException;

    public void writeItems (String filename)
            throws RemoteException;

    public Item makeBid (int bnum, String inum, int bamount, boolean noCheck)
            throws RemoteException;

    public boolean validateItemNumber (String inum)
            throws RemoteException;

    public boolean validateBidderNumber (int bnum)
            throws RemoteException;

    public Vector<Item> getItemsForBidder (int bidderNum)
            throws RemoteException;

    public Vector<Bidder> findBidderNumber (String s)
            throws RemoteException;

    public Bidder getBidderRecord (int bnum)
            throws RemoteException;

    public boolean updateBidderRecord (Bidder newBidder)
            throws RemoteException;

    public int getNextCashierNum ()
            throws RemoteException;
}