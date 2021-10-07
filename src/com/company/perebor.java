package com.company;


import static java.lang.System.out;
import static java.lang.System.setOut;

public class perebor {
    final static int n=5;//кол-во базисных переменных
    final static int m=4;//кол-во строки или ограничений
    final static int min = 120;
    public static void main(String[] args) {

        long[] c = new long[n];     // коэффициенты из ограничений
        long[][] a = new long[m][n];// коэффициенты из целевой функции
        long[] C = new long[m];// коэффициенты ценности
        int [] Ci = new int[m];
        c[0] = 10;
        c[1] = 20;
        c[2] = 30;
        c[3] = 40;
        c[4] = 50;
        double s = Double.POSITIVE_INFINITY;
        for (int i = 0; i < m; i++){
            C[i]= (long) s;
        }
        a[0][0] = 8; a[0][1] =10; a[0][2] = 0; a[0][3] = 21; a[0][4] = 40;
        a[1][0] = 0; a[1][1] =13; a[1][2] = 17; a[1][3] = 25; a[1][4] = 45;
        a[2][0] = 6; a[2][1] =16; a[2][2] = 20; a[2][3] = 19; a[2][4] = 0;
        a[3][0] = 9; a[3][1] =14; a[3][2] = 16; a[3][3] = 19; a[3][4] = 39;

        prividenie(c,a);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                out.print(a[i][j] + "\t");
            }
            out.println();
        }
        MinC(a,C,Ci);
        for (int i = 0; i < m; i++) {
            out.print(C[i]+"\t");
        }
        out.println(proverka(Ci,C,c));
//        printTable(c,a,cB,L,B);

    }

    static void prividenie(long [] c, long [][]a){
        int coof=1;
        for(int i=0;i<n;i++){
            coof*=c[i];
        }
        for(int i=0;i<m;i++)
        for (int j=0;j<n;j++){
            a[i][j] *= (coof/c[j]);
        }
    }
    static void MinC(long [][]a,long []C,int []Ci){
        for(int i=0;i<m;i++) {
            for (int j = 0; j < n; j++) {
                if((a[i][j]<C[i])&&(a[i][j]!=0)){
                    C[i]=a[i][j];
                    Ci[i]=j;
                }
            }
        }
    }

    static int proverka(int[]Ci,long[]C,long[]c){
        int sum = 0;
        for(int i=0;i<m;i++){
            sum+=c[Ci[i]];
        }
        return sum;
    }

    static void printTable(double[] c, double[][] a, double[] cB,double L[],double B[]) {

        // вывод: строка коэффициентов
        out.print("\t\t\t");
        for (int j = 0; j < n; j++) {
            out.print(c[j]+" \t");
        }
        out.println("\t");

        // вывод: ряд x
        out.print("cB\t");
        out.print("B\t\t");
        for (int j = 0; j < n; j++) {
            out.print("x[" + j + "]\t");
        }
        out.println();
        for (int i = 0; i < m; i++) {
            out.print(cB[i]+"\t");
            out.print(B[i]+" \t");
            for (int j = 0; j < n; j++) {
                out.print(a[i][j] + "\t");
            }}
        out.println();
        out.print("L\t");
        for (int i = 0; i < n+1; i++){
            out.print(L[i] + " \t");
        }
        out.println();
    }
}
