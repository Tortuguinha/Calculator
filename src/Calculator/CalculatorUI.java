package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CalculatorUI extends JFrame {

    private JTextField display; // Campo de texto para exibir o cálculo
    private boolean isResultDisplayed = false; // Flag para verificar se o resultado foi exibido

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
                isResultDisplayed = false; // Reseta o estado
            } else if ("=".equals(command)) {
                // Calcula o resultado da expressão completa
                try {
                    String expression = display.getText(); // Obtém a expressão do display
                    double result = evaluateExpression(expression); // Avalia a expressão
                    display.setText(String.valueOf(result)); // Mostra o resultado
                    isResultDisplayed = true; // Marca que o resultado foi exibido
                } catch (Exception ex) {
                    display.setText("Erro");
                    isResultDisplayed = false; // Erro, reseta o estado
                }
            } else {
                // Se o resultado foi exibido e o botão clicado é um número, limpa o display
                if (isResultDisplayed && Character.isDigit(command.charAt(0))) {
                    display.setText(command); // Começa um novo cálculo com o número clicado
                    isResultDisplayed = false;
                } else {
                    // Caso contrário, adiciona o texto ao display
                    display.setText(display.getText() + command);
                    isResultDisplayed = false;
                }
            }
        }

        // Método para avaliar a expressão usando pilhas
        private double evaluateExpression(String expression) throws Exception {
            return evaluatePostfix(infixToPostfix(expression));
        }

        // Converte a expressão infixa para postfix (notação polonesa reversa)
        private String infixToPostfix(String infix) {
            StringBuilder postfix = new StringBuilder();
            Stack<Character> stack = new Stack<>();

            for (char c : infix.toCharArray()) {
                if (Character.isDigit(c) || c == '.') {
                    postfix.append(c);
                } else if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postfix.append(' ').append(stack.pop());
                    }
                    stack.pop();
                } else if ("+-*/".indexOf(c) >= 0) {
                    postfix.append(' ');
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                        postfix.append(stack.pop()).append(' ');
                    }
                    stack.push(c);
                }
            }
            while (!stack.isEmpty()) {
                postfix.append(' ').append(stack.pop());
            }
            return postfix.toString();
        }

        // Avalia a expressão em postfix
        private double evaluatePostfix(String postfix) {
            Stack<Double> stack = new Stack<>();
            for (String token : postfix.split("\\s")) {
                if (token.isEmpty()) continue;
                if ("+-*/".contains(token)) {
                    double b = stack.pop();
                    double a = stack.pop();
                    switch (token.charAt(0)) {
                        case '+': stack.push(a + b); break;
                        case '-': stack.push(a - b); break;
                        case '*': stack.push(a * b); break;
                        case '/': stack.push(a / b); break;
                    }
                } else {
                    stack.push(Double.parseDouble(token));
                }
            }
            return stack.pop();
        }

        // Define a precedência dos operadores
        private int precedence(char op) {
            if (op == '+' || op == '-') return 1;
            if (op == '*' || op == '/') return 2;
            return 0;
        }
    }
}
