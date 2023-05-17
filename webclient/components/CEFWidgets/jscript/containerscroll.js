/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
/* Modifications
 * -------------
 * 05-Oct-2012 BD [CR00346180] Dojo 1.7 Upgrade 
 */ 
require(["dojo/fx/easing","dojox/fx/scroll"]);

var containerScroll = {

container: null,
nextButton: null,
prevButton: null,
scrollItemWidth: 0,
scrollItemCount: 2,
scrollOfsett: 0,
scrollItemBorder: 34,
// BEGIN, CR00346180, SSK
initialOffSet : 0,
// END, CR00346180

initContainerScroll: function() {

  containerScroll.prevButton.style.visibility = "hidden";
  
  //  get td node of container
  var tdNode = containerScroll.container.firstChild.firstChild.firstChild;
  
  var totalItemCount = tdNode.childNodes.length;
  
  if (totalItemCount <= containerScroll.scrollItemCount) {
    containerScroll.nextButton.style.visibility = "hidden";
  }
  
  containerScroll.scrollItemWidth = parseInt(containerScroll.getStyle(tdNode.firstChild, "width"));

  containerScroll.scrollOfsett = containerScroll.scrollItemWidth * containerScroll.scrollItemCount + containerScroll.scrollItemBorder;

  // BEGIN, CR00346180, SSK
   initialOffSet = containerScroll.scrollOfsett;
   // END, CR00346180 

},

gotoPrevElement: function() {

  var currentPoss = containerScroll.container.scrollLeft;
 
  // BEGIN, CR00346180, SSK  
  var target = {x:(0-containerScroll.scrollOfsett), y:0};
  // END, CR00346180
  var anim0 = dojox.fx.smoothScroll({target: target, win: containerScroll.container, duration:800, easing:dojo.fx.easing.easeOut });
  anim0.play();

  containerScroll.nextButton.style.visibility = "visible";

  // BEGIN, CR00346180, SSK	
  currentPoss = containerScroll.container.scrollLeft;
	
  if (currentPoss <= initialOffSet) {
  // END, CR00346180
    containerScroll.prevButton.style.visibility = "hidden";
  }
},

gotoNextElement: function() {

  //  get td node of container
  var tdNode = containerScroll.container.firstChild.firstChild.firstChild;
  
  var totalItemCount = tdNode.childNodes.length;
  
  var currentPos = containerScroll.container.scrollLeft;
  var target = { x:currentPos + containerScroll.scrollOfsett, y:0};
  var anim0 = dojox.fx.smoothScroll({target: target, win: containerScroll.container, duration:500, easing:dojo.fx.easing.easeOut });
  anim0.play();

  containerScroll.prevButton.style.visibility = "visible";
  
  if (totalItemCount * containerScroll.scrollItemWidth < currentPos + (containerScroll.scrollOfsett * containerScroll.scrollItemCount)) {
    containerScroll.nextButton.style.visibility = "hidden";
  }

},


getStyle: function(node, styleName)
{
  if (node.currentStyle)
    var styleValue = node.currentStyle[styleName];
  else if (window.getComputedStyle)
    var styleValue = document.defaultView.getComputedStyle(node, null).getPropertyValue(styleName);    
  return styleValue;
}


};