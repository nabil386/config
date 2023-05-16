/**
 * Modification History
 * --------------------
 * 10-Nov-2010 BD  [CR00230549] Initial Version
 */

dojo.provide("curam.debugger");

// Set to true to enable debugging.
var DEBUG = "false"

curam.debugger = {

  /**
   * A logging method that can be turned on or off using the global
   * DEBUG property.
   */
  log: function() {
    if (DEBUG == "true") {
      try{
        console.log.apply(console.log, arguments);
      }catch(e){}
    }
  }
};