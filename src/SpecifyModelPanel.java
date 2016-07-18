import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

public class SpecifyModelPanel extends JPanel {
	private JTable variableTable;
	private JTable modelVariables;
	private JTable imputationVariablesTable;
	private SyntaxModel model;
	private int mode;
	/**
	 * Create the panel.
	 */
	
	class VariablesTableModel extends AbstractTableModel {
		private String columnNames;
		private ArrayList<Variable> data;
		
		VariablesTableModel(String colnames, ArrayList<Variable> data) {
			this.columnNames = colnames;
			this.data = data;
		}
		  public int getColumnCount() {
		        return 1;
		    }

		    public int getRowCount() {
		        return data.size();
		    }

		    public String getColumnName(int col) {
		        return columnNames;
		    }

		    public Object getValueAt(int row, int col) {
//		    	col = 0;
		        Variable var = data.get(row);
		        return var.name;
		    }
	

		    
//		    public boolean isCellEditable(int row, int col) {
//		        switch (col) {
//		            case 0:
//		            case 1:
//		                return true;
//		            default:
//		                return false;
//		         }
//		   }
		    
//		   public void setValueAt(Object value, int row, int col) {
//		        data[row][col] = value;
//		        fireTableCellUpdated(row, col);
//		    }
	}
	
	class ModelTableModel extends AbstractTableModel {
		private String columnNames;
		private ArrayList<Variable> data;
		
		ModelTableModel(String colnames, ArrayList<Variable> data) {
			this.columnNames = colnames;
			this.data = data;
		}
		  public int getColumnCount() {
		        return 1;
		    }

		    public int getRowCount() {
		        return data.size();
		    }

		    public String getColumnName(int col) {
		        return columnNames;
		    }

		    public Object getValueAt(int row, int col) {
//		    	col = 0;
		    	Variable var = data.get(row);
		        return var.name;
		    }
	

		    
//		    public boolean isCellEditable(int row, int col) {
//		        switch (col) {
//		            case 0:
//		            case 1:
//		                return true;
//		            default:
//		                return false;
//		         }
//		   }
		    
//		   public void setValueAt(Object value, int row, int col) {
//		        data[row][col] = value;
//		        fireTableCellUpdated(row, col);
//		    }
	}
	public SpecifyModelPanel() {
		setLayout(null);
		
		model = SyntaxModel.getInstance();
		mode = 0; // 0 = Main Effects, 1 = Random Slopes
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 44, 210, 367);
		add(scrollPane);
		
		variableTable = new JTable(new VariablesTableModel("Variables", model.variables));
		scrollPane.setViewportView(variableTable);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(442, 191, 183, 220);
		add(scrollPane_1);
		
		modelVariables = new JTable();
		scrollPane_1.setViewportView(modelVariables);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(442, 44, 183, 132);
		add(scrollPane_2);
		
		imputationVariablesTable = new JTable();
		scrollPane_2.setViewportView(imputationVariablesTable);
		
		JButton button = new JButton(">");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow[] = variableTable.getSelectedRows();
				System.out.println("Selected Row: " + selectedRow.length);
				
				if(mode == 0) {
					// Main Effects
					for(int i = 0; i < selectedRow.length; i++) {
						Variable variable = model.variables.get(selectedRow[i]);
						System.out.println(variable.name);
						int index = findInArrayList(model.modelVariables, variable.name);
						if(index != -1){
							System.out.println("Duplicate variable found. Not Adding..");
							break;
						}
						model.modelVariables.add(variable);
					}
					
				} else {
					// Random Slopes
					if(selectedRow.length < 2){
						System.out.println("Please select atleast 2 variables in Random Slopes mode");
					} else {
						ArrayList<String> selectedVars = new ArrayList<String>();
						for(int i = 0; i < selectedRow.length; i++) {
							int index = findInArrayList(model.modelVariables, model.variables.get(selectedRow[i]).name);
							if(index != -1){
								System.out.println("Duplicate variable found in Random Slopes. Removing duplicate first..");
								model.modelVariables.remove(index);
							}
							selectedVars.add(model.variables.get(selectedRow[i]).name);
						}
						
						ArrayList<String> combinations = generateCombinations(selectedVars);
						
						for(int i = 0; i < combinations.size(); i++){
							int index = findInArrayList(model.modelVariables, combinations.get(i));
							if(index != -1) {
								System.out.println("Combinated variable "+ combinations.get(i) + " already present. Removing it..");
								model.modelVariables.remove(index);
							}
							Variable newModelVariable = new Variable(combinations.get(i), "Undefined", -1);
							model.modelVariables.add(newModelVariable);
							modelVariables.repaint();
						}
					}
				}
				
