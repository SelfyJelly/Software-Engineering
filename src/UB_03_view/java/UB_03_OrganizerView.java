package UB_03_view.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import UB_03_query.valueobjects.UB_03_Range;
import UB_03_query.valueobjects.UB_03_Sort;
import UB_03_query.valueobjects.UB_03_Sort.Column;
import UB_03_query.valueobjects.UB_03_Sort.Order;
import domain.model.Task;
import usecases.OrganizerUseCases;

@SuppressWarnings("serial")
public class UB_03_OrganizerView extends JFrame {
	
	private static final int HEIGHT = 600, WIDTH = 900;
	private static final int PAGE_SIZE = 30;
	private OrganizerUseCases manager;
	
	private JList<Task> taskList;
	private DefaultListModel<Task> taskListModel;
	private JTextArea taskInfo;
	private JButton newButton, editButton, deleteButton, doneButton;
	private JLabel pageLabel, statusLabel;
	private JCheckBox allTasksSelection;
	private JSpinner pageSpinner;
	private SpinnerNumberModel pageSpinnerModel;
	private JComboBox<Column> sortSelection;
	private JCheckBox ascendingSelection;
	private Font controlFont;
	
	private Column currentSortSelection;
	private boolean currentAscendingSelection;
	
	public UB_03_OrganizerView(OrganizerUseCases manager) {
		super("Organizer");
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		this.manager = manager;
									
		initializeWidgets();
		JComponent content = createWidgetLayout();		
		createWidgetInteraction();
		addMenus();

		this.setContentPane(content);
		
		this.setLocationByPlatform(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}

	public void open() {
		this.setVisible(true);
	}

	private void initializeWidgets() {
		controlFont = UIManager.getFont("Label.font").deriveFont(12f);
		taskListModel = new DefaultListModel<>();
		manager.getTasks().forEach(t -> taskListModel.addElement(t));
		taskList = new JList<>(taskListModel);
		Font standardFont = controlFont.deriveFont(14f);
		taskList.setFont(standardFont);
		taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
		// see: https://stackoverflow.com/questions/6224022/how-to-make-one-item-in-a-jlist-bold
		// see: https://www.oracle.com/technical-resources/articles/javase/oconner-customlist-gd-aurev.html
		taskList.setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        Task task = (Task) value;
		        this.setText(task.title());
		        this.setFont(getFont().deriveFont(task.done() ? Font.ITALIC : Font.BOLD));
		        this.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
		        if(!isSelected)
		        	this.setForeground(task.done() ? Color.GRAY : list.getForeground());
		        return this;
		    }			
		});
		
		allTasksSelection = new JCheckBox("Show all tasks", true);
		allTasksSelection.setFont(controlFont);
		pageLabel = new JLabel("Page:");
		pageLabel.setFont(controlFont);
		pageLabel.setEnabled(false);
		pageSpinnerModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
		pageSpinner = new JSpinner(pageSpinnerModel);
		pageSpinner.setFont(controlFont);
		pageSpinner.setEnabled(false);
		
		currentSortSelection = Column.ID;
		currentAscendingSelection = true;

		sortSelection = new JComboBox<>(Column.values());
		sortSelection.setFont(controlFont);
		sortSelection.setSelectedItem(currentSortSelection);
		ascendingSelection = new JCheckBox("Ascending", currentAscendingSelection);
		ascendingSelection.setFont(controlFont);
		
		taskInfo = new JTextArea();
		taskInfo.setLineWrap(true);
		taskInfo.setWrapStyleWord(true);
		taskInfo.setEditable(false);
		taskInfo.setBackground(UIManager.getColor("Panel.background"));
		taskInfo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		taskInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, standardFont.getSize()));
		
		newButton = new JButton("New");
		newButton.setFont(controlFont);
		editButton = new JButton("Edit");
		editButton.setFont(controlFont);
		editButton.setEnabled(false);
		deleteButton = new JButton("Delete");
		deleteButton.setFont(controlFont);
		deleteButton.setEnabled(false);

		doneButton = new JButton("Mark as done");		
		doneButton.setFont(controlFont);
		doneButton.setEnabled(false);
		
		statusLabel = new JLabel("");
		statusLabel.setFont(controlFont);
	}
	

	private JComponent createWidgetLayout() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(1, 2, 10, 0));
		
		// ListPanel - left ///////////////////////////
		JPanel listPanel = new JPanel(new BorderLayout());
		listPanel.setBorder(BorderFactory.createTitledBorder(null, "Tasks",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, controlFont));
		listPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
		
		JPanel taskControls = new JPanel();
		taskControls.add(newButton);
		taskControls.add(editButton);
		taskControls.add(deleteButton);

		JPanel pageControls = new JPanel();
		pageControls.add(allTasksSelection);
		pageControls.add(pageLabel);
		pageControls.add(pageSpinner);

		JPanel sortControls = new JPanel();
		JLabel sortLabel = new JLabel("Sort by:");
		sortLabel.setFont(controlFont);
		sortControls.add(sortLabel);
		sortControls.add(sortSelection);
		sortControls.add(ascendingSelection);

		JPanel listControls = new JPanel(new GridLayout(3, 1));
		listControls.add(taskControls);
		listControls.add(pageControls);
		listControls.add(sortControls);
		
		listPanel.add(listControls, BorderLayout.SOUTH);
		
		content.add(listPanel);
		
		// TaskPanel - right ///////////////////////////
		JPanel taskPanel = new JPanel(new BorderLayout());
		taskPanel.setBorder(BorderFactory.createTitledBorder(null, "Task details",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, controlFont));
		taskPanel.add(new JScrollPane(taskInfo), BorderLayout.CENTER);
		JPanel doneControls = new JPanel();
		doneControls.add(doneButton);
		taskPanel.add(doneControls, BorderLayout.SOUTH);
		content.add(taskPanel);
				
		panel.add(content, BorderLayout.CENTER);
		
		// create the status bar panel and shove it down the bottom of the frame
		// see: https://stackoverflow.com/questions/3035880/how-can-i-create-a-bar-in-the-bottom-of-a-java-app-like-a-status-bar
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		panel.add(statusPanel, BorderLayout.SOUTH);
		
		return panel;
	}

	@SuppressWarnings("unused")
	private void createWidgetInteraction() {
		taskList.addListSelectionListener(e -> { if(!e.getValueIsAdjusting()) updateTaskInfo(); });
		
		allTasksSelection.addItemListener(e -> updateDisplayedTasks());
		
		pageSpinner.addChangeListener(e -> updateDisplayedPage());
		
		sortSelection.addActionListener(e -> updateDisplayedTasks());
		ascendingSelection.addActionListener(e -> updateDisplayedTasks());
		
		newButton.addActionListener(e -> newDialog());
		editButton.addActionListener(e -> editDialog());
		deleteButton.addActionListener(e -> deleteDialog());
		doneButton.addActionListener(e -> setTaskToDone());
	}
	
	@SuppressWarnings("unused")
	private void addMenus() {
	    JPopupMenu menu = new JPopupMenu();
	    addItem(menu, "New", e -> newDialog());
	    addItem(menu, "Edit", e -> editDialog());
	    addItem(menu, "Delete", e -> deleteDialog());
	
		taskList.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent e) { check(e); }
	        public void mouseReleased(MouseEvent e) { check(e); }
	        
	    	public void check(MouseEvent e) {
	    	    if (e.isPopupTrigger()) { //if the event shows the menu
	    	    	taskList.setSelectedIndex(taskList.locationToIndex(e.getPoint())); //select the item
	    	        menu.show(taskList, e.getX(), e.getY()); //and show the menu
	    	    }
	    	}
	    });
	}

	private void addItem(JPopupMenu menu, String label, ActionListener l) {
	    JMenuItem item = new JMenuItem(label);
	    item.addActionListener(l);
	    menu.add(item);
	}

	private void updateTaskInfo() {
		Task task = taskList.getSelectedValue();
		if(task == null) {
			taskInfo.setText("");
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			doneButton.setEnabled(false);
			setStatus("nothing selected");
			return;
		}
	
		String content = String.format("""
				Id:  	%d
				Title:	%s
				Entry:	%s
				Estim.:	%d h
				Prio.:	%s
				Due:	%s
				Done:	%b
				
				%s
				""", task.id(), task.title(), task.entry(), task.estimate(), task.priority(), task.due(), task.done(), task.description());
	
			taskInfo.setText(content);
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
			doneButton.setEnabled(!task.done());
			setStatus(task.title() + " selected");
	}

	private void updateDisplayedTasks() {
		int selection = taskList.getSelectedIndex();
		if(currentSortSelection != sortSelection.getSelectedItem() || currentAscendingSelection != ascendingSelection.isSelected()) {
			currentSortSelection = (Column) sortSelection.getSelectedItem();
			currentAscendingSelection = ascendingSelection.isSelected();
			selection = -1;
		}
		Optional<UB_03_Range> range = Optional.empty();
		Optional<UB_03_Sort> sort = UB_03_Sort.of((Column)(sortSelection.getSelectedItem()), (ascendingSelection.isSelected() ? Order.ASCENDING : Order.DESCENDING));
		
		if(allTasksSelection.isSelected()) {
			if(selection != -1)
				selection = (pageSpinnerModel.getNumber().intValue() - 1) * PAGE_SIZE + selection;
			pageSpinnerModel.setValue(1);
			pageLabel.setEnabled(false);
			pageSpinner.setEnabled(false);
		} else {
			range = UB_03_Range.of(selection / PAGE_SIZE * PAGE_SIZE, PAGE_SIZE);
			int count = manager.getNumberOfTasks();
			int maxPages = count / PAGE_SIZE + (count % PAGE_SIZE == 0 ? 0 : 1);
			pageSpinnerModel.setMaximum(maxPages);
			pageSpinnerModel.setValue(1);
			if(selection != -1) {
				pageSpinnerModel.setValue(selection / PAGE_SIZE + 1);
				selection = selection % PAGE_SIZE;
			}
			pageLabel.setEnabled(true);
			pageSpinner.setEnabled(true);
		}
		
		// create a new model - an update of the existing model delays swing
		taskListModel = new DefaultListModel<>();
		manager.getTasks(sort, range).forEach(t -> taskListModel.addElement(t));
		taskList.setModel(taskListModel);

		if(selection != -1) {
			taskList.setSelectedIndex(selection);
			taskList.ensureIndexIsVisible(selection);
		}
	}

	private void updateDisplayedPage() {
		int selection = taskList.getSelectedIndex();
		Optional<UB_03_Range> range = UB_03_Range.of((pageSpinnerModel.getNumber().intValue() - 1) * PAGE_SIZE, PAGE_SIZE);
		Optional<UB_03_Sort> sort = UB_03_Sort.of((Column)(sortSelection.getSelectedItem()), (ascendingSelection.isSelected() ? Order.ASCENDING : Order.DESCENDING));
				
		// create a new model - an update of the existing model delays swing
		taskListModel = new DefaultListModel<>();
		manager.getTasks(sort, range).forEach(t -> taskListModel.addElement(t));
		taskList.setModel(taskListModel);

		if(selection != -1) {
			taskList.setSelectedIndex(selection);
			taskList.ensureIndexIsVisible(selection);
		}
	}

	private void newDialog() {
		taskList.clearSelection();
		Task newTask = new UB_03_OrganizerDialog().newTask(this);
		if(newTask == null)
			return;
		
		Task task = manager.enter(newTask);
		
		if(task != null)
			this.updateDisplayedTasks();
		else
			setStatus("enter failed");
	}

	private void editDialog() {
		Task task = taskList.getSelectedValue();
		if(task == null)
			return;
		
		Object oldValue = currentSortSelection.from(task);

		Task editedTask = new UB_03_OrganizerDialog().editTask(this, task);
		if(editedTask == null)
			return;
		
		Object newValue = currentSortSelection.from(editedTask);
		
		if(oldValue != null && !oldValue.equals(newValue))
			taskList.clearSelection();
		
		boolean ok = manager.update(editedTask);
		
		if(ok)
			this.updateDisplayedTasks();
		else
			setStatus("update failed");
	}

	private void deleteDialog() {
		Task task = taskList.getSelectedValue();
		if(task == null)
			return;
		
        int result = JOptionPane.showConfirmDialog(this, "Do you want to delete Task " + task.title(), "Delete Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(result != JOptionPane.YES_OPTION)
        	return;
        
        boolean ok = manager.delete(task.id());
        
		if(ok)
			this.updateDisplayedTasks();
		else
			setStatus("delete failed");
	}

	private void setTaskToDone() {
		Task task = taskList.getSelectedValue();
		if(task == null || task.done())
			return;
		
		task = new Task(task.id(), task.title(), task.entry(), task.estimate(), task.priority(), task.due(), true, task.description());
		boolean ok = manager.update(task);
		if(!ok) {
			setStatus("update failed");
			return;
		}
		
		int index = taskList.getSelectedIndex();
		taskListModel.set(index, task);
		this.updateTaskInfo();
	}

	public void setStatus(String text) {
		statusLabel.setText(text);
	}
	
	public void clearStatus() {
		statusLabel.setText("");
	}
}
