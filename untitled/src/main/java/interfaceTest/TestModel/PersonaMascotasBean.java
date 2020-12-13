package interfaceTest.TestModel;

import interfaceTest.LStream.LStream;

public class PersonaMascotasBean {

	private  LStream<Person> listaPersonas;
	private  LStream<Perro> listaMascotas;
	private String personaName;
	private String mascotaName;
	private Adress adress;

	public LStream<Person> getListaPersonas() {
		return listaPersonas;
	}

	public void setListaPersonas(LStream<Person> listaPersonas) {
		this.listaPersonas = listaPersonas;
	}

	public LStream<Perro> getListaMascotas() {
		return listaMascotas;
	}

	public void setListaMascotas(LStream<Perro> listaMascotas) {
		this.listaMascotas = listaMascotas;
	}

	public PersonaMascotasBean(String personaName, String mascotaName, Adress adress) {
		this.personaName = personaName;
		this.mascotaName = mascotaName;
		this.adress = adress;
	}

	public PersonaMascotasBean(LStream<Person> listaPersonas, LStream<Perro> listaMascotas) {

		this.listaPersonas = listaPersonas;
		this.listaMascotas = listaMascotas;
	}

	public String getPersonaName() {
		return personaName;
	}

	public void setPersonaName(String personaName) {
		this.personaName = personaName;
	}

	public String getMascotaName() {
		return mascotaName;
	}

	public void setMascotaName(String mascotaName) {
		this.mascotaName = mascotaName;
	}

	public Adress getAdress() {
		return adress;
	}

	public void setAdress(Adress adress) {
		this.adress = adress;
	}
}
