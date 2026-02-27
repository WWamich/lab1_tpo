package org.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenHashTableTest {

    private OpenHashTable<Integer, String> map;

    @BeforeEach
    public void setup() {
        map = new OpenHashTable<>(10);
        map.clearTrace();
    }

    private void seed(Integer[] keys, String[] values) {
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        map.clearTrace();
    }

    static Stream<Arguments> putProvider() {
        return Stream.of(
                Arguments.of(new Integer[]{}, new String[]{}, 10, "Val1", List.of("PUT_START", "CALC_HASH", "INSERT_NEW")),
                Arguments.of(new Integer[]{10}, new String[]{"Val1"}, 10, "Val2", List.of("PUT_START", "CALC_HASH", "ITERATE_CHAIN", "REPLACE_VALUE")),
                Arguments.of(new Integer[]{10}, new String[]{"Val1"}, 20, "Val2", List.of("PUT_START", "CALC_HASH", "ITERATE_CHAIN", "INSERT_NEW"))
        );
    }

    @ParameterizedTest
    @MethodSource("putProvider")
    void testPut(Integer[] initialKeys, String[] initialValues, Integer key, String value, List<String> expected) {
        seed(initialKeys, initialValues);
        map.put(key, value);
        assertEquals(expected, map.getTrace());
    }

    static Stream<Arguments> getProvider() {
        return Stream.of(
                Arguments.of(new Integer[]{10, 20, 30}, new String[]{"V1", "V2", "V3"}, 30, "V3", List.of("GET_START", "CALC_HASH", "ITERATE_CHAIN", "FOUND_KEY")),
                Arguments.of(new Integer[]{10, 20, 30}, new String[]{"V1", "V2", "V3"}, 10, "V1", List.of("GET_START", "CALC_HASH", "ITERATE_CHAIN", "ITERATE_CHAIN", "ITERATE_CHAIN", "FOUND_KEY")),
                Arguments.of(new Integer[]{10}, new String[]{"V1"}, 99, null, List.of("GET_START", "CALC_HASH", "NOT_FOUND"))
        );
    }

    @ParameterizedTest
    @MethodSource("getProvider")
    void testGet(Integer[] initialKeys, String[] initialValues, Integer key, String expectedValue, List<String> expectedTrace) {
        seed(initialKeys, initialValues);
        String result = map.get(key);
        assertEquals(expectedValue, result);
        assertEquals(expectedTrace, map.getTrace());
    }

    static Stream<Arguments> removeProvider() {
        return Stream.of(
                Arguments.of(new Integer[]{10, 20}, new String[]{"V1", "V2"}, 20, List.of("REMOVE_START", "CALC_HASH", "ITERATE_CHAIN", "REMOVE_FOUND", "REMOVE_HEAD")),
                Arguments.of(new Integer[]{10, 20}, new String[]{"V1", "V2"}, 10, List.of("REMOVE_START", "CALC_HASH", "ITERATE_CHAIN", "ITERATE_CHAIN", "REMOVE_FOUND", "REMOVE_INNER")),
                Arguments.of(new Integer[]{10}, new String[]{"V1"}, 15, List.of("REMOVE_START", "CALC_HASH", "REMOVE_NOT_FOUND")),
                Arguments.of(new Integer[]{10}, new String[]{"V1"}, 20, List.of("REMOVE_START", "CALC_HASH", "ITERATE_CHAIN", "REMOVE_NOT_FOUND"))
        );
    }

    @ParameterizedTest
    @MethodSource("removeProvider")
    void testRemove(Integer[] initialKeys, String[] initialValues, Integer key, List<String> expectedTrace) {
        seed(initialKeys, initialValues);
        map.remove(key);
        assertEquals(expectedTrace, map.getTrace());
    }
}