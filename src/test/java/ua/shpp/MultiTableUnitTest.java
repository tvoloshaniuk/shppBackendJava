package ua.shpp;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//.
class MultiTableUnitTest {

    @Nested
    class CountSourceRowInvalidInputTest {

        @Test
        void minGreaterThanMaxThrows() {

            assertThrows(IllegalArgumentException.class, () -> MultiTable.countSourceRow(10, 5, 1, "int"));
        }

        @Test
        void incrementIsZeroReturnsOnlyMin() {
            Number[] result = MultiTable.countSourceRow(0, 100, 0, "int");
            assertArrayEquals(new Number[]{0}, result);
        }

        @Test
        void incrementGreaterThanRangeReturnsOnlyMin() {
            Number[] result = MultiTable.countSourceRow(0, 10, 20, "int");
            assertArrayEquals(new Number[]{0}, result);
        }

        @Test
        void negativeIncrementThrows() {
            assertThrows(IllegalArgumentException.class, () -> MultiTable.countSourceRow(0, 10, -1, "int"));
        }

        @Test
        void minEqualsMaxReturnsOnlyMin() {
            Number[] result = MultiTable.countSourceRow(5, 5, 1, "int");
            assertArrayEquals(new Number[]{5}, result);
        }

        @Test
        void wrongTypeFallbackToInt() {
            Number[] result = MultiTable.countSourceRow(1.5, 3.5, 1.0, "unknownType");
            assertArrayEquals(new Number[]{1, 2, 3}, result);
        }

        @Test
        void wrongTypeWithInvalidRangeStillThrows() {
            assertThrows(IllegalArgumentException.class, () -> MultiTable.countSourceRow(5, 1, 1, "invalidType"));
        }

        @Test
        void wrongTypeWithIncrementZeroReturnsOnlyMin() {
            Number[] result = MultiTable.countSourceRow(10, 20, 0, "invalidType");
            assertArrayEquals(new Number[]{10}, result);
        }
    }


    @Nested
    class CountResultEntriesTest {

