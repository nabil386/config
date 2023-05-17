/* Copyright IBM Corp. 2010-2014 All Rights Reserved.                    */

(function(){
	CKEDITOR.plugins.add('ibmmarkdown', {
		lang: 'ar,ca,cs,da,de,el,en,es,fi,fr,he,hr,hu,it,iw,ja,kk,ko,nb,nl,no,pl,pt,pt-br,ro,ru,sk,sl,sv,th,tr,uk,zh,zh-cn,zh-tw', // %REMOVE_LINE_CORE%
		icons: 'bookmark', // %REMOVE_LINE_CORE%

		init: function(editor) {
			//positions of menu button are not yet registered automatically as features (#10440)
			editor.addFeature( editor.getCommand( 'ibmmarkdown' ) );

			var rootPath = this.path;

			editor.on('instanceReady',function(){
				if (typeof(marked) == 'undefined') {
					CKEDITOR.scriptLoader.load(rootPath + 'js/marked.js', function(){
						marked.setOptions({
							renderer: new marked.Renderer(),
							gfm: true,
							tables: true,
							breaks: false,
							pedantic: false,
							sanitize: true,
							smartLists: true,
							smartypants: false
						});
					});
				}
			});

       		editor.on( 'key', function( evt ) {
			 	if(editor.config.ibmAutoConvertMarkdown) {
					var key = evt.data.keyCode;
					// Key codes: enter = 13
					if((key == 13) && "source".valueOf() != new String(editor.mode).valueOf()) {
						if(!CKEDITOR.env.webkit)//dont save snapshot for Chrome as if text has style applied then cursor will jump to wrong position on undo
							editor.fire( 'saveSnapshot' );
						converterMarkdownToHTML(evt);
	       			}
       			}
       		});

       		function isMarkdown(text) {
       			if(CKEDITOR.env.webkit)
			 		text = text.replace(/\u200B/g, '');		//remove unicode space character added by webkit
				if(CKEDITOR.env.ie) {
					text = text.replace(/\u00A0/g, '');		//remove unicode space character added by IE
				}

				var markdownMatch = marked.lexer(text);
				return markdownMatch && (typeof markdownMatch[0].type != "undefined")
					&& markdownMatch[0].type != 'html' && (markdownMatch[0].type != 'paragraph'
					|| marked(text).replace('<p>', '').replace('</p>', '') != text);
       		}


			var converterMarkdownToHTML = function(evt) {

				var selection = editor.getSelection().getRanges()[0];
				var endContainer = selection.endContainer;
				var result;
				var text;

				//Get the text just before where the cursor is positioned
				if(endContainer.type == CKEDITOR.NODE_ELEMENT) {	//endcontainer is an element node -> get the text value from the element at the endOffset position
					var selectedElement = endContainer.getChild(selection.endOffset-1);
					if(selectedElement && selectedElement.type == CKEDITOR.NODE_TEXT) {
						text = selectedElement.getText();
					}
				} else { //endcontainer is a text node
					text = endContainer.getText().substring(0, selection.endOffset);
				}

				if(text && /\S/.test(text)) {		//make sure there is actually some text content to check for a MD

					result = isMarkdown(text);

					if(!result) {		//endcontainer isn't a Markdown by itself

						//there may be content after the cursor, keep a reference to everything after the endOffset so that we can add it again later
						var endContainerText;
						if( endContainer.type == CKEDITOR.NODE_TEXT ) {
							endContainerText = endContainer.getText().substring(selection.endOffset);
						}

						var element = endContainer = (endContainer.type == CKEDITOR.NODE_ELEMENT && endContainer.getChildCount() > 0) ? endContainer.getChild(selection.endOffset-1) : endContainer;
						var elementText;
						//check previous nodes for link content if the endcontainer is not a link and it does not start with a space - this can happen when a link is split across many different nodes e.g. editing link text after it is inserted
						if(((element.getName && element.getName() != 'a') || element.type == CKEDITOR.NODE_TEXT && endContainer.getText().charAt(0) != ' ')) {

							var flag = false;
							var count = 0;
							//walk previous nodes and append them into the endContainer until we reach the first node or a space
							while (element && element.getPrevious() && element.getPrevious().type == CKEDITOR.NODE_TEXT ){
								count++;
								if(!flag) {
									element = element.getPrevious();	//get the element's previous node on the first loop
									flag = true;
								}

								//reset text with the text from the previous node + everything in the endcontainer up to the last space e.g with the following node structure test www./ibm/.com text,
								//1st loop: element.getText() = /ibm/, text = .com
								//2nd loop:  element.getText() = www., text = ibm.com
								text = element.getText() + text;
								elementText = text;

								//check whether there is another previous node
								if(element.getPrevious && element.getPrevious() !== null) {
									var prevElement = element.getPrevious();
								}

								//reset element to the new previous element or null
								if (prevElement)
									element = prevElement;
								else
									element = null;

								//see if endContainer text contains a space - if it does we don't need to check any more previous nodes
								if (elementText.indexOf(' ') >= 0 || (CKEDITOR.env.webkit && /\s/g.test(elementText)))
									break;
							}
							// from www.ibmireland.com -> ireland.com/ next step to get www.ibm
							var removeLastNode = false;
							//If the url text is at the start of a paragraph, we still need to check whether element is part of the url. The while loop above will not catch this as the start node does not have a previous element.
							if (element && elementText && element.type == CKEDITOR.NODE_TEXT && element.getText() != elementText){
								var lastElementText = element.getText();
								var lastChar = lastElementText.charAt(lastElementText.length-1);
								if(lastChar != ' '){
									removeLastNode = true;
									var lastSpaceIndex = lastElementText.lastIndexOf(" ");
									if(lastSpaceIndex != -1)
										elementText = element.getText().substring( lastSpaceIndex + 1 ) + elementText;
									else
										elementText = element.getText() + elementText;
								}
							}
							//Get the new text value of endContainer and see if it contains a url
							if(elementText)
								text = elementText;

							spaceIndex = text.lastIndexOf(' ');
							if(spaceIndex != -1) {
								mdText = text.substring(spaceIndex+1);
							} else {
								mdText = text;
							}
							result = isMarkdown(mdText);

							// remove nodes
							if(result) {
								var element = endContainer = (endContainer.type == CKEDITOR.NODE_ELEMENT && endContainer.getChildCount() > 0) ? endContainer.getChild(selection.endOffset-1) : endContainer;
								var times = 0;
								var flag = false;
								for(var i = 0; i<count; i++) {
									times++;
									if(!flag) {
										element = element.getPrevious();	//get the element's previous node on the first loop
										flag = true;
									}
									var prevElement = element.getPrevious();
									element.remove(); //remove the previous text node - it's content is now included inside the endContainer
									element = prevElement;
								}

								if(removeLastNode){
									var lastSpaceIndex = element.getText().lastIndexOf(" ");
									if(lastSpaceIndex != -1)
										element.setText(element.getText().substring(0, lastSpaceIndex + 1 ));
									else
										element.setText("");
								}
								endContainer.setText(text);
								if(endContainerText && text != endContainerText) { //url detected and we need to re-add content from after the cursor
									var newTextNode = new CKEDITOR.dom.text( endContainerText );
									newTextNode.insertAfter(endContainer);
								}
							}
						}
					}
					if(result) { // markdown detected
						//find the markdown within the endContainerText
						var lastSpaceIndex = text.lastIndexOf(" ");
						var cleanText = text.replace(/^\s+|\s+$/g,'');//remove spaces surrounding the URL
						if(CKEDITOR.env.webkit)
							text = text.replace(/\u200B/g, '');		//remove unicode space character added by webkit
						var unicodeSpaceCharacterIndex = -1;
						if(CKEDITOR.env.ie) {
							unicodeSpaceCharacterIndex = text.lastIndexOf('\u00A0'); //check for unicode space character added by ie
						}
						var markdownTextStartIndex = text.lastIndexOf(cleanText); 	//begining of the markdown text
						//reset endContainer to the just the node that contains the Markdown Text
						if((lastSpaceIndex != -1 || markdownTextStartIndex == 0) || CKEDITOR.env.ie && unicodeSpaceCharacterIndex != -1 ) {
							if ( endContainer.type != CKEDITOR.NODE_TEXT && endContainer.getChildCount() > 0)
								endContainer = endContainer.getChild(selection.endOffset-1);
						}


						var node = endContainer.getParent();

						if(CKEDITOR.env.webkit){
							endContainer.setText((endContainer.getText()).replace(/\u200B/g, ''));//remove unicode space character added by webkit
							fixInitialSelection(editor);
						}

						//insert space or new line and stop the original event
						evt.cancel();		//cancel the default browser behavior for this keystroke
						var key = evt.data.keyCode;
						var str = endContainer.getText().substring(markdownTextStartIndex + cleanText.length);

						// insert new line

						editor.execCommand( 'enter' );

						var originalEndContainer = editor.getSelection().getRanges()[0].endContainer;// save the selection so we can restore it when link will be inserted
						editor.fire( 'saveSnapshot' );

						//Select just the URL
						var range = editor.createRange();
						range.setStart( endContainer, markdownTextStartIndex );
						range.setEnd( endContainer, markdownTextStartIndex + cleanText.length);
						range.select();

						//Set the attribute for the link and insert it
						var selectedText = editor.getSelection().getSelectedText();

						var markdownConversionCallback = function(){

							// convert Markdown and insert HTML
							editor.insertHtml( marked(selectedText).replace('<p>', '').replace('</p>', '') );

							//enter key was pressed
							var zeroWidthSpaceChar = new CKEDITOR.dom.element.createFromHtml('&#8203');
							if(CKEDITOR.env.webkit && !(/\S/.test(originalEndContainer.getText()))) // zero- width space char should be inserted for chrome in order do not loose the style on next line
								zeroWidthSpaceChar.insertAfter(originalEndContainer);

							moveSelectionToElement(originalEndContainer);
						};

						// Convert to Markdown and Fill the textarea.
						if (typeof(marked) == 'undefined') {
							CKEDITOR.scriptLoader.load(rootPath + 'js/marked.js', markdownConversionCallback);
						} else {
							markdownConversionCallback();
						}

					}
				}
			};

       		function moveSelectionToElement(element) {
       			var range = new CKEDITOR.dom.range(editor.document);
				range.moveToElementEditablePosition(element);
				range.select();
       		}

       		function fixInitialSelection( editor ) {
       			var editable = editor.editable();
				var resetSelection = 0;
				if ( editable ){
					var fillingChar = editable.getCustomData( 'cke-fillingChar' );

					if ( fillingChar ) {
						// If cursor is right blinking by side of the filler node, save it for restoring,
						// as the following text substitution will blind it. (#7437)
						var sel = editor.document.$.defaultView.getSelection();
						if ( sel.type == 'Caret' && sel.anchorNode == fillingChar.$ ){
							resetSelection = 1;
						}
						var fillingCharBefore = fillingChar.getText();
						fillingChar.setText( replaceFillingChar( fillingCharBefore ) );

						if ( resetSelection ) {
							editor.document.$.defaultView.getSelection().setPosition( fillingChar.$, fillingChar.getLength() );
							resetSelection = 0;
						}
					}
				}
       		}

       		function replaceFillingChar( html ) {
       			return html.replace( /\u200B( )?/g, function( match ) {
       				// #10291 if filling char is followed by a space replace it with nbsp.
       				return match[ 1 ] ? '\xa0' : '';
       			} );
       		}

		}

	});

})();
