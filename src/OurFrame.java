import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.WindowConstants;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.opencv.videoio.VideoCapture;

public class OurFrame extends JFrame {
	CameraPanel panel;

	// constructor
	public OurFrame() {
		super("Motion Detect v1.0");

		ImageIcon icon = new ImageIcon(getClass().getResource("8.png"));

		System.loadLibrary("opencv_java310");

		// its a JPanel class and calling constructor
		panel = new CameraPanel();

		Thread thread = new Thread(panel);

		// start the thread
		thread.start();

		// Add Jpanel in JFrame Default appear as BorderLayout.Center
		add(panel);

		setIconImage(icon.getImage());
		setSize(400, 400);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the application? ",
						"EXIT Application", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					panel.capture.release();
					System.exit(0);
				} else if (result == JOptionPane.NO_OPTION) {

					setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}

			}
		});

		setLocationRelativeTo(null);
		setVisible(true);

	}

}
