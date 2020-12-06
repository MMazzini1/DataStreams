package pecs;

import TestModel.Employer;
import TestModel.GoodEmployer;
import TestModel.Person;

import java.util.ArrayList;
import java.util.Collection;

public class Asd<T> {

	public static void main(String[] args) {

		Asd<Employer> goodEmployerAsd = new Asd<>();

		goodEmployerAsd.popAll(new ArrayList<Person>());
		goodEmployerAsd.popAll(new ArrayList<Employer>());
		//goodEmployerAsd.popAll(new ArrayList<GoodEmployer>());//

		/**
		 * en resumen, el metodo popAll debe aceptar una lista<? super T></?>
		 * porque va a consumir Ts. entonces tiene que ser superclase de T , para aceptarlo
		 *
		 *
		 *
		 */

	}

	public void pushAll(Iterable<? extends T> src) {
		for (T e : src)
			push(e);
	}

	private void push(T e) {
	}

	public void popAll(Collection<? super T> dst) {
		while (true)
			dst.add(pop());
	}

	private T pop() {
		return null;
	}

}
