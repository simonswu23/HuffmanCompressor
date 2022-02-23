//Creates Class HuffmanCode that can compress text 
//files into smaller files and can decompress
//standard pre-formatted files into larger files

import java.util.*;
import java.io.*;

public class HuffmanCode {

    private HuffmanNode overallRoot;
    
    //Constructs a code based on the Huffman Encoding Scheme from
    //an array 'int[] frequencies,' where the index of the array
    //corresponds to the respective ascii value of the char
    //and the value of the index corresponds to how frequently it 
    //appears, and initializes it to the current console
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> freqQ = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) {
            int freq = frequencies[i];
            if (freq != 0) {
                HuffmanNode current = new HuffmanNode(i, freq);
                freqQ.add(current);
            }
        }
        buildHelper(freqQ);
    }
    
    //Builds a Huffman code by managing a priority 'Queue<HuffmanNode> freqQ'
    //of the frequencies of the chars represented by their
    //ASCII values. Sets the final HuffmanNode in the Q
    //to be overall root when finished.
    private void buildHelper(Queue<HuffmanNode> freqQ) {
        if (freqQ.size() == 1) {
            overallRoot = freqQ.peek();
        } else {
            HuffmanNode zero = freqQ.remove();
            HuffmanNode one = freqQ.remove();
            freqQ.add(new HuffmanNode(0, zero.freq + one.freq, zero, one));
            buildHelper(freqQ);
        }
    }
    
    //Reads in a previously constructed Huffman code from
    //a standard formatted file using 'Scanner input'
    //and intializes it to the current console
    public HuffmanCode(Scanner input) {
        while(input.hasNext()) {
            int ascii = input.nextInt();
            String path = input.next();
            overallRoot = huffmanHelper(ascii, path, overallRoot);
        }
    }
    
    //Recursively builds a HuffmanNode tree based on 'String path'
    //by traversing through 'HuffmanNode' root. Once the path reaches
    //the end, creates and returns a HuffmanNode with 'int ascii' ascii value
    private HuffmanNode huffmanHelper(int ascii, String path, HuffmanNode root) {
        if (path.length() == 0) {
            HuffmanNode leaf = new HuffmanNode(ascii);
            return leaf;
        } else {
            char direction = path.charAt(0);
            path = path.substring(1);
            if (root == null) {
                root = new HuffmanNode(0);
            }
            if (direction == '0') {
                root.zero = huffmanHelper(ascii, path, root.zero);
            } else {
                root.one = huffmanHelper(ascii, path, root.one);
            }
            return root;
        } 
    }
    

    //Sends the current Huffman code to 'PrintStream output'
    //formatted in standard format and creates a file
    public void save(PrintStream output) {
        save(output, overallRoot, new String());
    }
    
    //Recursively generates a 'String code' code by
    //traversing down 'HuffmanNode current,' and finally
    //printing the char codes to 'PrintStream output'
    private void save(PrintStream output, HuffmanNode current, String code) {
        if (current.zero == null && current.one == null) {
            output.println(current.ascii);
            output.println(code);
        } else {
            save(output, current.zero, code + "0");
            save(output, current.one, code + "1");    
        }
    }
    
    //Converts a legally encoded 'BitInputStream input' to 
    //text using the current Huffman code, which then gets 
    //sent to 'PrintStream output' file
    public void translate(BitInputStream input, PrintStream output) {
        while (input.hasNextBit()) {
            translateHelper(input, output, overallRoot);
        }
    }
    
    //Recursively traverses 'HuffmanNode root' based on 
    //'BitInputStream input' until reaching a leafNode,
    //then writes ascii value to 'PrintStream output'
    private void translateHelper(BitInputStream input, PrintStream output, HuffmanNode root) {
        if (root.zero == null && root.one == null) {
            output.write(root.ascii);
        } else if (input.nextBit() == 0) {
            translateHelper(input, output, root.zero);
        } else {
            translateHelper(input, output, root.one);
        }
    }
    
    //Creates class HuffmanNode that stores the 
    //ascii values and frequencies of a Huffman Code
    private static class HuffmanNode implements Comparable<HuffmanNode> {
        public HuffmanNode zero;
        public HuffmanNode one;
        public int ascii;
        public int freq;
   
        //Creates a HuffmanNode that represents 'int ascii'
        //ascii value and its frequency
        public HuffmanNode(int ascii, int frequency) {
            this(ascii, frequency, null, null);
        }
        
        //Creates a HuffmanNode that reprseents
        //'int ascii' ascii value
        public HuffmanNode(int ascii) {
            this(ascii, 0, null, null);
        }
        
        //Creates a HuffmanNode that represents 
        //and its 'int ascii', 'int frequency', 
        //and 'HuffmanNode zero' and 'HuffmanNode one'
        public HuffmanNode(int ascii, int frequency, HuffmanNode zero, HuffmanNode one) {
            this.ascii = ascii;
            this.freq = frequency;
            this.zero = zero;
            this.one = one;
        }
        
        //compares this HuffmanNode to 'HuffmanNode other'
        //returns a negative, 0, or positive int depending on
        //whether this HuffmanNode's frequency is 
        //less, equal to, or greater than other HuffmanNode
        public int compareTo(HuffmanNode other) {
            return this.freq - other.freq;
        }
    }
}
