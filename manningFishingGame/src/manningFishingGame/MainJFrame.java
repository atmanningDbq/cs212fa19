package manningFishingGame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class MainJFrame extends JFrame {

	private JPanel contentPane;

	// declare some objects to access from event handlers
	JScrollPane scrollPane;
	JTextArea txtrOutput;
	JButton btnCast;
	
	// declare and populate my items - later we will put this in an array
	Item item1 = new Item("small fish", 1);
	Item item2 = new Item("medium fish", 2);
	Item item3 = new Item("big fish", 3);
	Item item4 = new Item("lunker",6);
	Item item5 = new Item("turtle", 4);
	Item item6 = new Item("baby shark", 5);
	
	// extra events
	Item event1 = new Item("dropped your pole", -1);
	Item event2 = new Item("spilled your bait", -2);
	Item event3 = new Item("lost your catch", -5);
	Item event4 = new Item("fell out of the boat", -10);
	
	
	int score = 0;
	String fishingLog = "fishingLog.txt";
	FileWriter fwriter = null;
	
	Date dateTime = new Date();
	private JButton btnDone;
	private JTextField txtNum;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainJFrame frame = new MainJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStartFishing = new JButton("Start Fishing!");
		btnStartFishing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date date = new Date();
				txtrOutput.append("New game started at " + date.toInstant() + "\n");
				btnCast.setEnabled(true); // enable casting
				btnDone.setEnabled(true);
				score = 0;
				
				// open our output file
				try {
					fwriter = new FileWriter(fishingLog,true);
				} catch( Exception e ) {
					System.out.println("Problem opening " + fishingLog + " for output!");
				}
			}
		});
		btnStartFishing.setBounds(35, 12, 148, 25);
		contentPane.add(btnStartFishing);
		
		btnCast = new JButton("Cast");
		btnCast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Item fish = null;
				Die d = new Die(6);
				d.roll();
				switch(d.getValue()) {
				case 1:
					fish = item1;
					break;
				case 2:
					fish = item2;
					break;
				case 3:
					fish = item3;
					break;
				case 4:
					fish = item4;
					break;
				case 5:
					fish = item5;
					break;
				case 6:
					fish = item6;
					break;
				}
				txtrOutput.append(dateTime.toInstant() + ": " + "you caught a " + fish.getTitle() + "\n");
				score += fish.getValue();
				
				// append the output to a file
				if( null != fwriter )
					try {
						fwriter.write(dateTime.toInstant() + ": caught: " + fish.getTitle() + " pts=" + fish.getValue() + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				// extra events
				Item event = null;
				Die d2 = new Die(10);
				d2.roll();
				switch(d2.getValue()) {
				case 1:
					event = event1;
					break;
				case 2:
					event = event2;
					break;
				case 3:
					event = event3;
					break;	
				case 4:
					event = event4;
					break;
				}
				
				if( null != event) {					
					txtrOutput.append("You " + event.getTitle() + "!\n");
					score += event.getValue();
					
					// append the output to a file
					if( null != fwriter )
						try {
							fwriter.write(dateTime.toInstant() + ": event: " + event.getTitle() + " pts=" + event.getValue() + "\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
					
			}
		});
		btnCast.setEnabled(false);
		btnCast.setBounds(195, 12, 109, 25);
		contentPane.add(btnCast);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 46, 391, 63);
		contentPane.add(scrollPane);
		
	    txtrOutput = new JTextArea();
		scrollPane.setViewportView(txtrOutput);
		txtrOutput.setText("output");
		
		btnDone = new JButton("Done!");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnCast.setEnabled(false);
				btnDone.setEnabled(false);
				txtrOutput.append(dateTime.toInstant() + " finished!  SCORE=" + score + "\n");
				
				// append the output to a file
				if( null != fwriter )
					try {
						fwriter.write(dateTime.toInstant() + "-======= Done Fishing, SCORE=" + score + " ========\n");
						fwriter.flush();
						fwriter.close();
						fwriter = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
			}
		});
		btnDone.setEnabled(false);
		btnDone.setBounds(316, 12, 109, 25);
		contentPane.add(btnDone);
		
		txtNum = new JTextField();
		txtNum.setText("num");
		txtNum.setBounds(35, 127, 124, 19);
		contentPane.add(txtNum);
		txtNum.setColumns(10);
		
		JButton btnCurious = new JButton("Curious?");
		btnCurious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int n = Integer.parseInt(txtNum.getText());
				if( isCurious(n) )
					btnCurious.setText("Curious!");
				else 
					btnCurious.setText("Boring");
			}
		});
		btnCurious.setBounds(190, 121, 114, 25);
		contentPane.add(btnCurious);
		
		
		// get the user's name and then set our title to their name
		String name;
		do {
			name = JOptionPane.showInputDialog("What is your name?");		
		} while (!isValidName(name));
		setTitle( name + " has gone Fishing");
	}
	
	
	
	boolean isValidName(String s) {
		// confirm whether name begins with capital and is more than 3 chars
		
		if( s.length() < 2 )  // must be sufficient length
			return false;
		
		char c1 = s.charAt(0);
		// must begin with capital letter
		if( !('A'<= c1 && c1<='Z')) 
			return false;
		
		// remaining characters must be alphabetic
		char c;
		for( int i=1; i<s.length(); i++ ) {
			c = s.charAt(i);
			if( !(  ('a'<=c && c<='z') || ('A'<=c && c<='Z'))) 
				return false;
		}
		
		// all tests passed
		return true;

	}
	
	
	// a number is curious if the sum of digits factorial is equal to the number
	// return true if the number is curious based on Euler problem 32
	//
	boolean isCurious(int num) {
		int n = num;
		int factorialDigits = 0;
		while( 0 < n ) {
			factorialDigits += factorial( n % 10 ); // calculate and sum rightmost digit
			n /= 10; // remove rightmost digit
		}
		
		System.out.println("num=" + num + ", sumDigits = " + factorialDigits);
		return ( num == factorialDigits );
	}
	
	
	int factorial(int n) {
		if( n < 0 ) 
			return 0;
		if( n <= 2)
			return n;
		return n * factorial(n-1);
	}
}
