package abell.engine.algorithm;

import abell.engine.Recommender;

import java.io.InputStream;
import java.net.URL;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public class TTSRecommender implements Recommender {

    @Override
    public Fetcher fetchForUser(long uid) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attachUser(long uid) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attachContent(long uid, InputStream inputStream) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attachContent(long uid, URL url) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attachContent(long uid, CharSequence chars) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
