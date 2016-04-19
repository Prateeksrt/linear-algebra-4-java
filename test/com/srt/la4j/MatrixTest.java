/*
 *     Copyright (C) 2016  Prateek Srivastava
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/gpl.html.
 *
 */

package com.srt.la4j;

import com.srt.la4j.exceptions.AdditionCompatibleException;
import com.srt.la4j.exceptions.MultiplicationCompatibleException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class MatrixTest {

    private Matrix matrix3x3;
    private Matrix first2x2Matrix;
    private Matrix second2x2Matrix;
    private Matrix matrix3x2;


    @Before
    public void setUp() throws Exception {
        double[][] array2x2 = {{1, 2}, {1, 2}};
        double[][] secondArray2x2 = {{1, 2}, {1, 2}};
        double[][] array3x2 = {{1, 2}, {1, 2}, {1, 2}};
        double[][] array3x3 = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};

        matrix3x3 = new Matrix(array3x3);
        matrix3x2 = new Matrix(array3x2);
        first2x2Matrix = new Matrix(array2x2);
        second2x2Matrix = new Matrix(secondArray2x2);

    }

    @Test
    public void shouldCreateMatrixWithElements() throws Exception {
        assertEquals(1d, matrix3x3.get(0, 0));
        assertEquals(3d, matrix3x3.get(2, 2));
        assertEquals(2d, matrix3x3.get(1, 1));
        assertEquals(1d, matrix3x3.get(2, 0));
    }

    @Test
    public void shouldSetCorrectValuesAtCorrectLocations() throws Exception {
        matrix3x3.set(1, 1, 10);

        assertEquals(10d, matrix3x3.get(1, 1));
    }

    @Test
    public void shouldAddCorrespondingElementsOfOneMatrixToAnother() throws Exception {
        Matrix result = first2x2Matrix.add(second2x2Matrix);

        assertEquals(2d, result.get(0, 0));
        assertEquals(4d, result.get(0, 1));
        assertEquals(2d, result.get(1, 0));
        assertEquals(4d, result.get(1, 1));
    }

    @Test(expected = AdditionCompatibleException.class)
    public void shouldThrowAdditionCompatibleExceptionIfMatricsAreNotAdditionCompatible() throws Exception {
        first2x2Matrix.add(matrix3x2);
    }

    @Test
    public void multiplicationOfMatrixToANumberShouldMultiplyEachElementWithThatNumber() throws Exception {
        Matrix result = first2x2Matrix.multiply(2);

        assertEquals(2d, result.get(0, 0));
        assertEquals(4d, result.get(0, 1));
        assertEquals(2d, result.get(1, 0));
        assertEquals(4d, result.get(1, 1));
    }

    @Test
    public void multiplicationOfMatrixShouldFollowTheMatrixMultiplicationRule() throws Exception {
        Matrix result = first2x2Matrix.multiply(second2x2Matrix);

        assertEquals(3d, result.get(0, 0));
        assertEquals(6d, result.get(0, 1));
        assertEquals(3d, result.get(1, 0));
        assertEquals(6d, result.get(1, 1));
    }

    @Test(expected = MultiplicationCompatibleException.class)
    public void multiplicationShouldThrowMultiplicationCompatibileExceptionInMatrixAreNotCompatible() throws Exception {
        first2x2Matrix.multiply(matrix3x2);
    }
}