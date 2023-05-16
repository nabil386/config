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

#----------------------------------
# Creates mapping module structure
#----------------------------------
def getMappingModule (  ):
        global J2CAlias
        authDataAlias_attr = ["authDataAlias", J2CAlias]
        mappingConfigAlias_attr = ["mappingConfigAlias", "DefaultPrincipalMapping"]
        mapping_props = [authDataAlias_attr, mappingConfigAlias_attr]
        mapping_attr = ["mapping", mapping_props]
        return mapping_attr
#endDef

def setupProvider (  ):

        #--------------------------------------------------------------
        # set up globals
        #--------------------------------------------------------------
        global AdminConfig, J2CAlias, CURAMSDEJ, DB2JCC_LICENSE_CISUZ_JAR, dbtype
        global serverName, provider, nodeName, templateName
        global contAlias, datasourceHelperClassname
        global dbmachine, dbport, dbname, dbServiceName, AdminControl, DB2DIR, dbzosjccpropfile

        DEFAULT_PRINCIPAL_MAPPING = "DefaultPrincipalMapping"

        if (cmp("ZOSRRS", dbtype) == 0):
                templateIndex = 3
        else:
                templateIndex = 0
        #endElse
        template = AdminConfig.listTemplates("JDBCProvider", templateName ).splitlines()
        template = template[templateIndex]
        if (len(template) == 0):
                print "Could not find a JDBCProvider template using "+templateName
                return 1
        #endIf

        scope = AdminConfig.getid("/Node:"+nodeName+"/" )
        varMap = AdminConfig.getid("/Node:"+nodeName+"/VariableMap:/" )
        variableList = AdminConfig.list("VariableSubstitutionEntry", varMap ).splitlines()
        for variable in variableList:
                existingvariable = AdminConfig.showAttribute(variable, "symbolicName" )

                if (cmp("ZOSRRS", dbtype) == 0):
                        if (cmp("DB2_JCC_DRIVER_PATH", existingvariable) == 0):
                                valattr1 = ["value", DB2DIR+"/classes"]
                                attrs = [valattr1]
                                AdminConfig.modify(variable, attrs )
                        #endIf
                        if (cmp("DB2_JCC_DRIVER_NATIVEPATH", existingvariable) == 0):
                                valattr1 = ["value", DB2DIR+"/lib"]
                                attrs = [valattr1]
                                AdminConfig.modify(variable, attrs )
                        #endIf
                #endIf
                else:
                        if (cmp("DB2_JCC_DRIVER_PATH", existingvariable) == 0):
                                valattr1 = ["value", CURAMSDEJ+"/drivers"]
                                attrs = [valattr1]
                                AdminConfig.modify(variable, attrs )
                        #endIf
                        if (cmp("ORACLE_JDBC_DRIVER_PATH", existingvariable) == 0):
                                valattr1 = ["value", CURAMSDEJ+"/drivers"]
                                attrs = [valattr1]
                                AdminConfig.modify(variable, attrs )
                        #endIf
                #endElse
        #endFor

        nodeId = AdminConfig.getid("/Node:"+nodeName+"/" )
        hostname = AdminConfig.showAttribute(nodeId, "hostName" )
        J2CAlias = hostname+"/"+contAlias

        print "Checking for existence of previous JDBCProviders"
        providerList = AdminConfig.list("JDBCProvider", scope ).splitlines()
        for provider in providerList:
                if (cmp(AdminConfig.showAttribute(provider, "name" ), templateName) == 0):
                        AdminConfig.remove(provider )
                #endIf
        #endFor

        print "Creating JDBCProvider named "+templateName
        name_attr = ["name", templateName]
        attrs = [name_attr]
        provider = AdminConfig.createUsingTemplate("JDBCProvider", scope, attrs, template )

        if (cmp("ZOS", dbtype) == 0):
                attr1 = ["classpath", DB2JCC_LICENSE_CISUZ_JAR]
                attrs = [attr1]
                AdminConfig.modify(provider, attrs )
        #endIf
        
        #Set Oracle JDBCProvider classpath to use ojdbc8.jar
        if (cmp("ORA", dbtype) == 0):
                AdminConfig.unsetAttributes(provider, '["classpath"]')
                attr1 = ["classpath", "${ORACLE_JDBC_DRIVER_PATH}/ojdbc8.jar"]
                attrs = [attr1]
                AdminConfig.modify(provider, attrs )
        #endIf

        defaultDS = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:/" )
        AdminConfig.remove(defaultDS )

        was40ds = AdminConfig.getid("/JDBCProvider:"+templateName+"/WAS40DataSource:/" )
        AdminConfig.remove(was40ds )

        _J2J_bracket_ = cmp("ZOSRRS", dbtype)
        if (_J2J_bracket_ == 0  and len(dbzosjccpropfile) > 0):
                print "Setting z/OS Type 2 JDBC driver JVM property db2.jcc.propertiesFile="+dbzosjccpropfile

                systemProperty = []
                systemProperty.append(["name", "db2.jcc.propertiesFile"])
                systemProperty.append(["value", dbzosjccpropfile])

                jvm = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
                jvmList = AdminConfig.list("JavaVirtualMachine", jvm ).splitlines()
                for jvm in jvmList:
                        AdminConfig.modify(jvm, [["systemProperties", [systemProperty]]] )
                #endFor
        #endIf
