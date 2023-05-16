/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/licensePortions Copyright IBM Corp., 2009-2016.
 */
var CKEDITOR_LANGS = (function()
{
	var langs ={"ar":"Arabic","eu":"Basque","bg":"Bulgarian","ca":"Catalan","zh":"Chinese Simplified","zh-tw":"Chinese Traditional","hr":"Croatian","cs":"Czech","da":"Danish","nl":"Dutch","en":"English","fi":"Finnish","fr":"French","de":"German","el":"Greek","iw":"Hebrew","hu":"Hungarian","id":"Indonesian","it":"Italian","ja":"Japanese","kk":"Kazakh","ko":"Korean","no":"Norwegian Bokmal","pl":"Polish","pt":"Portuguese","pt-br":"Portuguese Brazilian","ro":"Romanian","ru":"Russian","sk":"Slovak","sl":"Slovenian","es":"Spanish","sr":"Serbian","sv":"Swedish","th":"Thai","tr":"Turkish"};

	// Patch for supported languages
	langs['he']	= 'Hebrew (HE)';
	// IBM Connections uses 'no' for Norwegian Bokmal
	langs['no'] = 'Norwegian';
	langs['nb'] = 'Norwegian Bokmal';
	langs['uk'] = 'Ukrainian';
	langs['zh-cn'] = 'Chinese Simplified (ZH-CN)';

	var langsArray = [];

    for ( var code in langs ) {
        langsArray.push({ code: code, name: ( langs[ code ] || code ) } );
    }

	langsArray.sort( function( a, b ) {
		return ( a.name < b.name ) ? -1 : 1;
	});

	return langsArray;
})();
