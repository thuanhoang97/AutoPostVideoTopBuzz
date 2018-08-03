package topbuzz.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.openqa.selenium.JavascriptExecutor;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

import topbuzz.model.AppCache;
import topbuzz.model.TopBuzzAuto;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TopBuzzGui extends JFrame {

	private JPanel contentPane;
	private JLabel lbAccount;
	private JLabel lbPasssword;
	private JTextField tfAccount;
	private JTextField tfPassword;
	private JButton btnRun;
	private final Action action = new RunApp();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TopBuzzGui frame = new TopBuzzGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TopBuzzGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(800, 50, 358, 477);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		lbAccount = new JLabel("Account:");
		lbAccount.setBounds(20, 11, 65, 21);
		contentPane.add(lbAccount);
		
		tfAccount = new JTextField();
		tfAccount.setBounds(93, 11, 242, 21);
		contentPane.add(tfAccount);
		tfAccount.setColumns(10);
		
		lbPasssword = new JLabel("Password:");
		lbPasssword.setBounds(20, 43, 65, 21);
		contentPane.add(lbPasssword);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(93, 43, 242, 21);
		contentPane.add(tfPassword);
		
		btnRun = new JButton("Run");
		btnRun.setAction(action);
		btnRun.setBounds(116, 407, 125, 30);
		contentPane.add(btnRun);
		
		JPopupMenu popupMenu_1 = new JPopupMenu();
		popupMenu_1.setBounds(243, 373, 200, 50);
		contentPane.add(popupMenu_1);
		
		setTitle("TopBuzz Automation");
		
		this.addWindowListener(new WindownInitialize());
	}
	
	
	private void loadFromCache() {
		AppCache.load();
		tfAccount.setText(AppCache.getAccount());
		tfPassword.setText(AppCache.getPassWord());
	}
	
	private class RunApp extends AbstractAction {
		public RunApp() {
			putValue(NAME, "Run");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			TopBuzzAuto auto = new TopBuzzAuto("https://www.topbuzz.com/");		
//			boolean isSuccess = auto.login(tfAccount.getText(), tfPassword.getText());
//			
//			if(isSuccess) {
//				auto.post();
//			}else {
//				
//			}
			
		}
	}
	
	private class WindownInitialize implements WindowListener{
		@Override
		public void windowActivated(WindowEvent e) {
			
//			System.out.println("windown activeted");
		}

		@Override
		public void windowClosed(WindowEvent e) {
//			System.out.println("windown closed");
		}

		@Override
		public void windowClosing(WindowEvent e) {
//			AppCache.setAccount(tfAccount.getText());
//			AppCache.setPassword(tfPassword.getText());
//			AppCache.setForderPath("none");
//			AppCache.save();
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
//			System.out.println("windown deactivated");
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
//			System.out.println("windown deniconified");
		}

		@Override
		public void windowIconified(WindowEvent e) {
//			System.out.println("windown iconified");
		}

		@Override
		public void windowOpened(WindowEvent e) {
//			loadFromCache();
//			System.out.println("windown opened");
		}
	}
}
