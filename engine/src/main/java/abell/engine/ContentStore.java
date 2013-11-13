package abell.engine;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface ContentStore {

    void iterate(Handler handler);

    interface Handler{

        void handle(Content item);

    }

}
