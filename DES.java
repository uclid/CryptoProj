/**
* The main class to implement the DES encryption algorithm
* showing every step and round in encrypting and decrypting
* the data. The size of the key here is 64-bit and it works
* on 64-bit data generating separate keys for 16 rounds.
* @author dixit bhatta
*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class DES {
    // initial permutation (split into left/right halves )
    // since DES numbers bits starting at 1, we will ignore x[0]
    public static int[] IP_perm = new int[]{ -1,
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7 };

    // final permutation (inverse initial permutation)
    public static int[] FP_perm = new int[]{ -1,
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25 };

    // per-round expansion
    public static int[] E_perm = new int[]{ -1,
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1 };

    // per-round permutation
    public static int[] P_perm = new int[]{ -1,
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25 };

    //8 Substitution boxes
    // note we do use element 0 in the S-Boxes
    public static int[][] S1 = new int[][]{
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13} };
    public static int[][] S2 = new int[][]{
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9} };
    public static int[][] S3 = new int[][]{
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12} };
    public static int[][] S4 = new int[][]{
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14} };
    public static int[][] S5 = new int[][]{
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3} };
    public static int[][] S6 = new int[][]{
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13} };
    public static int[][] S7 = new int[][]{
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12} };
    public static int[][] S8 = new int[][]{
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11} };

    // first, key, permutation
    public static int[] PC_1_perm = new int[]{ -1,
            // C subkey bits
            57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
            // D subkey bits
            63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

    // per-round, key, selection, permutation
    public static int[] PC_2_perm = new int[]{ -1,
            14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

    public static String input, output, key, permKey, IPerm; //input, output, the key, permuted key and 

permuted input
    public static String c,d; //c and d key blocks
    public static String l,r; //l and r data blocks
    public static String[] roundkeys = new String[17]; //roungkeys

    public static void main (String[] args){

        //initializing the GUI interface to begin the DES program
        initGUI();
    }

    private static void initGUI() {
        //initializing the GUI interface
        final desGUI inputForm = new desGUI();
        //show the form
        inputForm.frm.setVisible(true);

        //implementing the Encrypt Button Click
        inputForm.Encrypt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                takeInput(inputForm); //take the input value and key
                if(key!=null)
                    encrypt(inputForm);
            }
        });

        //implementing the Decrypt Button Click
        inputForm.Decrypt.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                takeInput(inputForm); //take the input value and key
                if(key!=null)
                    decrypt(inputForm);
            }
        });
    }

    private static void takeInput(desGUI inputForm) {
        boolean isInputHex = inputForm.tInput.getText().matches("[0-9A-F]+"); //checks if the input is 

hexadecimal or not
        boolean isKeyHex = inputForm.tKey.getText().matches("[0-9A-F]+"); //checks if the input is 

hexadecimal or not
        //use strictly 16 digit hex input and hex key
        if(inputForm.tInput.getText().length() != 16 || inputForm.tKey.getText().length() != 16){
            JOptionPane.showMessageDialog(null, "Input and Key must be exactly 16 digits", "Error", 

JOptionPane.WARNING_MESSAGE);
        }
        //use hexadecimal input and key only
        else if(!isInputHex || !isKeyHex){
            JOptionPane.showMessageDialog(null, "Use hex values only (Capital letters)", "Error", 

JOptionPane.WARNING_MESSAGE);
        }
        //use valid input
        else{
            input = inputForm.tInput.getText();
            key = inputForm.tKey.getText();
            input = toBinary(input); //change the input into binary form
            key = toBinary(key); //change the input into binary form
        }
    }

    //for changing hex values to binary
    private static String toBinary(String value) {
        char[] valChars = value.toCharArray();
        value = ""; //reset
        //binary values for each hex character
        for(int i =0; i< valChars.length; i++){
            if(valChars[i] == '0'){
                value = value + "0000";
            }
            else if(valChars[i] == '1'){
                value = value + "0001";
            }
            else if(valChars[i] == '2'){
                value = value + "0010";
            }
            else if(valChars[i] == '3'){
                value = value + "0011";
            }
            else if(valChars[i] == '4'){
                value = value + "0100";
            }
            else if(valChars[i] == '5'){
                value = value + "0101";
            }
            else if(valChars[i] == '6'){
                value = value + "0110";
            }
            else if(valChars[i] == '7'){
                value = value + "0111";
            }
            else if(valChars[i] == '8'){
                value = value + "1000";
            }
            else if(valChars[i] == '9'){
                value = value + "1001";
            }
            else if(valChars[i] == 'A'){
                value = value + "1010";
            }
            else if(valChars[i] == 'B'){
                value = value + "1011";
            }
            else if(valChars[i] == 'C'){
                value = value + "1100";
            }
            else if(valChars[i] == 'D'){
                value = value + "1101";
            }
            else if(valChars[i] == 'E'){
                value = value + "1110";
            }
            else{
                value = value + "1111";
            }

        }
        return value;
    }

    private static void setKeys(desGUI inputForm) {
        int i;

        permKey = "";
        permKey = permute(key, PC_1_perm);
        c = permKey.substring(0,28);
        d = permKey.substring(28);
        inputForm.tOutput.append("K+: " + permKey + "\n");
        inputForm.tOutput.append("\nC0: " + c + "\n");
        inputForm.tOutput.append("D0: " + d + "\n");

        //key generation rounds
        for(i = 1; i <= 16; i++){
            if(i ==1 || i==2 || i==9 || i==16){ //double shift except these rounds
                c = c.substring(1)+ c.substring(0,1);
                d = d.substring(1)+ d.substring(0,1);
            }
            else{
                c = c.substring(2)+ c.substring(0,2);
                d = d.substring(2)+ d.substring(0,2);
            }
            inputForm.tOutput.append("C"+ i + ": " + c + "\n");
            inputForm.tOutput.append("D"+ i + ": " + d + "\n");
            roundkeys[i] = permute(c+d,PC_2_perm);
            inputForm.tOutput.append("K"+ i + ": " + roundkeys[i] + "\n");
        }

    }

    //work on encrypting the data
    private static void setData(desGUI inputForm) {
        int i=1;
        String templ,tempr;
        IPerm = "";
        IPerm = permute(input, IP_perm);
        l = IPerm.substring(0, 32);
        r = IPerm.substring(32);
        inputForm.tOutput.append("\nL0: " + l + "\n");
        inputForm.tOutput.append("R0: " + r + "\n");

        //encryption rounds
       for(i = 1; i <= 16; i++){
            templ = l; //templ is ln-1
            tempr = r; //tempr is rn-1
            l = tempr;

            //functions start for encryption of right block
            //first expand r
            r = expand(r, E_perm);
            inputForm.tOutput.append("E(R"+ i + "): " + r + "\n");
            //perform xor operation with round key
            r = xor(r, roundkeys[i]);
            inputForm.tOutput.append("K"+ i +"-XOR-E(R"+ i + "): " + r + "\n");
            //apply substitution boxes
            r = substitute(r);
            inputForm.tOutput.append("S-boxes: " + r + "\n");
            //apply round permutation box
            r = permute(r,P_perm );
            inputForm.tOutput.append("P-box: " + r + "\n");
            //xor left block with the right block
            r = xor32(r, l);
            inputForm.tOutput.append("(L"+ (i-1) +"-XOR-R"+ (i-1) + "): " + r + "\n");
            //end of the function
            inputForm.tOutput.append("L"+ i + ": " + l + "\n");
            inputForm.tOutput.append("R"+ i + ": " + r + "\n\n");
       }
        inputForm.tOutput.append("Reverse: " + r+l + "\n\n");
        output = permute(r+l, FP_perm);
        inputForm.tOutput.append("Final Permutation: "+ output + "\n\n");
        String hex = 

Long.toHexString(Long.parseLong(output.substring(0,32),2))+Long.toHexString(Long.parseLong(o

utput.substring(32),2));
        inputForm.tOutput.append("Output(hex): "+ hex.toUpperCase() + "\n\n");
        inputForm.tOput.setText(hex.toUpperCase());

    }

    private static String substitute(String r) {
        String a="";
        int b1,b2,b3,b4,b5,b6,b7,b8;
        int b11,b21,b31,b41,b51,b61,b71,b81;
        b1 = Integer.parseInt(r.substring(1,5),2); b11 = 

Integer.parseInt((r.substring(0,1)+r.substring(5,6)),2);
        b2 = Integer.parseInt(r.substring(7,11),2); b21 = 

Integer.parseInt((r.substring(6,7)+r.substring(11,12)),2);
        b3 = Integer.parseInt(r.substring(13,17),2); b31 = 

Integer.parseInt((r.substring(12,13)+r.substring(17,18)),2);
        b4 = Integer.parseInt(r.substring(19,23),2); b41 = 

Integer.parseInt((r.substring(18,19)+r.substring(23,24)),2);
        b5 = Integer.parseInt(r.substring(25,29),2); b51= 

Integer.parseInt((r.substring(24,25)+r.substring(29,30)),2);
        b6 = Integer.parseInt(r.substring(31,35),2); b61= 

Integer.parseInt((r.substring(32,33)+r.substring(35,36)),2);
        b7 = Integer.parseInt(r.substring(37,41),2); b71= 

Integer.parseInt((r.substring(36,37)+r.substring(41,42)),2);
        b8 = Integer.parseInt(r.substring(43,47),2); b81 = 

Integer.parseInt((r.substring(42,43)+r.substring(47)),2);

        r=""; //reset

        a = Integer.toBinaryString(S1[b11][b1]);
        a = pad(a);
        r = r+a;

        a = Integer.toBinaryString(S2[b21][b2]);
        a = pad(a);
        r = r+a;

        a = Integer.toBinaryString(S3[b31][b3]);
        a = pad(a);
        r = r+a;

        a = Integer.toBinaryString(S4[b41][b4]);
        a = pad(a);
        r = r+a;

        a = Integer.toBinaryString(S5[b51][b5]);
        a = pad(a);
        r = r+a;

        a = Integer.toBinaryString(S6[b61][b6]);
        a = pad(a);
        r = r+a;

        a = Integer.toBinaryString(S7[b71][b7]);
        a = pad(a);
        r = r+a;

        a = Integer.toBinaryString(S8[b81][b8]);
        a = pad(a);
        r = r+a;

        return r;

    }

    //for making binary digits of s-box output of exactly 4 bits
    private static String pad(String a) {
        while(a.length()!= 4){
                a = "0" + a;
        };
        return a;
    }

    //for xor-ing two data
    private static String xor(String a, String b) {
        BigInteger b1,b2;
        b1 = new BigInteger(a, 2);
        b2 = new BigInteger(b, 2);
        b2 = b2.xor(b1);
        a = b2.toString(2);
        //adjust the binary string to correct 48 bit length
        while(a.length()!= 48){
            a = "0" + a;
        };
        return a;
    }

    //for xor-ing end values
    private static String xor32(String a, String b) {
        BigInteger b1,b2;
        b1 = new BigInteger(a, 2);
        b2 = new BigInteger(b, 2);
        b2 = b2.xor(b1);
        a = b2.toString(2);
        //adjust the binary string to correct 32 bit length
        while(a.length()!= 32){
            a = "0" + a;
        };
        return a;
    }

    //expansion
    private static String expand(String r, int[] e_perm) {
        int i, fromloc;
        String dest = "";

        for( i=1; i < e_perm.length; i++ )
        {
            fromloc = e_perm[i];
            dest = dest + r.charAt(fromloc-1);
        }
        return dest;
    }

    //performs permutation
    private static String permute(String source, int[] perm) {
        int i, fromloc;
        String dest = "";

        for( i=1; i < perm.length; i++ )
        {
            fromloc = perm[i];
            dest = dest + source.charAt(fromloc-1);
        }
        return dest;
    }

    private static void encrypt(desGUI inputForm) {
        inputForm.tOutput.setText("");
        inputForm.tOutput.append("Encrypting\n");
        inputForm.tOutput.append("Input: " + input+ "\n");
        inputForm.tOutput.append("Key: " + key+ "\n");
        setKeys(inputForm);
        setData(inputForm);

    }




    private static void decrypt(desGUI inputForm) {
        inputForm.tOutput.setText("");
        inputForm.tOutput.append("Decrypting\n");
        inputForm.tOutput.append("Input: " + input+ "\n");
        inputForm.tOutput.append("Key: " + key+ "\n");
        setKeys(inputForm);
    }

}
