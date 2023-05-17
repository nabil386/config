/**
 * Modification History
 * ====================
 * 17-Oct-2014 DM [CR00447373] Issue with Gantt Chart toggle buttons not being accessible
 * 16-Sep-2014 DM [CR00444949] Issue with Gantt Chart being embedded in a collapsed cluster. When the 
 *                 Gantt Chart widget was initialised, on page load, it had the offsetWidth&offsetHeight equal to 0
 * 05-Oct-2012 BD [CR00346093] Dojo 1.7 Upgrade. No changes for Dojo but issues with 
 *                             IE9 laying out the widget resulted in commenting out code
 *                                
 * 28-Oct-2011 BD [CR00296433] Moved function to open modal to CEFUtils.js 
 * 27-Oct-2011 BD [CR00296073] Add function to open a modal dialog.
 */
 
 dojo.require("dojo.NodeList-traverse"); 
 
var Gantt = {
  tree: null,
  actualDisplayed: false,
  expectedDisplayed: false
};

function addSynchScroll(){
  var leftData = dojo.byId("left-data");
  var rightData = dojo.byId("right-data");
  var rightHeader = dojo.byId("right-header");
  rightData._sync = function () {
    leftData.scrollTop = rightData.scrollTop;
    rightHeader.scrollLeft = rightData.scrollLeft;
  };
  rightData.onscroll = rightData._sync;
}

function resizeHandle(e) {
  // Do not continually resize, only do it in intervals.
  // Otherwise, slow browsers like Internet Explorer
  // cause JavaScript errors.

  if(Gantt.timer) {
    clearTimeout(Gantt.timer);
  }
  Gantt.timer = setTimeout(function(){
    initGantt(false);
  }, 200);
}

function initGantt(initialScroll) {
  if(window.parent && window.parent != window && window.parent.isModalWrapper){
      return;
  }

  var ganttData = dojo.byId("gantt-data");
  if (ganttData == null) {
    return;
  }
  var leftData = dojo.byId("left-data");
  var rightData = dojo.byId("right-data");
  var leftHeader = dojo.byId("left-header");
  var rightHeader = dojo.byId("right-header");
  
  //CR00444949, DM
  //if the cluster containing the gantt chart is not expanded by default
  if ((ganttData.offsetWidth==0)&&((ganttData.offsetHeight==0))){
    //get the first cluster html node containing the gantt chart
    var formNode = dojo.NodeList(ganttData).parents(".cluster").first();
    if (formNode){
    	//get the toggle button inside the cluster  
    	var toggleButton=dojo.query(".grouptoggleArrow", formNode[0])[0];
	    if (toggleButton){
    		//when the cluster is expanded, re-initialize the Gantt Chart widget
			var handle = dojo.connect(toggleButton, "onclick", function(evt){initGantt(true);dojo.disconnect(handle);});
			var handle1 = dojo.connect(toggleButton, "onkeypress", function(evt){if(curam.util.enterKeyPress(evt)){initGantt(true);dojo.disconnect(handle1);}});
			return;
		}
	}
  }
  //END, CR00444949, DM

  var rightDataWidth = ganttData.offsetWidth - 244;

  leftData.style.width = 210 + "px";
  rightData.style.width = rightDataWidth + "px";
  leftHeader.style.width = 210 + "px";
  rightHeader.style.width = rightDataWidth + "px";

  ganttData.style.height = (document.body.offsetHeight - heightOffset) * chartHeight/100 + "px";

  leftData.style.height = ganttData.offsetHeight - 1 + "px";
  rightData.style.height = ganttData.offsetHeight - 1 + "px";

  // Initialize the hidden data tree that holds the service plan hierarchy
  Gantt.tree = window.dojo.byId('xmlDataIsland');

  // Initially, the actuals and expecteds are both displayed
  Gantt.actualDisplayed = true;
  Gantt.expectedDisplayed = true;

  // synch up the scrolling divs
  addSynchScroll();

  dojo.connect(window, 'onresize', resizeHandle);

  if (initialScroll) {
    scrollTo(initialScrollPos);
  }

  // Have to call placeSlider twice, once so that DoResize generates the
  // right sizes, and second time to resize the slider.
  ganttSlider.placeSlider();
  // BEGIN, CR00346093, BD
  //ganttSlider.DoResize(null, true);
  // END, CR00346093
  // Have to call this again to size the slider correctly
  ganttSlider.placeSlider(true);
  
  // BEGIN, CR00161962, KY
  //set up the initial closed nodes
  initDisplay();
  // END, CR00161962
}

