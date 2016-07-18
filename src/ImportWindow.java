import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JScrollPane;

public class ImportWindow extends JFrame {

	private JPanel contentPane;
	private SyntaxModel model;
	private JTextField MV_Code;
	private JComboBox DelimiterComboBox;
	private JTextArea rawDataView;
	private JTable dataTable;
	private ArrayList<String[]> parsedFile;
	private String[][] contents;
	private int columns;
	private JPanel dataPanel;
	private JPanel variablePanel;
	private JTextField newVarName;
	private JTable VariablesTable;
	private JComboBox<String> variableNamesComboBox;
	private String[][] variablesTableContents;
	/**
	 * Launch the application.
	 */
	public static void showWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					ImportWindow frame = new ImportWindow();
//					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	class VariablesTableModel extends AbstractTableModel {
		private String[] columnNames;
		private Object[][] data;
		
		VariablesTableModel(String[] colnames, Object[][] data) {
			this.columnNames = colnames;
			this.data = data;
		}
		  public int getColumnCount() {
		        return columnNames.length;
		    }

		    public int getRowCount() {
		        return data.length;
		    }

		    public String getColumnName(int col) {
		        return columnNames[col];
		    }

		    public Object getValueAt(int row, int col) {
		        return data[row][col];
		    }
		    
		    public boolean isCellEditable(int row, int col) {
		        switch (col) {
		            case 0:
		            case 1:
		                return true;
		            default:
		                return false;
		         }
		   }
		    
