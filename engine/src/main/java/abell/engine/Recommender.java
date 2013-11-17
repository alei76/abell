package abell.engine;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface Recommender {

    Fetcher fetch(long uid);

    void attachUser(long uid);
    
    void attachRelationship(long uid1, long uid2);

    void attachContent(long uid, InputStream inputStream);

    void attachContent(long uid, URL url);

    void attachContent(long uid, CharSequence chars);

    public interface Fetcher {

        int count();

        List<Long> list(int offset, int count);

    }

}
