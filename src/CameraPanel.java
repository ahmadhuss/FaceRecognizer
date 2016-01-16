import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class CameraPanel extends JPanel implements Runnable {

	// interface Runnable implements that override Run method for reading image
	// in new JPanel Thread
	BufferedImage image;

	VideoCapture capture;

	private JButton b1;

	private int count;

	// Cascade class file
	CascadeClassifier face;

	// create Mat of rectangle these used for detection faces
	MatOfRect facedetections;

	// constructor
	public CameraPanel() {
		count = 0;

		// return floating point values high processing
		// face = new
		// CascadeClassifier("./res/haarcascade_frontalface_alt.xml");
		// return integer values low processing
		face = new CascadeClassifier("./res/lbpcascade_frontalface.xml");

		facedetections = new MatOfRect();

		b1 = new JButton("Take Snap");
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// create a new file with name snap1
				File file = new File("snap1.png");
				int i = 0;

				// loop to check if file is exist than create with new name i++
				while (file.exists()) {
					i++;
					file = new File("snap1" + i + " .png");
				}

				try {
					ImageIO.write(image, "png", file);
					JOptionPane.showMessageDialog(null, "Image is saved in your project Directory");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		});

		add(b1);

	}

	@Override
	public void run() {

		System.loadLibrary("opencv_java310");

		capture = new VideoCapture(0);

		Mat webImage = new Mat();

		if (!capture.isOpened()) {

			System.out.println("Sorry Not Connected");
		}

		else {
			System.out.println("Connected Successfully and Camera Open Reading Start");

			// reading image infinite loop start
			while (true) {

				// read the image with camera and store values in variable
				// webImage
				capture.read(webImage);

				// this if statement is always true because camera is reading
				// images
				if (!webImage.empty()) {

					// Frame presentation getWindowAncestor like this
					JFrame topframe = (JFrame) SwingUtilities.getWindowAncestor(this);
					topframe.setSize(webImage.width() + 40, webImage.height() + 110);

					// convert to buffered image
					MatToBufferedImage(webImage);

					// now it's using xml file to find the faces and putting
					// them into face detections

					face.detectMultiScale(webImage, facedetections);

					// repaint : swing clear the graphics automatically and it
					// repaint every frame by frame because of while loop
					repaint();

				}

			}

		} // else closed

	}// Thread Process close

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			System.out.println("!! The JPanel Image is null !!");
			return;

		}

		g.drawImage(image, 10, 40, image.getWidth(), image.getHeight(), null);

		g.setColor(Color.BLUE);
		// Rect is openCV class and it fetch face in rectangles in the form of Array
		for (Rect rect : facedetections.toArray()) {
			// paint component print face rectangles
			g.drawRect(rect.x + 10, rect.y + 40, rect.width, rect.height);
		}

		g.setFont(new Font("arial", 2, 20));
		g.setColor(Color.BLACK);
		g.drawString("Camera is processing Frame by Frame [Frame: " + (count++) + " ]", 55, 500);
	}

	public void MatToBufferedImage(Mat matBGR) {

		// get some values
		int width = matBGR.width();

		int height = matBGR.height();

		// now some channels

		int channels = matBGR.channels();

		byte[] source = new byte[width * height * channels];

		matBGR.get(0, 0, source);

		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		final byte[] target = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

		System.arraycopy(source, 0, target, 0, source.length);

	}

}
