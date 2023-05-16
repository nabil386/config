/*
Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license

Portions Copyright IBM Corp., 2010-2014.
*/

CKEDITOR.skin.name = 'oneui3';

CKEDITOR.skin.ua_editor = 'ie,ie7,ie8,iequirks,webkit,opera,gecko';
CKEDITOR.skin.ua_dialog = 'ie,ie7,ie8,ie9,iequirks,opera,gecko';

// Because we customise the dialogs they need new widths and heights.
CKEDITOR.skin.dialogDimensions = {
		// Dialog name: [width, height]
		anchor: [300, 120],
		a11yHelp: [600, 400],
		cellProperties: [280, 230],
		colordialog: [360, 230],
		find: [300, 100],
		flash: [340, 292],
		iframe: [350, 200],
		image: [366, 353],
		link: [450, 300],
		paste: [300, 220],
		pastetext: [300, 220],
		smiley: [300, 80],
		specialchar: [410, 305],
		table: [320, 100],
		tableProperties: [320, 250],
		numberedListStyle: [360, 45],
		bulletedListStyle: [200, 45]
	};



/*CKEDITOR.skin.chameleon = function( editor, part ) {
	var css,
		cssId = '.' + editor.id;

	if ( part == 'editor' ) {
		css = cssId + ' .cke_inner,' +
			cssId + '_dialog .cke_dialog_contents,' +
			cssId + '_dialog a.cke_dialog_tab,' +
			cssId + '_dialog .cke_dialog_footer' +
			'{' +
				'background-color:$color !important;' +
				'background:-webkit-gradient(linear,0 -15,0 40,from(#fff),to($color));' +
				getLinearBackground( 'top,#fff -15px,$color 40px' ) +
			'}' +

			cssId + ' .cke_toolgroup' +
			'{' +
				'background:-webkit-gradient(linear,0 0,0 100,from(#fff),to($color));' +
				getLinearBackground( 'top,#fff,$color 100px' ) +
			'}' +

			cssId + ' .cke_combo_button' +
			'{' +
				'background:-webkit-gradient(linear, left bottom, left -100, from(#fff), to($color));' +
				getLinearBackground( 'bottom,#fff,$color 100px' ) +
			'}' +

			// TODO: This is not working because the panel doesn't go under the main UI element (cssID).
			cssId + ' .cke_combopanel' +
			'{' +
				'border: 1px solid $color;' +
			'}';
	} else if ( part == 'panel' ) {
		css = '.cke_menuitem .cke_icon_wrapper' +
			'{' +
				'background-color:$color !important;' +
				'border-color:$color !important;' +
			'}' +

			'.cke_menuitem a:hover .cke_icon_wrapper,' +
			'.cke_menuitem a:focus .cke_icon_wrapper,' +
			'.cke_menuitem a:active .cke_icon_wrapper' +
			'{' +
				'background-color:$color !important;' +
				'border-color:$color !important;' +
			'}' +

			'.cke_menuitem a:hover .cke_label,' +
			'.cke_menuitem a:focus .cke_label,' +
			'.cke_menuitem a:active .cke_label' +
			'{' +
				'background-color:$color !important;' +
			'}' +

			'.cke_menuitem a.cke_disabled:hover .cke_label,' +
			'.cke_menuitem a.cke_disabled:focus .cke_label,' +
			'.cke_menuitem a.cke_disabled:active .cke_label' +
			'{' +
				'background-color: transparent !important;' +
			'}' +

			'.cke_menuitem a.cke_disabled:hover .cke_icon_wrapper,' +
			'.cke_menuitem a.cke_disabled:focus .cke_icon_wrapper,' +
			'.cke_menuitem a.cke_disabled:active .cke_icon_wrapper' +
			'{' +
				'background-color:$color !important;' +
				'border-color:$color !important;' +
			'}' +

			'.cke_menuitem a.cke_disabled .cke_icon_wrapper' +
			'{' +
				'background-color:$color !important;' +
				'border-color:$color !important;' +
			'}' +

			'.cke_menuseparator' +
			'{' +
				'background-color:$color !important;' +
			'}' +

			'.cke_menuitem a:hover,' +
			'.cke_menuitem a:focus,' +
			'.cke_menuitem a:active' +
			'{' +
				'background-color:$color !important;' +
			'}';
	}

	return css;

	function getLinearBackground( definition ) {
		return 'background:-moz-linear-gradient(' + definition + ');' + // FF3.6+
			'background:-webkit-linear-gradient(' + definition + ');' + // Chrome10+, Safari5.1+
			'background:-o-linear-gradient(' + definition + ');' + // Opera 11.10+
			'background:-ms-linear-gradient(' + definition + ');' + // IE10+
			'background:linear-gradient(' + definition + ');'; // W3C
	}
};
*/
