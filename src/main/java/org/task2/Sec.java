package org.task2;

public final class Sec {
    private Sec() {}

    public static double sec(double x, double eps) {
        if (eps <= 0) {
            throw new IllegalArgumentException("eps must be > 0");
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }

        double twoPi = 2.0 * Math.PI;
        x = x % twoPi;

        double cos = cosBySeries(x, eps);

        if (Math.abs(cos) < 1e-12) {
            throw new IllegalArgumentException("sec(x) is undefined for cos(x)=0 (near pi/2 + k*pi)");
        }

        return 1.0 / cos;
    }

    public static double sec(double x) {
        return sec(x, 1e-12);
    }

    private static double cosBySeries(double x, double eps) {
        double sum = 1.0;
        double term = 1.0; // n=0
        int n = 0;

        int maxIters = 2000;

        while (maxIters-- > 0) {
            n++;
            term *= (-x * x) / ((2.0 * n - 1.0) * (2.0 * n));
            sum += term;

            if (Math.abs(term) < eps) {
                break;
            }
        }

        return sum;
    }
}
