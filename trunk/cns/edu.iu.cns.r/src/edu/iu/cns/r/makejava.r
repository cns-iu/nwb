makeNumericArray <- function(targetObject, javaCode, variableName) {
	targetObjectType <- "Double"

	if (is.integer(targetObject)) {
		targetObjectType = "Integer"
	}

	if (0 == length(targetObject)) {
		javaCode <- paste(javaCode, targetObjectType, " ", variableName, " = null;", sep="");
	} else if (1 == length(targetObject)) {
		javaCode <- paste(javaCode, targetObjectType, " ", variableName, " = new ", targetObjectType, "(", targetObject, ");", sep="");
	} else {
		javaCode <- paste(javaCode, targetObjectType, "[] ", variableName, " = new ", targetObjectType, "[] { ", sep="");

		for (i in 1 : length(targetObject)) {
			javaCode <- paste(javaCode, targetObject[i], sep="");

			if (i < length(targetObject)) {
				javaCode <- paste(javaCode, ", ", sep="");
			}
		}

		javaCode <- paste(javaCode, " };\n", sep="");
	}

	return (javaCode);
}

makeAlphaNumericArray <- function(targetObject, javaCode, variableName) {
	if (0 == length(targetObject)) {
		javaCode <- paste(javaCode, "String ", variableName, " = null;", sep="");
	} else if (1 == length(targetObject)) {
		javaCode <- paste(javaCode, "String ", variablename, " = ", targetObject, ";", sep="");
	} else {
		javaCode <- paste(javaCode, "String[] ", variableName, " = new String[] { ", sep="");

		for (i in 1 : length(targetObject)) {
			javaCode <- paste(javaCode, "\"", targetObject[i], "\"", sep="");

			if (i < length(targetObject)) {
				javaCode <- paste(javaCode, ", ", sep="");
			}
		}

		javaCode <- paste(javaCode, "};\n", sep="");
	}

	return (javaCode);
}

cleanName <- function(name) {
	variableName <- paste(unlist(strsplit(name, "\\.")), collapse="");

	return (variableName);
}

createJavaCodeForObject <- function(targetObject, variableName) {
	javaCode <- "";

	if (is.vector(targetObject) && is.numeric(targetObject)) {
		javaCode <- makeNumericArray(targetObject, javaCode, variableName);
	}

	if (is.vector(targetObject) && !is.numeric(targetObject)) {
    	javaCode <- makeAlphaNumericArray(targetObject, javaCode, variableName);
	}

	if (is.list(targetObject)) {
		elementNames <- names(targetObject);

		for (i in 1 : length(targetObject)) {
			if (is.vector(targetObject[i]) && is.numeric(targetObject[[i]])) {
				javaCode <- makeNumericArray(targetObject[[i]], javaCode, cleanName(elementNames[i]));
			}

			if (is.vector(targetObject[i]) && is.character(targetObject[[i]])) {
				javaCode <- makeAlphaNumericArray(targetObject[[i]], javaCode, cleanName(elementNames[i]));
			}
		}
	}

	return (javaCode);
}

q()	# Hack?