/**
 * Create a reflection effect in the Browser by duplicating a HTML node and 
 * applying a style to flip it upside down and fade out. The styling effect is
 * only available in IE so the effect is only applied in IE. This function 
 * simply clones the node and adds a wrapper around the original and the cloned
 * and then adds a class to which the style is applied.
 *
 * Note: There is no guarantee on how the final product will appear, which is 
 * obviously subjective, so it is the users responsibility to ensure that the 
 * final rendered product looks correct.
 * 
 * Modification History
 * --------------------
 * 14-Apr-2010 BD [CR00193523] Initial version.
 */
var reflection = {
    
    reflect: function(node){
    
      if(dojo.isIE){
        // Get pointers
        var child = node;
        var parent = child.parentNode;

        // Remove the flag for reflection before cloning,
        // otherwise this will trigger this function again.
        child.removeAttribute("reflect-me");

        // Store the original set of classes to be added to clones.
        var originalClasses = child.getAttribute("className");

        // Clone the original for the new original!
        var original = child.cloneNode(true);
        var className = originalClasses + " original";
        original.setAttribute("className", className);
        
        // Clone the original for the reflection.
        var reflection = child.cloneNode(true);
        className = originalClasses + " reflection";
        reflection.setAttribute("className", className);
                
        // Create a wrapper and append all the Nodes.
        var wrapper = document.createElement("div");
        wrapper.setAttribute("className", "content");
        wrapper.appendChild(original);
        wrapper.appendChild(reflection);
        
        // Replace the original child with the wrapper.
        parent.replaceChild(wrapper,child);
      }
    }
};
