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
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class MatrixTest {
    @Test
    public void testMatrixIsCreateWithTheElements() throws Exception {
        double[][] array = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        Matrix matrix = new Matrix(array);

        assertEquals(1d,matrix.get(0,0));
        assertEquals(3d,matrix.get(2,2));
        assertEquals(2d,matrix.get(1,1));
        assertEquals(1d,matrix.get(2,0));
    }

    @Test
    public void setMethodShouldSetCorrectValueAtCorrectLocation() throws Exception {
        double[][] array = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        Matrix matrix = new Matrix(array);

        matrix.set(1,1,10);

        assertEquals(10d,matrix.get(1,1));
    }

    @Test
    public void shouldAddEachElementOfTwoMatrix() throws Exception {
        double[][] array = {{1, 2}, {1, 2}};
        double[][] array1 = {{1, 2}, {1, 2}};
        Matrix matrix = new Matrix(array);
        Matrix matrix1 = new Matrix(array1);

        Matrix result = matrix.add(matrix1);

        assertEquals(2d,result.get(0,0));
        assertEquals(4d,result.get(0,1));
        assertEquals(2d,result.get(1,0));
        assertEquals(4d,result.get(1,1));
    }

    @Test(expected = AdditionCompatibleException.class)
    public void addMethodShouldThrowAdditionCompatibleExceptionIfTwoMatrixAreNotEqualInSize() throws Exception {
        double[][] array = {{1, 2}, {1, 2}};
        double[][] array1 = {{1, 2}, {1, 2},{1,2}};
        Matrix matrix = new Matrix(array);
        Matrix matrix1 = new Matrix(array1);

        matrix.add(matrix1);
    }

    @Test
    public void multiplicationOfMatrixToANumberShouldMultiplyEachElementWithThatNumber() throws Exception {
        double[][] array = {{1, 2}, {1, 2}};
        Matrix matrix = new Matrix(array);

        Matrix result = matrix.multiply(2);

        assertEquals(2d,result.get(0,0));
        assertEquals(4d,result.get(0,1));
        assertEquals(2d,result.get(1,0));
        assertEquals(4d,result.get(1,1));
    }

    @Test
    public void multiplicationOfMatrixShouldFollowTheMatrixMultiplicationRule() throws Exception {
        double[][] array = {{1, 2}, {1, 2}};
        double[][] array1 = {{1, 2}, {1, 2}};
        Matrix matrix = new Matrix(array);
        Matrix matrix1 = new Matrix(array1);

        Matrix result = matrix.multiply(matrix1);

        assertEquals(3d,result.get(0,0));
        assertEquals(6d,result.get(0,1));
        assertEquals(3d,result.get(1,0));
        assertEquals(6d,result.get(1,1));
    }

    @Test(expected = MultiplicationCompatibleException.class)
    public void multiplicationShouldThrowMultiplicationCompatibileExceptionInMatrixAreNotCompatible() throws Exception {
        double[][] array = {{1,2},{1,2}};
        double[][] array1 = {{1,2},{1,2},{1,2}};
        Matrix matrix = new Matrix(array);
        Matrix matrix1 = new Matrix(array1);

        Matrix result = matrix.multiply(matrix1);
    }
}
