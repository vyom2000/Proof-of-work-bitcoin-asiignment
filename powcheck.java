import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
class powcheck
{
     public static byte[] getSHA(String input) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return md.digest(input.getBytes(StandardCharsets.UTF_8));
	}

    public static byte[] get(String input,File inp) throws Exception
	{
        InputStream in = new FileInputStream(inp);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] block = new byte[4096];
        int length;
        while ((length = in.read(block)) > 0) 
        {
                digest.update(block, 0, length);
        }
        return digest.digest();
    }

    public static String toHexString(byte[] hash)
	{
		BigInteger number = new BigInteger(1, hash);
    	StringBuilder hexString = new StringBuilder(number.toString(16));
    	while (hexString.length() < 64)
		{
			hexString.insert(0, '0');
		}
		return hexString.toString();
	}

    static String hexToBinary(String hex)
    {
        String binary = "";
        hex = hex.toUpperCase();
        HashMap<Character, String> hashMap= new HashMap<Character, String>();
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");
 
        int i;
        char ch;
 
        for (i = 0; i < hex.length(); i++) 
        {
            ch = hex.charAt(i);
            if (hashMap.containsKey(ch))
                binary += hashMap.get(ch);
            else 
            {
                binary = "Invalid Hexadecimal String";
                return binary;
            }
        }
        return binary;
    }

    static long checkleading(String binString)
    {
        int i=0;
    	long count=0;
    	while(i<binString.length() && binString.charAt(i)=='0')
    	{
    		count++;
    		i++;
    	}
        return count;	
    }

    static boolean verify(String s1,String hp)
    {
        return s1.equals(hp);
    }
    static boolean verify(long s1,long hp)
    {
        return s1==hp;
    }
    
    public static void main(String args[]) throws Exception
    {
        try
        {
            String s1="";
            String initialhash="",pow="",computehash="",finalhash="",calculatedhash="";
            long leadzero=0,givenzero=0;
            FileReader headerfile = new FileReader(args[0]);
            BufferedReader br = new BufferedReader(headerfile);  
            File testfile = new File(args[1]);
            s1=toHexString(get(s1,testfile));
            String line="";
            while((line=br.readLine())!=null)
            {
                String lines[]=line.split(": ",0);
                if(lines[0].equals("Initial-hash"))
                {
                    initialhash=lines[1];
                }
                if(lines[0].equals("Proof-of-work"))
                {
                    pow=lines[1];
                }
                if(lines[0].equals("Hash"))
                {
                    finalhash=lines[1];
                }
                if(lines[0].equals("Leading-bits"))
                {
                    givenzero=Long.parseLong(lines[1]);
                }
            }
            calculatedhash=toHexString(getSHA(pow+initialhash));
            leadzero=checkleading(hexToBinary(calculatedhash));
            if(verify(s1,initialhash))
            {
                if(verify(calculatedhash,finalhash))
                {
                    if(verify(givenzero,leadzero))
                    {
                        System.out.println("Pass");
                    }
                    else
                    {
                        System.out.println("Fail");
                        System.out.println("leading zero doesn't match");
                    }
                }
                else
                {
                    System.out.println("Fail");
                    System.out.println("final hash doesn't match");
                }
            }
            else
            {
                System.out.println("Fail");
                System.out.println("Hash of file doesn't match");
            }
        }
        catch (NoSuchAlgorithmException e)
        {
			System.out.println("Exception thrown for incorrect algorithm: " + e);
		}
        catch (IOException e) 
        {
            System.out.print(e.getMessage());
        }
    }
}