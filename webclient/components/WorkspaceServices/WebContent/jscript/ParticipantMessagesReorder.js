function highlightField(_targetID) {
  var td = dojo.byId(_targetID);
  dojo.style(td, "backgroundColor", "#CCCCCC");
  td.setAttribute("selectedTD","true");
  resetNotSelectedFields(td);
}

function resetNotSelectedFields(_selectedTd) {

  var tbody = _selectedTd.parentNode.parentNode;
  if(tbody) {

    var trList = dojo.query('tr', tbody);

    dojo.forEach(trList, 
      function(nextTr) {
        var tdList = dojo.query('td', nextTr);
        
        dojo.forEach(tdList,
          function(currentTd) {
            deselectTD(currentTd, _selectedTd);
          }
        ); // end forEach(tdList
      }
    ); // end forEach(trList
  }
}

function deselectTD(_currentTd, _selectedTd) {

  if(_currentTd !== _selectedTd) {
    dojo.style(_currentTd, "backgroundColor", "#FFFFFF");
    _currentTd.setAttribute("selectedTD","false");
  }
}


function moveUp(_targetID, _tableID) {   

  var radioArray = new Array();
  var tbody = dojo.byId("tbody" + _tableID); 
    
  if(tbody) {
    
    var trList = dojo.query('tr', tbody);
    var checkedButton = -1;
    var counter=0;
    dojo.forEach(trList, 
      function(currentTr) {
      
        radioArray[counter] = currentTr;
        var td = dojo.query('td:first-child', currentTr);
        if(td[0].getAttribute("selectedTD") == "true") {
          checkedButton = counter;
        }
        counter++;
      }
    );
    
    if(checkedButton == 0) {
      insertAfter(radioArray[radioArray.length-1], radioArray[checkedButton]);
      radioArray = resetRadioArray(radioArray, _tableID);           
      setHiddenValue(_targetID, radioArray);         
    }
    if(checkedButton > 0) {
      insertBefore(radioArray[checkedButton-1], radioArray[checkedButton]);
      radioArray = resetRadioArray(radioArray, _tableID);
      setHiddenValue(_targetID, radioArray);
    }
  }   
}

function recheckButton(_checkedTr) {

  var firstTd = dojo.query('td:first-child', _checkedTr); 
  if(firstTD[0]) {
    var firstInput = dojo.query('input:first-child', firstTd[0]);    
    if(firstInput[0]) {     
      input.checked = true;
    }
  }
}


function setHiddenValue(_targetID, _radioArray) {
  var hiddenTextField = dojo.byId(_targetID); 

  if(hiddenTextField) { 
  
    var hiddenTextFieldString;
    for(i=0; i<_radioArray.length; i++) {
 
      td = dojo.query('td:first-child', _radioArray[i]); 
      hiddenInputTd = dojo.query('input:first-child', td[0]);

      if(i == 0) {
        hiddenTextFieldString = hiddenInputTd[0].getAttribute('orderableItemID');
      }
      else {
        hiddenTextFieldString = hiddenTextFieldString + "," + hiddenInputTd[0].getAttribute('orderableItemID');
      }
    }
    hiddenTextField.value = hiddenTextFieldString;
  }
}


function moveDown(_targetID, _tableID)
{   
  var radioArray = new Array();   
  var tbody = dojo.byId("tbody" + _tableID);
    
  if(tbody) {
    var trList = dojo.query('tr', tbody);
    var checkedButton = -1;
    var counter=0;
    dojo.forEach(trList, 
      function(currentTr){
        radioArray[counter] = currentTr;
        var td = dojo.query('td:first-child', currentTr);
        var input = dojo.query('input:first-child', td[0]);
        if(td[0].getAttribute("selectedTD") == "true") {
          checkedButton = counter;
        }
        counter++;
      }
    );
    
    if(checkedButton==radioArray.length-1) {
      insertBefore(radioArray[0], radioArray[checkedButton]);
      radioArray = resetRadioArray(radioArray, _tableID);       
      setHiddenValue(_targetID, radioArray);
    }
    else if(checkedButton>-1) {
      insertAfter(radioArray[checkedButton+1], radioArray[checkedButton]);
      radioArray = resetRadioArray(radioArray, _tableID);    
      setHiddenValue(_targetID, radioArray);
    }    
  } 
}
  
function resetRadioArray(radioArray, _tableID) {
  radioArray = new Array();
  
  var tbody = dojo.byId("tbody" + _tableID);    
  if(tbody) {
  
    var trList = dojo.query('tr', tbody);
    var checkedButton = -1;
    var counter=0;
    dojo.forEach(trList, 
      function(currentTr) {
        radioArray[counter] = currentTr;
        var td = dojo.query('td:first-child', currentTr);
        var input = dojo.query('input:first-child', td[0]);
        if(td[0].getAttribute("selectedTD") == "true") {
          checkedButton = counter;
        }
        counter++;
      }
    );
  } 
  return radioArray;
}


// This function inserts newNode before referenceNode
function insertBefore( referenceNode, newNode) {
    referenceNode.parentNode.insertBefore( newNode, referenceNode);
}
// This function inserts newNode after referenceNode
function insertAfter( referenceNode, newNode) {
    referenceNode.parentNode.insertBefore( newNode, referenceNode.nextSibling);
}