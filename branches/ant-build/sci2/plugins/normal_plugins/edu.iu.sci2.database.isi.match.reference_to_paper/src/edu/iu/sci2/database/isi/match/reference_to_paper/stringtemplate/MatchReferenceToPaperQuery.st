group match_reference_to_paper_group;

matchReferenceToPaperQuery(
	referenceTableName,
	referencePK,
	referenceAuthorFK,
	referencePageNumber,
	referencePaperFK,
	referenceSource,
	referenceVolume,
	referenceYear,
	documentTableName,
	documentPK,
	documentBeginningPage,
	documentFirstAuthorFK,
	documentSource,
	documentVolume,
	documentYear) ::= <<

UPDATE
	<referenceTableName>
SET
	<referenceTableName>.<referencePaperFK> = (SELECT <documentTableName>.<documentPK>
	                                           FROM <documentTableName>
	                                           WHERE
	                                             <referenceTableName>.<referenceAuthorFK> = <documentTableName>.<documentFirstAuthorFK> AND
	                                             <referenceTableName>.<referencePageNumber> = <documentTableName>.<documentBeginningPage> AND
	                                             <referenceTableName>.<referenceSource> = <documentTableName>.<documentSource> AND
	                                             <referenceTableName>.<referenceVolume> = <documentTableName>.<documentVolume> AND
	                                             <referenceTableName>.<referenceYear> = <documentTableName>.<documentYear>
	                                             )
WHERE
	1 = ALL (
	SELECT
		count(<documentTableName>.<documentPK>)
	FROM  <documentTableName>
	WHERE
			<referenceTableName>.<referenceAuthorFK> = <documentTableName>.<documentFirstAuthorFK> AND
			<referenceTableName>.<referencePageNumber> = <documentTableName>.<documentBeginningPage> AND
			<referenceTableName>.<referenceSource> = <documentTableName>.<documentSource> AND
			<referenceTableName>.<referenceVolume> = <documentTableName>.<documentVolume> AND
			<referenceTableName>.<referenceYear> = <documentTableName>.<documentYear> AND
			<referenceTableName>.<referencePaperFK> IS NULL AND
			<referenceTableName>.<referenceAuthorFK> IS NOT NULL AND
			<referenceTableName>.<referencePageNumber> IS NOT NULL AND
			<referenceTableName>.<referenceSource> IS NOT NULL AND
			<referenceTableName>.<referenceVolume> IS NOT NULL AND
			<referenceTableName>.<referenceYear> IS NOT NULL
)
>>

selectOvermatchedReferencesQuery(
	referenceTableName,
	referencePK,
	referenceAuthorFK,
	referencePageNumber,
	referencePaperFK,
	referenceRaw,
	referenceSource,
	referenceVolume,
	referenceYear,
	documentTableName,
	documentPK,
	documentBeginningPage,
	documentFirstAuthorFK,
	documentSource,
	documentVolume,
	documentYear) ::= <<

SELECT
	<referenceTableName>.<referenceRaw>, count(<documentTableName>.<documentPK>) as total
FROM <referenceTableName>, <documentTableName>
WHERE
	<referenceTableName>.<referenceAuthorFK> = <documentTableName>.<documentFirstAuthorFK> AND
	<referenceTableName>.<referencePageNumber> = <documentTableName>.<documentBeginningPage> AND
	<referenceTableName>.<referenceSource> = <documentTableName>.<documentSource> AND
	<referenceTableName>.<referenceVolume> = <documentTableName>.<documentVolume> AND
	<referenceTableName>.<referenceYear> = <documentTableName>.<documentYear> AND
	<referenceTableName>.<referencePaperFK> IS NULL AND
	<referenceTableName>.<referenceAuthorFK> IS NOT NULL AND
	<referenceTableName>.<referencePageNumber> IS NOT NULL AND
	<referenceTableName>.<referenceSource> IS NOT NULL AND
	<referenceTableName>.<referenceVolume> IS NOT NULL AND
	<referenceTableName>.<referenceYear> IS NOT NULL
GROUP BY <referenceTableName>.<referencePK>, <referenceTableName>.<referenceRaw>
HAVING count(<documentTableName>.<documentPK>) > 1
>>