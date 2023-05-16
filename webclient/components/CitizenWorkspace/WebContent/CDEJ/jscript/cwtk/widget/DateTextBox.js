/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
    "dojo/_base/declare",
    "idx/form/DateTextBox",
    "dojo/date/locale"
], function(declare, DateTextBox) {
	return declare([idx.form.DateTextBox], {

		serializationFormat: null,

		serialize: function(value, options)
		{
			if(this.serializationFormat != null) {
				value = dojo.date.locale.format(
					value,
					{selector: "date", fullYear: true, datePattern: this.serializationFormat}
				);
			}
			return value;
		}

	});
});
