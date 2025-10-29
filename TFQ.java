package Quizzproject;
import java.util.ArrayList;


public class TFQ implements Question{
    String question;
    ArrayList<String> answers;
    String correctAnswer;

    public TFQ(String question, ArrayList<String> answers, String correctAnswer){
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }


    @Override
    public String getQuestion(){
        return question;
    }

    @Override
    public ArrayList<String> getAllAnswers(){
        return answers;
    }

    @Override
    public String getCorrectAnswer(){
        return correctAnswer;
    }


    public void setQuestion(String newQuestion){
        this.question = newQuestion;
    }
    
    public void setAnswer(ArrayList<String> newAnswers){
        this.answers = newAnswers;
    }

    public void setCorrectAnswer(String newCorrectAnswer){
        this.correctAnswer = newCorrectAnswer;
    }

    @Override
    public String toString(){
        return "question :" + question + "possible answers :" + answers+ "correct answer :" + correctAnswer;
    }

    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        if (other == null || !(other instanceof TFQ)) return false;
                return false;
    }
  
}