#endDef

def setupDataSource ( jdbcname ):

        global AdminConfig, J2CAlias, CURAMSDEJ, DB2JCC_LICENSE_CISUZ_JAR, dbtype
        global nodeName, serverName, dstemplateName, templateName
        global contAlias, datasourceHelperClassname
        global dbmachine, dbport, dbname, dbServiceName, AdminControl, provider, dbdrivertype, DB2DIR
        global purescale, db2ssl, db2TrustStoreLocation, db2TrustStorePassword

        template = AdminConfig.listTemplates("DataSource", dstemplateName ).splitlines()
        template = template[0]
        if (len(template) == 0):
                print "The "+dstemplateName+" data source template could not be found."
                return 1
        #endIf

        print "Creating DataSource named "+jdbcname
        rra = AdminConfig.getid("/Node:"+nodeName+"/J2CResourceAdapter:WebSphere Relational Resource Adapter/" )
        attr1 = ["name", jdbcname]
        attr2 = ["jndiName", "jdbc/"+jdbcname]
        attr3 = ["authDataAlias", J2CAlias]
        attr4 = ["authMechanismPreference", "BASIC_PASSWORD"]
        attr5 = getMappingModule( )
        attr6 = ["datasourceHelperClassname", datasourceHelperClassname]
        attr7 = ["relationalResourceAdapter", rra]
        attrs = [attr1, attr2, attr3, attr4, attr5, attr6, attr7]
        ds = AdminConfig.createUsingTemplate("DataSource", provider, attrs, template )

        #--------------------------------------------------------------
        # Add desired properties to the DataSource.
        #--------------------------------------------------------------
        if (cmp("ORA", dbtype) == 0):
          # If Oracle service name is not set then we get its value as ${curam.db.oracle.servicename}.
          if (cmp("${curam.db.oracle.servicename}", dbServiceName) != 0):
            dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:URL" )
            attr1 = ["value", "jdbc:oracle:thin:/@//"+dbmachine+":"+dbport+"/"+dbServiceName]
            attrs = [attr1]
            AdminConfig.modify(dbname1, attrs )
          else:
            dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:URL" )
            attr1 = ["value", "jdbc:oracle:thin:@"+dbmachine+":"+dbport+":"+dbname]
            attrs = [attr1]
            AdminConfig.modify(dbname1, attrs )
          #endIf        
           
        #endIf
        
        if (cmp("ORA", dbtype) != 0):
          dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:databaseName" )
          attr1 = ["value", dbname]
          attrs = [attr1]
          AdminConfig.modify(dbname1, attrs )

          dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:driverType" )
          attr1 = ["value", dbdrivertype]
          attrs = [attr1]
          AdminConfig.modify(dbname1, attrs )

          dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:serverName" )
          attr1 = ["value", dbmachine]
          attrs = [attr1]
          AdminConfig.modify(dbname1, attrs )

          dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:portNumber" )
          attr1 = ["value", dbport]
          attrs = [attr1]
          AdminConfig.modify(dbname1, attrs )

          dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:fullyMaterializeLobData" )
          attr1 = ["value", "false"]
          attrs = [attr1]
          AdminConfig.modify(dbname1, attrs )

          if purescale.lower() in ['true', 'yes']:

            print "Setting DB2 pureScale enableSysplexWLB="+purescale
            dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/" )
            propName = ["name", "enableSysplexWLB"]
            propValue = ["value", "true"]
            propType = ["type", "java.lang.String"]
            propAttrs = [propName, propValue, propType]
            propCreate = AdminConfig.create('J2EEResourceProperty', dbname1, propAttrs)
          #endIf
          
          if db2ssl.lower() in ['true', 'yes']:

            print "Enabling DB2 SSL Connection for "+jdbcname
            dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/J2EEResourceProperty:sslConnection" )
            attr1 = ["value", "true"]
            attrs = [attr1]
            AdminConfig.modify(dbname1, attrs )

            if len(db2TrustStoreLocation) > 0:
                dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/" )
                propName = ["name", "sslTrustStoreLocation"]
                propValue = ["value", db2TrustStoreLocation]
                propType = ["type", "java.lang.String"]
                propAttrs = [propName, propValue, propType]
                propCreate = AdminConfig.create('J2EEResourceProperty', dbname1, propAttrs)

            if len(db2TrustStorePassword) > 0:
                dbname1 = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/J2EEResourcePropertySet:/" )
                propName = ["name", "sslTrustStorePassword"]
                propValue = ["value", db2TrustStorePassword]
                propType = ["type", "java.lang.String"]
                propAttrs = [propName, propValue, propType]
                propCreate = AdminConfig.create('J2EEResourceProperty', dbname1, propAttrs)
        #endIf

        cp = AdminConfig.getid("/Node:"+nodeName+"/JDBCProvider:"+templateName+"/DataSource:"+jdbcname+"/ConnectionPool:/" )
        attr1 = ["maxConnections", 30]
        attrs = [attr1]
        AdminConfig.modify(cp, attrs )

        #---------------------------------------------------------
        # Create a CMPConnectorFactory, using the datasource from earlier
        #---------------------------------------------------------
        print "Create a new CMPConnectorFactory object named "+jdbcname+"_CF"

        connectorfactoryexists = AdminConfig.getid("/Node:"+nodeName+"/J2CResourceAdapter:WebSphere Relational Resource Adapter/CMPConnectorFactory:"+jdbcname+"_CF" )
        print "Checking for existence of CMPConnectorFactory "+jdbcname+"_CF"
        if (len(connectorfactoryexists) != 0):
                print "CMPConnectorFactory "+jdbcname+"_CF already exists - removing"
                AdminConfig.remove(connectorfactoryexists )
        #endIf

        template = AdminConfig.listTemplates("CMPConnectorFactory" ).splitlines()
        template = template[0]
        if (len(template) == 0):
                print "Could not find a CMPConnectorFactory template"
                return 1
        #endIf

        attr1 = ["name", jdbcname+"_CF"]
        attr2 = ["authMechanismPreference", "BASIC_PASSWORD"]
        attr3 = ["cmpDatasource", ds]
        attr4 = getMappingModule( )
        attr5 = ["authDataAlias", J2CAlias]
        attrs = [attr1, attr2, attr3, attr4, attr5]
        new1 = AdminConfig.create("CMPConnectorFactory", rra, attrs )