				System.out.println("Model Variables length: " + model.modelVariables.size());
				modelVariables = new JTable(new ModelTableModel("Model Variables", model.modelVariables));
				scrollPane_1.setViewportView(modelVariables);
			}
		});
		button.setBounds(293, 255, 89, 33);
		add(button);
		
		JButton button_1 = new JButton("<");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = modelVariables.getSelectedRows();
				
				int count = 0;
				for(int i = 0; i < selectedRows.length; i++) {
					System.out.println("Removing variable from model: " + model.modelVariables.get(selectedRows[i] - count));
					model.modelVariables.remove(selectedRows[i] - count);
					count++;
				}
				modelVariables.repaint();
			}
		});
		button_1.setBounds(293, 313, 89, 33);
		add(button_1);
		
		JLabel lblVariables = new JLabel("Variables");
		lblVariables.setBounds(25, 30, 76, 14);
		add(lblVariables);
		
		JLabel lblImputationVariables = new JLabel("Imputation Variables");
		lblImputationVariables.setBounds(442, 30, 121, 14);
		add(lblImputationVariables);
		
		JButton button_2 = new JButton(">");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow[] = variableTable.getSelectedRows();
				System.out.println("Selected Row: " + selectedRow.length);
				System.out.println("identifier variables size: "+ model.identifierVariables.size());
				System.out.println("bandwidth: " + (3-model.variables.size()));
				
				if(model.identifierVariables.size() < 3 && selectedRow.length <= (3-model.identifierVariables.size())){
				
				for(int i = 0; i < selectedRow.length; i++) {
					Variable variable = model.variables.get(selectedRow[i]);
					System.out.println(variable.name);
					Variable identifierVariable = new Variable(variable.name+"(L"+(model.identifierVariables.size()+1)+")", "Undefined", variable.position);
					model.identifierVariables.add(identifierVariable);
					
				}
				int count = 0;
				for(int i = 0; i < selectedRow.length; i++) {
					System.out.println("Removing "+model.variables.get(selectedRow[i-count]).name);
					model.variables.remove(selectedRow[i-count]);
					variableTable.repaint();
					count++;
				}
				
				System.out.println("Imputation Variables length: " + model.identifierVariables.size());
				imputationVariablesTable = new JTable(new ModelTableModel("Imputation Variables", model.identifierVariables));
				scrollPane_2.setViewportView(imputationVariablesTable);
				} else {
					System.out.println("Maximum bound on identifier variables reached. Not adding more.");
				}
			}
			
		});
		button_2.setBounds(293, 62, 89, 33);
		add(button_2);
		
		JButton button_3 = new JButton("<");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = imputationVariablesTable.getSelectedRows();
				System.out.println("Size of idenitifier variale: " + model.identifierVariables.size());
				
				for(Variable var : model.variables){
					System.out.println(var.name + ":" + " " + var.position);
				}
				int count = 0;
				for(int i = 0; i < selectedRows.length; i++) {
					model.variables.add(model.identifierVariables.get(selectedRows[i]-count));
					Collections.sort(model.variables);
					model.identifierVariables.remove(selectedRows[i]-count);
					count++;
				}
				variableTable.repaint();
				imputationVariablesTable.repaint();
			}
		});
		button_3.setBounds(293, 119, 89, 33);
		add(button_3);
		
		JComboBox<String> comboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[] {"Main Effects", "Random Slopes"}));
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedItem() == "Random Slopes"){
					mode = 1;
				} else {
					mode = 0;
				}
			}
		});
		comboBox.setBounds(293, 208, 89, 20);
		add(comboBox);
		
		JLabel lblBuildTerms = new JLabel("Build Terms");
		lblBuildTerms.setBounds(309, 192, 54, 14);
		add(lblBuildTerms);

	}
	
	public int findInArrayList(ArrayList<Variable> list, String variableName) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println("Comparing " + list.get(i) + " against " + variableName);
			if(list.get(i).name.equals(variableName)){
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<String> generateCombinations(ArrayList<String> list) {
		ArrayList<String> combinations = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			for(int j = i+1; j < list.size(); j++) {
				String combinedVariable = list.get(i) + ":" + list.get(j);
				combinations.add(combinedVariable);
				System.out.println("Generated combined variable: "+combinedVariable);
			}
		}
		return combinations;
	}
	
	public void reset() {
		model.modelVariables.clear();
		for(int i = 0; i < model.identifierVariables.size(); i++) {
			String name = model.identifierVariables.get(i).name;
			String truncatedName = name.substring(0, name.lastIndexOf("("));
			model.identifierVariables.get(i).name = truncatedName;
			model.variables.add(model.identifierVariables.get(i));
		}
		model.identifierVariables.clear();
		Collections.sort(model.variables);
		modelVariables.repaint();
		imputationVariablesTable.repaint();
		variableTable.repaint();
	}
}
