package nastyazhabko.dev.services;

import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class GetConsoleInputValue {
    private final Scanner sc = new Scanner(System.in);
    private final String stringIncorrectMessage = "Введите корректную строку!";

    public String getStringValue() {
        String result;

        try {
            result = sc.nextLine();
            return result;
        } catch (InputMismatchException e) {
            throw new InputMismatchException(stringIncorrectMessage);
        }
    }

    public int getIntValue() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                throw new InputMismatchException(stringIncorrectMessage);
            }
        }
    }
}