// BEGIN, CR00161962, KY
/* sorts out the show or hide of particular nodes by looping through all elements in the data island */
function initDisplay(){
  var _e = Gantt.tree;
  var closedNodeList = findNodesSetToClosed(_e, new Array());
  for(var i=0;i<closedNodeList.length;i++){
    var elem = closedNodeList[i];
    //go get the dom image object we need to update
  var elemId = dojo.attr(elem, 'id');
  var image = dojo.byId(elemId + 'img');
  var imagesrc = image.getAttribute('src');
  var temp = imagesrc.split('collapse.gif');
  imagesrc = temp.join('expand.gif');
  image.src = imagesrc;
  //walkTree expects the attrib open to be true
  elem.setAttribute('open', 'true');
  walkTree(elemId);
  }
}
// END, CR00161962

// BEGIN, CR00161962, KY
/* gives us the nodes of those that have been initially set to open=false in the data island*/
function findNodesSetToClosed(elem, closedNodeList){
  if (elem.hasChildNodes) {
    for (var i = 0; i < elem.childNodes.length; i++) {
      findNodesSetToClosed(elem.childNodes[i], closedNodeList);
    }
  }
  var open = dojo.attr(elem,'open');
  if (open == 'false') {
    closedNodeList.push(elem);
  }
  return closedNodeList;
}
// END, CR00161962

/* Open or close a tree node */

function toggle(nodeId, image) {
  if (image.src.indexOf('expand.gif') > -1) {
    image.src = image.src.replace('expand.gif', 'collapse.gif');
  } else {
    image.src = image.src.replace('collapse.gif', 'expand.gif');
  }
  walkTree(nodeId);
  
}

function toggleEnter(nodeId,image) {
  var evt=(window.event)?window.event:null;
  if(evt){
    var key=(evt.charCode)?evt.charCode:
      ((evt.keyCode)?evt.keyCode:((evt.which)?evt.which:0));
    if(key=="13")
    {
      if (image.src.indexOf('expand.gif') > -1) {
    	image.src = image.src.replace('expand.gif', 'collapse.gif');
  		} else {
    	image.src = image.src.replace('collapse.gif', 'expand.gif');
  		}
  		walkTree(nodeId);
      }
  }
  
}
// BEGIN CR00181672, BD
// Walk the tree looking for the particular node to
// toggle open or closed
function walkTree(nodeId) {

  // Find the Node
  var queryId = "#" + nodeId;
  var elemList = dojo.query(queryId);

  // if the query returned multiple Nodes there is an issue  
  if(elemList.length > 1){
    alert("Error: Duplicate ID issue. Please contact your administrator");
  }
  
  // Toggle the item open or close
  if(elemList.length == 1){
    // Get the first (only!) Node in the list
    var elem = elemList[0];
    var openState = dojo.attr(elem,'open');
    if (openState == 'true') {
      openState = 'false';
    } else {
      openState = 'true';
    }
    dojo.attr(elem,'open',openState);
    propagateChange(elem, openState);
  }
}


