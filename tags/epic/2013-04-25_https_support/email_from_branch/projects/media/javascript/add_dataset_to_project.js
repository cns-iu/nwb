// TODO: Finish this functionality!

var TOTAL_FORMS_ID = "id_add_dataset-TOTAL_FORMS";
var BASE_INPUT_ID_PART_1 = "id_add_dataset-";
var BASE_INPUT_ID_PART_2 = "-dataset_url";
var BASE_REMOVE_INPUT_FIELD_ID = "remove_";
var BASE_BREAK_ID = "break_";
var ADD_ANOTHER_DATASET_URL__LINK__CONTAINER__ID =
	"add_another_dataset__link__container__id";
var ADD_ANOTHER_DATASET_URL__LINK__ID = "add_another_dataset_url__link__id";

var inputFieldCount = 1;
var nextInputFieldID = 1;
var container = null;
var lastSibling = null;
var totalForms = null;

function initialize()
{
	container = getContainer();
	lastSibling = insert_AddAnotherDatasetURL_Link();
	totalForms = document.getElementById(TOTAL_FORMS_ID);
	inputFieldCount = parseInt(totalForms.value);
	nextInputFieldID = inputFieldCount;
	
	var firstInputFieldID = form_InputFieldID(0);
	var firstInputField = document.getElementById(firstInputFieldID);
	
    insert_RemoveInputField_Link(firstInputFieldID);
    insert_PageBreak(firstInputFieldID);
}

function create_AddDatasetURL_Form()
{
    var newInputField = insert_InputField();
    insert_RemoveInputField_Link(newInputField.id);
    insert_PageBreak(newInputField.id);
    
    incrementDatasetURLCount();
}

function removeInputField(inputFieldID)
{
    var removeNode = document.getElementById(inputFieldID);
    removeNode.parentNode.removeChild(removeNode);
    var removeText = document.getElementById(BASE_REMOVE_INPUT_FIELD_ID + removeNode.id);
    removeText.parentNode.removeChild(removeText);
    var removeBreak = document.getElementById(BASE_BREAK_ID + removeNode.id);
    removeBreak.parentNode.removeChild(removeBreak);
    
    decrementDatasetURLCount();
}

function getContainer()
{
	firstAddDatasetURLField =
		document.getElementById("id_add_dataset-0-dataset_url");
	
	container = firstAddDatasetURLField.parentNode;
	
	return container;
}

function insert_AddAnotherDatasetURL_Link()
{
	var onClickText = form_AddAnotherDatasetURL_Link_OnClickText();
	var containingParagraph = document.createElement("p");
	containingParagraph.id = ADD_ANOTHER_DATASET_URL__LINK__CONTAINER__ID;
	
	var addAnotherDatasetURLLink = document.createElement("a");
	addAnotherDatasetURLLink.id = ADD_ANOTHER_DATASET_URL__LINK__ID;
    addAnotherDatasetURLLink.innerHTML = "Add Another Dataset URL";
    addAnotherDatasetURLLink.setAttribute("onclick", onClickText);
    
    containingParagraph.insertBefore(addAnotherDatasetURLLink, null);
    container.insertBefore(containingParagraph, null);
    
    return containingParagraph;
}

function incrementDatasetURLCount()
{
	inputFieldCount++;
	nextInputFieldID++;
	
	totalForms.value = inputFieldCount;
}

function decrementDatasetURLCount()
{
	inputFieldCount--;
	
	totalForms.value = inputFieldCount;
}

function getLastInputField()
{
	lastPageBreak = lastSibling.previousSibling;
	lastRemoveLink = lastPageBreak.previousSibling;
	lastInputField = lastRemoveLink.previousSibling;
	
	return lastInputField;
}

function insert_InputField()
{
	newInputField = create_InputField();
    container.insertBefore(newInputField, lastSibling);
    
    return newInputField;
}

function insert_RemoveInputField_Link(inputFieldID)
{
	removeInputField_Link = create_RemoveInputField_Link(inputFieldID);
    container.insertBefore(removeInputField_Link, lastSibling);
    
    return removeInputField_Link;
}

function insert_PageBreak(inputFieldID)
{
	pageBreak = create_PageBreak(inputFieldID);
    container.insertBefore(pageBreak, lastSibling);
    
    return pageBreak;
}

function create_InputField()
{
	var newInputFieldID = form_InputFieldID(nextInputFieldID);
	var newInputField_Name = newInputFieldID;
	
	var newInputElement = document.createElement("input");
    newInputElement.id = newInputFieldID;
    newInputElement.name = newInputField_Name;
    newInputElement.type = "text";
    
    return newInputElement;
}

function create_RemoveInputField_Link(inputFieldID)
{
	var removeInputFieldID =
		form_RemoveInputFieldID(inputFieldID);
	var removeInputFieldOnClickText =
		form_RemoveInputField_OnClickText(inputFieldID);
	
	var removeInputField_Link = document.createElement("a");
    removeInputField_Link.id = removeInputFieldID;
    removeInputField_Link.innerHTML = "&nbsp;&nbsp;Remove this Dataset URL";
    removeInputField_Link.setAttribute("onclick", removeInputFieldOnClickText);
    
    return removeInputField_Link;
}

function create_PageBreak(inputFieldID)
{
	var pageBreak = document.createElement("br");
    pageBreak.id = BASE_BREAK_ID + inputFieldID;
    
    return pageBreak;
}

function form_InputFieldID(idNumber)
{
	stringInputElementID =
		BASE_INPUT_ID_PART_1 + idNumber + BASE_INPUT_ID_PART_2;
	
	return stringInputElementID;
}

function form_RemoveInputFieldID(inputFieldID)
{
	var removeInputFieldID =
		BASE_REMOVE_INPUT_FIELD_ID + inputFieldID;
	
	return removeInputFieldID;
}

function form_PageBreakID(inputFieldID)
{
	var pageBreakID = BASE_BREAK_ID + inputFieldID;
	
	return pageBreakID;
}

function form_AddAnotherDatasetURL_Link_OnClickText()
{
	onClickText = "javascript:create_AddDatasetURL_Form(); return false;";
	
	return onClickText;
}

function form_RemoveInputField_OnClickText(inputFieldID)
{
	removeInputFieldOnClickText = "javascript:removeInputField('" +
								  inputFieldID +
								  "'); return false;";
	
	return removeInputFieldOnClickText;
}

function insertAfter(referenceNode, newNode)
{
	referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}
