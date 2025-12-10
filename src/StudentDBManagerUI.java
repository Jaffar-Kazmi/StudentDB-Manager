import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentDBManagerUI extends JFrame {

    private JTextField txtFirstName, txtLastName, txtAge, txtEmail, txtSearchId;
    private JButton btnAdd, btnView, btnSearch, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea txtStatus;
    private JLabel lblRecordCount;
    private JProgressBar progressBar;

    private StudentDAO studentDAO = new StudentDAO();

    public StudentDBManagerUI() {
        setTitle("Student Database Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        initLayout();
        initListeners();
    }

    private void initComponents() {
        // Input fields with tooltips
        txtFirstName = new JTextField(20);
        txtFirstName.setToolTipText("Enter student's first name");

        txtLastName = new JTextField(20);
        txtLastName.setToolTipText("Enter student's last name");

        txtAge = new JTextField(20);
        txtAge.setToolTipText("Enter student's age (numeric)");

        txtEmail = new JTextField(20);
        txtEmail.setToolTipText("Enter student's email address");

        txtSearchId = new JTextField(15);
        txtSearchId.setToolTipText("Enter student ID to search");

        // Styled buttons
        btnAdd = createStyledButton("Add Student", new Color(46, 125, 50));
        btnView = createStyledButton("View All Students", new Color(123, 31, 162));
        btnSearch = createStyledButton("Search", new Color(255, 152, 0));
        btnClear = createStyledButton("Clear Fields", new Color(96, 125, 139));

        // Table with custom styling
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "First Name", "Last Name", "Age", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setGridColor(new Color(224, 224, 224));

        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(63, 81, 181));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Status area
        txtStatus = new JTextArea(5, 50);
        txtStatus.setEditable(false);
        txtStatus.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Record count label
        lblRecordCount = new JLabel("Total Records: 0");
        lblRecordCount.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void initLayout() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(63, 81, 181));
        JLabel titleLabel = new JLabel("Student Database Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Input panel with grouped layout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(63, 81, 181), 2),
                        "Student Information",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 14),
                        new Color(63, 81, 181)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // First row
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(createLabel("First Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtFirstName, gbc);

        gbc.gridx = 2;
        inputPanel.add(createLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(txtLastName, gbc);

        // Second row
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(createLabel("Age:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtAge, gbc);

        gbc.gridx = 2;
        inputPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(txtEmail, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(255, 152, 0), 2),
                        "Search & View",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 14),
                        new Color(255, 152, 0)
                ),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchPanel.add(createLabel("Student ID:"));
        searchPanel.add(txtSearchId);
        searchPanel.add(btnSearch);
        searchPanel.add(btnView);
        searchPanel.add(lblRecordCount);

        // Combine input and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table panel
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(123, 31, 162), 2),
                        "Student Records",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 14),
                        new Color(123, 31, 162)
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Status panel
        JScrollPane statusScroll = new JScrollPane(txtStatus);
        statusScroll.setBorder(BorderFactory.createLineBorder(new Color(96, 125, 139)));

        JPanel statusPanel = new JPanel(new BorderLayout(5, 5));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(96, 125, 139), 2),
                        "Status Log",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 14),
                        new Color(96, 125, 139)
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        statusPanel.add(statusScroll, BorderLayout.CENTER);
        statusPanel.add(progressBar, BorderLayout.SOUTH);

        // Assemble main panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(searchPanel, BorderLayout.AFTER_LINE_ENDS);
        contentPanel.add(tableScroll, BorderLayout.CENTER);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private void initListeners() {
        btnAdd.addActionListener(e -> addStudentAsync());
        btnView.addActionListener(e -> loadStudentsAsync());
        btnSearch.addActionListener(e -> searchStudentAsync());
        btnClear.addActionListener(e -> clearInputFields());
    }

    private void addStudentAsync() {
        String first = txtFirstName.getText().trim();
        String last = txtLastName.getText().trim();
        String ageText = txtAge.getText().trim();
        String email = txtEmail.getText().trim();

        if (first.isEmpty() || last.isEmpty() || ageText.isEmpty() || email.isEmpty()) {
            appendStatus("❌ Error: Please fill all fields.");
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age < 1 || age > 150) {
                throw new NumberFormatException("Age out of range");
            }
        } catch (NumberFormatException ex) {
            appendStatus("❌ Error: Age must be a valid number (1-150).");
            JOptionPane.showMessageDialog(this, "Age must be a valid number between 1 and 150.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student s = new Student(first, last, age, email);
        showProgress(true, "Adding student...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                studentDAO.addStudent(s);
                return null;
            }

            @Override
            protected void done() {
                showProgress(false, "");
                try {
                    get();
                    appendStatus("✓ Student added successfully: " + first + " " + last);
                    clearInputFields();
                    JOptionPane.showMessageDialog(StudentDBManagerUI.this,
                            "Student added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    appendStatus("❌ Error adding student: " + ex.getMessage());
                    JOptionPane.showMessageDialog(StudentDBManagerUI.this,
                            "Error adding student: " + ex.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void loadStudentsAsync() {
        showProgress(true, "Loading students...");

        SwingWorker<List<Student>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Student> doInBackground() throws Exception {
                return studentDAO.getAllStudents();
            }

            @Override
            protected void done() {
                showProgress(false, "");
                try {
                    List<Student> students = get();
                    tableModel.setRowCount(0);
                    for (Student s : students) {
                        tableModel.addRow(new Object[]{
                                s.getId(),
                                s.getFirstName(),
                                s.getLastName(),
                                s.getAge(),
                                s.getEmail()
                        });
                    }
                    lblRecordCount.setText("Total Records: " + students.size());
                    appendStatus("✓ Loaded " + students.size() + " student(s).");
                } catch (Exception ex) {
                    appendStatus("❌ Error loading students: " + ex.getMessage());
                    JOptionPane.showMessageDialog(StudentDBManagerUI.this,
                            "Error loading students: " + ex.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void searchStudentAsync() {
        String idText = txtSearchId.getText().trim();
        if (idText.isEmpty()) {
            appendStatus("❌ Error: Please enter ID to search.");
            JOptionPane.showMessageDialog(this, "Please enter a student ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            appendStatus("❌ Error: ID must be a number.");
            return;
        }

        showProgress(true, "Searching...");

        SwingWorker<Student, Void> worker = new SwingWorker<>() {
            @Override
            protected Student doInBackground() throws Exception {
                return studentDAO.getStudentById(id);
            }

            @Override
            protected void done() {
                showProgress(false, "");
                try {
                    Student s = get();
                    tableModel.setRowCount(0);
                    if (s != null) {
                        tableModel.addRow(new Object[]{
                                s.getId(),
                                s.getFirstName(),
                                s.getLastName(),
                                s.getAge(),
                                s.getEmail()
                        });
                        lblRecordCount.setText("Total Records: 1");
                        appendStatus("✓ Student found with ID " + id + ".");
                    } else {
                        lblRecordCount.setText("Total Records: 0");
                        appendStatus("⚠ No student found with ID " + id + ".");
                        JOptionPane.showMessageDialog(StudentDBManagerUI.this,
                                "No student found with ID: " + id,
                                "Not Found",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    appendStatus("❌ Error searching student: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void showProgress(boolean show, String message) {
        progressBar.setVisible(show);
        if (show) {
            progressBar.setIndeterminate(true);
            progressBar.setString(message);
        }
    }

    private void appendStatus(String msg) {
        txtStatus.append("[" + java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + msg + "\n");
        txtStatus.setCaretPosition(txtStatus.getDocument().getLength());
    }

    private void clearInputFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtAge.setText("");
        txtEmail.setText("");
        txtSearchId.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDBManagerUI().setVisible(true));
    }
}