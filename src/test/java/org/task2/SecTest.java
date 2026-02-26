package org.task2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SecTest {

    @Test
    void secAtZero_isOne() {
        assertEquals(1.0, Sec.sec(0.0), 1e-12);
    }

    @Test
    void secSmallX_matchesMathCosInverse() {
        double x = 0.1;
        double expected = 1.0 / Math.cos(x);
        assertEquals(expected, Sec.sec(x), 1e-10);
    }

    @Test
    void secIsEvenFunction() {
        double x = 0.7;
        assertEquals(Sec.sec(x), Sec.sec(-x), 1e-12);
    }

    @Test
    void secSeveralPoints_matchesOracle() {
        double[] xs = { -2.0, -1.0, -0.3, 0.3, 1.0, 2.0 };
        for (double x : xs) {
            double expected = 1.0 / Math.cos(x);
            assertEquals(expected, Sec.sec(x), 1e-9, "x=" + x);
        }
    }

    @Test
    void secIsPeriodicWith2Pi() {
        double x = 0.35;
        double x2 = x + 2.0 * Math.PI;
        assertEquals(Sec.sec(x), Sec.sec(x2), 1e-10);
    }

    @Test
    void secUndefinedAtPiOver2_throws() {
        assertThrows(IllegalArgumentException.class, () -> Sec.sec(Math.PI / 2.0));
    }

    @Test
    void secUndefinedNearPiOver2_throwsWhenCosTooSmall() {
        double x = (Math.PI / 2.0) - 1e-15;
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

    @Test
    void invalidEps_throws() {
        assertThrows(IllegalArgumentException.class, () -> Sec.sec(1.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> Sec.sec(1.0, -1e-6));
    }
}
