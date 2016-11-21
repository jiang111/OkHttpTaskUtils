package com.jiang.android.okhttptaskutils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test2() {


        int[] arr = init(1,2,3,4);
        System.out.print(arr.toString());
    }


    private int[] init(int... value) {
        return value;
    }
}