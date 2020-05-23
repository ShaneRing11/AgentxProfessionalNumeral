package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

public class Game {

    private QuestionBuilder questionBuilder;
    private Question question;
    private int score;
    private int detection;
    private int bombsRemaining;

    public Game(Difficulty difficulty) {
        questionBuilder = new QuestionBuilder(difficulty);
        score = 0;
        detection = 0;
        bombsRemaining = 3;
    }

    public Game(Difficulty difficulty, int score, int detection, int bombsRemaining, Question question) {
        questionBuilder = new QuestionBuilder(difficulty);
        this.score = score;
        this.detection = detection;
        this.bombsRemaining = bombsRemaining;
        this.question = question;
    }

    public void generateQuestion() {
        question = questionBuilder.buildQuestion();
    }

    public void updateDetection(int addedDetection) {
        detection += addedDetection;
    }

    public void updateScore(int timeBonus) {
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

    public boolean checkGuess(int guess) {
        return question.checkGuess(guess);
    }

    public void useBomb() {
        if (bombsRemaining > 0) {
            bombsRemaining -= 1;
            updateDetection(5);
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

    public Question getQuestion() {
        return question;
    }
}