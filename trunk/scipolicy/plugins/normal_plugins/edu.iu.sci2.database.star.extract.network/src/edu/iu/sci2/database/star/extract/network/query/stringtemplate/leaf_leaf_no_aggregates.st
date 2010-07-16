group leaf_leaf_no_aggregates;

nodeQuery(
		leafTable1Name,
		leafTable1Name_SentenceCased,
		leafTable1Name_SentencedCased_WithUnderscorePostfix,
		leafTable2Name,
		leafTable2Name_SentencedCased,
		leafTable2Name_SentencedCased_WithUnderscorePostfix,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('$leafTable1Name_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTable1Name$.PK), $idSize$) AS ID,
		CHAR ('$leafTable1Name_SentenceCased$', $entityTypeSize$) AS "Entity_Type",
		CHAR ($leafTable1Name$.$leafTable1Name$, $labelSize$) AS "Label"
	FROM
		$leafTable1Name$
	JOIN
		$coreTableName$_TO_$leafTable1Name$
			ON $leafTable1Name$.PK = $coreTableName$_TO_$leafTable1Name$.$coreTableName$_TO_$leafTable1Name$_$leafTable1Name$_FOREIGN_KEY
UNION
	SELECT
		CHAR ('$leafTable2Name_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTable2Name$.PK), $idSize$) AS ID,
		CHAR ('$leafTable2Name_SentencedCased$', $entityTypeSize$) AS "Entity_Type",
		CHAR ($leafTable2Name$.$leafTable2Name$, $labelSize$) AS "Label"
	FROM
		$leafTable2Name$
	JOIN
		$coreTableName$_TO_$leafTable2Name$
			ON $leafTable2Name$.PK = $coreTableName$_TO_$leafTable2Name$.$coreTableName$_TO_$leafTable2Name$_$leafTable2Name$_FOREIGN_KEY
>>

edgeQuery(
		leafTable1Name,
		leafTable1Name_SentencedCased_WithUnderscorePostfix,
		leafTable2Name,
		leafTable2Name_SentencedCased_WithUnderscorePostfix,
		coreTableNameColumns,
		idSize) ::= <<
SELECT
	CHAR ('$leafTable1Name_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTable1Name$.PK), $idSize$) AS TARGET,
	CHAR ('$leafTable2Name_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTable2Name$.PK), $idSize$) AS SOURCE,
	$coreTableNameColumns$,
	COUNT (*) AS "Weight"
FROM
	$leafTable1Name$
JOIN
	$coreTableName$_TO_$leafTable1Name$
		ON $leafTable1Name$.PK = $coreTableName$_TO_$leafTable1Name$.$coreTableName$_TO_$leafTable1Name$_$leafTable1Name$_FOREIGN_KEY
JOIN
	$coreTableName$
		ON $coreTableName$_TO_$leafTable1Name$.$coreTableName$_TO_$leafTable1Name$_$coreTableName$_FOREIGN_KEY = $coreTableName$.PK
JOIN
	$coreTableName$_TO_$leafTable2Name$
		ON $coreTableName$.PK = $coreTableName$_TO_$leafTable2Name$_$coreTableName$_FOREIGN_KEY
JOIN
	$leafTable2Name$
		ON $coreTableName$_TO_$leafTable2Name$.$coreTableName$_TO_$leafTable2Name$_$leafTable2Name$_FOREIGN_KEY = $leafTable2Name$.PK
GROUP BY
	$leafTable1Name$.PK,
	$leafTable2Name$.PK,
	$coreTableNameColumns$
>>
