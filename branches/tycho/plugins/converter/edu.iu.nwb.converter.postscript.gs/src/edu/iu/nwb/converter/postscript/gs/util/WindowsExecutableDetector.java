package edu.iu.nwb.converter.postscript.gs.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

import org.cishell.utilities.process.OutputGobblingProcessRunner;
import org.cishell.utilities.process.ProcessReport;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class WindowsExecutableDetector {
	/**
	 * @return	The first name in {@code candidateExecutableNames} that
	 * 			{@link #isPresentOnSystem(String, String...)} when invoked with the given arguments.
	 * @throws	NoSuchElementException	if no candidate is present on the system.
	 */
	public static String findFirstPresentOnSystem(
			Iterable<String> candidateExecutableNames, final String... arguments) {
		return Iterables.find(
				candidateExecutableNames,
				new Predicate<String>() {
					@Override public boolean apply(String candidate) {
						return isPresentOnSystem(candidate, arguments);
					}
				});
	}
	
	/**
	 * @return	True if {@code executableName} invoked with {@code arguments} exits normally.
	 */
	public static boolean isPresentOnSystem(String executableName, String... arguments) {
		OutputGobblingProcessRunner processRunner =
				new OutputGobblingProcessRunner(
						new ProcessBuilder(
								ImmutableList.copyOf(Iterables.concat(
										ImmutableList.of("cmd", "/C", executableName),
										ImmutableList.copyOf(arguments)))),
						Charset.defaultCharset().toString());		
		
		try {
			ProcessReport report = processRunner.run();			
			return report.isExitNormal();
		} catch (IOException e) {
			return false;
		} catch (InterruptedException e) {
			return false;
		}
	}
}
