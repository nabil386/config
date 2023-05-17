var profile = (function() {
  var configData = @___configData___replaced_by_build_script___@;

  /**
   * Generate packages array for the specified dependencies.
   *
   * @param {String[]} deps Aray of component names.
   */
  var generateComponentDependencyPackages = function(deps) {
    var i, result = [];

    if (deps) {
      for (i = 0; i < deps.length; i++) {
        result.push({ name: deps[i], location: deps[i]});
      }
    }
    
    return result;
  };

  var getLayers = function(conf) {
    var result = {}, excludes = conf.layerExcludes || [];
    result["dojo/" + "@___layerName___replaced_by_build_script___@"] = { include: conf.layerContent, exclude: excludes };
    return result;
  };
  
  // return the profile configuration
  return {
    // relative to this file
    basePath: ".",

    // relative to base path
    releaseDir: "@___releaseDir___replaced_by_build_script___@",
    
    mini: true,
    selectorEngine: "acme",
    optimize: "shrinksafe",
    layerOptimize: "shrinksafe",
    stripConsole: "none",
    cssOptimize:"comments.keepLines",

    localeList: configData.localeList,
    
    packages: [
      { name: "dojo", location: "dojo" },
      { name: "dojox", location: "dojox" },
      { name: "dijit", location: "dijit" },
      { name: "idx", location: "idx" },
      { name: "curam", location: "curam" },
      { name: "cm", location: "cm" },
	  { name: "gridx", location: "gridx"},
      { name: configData.packageName, location: configData.packageName } //assumes the package content is copied in the base location by the build script
    ].concat(generateComponentDependencyPackages(configData.componentDependencies)),

    layers: getLayers(configData)
  };
})();