function propagateChange(elem, open) {
  var elemId;
  if (elem.hasChildNodes) {
  
    for (var i = 0; i < elem.childNodes.length; i++) {
    
      var childElem = elem.childNodes[i];
      
      // Ignode this Node if it is a comment
      // The comment Nodes were added to prevent the empty 'node' tags from collapsing.
      // When the empty 'node' tags collapsed the renderer produced incorrect html. 
      // This workaround ensures the HTML is produced correctly, but we now must explicitly
      // skip the comments.
      
      if(childElem.nodeType!== 8){  /* 8 == COMMENT_NODE */
      
        elemId = dojo.attr(childElem, 'id');
        if (open == 'false') {
          // hide the element regardless and pass that on
          dojo.style(elemId + 'a', "display", "none");
          dojo.style(elemId + 'b', "display", "none");
          if (dojo.byId(elemId + 'c')) {
            dojo.style(elemId + 'c', "display", "none");
          }
          propagateChange(elem.childNodes[i], 'false');
        } else {
          // display the element
          dojo.style(elemId + 'a', "display", "");
          dojo.style(elemId + 'b', "display", "");
          if (dojo.byId(elemId + 'c')) {
            dojo.style(elemId + 'c', "display", "");
          }
          propagateChange(elem.childNodes[i], dojo.attr(childElem,'open'));
        }
      }
    }
  }
}
// END CR00181672

/* End of open or close a tree node */

function showAll(elem) {
  var i;
  var rowHeader;
  var headerImage;
  var childName;
  var elemId = dojo.attr(elem, 'id');
  var open = dojo.attr(elem, 'open');
  if (open !== null) {
    rowHeader = dojo.byId(elemId + 'a');
    // If there is an expand/collapse control on this header, set it to
    // 'collapse'.
    if (rowHeader.hasChildNodes) {
      for (i = 0; i < rowHeader.childNodes.length; i++) {
        childName = rowHeader.childNodes[i].nodeName;
        if ((childName == 'img') || (childName == 'IMG')) {
          headerImage = rowHeader.childNodes[i];
          headerImage.src = headerImage.src.replace('expand.gif', 'collapse.gif');
        }
      }
    }
    dojo.style(rowHeader, "display", "");
    dojo.style(elemId + 'b', "display", "");
    if (dojo.byId(elemId + 'c')) {
      dojo.style(elemId + 'c', "display", "");
    }    
    elem.setAttribute('open', 'true');
  }
  dojo.forEach(elem.childNodes, showAll, true);
}


function showActual() {
  var ganttData = document.getElementById("right-data");
  var images = dojo.query("img.actual", ganttData);

  if (Gantt.actualDisplayed) {
    dojo.forEach(images, cm.hide, true);
    Gantt.actualDisplayed = false;
  } else {
    dojo.forEach(images, cm.show, true);
    Gantt.actualDisplayed = true;
  }
}

function showExpected() {
  var ganttData = document.getElementById("right-data");
  var images = dojo.query("img.expected",ganttData);

  if (Gantt.expectedDisplayed) {
    dojo.forEach(images, cm.hide, true);
    Gantt.expectedDisplayed = false;
  } else {
    dojo.forEach(images, cm.show, true);
    Gantt.expectedDisplayed = true;
  }
}

function scrollTo(x) {
  var rightData = dojo.byId("right-data");
  var rightHeader = dojo.byId("right-header");
  var isLTR = dojo._isBodyLtr();
  if (x < -100 && isLTR) {
    rightData.scrollLeft = rightData.scrollWidth;
    rightHeader.scrollLeft = rightHeader.scrollWidth;
  } else if ( x > 100 && !isLTR) {
    rightData.scrollLeft = -rightData.scrollWidth;
    rightHeader.scrollLeft = -rightHeader.scrollWidth;
  } else {
    x = dojo.isIE ? Math.abs(x) : x;
    rightData.scrollLeft = x;
    rightHeader.scrollLeft = x;
  }
}

/****************************************************************
 * Slider
 *
 * Slider050723
 * by Christiaan Hofman, July 2005
 *
 * You may use or modify this code provided that this copyright notice
 * appears on all copies.
 *
 ****************************************************************/

// Constructor

