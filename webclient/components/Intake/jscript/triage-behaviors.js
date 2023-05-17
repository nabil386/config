require(["dojo/fx","cm/_base/_pageBehaviors"]);

cm.registerBehavior("collapsibleDescription", {
  ".bhv-open" :
  {
    "onclick": function(evt) {
      dojo.stopEvent(evt);
      var wrapper = cm.getParentByClass(evt.target, "triage-description");
      
      dojo.query(".bhv-collapsible", wrapper)
        .forEach(function(collapsible){
          if (dojo.style(collapsible, "display") == 'none')
          {
            dojo.style(collapsible, "display", "block");
            if (!collapsible._shrunk && dojo.isIE == 6) {
              collapsible._shrunk = true;
              var width = dojo.style(collapsible.parentNode, "width");
              dojo.style(collapsible, "width", (width - 4) + "px");
            }            
            dojo.style(collapsible, "height", "1px");
            dojo.fx.wipeIn({node:collapsible, onEnd: function(){
              // Transfer the focus to the the matching anchor tag for a11y
              var href = evt.target.getAttribute("href");
              if (href != null && href.length > 1) {
                dojo.query("a[name=" + href.substring(1) + "]").forEach("item.focus()");
              }
            }}).play();
            dojo.removeClass(collapsible, "ifScript-hide");
            dojo.removeClass(evt.target, "openDescriptionLink");
            dojo.addClass(evt.target, "openDescriptionLinkSelected");
          }
          else
          {
            dojo.fx.wipeOut({node:collapsible, onEnd:function(){
            dojo.style(collapsible, "display", "none");
              dojo.removeClass(evt.target, "openDescriptionLinkSelected");
              dojo.addClass(evt.target, "openDescriptionLink");
          }}).play();
        }
      });
      dojo.publish("/anim/toggle");
    }
  },
  ".bhv-close" :
  {
    "onclick": function(evt) {
      dojo.stopEvent(evt);
      var input = evt.target;
      var wrapper = cm.getParentByClass(evt.target, "cluster") || cm.getParentByClass(evt.target, "list");
      var collapsible = dojo.query(".bhv-collapsible", wrapper)[0];
      dojo.style(collapsible, "overflowY", "hidden");

      dojo.fx.combine([
        dojo.fx.wipeOut({node: collapsible})
      ]).play();

      var openLink = dojo.query(".bhv-open", wrapper)[0];

      dojo.removeClass(openLink, "openDescriptionLinkSelected");
      dojo.addClass(openLink, "openDescriptionLink");

      dojo.publish("/anim/toggle");
    }
  }
});