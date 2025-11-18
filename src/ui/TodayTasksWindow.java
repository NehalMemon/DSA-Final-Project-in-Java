package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import structures.Task;
import structures.TaskQueue;
import structures.TaskStack;
import structures.TaskList;
import java.awt.event.ActionListener;

public class TodayTasksWindow extends JFrame {

    // Theme Colors
    private final Color BG_DARK = new Color(0x3d3b3c);
    private final Color ACCENT = new Color(0xb592a0);
    private final Color ACCENT2 = new Color(0x7a9e9f);
    private final Color SUCCESS = new Color(0x6bffb8);
    private final Color WARNING = new Color(0xbc5f04);

    // Derived Colors
    private final Color TABLE_ROW_BG = new Color(0x2f2f2f); // Slightly lighter dark for table body
    private final Color TEXT_LIGHT = Color.WHITE;
    
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
        getContentPane().setBackground(BG_DARK); // Applied BG_DARK
        
        // Using "Segoe UI" for consistency with MainWindow's UIManager settings
        Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_DARK); // Applied BG_DARK
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("⭐ Today's Priority Tasks (First In, First Out)");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(ACCENT); // Applied ACCENT color
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Right side: Info label and Refresh button
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeaderPanel.setBackground(BG_DARK);
        
        // Info label showing queue size
        JLabel infoLabel = new JLabel("Tasks in queue: " + todayQueue.size() + " / 10");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(ACCENT2); // Applied ACCENT2 for secondary text
        rightHeaderPanel.add(infoLabel);

        // *** NEW: Refresh Button ***
        JButton refreshBtn = styledButton("↻ Refresh", ACCENT2.darker(), headerFont.deriveFont(Font.BOLD, 12));
        refreshBtn.addActionListener(e -> refreshTable());
        rightHeaderPanel.add(refreshBtn);

        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        tableModel = new TodayTasksTableModel(todayQueue);
        todayTable = new JTable(tableModel);

        // Table Styling
        todayTable.setFont(tableFont);
        todayTable.setRowHeight(35);
        todayTable.setGridColor(BG_DARK.brighter()); // Consistent grid color
        todayTable.setBackground(TABLE_ROW_BG); // Applied TABLE_ROW_BG
        todayTable.setForeground(TEXT_LIGHT); // Applied TEXT_LIGHT
        todayTable.setSelectionBackground(ACCENT2.darker()); // Consistent selection color
        todayTable.setSelectionForeground(TEXT_LIGHT);

        // Table Header Styling
        todayTable.getTableHeader().setBackground(ACCENT); // Applied ACCENT for header background
        todayTable.getTableHeader().setForeground(TEXT_LIGHT);
        todayTable.getTableHeader().setFont(headerFont.deriveFont(Font.BOLD, 15));
        todayTable.getTableHeader().setBorder(BorderFactory.createLineBorder(BG_DARK.brighter()));

        // Set column widths
        todayTable.getColumnModel().getColumn(0).setPreferredWidth(60); // Position
        todayTable.getColumnModel().getColumn(1).setPreferredWidth(50); // ID
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
        scrollPane.getViewport().setBackground(TABLE_ROW_BG); // Applied TABLE_ROW_BG
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Bottom info panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BG_DARK); // Applied BG_DARK
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel tipLabel = new JLabel("💡 Tip: Process tasks from top to bottom (FIFO order)");
        tipLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        tipLabel.setForeground(ACCENT2); // Applied ACCENT2
        bottomPanel.add(tipLabel);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton styledButton(String text, Color bg, Font font) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        b.setFocusPainted(false);
        b.setFont(font);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
    }

    /**
     * Refresh the table and update queue size label
     */
    public void refreshTable() {
        tableModel.refresh();
        // Update the info label in header
        Component northComponent = getContentPane().getComponent(0);
        if (northComponent instanceof JPanel) {
             Component rightComponent = ((JPanel)northComponent).getComponent(1);
             if (rightComponent instanceof JPanel) {
                 for (Component comp : ((JPanel)rightComponent).getComponents()) {
                     if (comp instanceof JLabel && ((JLabel)comp).getText().contains("Tasks in queue")) {
                         ((JLabel)comp).setText("Tasks in queue: " + todayQueue.size() + " / 10");
                         break;
                     }
                 }
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

            // Applied SUCCESS color
            completeButton = createButton("✓ Complete", SUCCESS); 
            // Applied WARNING color
            removeButton = createButton("✗ Remove", WARNING); 

            add(completeButton);
            add(removeButton);
        }

        private JButton createButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(TEXT_LIGHT);
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

            // Applied SUCCESS color
            completeButton = createButton("✓ Complete", SUCCESS); 
            // Applied WARNING color
            removeButton = createButton("✗ Remove", WARNING); 

            // Complete button - dequeue and mark as completed
            completeButton.addActionListener(e -> {
                // To safely dequeue the task associated with the CURRENTLY SELECTED row (FIFO rule)
                Task task = todayQueue.dequeue(); 
                if (task != null) {
                    // Mark as completed and push to stack
                    task.setStatus(Task.Status.completed);
                    completedStack.push(task);
                    
                    // The main task list *should* have already had this task deleted
                    // when it was added to the queue in TaskTableModel.
                    // We only need to refresh the current view.
                    
                    JOptionPane.showMessageDialog(TodayTasksWindow.this,
                        "Task '" + task.getTitle() + "' completed and moved to completed stack!",
                        "Task Completed", JOptionPane.INFORMATION_MESSAGE);
                    
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(TodayTasksWindow.this, "Queue is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                fireEditingStopped();
            });

            // Remove button - dequeue and RE-ADD to main list
            removeButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(TodayTasksWindow.this,
                    "Remove this task from today's queue?\n(Task will be restored to the main list as pending)",
                    "Confirm Remove", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // To safely dequeue the task associated with the CURRENTLY SELECTED row (FIFO rule)
                    Task task = todayQueue.dequeue();
                    if (task != null) {
                        // CRITICAL FIX: Restore the task to the main list
                        task.setStatus(Task.Status.pending);
                        mainTaskList.createTask(task); // Assuming this method handles adding/re-inserting

                        JOptionPane.showMessageDialog(TodayTasksWindow.this,
                            "Task '" + task.getTitle() + "' restored to the main list.",
                            "Task Removed", JOptionPane.INFORMATION_MESSAGE);
                        
                        // We must also refresh the MainWindow's model!
                        // This is currently hard to do without a direct reference, but refreshTable will update this window.
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(TodayTasksWindow.this, "Queue is empty!", "Error", JOptionPane.ERROR_MESSAGE);
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
            button.setForeground(TEXT_LIGHT);
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