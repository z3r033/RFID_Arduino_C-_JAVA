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
 

public class MotPassChangeForm extends JFrame implements ActionListener {
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
					System.out.println(dataBuffer);	
					
					  textArea.setText(dataBuffer2);
						
					    textArea.setCaretPosition(textArea.getDocument().getLength() - 1);
		
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
    JLabel mdpLabel = new JLabel("mot de pass");
    JLabel mdp2Label = new JLabel("repeat mdp");
    JTextField mdpTextField = new JTextField();
    JTextField mdp2TextField = new JTextField();
    JButton validerButton = new JButton("valider");
    JLabel ensiasLabel = new JLabel("ensias");
    JLabel resetButton = new JLabel("RESET");
    ImageIcon iconEnsias = createImageIcon("s.png","ensias");
    ImageIcon icon = createImageIcon("rffid.gif","rfid");
    JPanel panel = new JPanel(new GridLayout(2,3));
    JTextArea textArea = new JTextArea("",5, 20);
    JScrollPane scrollPane = new JScrollPane();
    public MotPassChangeForm() {
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
        resetButton.setBackground(Color.white);
    	mdpLabel.setBounds(50, 150, 100, 30);
    	mdp2Label.setBounds(50, 220, 100, 30);
    	mdpTextField.setBounds(150, 150, 150, 30);
    	mdp2TextField.setBounds(150, 220, 150, 30);

        validerButton.setBounds(250, 289, 100, 30);
       validerButton.setBackground(Color.BLACK);
        validerButton.setForeground(Color.WHITE);
        
        ensiasLabel.setBounds(5, 5, 180, 150);
    	ensiasLabel.setIcon(iconEnsias);
      	resetButton.setBounds(140, 290,700, 700);
        resetButton.setIcon(icon);

        panel.setBounds(10, 400, 200, 800);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.setBackground(Color.white);
        scrollPane.add(textArea);
        scrollPane.setViewportView(textArea);


    }

    public void addComponentsToContainer() {
        container.add(mdpLabel);
        container.add(mdp2Label);
        container.add(mdpTextField);
        container.add(mdp2TextField);
        container.add(resetButton);
        container.add(ensiasLabel);
        container.add(validerButton);

        panel.add(textArea);
        container.add(panel);
        
    
    }

    public void addActionEvent() {
    	validerButton.addActionListener(this);
      
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validerButton) {
            String mdp1;
            String mdp2;
            mdp1 = mdpTextField.getText();
            mdp2 = mdp2TextField.getText();
            if(mdp1.isEmpty() || mdp1.isEmpty()) {
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
        		
            	
		
            }else if(!mdp1.equals(mdp2)) {
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
				JOptionPane.showOptionDialog(null, "les deux mot de passe ne sont pas les meme", "Authentification", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), buttons, buttons[0]);
		
    	
     
            
            }else {
			Thread thread = new Thread() {
				@Override public void run() {
				try {	Thread.sleep(100);}catch(Exception e) {}
				PrintWriter output = new PrintWriter(sp.getOutputStream());
				output.print("CHN,"+mdpTextField.getText()+"#");
				output.flush();
			
				dispose();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sp.closePort();
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
     
    }
}
