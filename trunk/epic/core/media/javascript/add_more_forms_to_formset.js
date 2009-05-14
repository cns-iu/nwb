FormSet = function (varName, formPrefix, fieldName, formLabel) {
	this.varName = varName; // TODO: Figure out a better way for this to work.
	this.formPrefix = formPrefix;
	this.fieldName = fieldName;
	this.formLabel = formLabel;
	this.forms = [];
	this.formSetManager =
		document.getElementById('id_' + this.formPrefix + '-TOTAL_FORMS');
};
FormSet.prototype.getFormCount = function () {
	var base = 10;
	return parseInt(this.formSetManager.value, base);
};

function initialize(formSet) {
	initializeFormSet(formSet);
	insertAddButton(formSet);
}
function createInputId(formSet, id) {
	return 'id_' + formSet.formPrefix + '-' +
	       id + '-' + formSet.fieldName;
}
function initializeFormSet(formSet) {
	var count = formSet.getFormCount();
	
	for (var i = 0; i < count; i++) {
		var inputId = createInputId(formSet, i);
		var input = document.getElementById(inputId);
		var td = input.parentNode;
		var tr = td.parentNode;
		
		if (i !== 0) {
			var removeLink = createRemoveLink(input.id, formSet);
			td.appendChild(removeLink);
		}
		
		formSet.forms[i] = tr;
	}
}
function insertAddButton(formSet) {
	var insertionPoint = getInsertionPoint(formSet);
	
	var addLink = document.createElement('a');
	var onClickAttribute = 'addForm(' + formSet.varName + '); return false;';
	addLink.setAttribute('onclick', onClickAttribute);
	addLink.setAttribute('class', 'add_more_link');
	addLink.innerHTML = 'Add More';
	
	var tr = document.createElement('tr');
	var th = document.createElement('th');
	var td = document.createElement('td');
	
	tr.appendChild(th);
	tr.appendChild(td);
	td.appendChild(addLink);
	
	insertionPoint.parentNode.insertBefore(tr,
										   insertionPoint.nextSibling);
}
function getInsertionPoint(formSet) {
	var base = 10;
	var lastInputId = 
		createInputId(formSet, parseInt(formSet.getFormCount() - 1,
			                            base));
	var lastInput =
		document.getElementById(lastInputId).parentNode.parentNode;
	return lastInput;
}
function addForm(formSet) {
	
	// Create the elements needed for the form.
	
	var newId = formSet.getFormCount();
	var label = createLabel(newId, formSet);
	var input = createInput(formSet, newId, '');
	var tr = document.createElement('tr');
	var th = document.createElement('th');
	var td = document.createElement('td');
	var removeLink = createRemoveLink(input.id, formSet);

	formSet.forms[formSet.getFormCount()] = tr;
	
	// Setup the tr element that represents the form.
	
	tr.appendChild(th);
	th.appendChild(label);

	tr.appendChild(td);
	td.appendChild(input);
	td.appendChild(removeLink);

	// Insert the tr into the page.
	
	var insertionPoint = getInsertionPoint(formSet);
	insertionPoint.parentNode.insertBefore(tr, insertionPoint.nextSibling);

	// Update the manager to reflect the addition of the form.
	
	var formSetManager = formSet.formSetManager;
	var base = 10;
	formSetManager.setAttribute('value',
								parseInt(formSetManager.value, base) + 1);
}
function createLabel(formNumber, formSet) {
	var label = document.createElement('label');

	var labelForId = 'id_' + formSet.formPrefix + '-' +
					 formNumber + '-' + formSet.fieldName;
	label.setAttribute('for', labelForId);
	label.innerHTML = formSet.formLabel;

	return label;
}
function createInput(formSet, formNumber, formValue) {
	var id = createInputId(formSet, formNumber);
	var value = formValue;
	var name =
		formSet.formPrefix + '-' + formNumber + '-' + formSet.fieldName;
	var input = document.createElement('input');

	input.setAttribute('type', 'text');
	input.setAttribute('id', id);
	input.setAttribute('value', value);
	input.setAttribute('name', name);
	input.setAttribute('size', 40);
	
	return input;
}
function createRemoveLink(id, formSet) {
	var removeLink = document.createElement('a');

	var onClickAttribute = 'removeForm("' + id + '",' +
						   formSet.varName + '); return false;';
	removeLink.setAttribute('onclick', onClickAttribute);
	removeLink.setAttribute('class', 'removelink');
	removeLink.innerHTML = 'Remove';

	return removeLink;
}
function removeForm(id, formSet) {
	var input = document.getElementById(id);
	var td = input.parentNode;
	var tr = td.parentNode;
	var tbody = tr.parentNode;

	tbody.removeChild(tr);
	
	var base = 10;
	formSet.formSetManager.setAttribute('value', 
			parseInt(formSet.formSetManager.value, base) - 1);
	
	var count = formSet.getFormCount();
	
	for (var i = 1; i < count; i++) {
		if(formSet.forms[i] == tr) {
			formSet.forms[i] = null;
		}
	}
	
	updateFormSetForms(formSet);
}
function updateFormSetForms(formSet) {
	/*
	** Drop out all the null elements.
	**/
	var count = formSet.forms.length;
	for (var i = 1; i < count; i++) {
		if (formSet.forms[i] === null) {
			formSet.forms.splice(i, 1);
		}
	}
	/*
	** Set the ids for the inputs and the remove links
	**   based on their position in the array so all
	**   the ids are in order for the formset because
	**   formsets require them to be in order.
	*/
	count = formSet.forms.length;
	for (i = 1; i < count; i++) {
		// the tr had two children if correctly formed - [0] is the 'input', [1] is the 'a'
		var input = formSet.forms[i].childNodes[1].childNodes[0];

		input.id = createInputId(formSet, i);
		input.name = formSet.formPrefix + '-' + i + '-' + formSet.fieldName;

		var removeLink = formSet.forms[i].childNodes[1].childNodes[1];
		var onclickAttribute = 'removeForm("' + input.id + 
							'",' + formSet.varName + '); return false;';
		removeLink.setAttribute('onclick', onclickAttribute);
	}
}