import java.io.*;

class Item implements Serializable
{
    private String category;
    private String itemNumber;
    private String descr;
    private int value;
    private int cost;
    private int bidder;
    private int bidAmount;
    private int code;

    public Item ()
    {
        category = "";
        itemNumber = "";
        descr = "";
        value = 0;
        cost = 0;
        bidder = -1;
        bidAmount = 0;
        code = 0;
    }

    public Item(String num, String pdescr, int pval)
    {
        category = "";
        itemNumber = num;
        descr = pdescr;
        value = pval;
        cost = 0;
        bidder = -1;
        bidAmount = 0;
        code = 0;
    }

    public Item(String num, String pdescr, int pval, int pcost)
    {
        category = "";
        itemNumber = num;
        descr = pdescr;
        value = pval;
        cost = pcost;
        bidder = -1;
        bidAmount = 0;
        code = 0;
    }

    public void setCategory (String val)
    {
        category= val;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setItemNumber(String val)
    {
        itemNumber = val;
    }

    public String getItemNumber ()
    {
        return itemNumber;
    }

    public void setDescr (String val)
    {
        descr = val;
    }

    public String getDescr ()
    {
        return descr;
    }

    public void setValue (int val)
    {
        value = val;
    }

    public int getValue ()
    {
        return value;
    }

    public void makeBid (int pbidder, int pbid)
    {
        bidder = pbidder;
        bidAmount = pbid;
    }

    public int getBidder()
    {
        return bidder;
    }

    public int getBidAmount()
    {
        return bidAmount;
    }

    public int getDonationAmount ()
    {
        if (value >= bidAmount)
            return 0;
        else
            return bidAmount - value;
    }

    public int getProfitAmount ()
    {
        return bidAmount - cost;
    }

    public void setCode (int val)
    {
        code = val;
    }

    public int getCode ()
    {
        return code;
    }

    public void inFromCVS ( String arg)
    {
        System.out.println (arg);

        String[] results = getFromCVSForm (arg);
        category = results[0];

        results = getFromCVSForm (results[1]);
        itemNumber = results[0];

        results = getFromCVSForm (results[1]);
        descr = results[0];

        results = getFromCVSForm (results[1]);
        try{
            value = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            value = 0;
        }

        results = getFromCVSForm (results[1]);
        try{
            bidder = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            bidder = -1;
        }

        results = getFromCVSForm (results[1]);
        try{
            bidAmount = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            bidAmount = 0;
        }
    }

    static public String [] getFromCVSForm (String arg)
    {
        String[] results = new String[2];
        int endPos;
        boolean goOn;

        if (arg.length() == 0)
        {
            results[0] = "";
            results[1] = "";
        }
        else if (arg.charAt(0) == '\"')
        {
            endPos = 1;
            goOn = true;

            while (goOn)
            {
                if (arg.charAt(endPos) != '\"')
                    endPos++;

                else if (endPos+1 >= arg.length())
                    goOn = false;

                else if (arg.charAt(endPos+1) == '\"')
                    endPos = endPos + 2;

                else
                    goOn = false;
            }
            String temp = arg.substring (1,endPos);
            results[0] = temp.replaceAll("\"\"", "\"");
            results[1] = arg.substring (endPos+2);
        }
        else
        {
            results = arg.split (",", 2);
        }
        return results;
    }

    public String outAsCVS ()
    {
        String retString = putInCVSForm(category) + "," +
                putInCVSForm(itemNumber) + "," +
                putInCVSForm (descr) + "," +
                value + "," + bidder + "," + bidAmount;

        return retString;
    }

    static public String putInCVSForm (String arg)
    {
        String retString = "";
        boolean putInQuotes = false;

        if (arg.contains(",") || arg.contains("\""))
        {
            putInQuotes = true;
            retString = retString + "\"";
        }

        String[] result = arg.split ("\"", -1);
        retString = retString + result[0];
        for (int i = 1; i < result.length; i++)
            retString = retString + "\"\"" + result[i];

        if (putInQuotes)
            retString = retString + "\"";

        return (retString);
    }


    public static void main (String[] args)
    {
        Item i1 = new Item("1", "Hello There", 50);
        System.out.println (i1.outAsCVS());

        i1 = new Item("1", "Hello, There", 50);
        System.out.println (i1.outAsCVS());

        i1 = new Item("1", "Hello \" There", 50);
        System.out.println (i1.outAsCVS());


        i1.inFromCVS ("Category 1,1,\"Hello, \"\"The\"\"re\",50,-1,0");
        System.out.println ("Category: " + i1.getCategory());
        System.out.println (i1.getItemNumber());
        System.out.println (i1.getDescr());
        System.out.println (i1.getValue());
        System.out.println (i1.getBidder());
        System.out.println (i1.getBidAmount());

        System.out.println ();
        i1.inFromCVS (",,,,");
        System.out.println (i1.getItemNumber());
        System.out.println (i1.getDescr());
        System.out.println (i1.getValue());
        System.out.println (i1.getBidder());
        System.out.println (i1.getBidAmount());
    }
}