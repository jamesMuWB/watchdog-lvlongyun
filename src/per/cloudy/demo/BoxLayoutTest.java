package per.cloudy.demo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class BoxLayoutTest {

	static JPanel topPanel;
	static JPanel bottomPanel;
	static JPanel middlePanel;

	static void createTopPanel() {
		topPanel = new JPanel();
		String[] columnName = { "姓名", "性别", "单位", "参加项目", "备注" };
		String[][] rowData = { { "张三", "男", "计算机系", "100米,200米", "" },
				               { "李四", "男", "化学系", "100米,铅球", "" },
				             };
		JTable table = new JTable(new DefaultTableModel(rowData, columnName));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(Box.createVerticalStrut(10));
		topPanel.add(scrollPane);
        topPanel.add(Box.createVerticalStrut(10));
	}
	public static void main(String[] args) {
		createTopPanel();
		JPanel panelContainer = new JPanel();
		panelContainer.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		c1.weightx = 1.0;
		c1.weighty = 1.0;

		c1.fill = GridBagConstraints.BOTH;
		panelContainer.add(topPanel, c1);

		JFrame frame = new JFrame("Boxlayout演示");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panelContainer);
		frame.setVisible(true);
	}
}

