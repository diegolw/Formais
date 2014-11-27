package manipulador.modelo;

import java.util.ArrayList;

public class Fragmento {
	public State start;
	public ArrayList<State> out;
	public ArrayList<Integer> outChange; //0 stands for change out, 1 stands for change out1;
	
	public Fragmento(){
		start = null;
		out = new ArrayList<State>();
		outChange = new ArrayList<Integer>();
	}
	
	public Fragmento(State s, ArrayList<State> out1){
		start = s;
		out = out1;
		outChange = new ArrayList<Integer>();
	}
	
	
}
