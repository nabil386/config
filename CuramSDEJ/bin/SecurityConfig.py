#/*
# * Licensed Materials - Property of IBM
# *
# * PID 5725-H26
# *
# * Copyright IBM Corporation 2012,2021. All rights reserved.
# *
# * US Government Users Restricted Rights - Use, duplication or disclosure
# * restricted by GSA ADP Schedule Contract with IBM Corp.
# */

import sys
import re
import sre
def regexp(pattern, string, flags=0):
        if(re.compile(pattern, flags).search(string)==None): return 0
        else: return 1
def regexpn(pattern, string, flags=0):
        r = re.compile(pattern, flags).subn("", string)
        return r[1]
#endDef

def parsePropFile ( props ):

        global inputauthmech, LTPAPassword, activeProtocol
        global enabled, enforceJava2Security, useDomainQualifiedUserNames, issuePermissionWarning
        global AdminUsername, AdminPassword, servlet24Compliance
        global j2c_alias, j2c_userid, j2c_password, j2c_description
        global login_trace, loginModuleClassName, loginModuleAuthStrategy, curam_loginModule
        global ltpaLoginModule, wsMapDefaultInboundLoginModule, checkIdentityOnly, userRegistryEnabled, userRegistryEnabledTypes, userRegistryDisabledTypes
        global LTPAInteroperability, platform, useZosSAFSecurity, usernameDelimiter, SingleSignOnLtpaToken2login
        global cookieName, cookieMaxAge, cookieDomain, cookiePath, cookieSecure, cookieHttpOnly, cookieUseContextRootAsPath
        global defaultNodeAlias, sslProtocol, securityLevel

        inputauthmech = props["activeAuthMechanism"].strip( )
        LTPAPassword = props["LTPAPassword"].strip( )
        activeProtocol = props["activeProtocol"].strip( )

        enabled = props["enabled"].strip( )
        enforceJava2Security = props["enforceJava2Security"].strip( )
        useDomainQualifiedUserNames = props["useDomainQualifiedUserNames"].strip( )
        issuePermissionWarning = props["issuePermissionWarning"].strip( )

        j2c_alias = props["j2cAlias"].strip( )
        j2c_userid = props["j2cUserid"].strip( )
        j2c_password = props["j2cPassword"].strip( )
        j2c_description = props["j2cDescription"].strip( )

        AdminUsername = props["AdminUsername"].strip( )
        AdminPassword = props["AdminPassword"].strip( )
        servlet24Compliance = props["servlet24Compliance"].strip( )

        login_trace = props["loginTrace"].strip( )
        loginModuleClassName = props["loginModuleClassName"].strip( )
        loginModuleAuthStrategy = props["loginModuleAuthStrategy"].strip( )
        curam_loginModule = props["curamLoginModule"].strip( )

        ltpaLoginModule = props["ltpaLoginModule"].strip( )
        wsMapDefaultInboundLoginModule = props["wsMapDefaultInboundLoginModule"].strip( )
        checkIdentityOnly = props["checkIdentityOnly"].strip( )
        userRegistryEnabled = props["userRegistryEnabled"].strip( )
        userRegistryEnabledTypes = props["userRegistryEnabledTypes"].strip( )
        userRegistryDisabledTypes = props["userRegistryDisabledTypes"].strip( )        
        LTPAInteroperability = props["LTPAInteroperability"].strip( )
        SingleSignOnLtpaToken2login = props["SingleSignOnLtpaToken2login"].strip( )

        platform = props["platform"].strip( )
        useZosSAFSecurity = props["useZosSAFSecurity"].strip( )
        usernameDelimiter = props["usernameDelimiter"].strip( )
        
        cookieName = props["cookieName"].strip( )
        cookieMaxAge = props["cookieMaxAge"].strip( )
        cookieDomain = props["cookieDomain"].strip( )
        cookiePath = props["cookiePath"].strip( )
        cookieSecure = props["cookieSecure"].strip( )
        cookieHttpOnly = props["cookieHttpOnly"].strip( )
        cookieUseContextRootAsPath = props["cookieUseContextRootAsPath"].strip( )
        
        defaultNodeAlias = props["defaultNodeAlias"].strip( )
        sslProtocol = props["sslProtocol"].strip( )
        securityLevel = props["securityLevel"].strip( )
