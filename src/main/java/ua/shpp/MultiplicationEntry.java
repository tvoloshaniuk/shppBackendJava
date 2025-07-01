package ua.shpp;

//3 values of 1 multiplication row
public class MultiplicationEntry {
    public final Number multipied;
    public final Number multipier;
    public final Object product;

    MultiplicationEntry(Number multipied, Number multipier, Object product) {
        this.multipied = multipied;
        this.multipier = multipier;
        this.product = product;
    }

    //Print in mask: a * b = c
    @Override
    public String toString() {
        return multipied + " * " + multipier + " = " + product;
    }
}
