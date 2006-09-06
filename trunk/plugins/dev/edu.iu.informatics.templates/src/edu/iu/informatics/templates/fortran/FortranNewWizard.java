/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu/).
 * 
 * Created on Oct 12, 2005 at Indiana University.
 */
package edu.iu.informatics.templates.fortran;

import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;

/**
 * 
 * @author Bruce Herr
 */
public class FortranNewWizard extends NewPluginTemplateWizard {
    /**
     * Initializes this Wizard
     * 
     * @param data The IFieldData object representing the data captured
     * by the new Plug-in Project wizard prior to the usage of this Wizard
     */
    public void init(IFieldData data) {
        super.init(data);
        setWindowTitle("IVC Fortran-based Algorithm");
        setHelpAvailable(false);
    }
    
    /**
     * @see org.eclipse.pde.ui.templates.NewPluginTemplateWizard#createTemplateSections()
     */
    public ITemplateSection[] createTemplateSections() {
        return new ITemplateSection[] { new FortranTemplate() };
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish() {
        return true;
    }
}
