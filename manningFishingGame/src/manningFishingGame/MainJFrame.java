package manningFishingGame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
				textAreaOutput.append("Going fishing!\n");
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
					textAreaOutput.append("You " + currentEvent.getTitle() + "\n");
					score += currentEvent.getValue();
				}
				
				
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
			}
		});
		buttonDone.setBounds(282, 12, 71, 25);
		contentPane.add(buttonDone);
	}
}
