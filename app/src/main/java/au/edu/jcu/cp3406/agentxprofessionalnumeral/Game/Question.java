package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

import android.util.Log;

import java.util.Arrays;

/**
 * Holds information about a question and functions to check guesses and display he question
 */
public class Question {

    private int[] numbers; // Numbers on the LHS of the equation
    private int[] operations; // List of operations from left to right of equation
    private int result; // RHS of the equation
    private int missingValue; // Index of the missing value
    private boolean hasMultiplication;
    private boolean hasDivision;

    public Question(int[] numbers, int[] operations, int result, int missingValue,
                    boolean hasMultiplication, boolean hasDivision) {
        this.numbers = numbers;
        this.operations = operations;
        Log.i("Question", Arrays.toString(operations));
        this.result = result;
        this.missingValue = missingValue;
        this.hasMultiplication = hasMultiplication;
        this.hasDivision = hasDivision;
    }

    // Checks if the players guess is correct
    public boolean checkGuess(int guess) {
         int answer;
         if (missingValue == numbers.length) {
             answer = result;
         }
         else {
             answer = numbers[missingValue];
         }
         return guess == answer;
    }

    // Builds and returns a string representation of the question, replacing the missing value with x
     String display() {
         StringBuilder questionString = new StringBuilder();
         for (int i = 0; i < operations.length; ++i) {
             // Append x if the current number is the missing value, otherwise append the number
             if (i == missingValue) {
                 questionString.append("x");
             } else {
                 questionString.append(numbers[i]);
             }
             // Append the operation to the right of the number
             String operationString = "";
             switch (operations[i]) {
                 case 0:
                     operationString = " + ";
                     break;
                 case 1:
                     operationString = " - ";
                     break;
                 case 2:
                     operationString = " * ";
                     break;
                 case 3:
                     operationString = " / ";
                     break;
             }
             questionString.append(operationString);
         }
         // Append either the final number or x if it is the missing value and add equals to finish LHS
         if (numbers.length - 1 == missingValue) {
             questionString.append("x = ");
         } else {
             questionString.append(numbers[numbers.length - 1]).append(" = ");
         }
        // Append x or final number as RHS
         if (numbers.length == missingValue) {
            questionString.append("x");
        } else {
            questionString.append(result);
        }
         return questionString.toString();
    }

    int getLength() {
         return operations.length;
    }

    public boolean hasMultiplication() {
         return hasMultiplication;
    }

    public boolean hasDivision() {
         return hasDivision;
    }

    // Checks to see if the RHS of the equation is the missing value
    boolean resultIsX() {
         return missingValue == numbers.length;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public  int[] getOperations() {
        return operations;
    }

    public int getResult() {
        return result;
    }

    public int getMissingValue() {
        return missingValue;
    }
}