#endDef

def configureTimerService ( ):

        global AdminConfig, J2CAlias
        global nodeName, serverName
        global contAlias, AdminControl, provider, dbdrivertype

        server = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        container = AdminConfig.list("EJBContainer", server )
        timer = AdminConfig.showAttribute(container, "timerSettings" )
        attr1 = ["datasourceJNDIName", "jdbc/curamtimerdb"]
        attr2 = ["datasourceAlias", J2CAlias]
        attrs = [attr1, attr2]
        AdminConfig.modify(timer, attrs )

#endDef


#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam DataSource Configuration Script"
print ""

global serverName, CURAMSDEJ, DB2JCC_LICENSE_CISUZ_JAR, dbtype
global contAlias, datasourceHelperClassname, dstemplateName
global dbmachine, dbport, dbname, nodeName, templateName, dbdrivertype, DB2DIR, dbzosjccpropfile
global purescale, db2ssl, db2TrustStoreLocation, db2TrustStorePassword

import getopt
opts, args = getopt.getopt(sys.argv, "", ["nodeName=", "serverName=", "dbType=", "contAlias=",
                                          "sdejDir=", "db2JccLicJar=", "dbName=", "dbServiceName=",
                                          "dbPort=", "dbMachine=", "dbDriverType=", "platform=",
                                          "db2Dir=", "pureScale=", "dbZosJccPropfile=",
                                          "db2ssl=", "db2TrustStoreLocation=", "db2TrustStorePassword="])
