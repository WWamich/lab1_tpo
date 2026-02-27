package org.task2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SecTest {

    private static final double PI = Math.PI;

    static Stream<double[]> secKnownPiPoints() {
        return Stream.of(
                new double[]{0.0, 1.0},
                new double[]{PI, -1.0},
                new double[]{2.0 * PI, 1.0},
                new double[]{-PI, -1.0},

                new double[]{PI / 6.0, 1.0 / Math.cos(PI / 6.0)},
                new double[]{PI / 4.0, 1.0 / Math.cos(PI / 4.0)},
                new double[]{PI / 3.0, 1.0 / Math.cos(PI / 3.0)},
                new double[]{5.0 * PI / 6.0, 1.0 / Math.cos(5.0 * PI / 6.0)},

                new double[]{-PI / 6.0, 1.0 / Math.cos(-PI / 6.0)},
                new double[]{-PI / 4.0, 1.0 / Math.cos(-PI / 4.0)},
                new double[]{-PI / 3.0, 1.0 / Math.cos(-PI / 3.0)}
        );
    }

    @ParameterizedTest(name = "sec({0}) matches 1/cos(x)")
    @MethodSource("secKnownPiPoints")
    void secAtPiBasedPoints_matchesMathCosInverse(double[] data) {
        double x = data[0];
        double expected = data[1];
        assertEquals(expected, Sec.sec(x), 1e-10, "x=" + x);
    }

    static Stream<Double> secSeveralPoints() {
        return Stream.of(
                -2.0, -1.0, -0.3, 0.3, 1.0, 2.0,
                0.1, 0.7, 1.3,
                PI / 10.0, PI / 7.0, 3.0 * PI / 8.0
        );
    }

    @ParameterizedTest(name = "sec({0}) ~= 1/cos({0})")
    @MethodSource("secSeveralPoints")
    void secSeveralPoints_matchesMathCosInverse(double x) {
        double expected = 1.0 / Math.cos(x);
        assertEquals(expected, Sec.sec(x), 1e-9, "x=" + x);
    }

    @ParameterizedTest(name = "sec is even: x={0}")
    @ValueSource(doubles = {0.1, 0.7, 1.3, Math.PI / 6.0, Math.PI / 4.0, 2.0})
    void secIsEvenFunction(double x) {
        assertEquals(Sec.sec(x), Sec.sec(-x), 1e-12, "x=" + x);
    }

    static Stream<double[]> periodicCases() {
        return Stream.of(
                new double[]{0.35, 1.0},
                new double[]{0.35, -1.0},
                new double[]{PI / 6.0, 1.0},
                new double[]{PI / 6.0, 2.0},
                new double[]{-1.2, 3.0}
        );
    }

    @ParameterizedTest(name = "sec periodic: x={0}, k={1}")
    @MethodSource("periodicCases")
    void secIsPeriodicWith2Pi(double[] data) {
        double x = data[0];
        double k = data[1];
        double x2 = x + 2.0 * PI * k;

        assertEquals(Sec.sec(x), Sec.sec(x2), 1e-10, "x=" + x + ", k=" + k);
    }

    static Stream<Double> undefinedPoints() {
        return Stream.of(
                PI / 2.0,
                3.0 * PI / 2.0,
                -PI / 2.0,
                -3.0 * PI / 2.0
        );
    }

    @ParameterizedTest(name = "sec undefined at x={0} throws")
    @MethodSource("undefinedPoints")
    void secUndefinedAtOddPiOver2_throws(double x) {
        assertThrows(IllegalArgumentException.class, () -> Sec.sec(x), "x=" + x);
    }

    @Test
    void secUndefinedNearPiOver2_throwsWhenCosTooSmall() {
        double x = (PI / 2.0) - 1e-15;
        assertThrows(IllegalArgumentException.class, () -> Sec.sec(x));
    }

    @Test
    void smallerEps_givesBetterAccuracy() {
        double x = 1.3;
        double expected = 1.0 / Math.cos(x);

        double rough = Sec.sec(x, 1e-4);
        double fine  = Sec.sec(x, 1e-12);

        double errRough = Math.abs(rough - expected);
        double errFine  = Math.abs(fine - expected);

        assertTrue(errFine <= errRough, "fine eps should not be worse than rough eps");
    }

    @ParameterizedTest(name = "invalid eps={0} throws")
    @ValueSource(doubles = {0.0, -1e-6, -1.0})
    void invalidEps_throws(double eps) {
        assertThrows(IllegalArgumentException.class, () -> Sec.sec(1.0, eps));
    }
}
