dojo.require("dijit.form.FilteringSelect");
dojo.require("dojo.data.ItemFileReadStore");

function filterTemplateCategorySubCategoryDropDown(value) {

var subCategoryInput = document.getElementById("__o3id2_display");
var subCategoryInputMenuList = subCategoryInput.parentElement;
var subCategoryInputListnextSibling = subCategoryInputMenuList.nextElementSibling;

subCategoryInputListnextSibling.addEventListener('DOMNodeInserted', function(event) {
   value=document.getElementById("__o3id1_display").value;
    var subCategoryInputListItems = Array.from(subCategoryInputListnextSibling.querySelectorAll(".bx--list-box__menu-item"));

var validValues = [];
subCategory.items.forEach(item => {
    if (item.category == value) {
        validValues.push(item.name);
    }
});

subCategoryInputListItems.forEach(item => {
    var child = item.firstElementChild;
    if (!validValues.includes(child.innerHTML) || child.innerHTML=="") {
      item.style.display = 'none';
    }
});
    
}, false);

}

