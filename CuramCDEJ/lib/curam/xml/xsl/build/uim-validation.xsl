<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<axsl:stylesheet xmlns:axsl="http://www.w3.org/1999/XSL/Transform" xmlns:sch="http://www.ascc.net/xml/schematron" version="1.0">
<axsl:include href="../common/string-utils.xsl"/>
<axsl:output method="text"/>
<axsl:variable select="'\:()|'" name="chars-to-escape"/>
<axsl:template mode="schematron-get-full-path" match="*|@*">
<axsl:apply-templates mode="schematron-get-full-path" select="parent::*"/>
<axsl:text>/</axsl:text>
<axsl:if test="count(. | ../@*) = count(../@*)">@</axsl:if>
<axsl:value-of select="name()"/>
<axsl:text>[</axsl:text>
<axsl:value-of select="1+count(preceding-sibling::*[name()=name(current())])"/>
<axsl:text>]</axsl:text>
</axsl:template>
<axsl:key use="@PAGE_ID" name="page-ref-by-page-id" match="PAGE_REF"/>
<axsl:key use="@GROUP_ID" name="page-group-by-group-id" match="PAGE_GROUP"/>
<axsl:key use="concat(../@PAGE_ID, '.', @NAME)" name="si-by-name" match="SERVER_INTERFACE"/>
<axsl:key use="concat(../@PAGE_ID, '.', @NAME)" name="action-si-by-name" match="SERVER_INTERFACE[@PHASE = 'ACTION']"/>
<axsl:key use="concat(../@PAGE_ID, '.', @NAME)" name="page-param-by-name" match="PAGE_PARAMETER"/>
<axsl:template match="/">
<axsl:apply-templates mode="M5" select="/"/>
<axsl:apply-templates mode="M6" select="/"/>
<axsl:apply-templates mode="M7" select="/"/>
</axsl:template>
<axsl:template mode="M5" priority="4000" match="ACTION_CONTROL">
<axsl:variable select="@TYPE = 'CLIPBOARD'" name="is-clipboard"/>
<axsl:variable select="@TYPE = 'SUBMIT'" name="is-submit"/>
<axsl:variable select="@ACTION_ID" name="act-id-value"/>
<axsl:if test="not($is-submit) and @DEFAULT">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_DEFAULT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not($is-submit) and LINK[@PAGE_ID = 'THIS']">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_LINK</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not($is-clipboard)                     and not(count(*) = count(LINK | SCRIPT | CONDITION))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not($is-clipboard) and count(LINK) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$is-clipboard and not(@LABEL) and not(@IMAGE)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_CLIP_MISSING_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$is-clipboard                     and (count(CONNECT) != 1                          or count(CONNECT/TARGET) != 0                          or count(CONNECT/INITIAL) != 0)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_CLIP_MULTIPLE_SOURCES</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$is-submit                     and ancestor::PAGE/descendant::WIDGET[@TYPE = 'FILE_EDIT']                     and LINK/CONNECT/SOURCE/@NAME = ancestor::PAGE/SERVER_INTERFACE[@PHASE = 'ACTION']/@NAME">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_FE_SOURCE_ACTION</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="(@TYPE = 'DISMISS') and not(ancestor::PAGE[@POPUP_PAGE = 'true'])">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_TYPE_DISMISS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@ACTION_ID and not($is-submit)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_WRONG_TYPE_FOR_ACTID</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@ACTION_ID         and not(count(ancestor::PAGE/descendant::ACTION_CONTROL[@ACTION_ID = $act-id-value]) = 1)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_ACTID_NOT_UNIQUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@ACTION_ID) and $is-submit           and ancestor::PAGE/descendant::ACTION_CONTROL[@TYPE='SUBMIT']/@ACTION_ID">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_NOT_ALL_IDS_SPECIFIED</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@ACTION_ID          and count(ancestor::PAGE/descendant::SERVER_INTERFACE/@ACTION_ID_PROPERTY) = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_SI_ACTID_PROPS_NOT_SPECIFIED</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3999" match="CLUSTER">
<axsl:if test="count(*) = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(ACTION_SET) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CLUSTER_INVALID_ACTSET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(ACTION_SET) = 0                     and count(FIELD) = 0                     and count(WIDGET) = 0                     and count(CONTAINER) = 0                     and count(CLUSTER) = 0                     and count(LIST) = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CLUSTER_INVALID_ELEMENTS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3998" match="CONNECT">
<axsl:variable select="count(SOURCE)" name="num-sources"/>
<axsl:variable select="count(TARGET)" name="num-targets"/>
<axsl:variable select="count(INITIAL)" name="num-initials"/>
<axsl:variable select="$num-sources + $num-targets + $num-initials" name="num-endpoints"/>
<axsl:if test="$num-endpoints = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_MISSING_ENDPOINT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(*) != $num-endpoints">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_INVALID_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$num-sources &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_MULTIPLE_SOURCES</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$num-targets &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_MULTIPLE_TARGETS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$num-initials &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_MULTIPLE_INITIALS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$num-endpoints &gt; 1 and parent::FIELD">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_FLD_ONE_EP</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3997" match="FIELD">
<axsl:variable select="count(CONNECT)" name="num-connects"/>
<axsl:if test="count(LINK) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_FIELD_MULTIPLE_LINKS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$num-connects &gt; 3">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_INVALID_NUM_FIELDS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(CONNECT[SOURCE]) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_FIELD_MULTIPLE_SOURCES</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(CONNECT[TARGET]) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_FIELD_MULTIPLE_TARGETS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(CONNECT[INITIAL]) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_FIELD_MULTIPLE_INITIALS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$num-connects = 0 and not(@CONTROL = 'SKIP')">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_INVALID_NUM_FIELDS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$num-connects &gt; 0                     and LINK[@HOME_PAGE = 'true']                     and not(CONNECT/SOURCE[@NAME = 'TEXT'])">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_FIELD_HOME_INVALID_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@HEIGHT and parent::LIST and CONNECT/TARGET">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LIST_INVALID_EDITABLE_FIELD_HEIGHT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3996" match="CONTAINER">
<axsl:if test="count(*) = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3995" match="LINK">
<axsl:variable select="count(CONNECT)" name="num-connects"/>
<axsl:variable select="boolean(@URI)" name="has-uri"/>
<axsl:variable select="boolean(@URL)" name="has-url"/>
<axsl:variable select="boolean(@URI_REF)" name="has-uri-ref"/>
<axsl:variable select="boolean(@PAGE_ID)" name="has-page-id"/>
<axsl:variable select="boolean(@PAGE_ID_REF)" name="has-page-ref"/>
<axsl:variable select="boolean(@URI_SOURCE_NAME)" name="has-uri-name"/>
<axsl:variable select="boolean(@URI_SOURCE_PROPERTY)" name="has-uri-property"/>
<axsl:if test="$has-uri and ($has-url or $has-uri-ref or $has-page-id                   or $has-page-ref or $has-uri-name or $has-uri-property)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$has-url and ($has-uri-ref or $has-page-id                   or $has-page-ref or $has-uri-name or $has-uri-property)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$has-uri-ref and ($has-page-id or $has-page-ref                   or $has-uri-name or $has-uri-property)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$has-page-id and ($has-page-ref or $has-uri-name or $has-uri-property)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$has-page-ref and ($has-uri-name or $has-uri-property)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="($has-uri-name and not($has-uri-property))                     or ($has-uri-property and not($has-uri-name))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_MISSING_SOURCE_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="CONNECT/SOURCE[@NAME = 'TEXT']">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_INVALID_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@PAGE_ID = 'THIS' and parent::ACTION_CONTROL                     and $num-connects &gt; 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LINK_INVALID_CONNS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="(parent::node()[@TYPE = 'SUBMIT'] and (@URL or @URI or @URI_REF or @URI_SOURCE_NAME or @URI_SOURCE_PROPERTY))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SUBMIT_LINK_WITH_URI</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3994" match="LIST">
<axsl:if test="count(*) = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(ACTION_SET) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LIST_MULTIPLE_ACTSETS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(DETAILS_ROW) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_LIST_MULTIPLE_DETAILS_ROW</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3993" match="DETAILS_ROW">
<axsl:if test="count(*) = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(INLINE_PAGE) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_DETAILS_ROW_MULTIPLE_INLINE_PAGE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3992" match="INLINE_PAGE">
<axsl:variable select="boolean(@PAGE_ID)" name="has-page-id"/>
<axsl:variable select="boolean(@URI_SOURCE_NAME)" name="has-uri-name"/>
<axsl:variable select="boolean(@URI_SOURCE_PROPERTY)" name="has-uri-property"/>
<axsl:if test="$has-page-id and ($has-uri-name or $has-uri-property)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_INLINE_PAGE_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="($has-uri-name and not($has-uri-property))                     or ($has-uri-property and not($has-uri-name))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_INLINE_PAGE_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3991" match="MENU">
<axsl:variable select="count(*)" name="num-children"/>
<axsl:variable select="@MODE = 'DYNAMIC' or @MODE = 'INTEGRATED_CASE'" name="is-dynamic-or-ic"/>
<axsl:variable select="@MODE = 'WIZARD_PROGRESS_BAR'" name="is-wizard_progress_bar"/>
<axsl:if test="$num-children = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="(@MODE = 'STATIC' or @MODE = 'NAVIGATION'                      or @MODE = 'IN_PAGE_NAVIGATION')                     and $num-children != count(ACTION_CONTROL)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_MENU_INVALID_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="($is-dynamic-or-ic or $is-wizard_progress_bar)                      and $num-children != count(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_MENU_DYN_INVALID_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$is-dynamic-or-ic and CONNECT/SOURCE[@NAME = 'CONSTANT']">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_MENU_INVALID_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3990" match="PAGE">
<axsl:variable select="@COMPONENT_STYLE = 'DATAPROVIDER'" name="is-ajax"/>
<axsl:if test="count(*) = 0">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(descendant::ACTION_CONTROL[@TYPE = 'SUBMIT'                                                and @DEFAULT = 'true']) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_MULTIPLE_SUBMITS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="descendant::ACTION_CONTROL[@TYPE = 'SUBMIT'] and not(SERVER_INTERFACE[@PHASE='ACTION'])">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_SUBMIT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(ACTION_SET) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ACTSET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(PAGE_TITLE) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ACTSET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(SHORTCUT_TITLE) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_SHORTCUT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(INFORMATIONAL) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_MULTIPLE_INFORMATIONALS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="CONNECT/TARGET[@NAME = 'PAGE']">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_INVALID_TARGET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="count(descendant::FIELD[@INITIAL_FOCUS = 'true']) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_FIELD_DUPLICATE_FOCUS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$is-ajax and count(descendant::SERVER_INTERFACE) != 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_AJAX_SERVER_INTF</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$is-ajax and not(SERVER_INTERFACE[@PHASE = 'DISPLAY'])">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_AJAX_SERVER_INTF</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$is-ajax and (count(*) != (count(SERVER_INTERFACE)                 + count(PAGE_PARAMETER) + count(CONNECT) + count(FIELD)) )">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_AJAX_UNEXPECTED_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3989" match="PAGE_GROUP">
<axsl:if test="@PARENT_TARGET and not(parent::*) or parent::CHUNK">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_GROUP_INVALID_PARENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(key('page-group-by-group-id', @GROUP_ID)) = 1"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_DUPLICATE_GROUP_ID</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@GROUP_ID"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3988" match="PAGE_REF">
<axsl:choose>
<axsl:when test="@PAGE_ID or @HOME_PAGE"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_REF_MISSING_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="@HIGHLIGHT_PAGE_ID and @VISIBLE = 'true'">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_REF_INVALID_VISIBLE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="not(@PAGE_ID)                     or count(key('page-ref-by-page-id', @PAGE_ID)) = 1"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_DUPLICATE_PAGE_ID</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@PAGE_ID"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3987" match="PAGE_TITLE">
<axsl:if test="CONNECT/TARGET">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_TITLE_INVALID_CONN</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="CONNECT/SOURCE"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_TITLE_MISSING_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3986" match="TITLE">
<axsl:choose>
<axsl:when test="CONNECT/SOURCE"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_TITLE_MISSING_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3985" match="CONDITION">
<axsl:if test="preceding-sibling::CONDITION">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_MULTIPLE_CONDITIONS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@TYPE='DYNAMIC' and not(parent::CLUSTER)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_DYNAMIC_CONDITION_PARENT_NOT_CLUSTER</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3984" match="SCRIPT">
<axsl:variable select="boolean(parent::CONDITION)" name="parent-is-condition"/>
<axsl:variable select="boolean(parent::node()[@TYPE = 'DYNAMIC'])" name="parent-is-dynamic"/>
<axsl:variable select="boolean(@EVENT)" name="has-event"/>
<axsl:variable select="boolean(@ACTION)" name="has-action"/>
<axsl:variable select="@EXPRESSION" name="expression"/>
<axsl:variable select="boolean(@EXPRESSION)" name="has-expression"/>
<axsl:choose>
<axsl:when test="@SCRIPT_FILE or ancestor::PAGE[@SCRIPT_FILE]"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_MISSING_SCRIPT_FILE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="following-sibling::SCRIPT[@EVENT = current()/@EVENT]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SCRIPT_MULTIPLE_EVENTS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$parent-is-condition and not($parent-is-dynamic)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONDITION_SCRIPT_DYNAMIC</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$parent-is-condition and $parent-is-dynamic and $has-event">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONDITION_SCRIPT_DYNAMIC_EVENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$parent-is-condition and $parent-is-dynamic and $has-action">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONDITION_SCRIPT_DYNAMIC_ACTION</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$parent-is-condition and $parent-is-dynamic and string-length(@EXPRESSION) &lt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONDITION_SCRIPT_EMPTY_EXPRESSION</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="$has-expression and count(ancestor::CLUSTER/descendant::SCRIPT[@EXPRESSION = $expression]) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_EXPRESSION_NOT_UNIQUE_IN_CLUSTER</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3983" match="SERVER_INTERFACE">
<axsl:if test="@NAME = 'TEXT' or @NAME = 'PAGE' or @NAME = 'CONSTANT'">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SI_INVALID_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@NAME"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@PHASE = 'ACTION'                     and not(../descendant::ACTION_CONTROL[@TYPE = 'SUBMIT' or @TYPE='SUBMIT_AND_DISMISS'])">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_MISSING_SUBMIT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="parent::PAGE and (preceding-sibling::*/@NAME = @NAME)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_DUPLICATE_BEAN_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@ACTION_ID_PROPERTY and not(@PHASE='ACTION')">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SI_ACTID_PROP_ONLY_ON_ACTION</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@ACTION_ID_PROPERTY) and @PHASE='ACTION'           and ancestor::PAGE/descendant::SERVER_INTERFACE[@PHASE='ACTION']/@ACTION_ID_PROPERTY">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SI_NOT_ALL_ACTID_PROPS_SPECIFIED</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@ACTION_ID_PROPERTY           and not(ancestor::PAGE/descendant::ACTION_CONTROL[@TYPE='SUBMIT']/@ACTION_ID)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SI_ACTCTL_IDS_NOT_SPECIFIED</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3982" match="SHORTCUT_TITLE">
<axsl:if test="CONNECT/TARGET">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SHORTCUT_HAS_TARGET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="CONNECT/SOURCE"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SHORTCUT_MISSING_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3981" match="INITIAL">
<axsl:if test="@NAME = 'CONSTANT' or @NAME = 'TEXT'">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_INVALID_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@NAME"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME = 'PAGE')                     and not(key('si-by-name',                                 concat(ancestor::PAGE/@PAGE_ID, '.', @NAME)))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_UNDECLARED_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@NAME"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3980" match="SOURCE">
<axsl:variable select="ancestor::PAGE/@PAGE_ID" name="page-id"/>
<axsl:choose>
<axsl:when test="not(@NAME = 'PAGE')               or key('page-param-by-name', concat($page-id, '.', @PROPERTY))"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_INVALID_PAGE_PARAM</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(@NAME = 'CONSTANT') and not(@NAME = 'TEXT')               and not(@NAME = 'PAGE')               and not(key('si-by-name', concat($page-id, '.', @NAME)))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_UNDECLARED_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@NAME"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME = 'PAGE') and not(@NAME = 'CONSTANT')               and ../../parent::ACTION_CONTROL/@TYPE = 'SUBMIT'               and not(key('action-si-by-name', concat($page-id, '.', @NAME)))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_SUBMIT_NO_ACTION</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="key('action-si-by-name', concat($page-id, '.', @NAME))                     and (../parent::WIDGET_PARAMETER/@NAME='MULTI_SELECT_SOURCE')                     and (count(ancestor::PAGE//ACTION_CONTROL[@TYPE='SUBMIT']/LINK[@PAGE_ID='THIS']) = 0)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONFIRMED_WIDGET_WRONG_CONTEXT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3979" match="TARGET">
<axsl:variable select="@PROPERTY" name="property"/>
<axsl:if test="@NAME = 'CONSTANT' or @NAME = 'TEXT'">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_INVALID_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@NAME"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="../parent::FIELD and count(../../LINK/@*) &gt; 2">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_FIELD_INVALID_LINK</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="../parent::FIELD and ../../CONNECT/SOURCE[@NAME = 'PAGE']">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_PAGE_PARAM_AND_TARGET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME = 'PAGE') and not(@NAME = 'BROWSER')                     and not(@NAME = 'JSCRIPT_REF')                     and not(key('si-by-name',                                 concat(ancestor::PAGE/@PAGE_ID, '.', @NAME)))">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_CONNECT_UNDECLARED_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@NAME"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="@NAME = 'JSCRIPT_REF' and count(ancestor::PAGE/descendant::TARGET[@NAME = 'JSCRIPT_REF' and @PROPERTY = $property]) &gt; 1">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_JSCRIPT_REF_NOT_UNIQUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3978" match="INFORMATIONAL">
<axsl:if test="CONNECT/SOURCE[@NAME = 'CONSTANT' or @NAME = 'TEXT'                                    or @NAME = 'PAGE']">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_INFORMATIONAL_INVALID_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3977" match="FOOTER_ROW">
<axsl:choose>
<axsl:when test="count(parent::LIST/FIELD | parent::LIST/ACTION_CONTROL                      | parent::LIST/CONTAINER) = count(FIELD)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_INVALID_FOOTER_ROW</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="3976" match="WIDGET">
<axsl:if test="not(@TYPE = 'MULTISELECT') and (@HAS_CONFIRM_PAGE                       and @HAS_CONFIRM_PAGE='true')">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_INVALID_CONFIRMED_WIDGET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M5"/>
</axsl:template>
<axsl:template mode="M5" priority="-1" match="text()"/>
<axsl:template mode="M6" priority="4000" match="PAGE">
<axsl:variable select="@TYPE='DETAILS'" name="is-details"/>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(TAB_NAME) = 1)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_DETAILS_INVALID_TAB_NAME_NUMBER</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="$is-details or count(TAB_NAME) = 0"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_TAB_NAME</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(PAGE_TITLE) = 1)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_DETAILS_INVALID_PAGE_TITLE_NUMBER</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(SHORTCUT_TITLE) = 0)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(MENU) = 0)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(JSP_SCRIPTLET) = 0)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(INFORMATIONAL) = 0)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(DESCRIPTION) = 0)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(SCRIPT) = 0)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="(not ($is-details)) or (count(INCLUDE) = 0)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_INVALID_ELEMENT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M6"/>
</axsl:template>
<axsl:template mode="M6" priority="3999" match="TAB_NAME">
<axsl:if test="CONNECT/TARGET">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_TAB_NAME_HAS_TARGET</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="CONNECT/SOURCE"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_TAB_NAME_MISSING_SOURCE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M6"/>
</axsl:template>
<axsl:template mode="M6" priority="3998" match="SERVER_INTERFACE">
<axsl:variable select="ancestor::PAGE/@TYPE='DETAILS'" name="is-details"/>
<axsl:choose>
<axsl:when test="not ($is-details) or @PHASE = 'DISPLAY'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_SI_INVALID_PHASE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@PHASE"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M6"/>
</axsl:template>
<axsl:template mode="M6" priority="3997" match="ACTION_CONTROL">
<axsl:variable select="ancestor::PAGE/@TYPE='DETAILS'" name="is-details"/>
<axsl:choose>
<axsl:when test="not ($is-details) or @TYPE = 'ACTION'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_TYPE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@TYPE"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="LINK/CONDITION">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ACTCTL_INVALID_LINK_CONDITION</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M6"/>
</axsl:template>
<axsl:template mode="M6" priority="3996" match="PAGE_TITLE">
<axsl:variable select="ancestor::PAGE/@TYPE='DETAILS'" name="is-details"/>
<axsl:choose>
<axsl:when test="not ($is-details) or count(CONNECT) = count(*)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_TITLE_INVALID_ELEMENTS</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not ($is-details) or not (@DESCRIPTION or @ICON)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_PAGE_TITLE_INVALID_ATTRIBUTES</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M6"/>
</axsl:template>
<axsl:template mode="M6" priority="-1" match="text()"/>
<axsl:template mode="M7" priority="4000" match="INCLUDE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'FILE_NAME')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INCLUDE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FILE_NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@FILE_NAME) or @FILE_NAME=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INCLUDE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FILE_NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INCLUDE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3999" match="VIEW">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'VIEW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="''"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(PAGE_TITLE | SHORTCUT_TITLE | SERVER_INTERFACE | MENU | ACTION_SET | PAGE_PARAMETER | CONNECT | JSP_SCRIPTLET | CLUSTER | LIST | SCRIPT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'VIEW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_TITLE, SHORTCUT_TITLE, SERVER_INTERFACE, MENU, ACTION_SET, PAGE_PARAMETER, CONNECT, JSP_SCRIPTLET, CLUSTER, LIST, SCRIPT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3998" match="PAGE_GROUP">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'GROUP_ID' or name() = 'TITLE' or name() = 'VISIBLE' or name() = 'APPLICATION' or name() = 'PAGE_ID' or name() = 'PARENT_TARGET')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'GROUP_ID, TITLE, VISIBLE, APPLICATION, PAGE_ID, PARENT_TARGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@GROUP_ID) or @GROUP_ID=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'GROUP_ID'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@TITLE) or @TITLE=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@APPLICATION) or @APPLICATION=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'APPLICATION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(PAGE_PARAMETER | PAGE_REF | PAGE_GROUP_REF | PAGE_GROUP)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_PARAMETER, PAGE_REF, PAGE_GROUP_REF, PAGE_GROUP'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3997" match="PAGE_GROUP_REF">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'FILE_NAME' or name() = 'VISIBLE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP_REF'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FILE_NAME, VISIBLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@FILE_NAME) or @FILE_NAME=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP_REF'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FILE_NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_GROUP_REF'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3996" match="PAGE_REF">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'PAGE_ID' or name() = 'TITLE' or name() = 'VISIBLE' or name() = 'DEFAULT' or name() = 'HOME_PAGE' or name() = 'HIGHLIGHT_PAGE_ID')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_REF'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_ID, TITLE, VISIBLE, DEFAULT, HOME_PAGE, HIGHLIGHT_PAGE_ID'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_REF'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3995" match="INFORMATIONAL">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INFORMATIONAL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="''"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INFORMATIONAL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3994" match="PAGE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'PAGE_ID' or name() = 'SCRIPT_FILE' or name() = 'POPUP_PAGE' or name() = 'APPEND_COLON' or name() = 'HIDE_CONDITIONAL_LINKS' or name() = 'COMPONENT_STYLE' or name() = 'TYPE' or name() = 'WINDOW_OPTIONS')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_ID, SCRIPT_FILE, POPUP_PAGE, APPEND_COLON, HIDE_CONDITIONAL_LINKS, COMPONENT_STYLE, TYPE, WINDOW_OPTIONS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@PAGE_ID) or @PAGE_ID=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_ID'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(FIELD | CONTAINER | WIDGET | INCLUDE | PAGE_TITLE | SHORTCUT_TITLE | TAB_NAME | SERVER_INTERFACE | INFORMATIONAL | MENU | ACTION_SET | PAGE_PARAMETER | CONNECT | JSP_SCRIPTLET | CLUSTER | LIST | SCRIPT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FIELD, CONTAINER, WIDGET, INCLUDE, PAGE_TITLE, SHORTCUT_TITLE, TAB_NAME, SERVER_INTERFACE, INFORMATIONAL, MENU, ACTION_SET, PAGE_PARAMETER, CONNECT, JSP_SCRIPTLET, CLUSTER, LIST, SCRIPT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@TYPE) or @TYPE = 'SPLIT_WINDOW' or @TYPE = 'DEFAULT' or @TYPE = 'DETAILS'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TYPE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SPLIT_WINDOW, DEFAULT, DETAILS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3993" match="TITLE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'SEPARATOR')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SEPARATOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3992" match="LABEL">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'SEPARATOR')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SEPARATOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3991" match="PAGE_TITLE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'DESCRIPTION' or name() = 'STYLE' or name() = 'ICON')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DESCRIPTION, STYLE, ICON'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(DESCRIPTION | CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DESCRIPTION, CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3990" match="SHORTCUT_TITLE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'ICON')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SHORTCUT_TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ICON'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SHORTCUT_TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SHORTCUT_TITLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3989" match="TAB_NAME">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'ICON')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TAB_NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ICON'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TAB_NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TAB_NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3988" match="CONDITION">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'TYPE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONDITION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TYPE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(IS_TRUE | IS_FALSE | SCRIPT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONDITION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_TRUE, IS_FALSE, SCRIPT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3987" match="IS_TRUE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'NAME' or name() = 'PROPERTY')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_TRUE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME, PROPERTY'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME) or @NAME=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_TRUE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@PROPERTY) or @PROPERTY=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_TRUE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PROPERTY'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_TRUE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3986" match="IS_FALSE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'NAME' or name() = 'PROPERTY')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_FALSE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME, PROPERTY'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME) or @NAME=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_FALSE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@PROPERTY) or @PROPERTY=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_FALSE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PROPERTY'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IS_FALSE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3985" match="ACTION_SET">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'TOP' or name() = 'MAX_INLINE_ITEMS' or name() = 'BOTTOM' or name() = 'ALIGNMENT' or name() = 'TYPE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION_SET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TOP, MAX_INLINE_ITEMS, BOTTOM, ALIGNMENT, TYPE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONDITION | ACTION_CONTROL | SEPARATOR)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION_SET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONDITION, ACTION_CONTROL, SEPARATOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(ACTION_CONTROL)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION_SET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION_CONTROL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="not(@ALIGNMENT) or @ALIGNMENT = 'LEFT' or @ALIGNMENT = 'RIGHT' or @ALIGNMENT = 'CENTER' or @ALIGNMENT = 'DEFAULT'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ALIGNMENT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LEFT, RIGHT, CENTER, DEFAULT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@TYPE) or @TYPE = 'LIST_ROW_MENU' or @TYPE = 'DEFAULT'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TYPE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LIST_ROW_MENU, DEFAULT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3984" match="PAGE_PARAMETER">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'NAME')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME) or @NAME=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3983" match="MENU">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'MODE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'MENU'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'MODE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(ACTION_CONTROL | CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'MENU'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION_CONTROL, CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@MODE) or @MODE = 'STATIC' or @MODE = 'DYNAMIC' or @MODE = 'INTEGRATED_CASE' or @MODE = 'NAVIGATION' or @MODE = 'IN_PAGE_NAVIGATION' or @MODE = 'WIZARD_PROGRESS_BAR'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'MODE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'STATIC, DYNAMIC, INTEGRATED_CASE, NAVIGATION, IN_PAGE_NAVIGATION, WIZARD_PROGRESS_BAR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3982" match="SOURCE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'NAME' or name() = 'PROPERTY' or name() = 'EXTENDED_PATH')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SOURCE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME, PROPERTY, EXTENDED_PATH'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SOURCE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3981" match="TARGET">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'NAME' or name() = 'PROPERTY' or name() = 'EXTENDED_PATH')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TARGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME, PROPERTY, EXTENDED_PATH'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TARGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3980" match="CONNECT">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="''"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(INITIAL | SOURCE | TARGET)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INITIAL, SOURCE, TARGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3979" match="LIST_CONNECT">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LIST_CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="''"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(SOURCE | TARGET)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LIST_CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SOURCE, TARGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(SOURCE)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LIST_CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SOURCE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(TARGET)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LIST_CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TARGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3978" match="SEPARATOR">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SEPARATOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="''"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SEPARATOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3977" match="SERVER_INTERFACE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'NAME' or name() = 'CLASS' or name() = 'OPERATION' or name() = 'PHASE' or name() = 'ACTION_ID_PROPERTY')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SERVER_INTERFACE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME, CLASS, OPERATION, PHASE, ACTION_ID_PROPERTY'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME) or @NAME=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SERVER_INTERFACE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@CLASS) or @CLASS=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SERVER_INTERFACE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CLASS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@OPERATION) or @OPERATION=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SERVER_INTERFACE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'OPERATION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SERVER_INTERFACE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="not(@PHASE) or @PHASE = 'DISPLAY' or @PHASE = 'ACTION'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PHASE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DISPLAY, ACTION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3976" match="INITIAL">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'HIDDEN_PROPERTY' or name() = 'NAME' or name() = 'PROPERTY' or name() = 'EXTENDED_PATH')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INITIAL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'HIDDEN_PROPERTY, NAME, PROPERTY, EXTENDED_PATH'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INITIAL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3975" match="LINK">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'PAGE_ID' or name() = 'PAGE_ID_REF' or name() = 'OPEN_NEW' or name() = 'OPEN_MODAL' or name() = 'DISMISS_MODAL' or name() = 'SAVE_LINK' or name() = 'URL' or name() = 'URI' or name() = 'URI_REF' or name() = 'URI_SOURCE_NAME' or name() = 'URI_SOURCE_PROPERTY' or name() = 'SET_HIERARCHY_RETURN_PAGE' or name() = 'USE_HIERARCHY_RETURN_PAGE' or name() = 'HOME_PAGE' or name() = 'WINDOW_OPTIONS')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LINK'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_ID, PAGE_ID_REF, OPEN_NEW, OPEN_MODAL, DISMISS_MODAL, SAVE_LINK, URL, URI, URI_REF, URI_SOURCE_NAME, URI_SOURCE_PROPERTY, SET_HIERARCHY_RETURN_PAGE, USE_HIERARCHY_RETURN_PAGE, HOME_PAGE, WINDOW_OPTIONS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT | CONDITION)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LINK'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT, CONDITION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3974" match="ACTION_CONTROL">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'LABEL' or name() = 'LABEL_ABBREVIATION' or name() = 'IMAGE' or name() = 'STYLE' or name() = 'CONFIRM' or name() = 'TYPE' or name() = 'DEFAULT' or name() = 'ACTION_ID' or name() = 'ALIGNMENT' or name() = 'APPEND_ELLIPSIS')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION_CONTROL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL, LABEL_ABBREVIATION, IMAGE, STYLE, CONFIRM, TYPE, DEFAULT, ACTION_ID, ALIGNMENT, APPEND_ELLIPSIS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(LINK | CONNECT | SCRIPT | CONDITION)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION_CONTROL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LINK, CONNECT, SCRIPT, CONDITION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@TYPE) or @TYPE = 'ACTION' or @TYPE = 'SUBMIT' or @TYPE = 'DISMISS' or @TYPE = 'SUBMIT_AND_DISMISS' or @TYPE = 'CLIPBOARD' or @TYPE = 'FILE_DOWNLOAD'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TYPE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ACTION, SUBMIT, DISMISS, SUBMIT_AND_DISMISS, CLIPBOARD, FILE_DOWNLOAD'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@ALIGNMENT) or @ALIGNMENT = 'LEFT' or @ALIGNMENT = 'RIGHT' or @ALIGNMENT = 'DEFAULT'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ALIGNMENT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LEFT, RIGHT, DEFAULT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3973" match="IMAGE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'IMAGE' or name() = 'LABEL' or name() = 'STYLE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IMAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IMAGE, LABEL, STYLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@IMAGE) or @IMAGE=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IMAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IMAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@LABEL) or @LABEL=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IMAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'IMAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3972" match="CONTAINER">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'LABEL' or name() = 'WIDTH' or name() = 'ALIGNMENT' or name() = 'SEPARATOR' or name() = 'LABEL_ABBREVIATION' or name() = 'STYLE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONTAINER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL, WIDTH, ALIGNMENT, SEPARATOR, LABEL_ABBREVIATION, STYLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(FIELD | IMAGE | ACTION_CONTROL | WIDGET)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONTAINER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FIELD, IMAGE, ACTION_CONTROL, WIDGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@ALIGNMENT) or @ALIGNMENT = 'LEFT' or @ALIGNMENT = 'RIGHT' or @ALIGNMENT = 'CENTER' or @ALIGNMENT = 'DEFAULT'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ALIGNMENT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LEFT, RIGHT, CENTER, DEFAULT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3971" match="CLUSTER">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'TITLE' or name() = 'SUMMARY' or name() = 'NUM_COLS' or name() = 'SHOW_LABELS' or name() = 'TAB_ORDER' or name() = 'LAYOUT_ORDER' or name() = 'WIDTH' or name() = 'STYLE' or name() = 'DESCRIPTION' or name() = 'LABEL_WIDTH' or name() = 'BEHAVIOR' or name() = 'SCROLL_HEIGHT')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CLUSTER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TITLE, SUMMARY, NUM_COLS, SHOW_LABELS, TAB_ORDER, LAYOUT_ORDER, WIDTH, STYLE, DESCRIPTION, LABEL_WIDTH, BEHAVIOR, SCROLL_HEIGHT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONDITION | TITLE | DESCRIPTION | ACTION_SET | FIELD | WIDGET | CONTAINER | CLUSTER | LIST)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CLUSTER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONDITION, TITLE, DESCRIPTION, ACTION_SET, FIELD, WIDGET, CONTAINER, CLUSTER, LIST'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@TAB_ORDER) or @TAB_ORDER = 'COLUMN' or @TAB_ORDER = 'ROW'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TAB_ORDER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'COLUMN, ROW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@LAYOUT_ORDER) or @LAYOUT_ORDER = 'FIELD' or @LAYOUT_ORDER = 'LABEL'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LAYOUT_ORDER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FIELD, LABEL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@BEHAVIOR) or @BEHAVIOR = 'EXPANDED' or @BEHAVIOR = 'COLLAPSED' or @BEHAVIOR = 'NONE'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'BEHAVIOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'EXPANDED, COLLAPSED, NONE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3970" match="LIST">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'STYLE' or name() = 'TITLE' or name() = 'DESCRIPTION' or name() = 'WIDTH' or name() = 'SORTABLE' or name() = 'BEHAVIOR' or name() = 'SCROLL_HEIGHT' or name() = 'SUMMARY' or name() = 'PAGINATED' or name() = 'DEFAULT_PAGE_SIZE' or name() = 'PAGINATION_THRESHOLD')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LIST'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'STYLE, TITLE, DESCRIPTION, WIDTH, SORTABLE, BEHAVIOR, SCROLL_HEIGHT, SUMMARY, PAGINATED, DEFAULT_PAGE_SIZE, PAGINATION_THRESHOLD'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONDITION | TITLE | DESCRIPTION | ACTION_SET | DETAILS_ROW | FIELD | ACTION_CONTROL | CONTAINER | FOOTER_ROW | WIDGET | LIST_CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LIST'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONDITION, TITLE, DESCRIPTION, ACTION_SET, DETAILS_ROW, FIELD, ACTION_CONTROL, CONTAINER, FOOTER_ROW, WIDGET, LIST_CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@BEHAVIOR) or @BEHAVIOR = 'EXPANDED' or @BEHAVIOR = 'COLLAPSED' or @BEHAVIOR = 'NONE'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'BEHAVIOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'EXPANDED, COLLAPSED, NONE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3969" match="FOOTER_ROW">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FOOTER_ROW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="''"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(FIELD)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FOOTER_ROW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FIELD'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3968" match="FIELD">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'EDITABLE' or name() = 'LABEL_ABBREVIATION' or name() = 'LABEL' or name() = 'DESCRIPTION' or name() = 'PROMPT' or name() = 'INITIAL_FOCUS' or name() = 'ALT_TEXT' or name() = 'CONTROL' or name() = 'CONTROL_REF' or name() = 'WIDTH' or name() = 'WIDTH_UNITS' or name() = 'USE_BLANK' or name() = 'USE_DEFAULT' or name() = 'HEIGHT' or name() = 'DOMAIN' or name() = 'ALIGNMENT' or name() = 'CONFIG' or name() = 'APPEND_ELLIPSIS')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FIELD'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'EDITABLE, LABEL_ABBREVIATION, LABEL, DESCRIPTION, PROMPT, INITIAL_FOCUS, ALT_TEXT, CONTROL, CONTROL_REF, WIDTH, WIDTH_UNITS, USE_BLANK, USE_DEFAULT, HEIGHT, DOMAIN, ALIGNMENT, CONFIG, APPEND_ELLIPSIS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(LABEL | CONNECT | LINK | SCRIPT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'FIELD'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL, CONNECT, LINK, SCRIPT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@EDITABLE) or @EDITABLE = 'EDITABLE' or @EDITABLE = 'NONEDITABLE' or @EDITABLE = 'UNSPECIFIED'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'EDITABLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'EDITABLE, NONEDITABLE, UNSPECIFIED'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@CONTROL) or @CONTROL = 'DEFAULT' or @CONTROL = 'SUMMARY' or @CONTROL = 'DYNAMIC' or @CONTROL = 'DYNAMIC_FULL_TREE' or @CONTROL = 'FAILURE' or @CONTROL = 'SKIP' or @CONTROL = 'TRANSFER_LIST' or @CONTROL = 'CHECKBOXED_LIST' or @CONTROL = 'CT_HIERARCHY_HORIZONTAL' or @CONTROL = 'CT_HIERARCHY_VERTICAL'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONTROL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DEFAULT, SUMMARY, DYNAMIC, DYNAMIC_FULL_TREE, FAILURE, SKIP, TRANSFER_LIST, CHECKBOXED_LIST, CT_HIERARCHY_HORIZONTAL, CT_HIERARCHY_VERTICAL'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@WIDTH_UNITS) or @WIDTH_UNITS = 'PERCENT' or @WIDTH_UNITS = 'CHARS'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDTH_UNITS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PERCENT, CHARS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@ALIGNMENT) or @ALIGNMENT = 'LEFT' or @ALIGNMENT = 'RIGHT' or @ALIGNMENT = 'CENTER' or @ALIGNMENT = 'DEFAULT'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ALIGNMENT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LEFT, RIGHT, CENTER, DEFAULT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3967" match="WIDGET">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'LABEL' or name() = 'WIDTH' or name() = 'WIDTH_UNITS' or name() = 'ALIGNMENT' or name() = 'TYPE' or name() = 'HAS_CONFIRM_PAGE' or name() = 'CONFIG' or name() = 'COMPONENT_STYLE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LABEL, WIDTH, WIDTH_UNITS, ALIGNMENT, TYPE, HAS_CONFIRM_PAGE, CONFIG, COMPONENT_STYLE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@TYPE) or @TYPE=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TYPE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(WIDGET_PARAMETER)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@WIDTH_UNITS) or @WIDTH_UNITS = 'PERCENT' or @WIDTH_UNITS = 'CHARS'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDTH_UNITS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PERCENT, CHARS'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@ALIGNMENT) or @ALIGNMENT = 'LEFT' or @ALIGNMENT = 'RIGHT' or @ALIGNMENT = 'CENTER' or @ALIGNMENT = 'DEFAULT'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ALIGNMENT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'LEFT, RIGHT, CENTER, DEFAULT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:choose>
<axsl:when test="not(@TYPE) or @TYPE = 'CONTENT' or @TYPE = 'FILE_UPLOAD' or @TYPE = 'FILE_EDIT' or @TYPE = 'MULTISELECT' or @TYPE = 'SINGLESELECT' or @TYPE = 'RULES_SIMULATION_EDITOR' or @TYPE = 'FILE_DOWNLOAD' or @TYPE = 'WIZARD_SUMMARY' or @TYPE = 'USER_PREFERENCE_EDITOR'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'TYPE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONTENT, FILE_UPLOAD, FILE_EDIT, MULTISELECT, SINGLESELECT, RULES_SIMULATION_EDITOR, FILE_DOWNLOAD, WIZARD_SUMMARY, USER_PREFERENCE_EDITOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3966" match="WIDGET_PARAMETER">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'NAME')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="not(@NAME) or @NAME=''">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_ATTRIBUTE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'NAME'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'WIDGET_PARAMETER'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3965" match="JSP_SCRIPTLET">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'JSP_SCRIPTLET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="''"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'JSP_SCRIPTLET'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3964" match="SCRIPT">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'EVENT' or name() = 'ACTION' or name() = 'EXPRESSION' or name() = 'SCRIPT_FILE')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SCRIPT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'EVENT, ACTION, EXPRESSION, SCRIPT_FILE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:if test="*">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_NOT_EMPTY</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SCRIPT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="not(@EVENT) or @EVENT = 'ONLOAD' or @EVENT = 'ONUNLOAD' or @EVENT = 'ONCLICK' or @EVENT = 'ONDBLCLICK' or @EVENT = 'ONCHANGE' or @EVENT = 'ONFOCUS' or @EVENT = 'ONBLUR' or @EVENT = 'ONKEYPRESS' or @EVENT = 'ONKEYDOWN' or @EVENT = 'ONKEYUP' or @EVENT = 'ONMOUSEDOWN' or @EVENT = 'ONMOUSEUP' or @EVENT = 'ONMOUSEMOVE' or @EVENT = 'ONMOUSEOVER' or @EVENT = 'ONMOUSEOUT'"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ATTRIBUTE_INVALID_VALUE</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'EVENT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'ONLOAD, ONUNLOAD, ONCLICK, ONDBLCLICK, ONCHANGE, ONFOCUS, ONBLUR, ONKEYPRESS, ONKEYDOWN, ONKEYUP, ONMOUSEDOWN, ONMOUSEUP, ONMOUSEMOVE, ONMOUSEOVER, ONMOUSEOUT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3963" match="DESCRIPTION">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'SEPARATOR')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DESCRIPTION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'SEPARATOR'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DESCRIPTION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(CONNECT)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DESCRIPTION'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3962" match="INLINE_PAGE">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'PAGE_ID' or name() = 'URI_SOURCE_NAME' or name() = 'URI_SOURCE_PROPERTY')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INLINE_PAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'PAGE_ID, URI_SOURCE_NAME, URI_SOURCE_PROPERTY'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONNECT)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INLINE_PAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONNECT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="3961" match="DETAILS_ROW">
<axsl:if test="@*[not(namespace-uri() or name() = '__line' or name() = '__file' or name() = 'MINIMUM_EXPANDED_HEIGHT')]">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_INVALID_ATT</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DETAILS_ROW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'MINIMUM_EXPANDED_HEIGHT'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:choose>
<axsl:when test="count(*) = count(CONDITION | INLINE_PAGE)"/>
<axsl:otherwise>
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_UNEXPECTED_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DETAILS_ROW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'CONDITION, INLINE_PAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:otherwise>
</axsl:choose>
<axsl:if test="not(INLINE_PAGE)">
<axsl:message>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__file"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="@__line"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="name()"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>:</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:text>ERR_ELEMENT_MISSING_CHILD</axsl:text>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>:</axsl:text>
<axsl:text>(</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'DETAILS_ROW'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>|</axsl:text>
<axsl:call-template name="escape-string">
<axsl:with-param name="string">
<axsl:value-of select="'INLINE_PAGE'"/>
</axsl:with-param>
<axsl:with-param select="$chars-to-escape" name="chars-to-escape"/>
</axsl:call-template>
<axsl:text>)</axsl:text>
</axsl:message>
</axsl:if>
<axsl:apply-templates mode="M7"/>
</axsl:template>
<axsl:template mode="M7" priority="-1" match="text()"/>
<axsl:template priority="-1" match="text()"/>
</axsl:stylesheet>
