import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.*;
import java.io.*;

public class CheckOutPanel extends JPanel implements ActionListener
{
    private DataControl dc;

    // Data for Check Out Pane
    private JTextField bidderNumberField;
    private JTextField cashAmountField;
    private JTextField checkAmountField;
    private JTextField checkNumberField;
    private JTextField chargeAmountField;
    private JTextField chargeAmount2Field;
    private JLabel bidderName;
    private JLabel totalAmount;
    private JLabel unpaidAmount;

    private JTextArea itemsArea;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;

    private JButton getBidder;
    private JButton findBidder;
    private JButton saveBidder;
    private JButton printBidder;
    private final int numBids = 10;

    private Bidder person;
    private Vector<Item> itemVector;
    private int totalDue;
    private int unpaid;
    private int cashierNumber;


    public CheckOutPanel (DataControl parmDC, int cashNum)
    {
        super ();

        // set up data
        dc = parmDC;
        person = null;
        itemVector = null;
        totalDue = 0;
        unpaid = 0;
        cashierNumber = cashNum;

        setLayout (new BorderLayout ());

        // set up the North panel
        JPanel upperPanel = new JPanel ();
        upperPanel.setLayout (new GridLayout (2,3));

        // add the column headings
        upperPanel.add (new JLabel ("Enter Bidder Number:"));
        bidderNumberField = new JTextField ();
        bidderNumberField.addActionListener (this);
        upperPanel.add (bidderNumberField);

        getBidder = new JButton ("Get Bidder Information");
        getBidder.addActionListener (this);
        upperPanel.add (getBidder);

        upperPanel.add (new JLabel ("Bidder Name:  ", JLabel.RIGHT));
        bidderName = new JLabel ();
        upperPanel.add (bidderName);
        findBidder = new JButton ("Find Bidder Number");
        findBidder.addActionListener (this);
        upperPanel.add (findBidder);

        add (upperPanel, BorderLayout.NORTH);

        // set up the middle area
        //itemsArea = new JTextArea (10, 60);
        //add (itemsArea, BorderLayout.CENTER);

        // set up empty table
        tableModel = new DefaultTableModel ();
        tableModel.addColumn ("Item Num");
        tableModel.addColumn ("Description");
        tableModel.addColumn ("Bid Amount");

        //String[] columnNames = {"Item Num", "Description", "Bid Amount"};
        //Object[][] data = { {"", "", "" } };
        JTable table = new JTable(tableModel);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(400, 100));

        //Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);

        // set up the South panel
        JPanel lowerPanel = new JPanel ();
        lowerPanel.setLayout (new GridLayout (8,3));

        lowerPanel.add (new JLabel ("Total Due:"));
        lowerPanel.add (new JLabel ("$", JLabel.RIGHT));
        totalAmount = new JLabel();
        lowerPanel.add (totalAmount);

        lowerPanel.add (new JLabel ("Cash Amount:"));
        lowerPanel.add (new JLabel ("$", JLabel.RIGHT));
        cashAmountField = new JTextField();
        lowerPanel.add (cashAmountField);

        lowerPanel.add (new JLabel ("Check Amount:"));
        lowerPanel.add (new JLabel ("$", JLabel.RIGHT));
        checkAmountField = new JTextField();
        lowerPanel.add (checkAmountField);

        lowerPanel.add (new JLabel ("Check Number:"));
        lowerPanel.add (new JLabel (""));
        checkNumberField = new JTextField();
        lowerPanel.add (checkNumberField);

        lowerPanel.add (new JLabel ("Charge Amount:"));
        lowerPanel.add (new JLabel ("$", JLabel.RIGHT));
        chargeAmountField = new JTextField();
        lowerPanel.add (chargeAmountField);

        lowerPanel.add (new JLabel ("Charge Amount 2:"));
        lowerPanel.add (new JLabel ("$", JLabel.RIGHT));
        chargeAmount2Field = new JTextField();
        lowerPanel.add (chargeAmount2Field);

        lowerPanel.add (new JLabel ("Remaining Due:"));
        lowerPanel.add (new JLabel ("$", JLabel.RIGHT));
        unpaidAmount = new JLabel();
        lowerPanel.add (unpaidAmount);

        lowerPanel.add (new JLabel ("Cashier: " + cashierNumber));
        saveBidder = new JButton ("Save Bidder Information");
        saveBidder.addActionListener (this);
        lowerPanel.add (saveBidder);
        printBidder = new JButton ("Print Bidder Information");
        printBidder.addActionListener (this);
        lowerPanel.add (printBidder);