#endDef 

# Add the necessary users to the registry
def createUsers (  ):
        global AdminConfig, AdminTask
        global AdminPassword, AdminUsername, j2c_userid, j2c_password

        print "Adding administrative user "+AdminUsername
        AdminTask.applyWizardSettings("-secureApps false -secureLocalResources false -userRegistryType WIMUserRegistry -adminName "+AdminUsername+" -adminPassword "+AdminPassword ) 

        print "Adding database user "+j2c_userid
        AdminTask.applyWizardSettings("-secureApps false -secureLocalResources false -userRegistryType WIMUserRegistry -adminName "+j2c_userid+" -adminPassword "+j2c_password ) 
#endDef 

# Set the Active user registry to be the WIMUserRegistry
def setActiveUserRegistry (  ):
        global AdminConfig

        security = AdminConfig.list("Security" )
        registry = AdminConfig.list("WIMUserRegistry" )
        activeUR = AdminConfig.showAttribute(security, "activeUserRegistry" )

        print "Setting user registry to be WIMUserRegistry"
        AdminConfig.modify(security, [["activeUserRegistry", registry]] )

        print "Configuring User Registry"
        attrib = []
        attrib.append(["primaryAdminId", AdminUsername])
        attrib.append(["useRegistryServerId", "false"])
        attrib.append(["realm", "defaultWIMFileBasedRealm"])
        attrib.append(["ignoreCase", "true"])
        attrib.append(["serverPassword", AdminPassword])
        attrib.append(["serverId", AdminUsername])
        attrib.append(["registryClassName", "com.ibm.ws.wim.registry.WIMUserRegistry"])

        AdminConfig.modify(registry, attrib )
#endDef 

# Configure Global Security
def configGlobalSecurity (  ):
        global AdminConfig, servlet24Compliance, AdminTask
        global enabled, enforceJava2Security, useDomainQualifiedUserNames, issuePermissionWarning
        global activeProtocol, inputauthmech

        security = AdminConfig.list("Security" )        
        attrib = []

        if (enabled != []):
                attrib.append(["enabled", enabled])
                attrib.append(["appEnabled", enabled])
        #endIf 

        if (enforceJava2Security != []):
                attrib.append(["enforceJava2Security", enforceJava2Security])
        #endIf 

        if (useDomainQualifiedUserNames != []):
                attrib.append(["useDomainQualifiedUserNames", useDomainQualifiedUserNames])
        #endIf 

        if (issuePermissionWarning != []):
                attrib.append(["issuePermissionWarning", issuePermissionWarning])
        #endIf 

        if (activeProtocol != []):
                attrib.append(["activeProtocol", activeProtocol])
        #endIf 

        if (inputauthmech != []):
                authmechsList = AdminConfig.show(security, "authMechanisms" ) 
                authmechs = authmechsList.split(" ") 
                for authmech in authmechs:
                        if (regexp(inputauthmech, authmech) ):
                                attrib.append(["activeAuthMechanism", authmech])
                                break 
                        #endIf 
                #endFor 
        #endIf 

        print "Enable administrative and application security."
        AdminConfig.modify(security, attrib )

        new_property = []
        new_property.append(["name", servlet24Compliance])
        new_property.append(["value", "false"])
        AdminConfig.modify(security, [["properties", [new_property]]] )
        print "Enabling security custom properties."
        AdminTask.setAdminActiveSecuritySettings('[-customProperties["com.ibm.ws.security.addHttpOnlyAttributeToCookies=true"]]')
        print "Setting SSO requires SSL."
        try:
               authmechsList = AdminConfig.show(security, "authMechanisms" ) 
               authmechs = authmechsList.split(" ") 
               for authmech in authmechs:
                 if (regexp(inputauthmech, authmech) ):
                     testt = AdminConfig.show(authmech,"singleSignon")
                     ssos = testt.split(" ")
                     AdminConfig.modify(ssos[1].split("]")[0], [["requiresSSL", "true"]] )
                 #endIf
               #endFor 
        except:
          print "Setting SSO requires SSL encountered an exception. Please review the WebSphere logs."  
        #endTry
        
