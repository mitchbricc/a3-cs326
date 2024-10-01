
/*****************************************************
   CS 326 - Fall 2024 - Assignment #2
   Full name of student #1: Mitchell Bricco
   Full name of student #2: Maximilian Patterson
   Full name of student #3: Luke Kastern
   (delete the line above this one if your team is a pair)
 *****************************************************/

import java.util.*;

class AES {

    /* AES S-box */
    static private int[][] sBox = {
            /* row 0 */ { 0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30,
                    0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76 },
            /* row 1 */ { 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad,
                    0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0 },
            /* row 2 */ { 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34,
                    0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15 },
            /* row 3 */ { 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07,
                    0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75 },
            /* row 4 */ { 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52,
                    0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84 },
            /* row 5 */ { 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a,
                    0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf },
            /* row 6 */ { 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45,
                    0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8 },
            /* row 7 */ { 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc,
                    0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2 },
            /* row 8 */ { 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4,
                    0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73 },
            /* row 9 */ { 0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46,
                    0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb },
            /* row 10 */ { 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2,
                    0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79 },
            /* row 11 */ { 0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c,
                    0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08 },
            /* row 12 */ { 0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8,
                    0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a },
            /* row 13 */ { 0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61,
                    0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e },
            /* row 14 */ { 0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b,
                    0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf },
            /* row 15 */ { 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41,
                    0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16 }
    };

    /* AES inverse S-box */
    static private int[][] invSBox = {
            /* row 0 */ { 0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38,
                    0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb },
            /* row 1 */ { 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87,
                    0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb },
            /* row 2 */ { 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d,
                    0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e },
            /* row 3 */ { 0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2,
                    0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25 },
            /* row 4 */ { 0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16,
                    0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92 },
            /* row 5 */ { 0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda,
                    0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84 },
            /* row 6 */ { 0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a,
                    0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06 },
            /* row 6 */ { 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02,
                    0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b },
            /* row 8 */ { 0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea,
                    0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73 },
            /* row 9 */ { 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85,
                    0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e },
            /* row 10 */ { 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89,
                    0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b },
            /* row 11 */ { 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20,
                    0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4 },
            /* row 12 */ { 0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31,
                    0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f },
            /* row 13 */ { 0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d,
                    0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef },
            /* row 14 */ { 0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0,
                    0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61 },
            /* row 15 */ { 0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26,
                    0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d }
    };

    /* AES RC array */
    static private int[] RC = {
            0x00, /* not used */
            0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36
    };

