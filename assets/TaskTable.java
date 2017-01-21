package assets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.table.DatePickerCellEditor;

public class TaskTable extends JTable{
	private static final long serialVersionUID = -3699156481430600212L;

	public TaskTable(TaskTableModel model){
		super(model);
		
		getColumnModel().getColumn(0).setPreferredWidth(150);
		
		// format date column
		TableColumn dateColumn = getColumnModel().getColumn(1);
		 dateColumn.setCellEditor(new DatePickerCellEditor());
		 
		 // add row sorting
		 TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>((TableModel) getModel());
		 setRowSorter(sorter);
		 List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		 // default sort by due date
		 sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		 sorter.setSortKeys(sortKeys);
	}
}
