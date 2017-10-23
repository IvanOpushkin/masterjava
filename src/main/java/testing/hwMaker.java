package testing;

/**
 * Created by Mega on 23.10.2017.
 */
public class hwMaker {

    //Как сделать тест ДЛЯ КОНКРЕТНОГО ФАЙЛА(интересно).

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        //Т.к в данном случае матрицы одинаковые и квадратные, длинна строки равна колву столбцов у обоих.
        //Иначе писалось бы вот так.
        //final int matrixAlength=matrixA.length;
        //final int matrixAwidth=matrixA.width;

        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        //1 из 4.Оптимизация модернизация (из олимпиадной модернизации) кода убираем matrixA.length везде в циклах.
        //Переносим вынос в пару Final переменных.
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    //Нулевые и единичные строки забыл запутался.
                    sum += matrixA[i][k] * matrixB[k][j];
                    //System.out.print(sum+" ");
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int [][] x = new int[10][20];
    public static int [][] y = new int[10][25];

    public static void fill2DMatrix(int[][] x)
    {
       //Первое строка, вторая столбец
       int sum = 0;

        for (int i=0; i<x.length;i++)
            for (int j=0; j<x[0].length;j++)
            {
                sum++;
                x[i][j] = sum;
            }

    }

    public static void print2DMatrix(int[][] x)
    {
        //Первый цикл по строке, второй по столбцам
        for (int i=0;i<x.length; i++) {
            System.out.println();
            for (int j = 0; j < x[0].length; j++)
                System.out.print(x[i][j] + " ");
        }
    }

    public static void main(String[] args)
    {
        //print2DMatrix(x);
        fill2DMatrix(x);
        fill2DMatrix(y);
        //print2DMatrix(y);
        print2DMatrix(singleThreadMultiply(x,y));
    }


}
