package com.johndestross.calculatorapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Setup for the UI of the calculator
 *
 * @author John Destross
 */
public class CalculatorUI {

    public static final int STANDARD_CALCULATOR_WIDTH = 334; // Standard size of Microsoft Windows Calculator App
    public static final int STANDARD_CALCULATOR_HEIGHT = 464; // Standard size of Microsoft Windows Calculator App
    public static final int BUTTON_SPACING_GAP = 3; // Button Spacing

    private final Stage calculatorStage;
    private Label calculatorDisplay;
    private Label operationDisplay;
    private final CalculatorEngine calculatorEngine;

    /**
     * Constructor for CalculatorUI. Set stage
     * variable and setup initial UI.
     *
     * @param stage Stage for calculator program
     */
    public CalculatorUI(Stage stage, CalculatorEngine engine) {
        this.calculatorStage = stage;
        this.calculatorEngine = engine;
        setupUI();
    }

    /**
     * Formats basic stage to be titled "Calculator",
     * spaces in top left corner of screen,
     * removes resizing functionality
     * and adds calculator icon.
     *
     */
    public void setupUI() {
        calculatorStage.setTitle("Calculator");
        calculatorStage.setX(50);
        calculatorStage.setY(50);
        calculatorStage.setResizable(false);

        String calculatorIconResource = getResourcePathFromURL("icons/calculator.png");
        calculatorStage.getIcons().add(new Image(calculatorIconResource));

        calculatorStage.setScene(setupScene());
    }

    /**
     * Create calculator screen
     *
     * @return Calculator screen
     */
    public Scene setupScene() {
        VBox calculatorLayout = new VBox(10);
        calculatorLayout.setPadding(new Insets(20));

        calculatorLayout.getChildren().addAll(setupDisplay(), setupButtons());

        Scene scene = new Scene(calculatorLayout, STANDARD_CALCULATOR_WIDTH, STANDARD_CALCULATOR_HEIGHT);
        String sceneStyleResource = CalculatorUI.getResourcePathFromURL("calculator_style.css");
        scene.getStylesheets().add(sceneStyleResource);

        setupKeyboardCapability(scene);

        return scene;
    }

    /**
     * Create operation/calculator display and join them.
     *
     * @return Stack with both displays conjoined
     */
    private StackPane setupDisplay() {
        // Operation display
        operationDisplay = new Label(""); // This will be your tooltip-style text
        operationDisplay.getStyleClass().add("operation-display");

        // Calculator display
        calculatorDisplay = new Label("0");
        calculatorDisplay.setPrefWidth(STANDARD_CALCULATOR_WIDTH);
        calculatorDisplay.setAlignment(Pos.BOTTOM_RIGHT);
        calculatorDisplay.getStyleClass().add("display");

        // Add operation display to calculator display
        StackPane displayStack = new StackPane(calculatorDisplay, operationDisplay);
        displayStack.setPrefWidth(STANDARD_CALCULATOR_WIDTH);
        StackPane.setAlignment(operationDisplay, Pos.TOP_RIGHT);
        return displayStack;
    }

    /**
     * Create buttons for calculator. Buttons will call
     * their respective event handlers when pressed.
     *
     * @return Grid of buttons
     */
    public GridPane setupButtons() {
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(BUTTON_SPACING_GAP);
        buttonGrid.setVgap(BUTTON_SPACING_GAP);

        String[][] buttons = {
            {"±", "CE", "C", "⌫"},
            {"7", "8", "9", "÷"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", ".", "=", "+"}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                Button button = new Button(buttons[row][col]);
                button.setDefaultButton(false);
                button.setFocusTraversable(false);
                button.getStyleClass().add("button");
                if ("±CE⌫".contains(buttons[row][col])) {
                    button.getStyleClass().add("button-control");
                }
                else if ("=÷*-+".contains(buttons[row][col])) {
                    button.getStyleClass().add("button-operator");
                }
                calculatorEngine.addButtonFunctionality(button);
                buttonGrid.add(button, col, row);
            }
        }
        return buttonGrid;
    }

    private void setupKeyboardCapability(Scene scene) {
        scene.setOnKeyPressed(keyPressEvent -> {
            switch (keyPressEvent.getCode()) {
                case SLASH -> calculatorEngine.handleKeyInput("÷");
                case ENTER -> calculatorEngine.handleKeyInput("=");
                case BACK_SPACE -> calculatorEngine.handleKeyInput("⌫");
                case DELETE -> calculatorEngine.handleKeyInput("CE");
                case ESCAPE -> calculatorEngine.handleKeyInput("C");
            }
        });

        scene.setOnKeyTyped(keyTypeEvent -> {
            if ("0123456789+-*÷=._".contains(keyTypeEvent.getCharacter())) {
                calculatorEngine.handleKeyInput(keyTypeEvent.getCharacter());
            }
        });
    }

    /**
     * Turns a given "URL" path into a usable resource path
     * for assets contained in the src/main/resources folder
     *
     * @param path Path to asset in src/main/resources folder
     * @return Usable path to asset in src/main/resources folder
     * @throws IllegalArgumentException If the provided path does not exist
     */
    public static String getResourcePathFromURL(String path) throws IllegalArgumentException {
        URL resourceURL = Main.class.getResource("/com/johndestross/calculatorapp/" + path);
        if (resourceURL == null) {
            throw new IllegalArgumentException("\"" + path + "\" resource not found");
        }
        return resourceURL.toExternalForm();
    }

    /**
     * Basic getter for stage.
     * @return UI's stage
     */
    public Stage getStage() {
        return this.calculatorStage;
    }

    /**
     * Basic getter for display.
     * @return Calculator display label
     */
    public Label getDisplay() {
        return this.calculatorDisplay;
    }

    /**
     * Basic getter for operation display.
     * @return Operation display label
     */
    public Label getOperationDisplay() {
        return this.operationDisplay;
    }
}
