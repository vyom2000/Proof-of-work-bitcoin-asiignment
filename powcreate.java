import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;

class powcreate 
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
	static String hl="",pt;
	static String y="";
    
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
 
	static void printAllKLength(char[] set, int k,long p)
	{
    	int n = set.length;
    	printAllKLengthRec(set, "", n, k,p);
	}
	
	static boolean flag=false;
	static long iter=0;
	static long max=0;
	static void printAllKLengthRec(char[] set,String prefix,int n, int k,long p)
	{
    	try
    	{ 
			if(flag==true)
    		return;
    		if (k == 0 && flag!=true)
    		{
        		iter++;
        		long ty=checkleading(hexToBinary(toHexString(getSHA(prefix+s1))));
				if(ty>=p)
				{
             		max=ty;
					y=prefix;
					flag=true;
        		}
	    		return;
    		}
    		for (int i = 0; i < n; ++i)
    		{
        		String newPrefix = prefix + set[i];
        		printAllKLengthRec(set, newPrefix,n, k - 1,p); 
    		}
		}
		catch (NoSuchAlgorithmException e) 
		{
			System.out.println("Exception thrown for incorrect algorithm: " + e);
		}
	}
	static String s1="";
	
	public static void main(String args[]) throws Exception
	{
		try
		{
			char arr[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a', 'b', 'c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','!','#','$','%','&','(',')','*','+',',','-','.','/',':',';','<','=','>','?','@','[','\\',']','^','_','`','{','|','}','~'};
	 		long p=Long.parseLong(args[0]);
            File file = new File(args[1]);
            s1=toHexString(get(s1,file));
            System.out.println("File: "+args[1]);
            System.out.println("Initial-hash: " + toHexString(get(s1,file)));
			int k=1;
            double start = System.currentTimeMillis();
			while(flag!=true)
			{
				printAllKLength(arr,k,p);
				k++;
			}
            double stop = System.currentTimeMillis();
			System.out.println("Proof-of-work: "+y);
           	System.out.println("Final Hash: "+toHexString(getSHA(y+s1)));
            System.out.println("Leading-bits: "+max);
            System.out.println("Iterations: "+iter);
            double time=(stop-start)/1000;
            System.out.println("Compute-time: "+time+ " seconds");
        }
		catch (NoSuchAlgorithmException e) 
		{
			System.out.println("Exception thrown for incorrect algorithm: " + e);
		}
	}
}
