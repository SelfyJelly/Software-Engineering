package assembly;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import UB_03_view.java.UB_03_OrganizerView;
import db.access.ORMLiteOrganizerRepository;
import domain.functions.TaskFunctions;
import domain.model.Task;
import file.access.TaskReader;
import usecases.OrganizerUseCases;

public class UB_03_ViewApplication {
    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setSystemLookAndFeel();
        ORMLiteOrganizerRepository repository = new ORMLiteOrganizerRepository("jdbc:sqlite:organizer.db");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> repository.close()));
        if (repository.countTasks() == 0) {
            System.out.println("Creating Tasks ...");
            List<Task> tasks = TaskReader.fromCsv("/samples/SampleTasks.csv");
            for (Task task : tasks)
                repository.create(task);
        }
        TaskFunctions service = new TaskFunctions();
        OrganizerUseCases manager = new OrganizerUseCases(repository, service);
        UB_03_OrganizerView view = new UB_03_OrganizerView(manager);
        EventQueue.invokeLater(() -> view.open());
    }
}
