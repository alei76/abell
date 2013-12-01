package abell.engine;

import java.io.Reader;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public class TTSRecommender implements Recommender {
	
	TTSRecommender() {
		
	}

    @Override
    public Fetcher fetch(long uid) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attachUser(long uid) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
    @Override
    public void attachRelationship(long uid1, long uid2, float score) {
    	
    }

    @Override
    public void attachContent(long cid, Reader reader) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attachContent(long cid, CharSequence chars) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
}
