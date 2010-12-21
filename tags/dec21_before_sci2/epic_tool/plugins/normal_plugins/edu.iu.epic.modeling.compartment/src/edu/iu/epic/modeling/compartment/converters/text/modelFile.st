group modelFile;

modelFile(parameterDefinitions, compartments, transitions) ::= <<
# Compartmental epidemics model

# Parameter definitions
<parameterDefinitions:parameterDefinition();separator="\n">

# Transitions
<transitions;separator="\n">
>>

parameterDefinition() ::= <<
<it.key> = <it.value>
>>