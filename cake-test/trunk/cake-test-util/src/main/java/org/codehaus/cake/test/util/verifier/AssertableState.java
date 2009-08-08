package org.codehaus.cake.test.util.verifier;

import java.util.BitSet;

import org.junit.Assert;

public class AssertableState implements Verifier {
    private int state; // 0 initial, 1 running, 2 dead
    private String currentState;

    private BitSet barries;
    private String barrierToState;
    private int barrierToStateInt;
    private int barrierLength;

    public synchronized AssertableState advance(String state) {
        if (state == null) {
            throw new NullPointerException("state is null");
        } else if (state.equals("")) {
            throw new IllegalArgumentException("state is ''");
        } else if (this.state == 2) {
            throw new IllegalStateException("Already in end state, cannot procedd");
        }
        StringParser sp = new StringParser(state);
        String from = sp.nextAlphaNumeric();
        Integer iBarrier = null;
        Integer totalBarrier = null;
        sp.consume('-');
        if (sp.ifNextConsume('[')) {
            iBarrier = sp.nextNumeric();
            sp.consume('/');
            totalBarrier = sp.nextNumeric();
            sp.consume(']');
        }
        sp.consume('>');
        String to = sp.nextAlphaNumeric();
        if (sp.hasNext()) {
            throw new IllegalArgumentException("should be empty");
        }
        return process(from, iBarrier, totalBarrier, to);
    }

    private AssertableState process(String from, Integer iBarrier, Integer totalBarrier, String to) {
        if (from == null && state > 0) {
            throw new IllegalArgumentException("Not in initial state");
        } else if (from != null && state == 0) {
            throw new IllegalArgumentException("Expected initial state, but was:" + from);
        } else if (currentState != null && !currentState.equals(from)) {
            throw new IllegalArgumentException("Is currently in '" + from + ", got '" + from + "'");
        }
        if (iBarrier != null) {
            if (barries == null) {
                barries = new BitSet(totalBarrier);
                barrierLength = totalBarrier;
                barrierToState = to;
            }
            if (totalBarrier != barrierLength) {
                throw new IllegalStateException();
            }
            if (iBarrier > barrierLength) {
                throw new IllegalArgumentException();
            }
            if (barrierToState == null) {
                if (to != null) {
                    throw new IllegalStateException();
                }
            } else if (!barrierToState.equals(to)) {
                throw new IllegalStateException();
            }
            if (barries.get(iBarrier - 1)) {
                throw new IllegalStateException("already set");
            }
            barries.set(iBarrier - 1);
            if (barries.cardinality() == barrierLength) {
                currentState = to;
                this.state = to == null ? 2 : 1;
                barrierLength = 0;
                barries = null;
                barrierToStateInt = 0;
                barrierToState = null;
            }
        } else {
            if (barries != null) {
                throw new IllegalArgumentException();
            }
            currentState = to;
            this.state = to == null ? 2 : 1;
        }
        return this;
    }

    // state.advance("0-[1|3]>1");
    public synchronized void assertState(String state) {
        Assert.assertEquals(currentState, state);
    }

    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetterOrDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public synchronized void assertInitialState() {
        Assert.assertEquals("not in initial state", 0, state);
    }

    public synchronized void assertIsActive() {
        Assert.assertEquals("not in active state, state=" + currentState, 1, state);
    }

    public synchronized void assertEndState() {
        if (state == 0) {
            throw new AssertionError("Expected to be in end state, was in start state");
        } else if (state == 1) {
            throw new AssertionError("Expected to be in end state, was in running state (" + state + ")");
        }
    }

    public synchronized void assertInitialOrEndState() {
        Assert.assertTrue("not in inital or end state", state != 1);
    }

    static class StringParser {
        private int index = 0;
        private String state;

        StringParser(String s) {
            state = s;
        }

        boolean ifNextConsume(char c) {
            if (isNext(c)) {
                index++;
                return true;
            }
            return false;
        }

        boolean isNext(char c) {
            return state.charAt(index) == c;
        }

        boolean hasNext() {
            return index < state.length();
        }

        static boolean isAlphaNumeric(char c) {
            return Character.isLetter(c) || Character.isDigit(c);

        }

        String nextAlphaNumeric() {
            int start = index;
            int i = index;
            for (; i < state.length(); i++) {
                if (!isAlphaNumeric(state.charAt(i))) {
                    break;
                }
            }
            index = i;
            if (index == start) {
                return null;
            }
            return state.substring(start, index);
        }

        int nextNumeric() {
            String str = nextAlphaNumeric();
            return Integer.parseInt(str);
        }

        StringParser consume(char c) {
            if (!isNext(c)) {
                throw new IllegalArgumentException("expected " + c + ", was " + state.charAt(index));
            }
            index++;
            return this;
        }
    }

    public void verify() {
        assertEndState();
    }

    //    
    // int minus_index = state.indexOf('-');
    // if (minus_index == -1) {
    // throw new IllegalStateException("exception '-' or from-state, was: " + state);
    // }
    // if (minus_index == 0) {
    //
    // } else {
    // String from = state.substring(0, minus_index);
    //
    // if (!isAlphanumeric(from)) {
    // throw new IllegalArgumentException("from-state not alphanumeric, was: " + state);
    // }
    //
    // }
    // if (minus_index == state.length()) {
    // throw new IllegalArgumentException("expected >");
    // }
    // char next = (state.charAt(minus_index + 1));
    // if (next == '[') {
    // int nbits = 3;
    // if (barries == null) {
    // barries = new BitSet(nbits);
    // }
    // }
    // int arrowIndex = state.indexOf('>', minus_index);
    //
    // if (arrowIndex == state.length() - 1) {
    //        
    // } else {
    // String toState = state.substring(arrowIndex + 1);
    // if (!isAlphanumeric(toState)) {
    // throw new IllegalArgumentException("to-state not alphanumeric, was: " + toState);
    // }
    //
    // }
}
