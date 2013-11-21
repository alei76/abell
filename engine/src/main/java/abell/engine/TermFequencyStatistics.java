package abell.engine;

public interface TermFequencyStatistics {

    int incr();

    int count();

    int incr(CharSequence chars);

    int count(CharSequence chars);
	
}