		   public void setValueAt(Object value, int row, int col) {
		        data[row][col] = value;
		        fireTableCellUpdated(row, col);
		    }
	}
	
	/**
	 * Create the frame.
	 */
	public ImportWindow(Window parent) {
		setTitle("Import Window");
		model = SyntaxModel.getInstance();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 615);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 5, 735, 523);
		contentPane.add(tabbedPane);
		
		dataPanel = new JPanel();
		tabbedPane.addTab("Data", null, dataPanel, null);
		dataPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Delimiter");
		lblNewLabel.setBounds(28, 25, 73, 14);
		dataPanel.add(lblNewLabel);
		
		JLabel lblMissingValueCode = new JLabel("Missing Value Code");
		lblMissingValueCode.setBounds(10, 68, 120, 14);
		dataPanel.add(lblMissingValueCode);
		
		MV_Code = new JTextField();
		MV_Code.setBounds(131, 65, 86, 20);
		dataPanel.add(MV_Code);
		MV_Code.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(285, 11, 356, 165);
		
		rawDataView = new JTextArea();
		rawDataView.setWrapStyleWord(false);
		rawDataView.setLineWrap(false);
		rawDataView.setBounds(285, 11, 356, 165);
		
		scrollPane.setViewportView(rawDataView);
		dataPanel.add(scrollPane);
		
		rawDataView.setText(model.importFileContentsInString);
		
		DelimiterComboBox = new JComboBox();
		DelimiterComboBox.setModel(new DefaultComboBoxModel(new String[] {"Comma", "Space"}));
		DelimiterComboBox.setBounds(131, 22, 86, 20);
		dataPanel.add(DelimiterComboBox);
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String delimiter;
				if ((String) DelimiterComboBox.getSelectedItem() == "Space") {
					delimiter = "\\s+";
				} else {
					delimiter = ",";
				}
				System.out.println(DelimiterComboBox.getSelectedItem());
				parseData(delimiter);
				initializeVariablesTable(initializeParsedFileTableView());
				
				if(!MV_Code.getText().equals(""))
					model.mappings.put("MVC", MV_Code.getText());
			}
		});
		
		btnImport.setBounds(80, 120, 89, 23);
		dataPanel.add(btnImport);
		
		variablePanel = new JPanel();
		tabbedPane.addTab("Variable", null, variablePanel, null);
		variablePanel.setLayout(null);
		
		JLabel lblVariable = new JLabel("Variable");
		lblVariable.setBounds(29, 27, 86, 14);
		variablePanel.add(lblVariable);
		
		JLabel lblNewName = new JLabel("New Name");
		lblNewName.setBounds(29, 67, 86, 14);
		variablePanel.add(lblNewName);
		
		JLabel lblVariableScale = new JLabel("Variable Scale");
		lblVariableScale.setBounds(29, 108, 86, 14);
		variablePanel.add(lblVariableScale);
		
		newVarName = new JTextField();
		newVarName.setBounds(125, 64, 150, 20);
		variablePanel.add(newVarName);
		newVarName.setColumns(10);
		
		JComboBox<String> varTypes =  new JComboBox<String>();
		varTypes.setModel(new DefaultComboBoxModel(new String[] {"Continuous", "Nominal", "Ordinal"}));
		varTypes.setBounds(125, 105, 150, 20);
		variablePanel.add(varTypes);
		
		JButton btnSaveVarDetails = new JButton("Save");
		btnSaveVarDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedVariable = variableNamesComboBox.getSelectedIndex();
				String newName = newVarName.getText();
				String newType = (String)varTypes.getSelectedItem();
				saveVariableChanges(selectedVariable, newName, newType);
			}
		});
		btnSaveVarDetails.setBounds(86, 160, 89, 23);
		variablePanel.add(btnSaveVarDetails);
		
		JButton btnPrintVariables = new JButton("Print Variables");
		btnPrintVariables.addActionListener(new doneActionListener(this));
		btnPrintVariables.setBounds(86, 194, 101, 23);
		variablePanel.add(btnPrintVariables);
		
		variableNamesComboBox = new JComboBox<String>();
		variableNamesComboBox.setBounds(125, 24, 150, 20);
		variablePanel.add(variableNamesComboBox);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new doneActionListener(this));
		
		btnDone.setBounds(601, 542, 137, 23);
		contentPane.add(btnDone);
		btnDone.setForeground(Color.BLACK);
		btnDone.setBackground(Color.LIGHT_GRAY);
		
		String[] dataColHeadings = new String[10];
		for(int i = 0; i < 10; i++) {
			dataColHeadings[i] = "V"+(i+1);
		}
	}
	
	public String[] initializeParsedFileTableView() {
		String[] variableNames = new String[columns];
		System.out.println(columns);
	
		
		for(int i = 0; i< variableNames.length; i++) {
			String name = "V" + (i+1);
			Variable newVariable = new Variable(name, "Continuous", i);
			variableNames[i] = name;
			model.variables.add(newVariable);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(285, 199, 356, 172);
		dataPanel.add(scrollPane);
		
		//dataTable = new JTable(contents, variableNames);
		dataTable = new JTable(new VariablesTableModel(variableNames, contents));
		scrollPane.setViewportView(dataTable);
		dataTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)dataTable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment( JLabel.RIGHT );
		return variableNames;
	}
	public void parseData(String delimiter) {
		
		parsedFile = new ArrayList<String[]>();
		
		int rows = model.importFileContents.size();
		columns = model.importFileContents.get(0).split(delimiter).length;
		contents = new String[rows][columns];
		System.out.println("rows: "+rows);
		System.out.println("columns:"+columns);
	
		for(int i = 0; i < rows; i++){
			String[] line = model.importFileContents.get(i).split(delimiter);
			for(int j = 0; j < columns; j++) {
				contents[i][j] = String.format("%.3g%n", Double.parseDouble(line[j]));
			}
		}
	}
	
	public void initializeVariablesTable(String[] variableNames) {
		String[] header = {"Variable Name", "Variable Type"};
		variablesTableContents = new String[variableNames.length][2];
		
		for(int i = 0; i < variableNames.length; i++){
				variablesTableContents[i][0] = variableNames[i];
				variablesTableContents[i][1] = "Continuous";
				System.out.println("Name: " + variablesTableContents[i][0]);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(293, 11, 348, 360);
		variablePanel.add(scrollPane);
		
		
		VariablesTable = new JTable(new VariablesTableModel(header, variablesTableContents));
		scrollPane.setViewportView(VariablesTable);
		VariablesTable.setBorder(new LineBorder(new Color(0, 0, 0)));

		variableNamesComboBox.setModel(new DefaultComboBoxModel<String>(variableNames));
	}
	
	public void saveVariableChanges(int index, String name, String type){
		Variable variable = model.variables.get(index);
		System.out.println("name: " + name);
		if(!name.equals("")){
			//Check if the new variable name contains whitespaces, if yes skip 
			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(name);
			boolean found = matcher.find();
			if(!found && !checkIfDuplicate(name)){
				variable.name = name;
				VariablesTable.getModel().setValueAt(name, index, 0);
				dataTable.getColumnModel().getColumn(index).setHeaderValue(name);
				dataTable.getTableHeader().repaint();
			}
		}
		variable.type = type;
		VariablesTable.getModel().setValueAt(type, index, 1);
		model.variables.remove(index);
		model.variables.add(index, variable);
	}
	
	public boolean checkIfDuplicate(String name) {
		boolean found = false;
		for(int i = 0; i < model.variables.size(); i++) {
			if(model.variables.get(i).name.equals(name))
				found = true;
		}
		return found;
	}
	class doneActionListener implements ActionListener{

	    private JFrame toBeClose;

	    public doneActionListener(JFrame toBeClose) {
	        this.toBeClose = toBeClose;
	    }

	    public void actionPerformed(ActionEvent e) {
	        toBeClose.setVisible(false);
	        toBeClose.dispose();
	    }
	}
}
