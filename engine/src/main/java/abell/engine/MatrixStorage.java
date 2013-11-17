package abell.engine;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public interface MatrixStorage<T extends IdentifiedVector> {

    void iterate(Handler<T> handler);

    interface Handler<H extends IdentifiedVector>{

        void handle(H item);

    }
}
