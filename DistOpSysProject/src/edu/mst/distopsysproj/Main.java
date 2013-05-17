package edu.mst.distopsysproj;

public class Main {

	/**
	 * @param argumentss
	 */
	public static void main(String[] args) {
		Boolean isTwoVisitors = Boolean.FALSE;
		
		if(args.length != 0){
			isTwoVisitors = Boolean.valueOf(args[0]);
		}
		
		String[] arguments = new String[5];
		arguments[0] = "gui:edu.mst.distopsysproj.gui.GUI";
		arguments[1] = "person1:edu.mst.distopsysproj.person.Person("+ isTwoVisitors.toString() +")";
		arguments[2] = "person2:edu.mst.distopsysproj.person.Person("+ isTwoVisitors.toString() +")";
		arguments[3] = "person3:edu.mst.distopsysproj.person.Person("+ isTwoVisitors.toString() +")";
		arguments[4] = "person4:edu.mst.distopsysproj.person.Person("+ isTwoVisitors.toString() +")";
		jade.Boot.main(arguments);
	}

}
