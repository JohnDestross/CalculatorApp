package com.johndestross.calculatorapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalculatorEngine {

    private Label calculatorDisplay;
    private Label operationDisplay;

    private double storedNumber = 0;
    private double displayNumber = 0;
    private String storedOperator = "";
    private boolean startNewNumber = true; // Indicates if next digit starts fresh
    private boolean operatorPressedLast = false; // Indicates if last button pressed was an operator

    public void addButtonFunctionality(Button button) {
        String buttonText = button.getText();
        button.setOnAction(buttonPress -> {
            if (buttonText.matches("[0-9]")) {
                handleNumber(Integer.parseInt(buttonText));
            } else if (buttonText.equals(".")) {
                handleDecimalPoint();
            } else if (buttonText.equals("±")) {
                handleNegative();
            } else if ("+-*/÷".contains(buttonText)) {
                handleOperator(buttonText);
            } else if (buttonText.equals("=")) {
                handleEquals();
            } else if (buttonText.equals("⌫")) {
                handleBackspace();
            } else if (buttonText.equals("C")) {
                clearDisplay();
            } else if (buttonText.equals("CE")) {
                clearEntry();
            }
        });
    }

    public void handleKeyInput(String input) {
        if (input.matches("[0-9]")) {
            handleNumber(Integer.parseInt(input));
        } else if (input.equals(".")) {
            handleDecimalPoint();
        } else if ("+-*/÷".contains(input)) {
            handleOperator(input);
        } else if (input.equals("=")) {
            handleEquals();
        } else if (input.equals("⌫")) {
            handleBackspace();
        } else if (input.equals("C")) {
            clearDisplay();
        } else if (input.equals("CE")) {
            clearEntry();
        } else if (input.equals("_")) {
            handleNegative();
        }
    }

    private void handleNumber(int digit) {
        String currentDisplayNumber = calculatorDisplay.getText();
        String newDisplayNumber;

        if (startNewNumber || currentDisplayNumber.equals("0")) {
            newDisplayNumber = Integer.toString(digit);
        } else {
            newDisplayNumber = currentDisplayNumber + digit;
        }
        calculatorDisplay.setText(newDisplayNumber);
        displayNumber = Double.parseDouble(newDisplayNumber);
        startNewNumber = false;
        operatorPressedLast = false;
    }

    private void handleDecimalPoint() {
        if (startNewNumber) {
            calculatorDisplay.setText("0.");
            startNewNumber = false;
        } else if (!calculatorDisplay.getText().contains(".")) {
            calculatorDisplay.setText(calculatorDisplay.getText() + ".");
        }
        operatorPressedLast = false;
    }

    private void handleNegative() {
        setDisplayNumber(displayNumber * -1);
        operatorPressedLast = false;
    }

    private void handleOperator(String operator) {
        if (!operatorPressedLast && !storedOperator.isEmpty()) {
            storedNumber = calculateResult(storedNumber, displayNumber, storedOperator);
            setDisplayNumber(storedNumber);
        } else {
            storedNumber = displayNumber;
        }

        storedOperator = operator;
        operatorPressedLast = true;
        startNewNumber = true;
        operationDisplay.setText(formatDisplayString(storedNumber) + " " + operator);
    }

    private void handleEquals() {
        double result;
        if (storedOperator.isEmpty()) {
            result = displayNumber;
            operationDisplay.setText(formatDisplayString(displayNumber) + " =");
        }
        else {
            result = calculateResult(storedNumber, displayNumber, storedOperator);
            operationDisplay.setText(formatDisplayString(storedNumber) + " " + storedOperator + " " + formatDisplayString(displayNumber) + " =");
        }
        setDisplayNumber(result);
        storedNumber = result;
        storedOperator = "";
        startNewNumber = true;
    }

    private double calculateResult(double num1, double num2, String operator) {
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "÷" -> (num2 != 0) ? num1 / num2 : 0;  // handle division by zero safely
            default -> num2;
        };
    }

    private void handleBackspace() {
        if (startNewNumber) return;

        String currentDisplayNumber = calculatorDisplay.getText();
        if (currentDisplayNumber.length() > 1) {
            String newDisplayNumber = currentDisplayNumber.substring(0, currentDisplayNumber.length() - 1);
            if (newDisplayNumber.equals("-")) {
                setDisplayNumber(0);
            } else {
                setDisplayNumber(Double.parseDouble(newDisplayNumber));
            }
        } else {
            setDisplayNumber(0);
        }
    }

    private void clearDisplay() {
        clearEntry();
        storedNumber = 0;
        storedOperator = "";
        operationDisplay.setText("");
    }

    private void clearEntry() {
        setDisplayNumber(0);
        startNewNumber = true;
        if (operationDisplay.getText().contains("=")) {
            operationDisplay.setText("");
        }
    }

    private void setDisplayNumber(double number) {
        calculatorDisplay.setText(formatDisplayString(number));
        displayNumber = number;
    }

    private String formatDisplayString(double value) {
        return (value == (long) value) ? String.format("%d", (long) value) : String.format("%s", value);
    }

    public void setDisplays(Label calculatorDisplay, Label operationDisplay) {
        this.calculatorDisplay = calculatorDisplay;
        this.operationDisplay = operationDisplay;
    }
}
