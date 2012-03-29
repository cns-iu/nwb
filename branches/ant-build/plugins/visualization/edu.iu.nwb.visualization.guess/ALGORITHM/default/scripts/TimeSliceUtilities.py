from au.com.bytecode.opencsv import *
from java.io import BufferedReader
from java.io import FileInputStream
from java.io import InputStreamReader
from java.io import PrintWriter


ORIGINAL_LABEL_NOT_FOUND_ERROR_MESSAGE = \
    'The field "originallabel" was expected in the input node position file but not found.'
X_NOT_FOUND_ERROR_MESSAGE = \
    'The field "x" was expected in the input node position file but not found.'
Y_NOT_FOUND_ERROR_MESSAGE = \
    'The field "y" was expected in the input node position file but not found.'
IMPORT_NODE_POSITIONS_TITLE = 'Import Node Positions Error'
CAMERA_SCALE_ROW = "[cameraScale]"
CAMERA_TRANSLATE_ROW = "[cameraTranslate]"

# 0
def setupExportNodePositionsMenuItem():
    exportNodePositionsMenuItem = JMenuItem("Export Node Positions", actionPerformed=exportNodePositionsAction)
    fileMenu = getFileMenu()
    fileMenu.insert(exportNodePositionsMenuItem, 3)

# 0
def setupImportNodePositionsMenuItem():
    importNodePositionsMenuItem = JMenuItem("Import Node Positions", actionPerformed=importNodePositionsAction)
    fileMenu = getFileMenu()
    fileMenu.insert(importNodePositionsMenuItem, 3)

# 1
def exportNodePositionsAction(event):
    chosenFile = showSaveAsDialog()
    
    if chosenFile is not None:
        exportNodePositions(chosenFile)

# 1
def importNodePositionsAction(event):
    chosenFile = showOpenDialog()
    
    if chosenFile is not None:
        importNodePositions(chosenFile)

# 1
def getFileMenu():
    menuBar = ui.getGMenuBar()
    menus = menuBar.getComponents()
    
    # Find the File menu.
    for menu in menus:
        if menu.text == "File":
            return menu
    
    return None

# 2
def showSaveAsDialog():
    title = "Save As"
    fileChooser = JFileChooser()
    fileChooser.setDialogTitle(title)
    
    while true:
        saveResult = fileChooser.showSaveDialog(None)
        
        if saveResult == JFileChooser.CANCEL_OPTION or saveResult == JFileChooser.ERROR_OPTION:
            return None
        else:
            chosenFile = fileChooser.getSelectedFile()
            
            if chosenFile.exists():
                message = chosenFile.getName() + " already exists.\nDo you want to replace it?"
                confirmOverwriteResult = JOptionPane.showConfirmDialog(
                    None, message, title, JOptionPane.YES_NO_CANCEL_OPTION)
                
                if confirmOverwriteResult == JOptionPane.YES_OPTION:
                    return chosenFile
                elif confirmOverwriteResult == JOptionPane.CANCEL_OPTION:
                    return None
                # TODO: Comment about NO behavior.
            else:
                return chosenFile

# 2
def showOpenDialog():
    title = "Open"
    fileChooser = JFileChooser()
    fileChooser.setDialogTitle(title)
    openResult = fileChooser.showOpenDialog(None)
    
    if openResult != JFileChooser.CANCEL_OPTION and openResult != JFileChooser.ERROR_OPTION:
        return fileChooser.getSelectedFile()
    else:
        return None

# 2
def exportNodePositions(chosenFile):
    writer = CSVWriter(BufferedWriter(FileWriter(chosenFile)))
    # TODO: Make constants
    header = ["originallabel", "x", "y"]
    writer.writeNext(header)
    writer.writeNext(formCameraScaleRow())
    writer.writeNext(formCameraTranslateRow())
    
    for node in g.nodes:
        writer.writeNext(getItemCells(node, header))
    
    writer.close()

# 2
def importNodePositions(chosenFile):
    reader = CSVReader(BufferedReader(InputStreamReader(FileInputStream(chosenFile))))
    header = reader.readNext()
    # TODO: Validating?
    # TODO: Make constants
    originalLabelIndex = find(header, "originallabel")
    xIndex = find(header, "x")
    yIndex = find(header, "y")
    
    if originalLabelIndex is None:
        JOptionPane.showMessageDialog(
            None,
            ORIGINAL_LABEL_NOT_FOUND_ERROR_MESSAGE,
            IMPORT_NODE_POSITIONS_TITLE,
            JOptionPane.ERROR_MESSAGE)
        
        return None
    
    if xIndex is None:
        JOptionPane.showMessageDialog(
            None,
            X_NOT_FOUND_ERROR_MESSAGE,
            IMPORT_NODE_POSITIONS_TITLE,
            JOptionPane.ERROR_MESSAGE)
        
        return None
    
    if yIndex is None:
        JOptionPane.showMessageDialog(
            None,
            Y_NOT_FOUND_ERROR_MESSAGE,
            IMPORT_NODE_POSITIONS_TITLE,
            JOptionPane.ERROR_MESSAGE)
        
        return None
    
    nodesByOriginalLabel = mapOriginalLabelsToNodes()
    
    scale = None
    translate = None
    line = reader.readNext()
    
    while line is not None:
        originalLabelAttribute = line[originalLabelIndex]
        xAttribute = line[xIndex]
        yAttribute = line[yIndex]
        
        if originalLabelAttribute == CAMERA_SCALE_ROW:
            scale = [Double(xAttribute), Double(yAttribute)]
        elif originalLabelAttribute == CAMERA_TRANSLATE_ROW:
            translate = [Double(xAttribute), Double(yAttribute)]
        elif nodesByOriginalLabel.get(originalLabelAttribute) is not None:
            nodesByOriginalLabel[originalLabelAttribute].x = Double(xAttribute)
            nodesByOriginalLabel[originalLabelAttribute].y = Double(yAttribute)
        
        line = reader.readNext()
    
    reader.close()
    
    if scale is not None or translate is not None:
        camera.getViewTransformReference().setToIdentity()
    
    if scale is not None:
        camera.getViewTransformReference().scale(scale[0], scale[1])
    
    if translate is not None:
        camera.getViewTransformReference().translate(translate[0], translate[1])

# 3
def formCameraScaleRow():
    cameraTransform = camera.getViewTransformReference()
    
    return [CAMERA_SCALE_ROW, str(cameraTransform.getScaleX()), str(cameraTransform.getScaleY())]

# 3
def formCameraTranslateRow():
    cameraTransform = camera.getViewTransformReference()
    
    return [CAMERA_TRANSLATE_ROW, str(cameraTransform.getTranslateX()), str(cameraTransform.getTranslateY())]

# 3
def getItemCells(item, header):
    itemCells = []
    
    for attributeName in header:
        attribute = getattr(item, attributeName)
        
        if attribute is not None:
            itemCells = itemCells + [str(attribute)]
        else:
            itemCells = itemCells + [""]
    
    return itemCells

# 3
def find(sequence, target):
    for ii in range(sequence.size()):
        element = sequence[ii]
        
        if element == target:
            return ii
    
    return None

# 3
def mapOriginalLabelsToNodes():
    nodesByOriginalLabel = {}
    
    for node in g.nodes:
        nodesByOriginalLabel[node.originallabel] = node
    
    return nodesByOriginalLabel