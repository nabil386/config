/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/** Modifications
 * ============
 * 04-Oct-2012 BD [CR00345902] Upgrade code for Dojo 1.7
 * 20-Jun-2012 DG [CR00329440] Added localization support.
 */
dojo.provide("curam.calendar");

require(["curam/util/ResourceBundle"]);
dojo.requireLocalization("curam.application", "calendar");
var bundle = new curam.util.ResourceBundle("calendar");

// Define the activity html
var prevMonthActivity = new Array(3);
var currMonthActivity = new Array(3);
var nextMonthActivity = new Array(3);

	currMonthActivity["1"] = "<TR><TD class=\"activityTitle\"> "+bundle.getProperty("currMonthActivity.1.activityTitle.1")+" </TD></TR>" +
			  "<TR><TD class=\"activitydetails\"> "+bundle.getProperty("currMonthActivity.1.activitydetails.1")+"</TD></TR>" +
			  "<TR><TD class=\"activityTitle\"> "+bundle.getProperty("currMonthActivity.1.activityTitle.2")+" </TD></TR>" +
			  "<TR><TD class=\"activitydetails\"> "+bundle.getProperty("currMonthActivity.1.activitydetails.2")+" </TD></TR></TBODY>";
	currMonthActivity["19"] = "<TR><TD class=\"activityTitle\"> "+bundle.getProperty("currMonthActivity.19.activityTitle.1")+" </TD></TR>" +
			   "<TR><TD> "+bundle.getProperty("currMonthActivity.19.activityTitle.2")+"</TD></TR>";
	currMonthActivity["30"] = "<TR><TD class=\"activityTitle\"> "+bundle.getProperty("currMonthActivity.30.activityTitle.1")+" </TD></TR>" +
			   "<TR><TD> "+bundle.getProperty("currMonthActivity.30.activityTitle.2")+"</TD></TR>";	   

var monthlyActivity = new Array(3);
monthlyActivity[10] = prevMonthActivity;
monthlyActivity[11] = currMonthActivity;
monthlyActivity[0] = nextMonthActivity;


function displayActivity(year, month, day)
{
  var date = new Date(year, month, day);
  var activityTitleHtml = '<B>' + getDateStrWithDOW(date) + '</B>';

  if(monthlyActivity[month][day] != null) {
    document.getElementById('activitiesDetails').innerHTML = '<TABLE><THEAD><TD>' + activityTitleHtml + '</TD><THEAD><TBODY>' + monthlyActivity[month][day] + '</TBODY></TABLE>';
  }
  else {
    document.getElementById('activitiesDetails').innerHTML = '<TABLE><THEAD><TD>' + activityTitleHtml + '</TD><THEAD></TABLE>';
  }

}


function getDay(intDay){
  var DayArray = new Array(bundle.getProperty("getDay.DayArray.Sunday"), bundle.getProperty("getDay.DayArray.Monday"), bundle.getProperty("getDay.DayArray.Tuesday"), bundle.getProperty("getDay.DayArray.Wednesday"),
                         bundle.getProperty("getDay.DayArray.Thursday"), bundle.getProperty("getDay.DayArray.Friday"), bundle.getProperty("getDay.DayArray.Saturday"));
  return DayArray[intDay];
}

function getMonth(intMonth){
  var MonthArray = new Array(bundle.getProperty("getMonth.MonthArray.January"), bundle.getProperty("getMonth.MonthArray.February"), bundle.getProperty("getMonth.MonthArray.March"),
                             bundle.getProperty("getMonth.MonthArray.April"), bundle.getProperty("getMonth.MonthArray.May"), bundle.getProperty("getMonth.MonthArray.June"),
                             bundle.getProperty("getMonth.MonthArray.July"), bundle.getProperty("getMonth.MonthArray.August"), bundle.getProperty("getMonth.MonthArray.September"),
                             bundle.getProperty("getMonth.MonthArray.October"), bundle.getProperty("getMonth.MonthArray.November"), bundle.getProperty("getMonth.MonthArray.December"));
  return MonthArray[intMonth];
}

function getDateStrWithDOW(date){
  var todayStr = getDay(date.getDay()) + ", "
  todayStr += getMonth(date.getMonth()) + " " + date.getDate();
  return todayStr;
}

