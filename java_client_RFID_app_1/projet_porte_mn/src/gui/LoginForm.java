package gui;

import com.fazecast.jSerialComm.SerialPort;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
//Creating LoginFrame class
public class LoginForm extends JFrame implements ActionListener {
	SerialPort sp;
	
    Container container = getContentPane();
    JLabel ensiasLabel = new JLabel("ensias");
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    ImageIcon iconEnsias = createImageIcon("s.png","ensias");
    ImageIcon icon = createImageIcon("rffid.gif","rfid");
    JLabel resetButton = new JLabel("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");
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

    public LoginForm() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

    }

    public void setLayoutManager() {
        container.setLayout(null);
        container.setBackground(Color.white);
    }

    public void setLocationAndSize() {
    	ensiasLabel.setBounds(5, 5, 180, 150);
    	ensiasLabel.setIcon(iconEnsias);
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        showPassword.setBackground(Color.white);
        loginButton.setBounds(250, 300, 100, 30);
        resetButton.setBounds(140, 200, 800, 800);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        resetButton.setIcon(icon);
        resetButton.setBackground(Color.white);

    }

    public void addComponentsToContainer() {
    	container.add(ensiasLabel);
        container.add(userLabel);
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
      
        showPassword.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of LOGIN button
        if (e.getSource() == loginButton) {
        	
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();
            if(userText.isEmpty() || pwdText.isEmpty()) {
             	UIManager um = new UIManager();
        				um.put("OptionPane.messageForeground", Color.black);
        				um.put("Panel.background", Color.white);
        				um.put("OptionPane.background",Color.white);
        				um.put("OptionPane.okButtonColor", Color.red);
        				JButton button = new JButton("OK");
        				button.setBackground(Color.BLACK);
        				button.setForeground(Color.WHITE);
        				button.addActionListener(new ActionListener() {
        				   @Override
        				   public void actionPerformed(ActionEvent actionEvent) {
        				       JOptionPane.getRootFrame().dispose();
        				   }
        				});
        				JButton[] buttons = { button };
        				JOptionPane.showOptionDialog(null, "remplire tout les champs", "Authentification", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), buttons, buttons[0]);
        		
            	
		
            }else {
            if (userText.equalsIgnoreCase("admin") && pwdText.equalsIgnoreCase("admin")) {

        CompteForm cf = new CompteForm();
        cf.setTitle("compte Form");
        cf.setVisible(true);
        cf.setBounds(10,10,600,600);
        cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cf.setResizable(false);
        cf.show();
        dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
            }
        }
        //Coding Part of RESET button
        if (e.getSource() == resetButton) {
            userTextField.setText("");
            passwordField.setText("");
        }
       //Coding Part of showPassword JCheckBox
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }


        }
    }
}
