package com.company;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {

    public static class MyVector extends Throwable {
            double num[];
            MyVector(Double a, Double b){
                num[0] = a;
                num[1] = b;
            }
        };

    static boolean isEqual(double a, double b){
        if (a == b){
            return true;
        }
        else return false;
    }

    static Vector<Double> calc(double a, double b, double c, boolean ok)
    {
        ok = true;
        if  (isEqual(a,0))
        {
            if (isEqual(b,0))
            {
                if (isEqual(c,0))
                {
                    ok = false;
                    return new Vector<Double>();
                }
                else return new Vector<Double>();
            }
            else{
                Vector<Double> vec = new Vector<Double>(1);
                vec.add(0,-c/b);
                return vec;
            }

        }
        double disc = (b*b) - (4*a*c);

        if (isEqual(disc,0))
        {
            Vector<Double> vec = new Vector<Double>(1);
            vec.add(0,(-b)/(2*a));
            return vec;

        }
        if (disc<0)
        {
            return new Vector<Double>();
        }
        else
        {
            Vector<Double> vec = new Vector<Double>(2);
            vec.add((-b+Math.sqrt(disc))/(2*a));
            vec.add((-b-Math.sqrt(disc))/(2*a));
            System.out.println(vec);
            return vec;
        }
    }

    static Vector<Double> exceptCalc(double a, double b, double c) throws Exception{
        if  (isEqual(a,0))
        {
            if (isEqual(b,0))
            {
                if (isEqual(c,0))
                {
                    throw new Exception("Invalid Parametrs");
                }
                else return new Vector<Double>();
            }
            else{
                Vector<Double> vec = new Vector<Double>(1);
                vec.add(0,-c/b);
                return vec;
            }

        }
        double disc = (b*b) - (4*a*c);

        if (isEqual(disc,0))
        {
            Vector<Double> vec = new Vector<Double>(1);
            vec.add(0,(-b)/(2*a));
            return vec;

        }
        if (disc<0)
        {
            return new Vector<Double>();
        }
        else
        {
            Vector<Double> vec = new Vector<Double>(2);
            vec.add((-b+Math.sqrt(disc))/(2*a));
            vec.add((-b-Math.sqrt(disc))/(2*a));
            return vec;
        }
    }

    static void wrongCalc(double a,double b,double c) throws Throwable {
        if  (isEqual(a,0))
        {
            if (isEqual(b,0))
            {
                if (isEqual(c,0))
                {
                    throw new Exception("Invalid Parametrs");
                }
                else throw new MyVector(null,null);
            }
            else throw new MyVector(-c/b,null);
        }
        double disc = (b*b) - (4*a*c);
        if (isEqual(disc,0))
        {
            throw new MyVector((-b)/(2*a),null);
        }
        if (disc<0)
        {
            throw new MyVector(null,null);
        }
        else
        {

                throw new MyVector((-b+Math.sqrt(disc))/(2*a),(-b-Math.sqrt(disc))/(2*a));
        }
        }


    public static void main(String[] args) {

        long time = System.nanoTime();
        run(2056);
        time = System.nanoTime() - time;
        System.out.printf("Elapsed %,9.3f ms\n", time/1_000_000.0);
    }

    public static void run(int n) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            for (int i = 0; i < n; i++) {
                calculate();
            }
        });
        executor.shutdown();
    }

    public static void calculate() {
        Random random = new Random();
        Vector<Double> a = calc(random.nextDouble(),random.nextDouble(),random.nextDouble(), true);
    }
}