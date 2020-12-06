package structure_experimental;

import TestModel.Person;

import java.util.ArrayList;
import java.util.List;

public class ListDataSimple2<This, Original>{

	public static void main(String[] args) {

		ListDataSimple2<Person ,Person> of = of(new ArrayList<Person>());
		ListDataSimple2<ListDataSimple2<Person, Person>, Person> wrap = of.wrap();
		ListDataSimple2<ListDataSimple2<ListDataSimple2<Person, Person>, Person>, Person> wrap1 = wrap.wrap();
		ListDataSimple2<ListDataSimple2<Person, Person>, Person> unwrap = wrap1.unwrap(Person.class);
		ListDataSimple2<Person, Person> unwrap1 = unwrap.unwrap(Person.class);





		//TODO ocultar 2do tipo
		//TODO void en todo caso si no se puede

		//		Helper<Person> of = of(new ArrayList<Person>());
		//
		//		ListDataSimple2<ListDataSimple2<Person>> wrap = of.wrap();
		//
		//		ListDataSimple2<Person> unwrap = wrap.unwrap();
		//
		//		Person unwrap1 = unwrap.unwrap();


	}

	public ListDataSimple2() {
	}

	public static <A> ListDataSimple2<A,A> of(List<A> list){
		return null;
	}

	public ListDataSimple2<ListDataSimple2<This, Original>, Original> wrap(){
		return null;
	}

	public This unwrap(Class<Original> original){
		return null;
	}
}