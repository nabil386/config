window.addEventListener("load",function(e){
if(window.spm){
window.spm.loadWatsonAssistant=null;
if(curam&&curam.util&&!curam.util.isModalWindow()){
spm.Modal=null;
}
}
});
window.addEventListener("unload",function(e){
window.spm=null;
});

