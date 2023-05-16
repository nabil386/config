/* Modification History
 * --------------------
 * 11-May-2011 BD [CR00266690] Upgraded for accessibility. All clickable 
 *                             components are now accessible via the keyboard.
 * 
 */

var calendarWeekly = {

  // summary:
  //   Set of functions to manage the Calendar Weekly view 
    
    
  selectWeek: function(/*Event*/ event, /*Node*/ selectedWeek) {

    // summary
    //   When a week is selected by the user:
    //     - highlight the selected tab 
    //     - switch the days view to the current week
    //     - select a new day in the events view.
    if (event.type==="keypress" && !CEFUtils.enterKeyPress(event)){
      return;
    }
    
    if (dojo.hasClass(selectedWeek, "bwc-selected")) {
      // Don't do anything if this is already the selected week.
      return;
    }
    
    // Select the week tab to display
    
    var weekTabsList = dojo.query(".week-tab", selectedWeek.parent);
    dojo.forEach(weekTabsList, function(week) {
      if (week == selectedWeek) {
        dojo.addClass(week,"bwc-selected");
      } else {
        dojo.removeClass(week,"bwc-selected");
      }
    });
    
    // Select the week days to display
    var weekDaysNode = dojo.query(".weeks")[0]
    var weekDaysList = dojo.query(".week-days", weekDaysNode);
    dojo.forEach(weekDaysList, function(week) {
      if (dojo.hasClass(week,"bwc-selected")) {
        dojo.removeClass(week,"bwc-selected");
      } else {
        dojo.addClass(week,"bwc-selected");
      }
    });
  },
    
  highlight: function(/*Event*/ event, /*Node*/ tableCell){

    // summary:
    //  highlight the day that the pointer is currently over.
    //
    // param: Node
    //  the tableCell that is pointed at.
    if (event.type==="keypress" && !CEFUtils.enterKeyPress(event)){
      return;
    }
    
    dojo.addClass(tableCell, "mouseover");
  },

  unhighlight: function(/*Event*/ event, /*Node*/ tableCell){

    // summary:
    //  unhighlight the day that the pointer was just removed from.
    //
    // param: Node
    //  the tableCell that the pointer moved out of.
    if (event.type==="keypress" && !CEFUtils.enterKeyPress(event)){
      return;
    }

    dojo.removeClass(tableCell, "mouseover");
  },

  selectDay: function (/*Event*/ event, /*Node*/ day) {
  
    // summary
    //     Given a node which is assumed to be one of the days in the widget
    //     highlight that day as the currently selected and show the events for 
    //     that date
    //
    // day : Node
    //     The day that was selected by the user.
  
    // console.log("CalendarWeekly.selectDay(day);
    // console.log(day);
    if (event.type==="keypress" && !CEFUtils.enterKeyPress(event)){
      return;
    }
    
    // Remove the selected class from all days and add it to the selected day
    dojo.query(".day-row > *").removeClass("bwc-selected");
    dojo.addClass(day, "bwc-selected");

    // The date is encode as a CSS class in the formant ddmmyyyy
    this.cssClass = "." + dojo.attr(day, "date");
    
    this.selectedDate = dojo.query(this.cssClass)[0];
    this.selectedDateString = dojo.query(".date-display" + this.cssClass)[0];
    this.selectedDateEventsTable = dojo.query(".events-table" + this.cssClass)[0];
    
    dojo.query(".date-display").removeClass("bwc-selected");
    dojo.query(".events-table").removeClass("bwc-selected");
    if (this.selectedDate) {
      
      // Add a class that will cause this date view to display
      if (this.selectedDate) {
        dojo.addClass(this.selectedDate, "bwc-selected");  
      }
      
      
      if (this.selectedDateString) {
        dojo.addClass(this.selectedDateString, "bwc-selected");  
      }
      
      if (this.selectedDateEventsTable) {
        dojo.addClass(this.selectedDateEventsTable, "bwc-selected");
      } else {
        // If no date, show an empty view
        this.noEventsView = dojo.query(".empty")[0];
        if (!dojo.hasClass(this.noEventsView, "bwc-selected")) {
          dojo.addClass(this.noEventsView, "bwc-selected");
      
        }
      }
    }
  }
};