#endDef

# Configure TLS 1.2 Security
def configTLS12 (  ):
        global AdminTask, defaultNodeAlias, sslProtocol, securityLevel

        print "Configuring TLSv1.2"

        tls12Params = ["-alias", defaultNodeAlias, "-sslProtocol",  sslProtocol, "-securityLevel",  securityLevel]
        AdminTask.modifySSLConfig(tls12Params) 
#endDef

# Add certificate to the keystore for SSL connections
def addSSLCertificate ( ):
        global AdminTask, xmlServerPath

        print "Adding SSL Certificate to Trust Store"

        xmlServerCellName = AdminControl.getCell()
        xmlServerNodeName = AdminControl.getNode()
        xmlServerKeystoreProps = loadProperties(xmlServerPath + "/TLSKeystore.properties")
        xmlServerKeystoreAlias = xmlServerKeystoreProps["keystore.alias"].strip( )
        xmlServerCertName = xmlServerKeystoreProps["certificate.filename"].strip( )
        xmlServerCertPath = xmlServerPath + '/' + xmlServerCertName
        AdminTask.addSignerCertificate('[-keyStoreName NodeDefaultTrustStore -keyStoreScope (cell):' + xmlServerCellName + ':(node):' + xmlServerNodeName + ' -certificateFilePath '+ xmlServerCertPath + ' -base64Encoded true -certificateAlias ' + xmlServerKeystoreAlias + ']')
        AdminTask.getSignerCertificate('[-keyStoreName NodeDefaultTrustStore -keyStoreScope (cell):' + xmlServerCellName + ':(node):' + xmlServerNodeName + ' -certificateAlias ' + xmlServerKeystoreAlias + ']')
#endDef

# Add certificate to the keystore for inbound JWT authentication
def addJWTCertificate ( ):
        global AdminTask, jwtPropFile, jwtCertPath

        print "Adding JWT Public Certificate to Trust Store"

        cellName = AdminControl.getCell()
        nodeName = AdminControl.getNode()
        keystoreProps = loadProperties(jwtPropFile)
        jwtKeystoreAlias = keystoreProps["jwt.cert.alias"].strip( )
        jwtCertName = keystoreProps["jwt.public.cert.filename"].strip( )
        jwtCertFilePath = jwtCertPath + '/' + jwtCertName
        AdminTask.addSignerCertificate('[-keyStoreName NodeDefaultTrustStore -keyStoreScope (cell):' + cellName + ':(node):' + nodeName + ' -certificateFilePath '+ jwtCertFilePath + ' -base64Encoded true -certificateAlias ' + jwtKeystoreAlias + ']')
#endDef

# Add the necessary users to the registry
def createUsersAndGroups (  ):
        global AdminConfig, AdminTask
        
        AdminTask.listGroupIDsOfAuthorizationGroup()
        AdminTask.mapGroupsToAdminRole('[-roleName monitor -specialSubjects [ALLAUTHENTICATED ]]')
        AdminTask.mapGroupsToAdminRole('[-roleName operator -specialSubjects [ALLAUTHENTICATED ]]')
        AdminTask.listGroupIDsOfAuthorizationGroup()

