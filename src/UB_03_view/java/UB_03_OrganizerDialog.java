package UB_03_view.java;

import static UB_03_query.valueobjects.UB_03_Sort.Column.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import UB_03_query.valueobjects.UB_03_Sort.Column;
import domain.model.Priority;
import domain.model.Task;


public class UB_03_OrganizerDialog {
	
	@FunctionalInterface
	public static interface Procedure {
		//see https://stackoverflow.com/questions/23868733/java-8-functional-interface-with-no-arguments-and-no-return-value
	    void run();

	    default Procedure andThen(Procedure after){
	        return () -> {
	            this.run();
	            after.run();
	        };
	    }

	    default Procedure compose(Procedure before){
	        return () -> {
	            before.run();
	            this.run();
	        };
	    }
	}
	
	@SuppressWarnings("unused")
	private static class DialogBuilder {
		
		private Component parent;
		private String title;
		
		private Map<Column, ? super JComponent> widgets = new HashMap<>();
		private Map<Column, Object> entries = new HashMap<>();
		private JPanel panel = new JPanel(new GridBagLayout());
		private Font controlFont = UIManager.getFont("Label.font").deriveFont(12f);
		private Font contentFont = controlFont.deriveFont(12f);
		private int row = 0;
		
		final JButton okay = newButton("   OK   ");
		final JButton cancel = newButton("Cancel");

		private DialogBuilder(Component parent, String title) {
			this.parent = parent;
			this.title = title;
		}

		static DialogBuilder dialog(Component parent, String title) {
			return new DialogBuilder(parent, title);
		}
		
	    private JOptionPane getOptionPane(JComponent parent) {
	        if (parent instanceof JOptionPane)
	        	return (JOptionPane) parent;
	        else
	        	return getOptionPane((JComponent)parent.getParent());
	    }

	    private JOptionPane getOptionPane(ActionEvent event) {
	    	return this.getOptionPane((JComponent)event.getSource());
		}
	    
	    private JButton newButton(String title) {
	    	final JButton button = new JButton(title);
	    	button.setFont(controlFont);
	    	button.addActionListener(e -> getOptionPane(e).setValue(button));
	    	return button;
	    }

	    private JLabel newLabel(String text) {
	    	JLabel label = new JLabel(text);
	    	label.setFont(controlFont);
	    	return label;
	    }

	    private void setComponentFont(Component component, Font font) {
	    	component.setFont(font);
	    	if(component instanceof Container) {
	    		for(Component child : ((Container)component).getComponents())
	    			setComponentFont(child, font);
	    	}
	    }

	    private GridBagConstraints constraints(int gridx, int gridy, int gridwidth, int ipady) {
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(1, 1, 1, 1);
			constraints.fill = GridBagConstraints.BOTH;
			
			constraints.gridx = gridx;
			constraints.gridy = gridy;
			constraints.gridwidth = gridwidth;
			constraints.ipady = ipady;
			
			return constraints;
		}
		
		private GridBagConstraints constraints(int gridx, int gridy, int gridwidth) {
			return constraints(gridx, gridy, gridwidth, 0);
		}

		private GridBagConstraints constraints(int gridx, int gridy) {
			return constraints(gridx, gridy, 1, 0);
		}
		
		static class OnChange implements DocumentListener {
			
			private Procedure action;
			
			static OnChange run(Procedure action) {
				OnChange oc = new OnChange();
				oc.action = action;
				return oc;
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				action.run();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				action.run();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				action.run();
			}
		}
		
		
		static class Validator implements DocumentListener {
			
			private JTextComponent component;
			private BooleanSupplier validate;
			
			static Validator run(JTextComponent component, BooleanSupplier validate) {
				Validator vd = new Validator();
				vd.component = component;
				vd.validate = validate;
				
				component.setInputVerifier(new InputVerifier() {
					
					@Override
					public boolean verify(JComponent input) {
						return validate.getAsBoolean();
					}
				});
				
				vd.changeState();
				
				return vd;
			}

			private void changeState() {
				component.setBackground(validate.getAsBoolean() ? Color.WHITE : Color.RED);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changeState();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changeState();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changeState();
			}
		}
		
		@SuppressWarnings("serial")
		class DateLabelFormatter extends AbstractFormatter {
			// see: https://www.codejava.net/java-se/swing/how-to-use-jdatepicker-to-display-calendar-component
			 
