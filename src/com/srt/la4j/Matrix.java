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

import java.util.IllegalFormatException;

public class  Matrix {

    private final int rows;
    private final int columns;
    private double matrix[][];
    private boolean approximate;

    private double minValue = 9.9E-10;

    public Matrix(double[][] matrix){
        this(matrix,false);
    }

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

    public double get(int rowIndex, int columnIndex){
        if(rowIndex >= rows || rowIndex < 0 || columnIndex >= columns || columnIndex <0 )
            throw new ArrayIndexOutOfBoundsException();
        return matrix[rowIndex][columnIndex];
    }

    private void set(int rowIndex, int columnIndex, double value){
        if(rowIndex >= rows || rowIndex < 0 || columnIndex >= columns || columnIndex <0 )
            throw new ArrayIndexOutOfBoundsException();
        matrix[rowIndex][columnIndex] = value;
        approximate();
    }

    public Matrix add(Matrix matrix) throws Exception {
        if(isAddCompatible(matrix)){
            double[][] d = new double[rows][columns];
            for(int i = 0; i< rows; i++){
                for(int j = 0; j< columns; j++){
                    double sum =  matrix.get(i,j)+this.get(i,j);
                    d[i][j]=sum;
                }
            }
            return new Matrix(d,approximate);
        }
        throw new Exception("Matrices are not compatible for addition");
    }

    public Matrix multiply(Matrix matrix) throws Exception {
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
        throw new Exception("Matrices are not compatible for multiplication");
    }

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

    public double getDeterminant() throws Exception {
        if(!isSquareMatrix()){
            throw new Exception("Cannot find determinant! Not a Square matrix");
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

    private boolean isAddCompatible(Matrix matrix) {
        return matrix.rows == this.rows && matrix.columns == this.columns;
    }

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

    public double cofactor(int rowIndex, int columnIndex) throws Exception {
        return minor(rowIndex, columnIndex) * Math.pow(-1,(rowIndex+columnIndex));
    }

    private double minor(int rowIndex, int columnIndex) throws Exception {
        return subMatrix(rowIndex,columnIndex).getDeterminant();
    }

    public Matrix inverse() throws Exception {
        double det = this.getDeterminant();
        if(det == 0)
            throw new Exception("This is a singular Matrix! It does not have a inverse.");
        return adjoint().transpose().times(Math.pow(det,-1));
    }

    public Matrix adjoint() throws Exception {
        double[][] cM = new double[this.rows][this.columns];
        for(int i =0;i<rows;i++){
            for(int j =0;j<columns;j++){
                cM[i][j] = cofactor(i,j);
            }
        }
        return new Matrix(cM,approximate);
    }

    public Matrix transpose(){
        double[][] d = new double[rows][columns];
        for(int i = 0;i<rows;i++){
            for(int j=0;j<columns;j++){
                d[i][j]=this.get(j,i);
            }
        }
        return new Matrix(d,approximate);
    }

    public Matrix times(double number){
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
                string.append(get(i,j)+" ");
            }
            string.append("\n");
        }
        return string.toString();
    }
}