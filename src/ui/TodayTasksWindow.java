package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import structures.Task;
import structures.TaskQueue;
import structures.TaskStack;
import structures.TaskList;

public class TodayTasksWindow extends JFrame {

    private TaskQueue todayQueue;
    private TaskStack completedStack;
    private TaskList mainTaskList;
    private TodayTasksTableModel tableModel;
    private JTable todayTable;

    public TodayTasksWindow(TaskQueue todayQueue, TaskStack completedStack, TaskList mainTaskList) {
        this.todayQueue = todayQueue;
        this.completedStack = completedStack;
        this.mainTaskList = mainTaskList;

        setTitle("Today's Tasks (Queue - FIFO)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // --- Styles matching other windows ---
        getContentPane().setBackground(new Color(20, 20, 20));
        Font headerFont = new Font("Montserrat", Font.BOLD, 18);
        Font tableFont = new Font("Montserrat", Font.PLAIN, 14);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(32, 32, 32));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("⭐ Today's Priority Tasks (First In, First Out)");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(new Color(0, 122, 255));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Info label showing queue size
        JLabel infoLabel = new JLabel("Tasks in queue: " + todayQueue.size() + " / 10");
        infoLabel.setFont(new Font("Montserrat", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(200, 200, 200));
        headerPanel.add(infoLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        tableModel = new TodayTasksTableModel(todayQueue);
        todayTable = new JTable(tableModel);

        // Table Styling
        todayTable.setFont(tableFont);
        todayTable.setRowHeight(35);
        todayTable.setGridColor(new Color(50, 50, 50));
        todayTable.setBackground(new Color(32, 32, 32));
        todayTable.setForeground(Color.WHITE);
        todayTable.setSelectionBackground(new Color(50, 50, 50));
        todayTable.setSelectionForeground(Color.WHITE);

        // Table Header Styling
        todayTable.getTableHeader().setBackground(new Color(45, 45, 45));
        todayTable.getTableHeader().setForeground(Color.WHITE);
        todayTable.getTableHeader().setFont(headerFont.deriveFont(Font.BOLD, 14));
        todayTable.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

        // Set column widths
        todayTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // Position
        todayTable.getColumnModel().getColumn(1).setPreferredWidth(50);  // ID
        todayTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Title
        todayTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Priority
        todayTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Deadline
        todayTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Actions

        // --- Button Renderer and Editor for Actions column ---
        TodayActionRenderer renderer = new TodayActionRenderer();
        todayTable.getColumn("Actions").setCellRenderer(renderer);
        
        TodayActionEditor editor = new TodayActionEditor(new JTextField());
        todayTable.getColumn("Actions").setCellEditor(editor);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(todayTable);
        scrollPane.getViewport().setBackground(new Color(32, 32, 32));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Bottom info panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(32, 32, 32));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel tipLabel = new JLabel("💡 Tip: Process tasks from top to bottom (FIFO order)");
        tipLabel.setFont(new Font("Montserrat", Font.ITALIC, 12));
        tipLabel.setForeground(new Color(150, 150, 150));
        bottomPanel.add(tipLabel);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Refresh the table and update queue size label
     */
    public void refreshTable() {
        tableModel.refresh();
        // Update the info label in header
        Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && ((JLabel)comp).getText().contains("Tasks in queue")) {
                ((JLabel)comp).setText("Tasks in queue: " + todayQueue.size() + " / 10");
                break;
            }
        }
    }

    // --- Action Renderer Class (Draws buttons) ---
    class TodayActionRenderer extends JPanel implements TableCellRenderer {
        private JButton completeButton;
        private JButton removeButton;

        public TodayActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);

            completeButton = createButton("✓ Complete", new Color(100, 200, 100));
            removeButton = createButton("✗ Remove", new Color(255, 100, 100));

            add(completeButton);
            add(removeButton);
        }

        private JButton createButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFont(getFont().deriveFont(Font.BOLD, 11));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    // --- Action Editor Class (Handles button clicks) ---
    class TodayActionEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton completeButton;
        private JButton removeButton;
        private int currentRow;

        public TodayActionEditor(JTextField textField) {
            super(textField);
            setClickCountToStart(1);

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);

            completeButton = createButton("✓ Complete", new Color(100, 200, 100));
            removeButton = createButton("✗ Remove", new Color(255, 100, 100));

            // Complete button - dequeue and mark as completed
            completeButton.addActionListener(e -> {
                Task task = todayQueue.dequeue();
                if (task != null) {
                    // Mark as completed and push to stack
                    task.setStatus(Task.Status.completed);
                    completedStack.push(task);
                    
                    // Remove from main list if it exists there
                    mainTaskList.deleteTask(task.getId());
                    
                    JOptionPane.showMessageDialog(TodayTasksWindow.this,
                        "Task '" + task.getTitle() + "' completed and moved to completed stack!",
                        "Task Completed", JOptionPane.INFORMATION_MESSAGE);
                    
                    refreshTable();
                }
                fireEditingStopped();
            });

            // Remove button - just dequeue without completing
            removeButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(TodayTasksWindow.this,
                    "Remove this task from today's queue?\n(Task will remain in main list)",
                    "Confirm Remove", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Task task = todayQueue.dequeue();
                    if (task != null) {
                        JOptionPane.showMessageDialog(TodayTasksWindow.this,
                            "Task '" + task.getTitle() + "' removed from today's queue.",
                            "Task Removed", JOptionPane.INFORMATION_MESSAGE);
                        refreshTable();
                    }
                }
                fireEditingStopped();
            });

            panel.add(completeButton);
            panel.add(removeButton);
        }

        private JButton createButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(Font.BOLD, 11));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}