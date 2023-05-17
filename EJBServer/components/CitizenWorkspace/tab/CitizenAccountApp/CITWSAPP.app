<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2016. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!--
  Copyright 2010 Curam Software Ltd.
  All rights reserved.
  
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config" id="CITWSAPP" mode="external">
  <ac:landing-page title="landing.title" icon="landing.logo" page-id="CitizenWorkspace_landingPage"/>

  <ac:navigation id="StandardUser" width="navbar.width"/>
  
  <ac:navigation id="CitizenAccount" width="navbar.width"/>
  
  <ac:banner-menu type="mega" title="mega.title">
      
    <ac:menu-item id="screening" title="menu.screening.title" text="menu.screening.text" icon="menu.screening.icon" page-id="PagePlayerResolveWrapper">
      <ac:param name="page" value="SetupScreening" description=""/>
    </ac:menu-item>  
    
    <!-- BEGIN, CR00464534, MV -->
    
    <ac:menu-item id="intake" title="menu.intake.title" text="menu.intake.text" icon="menu.intake.icon" page-id="CitizenWorkspace_apply"/>

    <ac:menu-item id="triage" title="menu.triage.title" text="menu.triage.text" icon="menu.triage.icon" page-id="PagePlayerResolveWrapper">
      <ac:param name="page" value="SetupTriage" description=""/>
    </ac:menu-item>
    <!-- END, CR00464534 -->

  </ac:banner-menu>
  
  <ac:banner-menu type="print" title="print.title"/>
  
  <ac:banner-menu type="help" title="menu.help.title">
  
    <ac:menu-item id="contactUs" title="menu.contactus.title" text="menu.contactus.text" icon="menu.contactus.icon" page-id="CitizenWorkspace_ContactUs"/>
    
    <ac:menu-item id="faq" title="menu.faq.title" text="menu.faq.text" icon="menu.faq.icon" page-id="CitizenWorkspace_FAQ"/>
    
  </ac:banner-menu>

  <ac:banner-menu type="person" title="person.title" page-id="CitizenWorkspace_userHomeResolver">      
    
    <ac:menu-item id="p1" title="menu.resetpassword.title" text="menu.resetpassword.text" page-id="PagePlayerWrapper">
      <ac:param name="page" value="ResetPasswordExistingPassword" description=""/>
    </ac:menu-item>
  <!-- BEGIN, CR00467393, PC -->      
  </ac:banner-menu>
  <ac:banner-menu type="logout" title="logout.title" />
  <!-- END, CR00467393 -->
</ac:application>