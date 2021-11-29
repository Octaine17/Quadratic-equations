package com.company;

import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {

    enum FuncType {
        NoException, Normal, FullException
    }


    public static class ThrowableVector extends Throwable {
        Vector<Double> vector = new Vector<Double>();
    }

    static boolean isEqual(double a, double b) {
        double eps = 0.00001;
        return Math.abs(a - b) < eps;
    }

    static Vector<Double> solve_correct_equation(double a, double b, double c) {
        assert (!(isEqual(a, 0) && isEqual(b, 0) && isEqual(c, 0)));
        if (isEqual(a, 0) && isEqual(b, 0)) {
            return new Vector<Double>(0);
        }

        if (isEqual(a, 0)) {
            Vector<Double> vec = new Vector<Double>(1);
            vec.add(-c / b);
            return vec;
        }

        double discriminant = (b * b) - (4 * a * c);
        if (isEqual(discriminant, 0)) {
            Vector<Double> vec = new Vector<Double>(1);
            vec.add(-b / (2 * a));
            return vec;
        }
        if (discriminant < 0) {
            return new Vector<Double>(0);
        }
        Vector<Double> vec = new Vector<Double>(2);
        vec.add((-b + Math.sqrt(discriminant)) / (2 * a));
        vec.add((-b - Math.sqrt(discriminant)) / (2 * a));
        return vec;
    }

    ;


    static Object solve_no_exceptions(double a, double b, double c, boolean ok) {
        ok = true;
        if (isEqual(a, 0) && isEqual(b, 0) && isEqual(c, 0)) {
            ok = false;
            return new Vector<Double>();
        }
        return new Pair<>(ok, solve_correct_equation(a, b, c));
    }

    static double call_solver(FuncType type, double a, double b, double c) {
        switch (type) {
            case Normal:
                return roots_sum(a, b, c);
            case NoException:
                return roots_sum_no_exception(a, b, c);
            case FullException:
                return roots_sum_full_exception(a, b, c);
            default:
                return 0;
        }
    }

    static double roots_sum(double a, double b, double c) {
        try {
            Vector<Double> roots = solve(a, b, c);
            return sum_vec(roots);
        } catch (RuntimeException runtimeException) {
            return 0;
        }
    }

    static Vector<Double> solve(double a, double b, double c) {
        if (isEqual(a, 0) && isEqual(b, 0) && isEqual(c, 0)) {
            throw new RuntimeException("root is any value");
        }
        return solve_correct_equation(a, b, c);
    }

    static double roots_sum_no_exception(double a, double b, double c) {
        Pair pair = (Pair) solve_no_exceptions(a, b, c, true);
        if (true == (boolean) pair.getKey()) {
            return sum_vec((Vector<Double>) pair.getValue());
        }
        return 0;
    }

    static double sum_vec(Vector<Double> roots) {
        double sum = 0;
        for (int i = 0; i < roots.size(); i++) {
            sum += roots.elementAt(i);
        }
        return sum;
    }

    public static double sum_vec(ThrowableVector roots) {
        double sum = 0;
        for (int i = 0; i < roots.vector.size(); i++) {
            sum += roots.vector.elementAt(i);
        }
        return sum;
    }

    static double roots_sum_full_exception(double a, double b, double c) {
        try {
            solve_full_exception(a, b, c);
        } catch (RuntimeException s) {
            return 0;
        } catch (ThrowableVector vector) {
            return sum_vec(vector);
        }
        throw new RuntimeException("wrong exception");
    }

    static void solve_full_exception(double a, double b, double c) throws ThrowableVector {
        if (isEqual(a, 0) && isEqual(b, 0) && isEqual(c, 0)) {
            throw new RuntimeException("root is any value");
        }
        ThrowableVector vec = new ThrowableVector();
        vec.vector = (solve_correct_equation(a, b, c));
        throw vec;
    }


    public static void main(String[] args) {
        Vector<Double> vec = new Vector<Double>();
        final int from = 4096;
        final int to = from * 512;
        for (long i = from; i <= to; i *= 2) {
            run(i, FuncType.NoException);
        }
        for (long i = from; i <= to; i *= 2) {
            run(i, FuncType.Normal);
        }
        for (long i = from; i <= to; i *= 2) {
            run(i, FuncType.FullException);
        }
    }

    static void run(long n, FuncType type) {
        long time = System.nanoTime();
        double sum = 0;
        for (long i = 0; i < n; i++) {
            double a = ((i % 2000) - 1000) / 33.0;
            double b = ((i % 200) - 100) / 22.0;
            double c = ((i % 20) - 10) / 11.0;
            sum += call_solver(type, a, b, c);
        }


//        ExecutorService executor = Executors.newFixedThreadPool(4);
//        double[] sum_ = new double[4];
//        sum_[0] = 0;
//        sum_[1] = 0;
//        sum_[2] = 0;
//        sum_[3] = 0;
//        executor.submit(() -> {
//            for (long i = 0; i < n; i++) {
//                double a = ((i % 2000) - 1000) / 33.0;
//                double b = ((i % 200) - 100) / 22.0;
//                double c = ((i % 20) - 10) / 11.0;
//                sum_[0] += call_solver(type, a, b, c);
//                sum_[1] += call_solver(type, a, b, c);
//                sum_[2] += call_solver(type, a, b, c);
//                sum_[3] += call_solver(type, a, b, c);
//            }
//            executor.shutdown();
//        });
//        for (int i = 0; i<4;i++){
//            sum += sum_[i];
//        }
        time = System.nanoTime() - time;
        System.out.printf("Elapsed %,9.3f ms\n", time / 1_000_000.0);
    }
}
