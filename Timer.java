package Quizzproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Timer implements EventHandler<ActionEvent> {
    private Label timerLabel;
    private int[] timeLeft;
    private Timeline timeline;
    private Stage stage;
    private Scene nextscene;
    private int initialTime;  // Store initial time
    private int elapsedTime = 0; // Track time elapsed

    public Timer(Label timerLabel, int[] time, Stage stage, Scene nextscene) {
        this.timerLabel = timerLabel;
        if (time == null || time.length == 0) {
            throw new IllegalArgumentException("Time array must have at least one element.");
        }
        this.timeLeft = time;
        this.initialTime = time[0];  // Store initial time
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), this));
        this.nextscene = nextscene;
        this.stage = stage;
    }

    // Start the timer
    public void start() {
        elapsedTime = 0; // Reset elapsed time when starting
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play(); 
    }

    // Stop the timer
    public void stop() {
        timeline.stop();
    }

    // Event handler for each second that passes
    @Override
    public void handle(ActionEvent event) {
        if (timeLeft[0] > 0) {
            timeLeft[0]--;
            elapsedTime++; // Increment elapsed time
            timerLabel.setText("Time: " + timeLeft[0]);
        } else {
            stop();
            timerLabel.setText("Time is up!");
            stage.setScene(nextscene);
        }
    }

    // Get time remaining
    public int getTimeLeft() {
        return timeLeft[0];
    }

    // Get total time taken (elapsed time)
    public int getTimeTaken() {
        return elapsedTime;
    }

    // Get initial time setting
    public int getInitialTime() {
        return initialTime;
    }
}