define(['dojo', 'dijit/dijit', "dojo/_base/declare", "dijit/MenuItem"],
       function(dojo, dijit, declare, MenuItem) {

  return declare("curam.widget.menu.BannerMenuItem", [MenuItem], {
      iconSrc: "unknown",
      _setIconSrcAttr: {node: "iconNode",
                        type: "attribute",
                        attribute: "src" },
      iconStyle: "unknown",
      _setIconStyleAttr: {node: "iconNode",
                          type: "attribute",
                          attribute: "style" }
  });
})
