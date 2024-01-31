import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BidPanel extends JPanel implements ActionListener
{
    private DataControl dc;

    // Data for Bid Pane
    private JTextField bidderNums[];
    private JTextField itemNums[];
    private JTextField bidAmounts[];
    private JButton makeBids;
    private JButton clearBids;
    private final int numBids = 10;


    public BidPanel (DataControl parmDC)
    {
        super ();

        // set up data
        dc = parmDC;

        setLayout (new GridLayout(numBids+2, 3));

        // add the column headings
        add (new JLabel ("Item Number"));
        add (new JLabel ("Bidder Number"));
        add (new JLabel ("Bid Amount"));

        // add the Textfield to enter bids
        bidderNums = new JTextField [numBids];
        itemNums = new JTextField [numBids];
        bidAmounts = new JTextField [numBids];

        for ( int count = 0; count < numBids ; count++ )
        {
            itemNums[ count ] = new JTextField ();
            add (itemNums[count]);
            bidderNums[ count ] = new JTextField ();
            add (bidderNums[count]);
            bidAmounts[ count ] = new JTextField ();
            add (bidAmounts[count]);
        }

        // add the make bid button
        makeBids = new JButton ("Record Bids");
        makeBids.addActionListener (this);
        add (makeBids);

        // add the make bid button
        clearBids = new JButton ("Clear Bids");
        clearBids.addActionListener (this);
        add (clearBids);

    }

    // handle button events
    public void actionPerformed( ActionEvent event )
    {
        if (event.getSource() == makeBids)
            doMakeBids ();

        else if (event.getSource() == clearBids)
            doClearBids();

    }

    public void doMakeBids ()
    {
        String sbnum;
        String inum;
        String sbamount;
        int bnum;
        int bamount;

        try
        {

            // check for valid data
            for ( int count = 0; count < numBids ; count++ )
            {
                sbnum = bidderNums[ count ].getText ();
                inum = itemNums[ count ].getText ();
                sbamount = bidAmounts[ count ].getText ();

                //System.out.println ("*" + sbnum + "*" + inum + "*" + sbamount + "*");
                if (sbnum.length() == 0 && inum.length() == 0 && sbamount.length() == 0)
                    ;
                else if (sbnum.length() == 0)
                {
                    //custom title, error icon
                    JOptionPane.showMessageDialog(this,
                            "Bidder Number, Item Number and Bid Amount must be filled in.",
                            "Error: Blank Bidder Number",
                            JOptionPane.ERROR_MESSAGE);
                    bidderNums[count].requestFocus();
                    return;
                }
                else if (inum.length() == 0)
                {
                    //custom title, error icon
                    JOptionPane.showMessageDialog(this,
                            "Bidder Number, Item Number and Bid Amount must be filled in.",
                            "Error: Blank Item Number",
                            JOptionPane.ERROR_MESSAGE);
                    itemNums[count].requestFocus();
                    return;
                }
                else if (sbamount.length() == 0)
                {
                    //custom title, error icon
                    JOptionPane.showMessageDialog(this,
                            "Bidder Number, Item Number and Bid Amount must be filled in.",
                            "Error: Blank Bidder Amount",
                            JOptionPane.ERROR_MESSAGE);
                    bidAmounts[count].requestFocus();
                    return;
                }
                else
                {
                    if (!dc.validateItemNumber(inum))
                    {
                        //custom title, error icon
                        JOptionPane.showMessageDialog(this,
                                "Item Number does not exist.",
                                "Error: Invalid Bidder Number",
                                JOptionPane.ERROR_MESSAGE);
                        itemNums[count].requestFocus();
                        return;
                    }

                    try{
                        bnum = Integer.parseInt(sbnum);
                        //System.out.println (sbnum + ", " + bnum );
                        if (!dc.validateBidderNumber(bnum))
                        {
                            //custom title, error icon
                            JOptionPane.showMessageDialog(this,
                                    "Bidder Number does not exist.",
                                    "Error: Invalid Bidder Number",
                                    JOptionPane.ERROR_MESSAGE);
                            bidderNums[count].requestFocus();
                            return;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        //custom title, error icon
                        JOptionPane.showMessageDialog(this,
                                "Bidder Number must be a number.",
                                "Error: Invalid Bidder Number",
                                JOptionPane.ERROR_MESSAGE);
                        bidderNums[count].requestFocus();
                        return;
                    }


                    try{
                        bamount = Integer.parseInt(sbamount);
                    }
                    catch (NumberFormatException e)
                    {
                        //custom title, error icon
                        JOptionPane.showMessageDialog(this,
                                "Bid Amount must be a number.",
                                "Error: Invalid Bid Amount",
                                JOptionPane.ERROR_MESSAGE);
                        bidAmounts[count].requestFocus();
                        return;
                    }
                }
            }

            // process entries
            for ( int count = 0; count < numBids ; count++ )
            {
                sbnum = bidderNums[ count ].getText ();
                inum = itemNums[ count ].getText ();
                sbamount = bidAmounts[ count ].getText ();

                if (sbnum.length() == 0 && inum.length() == 0 && sbamount.length() == 0)
                    ;
                else
                {
                    try{
                        bnum = Integer.parseInt(sbnum);
                        bamount = Integer.parseInt(sbamount);
                        Item retItem = dc.makeBid (bnum, inum, bamount, false);

                        // check for valid return
                        if (retItem.getCode() == -3)
                        {
                            int n = JOptionPane.showConfirmDialog(this,
                                    "The bid for item " + inum +
                                            " that was just entered \n   for Bidder: " +
                                            bnum + "\n   for Amount: " + bamount +
                                            "\nalready has a bid.\n\n" +
                                            "The previous bid is\n   for Bidder: " +
                                            retItem.getBidder() + "\n   for Amount: " +
                                            retItem.getBidAmount() +
                                            "\n\nDo you wish to proceed with this new bid?",
                                    "Bid Already Exists for Item",
                                    JOptionPane.YES_NO_OPTION);
                            if (n == JOptionPane.YES_OPTION)

                                retItem = dc.makeBid (bnum, inum, bamount, true);

                        }

                        // check for valid return
                        if (retItem.getCode() == 1)
                        {
                            bidderNums[ count ].setText ("");
                            itemNums[ count ].setText ("");
                            bidAmounts[ count ].setText ("");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            itemNums[0].requestFocus();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void doClearBids ()
    {
        for ( int count = 0; count < numBids ; count++ )
        {
            bidderNums[ count ].setText ("");
            itemNums[ count ].setText ("");
            bidAmounts[ count ].setText ("");
        }
        itemNums[0].requestFocus();
    }



}