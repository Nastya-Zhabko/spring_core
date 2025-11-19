package nastyazhabko.dev.services;

import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class GetConsoleInputValue {
    private final Scanner sc;
    private final static String stringIncorrectMessage = "Введите корректную строку!";

    public GetConsoleInputValue(Scanner sc) {
        this.sc = sc;
    }

    public String getStringValue(String text) {
        System.out.println(text);
        String result;

        try {
            result = sc.nextLine();
            return result;
        } catch (InputMismatchException e) {
            throw new InputMismatchException(stringIncorrectMessage);
        }
    }

    public int getIntValue(String text) {
        System.out.println(text);
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                throw new InputMismatchException(stringIncorrectMessage);
            }
        }
    }
}
