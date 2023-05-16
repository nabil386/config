/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

/**
 * @fileOverview Mentions plugin built on top of {@link CKEDITOR.plugins.autocomplete} to create customizable mentioning system.
 */

'use strict';


( function() {

	CKEDITOR.plugins.add( 'mentions', {
		requires: 'autocomplete,textmatch,vcard',

		init: function( editor ) {
			editor.on( 'instanceReady', function() {
				var prefix = getPrefix( editor.config, '@' ),
					minChars = editor.config.mentions_minChars || 2,
					allowSpaces = editor.config.mentions_allowSpaces !== false,
					itemTemplate = editor.config.mentions_itemTemplate,
					dataCallback = getDataCallback( editor.config );

				if ( dataCallback ) {
					editor.mentions = new CKEDITOR.plugins.mentions( editor, getTextTestCallback( prefix, minChars, allowSpaces, true, true ), dataCallback, itemTemplate );

					// Run widget checking on every change so destroy event is fired right after widget is destroyed.
					editor.on( 'change', function() {
						editor.widgets.checkWidgets();
					} );
				}
			} );
		},

		onLoad: function() {
			createMentionsPlugin();
		}
	} );


	function createMentionsPlugin() {
		var Autocomplete = CKEDITOR.plugins.autocomplete;

		/**
		* Specialized mentions class build on top of generic {@link CKEDITOR.plugins.autocomplete} plugin.
		*
		* @class CKEDITOR.plugins.mentions
		* @extends CKEDITOR.plugins.autocomplete
		* @constructor Creates the new instance of mentions and attaches it to the editor.
		* @param {CKEDITOR.editor} editor The editor to watch.
		* @param {Function} textTestCallback Callback executed to check if a text next to the selection should open
		* the {@link CKEDITOR.plugins.autocomplete} panel. See the {@link CKEDITOR.plugins.textWatcher}'s `callback` argument.
		* @param {Function} dataCallback Callback executed to get suggestion data based on search query. The returned data will be
		* displayed in the {@link CKEDITOR.plugins.autocomplete} panel.
		* @param {String} dataCallback.query The query string that was accepted by the `textTestCallback`.
		* @param {CKEDITOR.dom.range} dataCallback.range The range in the DOM where the query text is.
		* @param {Function} dataCallback.callback The callback which should be executed with the data.
		* @param {CKEDITOR.plugins.autocomplete.model.item[]} dataCallback.callback.data The suggestion data that should be
		* displayed in the {@link CKEDITOR.plugins.autocomplete} panel for a given query. The data items should implement the
		* {@link CKEDITOR.plugins.autocomplete.model.item} interface.
		* @param {String} itemTemplate Html template used to render single item inside {@link CKEDITOR.plugins.autocomplete} panel.
		*/
		function Mentions( editor, textTestCallback, dataCallback, itemTemplate ) {

			/**
			 * Whether mention is currently being inserted.
			 *
			 * @readonly
			 * @property {Boolean} pending
			 */
			this.pending = false;

			/**
			 * Whether widget was inserted since last {@link CKEDITOR.plugins.mentions#mentionStart} event. Used
			 * to distinguish if mentioning was canceled or if a new item (mention) was inserted.
			 *
			 * @readonly
			 * @property {Boolean} inserted
			 */
			this.inserted = false;

			/**
			 * Active mention wrapper object.
			 *
			 * @readonly
			 * @type {CKEDITOR.dom.element} wrapper
			 */
			this.wrapper = null;

			Autocomplete.call( this, editor, textTestCallback, dataCallback, 'vcard' );

			if ( itemTemplate ) {
				this.view.itemTemplate = new CKEDITOR.template( itemTemplate );
			}
		}

		/**
		 * Value of data-cke-id wrapper attribute. Used for identifying existing wrappers.
		 *
		 * @static
		 * @property {String} wrapperDataId
		 */
		Mentions.wrapperDataId = 'mentions-wrapper';


		Mentions.prototype = CKEDITOR.tools.prototypedCopy( Autocomplete.prototype );

		Mentions.prototype.commit = function( itemId ) {
			this.unwrapMatchedText();
			Autocomplete.prototype.commit.call( this, itemId );
		};

		Mentions.prototype.onTextMatched = function( evt ) {
			this.wrapMatchedText( evt.data.text, evt.data.range );

			if ( !this.pending ) {
				this.pending = true;
				this.inserted = false;
				this.editor.fire( 'mentionStart' );
			}
			Autocomplete.prototype.onTextMatched.call( this, evt );
		};

		Mentions.prototype.onTextUnmatched = function( evt ) {
			this.wrapper = null;

			Autocomplete.prototype.onTextUnmatched.call( this, evt );

			if ( this.pending ) {
				if ( !this.inserted ) {
					this.editor.fire( 'mentionCancel' );
				}
				this.pending = false;
				this.inserted = false;
			}
		};

		Mentions.prototype.insertWidget = function( item ) {
			// Flag needs to be set before insertWidget execution. The asynchronous insertWidget will complete after onTextUnmatched.
			this.inserted = true;
			var widget = Autocomplete.prototype.insertWidget.call( this, item );

			this.editor.fire( 'mentionComplete', widget );
			return widget;
		};

		/**
		 * Wraps text within a given range with a span wrapper.
		 *
		 * @param {String} text
		 * @param {CKEDITOR.dom.range} range
		 */
		Mentions.prototype.wrapMatchedText = function( text, range ) {
			if ( text && range && !this.getTextWrapper( range ) ) {
				var wrapper = new CKEDITOR.dom.element( 'span' );
				wrapper.setAttribute( 'data-cke-id', Mentions.wrapperDataId );
				wrapper.setText( text );

				range.deleteContents();
				range.insertNode( wrapper );

				var wrapperRange = range.clone();
				wrapperRange.selectNodeContents( wrapper );
				wrapperRange.collapse();

				this.editor.getSelection().selectRanges( [ wrapperRange ] );
			}
		};

		/**
		 * Removes wrapper which includes current selection.
		 */
		Mentions.prototype.unwrapMatchedText = function() {
			var wrapper = this.getTextWrapper( this.editor.getSelection().getRanges()[ 0 ] );
			if ( wrapper ) {
				wrapper.remove( true );
				this.wrapper = null;
			}
		};

		/**
		 * Returns wrapper which includes given range.
		 *
		 * @param {CKEDITOR.dom.range} range
		 * @returns {CKEDITOR.dom.element}
		 */
		Mentions.prototype.getTextWrapper = function( range ) {
			if ( !this.wrapper && range ) {
				var startParent = range.startContainer.getParent(),
					endParent = range.endContainer.getParent();

				if ( startParent && startParent.getName() == 'span' && startParent.equals( endParent ) &&
					startParent.getAttribute( 'data-cke-id' ) == Mentions.wrapperDataId ) {

					this.wrapper = startParent;
				}
			}

			return this.wrapper;
		};

		CKEDITOR.plugins.mentions = Mentions;
	}


	function getTextTestCallback( prefix, minChars, allowSpaces, requireSpaceBefore, requireSpaceAfter ) {
		var matchPattern = createPattern( minChars ),
			activeMatchPattern = createPattern( false );

		return function( range ) {
			if ( !range.collapsed ) {
				return null;
			}

			return CKEDITOR.plugins.textMatch.match( range, function( text, offset ) {
				return matchCallback( text, offset, range );
			} );
		};

		function matchCallback( text, offset, range ) {
			var left = text.slice( 0, offset ),
				right = text.slice( offset ),
				activeMatch = isActiveMatch( range ),
				match = activeMatch ? left.match( activeMatchPattern ) : left.match( matchPattern ),
				end = activeMatch ? getActiveMatchEnd( match, range ) : offset;

			if ( !match ) {
				return null;
			}

			if ( !activeMatch && requireSpaceBefore ) {
				var textBeforeMatch = left.substr( 0, match.index );
				// Require space before the matched text.
				if ( textBeforeMatch && !textBeforeMatch.match( /\s+$/ ) ) {
					return null;
				}
			}

			if ( !activeMatch && requireSpaceAfter ) {
				// Require space (or end of text) after the caret.
				if ( right && !right.match( /^\s/ ) ) {
					return null;
				}
			}

			return { start: match.index, end: end, position: offset };
		}

		function createPattern( minChars ) {
			// Escape special regex characters if used as prefix.
			var escapedPrefix = prefix.replace( /[-\/\\^$*+?.()|[\]{}]/g, '\\$&' ),
				allowedCharacters = allowSpaces ? '\\w\\s' : '\\w',
				pattern = escapedPrefix + '[' + allowedCharacters + ']';

			if ( minChars ) {
				pattern += '{' + minChars + ',}';
			} else {
				pattern += '*';
			}

			pattern += '$';

			return new RegExp( pattern );
		}

		function isActiveMatch( range ) {
			var startParent = range.startContainer.getParent(),
				endParent = range.endContainer.getParent();

			return startParent.getName() == 'span' && startParent.equals( endParent ) &&
				startParent.getAttribute( 'data-cke-id' ) == CKEDITOR.plugins.mentions.wrapperDataId;
		}

		function getActiveMatchEnd( match, range ) {
			return match.index + range.startContainer.getParent().getText().length;
		}
	}

	function getPrefix( config, defaultPrefix ) {
		return config.mentions_prefix === '' ? config.mentions_prefix : config.mentions_prefix || defaultPrefix;
	}

	function getDataCallback( config ) {
		return config.mentions_dataCallback && isFunction( config.mentions_dataCallback ) ?
			config.mentions_dataCallback : null;
	}

	function isFunction( f ) {
		// For IE8 typeof fun == object so we cannot use it.
		return !!( f && f.call && f.apply );
	}
} )();

