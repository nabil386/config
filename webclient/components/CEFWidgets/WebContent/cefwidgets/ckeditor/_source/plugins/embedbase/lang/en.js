/*
Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.md or http://ckeditor.com/licensePortions Copyright IBM Corp., 2009-2016.
*/
CKEDITOR.plugins.setLang( 'embedbase', 'en', {
	pathName: 'media object',
	title: 'Media',
	url: 'URL:',
	button: 'Insert Media',
	unsupportedUrlGiven: 'The specified URL is not supported.',
	unsupportedUrl: 'The URL {url} is not supported by Media Embed.',
	fetchingFailedGiven: 'Failed to fetch content for the given URL.',
	fetchingFailed: 'Failed to fetch content for {url}.',
	fetchingOne: 'Fetching oEmbed response...',
	fetchingMany: 'Fetching oEmbed responses, {current} of {max} done...',
		
	ibm :
	{
		alignment : "Alignment:",
		options : "Alignment Options",
		left:"Left",
		center:"Center",
		right:"Right",
		predefined:"Media Width:",
		specify : "Specify Size",
		maxWidth : "Width:",
		maxHeight : "Height:",
		emptyURL: "Media source URL is missing.",
		emptyWidth : "Width field should not be empty.",
		emptyHeight : "Height field should not be empty.",
		unsafeContent: "Unsafe Content",
		buttons:{
			original : "Original",
			small : "Small (300px)",
			medium : "Medium (500px)",
			fitPageWidth : "Fit page width"
		},
		lockRatio : "Lock Ratio",
		resetSize : "Reset Size"

	}
} );
