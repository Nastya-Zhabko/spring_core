package nastyazhabko.dev;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class TransactionHelper {
    private final SessionFactory sessionFactory;
    private final ThreadLocal<Session> threadSession = new ThreadLocal<>();

    public TransactionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void executeInTransaction(Consumer<Session> action) {
        Session session = threadSession.get();
        boolean isNewSession = false;
        boolean isNewTransaction = false;
        Transaction transaction = null;

        try {
            if (session == null) {
                session = sessionFactory.openSession();
                threadSession.set(session);
                isNewSession = true;
            }

            transaction = session.getTransaction();

            if (!transaction.isActive()) {
                transaction.begin();
                isNewTransaction = true;
            }

            action.accept(session);

            if (isNewTransaction) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (isNewTransaction && transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (isNewSession) {
                threadSession.remove();
                session.close();
            }
        }

    }

    public <T> T executeInTransaction(Function<Session, T> action) {
        Session session = threadSession.get();
        boolean isNewSession = false;
        boolean isNewTransaction = false;
        Transaction transaction = null;


        try {

            if (session == null) {
                session = sessionFactory.openSession();
                threadSession.set(session);
                isNewSession = true;
            }

            transaction = session.getTransaction();

            if (!transaction.isActive()) {
                transaction.begin();
                isNewTransaction = true;
            }

            var result = action.apply(session);

            if (isNewTransaction) {
                transaction.commit();
            }

            return result;
        } catch (Exception e) {
            if (isNewTransaction && transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (isNewSession) {
                threadSession.remove();
                session.close();
            }
        }

    }

    public <T> T executeInSession(Function<Session, T> action) {
        Session session = threadSession.get();
        boolean isNewSession = false;

        try {

            if (session == null) {
                session = sessionFactory.openSession();
                threadSession.set(session);
                isNewSession = true;
            }

            var result = action.apply(session);

            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (isNewSession) {
                threadSession.remove();
                session.close();
            }
        }

    }


}
