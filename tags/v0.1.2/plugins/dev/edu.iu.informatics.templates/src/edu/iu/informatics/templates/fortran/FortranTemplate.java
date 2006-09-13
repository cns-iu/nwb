/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu/).
 * 
 * Created on Oct 12, 2005 at Indiana University.
 */
package edu.iu.informatics.templates.fortran;

import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import edu.iu.informatics.templates.TemplatesPlugin;
import edu.iu.iv.templates.executable.ExecutableTemplate;

/**
 * 
 * @author Bruce Herr
 */
public class FortranTemplate extends ExecutableTemplate {
    public static final String KEY_SUPPORTS_STATEMENT = "supportsStatement";
    public static final String KEY_EXTRA_IMPORT_STATEMENTS = "extraImportStatements";
    public static final String KEY_EXTRA_RUNNER_COMMANDS = "extraRunnerCommands";
    
    public FortranTemplate() { 
        super("fortran");
    }
    
    /**
     * @see org.eclipse.pde.ui.templates.AbstractTemplateSection#updateModel(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void updateModel(IProgressMonitor monitor) throws CoreException {
        addDependency("edu.iu.informatics.shared");
        super.updateModel(monitor);
    }
    
    protected void generateFiles(IProgressMonitor monitor) throws CoreException {
        int algType = ((Integer)getParameterValue(KEY_ALGORITHM_TYPE)).intValue();
        
        if (ALGORITHM_TYPES[algType] == ALG_TYPE_MODELING || 
                ALGORITHM_TYPES[algType] == ALG_TYPE_OTHER) {
            setValue(KEY_SUPPORTS_STATEMENT,"return true;");
            setValue(KEY_EXTRA_IMPORT_STATEMENTS,"");
            setValue(KEY_EXTRA_RUNNER_COMMANDS,"makeInputFile(runner, \"inputfile\");");
        } else {
            setValue(KEY_SUPPORTS_STATEMENT,"return model.getData() instanceof FileResourceDescriptor;");
            setValue(KEY_EXTRA_IMPORT_STATEMENTS,"import edu.iu.iv.core.persistence.FileResourceDescriptor;");
            setValue(KEY_EXTRA_RUNNER_COMMANDS,"saveDataModel(runner, \"network\");\n\t\tmakeInputFile(runner, \"inputfile\");");
        }
        
        super.generateFiles(monitor);
    }
    
    /**
     * Returns the install URL of the plug-in that contributes this template.
     * 
     * @return the install URL of the contributing plug-in
     */
    protected URL getInstallURL() {
        return TemplatesPlugin.getDefault().getBundle().getEntry("/");
    }

    /**
     * @see org.eclipse.pde.ui.templates.AbstractTemplateSection#getPluginResourceBundle()
     */
    protected ResourceBundle getPluginResourceBundle() {
        return TemplatesPlugin.getDefault().getResourceBundle();
    }
}
