package structure_experimental;

import TestModel.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDataSimple<This, Before, Original>  {

	public static void main(String[] args) {

		ListDataSimple<Person, Person, Person> of = of(new ArrayList<Person>());
		ListDataSimple<ListDataSimple<Person, Person, Person>, Person, Person> wrap = of.wrap();
		ListDataSimple<ListDataSimple<ListDataSimple<Person, Person, Person>, Person, Person>, ListDataSimple<Person, Person, Person>, Person> wrap1 = wrap.wrap();
		ListDataSimple<ListDataSimple<Person, Person, Person>, Person, Person> unwrap2 = wrap1.unwrap(Person.class);
		ListDataSimple<Person, Person, Person> unwrap3 = unwrap2.unwrap(Person.class);


		ListDataSimple<Person, Person, Person> unwrap = wrap.unwrap(Person.class);




		//TODO ocultar 2do tipo
		//TODO void en todo caso si no se puede

//		Helper<Person> of = of(new ArrayList<Person>());
//
//		ListDataSimple<ListDataSimple<Person>> wrap = of.wrap();
//
//		ListDataSimple<Person> unwrap = wrap.unwrap();
//
//		Person unwrap1 = unwrap.unwrap();


	}

	public ListDataSimple() {
	}

	public static <A> ListDataSimple<A,A,A> of(List<A> list){
		return null;
	}

	public ListDataSimple<ListDataSimple<This, Before, Original>, This, Original> wrap(){
		return null;
	}

	public This unwrap(Class<Original> original){
		return null;
	}
}
