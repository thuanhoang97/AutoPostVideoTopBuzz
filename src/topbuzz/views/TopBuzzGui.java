/*
 * Author: Rem
 * email:hoangvietthuan97@gmail.com
 * 
 * */
package topbuzz.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

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
import topbuzz.model.Config;
import topbuzz.model.TopBuzzAuto;
import topbuzz.model.videosource.VideoFile;
import topbuzz.model.videosource.VideoSource;

import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JMenu;
import javax.swing.JTextArea;
import javax.swing.JMenuItem;
import javax.swing.JCheckBox;

public class TopBuzzGui extends JFrame {

	private JPanel contentPane;
	private JLabel lbAccount;
	private JLabel lbPasssword;
	private JLabel lbForderPath;
	private JTextField tfAccount;
	private JTextField tfPassword;
	private JButton btnRun;
	private final Action action = new RunApp();
	private JTextField tfForderPath;
	private JLabel label;
	private JTextArea videoFilesView;
	private VideoSource videoSource;
	private TopBuzzAuto auto;
//	private TopBuzzAuto autoCheck;
	private JCheckBox cbIsCheck;
	private JTextField tfTimeCheck;
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
		setBounds(800, 50, 479, 477);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		lbAccount = new JLabel("Account:");
		lbAccount.setBounds(20, 11, 65, 21);
		contentPane.add(lbAccount);
		
		tfAccount = new JTextField();
		tfAccount.setBounds(140, 11, 262, 21);
		contentPane.add(tfAccount);
		tfAccount.setColumns(10);
		
		lbPasssword = new JLabel("Password:");
		lbPasssword.setBounds(20, 43, 65, 21);
		contentPane.add(lbPasssword);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(140, 43, 262, 21);
		contentPane.add(tfPassword);
		
		btnRun = new JButton("Run");
		btnRun.setAction(action);
		btnRun.setBounds(114, 394, 125, 30);
		contentPane.add(btnRun);
		
		
		lbForderPath = new JLabel("Forder Video:");
		lbForderPath.setBounds(20, 75, 93, 21);
		contentPane.add(lbForderPath);
		
		tfForderPath = new JTextField();
		tfForderPath.setColumns(10);
		tfForderPath.setBounds(140, 75, 262, 21);
		contentPane.add(tfForderPath);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(20, 166, 330, 205);
		contentPane.add(scroll);
		
		videoFilesView = new JTextArea();
		scroll.setViewportView(videoFilesView);
		videoFilesView.setEditable(false);
		
		JButton btnNewButton = new JButton("Load");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				videoFilesView.setText("");
				videoSource.getFromForder(tfForderPath.getText());
				videoFilesView.setText(videoSource.toString());
				videoFilesView.append("========================================\n");
				videoFilesView.append("Log:\n");
			}
		});
		btnNewButton.setBounds(374, 168, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Add");
		btnNewButton_1.setBounds(374, 202, 89, 23);
		contentPane.add(btnNewButton_1);
		
		label = new JLabel("Video:");
		label.setBounds(20, 134, 65, 21);
		contentPane.add(label);
		
		tfTimeCheck = new JTextField("10");
		tfTimeCheck.setColumns(10);
		tfTimeCheck.setBounds(140, 107, 262, 21);
		contentPane.add(tfTimeCheck);
		
		cbIsCheck = new JCheckBox("Check Status");
		cbIsCheck.setBounds(16, 103, 118, 23);
		cbIsCheck.setSelected(true);
		cbIsCheck.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		          if(cbIsCheck.isSelected()) {
		        	  tfTimeCheck.setEnabled(true);
		          }else{
		        	  tfTimeCheck.setEnabled(false);
		          }
		        }
		      });
		contentPane.add(cbIsCheck);
		
		
		
		setTitle("TopBuzz Automation");
		
		videoSource = new VideoSource();
		
		this.addWindowListener(new WindownInitialize());
	}
	
	
	private void loadFromCache() {
		AppCache.load();
		tfAccount.setText(AppCache.getAccount());
		tfPassword.setText(AppCache.getPassWord());
		tfForderPath.setText(AppCache.getForderPath());
	}
	
	private class RunApp extends AbstractAction {
		public RunApp() {
			putValue(NAME, "Run");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			
			if(videoSource.getFiles().size()<=0) {
				new ErrorDialog("No File Selected", "Please load video file");
				return ;
			}
			String account = tfAccount.getText();
			String password = tfPassword.getText();
			auto = new TopBuzzAuto(account, password);
			auto.setVideoSource(videoSource);
			auto.setViewLog(videoFilesView);
			auto.start();
			
			
//			try {
//				int count = 0;
//				auto = new TopBuzzAuto();		
//				auto.login(tfAccount.getText(), tfPassword.getText());
//	//			auto.post(videoSource.getFiles().get(0));
//				for(VideoFile video : videoSource.getFiles()) {
//					auto.post(video);
//					new  File(video.getAbsolutePath()).delete();
//					videoFilesView.append("Posted: " + video.getTitle() +"\n");
//					auto.deleteRejectedVideo(videoFilesView);
//					
//					System.out.println("Doned " + (++count)+ " : " + video.getTitle());
//				}
//				System.out.println("Finished");
//			}catch(Exception ex) {
//				ex.printStackTrace();
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
			AppCache.setAccount(tfAccount.getText());
			AppCache.setPassword(tfPassword.getText());
			AppCache.setForderPath(tfForderPath.getText());
			AppCache.save();
			if(auto!=null && auto.isAlive()) auto.close();
//			if(autoCheck !=null) autoCheck.close();
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
			loadFromCache();
//			System.out.println("windown opened");
		}
	}
}
