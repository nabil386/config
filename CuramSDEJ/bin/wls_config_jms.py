#/*
# * Licensed Materials - Property of IBM
# *
# * PID 5725-H26
# *
# * Copyright IBM Corporation 2018,2021. All rights reserved.
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
  print " INFO - Connecting to WebLogic ..."
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

def create_JMSSystemResource(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type JMSSystemResource @ " + path + " ... "
    theBean = cmo.lookupJMSSystemResource(beanName)
    if theBean == None:
      cmo.createJMSSystemResource(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_Topic(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type Topic @ " + path + " ... "
    theBean = cmo.lookupTopic(beanName)
    if theBean == None:
      cmo.createTopic(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_Quota(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type Quota @ " + path + " ... "  
    theBean = cmo.lookupQuota(beanName)
    if theBean == None:
      cmo.createQuota(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_ConnectionFactory(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type ConnectionFactory @ " + path + " ... "  
    theBean = cmo.lookupConnectionFactory(beanName)
    if theBean == None:
      cmo.createConnectionFactory(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_Queue(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type Queue @ " + path + " ... "  
    theBean = cmo.lookupQueue(beanName)
    if theBean == None:
      cmo.createQueue(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_SubDeployment(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type SubDeployment @ " + path + " ... "  
    theBean = cmo.lookupSubDeployment(beanName)
    if theBean == None:
      cmo.createSubDeployment(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_JMSServer(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type JMSServer @ " + path + " ... "  
    theBean = cmo.lookupJMSServer(beanName)
    if theBean == None:
      cmo.createJMSServer(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def create_FileStore(path, beanName):
  cd(path)
  try:
    print "INFO - Creating mbean " + beanName + " of type FileStore @ " + path + " ... "  
    theBean = cmo.lookupFileStore(beanName)
    if theBean == None:
      cmo.createFileStore(beanName)
  except java.lang.UnsupportedOperationException, usoe:
    print "ERROR - java.lang.UnsupportedOperationException"
  except weblogic.descriptor.BeanAlreadyExistsException,bae:
    print "ERROR - weblogic.descriptor.BeanAlreadyExistsException"
  except java.lang.reflect.UndeclaredThrowableException,udt:
    print "ERROR - java.lang.reflect.UndeclaredThrowableException"

def setAttributesFor_JMSSystemResource(path, target):
  cd(path) 
  print "INFO - Setting target for JMS System Module " + JMSModuleName + " to " + target + "."
  refBean0 = getMBean("/Servers/" + target)
  cmo.addTarget(refBean0)

def setAttributesFor_JMS_WithSubDeployment(type, name):
  cd(JMSResourceConfigPath + "/" + type + "/" + name) 
  print "INFO - Setting attributes for mbean " + name + " of type " +  type + "with subdeployment to " + JMSServer + "."
  set("JNDIName", "jms/" + name )
  set("Name", name)
  set("SubDeploymentName", JMSServer)

def setAttributesFor_JMS(type, name):
  cd(JMSResourceConfigPath + "/" + type + "/" + name) 
  print "INFO - Setting attributes for mbean " + name + " of type " +  type + "."
  set("JNDIName", "jms/" + name)
  set("Name", name)  

def setAttributesFor_Quota(name):
  type='Quotas'
  cd(JMSResourceConfigPath + "/" + type + "/" + name + ".Quota") 
  print "INFO - Setting attributes for mbean " + name + " of type " +  type + "."
  set("Shared", "false")
  set("Name", name + ".Quota")

def setSubDeploymentServerName(name):
  cd(JMSModuleConfigPath + "/SubDeployments/" + name)
  print "INFO - Setting target server " + curam_server_name + " for mbean " + name + "."
  refBean0 = getMBean("/Servers/" + curam_server_name)
  theValue = jarray.array([refBean0], Class.forName("weblogic.management.configuration.TargetMBean"))
  cmo.setTargets(theValue)

def setSubDeploymentJMSServersName():
  cd(JMSModuleConfigPath + "/SubDeployments/" + JMSServer)
  print "INFO - Setting target server " + JMSServer + " for JMSServers mbean."
  refBean0 = getMBean("/JMSServers/" + JMSServer)
  theValue = jarray.array([refBean0], Class.forName("weblogic.management.configuration.TargetMBean"))
  cmo.setTargets(theValue)
  
def setJMSServerAttribute_FileStores():
  cd("/JMSServers/" + JMSServer)
  print "INFO - Setting FileStores attribute for mbean " + JMSServer + " of type JMSServer."
  refBean0 = getMBean("/Servers/" + curam_server_name)
  theValue = jarray.array([refBean0], Class.forName("weblogic.management.configuration.TargetMBean"))
  cmo.setTargets(theValue)

  bean = getMBean("/FileStores/" + JMSFileStore)
  cmo.setPersistentStore(bean)  

def setDeliveryFailureLimit(name, type):
  cd(JMSResourceConfigPath + "/" + type + "/" + name + "/DeliveryFailureParams/" + name)
  print "INFO - Setting RedeliveryLimit for mbean " + name + " of type " + type + "."
  set("RedeliveryLimit", "1")

def setDeliveryMode(name, type):
  cd(JMSResourceConfigPath + "/" + type + "/" + name + "/DeliveryParamsOverrides/" + name)
  print "INFO - Setting DeliveryMode attribute for mbean " + name + " of type " + type + "."
  set("DeliveryMode", "Persistent")


def setFileStoresAttributes():
  cd("/FileStores/" + JMSFileStore)
  print "INFO - Setting FileStores attributes for mbean " + JMSFileStore + "."
  set("SynchronousWritePolicy", "Direct-Write")
  set("Directory", domainDir)
  refBean0 = getMBean("/Servers/"  + curam_server_name)
  theValue = jarray.array([refBean0], Class.forName("weblogic.management.configuration.TargetMBean"))
  cmo.setTargets(theValue)

def setConnectionFactoriesXAEnabled(name):
  cd(JMSResourceConfigPath + "/ConnectionFactories/" + name + "/TransactionParams/" + name)
  print "INFO - Setting XAConnectionFactoryEnabled for mbean " + name + " of type TransactionParams."
  set("XAConnectionFactoryEnabled", "true")

def setErrorDestination(name, type, destination):
  cd(JMSResourceConfigPath + "/" + type + "/" + name + "/DeliveryFailureParams/" + name)
  print "INFO - Setting Error Destination for mbean " + name + " of type " + type + " to " + destination + "."
  set("ErrorDestination", getMBean(JMSResourceConfigPath + "/Queues/" + destination))

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

  domainDir=sys.argv[7]
  print "INFO - domainDir=" + domainDir

  JMSFileStore=sys.argv[8]
  print "INFO - JMSFileStore=" + JMSFileStore

  JMSServer=sys.argv[9]
  print "INFO - JMSServer=" + JMSServer
  
  JMSModuleName=sys.argv[10]
  print "INFO - JMSModuleName=" + JMSModuleName

  WLSTVerboseMode=sys.argv[11]
  print "INFO - WLSTVerboseMode=" + WLSTVerboseMode

  print " "

  #Create mBean path variables for configuration
  JMSModuleConfigPath="/JMSSystemResources/" + JMSModuleName
  JMSResourceConfigPath="/JMSSystemResources/" + JMSModuleName + "/JMSResource/" + JMSModuleName

  if (WLSTVerboseMode != "true"):
    # These are APIs that WebLogic generates via configToScript that are not documented and do not have any apparent functionality:
    hideDisplay()
    hideDumpStack("true")
  #endIf

  connectToServer()

  startTransaction()

  create_JMSSystemResource("/", JMSModuleName)
  setAttributesFor_JMSSystemResource(JMSModuleConfigPath, curam_server_name)
  create_Topic(JMSResourceConfigPath, "CuramCacheInvalidationTopic")
  create_Quota(JMSResourceConfigPath, "CuramDeadMessageQueue.Quota")
  create_Quota(JMSResourceConfigPath, "DPError.Quota")
  create_Quota(JMSResourceConfigPath, "DPEnactment.Quota")
  create_Quota(JMSResourceConfigPath, "WorkflowError.Quota")
  create_Quota(JMSResourceConfigPath, "WorkflowEnactment.Quota")
  create_Quota(JMSResourceConfigPath, "WorkflowActivity.Quota")
  create_Quota(JMSResourceConfigPath, "CuramCacheInvalidationTopic.Quota")
  create_ConnectionFactory(JMSResourceConfigPath, "CuramQueueConnectionFactory")
  create_ConnectionFactory(JMSResourceConfigPath, "CuramTopicConnectionFactory")
  create_Queue(JMSResourceConfigPath, "CuramDeadMessageQueue")
  create_Queue(JMSResourceConfigPath, "DPError")
  create_Queue(JMSResourceConfigPath, "DPEnactment")
  create_Queue(JMSResourceConfigPath, "WorkflowError")
  create_Queue(JMSResourceConfigPath, "WorkflowEnactment")
  create_Queue(JMSResourceConfigPath, "WorkflowActivity")
  create_SubDeployment(JMSModuleConfigPath, "CuramQueueConnectionFactory")
  create_SubDeployment(JMSModuleConfigPath, "CuramTopicConnectionFactory")
  create_SubDeployment(JMSModuleConfigPath, JMSServer)
  create_JMSServer("/", JMSServer)
  create_FileStore("/", JMSFileStore)

  setAttributesFor_JMS_WithSubDeployment("Topics", "CuramCacheInvalidationTopic")

  setAttributesFor_Quota("CuramDeadMessageQueue")

  setAttributesFor_Quota("DPError")

  setAttributesFor_Quota("DPEnactment")

  setAttributesFor_Quota("WorkflowError")

  setAttributesFor_Quota("WorkflowEnactment")

  setAttributesFor_Quota("WorkflowActivity")

  setAttributesFor_Quota("CuramCacheInvalidationTopic")

  setAttributesFor_JMS("ConnectionFactories", "CuramQueueConnectionFactory")

  setAttributesFor_JMS("ConnectionFactories", "CuramTopicConnectionFactory")

  setAttributesFor_JMS_WithSubDeployment("Queues", "CuramDeadMessageQueue")

  setAttributesFor_JMS_WithSubDeployment("Queues", "DPError")

  setAttributesFor_JMS_WithSubDeployment("Queues", "DPEnactment")

  setAttributesFor_JMS_WithSubDeployment("Queues", "WorkflowError")

  setAttributesFor_JMS_WithSubDeployment("Queues", "WorkflowEnactment")

  setAttributesFor_JMS_WithSubDeployment("Queues", "WorkflowActivity")

  setSubDeploymentServerName("CuramQueueConnectionFactory")

  setSubDeploymentServerName("CuramTopicConnectionFactory")

  setSubDeploymentJMSServersName()
  
  setJMSServerAttribute_FileStores()
  
  setFileStoresAttributes()
  
  setDeliveryFailureLimit("DPEnactment", "Queues")
  
  setDeliveryFailureLimit("WorkflowEnactment", "Queues")
  
  setDeliveryMode("DPEnactment", "Queues")

  setDeliveryFailureLimit("CuramDeadMessageQueue", "Queues")
  
  setDeliveryMode("WorkflowError", "Queues")
  
  setDeliveryFailureLimit("WorkflowActivity", "Queues")
  
  setDeliveryFailureLimit("CuramCacheInvalidationTopic", "Topics")
  
  setDeliveryMode("CuramDeadMessageQueue", "Queues")
  
  setConnectionFactoriesXAEnabled("CuramQueueConnectionFactory")
  setConnectionFactoriesXAEnabled("CuramTopicConnectionFactory")
  
  setDeliveryFailureLimit("DPError", "Queues")
  
  setDeliveryMode("DPError", "Queues")
  
  setDeliveryMode("WorkflowEnactment", "Queues")
  
  setDeliveryMode("WorkflowActivity", "Queues")
  
  setDeliveryFailureLimit("WorkflowError", "Queues")

  setErrorDestination("DPEnactment", "Queues" , "DPError")
  setErrorDestination("WorkflowEnactment", "Queues" , "WorkflowError")
  setErrorDestination("WorkflowActivity", "Queues" , "WorkflowError")
  setErrorDestination("DPError", "Queues", "CuramDeadMessageQueue")
  setErrorDestination("WorkflowError", "Queues", "CuramDeadMessageQueue")

  endTransaction()
  print "INFO - End of configuration script."

finally:
  print "INFO - Exiting configuration script."
  
# <end>

