/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012, 2022. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/* Modification History
 * ====================
 * 05-Sep-2022 BD   [SPM115478]   Fix accessibility issue to the next, previous and
                                  play button.
 * 21-Apr-2021 SH   [RTC254545]   Fix order of control buttons for RTL locales.
 * 09-Aug-2019 CM   [RTC250280]   Fix issue with no announcements breaking home pages.
 * 27-Mar-2019 CM   [RTC243006]   Update announcement player buttons images 
 * 								  instead of background images for accessibility
 *                                issues with high contrast mode.
 * 04-Sep-2012 BD   [CR00339777]  Upgrade for Dojo 1.7. Removed some redundant 
 *                                imports.
 * 12-Apr-2012 BD   [CR00317048]  Improve accessibility of controls by adding
 *                                a role of button and adding alt and title text.
 * 15-Dec-2011 BD   [CR00299689]  Fix issue with hide feature in the startup 
 *                                function. The reference to the renamed
 *                                announcementsWidget object was not updated as 
 *                                part of the work on the 25th-Oct.
 * 28-Oct-2011 BD   [CR00296433]  Move call to open modal using UimDialog to
 *                                CEFUtils.js.  
 * 25 Oct 2011 BD   [CR00296073]  Converted use of openModalDialog to UimDialog
 *                                API. Added declarations to some undeclared
 *                                variables.
 * 01 Jun 2011 SD   [CR00267939]  Updated to prevent over-riding of click 
 *                                functionality by accessibility key presses. 
 * 28 Apr 2011 BD   [CR00264942]  Upgraded for Accessibility. Process keyboard
 *                                events on controls and add title text for the
 *                                link.
 * 11 Oct 2010 BD   [CR00223143]  Completed documentation and formatted code.
 * 08 Oct 2010 BD   [CR00223056]  Add listener for modal close event to restart
 *                                the player. Change the modal page open link
 *                                so that the only parameter that needs to be
 *                                passed is the UIM page-id.
 * 04 Oct 2010 BD   [CR00222455]  Updated to add correct styling and images. 
 * 01 Oct 2010 BD   [CR00221358]  Updated to JS class. Converted to use Dojo 
 *                                text.js widgets.
 * ?? ??? 2008 PD   Initial Version
 */

