package abell.engine;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface Recommender {

    Fetcher fetch(long uid);

    void attachUser(long uid);
    
    void attachRelationship(long uid1, long uid2, float score);

    void attachContent(long cid, Reader reader) throws IOException;

    void attachContent(long cid, CharSequence chars);

    public interface Fetcher {

        int count();

        List<Long> list(int offset, int count);

    }

}
