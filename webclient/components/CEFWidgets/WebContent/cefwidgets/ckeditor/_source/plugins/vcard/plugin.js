/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

/**
 * @fileOverview VCard widget used by {@link CKEDITOR.plugins.mentions} plugin to insert mention.
 */

'use strict';


( function() {
	var widgetClass = 'cke_vcard',
		widgetIdAttr = 'data-cke-inner-id';

	CKEDITOR.plugins.add( 'vcard', {
		requires: 'widget',

		init: function( editor ) {
			var widgetRegister = {},
				widgetInstances = {},
				innerTemplateHtml = editor.config.mentions_mentionTemplate || '{name}',
				innerTemplate = innerTemplateHtml ? new CKEDITOR.template( innerTemplateHtml ) : null;

			// Empty widget register on mode switch. When back
			// to WYSIWYG mode all widgets are recreated with new inner ids.
			editor.on( 'beforeSetMode', function() {
				widgetRegister = {};
				widgetInstances = {};
			} );

			// Monitor widgets and check if after change event any were removed/added.
			editor.on( 'change', function() {
				var widgets = editor.editable().find( '.' + widgetClass ),
					widgetIds = CKEDITOR.tools.objectKeys( widgetRegister ),
					activeWidgets = {},
					widgetId;

				// Check for existing widgets.
				for ( var i = 0; i < widgets.count(); i++ ) {
					widgetId = widgets.getItem( i ).getAttribute( widgetIdAttr );
					if ( widgetId ) {
						activeWidgets[ widgetId ] = true;
					}
				}

				// Check if any widgets were removed.
				for ( var j = 0; j < widgetIds.length; j++ ) {
					widgetId = widgetIds[ j ];
					// Widget was removed.
					if ( !activeWidgets[ widgetId ] && isWidgetActive( widgetId ) ) {
						widgetRegister[ widgetId ] = false;
						editor.fire( 'mentionRemove', widgetInstances[ widgetId ] );
						widgetInstances[ widgetId ] = null;
					}
				}
			} );

			editor.widgets.add( 'vcard', {
				template: '<span class="' + widgetClass + '"></span>',
				draggable: !!editor.config.mentions_draggable,

				data: function() {

					// Generate inner id if widget is newly added and does not have id
					// or if widget was copied so its inner id needs to be changed.
					if ( !this.element.getAttribute( widgetIdAttr ) ||
						countWidgets( this.element.getAttribute( widgetIdAttr ) ) > 1 ) {

						this.element.setAttribute( widgetIdAttr, generateInnerId() );
					}

					var innerId = this.element.getAttribute( widgetIdAttr );
					widgetRegister[ innerId ] = true;
					widgetInstances[ innerId ] = this;

					this.element.addClass( widgetClass );
					this.element.setHtml( innerTemplate.output( this.data ) );
				},

				downcast: function( element ) {
					// Store data used in widget as JSON so it can be upcasted from source mode/html.
					element.attributes[ 'data-vcard-item' ] = getTemplateDataJSON( this.data, innerTemplateHtml );
					return element;
				},

				upcast: function( element, data ) {
					if ( element.name == 'span' && element.attributes[ 'data-vcard-item' ] ) {
						CKEDITOR.tools.extend( data, parseTemplateDataJSON( element.attributes[ 'data-vcard-item' ] ) );
						return true;
					}
					return false;
				}
			} );


			function generateInnerId() {
				return new Date().getTime();
			}

			function isWidgetActive( widgetInnerId ) {
				return widgetRegister[ widgetInnerId ] === true;
			}

			function countWidgets( widgetInnerId ) {
				var count = 0,
					widgets = editor.editable().find( '.' + widgetClass );

				for ( var i = 0; i < widgets.count(); i++ ) {
					if ( widgets.getItem( i ).getAttribute( widgetIdAttr ) == widgetInnerId ) {
						count++;
					}
				}
				return count;
			}

			function getTemplateDataJSON( data, template ) {
				var i = 0,
					usedData = {},
					keys = CKEDITOR.tools.objectKeys( data );

				for ( i; i < keys.length; i++ ) {
					if ( keys[ i ] != 'classes' && template.indexOf( '{' + keys[ i ] + '}' ) !== -1 ) {
						usedData[ keys[ i ] ] = data[ keys[ i ] ];
					}
				}
				return encodeURIComponent( JSON.stringify( usedData ) );
			}

			function parseTemplateDataJSON( data ) {
				return JSON.parse( decodeURIComponent( data ) );
			}
		}
	} );
} )();

/**
 * Defines if mention item is draggable.
 *
 *		config.mentions_draggable = true;
 *
 * @cfg {Boolean} [mentions_draggable=false]
 * @member CKEDITOR.config
 */

/**
 * HTML template which will be used instead of default template to insert mentions.
 *
 *		config.mentions_mentionTemplate = '<span>{name}</span>';
 *
 * @cfg {String} [mentions_mentionTemplate=see source]
 * @member CKEDITOR.config
 */

/**
 * Fired after mention is removed.
 *
 * @event mentionRemove
 * @member CKEDITOR.editor
 * @param {CKEDITOR.plugins.widget} item Removed mention instance.
 */
