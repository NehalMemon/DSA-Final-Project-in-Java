package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
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

        // Fonts
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(59, 130, 246)),
                BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));
        JLabel titleLabel = new JLabel("✅ Completed Tasks (Most Recent on Top)");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(new Color(59, 130, 246));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table Setup
        tableModel = new CompletedTasksTableModel(completedStack);
        completedTable = new JTable(tableModel) {
            // Alternate row colors
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        };
        completedTable.setFont(tableFont);
        completedTable.setRowHeight(44);
        completedTable.setGridColor(new Color(220, 220, 220));
        completedTable.setShowVerticalLines(false);
        completedTable.setSelectionBackground(new Color(187, 222, 251));
        completedTable.setSelectionForeground(Color.BLACK);
        completedTable.setAutoCreateRowSorter(true);

        // Table Header Styling
        JTableHeader header = completedTable.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(59, 130, 246));
        header.setFont(headerFont.deriveFont(Font.BOLD, 15));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(59, 130, 246)));
        header.setReorderingAllowed(false);

        // Set column widths
        completedTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        completedTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        completedTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        completedTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        // Undo Button Renderer & Editor
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

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(completedTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.refresh();
    }

    // Round Gradient Button Renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(Font.BOLD, 13));
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder());
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gradient background
            GradientPaint gradient = new GradientPaint(0, 0, new Color(59, 130, 246),
                                                       0, getHeight(), new Color(37, 99, 235));
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            super.paintComponent(g);
            g2d.dispose();
        }
    }

    // Round Gradient Button Editor
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;

        public ButtonEditor(JTextField textField) {
            super(textField);
            setClickCountToStart(1);

            button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    GradientPaint gradient = new GradientPaint(0, 0, new Color(59, 130, 246),
                                                               0, getHeight(), new Color(37, 99, 235));
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    super.paintComponent(g);
                    g2d.dispose();
                }
            };
            button.setOpaque(false);
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(Font.BOLD, 13));
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public JButton getButton() {
            return this.button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = value != null ? value.toString() : "";
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }
}