function renderCalendar(currentDate){

	// Populate the days of the week

	document.write ("<TR>");
	document.write ("<TD ALIGN=center>"+bundle.getProperty("renderCalendar.Populate.DaysofWeek.Monday")+"</TD>");
	document.write ("<TD ALIGN=center>"+bundle.getProperty("renderCalendar.Populate.DaysofWeek.Tuesday")+"</TD>");
	document.write ("<TD ALIGN=center>"+bundle.getProperty("renderCalendar.Populate.DaysofWeek.Wednesday")+"</TD>");
	document.write ("<TD ALIGN=center>"+bundle.getProperty("renderCalendar.Populate.DaysofWeek.Thursday")+"</TD>");
	document.write ("<TD ALIGN=center>"+bundle.getProperty("renderCalendar.Populate.DaysofWeek.Friday")+"</TD>");
	document.write ("<TD ALIGN=center>"+bundle.getProperty("renderCalendar.Populate.DaysofWeek.Saturday")+"</TD>");
	document.write ("<TD ALIGN=center>"+bundle.getProperty("renderCalendar.Populate.DaysofWeek.Sunday")+"</TD>");
	document.write ("</TR>");

	// need to get the day index of the current day
	var startDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
	var startDay = startDate.getDay();

	// need to synchronise the day value so that
	// it starts with monday as the first day of the week
	if(startDay == 0) startDay = 6;
	else startDay--;


	var numberofDaysInCurrentMonth = 32 - new Date(currentDate.getYear(), currentDate.getMonth(), 32).getDate();
	prevMonth = currentDate.getMonth() - 1;
	var numberofDaysInpreviousMonth = 32 - new Date(currentDate.getYear(), prevMonth, 32).getDate();


	dayCounter = 1;
	for (i=1; i <= numberofDaysInCurrentMonth; i++){

	// if the 1st of the month does not start on a monday,
	// then include the last few days of the previous month
	// in the calendar table.
	if(startDay > 0 && i==1) {

	// populate the first days of the calendar
	// up to the start day of this month
	for (j=0; j <= startDay -1 ; j++){

	// For the first day, start the table row
	if(j==0){
	  document.write ("<TR>");
	}

	document.write ("<TD ALIGN=center>"+ ((numberofDaysInpreviousMonth - startDay)+j+1) +"</TD>");
	dayCounter++;
	}

	}


	// Determine the styling for a day that has an activity
	date = i;
	if(monthlyActivity[currentDate.getMonth()][i] != null){

	  date = '<B>' + i + '</B>';
	}


	// When the first day of the week, start a new row
	if (dayCounter == 1) {

	 if(startDay == 0) {
	 document.write ("<TR>");
	 }

	 document.write ("<TD " + "onclick=\"displayActivity(" + currentDate.getFullYear() + "," +  currentDate.getMonth() + "," + i +")\""+" ALIGN=center>"+ date +"</TD>");
	 dayCounter++;
	}
	// When the last day of the week, end the row
	else if (dayCounter == 7) {
	 document.write ("<TD " + "onclick=\"displayActivity(" + currentDate.getFullYear() + "," +  currentDate.getMonth() + "," + i +")\""+" ALIGN=center>"+ date +"</TD>");
	 document.write ("</TR>");
	 dayCounter = 1;
	}
	// Its during the week so add just the day
	else {
	document.write ("<TD " + "onclick=\"displayActivity(" + currentDate.getFullYear() + "," +  currentDate.getMonth() + "," + i +")\""+" ALIGN=center>"+ date +"</TD>");
	dayCounter++;
	}


	// If the last day of the month is not on a saturday,
	// then include some dates from the next month
	if(numberofDaysInCurrentMonth == i && dayCounter !=7)

	for (k=1; k <= ((7-dayCounter)+1); k++){
	  document.write ("<TD " + "onclick=\"displayActivity(" + currentDate.getFullYear() + "," +  currentDate.getMonth() + "," + k +")\""+" ALIGN=center>"+ k +"</TD>");

	  if(k == ((7-dayCounter)+1)) {
	   document.write ("</TR>");
	  }

	}
}
}
