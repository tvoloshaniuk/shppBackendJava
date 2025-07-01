package ua.shpp;

import java.util.LinkedList;
import java.util.List;

import static ua.shpp.NumericCalc.calculateNumbers;
import static ua.shpp.PropertyProcessor.*;

/**
 * The MultiTable class encapsulates all the logic needed to generate
 * and represent a multiplication table for a configurable numeric range.
 * <p>
 * Core responsibilities:
 * - Accepts min, max, increment, and a target numeric type (e.g., int, float).
 * - Automatically generates a row of source numbers based on these parameters.
 * - Computes the corresponding multiplication results into an array of objects
 * containing three fields: multiplier, multiplied, and product.
 * <p>
 * Internally stores:
 * - sourceColumn — an array of base numbers (a single row), generated from the given min, max, and increment.
 * - resultColumn — a 2D structure (via MultiplicationEntry) storing all results
 * as objects with three fields: Number multiplier, Number multiplied, and Object product.
 * The product field can represent special cases like overflow, NaN, Infinity, etc.
 * <p>
 * Additional features:
 * - Can be instantiated either manually or by reading from a .properties file. (several constructors)
 * - Provides accessor methods to retrieve configuration and result data.
 * - Designed to be easily used across the application to access
 * rows, specific results, or print the entire table via drawTable().
 * - Supports multiple constructors, including one that accepts a properties file name.
 * <p>
 * The actual multiplication logic and all error handling (overflow, underflow, etc.)
 * are delegated to utility methods and classes, allowing this class to remain focused and clean.
 */

public class MultiTable {
    /* Input fields: */
    private Number min;
    private Number max;
    private Number increment;
    private String type;

    /* Result fields */
    private Number[] sourceRow; //The "entrance row" from which multiplication will be built.
    private List<MultiplicationEntry> resultEntries; // objects with 3 fields (multiplier, multiplied, Object product)


    /* Constructors: */
    /* Main case constructor - get filename and using PropertyProcessor object - search file in classpath and parse */
    MultiTable(String filename) {
        parseDataFromFileAndSystem(filename);
        countTableColumns();
    }
    /* Alternate case constructors
        (needed for ex for lazy tests without JUnit mocks) */
    // Alternate case constructor - put all data manually as const params
    MultiTable(Number min, Number max, Number increment, String type) {
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.type = type;
        countTableColumns();
    }
    // Alternate case constructor - overloading without type -- will be "int" as default
    MultiTable(Number min, Number max, Number increment) {
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.type = "int";
        countTableColumns();
    }

    /* Getters */
    public Number[] getSourceRow() {
        return sourceRow;
    }

    public List<MultiplicationEntry> getResultEntries() {
        return resultEntries;
    }

    public Number getMin() {
        return min;
    }
    public Number getMax() {
        return max;
    }
    public Number getIncrement() {
        return increment;
    }
    public String getType() {
        return type;
    }


    public void parseDataFromFileAndSystem(String filename) {
        PropertyProcessor propertyProcessor = new PropertyProcessor(filename);
        type = propertyProcessor.getTypePropertyOrDefault();
        min = propertyProcessor.getNumericProperty("min", type);
        max = propertyProcessor.getNumericProperty("max", type);
        increment = propertyProcessor.getNumericProperty("increment", type);
    }

    //fills in sourceRow and resultEntries
    public void countTableColumns() {
        logger.info("Try to start countSourceRow()...");
        sourceRow = countSourceRow(min, max, increment, type);
        logger.info("countSourceRow() counted successfully!...");

        logger.info("Try to start countResultEntries()...");
        resultEntries = countResultEntries(sourceRow, type);
        logger.info("countResultEntries() counted successfully!...");
    }

    //First and last elements -- are always "min" and "max". Values between - are calculated using "increment" variable
    //return: values will be of type Number, but cast to type according to 'type' variable
    public static Number[] countSourceRow(Number min, Number max, Number increment, String type) {
        logger.info("Try to validate inputs...");
        /* input validation */
        type = checkTypeProperty(type);
        min = castToType(min, type);
        max = castToType(max, type);
        increment = castToType(increment, type);
        checkInputLogicMistakes(min, max, increment);

        if (increment.doubleValue() == 0 || increment.doubleValue() >  max.doubleValue() - min.doubleValue()) {
            return new Number[]{min}; //return arr with only one element -- min
        }

        logger.info("Try to count SourceRow()...");

        LinkedList<Number> result = new LinkedList<>();
        Number currentNum = min;
        while (currentNum.doubleValue() < max.doubleValue()) {
            result.add(currentNum);
            currentNum = calculateNumbers(currentNum, increment, '+');

            if (currentNum.doubleValue() < min.doubleValue()) //edge case: if numeric overflow detected - stop
                return result.toArray(new Number[0]);
        }
        result.add(max);
        return result.toArray(new Number[0]);
    }

    //min, max, increment can be valid in sense of type, but invalid in sense of logical mistakes. (ex: max > min)
    //throws exception and message to logger: IllegalArgumentException.class - if input data has mistakes
    private static void checkInputLogicMistakes(Number min, Number max, Number increment) {
        if (min.doubleValue() > max.doubleValue()) {
            String msg = String.format("Invalid input: invalid range %s..%s -- min > max", min, max);
            throw new IllegalArgumentException(msg);
        }

        if (increment.doubleValue() < 0) {
            String msg = String.format("Invalid input: increment=%s is invalid for range %s..%s", increment, min, max);
            throw new IllegalArgumentException(msg);
        }
    }

    public static List<MultiplicationEntry> countResultEntries(Number[] sourceRow, String type) {
        LinkedList<MultiplicationEntry> resultEntries = new LinkedList<>();
        int startIndex = 0;
        for (int i = 0; i < sourceRow.length; i++) {
            for (int j = startIndex; j < sourceRow.length; j++) {
                Number multiplier = sourceRow[i];
                Number multiplied = sourceRow[j];
                Object product = NumericCalc.safeMultiply(multiplier, multiplied, type);
                MultiplicationEntry currentEntry = new MultiplicationEntry(multiplier, multiplied, product);
                logger.info(currentEntry.toString()); //print current entry
                resultEntries.add(currentEntry); //safe into list
            }
            startIndex++;
        }
        return resultEntries;
    }



}

//todo винести логіку виводу таблиці в окремий метод