        add (lowerPanel, BorderLayout.SOUTH);



    }

    // handle button events
    public void actionPerformed( ActionEvent event )
    {
        if (event.getSource() == getBidder ||
                event.getSource() == bidderNumberField)
            doGetBidderInfo ();

        else if (event.getSource() == findBidder)
            doFindBidderInfo();

        else if (event.getSource() == saveBidder)
            doSaveBidderInfo();

        else if (event.getSource() == printBidder)
            doPrintBidderInfo();

    }



    public void doGetBidderInfo ()
    {
        String sbnum;
        int bnum;

        try
        {

            // verify a number was entered
            sbnum = bidderNumberField.getText ();
            try{
                bnum = Integer.parseInt(sbnum);

            }
            catch (NumberFormatException e)
            {
                //custom title, error icon
                JOptionPane.showMessageDialog(this,
                        "Bidder Number must be a number.",
                        "Error: Invalid Bidder Number",
                        JOptionPane.ERROR_MESSAGE);
                bidderNumberField.requestFocus();
                return;
            }

            // make sure bidder exists
            if (!dc.validateBidderNumber(bnum))
            {
                //custom title, error icon
                JOptionPane.showMessageDialog(this,
                        "Bidder Number does not exist.",
                        "Error: Invalid Bidder Number",
                        JOptionPane.ERROR_MESSAGE);
                bidderNumberField.requestFocus();
                return;
            }

            // get Bidder info
            person = dc.getBidderRecord (bnum);
            fillCheckOutPanel(bnum);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void fillCheckOutPanel(int bnum)
    {

        try
        {

            bidderName.setText (person.getName());
            cashAmountField.setText ("" + person.getCashAmount());
            checkAmountField.setText ("" + person.getCheckAmount());
            checkNumberField.setText ("" + person.getCheckNumber());
            chargeAmountField.setText ("" + person.getChargeAmount());
            chargeAmount2Field.setText ("" + person.getChargeAmount2());

            String[] columnNames = {"Item Num", "Description", "Bid Amount"};
            Object[]data;


            // won items       123456789012345678901234567890123456789012345678901234567890
            //itemsArea.setText("Item      Decription                        Bid\n"+
            //                  "----      ----------                        ---\n");
            totalDue = 0;
            itemVector = dc.getItemsForBidder (bnum);

            int tableSize = tableModel.getRowCount();
            //System.out.println("Table size: " + tableSize);
            for (int i = 0; i < tableSize; i++)
            {
                //System.out.println("Row: " + i + ", " + tableModel.getValueAt(0,1));
                tableModel.removeRow(0);
            }
            data = new Object [3];

            for (int i = 0; i < itemVector.size(); i++)
            {
                Item vi = itemVector.get(i);
                //data[i] = new Object [3];
                data[0] = vi.getItemNumber();
                data[1] = vi.getDescr();
                data[2] = "$" + vi.getBidAmount();
                tableModel.addRow (data);
                //itemsArea.append(//rightJust ("" + (i+1), 5)  +
                //                 leftJust (vi.getItemNumber(), 9) + " " +
                //               	leftJust (vi.getDescr(), 30) +
                //                rightJust ("$" + vi.getValue(), 7) +
                //                 rightJust ("$" + vi.getBidAmount(), 8) + "\n");
                totalDue = totalDue + vi.getBidAmount();
            }

            totalAmount.setText ("" + totalDue);
            unpaid = totalDue - (person.getCashAmount() +
                    person.getCheckAmount() + person.getChargeAmount());
            unpaidAmount.setText ("" + unpaid);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static String leftJust (String s, int size)
    {
        if (s.length() > size)
            return s.substring (0, size);

        String temp = "";
        for (int i =0; i < size - s.length(); i++)
            temp = temp + " ";
        return s + temp;
    }

    static String rightJust (String s, int size)
    {
        if (s.length() > size)
            return s.substring (0, size);

        String temp = "";
        for (int i =0; i < size - s.length(); i++)
            temp = temp + " ";
        return temp + s;
    }

    public void doFindBidderInfo ()
    {
        Vector <Bidder> bidderVector;

        try
        {

            String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Enter Part of Last Name:\n",
                    "Search for Bidder Number",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);

            if (s == null)
                return;

            bidderVector = dc.findBidderNumber(s);

            if (bidderVector.size() == 0)
            {
                //custom title, error icon
                JOptionPane.showMessageDialog(this,
                        "There are no bidders whose last name start with: " + s,
                        "Search: No Matching Bidders",
                        JOptionPane.ERROR_MESSAGE);
                bidderNumberField.requestFocus();
                return;
            }

            if (bidderVector.size() == 1)
            {
                // get Bidder info
                person = bidderVector.get(0);
                bidderNumberField.setText("" + person.getBidNumber());
                fillCheckOutPanel(person.getBidNumber());
                return;
            }

            Object[] possibilities = new Object[bidderVector.size()];
            for (int i = 0; i < bidderVector.size(); i++)
            {
                possibilities[i] = rightJust ("" + bidderVector.get(i).getBidNumber(), 3) + " " + bidderVector.get(i).getName();
            }

            s = (String)JOptionPane.showInputDialog(
                    this,
                    "Enter Part of Last Name:\n",
                    "Search for Bidder Number",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    null);

            if (s == null)
                return;

            for (int i = 0; i < bidderVector.size(); i++)
            {
                if (s.equals(possibilities[i]))
                {
                    person = bidderVector.get(i);
                    bidderNumberField.setText("" + person.getBidNumber());
                    fillCheckOutPanel(person.getBidNumber());
                    return;
                }
            }
            //bidderNumberField.setText (s);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void doSaveBidderInfo ()
    {
        int cashAmt;
        int checkAmt;
        int checkNum;
        int chargeAmt;
        int chargeAmt2;
        Bidder updateBidder;

        if (person == null)
        {
            //custom title, error icon
            JOptionPane.showMessageDialog(this,
                    "Enter a bidder number before printing.",
                    "Error: Do Data To Print",
                    JOptionPane.ERROR_MESSAGE);
            bidderNumberField.requestFocus();
            return;
        }

        // verify a number was entered
        try{
            cashAmt = Integer.parseInt(cashAmountField.getText ());
            checkAmt = Integer.parseInt(checkAmountField.getText ());
            checkNum = Integer.parseInt(checkNumberField.getText ());
            chargeAmt = Integer.parseInt(chargeAmountField.getText ());
            chargeAmt2 = Integer.parseInt(chargeAmount2Field.getText ());
        }
        catch (NumberFormatException e)
        {
            //custom title, error icon
            JOptionPane.showMessageDialog(this,
                    "Values must be a whole number.",
                    "Error: Invalid Number",
                    JOptionPane.ERROR_MESSAGE);
            cashAmountField.requestFocus();
            return;
        }

        unpaid = totalDue - (cashAmt + checkAmt + chargeAmt + chargeAmt2);
        unpaidAmount.setText ("" + unpaid);
        updateBidder = new Bidder ();
        updateBidder.setBidNumber (person.getBidNumber());
        updateBidder.setLName (person.getLName());
        updateBidder.setFName (person.getFName());
        updateBidder.setPhone (person.getPhone());
        updateBidder.setCashAmount (cashAmt);
        updateBidder.setCheckAmount (checkAmt);
        updateBidder.setCheckNumber (checkNum);
        updateBidder.setChargeAmount (chargeAmt);
        updateBidder.setChargeAmount2 (chargeAmt2);
        updateBidder.setCashier(cashierNumber);

        try
        {
            dc.updateBidderRecord (updateBidder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        doGetBidderInfo ();
    }

    public void doPrintBidderInfo ()
    {
        if (person == null)
        {
            //custom title, error icon
            JOptionPane.showMessageDialog(this,
                    "Enter a bidder number before printing.",
                    "Error: Do Data To Print",
                    JOptionPane.ERROR_MESSAGE);
            bidderNumberField.requestFocus();
            return;
        }

        try
        {
            doSaveBidderInfo ();

            // Set up the output file
            String filename = "t" + person.getBidNumber() + ".txt";
            FileWriter fw = new FileWriter (filename);
            BufferedWriter bw = new BufferedWriter (fw);
            PrintWriter pw = new PrintWriter (bw);

            pw.println ("St. Cyril & Methodius Mothers Club");
            pw.println ();
            pw.println ("Black & White Affair Charity Auction Receipt");
            pw.println ("November 3, 2006");
            pw.println ();
            pw.println ();
            pw.println ();
            pw.println (person.getName());
            pw.println ("Bid Number: " + person.getBidNumber());
            pw.println ();
            pw.println ();

            pw.println ("Item      Decription                        Bid");
            pw.println ("----      ----------                        ---");

            for (int i = 0; i < itemVector.size(); i++)
            {
                Item vi = itemVector.get(i);
                pw.println ( leftJust (vi.getItemNumber(), 9) + " " +
                        leftJust (vi.getDescr(), 30) +
                        //                 rightJust ("$" + vi.getValue(), 7) +
                        rightJust ("$" + vi.getBidAmount(), 8));

                //totalDue = totalDue + vi.getBidAmount();
            }

            //totalAmount.setText ("" + totalDue);
            //unpaid = totalDue - (person.getCashAmount() +
            //           person.getCheckAmount() + person.getChargeAmount());
            //unpaidAmount.setText ("" + unpaid);

            pw.println ();
            pw.println ();
            pw.println ();
            pw.println ("Total Due:     " + rightJust ("$" + totalDue, 20));
            pw.println ();
            pw.println ("Cash Amount:   " + rightJust ("$" + person.getCashAmount(), 20));
            pw.println ("Check Amount:  " + rightJust ("$" + person.getCheckAmount(), 20));
            pw.println ("Check Number:  " + rightJust ("" + person.getCheckNumber(), 20));
            pw.println ("Charge Amount: " + rightJust ("$" + person.getChargeAmount(), 20));
            pw.println ("Charge Amount2:" + rightJust ("$" + person.getChargeAmount2(), 20));
            pw.println ();
            pw.println ("Total Paid:    " + rightJust ("$" + (totalDue - unpaid) , 20));
            pw.close();

            // The actual print command
//    Process p = Runtime.getRuntime().exec("print " + filename);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }





}