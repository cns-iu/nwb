# -*- coding: iso-8859-1 -*-
# $Id: gui.py,v 1.1 2005/10/05 20:19:28 eytanadar Exp $


"""
Creates a Graphic User Interface (GUI) to some of the functions in the
pyNetConv package

@author: Marcio Rosa da Silva
@organization: Systems Biology Group / GBF
@contact: mrs@gbf.de
@license: GPL
"""

from Tkinter import *
from tkFileDialog import askopenfilename, asksaveasfilename
from tkSimpleDialog import *
from network import *
from pajek import *
from cytoscape import *
from gml import *
from expression import *

NET_FORMATS = ['net', 'sif', 'gml']
ATTR_FORMATS = ['clu', 'attr', 'expr', 'none']
NET_FORMAT_NAME    = { "net" : "Pajek .net",
                       "sif" : "Cytoscape .sif",
                       "gml" : "GML"}
ATTR_FORMAT_NAME   = { "clu" : "Pajek .clu",
                       "attr": "Cytoscape node attr.",
                       "expr": "Expression data",
                       "none": "No cluster file"}
GML_FILE_MASK = ("GML file", "*.gml")
NET_FILE_MASK = ("Pajek network", "*.net")
CLU_FILE_MASK = ("Pajek cluster", "*.clu")
SIF_FILE_MASK = ("Cytoscape network", "*.sif")
ATTR_FILE_MASK = ("Cytoscape node attribute", "*")
EXPR_FILE_MASK = ("Expression file", "*")
ALL_FILE_MASK = ("All files", "*")

def searchDictKey(d,value):
    for i in d.keys():
        if d[i] == value:
            return i
    return None

