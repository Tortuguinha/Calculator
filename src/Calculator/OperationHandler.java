package Calculator;

import java.util.Stack;

public class OperationHandler {

    // Avalia uma expressão matemática completa
    public String evaluate(String expression) throws Exception {
        double result = evaluatePostfix(infixToPostfix(expression));
        return String.valueOf(result);
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
            } else if ("+-*/^".indexOf(c) >= 0) {
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
            if ("+-*/^".contains(token)) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/': stack.push(a / b); break;
                    case '^': stack.push(Math.pow(a, b)); break;
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
        if (op == '^') return 3;
        return 0;
    }
}
