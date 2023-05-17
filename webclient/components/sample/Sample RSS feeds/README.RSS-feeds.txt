#
# Cúram-based RSS feeds sample
#

This is a sample code to demonstrate how RSS feeds in Cúram can be implemented.

Two sample feeds are provided - one for the list of tasks assigned to a user and second for a list of cases for a specified participant.

There are limitations in the way the RSS readers (for example Outlook and Google Desktop) access the feeds. These readers do not provide any support for user authentication or secured connection using HTTPS. In order to provide feeds that will work in the mentioned RSS readers the code providing the feed data has to authenticate with the Cúram server internally, without prompting the user for login credentials. The feeds also have to be served using plain unsecured HTTP protocol.

The following sections contain instructions how to configure and use the provided sample RSS feeds.



Build-time activities
These are optional changes which, if required, must be done before building the client application.

The two sample RSS feeds are implemented as HTTP servlets and the corresponding Java classes are placed in the <webclient>/JavaSource/curam/sample/rss/ directory.

You may want to change the default user name and password (which is caseworker/password) the feeds will use to authenticate with the Cúram server.
This is done in the following Java file: <webclient>/JavaSource/curam/sample/rss/AbstractRssFeedServlet.java
There are stUserName and stPassword constants at the top of the file that can be changed to suit your demo needs.

After setting the user name and password the client application can be built and the classes for the two feed servlets will be automatically placed in the classpath of the web application.



Deployment/runtime activities
Please note the following configuration changes to web.xml file can also be performed before building the EAR file for your application in which case you do not need to make the changes on the deployed server later on.

Following is the build-time and deployment/runtime configuration required to make the sample feeds work on the Websphere application server. The application server must be restarted for these changes to take effect.

Your web application must be configured to make the feeds available and serve them under a specific URL. The following XML needs to be added to your web application's web.xml, directly under the <web-app> element:
  <servlet>
    <servlet-name>TaskListRssFeedServlet</servlet-name>
    <servlet-class>curam.sample.rss.TaskListRssFeedServlet</servlet-class>    
  </servlet>
  <servlet>
    <servlet-name>CaseListRssFeedServlet</servlet-name>
    <servlet-class>curam.sample.rss.CaseListRssFeedServlet</servlet-class>    
  </servlet>
  <servlet-mapping>
    <servlet-name>TaskListRssFeedServlet</servlet-name>
    <url-pattern>/servlet-unauth/RSS-TaskList</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>CaseListRssFeedServlet</servlet-name>
    <url-pattern>/servlet-unauth/RSS-CaseList</url-pattern>
  </servlet-mapping>

You will also need to configure the server to accept non-secured HTTP connections so that the RSS readers can use HTTP to read the feed data.
This is done in two steps: a) change web.xml b) configure Websphere using the admin console.
a) Change the contents of the web-app/security-constraint/user-data-constraint/transport-guarantee element in the web.xml file from CONFIDENTIAL to NONE. As in the previous step, this can be done either before building he *.ear file or in a deployed application on the Websphere server.
b) Login to the Websphere admin console, go to Environment->Virtual Hosts->client_host->Host Aliases section, click New and enter * for host name and 9082 for port number, then click Ok. On the next page click Save to store your new value to the server configuration. Please note that the port 9082 corresponds to the CuramWebServicesChain configured in the default Cúram client application.

The sample feeds are ready to be used after restarting the server.



Usage

If you have used the configuration suggested above URLs for the feeds can be accessed at the following URLs.
http://localhost:9082/Curam/servlet-unauth/RSS-TaskList
http://localhost:9082/Curam/servlet-unauth/RSS-CaseList?concernRoleId=XX (where XX is the concern role ID of the case participant you want to view cases for)

To obtain the concernRoleId, search for a pereson in the application, open their home page and copy the value of the concernRoleID parameter in the URL of the page. Make sure you copy the full value, including the minus sign if it is present.

You can view the feeds by adding the above URLs in the RSS reader. Live data from the Cúram application are displayed so if you for example create a new case it will appear as a new item in the feed in your RSS reader.

Using Outlook to view RSS feeds
In Outlook 2007 there is an "RSS Feeds" folder in the main mailbox. New feeds can be added to it by right-clicking the folder and choosing the "Add a New RSS Feed..." menu item. Put the URL of your feed in the text box provided in the "New RSS Feed" dialog and click "Add". You will be prompted once more if you really want to add your feed and you are done. The feed appears as a subfolder in the "RSS Feeds" folder and the feed items appear as individual messages.



Future directions

Please note this is merely a sample implementation written on top of the standard OOTB Cúram application. The full infrastructure support will have to include:
  * Automatic generation of information feeds based on some form of metadata
  * Full security support
  * Streamlined deployment and configuration of the feeds
  * ...and more


