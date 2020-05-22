package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.QuestionBuilder;

import static org.junit.Assert.*;

/**
 * Unit tests for QuestionBuilder object methods
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QuestionBuilderTest {
    @Test
    public void test_build_Question() {
        //TODO rewrite tests to use buildQuestion instead of getNumbers directly

        /*QuestionBuilder questionBuilder = new QuestionBuilder(4);
        Random random = new Random();

        for (int iteration = 0; iteration < 100; ++iteration) {
            System.out.println("\nTest " + iteration + ":");
            // Generate a number of operations between 1 and maxOperations
            int numOperations = random.nextInt(5);
            if (numOperations == 0) {
                numOperations = 1;
            }
            System.out.println("Number of Operations: " + numOperations);
            // Generate a random set of operations
            int[] operations = new int[numOperations];
            boolean containsDivision = false;
            for (int operationIndex = 0; operationIndex < operations.length; ++operationIndex) {
                int operation = random.nextInt(4);
                operations[operationIndex] = operation;
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
            System.out.println("Generating numbers with a result of " + result + " with operations " + Arrays.toString(operations));
            ArrayList<Integer> numbers = questionBuilder.generateNumbers(operations, result);
            ArrayList<Integer> operationsList = new ArrayList<>();
            for (int operation : operations) {
                operationsList.add(operation);
            }
            System.out.println("Final numbers: " + numbers.toString());
            // Perform multiplication
            int i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 2) {
                    System.out.println(numbers.get(i) + " * " + numbers.get(i + 1) + " = " + (numbers.get(i) * numbers.get(i + 1)));
                    numbers.set(i, numbers.get(i) * numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                    System.out.println("Multiplication performed, new operations: " + operationsList.toString() + ", new numbers: " + numbers.toString());
                } else {
                    ++i;
                }
            }
            // Perform division
            i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 3) {
                    System.out.println(numbers.get(i) + " / " + numbers.get(i + 1) + " = " + (numbers.get(i) / numbers.get(i + 1)));
                    numbers.set(i, numbers.get(i) / numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                    System.out.println("Division performed, new operations: " + operationsList.toString() + ", new numbers: " + numbers.toString());
                } else {
                    ++i;
                }
            }
            //Perform addition
            i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 0) {
                    System.out.println(numbers.get(i) + " + " + numbers.get(i + 1) + " = " + (numbers.get(i) + numbers.get(i + 1)));
                    numbers.set(i, numbers.get(i) + numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                    System.out.println("new operations: " + operationsList.toString() + ", new numbers: " + numbers.toString());
                } else {
                    ++i;
                }
            }
            //Perform subtraction
            i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 1) {
                    System.out.println(numbers.get(i) + " - " + numbers.get(i + 1) + " = " + (numbers.get(i) - numbers.get(i + 1)));
                    numbers.set(i, numbers.get(i) - numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                    System.out.println("Subtraction performed, new operations: " + operationsList.toString() + ", new numbers: " + numbers.toString());
                } else {
                    ++i;
                }
            }
            assertEquals(result, numbers.get(0).intValue());
        }*/
    }
}