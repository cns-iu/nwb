Form = function(varName, formPrefix, formLabel){
	this.varName = varName; // TODO: Figure out a better way for this to work. 
	this.formPrefix = formPrefix;
	this.formLabel = formLabel;
	this.formArray = [];
	this.manager = document.getElementById('id_' + this.formPrefix + '-TOTAL_FORMS');
}

Form.prototype.getCount = function(){
	return parseInt(this.manager.value);
}
function initialize(form){
	initializeForm(form);
	insertAddButton(form);
}
function initializeForm(form){
	var count = form.getCount();
	for(i = 0; i < count; i++){
		var input = document.getElementById('id_' + form.formPrefix + '-' + i + '-' + form.formPrefix);
		var td = input.parentNode;
		var tr = td.parentNode;
		
		if (i != 0) {
			var removeLink = createRemoveLink(input.id, form)
			td.appendChild(removeLink);
		}
		
		form.formArray[i] = tr;
	}
}
function insertAddButton(form){
	var insertionPoint = getInsertionPoint(form);

	addLink = document.createElement('a');
	addLink.setAttribute('onclick', 'addMore(' + form.varName + '); return false;');
	addLink.innerHTML = 'Add More';
	insertionPoint.parentNode.insertBefore(addLink, insertionPoint.nextSibling);	
}
function getInsertionPoint(form){
	var lastReference = document.getElementById('id_' + form.formPrefix + '-' + parseInt(form.getCount() - 1) + '-' + form.formPrefix).parentNode.parentNode;
	return lastReference;
}
function addMore(form){

	var newID = form.getCount();
	
	var label = createLabel(newID, form);
	var input = createReference(form, newID, '');
	var tr = document.createElement('tr');
	var th = document.createElement('th');
	var td = document.createElement('td');
	var removeLink = createRemoveLink(input.id, form);

	form.formArray[form.getCount()] = tr;
	
	tr.appendChild(th);
	th.appendChild(label);

	tr.appendChild(td);
	td.appendChild(input);
	td.appendChild(removeLink);

	var insertionPoint = getInsertionPoint(form);
	insertionPoint.parentNode.insertBefore(tr, insertionPoint.nextSibling);

	var manager = form.manager;
	manager.setAttribute('value', parseInt(manager.value) + 1);
}
function createLabel(formNumber, form){
	var label = document.createElement('label');

	label.setAttribute('for', 'id_' + form.formPrefix + '-' + formNumber + '-' + form.formPrefix);
	label.innerHTML = form.formLabel;

	return label;
}
function createReference(form, formNumber, formValue){
	var id = 'id_' + form.formPrefix + '-' + formNumber + '-' + form.formPrefix;
	var value = formValue;
	var name = form.formPrefix + '-' + formNumber + '-' + form.formPrefix;
	var input = document.createElement('input');

	input.setAttribute('type', 'text');
	input.setAttribute('id', id);
	input.setAttribute('value', value);
	input.setAttribute('name', name);

	return input;
}
function createRemoveLink(id, form){
	var removeLink = document.createElement('a');

	removeLink.setAttribute('onclick', 'removeForm("' + id + '",' + form.varName + '); return false;');
	removeLink.innerHTML = 'Remove';

	return removeLink;
}
function removeForm(id, form){
	var input = document.getElementById(id);
	var td = input.parentNode;
	var tr = td.parentNode;
	var tbody = tr.parentNode;

	tbody.removeChild(tr);

	form.manager.setAttribute('value', parseInt(form.manager.value) - 1);
	
	var count = form.getCount();
	for(var i = 1; i < count; i++){
		if(form.formArray[i] == tr){
			form.formArray[i] = null;
		}
	}
	
	updateFormArray(form);
}

function updateFormArray(form){
	/*
	** Drop out all the null elements.
	**/
	var count = form.formArray.length;
	for(var i = 1; i < count; i++){
		if(form.formArray[i] == null){
			form.formArray.splice(i,1);
		}
	}
	/*
	** Set the ids for the inputs and the remove links
	**   based on their position in the array so all
	**   the ids are in order for the formset.
	*/
	count = form.formArray.length;
	for(var i = 1; i < count; i++){
		// the tr had two children if correctly formed - [0] is the 'input', [1] is the 'a'
		var input = form.formArray[i].childNodes[1].childNodes[0];

		input.id = 'id_' + form.formPrefix + '-' + i + '-' + form.formPrefix;
		input.name = form.formPrefix + '-' + i + '-' + form.formPrefix;

		var removeLink = form.formArray[i].childNodes[1].childNodes[1];

		removeLink.setAttribute('onclick', 'removeForm("' + input.id + '",' + form.varName + '); return false;');
	}
}