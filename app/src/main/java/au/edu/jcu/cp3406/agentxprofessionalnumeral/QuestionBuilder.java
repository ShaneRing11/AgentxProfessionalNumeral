package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class QuestionBuilder {

    private int maxOperations;
    private Random random;

    QuestionBuilder(int maxOperations) {
        this.maxOperations = maxOperations;
        random = new Random();
    }

    public Question buildQuestion() {
        // Generate a number of operations between 1 and maxOperations
        int numOperations = random.nextInt(maxOperations);
        if (numOperations == 0) {
            numOperations = 1;
        }
        // Generate a random set of operations
        int[] operations = new int[numOperations];
        boolean containsDivision = false;
        for (int operation : operations) {
            operation = random.nextInt(4);
            if (operation == 3) {
                containsDivision = true;
            }
        }
        // Generate equations result
        int resultBound = 101;
        // Make result a smaller number if the question contains division
        if (containsDivision) {
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
        return new Question(numbersArray, operations, result, missingValue);
    }

     ArrayList<Integer> generateNumbers(int[] operations, int result) {
        ArrayList<Integer> numbers = new ArrayList<>();
        int randomNumber = 0;
        int oldRandomNumber;
        // Check for subtraction
        for (int i = operations.length - 1;  i > -1; --i) {
            if (operations[i] == 1  && numbers.size() < i) {
                System.out.println("Found subtraction at " + i);
                if (numbers.size() == i) {
                    System.out.println("Number directly to the left determined, use random number " + randomNumber + " in place of result");
                    oldRandomNumber = randomNumber;
                    randomNumber = random.nextInt(13);
                    numbers.add(oldRandomNumber + randomNumber);
                    System.out.println(oldRandomNumber + " + " + randomNumber + " = " + (oldRandomNumber + randomNumber));
                } else {
                    randomNumber = random.nextInt(13);
                    System.out.println(result + " + " + randomNumber + " = " + (result + randomNumber));
                    System.out.println("Recurring with operations " + Arrays.toString(Arrays.copyOfRange(operations, 0, i)) + " and result " + (result + randomNumber));
                    numbers.addAll(generateNumbers(
                            Arrays.copyOfRange(operations, 0, i),
                            result + randomNumber));
                }
                // If all numbers left of operators have been generated add the random number to the right
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    System.out.println("All numbers to the left determined, append " + randomNumber);
                    return numbers;
                }
            }
        }
        // Check for addition
        for (int i = operations.length - 1;  i > -1; --i) {
            if (operations[i] == 0  && numbers.size() < i) {
                System.out.println("Found addition at " + i);
                if (numbers.size() == i) {
                    System.out.println("Number directly to the left determined, use random number " + randomNumber + " in place of result");
                    oldRandomNumber = randomNumber;
                    randomNumber = random.nextInt(13);
                    numbers.add(oldRandomNumber - randomNumber);
                    System.out.println(oldRandomNumber + " - " + randomNumber + " = " + (oldRandomNumber - randomNumber));
                } else {
                    randomNumber = random.nextInt(result);
                    System.out.println(result + " - " + randomNumber + " = " + (result - randomNumber));
                    System.out.println("Recurring with operations " + Arrays.toString(Arrays.copyOfRange(operations, 0, i)) + " and result " + (result - randomNumber));
                    numbers.addAll(generateNumbers(
                            Arrays.copyOfRange(operations, 0, i),
                            result - randomNumber));

                }
                // If all numbers left of operators have been generated add the random number to the right
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    System.out.println("All numbers to the left determined, append " + randomNumber);
                    return numbers;
                }
            }
        }
        // Check for division
        for (int i = operations.length - 1;  i > -1; --i) {
            if (operations[i] == 3 && numbers.size() - 1 < i) {
                System.out.println("Found division at " + i);
                if (numbers.size() == i && numbers.size() > 0) {
                    System.out.println("Number directly to the left determined, use random number " + randomNumber + " in place of result");
                    oldRandomNumber = randomNumber;
                    randomNumber = random.nextInt(12) + 1;
                    numbers.add(oldRandomNumber * randomNumber);
                    System.out.println(oldRandomNumber + " * " + randomNumber + " = " + (oldRandomNumber * randomNumber));
                } else {
                    randomNumber = random.nextInt(12) + 1;
                    System.out.println(result + " * " + randomNumber + " = " + (result * randomNumber));
                    System.out.println("Recurring with operations " + Arrays.toString(Arrays.copyOfRange(operations, 0, i)) + " and result " + (result * randomNumber));
                    numbers.addAll(generateNumbers(
                            Arrays.copyOfRange(operations, 0, i),
                            result * randomNumber));
                }
                // If all numbers left of operators have been generated add the random number to the right
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    System.out.println("All numbers to the left determined, append " + randomNumber);
                    return numbers;
                }
            }
        }
        // Check for multiplication
        for (int i = operations.length - 1;  i > -1; --i) {
            if (operations[i] == 2  && numbers.size() - 1 < i && numbers.size() > 0) {
                System.out.println("Found multiplication at " + i);
                if (numbers.size() == i) {
                    System.out.println("Number directly to the left determined, use random number " + randomNumber + " in place of result");
                    if (randomNumber == 0) {
                        System.out.println("You're not on the list...");
                        randomNumber = random.nextInt(13);
                        if (randomNumber == 0) {
                            numbers.add(random.nextInt(12 + 1));
                        } else {
                            numbers.add(randomNumber);
                            numbers.add(0);
                            return numbers;
                        }
                    } else {
                        oldRandomNumber = randomNumber;
                        randomNumber = random.nextInt(12) + 1; //TODO make into set of possible integers and loop while removing selection for finite loop + make into helper method
                        // Generate numbers until the result of division is a whole number other than 0
                        while (oldRandomNumber % randomNumber != 0 || result / randomNumber == 0) {
                            System.out.println("Number " + randomNumber + " bad, try again");
                            randomNumber = random.nextInt(12) + 1;
                        }
                        numbers.add(oldRandomNumber / randomNumber);
                        System.out.println(oldRandomNumber + " / " + randomNumber + " = " + (oldRandomNumber / randomNumber));
                    }
                } else {
                    randomNumber = random.nextInt(12) + 1; //TODO make into set of possible integers and loop while removing selection for finite loop + make into helper method
                    // Generate numbers until the result of division is a whole number
                    while (result % randomNumber != 0 || result / randomNumber == 0) {
                        System.out.println("Number bad, try again");
                        randomNumber = random.nextInt(12) + 1;
                    }
                    System.out.println(result + " / " + randomNumber + " = " + (result / randomNumber));
                    System.out.println("Recurring with operations " + Arrays.toString(Arrays.copyOfRange(operations, 0, i)) + " and result " + (result / randomNumber));
                    numbers.addAll(generateNumbers(
                            Arrays.copyOfRange(operations, 0, i),
                            result / randomNumber));
                }
                // If all numbers left of operators have been generated add the random number to the right
                if (numbers.size() == operations.length) {
                    numbers.add(randomNumber);
                    System.out.println("All numbers to the left determined, append " + randomNumber);
                    return numbers;
                }
            }
        }
        // If operations is empty add the result to numbers
        numbers.add(result);
        System.out.println("No operations, appending " + result);
        return numbers;
    }
}