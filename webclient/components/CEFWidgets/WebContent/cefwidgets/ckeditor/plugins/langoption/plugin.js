   /*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corporation 2014.
 *
 * The source code for this program is not published or otherwise divested of
 * its trade secrets, irrespective of what has been deposited with the US
 * Copyright Office.
 */

   CKEDITOR.plugins.add('langoption',
    {
    requires : ['richcombo'],

    init: function (editor) {
            var pluginName = 'langoption';
            var config = editor.config;
			lang = editor.lang.format;

			var selectedLang;

            editor.ui.addRichCombo('LangOption',
				{
					title: editor.lang.ibmspellchecker.title,
                    multiSelect: false,
					toolbar: 'styles,20',
	                panel :
					{
					css: [ CKEDITOR.skin.getPath( 'editor' ) ].concat( config.contentsCss ),
				        multiSelect: false,
				    	attributes : { 'aria-label' : editor.lang.ibmspellchecker.title }
					},
                    init: function () {
                        for (var this_locale in spellcheckLocales){
							this.add(spellcheckLocales[this_locale][0],spellcheckLocales[this_locale][1],spellcheckLocales[this_locale][2]);
                    	};
						this.setValue(spellcheckDefaultLocale[0],spellcheckDefaultLocale[1],spellcheckDefaultLocale[1]);
						selectedLang=spellcheckDefaultLocale[0];
                    },
        			onRender : function()
					{
					 	editor.on( 'selectionChange', function( ev1 )
							{
								if ( !selectedLang )
						            return;
								for (var this_locale in spellcheckLocales){
								if (spellcheckLocales[this_locale][0]===selectedLang){
									editor.config.ibmSpellChecker.lang=selectedLang;
									this.setValue(spellcheckLocales[this_locale][0],spellcheckLocales[this_locale][1],spellcheckLocales[this_locale][2]);
									return;
								}
						    }
							},
							this);


						editor.on( 'instanceReady', function( ev )
							{
								this.setValue(spellcheckDefaultLocale[0],spellcheckDefaultLocale[1],spellcheckDefaultLocale[1]);
								selectedLang=spellcheckDefaultLocale[0];
						 	},this);

					}
					,

					onClick : function( value )
					{
						for (var this_locale in spellcheckLocales){
						    if (spellcheckLocales[this_locale][0]===value){
							    selectedLang=value;
							    editor.config.ibmSpellChecker.lang=value;
						        this.setValue(spellcheckLocales[this_locale][0],spellcheckLocales[this_locale][1],spellcheckLocales[this_locale][2]);
								return;
							}
						}
					},


					refresh: function() {
					    if ( !selectedLang )
						    return;
						for (var this_locale in spellcheckLocales){
						    if (spellcheckLocales[this_locale][0]===selectedLang){
							    editor.config.ibmSpellChecker.lang=selectedLang;
						        this.setValue(spellcheckLocales[this_locale][0],spellcheckLocales[this_locale][1],spellcheckLocales[this_locale][2]);
								return;
							}
						}
					}

        		}

				);


        }
    });
