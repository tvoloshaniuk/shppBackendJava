package ua.shpp;

import java.util.List;

public class CriticalResourceException extends RuntimeException {
    private final List<String> listOfProblems;

    public CriticalResourceException(List<String> listOfProblems) {
        this.listOfProblems = listOfProblems;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (String s : listOfProblems) {
            sb.append(s).append("\n");
        }
        return super.getMessage() + "\n" + sb;
    }
}
