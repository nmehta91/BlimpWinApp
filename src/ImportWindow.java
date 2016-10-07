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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.awt.Color;
import java.awt.Desktop.Action;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

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
	private JTable VariablesTable;
	private String[][] variablesTableContents;
	private String[] variableNames;
	private JScrollPane scrollPane;
	private JScrollPane variableTableSB;
	private JScrollPane scrollPane_1;
	/**
	 * Launch the application.
	 */
	
    class VariablesTableModel extends AbstractTableModel {
    	private String[] columnNames = {"Variable Name", "Variable Type"};
    	private Object[][] data;

    	public VariablesTableModel() {
    		data = new Object[model.variables.size()][2];
    		for(int i = 0; i < model.variables.size(); i++){
    			data[i][0] = model.variables.get(i).name;
    			data[i][1] = model.variables.get(i).type;
    		}
		}
    	
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return model.variables.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
        	if(col == 0){
	    		return model.variables.get(row).name;
	    	}
	    	else if(col == 1){
	    		return model.variables.get(row).type;
	    	}
	    	return null;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
        	return true;
        }

        public void setValueAt(Object value, int row, int col) {
        	if(col == 0){
        		// Update Variable Name
        		// Validate before changing variable name
        		boolean shouldChange = validateCellChange(value, row, col);
        		if(shouldChange){
        			data[row][0] = value.toString();
            		model.variables.get(row).name = value.toString();
            		changeDataTableHeader(value.toString(), row);
                	fireTableCellUpdated(row, col);
        		}
        	}
        	else if(col == 1) {
        		// Update variable scale
        		data[row][1] = value;
                model.variables.get(row).type = value.toString();
                fireTableCellUpdated(row, col);
        		
        	}	
        }
        
        public boolean validateCellChange(Object value, int row, int col) {
        	String name = value.toString();
        	if(!name.equals("")){
    			//Check if the new variable name contains whitespaces, if yes skip 
    			Pattern pattern = Pattern.compile("\\s");
    			Matcher matcher = pattern.matcher(name);
    			boolean found = matcher.find();
    			
    			if(!found && !checkIfDuplicate(name)){
    				return true;
    			} else if(found) {
    				JOptionPane.showMessageDialog(contentPane,
    						"Variable name cannot contain white spaces.",
    						"Error!",
    						JOptionPane.ERROR_MESSAGE);
    			} else if(checkIfDuplicate(name)) {
    				JOptionPane.showMessageDialog(contentPane,
    						"Variable name already exists. Please choose another name.",
    						"Error!",
    						JOptionPane.ERROR_MESSAGE);
    			}
    		}
        	
        	return false;
        }
        
        public boolean checkIfDuplicate(String name) {
    		boolean found = false;
    		for(int i = 0; i < model.variables.size(); i++) {
    			String lname = model.variables.get(i).name.toLowerCase();
    			System.out.println("Checking "+lname);
    			if(lname.equals(name.toLowerCase()))
    				found = true;
    		}
    		return found;
    	}
    }
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

	/**
	 * Create the frame.
	 */
	public ImportWindow(Window parent) {
		setTitle("Import Window");
		model = SyntaxModel.getInstance();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 829, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JFrame frame = this;
		ImageIcon img = new ImageIcon("Resources\\blimplogo_32x32.png");
		System.out.println("width: "+ img.getIconWidth() + "height:" + img.getIconHeight());
		frame.setIconImage(img.getImage());
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{723, 0};
		gbl_contentPane.rowHeights = new int[]{449, 23, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new doneActionListener(this));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		dataPanel = new JPanel();
		tabbedPane.addTab("Data", null, dataPanel, null);
		GridBagLayout gbl_dataPanel = new GridBagLayout();
		gbl_dataPanel.columnWidths = new int[]{64, 120, 86, 68, 397, 0, 0, 0};
		gbl_dataPanel.rowHeights = new int[]{31, 20, 37, 0, 78, 187, 0, 0, 0};
		gbl_dataPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_dataPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		dataPanel.setLayout(gbl_dataPanel);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.gridheight = 5;
		gbc_scrollPane_1.gridx = 4;
		gbc_scrollPane_1.gridy = 0;
		dataPanel.add(scrollPane_1, gbc_scrollPane_1);
		
			rawDataView = new JTextArea();
			scrollPane_1.setViewportView(rawDataView);
			rawDataView.setWrapStyleWord(false);
			rawDataView.setLineWrap(false);	
			rawDataView.setText(model.importFileContentsInString);
			
			variablePanel = new JPanel();
			tabbedPane.addTab("Variable", null, variablePanel, null);
			variablePanel.setLayout(null);
			
			variableTableSB = new JScrollPane();
			variableTableSB.setBounds(182, 11, 341, 405);
			variablePanel.add(variableTableSB);
			
			VariablesTable = new JTable(model);
			VariablesTable.setOpaque(true);
			VariablesTable.setFillsViewportHeight(true);
			VariablesTable.setBackground(Color.WHITE);
			VariablesTable.getTableHeader().setReorderingAllowed(false);
			
					VariablesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					variableTableSB.setViewportView(VariablesTable);
					
					JButton btnImport = new JButton("Import");
					btnImport.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String delimiter;
							if ((String) DelimiterComboBox.getSelectedItem() == "Space") {
								delimiter = "\\s+";
							} else {
								delimiter = ",";
							}
						
							if(parseData(delimiter)) {
								initializeVariablesTable(initializeParsedFileTableView());
							} else {
								JOptionPane.showMessageDialog(contentPane.getParent(),
										"There was an error in parsing the file. Please re-select the appropriate delimiter.",
										"Error!",
										JOptionPane.ERROR_MESSAGE);
							}
						}	
					});
					
					JLabel lblNewLabel = new JLabel("Delimiter");
					GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
					gbc_lblNewLabel.anchor = GridBagConstraints.SOUTH;
					gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
					gbc_lblNewLabel.gridx = 1;
					gbc_lblNewLabel.gridy = 2;
					dataPanel.add(lblNewLabel, gbc_lblNewLabel);
					
					DelimiterComboBox = new JComboBox();
					DelimiterComboBox.setModel(new DefaultComboBoxModel(new String[] {"Comma", "Space"}));
					GridBagConstraints gbc_DelimiterComboBox = new GridBagConstraints();
					gbc_DelimiterComboBox.anchor = GridBagConstraints.SOUTH;
					gbc_DelimiterComboBox.fill = GridBagConstraints.HORIZONTAL;
					gbc_DelimiterComboBox.insets = new Insets(0, 0, 5, 5);
					gbc_DelimiterComboBox.gridx = 2;
					gbc_DelimiterComboBox.gridy = 2;
					dataPanel.add(DelimiterComboBox, gbc_DelimiterComboBox);
					
					JLabel lblMissingValueCode = new JLabel("Missing Value Code");
					GridBagConstraints gbc_lblMissingValueCode = new GridBagConstraints();
					gbc_lblMissingValueCode.insets = new Insets(0, 0, 5, 5);
					gbc_lblMissingValueCode.gridx = 1;
					gbc_lblMissingValueCode.gridy = 3;
					dataPanel.add(lblMissingValueCode, gbc_lblMissingValueCode);
					
					MV_Code = new JTextField();
					GridBagConstraints gbc_MV_Code = new GridBagConstraints();
					gbc_MV_Code.fill = GridBagConstraints.HORIZONTAL;
					gbc_MV_Code.anchor = GridBagConstraints.NORTH;
					gbc_MV_Code.insets = new Insets(0, 0, 5, 5);
					gbc_MV_Code.gridx = 2;
					gbc_MV_Code.gridy = 3;
					dataPanel.add(MV_Code, gbc_MV_Code);
					MV_Code.setColumns(10);
					GridBagConstraints gbc_btnImport = new GridBagConstraints();
					gbc_btnImport.insets = new Insets(0, 0, 5, 5);
					gbc_btnImport.gridx = 2;
					gbc_btnImport.gridy = 4;
					dataPanel.add(btnImport, gbc_btnImport);
					
					
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridwidth = 4;
					gbc_scrollPane.gridx = 1;
					gbc_scrollPane.gridy = 5;
					dataPanel.add(scrollPane, gbc_scrollPane);
					
						
							dataTable = new JTable(model);
							dataTable.setAutoResizeMode(0);
							dataTable.getTableHeader().setReorderingAllowed(false);	
							
							scrollPane.setViewportView(dataTable);
							dataTable.setBorder(new LineBorder(new Color(0, 0, 0)));
							
							DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)dataTable.getDefaultRenderer(Object.class);
		GridBagConstraints gbc_btnDone = new GridBagConstraints();
		gbc_btnDone.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnDone.gridx = 0;
		gbc_btnDone.gridy = 1;
		contentPane.add(btnDone, gbc_btnDone);
		
		String[] variableTBColumnHeadings = {"Variable Name", "Variable Type"};
		DefaultTableModel model = new DefaultTableModel(25, variableTBColumnHeadings.length) {
			@Override
			   public boolean isCellEditable(int row, int column) {
			       //Only the third column
			       return false;
			   }
		};
		model.setColumnIdentifiers(variableTBColumnHeadings);
		
		String[] dataColHeadings = new String[10];
		for(int i = 0; i < 10; i++) {
			dataColHeadings[i] = "V"+(i+1);
		}
		
		initializeDataTable();
	}
	
	public String[] initializeParsedFileTableView() {
		variableNames = new String[columns];
		System.out.println(columns);
		
		// To ensure if Import button clicked more than once, variables don't get duplicated
		model.variables.clear();
		
		for(int i = 0; i< variableNames.length; i++) {
			String name = "VAR" + (i+1);
			Variable newVariable = new Variable(name, "Continuous", i);
			variableNames[i] = name;
			model.variables.add(newVariable);
		}
		
		DefaultTableModel tm = (DefaultTableModel)dataTable.getModel();
		tm.setColumnIdentifiers(variableNames);
		tm.setRowCount(0);
		
		for(int i = 0; i < contents.length; i++) {
			tm.addRow(contents[i]);
		}
		
		return variableNames;
	}
	
	public void initializeDataTable() {
		String[] defaultColumnHeadings = {"VAR1", "VAR2", "VAR3", "VAR4", "VAR5", "VAR6", "VAR7", "VAR8", "VAR9", "VAR10"};
		int numOfRows = 10;

		DefaultTableModel model = new DefaultTableModel(numOfRows, defaultColumnHeadings.length) {
			@Override
			   public boolean isCellEditable(int row, int column) {
			       //Only the third column
			       return false;
			   }
		};
		model.setColumnIdentifiers(defaultColumnHeadings);
		for(int i = 0; i < defaultColumnHeadings.length; i++) {
			//TableColumn column = dataTable.getColumnModel().getColumn(i);
			//column.setPreferredWidth(70);
		}
		renderer.setHorizontalAlignment( JLabel.RIGHT );
	}
	
	public void initializeVariablesTable(String[] variableNames) {

		VariablesTable.setModel(new VariablesTableModel());
		TableColumn variableScaleCol = VariablesTable.getColumnModel().getColumn(1);
		JComboBox<String> variableScales = new JComboBox<String>();
		variableScales.addItem("Continuous");
		variableScales.addItem("Ordinal");
		variableScales.addItem("Nominal");
		variableScaleCol.setCellEditor(new DefaultCellEditor(variableScales));

        VariablesTable.setRowHeight(25);
		VariablesTable.repaint();
	
	}
	
	public boolean parseData(String delimiter) {
		
		boolean success = true;
		parsedFile = new ArrayList<String[]>();
		
		int rows = model.importFileContents.size();
		ArrayList<String> parsedLine = new ArrayList<String>();
		String[] temp = model.importFileContents.get(0).split(delimiter);
		for(String element: temp) {
			if(element.trim().length() <= 0){
				System.out.println("Element is blank");
			} else {
				parsedLine.add(element);
			}
		}
		columns = parsedLine.size();
		contents = new String[rows][columns];
		System.out.println("rows: "+rows);
		System.out.println("columns:"+columns);
	
		for(int i = 0; i < rows; i++){
			try{
				ArrayList<String> trimmedParsedLine = new ArrayList<String>();
				String[] line = model.importFileContents.get(i).split(delimiter);
				System.out.println(Arrays.toString(line).trim());
				for(String element: line) {
					if(element.trim().length() <= 0){
						System.out.println("Element is blank");
					} else {
						trimmedParsedLine.add(element);
					}
				}
				
				for(int j = 0; j < columns; j++) {
					contents[i][j] = String.format("%.3g%n", Double.parseDouble(trimmedParsedLine.get(j)));
				}
			} catch(Exception e) {
				success = false;
				break;
			}
		}
		return success;
	}
		
	public boolean checkIfDuplicate(String name) {
		boolean found = false;
		for(int i = 0; i < model.variables.size(); i++) {
			String lname = model.variables.get(i).name.toLowerCase();
			if(lname.equals(name.toLowerCase()))
				found = true;
		}
		return found;
	}

	public void changeDataTableHeader(String newName, int col) {
		dataTable.getColumnModel().getColumn(col).setHeaderValue(newName);
		dataTable.getColumnModel().getColumn(col).setPreferredWidth(70);
		dataTable.getTableHeader().repaint();
	}
	
	class doneActionListener implements ActionListener{

	    private JFrame toBeClose;

	    public doneActionListener(JFrame toBeClose) {
	        this.toBeClose = toBeClose;
	    }

	    public void actionPerformed(ActionEvent e) {
	    	if(!MV_Code.getText().equals("")){
	    		model.mappings.put("MVC", MV_Code.getText());
	    		toBeClose.setVisible(false);
		        toBeClose.dispose();
	    	} else {
	    		// Missing value textbox is empty
	    		JOptionPane.showMessageDialog(contentPane,
						"Please enter a missing value code and then click Done.",
						"Error!",
						JOptionPane.ERROR_MESSAGE);
	    	}
	    }
	}
	
}
