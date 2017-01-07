package assets;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
// view/controller to add a new task to a given course
public class AddTaskFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = -5803783168709979887L;
	JTextField nameIn, weightIn;
	JDatePickerImpl dateIn;
	JButton submit, cancel;
	private Course c_model;
	
	public AddTaskFrame(Course c){
		c_model = c;
		setSize(350,200);
		setLayout(new GridLayout(4,0));
		setTitle("New Task");
		JPanel line = new JPanel(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Task: "));
		nameIn = new JTextField(25);
		line.add(nameIn);
		this.add(line);
		
		line = new JPanel(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Date: "));
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		dateIn = new JDatePickerImpl(datePanel, new DateComponentFormatter());
	    line.add(dateIn);
	    this.add(line);
		
		line = new JPanel();
		line = new JPanel(new FlowLayout(FlowLayout.LEADING));
		line.add(new JLabel("Weight: "));
		weightIn = new JTextField(8);
		line.add(weightIn);
		this.add(line);
		
		line = new JPanel(new FlowLayout(FlowLayout.LEADING));
		submit = new JButton("Add Task");
		submit.addActionListener(this);
		line.add(submit);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		line.add(cancel);
		this.add(line);
	}

	@Override // handle new task creation and window disposal
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add Task")){
			System.out.println("Adding task");
			String name = nameIn.getText();
			double weight = Double.parseDouble(weightIn.getText());
			Task t = new Task(name);
			c_model.addTask(t);
			t.setWeight(weight);
			try{
				Date dueDate = (Date) (dateIn.getModel().getValue());
				if (dueDate != null)
					t.setDueDate(dueDate);
			} catch (Exception ex) {
				System.out.println("Invalid date: " + dateIn.getJFormattedTextField().getText());
			}
		}
		this.dispose();	
	}
}
