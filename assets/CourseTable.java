package assets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class CourseTable extends JTable {
	private static final long serialVersionUID = 3010793522782803214L;
	private TableRowSorter<TableModel> sorter;
	public CourseTable(CourseTableModel courseTableModel){
		super(courseTableModel);
		
		 // setting size of columns
		  getColumnModel().getColumn(1).setPreferredWidth(255);
		  getColumnModel().getColumn(2).setPreferredWidth(90);
		  getColumnModel().getColumn(5).setPreferredWidth(50);
		  // add row sorting
		  sorter = new TableRowSorter<TableModel>((TableModel) getModel());
	      setRowSorter(sorter);
	      List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
	      // default to sorted by Semester, Code
	      sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
	      sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
	      sorter.setSortKeys(sortKeys);
	}
}
