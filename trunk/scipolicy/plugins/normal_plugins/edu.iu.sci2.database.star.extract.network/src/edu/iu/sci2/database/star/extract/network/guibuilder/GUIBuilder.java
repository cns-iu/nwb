package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.HashMap;
import java.util.Map;

import org.cishell.service.database.Database;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.iu.cns.shared.utilities.swt.GUIBuilderUtilities;
import edu.iu.sci2.database.star.extract.network.CoreTableDescriptor;
import edu.iu.sci2.database.star.extract.network.LeafTableDescriptor;
import edu.iu.sci2.database.star.extract.network.StarDatabase;

public class GUIBuilder {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 800;

	public static void createGUI(
			String windowTitle, int windowWidth, int windowHeight, StarDatabase starDatabase) {
		Display display = GUIBuilderUtilities.createDisplay();
		Shell shell =
			GUIBuilderUtilities.createShell(display, windowTitle, windowWidth, windowHeight);
		starDatabase.getCoreTableDescriptor().createLeafSelectionInputField(shell, SWT.NONE, true);

		runGUI(display, shell, windowHeight);
	}

	private static void runGUI(
			Display display,
			Shell shell,
			int windowHeight) {
		GUIBuilderUtilities.openShell(shell, windowHeight, true);
    	GUIBuilderUtilities.swtLoop(display, shell);
	}

	public static void main(String[] arguments) {
		Database database = null;
		Map<String, String> columnNamesToTypes = createColumnNamesToTypes();
		CoreTableDescriptor coreTableDescriptor = new CoreTableDescriptor(columnNamesToTypes);
		Map<String, LeafTableDescriptor> leafTableDescriptorsByName =
			new HashMap<String, LeafTableDescriptor>();
		StarDatabase starDatabase =
			new StarDatabase(database, coreTableDescriptor, leafTableDescriptorsByName);

		createGUI("Extract Co-Occurrence Network", WINDOW_WIDTH, WINDOW_HEIGHT, starDatabase);
	}

	private static Map<String, String> createColumnNamesToTypes() {
		Map<String, String> columnNamesToTypes = new HashMap<String, String>();
		columnNamesToTypes.put("CITES", "INTEGER");

		return columnNamesToTypes;
	}
}