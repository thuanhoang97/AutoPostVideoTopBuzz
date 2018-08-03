package topbuzz.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ErrorDialog extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private final JLabel lbError ;

	/**
	 * Create the dialog.
	 */
	public ErrorDialog(String title, String errText) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(title);
		setFocusable(true);
		setVisible(true);
		setBounds(500, 300, 400, 152);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ErrorDialog.this.dispose();
				}
			});
			okButton.setBounds(145, 73, 81, 31);
			contentPanel.add(okButton);
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
			lbError = new JLabel(errText, SwingConstants.CENTER);
			lbError.setBounds(33, 11, 327, 23);
			
			contentPanel.add(lbError);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
		
		Toolkit.getDefaultToolkit().beep();
	}

}
