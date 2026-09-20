import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin Dashboard Panel: Student Management Table, Live Search, Add/Edit/Delete actions.
 */
public class AdminDashboardPanel extends JPanel {
    private final MainFrame mainFrame;
    private final StudentDAO studentDAO;
    private final UserDAO userDAO;
    private User currentUser;

    private StyledTextField tfSearch;
    private StyledTable tblStudents;
    private DefaultTableModel tableModel;
    private ToastNotification toast;

    public AdminDashboardPanel(MainFrame mainFrame, StudentDAO studentDAO, UserDAO userDAO) {
        this.mainFrame = mainFrame;
        this.studentDAO = studentDAO;
        this.userDAO = userDAO;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_APP);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // 1. Top Section (Title + Toast)
        JPanel topBox = new JPanel(new BorderLayout(0, 12));
        topBox.setOpaque(false);

        JLabel lblTitle = new JLabel("Student Directory Management");
        lblTitle.setFont(Theme.FONT_PAGE_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);

        toast = new ToastNotification();
        topBox.add(lblTitle, BorderLayout.NORTH);
        topBox.add(toast, BorderLayout.SOUTH);
        add(topBox, BorderLayout.NORTH);

        // 2. Main Content Card (Toolbar + Table + Actions)
        CardPanel card = new CardPanel(12, new Insets(20, 20, 20, 20));
        card.setLayout(new BorderLayout(0, 16));

        // Toolbar (Search + Buttons)
        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.setOpaque(false);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(Theme.FONT_BODY_BOLD);
        lblSearch.setForeground(Theme.TEXT_SECONDARY);

        tfSearch = new StyledTextField(22);
        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filterLive(); }
            @Override public void removeUpdate(DocumentEvent e) { filterLive(); }
            @Override public void changedUpdate(DocumentEvent e) { filterLive(); }
        });

        StyledButton btnRefresh = StyledButton.createSecondary("Refresh");
        btnRefresh.addActionListener(e -> {
            tfSearch.setText("");
            loadStudentsAsync();
        });

        searchBox.add(lblSearch);
        searchBox.add(tfSearch);
        searchBox.add(btnRefresh);
        toolbar.add(searchBox, BorderLayout.WEST);

        StyledButton btnAdd = StyledButton.createPrimary("+ Add Student");
        btnAdd.addActionListener(e -> openAddDialog());
        toolbar.add(btnAdd, BorderLayout.EAST);

        card.add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Student ID", "Full Name", "Department", "Year", "CGPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblStudents = new StyledTable(tableModel);
        card.add(tblStudents.createScrollPane(), BorderLayout.CENTER);

        // Bottom Action Bar (Edit, Delete)
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomBar.setOpaque(false);

        StyledButton btnEdit = StyledButton.createSecondary("Edit Student");
        btnEdit.addActionListener(e -> openEditDialog());

        StyledButton btnDelete = StyledButton.createDanger("Delete Student");
        btnDelete.addActionListener(e -> handleDeleteStudent());

        bottomBar.add(btnEdit);
        bottomBar.add(btnDelete);
        card.add(bottomBar, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    public void setUser(User user) {
        this.currentUser = user;
        loadStudentsAsync();
    }

    private void loadStudentsAsync() {
        toast.hideToast();
        SwingWorker<List<Student>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Student> doInBackground() throws Exception {
                return studentDAO.getAllStudents();
            }

            @Override
            protected void done() {
                try {
                    List<Student> list = get();
                    populateTable(list);
                } catch (Exception e) {
                    toast.showError("Failed to load students: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void filterLive() {
        String query = tfSearch.getText().trim();
        if (query.isEmpty()) {
            loadStudentsAsync();
            return;
        }

        SwingWorker<List<Student>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Student> doInBackground() throws Exception {
                return studentDAO.searchStudents(query);
            }

            @Override
            protected void done() {
                try {
                    List<Student> list = get();
                    populateTable(list);
                } catch (Exception e) {
                    toast.showError("Search failed: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void populateTable(List<Student> list) {
        tableModel.setRowCount(0);
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                s.getStudentId(),
                s.getName(),
                s.getDepartment(),
                s.getYear(),
                String.format("%.2f", s.getCgpa())
            });
        }
    }

    private void openAddDialog() {
        StudentFormDialog dialog = new StudentFormDialog(mainFrame, null, studentDAO, userDAO);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            toast.showSuccess("Student added successfully.");
            loadStudentsAsync();
        }
    }

    private void openEditDialog() {
        int row = tblStudents.getSelectedRow();
        if (row < 0) {
            toast.showInfo("Please select a student from the table to edit.");
            return;
        }

        String studentId = (String) tableModel.getValueAt(row, 0);
        SwingWorker<Student, Void> worker = new SwingWorker<>() {
            @Override
            protected Student doInBackground() throws Exception {
                return studentDAO.getStudentById(studentId);
            }

            @Override
            protected void done() {
                try {
                    Student student = get();
                    if (student != null) {
                        StudentFormDialog dialog = new StudentFormDialog(mainFrame, student, studentDAO, userDAO);
                        dialog.setVisible(true);
                        if (dialog.isSaved()) {
                            toast.showSuccess("Student details updated successfully.");
                            loadStudentsAsync();
                        }
                    }
                } catch (Exception e) {
                    toast.showError("Error loading student details: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void handleDeleteStudent() {
        int row = tblStudents.getSelectedRow();
        if (row < 0) {
            toast.showInfo("Please select a student from the table to delete.");
            return;
        }

        String studentId = (String) tableModel.getValueAt(row, 0);
        String studentName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(
                mainFrame,
                "Are you sure you want to delete '" + studentName + "' (" + studentId + ")?\nThis will also remove their associated user account.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return studentDAO.deleteStudent(studentId);
                }

                @Override
                protected void done() {
                    try {
                        boolean ok = get();
                        if (ok) {
                            toast.showSuccess("Student '" + studentName + "' deleted successfully.");
                            loadStudentsAsync();
                        } else {
                            toast.showError("Failed to delete student.");
                        }
                    } catch (Exception e) {
                        toast.showError("Error deleting student: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }
}
