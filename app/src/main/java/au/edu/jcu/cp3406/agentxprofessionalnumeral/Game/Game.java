package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

public class Game {

    private QuestionBuilder questionBuilder;
    private Question question;
    private int score;
    private int detection;
    private int bombsRemaining;
    private int incorrectGuesses;
    private int timeBonus;

    public Game(Difficulty difficulty) {
        questionBuilder = new QuestionBuilder(difficulty);
        score = 0;
        detection = 0;
        bombsRemaining = 3;
        incorrectGuesses = 0;
    }

    private void generateQuestion() {
        incorrectGuesses = 0;
        timeBonus = 15;
        question = questionBuilder.buildQuestion();
    }

    private void updateDetection(int addedDetection) {
        detection += addedDetection;
    }

    private void updateScore() {
        int points = question.getLength() + timeBonus;
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

    // Increment the time bonus and threat values
    public void tick() {
        if (timeBonus > 0) {
            --timeBonus;
        }
        updateDetection(1);
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
            updateScore();
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

    public String displayQuestion() {
        return question.display();
    }
}