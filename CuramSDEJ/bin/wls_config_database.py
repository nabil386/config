#/*
# * Licensed Materials - Property of IBM
# *
# * PID 5725-H26
# *
# * Copyright IBM Corporation 2018. All rights reserved.
# *
# * US Government Users Restricted Rights - Use, duplication or disclosure
# * restricted by GSA ADP Schedule Contract with IBM Corp.
# */


from weblogic.descriptor import BeanAlreadyExistsException
from java.lang.reflect import UndeclaredThrowableException
from java.lang import System
import javax
from javax import management
from java.lang import UnsupportedOperationException
from java.lang import Exception
from jarray import array

import sys

def connectToServer():
  print "INFO - Connecting to WebLogic ..."
  try:
    URL="t3://"+adminServer+":"+curam_server_port
    connect(security_username, security_password, URL)
  except WLSTException:
    print 'ERROR - No server is running at '+URL+'.'
    sys.exit (1)
  print "INFO - Connected to WebLogic."

def startTransaction():
  edit()
  startEdit()

def endTransaction():
  startEdit()
  save()
  activate(block="true")

def create_JDBCSystemResource(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type JDBCSystemResource @ " + path + " ... "
    theBean = cmo.lookupJDBCSystemResource(beanName)
    if theBean == None:
      cmo.createJDBCSystemResource(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_Property(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type Property @ " + path + " ... "
    theBean = cmo.lookupProperty(beanName)
    if theBean == None:
      cmo.createProperty(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"
  except TypeError:
    prop = cmo.createProperty()
    prop.setName(beanName)

def setDbPoolAttributes(poolName, serverName):
  cd("/JDBCSystemResources/CP-" + poolName)
  print "INFO - Setting attributes for mbean " + poolName + " of type JDBCSystemResource for " + serverName + "."
  set("DeploymentOrder", "1")
  refBean0 = getMBean("/Servers/" + serverName)
  theValue = jarray.array([refBean0], Class.forName("weblogic.management.configuration.TargetMBean"))
  cmo.setTargets(theValue)
  
def setDbPoolAttribute_User(poolName, userName):
  cd("/JDBCSystemResources/CP-" + poolName + "/JDBCResource/CP-" + poolName + "/JDBCDriverParams/CP-" + poolName + "/Properties/CP-" + poolName + "/Properties/user")
  print "INFO - Setting user attributes for mbean " + poolName + " of type JDBCProperty; user=" + userName + "."
  set("Name", "user")
  set("Value", curam_db_username)

def setDbPoolAttribute_LegacyType(poolName):
  cd("/JDBCSystemResources/CP-" + poolName + "/JDBCResource/CP-" + poolName + "/InternalProperties/CP-" + poolName + "/Properties/LegacyType")
  print "INFO - Setting LegacyType attribute for mbean " + poolName + " of type JDBCProperty."
  set("Name", "LegacyType")
  set("Value", "1")  

def setDbPoolAttribute_TestConnectionsOnCreate(poolName):
  cd("/JDBCSystemResources/CP-" + poolName + "/JDBCResource/CP-" + poolName + "/InternalProperties/CP-" + poolName + "/Properties/TestConnectionsOnCreate")
  print "INFO - Setting TestConnectionsOnCreate attribute for mbean " + poolName + " of type JDBCProperty."
  set("Name", "TestConnectionsOnCreate")
  set("Value", "true")

def setDbPoolAttribute_TestConnectionsOnRelease(poolName):
  cd("/JDBCSystemResources/CP-" + poolName + "/JDBCResource/CP-" + poolName + "/InternalProperties/CP-" + poolName + "/Properties/TestConnectionsOnRelease")
  print "INFO - Setting TestConnectionsOnRelease attribute for mbean " + poolName + " of type JDBCProperty."
  set("Name", "TestConnectionsOnRelease")
  set("Value", "true")

def setDbAttributes(datasourceName, serverName):
  cd("/JDBCSystemResources/TxDS-" + datasourceName)
  print "INFO - Setting attributes for mbean " + datasourceName + " of type JDBCSystemResource for " + serverName + "."
  set("DeploymentOrder", "4")
  refBean0 = getMBean("/Servers/" + serverName)
  theValue = jarray.array([refBean0], Class.forName("weblogic.management.configuration.TargetMBean"))
  cmo.setTargets(theValue)

def setDbAttribute_LegacyType(datasourceName):
  cd("/JDBCSystemResources/TxDS-" + datasourceName + "/JDBCResource/TxDS-" + datasourceName + "/InternalProperties/TxDS-" + datasourceName + "/Properties/LegacyType")
  print "INFO - Setting LegacyType attribute for mbean " + datasourceName + " of type JDBCProperty."
  set("Name", "LegacyType")
  set("Value", "4")

def setDbAttribute_LegacyPoolName(datasourceName):
  cd("/JDBCSystemResources/TxDS-" + datasourceName + "/JDBCResource/TxDS-" + datasourceName + "/InternalProperties/TxDS-" + datasourceName + "/Properties/LegacyPoolName")
  print "INFO - Setting LegacyPoolName attribute for mbean " + datasourceName + " of type JDBCProperty."
  set("Name", "LegacyPoolName")
  set("Value", JDBCConnectionPool)

def setDbPoolParamsAttributes(poolName):
  cd("/JDBCSystemResources/CP-" + poolName + "/JDBCResource/CP-" + poolName + "/JDBCConnectionPoolParams/CP-" + poolName)
  print "INFO - Setting attributes for mbean " + poolName + " of type JDBCConnectionPoolParams."
  set("TestConnectionsOnReserve", "true")
  set("TestTableName",  TestTableName)

def setDbPoolAttribute_Name(poolName):
  cd("/JDBCSystemResources/CP-" + poolName + "/JDBCResource/CP-" + poolName)
  print "INFO - Setting Name attribute for mbean " + poolName + " of type JDBCDataSource."
  set("Name", poolName)

def setDbPoolJDBCDriverParamsAttributes(poolName):
  cd("/JDBCSystemResources/CP-" + poolName + "/JDBCResource/CP-" + poolName + "/JDBCDriverParams/CP-" + poolName)
  print "INFO - Setting attributes for mbean " + poolName + " of type JDBCDriverParams."
  set("DriverName", "oracle.jdbc.xa.client.OracleXADataSource")
  set('Password', curam_db_password)
  set("Url", dbURL)
  set('Password', curam_db_password)

  
def setDbAttribute_Name(datasourceName):
  cd("/JDBCSystemResources/TxDS-" + datasourceName + "/JDBCResource/TxDS-" + datasourceName)
  print "Info - Setting Name attribute for mbean " + datasourceName + " of type JDBCDataSource."
  set("Name", datasourceName)
  
def setDbJDBCDataSourceParamsAttributes(datasourceName):
  cd("/JDBCSystemResources/TxDS-" + datasourceName + "/JDBCResource/TxDS-" + datasourceName + "/JDBCDataSourceParams/TxDS-" + datasourceName)
  print "INFO - Setting attributes for mbean " + datasourceName + " of type JDBCDataSourceParams."
  set("JNDINames", jarray.array(["jdbc/" + datasourceName], String))
  set("GlobalTransactionsProtocol", "OnePhaseCommit")


#=====
# MAIN
#=====
try:

  node_name=sys.argv[1]
  print "INFO - node_name=" + node_name
  
  adminServer=sys.argv[2]
  print "INFO - adminServer=" + adminServer

  curam_server_port=sys.argv[3]
  print "INFO - curam_server_port=" + curam_server_port

  security_username=sys.argv[4]
  print "INFO - security_username=" + security_username
    
  security_password=sys.argv[5]
  #print "INFO - security_password=" + security_password
 
  curam_server_name=sys.argv[6]
  print "INFO - curam_server_name=" + curam_server_name
  
  dbURL=sys.argv[7]
  print "INFO - Oracle database URL=" + dbURL
  
  curam_db_username=sys.argv[8]
  print "INFO - curam_db_username=" + curam_db_username
  
  curam_db_password=sys.argv[9]
  #print "INFO - curam_db_password=" + curam_db_password
  
  DataSourceName=sys.argv[10]
  print "INFO - DataSourceName=" + DataSourceName

  JDBCConnectionPool=sys.argv[11]
  print "INFO - JDBCConnectionPool=" + JDBCConnectionPool

  TestTableName=sys.argv[12]
  print "INFO - TestTableName=" + TestTableName

  WLSTVerboseMode=sys.argv[13]
  print "INFO - WLSTVerboseMode=" + WLSTVerboseMode
  
  print " "
  
  
  
  if (WLSTVerboseMode != "true"):
    # These are APIs that WebLogic generates via configToScript that are not documented and do not have any apparent functionality:
    hideDisplay()
    hideDumpStack("true")
  #endIf
  
  connectToServer()
  
  startTransaction()
  
  create_JDBCSystemResource("/", "CP-" + JDBCConnectionPool)
  create_Property("/JDBCSystemResources/CP-" + JDBCConnectionPool + "/JDBCResource/CP-" + JDBCConnectionPool + "/JDBCDriverParams/CP-" + JDBCConnectionPool + "/Properties/CP-" + JDBCConnectionPool, "user")
  create_Property("/JDBCSystemResources/CP-" + JDBCConnectionPool + "/JDBCResource/CP-" + JDBCConnectionPool + "/InternalProperties/CP-" + JDBCConnectionPool, "LegacyType")
  create_Property("/JDBCSystemResources/CP-" + JDBCConnectionPool + "/JDBCResource/CP-" + JDBCConnectionPool + "/InternalProperties/CP-" + JDBCConnectionPool, "TestConnectionsOnCreate")
  create_Property("/JDBCSystemResources/CP-" + JDBCConnectionPool + "/JDBCResource/CP-" + JDBCConnectionPool + "/InternalProperties/CP-" + JDBCConnectionPool, "TestConnectionsOnRelease")
  
  create_JDBCSystemResource("/", "TxDS-" + DataSourceName)
  create_Property("/JDBCSystemResources/TxDS-" + DataSourceName + "/JDBCResource/TxDS-" + DataSourceName + "/InternalProperties/TxDS-" + DataSourceName, "LegacyType")
  create_Property("/JDBCSystemResources/TxDS-" + DataSourceName + "/JDBCResource/TxDS-" + DataSourceName + "/InternalProperties/TxDS-" + DataSourceName, "LegacyPoolName")

  setDbPoolAttributes(JDBCConnectionPool, curam_server_name)
  setDbPoolAttribute_User(JDBCConnectionPool, curam_db_username)
  setDbPoolAttribute_LegacyType(JDBCConnectionPool)
  setDbPoolAttribute_TestConnectionsOnCreate(JDBCConnectionPool)
  setDbPoolAttribute_TestConnectionsOnRelease(JDBCConnectionPool)
  
  setDbAttributes(DataSourceName, curam_server_name)
  setDbAttribute_LegacyType(DataSourceName)
  setDbAttribute_LegacyPoolName(DataSourceName)

  setDbPoolParamsAttributes(JDBCConnectionPool)
  setDbPoolAttribute_Name(JDBCConnectionPool)
  setDbPoolJDBCDriverParamsAttributes(JDBCConnectionPool)

  setDbAttribute_Name(DataSourceName)
  setDbJDBCDataSourceParamsAttributes(DataSourceName)

  endTransaction()
  print "INFO - End of configuration script."
  
finally:
  print "INFO - Exiting configuration script."
  
# <end>

