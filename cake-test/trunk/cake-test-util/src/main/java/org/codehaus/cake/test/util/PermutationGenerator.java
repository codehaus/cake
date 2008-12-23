package org.codehaus.cake.test.util;


/**
 * Taken from http://www.merriampark.com/perm.htm
 */
public class PermutationGenerator {

    private Integer[] a;
    private long numLeft;
    private final long total;

    public PermutationGenerator(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be a positive number");
        }
        a = new Integer[n];
        long fact = 1;
        for (int i = n; i > 1; i--) {
            fact *= i;
        }
        total = fact;
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        numLeft = total;
    }

    public long getNumLeft() {
        return numLeft;
    }

    public long getTotal() {
        return total;
    }

    public boolean hasMore() {
        return numLeft > 0;
    }

    public Integer[] getNext() {

        if (numLeft == total) {
            numLeft -= 1;
            return a;
        }

        int temp;

        // Find largest index j with a[j] < a[j+1]

        int j = a.length - 2;
        while (a[j] > a[j + 1]) {
            j--;
        }

        // Find index k such that a[k] is smallest integer
        // greater than a[j] to the right of a[j]

        int k = a.length - 1;
        while (a[j] > a[k]) {
            k--;
        }

        // Interchange a[j] and a[k]

        temp = a[k];
        a[k] = a[j];
        a[j] = temp;

        // Put tail end of permutation after jth position in increasing order

        int r = a.length - 1;
        int s = j + 1;

        while (r > s) {
            temp = a[s];
            a[s] = a[r];
            a[r] = temp;
            r--;
            s++;
        }

        numLeft--;
        return a;

    }
}