		    private String datePattern = "dd.MM.yyyy";
		    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
		     
		    @Override
		    public Object stringToValue(String text) throws ParseException {
		        return dateFormatter.parseObject(text);
		    }
		 
		    @Override
		    public String valueToString(Object value) throws ParseException {
		        if (value != null) {
		            Calendar cal = (Calendar) value;
		            return dateFormatter.format(cal.getTime());
		        } else
		        	return "";
		    }		 
		}
		

		private void setToolTip(JComponent component, String toolTip) {
			if(toolTip == null  ||toolTip.isBlank())
				return;
			component.setToolTipText("<html>" + toolTip.replace("\n", "<br>") + "</html>");
		}

		DialogBuilder addField(final Column element) {
			return addField(element, "");
		}
		
		DialogBuilder addField(final Column element, String preset) {
			final JTextField field = new JTextField(30);
			field.setFont(contentFont);
			widgets.put(element, field);

			field.setText(preset);
			entries.put(element, preset);
			
			panel.add(newLabel(element.label()), constraints(0, row));
			panel.add(field, constraints(1, row, 3));
			row++;
			
			field.getDocument().addDocumentListener(
					OnChange.run(() -> entries.put(element, field.getText())));

			field.getDocument().addDocumentListener(
					Validator.run(field, () -> !field.getText().isBlank()));

			return this;
		}
		
		DialogBuilder addIntSpinner(final Column element) {
			return addIntSpinner(element, 0, 0);
		}
		
		DialogBuilder addIntSpinner(final Column element, int lowerBound, int preset) {
			SpinnerModel model = new SpinnerNumberModel(preset, lowerBound, Integer.MAX_VALUE, 1); //default value,lower bound,upper bound,increment by sm = new SpinnerNumberModel(0, 0, 9, 1); //default value,lower bound,upper bound,increment by
			final JSpinner spinner = new JSpinner(model);
			spinner.setFont(contentFont);
			widgets.put(element, spinner);

			entries.put(element, preset);
			
			panel.add(newLabel(element.label()), constraints(0, row));
			panel.add(spinner, constraints(1, row, 3));
			row++;
			spinner.addChangeListener(e -> entries.put(element, model.getValue()));
			
			return this;
		}
		
		DialogBuilder addArea(final Column element) {
			return addArea(element, "");
		}

		DialogBuilder addArea(final Column element, String preset) {
			final JTextArea area = new JTextArea(3, 15);
			area.setFont(contentFont);
			widgets.put(element, area);

			area.setLineWrap(true);
			area.setWrapStyleWord(true);
			area.setText(preset);
			final JScrollPane wrappedArea = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
																	JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			entries.put(element, preset);
		
			panel.add(newLabel(element.label()), constraints(0, row));
			panel.add(wrappedArea, constraints(1, row, 3, 40));
			row++;
			
			area.getDocument().addDocumentListener(
					OnChange.run(() -> entries.put(element, area.getText())));
		
			area.getDocument().addDocumentListener(
					Validator.run(area, () -> !area.getText().isBlank()));

			return this;
		}

		DialogBuilder addCheck(final Column element) {
			return addCheck(element, false);
		}

			
		DialogBuilder addCheck(final Column element, final boolean selected) {
			final JCheckBox check = new JCheckBox();
			widgets.put(element, check);

			check.setSelected(selected);
			entries.put(element, selected);
			
			panel.add(newLabel(element.label()), constraints(0, row));
			panel.add(check, constraints(1, row, 2));
			row++;
			
			check.addActionListener(e -> entries.put(element, check.isSelected()));
			
			return this;
		}
		
		@SuppressWarnings("unchecked")
		<E>DialogBuilder addComboBox(final Column element, final List<E> list, E preset, E prototype) {
			return addComboBox(element, (E[]) list.toArray(), preset, prototype);
		}

		<E>DialogBuilder addComboBox(final Column element, final E[] elements, E preset, E prototype) {
			final JComboBox<E> comboBox = new JComboBox<>(elements);
			comboBox.setFont(contentFont);
			widgets.put(element, comboBox);
			
			comboBox.setPrototypeDisplayValue(prototype);
			comboBox.setSelectedItem(preset);
			entries.put(element, preset);
			
			panel.add(newLabel(element.label()), constraints(0, row, 2));			
			panel.add(comboBox, constraints(2, row));
			row++;
			
			comboBox.addActionListener(e -> entries.put(element, comboBox.getSelectedItem()));

			return this;
		}
		
