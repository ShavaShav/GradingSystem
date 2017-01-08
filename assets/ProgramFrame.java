package assets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class ProgramFrame extends JFrame implements Observer, ActionListener{
	private static final long serialVersionUID = 1L;
	private Program program;	
	private HashMap<Course, CourseController> c_controllers;
	
	private JButton newProgram, addCourse, saveButton, loadButton;	
	private JLabel progLabel, credLabel, cumAvgLabel;;
	private JPanel mainPanel;
	private JScrollPane coursePane;
	private JTable courseTable;
	private JProgressBar completionBar;
	
	private int height, width;
	
	
	public ProgramFrame(Program p){
		program = p;	// use model given
		c_controllers = new HashMap<Course, CourseController>();
		height = 600;
		width = 700;
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.setTitle("Grade Tracker");
		this.add(createHeader(), BorderLayout.PAGE_START);
		mainPanel = createMainPanel();
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(createFooter(), BorderLayout.PAGE_END);		
		try{
			updateScreen();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private JPanel createHeader(){
		JPanel programButtons = new JPanel(new FlowLayout());
		JPanel pb2 = new JPanel(new GridLayout(1,3));		
		newProgram = new JButton("New Program");
		loadButton = new JButton("Load Program");
		saveButton = new JButton("Save Program");
		pb2.add(newProgram);
		newProgram.addActionListener(this);
		// Load and Save listeners implemented by controller
		pb2.add(loadButton);
		pb2.add(saveButton);
		programButtons.add(pb2);
		return programButtons;
	}
	
	private JPanel createMainPanel(){
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel mainLabels = new JPanel(new GridLayout(0,1));
		mainLabels.setSize(width, 30);
		progLabel  = new JLabel("Program: ");
		credLabel  = new JLabel("Credits: ");
		mainLabels.add(progLabel);
		mainLabels.add(credLabel);
		cumAvgLabel = new JLabel("Cumulative Average: ");
		mainLabels.add(cumAvgLabel);
		completionBar = new JProgressBar(0, program.getCredits());
		completionBar.setValue(0);
		completionBar.setStringPainted(true);
		mainLabels.add(completionBar);
		mainPanel.add(mainLabels, BorderLayout.PAGE_START);
		if (program.getNumCourses() == 0){
			coursePane = new JScrollPane();
			coursePane.add(new JLabel("No courses yet..."));
		} else {
			coursePane = createCoursePane();
		}
		mainPanel.add(coursePane, BorderLayout.CENTER);		
		return mainPanel;
	}
	
	private JPanel createFooter(){
		JPanel bottom = new JPanel(new FlowLayout());	
		JButton viewCourse = new JButton("View Course Details");
		viewCourse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int tableRow = courseTable.getSelectedRow();
				if (tableRow >= 0){ // selected course
					Course c = program.getCourseList().get(tableRow);
					// if course doesn't have controller, add it on fly
					if (!c_controllers.containsKey(c)){
						CourseFrame c_view = new CourseFrame(c);
						c_controllers.put(c, new CourseController(c, c_view));							
					}
					// show the view for course using controller
					c_controllers.get(c).showView();
				}
			}
		});
		bottom.add(viewCourse);
		addCourse  = new JButton("Add Course");
		bottom.add(addCourse);
		addCourse.addActionListener(this);
		
		return bottom;
	}
	
	// returns scroll pane containing course table
	public JScrollPane createCoursePane(){
		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 3400114293267312129L;
			
			String[] columnNames = {"Course #",
														"Name",
														"Semester",
														"Room",
														"Grade",
														"Credits",
														"Complete?"};
	          public int getRowCount() { 
	        	  return program.getNumCourses();
	          }
	          public int getColumnCount() {
	        	  return columnNames.length;
	          }
	          public String getColumnName(int index){
	        	  return columnNames[index];
	          }
	          public Class<?> getColumnClass(int columnIndex){
	        	  switch (columnIndex){
	        	  	case 4: // grade
	        		  return  Double.class;
	        	  	case 5:  // credits
	        	  		return Integer.class;
	        	  	case 6: // complete?
	        	  		return  Boolean.class;
	        	  	default:
	        	  		return String.class;
	        	  }
	          }
	          public boolean isCellEditable(int row, int column){
	        	  return true;
	          }
	          public void setValueAt(Object value, int row, int col) {
	        	  System.out.println("Setting value");
	        	  Course c = program.getCourseList().get(row);
	        	  switch (col){
		        	  case 0: 
		        		  c.setCode((String)value);
		        		  break;
		        	  case 1: 
		        		  c.setName((String)value);
		        		  break;
		        	  case 2: 
		        		  String s = (String) value;
		        		  String[] sem = s.split(" ");
		        		  c.setSemester((String)sem[0], Integer.parseInt(sem[1]));
		        		  break;
		        	  case 3: 
		        		  c.setClassRoom((String)value);
		        		  break;
		        	  case 4: 
		        		  // percentage view to fractional
		        		  c.setGrade((Double)value * 0.01);
		        		  break;
		        	  case 5: 
		        		  c.setCredits((Integer)value);
		        		  break;
		        	  case 6: 
		        		  c.setComplete((Boolean)value);
		        		  break;
	        	  }
	              fireTableCellUpdated(row, col);
	              updateScreen();
	          }
	          public Object getValueAt(int row, int col) { 
	        	  Course c = program.getCourseList().get(row);
	        	  switch (col){
		        	  case 0: return c.getCode();
		        	  case 1: return c.getName();
		        	  case 2: return c.getSemester().toString();
		        	  case 3: return c.getClassRoom();
		        	  case 4: return c.getGrade() * 100; // fractional to percentage view
		        	  case 5: return c.getCreditHours();
		        	  case 6: return c.isComplete();
	        	  }
	        	  return null;
	          }
	      };
	      /*
	      dataModel.addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				TableModel model = (TableModel)e.getSource();
				String columnName = model.getColumnName(column);
				Object data = model.getValueAt(row, column);
				System.out.println(columnName);
			}  	  
	      });
	      */
	      courseTable = new JTable(dataModel);
	      return new JScrollPane(courseTable);
	}
	
	public void addLoadListener(ActionListener lal){
		loadButton.addActionListener(lal);
	}
	
	public void addSaveListener(ActionListener sal){
		saveButton.addActionListener(sal);
	}

	public void updateScreen(){
		// Update program information
		progLabel.setText("Program: " + program.getName());
		credLabel.setText("Credits: " + program.getCredits());
		cumAvgLabel.setText("Cumulative Average: "+program.getCumulativeGrade()*100+" %");
		completionBar.setValue(program.getCreditsEarned());
		// Update course pane
		if (program.getNumCourses() > 0){
			mainPanel.remove(coursePane);
			coursePane = createCoursePane();
			mainPanel.add(coursePane);			
		}
		
		this.setVisible(true);
	}

	// Updates screen when model is updated
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Program Frame has received update");
		updateScreen();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Program")){
			// pass this frame so that it can be closed by np_view
			NewProgramFrame np_view = new NewProgramFrame(this);
			np_view.setVisible(true);
			np_view.setAlwaysOnTop(true);
			np_view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);			
		} else if (e.getActionCommand().equals("Add Course")){
			AddCourseFrame ac_view = new AddCourseFrame(program);
			ac_view.setVisible(true);
			ac_view.setAlwaysOnTop(true);
			ac_view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}
	
}
