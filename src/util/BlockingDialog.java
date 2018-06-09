package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
public class BlockingDialog {

	/**
	 * <p>The width and height are default 200 pixels.</p>
	 * <p>Example implementation:</p>
	 * <p style="font-family: consolas">
	 * JPanel panel = new JPanel();<br/>
	 * Thread t = Thread.currentThread();<br/>
	 * BlockingDialog.showBlockingDialogue(panel, 200, 200 t);<br/>
	 * synchronized (t) {<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;try {<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;t.wait(); // wait on thread t's monitor<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;} catch (InterruptedException e) {}<br/>
	 * }
	 * </p>
	 * @param content the content to display in the popup.
	 * @param threadBlocked the thread that is calling this method. Use Thread.currentThread().
	 */
	public static void showBlockingDialogue(JPanel content, Thread threadBlocked) {
		showBlockingDialogue(content, 200, 200, threadBlocked);
	}

	/**
	 * <p>Example implementation:</p>
	 * <p style="font-family: consolas">
	 * JPanel panel = new JPanel();<br/>
	 * Thread t = Thread.currentThread();<br/>
	 * BlockingDialog.showBlockingDialogue(panel, 200, 200 t);<br/>
	 * synchronized (t) {<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;try {<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;t.wait(); // wait on thread t's monitor<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;} catch (InterruptedException e) {}<br/>
	 * }
	 * </p>
	 * @param content the content to display in the popup.
	 * @param width the width of the popup, in pixels.
	 * @param height the height of the popup, in pixels.
	 * @param threadBlocked the thread that is calling this method. Use Thread.currentThread().
	 */
	public static void showBlockingDialogue(JPanel content, int width, int height, Thread threadBlocked) {
		JFrame frame = new JFrame();
		frame.add(content);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				synchronized (threadBlocked) {
					threadBlocked.notify();
				}
				frame.dispose();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized (threadBlocked) {
					threadBlocked.notify();
				}
				frame.dispose();
			}
		});
		frame.setSize(width, height);
		frame.setVisible(true);
	}
}
