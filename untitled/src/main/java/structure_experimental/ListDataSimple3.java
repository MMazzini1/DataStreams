package structure_experimental;

import TestModel.Person;
import org.omg.CORBA.ORB;

import java.util.ArrayList;
import java.util.List;

public class ListDataSimple3<This, Original> extends HelperBase<This, Original> {

	public static void main(String[] args) {

//		ListDataSimple3<Person,Person> of = of(new ArrayList<Person>());
//		ListDataSimple3<ListDataSimple3<Person, Person>, Person> wrap = of.wrap();
//		ListDataSimple3<ListDataSimple3<ListDataSimple3<Person, Person>, Person>, Person> wrap1 = wrap.wrap();
//		ListDataSimple3<ListDataSimple3<Person, Person>, Person> unwrap = wrap1.unwrap(Person.class);
//		ListDataSimple3<Person, Person> unwrap1 = unwrap.unwrap(Person.class);

		Helper2<Person, Person> of = of(new ArrayList<Person>());
		Helper2<Helper<Person>, Person> wrap = of.wrap();
		Helper2<Helper<Helper<Person>>, Person> wrap1 = wrap.wrap();
		Helper<Helper<Person>> unwrap = wrap1.unwrap(Person.class);




		//TODO ocultar 2do tipo
		//TODO void en todo caso si no se puede

		//		Helper<Person> of = of(new ArrayList<Person>());
		//
		//		ListDataSimple3<ListDataSimple3<Person>> wrap = of.wrap();
		//
		//		ListDataSimple3<Person> unwrap = wrap.unwrap();
		//
		//		Person unwrap1 = unwrap.unwrap();


	}

	public ListDataSimple3() {
	}

	public static <A> Helper2<A,A> of(List<A> list){
		return null;
	}

	public Helper2<Helper<This>, Original> wrap(){
		return null;
	}

	public This unwrap(Class<Original> original){
		return null;
	}


}