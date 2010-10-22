package edu.iu.cns.r.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import bsh.EvalError;

public class JavaCodeStreamReader extends Thread {
	private InputStream inputStream;
	private bsh.Interpreter javaInterpreter;

	public JavaCodeStreamReader(InputStream inputStream, bsh.Interpreter javaInterpreter) {
		this.inputStream = inputStream;
		this.javaInterpreter = javaInterpreter;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));
			String line = null;

			while ((line = reader.readLine()) != null) {
				this.javaInterpreter.eval(line);
			}
		} catch (EvalError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}