import java.util.*;
import java.io.*;


public class tm {

	static char [] alphabet = new char [] {'a', 'b', 'B', '#'};
	char turingTape[];
	int headOfTape;
	int currentState;
	static String status;
	out[][] table;
	
	public tm(int numStates ){
		//initialize everything for each tm to simulate
		currentState = 0; 									
		headOfTape = 0;
		
		// show that the machine is running and not in accept or reject
		tm.status = run();
		
		//initialize
		table = new out[numStates][4];
		for (int i=0; i < numStates; i++) 
		{
			
			for (int j=0; j < 4; j++) 
			{
				table[i][j] = null;
			}
		}
		
	}
	public void reset(String st) {

		// Give some extra room here.
		turingTape = new char[2*st.length()];
		headOfTape = 0;
		currentState = 0;
		tm.status = run();
		
		
		// Copy over tape contents.
		for (int i=0; i<st.length(); i++)
			turingTape[i] = st.charAt(i);
			
		// Pad the end of the tape with blanks.
		for (int i=st.length(); i<turingTape.length; i++)
			turingTape[i] = 'B';		
		
	}
	
	
	private static int referChar(char a) {
		
		if (a == 'a')
			return 0;
		else if (a == 'b')
			return 1;
		else if (a == '#')
			return 2;
			
		else if ( a == 'B')
			return 3;
		return 3;
	}
	
	public void addTransition (char in, int outputState, char direction, char out, int inputState ) {
		
		// Set the direction.
		boolean whichWay = true;
		if (direction == 'L')
			whichWay = false;
			
		out tmp = new out(out, whichWay, outputState);
		table[inputState][referChar(in)] = tmp;	
	}
	
	public String run() {
		return "run";
	}
	public String accept() {
		return "accept";
	}
	
	public String reject() {
		return "reject";
	}	
	
	
	public boolean step() {
		
		// Can only do steps when machine is turned "on"
		if (tm.status != "run")
			return false;

		char ch = turingTape[headOfTape];
		
		out newPos = table[currentState][referChar(ch)];
		
		// This is if we have no moves to make
		if (newPos == null)
			return false;
			

		currentState = newPos.getState();
		turingTape[headOfTape] = newPos.getWriteChar();
		
		
		
		if (currentState == 1) {
			tm.status = accept();
			
			return true;
		}
		
		
		else if (currentState == 2) {
			tm.status = reject();
			
			return false;
		
		}
		
		// this step is overly complicated
		if ((headOfTape == 0 && newPos.getDir()) || 
		    (headOfTape > 0 && headOfTape < turingTape.length-1) ||
		    (headOfTape == turingTape.length-1 && !newPos.getDir())) {
		    	
		    
		    int step = -1;
		    if (newPos.getDir())
		    	step = 1;
		    		
		    headOfTape = headOfTape + step;
		}
		
		// make more space if we go too far right. If we go too far left, 
		// it doesn't matter state stays the same so do nothing
		else if (headOfTape == turingTape.length-1 && newPos.getDir()) {
			
			int tempLen = (turingTape.length + turingTape.length - 1);
			char[] temp = new char[tempLen];
			
			for (int i=0; i<turingTape.length; i++)
				temp[i] = turingTape[i];
			
			for (int i=turingTape.length; i<temp.length; i++)
				temp[i] = 'B';
				
			
			turingTape = temp;
			headOfTape ++;
		}
			
		
		return true;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		
		@SuppressWarnings("resource")
		Scanner reading = new Scanner(new File("tm.in"));
		
		// First scan in the number of Turing Machines
		int numTMs = reading.nextInt(); 	
		
		
		// Loop through each machine to scan input for each one
		for( int i = 0; i < numTMs; i++ )	
		{
			int numStates = reading.nextInt();
			int numRules = reading.nextInt();
			
			tm turingMachine = new tm(numStates);
			
			//The  next  numRules  lines  will  contain  one  rule  each.  Each  has  five  items
			//separated by spaces : input state, character to read, output state int, character to write
			//						L or R representing a left or right move
			for ( int j = 0; j < numRules; j++ )
			{
				int inputState = reading.nextInt();
				String charRead = reading.next(".");
				int outputState = reading.nextInt();
				String charWrite = reading.next(".");
				String leftOrRight = reading.next(".");
				// Now we need to call on our transition here
				turingMachine.addTransition(charRead.charAt(0), outputState, leftOrRight.charAt(0)
											, charWrite.charAt(0), inputState);
				
			}
			
			System.out.println("Machine #"+(i+1)+":");
			// Now we scan the number of input strings and the max amount of steps to run each simulation
			int numStrings = reading.nextInt();
			int maxSteps = reading.nextInt();
			
			for( int stringTest = 0; stringTest < numStrings; stringTest ++ )
			{
				String test = reading.next();
				turingMachine.reset(test);
				
				boolean ans = false;
				boolean stop = false;
				for (int k=0; i < maxSteps; k++) {
					
					turingMachine.step();
				
				
					if ( tm.status == "accept") {
						ans = true;
						stop = true;
						
						break;
					}
					else if ( tm.status == "reject") {
						stop = true;
						break;
					}

					
				}
				
				if (ans) 
					System.out.println(test+": YES");
				
				else if (stop) 
					System.out.println(test+": NO");
				
				else
					System.out.println(test+": DOES NOT HALT IN "+maxSteps+" STEPS");
				
				
				
			} 
			System.out.println();
			
		}
	}
		
}
class out {
	
	char writeChar;
	boolean LorR;
	int state;
	
	public boolean getDir() {
		return LorR;
	}
	
	public int getState() {
		return state;
	}
	
	public char getWriteChar() {
		return writeChar;
	}
	
	public out(char a, boolean direction, int newState) {
		writeChar = a;
		LorR = direction;
		state = newState;
	} 
}



