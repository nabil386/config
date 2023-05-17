/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

'use strict';

( function() {

	CKEDITOR.plugins.add( 'autocompletearia', {
		lang: 'en',

		beforeInit: function( editor ) {
			editor.on( 'autoCompleteAttached', function( evt ) {
				evt.data.ariaHelper = new CKEDITOR.plugins.autocompletearia( evt.data.view, evt.editor );
			} );
		}
	} );

	/**
	 * Helper class for generating aria messages informing screen readers about View updates/changes.
	 * The Autocomplete panel always works in the unfocused state (the focus always stays inside the editor to
	 * make further typing possible). There is no reliable way to make screen readers read unfocused elements
	 * in the same manner as if they were focused (e.g. aria-live does not respect role and aria-owns does not work
	 * across documents) and that is the main reason why this class was created - to make updates in the
	 * Autocomplete panel announced by screen readers in as similar way to its native announcements as possible.
	 *
	 * @class CKEDITOR.plugins.autocompletearia
	 * @constructor Creates the autocompletearia instance.
	 * @param {CKEDITOR.plugins.autocomplete.view} view The autocomplete view instance on which listen to the change events.
	 * @param {CKEDITOR.editor} editor The active editor instance.
	 */
	function AutocompleteAria( view, editor ) {
		/**
		 * Element which stores aria status text announced by screen readers.
		 *
		 * @readonly
		 * @property {CKEDITOR.dom.element}
		 */
		this.ariaElement = null;

		/**
		 * Whether the panel is opened. The aria status depends on previous state (open/update).
		 *
		 * The reason to keep this property is that generic autocomplete does not contain information whether
		 * the autocomplete is already open or is it just being updated.
		 *
		 * @readonly
		 * @property {Boolean}
		 */
		this.opened = null;

		/**
		 * Language object shortcut. Use to create aria status text.
		 *
		 * @readonly
		 * @property {Object}
		 */
		this.lang = editor.lang.autocompletearia;

		/**
		 * Language templates use to create aria status text.
		 *
		 * @readonly
		 * @property {Object}
		 */
		this.langTemplates = {
			resultsPlural: new CKEDITOR.template( this.lang.resultsPlural ),
			listLength: new CKEDITOR.template( this.lang.listLength )
		};

		/**
		 * Aria status text change is delayed to work better with screen readers
		 * and to not attack user with to many announcement during quick interactions.
		 * The timeoutId is used to mange those delays.
		 *
		 * @readonly
		 * @property {Number}
		 */
		this.timeoutId = null;


		this.attach( view );
	}

	AutocompleteAria.prototype = {
		/**
		 * Creates the element ({@link #ariaElement}) which
		 * holds aria status and adds it to the DOM. Initializes view event listeners.
		 *
		 * @private
		 * @param {CKEDITOR.plugins.autocomplete.view} view The autocomplete view instance on which listen to the change events.
		 */
		attach: function( view ) {
			var ariaElement = new CKEDITOR.dom.element( 'span', CKEDITOR.document );

			ariaElement.setAttributes( {
				'aria-live': CKEDITOR.env.ie ? 'assertive' : 'polite',
				'aria-atomic': true,
				'role': 'status'
			} );

			// Inline styles to be independent of external stylesheets.
			ariaElement.setStyles( {
				'border': 0,
				'clip': 'rect(0 0 0 0)',
				'height': '1px',
				'margin': '-1px',
				'overflow': 'hidden',
				'padding': '0',
				'position': 'absolute',
				'width': '1px'
			} );

			this.ariaElement = ariaElement;
			CKEDITOR.document.getBody().append( this.ariaElement );

			view.on( 'opened', this.onListOpened, this );
			view.on( 'closed', this.onListClosed, this );
			view.on( 'updated', this.onListUpdated, this );
			view.on( 'selected', this.onItemSelected, this );
		},

		/**
		 * The function registered by the {@link #attach} method
		 * as the {@link CKEDITOR.plugins.autocomplete.view#opened} event listener.
		 * Updates aria status when the autocomplete list was opened.
		 *
		 * @private
		 * @param {CKEDITOR.eventInfo} evt
		 */
		onListOpened: function( evt ) {
			if ( !this.opened ) {
				this.opened = true;
				this.updateStatus( this.getOpenInfo( evt.data.size ) );
			}
		},

		/**
		 * The function registered by the {@link #attach} method
		 * as the {@link CKEDITOR.plugins.autocomplete.view#closed} event listener.
		 *
		 * @private
		 * @param {CKEDITOR.eventInfo} evt
		 */
		onListClosed: function() {
			this.opened = false;
		},

		/**
		 * The function registered by the {@link #attach} method
		 * as the {@link CKEDITOR.plugins.autocomplete.view#updated} event listener.
		 * Updates aria status with the new list size.
		 *
		 * @private
		 * @param {CKEDITOR.eventInfo} evt
		 */
		onListUpdated: function( evt ) {
			if ( this.opened ) {
				this.updateStatus( this.getUpdateInfo( evt.data.size ) );
			}
		},

		/**
		 * The function registered by the {@link #attach} method
		 * as the {@link CKEDITOR.plugins.autocomplete.view#selected} event listener.
		 * Updates aria status with selected item information.
		 *
		 * @private
		 * @param {CKEDITOR.eventInfo} evt
		 */
		onItemSelected: function( evt ) {
			if ( this.opened ) {
				this.updateStatus( this.getSelectedItemInfo( evt.data.text, evt.data.index, evt.data.size ) );
			}
		},

		/**
		 * Updates aria status with given text so that screen readers may announce it.
		 *
		 * @private
		 * @param {String} statusText
		 */
		updateStatus: function( statusText ) {
			if ( this.timeoutId ) {
				clearTimeout( this.timeoutId );
			}

			this.timeoutId = CKEDITOR.tools.setTimeout( function() {
				this.ariaElement.setText( statusText + '.' );
			}, 250, this );
		},

		/**
		 * Generates status text for panel open.
		 *
		 * @private
		 * @param {Number} size
		 * @returns {String}
		 * @example 'Autocomplete list, 10 results are available, to move to an item press the arrow keys'
		 */
		getOpenInfo: function( size ) {
			return this.lang.label + ', ' + this.getResultsLengthInfo( size ) + ', ' + this.lang.navigationInfo;
		},

		/**
		 * Generates status text for panel update.
		 *
		 * @private
		 * @param {Number} size
		 * @returns {String}
		 * @example 'Autocomplete list updated, 5 results are available'
		 */
		getUpdateInfo: function( size ) {
			return this.lang.label + ' ' + this.lang.updated + ', ' + this.getResultsLengthInfo( size );
		},

		/**
		 * Generates status text for selected item.
		 *
		 * @private
		 * @param {String} text
		 * @param {Number} index
		 * @param {Number} size
		 * @returns {String}
		 * @example 'CKEditor 4 selected, 2 of 10'
		 */
		getSelectedItemInfo: function( text, index, size ) {
			return text + ' ' + this.lang.selected + ', ' +
				this.langTemplates.listLength.output( {
					selected: index,
					size: size
				} );
		},

		/**
		 * Generates status text with list size information.
		 *
		 * @private
		 * @param {Number} size
		 * @returns {String}
		 * @example '1 result available'
		 */
		getResultsLengthInfo: function( size ) {
			var resultsText = '';
			if ( size === 0 ) {
				resultsText = this.lang.noResults;

			} else if ( size == 1 ) {
				resultsText = this.lang.resultsSingular;

			} else {
				resultsText = this.langTemplates.resultsPlural.output( { size: size } );
			}
			return resultsText;
		}
	};

	CKEDITOR.plugins.autocompletearia = AutocompleteAria;

	/**
	 * A helper object used to create aria specific markup.
	 *
	 * @property {CKEDITOR.plugins.autocompletearia} ariaHelper
	 * @member CKEDITOR.plugins.autocomplete
	 */
} )();
