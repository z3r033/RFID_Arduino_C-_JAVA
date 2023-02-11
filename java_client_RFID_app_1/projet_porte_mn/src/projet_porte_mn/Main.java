package projet_porte_mn;

import javax.swing.JFrame;

import gui.CompteForm;
import gui.LoginForm;

public class Main {

	    public static void main(String[] a){
	       
	        LoginForm frame=new LoginForm();
	        frame.setTitle("Login Form");
	        frame.setVisible(true);
	        frame.setBounds(10,10,600,600);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setResizable(false);
	        
	    }
	 
}
