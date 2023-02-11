package gui;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintWriter;
 

public class LoginForm extends JFrame implements ActionListener {
	SerialPort serialPort;
	OutputStream outputStream;
	String dataBuffer=""; 
	String dataBuffer2=""; 
	SerialPort sp;

	private void Serial_EventBasedReading(SerialPort activePort) {
	 
		activePort.addDataListener(new SerialPortDataListener() {
			
			@Override
			public void serialEvent(SerialPortEvent ev) {
				// TODO Auto-generated method stub
				if(ev.getEventType()==SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
					byte[] newData = ev.getReceivedData();
					for(int i=0;i<newData.length;i++) {
						dataBuffer += (char) newData[i];				
					}
					dataBuffer2 +="\n"+ dataBuffer;
					
					if(dataBuffer.contains("0xsuccessx0")) {
						System.out.println("success =======");
						sp.closePort();
					        MenuForm cf = new MenuForm();
					        cf.setTitle("compte Form");
					        cf.setVisible(true);
					        cf.setBounds(10,10,600,600);
					        cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					        cf.setResizable(false);
					        cf.show();
					        dispose();
					}else if(dataBuffer.contains("0xfailedx0")) {
						System.out.println("Failed =======");	
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
						JOptionPane.showOptionDialog(null, "Mot de pass ou user incorrect", "Authentification", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), buttons, buttons[0]);
				
					}
					if(dataBuffer.contains("xlp//")&&dataBuffer.contains("#")) {

						String[] arrOfStr = dataBuffer.split("//");
						System.out.println("===================");
						String[] login = arrOfStr[2].split("#");
						System.out.println(arrOfStr[2]);
						System.out.println("===================");
				
					}
					System.out.println(dataBuffer);
					
					  textArea.setText(dataBuffer2);
						
					    textArea.setCaretPosition(textArea.getDocument().getLength() - 1);
				//		}
					    
					    if(dataBuffer2.length()>300) {
					    	textArea.setText("");
					    	dataBuffer2="";
					    }
					dataBuffer="";
				}
			
			}
			
			@Override
			public int getListeningEvents() {
				// TODO Auto-generated method stub
				return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
			}
		});
	}
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
    JPanel panel = new JPanel(new GridLayout(2,3));
    JTextArea textArea = new JTextArea("",5, 20);
    JScrollPane scrollPane = new JScrollPane();
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
    	SerialPort[] list= SerialPort.getCommPorts();
		sp =SerialPort.getCommPorts()[0];
		sp.setComPortParameters(9600, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
    	sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
    	
    	if(sp.openPort()) {
    		System.out.println("port is opened : ");		
    	}else {
    		System.out.println("port is not opened :");
    		return;
    	}
    	Serial_EventBasedReading(sp);
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
        
        panel.setBounds(10, 400, 200, 800);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.setBackground(Color.white);
        scrollPane.add(textArea);
        scrollPane.setViewportView(textArea);

 
    }

    public void addComponentsToContainer() {
    	container.add(ensiasLabel);
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    
        panel.add(textArea);
        container.add(panel);
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
            	Thread thread = new Thread() {
    				@Override public void run() {
    				try {	Thread.sleep(100);}catch(Exception e) {}
    				PrintWriter output = new PrintWriter(sp.getOutputStream());
    				output.print("LOG,"+userTextField.getText()+"#,"+passwordField.getText()+"#");
    				output.flush();
    					
    				}
    			};
    			thread.start();
        		try {
    				Thread.sleep(1000);
    			} catch (InterruptedException e2) {
    				// TODO Auto-generated catch block
    				e2.printStackTrace();
    			}
       
            }
     
        }
     
        if (e.getSource() == resetButton) {
            userTextField.setText("");
            passwordField.setText("");
        }
     
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }


        }
    }
}
