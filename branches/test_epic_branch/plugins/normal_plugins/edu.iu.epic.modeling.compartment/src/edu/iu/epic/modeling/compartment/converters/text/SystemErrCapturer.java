package edu.iu.epic.modeling.compartment.converters.text;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Captures all messages to System.err between calls to startCapturing and stopCapturing
 * in a StringBuffer.
 * The original System.err is considered to be whatever it was at static-initialize-time.
 * <p/>
 * For an instance systemErrCapturer, always use the following control structure to ensure
 * that the original System.err never fails to be restored:
 * <p/>
 * <code>
 * try {
 *     systemErrCapturer.startCapturing();
 *     (code from which to capture System.err messages)
 * } finally {
 *     systemErrCapturer.stopCapturing();
 * }
 * </code>
 */
public class SystemErrCapturer {
	public static final PrintStream ORIGINAL_SYSTEM_ERR = System.err;

	private StringBuffer buffer;

	public SystemErrCapturer() {
		buffer = new StringBuffer();
	}


	public void startCapturing() {
		PrintStream capturingPrintStream = new PrintStream(new OutputStream() {
			@Override
			public void write(byte[] b) { getBuffer().append(new String(b)); }

			@Override
			public void write(int b) { getBuffer().append(new String(new byte[]{(byte) b})); }
		});

		System.setErr(capturingPrintStream);
	}

	public void stopCapturing() {
		System.setErr(ORIGINAL_SYSTEM_ERR);
	}

	public boolean isEmpty() {
		return (buffer.length() == 0);
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public String getCapturedMessages() {
		return buffer.toString();
	}
}
