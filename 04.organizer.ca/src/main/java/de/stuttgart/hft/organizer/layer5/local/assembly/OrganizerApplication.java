package de.stuttgart.hft.organizer.layer5.local.assembly;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.stuttgart.hft.organizer.layer1.domain.model.Task;
import de.stuttgart.hft.organizer.layer2.domain.functions.TaskFunctions;
import de.stuttgart.hft.organizer.layer2.ports.in.TaskService;
import de.stuttgart.hft.organizer.layer3.ports.in.OrganizerManager;
import de.stuttgart.hft.organizer.layer3.ports.out.OrganizerRepository;
import de.stuttgart.hft.organizer.layer3.usecases.OrganizerUseCases;
import de.stuttgart.hft.organizer.layer4.db.access.ORMLiteOrganizerRepository;
import de.stuttgart.hft.organizer.layer4.file.access.TaskReader;
import de.stuttgart.hft.organizer.layer4.view.java.UB_03_OrganizerView;

public class OrganizerApplication {

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        setSystemLookAndFeel();

        OrganizerRepository repository =
            new ORMLiteOrganizerRepository("jdbc:sqlite:organizer.db");

        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> repository.close())
        );

        if (repository.countTasks() == 0) {
            System.out.println("Creating Tasks ...");

            List<Task> tasks =
                TaskReader.fromCsv("/samples/SampleTasks.csv");

            for (Task task : tasks) {
                repository.create(task);
            }
        }

        TaskService service = new TaskFunctions();

        OrganizerManager manager =
            new OrganizerUseCases(repository, service);

        UB_03_OrganizerView view =
            new UB_03_OrganizerView(manager);

        EventQueue.invokeLater(() -> view.open());
    }
}
