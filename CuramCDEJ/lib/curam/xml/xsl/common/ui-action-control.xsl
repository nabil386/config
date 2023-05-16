<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright © 2003 Curam Software Ltd.
  All rights reserved.
 
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <!--
    NOTE: While these templates do not have a "match" attribute,
    there will still be a context node if the calling template
    had a context node. This context node is automatically passed
    through to templates called from this template.
  -->

  <!--
    Generate an action control. Override this to generate the
    control for the given context.

    $context  The context of the control: 'action-set'
  -->
  <xsl:template name="gen-action-control">
    <xsl:param name="context" />

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-action-control</xsl:text>
    </xsl:message>

  </xsl:template>

</xsl:stylesheet>