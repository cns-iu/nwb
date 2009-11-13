group spemShellModelFile;

spemShellModelFile(parameterDefinitions, transitions) ::= <<
<parameterDefinitions:parameterDefinition();separator="\n">
<transitions;separator="\n">
>>

parameterDefinition() ::= <<
<it.key> = <it.value>
>>