function Slider(name) {
    this.leftValue = 0;
    this.rightValue = 1;
    this.defaultValue = 0;
    this.offsetX = 1;
    this.offsetY = 1;
    this.maxSlide = 100;
    this.buttonWidth = 6;
    this.buttonHeight = 28;
    this.buttonImg = "bar.gif";
    this.buttonHiliteImg = "bar_selected.gif";
    this.buttonHoverImg = null;
    this.imgPath = "";
    this.orientation = "h";

    this.writeSlider = Slider.writeSlider;
    this.placeSlider = Slider.placeSlider;
    this.makeEventHandler = Slider.makeEventHandler;
    this.isPrototype = Slider.isPrototype;
    this.getValue = Slider.getValue;
    this.setValue = Slider.setValue;

    this.MouseOver = Slider.MouseOver;
    this.MouseOut = Slider.MouseOut;
    this.MouseDown = Slider.MouseDown;
    this.MouseUp = Slider.MouseUp;
    this.MouseSlide = Slider.MouseSlide;
    this.DoResize = Slider.DoResize;

    this.onmouseover = null;
    this.onmouseout = null;
    this.onmousedown = null;
    this.onmouseup = null;
    this.onslide = null;
    this.onchange = null;
    this.onclick = null;

    if (!window.sliders)  window.sliders = new Array();
    this.name = name || "slider"+window.sliders.length;
    window.sliders[window.sliders.length] = this;
    window.sliders[this.name] = this;
    if (!window.sliderDrag)  window.sliderDrag = new Object();
}

// method write the button DIV

Slider.writeSlider = function () {

    var proto = this.prototype || this;

    // create images for the prototype, if not already set
    if (!proto.loImg) {
        proto.loImg = new Image(proto.buttonWidth,proto.buttonHeight);
        proto.loImg.src = proto.imgPath + proto.buttonImg;
       if (proto.buttonHiliteImg) {
            proto.hiImg = new Image(proto.buttonWidth,proto.buttonHeight);
            proto.hiImg.src = proto.imgPath + (proto.buttonHiliteImg || proto.buttonImg);
        }
        if (proto.buttonHoverImg) {
            proto.hoImg = new Image(proto.buttonWidth,proto.buttonHeight);
            proto.hoImg.src = proto.imgPath + proto.buttonHoverImg;
        }
    }
    // set the properties according to the prototype
    if (proto != this) {
        this.loImg = proto.loImg;
        if (proto.hiImg)  this.hiImg = proto.hiImg;
        if (proto.hoImg)  this.hoImg = proto.hoImg;
        this.orientation = proto.orientation;
        this.maxSlide = proto.maxSlide;
    }


    // set button properties and mouse event handlers
  this.button = dojo.byId("slider-div");
  this.button.img = dojo.byId("slider-image");
  this.button.style.width = proto.buttonWidth + 'px';
  this.button.style.height = proto.buttonHeight + 'px';
  if (this.button.addEventListener) {
    this.button.addEventListener("mousedown",this.MouseDown,false);
    this.button.addEventListener("mouseout",this.MouseOut,false);
    this.button.addEventListener("mouseover",this.MouseOver,false);
    } else {
        this.button.onmousedown = this.MouseDown;
        this.button.onmouseout = this.MouseOut;
        this.button.onmouseover = this.MouseOver;
    }
    // set event handlers as functions
    this.onmouseover = this.makeEventHandler(this.onmouseover);
    this.onmouseout  = this.makeEventHandler(this.onmouseout);
    this.onmousedown = this.makeEventHandler(this.onmousedown);
    this.onmouseup   = this.makeEventHandler(this.onmouseup);
    this.onslide     = this.makeEventHandler(this.onslide);
    this.onchange    = this.makeEventHandler(this.onchange);
    this.onclick     = this.makeEventHandler(this.onclick);
    // tell button who we are
    this.button.slider = this;
    // from now on button refers to the style object
    this.button = this.button.style;
}

// method to put the slider button in place

