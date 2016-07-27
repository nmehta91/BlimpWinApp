import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SpecifyModelPanel extends JPanel {
	private JTable variableTable;
	private JTable modelVariables;
	private JTable imputationVariablesTable;
	private SyntaxModel model;
	private int mode;
	private Frame parentFrame;
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
		    

	}
	public SpecifyModelPanel() {
		setLayout(null);
		
		model = SyntaxModel.getInstance();
		mode = 0; // 0 = Main Effects, 1 = Random Slopes
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 11, 209, 455);
		add(scrollPane);
		
		variableTable = new JTable(new VariablesTableModel("Variables", model.variables));
		variableTable.setOpaque(true);
		variableTable.setFillsViewportHeight(true);
		variableTable.setBackground(Color.WHITE);
		
		scrollPane.setViewportView(variableTable);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(442, 192, 222, 274);
		add(scrollPane_1);
		
		DefaultTableModel tblmodel = new DefaultTableModel(0, 1);
		tblmodel.setColumnIdentifiers(new String[] {"Imputation Model Variables"});
		modelVariables = new JTable(tblmodel);
		modelVariables.setOpaque(true);
		modelVariables.setFillsViewportHeight(true);
		modelVariables.setBackground(Color.WHITE);
		scrollPane_1.setViewportView(modelVariables);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(442, 11, 222, 129);
		add(scrollPane_2);
		
		DefaultTableModel tblmodel1 = new DefaultTableModel(0, 1);
		tblmodel1.setColumnIdentifiers(new String[] {"Cluster-Level Identifier Variables"});
		imputationVariablesTable = new JTable(tblmodel1);
		imputationVariablesTable.setOpaque(true);
		imputationVariablesTable.setFillsViewportHeight(true);
		imputationVariablesTable.setBackground(Color.WHITE);
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
						JOptionPane.showMessageDialog(getParentFrame(),
							    "Random Slopes requires more than one variable to be selected.",
							    "Error!",
							    JOptionPane.ERROR_MESSAGE);
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
							String variableName = combinations.get(i) + " (Random Slopes)";
							Variable newModelVariable = new Variable(variableName, "Undefined", -1);
							model.modelVariables.add(newModelVariable);
							modelVariables.repaint();
						}
					}
				}
				
				System.out.println("Model Variables length: " + model.modelVariables.size());
				modelVariables = new JTable(new ModelTableModel("Model Variables", model.modelVariables));
				modelVariables.setOpaque(true);
				modelVariables.setFillsViewportHeight(true);
				modelVariables.setBackground(Color.WHITE);
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
		
		JButton addToImputation = new JButton(">");
		addToImputation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow[] = variableTable.getSelectedRows();
				System.out.println("Selected Row: " + selectedRow.length);
				System.out.println("identifier variables size: "+ model.identifierVariables.size());
				System.out.println("bandwidth: " + (3-model.variables.size()));
				
				if(model.identifierVariables.size() < 3 && selectedRow.length <= (3-model.identifierVariables.size())){
				
				for(int i = 0; i < selectedRow.length; i++) {
					Variable variable = model.variables.get(selectedRow[i]);
					System.out.println(variable.name);
					Variable identifierVariable = new Variable(variable.name+" (L"+(model.identifierVariables.size()+1)+")", "Undefined", variable.position);
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
				imputationVariablesTable.setOpaque(true);
				imputationVariablesTable.setFillsViewportHeight(true);
				imputationVariablesTable.setBackground(Color.WHITE);
				scrollPane_2.setViewportView(imputationVariablesTable);
				} else {
					JOptionPane.showMessageDialog(getParentFrame(),
						    "Maximum of three identifier variables allowed!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		addToImputation.setBounds(293, 62, 89, 33);
		add(addToImputation);
		
		JButton removeFromImputation = new JButton("<");
		removeFromImputation.addActionListener(new ActionListener() {
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
		removeFromImputation.setBounds(293, 119, 89, 33);
		add(removeFromImputation);
		
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
		comboBox.setBounds(281, 207, 116, 20);
		add(comboBox);
		
		JLabel lblBuildTerms = new JLabel("Build Terms");
		lblBuildTerms.setBounds(308, 184, 89, 23);
		add(lblBuildTerms);
		
		JCheckBox chckbxSingleImputation = new JCheckBox("Single Imputation");
		chckbxSingleImputation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxSingleImputation.isSelected()) {
					int[] indexes = new int[model.modelVariables.size()];
					int noOfVarsToRemove = 0;
					for(int i = 0; i < model.modelVariables.size(); i++) {
						Variable var = model.modelVariables.get(i);
						if(var.name.lastIndexOf(":") != -1){
							indexes[noOfVarsToRemove++] = i;
						}		
					}
					
					for(int i = model.modelVariables.size()-1; i >= 0; i--) {
						for(int j = 0; j < noOfVarsToRemove; j++){
							if(i == indexes[j])
								model.modelVariables.remove(i);
						}
					}
					
					for(int i = 0; i < model.identifierVariables.size(); i++) {
						Variable var = model.identifierVariables.get(i);
						String truncatedName = var.name.substring(0, var.name.lastIndexOf("("));
						var.name = truncatedName;
						model.variables.add(var);
					}
					model.identifierVariables.clear();
					imputationVariablesTable.repaint();
					modelVariables.repaint();
					
					Collections.sort(model.variables);
					variableTable.repaint();
					
					addToImputation.setEnabled(false);
					removeFromImputation.setEnabled(false);
					comboBox.removeItemAt(1);
				} else {
					addToImputation.setEnabled(true);
					removeFromImputation.setEnabled(true);
					comboBox.addItem("Random Slopes");
				}
			}
		});
		chckbxSingleImputation.setBounds(442, 147, 131, 23);
		add(chckbxSingleImputation);
		
		
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
	
	public Frame getParentFrame() {
		Window parentWindow = SwingUtilities.windowForComponent(this);
		
		Frame parentFrame = null;
		if (parentWindow instanceof Frame) {
		    parentFrame = (Frame)parentWindow;
		}
		
		return parentFrame;
	}
	
	public boolean isComplete() {
		boolean isComplete = true;
		if(model.modelVariables.size() == 0) {
			isComplete = false;
		}
		return isComplete;
	}
}