for opt, arg in opts:
    if (opt == "--nodeName"):
        nodeName = arg
    if (opt == "--serverName"):
        serverName = arg
    if (opt == "--dbType"):
        dbtype = arg
    if (opt == "--contAlias"):
        contAlias = arg
    if (opt == "--sdejDir"):
        CURAMSDEJ = arg
    if (opt == "--db2JccLicJar"):
        DB2JCC_LICENSE_CISUZ_JAR = arg
    if (opt == "--dbName"):
        dbname = arg
    if (opt == "--dbServiceName"):
        dbServiceName = arg
    if (opt == "--dbPort"):
        dbport = arg
    if (opt == "--dbMachine"):
        dbmachine = arg
    if (opt == "--dbDriverType"):
        dbdrivertype = arg
    if (opt == "--platform"):
        platform = arg
    if (opt == "--db2Dir"):
        DB2DIR = arg
    if (opt == "--pureScale"):
        purescale = arg
    if (opt == "--dbZosJccPropfile"):
        # This argument is optional for when DB2 Type 2 is being used on z/OS.
        dbzosjccpropfile = arg
    if (opt == "--db2ssl"):
        db2ssl = arg
    if (opt == "--db2TrustStoreLocation"):
        db2TrustStoreLocation = arg
    if (opt == "--db2TrustStorePassword"):
        db2TrustStorePassword = arg

if (cmp("DB2", dbtype) == 0):
        templateName = "DB2 Using IBM JCC Driver (XA)"
        dstemplateName = "DB2 Using IBM JCC Driver - XA DataSource"
        datasourceHelperClassname = "com.ibm.websphere.rsadapter.DB2UniversalDataStoreHelper"
#endIf
if (cmp("ORA", dbtype) == 0):
        templateName = "Oracle JDBC Driver (XA)"
        dstemplateName = "Oracle JDBC Driver XA DataSource"
        datasourceHelperClassname = "com.ibm.websphere.rsadapter.Oracle11gDataStoreHelper"
#endIf
if (cmp("ZOS", dbtype) == 0):
        templateName = "DB2 Using IBM JCC Driver (XA)"
        dstemplateName = "DB2 Using IBM JCC Driver - XA DataSource"
        datasourceHelperClassname = "com.ibm.websphere.rsadapter.DB2UniversalDataStoreHelper"
#endIf

if (cmp("ZOS", dbtype) == 0):
        _J2J_bracket_ = cmp("2", dbdrivertype)
        if (_J2J_bracket_ == 0  and cmp("zos", platform) == 0):
                dbtype = "ZOSRRS"
        #endIf
#endIf

if (cmp("ZOSRRS", dbtype) == 0):
        templateName = "DB2 Using IBM JCC Driver"
        dstemplateName = "DB2 Using IBM JCC Driver - DataSource"
        datasourceHelperClassname = "com.ibm.websphere.rsadapter.DB2UniversalDataStoreHelper"
        if (len(dbzosjccpropfile) == 0):
                print " "
                print "Note: When using the DB2 Type 2 Universal JDBC Driver (RRS, z/OS-only) you may require "
                print "      additional configuration steps depending upon your local DB2 configuration.      "
                print "      See the WebSphere InfoCenter and the IBM Techdoc TD101663 for more information.  "
                print " "
        #endIf
#endIf

setupProvider( )
setupDataSource("curamdb" )
setupDataSource("curamsibdb" )
if (cmp("ORA", dbtype) == 0):
        templateName = "Oracle JDBC Driver"
        dstemplateName = "Oracle JDBC Driver DataSource"
        datasourceHelperClassname = "com.ibm.websphere.rsadapter.Oracle11gDataStoreHelper"
        setupProvider( )
#endIf
setupDataSource("curamtimerdb" )
configureTimerService( )

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "DataSource Configuration Script executed successfully."
print ""
