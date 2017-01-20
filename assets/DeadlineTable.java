package assets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DeadlineTable extends JTable {
	private static final long serialVersionUID = 7786589151124398262L;

	public DeadlineTable(DeadlineTableModel model){
		 super(model);
		 
		 // add row sorting
		 TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>((TableModel) getModel());
		 setRowSorter(sorter);
		 List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		 // default sort by due date
		 sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
		 sorter.setSortKeys(sortKeys);
	}
}