class Application:
    """
    Creates a GUI to use the pyNetConv library for conversion of networks
    """
    def __init__(self):
        self.root = Tk()

    def makeGUI(self):
        self.root.wm_title("pyNetConv - Network Conversion Tool")
        self.initVars()
        self.mainFrame = Frame(self.root)
        self.mainFrame.pack()
        self.topFrame = Frame(self.mainFrame)
        self.topFrame.pack(side=TOP, expand=1, fill='x')
        self.middleFrame = Frame(self.mainFrame)
        self.middleFrame.pack(side=TOP, expand=1, fill='x')
        self.bottomFrame = Frame(self.mainFrame)
        self.bottomFrame.pack(side=TOP, expand=1, fill='x')
        self.importFrame = LabelFrame(self.topFrame, text='Import')
        self.makeEntry(self.importFrame, self.selFormat['netIn'],
                       self.file['netIn'], self.netMenuItems)
        self.makeEntry(self.importFrame, self.selFormat['cluIn'],
                       self.file['cluIn'], self.cluMenuItems)
        self.importFrame.pack(side=LEFT,expand=1,fill='x')
        self.importButton = Button(self.topFrame, text="Import",
                                   command=self.convertIn).pack(side=LEFT,
                                                                fill='y')
                                                       
        self.makeStatsFrame()
        self.statsFrame.pack(side=LEFT,expand=1,fill='x')
        self.exportFrame = LabelFrame(self.bottomFrame, text='Export')
        self.makeEntry(self.exportFrame, self.selFormat['netOut'],
                       self.file['netOut'], self.netMenuItems, save=True)
        self.makeEntry(self.exportFrame, self.selFormat['cluOut'],
                       self.file['cluOut'], self.cluMenuItems, save=True)
        self.exportFrame.pack(side=LEFT,expand=1,fill='x')
        self.exportButton = Button(self.bottomFrame, text="Export",
                                   command=self.convertOut).pack(side=LEFT,
                                                                 fill='y')
        self.root.mainloop()

    def initVars(self):
        self.sel = {}
        self.selFormat = {}
        self.file = {}
        self.nnodes = IntVar()
        self.nedges = IntVar()
        self.nclusters = IntVar()
        self.nexpr = StringVar()
        self.nnodes.set(0)
        self.nedges.set(0)
        self.nclusters.set(0)
        self.nexpr.set('not loaded')
        self.netMenuItems = map(lambda x: NET_FORMAT_NAME[x], NET_FORMATS)
        self.cluMenuItems = map(lambda x: ATTR_FORMAT_NAME[x], ATTR_FORMATS)
        for i in ['netIn', 'netOut', 'cluIn', 'cluOut']:
            self.selFormat[i] = StringVar()
            self.selFormat[i].set('-- select format --')
            self.file[i] = StringVar()
            self.file[i].set('')
        
    def makeEntry(self, frame, sel, var, menuItems, save=False):
        myFrame = Frame(frame)
        def myFunc():
            return self.selFile(var, save)
        Button(myFrame, text='choose file...',
               command=myFunc).pack(side=RIGHT)
        Entry(myFrame, textvariable=var).pack(side=RIGHT)
        self.combo(myFrame, sel, menuItems).pack(side=RIGHT,
                                                 expand=1, fill='x')
        myFrame.pack(side=TOP, anchor='e', expand=1, fill='x')
        return myFrame

    def combo(self, root, sel, items):
        return apply(OptionMenu, (root, sel) + tuple(items))

    def makeStatsFrame(self):
        self.statsFrame = LabelFrame(self.middleFrame, text='Statsistics')
        self.makeStatLine(self.nnodes, 'nodes')
        self.makeStatLine(self.nedges, 'edges')
        self.makeStatLine(self.nclusters, 'clusters')
        self.makeStatLine(self.nexpr, 'Expression data', 1)

    def makeStatLine(self, var, text, inv=False):
        myFrame = Frame(self.statsFrame)
        if not inv:
            Label(myFrame, textvariable=var, width=5,
                  justify=RIGHT, anchor='e').pack(side=LEFT)
        Label(myFrame, text=text, anchor='w').pack(side=LEFT)
        if inv:
            Label(myFrame, textvariable=var,
                  justify=RIGHT, anchor='e').pack(side=LEFT)
        myFrame.pack(side=TOP, anchor='w')

    def selFile(self, var, save):
        if save:
            var.set(asksaveasfilename())
        else:
            var.set(askopenfilename())

    def getConversionParameters(self):
        self.sel['netIn'] = searchDictKey(NET_FORMAT_NAME,
                                          self.selFormat['netIn'].get())
        self.sel['cluIn'] = searchDictKey(ATTR_FORMAT_NAME,
                                          self.selFormat['cluIn'].get())
        self.sel['netOut'] = searchDictKey(NET_FORMAT_NAME,
                                           self.selFormat['netOut'].get())
        self.sel['cluOut'] = searchDictKey(ATTR_FORMAT_NAME,
                                           self.selFormat['cluOut'].get())
        return (self.sel['netIn'],
                self.sel['cluIn'],
                self.sel['netOut'],
                self.sel['cluOut'])


    def convertIn(self):
        (netIn, cluIn, netOut, cluOut) = self.getConversionParameters()
        self.n = Network()
        if netIn == 'net':
            self.n.reset()
            importPajek(self.n, self.file['netIn'].get())
            if cluIn == 'clu':
                importPajekClu(self.n, self.file['cluIn'].get())
        if netIn == 'sif':
            importSIF(self.n, self.file['netIn'].get())
            if cluIn == 'attr':
                importCyAttributes(self.n, self.file['cluIn'].get())
        if netIn == 'gml':
            importGML(self.n, self.file['netIn'].get())
        if cluIn == 'expr':
            importExpression(self.n, self.file['cluIn'].get())
            self.nexpr.set('loaded')
        # Show statistics
        self.nnodes.set(self.n.nnodes)
        self.nedges.set(self.n.nedges)
        if cluIn == 'clu':
            self.nclusters.set(self.n.nclusters)

    def convertOut(self):
        (netIn, cluIn, netOut, cluOut) = self.getConversionParameters()
        if netOut == 'net':
            if netIn == 'gml':
                self.n.pos2pajek()
            exportPajek(self.n, self.file['netOut'].get())
        if cluOut == 'clu':
            if cluIn == 'attr':
                self.n.attributes2Cluster()
            exportPajekClu(self.n, self.file['cluOut'].get())
        if netOut == 'sif':
            exportSIF(self.n, self.file['netOut'].get())
        if netOut == 'gml':
            if netIn == 'net':
                mult = askinteger('Select multiplier',
                                  'Multiply Pajek pos by:') or 1
                self.n.pajek2pos(mult)
            exportGML(self.n, self.file['netOut'].get())
        # Export attribute based on selection of user
        attributes_list = self.n.nodes[0].attributes.keys()
        if cluOut == 'attr':
            attribute2export = []
            #Select Name of attribute to export
            export_attribute_name = askList(self.root,
                                            attributes_list,
                                            "Select attribute",
                                            "Select attribute to export",
                                            0)[0]
            # now get the contents of attribute
            export_attribute = self.n.nodes[0][export_attribute_name]
            # if attribute is a group, select attribute in group
            attribute2export.append(export_attribute_name)
            while isinstance(export_attribute,dict):
                attributes_list = export_attribute.keys()
                #Select Name of attribute to export
                export_attribute_name = askList(self.root,
                                                attributes_list,
                                                "Select attribute",
                                                "Select attribute to export",
                                                0)[0]
                # now get the contents of attribute
                export_attribute = export_attribute[export_attribute_name]
                attribute2export.append(export_attribute_name)
            attribute2export = tuple(attribute2export)
            if export_attribute_name == 'cluster':
                s = askstring('Select cluster text',
                              'Text to add to cluster number (blank for none):') or ''
                self.n.cluster2Attributes(s=s)
            exportCyAttributes(self.n, self.file['cluOut'].get(),
                               attribute2export)
        if cluOut == 'expr':
            attributes_list = self.n.nodes[0].attributes['expression'].keys()
            #Select Name of attribute to export
            export_attributes = askList(self.root,
                                        attributes_list,
                                        "Select attribute",
                                        "Select attribute to export",
                                        1)
            #attribute2export = tuple(export_attribute_name)
            # now get the contents of attribute
            exportCyExpression(self.n, export_attributes,
                               self.file['cluOut'].get())
    

class _AskList(Dialog):
    def __init__(self, master, title, message, list, selectmode):
        self.result = None
        self.message = message
        self.list = list
        self.selectmode = selectmode
        Dialog.__init__(self, master, title)

    def body(self, master):
        Label(master, text=self.message).pack(side=TOP, expand=1, fill='x')
        self.lb = Listbox(master, selectmode=self.selectmode)
        self.lb.pack(side=TOP, expand=1, fill='x')
        for i in self.list:
            self.lb.insert(END, i)
        return self.lb

    def apply(self):
        self.result = []
        for i in self.lb.curselection():
            self.result.append(self.list[int(i)])

def askList(master, list, title, message, multi):
    """Creates a dialog box to select values from a list

    @param master: parent window
    @param title: title of dialog box
    @param list: list to get the values from
    @param message: message to be explain the values in the list
    @param multi: is multi-selection allowed?
    @return: list of selected values
    """
    if multi:
        selectmode = MULTIPLE
    else:
        selectmode = SINGLE
    d = _AskList(master, title, message, list, selectmode)
    return d.result

def startGUI():
    Application().makeGUI()

if __name__ == '__main__':
    startGUI()

