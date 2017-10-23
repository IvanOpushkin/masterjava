package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {

        //1.п уже сделан. размер файнал *п52 выключен Java типо* -простой случай квадратной таблицы
        final int matrixSize = matrixA.length;

        final int[][] matrixC = new int[matrixSize][matrixSize];

        //2.п переносим столбцы в строки второй таблицы
/*
        int[][] optimize = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++)
            for (int j = 0; j < matrixSize; j++)
            {
                optimize[i][j] = matrixB[j][i];
            }

*/

//п19 вынос за скобку общего и перемножение банально без вынесенго за скобку AB-DA=(B-D)*A ПРОФЕССОР LOGIC


        try {
            for (int i = 0; i < matrixSize; i++) {

                int[] bRow = new int[matrixSize];

                for (int k = 0; k < matrixSize; k++)
                    bRow[k] = matrixB[k][i]; //переворот столбца в строку (automatic)


                for (int j = 0; j < matrixSize; j++) {
                    int sum = 0;
                    int[] rowA = matrixA[j];//строки в А нас устраивают изначально
                    for (int k = 0; k < matrixSize; k++) {
                        sum += bRow[k] * rowA[k];


                        //заменили матрикс б на нашу перевернутую
                        // sum += matrixA[i][k] * matrixB[k][j];
                        // sum += matrixA[i][k] * optimize[j][k];

                    }
                    matrixC[i][j] = sum;
                }
            }
        }catch(IndexOutOfBoundsException ignore)
        {
            //На случай выхода за массив = выгодно.
        }
            return matrixC;
        }


    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
