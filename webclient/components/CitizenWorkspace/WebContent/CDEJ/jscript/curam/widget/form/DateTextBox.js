define(["dojo/_base/declare",
        "idx/form/DateTextBox",
        "dojo/date/locale",
        "dojo/dom",
        "dojo/query"],

        function(declare, OneUIDateTextBox, locale, dom, query){

          return declare("curam.widget.form.DateTextBox", OneUIDateTextBox, {
            curamFormat: {selector: 'date', datePattern: jsDF, locale: dojo.config.locale},
            value: "", // prevent parser from trying to convert to Date object
            postMixInProperties: function(){ // change value string to Date object
                this.inherited(arguments);
                // convert value to Date object
                this.value = locale.parse(this.value, this.curamFormat);
            },
            postCreate: function() {
              this.inherited(arguments);
              // temp fix for bug in one ui widget. This ensures the hidden field is set when the widget is created.
              // the bug doesn't exist in the ootb dijit, only the oneui version.
              // TODO: see if this is fixed in IDX 1.3

              //this._setValueAttr(this.value, true);
              var hiddenField =
                query("input[type='hidden'][name='" + this.id + "']",
                      this.domNode)[0];
              if (hiddenField) {
                hiddenField.value = locale.format(this.value, this.curamFormat);
              }
            },
            // To write back to the server in Curam format,
            // override the serialize method:
            serialize: function(dateObject, options){
                return locale.format(dateObject, this.curamFormat);
            }
          });

});