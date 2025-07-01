package ua.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Class, which contains all necessary methods to work with Number type.
 * Useful to write some logic, which will be applied to many different types of num */
class NumericCalc {

    private static final Logger logger = LoggerFactory.getLogger(NumericCalc.class);

    protected static Number calculateNumbers(Number a, Number b, char op) {
        if (!a.getClass().equals(b.getClass())) {
            String msg = String.format("Mismatched types: %s vs %s", a.getClass(), b.getClass());
            throw new IllegalArgumentException(msg);
        }

        Object result;
        try {
            result = switch (a) {
                case Byte x -> switch (op) {
                    case '+' -> (byte) (x + b.byteValue());
                    case '*' -> (byte) (x * b.byteValue());
                    default -> throw createUnsupportedOpException(op);
                };
                case Short x -> switch (op) {
                    case '+' -> (short) (x + b.shortValue());
                    case '*' -> (short) (x * b.shortValue());
                    default -> throw createUnsupportedOpException(op);
                };
                case Integer x -> switch (op) {
                    case '+' -> x + b.intValue();
                    case '*' -> x * b.intValue();
                    default -> throw createUnsupportedOpException(op);
                };
                case Long x -> switch (op) {
                    case '+' -> x + b.longValue();
                    case '*' -> x * b.longValue();
                    default -> throw createUnsupportedOpException(op);
                };
                case Float x -> switch (op) {
                    case '+' -> x + b.floatValue();
                    case '*' -> x * b.floatValue();
                    default -> throw createUnsupportedOpException(op);
                };
                case Double x -> switch (op) {
                    case '+' -> x + b.doubleValue();
                    case '*' -> x * b.doubleValue();
                    default -> throw createUnsupportedOpException(op);
                };
                default -> {
                    String msg = "Unsupported type: " + a.getClass();
                    throw new IllegalArgumentException(msg);
                }
            };
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error during calculation");
        }

        return (Number) result;
    }

    private static IllegalArgumentException createUnsupportedOpException(char op) {
        String msg = "Unsupported operation: " + op;
        IllegalArgumentException ex = new IllegalArgumentException(msg);
        logger.info(msg);
        logger.error(msg, ex);
        return ex;
    }

    // Multiplies two Numbers,
    // Returns the product of Object type (to be able to safe wrong results inside it (NaN, overflow)
    public static Object safeMultiply(Number a, Number b, String targetType) {
        if (a == null || b == null) {
            logger.warn("Null detected in multiplication: a = {}, b = {}", a, b);
            return null;
        }
        // Multiply in double
        double resultDouble = a.doubleValue() * b.doubleValue();

        // First check on Infinity
        if (Double.isInfinite(resultDouble)) {
            logger.warn("Infinity detected in multiplication: a = {}, b = {}, result = {}", a, b, resultDouble);
        }

        // NaN
        if (Double.isNaN(resultDouble)) {
            logger.warn("NaN detected in multiplication: a = {}, b = {}, result = {}", a, b, resultDouble);
        }

        // Boundaries for this TargetType
        double max;
        switch (targetType) {
            case "byte" -> max = Byte.MAX_VALUE;
            case "short" -> max = Short.MAX_VALUE;
            case "int" -> max = Integer.MAX_VALUE;
            case "long" -> max = Long.MAX_VALUE;
            case "float" -> max = Float.MAX_VALUE;
            case "double" -> max = Double.MAX_VALUE;
            default -> throw new IllegalArgumentException("Unsupported target type: " + targetType);
        }

        // Overflow
        if (resultDouble > max) {
            logger.warn("Overflow detected for {} result: a = {}, b = {}", targetType, a, b);
        }

        // Form Product → Even if there were warnings, Product put on the rules
        return getFinalProduct(targetType, resultDouble, max);
    }

    private static Object getFinalProduct(String targetType, double resultDouble, double max) {
        Object finalProduct;

        if (Double.isNaN(resultDouble)) {
            finalProduct = "NaN";
        } else if (resultDouble > max) {
            finalProduct = "Overflow";
//            logger.warn("Overflow detected for {} result: a = {}, b = {}", targetType, a, b);
        } else {
            // OK — формуємо по типу
            switch (targetType) {
                case "byte" -> finalProduct = (byte) resultDouble;
                case "short" -> finalProduct = (short) resultDouble;
                case "int" -> finalProduct = (int) resultDouble;
                case "long" -> finalProduct = (long) resultDouble;
                case "float" -> finalProduct = (float) resultDouble;
                case "double" -> finalProduct = resultDouble;
                default -> throw new IllegalArgumentException("Unsupported target type: " + targetType);
            }
        }
        return finalProduct;
    }
}