/**
 * Announcement Player widget.
 * A widget that loops over a set of announcements that are passed to the 
 * container page as a JSON object. The player fades in and fades out the 
 * message and also contains buttons for pausing/playing and navigating through
 * the announcements. When the text is clicked a modal window shows the full
 * text. 
 * 
 */
 dojo.provide("AnnouncementsPlayer");
 require(["dijit/_Widget","dijit/_Templated", "curam/util/Dialog"]);
 
 dojo.declare("AnnouncementsPlayer", [dijit._Widget, dijit._Templated], {
 
   // announcementsId: String
   //    The id of the widget.
   announcementsId:"",
   // currentMessage: String
   //    a String containing the message that is to be shown.
   currentMessage:"",
   // currentState: String
   //    a String that defines the current state of the player, 
   //    playing/paused/showingModal
   currentState:"",
   // fullTextURL: String
   //    a URL that will open a modal window showing the full text.   
   fullTextURL:"",
   // fullTextURLTitleText: String
   //    a String containing alternative text for screen readers.
   fullTextURLTitleText:"",
   // timeIn: Integer
   //    an Integer value that describes in milliseconds the amount of time it 
   //    takes for the message to fade in. Defaulted to 1000 (1 second).
   timeIn: 1000,
   // timeOut: Integer
   //    an Integer value that describes in milliseconds the amount of time it 
   //    takes for the message to fade out. Defaulted to 1000 (1 second).
   timeOut: 1000,
   // timeDisplayed: Integer
   //    an Integer value that describes in milliseconds the amount of time the 
   //    message is displayed for. Defaulted to 3000 (3 seconds).
   timeDisplayed: 3000,
   // altTextLast: String
   //    alt text for accessibility.
   altTextLast: "",
   // altTextLast: String
   //    alt text for accessibility.
   altTextPlay: "",
   // altTextPause: String
   //    alt text for accessibility.
   altTextPause: "",
   // altTextNext: String
   //    alt text for accessibility.
   altTextNext: "",
   // imgPlayPause: String
   //    image url.
   imgPlayPause: "",
   // imgLast: String
   //    image url.
   imgLast: "",
   // imgNext: String
   //    image url.
   imgNext: "",
    // bidiDir: String
    //   text direction
    bidiDir: "",
    // announcementsTarget: object
    //    announcement span element.
    announcementsTarget:"",
   
   // templateString: String
   //    the HTML for this widget.
   //BEGIN, CR00448886, YF
   templateString:
     "<div dojoAttachPoint='announcementsWidget' id=${announcementsId} class='announcements' role='application' aria-labelledby='announcements' timeIn='${timeIn}' timeDisplayed='${timeDisplayed}' timeOut='${timeOut}' fullTextURL='${fullTextURL}'>" +
       "<span class='text-container' dojoAttachEvent='onmouseout:release'>" +
         "<span aria-hidden='true' class='announcement-text' dojoAttachPoint='textDisplay' id='announcements'>" +
          "<b dojoAttachPoint='dateTime'></b>" +
          "<a href='' title='${fullTextURLTitleText}' tabindex='0' dojoAttachPoint='text' dojoAttachEvent='onmouseover:hold, onfocus:hold, onkeypress:showFullMessageText, onclick:showFullMessageText'></a>" +
         "</span>" +
       "</span>" +
       "<span class='controls'>" +
         "<span role='button' tabindex='0' title='${altTextLast}' alt='${altTextLast}' id='last' class='control last' dojoAttachEvent='onmouseover:_mouseEventLast, onmouseout:_mouseEventLast,onmousedown:_mouseEventLast,onmouseup:_mouseEventLast,onkeyup:_mouseEventLast, onkeydown:_mouseEventLast, onblur:_mouseEventLast, onfocus:_mouseEventLast'><img src='${imgLast}' alt=''></span>" +
         "<span role='button' tabindex='0' title='${altTextPause}' alt='${altTextPause}' id='pausePlay' class='control pause' dojoAttachPoint='pausePlay' dojoAttachEvent='onmouseover:_mouseEventPausePlay, onmouseout:_mouseEventPausePlay,onmousedown:_mouseEventPausePlay,onmouseup:_mouseEventPausePlay, onkeyup:_mouseEventPausePlay, onkeydown:_mouseEventPausePlay, onblur:_mouseEventPausePlay, onfocus:_mouseEventPausePlay, onfocusout:_removeAriaLive'><img src='${imgPlayPause}' alt=''></span>" +          
         "<span role='button' tabindex='0' title='${altTextNext}' alt='${altTextNext}' id='next' class='control next' dojoAttachEvent='onmouseover:_mouseEventNext, onmouseout:_mouseEventNext,onmousedown:_mouseEventNext,onmouseup:_mouseEventNext,onkeyup:_mouseEventNext, onkeydown:_mouseEventNext, onblur:_mouseEventNext, onfocus:_mouseEventNext'><img src='${imgNext}' alt=''></span>" +
       "</span>" +
      "</div>",
   //END, CR00448886, YF              
   constructor: function(args) {
 
    // summary:
    //     Create a new Announcements player
    // description
    //     The announcements player takes a JSON object describing 0 or more 
    //     announcements and displays them in a player. The player will fade 
    //     the message in, pause and fade out, cycling through all the
    //     announcements.
 
    this.bidiDir = args.dir;
    this.announcementsId=args.announcementsId;
    this.altTextPlay = args.altTextPlay;
 
    this.buttonPlay = this._getImages("play")[0];
    this.buttonPause = this._getImages("pause")[0];
    this.buttonNext = this._getImages("next")[0];
    this.buttonLast = this._getImages("last")[0];
    
    if(this.buttonPause && this.buttonNext && this.buttonLast){
        this.imgPlayPause= this.buttonPause.img[0];
        this.imgNext= this.buttonNext.img[0];
        this.imgLast= this.buttonLast.img[0];
    }
 
    this.fullTextURL = args.fullTextURL;  
    if (args.timeIn) {
      this.timeIn = args.timeIn;  
    }
    if (args.timeOut) {
      this.timeOut = args.timeOut;
    }
    if (args.timeDisplayed) {
      this.timeDisplayed = args.timeDisplayed;
    }
    this.currentMessageIdx = 0;
   },
   
   postCreate: function(){

    // reverse the order of the last/play/next controls for RTL locales
    if (this.bidiDir == "rtl") {
        var controls = document.querySelectorAll(".controls")[0];
        var items = controls.children;
        var newControls = document.createDocumentFragment();

        newControls.appendChild(items[2]);
        newControls.appendChild(items[1]);
        newControls.appendChild(items[0]);

        controls.innerHTML = null;
        controls.appendChild(newControls);
    }

    if (!this.announcementsId) {
     console.error("no announcement id");
     return;
    }
   },
   
   startup: function() {
     
     // summary:
     //    Initialize the widget.
     //
     // description:
     //    Configure the player using the parameters passed to it.
     announcementsTarget = dojo.byId("announcements");
     if(announcements.announcements.length == 0) {
       dojo.addClass(this.announcementsWidget, "hide-announcements");
       return;
     }
     // Create a delay on startup to give the page tme to
     // draw before attempting the animation. Otherwise the
     // animation is stuttery.
     dojo.subscribe("text/start", this, function() {
       this.currentMessageIdx = announcements.announcements.length;
       this.play();
     });
     setTimeout("dojo.publish(\"text/start\");", 3000);
   },
 
   getMessage: function(){
 
     // summary:
     //    Get the current message. 
     
      return announcements.announcements[this.currentMessageIdx];
   },
   
   getNextMessage: function(){
 
     // summary:
     //    Get the next message. 
 
     this._incrementMessageIdx();
     var announcement = announcements.announcements[this.currentMessageIdx];
     return announcement;
   },
   
   getLastMessage: function(){
 
     // summary:
     //    Get the last message. 
 
     this._decrementMessageIdx();
     var announcement = announcements.announcements[this.currentMessageIdx];
     return announcement;
   },   
   
   _incrementMessageIdx: function() {
     
     // summary:
     //    Update the current message id by incrementing it. 
     //
     // description:
     //    Used in conjucntion with the next function to control the state of 
     //    the player.
     
     this.currentMessageIdx++;
     if (this.currentMessageIdx >= announcements.announcements.length){
       this.currentMessageIdx = 0;
     }
   },
   
   _decrementMessageIdx: function() {
 
     // summary:
     //    Update the current message id by decrementing it. 
     //
     // description:
     //    Used in conjucntion with the last function to control the state of 
     //    the player.
     
     this.currentMessageIdx--;
     if (this.currentMessageIdx < 0){
       this.currentMessageIdx = announcements.announcements.length -1;
     }
   },
   
   play: function() {
 
     // summary:
     //    Play the announcements. 
     //
     // description:
     //    Gets the next message and starts the player. Creates a listener that 
     //    will get the next message and play it when the current one ends. 
     
     this.currentState="playing";
     dojo.removeClass(this.pausePlay, "play");
     dojo.attr(this.pausePlay, "alt", this.altTextPause); 
     dojo.attr(this.pausePlay, "title", this.altTextPause); 
 
     var message = this.getNextMessage();
     this.displayMessage(message);
     dojo.style(this.textDisplay, "opacity", "0");
     this.player = dojo.fx.chain([
         dojo.fadeIn({
           node: this.textDisplay, 
           duration: this.timeIn
         }),
         dojo.fadeOut({ 
           node: this.textDisplay, 
           delay: this.timeDisplayed,  
           duration: this.timeOut 
         })
     ]);
 
     // Loop over.
     var playConnection = dojo.connect(this.player, "onEnd", this, function(){
       dojo.disconnect(playConnection);
       this.play();  
     });
     this.player.play(); // play the animation
     return;
   },
 
   _pauseOrPlay: function() {
 
     // summary:
     //    Decides whether to pause or play the player. 
     //
     // description:
     //    Based on the current state decide what to do, play or pause.
     
     if (this.currentState=="playing"){
       this.pause();
     } else {
       this.play();
     }
   },
   
   hold: function(){
 
     // summary:
     //    Hold the player on the current message. 
     //
     // description:
     //    Used in conjunction with the release function. This provides the 
     //    functionality required for the mouseover/mouseout events, where the 
     //    player must be temporarily paused.
 
     // Don't bother if already paused.
     if(this.currentState!="paused"){
       dojo.addClass(this.pausePlay, "play");
       var button = dojo.byId("pausePlay");
       button.children[0].src = this.buttonPlay.img[0];
       this.show();
       this.player.pause();
     }
   },
   
   release: function(){
 
     // summary:
     //    Release the player from the hold state. 
     //
     // description:
     //    Used in conjunction with the hold function. This provides the 
     //    functionality required for the mouseover/mouseout events, where the 
     //    player must be temporarily paused.
     
     // Only play if the announcements were playing previous to the hold.
     if(this.currentState=="playing"){
       dojo.removeClass(this.pausePlay, "play");
       var button = dojo.byId("pausePlay");
       button.children[0].src = this.buttonPause.img[0];
       this.player.play();
     }
   },
   
   show: function() {
     
     // summary:
     //    Get the next message and show it.
     //
     // description:
     //    Uses the internal getNextMessage function to return the next message 
     //    and then uses the pause function to show that message in a paused 
     //    state.
     
     if (this.player) {
       this.player.stop();
     }
     var message = this.getMessage();
     this.displayMessage(message);
     dojo.attr(announcementsTarget, "aria-live", "polite"); 
   },
   
   pause: function() {
     
     // summary:
     //    Pause the player and show the current message.
     //
     // description:
     //    Pauses the animation and uses the internal function show() to display
     //    the message.
     
     dojo.addClass(this.pausePlay, "play");
     dojo.attr(this.pausePlay, "alt", this.altTextPlay);
     dojo.attr(this.pausePlay, "title", this.altTextPlay);
     var button = dojo.byId("pausePlay");
     button.children[0].src = this.buttonPlay.img[0];
     this.show();
     if (this.player) {
       this.player.pause();
     }
     this.currentState="paused";
   },
 
   last: function() {
 
     // summary:
     //    Get the last message and show it.
     //
     // description:
     //    Uses the internal getLastMessage function to return the next message 
     //    and then uses the pause function to show that message in a paused 
     //    state.
 
     this.currentMessage = this.getLastMessage();
     this.pause();
   },
   
   next: function() {
     
     // summary:
     //    Get the next message and show it.
     //
     // description:
     //    Uses the internal getNextMessage function to return the next message 
     //    and then uses the pause function to show that message in a paused 
     //    state.
     
     this.currentMessage = this.getNextMessage();
     this.pause();
   },
 
   displayMessage: function(message){
     
     // summary:
     //    Just display the given message.
     //
     // description:
     //    Receives a message to be displayed in the player. Creates the link to
     //    the modal from the parameters of the message object.
     //
     // parameter:
     //    message, a javascript object containing the message details.
          
     dojo.style(this.textDisplay, "opacity", "1");
     if (message.bidiDir) {
         dojo.style(this.textDisplay, "direction", message.bidiDir );
         dojo.style(this.textDisplay, "display", "inline-block" );
         dojo.style(this.textDisplay, "textAlign", "start" );
     }
     this.dateTime.innerHTML = message.date + " " + message.time + " ";
     this.text.innerHTML = message.text;
     var link = this.fullTextURL + "Page.do?o3ctx=4096&announcementID=" + message.id;
     dojo.attr(this.text, "href", link);
   },
   
   showFullMessageText: function(event){
     
     // summary:
     //   Display the full announcement text in a modal window.
 
     // description
     //   Pauses the player and loads a modal window which will show the message.
     //   Creates a listener on the close event of the modal window to start 
     //   the player again.
     
     var doesKeyPressExist = CEFUtils.keyPressExist(event);
     var wasReturnKeyPressed = CEFUtils.enterKeyPress(event);
     
     // Ignore if the key press wasn't on the return key
     if (doesKeyPressExist == true && wasReturnKeyPressed !== true) {
       return
     }
       
     if(this.fullTextURL.length==0){
       return;
     }
     this.currentState = "showingModal";
 
     var messageDialog = CEFUtils.showInModal(event);
     var _player = this.player;
 
     // Pause on open
     messageDialog.registerOnDisplayHandler(function() {
       _player.pause();
     });
     
     // Play on close
     messageDialog.registerBeforeCloseHandler(function(){
         _player.play();
         
     });
   },
 
   _validKeyPress: function (/*Event*/ event){
 
     // summary: 
     //    If this was a key press check if it was the 'enter' button.
     //    Exit if not the enter button.
     // parameter:
     //    event, an event that just occured on the 'pausePlay' element. 
     if (event.type === "keyup" || event.type === "keydown") {
       if (CEFUtils.enterKeyPress(event) !== true) {
         return false;
       }
     }
     return true;
   },
   
   _actionEvent: function(event) {
 
     // summary: 
     //    Check if the event is a mouseup or keyup. These 2 events are 
     //    consider the action events. Only when a user releases the mouse 
     //    button or the keyboared button has the event occured.
     // parameter:
     //    event, an event that just occured. 
     
     return (event.type=="mouseup" || event.type=="keyup");
   },
   
   _mouseEventPausePlay: function(/*Event*/ event){
 
     // summary:
     //    Catch and process a mouse or keyboard event on the 'pausePlay' 
     //    element.
     // parameter:
     //    event, an event that just occured on the 'pausePlay' element.      
     if (!this._validKeyPress(event)) {
       return;
     }
     
     // Style the button based on the event.
     this._pausePlayStyling(event);
     
     // Take the action, but only if it was a mouseup or keyup event.
     if (this._actionEvent(event)) {
       this._pauseOrPlay();
     }
   },
   
   _mouseEventLast: function(/*Event*/ event){
 
     // summary:
     //    Catch and process a mouse event on the 'last' element.
 
     if (!this._validKeyPress(event)) {
       return;
     }
     
     this._lastNextStyling("last", event);
     
     // Take the action, but only if it was a mouseup or keyup event.
     if (this._actionEvent(event)) {
       this.last();
     }
   },
   
   _mouseEventNext: function(/*Event*/ event){
     
     // summary:
     //    Catch and process a mouse event on the 'next' element.
     if (!this._validKeyPress(event)) {
       return;
     }
     this._lastNextStyling("next", event);
     
     // Take the action, but only if it was a mouseup or keyup event.
     if (this._actionEvent(event)) {
       this.next();
     }
   },
 
   _pausePlayStyling: function(/*Event*/ event){
     
     // summary:
     //   Apply styling to the play/pause buttons based on an Event.
     //
     // description:
     //   When a user invokes an event on the play/pause button the event is 
     //   passed to this function which will decide based on the current state 
     //   of the button what new style should be applied. E.g. if the button is
     //   currently in the play state and the user rolls over the button then the
     //   button is styled with the play roll over. Hoever, if the button was in
     //   the pause state then a different style would be applied to show the 
     //   pause roll over.
     //
     // 
     var button = dojo.byId("pausePlay");
     if (event.type=="mouseover" || event.type=="focus") {
       if (dojo.hasClass(button, "play")) {
         dojo.removeClass(button, "play");
         dojo.addClass(button, "play-roll-over");
         button.children[0].src = this.buttonPlay.imgFocus[0];
       } else {
         dojo.addClass(button, "pause-roll-over");
         button.children[0].src = this.buttonPause.imgFocus[0];
       }
     } else if (event.type=="mouseout" || event.type=="blur") {
       if (dojo.hasClass(button,"play-roll-over")) {
         dojo.removeClass(button, "play-roll-over");
         dojo.addClass(button, "play");
         button.children[0].src = this.buttonPlay.img[0];
       } else {
         dojo.removeClass(button,"pause-roll-over");
         button.children[0].src= this.buttonPause.img[0];
       }
     } else if (event.type=="mousedown" || event.type=="keydown") {
       if (dojo.hasClass(button, "pause-roll-over")) {
         dojo.removeClass(button, "pause-roll-over");
         dojo.addClass(button, "pause-select");
         button.children[0].src = this.buttonPause.imgFocus[0];
       } else {
         dojo.removeClass(button, "play-roll-over");
         dojo.addClass(button, "play-select");
         button.children[0].src= this.buttonPlay.imgFocus[0];
       }
     } else if (event.type=="mouseup" || event.type=="keyup") {
       if (dojo.hasClass(button, "play-select")){
         dojo.removeClass(button, "play-select");
         dojo.addClass(button, "pause-roll-over");
         button.children[0].src = this.buttonPause.imgFocus[0];
       } else {
         dojo.removeClass(button,"pause-select");
         dojo.addClass(button, "play-roll-over");
         button.children[0].src = this.buttonPlay.imgFocus[0];
       }
     }
   },
   
   _lastNextStyling: function(/*String*/ buttonId, /*Event*/ event){
     
     // summary:
     //   Apply styling to the last/next buttons based on an Event.
     //
     // description:
     //   When a user invokes an event on the last or next buttons the event is 
     //   passed to this function which will decide based on the current state 
     //   of the button what new style should be applied. 
     
     var mouseOver = buttonId + "-roll-over";  
     var mouseDown = buttonId + "-select";
     var mouseOut = buttonId;
     var button = dojo.byId(buttonId)
     if (event.type=="mouseover" || event.type=="focus") {
        dojo.addClass(button, mouseOver);
        button.children[0].src = this._getLastNextImage(buttonId, true);
     } else if (event.type=="mousedown" || event.type=="keydown") {
       dojo.removeClass(button, mouseOver);
       dojo.addClass(button, mouseDown);
       button.children[0].src = this._getLastNextImage(buttonId, true);
     } else if (event.type=="mouseup" || event.type=="keyup") {
       dojo.removeClass(button, mouseDown);
       dojo.addClass(button, mouseOver);
       button.children[0].src = this._getLastNextImage(buttonId, true);
     } else if (event.type=="mouseout" || event.type=="blur") {
       dojo.removeClass(button, mouseOver);
       button.children[0].src = this._getLastNextImage(buttonId, false);
     }
     
    
   },
   
   /**
    * Returns the image for the correct button type passed. 
    * 
    * @param buttonType The string reference to the button.
    */
   _getImages: function(/*String*/ buttonType){
       
       return  announcements.images.filter(function(image){
            return image.type == buttonType;
          });
   },
   
   /**
    * Returns the correct last button or next button image.
    * 
    * @param buttonId The string reference to the button.
    * @param isFocusedInd Is true if focused image version required.
    */
    _getLastNextImage: function(/*String*/ buttonId, /*boolean*/ isFocusedInd){
    
     if(isFocusedInd){
       if(buttonId=="next"){
         return this.buttonNext.imgFocus[0];
       }else{
         return this.buttonLast.imgFocus[0];
       }
     }else if(buttonId=="next"){
       return this.buttonNext.img[0];  
     }else {
       return this.buttonLast.img[0];
     }
    },
    
    /**
    * Remove the aria-live attribute from the announcementsTarget.
    */
    _removeAriaLive: function (){
    	dojo.removeAttr(announcementsTarget, "aria-live"); 
    }
 });