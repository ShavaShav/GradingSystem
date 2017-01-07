package assets;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
// controller/view for adding a course
public class AddCourseFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = -2354363633518578045L;
	private Program p_model;
	JTextField nameIn, codeIn, creditIn, semYear, roomIn;
	JComboBox<String> semSeason;
	JButton submit, cancel;
	
	public AddCourseFrame(Program p){
		p_model = p;
		setSize(350,250);
		setLayout(new GridLayout(6,0));
		setTitle("New Course");
		
		JPanel line = new JPanel();
		line.setLayout(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Name: "));
		nameIn = new JTextField(25);
		line.add(nameIn);
		this.add(line);
		
		line = new JPanel();
		line.setLayout(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Course Code: "));
		codeIn = new JTextField(8);
		line.add(codeIn);
		this.add(line);
		
		line = new JPanel();
		line.setLayout(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Credits: "));
		creditIn = new JTextField(3);
		line.add(creditIn);
		this.add(line);
		
		line = new JPanel();
		line.setLayout(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Season: "));
		semSeason = new JComboBox<String>(Semester.getValidSeasons());
		line.add(semSeason);
		line.add(new JLabel("Year: "));
		semYear = new JTextField(4);
		line.add(semYear);
		this.add(line);
		
		line = new JPanel();
		line.setLayout(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Room: "));
		roomIn = new JTextField(8);
		line.add(roomIn);
		this.add(line);
		
		line = new JPanel();
		line.setLayout(new FlowLayout());
		submit = new JButton("Add Course");
		submit.addActionListener(this);
		line.add(submit);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		line.add(cancel);
		this.add(line);
	}
	
	@Override // close window
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add Course")){
			String name = nameIn.getText();
			String code = codeIn.getText();
			int credits = Integer.parseInt(creditIn.getText());
			String room = roomIn.getText();
			try {
				Semester semester = new Semester((String) (semSeason.getSelectedItem()), Integer.parseInt(semYear.getText()));
				p_model.addCourse(new Course(name, code, credits, semester, room));
			} catch (NumberFormatException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		this.dispose();	
	}
}
