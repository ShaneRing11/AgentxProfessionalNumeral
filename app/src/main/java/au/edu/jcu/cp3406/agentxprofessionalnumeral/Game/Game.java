package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

public class Game {

    private Question question;
    private int score;
    private int bombsRemaining;

    public Game() {
        score = 0;
        bombsRemaining = 3;
    }

    public Game(int score, int bombsRemaining, Question question) {
        this.score = score;
        this.bombsRemaining = bombsRemaining;
        this.question = question;
    }

    public int updateScore(int bonus) {
        int points = question.getLength() + bonus;
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
        return score;
    }

    public boolean checkGuess(int guess) {
        return question.checkGuess(guess);
    }

    public void useBomb() {
        if (bombsRemaining > 0) {
            bombsRemaining -= 1;
        }
    }

    public int getScore() {
        return score;
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

    public void setQuestion(Question question) {
        this.question = question;
    }
}