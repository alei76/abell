package abell.engine;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public interface Matrix<T extends IdentifiedVector> {
	
	void attach(T item);

    void iterate(Handler<T> handler);

    interface Handler<H extends IdentifiedVector>{

        void handle(H item);

    }
}
