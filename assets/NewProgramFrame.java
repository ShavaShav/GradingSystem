package assets;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
// controller/view for creating a new course
public class NewProgramFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 2035857104633151983L;
	private ProgramFrame p_view;
	private JLabel name, credits;
	private JTextField nameP, creditsP;
	private JButton submit, cancel;
	
	public NewProgramFrame(ProgramFrame view){
		this.p_view = view;
		this.setSize(400, 200);
		this.setLayout(new GridLayout(3, 2));
		this.setTitle("New Program");
		name = new JLabel("Name of Program: ");
		credits = new JLabel("Credits to Earn: ");
		nameP = new JTextField();
		creditsP = new JTextField();
		submit = new JButton("OK");
		cancel = new JButton("Cancel");
		this.add(name);
		this.add(nameP);
		this.add(credits);
		this.add(creditsP);
		this.add(submit);
		this.add(cancel);
		System.out.println("Adding action listeners");
		submit.addActionListener(this);
		cancel.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Button pressed");
		if (e.getActionCommand().equals("OK")){
			System.out.println("Closing window");
			p_view.dispose(); // get rid of old frame
			StartProgram.go(new Program(getName(), getCredits()));
		}
		this.dispose();
	}	
	
	public String getName(){
		return nameP.getText();
	}
	
	public int getCredits(){
		return Integer.valueOf(creditsP.getText());
	}
}
