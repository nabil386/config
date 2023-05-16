/*
  Function which determines if a search is automatically run when
  the custom Process Instance Errors page is opened.
*/
function autoSearch(isSearchEnabledParam){

  /*
    Grabs the value of the page parameter, which is 
    passed to the page to determine if the auto search runs
  */
  var searchEnabled = dojo.attr(isSearchEnabledParam, "value");
  /*
    Sets the value of the page parameter to false,
    which turns off the auto search feature.
  */
  var searchDisabled = dojo.attr(isSearchEnabledParam, "value", "false");

  dojo.ready(function(){

    if(searchEnabled == "true"){
        /*
          Finds the search button on the page and simulates clicking this button
        */
        var defaultSubmitButton = null;
        var explicitDefaultBtnArray = dojo.query(".curam-default-action");
        // take the default button if set
        if (explicitDefaultBtnArray.length > 0) {
          defaultSubmitButton = explicitDefaultBtnArray[0];

        } else {
          // otherwise take the first found submit button
            var submitButtonsArr = dojo.query("input[type='submit']");
          if (submitButtonsArr.length > 0) {
            defaultSubmitButton = submitButtonsArr[0];
          }
        }

        // now click the button found
        if (defaultSubmitButton != null) {
          curam.util.clickButton(defaultSubmitButton);
        }
    
      }

  });
}

/*
  Changes the width of the WF and DP charts
  to 100% on IE 10 and IE 11
*/
function changeChartWidthDependingOnBrowser(){

  var isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/));
  var isIE10 = !!(navigator.userAgent.match(/MSIE 10/));
  
  if(isAtLeastIE11 || isIE10){
  
    var workflowChart = dojo.query(".WORKFLOWCHART table td");
    var wChart = workflowChart[0];
    dojo.style(wChart, "width", "100%");
    var deferredProcessChart = dojo.query(".DEFERREDPROCESSCHART table td");
    var dChart = deferredProcessChart[0];
    dojo.style(dChart, "width", "100%");
    
  }
}
