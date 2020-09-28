import edu.princeton.cs.algs4.BST;

import java.io.*;

public class BinPackager {
    public static final int MAX_BIN_CAPACITY = 50;

    public static void main(String[] args) {
        BufferedReader file = null;
        boolean hasInput = false;
        //takes file from command line to argument
        while(!hasInput) {
            try {
                file = new BufferedReader(new FileReader("inputFile.txt"));
                hasInput = true;
            } catch (FileNotFoundException f) {
                System.out.println("File was not found");

            }
        }

        BST<Integer, Bin> bst = firstFit(file);
    }

    static class Bin {
        private int value;
        private int maxCapacity = MAX_BIN_CAPACITY;

        public Bin() {
            value = 0;
        }

        public Bin(int initial) {
            add(initial);
        }

        public boolean add(int input) {
            if(input + value <= maxCapacity) {
                value += input;
                return true;
            } else {
                return false;
            }
        }
    }

    public static BST<Integer, Bin> firstFit(BufferedReader inputText) {
        BST<Integer, Bin> output = new BST<>();
        int lastWorkingBinKey =  -1;
        inputText.lines().forEach(l -> {
            int value = Integer.parseInt(l);
            boolean wasAdded = false;
            for(Integer i : output.keys()) {
                if(output.get(i).add(value)) {
                    wasAdded = true;
                    break;
                }
            }

            if(!wasAdded) {
                Bin tmp = new Bin(value);
                output.put(tmp.hashCode(), tmp);
            }
        });

        return output;
    }
}
