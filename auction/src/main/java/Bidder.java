import java.io.*;

public class Bidder implements Serializable
{
    private int bidNumber;
    private String lname;
    private String fname;
    private String phone;
    private int cashAmount;
    private int checkAmount;
    private int checkNumber;
    private int chargeAmount;
    private int chargeAmount2;
    private int cashierNum;
    private int code;

    public Bidder()
    {
        bidNumber = -1;
        lname = "";
        fname = "";
        phone = "";
        cashAmount = 0;
        checkAmount = 0;
        checkNumber = -1;
        chargeAmount = 0;
        chargeAmount2 = 0;
        cashierNum = 0;
        code = 0;
    }

    public Bidder(int num, String pName)
    {
        bidNumber = num;
        lname = pName;
        fname = "";
        phone = "";
        cashAmount = 0;
        checkAmount = 0;
        checkNumber = -1;
        chargeAmount = 0;
        chargeAmount2 = 0;
        cashierNum = 0;
        code = 0;
    }

    public void setBidNumber(int val)
    {
        bidNumber = val;
    }

    public int getBidNumber ()
    {
        return bidNumber;
    }

    public void setName (String val)
    {
        lname = val;
    }

    public String getName ()
    {
        return lname + ", " + fname;
    }

    public void setLName (String val)
    {
        lname = val;
    }

    public String getLName ()
    {
        return lname;
    }

    public void setFName (String val)
    {
        fname = val;
    }

    public String getFName ()
    {
        return fname;
    }

    public void setPhone (String val)
    {
        phone = val;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setCashAmount (int val)
    {
        cashAmount = val;
    }

    public int getCashAmount ()
    {
        return cashAmount;
    }

    public void setCheckAmount (int val)
    {
        checkAmount = val;
    }

    public int getCheckAmount ()
    {
        return checkAmount;
    }

    public void setCheckNumber (int val)
    {
        checkNumber = val;
    }

    public int getCheckNumber ()
    {
        return checkNumber;
    }

    public void setChargeAmount (int val)
    {
        chargeAmount = val;
    }

    public int getChargeAmount ()
    {
        return chargeAmount;
    }

    public void setChargeAmount2 (int val)
    {
        chargeAmount2 = val;
    }

    public int getChargeAmount2 ()
    {
        return chargeAmount2;
    }

    public int getTotalAmount ()
    {
        return cashAmount + checkAmount + chargeAmount + chargeAmount2;
    }

    public void setCode (int val)
    {
        code = val;
    }

    public int getCode ()
    {
        return code;
    }

    public void setCashier (int val)
    {
        cashierNum = val;
    }

    public int getCashier ()
    {
        return cashierNum;
    }

    public void inFromCVS ( String arg)
    {
        String[] results = getFromCVSForm (arg);
        try{
            bidNumber = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            bidNumber = -1;
        }

        results = getFromCVSForm (results[1]);
        lname = results[0];

        results = getFromCVSForm (results[1]);
        fname = results[0];

        results = getFromCVSForm (results[1]);
        phone = results[0];

        results = getFromCVSForm (results[1]);
        try{
            cashAmount = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            cashAmount = 0;
        }

        results = getFromCVSForm (results[1]);
        try{
            checkAmount = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            checkAmount = 0;
        }

        results = getFromCVSForm (results[1]);
        try{
            checkNumber = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            checkNumber = -1;
        }

        results = getFromCVSForm (results[1]);
        try{
            chargeAmount = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            chargeAmount = 0;
        }

        results = getFromCVSForm (results[1]);
        try{
            chargeAmount2 = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            chargeAmount2 = 0;
        }

        results = getFromCVSForm (results[1]);
        try{
            cashierNum = Integer.parseInt (results[0]);
        }
        catch (NumberFormatException e)
        {
            cashierNum = 0;
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
        String retString = "" + bidNumber + ",";

        retString = retString + putInCVSForm (lname) + "," +
                putInCVSForm (fname) + "," +
                putInCVSForm (phone) + "," +
                cashAmount + "," + checkAmount + "," +
                checkNumber + "," + chargeAmount + "," +
                chargeAmount2 + "," + cashierNum;

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
        Bidder i1 = new Bidder(1, "Hello There");
        System.out.println (i1.outAsCVS());

        i1 = new Bidder(1, "Hello, There");
        System.out.println (i1.outAsCVS());

        i1 = new Bidder(1, "Hello \" There");
        System.out.println (i1.outAsCVS());


        i1.inFromCVS ("1,\"Hello, \"\"The\"\"re\",50,,,");
        i1.inFromCVS ("1,Troy,Pat,630-257-6967,1,2,3,4,5,6");
        System.out.println (i1.getBidNumber());
        System.out.println (i1.getName());
        System.out.println (i1.getLName());
        System.out.println (i1.getFName());
        System.out.println (i1.getPhone());
        System.out.println (i1.getCashAmount());
        System.out.println (i1.getCheckAmount());
        System.out.println (i1.getCheckNumber());
        System.out.println (i1.getChargeAmount());
        System.out.println (i1.getChargeAmount2());
        System.out.println (i1.getCashier());
        System.out.println (i1.outAsCVS());
    }
}