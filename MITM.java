/*****************************************************
   CS 326 - Fall 2024 - Assignment #3
   Full name of student #1: Mitchell Bricco
   Full name of student #2: Luke kastern
   Full name of student #3: Maximilian Patterson
   (delete the line above this one if your team is a pair)
 *****************************************************/

import java.util.*;

class MITM extends BruteForce
{

    /* Given two plaintext-ciphertext pairs and the number numBits of
     * significant bits in each key, perform a MITM attack of
     * DoubleAES (refer to your class notes about the algorithmic
     * details of this attack) and output the key pair that produces
     * the two given ciphertexts given the respective plaintexts. This
     * method also records the time in seconds to find the key pair.
     * This method sends to the standard output stream some output
     * whose format is specified in the handout.
    */
    static void meetInTheMiddle(String plaintext1, String ciphertext1, 
                                String plaintext2, String ciphertext2,
                                int numBits)
    {
        long startTime, elapsedTime;
        startTime = System.currentTimeMillis();

        int maxKey = (int) Math.pow(2, numBits);

        // Precompute p1`
        String[] pPrimes1 = new String[maxKey];
        String[] pPrimes2 = new String[maxKey];
        for (int pKey = 0; pKey < maxKey; pKey++) {
            String keyHex = String.format("%32s", 
                binStringToHex(Integer.toBinaryString(pKey)))
                    .replaceAll(" ", "0");
            int[][] pPrime1 = AES.encrypt(plaintext1, keyHex);
            pPrimes1[pKey] = stateToString(pPrime1);

            int[][] pPrime2 = AES.encrypt(plaintext2, keyHex);
            pPrimes2[pKey] = stateToString(pPrime2);
        }

        HashMap<String,Integer> cPrimeMap1 = new HashMap<>();
        HashMap<String,Integer> cPrimeMap2 = new HashMap<>();
        for (int decryptKey = 0; decryptKey < maxKey; decryptKey++) {
            String keyHex = String.format("%32s", 
                binStringToHex(Integer.toBinaryString(decryptKey)))
                    .replaceAll(" ", "0");
            
            String cPrime1 = stateToString(AES.decrypt(ciphertext1, keyHex));
            cPrimeMap1.put(cPrime1, decryptKey);

            String cPrime2 = stateToString(AES.decrypt(ciphertext2, keyHex));
            cPrimeMap2.put(cPrime2, decryptKey);
        }

        int key1e = -1, key2e = -1;
        int key1d = -1, key2d = -1;
        for (int i = 0; i < pPrimes1.length; i++) {
            if(cPrimeMap1.containsKey(pPrimes1[i])){
                key1e = i;
                key1d = cPrimeMap1.get(pPrimes1[i]);
            }
        }

        for (int i = 0; i < pPrimes2.length; i++) {
            if(cPrimeMap2.containsKey(pPrimes2[i])){
                key2e = i;
                key2d = cPrimeMap2.get(pPrimes2[i]);
            }
        }
        if(key1e != -1 && key2e != -1 && key1e == key2e && key1d == key2d){
            String key1Hex = String.format("%32s", 
                binStringToHex(Integer.toBinaryString(key1e)))
                    .replaceAll(" ", "0");
            String key2Hex = String.format("%32s", 
                binStringToHex(Integer.toBinaryString(key2d)))
                    .replaceAll(" ", "0");
            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.printf("%13s", "");
            System.out.println(key1Hex + " " + key2Hex + " " + 
                " time = " + (elapsedTime/1000.0) + "s");
            return;
        }
    }// meetInTheMiddle method


    
    /* This code is used for testing purposes.  This driver code
     * invokes the MIMT method repeatedly with numBits equal to 2,
     * then to 3, then to 4, etc. You will have to terminate this
     * program (e.g.,with a CTRL-C) as soon as the TOTAL runtime of
     * the last meet-in-the-middle search has exceeded 60 seconds.
     *
     * Do NOT modify this method.
     */
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("Usage: java MITM [test1 or test2] " +
                               "<P1> <P2>");
            System.exit(1);
        }
        String testCase = args[0];
        String P1 = args[1];
        String P2 = args[2];    
        String key1 = "", key2 = "";
        int numBits;
        String[] keys = null;
        for(numBits = 2; true; numBits++)
        {
            // generate a key pair
            if (testCase.equals("test1"))
            {      
                keys = getKeyPair(1,numBits);
            } else if (testCase.equals("test2"))
            {
                keys = getKeyPair(2,numBits);
            } else
            {
                System.out.println("This test case is not implemented.");
                System.exit(1);
            }
            key1 = keys[0];
            key2 = keys[1];
            // encrypt the plaintexts
            String C1 = stateToString(encryptDAES(key1,key2,P1));
            String C2 = stateToString(encryptDAES(key1,key2,P2));
            meetInTheMiddle(P1,C1,P2,C2,numBits);            
        }// loop on numBits        
    }// main method
}// MITM class