		DialogBuilder addDatePicker(final Column element) {
			return addDatePicker(element, LocalDate.now());
		}

		
		DialogBuilder addDatePicker(final Column element, LocalDate date) {
			final UtilDateModel model = new UtilDateModel();			
			final JDatePicker datePicker = new JDatePicker(model);
			setComponentFont(datePicker, contentFont);
			setComponentFont((Component)datePicker.getJDateInstantPanel(), contentFont);
			 
			widgets.put(element, datePicker);

			if(date != null) {
				model.setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
				model.setSelected(true);
			}
			entries.put(element, date);
			
			panel.add(newLabel(element.label()), constraints(0, row));
			panel.add(datePicker, constraints(1, row, 2));
			row++;
			
			datePicker.addActionListener(e -> entries.put(element, (model.isSelected() ? LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay()) : null)));

			return this;
		}
		
		int open() {
			Object[] options = {okay, cancel};
			int result = JOptionPane.showOptionDialog(parent, 
					panel, // "Logic goes here", 
					title,
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.PLAIN_MESSAGE, 
					null, // do not use a custom Icon
					options, // the titles of buttons
					options[1]); // default button title
			return result;			
		}
		
		<T> T getValue(Column element, Class<T> cls){
			return cls.cast(entries.get(element));
		}
		
		<T> T getWidget(Column element, Class<T> cls){
			return cls.cast(widgets.get(element));
		}
		
		JTextField getField(Column element){
			return getWidget(element, JTextField.class);
		}
		
	}
	
	public Task newTask(Component parent) {
		
		DialogBuilder dialog = DialogBuilder.dialog(parent, "Add new Task")
				.addField(TITLE)
				.addDatePicker(ENTRY_DATE)
				.addIntSpinner(ESTIMATE)
				.addComboBox(PRIORITY, Priority.values(), Priority.NONE, Priority.MEDIUM)
				.addDatePicker(DUE_DATE, null)
				.addCheck(DONE)
				.addArea(DESCRIPTION);

		int result = dialog.open();
		
		if(result != JOptionPane.OK_OPTION)
			return null;
		
		String title = dialog.getValue(TITLE, String.class);
		LocalDate entryDate = dialog.getValue(ENTRY_DATE, LocalDate.class);
		int estimate = dialog.getValue(ESTIMATE, Integer.class);
		Priority priority = dialog.getValue(PRIORITY, Priority.class);
		LocalDate dueDate = dialog.getValue(DUE_DATE, LocalDate.class);
		boolean done = dialog.getValue(DONE, Boolean.class);
		String description = dialog.getValue(DESCRIPTION, String.class);
		
		if(title.isBlank() || description.isBlank() || entryDate == null)
			return null;
		
		Task task = Task.of(title, entryDate, estimate, priority, dueDate, done, description);
		return task;
	}

	public Task editTask(Component parent, Task task) {
		
		DialogBuilder dialog = DialogBuilder.dialog(parent, "Edit Task " + task.id())
				.addField(TITLE, task.title())
				.addDatePicker(ENTRY_DATE, task.entry())
				.addIntSpinner(ESTIMATE, 0, task.estimate())
				.addComboBox(PRIORITY, Priority.values(), task.priority(), Priority.MEDIUM)
				.addDatePicker(DUE_DATE, task.due())
				.addCheck(DONE, task.done())
				.addArea(DESCRIPTION, task.description());

		int result = dialog.open();
		
		if(result != JOptionPane.OK_OPTION)
			return null;
		
		int id = task.id();
		String title = dialog.getValue(TITLE, String.class);
		LocalDate entryDate = dialog.getValue(ENTRY_DATE, LocalDate.class);
		int estimate = dialog.getValue(ESTIMATE, Integer.class);
		Priority priority = dialog.getValue(PRIORITY, Priority.class);
		LocalDate dueDate = dialog.getValue(DUE_DATE, LocalDate.class);
		boolean done = dialog.getValue(DONE, Boolean.class);
		String description = dialog.getValue(DESCRIPTION, String.class);
		
		if(title.isBlank() || description.isBlank() || entryDate == null)
			return null;
		
		task = new Task(id, title, entryDate, estimate, priority, dueDate, done, description);
		return task;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			System.out.println(new UB_03_OrganizerDialog().newTask(null));
		});

	}
}
