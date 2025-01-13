package Calculator;

public class DisplayManager {
    private boolean isResultDisplayed = false; // Flag para verificar se o resultado foi exibido

    // Retorna o texto atualizado do display com base na entrada do usuário
    public String handleInput(String currentDisplay, String input, boolean isOperator) {
        if (isResultDisplayed) {
            if (!isOperator) {
                // Se for um número, limpa o display para iniciar um novo cálculo
                isResultDisplayed = false;
                return input;
            }
            // Se for um operador, adiciona ao display sem limpar
            isResultDisplayed = false;
            return currentDisplay + input;
        }
        // Caso normal: adiciona o input ao display
        return currentDisplay + input;
    }

    // Marca que o resultado foi exibido
    public void setResultDisplayed(boolean isDisplayed) {
        isResultDisplayed = isDisplayed;
    }

    // Retorna se o último valor no display é um resultado
    public boolean isResultDisplayed() {
        return isResultDisplayed;
    }
}
