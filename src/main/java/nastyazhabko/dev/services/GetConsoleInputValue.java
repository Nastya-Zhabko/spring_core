package nastyazhabko.dev.services;

import java.util.InputMismatchException;
import java.util.Scanner;


public class GetConsoleInputValue {
    private static final Scanner sc = new Scanner(System.in);

    public static String getStringValue(String text) {
        System.out.println(text);
        String result;

        try {
            result = sc.nextLine();
            return result;
        } catch (InputMismatchException e) {
            System.out.println("Введите корректную строку!");
            return null;
        }
    }

    public static int getIntValue(String text) {
        System.out.println(text);
        int result;

        try {
            result = sc.nextInt();
            sc.nextLine();
            return result;
        } catch (InputMismatchException e) {
            System.out.println("Введите корректную строку!");
            return 0;
        }
    }
}
