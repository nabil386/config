These are the default properties for the client application and can be used
as the basis for customization or localization. Refer to the web client 
reference manual for more information.

Infrastructure Widget Properties Files:
To customize a widget properties file, create a new version under the webclient/
components/custom component folder, where the default
content for the file can be found in the corresponding sample widget properties
file located in the <cdej-dir>/doc/properties/ folder. For
each entry in Cúram's version of the file you wish to change, add a corresponding
entry to your custom file. 

RuntimeMessages.properties:
The Cúram CDEJ runtime messages can be localized or customized by creating
a RuntimeMessages.properties file within the curam/
omega3/i18n folder below the web application project's JavaSource
folder, i.e. the <client-dir>/JavaSource folder. The default content
for this file can be found in the <cdej-dir>/doc/defaultproperties/ folder. 
Any messages present in this file will override the corresponding messages from 
the RuntimeMessages.properties shipped with the Cúram CDEJ.