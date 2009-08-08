package org.codehaus.cake.test.util.verifier;

import org.junit.Before;
import org.junit.Test;

public class AssertableStateTest {

    AssertableState state;

    @Before
    public void setup() {
        state = new AssertableState();
    }

    @Test(expected = NullPointerException.class)
    public void advanceNPE() {
        state.advance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void advanceEmptyString() {
        state.advance("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void notFromInitialState() {
        state.advance("S->A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void twoInitialStates() {
        state.advance("->A").advance("->B");
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongState() {
        state.advance("->A").advance("B->C");
    }

    @Test(expected = IllegalStateException.class)
    public void fromEndState() {
        state.advance("->");
        state.assertEndState();
        state.advance("->B");
    }

    @Test
    public void fromInitialStateToIllegalState() {
        state.advance("->");
        state.assertInitialOrEndState();
        state.assertEndState();
    }

    @Test
    public void simple() {
        state.assertInitialState();
        state.assertInitialOrEndState();
        state.advance("->A");
        state.assertState("A");
        state.advance("A->");
        state.assertInitialOrEndState();
        state.assertEndState();
    }

    @Test
    public void manyStates() {
        state.advance("->A").advance("A->B").advance("B->C").advance("C->");
        state.assertEndState();
    }

    @Test
    public void simpleBarrier() {
        state.advance("-[1/1]>");
        state.assertEndState();
    }

    @Test
    public void simpleBarrier2() {
        state.advance("-[1/2]>");
        state.advance("-[2/2]>");
        state.assertEndState();
    }

    @Test
    public void simpleBarrier3() {
        state.advance("-[1/3]>").advance("-[3/3]>").advance("-[2/3]>");
        state.assertEndState();
    }

    @Test(expected = IllegalStateException.class)
    public void simpleBarrierWrong() {
        state.advance("-[1/2]>").advance("-[2/3]>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongBarrier() {
        state.advance("-[1/2]>").advance("-[3/2]>");
        state.assertEndState();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongBarrier2() {
        state.advance("-[1/2]>").advance("->");
        state.assertEndState();
    }

    @Test
    public void complexBarrier() {
        state.advance("->A");
        state.advance("A-[1/2]>B").advance("A-[2/2]>B");
        state.advance("B-[2/2]>C").advance("B-[1/2]>C");
        state.advance("C->D");
        state.advance("D-[3/3]>").advance("D-[1/3]>").advance("D-[2/3]>");
        state.assertEndState();
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleBarrierWrong2() {
        state.advance("->A");
        state.advance("A-[1/2]>").advance("B-[2/2]>");
    }

    @Test
    public void barrierPartial() {
        state.advance("-[1/2]>");
        state.assertInitialState();
    }
}
