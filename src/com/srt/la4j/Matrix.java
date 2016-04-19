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
import com.srt.la4j.exceptions.NotASqaureMatrixException;

@SuppressWarnings("unused")
public class  Matrix {

    private final int rows;
    private final int columns;
    private double matrix[][];
    private boolean approximate;

    @SuppressWarnings("FieldCanBeLocal")
    private double minValue = 9.9E-10;

    /**
     * @param matrix is two dimensional array which is used to create matrix
     */
    public Matrix(double[][] matrix){
        this(matrix,false);
    }

    /**
     * @param matrix is two dimensional array which is used to create matrix.
     * @param approximate sets the approximation true.
     */
    public Matrix(double[][] matrix, boolean approximate) {
        this.matrix = matrix;
        this.rows = matrix.length;
        try {
            this.columns = matrix[0].length;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Not a valid 2-dimensional array! " +
                    "Number Of columns not defined.");
        }
        this.approximate = approximate;
        approximate();
    }

    /**
     * Returns the element present at rowIndex and columnIndex position.
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex are out side the range.
     *
     * @param rowIndex
     * @param columnIndex
     * @return the element present at rowIndex and columnIndex position in the matrix.
     */
    public double get(int rowIndex, int columnIndex){
        if(rowIndex >= rows || rowIndex < 0 || columnIndex >= columns || columnIndex <0 )
            throw new ArrayIndexOutOfBoundsException();
        return matrix[rowIndex][columnIndex];
    }

    /**
     * Sets the value that will be inserted in the position specified by rowIndex and columnIndex.
     * @throws ArrayIndexOutOfBoundsException if rowIndex or columnIndex are out side the range.
     *
     * @param rowIndex
     * @param columnIndex
     * @param value is the value that will be inserted in the position specified by
     *              rowIndex and columnIndex.
     */
    public void set(int rowIndex, int columnIndex, double value){
        if(rowIndex >= rows || rowIndex < 0 || columnIndex >= columns || columnIndex <0 )
            throw new ArrayIndexOutOfBoundsException();
        matrix[rowIndex][columnIndex] = value;
        approximate();
    }

    /**
     * Add matrix element wise and return a new matrix that is sum of both the matrix.
     * Does not modifies the original matrix.
     *
     * @param matrix
     * @return
     * @throws AdditionCompatibleException if number of rows and columns of the matrix are not
     *           equal to the number of rows and columns of matrix with which addition is going
     *           to be performed.
     */
    public Matrix add(Matrix matrix) throws AdditionCompatibleException {
        if(isDimensionallyEqual(matrix)){
            double[][] d = new double[rows][columns];
            for(int i = 0; i< rows; i++){
                for(int j = 0; j< columns; j++){
                    double sum =  matrix.get(i,j)+this.get(i,j);
                    d[i][j]=sum;
                }
            }
            return new Matrix(d,approximate);
        }
        throw new AdditionCompatibleException();
    }

    /**
     * Multiply the matrix with normal rule of matrix multiplication and return product matrix.
     * This method does not modifies the original matrix.
     *
     * @param matrix
     * @return
     * @throws MultiplicationCompatibleException if number of columns of the matrix is not equal to
     *               number of rows of the matrix with multiplication has to performed.
     */
    public Matrix multiply(Matrix matrix) throws MultiplicationCompatibleException {
        if(isMultiplicationCompatible(matrix)){
            double[][] d = new double[rows][columns];
            for(int i = 0; i<this.rows; i++){
                for(int j = 0; j<matrix.columns; j++){
                    double sum = 0;
                    for(int k=0;k<this.columns;k++){
                        sum += this.get(i,k)*matrix.get(k,j);
                    }
                    d[i][j]=sum;
                }
            }
            return new Matrix(d,approximate);
        }
        throw new MultiplicationCompatibleException();
    }

    /**
     * @param dimension
     * @return identity square matrix of dimension specified as parameter.
     */
    public static Matrix identity(int dimension){
        double[][] d = new double[dimension][dimension];
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(i==j) d[i][j]=1;
                else d[i][j]=0;
            }
        }
        return new Matrix(d);
    }

    /**
     * @return determinant of the given matrix.
     * @throws Exception if the given matrix is not a square matrix.
     */
    public double getDeterminant() throws Exception {
        if(!isSquareMatrix()){
            throw new NotASqaureMatrixException("Cannot find determinant! Not a Square matrix");
        }
        return determinant();
    }

    private boolean isSquareMatrix() {
        return this.rows == this.columns;
    }

    private double determinant() {
        if(this.rows == 1){
            return get(0,0);
        }
        if(this.rows == 2){
            return this.get(0,0)*this.get(1,1) - get(0,1)*get(1,0);
        }
        int determinant = 0;
        int sign = 1;
        for(int i = 0 ; i< columns;i++){
            determinant += sign * get(0,i) * subMatrix(i).determinant();
            sign *= -1;
        }
        return determinant;
    }

    private Matrix subMatrix(int columnIndex){
        return subMatrix(0,columnIndex);
    }

    private Matrix subMatrix(int rowIndex, int columnIndex){
        double[][] d = new double[rows-1][columns-1];
        for(int i =0, subRow=0; i<rows;i++){
            for(int j=0, subColumn=0 ;j<columns;j++){
                if(i==rowIndex || j==columnIndex){
                    continue;
                }
                d[subRow][subColumn++] = get(i,j);
            }
            if(i!=rowIndex){
                subRow++;
            }

        }
        return new Matrix(d,approximate);
    }


    private boolean isMultiplicationCompatible(Matrix matrix) {
        return this.columns == matrix.rows;
    }

    private boolean isDimensionallyEqual(Matrix matrix) {
        return matrix.rows == this.rows && matrix.columns == this.columns;
    }

    /**
     * Checks if each and every element of matrix and matches it with the matrix.
     *
     * @param obj
     * @return true if equals else return false
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Matrix)){
            return false;
        }
        Matrix that = (Matrix)obj;
        if(that.rows != this.rows || that.columns!=this.columns){
            return false;
        }
        for(int i =0;i<this.rows;i++){
            for(int j=0;j<this.columns;j++){
                if(this.get(i,j)!=that.get(i,j)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param rowIndex
     * @param columnIndex
     * @return cofactor of the elements specified by rowIndex and columnIndex.
     * @throws Exception
     */
    public double cofactor(int rowIndex, int columnIndex) throws Exception {
        return minor(rowIndex, columnIndex) * Math.pow(-1,(rowIndex+columnIndex));
    }

    private double minor(int rowIndex, int columnIndex) throws Exception {
        return subMatrix(rowIndex,columnIndex).getDeterminant();
    }

    /**
     * @return inverse of the matrix
     * @throws Exception if the given matrix is a singular matrix.
     */
    public Matrix inverse() throws Exception {
        double det = this.getDeterminant();
        if(det == 0)
            throw new Exception("This is a singular Matrix! It does not have a inverse.");
        return adjoint().transpose().multiply(Math.pow(det,-1));
    }

    /**
     * @return adjoint of the given matrix.
     * @throws Exception
     */
    public Matrix adjoint() throws Exception {
        double[][] cM = new double[this.rows][this.columns];
        for(int i =0;i<rows;i++){
            for(int j =0;j<columns;j++){
                cM[i][j] = cofactor(i,j);
            }
        }
        return new Matrix(cM,approximate);
    }

    /**
     * @return transpose of the given matrix
     */
    public Matrix transpose(){
        double[][] d = new double[rows][columns];
        for(int i = 0;i<rows;i++){
            for(int j=0;j<columns;j++){
                d[i][j]=this.get(j,i);
            }
        }
        return new Matrix(d,approximate);
    }

    /**
     * @param number
     * @return return a matrix whose each element is multiplied by number specified as parameter.
     */
    public Matrix multiply(double number){
        double[][] d = new double[rows][columns];
        for(int i = 0;i<rows;i++){
            for(int j =0;j<columns;j++){
                d[i][j] = get(i,j)*number;
            }
        }
        return new Matrix(d,approximate);
    }

    private void approximate(){
        if(!approximate) return;
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(Math.abs(matrix[i][j])<minValue){
                    this.matrix[i][j]=0;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for(int i =0;i<rows;i++){
            for(int j =0 ;j<columns;j++){
                string.append(get(i, j)).append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }

    /**
     * @param that
     * @return a product matrix by perform element wise multiplication
     * @throws MultiplicationCompatibleException
     */
    public Matrix multiplyElementWise(Matrix that) throws MultiplicationCompatibleException {
        if(isDimensionallyEqual(that)){
            double[][] d = new double[rows][columns];
            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    d[i][j] = that.get(i,j)*this.get(i,j);
                }
            }
            return new Matrix(d,approximate);
        }
        throw new MultiplicationCompatibleException();
    }
}