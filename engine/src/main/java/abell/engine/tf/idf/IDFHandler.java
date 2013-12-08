package abell.engine.tf.idf;

public interface IDFHandler {

    void increaseDocument();

    void increaseDocument(int count);

    long countDocument();

    void increaseTerm(int index);
    
    void increaseTerm(int index, int count);

    long countTerm(int index);
    
	void resolve();
	
}
