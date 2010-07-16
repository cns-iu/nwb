group leaf_core_no_aggregates;

nodeQuery(
		leafTableName,
		leafTableName_SentenceCased,
		leafTableName_SentencedCased_WithUnderscorePostfix,
		coreTableNameColumns_ForLeaf,
		coreTableName,
		coreTableName_SentenceCased,
		coreTableName_SentencedCased_WithUnderscorePostfix,
		coreTableNameColumns_ForCore,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('$leafTableName_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTableName$.PK), $idSize$) AS ID,
		CHAR ('$leafTableName_SentenceCased$', $entityTypeSize$) AS "Entity_Type",
		CHAR ($leafTableName$.$leafTableName$, $labelSize$) AS "Label",
		$coreTableNameColumns_ForLeaf$
	FROM
		$leafTableName$
	JOIN
		$coreTableName$_TO_$leafTableName$
			ON $leafTableName$.PK = $coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$leafTableName$_FOREIGN_KEY
UNION
	SELECT
		CHAR ('$coreTableName_SentencedCased_WithUnderscorePostfix$' || CHAR ($coreTableName$.PK), $idSize$) AS ID,
		CHAR ('$coreTableName_SentenceCased$', $entityTypeSize$) AS "Entity_Type",
		CHAR ('$coreTableName$ ' || CHAR ($coreTableName$.PK), $labelSize) AS "Label",
		$coreTableNameColumns_ForCore$
	FROM
		$coreTableName$
>>

edgeQuery(
		leafTableName,
		coreTableName,
		leafTableName_SentencedCased_WithUnderscorePostfix,
		coreTableName_SentenceCased,
		leafEdgeEnd,
		coreEdgeEnd,
		coreTableNameColumns,
		idSize) ::= <<
SELECT
	CHAR ('$leafTableName_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTableName$.PK), $idSize$) AS $leafEdgeEnd$,
	CHAR ('$coreTableName_SentenceCased$' || CHAR ($coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$coreTableName$_FOREIGN_KEY), $idSize$) AS $coreEdgeEnd$,
	$coreTableNameColumns$,
	COUNT (*) AS "Weight"
FROM
	$leafTableName$
JOIN
	$coreTableName$_TO_$leafTableName$
		ON $leafTableName$.PK = $coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$leafTableName$_FOREIGN_KEY
GROUP BY
	$leafTableName$.PK,
	$coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$coreTableName$_FOREIGN_KEY,
	$coreTableNameColumns$
>>
