package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import structures.Task;
import structures.TaskList;
import structures.TaskStack;

public class CompletedTasks extends JFrame {

    private TaskList mainTaskList;
    private TaskStack completedStack;
    private CompletedTasksTableModel tableModel;
    private JTable completedTable;

    public CompletedTasks(TaskList mainTaskList, TaskStack completedStack) {
        this.mainTaskList = mainTaskList;
        this.completedStack = completedStack;

        setTitle("Completed Tasks (Stack)");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // --- Background ---
        getContentPane().setBackground(new Color(28, 28, 28)); // Dark gray

        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 14);

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(35, 35, 35));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 255, 180), 2, true),
                BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));

        JLabel titleLabel = new JLabel("✅ Completed Tasks (Most Recent on Top)");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(new Color(0, 255, 180)); // Neon cyan
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table ---
        tableModel = new CompletedTasksTableModel(completedStack);
        completedTable = new JTable(tableModel);
        completedTable.setFont(tableFont);
        completedTable.setRowHeight(42);
        completedTable.setGridColor(new Color(70, 70, 70));
        completedTable.setShowVerticalLines(false);
        completedTable.setShowHorizontalLines(true);
        completedTable.setBackground(new Color(42, 42, 42));
        completedTable.setForeground(Color.WHITE);
        completedTable.setSelectionBackground(new Color(0, 255, 180, 80));
        completedTable.setSelectionForeground(Color.BLACK);
        completedTable.setAutoCreateRowSorter(true);

        // --- Table Header ---
        completedTable.getTableHeader().setBackground(new Color(40, 40, 40));
        completedTable.getTableHeader().setForeground(new Color(0, 255, 180));
        completedTable.getTableHeader().setFont(headerFont.deriveFont(Font.BOLD, 15));
        completedTable.getTableHeader().setBorder(new LineBorder(new Color(0, 255, 180), 2, true));
        completedTable.getTableHeader().setReorderingAllowed(false);

        // Column widths
        completedTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        completedTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        completedTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        completedTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        // --- Undo Button Renderer & Editor ---
        ButtonRenderer renderer = new ButtonRenderer("Undo");
        completedTable.getColumn("Action").setCellRenderer(renderer);

        ButtonEditor editor = new ButtonEditor(new JTextField());
        completedTable.getColumn("Action").setCellEditor(editor);

        editor.getButton().addActionListener(e -> {
            int viewRow = completedTable.getEditingRow();
            if (viewRow == -1) return;

            int modelRow = completedTable.convertRowIndexToModel(viewRow);
            List<Task> currentTasks = completedStack.getTasks();
            Task taskToUndo = currentTasks.get(modelRow);

            Task poppedTask = null;
            TaskStack tempStack = new TaskStack();

            while (!completedStack.isEmpty()) {
                Task current = completedStack.pop();
                if (current.getId() == taskToUndo.getId()) {
                    poppedTask = current;
                    break;
                }
                tempStack.push(current);
            }

            while (!tempStack.isEmpty()) {
                completedStack.push(tempStack.pop());
            }

            if (poppedTask != null) {
                poppedTask.setStatus(Task.Status.pending);
                mainTaskList.createTask(poppedTask);
                tableModel.refresh();
                JOptionPane.showMessageDialog(this,
                        "Task '" + taskToUndo.getTitle() + "' restored to main list.",
                        "Task Undone", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: Could not find task to undo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            SwingUtilities.invokeLater(() -> completedTable.repaint());
        });

        // --- Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(completedTable);
        scrollPane.getViewport().setBackground(new Color(28, 28, 28));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.refresh();
    }

    // --- Button Renderer ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
            setBackground(new Color(0, 255, 180));
            setForeground(Color.BLACK);
            setFont(getFont().deriveFont(Font.BOLD, 13));
            setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(new Color(0, 255, 180, 120));
            } else {
                setBackground(new Color(0, 255, 180));
            }
            return this;
        }
    }

    // --- Button Editor ---
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;

        public ButtonEditor(JTextField textField) {
            super(textField);
            setClickCountToStart(1);

            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(0, 255, 180));
            button.setForeground(Color.BLACK);
            button.setFont(button.getFont().deriveFont(Font.BOLD, 13));
            button.setFocusPainted(false);
        }

        public JButton getButton() {
            return this.button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }
}
