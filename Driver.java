/*Author: Shannon TJ 10101385 Tutorial 1
  Version: Apr 6, 2015

  Features: The program's starting point. Instantiates an extension of the JFrame class, which also implements ActionListener. 

  Limitations: Does not directly interact with any other part of the program.

 */


public class Driver
{
    //Starting point for the GUI
    public static void main (String [] args)
    {
	//Instantiates MyFrame
	MyFrame myFrame1 = new MyFrame();
	myFrame1.setVisible(true);
    }
}
