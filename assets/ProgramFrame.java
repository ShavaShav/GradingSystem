package assets;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class ProgramFrame extends JFrame implements Observer, ActionListener{
	private static final long serialVersionUID = 1L;
	private Program program;	
	private HashMap<Course, CourseController> c_controllers;
	
	private JButton newProgram, addCourse, saveButton, loadButton;	
	private JLabel progLabel, credLabel, cumAvgLabel, mjrAvgLabel;
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
		JPanel gradeLabels = new JPanel(new GridLayout(0,3));
		cumAvgLabel = new JLabel("Cumulative Average: ");
		gradeLabels.add(cumAvgLabel);
		mjrAvgLabel = new JLabel("\tMajor Average: ");
		gradeLabels.add(mjrAvgLabel);
		mainLabels.add(gradeLabels);;
		completionBar = new JProgressBar(0, program.getCredits());
		completionBar.setValue(0);
		completionBar.setStringPainted(true);
		mainLabels.add(completionBar);
		mainPanel.add(mainLabels, BorderLayout.PAGE_START);
		if (program.getNumCourses() == 0){
			coursePane = new JScrollPane();
			coursePane.add(new JLabel("No courses yet..."));
		} else {
			coursePane = createCoursePane(program.getCourseList());
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
		
		JButton deleteCourse = new JButton("Delete Course");
		deleteCourse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (courseTable.getSelectedRow() >= 0)
					program.removeCourse(courseTable.getSelectedRow());
			}
		});
		bottom.add(deleteCourse);
		return bottom;
	}
	
	// returns scroll pane containing course table
	public JScrollPane createCoursePane(ArrayList<Course> courseList){
		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 3400114293267312129L;
			
			String[] columnNames = {"Course #",
														"Name",
														"Semester",
														"Major",
														"Grade",
														"Credits",
														"Complete?"};
	          public int getRowCount() { 
	        	  return courseList.size();
	          }
	          public int getColumnCount() {
	        	  return columnNames.length;
	          }
	          public String getColumnName(int index){
	        	  return columnNames[index];
	          }
	          public Class<?> getColumnClass(int columnIndex){
	        	  switch (columnIndex){
	        	  case 3: // complete?
	        	  		return  Boolean.class;
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
	        	  Course c = courseList.get(row);
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
		        		  c.setMajor((Boolean)value);
		        		  break;
		        	  case 4: 
		        		  // percentage view to fractional
		        		  Double grade = Double.parseDouble(((String)value).replace('%', '\0'));
		        		  c.setGrade(grade * 0.01);
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
	        	  Course c = courseList.get(row);
	        	  switch (col){
		        	  case 0: return c.getCode();
		        	  case 1: return c.getName();
		        	  case 2: return c.getSemester().toString();
		        	  case 3: return c.isMajor();
		        	  case 4: return c.getGrade() * 100 + " %"; // fractional to percentage view
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
	      
	      // setting size of columns
	      courseTable.getColumnModel().getColumn(1).setPreferredWidth(255);
	      courseTable.getColumnModel().getColumn(2).setPreferredWidth(90);
	      courseTable.getColumnModel().getColumn(5).setPreferredWidth(50);
	      
	      
	      final JTableHeader header = courseTable.getTableHeader();  
	      boolean[] sorted = new boolean[dataModel.getColumnCount()];
	      header.addMouseListener(new MouseAdapter() {  
	            public void mouseClicked(MouseEvent e) { 
	                int col = header.columnAtPoint(e.getPoint());  
	                if(header.getCursor().getType() == Cursor.E_RESIZE_CURSOR)  
	                    e.consume();  
	                else {  
	                    System.out.printf("Sorting %s in %s order.%n", 
	                    		dataModel.getColumnName(col), sorted[col] ? "ascending" : "descending"); 
	                    
	                    courseTable.clearSelection();
	                    courseTable.setColumnSelectionInterval(col,col);
	                    switch (col){
	                    	case 0: // sort by Code		
	                    		courseList.sort(new CourseComparator(sorted[col], CourseComparator.CODE));
	                    		break;
	                    	case 1: // sort by course Name		
	                    		courseList.sort(new CourseComparator(sorted[col], CourseComparator.NAME));
	                    		break;
	                    	case 2: // sort by Semester
	                    		courseList.sort(new CourseComparator(sorted[col], CourseComparator.SEMESTER));
	                    		break;
	                    	case 3:
	                    		courseList.sort(new CourseComparator(sorted[col], CourseComparator.MAJOR));
	                    		break;
	                    	case 4:
	                    		courseList.sort(new CourseComparator(sorted[col], CourseComparator.GRADE));
	                    		break;
	                    	case 5:
	                    		courseList.sort(new CourseComparator(sorted[col], CourseComparator.CREDITS));
	                    		break;
	                    	case 6:
	                    		courseList.sort(new CourseComparator(sorted[col], CourseComparator.COMPLETE));
	                    		break;
	                    }
	                    sorted[col] = !sorted[col]; // reverse flag so next time with sort in reverse
	                    //dataModel[selectedTab].sortArrayList(col);  
	                }  
	            }  
	        });  

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
		String cumAvg = String.format("%.2f", program.getCumulativeAverage()*100);
		cumAvgLabel.setText("Cumulative Average: "+ cumAvg +" %");
		String mjrAvg = String.format("%.2f", program.getMajorAverage()*100);
		mjrAvgLabel.setText("Major Average: " + mjrAvg + " %");
		completionBar.setValue(program.getCreditsEarned());
		// Update course pane
		if (program.getNumCourses() > 0){
			mainPanel.remove(coursePane);
			coursePane = createCoursePane(program.getCourseList());
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
