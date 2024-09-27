package tobiaomd.calc;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;

public class CalcController {

    private Calc calc;

    public CalcController() {
        calc = new Calc(0.0, 0.0, 0.0);
    }

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
        updateOperandsView();
    }

    @FXML
    private ListView<Double> operandsView;

    @FXML
    private Label operandView;

    @FXML
    void initialize() {
        updateOperandsView();
    }

    private void updateOperandsView() {
        List<Double> operands = operandsView.getItems();
        operands.clear();
        int elementCount = Math.min(calc.getOperandCount(), 3);
        for (int i = 0; i < elementCount; i++) {
            operands.add(calc.peekOperand(elementCount - i - 1));
        }
    }

    private String getOperandString() {
        return operandView.getText();
    }

    private boolean hasOperand() {
        return ! getOperandString().isBlank();
    }

    private double getOperand() {
        return Double.valueOf(operandView.getText());
    }
    
    private void setOperand(String operandString) {
        operandView.setText(operandString); 
    }

    @FXML
    void handleEnter() {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
        } else {
            calc.dup();
        }
        handleClear();
        updateOperandsView();
    }

    private void appendToOperand(String s) {
        // TODO
        String word = getOperandString();
        String newWord = word + s;
        setOperand(newWord);
    }

    @FXML
    void handleDigit(ActionEvent ae) {
        if (ae.getSource() instanceof Labeled l) {
            // TODO append button label to operand
            appendToOperand(l.getText());   
            updateOperandsView();
        }
    }

    @FXML
    void handlePoint() {
        var operandString = getOperandString();
        if (operandString.contains(".")) {
            // TODO remove characters after point
            int index = operandString.indexOf(".");
            setOperand(operandString.substring(0,index + 1));
        } else {
            // TODO append point
            appendToOperand(".");
        }
        updateOperandsView();
    }

    @FXML
    void handleClear() {
        // TODO clear operand
        setOperand("");
        updateOperandsView();
    }

    @FXML
    void handleSwap() {
        // TODO clear operand
        calc.swap();
        updateOperandsView();
    }

    private void performOperation(UnaryOperator<Double> op) {
        // TODO
        calc.performOperation(op);
        handleClear();
        updateOperandsView();
    }

    private void performOperation(boolean swap, BinaryOperator<Double> op) {
        if (hasOperand()) {
            // TODO push operand first
            calc.pushOperand(getOperand());   
        }
        // TODO perform operation, but swap first if needed
        if (swap == true) {
            handleSwap();    
        }
        calc.performOperation(op);
        handleClear();
        updateOperandsView();
    }

    @FXML
    void handleOpAdd() {
        // TODO
        if (!hasOperand()) {
            performOperation( false, (x,y) -> x + y );
        }
        else {
            performOperation( (x) -> x += getOperand());
        }
        updateOperandsView();
    }

    @FXML
    void handleOpSub() {
        // TODO
        if (!hasOperand()) {
            performOperation( false, (x,y) -> x - y );
        }
        else {
            performOperation( (x) -> x -= getOperand());
        }
        updateOperandsView();
    }

    @FXML
    void handleOpMult() {
        // TODO
        if (!hasOperand()) {
            performOperation( false, (x,y) -> x * y );
        }
        else {
            performOperation( (x) -> x *= getOperand());
        }
        updateOperandsView();
    }


    
    @FXML
    void handleOpDivide() {
        // TODO
        if (!hasOperand()) {
            performOperation( false, (x,y) -> x / y );
        }
        else {
            performOperation( (x) -> x /= getOperand());
        }
        updateOperandsView();
    }

    
    @FXML
    void handleOpSquare() {
        // TODO
        calc.pushOperand(Math.sqrt(calc.popOperand()));
        updateOperandsView();
    }

    
    @FXML
    void handleOpPi() {
        // TODO
        calc.pushOperand(Math.PI);
        updateOperandsView();
    }
}
