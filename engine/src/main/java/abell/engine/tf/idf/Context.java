package abell.engine.tf.idf;

public interface Context {

    void increaseDocument();

    void increaseDocument(int count);

    int countDocument();

    void increaseTerm(CharSequence chars);
    
    void increaseTerm(CharSequence chars, int count);

    int countTerm(CharSequence chars);
    
	void resolve();
	
}
