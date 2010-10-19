group simulatorModelFile;

simulatorModelFile(parameterDefinitions, transitions) ::= <<
<parameterDefinitions:parameterDefinition();separator="\n">
<transitions;separator="\n">
>>

parameterDefinition() ::= <<
<it.key> = <it.value>
>>