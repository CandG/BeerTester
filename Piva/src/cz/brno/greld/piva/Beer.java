package cz.brno.greld.piva;

public class Beer {
	private int id;
	private String name;

	public Beer(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	
}
