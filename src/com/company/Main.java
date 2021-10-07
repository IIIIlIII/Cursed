package com.company;


import static java.lang.System.out;


public class Main {

    public static void main(String[] args) {
        int n=17;//кол-во базисных переменных
        int m=5;//кол-во строки или ограничений
        int k=4;
        double[] c = new double[n];     // коэффициенты из целевой функции
        double[][] a = new double[m][n];// коэффициенты из ограничений
        //double[] cB = new double[m];    // коэффициенты при базисных переменных
        double[] L = new double[n+1];   //целевая функция
        double[] B = new double[m];     //столбец B
        double[] oldB = new double[m];
        double[][] newa =new double[m][n+1];//конечный массивы a,L,C после симплекс метода
        double[] newL = new double[n+2];
        double[] newC = new double[n+1];
        int [] bas = new int[m];//базиснные переменные
        int [] h = new int [n];
        double []resultC = new double[k+1];//массив результата
        double []resultX = new double[k+1];
        int [] x = new int[n+1];

        a[0][0] = 10;a[0][1] = 20;a[0][2] = 40;a[0][3] = 50;
        a[0][4] = 20;a[0][5] = 30;a[0][6] = 40;a[0][7] = 50;
        a[0][8] = 10;a[0][9] = 20;a[0][10] = 30;a[0][11] = 40;
        a[0][12] = 10;a[0][13] = 20;a[0][14] = 30;a[0][15] = 40;a[0][16] = 50;

        a[1][0]=1;a[1][1]=1;a[1][2]=1;a[1][3]=1;
        a[2][4]=1;a[2][5]=1;a[2][6]=1;a[2][7]=1;
        a[3][8]=1;a[3][9]=1;a[3][10]=1;a[3][11]=1;
        a[4][12]=1;a[4][13]=1;a[4][14]=1;a[4][15]=1;a[4][16]=1;

        c[0] = 8; c[1] =10; c[2] = 21; c[3] = 40;
        c[4]=13;c[5] = 17;c[6] = 25;c[7] = 45;
        c[8] = 6;c[9] = 16;c[10] = 20;c[11] = 19;
        c[12] = 9;c[13] = 14;c[14] = 16;c[15] = 19;c[16] = 39;
        B[0]=-120;B[1]=1;B[2]=1;B[3]=1;B[4]=1;
        oldB[0]=120;

        double maxElement=0;
        for(int i=0; i<a[0].length;i++){
            if(a[0][i]>maxElement){
                maxElement=a[0][i];
            }
        }
        int z=0;
        while(z<x.length){
            x[z]=++z;
        }
            printTable(c, a,  B, bas,x);
            preobraz_a(a, newa, B, newC, c, bas,x);
            printTable(c, a,  B, bas,x);
            cs(B,newa,bas,newC,x);
            b(x,bas);
            Lcalc(newC, newL, newa,bas,B);
            printTable(newC, newa, newL, B, bas,x);
            out.println();
            while (optPlan(newL)) {
                out.println(maxPositiveColumn(newL));
                out.println(otnRow(newa, maxPositiveColumn(newL), B));
                simplexMethod(otnRow(newa, maxPositiveColumn(newL), B), maxPositiveColumn(newL), newa, B,bas,x);
                Lcalc(newC,newL,newa,bas,B);
                printTable(newC, newa, newL, B, bas, x);
            }
            for(int i=0;i<resultC.length;i++) {
                if(B[i]>=0.5) {
                    resultC[i] = c[bas[i] - 1];
                    resultX[i] = a[0][bas[i] - 1];
                }
            }
        out.println();
        out.print("ответ:");
        out.println();
        int sum=0;
        for(int i=0;i<resultC.length;i++){

                sum += (int) resultC[i];
                if (resultC[i]!=0)
                    out.println("Для "+(i+1)+" завода нужно выбрать мощность "+resultX[i]+" стоимостью "+resultC[i]);
        }

        out.println();

        out.println();

        //Gamori(B,newL,newa,bas,x,newC);
    }
    static void preobraz_a(double [][]a,double[][]newa,double []B,double[]newC,double[]c,int []bas,int []x) {//преобразование массива для дальнейших вычисленний
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (i == 0) {
                    newa[i][j] = -a[i][j];
                } else {
                    newa[i][j] = a[i][j];
                }

            }
        }
        bas[0] = newa[0].length;
        newa[0][a[0].length] = 1;
        for (int j = 0; j < c.length; j++) {
            newC[j] = c[j];
        }
        for (int i = 1; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (newa[i][j] != 0) {
                    bas[i] = j + 1;
                    double help = newa[0][j];

                    for (int l = 0; l < a[0].length; l++) {
                        //out.println(newa[0][l]+" - "+newa[i][l]+" * "+help);
                        newa[0][l] -= newa[i][l] * help;
                    }
                    B[0] -= help;
                    break;
                }

            }
        }
    }
        static void cs (double []B,double[][]newa,int []bas,double[]newC,int []x) {
            int it;
            double help;
            while (negativeCheck(B)) {

                it = maxB(B);
                maxA(newa,bas,it);
                help = newa[it][bas[it] - 1];
                //out.println(help);
                for (int i = 0; i < newa[0].length; i++) {
                    newa[it][i] /= help;
                    //out.println(newa[it][i] + " / " + help);
                }
                B[it] /= help;
                printTable(newC, newa,  B, bas, x);

                for (int i = 0; i < newa.length; i++) {
                    help = newa[i][bas[it] - 1];
                    for (int j = 0; j < newa[0].length; j++) {
                        if (i != it) {
                            //out.println(newa[i][j]+" - "+newa[it][j]+" * "+help);
                            newa[i][j] -= newa[it][j] * help;
                        }
                    }
                    if (i != it) {
                        B[i] -= B[it] * help;
                    }
                }
                printTable(newC, newa, B, bas, x);
            }
        }

        static void b(int[] x,int []bas){
            for(int i=0;i<bas.length;i++)
                for(int j=0;j<x.length;j++){
                    if(bas[i]==x[j])
                        x[j]=0;
                }
        }

    static boolean negativeCheck(double[]B){
        for(int i=0;i<B.length;i++){
            if(B[i]<0){
                return true;
            }
        }
        return false;
    }

    static int maxB(double []B) {
        double help = 0.0;
        int it = 0;
        for (int i = 0; i < B.length; i++) {
            if ((Math.abs(B[i]) > help) && (B[i] < 0)) {
                help = Math.abs(B[i]);
                it = i;
            }
        }
        return it;
    }

    static void maxA(double[][]newa,int []bas,int it){
        double max = 0;
        for (int i = 0; i < newa[0].length; i++) {
            if ((Math.abs(newa[it][i]) > max) && (newa[it][i] < 0)) {
                max = Math.abs(newa[it][i]);
                bas[it] = i + 1;
                //out.println(max+" "+bas[it]);
            }
        }
    }

    static void Lcalc(double[] c, double L[], double[][] a,int []bas,double[]B){    //вычисление L
        double help[] = new double[L.length];
        for (int j = 0; j < bas.length; j++) {
            //out.println(help[0] + " + " + c[bas[j] - 1] + " * " + B[j]);
            help[0] += c[bas[j] - 1] * B[j];
        }
        for(int i=1;i<a[0].length+1;i++) {
            for (int j = 0; j < bas.length; j++) {
                //out.println(i+": "+help[i]+" + "+c[bas[j]-1]+" * "+a[j][i-1]);
                help[i] +=c[bas[j]-1]*a[j][i-1];
            }
            if(i!=a[0].length) {
                //out.println(help[i] + "-" + c[i]);
                help[i] -= c[i - 1];
            }
        }

        for(int i = 0; i < L.length; i++){
            L[i]= help[i];
            if((L[i]<1)&&(L[i]>0))
                L[i]=0;
        }
    }
    static void Gamori(double[]B,double[]newL,double[][]newa,int []bas,int[]x,double []newC){
        double [] z= new double[newL.length];
        int it=maxB(B);
        if(B[it]>1){
            z[0]=-B[it] % (int)B[it];
        }else
            z[0]=-B[it];
        for(int i=1;i<z.length;i++){
            if(newa[it][i-1]>1){
                z[i]=-newa[it][i-1] % (int)newa[it][i-1];
            }else
                z[i]=-newa[it][i-1];
        }

        int max_column=maxNegativeColumn(z);
        double help = 1/z[max_column];

        newL[max_column]*=help;
        for(int i=0;i<newa.length;i++){
            newa[i][max_column-1]*=help;
        }
        for(int i=0;i<newa.length;i++)
            for(int j=0;j<newa[0].length;j++){
                if(j!=max_column-1)
                newa[i][j]+=z[j+1]*newa[i][max_column-1];
            }
        for(int i=0;i<B.length;i++){
            B[i]+=z[0]*newa[i][max_column-1];
        }
        for(int i=0;i<z.length;i++){
            if(i!=max_column)
                z[i]*=help;
        }
        for(int i=0;i<z.length;i++){
            out.print(String.format("%.1f",z[i])+" ");
        }
        out.println();
        // рекурсия нужна с созданием новым массивом на +1
        double [][]ra = new double[newa.length+1][newa[0].length];
        double []rB = new double[newa.length+1];
        int []rbas = new int[newa.length+1];
        for(int i=0;i<newa.length;i++)
            for(int j=0;j<newa[0].length;j++){
                ra[i][j]=newa[i][j];
            }
        for(int i=1;i<z.length;i++)
            ra[newa.length][i-1]=z[i];
        for(int i=0;i<B.length;i++) {
            rbas[i] = bas[i];
            rB[i] = B[i];
        }
        rB[newa.length]=z[0];
        rbas[newa.length]=101;
        printTable(newC, ra, newL, rB, rbas,x);

        //Gamori(rB,newL,ra,rbas,x,newC);
    }
    static boolean optPlan(double L[]){//проверка оптимальности плана
        for(int i=1;i<L.length;i++){
            if(L[i]>0){
                return true;
            }
        }
        return false;
    }
    static int maxPositiveColumn (double L[]){//вычисление разрещающего столбца
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
    static int maxNegativeColumn (double L[]){//вычисление разрещающего столбца
        int NumberMinColumn = 1;
        double mincolumn = 0;
        for(int i=1;i<L.length;i++) {
            if ((L[i] < 0) && (L[i] < mincolumn)) {
                NumberMinColumn = i;
                mincolumn = L[i];
            }
        }
        //out.println(mincolumn);
        return NumberMinColumn;
    }
    static int otnRow(double [][]a, int maxNegativeColumn,double []B){//вычисление разрещающей строки
        int NumberMaxColumn = 0;
        double MaxColumn = Double.POSITIVE_INFINITY;
        for(int i=0;i<B.length;i++){
            if((B[i]/a[i][maxNegativeColumn-1]<MaxColumn)&&(a[i][maxNegativeColumn-1]>0)){
                NumberMaxColumn = i;
                MaxColumn=B[i]/a[i][maxNegativeColumn];
            }
        }
        return NumberMaxColumn;
    }
    static void simplexMethod(int row,int column,double [][]newa, double []B,int []bas,int []x){//сам симплекс метод
        int c = column-1;
        int q=bas[row];
        bas[row]=column;
        x[c]=q;
        double help;
        help = newa[row][c];
        //out.println(help);
        for (int i = 0; i < newa[0].length; i++) {
            //out.println(newa[row][i] + " / " + help);
            newa[row][i] /= help;

        }
        B[row] /= help;
        for (int i = 0; i < newa.length; i++) {
            help = newa[i][c];
            for (int j = 0; j < newa[0].length; j++) {
                if (i != row) {
                    //out.println(newa[i][j]+" - "+newa[it][j]+" * "+help);
                    newa[i][j] -= newa[row][j] * help;
                }
            }
            if (i != row) {
                B[i] -= B[row] * help;
            }
        }
    }
    static void printTable(double[] c, double[][] a,double L[],double B[],int []bas,int []x) {
        // вывод: строка коэффициентов
        for(int i=0;i<bas.length;i++){
            for(int j=0;j<x.length;j++){
                if(bas[i]==x[j])
                    x[j]=0;
            }
        }
        out.print("\t\t\t");
        for (int j = 0; j < c.length; j++) {
            if(x[j]!=0)
            out.print(c[j]+" \t");
        }
        out.println("\t");

        // вывод: ряд x
        out.print("Bas"+"\t");
        out.print("B\t\t");
        for (int j = 0; j < x.length; j++) {
            if(x[j]!=0)
            out.print("x[" + x[j] + "]\t");
        }
        out.println();
        for (int i = 0; i < a.length; i++) {

            out.print("x"+bas[i]+"\t");
            out.print(String.format("%.1f",B[i])+" \t");
            for (int j = 0; j < a[0].length; j++) {
                if(x[j]!=0)
                out.print(String.format("%.1f",a[i][j]) + " \t");
            }
            out.println();
        }
        out.println();
        out.print("L\t");
        out.print(String.format("%.1f",L[0]) + " \t");
        for (int i = 1; i < L.length-1; i++){
            if(x[i]!=0)
            out.print(String.format("%.1f",L[i]) + " \t");
        }
        out.println();
    }
    static void printTable(double[] c, double[][] a,double B[],int []bas,int []x) {
        // вывод: строка коэффициентов
        out.print("\t\t\t");
        for (int j = 0; j < c.length; j++) {
            if(x[j]!=0)
            out.print(c[j]+" \t");
        }
        out.println("\t");

        // вывод: ряд x
        out.print("Bas"+"\t");
        out.print("B\t\t");
        for (int j = 0; j < x.length; j++) {
            if(x[j]!=0)
            out.print("x[" + x[j] + "]\t");
        }
        out.println();
        for (int i = 0; i < a.length; i++) {

            out.print("x"+bas[i]+"\t");
            out.print(B[i]+" \t");
            for (int j = 0; j < a[0].length; j++) {
                if(x[j]!=0)
                out.print(a[i][j] + " \t");
            }
            out.println();
        }
        out.println();
    }
}
