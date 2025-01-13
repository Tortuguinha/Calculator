package Calculator;

import java.util.Stack;

public class OperationHandler {

    // Avalia uma expressão matemática completa
    public String evaluate(String expression) throws Exception {
        double result = evaluatePostfix(infixToPostfix(expression));
        return String.valueOf(result);
    }

    // Calcula a raiz quadrada de um número
    public String squareRoot(String value) {
        try {
            double number = Double.parseDouble(value);
            if (number < 0) {
                return "Erro"; // Raiz de número negativo não é permitida
            }
            return String.valueOf(Math.sqrt(number));
        } catch (NumberFormatException e) {
            return "Erro"; // Tratamento para valores inválidos
        }
    }

    // Converte expressão infixa para postfix (notação polonesa reversa)
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
            } else if ("+-*/^√".indexOf(c) >= 0) {
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
            if ("+-*/^√".contains(token)) {
                double result;
                if ("√".equals(token)) {
                    // Raiz quadrada: opera em apenas um número
                    double a = stack.pop();
                    if (a < 0) {
                        throw new IllegalArgumentException("Raiz de número negativo");
                    }
                    result = Math.sqrt(a);
                } else {
                    // Operações binárias
                    double b = stack.pop();
                    double a = stack.pop();
                    switch (token.charAt(0)) {
                        case '+': result = a + b; break;
                        case '-': result = a - b; break;
                        case '*': result = a * b; break;
                        case '/': result = a / b; break;
                        case '^': result = Math.pow(a, b); break;
                        default: throw new IllegalArgumentException("Operador desconhecido");
                    }
                }
                stack.push(result);
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
        if (op == '^') return 3;
        if (op == '√') return 4; // Maior precedência para raiz quadrada
        return 0;
    }
}
