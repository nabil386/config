/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

/**
 * @fileOverview The "fontpx" plugin provides an integration for matching font sizes in any unit to the font size dropdown.
 */

( function() {

	'use strict';

	CKEDITOR.plugins.add( 'fontpx', {
		lang: 'en', // %REMOVE_LINE_CORE%
		icons: 'fontpx', // %REMOVE_LINE_CORE%
		requires: 'font',

		onLoad: function() {
			var sizeElementHolderStyles = CKEDITOR.env.ie ? 'display:none' : 'position:fixed;top:0;left:-1000px';

			function convertSizeToPx( style, editor ) {
				var fontSize = -1;

				if ( style ) {
					var element = editor.document.createElement( style.element );
					style.applyToObject( element );

					fontSize = Math.floor( parseFloat( getFontSize( element, editor ) ) ) + 'px';
				}
				return fontSize;
			}

			function getFontSize( element, editor ) {
				var fontSize,
					sizeElementHolder = CKEDITOR.dom.element.createFromHtml( '' +
						'<div data-cke-temp="1" style="' + sizeElementHolderStyles + '"></div>', editor.document );

				sizeElementHolder.append( element );
				editor.editable().append( sizeElementHolder );

				fontSize = element.getComputedStyle( 'font-size' );

				sizeElementHolder.remove();
				return fontSize;
			}

			CKEDITOR.plugins.font.on( 'matchSize', function( evt ) {
				var styles = evt.data.styles,
					computedValue = evt.data.computedValue,
					style,
					definition,
					value;

				for ( value in styles ) {
					style = styles[ value ],
					definition = style.getDefinition();

					if ( definition.styles && definition.styles[ 'font-size' ] && convertSizeToPx( style, evt.editor ) == computedValue ) {
						evt.data.setValue( value );
						return evt.cancel();
					}
				}
			} );
		}
	} );

} )();
