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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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
		
		model = SyntaxModel.getInstance();
		mode = 0; // 0 = Main Effects, 1 = Random Slopes
		
		JScrollPane scrollPane = new JScrollPane();
		
		variableTable = new JTable(new VariablesTableModel("Variables", model.variables));
		variableTable.setOpaque(true);
		variableTable.setFillsViewportHeight(true);
		variableTable.setBackground(Color.WHITE);
		
		scrollPane.setViewportView(variableTable);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		DefaultTableModel tblmodel = new DefaultTableModel(0, 1);
		tblmodel.setColumnIdentifiers(new String[] {"Imputation Model Variables"});
		modelVariables = new JTable(tblmodel);
		modelVariables.setOpaque(true);
		modelVariables.setFillsViewportHeight(true);
		modelVariables.setBackground(Color.WHITE);
		scrollPane_1.setViewportView(modelVariables);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
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
							// if duplicate variable found exists already, throw error
							JOptionPane.showMessageDialog(getParentFrame(),
									"Duplicate variable " + variable.name + " already exists. \nPlease remove any duplicate variables from the model before adding.",
									"Error!",
									JOptionPane.ERROR_MESSAGE);
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
				modelVariables = new JTable(new ModelTableModel("Imputation Model Variables", model.modelVariables));
				modelVariables.setOpaque(true);
				modelVariables.setFillsViewportHeight(true);
				modelVariables.setBackground(Color.WHITE);
				scrollPane_1.setViewportView(modelVariables);
			}
		});
		
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
		
		JButton addToImputation = new JButton(">");
		addToImputation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow[] = variableTable.getSelectedRows();
				System.out.println("Selected Row: " + selectedRow.length);
				System.out.println("identifier variables size: "+ model.identifierVariables.size());
				System.out.println("bandwidth: " + (3-model.variables.size()));
				
				if(model.identifierVariables.size() < 3 && selectedRow.length <= (3-model.identifierVariables.size())){
				
				// Array specifies which indexes to remove based on comparison with model variables
				Boolean[] indexesToRemove = new Boolean[model.variables.size()];
				for(int i = 0; i < model.variables.size(); i++) {
					indexesToRemove[i] = false;
				}
				
				for(int i = 0; i < selectedRow.length; i++) {
					Variable variable = model.variables.get(selectedRow[i]);
					int index = findInArrayList(model.modelVariables, variable.name);
					if(index != -1) {
						JOptionPane.showMessageDialog(getParentFrame(),
								"Duplicate variable " + variable.name + " already exists. \nPlease remove any duplicate variables from the model before adding.",
								"Error!",
								JOptionPane.ERROR_MESSAGE);
						continue;
					}
					indexesToRemove[i] = true;
					System.out.println(variable.name);
					Variable identifierVariable = new Variable(variable.name+" (L"+(model.identifierVariables.size()+1)+")", variable.type, variable.position);
					model.identifierVariables.add(identifierVariable);
					
				}
				int count = 0;
				for(int i = 0; i < selectedRow.length; i++) {
					if(indexesToRemove[i]) {
						System.out.println("Removing "+model.variables.get(selectedRow[i-count]).name);
						model.variables.remove(selectedRow[i-count]);
						variableTable.repaint();
						count++;
					}
				}
				
				System.out.println("Imputation Variables length: " + model.identifierVariables.size());
				imputationVariablesTable = new JTable(new ModelTableModel("Cluster-Level Identifier Variables", model.identifierVariables));
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
					Variable var = model.identifierVariables.get(selectedRows[i]-count);
					if(var.name.lastIndexOf("(") != -1) {
						var.name = var.name.substring(0, var.name.lastIndexOf("(")).trim();
					}
					model.variables.add(var);
					Collections.sort(model.variables);
					model.identifierVariables.remove(selectedRows[i]-count);
					count++;
				}
				variableTable.repaint();
				imputationVariablesTable.repaint();
			}
		});
		
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
		
		JLabel lblBuildTerms = new JLabel("Build Terms");
		
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
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
					.addGap(47)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(addToImputation, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(removeFromImputation, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(27)
							.addComponent(lblBuildTerms, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)))
					.addGap(45)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
						.addComponent(chckbxSingleImputation, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
					.addGap(100))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
					.addGap(39))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(62)
					.addComponent(addToImputation, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(24)
					.addComponent(removeFromImputation, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(32)
					.addComponent(lblBuildTerms, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(28)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(25)
					.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
					.addGap(7)
					.addComponent(chckbxSingleImputation)
					.addGap(23)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
					.addGap(39))
		);
		setLayout(groupLayout);
		
		
	}
	
	public int findInArrayList(ArrayList<Variable> list, String variableName) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println("Comparing " + list.get(i) + " against " + variableName);
			String [] separatedVariables = list.get(i).name.split(":");
			
			if(separatedVariables.length > 1){
				// Random Variable
				String lastVariableToTrim = separatedVariables[separatedVariables.length-1];
				separatedVariables[separatedVariables.length-1] = lastVariableToTrim.substring(0, lastVariableToTrim.lastIndexOf("(")).trim();
				System.out.println("Last varaibel : " + separatedVariables[separatedVariables.length-1]);
				for(int j = 0; j < separatedVariables.length; j++) {
					System.out.println("Comparing "+ separatedVariables[j] + "with " +variableName);
					if(separatedVariables[j].equals(variableName))
						return i;
				}
			} else {
				// Main effects variable
				if(list.get(i).name.equals(variableName)){
					return i;
				}
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
