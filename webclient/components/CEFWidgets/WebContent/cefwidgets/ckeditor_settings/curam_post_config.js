CKEDITOR.on('instanceCreated',function(e) {

var editor = e.editor;

editor.name=mandatoryRichText;

editor.updateElement();
});