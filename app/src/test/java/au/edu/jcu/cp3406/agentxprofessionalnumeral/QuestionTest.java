package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import org.junit.Test;

import java.util.ArrayList;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Question;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.QuestionBuilder;

import static org.junit.Assert.*;

/**
 * Unit tests for Question object methods
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QuestionTest {

    // Check that the LHS of the equation equals the RHS and is solvable
    @Test
    public void test_build_Question() {
        QuestionBuilder questionBuilder = new QuestionBuilder(Difficulty.EXPERT);
        for (int repeats = 0; repeats < 100; ++repeats) {
            Question question = questionBuilder.buildQuestion();
            int result = question.getResult();
            int[] operations = question.getOperations();
            ArrayList<Integer> operationsList = new ArrayList<>();
            for (int operation : operations) {
                operationsList.add(operation);
            }
            int[] numbersArray = question.getNumbers();
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int number : numbersArray) {
                numbers.add(number);
            }
            // Calculate the LHS of the equation
            // Perform multiplication
            int i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 2) {
                    numbers.set(i, numbers.get(i) * numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                } else {
                    ++i;
                }
            }
            // Perform division
            i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 3) {
                    numbers.set(i, numbers.get(i) / numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                } else {
                    ++i;
                }
            }
            //Perform addition
            i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 0) {
                    numbers.set(i, numbers.get(i) + numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                } else {
                    ++i;
                }
            }
            //Perform subtraction
            i = 0;
            while (i < operationsList.size()) {
                if (operationsList.get(i) == 1) {
                    numbers.set(i, numbers.get(i) - numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operationsList.remove(i);
                } else {
                    ++i;
                }
            }
            assertEquals(result, numbers.get(0).intValue());
        }
    }

    // Check that the checkGuess function responds correctly
    @Test
    public void test_check_guess () {
        QuestionBuilder questionBuilder = new QuestionBuilder(Difficulty.EXPERT);
        Question question = questionBuilder.buildQuestion();
        int result = question.getResult();
        int[] numbers = question.getNumbers();
        int missingValue = question.getMissingValue();
            int guess;
            if (missingValue == numbers.length) {
                guess = result;
            } else {
                guess = numbers[missingValue];
            }
            assertTrue(question.checkGuess(guess));
    }
}
