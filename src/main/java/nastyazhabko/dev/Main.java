package nastyazhabko.dev;

import nastyazhabko.dev.commandhandlers.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@ComponentScan(basePackages = "nastyazhabko.dev")
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        OperationsConsoleListener operationsConsoleListener = context.getBean(OperationsConsoleListener.class);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(operationsConsoleListener);
        executorService.shutdown();
    }
}
