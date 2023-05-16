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

errorCode = ""
errorInfo = ""

######################################################################################################
#
#  loadProperties
# This is the procedure to load a property file.  Java's Properties class is used to load the
# property file name passed in as input paramenter
#
######################################################################################################

def loadProperties ( propFileName ):
        from java.io import FileInputStream
        from java.util import Properties

        fileprop = Properties(  ) 
        fileStream = FileInputStream( propFileName )
        fileprop.load(fileStream)

        return fileprop

#endDef 

######################################################################################################
#
# getSecurityAdminMbean
# Procedure to get securityAdmin Mbean.
#
######################################################################################################

def getSecurityAdminMbean (  ):
        global AdminControl

        try:
                _excp_ = 0
                result = AdminControl.queryNames("WebSphere:type=SecurityAdmin,*" )
                _excp_ = 0 #reset (in case of nested exceptions)
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                result = str(_value_)
                _excp_ = 1
        #endTry 
        if (_excp_ ):
                print "$AdminControl queryNames WebSphere:type=SecurityAdmin,* caught an exception "+result+"\n"
                return 
        else:
                if (result != []):
                        # incase more than one, just get the first one
                        secMbean = result.splitlines()
                        secMbean = secMbean[0]
                        return secMbean
                else:
                        print "Security Mbean was not found\n"
                        return 
                #endElse 
        #endElse 
#endDef 

def generateLTPAKeys ( ltpaPasw ):
        global AdminControl

        secMbean = getSecurityAdminMbean( )
        if (secMbean != [] or secMbean != "null"):
                try:
                        _excp_ = 0
                        result = AdminControl.invoke(secMbean, "generateKeys", ltpaPasw )
                        _excp_ = 0 #reset (in case of nested exceptions)
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        result = str(_value_)
                        _excp_ = 1
                #endTry 
                if (_excp_ ):
                        print "$AdminControl invoke "+secMbean+" generateKeys "+ltpaPasw+" caught an exception "+result+"\n"
                        return 
                #endIf 
        #endIf 
        else:
                print "unable to get securityAdmin Mbean, LTPA key not generated"
        #endElse 
#endDef 
