group modelFile;

modelFile(parameterDefinitions, compartments, transitions) ::= <<
# Compartment model

# Parameter definitions
<parameterDefinitions:parameterDefinition();separator="\n">

# Compartments
<compartments:compartmentDeclaration();separator="\n">

# Transitions
<transitions;separator="\n">
>>

parameterDefinition() ::= <<
<it.key> = <it.value>
>>

compartmentDeclaration() ::= <<
<it.type> <it.name>
>>