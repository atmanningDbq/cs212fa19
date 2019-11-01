package manningFishingGame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

public class MainJFrame extends JFrame {

	private JPanel contentPane;
	JButton btnCast;
	JButton buttonDone;
	Die dieCast = new Die(6);  // for casting for fish
	Die dieEvent = new Die(10);  // used for determining special events
	
	// items to catch - determined by the roll of the die and a switch statement
	// arrays are great to use when we have a series of similar objects like this
	Item item1 = new Item("small sunfish", 1);
	Item item2 = new Item("small crappie", 2);
	Item item3 = new Item("medium Bluegill", 3);
	Item item4 = new Item("big Bass", 5);
	Item item5 = new Item("largemouth Bass",10);
	Item item6 = new Item("lunker", 20);
	
	// now lets create some other events (10 max)
	Item event1 = new Item("fell out of your boat.", -30);
	Item event2 = new Item("let it get away!", -10);
	Item event3 = new Item("kicked over your bait", -5);
	Item event4 = new Item("dropped your pole into the water", -25);
	
	int score = 0;  // keep track of our fishing score
	String playerName = "";
	private JTextField textField;
	private final JLabel lblEnterANumber = new JLabel("Enter a number and click  \nto see if it is curious");
	
	FileWriter fwriter;
	String fname = "fishing-diary.txt";
	Date date = new Date();
	
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 42, 344, 74);
		contentPane.add(scrollPane);
		
		JTextArea textAreaOutput = new JTextArea();
		scrollPane.setViewportView(textAreaOutput);
		
		JButton btnLetsGoFishing = new JButton("Let's Go Fishing!");
		btnLetsGoFishing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textAreaOutput.append( playerName + " is going fishing!\n");
				btnCast.setEnabled(true);  // not ready to cast until game starts
				buttonDone.setEnabled(true); // disabled until fishing starts
				
				score = 0;  // reset our score if we start fishing again
			}
		});
		btnLetsGoFishing.setBounds(12, 12, 148, 25);
		contentPane.add(btnLetsGoFishing);
		
		btnCast = new JButton("Cast");
		btnCast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// cast our line using the Die class to determine what we catch
				dieCast.roll();
				// fish Items are chosen based on the roll
				// currentItem is set to one of these
				Item currentItem = null; // avoid warning error abour uninitialized var
				switch(dieCast.getValue()) {
				case 1:
					currentItem = item1;
					break;
				case 2:
					currentItem = item2;
					break;
				case 3:
					currentItem = item3;
					break;
				case 4:
					currentItem = item4;
					break;
				case 5:
					currentItem = item5;
					break;
				case 6:
					currentItem = item6;
					break;
				default:
					currentItem = null; // just in case our dice has more than 6 sides
					break;
				}
				
				// display a message about our catch
				textAreaOutput.append("You caught a " + currentItem.getTitle() + "\n");
				
				// determine if any special event occurred
				dieEvent.roll();
				Item currentEvent = null;
				switch(dieEvent.getValue()) {
				case 1:
					currentEvent = event1;
					break;
				case 2:
					currentEvent = event2;
					break;
				case 3:
					currentEvent = event3;
					break;
				case 4:
					currentEvent = event4;
					break;
				default:
					currentEvent = null;
				}
				
				// display event info
				if( null != currentEvent) {
					textAreaOutput.append("  You " + currentEvent.getTitle() + "\n");
					score += currentEvent.getValue();
				}
				
				// log our activity
				write2File( fwriter, date.toInstant() 
					+ " caught: " + currentItem.getTitle() 
					+ (null != currentEvent ? " Event:" + currentEvent.getTitle() : " ")
					+ " score=" + score );
				

				
				// update our score
				score += currentItem.getValue();
				
				// just for debugging 
				System.out.println("score=" + score);
			}
		});
		btnCast.setEnabled(false);
		btnCast.setBounds(172, 12, 71, 25);
		contentPane.add(btnCast);
		
	    buttonDone = new JButton("Done");
	    buttonDone.setEnabled(false);
		buttonDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonDone.setEnabled(false);
				btnCast.setEnabled(false);
				
				textAreaOutput.append("======  Done Fishing!... Score=" + score + " =======\n");
				
				write2File( fwriter, date.toInstant() + "======  Done Fishing!... Score=" + score + " =======\n");

				// an extra message if their score turns out to be a curious number
				if( isCurious( score ) )
					textAreaOutput.append(" Your score is a CURIOUS number! \n" );
				
			}
		});
		buttonDone.setBounds(282, 12, 71, 25);
		contentPane.add(buttonDone);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("145");
		textField.setBounds(23, 148, 124, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnCurious = new JButton("curious?");
		btnCurious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// testing if input num is curious - also serves as test of the code
				if( isCurious( Integer.parseInt(textField.getText()))) {
					btnCurious.setText("Yes!");
				} else
					btnCurious.setText("No");
			}
		});
		btnCurious.setBounds(172, 145, 114, 25);
		contentPane.add(btnCurious);
		lblEnterANumber.setBounds(33, 157, 369, 50);
		contentPane.add(lblEnterANumber);
		
		
		try {
			fwriter = new FileWriter( fname );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Error opening file " + fname );
		}
		
		write2File( fwriter, date.toInstant() + " Fishing game has started! - not fishing yet \n");
	
		
		// Loop to request valid name - keep looping while name not valid
		playerName = "";
		do {
			playerName = JOptionPane.showInputDialog("Please enter a valid name");
			
		} while ( !isValidName(playerName) );
		
		// show their name in the title
		setTitle(playerName);
		
	}
	
	
	
	boolean isValidName(String s) {
		// return true if s is a valid name
		
		// test for valid length
		if( s.length() < 2)
			return false;
		
		// first char capital letter
		char c = s.charAt(0);
		
		if( !('A'<=c && c <= 'Z')) 
			return false;
		
		// rest of letters alphabetic - lower or upper case alphabetic
		// other ways may be possible, but this is simple 
		for( int i=1; i<s.length(); i++ ) {
			c = s.charAt(i);
			if  ( !(('a'<=c && c <= 'z') || ('A'<=c && c <= 'Z')) )
				return false;
		}
		
		// valid if we got this far
		return true;
					
	}
	
	
	boolean isCurious( int n ) {
		// return true if n is a curious number
		// 145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.
		int sum = 0;
		int num = n;
		// get rightmost digit and calculate factorial.  Add it to sum
		while( 0 < num ) {
			sum += factorial( num % 10 );
			num /= 10; // now remove rightmost digit from num
		}
		
		// debugging:
		System.out.println("sum = " + sum + ", num=" + n );
		
		// this number is curious is sum of factorials of digits is equal to number itself
		return( sum == n );
	}
	
	
	// calculate factorial of n
	int factorial( int n ) {
		if ( n < 2 )  // its really only defined for positive numbers, but this catches all
			return 1;
		return n * factorial( n-1 );  // this is the classic recursive solution
	}
	
	
	// a utility method to append a String to a file - add a newline at the end
	void write2File( FileWriter fw, String s) {
		if( null != fw )
			try {
				fw.append(s + "\n");
				fw.flush();  // we could close the file each time, but this should ensure output
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
