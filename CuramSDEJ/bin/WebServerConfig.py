#/*
# * Licensed Materials - Property of IBM
# *
# *
# * Copyright IBM Corporation 2017. All rights reserved.
# *
# * US Government Users Restricted Rights - Use, duplication or disclosure
# * restricted by GSA ADP Schedule Contract with IBM Corp.
# */

###############################################################################
#  WebServerConfig.py
#   
#  Routines for mapping the modules to web server and propogating the key ring.
#
###############################################################################
import sys;


# Maps modules of EAR to appserver and web servers. Method expects
# the cell name, node name, server name and application name to be
# passed in. It map the modules to default server and iterates through
# the configured web servers and maps the modules to each of web server
def mapModulesToServers():

  defCellName = sys.argv[1]
  defNodeName = sys.argv[2]
  defServerName = sys.argv[3]
  applicationName = sys.argv[4]
     
  websrvnodes = getNodes(defCellName);
  
  # Web servers map
  webserversMap = ""
  
  #Iterating through all the nodes in the cell
  for websrvnode in websrvnodes:
  
    webSrvNodeName = AdminConfig.showAttribute(websrvnode, "name")
    
    # Getting the list of all web servers in the node to be mapped
    webservers = AdminTask.listServers('[-serverType WEB_SERVER -nodeName ' +webSrvNodeName+']').split()
    
    for webserver in webservers:
      websrvName =  AdminConfig.showAttribute(webserver, "name")
      
      # Building the web servers map for modules
      webserversMap += '+WebSphere:cell='+defCellName+',node='+webSrvNodeName+',server='+websrvName
  
  # Default server map
  defaultServerMap = 'WebSphere:cell='+defCellName+',node='+defNodeName+',server='+defServerName
  
  # Applying the mappings to all the modules
  AdminApp.edit(applicationName, ['-MapModulesToServers', [['.*', '.*', defaultServerMap+webserversMap]]])
  save()
  
#endDef

def getNodes(cellName):
  cell = AdminConfig.getid('/Cell:'+cellName)
  nodeLst = AdminConfig.list('Node', cell).split() 
  return nodeLst

  
# Adds web server virtual hosts to client_hosts
# The hosts are comma delimited values set to the 
# property 'curam.webserver.virtualhosts' in 
# AppServer.properties and passed into this script.
# If the property is not set the default value 80 and 443
# are passed in. 
def addWebServerVirtualHosts(): 
  defCellName = sys.argv[1]
  webHosts = sys.argv[6]
  hostsList = webHosts.split(',')  
  for host in hostsList:
    addHosts(defCellName, host)
#endDef    
  
  
def addHosts(defCellName,clientPort):  

  portFound = "false"
  client_host = AdminConfig.getid( '/Cell:' + defCellName + '/VirtualHost:client_host/')
  virtualHostAliasList = AdminConfig.list('HostAlias', client_host).splitlines()
   
  for virtualHostAlias in virtualHostAliasList:
    virtualHostAliasPort = AdminConfig.showAttribute(virtualHostAlias, 'port')
    if (int(virtualHostAliasPort) == int(clientPort)):
      print 'Client host alias with port ' + clientPort + ' already exist.'
      portFound = "true"
      break
   
  if (portFound == "false"):   
    host_attr = ["hostname", "*" ]
    port_attr = ["port", clientPort]
    attrs = [host_attr, port_attr]
    AdminConfig.create("HostAlias", client_host, attrs)
    save()
    print 'Added client host alias with port ' + clientPort + ' successfully.'
 
 #endDef
  

# Propogates the plug-in keyring for the Web server. 
def propogateKeyRing():

  defCellName = sys.argv[1]
  defNodeName = sys.argv[2]
  defServerName = sys.argv[3]
  defWAShome = sys.argv[4]
  defProfileName = sys.argv[5]
    
  profilePath = defWAShome + '/profiles/' + defProfileName + '/config'
  
  webserverObj = AdminControl.completeObjectName('type=WebServer,*')
  pluginCfgObj = AdminControl.completeObjectName('type=PluginCfgGenerator,*')
  websrvnodes = getNodes(defCellName);
  
  #Iterating through all the nodes in the cell
  for websrvnode in websrvnodes:
  
    webSrvNodeName = AdminConfig.showAttribute(websrvnode, "name")
    
    # Getting the list of all web servers 
    webservers = AdminTask.listServers('[-serverType WEB_SERVER -nodeName ' +webSrvNodeName+']').split()

    for webserver in webservers:
     
      websrvName =  AdminConfig.showAttribute(webserver, "name")
      
      # checking if the web server is running
      wsStatus = AdminControl.invoke(webserverObj, 'ping', defCellName+' ' ' '+webSrvNodeName+'' ' '+websrvName+']')
  
      print "Attempting to propogate the plug-in keyring for the web server " + websrvName + " in the cell "+defCellName+" and the node "+ webSrvNodeName + "..."
      print "Web server " + websrvName + " in the cell "+defCellName+" and the node "+ webSrvNodeName + " is "+ wsStatus
      AdminControl.invoke(pluginCfgObj, 'propagateKeyring', profilePath + ' ' + defCellName + ' ' + webSrvNodeName + ' ' + websrvName)
      save()
      print "Successfully propogated the plug-in keyring for the web server " + websrvName

  addWebServerVirtualHosts()
#endDef


# Saves the changes to the server configuration.
def save():
 AdminConfig.save()

#####################################################################################################
#
#  Main procedure
#
#####################################################################################################

# Reading the function name to call from the first argument.
definitionName = sys.argv[0]

webservers = AdminConfig.list('WebServer').split()
  
if len(webservers) > 0:
  locals()[definitionName]()
else :
  print "No web servers exist."
 
#<end>
