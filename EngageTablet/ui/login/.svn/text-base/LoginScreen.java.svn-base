package ui.login;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import main.Logger;
import backend.library.ProgramMedia;
import backend.queue.QueueLibrary;


/**
 * This is the first screen users see, and they need to select the educational program,
 * their own name, and whether or not they connect to the large secondary display.
 * Instructors can also be created and deleted here.
 * 
 * @author Kayre Hylton
 *
 */
public class LoginScreen extends JFrame 
implements ActionListener, KeyListener, WindowListener{

	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------
	
	
	/**
	 * Whether or not the user is done logging in
	 */
	public boolean done = false;
	
	/**
	 * Vector of Strings of names of programs
	 */
	private Vector<String> programNames = new Vector<String>();
	
	/**
	 * Name of the instructor logging in
	 */
	private String instructor;
	
	/**
	 * The name of the selected program
	 */
	private String program;
	
	/**
	 * The password for logging in as the deafult or deleting instructors
	 */
	private String password;
		
	/**
	 * Whether or not an instructor is being added
	 */
	private boolean addingInstructor = false;
		
	/**
	 * The QueueLibrary
	 */
	private QueueLibrary qLibrary;
	
	/**
	 * The default text that shows in the instructor drop down
	 */
	private String defaultText = "type instructor name here";
	
	/**
	 * The name that has been typed in
	 */
	private String nameTyped = "";
	
	/**
	 * Whether or not the user wants to connect to an extra screen
	 */
	private boolean connectedToScreen = true;
	
	/**
	 * The background color for Dialogs
	 */
	private Color bgColor = new Color(24,41,85);
	
	/**
	 * The log in button
	 */
	private JButton logInButton = new JButton("Log In");
	
	/**
	 * The drop down menu with instructor names
	 */
	private JComboBox instructorDropDown;

	/**
	 * The drop down menu with program names
	 */
	private JComboBox programDropDown = new JComboBox();
	
	/**
	 * The text field editor for the instructor drop down when it is editable
	 */
	private JTextField instructorTextField;
	
	/**
	 * The add instructor button
	 */
	private JButton addButton = new JButton("Add Instructor");
	
	/**
	 * The delete instructor button
	 */
	private JButton deleteButton = new JButton("Delete Instructor...");
	
	/**
	 * The dialog for deleting instructors
	 */
	private JDialog deletePane;
	
	/**
	 * The dialog for duplicated instructor names
	 */
	private JDialog instructorExistsDialog;
	
	/**
	 * The dialog for logging in as the default instructor
	 */
	private JDialog defaultLoginDialog;
	
	/**
	 * The password field for deleting
	 */
	private JPasswordField deleteCodeField;
	
	/**
	 * The label for wrong password when deleting
	 */
	private JLabel wrongPasswordLabel;
	
	/**
	 * The button to confirm a deletion
	 */
	private JButton confirmDeleteButton;
	
	/**
	 * The button to cancel a deletion
	 */
	private JButton cancelDeleteButton;
	
	/**
	 * The content pane of the LoginScreen
	 */
	private Container content;
	
	/**
	 * The checkbox for connecting to a secondary screen
	 */
	private JCheckBox connectToScreenBox;
	
	/**
	 * The password field for logging in as the deafult instructor
	 */
	private JPasswordField defaultLoginCodeField;

	/**
	 * The button to confirm login as default
	 */
	private JButton confirmDefaultLoginButton;

	/**
	 * The button to cancel login as default
	 */
	private JButton cancelDefaultLoginButton;

	/**
	 * The label for wrong password when loggin in as default
	 */
	private JLabel wrongLoginPasswordLabel;
	
	/**
	 * The width of the screen
	 */	
	private int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
     
	/**
	 * The height of the screen
	 */
	private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	/**
	 * The width of the LoginScreen
	 */
	private int loginScreenWidth = 400;
	
	/**
	 * The height of the LoginScreen
	 */
	private int loginScreenHeight = 250;
	

	
	//-------------------------------------------------------------------------
	// Getters & Setters
	//-------------------------------------------------------------------------

	/**
	 * Returns the name of the instructor logging in
	 * @return a String of the name of the instructor logging in
	 */
	public String getInstructorName() {
		return instructor;
	}

	/**
	 * Returns the name of the program being logged into
	 * @return a String of the name of the program being logged into
	 */
	public String getProgramName() {
		return program;
	}
		
	/**
	 * Returns the QueueLibrary that manages the instructors
	 * 
	 * @return the QueueLibrary that manages the instructors
	 */
	public QueueLibrary getQLibrary() {
		return qLibrary;
	}

	/**
	 * Sets the QueueLibrary that manages the instructors
	 * @param library the QueueLibrary that manages the instructors
	 */
	public void setQLibrary(QueueLibrary library) {
		qLibrary = library;
	}

	/**
	 * Returns whether or not the user is done logging in
	 * @return true if the user is done logging in, false otherwise
	 */
	public boolean isDone(){
		return done;
	}
	
	/**
	 * Sets the password used to delete instructors and log in as the default
	 * @param pass the password used to delete instructors and log in as the default
	 */
	public void setPassword(String pass){
		this.password = pass;
	}
	
	/**
	 * Returns whether or not the user wants to try to connect to a secondary screen
	 * @return true if the user wants to try to connect to a secondary screen,
	 * false otherwise
	 */
	public boolean isConnectedToScreen() {
		return connectedToScreen;
	}

	/**
	 * Sets whether or not the user wants to try to connect to a secondary screen
	 * @param connectedToScreen true if the user wants to try to connect to a secondary screen,
	 * false otherwise
	 */
	public void setConnectedToScreen(boolean connectedToScreen) {
		this.connectedToScreen = connectedToScreen;
	}
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	
	/**
	 * Creates a LoginScreen from a QueueLibrary and a list of program names.
	 * The QueueLibrary is used initially to get the instructor names for the
	 * instructor drop down menu, and the list of program names is used for the
	 * program drop down menu.
	 * 
	 * @param qLibrary the QueueLibrary
	 * @param programs an Iterator of program names
	 */
	public LoginScreen(QueueLibrary qLibrary, Iterator programs){
		super("Engage Media Display : Please Log In");
		setQLibrary(qLibrary);
		String[] instructors = qLibrary.getInstructorNames();
		
		//Create drop down for instructor names
		instructorDropDown = new JComboBox(instructors);
		instructorDropDown.addActionListener(this);
		instructorDropDown.setActionCommand("instructor_drop_down");
		instructorDropDown.getEditor().getEditorComponent().addKeyListener(this);
		instructorDropDown.setPreferredSize(new Dimension(220,20));
		instructorDropDown.setOpaque(false);
		
		//Create instructor label
		JLabel instructorLabel = new JLabel("Instructor:   ");
		instructorLabel.setForeground(Color.WHITE);
		instructorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		instructorLabel.setPreferredSize(new Dimension(70,20));

		//Place instructor drop down and label in an instructor panel
		JPanel instructorPanel = new JPanel(new FlowLayout());
		instructorPanel.add(instructorLabel);
		instructorPanel.add(instructorDropDown);
		instructorPanel.setOpaque(false);
		int instructorPanelWidth = instructorPanel.getPreferredSize().width;
				
		// Create drop down for programs
		while(programs.hasNext()){
			ProgramMedia pm = (ProgramMedia)programs.next();
			programDropDown.addItem(pm.getDisplayName());
			programNames.add(pm.getName());
		}
		//programDropDown = new JComboBox(programDisplayNames);
		programDropDown.setPreferredSize(new Dimension(220,20));
		programDropDown.setOpaque(false);
		
		//Create label for programs
		JLabel programLabel = new JLabel("Program:   ");
		programLabel.setForeground(Color.WHITE);
		programLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		programLabel.setPreferredSize(new Dimension(70,20));
		
		//Place program drop down and label in a program panel
		JPanel programPanel = new JPanel(new FlowLayout());
		programPanel.add(programLabel);
		programPanel.add(programDropDown);
		programPanel.setOpaque(false);
		
		//set things for log in button
		logInButton.setActionCommand("log_in");
		logInButton.addActionListener(this);
		logInButton.setFocusPainted(false);
		
		//set things for quit button
		JButton quitButton = new JButton("Quit");
		quitButton.setActionCommand("quit");
		quitButton.addActionListener(this);
		quitButton.setFocusPainted(false);
		
		//set things for add button
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		addButton.setFocusPainted(false);
	
		//set things for delete button
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(this);
		deleteButton.setFocusPainted(false);
		
		//place add and delete buttons in a panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(deleteButton);
		buttonPanel.add(addButton);	
		buttonPanel.setOpaque(false);

		//Create checkbox and label for connecting to a secondary screen
		connectToScreenBox = new JCheckBox();
		connectToScreenBox.setActionCommand("connect");
		connectToScreenBox.addActionListener(this);
		connectToScreenBox.setSelected(true);
		connectToScreenBox.setOpaque(false);
		JLabel connectToScreenLabel = new JLabel("Connect tablet to large display");
		connectToScreenLabel.setForeground(Color.WHITE);
		
		//Place connection checkbox and label in a panel
		JPanel connectPanel = new JPanel(new FlowLayout());
		connectPanel.add(connectToScreenLabel);
		connectPanel.add(connectToScreenBox);
		connectPanel.setOpaque(false);
		
		//Add components to content pane
		SpringLayout layout = new SpringLayout();
		content = new ContentPane();
		content.setLayout(layout);
		content.add(instructorPanel);
		content.add(buttonPanel);
		content.add(programPanel);
		content.add(logInButton);
		content.add(quitButton);
		content.add(connectPanel);
		
		//horizontal space on either side of instructorPanel if centered
		int hSpace = (instructorPanelWidth-loginScreenWidth)/2;
		
		//lay out components
		layout.putConstraint(SpringLayout.NORTH, instructorPanel, 20, SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.EAST, instructorPanel, hSpace-5,
				SpringLayout.EAST, content);
		layout.putConstraint(SpringLayout.NORTH, buttonPanel, 2, SpringLayout.SOUTH, instructorPanel);
		layout.putConstraint(SpringLayout.EAST, buttonPanel, hSpace-5, SpringLayout.EAST, content);
		layout.putConstraint(SpringLayout.NORTH, programPanel, 20, SpringLayout.SOUTH, buttonPanel);
		layout.putConstraint(SpringLayout.EAST, programPanel, hSpace-5, SpringLayout.EAST, content);
		layout.putConstraint(SpringLayout.NORTH, connectPanel, 10, SpringLayout.SOUTH, programPanel);
		layout.putConstraint(SpringLayout.EAST, connectPanel, hSpace-1, SpringLayout.EAST, content);
		layout.putConstraint(SpringLayout.EAST, logInButton, -15, SpringLayout.EAST, content);
		layout.putConstraint(SpringLayout.SOUTH, logInButton, -15, SpringLayout.SOUTH, content);
		layout.putConstraint(SpringLayout.WEST, quitButton, 15, SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.SOUTH, quitButton, -15, SpringLayout.SOUTH, content);
		
		this.setContentPane(content);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(loginScreenWidth,loginScreenHeight);
		this.setResizable(false);
		this.setLocation((screenWidth-loginScreenWidth)/2,
				         (screenHeight-loginScreenHeight-30));
	}
		
	
	//-------------------------------------------------------------------------
	// ActionListener Methods
	//-------------------------------------------------------------------------


	/**
	 * Called when an ActionEvent is fired.
	 * 
	 * @param e the ActionEvent
	 */
	public void actionPerformed(ActionEvent e){
			//Log In button pressed
			if(e.getActionCommand().equals("log_in")){
				instructor = (String)instructorDropDown.getSelectedItem();
				makeInstructorNameValid();
				//if we are adding an instructor that does not exist, or not adding an instructor
				if(!instructorExists(instructor) || !addingInstructor){
					
					if(instructor.equals("edit default")){
						instructor = "default";
						showDefaultLoginDialog();
					}
					else{
						successfulLogin();					
					}
				}
				//if we are trying to add an existing instructor
				else{
					showInstructorExistsDialog();
				}
			}
			//Log In button on default instructor pane pressed
			else if(e.getActionCommand().equals("confirm_default_login")){
				String pass = new String(defaultLoginCodeField.getPassword());
				if(pass.equals(password)){
					done = true;
					defaultLoginDialog.dispose();
				}
				else{
					wrongLoginPasswordLabel.setVisible(true);
					defaultLoginCodeField.setText("");
				}
			}
			//Cancel button on default instructor pane pressed
			else if(e.getActionCommand().equals("cancel_default_login")){
				defaultLoginDialog.dispose();
			}
			//OK button on duplicate instructor warning pane pressed
			else if(e.getActionCommand().equals("confirm_duplicate")){
				instructorExistsDialog.dispose();
			}
			//Add instructor button pressed
			else if(e.getActionCommand().equals("add")){
				addingInstructor(true);
				addButton.setEnabled(false);
				logInButton.setEnabled(false);
			}
			//Delete instructor button pressed
			else if(e.getActionCommand().equals("delete")){
				instructor = (String)instructorDropDown.getSelectedItem();
				showDeleteDialog();
			}
			//Delete button pressed on delete instructor pane
			else if(e.getActionCommand().equals("confirm_delete")){
				String pass = new String(deleteCodeField.getPassword());
				if(pass.equals(password)){
					//Clear contents of default instructor
					if(instructor.equals("edit default")){
						qLibrary.getInstructorQueue("default").addEmptyStructure();
						qLibrary.getInstructorQueue("default").toXML();
					}
					//remove an instructor
					else{
					qLibrary.removeInstructorQueue(instructor);
					instructorDropDown.removeItem(instructor);
					instructorDropDown.setSelectedIndex(0);
					}
					deletePane.dispose();
				}
				else{
					wrongPasswordLabel.setVisible(true);
					deleteCodeField.setText("");
				}
			}
			//Cancel button pressed on delete instructor pane
			else if(e.getActionCommand().equals("cancel_delete")){
				deletePane.dispose();
			}
			//Quit button pressed
			else if(e.getActionCommand().equals("quit")){
				System.exit(0);
			}
			//Event occurred in instructor drop down menu
			else if(e.getActionCommand().equals("instructor_drop_down")){
				if(addingInstructor && instructorDropDown.getSelectedIndex() > 0 ){
					if(instructorDropDown.getEditor().getEditorComponent().hasFocus()){
						addingInstructor(false);
					}
				}
			}
			//Connection checkbox selected/deselected
			else if(e.getActionCommand().equals("connect")){
				this.setConnectedToScreen(connectToScreenBox.isSelected());
			}
		}

	


	//-------------------------------------------------------------------------
	// KeyListener Methods
	//-------------------------------------------------------------------------
	

	/**
	 * Inherited from KeyListener
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Inherited from KeyListener
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Inherited from KeyListener. Handles events generated when a key is released
	 * when the instructor drop down menu's editor is selected.
	 * 
	 * @param e the KeyEvent
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getSource().equals(instructorDropDown.getEditor().getEditorComponent())){
			nameTyped = instructorTextField.getText();
			int pos = instructorTextField.getCaretPosition(); 
			
			instructorDropDown.insertItemAt(nameTyped,0);
			instructorDropDown.setSelectedIndex(0);
			instructorDropDown.removeItemAt(1);
			instructorTextField.setCaretPosition(pos);
			if(nameTyped.equals("")){
				logInButton.setEnabled(false);
			}
			else{
				logInButton.setEnabled(true);
			}
		}
	}
	

	
		
	//-------------------------------------------------------------------------
	// WindowListener Methods
	//-------------------------------------------------------------------------

	/**
	 * Inherited from WindowListener
	 */
	public void windowOpened(WindowEvent arg0) {
	}

	/**
	 * Inherited from WindowListener
	 */
	public void windowClosing(WindowEvent arg0) {
		this.setEnabled(true);
	}

	/**
	 * Inherited from WindowListener. Handles window closed events from the dialogs
	 * that arise from the LoginScreen.
	 * 
	 * @param arg0 the WindowEvent
	 */
	public void windowClosed(WindowEvent arg0) {
		this.setEnabled(true);
		this.requestFocus();
		if(done){
			successfulLogin();
		}
	}

	/**
	 * Inherited from WindowListener
	 */
	public void windowIconified(WindowEvent arg0) {
	}

	/**
	 * Inherited from WindowListener
	 */
	public void windowDeiconified(WindowEvent arg0) {
	}

	/**
	 * Inherited from WindowListener
	 */
	public void windowActivated(WindowEvent arg0) {
	}

	/**
	 * Inherited from WindowListener
	 */
	public void windowDeactivated(WindowEvent arg0) {
	}
	
	
	//-------------------------------------------------------------------------
	// Methods for Showing dialogs
	//-------------------------------------------------------------------------

	/**
	 * Shows an error dialog to warn about duplicate instructors. Called when
	 * a user tries to create and instructor who already exists.
	 */
	private void showInstructorExistsDialog() {
		//Create the dialog
		instructorExistsDialog = new JDialog(this, "Duplicate instructor name");
		instructorExistsDialog.addWindowListener(this);
		instructorExistsDialog.getContentPane().setBackground(bgColor);
		
		//create the label and OK button
		JLabel label = new JLabel("An instructor with this name already exists");
		label.setForeground(Color.WHITE);
		label.setOpaque(false);
		JButton okButton = new JButton("OK");
		okButton.setFocusPainted(false);
		okButton.setActionCommand("confirm_duplicate");
		okButton.addActionListener(this);
		
		//add label and button to layout
		instructorExistsDialog.getContentPane().setLayout(new FlowLayout());
		instructorExistsDialog.getContentPane().add(label);
		instructorExistsDialog.getContentPane().add(okButton);

		//sets particulars of this pane and disables the LoginScreen
		instructorExistsDialog.setSize(270,105);
		instructorExistsDialog.setLocation((screenWidth-270)/2, (screenHeight-100)/2);
		instructorExistsDialog.setVisible(true);
		instructorExistsDialog.setResizable(false);
		this.setEnabled(false);
	}
	
	/**
	 * Shows a dialog to let users log in as the default instructor. Called when
	 * users try to log in as the default instructor. Users will have to enter 
	 * a password to proceed.
	 */
	private void showDefaultLoginDialog(){
		//create the dialog
		defaultLoginDialog = new JDialog(this, "Log in as the default instructor");
		defaultLoginDialog.addWindowListener(this);
		defaultLoginDialog.getContentPane().setBackground(bgColor);
		
		//set things for label
		JLabel defaultLoginLabel = new JLabel();
		defaultLoginLabel.setText("<html><p align=\"center\">Enter password to log in " +
				"as<br>the default instructor</p></html>");
		defaultLoginLabel.setPreferredSize(new Dimension(260, 50));
		defaultLoginLabel.setForeground(Color.WHITE);
		defaultLoginLabel.setVerticalAlignment(SwingConstants.CENTER);
		defaultLoginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		defaultLoginCodeField = new JPasswordField(15);
		
		JPanel defaultLoginButtonPanel = new JPanel();
		
		//set things for log in button
		confirmDefaultLoginButton = new JButton("Log In");
		confirmDefaultLoginButton.addActionListener(this);
		confirmDefaultLoginButton.setActionCommand("confirm_default_login");
		confirmDefaultLoginButton.setFocusPainted(false);
		
		//set things for cancel button
		cancelDefaultLoginButton = new JButton("Cancel");
		cancelDefaultLoginButton.addActionListener(this);
		cancelDefaultLoginButton.addKeyListener(this);
		cancelDefaultLoginButton.setActionCommand("cancel_default_login");
		cancelDefaultLoginButton.setFocusPainted(false);
		
		//add buttons to panel
		defaultLoginButtonPanel.add(confirmDefaultLoginButton);
		defaultLoginButtonPanel.add(cancelDefaultLoginButton);
		defaultLoginButtonPanel.setOpaque(false);
		
		//set sometimes visible wrong password label
		wrongLoginPasswordLabel = new JLabel("Password is not valid!");
		wrongLoginPasswordLabel.setForeground(new Color(250,166,26));
		wrongLoginPasswordLabel.setPreferredSize(new Dimension(200,20));
		wrongLoginPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add components to the layout and disable the LoginScreen
		defaultLoginDialog.setLayout(new FlowLayout());
		defaultLoginDialog.add(defaultLoginLabel);
		defaultLoginDialog.add(defaultLoginCodeField);
		defaultLoginDialog.add(defaultLoginButtonPanel);
		defaultLoginDialog.add(wrongLoginPasswordLabel);
		wrongLoginPasswordLabel.setVisible(false);
		defaultLoginDialog.setSize(300,190);
		defaultLoginDialog.setLocation((screenWidth-300)/2,	(screenHeight-190)/2);
		defaultLoginDialog.setResizable(false);
		defaultLoginDialog.setVisible(true);
		this.setEnabled(false);
	}
	
	/**
	 * Shows a dialog to confirm deletion of an instructor. Called when the delete
	 * instructor button is pressed.
	 */
	private void showDeleteDialog() {
		JLabel deleteLabel = new JLabel();
		//Sets frame title and label based on whether the instructor is the default one or not
		if(instructor.equals("edit default")){
			deletePane = new JDialog(this, 
					"Clear contents of default instructor");
			deleteLabel.setText("<html><p align=\"center\">Enter password to clear the contents of" +
					" the default instructor </p></html>");
		}else{
			deletePane = new JDialog(this, 
					"Delete instructor");
			deleteLabel.setText("<html><p align=\"center\">Enter password to delete instructor <br> \"" 
					+ instructor + "\"</p></html>");
		}
		
		deletePane.addWindowListener(this);
		deletePane.getContentPane().setBackground(bgColor);
		
		//set other particulars of the label
		deleteLabel.setPreferredSize(new Dimension(260, 50));
		deleteLabel.setForeground(Color.WHITE);
		deleteLabel.setVerticalAlignment(SwingConstants.CENTER);
		deleteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		deleteCodeField = new JPasswordField(15);
		
		JPanel deleteButtonPanel = new JPanel();
		
		//set things for the delete button
		confirmDeleteButton = new JButton("Delete");
		confirmDeleteButton.addActionListener(this);
		confirmDeleteButton.setActionCommand("confirm_delete");
		confirmDeleteButton.setFocusPainted(false);
		
		//set things for the cancel button
		cancelDeleteButton = new JButton("Cancel");
		cancelDeleteButton.addActionListener(this);
		cancelDeleteButton.setActionCommand("cancel_delete");
		cancelDeleteButton.setFocusPainted(false);
		
		//add buttons to the button panel
		deleteButtonPanel.add(confirmDeleteButton);
		deleteButtonPanel.add(cancelDeleteButton);
		deleteButtonPanel.setOpaque(false);
		
		//set sometimes visible wrong password label
		wrongPasswordLabel = new JLabel("Password is not valid!");
		wrongPasswordLabel.setForeground(new Color(250,166,26));
		wrongPasswordLabel.setPreferredSize(new Dimension(200,20));
		wrongPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add components to the layout and disable the LoginScreen
		deletePane.setLayout(new FlowLayout());
		deletePane.add(deleteLabel);
		deletePane.add(deleteCodeField);
		deletePane.add(deleteButtonPanel);
		deletePane.add(wrongPasswordLabel);
		wrongPasswordLabel.setVisible(false);
		deletePane.setSize(300,190);
		deletePane.setLocation((screenWidth-300)/2,	(screenHeight-190)/2);
		deletePane.setResizable(false);
		deletePane.setVisible(true);
		this.setEnabled(false);
	}

	/**
	 * Displays the LoginScreen. Called on initialization, and on logout
	 */
	public void showLoginScreen() {
		this.setVisible(true);
		done = false;
	}

	
	//-------------------------------------------------------------------------
	// Instructor Methods
	//-------------------------------------------------------------------------

	
	/**
	 * Let's the main part of the application know that log in has been successful.
	 * Called when an instructor has successfully logged in.
	 */
	private void successfulLogin(){
		done = true;
		addingInstructor(false);
		this.setVisible(false);
		program = programNames.get(programDropDown.getSelectedIndex());
		Logger.log(Logger.IMPORTANT,"LoginScreen: "+instructor+" is logging into "+program+".");
	}
	
	
	/**
	 * Checks to see if an instructor already exists.
	 * @param instructor2 the name of an instructor
	 * @return true if this instructor exists, false otherwise
	 */
	private boolean instructorExists(String instructor2) {
			for(int i=1; i<instructorDropDown.getItemCount(); i++){
				if(((String)instructorDropDown.getItemAt(i)).equalsIgnoreCase(instructor2)){
					return true;
				}
			}
			if(instructor2.equals("default")){
				return true;
			}
			return false;
		}

	
	/**
	 * Takes the name of an instructor and makes it valid. All characters which
	 * are not letters, numbers or spaces are turned into underscore characters.
	 */
	private void makeInstructorNameValid() {
		instructor = instructor.trim();
		instructor = instructor.replaceAll("[[^a-z]&&[^A-Z]&&\\D&&\\S]", "_");
	}

	/**
	 * Sets whether a user is in the process of adding an instructor or not.
	 * The state of certain buttons and other variables are determined by whether
	 * or not the process is occurring.
	 * 
	 * @param adding a boolean - true if adding an instructor, false otherwise
	 */
	private void addingInstructor(boolean adding){
			if(adding){
				logInButton.setText("Create instructor and Log In");
				instructorDropDown.setEditable(adding);
				if(!instructorDropDown.getItemAt(0).equals("")){
				instructorDropDown.insertItemAt(defaultText,0);

				instructorDropDown.setSelectedIndex(0);
				this.instructorDropDown.requestFocus();
				instructorTextField = (JTextField)instructorDropDown.getEditor().getEditorComponent();
				instructorTextField.selectAll();
				addingInstructor = true;
				}
				logInButton.setEnabled(false);
				
				deleteButton.setEnabled(!adding);
			}
			else{
				logInButton.setText("Log In");
				logInButton.setEnabled(true);
				instructorDropDown.setEditable(adding);
				deleteButton.setEnabled(!adding);
				if(addingInstructor){
					instructorDropDown.removeItemAt(0);
					
					if(done){
						instructorDropDown.insertItemAt(nameTyped , alphabeticalPosition(nameTyped));
					}
				}
				addButton.setEnabled(!adding);
				addingInstructor = false;
			}
		}

	
	/**
	 * Gets the alphabetical position of an instructor in the instructor
	 * drop down menu.
	 * 
	 * @param name a String of the name of an instructor
	 * @return an int of its position alphabetically
	 */
	private int alphabeticalPosition(String name) {
		for(int i=0; i<instructorDropDown.getItemCount(); i++){
			if(name.compareToIgnoreCase((String)instructorDropDown.getItemAt(i)) < 0){
				return i;
			}
		}
		return instructorDropDown.getItemCount() - 1;
	}


	
	//-------------------------------------------------------------------------
	// Inner Class
	//-------------------------------------------------------------------------

	/**
	 * Serves as the content pane for the LoginScreen. Overrides the paint method to
	 * include the Engage background image.
	 * 
	 * @author Kayre Hylton
	 *
	 */
	private class ContentPane extends JPanel{
		
		/**
		 * Overrides JPanel's paint method to include the Engage background image
		 * @param g the Graphics to paint on
		 */
		public void paint(Graphics g){
			super.paint(g);
			
			ImageIcon bgImageIcon = new ImageIcon("ui" + File.separator + 
					"login" + File.separator + "loginScreenBG.jpg");
			Image bgImage = bgImageIcon.getImage();
			g.drawImage(bgImage, 0, 0, null);
			this.paintChildren(g);
		}
	}




	
	
}