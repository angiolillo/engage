package ui.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import main.Logger;
import main.Engage;

public class LogoutButton extends JButton implements ActionListener{

	private Engage parent;
	
	public LogoutButton(Engage p){
		super(new ImageIcon("ui/nav/logout.png"));
		parent = p;
		this.addActionListener(this);
		this.setFocusPainted(false);
	}
	
	public void actionPerformed(ActionEvent a){
		parent.presentLoginScreen();
		parent.setVisible(false);
		Logger.log(Logger.IMPORTANT,"LogoutButton: Logging out.");
	}
	
}
