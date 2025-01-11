package Main;

import Calculator.CalculatorUI;

public class Main {
    public static void main(String[] args) {
        // Inicializa a interface gráfica
        javax.swing.SwingUtilities.invokeLater(() -> {
            CalculatorUI calculator = new CalculatorUI();
            calculator.setVisible(true); // Exibe a janela da calculadora
        });
    }
}
