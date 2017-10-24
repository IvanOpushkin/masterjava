package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

 /*   private static final ExecutorService matrixMultiplyService = Executors.newFixedThreadPool(10);*/

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        /*По кусочкам всё записывается в матрицу, не вся*/
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[][] matrixBCopy = new int[matrixSize][matrixSize];

        /*(Идея)Возможно тут можно с таймером всё расчитать, если процессорные мощности симметричны. Пул заполненный всегда шире*/
        for (int i = 0 ; i < matrixSize; i++)
            for (int j = 0 ; j < matrixSize; j++)
                /*Прописанно в компиляторе УЖЕ по работе с кэшами (Строко мощь) НО ЗНАТЬ*/
                matrixBCopy[i][j] = matrixB[j][i];




        /*Итого Пускаем в разные потоки по половине i,j (Simple First Concurrent Optimization)*/

        /*(ДЕТАЛЬ)Возможно нужно проверять экзекутор и ставить если больше MainMatrix. ThreadNumber 10. Если больше 10.*/

        //Как я понял теряются значения, потому что массивы не успевают заполница mb, а их уже читают в другом потоке

        //Спокойно должен пройти вариант Чётное. Нечётное по идее

        //region ПЕРЕМНОЖЕНИЕ ЧЁТНЫХ СТРОК (И СТОЛБЦОВ)
        executor.submit(new Runnable(){
            @Override
            public void run()  {


                //Чётные строки перебор
                for (int i = 0 ; i < matrixSize; i=+2)
                    //Весь перебор множителя под конкретную строку
                    for (int j = 0 ; j < matrixSize; j++)
                    {
                        int sum=0;

                        for(int k=0;  k < matrixSize; k++)
                            sum+=matrixA[i][k]*matrixBCopy[j][k];

                        /*System.out.println(sum);*/
                        matrixC[i][j] = sum;
                    }
            }
        });
        //endregion

        //region ПЕРЕМНОЖЕНИЕ НЕЧЁТНЫХ СТРОК (И СТОЛБЦОВ)

        executor.submit(new Runnable(){
            @Override
            public void run()  {

                //Нечётные строки перебор
                for (int i = 1 ; i < matrixSize; i=+2)
                    //Весь перебор множителя под конкретную строку
                    for (int j = 1 ; j < matrixSize; j++)
                    {
                        int sum=0;

                        for(int k=0;  k < matrixSize; k++)
                            sum+=matrixA[i][k]*matrixBCopy[j][k];


                        /*System.out.println(sum);
                        * if(i==2 && j==2) System.out.println(sum);
                        * */

                        matrixC[i][j] = sum;
                    }


            }
        });
        //(K)ЗАПОЛНЕНИЕ НЕЧЁТНЫХ
        //endregion


        /*Микс Блок-Очереди и Экзекютора = КомплетионСервис
        //final CompletionService<int[][]> completionService = new ExecutorCompletionService<>(executor);

        //completionService.submit(Callable<int[][])>);*/



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

        int[] bRow = new int[matrixSize];
        //Была ошибка каждый ряд обнулялся
        try {
            for (int i = 0; i < matrixSize; i++) {
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
        } catch (IndexOutOfBoundsException ignore) {
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

                    System.out.println(i + " " + j);
                    return false;

                }
            }
        }
        return true;
    }
}
