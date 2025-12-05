package gerrybot.diceroller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.dv8tion.jda.api.entities.User;

@SuppressWarnings("serial")
public class DRFrame extends JFrame {
	
	private static DRFrame instance = new DRFrame();
	
	private JProgressBar loading;
	private JList<User> rollList;
	
	private JPanel drPanel;
	private JLabel diceLabel;
	private JLabel valueLabel;
	private JFormattedTextField dice;
	private JFormattedTextField value;
	private JButton register;
	private JButton crit;
	private JButton fail;
	private JButton remove;
	
	private JList<String> drList;
	
	private DRFrame() {
		setTitle("GerryBot UI");
		setSize(600, 400);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Loading Bar
		loading = new JProgressBar();
		loading.setSize(getWidth() / 2, getHeight() / 4);
		loading.setLocation((int) (loading.getWidth() / 2.2), (int)(loading.getHeight() / 0.8));
		loading.setIndeterminate(true);
		add(loading);
	}
	
	public static DRFrame getInstance() {
		return instance;
	}
	
	public void removeLoading() {
		remove(loading);
		addMainComponents();
		repaint();
	}
	
	public void addMainComponents() {
		
		Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
		
		// List of Users rolling
		rollList = new JList<User>(new DefaultListModel<>());
		rollList.setLocation(20, 5);
		rollList.setSize(148, getHeight() - 48);
		rollList.setBorder(blackBorder);
		rollList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				User author = (User) value;
			
				label.setText(author.getEffectiveName());
				label.setHorizontalAlignment(SwingConstants.CENTER);
				
				Border margin = new EmptyBorder(2, -6, 2, -6);
				label.setBorder(new CompoundBorder(blackBorder, margin));
				
				ImageIcon icon = (ImageIcon) list.getClientProperty(author.getId());
				
				if(icon == null) {
					try {						
						BufferedImage image = ImageIO.read(author.getEffectiveAvatar().download().get());
						ImageIcon avatar = new ImageIcon(image.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
						
						list.putClientProperty(author.getId(), avatar);
						
						icon = avatar;
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				label.setIcon(icon);
				return label;
			}
		});
		
		rollList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting() || drPanel.isEnabled()) return;
				setEnabledRecursive(drPanel, true);
			}
		});
		
		drPanel = new JPanel();
		drPanel.setLayout(null);
		drPanel.setSize(rollList.getSize());
		drPanel.setLocation(rollList.getX() + 200, rollList.getY());
		drPanel.setBorder(blackBorder);
		
		NumberFormat format = NumberFormat.getIntegerInstance();
		
		diceLabel = new JLabel("Dice");
		diceLabel.setBounds(10, 20, drPanel.getWidth() - 20, 25);
		
		dice = new JFormattedTextField(format);
		dice.setHorizontalAlignment(SwingConstants.CENTER);
		dice.setSize(diceLabel.getSize());
		dice.setLocation(diceLabel.getX(), diceLabel.getY() + 20);
		
		valueLabel = new JLabel("Value");
		valueLabel.setSize(diceLabel.getSize());
		valueLabel.setLocation(dice.getX(), dice.getY() + 40);
		
		value = new JFormattedTextField(format);
		value.setHorizontalAlignment(SwingConstants.CENTER);
		value.setSize(valueLabel.getSize());
		value.setLocation(valueLabel.getX(), valueLabel.getY() + 20);
		
		register = new JButton("Register");
		register.setSize(diceLabel.getWidth(), diceLabel.getHeight() + 10);
		register.setLocation(value.getX(), value.getY() + 40);
		
		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User author = rollList.getSelectedValue();
				String diceS = dice.getText();
				String valueS = value.getText();
				
				if(diceS.isEmpty() || valueS.isEmpty()) return;
				
				((DefaultListModel<String>)drList.getModel()).addElement(author.getId() + "=" + diceS + "=" + valueS);
			}
		});
		
		crit = new JButton("Crit");
		crit.setSize(diceLabel.getWidth() / 2, diceLabel.getHeight());
		crit.setLocation(register.getX(), register.getY() + 55);
		crit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dice.setText("20");
				value.setText("20");
				register.doClick();
			}
		});
		
		
		fail = new JButton("Fail");
		fail.setSize(crit.getSize());
		fail.setLocation(crit.getX() + crit.getWidth() + 2, crit.getY());
		fail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dice.setText("20");
				value.setText("1");
				register.doClick();
			}
		});
		
		remove = new JButton("Remove");
		remove.setSize(register.getSize());
		remove.setLocation(crit.getX(), crit.getY() + 50);
		remove.setEnabled(false);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> model = getDRListModel();
				model.remove(drList.getSelectedIndex());
				
				if(model.size() == 0) {
					remove.setEnabled(false);
				}
			}
		});
		
		drList = new JList<String>(new DefaultListModel<>());
		drList.setSize(rollList.getSize());
		drList.setLocation(drPanel.getX() + 200, rollList.getY());
		drList.setBorder(blackBorder);
		drList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				String dr = (String) value;
				
				label.setText("d" + dr.substring(dr.indexOf("=") + 1));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				
				Border margin = new EmptyBorder(2, -6, 2, -6);
				label.setBorder(new CompoundBorder(blackBorder, margin));
				
				label.setIcon((ImageIcon) rollList.getClientProperty(dr.split("=")[0]));
				
				return label;
			}
		});
		
		drList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting() || remove.isEnabled()) return;
				remove.setEnabled(true);
			}
		});
		
		add(rollList);
		add(drPanel);
		add(drList);
		drPanel.add(diceLabel);
		drPanel.add(valueLabel);
		drPanel.add(dice);
		drPanel.add(value);
		drPanel.add(register);
		drPanel.add(crit);
		drPanel.add(fail);
		drPanel.add(remove);
		
		setEnabledRecursive(drPanel, false);
	}
	
    private void setEnabledRecursive(Component component, boolean enabled) {
        if(component == remove) return;
    	
    	component.setEnabled(enabled); // Enable/disable the component

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setEnabledRecursive(child, enabled); // Recursively enable/disable children
            }
        }
    }
	
	public void addUserToRollList(User author) {
		if(!isUserInList(author)) {
			((DefaultListModel<User>)rollList.getModel()).addElement(author);
		}
	}
	
	private boolean isUserInList(User author) {
		DefaultListModel<User> model = (DefaultListModel<User>) rollList.getModel();
		
		for(int i = 0; i < model.getSize(); i++) {
			if(model.get(i).getIdLong() == author.getIdLong())
				return true;
		}
		
		return false;
	}
	
	public ArrayList<String> getDRRollsFromUser(User author) {
		ArrayList<String> drRolls = new ArrayList<>();
		
		DefaultListModel<String> model = getDRListModel();

		for(int i = 0; i < model.size(); i++) {
			String currentDr = model.get(i);
			
			if(currentDr.startsWith(author.getId())) {
				drRolls.add(currentDr);
			}
		}
		
		return drRolls;
	}
	
	public DefaultListModel<String> getDRListModel() {
		return ((DefaultListModel<String>)drList.getModel());
	}
}
