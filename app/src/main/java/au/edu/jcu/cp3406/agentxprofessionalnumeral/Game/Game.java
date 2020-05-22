package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

public class Game {

    private QuestionBuilder questionBuilder;
    private Question question;
    private int score;
    private int detection;
    private int bombsRemaining;
    private int incorrectGuesses;

    public Game(Difficulty difficulty) {
        questionBuilder = new QuestionBuilder(difficulty);
        score = 0;
        detection = 0;
        bombsRemaining = 3;
        incorrectGuesses = 0;
    }

    private void generateQuestion() {
        incorrectGuesses = 0;
        question = questionBuilder.buildQuestion();
    }

    public void updateDetection(int addedDetection) {
        detection += addedDetection;
    }

    public void updateScore() {
        //TODO add time tracking and bonus
        int points = question.getLength();
        if (question.hasMultiplication()) {
            points *= 2;
        }
        if (question.hasDivision()) {
            points *= 2;
        }
        if (question.resultIsX()) {
            points /= 2;
        }
        score += points;
    }

    public void checkGuess(int guess) {
        //TODO move into GameActivity and
        boolean isCorrect = question.checkGuess(guess);
        if (!isCorrect) {
            updateDetection(10);
            ++incorrectGuesses;
            if (incorrectGuesses == 3) {
                generateQuestion();
            }
        } else {
            // Update score
            generateQuestion();
        }
    }

    public void useBomb() {
        if (bombsRemaining > 0) {
            bombsRemaining -= 1;
            updateDetection(5);
            generateQuestion();
        }
    }

    public int getScore() {
        return score;
    }


    public int getDetection() {
        return detection;
    }

    public int getBombsRemaining(){
        return bombsRemaining;
    }
}