Slider.placeSlider = function (dontWrite) {
    if(!dontWrite) {
      this.writeSlider();
    }
    var proto = this.prototype || this;

    var ganttData = dojo.byId('gantt-data');
    var ganttHeader = dojo.byId('gantt-header');
    var leftData = dojo.byId('left-data');
    var sliderImage = dojo.byId('slider-image');

    this.rail = ganttHeader;
    // offset w.r.t rail
    var x = proto.offsetX;
    var y = proto.offsetY;
    this.maxSlide = ganttData.offsetWidth;
    sliderImage.style.height =
      ganttData.offsetHeight + ganttHeader.offsetHeight - 2 + 'px';
    sliderImage.style.width = '6px';
    // set position of button
    if(dojo._isBodyLtr()){
    	this.button.left = x + leftData.offsetWidth + 'px';
    }else{
        var rightData = dojo.byId("right-data");
        this.button.left = x + rightData.offsetWidth +  (dojo.isIE ? 8 : 9) + proto.buttonWidth + 'px';
    }
    this.button.top = y + 'px';
    // offset is remembered for later sliding
    this.offset = x;
    this.button.visibility = 'inherit';
}

// makes slider a prototype for all previously defined sliders

Slider.isPrototype = function () {
    for (var i=0; i<window.sliders.length; i++)
        window.sliders[i].prototype = window.sliders[i].prototype || this;
}

// mouseover handler of the button, only calls handler of slider

Slider.MouseOver = function (e) {
    window.sliderDrag.isOver = true;
    if (this.slider.hoImg && !window.sliderDrag.isDown)  this.img.src = this.slider.hoImg.src;
    if (this.slider.onmouseover)  this.slider.onmouseover(e);
}

// mouseout handler of the button, only calls handler of slider

Slider.MouseOut = function (e) {
    window.sliderDrag.isOver = false;
    if (this.slider.hoImg && !window.sliderDrag.isDown)  this.img.src = this.slider.loImg.src;
    if (this.slider.onmouseout)  this.slider.onmouseout(e);
}

// mousedown handler of the button

Slider.MouseDown = function (e) {
    var slider = this.slider;
    // remember me
    window.sliderDrag.dragLayer = this;
    window.sliderDrag.dragged = false;
    window.sliderDrag.isDown = true;
    // event position
    var evtX = evtY = 0;
    if (!e) e = window.event;
    if (typeof(e.pageX) == 'number') {
        evtX = e.pageX;
        evtY = e.pageY;
    } else if (typeof(e.clientX) == 'number') {
        evtX = e.clientX + (document.body.scrollLeft || 0);
        evtY = e.clientY + (document.body.scrollTop || 0);
    }
    // ignore right mouse button
    if ((e.which && e.which == 3) || (e.button && e.button == 2)) return true;
    // set starting offset of event
  window.sliderDrag.offX  =  evtX - parseInt(this.style.left) + slider.offset;
  window.sliderDrag.offY  =  evtY - parseInt(this.style.top) + slider.offset;
  if (e.cancelable) e.preventDefault();
  if (e.stopPropagation) e.stopPropagation();
  e.cancelBubble = true;
    // document handles move and up events
    document.onmousemove = slider.MouseSlide;
    document.onmouseup = slider.MouseUp;
    if (document.captureEvents) document.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
    // show hilite img
    if (slider.hiImg) {
      this.img.src = this.img.src.replace('bar.gif', 'bar_selected.gif');
    }
    // call event handler of slider
    if (slider.onmousedown)  slider.onmousedown(e);
    return false;
}

// mouseup handler of the button

Slider.MouseUp = function (e) {
    // button and slider that was draged
    var l = window.sliderDrag.dragLayer || this;
    var slider = l.slider;
    window.sliderDrag.isDown = false;
    // cancel move and up event handlers of document
    document.onmousemove = null;
    document.onmouseup = null;
    if (document.releaseEvents) {
  document.releaseEvents(Event.MOUSEMOVE|Event.MOUSEUP);
    }
    window.sliderDrag.dragLayer = null;
    // show normal image
    if (slider.hiImg) {
      l.img.src = l.img.src.replace('bar_selected.gif', 'bar.gif');
    }

    slider.DoResize();

    // cal event handlers of slider
    if (slider.onmouseup) {
      slider.onmouseup(e);
    }
    if (window.sliderDrag.dragged) {
        if (slider.onchange) {
          slider.onchange(e);
        }
    } else {
        if (slider.onclick) {
          slider.onclick(e);
        }
    }
    return false;
}

