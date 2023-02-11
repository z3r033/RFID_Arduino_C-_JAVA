package gui;


import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.text.DefaultCaret;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
 

public class CompteForm extends JFrame implements ActionListener {
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
					if(dataBuffer.contains("solde")&&dataBuffer.contains("=tx19")) {
				
					String[] arrOfStr = dataBuffer.split("=");
					
					System.out.println(dataBuffer);
					
					soldeTextField.setText(arrOfStr[1]);
					}
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
    JLabel ensiasLabel = new JLabel("ensias");
    ImageIcon iconEnsias = createImageIcon("s.png","ensias");
    ImageIcon icon = createImageIcon("rffid.gif","rfid");
    JLabel resetButton = new JLabel("RESET");
    JPanel panel = new JPanel(new GridLayout(2,3));
    JLabel SoldeLabel = new JLabel("Solde");
    JLabel CreditLabel = new JLabel("débit");
    JLabel DebitLabel = new JLabel("crédit");
    JTextField soldeTextField = new JTextField();
    JTextField creditField = new JTextField(); 
    JTextField debitField = new JTextField(); 

    JButton setButton1 = new JButton("increment");
    JButton setButton2 = new JButton("decrement");
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

    public CompteForm() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        
    	System.out.println("f");
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
    	SoldeLabel.setBounds(50, 150, 100, 30);
    	CreditLabel.setBounds(50, 220, 100, 30);
    	DebitLabel.setBounds(50, 290, 100, 30);
        soldeTextField.setBounds(150, 150, 150, 30);
        creditField.setBounds(150, 220, 150, 30);
        debitField.setBounds(150, 290, 150, 30);
        setButton1.setBounds(300, 220, 150, 29);
        setButton2.setBounds(300, 290, 150, 29);
        
        setButton1.setBackground(Color.BLACK);
        setButton1.setForeground(Color.WHITE);
        
        setButton2.setBackground(Color.BLACK);
        setButton2.setForeground(Color.WHITE);
        
        panel.setBounds(10, 400, 300, 800);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.setBackground(Color.white);
        scrollPane.add(textArea);
        scrollPane.setViewportView(textArea);

        
        resetButton.setBounds(140, 200, 800, 800);
        resetButton.setIcon(icon);
        resetButton.setBackground(Color.white);

    }

    public void addComponentsToContainer() {
        container.add(SoldeLabel);
        container.add(CreditLabel);
        container.add(DebitLabel);
        container.add(soldeTextField);
        container.add(creditField);
        container.add(debitField);
        container.add(setButton1);
        container.add(setButton2);
        panel.add(textArea);
        container.add(panel);
        container.add(resetButton);
    	container.add(ensiasLabel);

    }

    public void addActionEvent() {
    	setButton1.addActionListener(this);
    	setButton2.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    
    	if (e.getSource() == setButton1) {
    		
    		 boolean isNumeric =  creditField.getText().matches("[+-]?\\d*(\\.\\d+)?");
    		 if(isNumeric) {
        		soldeTextField.setText("");	
				Thread thread = new Thread() {
					@Override public void run() {
					try {	Thread.sleep(100);}catch(Exception e) {}
					PrintWriter output = new PrintWriter(sp.getOutputStream());
					output.print("INC,"+creditField.getText());
					output.flush();
				    creditField.setText("");
					}
				};
				thread.start();
        		try {
    				Thread.sleep(1000);
    			} catch (InterruptedException e2) {
    				// TODO Auto-generated catch block
    				e2.printStackTrace();
    			}
    		 }else {
    			 JOptionPane.showMessageDialog(this, "entrer une numero");
    		 }
        	}

    	if(e.getSource()==setButton2) {
    		 boolean isNumeric =  debitField.getText().matches("[+-]?\\d*(\\.\\d+)?");
    		 if(isNumeric) {
    		soldeTextField.setText("");	
			Thread thread = new Thread() {
				@Override public void run() {
				try {	Thread.sleep(100);}catch(Exception e) {}
				PrintWriter output = new PrintWriter(sp.getOutputStream());
				output.print("DEC,"+debitField.getText());
				output.flush();
				  debitField.setText("");
				}
			};
			thread.start();
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
    	
    		 }else {
    			 JOptionPane.showMessageDialog(this, "entrer une numero");
    		 }

    	}
    
    }
}