    /*
     * Given a 2D array of byte values, send it to the standard output
     * stream using the format shown below. Given the input matrix:
     * { { 1, 2, 3 },
     * { 4, 5, 6 },
     * { 7, 8, 9 },
     * { 10, 11, 255 } }
     *
     * output:
     *
     * 01 02 03
     * 04 05 06
     * 07 08 09
     * 0A 0B FF
     *
     * with each value being printed in hexadecimal with exactly 2
     * digits per value, one space between columns, and no extra space
     * before or after each row. Finally, you must output a single newline
     * character after the last row. Note that the input array may have any
     * number of rows and columns.
     */
    protected static void printMatrix(int[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                System.out.printf("%02x ".toUpperCase(), matrix[row][col]);
            }
            System.out.println();
        }

    }// printMatrix method

    /*
     * Given a string of 32 hexadecimal digits, return a 2D array containing
     * the corresponding 16 decimal byte values in the order implied by the
     * following example. Given the input:
     * "000102030405060708090A0B0C0D0EFF"
     * return the array:
     * { { 0, 4, 8, 12 },
     * { 1, 5, 9, 13 },
     * { 2, 6, 10, 14 },
     * { 3, 7, 11, 255 } }
     * Note that the array is filled column by column, not row by row.
     */
    protected static int[][] hexStringToByteArray(String hex) {
        int[][] out = new int[4][4];
        for (int i = 0; i < hex.length(); i = i + 2) {
            out[(i / 2) % 4][(i / 2) / 4] = Integer.decode("0X" + 
            hex.substring(i, i + 2));
        }

        return out; // here to please the compiler; should be modified
    }// hexStringToByteArray method

    /*
     * Given two byte values (i.e., between 0 and 255, each stored in
     * an int variable), return their sum in GF(256).
     */
    protected static int add(int v1, int v2) {
        return v1 ^ v2 & 0xFF;
    }// add method

    /*
     * Given a byte value, return twice that value in GF(256).
     */
    protected static int times2(int value) {
        boolean shouldXor = value >= 128;
        int shifted = (value << 1) & 0xff;
        if (shouldXor) {
           return shifted ^ 27; // xor 00011011
        }

        return shifted;
    }// times2 method

    /*
     * Given two byte values, return their product in GF(256). We
     * described in class how to code this method correctly using
     * the two previous methods.
     */    
    protected static int times(int v1, int v2)
    {
        BitSet bitset = BitSet.valueOf(new long[]{v1});

        int product = 0;
        for (int i = bitset.length(); i >= 0; i--) {
            product = add(times2(product), bitset.get(i) ? v2 : 0);
        }
        
        return product; 
    }// times method
    
    /* Given a byte value, return the byte value that replaces it
     * according to the AES S-box.
     */
    protected static int forwardSubstituteByte(int byteValue) {
        int row = (byteValue & 0xf0) >>> 4;
        int col = byteValue & 0x0f;
        return sBox[row][col];
    }// forwardSubstituteByte method

    /*
     * Modify the given State array by replacing each one of its byte
     * values according to the AES S-box.
     */
    protected static void forwardSubstituteBytes(int[][] state) {
        for (int row = 0; row < state.length; row++) {
            for (int col = 0; col < state[row].length; col++) {
                state[row][col] = forwardSubstituteByte(state[row][col]);
            }
        }
    }// forwardSubstituteBytes method

    /*
     * Given a byte value, return the byte value that replaces it
     * according to the AES inverse S-box.
     */
    protected static int inverseSubstituteByte(int byteValue) {
        int row = (byteValue & 0xf0) >>> 4;
        int col = byteValue & 0x0f;
        return invSBox[row][col];
    }// inverseSubstituteByte method

    /*
     * Modify the given State array by replacing each one of its byte
     * values according to the AES inverse S-box.
     */
    protected static void inverseSubstituteBytes(int[][] state) {
        for (int row = 0; row < state.length; row++) {
            for (int col = 0; col < state[row].length; col++) {
                state[row][col] = inverseSubstituteByte(state[row][col]);
            }
        }
    }// inverseSubstituteBytes method

    /*
     * Shift each row of the input state according to the Shift Rows
     * transformation.
     */
    protected static void shiftRows(int[][] state) {
        for (int row = 1; row < state.length; row++) {
            int[] temp = new int[state[row].length];
            for (int col = 0; col < state[row].length; col++) {
                temp[(col + 4 - row) % 4] = state[row][col];
            }
            state[row] = temp;
        }
    }// shiftRows method

    /*
     * Shift each row of the input state according to the Inverse Shift Rows
     * transformation.
     */
    protected static void inverseShiftRows(int[][] state) {
        for (int row = 1; row < state.length; row++) {
            int[] temp = new int[state[row].length];
            for (int col = 0; col < state[row].length; col++) {
                temp[(col + row) % 4] = state[row][col];
            }
            state[row] = temp;
        }
    }// inverseShiftRows method

    /*
     * Update the input state according to the Mix Columns transformation.
     */
    protected static void mixColumns(int[][] state)
    {
        for (int col = 0; col < 4; col++) {
            int[] temp = new int[4];
            int c0 = state[0][col]; // 1
            int c1 = state[1][col]; // 2
            int c2 = state[2][col]; // 3
            int c3 = state[3][col]; // 4
    
            temp[0] = add(add(add(times2(c0), (times(3,c1))), c2), c3) & 0xFF;
            temp[1] = add(add(add(times2(c1), (times(3,c2))), c0), c3) & 0xFF;
            temp[2] = add(add(add(times2(c2), (times(3,c3))), c0), c1) & 0xFF;
            temp[3] = add(add(add(times2(c3), (times(3,c0))), c1), c2) & 0xFF;
    
            // Copy the transformed column back to the state matrix
            for (int row = 0; row < 4; row++) {
                state[row][col] = temp[row];
            }
        }
    }// mixColumns method

    /*
     * Update the input state according to the Inverse Mix Columns
     * transformation.
     */
    protected static void inverseMixColumns(int[][] state) {
        for (int col = 0; col < 4; col++) {
            int[] temp = new int[4];
            int c0 = state[0][col];
            int c1 = state[1][col];
            int c2 = state[2][col];
            int c3 = state[3][col];
    
            temp[0] = add(add(add(times(14, c0), times(11, c1)),
                times(13, c2)), times(9, c3)) & 0xFF;
            temp[1] = add(add(add(times(9, c0), times(14, c1)), 
                times(11, c2)), times(13, c3)) & 0xFF;
            temp[2] = add(add(add(times(13, c0), times(9, c1)), 
                times(14, c2)), times(11, c3)) & 0xFF;
            temp[3] = add(add(add(times(11, c0), times(13, c1)), 
                times(9, c2)), times(14, c3)) & 0xFF;
    
            for (int row = 0; row < 4; row++) {
                state[row][col] = temp[row];
            }
        }
    }// inverseMixColumns method

    /*
     * Given a state array, the array of 44 one-word round key values, and
     * the number of the current round (between 1 and 10), update the state
     * according to the Add Round Key transformation for the given round.
     */
    protected static void addRoundKey(int[][] state, int[] w, int round) {
        byte[] temp = new byte[w.length * 4];
        for (int i = (round-1) * 4; i < (round-1) * 4 + 4; i++) {
            temp[i * 4] = (byte) ((w[i] >> 24)&0xff);
            temp[i * 4 + 1] = (byte) ((w[i] >> 16)&0xff);
            temp[i * 4 + 2] = (byte) ((w[i] >> 8)&0xff);
            temp[i * 4 + 3] = (byte) (w[i]&0xff);
        }
        for (int i = (round-1) * 16; i < (round-1) * 16 + 16; i++) {
            state[(i%16) % 4][(i%16) / 4] ^= temp[i] &0xff;
        }
    }// addRoundKey method

    /*
     * Given a state array, the array of 44 one-word round key values, and
     * the number of the current round (between 1 and 10), update the state
     * according to the Inverse Add Round Key transformation for the given
     * round.
     */
    protected static void inverseAddRoundKey(int[][] state, int[] w, 
    int round) {
        addRoundKey(state, w, 12-round);
    }// inverseAddRoundKey method

    /*
     * Given a 4-byte value, return the 4-byte value obtained by cyclically
     * shifting the input by one byte to the left. For example:
     * rotWord( 0x01020304 ) returns 0x02030401
     */
    protected static int rotWord(int word) {
        int byte1 = (word >>> 24) & 0xFF;
        int byte2 = (word >>> 16) & 0xFF;
        int byte3 = (word >>> 8) & 0xFF;
        int byte4 = word & 0xFF;
        
        int rotatedWord = (byte2 << 24) | (byte3 << 16) | (byte4 << 8) | byte1;
        return rotatedWord;
    }// rotWord method

    /*
     * Given an integer j between 1 and 10 (inclusive), return the
     * 4-byte value obtained by concatenating three 0-valued bytes to
     * the right of the RC value at index j.
     */
    protected static int rCon(int j) {
        return RC[j] << 24;
    }// rCon method

    /*
     * Given a 4x4 2D array of byte values (i.e., the AES key arranged
     * in columns): { { k0, k4, k8, k12 },
     * { k1, k5, k9, k13 },
     * { k2, k6, k10, k14 },
     * { k3, k7, k11, k15 } }
     * return a 44-word array according to the Key Expansion algorithm.
     */
    protected static int[] expandKey(int[][] key) {
        int[] wordArray = new int[44];
        
        for (int i = 0; i < 4; i++) {
            wordArray[i] = (key[0][i] << 24) | (key[1][i] << 16) | 
                (key[2][i] << 8) | key[3][i];
        }
        
        for (int i = 4; i < 44; i++) {
            int temp = wordArray[i - 1];
            if (i % 4 == 0) {
                temp = rotWord(temp);
                int[] tempBytes = new int[4];
                for (int j = 0; j < 4; j++) {
                    tempBytes[j]= forwardSubstituteByte((temp>>>8*j)&0xff);
                }
                temp = (tempBytes[3] << 24) | (tempBytes[2] << 16) | 
                    (tempBytes[1] << 8) | tempBytes[0];
                temp ^= rCon(i / 4);
            }
            wordArray[i] = wordArray[i - 4] ^ temp;
        }
        return wordArray;
    }// expandKey method

    /*
     * Given a 16-byte hexadecimal number (the plaintext block) represented as
     * a String (e.g., "000102030405060708090A0B0C0D0E0F"), and a 16-byte
     * hexadecimal number (the AES key) with the same representation, return
     * the corresponding ciphertext block stored column by column in a 4x4
     * array of byte values.
     */
    protected static int[][] encrypt(String block, String keyStr) {
        int[][] m = hexStringToByteArray(block);
        int[] w = expandKey(hexStringToByteArray(keyStr));

        addRoundKey(m, w, 1);
        for (int i = 2; i < 11; i++) {
            forwardSubstituteBytes(m);
            shiftRows(m);
            mixColumns(m);
            addRoundKey(m, w, i);
        }
        forwardSubstituteBytes(m);
        shiftRows(m);
        addRoundKey(m, w, 11);

        return m; 
    }// encrypt method

    /*
     * Given a 16-byte hexadecimal number (the ciphertext block) represented as
     * a String (e.g., "000102030405060708090A0B0C0D0E0F"), and a 16-byte
     * hexadecimal number (the AES key) with the same representation, return
     * the corresponding plaintext block stored column by column in a 4x4
     * array of byte values.
     */
    protected static int[][] decrypt(String block, String keyStr) {
        int[][] m = hexStringToByteArray(block);
        int[] w = expandKey(hexStringToByteArray(keyStr));
        
        inverseAddRoundKey(m, w, 1);
        for (int i = 2; i < 11; i++) {
            inverseShiftRows(m);
            inverseSubstituteBytes(m);
            inverseAddRoundKey(m, w, i);
            inverseMixColumns(m);
        }
        inverseShiftRows(m);
        inverseSubstituteBytes(m);
        inverseAddRoundKey(m, w, 11);

        return m; 
    }// decrypt method
    
}// AES class