#endDef 
# Add Curam specific login modules
def configSysJAASLogin ( sysentry ):
        global AdminConfig
        global loginModuleAuthStrategy, loginModuleClassName
        global curam_loginModule, login_trace, AdminUsername, j2c_userid
        global ltpaLoginModule, wsMapDefaultInboundLoginModule, checkIdentityOnly, userRegistryEnabled, usernameDelimiter, userRegistryEnabledTypes, userRegistryDisabledTypes

        # Get the system login configuration
        sysLoginConfigId = AdminConfig.list("JAASConfiguration" ).splitlines()[1]


        # Delete the configuration entry  
        configurationEntries = AdminConfig.list("JAASConfigurationEntry", sysLoginConfigId ).splitlines()
        for entry in configurationEntries:
                alias = AdminConfig.showAttribute(entry, "alias" )
                if (alias == sysentry):
                        print "Deleting "+sysentry+" JAAS Configuration entry"
                        AdminConfig.remove(entry )
                        break 
                #endIf 
        #endFor 

        # Create the configuration entry
        print "Recreating "+sysentry+" JAAS Configuration entry"
        module_attrib = []
        module_attrib.append(["alias", sysentry])
        defaultConfigurationEntry = AdminConfig.create("JAASConfigurationEntry", sysLoginConfigId, module_attrib )

        # Create Curam JAAS login module
        print "Creating Curam JAAS login module"
        module_attrib = []
        module_attrib.append(["moduleClassName", loginModuleClassName])
        module_attrib.append(["authenticationStrategy", loginModuleAuthStrategy])
        newJAASLoginModuleId = AdminConfig.create("JAASLoginModule", defaultConfigurationEntry, module_attrib )

        # Required attributes
        options_attrib = []
        options_attrib.append(["name", "delegate"])
        options_attrib.append(["value", curam_loginModule])
        AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )

        options_attrib = []
        options_attrib.append(["name", "module_name"])
        options_attrib.append(["value", sysentry])
        AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )

        # Add the database and websphere user to the excluded list
        options_attrib = []
        usernames=""
        usernames=usernames+AdminUsername
        usernames=usernames+usernameDelimiter
        usernames=usernames+j2c_userid
        options_attrib.append(["name", "exclude_usernames"])
        options_attrib.append(["value", usernames])
        AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )

        # Optional attributes
        if (login_trace != []):
                options_attrib = []
                options_attrib.append(["name", "login_trace"])
                options_attrib.append(["value", login_trace])
                AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )
        #endIf 

        if (checkIdentityOnly != []):
                options_attrib = []
                options_attrib.append(["name", "check_identity_only"])
                options_attrib.append(["value", checkIdentityOnly])
                AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )
        #endIf 

        if (userRegistryEnabled != []):
                options_attrib = []
                options_attrib.append(["name", "user_registry_enabled"])
                options_attrib.append(["value", userRegistryEnabled])
                AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )
        #endIf 

        if (usernameDelimiter != ","):
                options_attrib = []
                options_attrib.append(["name", "exclude_usernames_delimiter"])
                options_attrib.append(["value", usernameDelimiter])
                AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )
        #endIf 

        if (userRegistryEnabledTypes != []):
                options_attrib = []
                options_attrib.append(["name", "user_registry_enabled_types"])
                options_attrib.append(["value", userRegistryEnabledTypes])
                AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )
        #endIf 

        if (userRegistryDisabledTypes != []):
                options_attrib = []
                options_attrib.append(["name", "user_registry_disabled_types"])
                options_attrib.append(["value", userRegistryDisabledTypes])
                AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )
        #endIf 

        # Create original login modules
        module_attrib = []
        module_attrib.append(["moduleClassName", loginModuleClassName])
        module_attrib.append(["authenticationStrategy", loginModuleAuthStrategy])

        newJAASLoginModuleId = AdminConfig.create("JAASLoginModule", defaultConfigurationEntry, module_attrib )
        options_attrib = []
        options_attrib.append(["name", "delegate"])
        options_attrib.append(["value", ltpaLoginModule])
        AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )

        newJAASLoginModuleId = AdminConfig.create("JAASLoginModule", defaultConfigurationEntry, module_attrib )
        options_attrib = []
        options_attrib.append(["name", "delegate"])
        options_attrib.append(["value", wsMapDefaultInboundLoginModule])
        AdminConfig.modify(newJAASLoginModuleId, [["options", [options_attrib]]] )

#endDef 

