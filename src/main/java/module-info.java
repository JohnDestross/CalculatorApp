module com.johndestross.calculatorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.johndestross.calculatorapp to javafx.fxml;
    exports com.johndestross.calculatorapp;
}