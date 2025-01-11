package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculatorUI extends JFrame {

    private JTextField display; // Campo de texto para exibir o cálculo

    public CalculatorUI() {
        setTitle("Calculadora");         // Título da janela
        setSize(350, 450);               // Dimensão da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Fecha o programa ao clicar no "X"
        setLayout(new BorderLayout());   // Layout principal

        // Campo de exibição (display)
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 30)); // Fonte do display
        display.setHorizontalAlignment(JTextField.RIGHT);  // Alinhamento à direita
        display.setEditable(false);                       // Não permite edição manual
        display.setBackground(Color.WHITE);               // Fundo branco
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margens
        add(display, BorderLayout.NORTH);                 // Adiciona o display na parte superior

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5)); // Grade 5x4 com espaçamento

        // Botões (incluindo parênteses)
        String[] buttons = {
            "(", ")", "C", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", ""
        };

        for (String text : buttons) {
            JButton button = createButton(text); // Cria os botões
            buttonPanel.add(button);             // Adiciona ao painel
        }

        add(buttonPanel, BorderLayout.CENTER); // Adiciona o painel de botões ao centro
    }

    // Método auxiliar para criar botões com estilo uniforme
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20)); // Estilo da fonte
        if (text.isEmpty()) {
            button.setEnabled(false); // Botão vazio (apenas espaço)
        } else if (isOperator(text) || isParenthesis(text)) {
            button.setBackground(Color.ORANGE); // Operadores e parênteses em laranja
        } else {
            button.setBackground(Color.LIGHT_GRAY); // Outros botões em cinza claro
        }

        // Adiciona funcionalidade ao botão
        button.addActionListener(new ButtonClickListener());
        button.setFocusPainted(false); // Remove o destaque ao clicar
        return button;
    }

    // Verifica se o texto do botão é uma operação
    private boolean isOperator(String text) {
        return "+-*/=".contains(text);
    }

    // Verifica se o texto do botão é um parêntese
    private boolean isParenthesis(String text) {
        return "()".contains(text);
    }

    // Classe interna para tratar os cliques nos botões
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText(); // Texto do botão clicado

            if ("C".equals(command)) {
                // Limpa o display
                display.setText("");
            } else if ("=".equals(command)) {
                // Calcula o resultado da expressão completa
                try {
                    String expression = display.getText(); // Obtém a expressão do display
                    double result = evaluateExpression(expression); // Avalia a expressão
                    display.setText(String.valueOf(result)); // Mostra o resultado
                } catch (Exception ex) {
                    display.setText("Erro");
                }
            } else {
                // Adiciona o texto do botão ao display
                display.setText(display.getText() + command);
            }
        }

        // Método para avaliar a expressão usando ScriptEngine
        private double evaluateExpression(String expression) throws ScriptException {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript"); // Avaliador de expressões
            Object result = engine.eval(expression); // Avalia a expressão
            return Double.parseDouble(result.toString()); // Retorna o resultado como double
        }
    }
}