# Add Authentication Information for Database Connection
def configJ2CAuthData (  ):
        global AdminConfig, AdminTask, nodeName
        global j2c_alias, j2c_userid, j2c_password, j2c_description

        print "Adding J2C Authentication Data"

        security = AdminConfig.list("Security" )
        attrib = []
        matchFound = 0
        nodeId = AdminConfig.getid("/Node:"+nodeName+"/" )
        hostname = AdminConfig.showAttribute(nodeId, "hostName" )
        givenAlias = hostname+"/"+j2c_alias

        listJAASAuthData = AdminConfig.list("JAASAuthData" ).splitlines()
        for jaasAuthId in listJAASAuthData:        
                existingAlias = AdminConfig.showAttribute(jaasAuthId, "alias" )                
                if (cmp(givenAlias, existingAlias) == 0):
                        matchFound = 1
                        break 
                #endIf 
        #endFor 

        attrib.append(["alias", givenAlias])

        attrib.append(["userId", j2c_userid])
        attrib.append(["password", j2c_password])
        attrib.append(["description", j2c_description])

        if (len(attrib) != 0):
                if (matchFound != 1):
                        AdminConfig.create("JAASAuthData", security, attrib )
                else:
                        AdminConfig.modify(jaasAuthId, [["userId", j2c_userid]] )
                        AdminConfig.modify(jaasAuthId, [["password", j2c_password]] )
                        AdminConfig.modify(jaasAuthId, [["description", j2c_description]] )
                #endElse 
        #endIf 

        return "true"
#endDef 

# Set Interoperability for LTPA token cookie support
def configLTPAInteroperability (  ):
        global AdminConfig, LTPAInteroperability

        print "Setting LTPA token interoperability to "+LTPAInteroperability

        security = AdminConfig.list("Security" )
        properties = AdminConfig.showAttribute(security, "properties" ).split()
        for property in properties:
                property = property.replace("[", "")
                property = property.replace("]", "")
                if (regexp("com.ibm.ws.security.ssoInteropModeEnabled", property) ):
                        interoperability = property
                        break
                #endIf 
        #endFor 
        AdminConfig.modify(interoperability, [["value", LTPAInteroperability]] )        
#endDef 

# Set Single Sign-on LtpaToken2 login
def configSingleSignOnLtpaToken2login (  ):
        global AdminConfig, SingleSignOnLtpaToken2login

        print "Setting Single Sign-on LTPA Token2 login to "+ SingleSignOnLtpaToken2login

        security = AdminConfig.list("Security" )
        properties = AdminConfig.showAttribute(security, "properties" ).split()
        for property in properties:
                property = property.replace("[", "")
                property = property.replace("]", "")
                if (regexp("com.ibm.ws.security.webChallengeIfCustomSubjectNotFound", property) ):
                        singleSignOn = property
                        break
                #endIf 
        #endFor 
        AdminConfig.modify(singleSignOn, [["value", SingleSignOnLtpaToken2login]] )
        
#endDef

# Enable Secure Flag for JSessionID Cookie and set default JSessionID WAS settings
def enableJSessionIDSecureFlag():
	global AdminConfig, cookieName, cookieMaxAge, cookieDomain 
        global cookiePath, cookieSecure, cookieUseContextRootAsPath
			
	print "Enabling the Secure attibute on JSESSIONID cookie"

        jsessionId = AdminConfig.list("Cookie" ).splitlines()[1].replace(cookieName, "")
        
        attrib = []
        try:
                AdminConfig.showAttribute(jsessionId, "maximumAge")
                attrib.append(["maximumAge", cookieMaxAge])
        except:
                print "maximumAge - attribute not available for cookie in this version of Websphere and is therefore not required."
        try:
                AdminConfig.showAttribute(jsessionId, "name")
                attrib.append(["name", cookieName])
        except:
                print "name - attribute not available for cookie in this version of Websphere and is therefore not required."
        try:
                AdminConfig.showAttribute(jsessionId, "useContextRootAsPath")
                attrib.append(["useContextRootAsPath", cookieUseContextRootAsPath])
        except:
                print "useContextRootAsPath - attribute not available for cookie in this version of Websphere and is therefore not required."
        try:
                AdminConfig.showAttribute(jsessionId, "domain")
                attrib.append(["domain", cookieDomain])
        except:
                print "domain - attribute not available for cookie in this version of Websphere and is therefore not required."
        try:
                AdminConfig.showAttribute(jsessionId, "path")
                attrib.append(["path", cookiePath])
        except:
                print "path - attribute not available for cookie in this version of Websphere and is therefore not required."
	try:
                AdminConfig.showAttribute(jsessionId, "secure")
                attrib.append(["secure", "true"])
        except:
                print "secure - attribute not available for cookie in this version of Websphere and is therefore not required."
                
        AdminConfig.modify(jsessionId, attrib )                
