package com.johndestross.calculatorapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        CalculatorEngine calculatorEngine = new CalculatorEngine();
        CalculatorUI calculatorUI = new CalculatorUI(stage, calculatorEngine);
        calculatorEngine.setDisplays(calculatorUI.getDisplay(), calculatorUI.getOperationDisplay());

        stage = calculatorUI.getStage();
        stage.show();
    }
}