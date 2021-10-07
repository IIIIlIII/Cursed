package com.company;


import java.util.ArrayList;

import static java.lang.System.out;


public class pr {

    public static void main(String[] args) {
        int n=17;//кол-во базисных переменных
        int m=1;//кол-во строки или ограничений
        int k=4;
        double[] c = new double[n];     // коэффициенты из целевой функции
        double[][] a = new double[m][n];// коэффициенты из ограничений
        double[] cB = new double[m];    // коэффициенты при базисных переменных
        double[] L = new double[n+1];   //целевая функция
        double[] B = new double[m];     //столбец B
        double[] oldB = new double[m];
        double[][] newa =new double[m][n+1];//конечный массивы a,L,C после симплекс метода
        double[] newL = new double[n+2];
        double[] newC = new double[n+1];
        int [] bas = new int[m];//базиснные переменные
        int [] h = new int [n];
        double []result= new double[k];//массив результата
        double []resultX =new double[k];

        a[0][0] = 10;a[0][1] = 20;a[0][2] = 40;a[0][3] = 50;
        a[0][4] = 20;a[0][5] = 30;a[0][6] = 40;a[0][7] = 50;
        a[0][8] = 10;a[0][9] = 20;a[0][10] = 30;a[0][11] = 40;
        a[0][12] = 10;a[0][13] = 20;a[0][14] = 30;a[0][15] = 40;a[0][16] = 50;

        h[0]=1;h[1]=1;h[2]=1;h[3]=1;
        h[4]=2;h[5]=2;h[6]=2;h[7]=2;
        h[8]=3;h[9]=3;h[10]=3;h[11]=3;
        h[12]=4;h[13]=4;h[14]=4;h[15]=4;h[16]=4;

        c[0] = 8; c[1] =10; c[2] = 21; c[3] = 40;c[4]=13;
        c[5] = 17;c[6] = 25;c[7] = 45;c[8] = 6;c[9] = 16;
        c[10] = 20;c[11] = 19;c[12] = 9;c[13] = 14;c[14] = 16;
        c[15] = 19;c[16] = 39;
        cB[0]=0;
        B[0]=120;
        oldB[0]=120;

        double maxElement=0;
        for(int i=0; i<a[0].length;i++){
            if(a[0][i]>maxElement){
                maxElement=a[0][i];
            }
        }
        ArrayList<Double>arraylist=new ArrayList<>();
        for(int i=0;i<4;i++) {
            Lcalc(cB, c, L, a);
            printTable(c, a, cB, L, B, bas);
            int number = preobraz_a(a, newa, B, newC, c, bas,oldB);
            //Lcalc(cB, c, L, a);
            preobraz_L(L, newL, newa, newC, number);
            out.println();
            while (optPlan(newL)) {
                printTable(c, newa, cB, newL, B, bas);
                out.println(maxNegativeColumn(newL));
                //out.println(otnRow(newa, maxNegativeColumn(newL), B));
                simplexMethod(otnRow(newa, maxNegativeColumn(newL), B), maxNegativeColumn(newL), newa, B, newL, L, c, bas);
                printTable(c, newa, cB, newL, B, bas);
            }

            arraylist.add(o(bas,a,c,L,oldB,maxElement,i,h,result));
            if(oldB[0]<=0){
                break;
            }
        }
        out.println();
        out.print("ответ:");
        for(int i=0;i<k;i++){
            if(result[i]!=0) {
                out.print(result[i] + " ");

            }
        }
        out.println();
        for(int i=0;i<arraylist.size();i++) {
            out.print(arraylist.get(i) + " ");
        }
    }
    static int preobraz_a(double [][]a,double[][]newa,double []B,double[]newC,double[]c,int []bas,double []oldB){//преобразование массива для дальнейших вычисленний
        for(int i=0;i<a.length;i++) {
            for (int j = 0; j < a[0].length; j++) {
                newa[i][j] = a[i][j];
            }
        }
        for (int j = 0; j < c.length; j++) {
            newC[j] = c[j];
        }
        int numberMax=0;
        double max=0;
        for (int i=0;i<a[0].length;i++){
            if(Math.abs(a[0][i])>max){
                max=Math.abs(a[0][i]);
                numberMax=i;
            }
        }
        newC[newC.length-1] = 0;
        newa[a.length-1][a[0].length]=-1/newa[a.length-1][numberMax];

        double del= newa[newa.length-1][numberMax];
        for(int j=0;j<B.length;j++) {
            B[j]=oldB[j]/newa[j][numberMax];
            for (int i = 0; i < a[0].length; i++) {
                //out.println(newa[j][i] +"/"+del);
                newa[j][i] /=del;
            }
        }
        bas[0]=numberMax+1;
        return numberMax;
    }
    static void preobraz_L(double []L,double[]newL,double[][]newa,double []newC,int number){
        for(int i=1;i<L.length;i++){
            newL[i]=L[i];
            //out.print(newL[i]+" ");
        }
        //out.println();
        newL[number+1]=1;
        for (int i=1;i<newL.length;i++){
            //out.println(newC[number] +"*"+ newa[0][i-1]+"-"+newC[i-1]);
            newL[i] = newC[number]*newa[0][i-1]-newC[i-1];
        }
    }
    static void Lcalc(double cB[],double[] c, double L[], double[][] a){    //вычисление L
        double help[] = new double[L.length];
        L[0] = 0;
        for(int j=0;j<cB.length;j++) {
            for (int i = 1; i < a[j].length; i++) {
                help[i] +=cB[j]*a[j][i];
            }
        }
        for(int i = 1; i < L.length; i++){
            L[i]= -c[i-1]+help[i-1];
        }
    }
    static boolean optPlan(double L[]){//проверка оптимальности плана
        int k=0;
        for(int i=1;i<L.length;i++){
            if(L[i]<=0){
                k++;
            }
        }
        if(k==L.length-1){return false;}
        return true;
    }
    static int maxNegativeColumn (double L[]){//вычисление разрещающего столбца
        int NumberMinColumn = -1;
        double mincolumn = 0;
        for(int i=1;i<L.length;i++) {
            if ((L[i] > 0) && (L[i] > mincolumn)) {
                NumberMinColumn = i;
                mincolumn = L[i];
            }
        }
        //out.println(mincolumn);
        return NumberMinColumn;
    }
    static int otnRow(double [][]a, int maxNegativeColumn,double []B){//вычисление разрещающей строки
        int NumberMaxColumn = 0;
        double MaxColumn = 0;
        for(int i=0;i<B.length;i++){
            if(B[i]/a[i][maxNegativeColumn]>MaxColumn){
                NumberMaxColumn = i;
                MaxColumn=B[i]/a[i][maxNegativeColumn];
            }
        }
        return NumberMaxColumn;
    }
    static void simplexMethod(int row,int column,double [][]a, double []B,double []newL,double []L,double []C,int []bas){//сам симплекс метод
        int c = column-1;
        bas[0]=column;
        double coof = 1/a[row][c];
        a[row][c]=a[row][c]*coof;
        B[0]=B[0]*coof;
        for(int i=0;i<a[0].length;i++){//коофиценты разрещающего столбца
            if(i!=c) {
                a[row][i] *= coof;
                //out.print(i+":"+a[row][i]+" ");
            }
        }
        out.println();
        for(int i=0;i<a.length;i++){//коофиценты разрещающего столбца
            if(i!=row) {
                a[i][c] *=-coof;
                //out.print(a[i][c]+" ");
            }
        }
        newL[column]*=-coof;
        //out.println(newL[column]+"*"+-coof);
//        for(int i=0;i<a.length;i++) {
//           for (int j = 0; j < a[i].length; j++){
//              out.print(a[i][j] +" ");
//           }
//            out.println();
//        }
        for(int i=1;i<newL.length-1;i++){

                //out.println(C[c]+ " * " +a[row][i-1]+" - "+L[i]);
                newL[i] = C[c] * a[row][i-1]+L[i];

        }
    }
    static double o(int []bas,double [][]a,double []c,double []L,double[]oldB,double maxElement,int iter,int []h,double []result){
        double help=0;
        if (oldB[0]<=maxElement){
            double []helpC = new double[a[0].length];
            double []helpA = new double[a[0].length];
            int j=0;
            double x=0,y=0;
            for(int i=0;i<a[0].length;i++){
                if(a[0][i]>=oldB[0]){
                    helpC[j]=c[i];
                    helpA[j]=a[0][i];
                    j++;
                }
            }
            for(int i=1;i<helpC.length;i++){
                if(helpC[i-1]<helpC[i]){
                    x=helpA[i-1];
                    y=helpC[i-1];
                }
            }
            oldB[0]-=x;
            result[iter]=y;
            return x;
        }else {
            out.println(oldB[0]+" - "+a[0][bas[0]-1]);
            help=a[0][bas[0] - 1];
            oldB[0] -= a[0][bas[0] - 1];
            result[iter]=c[bas[0]-1];
            out.println(oldB[0]);
            for (int i = 0; i < a[0].length; i++) {
                //out.print(h[i] + " " + h[bas[0]]);
                if (h[i] == h[bas[0] - 1]) {
                    a[0][i] = 0;
                    c[i] = 0;
                    L[i] = -1000;
                }
            }
        }
        return help;
    }

    static void printTable(double[] c, double[][] a, double[] cB,double L[],double B[],int []bas) {
        // вывод: строка коэффициентов
        out.print("\t\t\t\t");
        for (int j = 0; j < c.length; j++) {
            out.print(c[j]+" \t");
        }
        out.println("\t");

        // вывод: ряд x
        out.print("cB\t");
        out.print("Bas"+"\t");
        out.print("B\t\t");
        for (int j = 1; j < L.length; j++) {
            out.print("x[" + j + "]\t");
        }
        out.println();
        for (int i = 0; i < a.length; i++) {
            out.print(cB[i]+"\t");
            out.print("x"+bas[i]+"\t");
            out.print(B[i]+" \t");
            for (int j = 0; j < a[i].length; j++) {
                out.print(a[i][j] + " \t");
            }
        }
        out.println();
        out.print("\tL\t");
        for (int i = 0; i < L.length; i++){
            out.print(L[i] + " \t");
        }
        out.println();
    }
}
