package Disassembler;

import javax.swing.JOptionPane;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			String choice = JOptionPane.showInputDialog("Input:\n0 to convert C++ to Assembly\n1 to convert Assembly to C++");
			if(choice.equalsIgnoreCase("0")){
				Try2 convert = new Try2();
				convert.main(args);
			}else if(choice.equalsIgnoreCase("1")){
				Try1 convert = new Try1();
				convert.main(args);
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "It seems you've pressed the wrong button.");
		}
	}

}
