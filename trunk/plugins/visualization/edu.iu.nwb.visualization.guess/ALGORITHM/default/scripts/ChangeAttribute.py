def changeAttribute_General(selectedIndex, graphModifier):
    # all is selected
    if selectedIndex == 0:
        changeAttribute_AllObjects(graphModifier)
    # set all nodes to something
    elif selectedIndex == 1:
        changeAttribute_AllNodes(graphModifier)
    # set all edges to something
    elif selectedIndex == 2:
        changeAttribute_AllEdges(graphModifier)
    else:
        changeAttribute_AllBasedOn(selectedIndex, graphModifier)

def changeAttribute_AllObjects(graphModifier):
    objects = "all objects"
    # apply attribute change to all colours
    if graphModifier.currentAction == "color":
        # change all node colors
        g.nodes.color = graphModifier.currentColor
        
        # change all edge colors
        g.edges.color = graphModifier.currentColor
    elif graphModifier.currentAction == "node_style":
        g.nodes.style = image_style # a hack to fix problem in switching between styles in GUESS
        g.nodes.style = graphModifier.currentNodeStyle
    elif graphModifier.currentAction == "show":
        # show all nodes
        g.nodes.visible = true
        
        # show all edges
        g.edges.visible = true
    elif graphModifier.currentAction == "hide":
        # hide all nodes
        g.nodes.visible = false
        
        # hide all edges
        g.nodes.visible = false
    elif graphModifier.currentAction == "size":
        # set the width and height of all nodes
        g.nodes.width = graphModifier.widthSlider.getValue()
        g.nodes.height = graphModifier.heightSlider.getValue()
        g.edges.width = graphModifier.widthSlider.getValue()
    elif graphModifier.currentAction == "show label":
        # show all labels of nodes
        g.nodes.labelVisible = true
        
        # show all labels of edges
        g.edges.labelVisible = true
        
        # repaint the graph
        v.repaint()
    elif graphModifier.currentAction == "hide label":
        # hide all labels of nodes
        g.nodes.labelVisible = false
        
        # hide all labels of edges
        g.edges.labelVisible = false
        
        # repaint the graph
        v.repaint()
    elif graphModifier.currentAction == "change label":
        # change the label of all nodes and edges
        all = g.nodes + g.edges
        for i in all:
            t[0] = i
            setLabel(t)

def changeAttribute_AllNodes(graphModifier):
    objects = "all nodes"
    if graphModifier.currentAction == "color":
        g.nodes.color = graphModifier.currentColor
    elif graphModifier.currentAction == "node_style":
        g.nodes.style = image_style
        g.nodes.style = graphModifier.currentNodeStyle
    elif graphModifier.currentAction == "show":
        g.nodes.visible = true
    elif graphModifier.currentAction == "hide":
        g.nodes.visible = false
    elif graphModifier.currentAction == "size":
        g.nodes.width = graphModifier.widthSlider.getValue()
        g.nodes.height = graphModifier.heightSlider.getValue()
    elif graphModifier.currentAction == "show label":
        g.nodes.labelVisible = true
    elif graphModifier.currentAction == "hide label":
        g.nodes.labelVisible = false
    elif graphModifier.currentAction == "change label":
        for i in g.nodes:
            t[0] = i
            setLabel(t)

def changeAttribute_AllEdges(graphModifier):
    objects = "all edges"
    if graphModifier.currentAction == "color":
        g.edges.color = graphModifier.currentColor
    elif graphModifier.currentAction == "node_style":
        g.nodes.style = image_style
        g.nodes.style = graphModifier.currentNodeStyle
    elif graphModifier.currentAction == "show":
        g.edges.visible = true
    elif graphModifier.currentAction == "hide":
        g.edges.visible = false
    elif graphModifier.currentAction == "size":
        g.edges.width = graphModifier.widthSlider.getValue()
    elif graphModifier.currentAction == "show label":
        g.edges.labelVisible = true
    elif graphModifier.currentAction == "hide label":
        g.edges.labelVisible = false
    elif graphModifier.currentAction == "change label":
        for i in g.edges:
            t[0] = i
            setLabel(t)

def changeAttribute_AllBasedOn(selectedIndex, graphModifier):
    indexList = ""
    propertyIndex = graphModifier.propertyBox.getSelectedIndex()
    propertyList = ""
    propertyDictionary = ""
    
    if selectedIndex == 3:
        objects = "all nodes whose property: "
        propertyList = graphModifier.nodeProperties
        propertyDictionary = nodeProperties
        indexList = nodeIndex
    else:
        objects = "all edges whose property: "
        propertyList = graphModifier.edgeProperties
        propertyDictionary = edgeProperties
        indexList = edgeIndex
    
    property = propertyList[propertyIndex]
    propertyType = propertyDictionary[property]
    currentOperatorIndex = graphModifier.operatorBox.getSelectedIndex()
    currentOperator = graphModifier.numberOperators[currentOperatorIndex]
    currentValue = ""
    
    if propertyType == type("string"):
        if nodePropertyValues.has_key(property):
            currentValue = graphModifier.valueBox.getSelectedItem()
        elif edgePropertyValues.has_key(property):
            currentValue = graphModifier.valueBox.getSelectedItem()
        else:
            raise AttributeError
    elif propertyType == type(1) or propertyType == type(1.5):
        currentValue = float(graphModifier.valueBoxValue.theText())
    
    value = currentValue
    operator = currentOperator
    # check to see which nodes or edges fulfill that property
    for i in indexList:
        objectValue = ""
        
        if selectedIndex == 3:
            t[0] = g.nodes[i[1]]
            objectValue = getattr(g.nodes[i[1]], property)
#            objectValue = eval("g.nodes[" + str(i[1]) + "]." + property)
        else:
            t[0] = g.edges[i[1]]
            objectValue = getattr(g.edges[i[1]], property)
#            objectValue = eval("g.edges[" + str(i[1]) + "]." + property)
        
        if currentOperator == graphModifier.numberOperators[0] and objectValue == currentValue:
            method[graphModifier.currentAction](t)
        elif currentOperator == graphModifier.numberOperators[1] and objectValue != currentValue:
            method[graphModifier.currentAction](t)
        elif currentOperator == graphModifier.numberOperators[2] and objectValue <= currentValue:
            method[graphModifier.currentAction](t)
        elif currentOperator == graphModifier.numberOperators[3] and objectValue < currentValue:
            method[graphModifier.currentAction](t)
        elif currentOperator == graphModifier.numberOperators[4] and objectValue >= currentValue:
            method[graphModifier.currentAction](t)
        elif currentOperator == graphModifier.numberOperators[5] and objectValue > currentValue:
            method[graphModifier.currentAction](t)
