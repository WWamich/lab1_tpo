package org.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OpenHashTableTest {

    private OpenHashTable hashTable;

    @BeforeEach
    public void setup() {
        hashTable = new OpenHashTable(10);
    }

    private void seed(int... keys) {
        for (int k : keys) {
            hashTable.put(k);
        }
    }

    static Stream<Arguments> putCases() {
        return Stream.of(
                Arguments.of(new int[]{}, 5,
                        List.of("PUT_START", "CALC_HASH", "INSERT_NEW")),

                Arguments.of(new int[]{5}, 15,
                        List.of("PUT_START", "CALC_HASH", "ITERATE_CHAIN", "INSERT_NEW")),

                Arguments.of(new int[]{25}, 25,
                        List.of("PUT_START", "CALC_HASH", "ITERATE_CHAIN", "FOUND_DUPLICATE")),

                Arguments.of(new int[]{}, -15,
                        List.of("PUT_START", "CALC_HASH", "INSERT_NEW"))
        );
    }

    @ParameterizedTest(name = "put key={1}, initial={0}")
    @MethodSource("putCases")
    void testPut_ExecutionPath(int[] initialKeys, int keyToPut, List<String> expectedTrace) {
        seed(initialKeys);
        hashTable.clearTrace();

        hashTable.put(keyToPut);

        assertEquals(expectedTrace, hashTable.getTrace());
        assertTrue(hashTable.contains(keyToPut));
    }

    static Stream<Arguments> containsCases() {
        return Stream.of(
                Arguments.of(new int[]{13, 23, 33}, 13, true,
                        List.of("SEARCH_START", "CALC_HASH",
                                "ITERATE_CHAIN", "ITERATE_CHAIN", "ITERATE_CHAIN",
                                "FOUND_TRUE")),

                Arguments.of(new int[]{5, 15}, 25, false,
                        List.of("SEARCH_START", "CALC_HASH",
                                "ITERATE_CHAIN", "ITERATE_CHAIN",
                                "FOUND_FALSE"))
        );
    }

    @Test
    public void testPut_EmptyBucket_ExecutionPath() {
        hashTable.clearTrace();
        hashTable.put(5);

        List<String> expectedTrace = Arrays.asList(
                "PUT_START", "CALC_HASH", "INSERT_NEW"
        );
        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @ParameterizedTest(name = "contains key={1}, expected={2}, initial={0}")
    @MethodSource("containsCases")
    void testContains_ExecutionPath(int[] initialKeys, int searchKey, boolean expectedResult, List<String> expectedTrace) {
        seed(initialKeys);
        hashTable.clearTrace();

        boolean result = hashTable.contains(searchKey);

        assertEquals(expectedResult, result);
        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @Test
    public void testPut_WithCollision_ExecutionPath() {
        hashTable.put(5);
        hashTable.clearTrace();

        hashTable.put(15);

        List<String> expectedTrace = Arrays.asList(
                "PUT_START", "CALC_HASH", "ITERATE_CHAIN", "INSERT_NEW"
        );
        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @Test
    public void testPut_DuplicateKey_ExecutionPath() {
        hashTable.put(25);
        hashTable.clearTrace();

        hashTable.put(25);

        List<String> expectedTrace = Arrays.asList(
                "PUT_START", "CALC_HASH", "ITERATE_CHAIN", "FOUND_DUPLICATE"
        );
        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @Test
    public void testContains_KeyFound_ExecutionPath() {
        hashTable.put(13);
        hashTable.put(23);
        hashTable.put(33);
        hashTable.clearTrace();

        boolean result = hashTable.contains(13);

        List<String> expectedTrace = Arrays.asList(
                "SEARCH_START",
                "CALC_HASH",
                "ITERATE_CHAIN",
                "ITERATE_CHAIN",
                "ITERATE_CHAIN",
                "FOUND_TRUE"
        );

        assertTrue(result);
        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @Test
    public void testRemove_RemoveHead_ExecutionPath() {
        hashTable.put(10);
        hashTable.put(20);
        hashTable.clearTrace();

        hashTable.remove(20);

        List<String> expectedTrace = Arrays.asList(
                "REMOVE_START",
                "CALC_HASH",
                "ITERATE_CHAIN",
                "REMOVE_FOUND",
                "REMOVE_HEAD"
        );

        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @Test
    public void testRemove_RemoveInner_ExecutionPath() {
        hashTable.put(10);
        hashTable.put(20);
        hashTable.put(30);
        hashTable.clearTrace();

        hashTable.remove(20);

        List<String> expectedTrace = Arrays.asList(
                "REMOVE_START",
                "CALC_HASH",
                "ITERATE_CHAIN",
                "ITERATE_CHAIN",
                "REMOVE_FOUND",
                "REMOVE_INNER"
        );

        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @Test
    public void testRemove_NotFound_ExecutionPath() {
        hashTable.put(2);
        hashTable.clearTrace();

        hashTable.remove(42);

        List<String> expectedTrace = Arrays.asList(
                "REMOVE_START",
                "CALC_HASH",
                "ITERATE_CHAIN",
                "REMOVE_NOT_FOUND"
        );

        assertEquals(expectedTrace, hashTable.getTrace());
    }
    @Test
    public void testContains_KeyNotFound_ExecutionPath() {
        hashTable.put(5);
        hashTable.put(15);
        hashTable.clearTrace();

        boolean result = hashTable.contains(25);

        List<String> expectedTrace = Arrays.asList(
                "SEARCH_START",
                "CALC_HASH",
                "ITERATE_CHAIN",
                "ITERATE_CHAIN",
                "FOUND_FALSE"
        );

        assertFalse(result);
        assertEquals(expectedTrace, hashTable.getTrace());
    }

    @Test
    public void testNegativeKey_EquivalenceClass_ExecutionPath() {
        hashTable.clearTrace();

        hashTable.put(-15);

        List<String> expectedTrace = Arrays.asList(
                "PUT_START", "CALC_HASH", "INSERT_NEW"
        );
        assertEquals(expectedTrace, hashTable.getTrace());
        assertTrue(hashTable.contains(-15));
    }
}