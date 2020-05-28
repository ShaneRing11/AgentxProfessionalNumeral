package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
* Used to generate random questions for games
*/
public class QuestionBuilder {

    private Difficulty difficulty;
    private Random random;

    public QuestionBuilder(Difficulty difficulty) {
        this.difficulty = difficulty;
        random = new Random();
    }

    public Question buildQuestion() {
        // Set maximum number of operations based on difficulty selected
        int maxOperations = 2;
        switch (difficulty) {
            case EASY:
                maxOperations = 1;
                break;
            case HARD:
                maxOperations = 3;
                break;
            case EXPERT:
                maxOperations = 4;
                break;
        }
        // Generate a number of operations between 1 and maxOperations
        int numOperations = random.nextInt(maxOperations);
        if (numOperations == 0) {
            numOperations = 1;
        }
        // Generate a random set of operations
        int[] operations = new int[numOperations];
        boolean hasDivision = false;
        boolean hasMultiplication = false;
        for (int operationIndex = 0; operationIndex < operations.length; ++operationIndex) {
            int operation = random.nextInt(4);
            operations[operationIndex] = operation;
            if (operation == 2) {
                hasMultiplication = true;
            }
            if (operation == 3) {
                hasDivision = true;
            }
        }
        // Generate equations result
        int resultBound = 101;
        // Make result a smaller number if the question contains division
        if (hasDivision) {
            resultBound = 13;
        }
        int result = random.nextInt(resultBound);
        // Randomly generate number values
        ArrayList<Integer> numbersList = generateNumbers(operations, result);
        // Convert the list of numbers into a primitive int array
        int[] numbersArray = new int[numOperations + 1];
        for (int i = 0; i < numbersArray.length; ++i) {
            numbersArray[i] = numbersList.get(i);
        }
        // Generate a random index value to be x with, length + 1 indicates the result is x
        int missingValue = random.nextInt(numbersArray.length + 1);
        return new Question(numbersArray, operations, result, missingValue, hasMultiplication, hasDivision);
    }

     private ArrayList<Integer> generateNumbers(int[] operations, int result) {
        ArrayList<Integer> numbers = new ArrayList<>();
        int randomNumber = 0;
        int oldRandom;
        // Check for subtraction
        for (int i = operations.length - 1;  i > -1; --i) {
            // Check if operation is subtraction and LHS is not generated
            if (operations[i] == 1  && numbers.size() <= i) {
                // If number to left of operation is generated, calculate using random number as required result
                if (numbers.size() >= i && i > 0) {
                    oldRandom = randomNumber;
                    randomNumber = random.nextInt(13);
                    numbers.add(oldRandom + randomNumber);
                } else {
                    // If there are numbers generated trim complete operations to the left
                    if (numbers.size() > 0) {
                        oldRandom = randomNumber;
                        randomNumber = random.nextInt(13);
                        // Generate and append numbers for incomplete operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                oldRandom + randomNumber));
                    } else {
                        randomNumber = random.nextInt(13);
                        // Generate and append numbers for operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                result + randomNumber));
                    }
                }
                // If all numbers left of operators have been generated add random number as RHS
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    return numbers;
                }
            }
        }
        // Check for addition
        for (int i = operations.length - 1;  i > -1; --i) {
            // Check if operation is addition and LHS is not generated
            if (operations[i] == 0  && numbers.size() <= i) {
                // If number to left of operation is generated, calculate using random number as required result
                if (numbers.size() >= i && i > 0) {
                    oldRandom = randomNumber;
                    if (oldRandom == 0) {
                        randomNumber = 0;
                    } else {
                        randomNumber = random.nextInt(oldRandom);
                    }
                    numbers.add(oldRandom - randomNumber);
                } else {
                    // If there are numbers generated trim complete operations to the left
                    if (numbers.size() > 0) {
                        oldRandom = randomNumber;
                        if (oldRandom == 0) {
                            randomNumber = 0;
                        } else {
                            randomNumber = random.nextInt(oldRandom);
                        }
                        // Generate and append numbers for incomplete operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                oldRandom - randomNumber));
                    } else {
                        if (result == 0) {
                            randomNumber = 0;
                        } else {
                            randomNumber = random.nextInt(result);
                        }
                        // Generate and append numbers for operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                result - randomNumber));
                    }
                }
                // If all numbers left of operators have been generated add random number as RHS
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    return numbers;
                }
            }
        }
        // Check for division
        for (int i = operations.length - 1;  i > -1; --i) {
            // Check if division is subtraction and LHS is not generated
            if (operations[i] == 3 && numbers.size() <= i) {
                // If number to left of operation is generated, calculate using random number as required result
                if (numbers.size() >= i && i > 0) {
                    oldRandom = randomNumber;
                    randomNumber = random.nextInt(12) + 1;
                    numbers.add(oldRandom * randomNumber);
                } else {
                    // If there are numbers generated trim complete operations to the left
                    if (numbers.size() > 0) {
                        oldRandom = randomNumber;
                        randomNumber = random.nextInt(12) + 1;
                        // Generate and append numbers for incomplete operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                oldRandom * randomNumber));
                    } else {
                        randomNumber = random.nextInt(12) + 1;
                        // Generate and append numbers for operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                result * randomNumber));
                    }
                }
                // If all numbers left of operators have been generated add random number as RHS
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    return numbers;
                }
            }
        }
        // Check for multiplication
        for (int i = operations.length - 1;  i > -1; --i) {
            // Check if multiplication is subtraction and LHS is not generated
            if (operations[i] == 2  && numbers.size() <= i) {
                // If number to left of operation is generated, calculate using random number as required result
                if (numbers.size() >= i && i > 0) {
                    oldRandom = randomNumber;
                    randomNumber = generateDivisor(oldRandom);
                    numbers.add(oldRandom / randomNumber);
                } else {
                    // If there are numbers generated trim complete operations to the left
                    if (numbers.size() > 0) {
                        oldRandom = randomNumber;
                        randomNumber = generateDivisor(oldRandom);
                        // Generate and append numbers for incomplete operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                oldRandom / randomNumber));
                    } else {
                        randomNumber = generateDivisor(result);
                        // Generate and append numbers for operations to the left
                        numbers.addAll(generateNumbers(
                                Arrays.copyOfRange(operations, numbers.size(), i),
                                result / randomNumber));
                    }
                }
                // If all numbers left of operators have been generated add random number as RHS
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    return numbers;
                }
            }
        }
        // If no operations append the result to numbers
        numbers.add(result);
        return numbers;
    }

    // Generates a divisor that will result in a whole number
    private int generateDivisor(int result) {
        int randomNumber = random.nextInt(12) + 1;
        if (result != 0) { // If the result is 0 dividing by anything will equal zero so any number will do
            while (result % randomNumber != 0 || result / randomNumber == 0) {
                randomNumber = random.nextInt(12) + 1;
            }
        }
        return randomNumber;
    }
}