Slider.DoResize = function(slider) {
    if(!slider) {
      slider = this;
    }

    // resize divs
    var ganttData = dojo.byId("gantt-data");
    var ganttHeader = dojo.byId("gantt-header");
    var leftData = dojo.byId("left-data");
    var rightData = dojo.byId("right-data");
    var leftHeader = dojo.byId("left-header");
    var rightHeader = dojo.byId("right-header");
    var sliderButton = dojo.byId("slider-div");
    var sliderImage = dojo.byId("slider-image");

    var leftDataWidth = parseInt(sliderButton.style.left) - slider.offset;

    function setWidth(elem, val) {
      if (elem.style.width != val + "px") {
        elem.style.width = val + "px";
      }
    }
    var sliderHeight = ganttData.offsetHeight
      + ganttHeader.offsetHeight - 2 + 'px';
    sliderImage.style.height = sliderButton.style.height = sliderHeight;
    var subVal = dojo.isIE ? 8 : 9;
    if(dojo._isBodyLtr()){
       setWidth(leftData, leftDataWidth);
       setWidth(rightData, ganttData.offsetWidth - leftDataWidth - subVal);

       setWidth(leftHeader, leftDataWidth);
       setWidth(rightHeader, ganttData.offsetWidth - leftDataWidth - subVal);
    }else{
       setWidth(leftData, ganttData.offsetWidth - leftDataWidth - subVal);
       setWidth(rightData, leftDataWidth);

       setWidth(leftHeader, ganttData.offsetWidth - leftDataWidth - subVal);
       setWidth(rightHeader, leftDataWidth);
    }   

    
}

// mousemove handler of the button for sliding

Slider.MouseSlide = function (e) {
  // button and slider to be draged
  var l = window.sliderDrag.dragLayer;
  var slider = l.slider;
  // we have dragged the slider; for click
  window.sliderDrag.dragged = true;
  // event position
  var evtX = evtY = 0;
  if (!e) e = window.event;
  if (typeof(e.pageX) == 'number') {
    evtX = e.pageX;
    evtY = e.pageY;
  } else if (typeof(e.clientX) == 'number') {
    evtX = e.clientX + (document.body.scrollLeft || 0);
    evtY = e.clientY + (document.body.scrollTop || 0);
  }
  var pos = Math.max(Math.min(evtX - window.sliderDrag.offX,slider.maxSlide),50) + slider.offset;
  // move slider.
  if (slider.orientation == "h") {
    l.style.left = pos + 'px';
  }
  else {
    l.style.top = pos + 'px';
  }
  dojo.stopEvent(e);

  // call slider event handlers
  if (slider.onchange)  slider.onchange(e);
  if (slider.onslide)  slider.onslide(e);
  return false;
}

// make an event handler, ensuring that it is a function

Slider.makeEventHandler = function (f) {
    return (typeof(f) == 'string')? new Function('e',f) : ((typeof(f) == 'function')? f : null);
}

// return a value as a string with a fixed number of decimals

function toDecimals(val,n) {
    if (isNaN(n)) return val;
    for (var m=0; m<n; m++)  val *= 10;
    for (var m=0; m>n; m--) val *= 0.1;
    val = Math.round(val);
    if (val < 0) {
        val = -val;
        var sgn = "-";
    } else {
        var sgn = "";
    }
    var valstr = val.toString();
    if (n>0) {
        while (valstr.length<=n) valstr = "0"+valstr;
        var len = valstr.length;
        valstr = valstr.substring(0,len-n) +"."+ valstr.substring(len-n,len);
    } else if (n<0) {
        for (m=0; m>n; m--) valstr = valstr+"0";
    }
    return sgn+valstr;
}

var ganttSlider = new Slider("slider");