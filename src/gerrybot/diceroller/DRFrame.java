package gerrybot.diceroller;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import net.dv8tion.jda.api.entities.User;

public class DRFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static DRFrame instance;
	
	private JProgressBar loading;
	private JList<User> rollList;
	private HashMap<Long, ImageIcon> avatars;
	
	public DRFrame() {
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
		
		setVisible(true);
	}
	
	public void removeLoading() {
		remove(loading);
		addMainComponents();
		repaint();
	}
	
	public void addMainComponents() {
		
		// List of Users rolling
		rollList = new JList<User>(new DefaultListModel<>());
		rollList.setLocation(30, 5);
		rollList.setSize(200, getHeight());
		rollList.setSelectedIndex(1);
		rollList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				User author = (User) value;
			
				label.setText(author.getEffectiveName());
				label.setHorizontalAlignment(SwingConstants.CENTER);
				
				Border border = label.getBorder();
				Border margin = new EmptyBorder(2, -6, 2, -6);
				label.setBorder(new CompoundBorder(border, margin));
				
				label.setIcon(avatars.get(author.getIdLong()));	
				
				return label;
			}
		});
		avatars = new HashMap<>();
		add(rollList);
	}
	
	public void addUserToRollList(User author) {
		try {
			DefaultListModel<User> model = (DefaultListModel<User>) rollList.getModel();
			
			if(!isUserInList(author)) {
					avatars.put(author.getIdLong(), new ImageIcon(ImageIO.read(author.getAvatar().download(64).get())));
					System.out.println("BAIXEEEI, ADICIONEEEI");
					model.addElement(author);
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isUserInList(User author) {
		DefaultListModel<User> model = (DefaultListModel<User>) rollList.getModel();
		
		for(int i = 0; i < model.getSize(); i++) {
			if(model.get(i).getIdLong() == author.getIdLong())
				return false;
		}
		
		return false;
	}
}