#endDef


######################################################################################################
#
# validation
# Procedure to validate security settings.  security cache timeout needs to be greater than 30
# sec. Username and password needs to be set.
#
######################################################################################################

def validation (  ):
        global AdminConfig, AdminUsername, j2c_userid, AdminTask, platform, useZosSAFSecurity

        security = AdminConfig.list("Security" )
        activeauthmech = AdminConfig.showAttribute(security, "activeAuthMechanism" )
        activeuserreg = AdminConfig.showAttribute(security, "activeUserRegistry" )
        userid = AdminConfig.showAttribute(activeuserreg, "primaryAdminId" )
        LTPA = AdminConfig.list("LTPA" )

        # validate cachetimeout.  This value should not be null and should > 30
        cachetimeout = AdminConfig.showAttribute(security, "cacheTimeout" )
        if (cachetimeout == "" or cachetimeout < 30):
                global errorInfo
                errorInfo = str(_tbck_.dumpStack())
                global errorCode
                errorCode = None
                raise RuntimeError, ["You must enter a security cache timeout value > 30."]
        #endIf 

        if (useZosSAFSecurity != "true"):
                if (userid == ""):
                        print "Uid is null.  Please enter valid Uid"
                #endIf 
        #endIf 

        if (AdminConfig.showAttribute(security, "cacheTimeout" ) == []):
                print "Security Cache Timeout needs to be set"
                return "false"
        #endIf 
        if (AdminConfig.showAttribute(security, "activeAuthMechanism" ) == []):
                print "Active Authentication Mechanism needs to be set"
                return "false"
        #endIf 
        if (AdminConfig.showAttribute(security, "activeProtocol" ) == []):
                print "Active Protocol needs to be set"
                return "false"
        #endIf 
        if (AdminConfig.showAttribute(security, "activeUserRegistry" ) == []):
                print "Active User Registry needs to be set"
                return "false"
        #endIf 

        if (regexp("LTPA", AdminConfig.showAttribute(security, "activeAuthMechanism" )) == 0):
                print "LTPA is not set as default authentication mechanism"
        #endIf 

        if (AdminConfig.showAttribute(LTPA, "timeout" ) == []):
                print "LTPA timeout needs to be set"
                return "false"
        #endIf 

        return "true"
#endDef 

#####################################################################################################
#
#  Main procedure
#
#####################################################################################################
print ""
print "Curam Websphere Security Configuration Script"
print ""

propFile = sys.argv[0]
props = loadProperties(propFile )
validationResult = "true"
nodeName = sys.argv[1]
xmlServerPath = sys.argv[2]
jwtPropFile = sys.argv[3]
jwtCertPath = sys.argv[4]

parsePropFile(props )

generateLTPAKeys(LTPAPassword )

if (platform == "zos" and useZosSAFSecurity == "true"):
        # z/OS SAF/RACF requires:
        #   userRegistryEnabled (curam.security.user.registry.enabled) - true
        #   checkIdentityOnly can be set as the user desires.  
        #
        print "z/OS security being configured to use SAF."

        if (userRegistryEnabled == []):
                userRegistryEnabled = "true"
                print "  User Registry being set to enabled."
        #endIf 
#endIf 
else:
        createUsers( )
        setActiveUserRegistry( )
#endElse 

configGlobalSecurity( )
configTLS12( )
addSSLCertificate( )
addJWTCertificate( )
createUsersAndGroups( )
configJ2CAuthData( )
configSysJAASLogin("DEFAULT" )
configSysJAASLogin("WEB_INBOUND" )
configSysJAASLogin("RMI_INBOUND" )
configLTPAInteroperability( )
configSingleSignOnLtpaToken2login( )
enableJSessionIDSecureFlag()

print "Validating Configuration"
validationResult = validation( )

# Save if everything validated correctly
if (validationResult =="true"):
        AdminConfig.validate( )
        AdminConfig.save( )
else:
        print "Validation failed. Please correct the missing/incorrect fields."
#endElse 
