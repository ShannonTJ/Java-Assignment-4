/*Author: Shannon TJ 10101385 Tutorial 1
  Version: Apr 6, 2015

  Features: Initializes a login window (adds and places components in the window) and displays the window. Gets a password from user input, decrypts a password saved in a text file (password.txt), and 
  compares the two passwords with each other. Initializes a shopping window (adds and places components in the window) and displays the window if the user enters the correct password. Allows the user 
  three attempts to enter the correct password before closing the program. Updates the window title to communicate messages to the user. Gets name and address from user input, and saves this information 
  to a text file (SaveInfo.txt). Clears this information from the text field and text area. 

  Limitations: The size and component placement of the login/shopping frames cannot be changed by the user. The component placement contains no internal padding. The Password.txt file has to contain a 
  password; if empty, the program will print an error message in the command terminal and count it as an incorrect password. This program only decrypts password files that contain all lowercase letters 
  and no special characters. 

 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class MyFrame extends JFrame implements ActionListener
{
    private JLabel loginLabel;
    private JLabel nameLabel;
    private JLabel addressLabel;

    private JButton loginButton;
    private JButton saveButton;
    private JButton clearButton;

    private JPasswordField loginPassword;

    private JTextField nameText;
    private JTextArea addressText;

    private GridBagLayout gridBag1 = new GridBagLayout();

    private GridBagConstraints constraint1 = new GridBagConstraints();

    private static final int LOGIN_WIDTH = 400;
    private static final int LOGIN_HEIGHT = 200;

    private static final int SHOPPING_WIDTH = 500;
    private static final int SHOPPING_HEIGHT = 400;

    private static final int DEFAULT_PAD = 10;
    private static final int NO_PAD = 0;

    private int loginAttempts = 0;
    private boolean correctPassword = true;

    public MyFrame()
    {
	loginFrame();
    }
	
    /*loginFrame: Initializes the title, size, and button/label/text placement in the login frame.   
      Parameters: N/A
      Returns: N/A
     */
    public void loginFrame()
    {
	setTitle("Login Window");
	setLayout(gridBag1);
	setSize(LOGIN_WIDTH, LOGIN_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	//Initializes login window components
	loginLabel = new JLabel("Enter Password");

	loginPassword = new JPasswordField(10);
	loginPassword.addActionListener(this);

	loginButton = new JButton("Login");
	loginButton.addActionListener(this);

	//Places login window components
	placeWidget(loginLabel, 0,0,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);
	placeWidget(loginPassword, 0,1,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);
	placeWidget(loginButton, 0,2,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);
    }

    /*shoppingFrame: Initializes the title, size, and button/label/text placement in the shopping frame. Sets the shopping frame to visible.  
      Parameters: N/A
      Returns: N/A
     */
    public void shoppingFrame()
    {
	setTitle("Order Information");
	setLayout(gridBag1);
	setSize(SHOPPING_WIDTH, SHOPPING_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	ImageIcon headerImage = new ImageIcon("cpsclogo.png");
	ImageIcon saveImage = new ImageIcon("savelogo.png");
	ImageIcon clearImage = new ImageIcon("clearlogo.png");

	//Adds image to header
	JLabel headerLabel = new JLabel(headerImage);

	nameLabel = new JLabel("Name");
	addressLabel = new JLabel("Address");

	nameText = new JTextField(15);
	addressText = new JTextArea(4, 15);

	//Adds image to button
	saveButton = new JButton("Save", saveImage);
	saveButton.addActionListener(this);

	//Adds image to button
	clearButton = new JButton("Clear", clearImage);
	clearButton.addActionListener(this);

	//Places shopping window components
	placeWidget(headerLabel, 0,1,2,1,  DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);

	placeWidget(nameLabel, 0,2,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);
	placeWidget(nameText, 0,3,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,DEFAULT_PAD);
	placeWidget(saveButton, 0,5,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);

	placeWidget(addressLabel, 1,2,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);
	placeWidget(addressText, 1,3,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);
	placeWidget(clearButton, 1,5,1,1, DEFAULT_PAD,NO_PAD,NO_PAD,NO_PAD);

	setVisible(true);
    }

    /*placeWidget: Determines the placement of components in a JFrame
      Parameters: A component, integer value for x-coordinate, integer value for y-coordinate, integer value for width, integer value for height, integer value for padding above, integer value for padding
      below, integer value for padding left, integer value for padding right
      Returns: N/A
     */
    public void placeWidget(Component widget, int x, int y, int width, int height, int padUp, int padDown, int padLeft, int padRight)
    {
	constraint1.gridx = x;
	constraint1.gridy = y;
	constraint1.gridwidth = width;
	constraint1.gridheight = height; 
	constraint1.insets = new Insets(padUp,padDown,padLeft,padRight);
	gridBag1.setConstraints(widget, constraint1);
	addWidget(widget);
    }

    /*addWidget: Adds components to a JFrame
      Parameters: A component 
      Returns: N/A
     */
    public void addWidget(Component widget)
    {
	add(widget);
    }

    /*actionPerformed: Listens for action events and performs actions based on the source of the action event
      Parameters: An action event
      Returns: N/A
     */
    public void actionPerformed(ActionEvent e)
    {
	String s = e.getActionCommand();

	//Handles events unique to the login window
	if(e.getSource() == loginPassword || s.equals("Login"))
	    {
		//Gets user input and saves it as a string
		char [] userPassword = loginPassword.getPassword();

		//Reads the encrypted password file
		comparePasswords(userPassword);
	    }
	//Handles events unique to the shopping window
	else
	    {
		//Performs actions related to the Save button
		if (s.equals("Save"))
		    {
			saveInfo();
		    }
		//Performs actions related to the Clear button
		else if (s.equals("Clear"))
		    {
			clearInfo();
		    }

		//Delays the window's title change by 2 seconds
		timeDelay();
		//Sets the window title back to its default
		setTitle("Order Information");
	    }
    }

    /*comparePasswords: Gets an encrypted password from a file, calls a method to decrypt the password, and compares the decrypted password with the user input password. Calls methods depending on
      whether the correct password was entered. 
      Parameters: A character array containing the password entered by the user
      Returns: N/A
     */
    public void comparePasswords(char [] userArray)
    {
	try
	    {
		//Reads the first line of the text file Password.txt and saves it as a string
		BufferedReader buffer1 = new BufferedReader(new FileReader("password.txt"));
	        String filePassword = buffer1.readLine();
		buffer1.close();
		//Converts userPassword to a string so that the password lengths can be compared
		String userPassword = String.valueOf(userArray);
		//Displays error message if the password file is empty
		if (filePassword == null)
		    {
			System.out.println("ERROR: Password file is empty. No password to compare to.");
			correctPassword = false;
		    }
		//Compares password length. When the input password's length does not match the file password's length, it is automatically incorrect
		else if (userPassword.length() != filePassword.length())
		    correctPassword = false;
		//Compares user password and file password when their lengths are identical and file password is not null
		else
		    {
			//User and file passwords are put into character arrays
			char [] fileArray = new char [filePassword.length()];
			//Decrypts every character in the file password
			fileArray = decryptPassword(filePassword, fileArray);
			//Compares every character in the user and file passwords. Checks for non-matching characters. 
			for (int i = 0; i < filePassword.length(); i++)
			    {
				if(fileArray[i] != userArray[i])
				    correctPassword = false;
			    }
		    }
		//Calls methods depending on whether or not the user entered the correct password
		if (correctPassword == true)
		    correctPassword();
		else
		    incorrectPassword();
		//Resets correctPassword to its default value
		correctPassword = true;
	    }
	catch (IOException e)
	    {
		System.out.println("ERROR: Could not read password from file.");
	    }
    }

    /*decryptPassword: Decrypts a Caesar cipher-encrypted password by shifting each letter by one place. 
      Parameters: A string containing the encrypted file password and a character array containing the encrypted file password's characters
      Returns: A character array containing the decrypted file password's characters
     */
    public char [] decryptPassword(String filePassword, char [] fileArray)
    {
	for (int i = 0; i < filePassword.length(); i++)
	    {
		char encrypted = filePassword.charAt(i);
		//Casts each character into an integer value
		int ascii = (int) encrypted;
		if (ascii == 122)		    
		    ascii = 97;
		else
		    ascii++;
		//Casts the integer value back into a character
		char decrypted = (char) ascii;
		fileArray [i] = decrypted;
	    }
	return(fileArray);
    }

    /*correctPassword: Closes the login window, clears the login window, and makes the shopping window appear
      Parameters: N/A
      Returns: N/A
     */
    public void correctPassword()
    {
	//Closes the login window
	dispose();
	//Clears all content in the window
	getContentPane().removeAll();
	revalidate();
	repaint();
	//Makes the shopping window appear
	shoppingFrame();
    }

    /*incorrectPassword: Updates the number of login attempts, changes the window title to reflect the number of incorrect logins, closes program if maximum login attempts (3) have been exceeded.
      Parameters: N/A
      Returns: N/A
     */
    public void incorrectPassword()
    {
	//Updates the number of login attempts
	loginAttempts++;
	//Sets window title to display number of incorrect login attempts
	setTitle("No. Incorrect Login Attempts (Max = 3): "+loginAttempts);
	if (loginAttempts == 3)
	    {
		//Sets window title to display max login attempts exceeded
		setTitle("Max Attempts Exceeded, Exiting...");
		timeDelay();
		//Closes the login window
		dispose();
	    }
    }

    /*saveInfo: Gets user input from the text field and text area in the shopping window, writes this info to a new file. The user's name is written on the first line and the address is written on
      subsequent lines. 
      Parameters: N/A
      Returns: N/A
     */
    public void saveInfo()
    {
	//Sets window title to status message
	setTitle("Please Wait: Saving Information...");
	//Gets user input from text fields
	String userName = nameText.getText();
	String userAddress = addressText.getText();

	//Saves info to the file SaveInfo.txt
	try
	    {
		FileWriter writer1 = new FileWriter("SaveInfo.txt");
		writer1.write(userName+"\n"+userAddress);
		writer1.close();
	    }
	catch (IOException e)
	    {
		System.out.println("ERROR: Could not save info to file.");
	    }
    }

    /*clearInfo: Clears the user input in the text field and text area on the shopping window
      Parameters: N/A
      Returns: N/A
     */
    public void clearInfo()
    {
	//Sets window title to status message
	setTitle("Please Wait: Clearing Information...");
	//Sets text fields to an empty string
	nameText.setText("");
	addressText.setText("");
    }

    /*timeDelay: Delays program operations by two seconds
      Parameters: N/A
      Returns: N/A
     */
    public void timeDelay()
    {
	try 
	    {
		Thread.sleep(2000);                 
	    } 
	catch(InterruptedException ex) 
	    {
		System.out.println("ERROR: Pausing was interrupted.");
	    }
    }
}