/**
 * Defines the prefix used for inserting mentions.
 *
 *		config.mentions_prefix = '#';
 *
 * @cfg {String} [mentions_prefix='@']
 * @member CKEDITOR.config
 */

/**
 * Defines the minimum text length after mentions prefix to start mentioning.
 *
 *		config.mentions_minChars = 0;
 *
 * @cfg {Number} [mentions_minChars=2]
 * @member CKEDITOR.config
 */

/**
 * Defines if space can be used while mentioning and does not stop it.
 *
 *		config.mentions_allowSpaces = false;
 *
 * @cfg {Number} [mentions_allowSpaces=true]
 * @member CKEDITOR.config
 */

/**
 * HTML template which will be used instead of default template to display suggestions.
 *
 *		config.mentions_itemTemplate = '<li>{name}<li>';
 *
 * @cfg {String} [mentions_itemTemplate=see source]
 * @member CKEDITOR.config
 */

/**
 * Defines the function which will be used by {@link CKEDITOR.plugins.mentions} plugin to retrieve data for suggestions.
 *
 *		config.mentions_dataCallback = function( query, range, callback ) {
 *			var data = fetchDataFromServer( query, range ); // Custom function for fetching data from external resource.
 *			callback( data );
 *		};
 *
 * @cfg {Function} mentions_dataCallback=undefined
 * @member CKEDITOR.config
 * @param {String} query The query string that was accepted by the `textTestCallback`. See
 * the {@link CKEDITOR.plugins.mentions} constructor `textTestCallback` param.
 * @param {CKEDITOR.dom.range} range The range in the DOM where the query text is.
 * @param {Function} callback The callback which should be executed with the data.
 * @param {CKEDITOR.plugins.autocomplete.model.item[]} callback.data The suggestion data that should be
 * displayed in the {@link CKEDITOR.plugins.autocomplete} panel for a given query. The data items should implement the
 * {@link CKEDITOR.plugins.autocomplete.model.item} interface.
 */

/**
 * Fired after mentioning starts.
 *
 * Mentioning starts when both {@link CKEDITOR.config#mentions_prefix}
 * and {@link CKEDITOR.config#mentions_minChars} matches the current caret position. The mentionStart event
 * is fired before {@link CKEDITOR.config#mentions_dataCallback} function is executed.
 *
 * @event mentionStart
 * @member CKEDITOR.editor
 */

/**
 * Fired after mentioning ends and new item (mention) is inserted.
 *
 * @event mentionComplete
 * @member CKEDITOR.editor
 * @param {CKEDITOR.plugins.widget} item Inserted mention instance.
 */

/**
 * Fired after mentioning is canceled.
 *
 * @event mentionCancel
 * @member CKEDITOR.editor
 */

/**
 * Main mentions object instance.
 *
 * @member CKEDITOR.editor
 * @property {CKEDITOR.plugins.mentions} mentions
 */
