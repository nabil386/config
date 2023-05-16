#/*
# * Licensed Materials - Property of IBM
# *
# * PID 5725-H26
# *
# * Copyright IBM Corporation 2012,2013. All rights reserved.
# *
# * US Government Users Restricted Rights - Use, duplication or disclosure
# * restricted by GSA ADP Schedule Contract with IBM Corp.
# */


import sys
def createPort ( nodeName, serverName, port, templateName, name, hostname, keepAliveBooleanVar, keepAliveTimeout ):

        global AdminConfig
        global AdminTask

        nodeId = AdminConfig.getid("/Node:"+nodeName+"/" )        
        server = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        wc = AdminConfig.list("WebContainer", server )
        chainName = name+"Chain"
        endPointName = name+"EndPoint"
        chainList = AdminConfig.list("Chain" ).splitlines()        
        for chain in chainList:                
                name = AdminConfig.showAttribute(chain, "name" )                
                if (cmp(name, chainName) == 0):
                        print "Transport Chain "+chainName+" already exists - removing"
                        AdminTask.deleteChain(chain, ["-deleteChannels"] )
                #endIf 
        #endFor 
        chainTemplateList = AdminConfig.listTemplates("Chain", templateName ).splitlines()[0]
        if (len(chainTemplateList) == 0):
                print "Could not find a Chain template using "+templateName
                return 1
        #endIf 

        transportchannelservice = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/TransportChannelService:/" ) 
        serverEntries = AdminConfig.list("ServerEntry", nodeId ).splitlines() 
        for serverEntry in serverEntries: 
                sName = AdminConfig.showAttribute(serverEntry, "serverName" )
                if (sName == serverName):               
                         specialEndPoints = AdminConfig.showAttribute(serverEntry, "specialEndpoints" ).split() 
                         for specialEndPoint in specialEndPoints:
                             specialEndPoint = specialEndPoint.replace('[','')
                             specialEndPoint = specialEndPoint.replace(']','')
                             endPointNm = AdminConfig.showAttribute(specialEndPoint, "endPointName" )
                             if (endPointNm == endPointName):
                                  print "EndPoint "+endPointName+" already exists - removing"
                                  AdminConfig.remove(specialEndPoint )
                             #endIf 
                          #endFor 
                #endIf
        #endFor 
        print "Create the EndPoint "+endPointName
        params = ["-name", endPointName, "-host", "*", "-port", port]
        endPoint = AdminTask.createTCPEndPoint(transportchannelservice, params )
        print "Create the Chain "+chainName
        params = ["-name", chainName, "-template", chainTemplateList, "-endPoint", endPoint]
        # The createChain fails if this script is run against an already configured environment
        chain = AdminTask.createChain(transportchannelservice, params )
        print "Setting the MaxKeepAliveConnections on the Chain "+chainName+" with KeepAlive enabled set to "+keepAliveBooleanVar+" and Timeout set to "+keepAliveTimeout
        HTTPInboundChannelList = AdminConfig.list("HTTPInboundChannel", transportchannelservice ).splitlines()        
        for HTTPInboundChannel in HTTPInboundChannelList:                
                keepAliveEnabled_attr = ["keepAlive", keepAliveBooleanVar]
                keepAliveTimeout_attr = ["persistentTimeout", keepAliveTimeout]
                attrs = [keepAliveEnabled_attr, keepAliveTimeout_attr]
                AdminConfig.modify(HTTPInboundChannel, attrs )
        #endFor 

        hostAliasList = AdminConfig.list("HostAlias" ).splitlines()        
        for hostAlias in hostAliasList:                
                existingport = AdminConfig.showAttribute(hostAlias, "port" )
                if (cmp(port, existingport) == 0):
                        print "HostAlias port "+port+" already exists - removing"
                        AdminConfig.remove(hostAlias )
                #endIf 
        #endFor 

        hostList = AdminConfig.list("VirtualHost" ).splitlines()        
        for item in hostList:                
                existinghostname = AdminConfig.showAttribute(item, "name" )
                if (cmp(hostname, existinghostname) == 0):
                        print "VirtualHost "+hostname+" already exists - removing"
                        AdminConfig.remove(item )
                #endIf 
        #endFor 

        cell = AdminConfig.getid("/Cell:/" )
        virtualHostTemplate = AdminConfig.listTemplates("VirtualHost", "default_host" )
        hostname_attr = ["name", hostname]
        attrs = [hostname_attr]
        host = AdminConfig.createUsingTemplate("VirtualHost", cell, attrs, virtualHostTemplate )

        host_attr = ["hostname", "*" ]
        port_attr = ["port", port]
        attrs = [host_attr, port_attr]
        print "Add a new HostAlias to the list - "+port+"."
        AdminConfig.create("HostAlias", host, attrs )       
#endDef 

#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam Websphere Port Configuration Script"
print ""

nodeName = sys.argv[0]
clientName = sys.argv[1]
clientPort = sys.argv[2]
webservicesName = sys.argv[3]
webservicesPort = sys.argv[4]
keepAliveBoolean = sys.argv[5]
keepAliveTimeout = sys.argv[6]

print "Curam Websphere Web Container Port Configuration Script - keepAliveBoolean set to "+keepAliveBoolean+" keepAliveTimeout set to keepAliveBoolean set to "+keepAliveTimeout

createPort(nodeName, clientName, clientPort, "WebContainer-Secure", "CuramClient", "client_host", keepAliveBoolean, keepAliveTimeout )
print ""
createPort(nodeName, webservicesName, webservicesPort, "WebContainer", "CuramWebServices", "webservices_host", keepAliveBoolean, keepAliveTimeout )

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "Port Configuration Script executed successfully."
print ""
