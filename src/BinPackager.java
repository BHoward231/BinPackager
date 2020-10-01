import edu.princeton.cs.algs4.BST;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BinPackager {
    public static BST<Integer, Bin> binSorted = new BST<>();
    public static BufferedReader inputReader;
    public static int maxBinCapacity;

    public static void main(String[] args) {
        boolean hasInput = false;
        //takes file from command line to argument
        while(!hasInput) {
            String max = args[0];
            maxBinCapacity = Integer.parseInt(max);
            try {
                inputReader = new BufferedReader(new FileReader("inputFile.txt"));
                hasInput = true;
            } catch (FileNotFoundException f) {
                System.out.println("File was not found");
            }
        }

        worstFitDecreasing();
    }


    public static void firstFit()  {
        inputReader.lines().forEach(l -> {
            int value = Integer.parseInt(l);
            if(isValid(value)) {
                boolean isAdded = false;
                Iterator<Integer> itr = binSorted.keys().iterator();
                while(itr.hasNext() && !isAdded) {
                    Bin next = binSorted.get(itr.next());
                    if(next.isValid(value)) {
                        next.addToSize(value);
                        isAdded = true;
                    }
                }

                if(!isAdded) {
                    binSorted.put(binSorted.size(), Bin.of(value));
                }
            } else {
                System.out.println(value + " Was not added as it was out of bounds");
            }
        });
    }

    public static void bestFitDecreasing() {
        inputReader.lines().forEach(l -> {
            int value = Integer.parseInt(l);
            Optional<Integer> bestBin = bestFit(value);
            if(bestBin.isPresent()) {
                if(bestBin.get() != 0) {
                    binSorted.get(bestBin.get()).addToSize(value);
                } else {
                    binSorted.put(binSorted.size(), Bin.of(value));
                }
            } else {
                System.out.println(value + " Was not added as it was out of bounds");
            }
        });
    }

    public static Optional<Integer> bestFit(int itemSize) {
        if(!isValid(itemSize)) {
            return Optional.empty();
        }

        AtomicInteger currentBest = new AtomicInteger(0);
        binSorted.keys().forEach(k -> {
            Bin tmp = binSorted.get(k);
            if(tmp.isValid(itemSize)) {
                if (currentBest.get() == 0) {
                    currentBest.set(k);
                } else {
                    if(tmp.getSizeAfter(itemSize) > binSorted.get(currentBest.get()).getSizeAfter(itemSize)) {
                        currentBest.set(k);
                    }
                }
            }
        });

        return Optional.of(currentBest.get());
    }

    public static void worstFitDecreasing() {
        inputReader.lines().forEach(l -> {
            int value = Integer.parseInt(l);
            Optional<Integer> worstBin = worstFit(value);
            if(worstBin.isPresent()) {
                if(worstBin.get() != 0) {
                    binSorted.get(worstBin.get()).addToSize(value);
                } else {
                    binSorted.put(binSorted.size(), Bin.of(value));
                }
            } else {
                System.out.println(value + " Was not added as it was out of bounds");
            }
        });
    }

    public static Optional<Integer> worstFit(int itemSize) {
        if(!isValid(itemSize)) {
            return Optional.empty();
        }

        AtomicInteger currentBest = new AtomicInteger(0);
        binSorted.keys().forEach(k -> {
            Bin tmp = binSorted.get(k);
            if(tmp.isValid(itemSize)) {
                if (currentBest.get() == 0) {
                    currentBest.set(k);
                } else {
                    if(tmp.getSizeAfter(itemSize) < binSorted.get(currentBest.get()).getSizeAfter(itemSize)) {
                        currentBest.set(k);
                    }
                }
            }
        });

        return Optional.of(currentBest.get());
    }

    public static boolean isValid(int value) {
        return value <= maxBinCapacity;
    }

    static class Bin {
        private int size;

        public Bin(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public void addToSize(int value) {
            size += value;
        }

        public int getSizeAfter(int value) {
            return value + size;
        }

        public boolean isValid(int value) {
            return value + size <= maxBinCapacity;
        }

        public static Bin of(int value) {
            return new Bin(value);
        }
    }
}
