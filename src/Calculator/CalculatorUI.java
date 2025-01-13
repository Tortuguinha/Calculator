package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorUI extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField display; // Campo de texto para exibir o cálculo
    private final OperationHandler handler; // Classe separada para lógica das operações

    public CalculatorUI() {
        setTitle("Calculadora Científica");         // Título da janela
        setSize(400, 500);                          // Dimensão da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE);   // Fecha o programa ao clicar no "X"
        setLayout(new BorderLayout());             // Layout principal

        // Inicializa o handler de operações
        handler = new OperationHandler();

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

        // Botões (incluindo exponenciação)
        String[] buttons = {
            "(", ")", "C", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "^", "="
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
        if (isOperator(text) || isParenthesis(text) || "^".equals(text)) {
            button.setBackground(Color.ORANGE); // Operadores e parênteses em laranja
        } else {
            button.setBackground(Color.LIGHT_GRAY); // Outros botões em cinza claro
        }

        // Adiciona funcionalidade ao botão
        button.addActionListener(new ButtonClickListener(text));
        button.setFocusPainted(false); // Remove o destaque ao clicar
        return button;
    }

    // Verifica se o texto do botão é uma operação
    private boolean isOperator(String text) {
        return "+-*/=^".contains(text);
    }

    // Verifica se o texto do botão é um parêntese
    private boolean isParenthesis(String text) {
        return "()".contains(text);
    }

    // Classe interna para tratar os cliques nos botões
    private class ButtonClickListener implements ActionListener {
        private final String command;

        public ButtonClickListener(String command) {
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if ("C".equals(command)) {
                // Limpa o display
                display.setText("");
            } else if ("=".equals(command)) {
                // Calcula o resultado completo
                try {
                    String result = handler.evaluate(display.getText());
                    display.setText(result);
                } catch (Exception ex) {
                    display.setText("Erro");
                }
            } else {
                // Adiciona o comando ao display
                display.setText(display.getText() + command);
            }
        }
    }
}
