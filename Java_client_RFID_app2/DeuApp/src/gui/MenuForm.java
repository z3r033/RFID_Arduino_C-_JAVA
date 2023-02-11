package gui;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintWriter;
 
public class MenuForm extends JFrame implements ActionListener {
	SerialPort serialPort;
	OutputStream outputStream;
	String dataBuffer=""; 
	String dataBuffer2=""; 
	SerialPort sp;
	
	
	
    Container container = getContentPane();
 
    JButton chngmdpButton = new JButton("changer mot de pass");
    JButton logout = new JButton("logout");
    JLabel ensiasLabel = new JLabel("ensias");
    JLabel resetButton = new JLabel("RESET");
    ImageIcon iconEnsias = createImageIcon("s.png","ensias");
    ImageIcon icon = createImageIcon("rffid.gif","rfid");
    public MenuForm() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    
    }
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    public void setLayoutManager() {
        container.setLayout(null);
        container.setBackground(Color.white);
    }

    public void setLocationAndSize() {
    
    	chngmdpButton.setBounds(200, 300, 250, 30);
    	chngmdpButton.setBackground(Color.BLACK);
    	chngmdpButton.setForeground(Color.WHITE);
    	logout.setBounds(200, 360, 250, 30);
    	logout.setBackground(Color.BLACK);
    	logout.setForeground(Color.WHITE);
    	ensiasLabel.setBounds(5, 5, 180, 150);
    	ensiasLabel.setIcon(iconEnsias);
    	resetButton.setBounds(140, 200, 800, 800);
        resetButton.setIcon(icon);
        resetButton.setBackground(Color.white);

    }

    public void addComponentsToContainer() {

        container.add(chngmdpButton);
        container.add(logout);
        container.add(resetButton);
        container.add(ensiasLabel);
    }

    public void addActionEvent() {
    	chngmdpButton.addActionListener(this);
    	logout.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of LOGIN button
        if (e.getSource() == chngmdpButton) {
            MotPassChangeForm cf = new MotPassChangeForm();
	        cf.setTitle("Changer mot de  pass");
	        cf.setVisible(true);
	        cf.setBounds(10,10,600,600);
	        cf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        cf.setResizable(false);
	        cf.show();
	      //  this.setVisible(false);
	     //   dispose();
        }
        if (e.getSource() == logout) {
            LoginForm cf = new LoginForm();
	        cf.setTitle("Login Auths");
	        cf.setVisible(true);
	        cf.setBounds(10,10,600,600);
	        cf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        cf.setResizable(false);
	        cf.show();
	      //  this.setVisible(false);
	        dispose();
        }

    }
}
