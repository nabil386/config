function filterTemplateCategorySubCategoryDropDown() {

	var subCategoryField = document.getElementById('__o3id2_display');
	
	if (subCategoryField) {
		subCategoryField.value = null;
	}	

	var subCategoryInput = subCategoryField;
	var subCategoryInputWrapper = subCategoryInput.parentElement;
	var subCategoryInputList = subCategoryInputWrapper.nextElementSibling;
	
	
	
	subCategoryInputList.addEventListener('DOMNodeInserted', function(event) {
		var category = document.getElementById('__o3id1_display').value;
		var subCategoryInputListItems = Array.from(subCategoryInputList.querySelectorAll('.bx--list-box__menu-item'));
		var existingSubCategory = document.getElementById('__o3id2_display').value;

	
		var validValues = [];
		subCategory.items.forEach(item => {
			if (item.category == category) {
				validValues.push(item.name);
			}
		});
		
		setTimeout(() => {
			if(!validValues.includes(subCategoryField.value)){
				subCategoryField.value = null;
			}
		},1);

		subCategoryInputListItems.forEach(item => {
			var child = item.firstElementChild;
			if (!validValues.includes(child.innerHTML) || child.innerHTML=="") {
				item.style.display = 'none';
			}
			
		item.addEventListener('mouseover', function(event) {
			
			setTimeout(() => {
				if(!validValues.includes(subCategoryField.value)){
					subCategoryField.value = null;
				}
			},1);
		
		});
		
		});
	
		
   
	}, false);

}

