dojo.require("curam.util.ResourceBundle");
dojo.requireLocalization("curam.application","SVGText");
var POPUP_TEXT_NOT_FOUND="Popup text not found.";
var bundle=new curam.util.ResourceBundle("SVGText");
function returnPopupText(_1){
var _2=_1.charAt(0);
if(_2=="r"){
return _returnRulesPopupText(_1);
}else{
if(_2=="e"){
return _returnEditorPopupText(_1);
}else{
if(_2=="l"){
return bundle.getProperty("LEGISLATION_POPUP_TEXT");
}else{
return bundle.getProperty("POPUP_TEXT_NOT_FOUND");
}
}
}
};
function _returnEditorPopupText(_3){
var _4=_3.charAt(1);
if(_4=="S"){
return _returnEditorSuccessPopupText(_3);
}else{
if(_4=="F"){
return _returnEditorFailurePopupText(_3);
}else{
return bundle.getProperty("POPUP_TEXT_NOT_FOUND");
}
}
};
function _returnEditorSuccessPopupText(_5){
if(_5=="eSProduct"){
return bundle.getProperty("EDITOR_Product_SUCCESS");
}else{
if(_5=="eSAssessment"){
return bundle.getProperty("EDITOR_Assessment_SUCCESS");
}else{
if(_5=="eSSubRuleSet"){
return bundle.getProperty("EDITOR_SubRuleSet_SUCCESS");
}else{
if(_5=="eSObjectiveGroup"){
return bundle.getProperty("EDITOR_ObjectiveGroup_SUCCESS");
}else{
if(_5=="eSObjectiveListGroup"){
return bundle.getProperty("EDITOR_ObjectiveListGroup_SUCCESS");
}else{
if(_5=="eSObjective"){
return bundle.getProperty("EDITOR_Objective_SUCCESS");
}else{
if(_5=="eSSubRuleSetLink"){
return bundle.getProperty("EDITOR_SubRuleSetLink_SUCCESS");
}else{
if(_5=="eSRuleGroup"){
return bundle.getProperty("EDITOR_RuleGroup_SUCCESS");
}else{
if(_5=="eSRuleListGroup"){
return bundle.getProperty("EDITOR_RuleListGroup_SUCCESS");
}else{
if(_5=="eSRule"){
return bundle.getProperty("EDITOR_Rule_SUCCESS");
}else{
if(_5=="eSDataItemAssignment"){
return bundle.getProperty("EDITOR_DataItemAssignment_SUCCESS");
}else{
return bundle.getProperty("POPUP_TEXT_NOT_FOUND");
}
}
}
}
}
}
}
}
}
}
}
};
function _returnEditorFailurePopupText(_6){
if(_6=="eFProduct"){
return bundle.getProperty("EDITOR_Product_FAILURE");
}else{
if(_6=="eFAssessment"){
return bundle.getProperty("EDITOR_Assessment_FAILURE");
}else{
if(_6=="eFSubRuleSet"){
return bundle.getProperty("EDITOR_SubRuleSet_FAILURE");
}else{
if(_6=="eFObjectiveGroup"){
return bundle.getProperty("EDITOR_ObjectiveGroup_FAILURE");
}else{
if(_6=="eFObjectiveListGroup"){
return bundle.getProperty("EDITOR_ObjectiveListGroup_FAILURE");
}else{
if(_6=="eFObjective"){
return bundle.getProperty("EDITOR_Objective_FAILURE");
}else{
if(_6=="eFSubRuleSetLink"){
return bundle.getProperty("EDITOR_SubRuleSetLink_FAILURE");
}else{
if(_6=="eFRuleGroup"){
return bundle.getProperty("EDITOR_RuleGroup_FAILURE");
}else{
if(_6=="eFRuleListGroup"){
return bundle.getProperty("EDITOR_RuleListGroup_FAILURE");
}else{
if(_6=="eFRule"){
return bundle.getProperty("EDITOR_Rule_FAILURE");
}else{
if(_6=="eFDataItemAssignment"){
return bundle.getProperty("EDITOR_DataItemAssignment_FAILURE");
}else{
return bundle.getProperty("POPUP_TEXT_NOT_FOUND");
}
}
}
}
}
}
}
}
}
}
}
};
function _returnRulesPopupText(_7){
var _8=_7.charAt(1);
if(_8=="S"){
return _returnSuccessRulesPopupText(_7);
}else{
if(_8=="F"){
return _returnFailureRulesPopupText(_7);
}else{
return bundle.getProperty("POPUP_TEXT_NOT_FOUND");
}
}
};
function _returnSuccessRulesPopupText(_9){
if(_9=="rSp"){
return bundle.getProperty("RULES_PRODUCT_SUCCESS");
}else{
if(_9=="rSa"){
return bundle.getProperty("RULES_ASSESSMENT_SUCCESS");
}else{
if(_9=="rSsrs"){
return bundle.getProperty("RULES_SUBRULESET_SUCCESS");
}else{
if(_9=="rSog"){
return bundle.getProperty("RULES_OBJECTIVE_GROUP_SUCCESS");
}else{
if(_9=="rSolg"){
return bundle.getProperty("RULES_OBJECTIVE_LIST_GROUP_SUCCESS");
}else{
if(_9=="rSo"){
return bundle.getProperty("RULES_OBJECTIVE_SUCCESS");
}else{
if(_9=="rSrg"){
return bundle.getProperty("RULES_RULE_GROUP_SUCCESS");
}else{
if(_9=="rSrlg"){
return bundle.getProperty("RULES_RULE_LIST_GROUP_SUCCESS");
}else{
if(_9=="rSr"){
return bundle.getProperty("RULES_RULE_SUCCESS");
}else{
return bundle.getProperty("POPUP_TEXT_NOT_FOUND");
}
}
}
}
}
}
}
}
}
};
function _returnFailureRulesPopupText(_a){
if(_a=="rFp"){
return bundle.getProperty("RULES_PRODUCT_FAILURE");
}else{
if(_a=="rFa"){
return bundle.getProperty("RULES_ASSESSMENT_FAILURE");
}else{
if(_a=="rFsrs"){
return bundle.getProperty("RULES_SUBRULESET_FAILURE");
}else{
if(_a=="rFog"){
return bundle.getProperty("RULES_OBJECTIVE_GROUP_FAILURE");
}else{
if(_a=="rFolg"){
return bundle.getProperty("RULES_OBJECTIVE_LIST_GROUP_FAILURE");
}else{
if(_a=="rFo"){
return bundle.getProperty("RULES_OBJECTIVE_FAILURE");
}else{
if(_a=="rFrg"){
return bundle.getProperty("RULES_RULE_GROUP_FAILURE");
}else{
if(_a=="rFrlg"){
return bundle.getProperty("RULES_RULE_LIST_GROUP_FAILURE");
}else{
if(_a=="rFr"){
return bundle.getProperty("RULES_RULE_FAILURE");
}else{
return bundle.getProperty("POPUP_TEXT_NOT_FOUND");
}
}
}
}
}
}
}
}
}
};