        @Test
        void basicTest() {
            List<MultiplicationEntry> expected = List.of(
                    new MultiplicationEntry(11, 11, 121),
                    new MultiplicationEntry(11, 12, 132),
                    new MultiplicationEntry(11, 13, 143),
                    new MultiplicationEntry(12, 12, 144),
                    new MultiplicationEntry(12, 13, 156),
                    new MultiplicationEntry(13, 13, 169)
            );
            List<MultiplicationEntry> actual = MultiTable.countResultEntries(
                    new Number[]{11, 12, 13}, "int"
            );
            assertEquals(expected.size(), actual.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i).product, actual.get(i).product);
            }
//            assertArrayEquals;
        }


        @Disabled
        @Test
        void floatValuesTest() {
            Number[] input = new Number[]{1.2f, 1.5f, 2.0f};
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "float");

            float[] actual = new float[result.size()];
            for (int i = 0; i < result.size(); i++) {
                actual[i] = ((Number) result.get(i).product).floatValue();
            }

            float[] expected = {1.44f, 2.25f, 4.0f};
            assertEquals(expected.length, actual.length);
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], actual[i], 1e-6f);
            }
        }


        @Test
        void emptyInputTest() {
            List<MultiplicationEntry> result = MultiTable.countResultEntries(new Number[]{}, "int");
            assertTrue(result.isEmpty());
        }

        @Test
        void singleElementTest() {
            List<MultiplicationEntry> result = MultiTable.countResultEntries(new Number[]{5}, "int");
            assertEquals(1, result.size());
            assertEquals(25, result.getFirst().product);
        }

        @Test
        void nullInputTest() {
            assertThrows(NullPointerException.class, () -> MultiTable.countResultEntries(null, "int"));
        }
    }

    @Nested
    class OverflowAndSpecialCasesTest {

        @Test
        void byteOverflowTest() {
            Number[] input = new Number[]{200}; // 200 > Byte.MAX_VALUE (127)
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "byte");

            assertEquals(1, result.size());
            Object product = result.getFirst().product;
            assertEquals("Overflow", product);
        }

        @Test
        void intOverflowTest() {
            Number[] input = new Number[]{Integer.MAX_VALUE};
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "int");

            assertEquals(1, result.size());
            Object product = result.getFirst().product;
            assertEquals("Overflow", product);

            //todo додати декілька біля самої границі ситуацій
        }

        @Test
        void doubleInfinityPositiveTest() {
            Number[] input = new Number[]{Double.MAX_VALUE};
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "double");

            assertEquals(1, result.size());
            Object product = result.getFirst().product;
            assertEquals("Overflow", product);
        }

        @Test
        void doubleInfinityNegativeTest() {
            Number[] input = new Number[]{-Double.MAX_VALUE};
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "double");

            assertEquals(1, result.size());
            Object product = result.getFirst().product;
            assertEquals("Overflow", product); // (-MAX * -MAX = +∞)
        }

        @Test
        void doubleNaNTest() {
            Number[] input = new Number[]{Double.NaN};
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "double");

            assertEquals(1, result.size());
            Object product = result.getFirst().product;
            assertEquals("NaN", product);
        }

        @Test
        void floatInfinityTest() {
            Number[] input = new Number[]{Float.MAX_VALUE};
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "float");

            assertEquals(1, result.size());
            Object product = result.getFirst().product;
            assertEquals("Overflow", product);
        }

        @Test
        void floatNaNTest() {
            Number[] input = new Number[]{Float.NaN};
            List<MultiplicationEntry> result = MultiTable.countResultEntries(input, "float");

            assertEquals(1, result.size());
            Object product = result.getFirst().product;
            assertEquals("NaN", product);
        }
    }

    @Nested
    class InvalidInputConstructorTest {

        @Test
        void minGreaterThanMaxShouldThrow() {
            assertThrows(IllegalArgumentException.class, () -> {
                new MultiTable(10, 5, 1); // min > max
            });
        }

        @Test
        void incrementIsZeroShouldNotThrowButReturnSingleMin() {
            MultiTable table = new MultiTable(0, 10, 0);
            Number[] row = table.getSourceRow();
            assertEquals(1, row.length);
            assertEquals(0, row[0]);
        }

        @Test
        void incrementGreaterThanRangeShouldReturnSingleMin() {
            MultiTable table = new MultiTable(0, 10, 15);
            Number[] row = table.getSourceRow();
            assertEquals(1, row.length);
            assertEquals(0, row[0]);
        }

        @Test
        void negativeIncrementShouldThrow() {
            assertThrows(IllegalArgumentException.class, () -> new MultiTable(0, 10, -1));
        }
    }

    @Nested
    class AppTest {

        @Test
        void runWithInvalidFileShouldThrowCriticalResourceException() {
            assertThrows(CriticalResourceException.class, () -> App.run("non_existing_file.properties"));
        }
    }

    @Nested
    class NumericCalcUnitTest {

        @Test
        void mismatchedTypesShouldThrow() {
            Number a = 5; // Integer
            Number b = 5.0; // Double
            assertThrows(IllegalArgumentException.class, () ->
                    NumericCalc.calculateNumbers(a, b, '+')
            );
        }

        @Test
        void unsupportedOperationShouldThrow() {
            Number a = 10;
            Number b = 20;
            assertThrows(IllegalArgumentException.class, () ->
                    NumericCalc.calculateNumbers(a, b, '-') // only '+' and '*' are supported
            );
        }

        @Test
        void unsupportedTypeShouldThrow() {

            Number a = new Number() {
                @Override
                public int intValue() {
                    return 0;
                }

                @Override
                public long longValue() {
                    return 0;
                }

                @Override
                public float floatValue() {
                    return 0;
                }

                @Override
                public double doubleValue() {
                    return 0;
                }
            };
            assertThrows(IllegalArgumentException.class, () ->
                    NumericCalc.calculateNumbers(a, a, '+')
            );
        }


        @Nested
        class SafeMultiplyTest {
            @Test
            void nullOperandReturnsNull() {
                assertNull(NumericCalc.safeMultiply(null, 5, "int"));
                assertNull(NumericCalc.safeMultiply(5, null, "int"));
            }

            @Test
            void unsupportedTargetTypeThrows() {
                assertThrows(IllegalArgumentException.class, () ->
                        NumericCalc.safeMultiply(5, 5, "complex")
                );
            }
        }
    }

    @Nested
    class PropertyProcessorTest {
        @Disabled
        @Test
        void parseStringToType_unsupportedType_throwsCriticalResourceException() {
            String value = "123";
            String type = "unsupported";
            CriticalResourceException ex = assertThrows(CriticalResourceException.class,
                    () -> PropertyProcessor.parseStringToType(value, type));
            assertTrue(ex.getMessage().contains("Failed to convert"));
        }

        @Test
        void castToType_unsupportedType_throwsIllegalArgumentException() {
            Number val = 42;
            String type = "char";
            assertThrows(IllegalArgumentException.class,
                    () -> PropertyProcessor.castToType(val, type));
        }

        @Test
        void checkTypeProperty_invalidType_returnsInt() {
            String fixedType = PropertyProcessor.checkTypeProperty("char");
            assertEquals("int", fixedType);
        }


        @Disabled
        @Test
        void getNumericProperty_missingKey_throwsException() {
            PropertyProcessor pp = new PropertyProcessor("test.properties");
            assertThrows(CriticalResourceException.class,
                    () -> pp.getNumericProperty("nonexistentKey", "int"));
        }

        @Disabled
        @Test
        void getNumericProperty_wrongValueFormat_throwsException() {
            PropertyProcessor pp = new PropertyProcessor("illegalArguments.properties");

            assertThrows(IllegalArgumentException.class,
                    () -> pp.getNumericProperty("badInt", "int"));
        }

        @Test
        void isSupportedType_validation() {
            assertTrue(PropertyProcessor.isSupportedType("int"));
            assertTrue(PropertyProcessor.isSupportedType("FLOAT"));
            assertFalse(PropertyProcessor.isSupportedType("BigDecimal"));
        }


    }

    @Test
    void checkLongInDouble() {
        Number a = Double.valueOf(Long.MAX_VALUE);
//        System.out.println(a);
        assertTrue(a.doubleValue() + 1100.0 > Long.MAX_VALUE);
//        double num = a.doubleValue();
//        while (num < Long.MAX_VALUE) {
//            num += 1.0;
//        }
//        assertEquals(a, a.doubleValue() + 1);
    }

}

//todo test in naming in end