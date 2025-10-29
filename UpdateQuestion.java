package Quizzproject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
public class UpdateQuestion implements EventHandler<ActionEvent>{
	private ArrayList<Integer> scoreandindex;
	private Question currentQuestion;

	private ArrayList<Question> selected;
	private Label questionLabel ;
	private Label possibleAnswers;
	private TextField answerField;

	private Scene scene2;
	private Stage stage;
	private Label result;
	public UpdateQuestion(Label result,ArrayList<Integer> scoreandindex,Question currentQuestion, ArrayList<Question> selected,Label questionLabel, Label possibleAnswers, TextField answerField, Scene scene2, Stage stage){
		this.scoreandindex = scoreandindex;
		this.currentQuestion = currentQuestion;
		this.selected = selected;
		this.questionLabel = questionLabel;
		this.possibleAnswers = possibleAnswers;
		this.answerField = answerField;
		this.scene2 = scene2;
		this.stage = stage;
		this.result = result;
		

	}
	@Override
	public void handle(ActionEvent event){
		if (scoreandindex.get(1) < selected.size() - 1) { 
			String answer = answerField.getText();
			if (answer.equals(currentQuestion.getCorrectAnswer())) {
				scoreandindex.set(0,scoreandindex.get(0)+1);
			}
			scoreandindex.set(1,scoreandindex.get(1)+1);
			currentQuestion = selected.get(scoreandindex.get(1));
			questionLabel.setText(currentQuestion.getQuestion());
			possibleAnswers.setText(String.join(", ", currentQuestion.getAllAnswers()));
			answerField.clear();
    	}
		else {
			result.setText("Your score : " + scoreandindex.get(0) + " out of " + selected.size());
			stage.setScene(scene2);
			
	
	}
}
}
