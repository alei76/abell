package abell.engine;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public interface MatrixStorage<T extends IdentifiedVector> {

    void iterate(Handler handler);

    interface Handler{

        void handle(T item);

    }
}
