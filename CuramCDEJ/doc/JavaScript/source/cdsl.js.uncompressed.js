require({cache:{
'dojo/request/default':function(){
define([
	'exports',
	'require',
	'../has'
], function(exports, require, has){
	var defId = has('config-requestProvider'),
		platformId;

	if( 1  || has('host-webworker')){
		platformId = './xhr';
	}else if( 0 ){
		platformId = './node';
	/* TODO:
	}else if( 0 ){
		platformId = './rhino';
   */
	}

	if(!defId){
		defId = platformId;
	}

	exports.getPlatformDefaultId = function(){
		return platformId;
	};

	exports.load = function(id, parentRequire, loaded, config){
		require([id == 'platform' ? platformId : defId], function(provider){
			loaded(provider);
		});
	};
});

},
'dojo/store/Memory':function(){
define(["../_base/declare", "./util/QueryResults", "./util/SimpleQueryEngine" /*=====, "./api/Store" =====*/],
function(declare, QueryResults, SimpleQueryEngine /*=====, Store =====*/){

// module:
//		dojo/store/Memory

// No base class, but for purposes of documentation, the base class is dojo/store/api/Store
var base = null;
/*===== base = Store; =====*/

return declare("dojo.store.Memory", base, {
	// summary:
	//		This is a basic in-memory object store. It implements dojo/store/api/Store.
	constructor: function(options){
		// summary:
		//		Creates a memory object store.
		// options: dojo/store/Memory
		//		This provides any configuration information that will be mixed into the store.
		//		This should generally include the data property to provide the starting set of data.
		for(var i in options){
			this[i] = options[i];
		}
		this.setData(this.data || []);
	},
	// data: Array
	//		The array of all the objects in the memory store
	data:null,

	// idProperty: String
	//		Indicates the property to use as the identity property. The values of this
	//		property should be unique.
	idProperty: "id",

	// index: Object
	//		An index of data indices into the data array by id
	index:null,

	// queryEngine: Function
	//		Defines the query engine to use for querying the data store
	queryEngine: SimpleQueryEngine,
	get: function(id){
		// summary:
		//		Retrieves an object by its identity
		// id: Number
		//		The identity to use to lookup the object
		// returns: Object
		//		The object in the store that matches the given id.
		return this.data[this.index[id]];
	},
	getIdentity: function(object){
		// summary:
		//		Returns an object's identity
		// object: Object
		//		The object to get the identity from
		// returns: Number
		return object[this.idProperty];
	},
	put: function(object, options){
		// summary:
		//		Stores an object
		// object: Object
		//		The object to store.
		// options: dojo/store/api/Store.PutDirectives?
		//		Additional metadata for storing the data.  Includes an "id"
		//		property if a specific id is to be used.
		// returns: Number
		var data = this.data,
			index = this.index,
			idProperty = this.idProperty;
		var id = object[idProperty] = (options && "id" in options) ? options.id : idProperty in object ? object[idProperty] : Math.random();
		if(id in index){
			// object exists
			if(options && options.overwrite === false){
				throw new Error("Object already exists");
			}
			// replace the entry in data
			data[index[id]] = object;
		}else{
			// add the new object
			index[id] = data.push(object) - 1;
		}
		return id;
	},
	add: function(object, options){
		// summary:
		//		Creates an object, throws an error if the object already exists
		// object: Object
		//		The object to store.
		// options: dojo/store/api/Store.PutDirectives?
		//		Additional metadata for storing the data.  Includes an "id"
		//		property if a specific id is to be used.
		// returns: Number
		(options = options || {}).overwrite = false;
		// call put with overwrite being false
		return this.put(object, options);
	},
	remove: function(id){
		// summary:
		//		Deletes an object by its identity
		// id: Number
		//		The identity to use to delete the object
		// returns: Boolean
		//		Returns true if an object was removed, falsy (undefined) if no object matched the id
		var index = this.index;
		var data = this.data;
		if(id in index){
			data.splice(index[id], 1);
			// now we have to reindex
			this.setData(data);
			return true;
		}
	},
	query: function(query, options){
		// summary:
		//		Queries the store for objects.
		// query: Object
		//		The query to use for retrieving objects from the store.
		// options: dojo/store/api/Store.QueryOptions?
		//		The optional arguments to apply to the resultset.
		// returns: dojo/store/api/Store.QueryResults
		//		The results of the query, extended with iterative methods.
		//
		// example:
		//		Given the following store:
		//
		// 	|	var store = new Memory({
		// 	|		data: [
		// 	|			{id: 1, name: "one", prime: false },
		//	|			{id: 2, name: "two", even: true, prime: true},
		//	|			{id: 3, name: "three", prime: true},
		//	|			{id: 4, name: "four", even: true, prime: false},
		//	|			{id: 5, name: "five", prime: true}
		//	|		]
		//	|	});
		//
		//	...find all items where "prime" is true:
		//
		//	|	var results = store.query({ prime: true });
		//
		//	...or find all items where "even" is true:
		//
		//	|	var results = store.query({ even: true });
		return QueryResults(this.queryEngine(query, options)(this.data));
	},
	setData: function(data){
		// summary:
		//		Sets the given data as the source for this store, and indexes it
		// data: Object[]
		//		An array of objects to use as the source of data.
		if(data.items){
			// just for convenience with the data format IFRS expects
			this.idProperty = data.identifier || this.idProperty;
			data = this.data = data.items;
		}else{
			this.data = data;
		}
		this.index = {};
		for(var i = 0, l = data.length; i < l; i++){
			this.index[data[i][this.idProperty]] = i;
		}
	}
});

});

},
'dojo/i18n':function(){
define(["./_base/kernel", "require", "./has", "./_base/array", "./_base/config", "./_base/lang", "./_base/xhr", "./json", "module"],
	function(dojo, require, has, array, config, lang, xhr, json, module){

	// module:
	//		dojo/i18n

	has.add("dojo-preload-i18n-Api",
		// if true, define the preload localizations machinery
		1
	);

	 1 || has.add("dojo-v1x-i18n-Api",
		// if true, define the v1.x i18n functions
		1
	);

	var
		thisModule = dojo.i18n =
			{
				// summary:
				//		This module implements the dojo/i18n! plugin and the v1.6- i18n API
				// description:
				//		We choose to include our own plugin to leverage functionality already contained in dojo
				//		and thereby reduce the size of the plugin compared to various loader implementations. Also, this
				//		allows foreign AMD loaders to be used without their plugins.
			},

		nlsRe =
			// regexp for reconstructing the master bundle name from parts of the regexp match
			// nlsRe.exec("foo/bar/baz/nls/en-ca/foo") gives:
			// ["foo/bar/baz/nls/en-ca/foo", "foo/bar/baz/nls/", "/", "/", "en-ca", "foo"]
			// nlsRe.exec("foo/bar/baz/nls/foo") gives:
			// ["foo/bar/baz/nls/foo", "foo/bar/baz/nls/", "/", "/", "foo", ""]
			// so, if match[5] is blank, it means this is the top bundle definition.
			// courtesy of http://requirejs.org
			/(^.*(^|\/)nls)(\/|$)([^\/]*)\/?([^\/]*)/,

		getAvailableLocales = function(
			root,
			locale,
			bundlePath,
			bundleName
		){
			// summary:
			//		return a vector of module ids containing all available locales with respect to the target locale
			//		For example, assuming:
			//
			//		- the root bundle indicates specific bundles for "fr" and "fr-ca",
			//		-  bundlePath is "myPackage/nls"
			//		- bundleName is "myBundle"
			//
			//		Then a locale argument of "fr-ca" would return
			//
			//			["myPackage/nls/myBundle", "myPackage/nls/fr/myBundle", "myPackage/nls/fr-ca/myBundle"]
			//
			//		Notice that bundles are returned least-specific to most-specific, starting with the root.
			//
			//		If root===false indicates we're working with a pre-AMD i18n bundle that doesn't tell about the available locales;
			//		therefore, assume everything is available and get 404 errors that indicate a particular localization is not available

			for(var result = [bundlePath + bundleName], localeParts = locale.split("-"), current = "", i = 0; i<localeParts.length; i++){
				current += (current ? "-" : "") + localeParts[i];
				if(!root || root[current]){
					result.push(bundlePath + current + "/" + bundleName);
					result.specificity = current;
				}
			}
			return result;
		},

		cache = {},

		getBundleName = function(moduleName, bundleName, locale){
			locale = locale ? locale.toLowerCase() : dojo.locale;
			moduleName = moduleName.replace(/\./g, "/");
			bundleName = bundleName.replace(/\./g, "/");
			return (/root/i.test(locale)) ?
				(moduleName + "/nls/" + bundleName) :
				(moduleName + "/nls/" + locale + "/" + bundleName);
		},

		getL10nName = dojo.getL10nName = function(moduleName, bundleName, locale){
			return moduleName = module.id + "!" + getBundleName(moduleName, bundleName, locale);
		},

		doLoad = function(require, bundlePathAndName, bundlePath, bundleName, locale, load){
			// summary:
			//		get the root bundle which instructs which other bundles are required to construct the localized bundle
			require([bundlePathAndName], function(root){
				var current = lang.clone(root.root || root.ROOT),// 1.6 built bundle defined ROOT
					availableLocales = getAvailableLocales(!root._v1x && root, locale, bundlePath, bundleName);
				require(availableLocales, function(){
					for (var i = 1; i<availableLocales.length; i++){
						current = lang.mixin(lang.clone(current), arguments[i]);
					}
					// target may not have been resolve (e.g., maybe only "fr" exists when "fr-ca" was requested)
					var target = bundlePathAndName + "/" + locale;
					cache[target] = current;
					current.$locale = availableLocales.specificity;
					load();
				});
			});
		},

		normalize = function(id, toAbsMid){
			// summary:
			//		id may be relative.
			//		preload has form `*preload*<path>/nls/<module>*<flattened locales>` and
			//		therefore never looks like a relative
			return /^\./.test(id) ? toAbsMid(id) : id;
		},

		getLocalesToLoad = function(targetLocale){
			var list = config.extraLocale || [];
			list = lang.isArray(list) ? list : [list];
			list.push(targetLocale);
			return list;
		},

		load = function(id, require, load){
			// summary:
			//		id is in one of the following formats
			//
			//		1. <path>/nls/<bundle>
			//			=> load the bundle, localized to config.locale; load all bundles localized to
			//			config.extraLocale (if any); return the loaded bundle localized to config.locale.
			//
			//		2. <path>/nls/<locale>/<bundle>
			//			=> load then return the bundle localized to <locale>
			//
			//		3. *preload*<path>/nls/<module>*<JSON array of available locales>
			//			=> for config.locale and all config.extraLocale, load all bundles found
			//			in the best-matching bundle rollup. A value of 1 is returned, which
			//			is meaningless other than to say the plugin is executing the requested
			//			preloads
			//
			//		In cases 1 and 2, <path> is always normalized to an absolute module id upon entry; see
			//		normalize. In case 3, it <path> is assumed to be absolute; this is arranged by the builder.
			//
			//		To load a bundle means to insert the bundle into the plugin's cache and publish the bundle
			//		value to the loader. Given <path>, <bundle>, and a particular <locale>, the cache key
			//
			//			<path>/nls/<bundle>/<locale>
			//
			//		will hold the value. Similarly, then plugin will publish this value to the loader by
			//
			//			define("<path>/nls/<bundle>/<locale>", <bundle-value>);
			//
			//		Given this algorithm, other machinery can provide fast load paths be preplacing
			//		values in the plugin's cache, which is public. When a load is demanded the
			//		cache is inspected before starting any loading. Explicitly placing values in the plugin
			//		cache is an advanced/experimental feature that should not be needed; use at your own risk.
			//
			//		For the normal AMD algorithm, the root bundle is loaded first, which instructs the
			//		plugin what additional localized bundles are required for a particular locale. These
			//		additional locales are loaded and a mix of the root and each progressively-specific
			//		locale is returned. For example:
			//
			//		1. The client demands "dojo/i18n!some/path/nls/someBundle
			//
			//		2. The loader demands load(some/path/nls/someBundle)
			//
			//		3. This plugin require's "some/path/nls/someBundle", which is the root bundle.
			//
			//		4. Assuming config.locale is "ab-cd-ef" and the root bundle indicates that localizations
			//		are available for "ab" and "ab-cd-ef" (note the missing "ab-cd", then the plugin
			//		requires "some/path/nls/ab/someBundle" and "some/path/nls/ab-cd-ef/someBundle"
			//
			//		5. Upon receiving all required bundles, the plugin constructs the value of the bundle
			//		ab-cd-ef as...
			//
			//				mixin(mixin(mixin({}, require("some/path/nls/someBundle"),
			//		  			require("some/path/nls/ab/someBundle")),
			//					require("some/path/nls/ab-cd-ef/someBundle"));
			//
			//		This value is inserted into the cache and published to the loader at the
			//		key/module-id some/path/nls/someBundle/ab-cd-ef.
			//
			//		The special preload signature (case 3) instructs the plugin to stop servicing all normal requests
			//		(further preload requests will be serviced) until all ongoing preloading has completed.
			//
			//		The preload signature instructs the plugin that a special rollup module is available that contains
			//		one or more flattened, localized bundles. The JSON array of available locales indicates which locales
			//		are available. Here is an example:
			//
			//			*preload*some/path/nls/someModule*["root", "ab", "ab-cd-ef"]
			//
			//		This indicates the following rollup modules are available:
			//
			//			some/path/nls/someModule_ROOT
			//			some/path/nls/someModule_ab
			//			some/path/nls/someModule_ab-cd-ef
			//
			//		Each of these modules is a normal AMD module that contains one or more flattened bundles in a hash.
			//		For example, assume someModule contained the bundles some/bundle/path/someBundle and
			//		some/bundle/path/someOtherBundle, then some/path/nls/someModule_ab would be expressed as follows:
			//
			//			define({
			//				some/bundle/path/someBundle:<value of someBundle, flattened with respect to locale ab>,
			//				some/bundle/path/someOtherBundle:<value of someOtherBundle, flattened with respect to locale ab>,
			//			});
			//
			//		E.g., given this design, preloading for locale=="ab" can execute the following algorithm:
			//
			//			require(["some/path/nls/someModule_ab"], function(rollup){
			//				for(var p in rollup){
			//					var id = p + "/ab",
			//					cache[id] = rollup[p];
			//					define(id, rollup[p]);
			//				}
			//			});
			//
			//		Similarly, if "ab-cd" is requested, the algorithm can determine that "ab" is the best available and
			//		load accordingly.
			//
			//		The builder will write such rollups for every layer if a non-empty localeList  profile property is
			//		provided. Further, the builder will include the following cache entry in the cache associated with
			//		any layer.
			//
			//			"*now":function(r){r(['dojo/i18n!*preload*<path>/nls/<module>*<JSON array of available locales>']);}
			//
			//		The *now special cache module instructs the loader to apply the provided function to context-require
			//		with respect to the particular layer being defined. This causes the plugin to hold all normal service
			//		requests until all preloading is complete.
			//
			//		Notice that this algorithm is rarely better than the standard AMD load algorithm. Consider the normal case
			//		where the target locale has a single segment and a layer depends on a single bundle:
			//
			//		Without Preloads:
			//
			//		1. Layer loads root bundle.
			//		2. bundle is demanded; plugin loads single localized bundle.
			//
			//		With Preloads:
			//
			//		1. Layer causes preloading of target bundle.
			//		2. bundle is demanded; service is delayed until preloading complete; bundle is returned.
			//
			//		In each case a single transaction is required to load the target bundle. In cases where multiple bundles
			//		are required and/or the locale has multiple segments, preloads still requires a single transaction whereas
			//		the normal path requires an additional transaction for each additional bundle/locale-segment. However all
			//		of these additional transactions can be done concurrently. Owing to this analysis, the entire preloading
			//		algorithm can be discard during a build by setting the has feature dojo-preload-i18n-Api to false.

			if(has("dojo-preload-i18n-Api")){
				var split = id.split("*"),
					preloadDemand = split[1] == "preload";
				if(preloadDemand){
					if(!cache[id]){
						// use cache[id] to prevent multiple preloads of the same preload; this shouldn't happen, but
						// who knows what over-aggressive human optimizers may attempt
						cache[id] = 1;
						preloadL10n(split[2], json.parse(split[3]), 1, require);
					}
					// don't stall the loader!
					load(1);
				}
				if(preloadDemand || waitForPreloads(id, require, load)){
					return;
				}
			}

			var match = nlsRe.exec(id),
				bundlePath = match[1] + "/",
				bundleName = match[5] || match[4],
				bundlePathAndName = bundlePath + bundleName,
				localeSpecified = (match[5] && match[4]),
				targetLocale =	localeSpecified || dojo.locale || "",
				loadTarget = bundlePathAndName + "/" + targetLocale,
				loadList = localeSpecified ? [targetLocale] : getLocalesToLoad(targetLocale),
				remaining = loadList.length,
				finish = function(){
					if(!--remaining){
						load(lang.delegate(cache[loadTarget]));
					}
				};
			array.forEach(loadList, function(locale){
				var target = bundlePathAndName + "/" + locale;
				if(has("dojo-preload-i18n-Api")){
					checkForLegacyModules(target);
				}
				if(!cache[target]){
					doLoad(require, bundlePathAndName, bundlePath, bundleName, locale, finish);
				}else{
					finish();
				}
			});
		};

	if(has("dojo-unit-tests")){
		var unitTests = thisModule.unitTests = [];
	}

	if(has("dojo-preload-i18n-Api") ||  1 ){
		var normalizeLocale = thisModule.normalizeLocale = function(locale){
				var result = locale ? locale.toLowerCase() : dojo.locale;
				return result == "root" ? "ROOT" : result;
			},

			isXd = function(mid, contextRequire){
				return ( 1  &&  1 ) ?
					contextRequire.isXdUrl(require.toUrl(mid + ".js")) :
					true;
			},

			preloading = 0,

			preloadWaitQueue = [],

			preloadL10n = thisModule._preloadLocalizations = function(/*String*/bundlePrefix, /*Array*/localesGenerated, /*boolean?*/ guaranteedAmdFormat, /*function?*/ contextRequire){
				// summary:
				//		Load available flattened resource bundles associated with a particular module for dojo/locale and all dojo/config.extraLocale (if any)
				// description:
				//		Only called by built layer files. The entire locale hierarchy is loaded. For example,
				//		if locale=="ab-cd", then ROOT, "ab", and "ab-cd" are loaded. This is different than v1.6-
				//		in that the v1.6- would only load ab-cd...which was *always* flattened.
				//
				//		If guaranteedAmdFormat is true, then the module can be loaded with require thereby circumventing the detection algorithm
				//		and the extra possible extra transaction.

				// If this function is called from legacy code, then guaranteedAmdFormat and contextRequire will be undefined. Since the function
				// needs a require in order to resolve module ids, fall back to the context-require associated with this dojo/i18n module, which
				// itself may have been mapped.
				contextRequire = contextRequire || require;

				function doRequire(mid, callback){
					if(isXd(mid, contextRequire) || guaranteedAmdFormat){
						contextRequire([mid], callback);
					}else{
						syncRequire([mid], callback, contextRequire);
					}
				}

				function forEachLocale(locale, func){
					// given locale= "ab-cd-ef", calls func on "ab-cd-ef", "ab-cd", "ab", "ROOT"; stops calling the first time func returns truthy
					var parts = locale.split("-");
					while(parts.length){
						if(func(parts.join("-"))){
							return;
						}
						parts.pop();
					}
					func("ROOT");
				}

					function preloadingAddLock(){
						preloading++;
					}

					function preloadingRelLock(){
						--preloading;
						while(!preloading && preloadWaitQueue.length){
							load.apply(null, preloadWaitQueue.shift());
						}
					}

					function cacheId(path, name, loc, require){
						// path is assumed to have a trailing "/"
						return require.toAbsMid(path + name + "/" + loc)
					}

					function preload(locale){
						locale = normalizeLocale(locale);
						forEachLocale(locale, function(loc){
							if(array.indexOf(localesGenerated, loc) >= 0){
								var mid = bundlePrefix.replace(/\./g, "/") + "_" + loc;
								preloadingAddLock();
								doRequire(mid, function(rollup){
									for(var p in rollup){
										var bundle = rollup[p],
											match = p.match(/(.+)\/([^\/]+)$/),
											bundleName, bundlePath;
											
											// If there is no match, the bundle is not a regular bundle from an AMD layer.
											if (!match){continue;}

											bundleName = match[2];
											bundlePath = match[1] + "/";

										// backcompat
										if(!bundle._localized){continue;}

										var localized;
										if(loc === "ROOT"){
											var root = localized = bundle._localized;
											delete bundle._localized;
											root.root = bundle;
											cache[require.toAbsMid(p)] = root;
										}else{
											localized = bundle._localized;
											cache[cacheId(bundlePath, bundleName, loc, require)] = bundle;
										}

										if(loc !== locale){
											// capture some locale variables
											function improveBundle(bundlePath, bundleName, bundle, localized){
												// locale was not flattened and we've fallen back to a less-specific locale that was flattened
												// for example, we had a flattened 'fr', a 'fr-ca' is available for at least this bundle, and
												// locale==='fr-ca'; therefore, we must improve the bundle as retrieved from the rollup by
												// manually loading the fr-ca version of the bundle and mixing this into the already-retrieved 'fr'
												// version of the bundle.
												//
												// Remember, different bundles may have different sets of locales available.
												//
												// we are really falling back on the regular algorithm here, but--hopefully--starting with most
												// of the required bundles already on board as given by the rollup and we need to "manually" load
												// only one locale from a few bundles...or even better...we won't find anything better to load.
												// This algorithm ensures there is nothing better to load even when we can only load a less-specific rollup.
												//
												// note: this feature is only available in async mode

												// inspect the loaded bundle that came from the rollup to see if something better is available
												// for any bundle in a rollup, more-specific available locales are given at localized.
												var requiredBundles = [],
													cacheIds = [];
												forEachLocale(locale, function(loc){
													if(localized[loc]){
														requiredBundles.push(require.toAbsMid(bundlePath + loc + "/" + bundleName));
														cacheIds.push(cacheId(bundlePath, bundleName, loc, require));
													}
												});

												if(requiredBundles.length){
													preloadingAddLock();
													contextRequire(requiredBundles, function(){
														// requiredBundles was constructed by forEachLocale so it contains locales from 
														// less specific to most specific. 
														// the loop starts with the most specific locale, the last one.
														for(var i = requiredBundles.length - 1; i >= 0 ; i--){
															bundle = lang.mixin(lang.clone(bundle), arguments[i]);
															cache[cacheIds[i]] = bundle;
														}
														// this is the best possible (maybe a perfect match, maybe not), accept it
														cache[cacheId(bundlePath, bundleName, locale, require)] = lang.clone(bundle);
														preloadingRelLock();
													});
												}else{
													// this is the best possible (definitely not a perfect match), accept it
													cache[cacheId(bundlePath, bundleName, locale, require)] = bundle;
												}
											}
											improveBundle(bundlePath, bundleName, bundle, localized);
										}
									}
									preloadingRelLock();
								});
								return true;
							}
							return false;
						});
					}

				preload();
				array.forEach(dojo.config.extraLocale, preload);
			},

			waitForPreloads = function(id, require, load){
				if(preloading){
					preloadWaitQueue.push([id, require, load]);
				}
				return preloading;
			},

			checkForLegacyModules = function()
				{};
	}

	if( 1 ){
		// this code path assumes the dojo loader and won't work with a standard AMD loader
		var amdValue = {},
			evalBundle =
				// use the function ctor to keep the minifiers away (also come close to global scope, but this is secondary)
				new Function(
					"__bundle",				   // the bundle to evalutate
					"__checkForLegacyModules", // a function that checks if __bundle defined __mid in the global space
					"__mid",				   // the mid that __bundle is intended to define
					"__amdValue",

					// returns one of:
					//		1 => the bundle was an AMD bundle
					//		a legacy bundle object that is the value of __mid
					//		instance of Error => could not figure out how to evaluate bundle

					  // used to detect when __bundle calls define
					  "var define = function(mid, factory){define.called = 1; __amdValue.result = factory || mid;},"
					+ "	   require = function(){define.called = 1;};"

					+ "try{"
					+		"define.called = 0;"
					+		"eval(__bundle);"
					+		"if(define.called==1)"
								// bundle called define; therefore signal it's an AMD bundle
					+			"return __amdValue;"

					+		"if((__checkForLegacyModules = __checkForLegacyModules(__mid)))"
								// bundle was probably a v1.6- built NLS flattened NLS bundle that defined __mid in the global space
					+			"return __checkForLegacyModules;"

					+ "}catch(e){}"
					// evaulating the bundle was *neither* an AMD *nor* a legacy flattened bundle
					// either way, re-eval *after* surrounding with parentheses

					+ "try{"
					+		"return eval('('+__bundle+')');"
					+ "}catch(e){"
					+		"return e;"
					+ "}"
				),

			syncRequire = function(deps, callback, require){
				var results = [];
				array.forEach(deps, function(mid){
					var url = require.toUrl(mid + ".js");

					function load(text){
						var result = evalBundle(text, checkForLegacyModules, mid, amdValue);
						if(result===amdValue){
							// the bundle was an AMD module; re-inject it through the normal AMD path
							// we gotta do this since it could be an anonymous module and simply evaluating
							// the text here won't provide the loader with the context to know what
							// module is being defined()'d. With browser caching, this should be free; further
							// this entire code path can be circumvented by using the AMD format to begin with
							results.push(cache[url] = amdValue.result);
						}else{
							if(result instanceof Error){
								console.error("failed to evaluate i18n bundle; url=" + url, result);
								result = {};
							}
							// nls/<locale>/<bundle-name> indicates not the root.
							results.push(cache[url] = (/nls\/[^\/]+\/[^\/]+$/.test(url) ? result : {root:result, _v1x:1}));
						}
					}

					if(cache[url]){
						results.push(cache[url]);
					}else{
						var bundle = require.syncLoadNls(mid);
						// need to check for legacy module here because there might be a legacy module for a
						// less specific locale (which was not looked up during the first checkForLegacyModules
						// call in load()).
						// Also need to reverse the locale and the module name in the mid because syncRequire
						// deps parameters uses the AMD style package/nls/locale/module while legacy code uses
						// package/nls/module/locale.
						if(!bundle){
							bundle = checkForLegacyModules(mid.replace(/nls\/([^\/]*)\/([^\/]*)$/, "nls/$2/$1"));
						}
						if(bundle){
							results.push(bundle);
						}else{
							if(!xhr){
								try{
									require.getText(url, true, load);
								}catch(e){
									results.push(cache[url] = {});
								}
							}else{
								xhr.get({
									url:url,
									sync:true,
									load:load,
									error:function(){
										results.push(cache[url] = {});
									}
								});
							}
						}
					}
				});
				callback && callback.apply(null, results);
			};

		checkForLegacyModules = function(target){
			// legacy code may have already loaded [e.g] the raw bundle x/y/z at x.y.z; when true, push into the cache
			for(var result, names = target.split("/"), object = dojo.global[names[0]], i = 1; object && i<names.length-1; object = object[names[i++]]){}
			if(object){
				result = object[names[i]];
				if(!result){
					// fallback for incorrect bundle build of 1.6
					result = object[names[i].replace(/-/g,"_")];
				}
				if(result){
					cache[target] = result;
				}
			}
			return result;
		};

		thisModule.getLocalization = function(moduleName, bundleName, locale){
			var result,
				l10nName = getBundleName(moduleName, bundleName, locale);
			load(
				l10nName,

				// isXd() and syncRequire() need a context-require in order to resolve the mid with respect to a reference module.
				// Since this legacy function does not have the concept of a reference module, resolve with respect to this
				// dojo/i18n module, which, itself may have been mapped.
				(!isXd(l10nName, require) ? function(deps, callback){ syncRequire(deps, callback, require); } : require),

				function(result_){ result = result_; }
			);
			return result;
		};

		if(has("dojo-unit-tests")){
			unitTests.push(function(doh){
				doh.register("tests.i18n.unit", function(t){
					var check;

					check = evalBundle("{prop:1}", checkForLegacyModules, "nonsense", amdValue);
					t.is({prop:1}, check); t.is(undefined, check[1]);

					check = evalBundle("({prop:1})", checkForLegacyModules, "nonsense", amdValue);
					t.is({prop:1}, check); t.is(undefined, check[1]);

					check = evalBundle("{'prop-x':1}", checkForLegacyModules, "nonsense", amdValue);
					t.is({'prop-x':1}, check); t.is(undefined, check[1]);

					check = evalBundle("({'prop-x':1})", checkForLegacyModules, "nonsense", amdValue);
					t.is({'prop-x':1}, check); t.is(undefined, check[1]);

					check = evalBundle("define({'prop-x':1})", checkForLegacyModules, "nonsense", amdValue);
					t.is(amdValue, check); t.is({'prop-x':1}, amdValue.result);

					check = evalBundle("define('some/module', {'prop-x':1})", checkForLegacyModules, "nonsense", amdValue);
					t.is(amdValue, check); t.is({'prop-x':1}, amdValue.result);

					check = evalBundle("this is total nonsense and should throw an error", checkForLegacyModules, "nonsense", amdValue);
					t.is(check instanceof Error, true);
				});
			});
		}
	}

	return lang.mixin(thisModule, {
		dynamic:true,
		normalize:normalize,
		load:load,
		cache:cache,
		getL10nName: getL10nName
	});
});

},
'curam/util/ResourceBundle':function(){
/*
 * Copyright 2012 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */
define(["dojo/_base/declare",
        "dojo/i18n",
        "dojo/string"
        ], function(declare, i18n, string) {

/*
 * Modification History
 * --------------------
 * 20-May-2013  MV  [CR00383012] Fail if there are no properties loaded.
 * 19-May-2012  BOS [CR00346368] Use new Dojo AMD format.
 * 15-Jun-2012  MV  [CR00329034] Added proper documentation.
 * 11-Jun-2012  MV  [CR00328689] Initial version.
 */

/**
 * @name curam.util.ResourceBundle
 * @namespace Provides access to localizable resources.
 * <p/>
 * The process for getting localized messaged from a resource bundle consists
 * of two steps: <ol>
 * <li>Load the resources using <code>dojo.requireLocalization()</code></li>
 * <li>Create an instance of <code>curam.util.ResourceBunlde</code> class
 *      to access the localized resources.</li>
 * </ol>
 *
 * <h2>Loading Resources</h2>
 * In most cases the call to load resources should look like this:
 * <code><pre>dojo.requireLocalization("curam.application", "MyResources")</pre></code>
 * <p/>
 * "curam.application" is the default package into which all localizable
 * resources are placed by Curam infrastructure.
 * <p/>
 * "MyResources" is an example of a resource bundle name. Resource bundle name
 * will be specific to your own JavaScript code and it is derived from the name
 * of the related resource bundle *.properties file.
 *
 * <h2>Accessing Localized Resources</h2>
 * Previously loaded localized resources can be accessed in the following way:
 * <code><pre>dojo.require("curam.util.ResourceBundle");
 * var bundle = new curam.util.ResourceBundle("MyResources");
 * var localizedMessage = bundle.getProperty("myPropertyKey");
 * </pre></code>
 * Note in the above example there is no need to specify the default package
 * name "curam.appliciation" - the infrastructure will use the default
 * if no package is specified. This should be the case in most normal
 * situations.
 *
 * <h2>Resource File Naming and Content</h2>
 * The localizable resources for your JavaScript are expected in the standard
 * Java Properties format.
 * <p/>
 * By convention the name of the resource file for your JavaScript should be
 * derived from name of the JavaScript file itself. For example if your
 * JavaScript file is called "MyJavaScript.js" then related localizable
 * resources should be placed in <code>MyJavaScript.js.properties</code> file.
 * This .properties file can be placed anywhere in the component directory, but
 * by convention it should be in the same directory as the related *.js file.
 * The only exception to this, is that a *.js file within a WebContent directory
 * cannot have it's associated .properties file within the same directory - the
 * associated .properties file must be placed within a directory outside of the
 * WebContent directory.
 * <p/>
 * The tranlations of the resource bundle should then be placed in files named
 * in the following way (again following the Java standard naming):
 * <code>MyJavaScript.js_fr_CA.properties</code>,
 * <code>MyJavaScript.js_fr.properties</code>,
 * <code>MyJavaScript.js_cs_CZ.properties</code>, etc.
 * <p/>
 * Sample content of a resource file is as follows:
 * <code><pre>myPropertyKey=A localizable message.
 * another.property.key=Another localizable message.
 * propertyKey3=A message with %s value placeholders %s.
 * </pre></code>
 * Please note property keys with dots are allowed and string value
 * substitution into mesages is supported.
 */
 var ResourceBundle = declare("curam.util.ResourceBundle", null,
/**
 * @lends curam.util.ResourceBundle.prototype
 */
{
  _bundle: undefined,

  /**
   * Constructor takes bundle name and optionally locale.
   *
   * @param {String} possiblyQualifiedBundleName Bundle name. Optionally
   *           qualified with package name. E.g. "my.package.MyResourceBundle".
   * @param {String} [locale] Locale string in the following format:
   *            <code> en-US</code> where "en" is language code and "US"
   *            is variant as per IETF specification.
   */
  constructor: function(possiblyQualifiedBundleName, locale) {
    var parts = possiblyQualifiedBundleName.split(".");
    var bundleName = parts[parts.length - 1];
    var packageName = parts.length == 1 ? "curam.application"
        : possiblyQualifiedBundleName.slice(0,
            possiblyQualifiedBundleName.length - bundleName.length - 1);
    try {
      var b = i18n.getLocalization(packageName, bundleName, locale);
      if (this._isEmpty(b)) {
        throw new Error("Empty resource bundle.");

      } else {
        this._bundle = b;
      }

    } catch (e) {
      throw new Error("Unable to access resource bundle: " + packageName + "."
          + bundleName + ": " + e.message);
    }
  },
  
  /**
   * Checks if the passed bundle is empty or has some properties.
   * @param bundle The bundle object to check.
   * @returns {Boolean} True if the bundle if empty, false if it contains
   *            properties.
   */
  _isEmpty: function(bundle) {
    for (var prop in bundle) {
      // if it has at least one property, return false - it is not empty
      return false;
    }
    // no properties - return true as it is empty
    return true;
  },

  /**
   * Gets the localized value of a specified property, optionally replacing any
   * placeholders with appropriate values from specified array.
   *
   * @param {String} key The property key of the required message.
   * @param {Array} [values] An array of values to be used for replacing
   *            placeholders within the specified message.
   * @returns {String} Value of the requested localized property from
   *            the bundle.
   */
  getProperty: function(key, values) {
    var msg = this._bundle[key];

    var result = msg;
    if (values) {
      result = string.substitute(msg, values);
    }

    return result;
  }
  });
 return ResourceBundle;
});
},
'dojo/string':function(){
define([
	"./_base/kernel",	// kernel.global
	"./_base/lang"
], function(kernel, lang){

// module:
//		dojo/string
var ESCAPE_REGEXP = /[&<>'"\/]/g;
var ESCAPE_MAP = {
	'&': '&amp;',
	'<': '&lt;',
	'>': '&gt;',
	'"': '&quot;',
	"'": '&#x27;',
	'/': '&#x2F;'
};
var string = {
	// summary:
	//		String utilities for Dojo
};
lang.setObject("dojo.string", string);

string.escape = function(/*String*/str){
	// summary:
	//		Efficiently escape a string for insertion into HTML (innerHTML or attributes), replacing &, <, >, ", ', and / characters.
	// str:
	//		the string to escape
	if(!str){ return ""; }
	return str.replace(ESCAPE_REGEXP, function(c) {
		return ESCAPE_MAP[c];
	});
};

string.rep = function(/*String*/str, /*Integer*/num){
	// summary:
	//		Efficiently replicate a string `n` times.
	// str:
	//		the string to replicate
	// num:
	//		number of times to replicate the string

	if(num <= 0 || !str){ return ""; }

	var buf = [];
	for(;;){
		if(num & 1){
			buf.push(str);
		}
		if(!(num >>= 1)){ break; }
		str += str;
	}
	return buf.join("");	// String
};

string.pad = function(/*String*/text, /*Integer*/size, /*String?*/ch, /*Boolean?*/end){
	// summary:
	//		Pad a string to guarantee that it is at least `size` length by
	//		filling with the character `ch` at either the start or end of the
	//		string. Pads at the start, by default.
	// text:
	//		the string to pad
	// size:
	//		length to provide padding
	// ch:
	//		character to pad, defaults to '0'
	// end:
	//		adds padding at the end if true, otherwise pads at start
	// example:
	//	|	// Fill the string to length 10 with "+" characters on the right.  Yields "Dojo++++++".
	//	|	string.pad("Dojo", 10, "+", true);

	if(!ch){
		ch = '0';
	}
	var out = String(text),
		pad = string.rep(ch, Math.ceil((size - out.length) / ch.length));
	return end ? out + pad : pad + out;	// String
};

string.substitute = function(	/*String*/		template,
									/*Object|Array*/map,
									/*Function?*/	transform,
									/*Object?*/		thisObject){
	// summary:
	//		Performs parameterized substitutions on a string. Throws an
	//		exception if any parameter is unmatched.
	// template:
	//		a string with expressions in the form `${key}` to be replaced or
	//		`${key:format}` which specifies a format function. keys are case-sensitive.
	// map:
	//		hash to search for substitutions
	// transform:
	//		a function to process all parameters before substitution takes
	//		place, e.g. mylib.encodeXML
	// thisObject:
	//		where to look for optional format function; default to the global
	//		namespace
	// example:
	//		Substitutes two expressions in a string from an Array or Object
	//	|	// returns "File 'foo.html' is not found in directory '/temp'."
	//	|	// by providing substitution data in an Array
	//	|	string.substitute(
	//	|		"File '${0}' is not found in directory '${1}'.",
	//	|		["foo.html","/temp"]
	//	|	);
	//	|
	//	|	// also returns "File 'foo.html' is not found in directory '/temp'."
	//	|	// but provides substitution data in an Object structure.  Dotted
	//	|	// notation may be used to traverse the structure.
	//	|	string.substitute(
	//	|		"File '${name}' is not found in directory '${info.dir}'.",
	//	|		{ name: "foo.html", info: { dir: "/temp" } }
	//	|	);
	// example:
	//		Use a transform function to modify the values:
	//	|	// returns "file 'foo.html' is not found in directory '/temp'."
	//	|	string.substitute(
	//	|		"${0} is not found in ${1}.",
	//	|		["foo.html","/temp"],
	//	|		function(str){
	//	|			// try to figure out the type
	//	|			var prefix = (str.charAt(0) == "/") ? "directory": "file";
	//	|			return prefix + " '" + str + "'";
	//	|		}
	//	|	);
	// example:
	//		Use a formatter
	//	|	// returns "thinger -- howdy"
	//	|	string.substitute(
	//	|		"${0:postfix}", ["thinger"], null, {
	//	|			postfix: function(value, key){
	//	|				return value + " -- howdy";
	//	|			}
	//	|		}
	//	|	);

	thisObject = thisObject || kernel.global;
	transform = transform ?
		lang.hitch(thisObject, transform) : function(v){ return v; };

	return template.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,
		function(match, key, format){
			var value = lang.getObject(key, false, map);
			if(format){
				value = lang.getObject(format, false, thisObject).call(thisObject, value, key);
			}
			return transform(value, key).toString();
		}); // String
};

string.trim = String.prototype.trim ?
	lang.trim : // aliasing to the native function
	function(str){
		str = str.replace(/^\s+/, '');
		for(var i = str.length - 1; i >= 0; i--){
			if(/\S/.test(str.charAt(i))){
				str = str.substring(0, i + 1);
				break;
			}
		}
		return str;
	};

/*=====
 string.trim = function(str){
	 // summary:
	 //		Trims whitespace from both sides of the string
	 // str: String
	 //		String to be trimmed
	 // returns: String
	 //		Returns the trimmed string
	 // description:
	 //		This version of trim() was taken from [Steven Levithan's blog](http://blog.stevenlevithan.com/archives/faster-trim-javascript).
	 //		The short yet performant version of this function is dojo/_base/lang.trim(),
	 //		which is part of Dojo base.  Uses String.prototype.trim instead, if available.
	 return "";	// String
 };
 =====*/

	return string;
});

},
'dojo/store/Observable':function(){
define(["../_base/kernel", "../_base/lang", "../when", "../_base/array" /*=====, "./api/Store" =====*/
], function(kernel, lang, when, array /*=====, Store =====*/){

// module:
//		dojo/store/Observable

var Observable = function(/*Store*/ store){
	// summary:
	//		The Observable store wrapper takes a store and sets an observe method on query()
	//		results that can be used to monitor results for changes.
	//
	// description:
	//		Observable wraps an existing store so that notifications can be made when a query
	//		is performed.
	//
	// example:
	//		Create a Memory store that returns an observable query, and then log some
	//		information about that query.
	//
	//	|	var store = Observable(new Memory({
	//	|		data: [
	//	|			{id: 1, name: "one", prime: false},
	//	|			{id: 2, name: "two", even: true, prime: true},
	//	|			{id: 3, name: "three", prime: true},
	//	|			{id: 4, name: "four", even: true, prime: false},
	//	|			{id: 5, name: "five", prime: true}
	//	|		]
	//	|	}));
	//	|	var changes = [], results = store.query({ prime: true });
	//	|	var observer = results.observe(function(object, previousIndex, newIndex){
	//	|		changes.push({previousIndex:previousIndex, newIndex:newIndex, object:object});
	//	|	});
	//
	//		See the Observable tests for more information.

	var undef, queryUpdaters = [], revision = 0;
	// a Comet driven store could directly call notify to notify observers when data has
	// changed on the backend
	// create a new instance
	store = lang.delegate(store);
	
	store.notify = function(object, existingId){
		revision++;
		var updaters = queryUpdaters.slice();
		for(var i = 0, l = updaters.length; i < l; i++){
			updaters[i](object, existingId);
		}
	};
	var originalQuery = store.query;
	store.query = function(query, options){
		options = options || {};
		var results = originalQuery.apply(this, arguments);
		if(results && results.forEach){
			var nonPagedOptions = lang.mixin({}, options);
			delete nonPagedOptions.start;
			delete nonPagedOptions.count;

			var queryExecutor = store.queryEngine && store.queryEngine(query, nonPagedOptions);
			var queryRevision = revision;
			var listeners = [], queryUpdater;
			results.observe = function(listener, includeObjectUpdates){
				if(listeners.push(listener) == 1){
					// first listener was added, create the query checker and updater
					queryUpdaters.push(queryUpdater = function(changed, existingId){
						when(results, function(resultsArray){
							var atEnd = resultsArray.length != options.count;
							var i, l, listener;
							if(++queryRevision != revision){
								throw new Error("Query is out of date, you must observe() the query prior to any data modifications");
							}
							var removedObject, removedFrom = -1, insertedInto = -1;
							if(existingId !== undef){
								// remove the old one
								var filteredArray = [].concat(resultsArray);
								if(queryExecutor && !changed){
									filteredArray = queryExecutor(resultsArray);
								}
								for(i = 0, l = resultsArray.length; i < l; i++){
									var object = resultsArray[i];
									if(store.getIdentity(object) == existingId){
										if(filteredArray.indexOf(object)<0) continue;
										removedObject = object;
										removedFrom = i;
										if(queryExecutor || !changed){// if it was changed and we don't have a queryExecutor, we shouldn't remove it because updated objects would be eliminated
											resultsArray.splice(i, 1);
										}
										break;
									}
								}
							}
							if(queryExecutor){
								// add the new one
								if(changed &&
										// if a matches function exists, use that (probably more efficient)
										(queryExecutor.matches ? queryExecutor.matches(changed) : queryExecutor([changed]).length)){

									var firstInsertedInto = removedFrom > -1 ? 
										removedFrom : // put back in the original slot so it doesn't move unless it needs to (relying on a stable sort below)
										resultsArray.length;
									resultsArray.splice(firstInsertedInto, 0, changed); // add the new item
									insertedInto = array.indexOf(queryExecutor(resultsArray), changed); // sort it
									// we now need to push the change back into the original results array
									resultsArray.splice(firstInsertedInto, 1); // remove the inserted item from the previous index
									
									if((options.start && insertedInto == 0) ||
										(!atEnd && insertedInto == resultsArray.length)){
										// if it is at the end of the page, assume it goes into the prev or next page
										insertedInto = -1;
									}else{
										resultsArray.splice(insertedInto, 0, changed); // and insert into the results array with the correct index
									}
								}
							}else if(changed){
								// we don't have a queryEngine, so we can't provide any information
								// about where it was inserted or moved to. If it is an update, we leave it's position alone, other we at least indicate a new object
								if(existingId !== undef){
									// an update, keep the index the same
									insertedInto = removedFrom;
								}else if(!options.start){
									// a new object
									insertedInto = store.defaultIndex || 0;
									resultsArray.splice(insertedInto, 0, changed);
								}
							}
							if((removedFrom > -1 || insertedInto > -1) &&
									(includeObjectUpdates || !queryExecutor || (removedFrom != insertedInto))){
								var copyListeners = listeners.slice();
								for(i = 0;listener = copyListeners[i]; i++){
									listener(changed || removedObject, removedFrom, insertedInto);
								}
							}
						});
					});
				}
				var handle = {};
				// TODO: Remove cancel in 2.0.
				handle.remove = handle.cancel = function(){
					// remove this listener
					var index = array.indexOf(listeners, listener);
					if(index > -1){ // check to make sure we haven't already called cancel
						listeners.splice(index, 1);
						if(!listeners.length){
							// no more listeners, remove the query updater too
							queryUpdaters.splice(array.indexOf(queryUpdaters, queryUpdater), 1);
						}
					}
				};
				return handle;
			};
		}
		return results;
	};
	var inMethod;
	function whenFinished(method, action){
		var original = store[method];
		if(original){
			store[method] = function(value){
				var originalId;
				if(method === 'put'){
					originalId = store.getIdentity(value);
				}
				if(inMethod){
					// if one method calls another (like add() calling put()) we don't want two events
					return original.apply(this, arguments);
				}
				inMethod = true;
				try{
					var results = original.apply(this, arguments);
					when(results, function(results){
						action((typeof results == "object" && results) || value, originalId);
					});
					return results;
				}finally{
					inMethod = false;
				}
			};
		}
	}
	// monitor for updates by listening to these methods
	whenFinished("put", function(object, originalId){
		store.notify(object, originalId);
	});
	whenFinished("add", function(object){
		store.notify(object);
	});
	whenFinished("remove", function(id){
		store.notify(undefined, id);
	});

	return store;
};

lang.setObject("dojo.store.Observable", Observable);

return Observable;
});

},
'dojo/request/registry':function(){
define([
	'require',
	'../_base/array',
	'./default!platform',
	'./util'
], function(require, array, fallbackProvider, util){
	var providers = [];

	function request(url, options){
		var matchers = providers.slice(0),
			i = 0,
			matcher;

		while(matcher=matchers[i++]){
			if(matcher(url, options)){
				return matcher.request.call(null, url, options);
			}
		}

		return fallbackProvider.apply(null, arguments);
	}

	function createMatcher(match, provider){
		var matcher;

		if(provider){
			if(match.test){
				// RegExp
				matcher = function(url){
					return match.test(url);
				};
			}else if(match.apply && match.call){
				matcher = function(){
					return match.apply(null, arguments);
				};
			}else{
				matcher = function(url){
					return url === match;
				};
			}

			matcher.request = provider;
		}else{
			// If only one argument was passed, assume it is a provider function
			// to apply unconditionally to all URLs
			matcher = function(){
				return true;
			};

			matcher.request = match;
		}

		return matcher;
	}

	request.register = function(url, provider, first){
		var matcher = createMatcher(url, provider);
		providers[(first ? 'unshift' : 'push')](matcher);

		return {
			remove: function(){
				var idx;
				if(~(idx = array.indexOf(providers, matcher))){
					providers.splice(idx, 1);
				}
			}
		};
	};

	request.load = function(id, parentRequire, loaded, config){
		if(id){
			// if there's an id, load and set the fallback provider
			require([id], function(fallback){
				fallbackProvider = fallback;
				loaded(request);
			});
		}else{
			loaded(request);
		}
	};

	util.addCommonMethods(request);

	return request;
});

},
'dojo/store/util/SimpleQueryEngine':function(){
define(["../../_base/array" /*=====, "../api/Store" =====*/], function(arrayUtil /*=====, Store =====*/){

// module:
//		dojo/store/util/SimpleQueryEngine

return function(query, options){
	// summary:
	//		Simple query engine that matches using filter functions, named filter
	//		functions or objects by name-value on a query object hash
	//
	// description:
	//		The SimpleQueryEngine provides a way of getting a QueryResults through
	//		the use of a simple object hash as a filter.  The hash will be used to
	//		match properties on data objects with the corresponding value given. In
	//		other words, only exact matches will be returned.
	//
	//		This function can be used as a template for more complex query engines;
	//		for example, an engine can be created that accepts an object hash that
	//		contains filtering functions, or a string that gets evaluated, etc.
	//
	//		When creating a new dojo.store, simply set the store's queryEngine
	//		field as a reference to this function.
	//
	// query: Object
	//		An object hash with fields that may match fields of items in the store.
	//		Values in the hash will be compared by normal == operator, but regular expressions
	//		or any object that provides a test() method are also supported and can be
	//		used to match strings by more complex expressions
	//		(and then the regex's or object's test() method will be used to match values).
	//
	// options: dojo/store/api/Store.QueryOptions?
	//		An object that contains optional information such as sort, start, and count.
	//
	// returns: Function
	//		A function that caches the passed query under the field "matches".  See any
	//		of the "query" methods on dojo.stores.
	//
	// example:
	//		Define a store with a reference to this engine, and set up a query method.
	//
	//	|	var myStore = function(options){
	//	|		//	...more properties here
	//	|		this.queryEngine = SimpleQueryEngine;
	//	|		//	define our query method
	//	|		this.query = function(query, options){
	//	|			return QueryResults(this.queryEngine(query, options)(this.data));
	//	|		};
	//	|	};

	// create our matching query function
	switch(typeof query){
		default:
			throw new Error("Can not query with a " + typeof query);
		case "object": case "undefined":
			var queryObject = query;
			query = function(object){
				for(var key in queryObject){
					var required = queryObject[key];
					if(required && required.test){
						// an object can provide a test method, which makes it work with regex
						if(!required.test(object[key], object)){
							return false;
						}
					}else if(required != object[key]){
						return false;
					}
				}
				return true;
			};
			break;
		case "string":
			// named query
			if(!this[query]){
				throw new Error("No filter function " + query + " was found in store");
			}
			query = this[query];
			// fall through
		case "function":
			// fall through
	}
	function execute(array){
		// execute the whole query, first we filter
		var results = arrayUtil.filter(array, query);
		// next we sort
		var sortSet = options && options.sort;
		if(sortSet){
			results.sort(typeof sortSet == "function" ? sortSet : function(a, b){
				for(var sort, i=0; sort = sortSet[i]; i++){
					var aValue = a[sort.attribute];
					var bValue = b[sort.attribute];
					// valueOf enables proper comparison of dates
					aValue = aValue != null ? aValue.valueOf() : aValue;
					bValue = bValue != null ? bValue.valueOf() : bValue;
					if (aValue != bValue){
						return !!sort.descending == (aValue == null || aValue > bValue) ? -1 : 1;
					}
				}
				return 0;
			});
		}
		// now we paginate
		if(options && (options.start || options.count)){
			var total = results.length;
			results = results.slice(options.start || 0, (options.start || 0) + (options.count || Infinity));
			results.total = total;
		}
		return results;
	}
	execute.matches = query;
	return execute;
};

});

},
'dojo/date':function(){
define(["./has", "./_base/lang"], function(has, lang){
// module:
//		dojo/date

var date = {
	// summary:
	//		Date manipulation utilities
};

date.getDaysInMonth = function(/*Date*/dateObject){
	// summary:
	//		Returns the number of days in the month used by dateObject
	var month = dateObject.getMonth();
	var days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	if(month == 1 && date.isLeapYear(dateObject)){ return 29; } // Number
	return days[month]; // Number
};

date.isLeapYear = function(/*Date*/dateObject){
	// summary:
	//		Determines if the year of the dateObject is a leap year
	// description:
	//		Leap years are years with an additional day YYYY-02-29, where the
	//		year number is a multiple of four with the following exception: If
	//		a year is a multiple of 100, then it is only a leap year if it is
	//		also a multiple of 400. For example, 1900 was not a leap year, but
	//		2000 is one.

	var year = dateObject.getFullYear();
	return !(year%400) || (!(year%4) && !!(year%100)); // Boolean
};

// FIXME: This is not localized
date.getTimezoneName = function(/*Date*/dateObject){
	// summary:
	//		Get the user's time zone as provided by the browser
	// dateObject:
	//		Needed because the timezone may vary with time (daylight savings)
	// description:
	//		Try to get time zone info from toString or toLocaleString method of
	//		the Date object -- UTC offset is not a time zone.  See
	//		http://www.twinsun.com/tz/tz-link.htm Note: results may be
	//		inconsistent across browsers.

	var str = dateObject.toString(); // Start looking in toString
	var tz = ''; // The result -- return empty string if nothing found
	var match;

	// First look for something in parentheses -- fast lookup, no regex
	var pos = str.indexOf('(');
	if(pos > -1){
		tz = str.substring(++pos, str.indexOf(')'));
	}else{
		// If at first you don't succeed ...
		// If IE knows about the TZ, it appears before the year
		// Capital letters or slash before a 4-digit year
		// at the end of string
		var pat = /([A-Z\/]+) \d{4}$/;
		if((match = str.match(pat))){
			tz = match[1];
		}else{
		// Some browsers (e.g. Safari) glue the TZ on the end
		// of toLocaleString instead of putting it in toString
			str = dateObject.toLocaleString();
			// Capital letters or slash -- end of string,
			// after space
			pat = / ([A-Z\/]+)$/;
			if((match = str.match(pat))){
				tz = match[1];
			}
		}
	}

	// Make sure it doesn't somehow end up return AM or PM
	return (tz == 'AM' || tz == 'PM') ? '' : tz; // String
};

// Utility methods to do arithmetic calculations with Dates

date.compare = function(/*Date*/date1, /*Date?*/date2, /*String?*/portion){
	// summary:
	//		Compare two date objects by date, time, or both.
	// description:
	//		Returns 0 if equal, positive if a > b, else negative.
	// date1:
	//		Date object
	// date2:
	//		Date object.  If not specified, the current Date is used.
	// portion:
	//		A string indicating the "date" or "time" portion of a Date object.
	//		Compares both "date" and "time" by default.  One of the following:
	//		"date", "time", "datetime"

	// Extra step required in copy for IE - see #3112
	date1 = new Date(+date1);
	date2 = new Date(+(date2 || new Date()));

	if(portion == "date"){
		// Ignore times and compare dates.
		date1.setHours(0, 0, 0, 0);
		date2.setHours(0, 0, 0, 0);
	}else if(portion == "time"){
		// Ignore dates and compare times.
		date1.setFullYear(0, 0, 0);
		date2.setFullYear(0, 0, 0);
	}

	if(date1 > date2){ return 1; } // int
	if(date1 < date2){ return -1; } // int
	return 0; // int
};

date.add = function(/*Date*/date, /*String*/interval, /*int*/amount){
	// summary:
	//		Add to a Date in intervals of different size, from milliseconds to years
	// date: Date
	//		Date object to start with
	// interval:
	//		A string representing the interval.  One of the following:
	//		"year", "month", "day", "hour", "minute", "second",
	//		"millisecond", "quarter", "week", "weekday"
	// amount:
	//		How much to add to the date.

	var sum = new Date(+date); // convert to Number before copying to accommodate IE (#3112)
	var fixOvershoot = false;
	var property = "Date";

	switch(interval){
		case "day":
			break;
		case "weekday":
			//i18n FIXME: assumes Saturday/Sunday weekend, but this is not always true.  see dojo/cldr/supplemental

			// Divide the increment time span into weekspans plus leftover days
			// e.g., 8 days is one 5-day weekspan / and two leftover days
			// Can't have zero leftover days, so numbers divisible by 5 get
			// a days value of 5, and the remaining days make up the number of weeks
			var days, weeks;
			var mod = amount % 5;
			if(!mod){
				days = (amount > 0) ? 5 : -5;
				weeks = (amount > 0) ? ((amount-5)/5) : ((amount+5)/5);
			}else{
				days = mod;
				weeks = parseInt(amount/5);
			}
			// Get weekday value for orig date param
			var strt = date.getDay();
			// Orig date is Sat / positive incrementer
			// Jump over Sun
			var adj = 0;
			if(strt == 6 && amount > 0){
				adj = 1;
			}else if(strt == 0 && amount < 0){
			// Orig date is Sun / negative incrementer
			// Jump back over Sat
				adj = -1;
			}
			// Get weekday val for the new date
			var trgt = strt + days;
			// New date is on Sat or Sun
			if(trgt == 0 || trgt == 6){
				adj = (amount > 0) ? 2 : -2;
			}
			// Increment by number of weeks plus leftover days plus
			// weekend adjustments
			amount = (7 * weeks) + days + adj;
			break;
		case "year":
			property = "FullYear";
			// Keep increment/decrement from 2/29 out of March
			fixOvershoot = true;
			break;
		case "week":
			amount *= 7;
			break;
		case "quarter":
			// Naive quarter is just three months
			amount *= 3;
			// fallthrough...
		case "month":
			// Reset to last day of month if you overshoot
			fixOvershoot = true;
			property = "Month";
			break;
//		case "hour":
//		case "minute":
//		case "second":
//		case "millisecond":
		default:
			property = "UTC"+interval.charAt(0).toUpperCase() + interval.substring(1) + "s";
	}

	if(property){
		sum["set"+property](sum["get"+property]()+amount);
	}

	if(fixOvershoot && (sum.getDate() < date.getDate())){
		sum.setDate(0);
	}

	return sum; // Date
};

date.difference = function(/*Date*/date1, /*Date?*/date2, /*String?*/interval){
	// summary:
	//		Get the difference in a specific unit of time (e.g., number of
	//		months, weeks, days, etc.) between two dates, rounded to the
	//		nearest integer.
	// date1:
	//		Date object
	// date2:
	//		Date object.  If not specified, the current Date is used.
	// interval:
	//		A string representing the interval.  One of the following:
	//		"year", "month", "day", "hour", "minute", "second",
	//		"millisecond", "quarter", "week", "weekday"
	//
	//		Defaults to "day".

	date2 = date2 || new Date();
	interval = interval || "day";
	var yearDiff = date2.getFullYear() - date1.getFullYear();
	var delta = 1; // Integer return value

	switch(interval){
		case "quarter":
			var m1 = date1.getMonth();
			var m2 = date2.getMonth();
			// Figure out which quarter the months are in
			var q1 = Math.floor(m1/3) + 1;
			var q2 = Math.floor(m2/3) + 1;
			// Add quarters for any year difference between the dates
			q2 += (yearDiff * 4);
			delta = q2 - q1;
			break;
		case "weekday":
			var days = Math.round(date.difference(date1, date2, "day"));
			var weeks = parseInt(date.difference(date1, date2, "week"));
			var mod = days % 7;

			// Even number of weeks
			if(mod == 0){
				days = weeks*5;
			}else{
				// Weeks plus spare change (< 7 days)
				var adj = 0;
				var aDay = date1.getDay();
				var bDay = date2.getDay();

				weeks = parseInt(days/7);
				mod = days % 7;
				// Mark the date advanced by the number of
				// round weeks (may be zero)
				var dtMark = new Date(date1);
				dtMark.setDate(dtMark.getDate()+(weeks*7));
				var dayMark = dtMark.getDay();

				// Spare change days -- 6 or less
				if(days > 0){
					switch(true){
						// Range starts on Sat
						case aDay == 6:
							adj = -1;
							break;
						// Range starts on Sun
						case aDay == 0:
							adj = 0;
							break;
						// Range ends on Sat
						case bDay == 6:
							adj = -1;
							break;
						// Range ends on Sun
						case bDay == 0:
							adj = -2;
							break;
						// Range contains weekend
						case (dayMark + mod) > 5:
							adj = -2;
					}
				}else if(days < 0){
					switch(true){
						// Range starts on Sat
						case aDay == 6:
							adj = 0;
							break;
						// Range starts on Sun
						case aDay == 0:
							adj = 1;
							break;
						// Range ends on Sat
						case bDay == 6:
							adj = 2;
							break;
						// Range ends on Sun
						case bDay == 0:
							adj = 1;
							break;
						// Range contains weekend
						case (dayMark + mod) < 0:
							adj = 2;
					}
				}
				days += adj;
				days -= (weeks*2);
			}
			delta = days;
			break;
		case "year":
			delta = yearDiff;
			break;
		case "month":
			delta = (date2.getMonth() - date1.getMonth()) + (yearDiff * 12);
			break;
		case "week":
			// Truncate instead of rounding
			// Don't use Math.floor -- value may be negative
			delta = parseInt(date.difference(date1, date2, "day")/7);
			break;
		case "day":
			delta /= 24;
			// fallthrough
		case "hour":
			delta /= 60;
			// fallthrough
		case "minute":
			delta /= 60;
			// fallthrough
		case "second":
			delta /= 1000;
			// fallthrough
		case "millisecond":
			delta *= date2.getTime() - date1.getTime();
	}

	// Round for fractional values and DST leaps
	return Math.round(delta); // Number (integer)
};

// Don't use setObject() because it may overwrite dojo/date/stamp (if that has already been loaded)
 1  && lang.mixin(lang.getObject("dojo.date", true), date);

return date;
});

},
'curam/cdsl/request/CuramService':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 10-Oct-2014  MV  [CR00446578] Add support for development mode.
 * 26-Sep-2014  MV  [CR00445374] Implement generic data hookpoint.
 */

define(['dojo/_base/declare',
        'curam/cdsl/Struct',
        'curam/cdsl/_base/FacadeMethodResponse',
        'dojo/_base/lang',
        'curam/cdsl/_base/_Connection',
        'curam/cdsl/_base/FacadeMethodCall'
        ], function(
            declare, Struct, FacadeMethodResponse, lang) {

  var DEFAULT_OPTIONS = {
    dataAdapter: null
  },
    
  processOptions = function(options) {
    var o = lang.clone(DEFAULT_OPTIONS);
    o = lang.mixin(o, options);
    return o;
  },
  
  rule = '********************************************************',
    
  /**
   * @name curam.cdsl.request.CuramService
   * @namespace API for making requests to Curam facade layer from JavaScript.
   */
  CuramService = declare(null,
  /**
   * @lends curam.cdsl.request.CuramService.prototype
   */
  {
    /** The connection to use for accessing server. */
    _connection: null,
    
    _dataAdapter: null,
    
    /**
     * Instantiates a data service API object.
     * 
     * @param {curam/cdsl/_base/_Connection} connection The connection object
     *  to be used. Use an instance of curam/cdsl/connection/CuramConnection
     *  class.
     * @param {Object} [options] An object containing optional parameters
     *  for the service. The supported options are: dataAdapter - allows
     *  making custom modifications to the data passed through the CDSL API
     *  from and to the server. More information on this in
     *  curam/cdsl/request/FacadeMethodCall.
     */
    constructor: function(connection, opts) {
      var options = processOptions(opts);
      
      this._connection = connection;
      this._dataAdapter = options.dataAdapter;
    },

    /**
     * Makes a request to the Curam facade layer for the specified server
     * interface method calls.
     * Note that currently making multiple method calls within the same request
     * is not supported.
     *
     * @param {[curam/cdsl/_base/FacadeMethodCall]} methodCalls An array
     *     of facade method calls to make.
     * @param {Number} timeout Number of milliseconds to wait for the response
     *  before timeout is signalled (Error will be thrown).
     *
     * @returns {dojo/Promise::[curam/cdsl/Struct]} A promise for the array
     *  of Struct objects returned from the server call.
     */
    call: function(methodCalls, timeout) {
      // FIXME: add support for multiple method calls
      var methodCall = methodCalls[0];
      
      if (!methodCall.dataAdapter()) {
        methodCall.dataAdapter(this._dataAdapter);
      }
      
      var promise = this._connection.invoke(methodCall, timeout);

      return promise.then(lang.hitch(this, function(jsonString) {
        var response = new FacadeMethodResponse(
            methodCall, jsonString, this._connection.metadata());
        
        if (response.failed()) {
          var e = response.getError();
          
          // in dev mode report the error to the console.
          if (response.devMode()) {
            console.log(rule);
            console.log(e.toString());
            console.log(rule);
          }
          
          // and then throw to fail the request
          throw e;
        }

        // process and cache any metadata sent by server
        this._connection.updateMetadata(response);

        // FIXME: support results from multiple server calls
        return [ response.returnValue() ];
      }));
    }
  });
  
  return CuramService;
});

},
'curam/cdsl/util/FormatDateTime':function(){
/*
 * Copyright 2014 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 16-Oct-2014  SC [] Initial version.
 */

define(['dojo/_base/declare',
        'dojo/_base/lang',
        'dojo/date/locale',
        'dojo/date'
        ], function(
            declare, lang, locale, DDate) {

  /**
   * A utility API for formatting date, time and datetime objects.
   * 
   * @name curam.cdsl.util.FormatDateTime
   * @namespace The main entry point to getting preferences in CDSL.
   */
  /**
   * @lends curam.cdsl.util.FormatDateTime.prototype
   */
  return {
        
    /**
     * Verify that the preferences object provided has been initialized.
     * 
     * @param {String} dateFormat The date format.
     * @param {String} timeFormat The time format.
     * @param {String} timezoneOffset The timezone offset.
     */
    _checkPreferences: function(dateFormat, timeFormat, timezoneOffset) {
      if (dateFormat === undefined) {
        throw new Error("The dateFormat preference was not available");        
      }
      
      if (timeFormat === undefined) {
        throw new Error("The timeFormat preference was not available");        
      }
  
      if (timezoneOffset === undefined) {
        throw new Error("The timezone preference was not available");        
      }      
    },
 
    /**
     * Returns the JavaScript date object formatted according to 
     * user preferences date and timezone settings. The preferences
     * object should be initialized before calling this method.
     * Note: The format should never include the timezone, as this will
     * always match the browser timezone, which may not be correct. 
     * The formatted value will always be in the timezone of the user, as
     * specified in the user's preferences.
     * 
     * @param {Date} date The date value to be formatted.
     * @param {curam/cdsl/util/Preferences} preferences The preferences to use for formatting.
     * 
     * @returns {String} The formatted date time value, in the users timezone preference.
     */
    formatDateTime: function(date, preferences) {
      var dateFormat = preferences.getPreference('dateFormat');
      var timeFormat = preferences.getPreference('timeFormat');
      var timezoneOffset = preferences.getPreference('timezone');
      
      this._checkPreferences(dateFormat, timeFormat, timezoneOffset);

      // Get the browser offset, in milliseconds
      var browserOffset = date.getTimezoneOffset() * 60000;        
      
      // Add browser offset to local time to get UTC milliseconds
      var utcDateMilliseconds = Date.parse(date) + browserOffset;
      // Account for user preferences
      var userDate = new Date(utcDateMilliseconds - (timezoneOffset * 60000));
      // Return formatted value
      return locale.format(userDate, 
          {'datePattern':dateFormat, 'timePattern':timeFormat});      
    },
  
    /**
     * Returns the JavaScript date object formatted according to 
     * user preferences date settings. The preferences
     * object should be initialized before calling this method.
     * 
     * @param {Date} date The date value to be formatted.
     * @param {curam/cdsl/util/Preferences} preferences The preferences to use for formatting.
     * 
     * @returns {String} The formatted date value.
     */
    formatDate: function(date, preferences) {
      var dateFormat = preferences.getPreference('dateFormat');
      var timezoneOffset = preferences.getPreference('timezone');
      
      this._checkPreferences(dateFormat, 'ignore', timezoneOffset);

      // Get the browser offset, in milliseconds
      var browserOffset = date.getTimezoneOffset() * 60000;        
      
      // Add browser offset to local time to get UTC milliseconds
      var utcDateMilliseconds = Date.parse(date) + browserOffset;
      var userDate = new Date(utcDateMilliseconds);
      // Return formatted value
      return locale.format(userDate, 
          {'datePattern':dateFormat, 'selector':'date'});      
    },
    
    /**
     * Returns the JavaScript date object formatted according to 
     * user preferences time settings. The preferences
     * object should be initialized before calling this method.
     * Note: The format should never include the timezone, as this will
     * always match the browser timezone, which may not be correct. 
     * The formatted value will by default be the UTC value, but
     * an optional parameter (timezoneaware) can be used to format the time
     * according to the user preference. 
     * 
     * @param {Date} date The date value to be formatted.
     * @param {curam/cdsl/util/Preferences} preferences The preferences to use for formatting.
     * @param {Boolean} timezoneaware Boolean indicating if the time should be returned 
     * according to user preferences or UTC value.
     * 
     * @returns {String} The formatted time value, in the users timezone preference.
     */
     _formatTime: function(date, preferences, timezoneaware) {
      var timeFormat = preferences.getPreference('timeFormat');
      var timezoneOffset = preferences.getPreference('timezone');
      
      this._checkPreferences('ignore', timeFormat, timezoneOffset);

      var browserOffset = date.getTimezoneOffset() * 60000;        
      var utcDateMilliseconds = Date.parse(date) + browserOffset;
            
      if (timezoneaware != undefined && timezoneaware) {          
        var userDate = new Date(utcDateMilliseconds - (timezoneOffset * 60000));
        return locale.format(userDate, 
          {'timePattern':timeFormat, 'selector':'time'});      
      } else {
        return locale.format(new Date(utcDateMilliseconds), 
          {'timePattern':timeFormat, 'selector':'time'});      
      }
    }   
  
  };

});

},
'curam/cdsl/types/codetable/CodeTableItem':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(['dojo/_base/declare'
        ], function(
            declare) {

  /**
   * @name curam.cdsl.types.codetable.CodeTableItem
   * @namespace 
   */
  var CodeTableItem = declare(null,
  /**
   * @lends curam.cdsl.types.codetable.CodeTableItem.prototype
   */
  {
    _code: null,
    
    _desc: null,
    
    _isDefault: null,
    
    /**
     * Creates an instance of code table item.
     * 
     * @param {String} code The code.
     * @param {String} desc The description.
     */
    constructor: function(code, desc) {
      this._code = code;
      this._desc = desc;
      this._isDefault = false;
    },
    
    /**
     * Gets the code.
     * @returns {String} The code.
     */
    getCode: function() {
      return this._code;
    },
    
    /**
     * Gets the description.
     * 
     * @returns {String} The description.
     */
    getDescription: function() {
      return this._desc;
    },
    
    /**
     * Get or set the default flag on this codetable item.
     * @param {boolean} [isDeflt] Flag to specify whether this code table item
     *  is defaul or not.
     */
    isDefault: function(isDeflt) {
      if (typeof isDeflt === 'undefined') {
        return this._isDefault || false;

      } else {
        var oldVal = this._isDefault;
        this._isDefault = isDeflt;
        return oldVal;
      }
    }
  });
  
  return CodeTableItem;
});

},
'dijit/_WidgetBase':function(){
define([
	"require", // require.toUrl
	"dojo/_base/array", // array.forEach array.map
	"dojo/aspect",
	"dojo/_base/config", // config.blankGif
	"dojo/_base/connect", // connect.connect
	"dojo/_base/declare", // declare
	"dojo/dom", // dom.byId
	"dojo/dom-attr", // domAttr.set domAttr.remove
	"dojo/dom-class", // domClass.add domClass.replace
	"dojo/dom-construct", // domConstruct.destroy domConstruct.place
	"dojo/dom-geometry", // isBodyLtr
	"dojo/dom-style", // domStyle.set, domStyle.get
	"dojo/has",
	"dojo/_base/kernel",
	"dojo/_base/lang", // mixin(), isArray(), etc.
	"dojo/on",
	"dojo/ready",
	"dojo/Stateful", // Stateful
	"dojo/topic",
	"dojo/_base/window", // win.body()
	"./Destroyable",
	"dojo/has!dojo-bidi?./_BidiMixin",
	"./registry"    // registry.getUniqueId(), registry.findWidgets()
], function(require, array, aspect, config, connect, declare,
			dom, domAttr, domClass, domConstruct, domGeometry, domStyle, has, kernel,
			lang, on, ready, Stateful, topic, win, Destroyable, _BidiMixin, registry){

/* CURAM-FIX: Extend ContentPane to allow P&S instrumentation. */
var curamPerfTrackingEnabled = typeof(dojo.global.perf) != "undefined";

	// module:
	//		dijit/_WidgetBase

	// Flag to make dijit load modules the app didn't explicitly request, for backwards compatibility
	 1 || has.add("dijit-legacy-requires", !kernel.isAsync);

	// Flag to enable support for textdir attribute
	has.add("dojo-bidi", false);


	// For back-compat, remove in 2.0.
	if( 1 ){
		ready(0, function(){
			var requires = ["dijit/_base/manager"];
			require(requires);	// use indirection so modules not rolled into a build
		});
	}

	// Nested hash listing attributes for each tag, all strings in lowercase.
	// ex: {"div": {"style": true, "tabindex" true}, "form": { ...
	var tagAttrs = {};

	function getAttrs(obj){
		var ret = {};
		for(var attr in obj){
			ret[attr.toLowerCase()] = true;
		}
		return ret;
	}

	function nonEmptyAttrToDom(attr){
		// summary:
		//		Returns a setter function that copies the attribute to this.domNode,
		//		or removes the attribute from this.domNode, depending on whether the
		//		value is defined or not.
		return function(val){
			domAttr[val ? "set" : "remove"](this.domNode, attr, val);
			this._set(attr, val);
		};
	}

	function isEqual(a, b){
		//	summary:
		//		Function that determines whether two values are identical,
		//		taking into account that NaN is not normally equal to itself
		//		in JS.

		return a === b || (/* a is NaN */ a !== a && /* b is NaN */ b !== b);
	}

	var _WidgetBase = declare("dijit._WidgetBase", [Stateful, Destroyable], {
		// summary:
		//		Future base class for all Dijit widgets.
		// description:
		//		Future base class for all Dijit widgets.
		//		_Widget extends this class adding support for various features needed by desktop.
		//
		//		Provides stubs for widget lifecycle methods for subclasses to extend, like postMixInProperties(), buildRendering(),
		//		postCreate(), startup(), and destroy(), and also public API methods like set(), get(), and watch().
		//
		//		Widgets can provide custom setters/getters for widget attributes, which are called automatically by set(name, value).
		//		For an attribute XXX, define methods _setXXXAttr() and/or _getXXXAttr().
		//
		//		_setXXXAttr can also be a string/hash/array mapping from a widget attribute XXX to the widget's DOMNodes:
		//
		//		- DOM node attribute
		// |		_setFocusAttr: {node: "focusNode", type: "attribute"}
		// |		_setFocusAttr: "focusNode"	(shorthand)
		// |		_setFocusAttr: ""		(shorthand, maps to this.domNode)
		//		Maps this.focus to this.focusNode.focus, or (last example) this.domNode.focus
		//
		//		- DOM node innerHTML
		//	|		_setTitleAttr: { node: "titleNode", type: "innerHTML" }
		//		Maps this.title to this.titleNode.innerHTML
		//
		//		- DOM node innerText
		//	|		_setTitleAttr: { node: "titleNode", type: "innerText" }
		//		Maps this.title to this.titleNode.innerText
		//
		//		- DOM node CSS class
		// |		_setMyClassAttr: { node: "domNode", type: "class" }
		//		Maps this.myClass to this.domNode.className
		//
		//		If the value of _setXXXAttr is an array, then each element in the array matches one of the
		//		formats of the above list.
		//
		//		If the custom setter is null, no action is performed other than saving the new value
		//		in the widget (in this).
		//
		//		If no custom setter is defined for an attribute, then it will be copied
		//		to this.focusNode (if the widget defines a focusNode), or this.domNode otherwise.
		//		That's only done though for attributes that match DOMNode attributes (title,
		//		alt, aria-labelledby, etc.)

		// id: [const] String
		//		A unique, opaque ID string that can be assigned by users or by the
		//		system. If the developer passes an ID which is known not to be
		//		unique, the specified ID is ignored and the system-generated ID is
		//		used instead.
		id: "",
		_setIdAttr: "domNode", // to copy to this.domNode even for auto-generated id's

		// lang: [const] String
		//		Rarely used.  Overrides the default Dojo locale used to render this widget,
		//		as defined by the [HTML LANG](http://www.w3.org/TR/html401/struct/dirlang.html#adef-lang) attribute.
		//		Value must be among the list of locales specified during by the Dojo bootstrap,
		//		formatted according to [RFC 3066](http://www.ietf.org/rfc/rfc3066.txt) (like en-us).
		lang: "",
		// set on domNode even when there's a focus node.	but don't set lang="", since that's invalid.
		_setLangAttr: nonEmptyAttrToDom("lang"),

		// dir: [const] String
		//		Bi-directional support, as defined by the [HTML DIR](http://www.w3.org/TR/html401/struct/dirlang.html#adef-dir)
		//		attribute. Either left-to-right "ltr" or right-to-left "rtl".  If undefined, widgets renders in page's
		//		default direction.
		dir: "",
		// set on domNode even when there's a focus node.	but don't set dir="", since that's invalid.
		_setDirAttr: nonEmptyAttrToDom("dir"), // to set on domNode even when there's a focus node

		// class: String
		//		HTML class attribute
		"class": "",
		_setClassAttr: { node: "domNode", type: "class" },

		// Override automatic assigning type --> focusNode, it causes exception on IE6-8.
		// Instead, type must be specified as ${type} in the template, as part of the original DOM.
		_setTypeAttr: null,
		// style: String||Object
		//		HTML style attributes as cssText string or name/value hash
		style: "",

		// title: String
		//		HTML title attribute.
		//
		//		For form widgets this specifies a tooltip to display when hovering over
		//		the widget (just like the native HTML title attribute).
		//
		//		For TitlePane or for when this widget is a child of a TabContainer, AccordionContainer,
		//		etc., it's used to specify the tab label, accordion pane title, etc.  In this case it's
		//		interpreted as HTML.
		title: "",

		// tooltip: String
		//		When this widget's title attribute is used to for a tab label, accordion pane title, etc.,
		//		this specifies the tooltip to appear when the mouse is hovered over that text.
		tooltip: "",

		// baseClass: [protected] String
		//		Root CSS class of the widget (ex: dijitTextBox), used to construct CSS classes to indicate
		//		widget state.
		baseClass: "",

		// srcNodeRef: [readonly] DomNode
		//		pointer to original DOM node
		srcNodeRef: null,

		// domNode: [readonly] DomNode
		//		This is our visible representation of the widget! Other DOM
		//		Nodes may by assigned to other properties, usually through the
		//		template system's data-dojo-attach-point syntax, but the domNode
		//		property is the canonical "top level" node in widget UI.
		domNode: null,

		// containerNode: [readonly] DomNode
		//		Designates where children of the source DOM node will be placed.
		//		"Children" in this case refers to both DOM nodes and widgets.
		//		For example, for myWidget:
		//
		//		|	<div data-dojo-type=myWidget>
		//		|		<b> here's a plain DOM node
		//		|		<span data-dojo-type=subWidget>and a widget</span>
		//		|		<i> and another plain DOM node </i>
		//		|	</div>
		//
		//		containerNode would point to:
		//
		//		|		<b> here's a plain DOM node
		//		|		<span data-dojo-type=subWidget>and a widget</span>
		//		|		<i> and another plain DOM node </i>
		//
		//		In templated widgets, "containerNode" is set via a
		//		data-dojo-attach-point assignment.
		//
		//		containerNode must be defined for any widget that accepts innerHTML
		//		(like ContentPane or BorderContainer or even Button), and conversely
		//		is null for widgets that don't, like TextBox.
		containerNode: null,

		// ownerDocument: [const] Document?
		//		The document this widget belongs to.  If not specified to constructor, will default to
		//		srcNodeRef.ownerDocument, or if no sourceRef specified, then to the document global
		ownerDocument: null,
		_setOwnerDocumentAttr: function(val){
			// this setter is merely to avoid automatically trying to set this.domNode.ownerDocument
			this._set("ownerDocument", val);
		},

		/*=====
		// _started: [readonly] Boolean
		//		startup() has completed.
		_started: false,
		=====*/

		// attributeMap: [protected] Object
		//		Deprecated.	Instead of attributeMap, widget should have a _setXXXAttr attribute
		//		for each XXX attribute to be mapped to the DOM.
		//
		//		attributeMap sets up a "binding" between attributes (aka properties)
		//		of the widget and the widget's DOM.
		//		Changes to widget attributes listed in attributeMap will be
		//		reflected into the DOM.
		//
		//		For example, calling set('title', 'hello')
		//		on a TitlePane will automatically cause the TitlePane's DOM to update
		//		with the new title.
		//
		//		attributeMap is a hash where the key is an attribute of the widget,
		//		and the value reflects a binding to a:
		//
		//		- DOM node attribute
		// |		focus: {node: "focusNode", type: "attribute"}
		//		Maps this.focus to this.focusNode.focus
		//
		//		- DOM node innerHTML
		//	|		title: { node: "titleNode", type: "innerHTML" }
		//		Maps this.title to this.titleNode.innerHTML
		//
		//		- DOM node innerText
		//	|		title: { node: "titleNode", type: "innerText" }
		//		Maps this.title to this.titleNode.innerText
		//
		//		- DOM node CSS class
		// |		myClass: { node: "domNode", type: "class" }
		//		Maps this.myClass to this.domNode.className
		//
		//		If the value is an array, then each element in the array matches one of the
		//		formats of the above list.
		//
		//		There are also some shorthands for backwards compatibility:
		//
		//		- string --> { node: string, type: "attribute" }, for example:
		//
		//	|	"focusNode" ---> { node: "focusNode", type: "attribute" }
		//
		//		- "" --> { node: "domNode", type: "attribute" }
		attributeMap: {},

		// _blankGif: [protected] String
		//		Path to a blank 1x1 image.
		//		Used by `<img>` nodes in templates that really get their image via CSS background-image.
		_blankGif: config.blankGif || require.toUrl("dojo/resources/blank.gif"),

		//////////// INITIALIZATION METHODS ///////////////////////////////////////

		/*=====
		constructor: function(params, srcNodeRef){
			// summary:
			//		Create the widget.
			// params: Object|null
			//		Hash of initialization parameters for widget, including scalar values (like title, duration etc.)
			//		and functions, typically callbacks like onClick.
			//		The hash can contain any of the widget's properties, excluding read-only properties.
			// srcNodeRef: DOMNode|String?
			//		If a srcNodeRef (DOM node) is specified:
			//
			//		- use srcNodeRef.innerHTML as my contents
			//		- if this is a behavioral widget then apply behavior to that srcNodeRef
			//		- otherwise, replace srcNodeRef with my generated DOM tree
		},
		=====*/

		_introspect: function(){
			// summary:
			//		Collect metadata about this widget (only once per class, not once per instance):
			//
			//			- list of attributes with custom setters, storing in this.constructor._setterAttrs
			//			- generate this.constructor._onMap, mapping names like "mousedown" to functions like onMouseDown

			var ctor = this.constructor;
			if(!ctor._setterAttrs){
				var proto = ctor.prototype,
					attrs = ctor._setterAttrs = [], // attributes with custom setters
					onMap = (ctor._onMap = {});

				// Items in this.attributeMap are like custom setters.  For back-compat, remove for 2.0.
				for(var name in proto.attributeMap){
					attrs.push(name);
				}

				// Loop over widget properties, collecting properties with custom setters and filling in ctor._onMap.
				for(name in proto){
					if(/^on/.test(name)){
						onMap[name.substring(2).toLowerCase()] = name;
					}

					if(/^_set[A-Z](.*)Attr$/.test(name)){
						name = name.charAt(4).toLowerCase() + name.substr(5, name.length - 9);
						if(!proto.attributeMap || !(name in proto.attributeMap)){
							attrs.push(name);
						}
					}
				}

				// Note: this isn't picking up info on properties like aria-label and role, that don't have custom setters
				// but that set() maps to attributes on this.domNode or this.focusNode
			}
		},

		postscript: function(/*Object?*/params, /*DomNode|String*/srcNodeRef){
			// summary:
			//		Kicks off widget instantiation.  See create() for details.
			// tags:
			//		private

			// Note that we skip calling this.inherited(), i.e. dojo/Stateful::postscript(), because 1.x widgets don't
			// expect their custom setters to get called until after buildRendering().  Consider changing for 2.0.

			this.create(params, srcNodeRef);
		},

		create: function(params, srcNodeRef){
			// summary:
			//		Kick off the life-cycle of a widget
			// description:
			//		Create calls a number of widget methods (postMixInProperties, buildRendering, postCreate,
			//		etc.), some of which of you'll want to override. See http://dojotoolkit.org/reference-guide/dijit/_WidgetBase.html
			//		for a discussion of the widget creation lifecycle.
			//
			//		Of course, adventurous developers could override create entirely, but this should
			//		only be done as a last resort.
			// params: Object|null
			//		Hash of initialization parameters for widget, including scalar values (like title, duration etc.)
			//		and functions, typically callbacks like onClick.
			//		The hash can contain any of the widget's properties, excluding read-only properties.
			// srcNodeRef: DOMNode|String?
			//		If a srcNodeRef (DOM node) is specified:
			//
			//		- use srcNodeRef.innerHTML as my contents
			//		- if this is a behavioral widget then apply behavior to that srcNodeRef
			//		- otherwise, replace srcNodeRef with my generated DOM tree
			// tags:
			//		private

                /* CURAM-FIX: P&S */
                if(curamPerfTrackingEnabled) {
                        perf.widgetStartedLoadingCallback();
                }
                /* END CURAM-FIX */

			// First time widget is instantiated, scan prototype to figure out info about custom setters etc.
			this._introspect();

			// store pointer to original DOM tree
			this.srcNodeRef = dom.byId(srcNodeRef);

			// No longer used, remove for 2.0.
			this._connects = [];
			this._supportingWidgets = [];

			// this is here for back-compat, remove in 2.0 (but check NodeList-instantiate.html test)
			if(this.srcNodeRef && (typeof this.srcNodeRef.id == "string")){
				this.id = this.srcNodeRef.id;
			}

			// mix in our passed parameters
			if(params){
				this.params = params;
				lang.mixin(this, params);
			}
			this.postMixInProperties();

			// Generate an id for the widget if one wasn't specified, or it was specified as id: undefined.
			// Do this before buildRendering() because it might expect the id to be there.
			if(!this.id){
				this.id = registry.getUniqueId(this.declaredClass.replace(/\./g, "_"));
				if(this.params){
					// if params contains {id: undefined}, prevent _applyAttributes() from processing it
					delete this.params.id;
				}
			}

			// The document and <body> node this widget is associated with
			this.ownerDocument = this.ownerDocument || (this.srcNodeRef ? this.srcNodeRef.ownerDocument : document);
			this.ownerDocumentBody = win.body(this.ownerDocument);

			registry.add(this);

			this.buildRendering();

			var deleteSrcNodeRef;

			if(this.domNode){
				// Copy attributes listed in attributeMap into the [newly created] DOM for the widget.
				// Also calls custom setters for all attributes with custom setters.
				this._applyAttributes();

				// If srcNodeRef was specified, then swap out original srcNode for this widget's DOM tree.
				// For 2.0, move this after postCreate().  postCreate() shouldn't depend on the
				// widget being attached to the DOM since it isn't when a widget is created programmatically like
				// new MyWidget({}).	See #11635.
				var source = this.srcNodeRef;
				if(source && source.parentNode && this.domNode !== source){
					source.parentNode.replaceChild(this.domNode, source);
					deleteSrcNodeRef = true;
				}

				// Note: for 2.0 may want to rename widgetId to dojo._scopeName + "_widgetId",
				// assuming that dojo._scopeName even exists in 2.0
				this.domNode.setAttribute("widgetId", this.id);
			}
			this.postCreate();

			// If srcNodeRef has been processed and removed from the DOM (e.g. TemplatedWidget) then delete it to allow GC.
			// I think for back-compatibility it isn't deleting srcNodeRef until after postCreate() has run.
			if(deleteSrcNodeRef){
				delete this.srcNodeRef;
			}

			this._created = true;

                /* CURAM-FIX: P&S */
                if(curamPerfTrackingEnabled) {
                        perf.widgetLoadedCallback(this);
                }
                /* END CURAM-FIX */
			
		},

		_applyAttributes: function(){
			// summary:
			//		Step during widget creation to copy  widget attributes to the
			//		DOM according to attributeMap and _setXXXAttr objects, and also to call
			//		custom _setXXXAttr() methods.
			//
			//		Skips over blank/false attribute values, unless they were explicitly specified
			//		as parameters to the widget, since those are the default anyway,
			//		and setting tabIndex="" is different than not setting tabIndex at all.
			//
			//		For backwards-compatibility reasons attributeMap overrides _setXXXAttr when
			//		_setXXXAttr is a hash/string/array, but _setXXXAttr as a functions override attributeMap.
			// tags:
			//		private

			// Call this.set() for each property that was either specified as parameter to constructor,
			// or is in the list found above.	For correlated properties like value and displayedValue, the one
			// specified as a parameter should take precedence.
			// Particularly important for new DateTextBox({displayedValue: ...}) since DateTextBox's default value is
			// NaN and thus is not ignored like a default value of "".

			// Step 1: Save the current values of the widget properties that were specified as parameters to the constructor.
			// Generally this.foo == this.params.foo, except if postMixInProperties() changed the value of this.foo.
			var params = {};
			for(var key in this.params || {}){
				params[key] = this._get(key);
			}

			// Step 2: Call set() for each property with a non-falsy value that wasn't passed as a parameter to the constructor
			array.forEach(this.constructor._setterAttrs, function(key){
				if(!(key in params)){
					var val = this._get(key);
					if(val){
						this.set(key, val);
					}
				}
			}, this);

			// Step 3: Call set() for each property that was specified as parameter to constructor.
			// Use params hash created above to ignore side effects from step #2 above.
			for(key in params){
				this.set(key, params[key]);
			}
		},

		postMixInProperties: function(){
			// summary:
			//		Called after the parameters to the widget have been read-in,
			//		but before the widget template is instantiated. Especially
			//		useful to set properties that are referenced in the widget
			//		template.
			// tags:
			//		protected
		},

		buildRendering: function(){
			// summary:
			//		Construct the UI for this widget, setting this.domNode.
			//		Most widgets will mixin `dijit._TemplatedMixin`, which implements this method.
			// tags:
			//		protected

			if(!this.domNode){
				// Create root node if it wasn't created by _TemplatedMixin
				this.domNode = this.srcNodeRef || this.ownerDocument.createElement("div");
			}

			// baseClass is a single class name or occasionally a space-separated list of names.
			// Add those classes to the DOMNode.  If RTL mode then also add with Rtl suffix.
			// TODO: make baseClass custom setter
			if(this.baseClass){
				var classes = this.baseClass.split(" ");
				if(!this.isLeftToRight()){
					classes = classes.concat(array.map(classes, function(name){
						return name + "Rtl";
					}));
				}
				domClass.add(this.domNode, classes);
			}
		},

		postCreate: function(){
			// summary:
			//		Processing after the DOM fragment is created
			// description:
			//		Called after the DOM fragment has been created, but not necessarily
			//		added to the document.  Do not include any operations which rely on
			//		node dimensions or placement.
			// tags:
			//		protected
		},

		startup: function(){
			// summary:
			//		Processing after the DOM fragment is added to the document
			// description:
			//		Called after a widget and its children have been created and added to the page,
			//		and all related widgets have finished their create() cycle, up through postCreate().
			//
			//		Note that startup() may be called while the widget is still hidden, for example if the widget is
			//		inside a hidden dijit/Dialog or an unselected tab of a dijit/layout/TabContainer.
			//		For widgets that need to do layout, it's best to put that layout code inside resize(), and then
			//		extend dijit/layout/_LayoutWidget so that resize() is called when the widget is visible.
			if(this._started){
				return;
			}
			this._started = true;
			array.forEach(this.getChildren(), function(obj){
				if(!obj._started && !obj._destroyed && lang.isFunction(obj.startup)){
					obj.startup();
					obj._started = true;
				}
			});
		},

		//////////// DESTROY FUNCTIONS ////////////////////////////////

		destroyRecursive: function(/*Boolean?*/ preserveDom){
			// summary:
			//		Destroy this widget and its descendants
			// description:
			//		This is the generic "destructor" function that all widget users
			//		should call to cleanly discard with a widget. Once a widget is
			//		destroyed, it is removed from the manager object.
			// preserveDom:
			//		If true, this method will leave the original DOM structure
			//		alone of descendant Widgets. Note: This will NOT work with
			//		dijit._TemplatedMixin widgets.

			this._beingDestroyed = true;
			this.destroyDescendants(preserveDom);
			this.destroy(preserveDom);
		},

		destroy: function(/*Boolean*/ preserveDom){
			// summary:
			//		Destroy this widget, but not its descendants.  Descendants means widgets inside of
			//		this.containerNode.   Will also destroy any resources (including widgets) registered via this.own().
			//
			//		This method will also destroy internal widgets such as those created from a template,
			//		assuming those widgets exist inside of this.domNode but outside of this.containerNode.
			//
			//		For 2.0 it's planned that this method will also destroy descendant widgets, so apps should not
			//		depend on the current ability to destroy a widget without destroying its descendants.   Generally
			//		they should use destroyRecursive() for widgets with children.
			// preserveDom: Boolean
			//		If true, this method will leave the original DOM structure alone.
			//		Note: This will not yet work with _TemplatedMixin widgets

			this._beingDestroyed = true;
			this.uninitialize();

			function destroy(w){
				if(w.destroyRecursive){
					w.destroyRecursive(preserveDom);
				}else if(w.destroy){
					w.destroy(preserveDom);
				}
			}

			// Back-compat, remove for 2.0
			array.forEach(this._connects, lang.hitch(this, "disconnect"));
			array.forEach(this._supportingWidgets, destroy);

			// Destroy supporting widgets, but not child widgets under this.containerNode (for 2.0, destroy child widgets
			// here too).   if() statement is to guard against exception if destroy() called multiple times (see #15815).
			if(this.domNode){
				array.forEach(registry.findWidgets(this.domNode, this.containerNode), destroy);
			}

			this.destroyRendering(preserveDom);
			registry.remove(this.id);
			this._destroyed = true;
		},

		destroyRendering: function(/*Boolean?*/ preserveDom){
			// summary:
			//		Destroys the DOM nodes associated with this widget.
			// preserveDom:
			//		If true, this method will leave the original DOM structure alone
			//		during tear-down. Note: this will not work with _Templated
			//		widgets yet.
			// tags:
			//		protected

			if(this.bgIframe){
				this.bgIframe.destroy(preserveDom);
				delete this.bgIframe;
			}

			if(this.domNode){
				if(preserveDom){
					domAttr.remove(this.domNode, "widgetId");
				}else{
					domConstruct.destroy(this.domNode);
				}
				delete this.domNode;
			}

			if(this.srcNodeRef){
				if(!preserveDom){
					domConstruct.destroy(this.srcNodeRef);
				}
				delete this.srcNodeRef;
			}
		},

		destroyDescendants: function(/*Boolean?*/ preserveDom){
			// summary:
			//		Recursively destroy the children of this widget and their
			//		descendants.
			// preserveDom:
			//		If true, the preserveDom attribute is passed to all descendant
			//		widget's .destroy() method. Not for use with _Templated
			//		widgets.

			// get all direct descendants and destroy them recursively
			array.forEach(this.getChildren(), function(widget){
				if(widget.destroyRecursive){
					widget.destroyRecursive(preserveDom);
				}
			});
		},

		uninitialize: function(){
			// summary:
			//		Deprecated. Override destroy() instead to implement custom widget tear-down
			//		behavior.
			// tags:
			//		protected
			return false;
		},

		////////////////// GET/SET, CUSTOM SETTERS, ETC. ///////////////////

		_setStyleAttr: function(/*String||Object*/ value){
			// summary:
			//		Sets the style attribute of the widget according to value,
			//		which is either a hash like {height: "5px", width: "3px"}
			//		or a plain string
			// description:
			//		Determines which node to set the style on based on style setting
			//		in attributeMap.
			// tags:
			//		protected

			var mapNode = this.domNode;

			// Note: technically we should revert any style setting made in a previous call
			// to his method, but that's difficult to keep track of.

			if(lang.isObject(value)){
				domStyle.set(mapNode, value);
			}else{
				if(mapNode.style.cssText){
					mapNode.style.cssText += "; " + value;
				}else{
					mapNode.style.cssText = value;
				}
			}

			this._set("style", value);
		},

		_attrToDom: function(/*String*/ attr, /*String*/ value, /*Object?*/ commands){
			// summary:
			//		Reflect a widget attribute (title, tabIndex, duration etc.) to
			//		the widget DOM, as specified by commands parameter.
			//		If commands isn't specified then it's looked up from attributeMap.
			//		Note some attributes like "type"
			//		cannot be processed this way as they are not mutable.
			// attr:
			//		Name of member variable (ex: "focusNode" maps to this.focusNode) pointing
			//		to DOMNode inside the widget, or alternately pointing to a subwidget
			// tags:
			//		private

			commands = arguments.length >= 3 ? commands : this.attributeMap[attr];

			array.forEach(lang.isArray(commands) ? commands : [commands], function(command){

				// Get target node and what we are doing to that node
				var mapNode = this[command.node || command || "domNode"];	// DOM node
				var type = command.type || "attribute";	// class, innerHTML, innerText, or attribute

				switch(type){
					case "attribute":
						if(lang.isFunction(value)){ // functions execute in the context of the widget
							value = lang.hitch(this, value);
						}

						// Get the name of the DOM node attribute; usually it's the same
						// as the name of the attribute in the widget (attr), but can be overridden.
						// Also maps handler names to lowercase, like onSubmit --> onsubmit
						var attrName = command.attribute ? command.attribute :
							(/^on[A-Z][a-zA-Z]*$/.test(attr) ? attr.toLowerCase() : attr);

						if(mapNode.tagName){
							// Normal case, mapping to a DOMNode.  Note that modern browsers will have a mapNode.set()
							// method, but for consistency we still call domAttr
							domAttr.set(mapNode, attrName, value);
						}else{
							// mapping to a sub-widget
							mapNode.set(attrName, value);
						}
						break;
					case "innerText":
						mapNode.innerHTML = "";
						mapNode.appendChild(this.ownerDocument.createTextNode(value));
						break;
					case "innerHTML":
						mapNode.innerHTML = value;
						break;
					case "class":
						domClass.replace(mapNode, value, this[attr]);
						break;
				}
			}, this);
		},

		get: function(name){
			// summary:
			//		Get a property from a widget.
			// name:
			//		The property to get.
			// description:
			//		Get a named property from a widget. The property may
			//		potentially be retrieved via a getter method. If no getter is defined, this
			//		just retrieves the object's property.
			//
			//		For example, if the widget has properties `foo` and `bar`
			//		and a method named `_getFooAttr()`, calling:
			//		`myWidget.get("foo")` would be equivalent to calling
			//		`widget._getFooAttr()` and `myWidget.get("bar")`
			//		would be equivalent to the expression
			//		`widget.bar2`
			var names = this._getAttrNames(name);
			return this[names.g] ? this[names.g]() : this._get(name);
		},

		set: function(name, value){
			// summary:
			//		Set a property on a widget
			// name:
			//		The property to set.
			// value:
			//		The value to set in the property.
			// description:
			//		Sets named properties on a widget which may potentially be handled by a
			//		setter in the widget.
			//
			//		For example, if the widget has properties `foo` and `bar`
			//		and a method named `_setFooAttr()`, calling
			//		`myWidget.set("foo", "Howdy!")` would be equivalent to calling
			//		`widget._setFooAttr("Howdy!")` and `myWidget.set("bar", 3)`
			//		would be equivalent to the statement `widget.bar = 3;`
			//
			//		set() may also be called with a hash of name/value pairs, ex:
			//
			//	|	myWidget.set({
			//	|		foo: "Howdy",
			//	|		bar: 3
			//	|	});
			//
			//	This is equivalent to calling `set(foo, "Howdy")` and `set(bar, 3)`

			if(typeof name === "object"){
				for(var x in name){
					this.set(x, name[x]);
				}
				return this;
			}
			var names = this._getAttrNames(name),
				setter = this[names.s];
			if(lang.isFunction(setter)){
				// use the explicit setter
				var result = setter.apply(this, Array.prototype.slice.call(arguments, 1));
			}else{
				// Mapping from widget attribute to DOMNode/subwidget attribute/value/etc.
				// Map according to:
				//		1. attributeMap setting, if one exists (TODO: attributeMap deprecated, remove in 2.0)
				//		2. _setFooAttr: {...} type attribute in the widget (if one exists)
				//		3. apply to focusNode or domNode if standard attribute name, excluding funcs like onClick.
				// Checks if an attribute is a "standard attribute" by whether the DOMNode JS object has a similar
				// attribute name (ex: accept-charset attribute matches jsObject.acceptCharset).
				// Note also that Tree.focusNode() is a function not a DOMNode, so test for that.
				var defaultNode = this.focusNode && !lang.isFunction(this.focusNode) ? "focusNode" : "domNode",
					tag = this[defaultNode] && this[defaultNode].tagName,
					attrsForTag = tag && (tagAttrs[tag] || (tagAttrs[tag] = getAttrs(this[defaultNode]))),
					map = name in this.attributeMap ? this.attributeMap[name] :
						names.s in this ? this[names.s] :
							((attrsForTag && names.l in attrsForTag && typeof value != "function") ||
								/^aria-|^data-|^role$/.test(name)) ? defaultNode : null;
				if(map != null){
					this._attrToDom(name, value, map);
				}
				this._set(name, value);
			}
			return result || this;
		},

		_attrPairNames: {}, // shared between all widgets
		_getAttrNames: function(name){
			// summary:
			//		Helper function for get() and set().
			//		Caches attribute name values so we don't do the string ops every time.
			// tags:
			//		private

			var apn = this._attrPairNames;
			if(apn[name]){
				return apn[name];
			}
			var uc = name.replace(/^[a-z]|-[a-zA-Z]/g, function(c){
				return c.charAt(c.length - 1).toUpperCase();
			});
			return (apn[name] = {
				n: name + "Node",
				s: "_set" + uc + "Attr", // converts dashes to camel case, ex: accept-charset --> _setAcceptCharsetAttr
				g: "_get" + uc + "Attr",
				l: uc.toLowerCase()        // lowercase name w/out dashes, ex: acceptcharset
			});
		},

		_set: function(/*String*/ name, /*anything*/ value){
			// summary:
			//		Helper function to set new value for specified property, and call handlers
			//		registered with watch() if the value has changed.
			var oldValue = this[name];
			this[name] = value;
			if(this._created && !isEqual(oldValue, value)){
				if(this._watchCallbacks){
					this._watchCallbacks(name, oldValue, value);
				}
				this.emit("attrmodified-" + name, {
					detail: {
						prevValue: oldValue,
						newValue: value
					}
				});
			}
		},

		_get: function(/*String*/ name){
			// summary:
			//		Helper function to get value for specified property stored by this._set(),
			//		i.e. for properties with custom setters.  Used mainly by custom getters.
			//
			//		For example, CheckBox._getValueAttr() calls this._get("value").

			// future: return name in this.props ? this.props[name] : this[name];
			return this[name];
		},

		emit: function(/*String*/ type, /*Object?*/ eventObj, /*Array?*/ callbackArgs){
			// summary:
			//		Used by widgets to signal that a synthetic event occurred, ex:
			//	|	myWidget.emit("attrmodified-selectedChildWidget", {}).
			//
			//		Emits an event on this.domNode named type.toLowerCase(), based on eventObj.
			//		Also calls onType() method, if present, and returns value from that method.
			//		By default passes eventObj to callback, but will pass callbackArgs instead, if specified.
			//		Modifies eventObj by adding missing parameters (bubbles, cancelable, widget).
			// tags:
			//		protected

			// Specify fallback values for bubbles, cancelable in case they are not set in eventObj.
			// Also set pointer to widget, although since we can't add a pointer to the widget for native events
			// (see #14729), maybe we shouldn't do it here?
			eventObj = eventObj || {};
			if(eventObj.bubbles === undefined){
				eventObj.bubbles = true;
			}
			if(eventObj.cancelable === undefined){
				eventObj.cancelable = true;
			}
			if(!eventObj.detail){
				eventObj.detail = {};
			}
			eventObj.detail.widget = this;

			var ret, callback = this["on" + type];
			if(callback){
				ret = callback.apply(this, callbackArgs ? callbackArgs : [eventObj]);
			}

			// Emit event, but avoid spurious emit()'s as parent sets properties on child during startup/destroy
			if(this._started && !this._beingDestroyed){
				on.emit(this.domNode, type.toLowerCase(), eventObj);
			}

			return ret;
		},

		on: function(/*String|Function*/ type, /*Function*/ func){
			// summary:
			//		Call specified function when event occurs, ex: myWidget.on("click", function(){ ... }).
			// type:
			//		Name of event (ex: "click") or extension event like touch.press.
			// description:
			//		Call specified function when event `type` occurs, ex: `myWidget.on("click", function(){ ... })`.
			//		Note that the function is not run in any particular scope, so if (for example) you want it to run in the
			//		widget's scope you must do `myWidget.on("click", lang.hitch(myWidget, func))`.

			// For backwards compatibility, if there's an onType() method in the widget then connect to that.
			// Remove in 2.0.
			var widgetMethod = this._onMap(type);
			if(widgetMethod){
				return aspect.after(this, widgetMethod, func, true);
			}

			// Otherwise, just listen for the event on this.domNode.
			return this.own(on(this.domNode, type, func))[0];
		},

		_onMap: function(/*String|Function*/ type){
			// summary:
			//		Maps on() type parameter (ex: "mousemove") to method name (ex: "onMouseMove").
			//		If type is a synthetic event like touch.press then returns undefined.
			var ctor = this.constructor, map = ctor._onMap;
			if(!map){
				map = (ctor._onMap = {});
				for(var attr in ctor.prototype){
					if(/^on/.test(attr)){
						map[attr.replace(/^on/, "").toLowerCase()] = attr;
					}
				}
			}
			return map[typeof type == "string" && type.toLowerCase()];	// String
		},

		toString: function(){
			// summary:
			//		Returns a string that represents the widget.
			// description:
			//		When a widget is cast to a string, this method will be used to generate the
			//		output. Currently, it does not implement any sort of reversible
			//		serialization.
			return '[Widget ' + this.declaredClass + ', ' + (this.id || 'NO ID') + ']'; // String
		},

		getChildren: function(){
			// summary:
			//		Returns all direct children of this widget, i.e. all widgets underneath this.containerNode whose parent
			//		is this widget.   Note that it does not return all descendants, but rather just direct children.
			//		Analogous to [Node.childNodes](https://developer.mozilla.org/en-US/docs/DOM/Node.childNodes),
			//		except containing widgets rather than DOMNodes.
			//
			//		The result intentionally excludes internally created widgets (a.k.a. supporting widgets)
			//		outside of this.containerNode.
			//
			//		Note that the array returned is a simple array.  Application code should not assume
			//		existence of methods like forEach().

			return this.containerNode ? registry.findWidgets(this.containerNode) : []; // dijit/_WidgetBase[]
		},

		getParent: function(){
			// summary:
			//		Returns the parent widget of this widget.

			return registry.getEnclosingWidget(this.domNode.parentNode);
		},

		connect: function(/*Object|null*/ obj, /*String|Function*/ event, /*String|Function*/ method){
			// summary:
			//		Deprecated, will be removed in 2.0, use this.own(on(...)) or this.own(aspect.after(...)) instead.
			//
			//		Connects specified obj/event to specified method of this object
			//		and registers for disconnect() on widget destroy.
			//
			//		Provide widget-specific analog to dojo.connect, except with the
			//		implicit use of this widget as the target object.
			//		Events connected with `this.connect` are disconnected upon
			//		destruction.
			// returns:
			//		A handle that can be passed to `disconnect` in order to disconnect before
			//		the widget is destroyed.
			// example:
			//	|	var btn = new Button();
			//	|	// when foo.bar() is called, call the listener we're going to
			//	|	// provide in the scope of btn
			//	|	btn.connect(foo, "bar", function(){
			//	|		console.debug(this.toString());
			//	|	});
			// tags:
			//		protected

			return this.own(connect.connect(obj, event, this, method))[0];	// handle
		},

		disconnect: function(handle){
			// summary:
			//		Deprecated, will be removed in 2.0, use handle.remove() instead.
			//
			//		Disconnects handle created by `connect`.
			// tags:
			//		protected

			handle.remove();
		},

		subscribe: function(t, method){
			// summary:
			//		Deprecated, will be removed in 2.0, use this.own(topic.subscribe()) instead.
			//
			//		Subscribes to the specified topic and calls the specified method
			//		of this object and registers for unsubscribe() on widget destroy.
			//
			//		Provide widget-specific analog to dojo.subscribe, except with the
			//		implicit use of this widget as the target object.
			// t: String
			//		The topic
			// method: Function
			//		The callback
			// example:
			//	|	var btn = new Button();
			//	|	// when /my/topic is published, this button changes its label to
			//	|	// be the parameter of the topic.
			//	|	btn.subscribe("/my/topic", function(v){
			//	|		this.set("label", v);
			//	|	});
			// tags:
			//		protected
			return this.own(topic.subscribe(t, lang.hitch(this, method)))[0];	// handle
		},

		unsubscribe: function(/*Object*/ handle){
			// summary:
			//		Deprecated, will be removed in 2.0, use handle.remove() instead.
			//
			//		Unsubscribes handle created by this.subscribe.
			//		Also removes handle from this widget's list of subscriptions
			// tags:
			//		protected

			handle.remove();
		},

		isLeftToRight: function(){
			// summary:
			//		Return this widget's explicit or implicit orientation (true for LTR, false for RTL)
			// tags:
			//		protected
			return this.dir ? (this.dir.toLowerCase() == "ltr") : domGeometry.isBodyLtr(this.ownerDocument); //Boolean
		},

		isFocusable: function(){
			// summary:
			//		Return true if this widget can currently be focused
			//		and false if not
			return this.focus && (domStyle.get(this.domNode, "display") != "none");
		},

		placeAt: function(/*String|DomNode|DocumentFragment|dijit/_WidgetBase*/ reference, /*String|Int?*/ position){
			// summary:
			//		Place this widget somewhere in the DOM based
			//		on standard domConstruct.place() conventions.
			// description:
			//		A convenience function provided in all _Widgets, providing a simple
			//		shorthand mechanism to put an existing (or newly created) Widget
			//		somewhere in the dom, and allow chaining.
			// reference:
			//		Widget, DOMNode, DocumentFragment, or id of widget or DOMNode
			// position:
			//		If reference is a widget (or id of widget), and that widget has an ".addChild" method,
			//		it will be called passing this widget instance into that method, supplying the optional
			//		position index passed.  In this case position (if specified) should be an integer.
			//
			//		If reference is a DOMNode (or id matching a DOMNode but not a widget),
			//		the position argument can be a numeric index or a string
			//		"first", "last", "before", or "after", same as dojo/dom-construct::place().
			// returns: dijit/_WidgetBase
			//		Provides a useful return of the newly created dijit._Widget instance so you
			//		can "chain" this function by instantiating, placing, then saving the return value
			//		to a variable.
			// example:
			//	|	// create a Button with no srcNodeRef, and place it in the body:
			//	|	var button = new Button({ label:"click" }).placeAt(win.body());
			//	|	// now, 'button' is still the widget reference to the newly created button
			//	|	button.on("click", function(e){ console.log('click'); }));
			// example:
			//	|	// create a button out of a node with id="src" and append it to id="wrapper":
			//	|	var button = new Button({},"src").placeAt("wrapper");
			// example:
			//	|	// place a new button as the first element of some div
			//	|	var button = new Button({ label:"click" }).placeAt("wrapper","first");
			// example:
			//	|	// create a contentpane and add it to a TabContainer
			//	|	var tc = dijit.byId("myTabs");
			//	|	new ContentPane({ href:"foo.html", title:"Wow!" }).placeAt(tc)

			var refWidget = !reference.tagName && registry.byId(reference);
			if(refWidget && refWidget.addChild && (!position || typeof position === "number")){
				// Adding this to refWidget and can use refWidget.addChild() to handle everything.
				refWidget.addChild(this, position);
			}else{
				// "reference" is a plain DOMNode, or we can't use refWidget.addChild().   Use domConstruct.place() and
				// target refWidget.containerNode for nested placement (position==number, "first", "last", "only"), and
				// refWidget.domNode otherwise ("after"/"before"/"replace").  (But not supported officially, see #14946.)
				var ref = refWidget && ("domNode" in refWidget) ?
					(refWidget.containerNode && !/after|before|replace/.test(position || "") ?
						refWidget.containerNode : refWidget.domNode) : dom.byId(reference, this.ownerDocument);
				domConstruct.place(this.domNode, ref, position);

				// Start this iff it has a parent widget that's already started.
				// TODO: for 2.0 maybe it should also start the widget when this.getParent() returns null??
				if(!this._started && (this.getParent() || {})._started){
					this.startup();
				}
			}
			return this;
		},

		defer: function(fcn, delay){
			// summary:
			//		Wrapper to setTimeout to avoid deferred functions executing
			//		after the originating widget has been destroyed.
			//		Returns an object handle with a remove method (that returns null) (replaces clearTimeout).
			// fcn: Function
			//		Function reference.
			// delay: Number?
			//		Delay, defaults to 0.
			// tags:
			//		protected

			var timer = setTimeout(lang.hitch(this,
				function(){
					if(!timer){
						return;
					}
					timer = null;
					if(!this._destroyed){
						lang.hitch(this, fcn)();
					}
				}),
				delay || 0
			);
			return {
				remove: function(){
					if(timer){
						clearTimeout(timer);
						timer = null;
					}
					return null; // so this works well: handle = handle.remove();
				}
			};
		}
	});

	if(has("dojo-bidi")){
		_WidgetBase.extend(_BidiMixin);
	}

	return _WidgetBase;
});

},
'curam/define':function(){
/*
 * Copyright 2011 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(["dojo/_base/lang"], function(lang) {

  /*
   * Modification History
   * --------------------
   * 29-Jul-2011  MV [CR00269970] Initial version.
   */

  /**
   * This package contains function for working with classes in our code.
   */
  var global = this;
  if(typeof(global.curam) == "undefined") {
    global.curam = {};
  }

  if (typeof(global.curam.define) == "undefined") {
    lang.mixin(global.curam, {define: {}});
  }

  lang.mixin(global.curam.define, {
    /**
     * Defines a singleton class ensuring any packages are created and no
     * existing packages are overwritten in the process.
     *
     * @param {String} singletonName Name of the singleton class to be defined.cdej
     * @param {Object} [content] Optional content of the singleton class.
     */
    singleton: function(singletonName, content) {
      var parts = singletonName.split(".");

      // we assume we are runnning within a browser environment so the window
      // object is available.
      var currentContext = window;

      // now check for existence and create any missing packages
      for (var i = 0; i < parts.length; i++) {
        var part = parts[i];
        if (typeof currentContext[part] == "undefined") {
          currentContext[part] = {};
        }
        currentContext = currentContext[part];
      }

      // now set the content
      if (content) {
        lang.mixin(currentContext, content);
      }
    }
  });

  return global.curam.define;
});

},
'dojo/cldr/supplemental':function(){
define(["../_base/lang", "../i18n"], function(lang, i18n){

// module:
//		dojo/cldr/supplemental


var supplemental = {
	// summary:
	//		TODOC
};
lang.setObject("dojo.cldr.supplemental", supplemental);

supplemental.getFirstDayOfWeek = function(/*String?*/locale){
	// summary:
	//		Returns a zero-based index for first day of the week
	// description:
	//		Returns a zero-based index for first day of the week, as used by the local (Gregorian) calendar.
	//		e.g. Sunday (returns 0), or Monday (returns 1)

	// from http://www.unicode.org/cldr/data/common/supplemental/supplementalData.xml:supplementalData/weekData/firstDay
	var firstDay = {/*default is 1=Monday*/
		bd:5,mv:5,
		ae:6,af:6,bh:6,dj:6,dz:6,eg:6,iq:6,ir:6,jo:6,kw:6,
		ly:6,ma:6,om:6,qa:6,sa:6,sd:6,sy:6,ye:6,
		ag:0,ar:0,as:0,au:0,br:0,bs:0,bt:0,bw:0,by:0,bz:0,ca:0,cn:0,
		co:0,dm:0,'do':0,et:0,gt:0,gu:0,hk:0,hn:0,id:0,ie:0,il:0,'in':0,
		jm:0,jp:0,ke:0,kh:0,kr:0,la:0,mh:0,mm:0,mo:0,mt:0,mx:0,mz:0,
		ni:0,np:0,nz:0,pa:0,pe:0,ph:0,pk:0,pr:0,py:0,sg:0,sv:0,th:0,
		tn:0,tt:0,tw:0,um:0,us:0,ve:0,vi:0,ws:0,za:0,zw:0
	};

	var country = supplemental._region(locale);
	var dow = firstDay[country];
	return (dow === undefined) ? 1 : dow; /*Number*/
};

supplemental._region = function(/*String?*/locale){
	locale = i18n.normalizeLocale(locale);
	var tags = locale.split('-');
	var region = tags[1];
	if(!region){
		// IE often gives language only (#2269)
		// Arbitrary mappings of language-only locales to a country:
		region = {
			aa:"et", ab:"ge", af:"za", ak:"gh", am:"et", ar:"eg", as:"in", av:"ru", ay:"bo", az:"az", ba:"ru",
			be:"by", bg:"bg", bi:"vu", bm:"ml", bn:"bd", bo:"cn", br:"fr", bs:"ba", ca:"es", ce:"ru", ch:"gu",
			co:"fr", cr:"ca", cs:"cz", cv:"ru", cy:"gb", da:"dk", de:"de", dv:"mv", dz:"bt", ee:"gh", el:"gr",
			en:"us", es:"es", et:"ee", eu:"es", fa:"ir", ff:"sn", fi:"fi", fj:"fj", fo:"fo", fr:"fr", fy:"nl",
			ga:"ie", gd:"gb", gl:"es", gn:"py", gu:"in", gv:"gb", ha:"ng", he:"il", hi:"in", ho:"pg", hr:"hr",
			ht:"ht", hu:"hu", hy:"am", ia:"fr", id:"id", ig:"ng", ii:"cn", ik:"us", "in":"id", is:"is", it:"it",
			iu:"ca", iw:"il", ja:"jp", ji:"ua", jv:"id", jw:"id", ka:"ge", kg:"cd", ki:"ke", kj:"na", kk:"kz",
			kl:"gl", km:"kh", kn:"in", ko:"kr", ks:"in", ku:"tr", kv:"ru", kw:"gb", ky:"kg", la:"va", lb:"lu",
			lg:"ug", li:"nl", ln:"cd", lo:"la", lt:"lt", lu:"cd", lv:"lv", mg:"mg", mh:"mh", mi:"nz", mk:"mk",
			ml:"in", mn:"mn", mo:"ro", mr:"in", ms:"my", mt:"mt", my:"mm", na:"nr", nb:"no", nd:"zw", ne:"np",
			ng:"na", nl:"nl", nn:"no", no:"no", nr:"za", nv:"us", ny:"mw", oc:"fr", om:"et", or:"in", os:"ge",
			pa:"in", pl:"pl", ps:"af", pt:"br", qu:"pe", rm:"ch", rn:"bi", ro:"ro", ru:"ru", rw:"rw", sa:"in",
			sd:"in", se:"no", sg:"cf", si:"lk", sk:"sk", sl:"si", sm:"ws", sn:"zw", so:"so", sq:"al", sr:"rs",
			ss:"za", st:"za", su:"id", sv:"se", sw:"tz", ta:"in", te:"in", tg:"tj", th:"th", ti:"et", tk:"tm",
			tl:"ph", tn:"za", to:"to", tr:"tr", ts:"za", tt:"ru", ty:"pf", ug:"cn", uk:"ua", ur:"pk", uz:"uz",
			ve:"za", vi:"vn", wa:"be", wo:"sn", xh:"za", yi:"il", yo:"ng", za:"cn", zh:"cn", zu:"za",
			ace:"id", ady:"ru", agq:"cm", alt:"ru", amo:"ng", asa:"tz", ast:"es", awa:"in", bal:"pk",
			ban:"id", bas:"cm", bax:"cm", bbc:"id", bem:"zm", bez:"tz", bfq:"in", bft:"pk", bfy:"in",
			bhb:"in", bho:"in", bik:"ph", bin:"ng", bjj:"in", bku:"ph", bqv:"ci", bra:"in", brx:"in",
			bss:"cm", btv:"pk", bua:"ru", buc:"yt", bug:"id", bya:"id", byn:"er", cch:"ng", ccp:"in",
			ceb:"ph", cgg:"ug", chk:"fm", chm:"ru", chp:"ca", chr:"us", cja:"kh", cjm:"vn", ckb:"iq",
			crk:"ca", csb:"pl", dar:"ru", dav:"ke", den:"ca", dgr:"ca", dje:"ne", doi:"in", dsb:"de",
			dua:"cm", dyo:"sn", dyu:"bf", ebu:"ke", efi:"ng", ewo:"cm", fan:"gq", fil:"ph", fon:"bj",
			fur:"it", gaa:"gh", gag:"md", gbm:"in", gcr:"gf", gez:"et", gil:"ki", gon:"in", gor:"id",
			grt:"in", gsw:"ch", guz:"ke", gwi:"ca", haw:"us", hil:"ph", hne:"in", hnn:"ph", hoc:"in",
			hoj:"in", ibb:"ng", ilo:"ph", inh:"ru", jgo:"cm", jmc:"tz", kaa:"uz", kab:"dz", kaj:"ng",
			kam:"ke", kbd:"ru", kcg:"ng", kde:"tz", kdt:"th", kea:"cv", ken:"cm", kfo:"ci", kfr:"in",
			kha:"in", khb:"cn", khq:"ml", kht:"in", kkj:"cm", kln:"ke", kmb:"ao", koi:"ru", kok:"in",
			kos:"fm", kpe:"lr", krc:"ru", kri:"sl", krl:"ru", kru:"in", ksb:"tz", ksf:"cm", ksh:"de",
			kum:"ru", lag:"tz", lah:"pk", lbe:"ru", lcp:"cn", lep:"in", lez:"ru", lif:"np", lis:"cn",
			lki:"ir", lmn:"in", lol:"cd", lua:"cd", luo:"ke", luy:"ke", lwl:"th", mad:"id", mag:"in",
			mai:"in", mak:"id", man:"gn", mas:"ke", mdf:"ru", mdh:"ph", mdr:"id", men:"sl", mer:"ke",
			mfe:"mu", mgh:"mz", mgo:"cm", min:"id", mni:"in", mnk:"gm", mnw:"mm", mos:"bf", mua:"cm",
			mwr:"in", myv:"ru", nap:"it", naq:"na", nds:"de", "new":"np", niu:"nu", nmg:"cm", nnh:"cm",
			nod:"th", nso:"za", nus:"sd", nym:"tz", nyn:"ug", pag:"ph", pam:"ph", pap:"bq", pau:"pw",
			pon:"fm", prd:"ir", raj:"in", rcf:"re", rej:"id", rjs:"np", rkt:"in", rof:"tz", rwk:"tz",
			saf:"gh", sah:"ru", saq:"ke", sas:"id", sat:"in", saz:"in", sbp:"tz", scn:"it", sco:"gb",
			sdh:"ir", seh:"mz", ses:"ml", shi:"ma", shn:"mm", sid:"et", sma:"se", smj:"se", smn:"fi",
			sms:"fi", snk:"ml", srn:"sr", srr:"sn", ssy:"er", suk:"tz", sus:"gn", swb:"yt", swc:"cd",
			syl:"bd", syr:"sy", tbw:"ph", tcy:"in", tdd:"cn", tem:"sl", teo:"ug", tet:"tl", tig:"er",
			tiv:"ng", tkl:"tk", tmh:"ne", tpi:"pg", trv:"tw", tsg:"ph", tts:"th", tum:"mw", tvl:"tv",
			twq:"ne", tyv:"ru", tzm:"ma", udm:"ru", uli:"fm", umb:"ao", unr:"in", unx:"in", vai:"lr",
			vun:"tz", wae:"ch", wal:"et", war:"ph", xog:"ug", xsr:"np", yao:"mz", yap:"fm", yav:"cm", zza:"tr"
		}[tags[0]];
	}else if(region.length == 4){
		// The ISO 3166 country code is usually in the second position, unless a
		// 4-letter script is given. See http://www.ietf.org/rfc/rfc4646.txt
		region = tags[2];
	}
	return region;
};

supplemental.getWeekend = function(/*String?*/locale){
	// summary:
	//		Returns a hash containing the start and end days of the weekend
	// description:
	//		Returns a hash containing the start and end days of the weekend according to local custom using locale,
	//		or by default in the user's locale.
	//		e.g. {start:6, end:0}

	// from http://www.unicode.org/cldr/data/common/supplemental/supplementalData.xml:supplementalData/weekData/weekend{Start,End}
	var weekendStart = {/*default is 6=Saturday*/
			'in':0,
			af:4,dz:4,ir:4,om:4,sa:4,ye:4,
			ae:5,bh:5,eg:5,il:5,iq:5,jo:5,kw:5,ly:5,ma:5,qa:5,sd:5,sy:5,tn:5
		},

		weekendEnd = {/*default is 0=Sunday*/
			af:5,dz:5,ir:5,om:5,sa:5,ye:5,
			ae:6,bh:5,eg:6,il:6,iq:6,jo:6,kw:6,ly:6,ma:6,qa:6,sd:6,sy:6,tn:6
		},

		country = supplemental._region(locale),
		start = weekendStart[country],
		end = weekendEnd[country];

	if(start === undefined){start=6;}
	if(end === undefined){end=0;}
	return {start:start, end:end}; /*Object {start,end}*/
};

return supplemental;
});

},
'curam/cdsl/_base/MetadataRegistry':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(['dojo/_base/declare',
        'curam/cdsl/types/codetable/CodeTables',
        'curam/cdsl/types/codetable/CodeTable',        
        'dojo/_base/lang'
        ], function(
            declare, CodeTables, CodeTable, lang) {

  /**
   * @name curam.cdsl.request.MetadataRegistry
   * @namespace 
   */
  var MetadataRegistry = declare(null,
  /**
   * @lends curam.cdsl.request.MetadataRegistry.prototype
   */
  {
    _callEntries: null,

    _codetables: null,
    
    constructor: function() {
      this._callEntries = {};
      this._codetables = {};
    },
    
    /**
     * Sets flags on a method call prior to calling server, based on metadata
     * we hold in cache.
     * 
     * @param methodCall The method call instance to be updated.
     */
    setFlags: function(methodCall) {
      var entryKey = methodCall.intf() + '.' + methodCall.method(),
          notCalledBefore = !this._callEntries[entryKey];

      // only request codetables if we haven't called the method before
      methodCall._sendCodetables(notCalledBefore);
    },
    
    update: function(response) {
      var methodCall = response.request(),
          entryKey = methodCall.intf() + '.' + methodCall.method(),
          callEntry = this._callEntries[entryKey];
      if (!callEntry) {
        callEntry = {};
        this._callEntries[entryKey] = callEntry;
      }
      if (response.hasCodetables()) {
        var data = response.getCodetablesData();
        for (var i = 0; i < data.length; i++) {
          this._codetables[data[i].name] =
              new CodeTable(data[i].name, data[i].defaultCode, data[i].codes);
        }
      }
    },
    
    codetables: function() {
      // TODO: should we clone the array before returning? safer,
      // but adds overhead
      return this._codetables;
    }
  });
  
  return MetadataRegistry;
});

},
'dojo/Stateful':function(){
define(["./_base/declare", "./_base/lang", "./_base/array", "./when"], function(declare, lang, array, when){
	// module:
	//		dojo/Stateful

return declare("dojo.Stateful", null, {
	// summary:
	//		Base class for objects that provide named properties with optional getter/setter
	//		control and the ability to watch for property changes
	//
	//		The class also provides the functionality to auto-magically manage getters
	//		and setters for object attributes/properties.
	//		
	//		Getters and Setters should follow the format of _xxxGetter or _xxxSetter where 
	//		the xxx is a name of the attribute to handle.  So an attribute of "foo" 
	//		would have a custom getter of _fooGetter and a custom setter of _fooSetter.
	//
	// example:
	//	|	require(["dojo/Stateful", function(Stateful) {
	//	|		var obj = new Stateful();
	//	|		obj.watch("foo", function(){
	//	|			console.log("foo changed to " + this.get("foo"));
	//	|		});
	//	|		obj.set("foo","bar");
	//	|	});

	// _attrPairNames: Hash
	//		Used across all instances a hash to cache attribute names and their getter 
	//		and setter names.
	_attrPairNames: {},

	_getAttrNames: function(name){
		// summary:
		//		Helper function for get() and set().
		//		Caches attribute name values so we don't do the string ops every time.
		// tags:
		//		private

		var apn = this._attrPairNames;
		if(apn[name]){ return apn[name]; }
		return (apn[name] = {
			s: "_" + name + "Setter",
			g: "_" + name + "Getter"
		});
	},

	postscript: function(/*Object?*/ params){
		// Automatic setting of params during construction
		if (params){ this.set(params); }
	},

	_get: function(name, names){
		// summary:
		//		Private function that does a get based off a hash of names
		// names:
		//		Hash of names of custom attributes
		return typeof this[names.g] === "function" ? this[names.g]() : this[name];
	},
	get: function(/*String*/name){
		// summary:
		//		Get a property on a Stateful instance.
		// name:
		//		The property to get.
		// returns:
		//		The property value on this Stateful instance.
		// description:
		//		Get a named property on a Stateful object. The property may
		//		potentially be retrieved via a getter method in subclasses. In the base class
		//		this just retrieves the object's property.
		// example:
		//	|	require(["dojo/Stateful", function(Stateful) {
		//	|		var stateful = new Stateful({foo: 3});
		//	|		stateful.get("foo") // returns 3
		//	|		stateful.foo // returns 3
		//	|	});

		return this._get(name, this._getAttrNames(name)); //Any
	},
	set: function(/*String*/name, /*Object*/value){
		// summary:
		//		Set a property on a Stateful instance
		// name:
		//		The property to set.
		// value:
		//		The value to set in the property.
		// returns:
		//		The function returns this dojo.Stateful instance.
		// description:
		//		Sets named properties on a stateful object and notifies any watchers of
		//		the property. A programmatic setter may be defined in subclasses.
		// example:
		//	|	require(["dojo/Stateful", function(Stateful) {
		//	|		var stateful = new Stateful();
		//	|		stateful.watch(function(name, oldValue, value){
		//	|			// this will be called on the set below
		//	|		}
		//	|		stateful.set(foo, 5);
		//	set() may also be called with a hash of name/value pairs, ex:
		//	|		stateful.set({
		//	|			foo: "Howdy",
		//	|			bar: 3
		//	|		});
		//	|	});
		//	This is equivalent to calling set(foo, "Howdy") and set(bar, 3)

		// If an object is used, iterate through object
		if(typeof name === "object"){
			for(var x in name){
				if(name.hasOwnProperty(x) && x !="_watchCallbacks"){
					this.set(x, name[x]);
				}
			}
			return this;
		}

		var names = this._getAttrNames(name),
			oldValue = this._get(name, names),
			setter = this[names.s],
			result;
		if(typeof setter === "function"){
			// use the explicit setter
			result = setter.apply(this, Array.prototype.slice.call(arguments, 1));
		}else{
			// no setter so set attribute directly
			this[name] = value;
		}
		if(this._watchCallbacks){
			var self = this;
			// If setter returned a promise, wait for it to complete, otherwise call watches immediately
			when(result, function(){
				self._watchCallbacks(name, oldValue, value);
			});
		}
		return this; // dojo/Stateful
	},
	_changeAttrValue: function(name, value){
		// summary:
		//		Internal helper for directly changing an attribute value.
		//
		// name: String
		//		The property to set.
		// value: Mixed
		//		The value to set in the property.
		//
		// description:
		//		Directly change the value of an attribute on an object, bypassing any 
		//		accessor setter.  Also handles the calling of watch and emitting events. 
		//		It is designed to be used by descendant class when there are two values 
		//		of attributes that are linked, but calling .set() is not appropriate.

		var oldValue = this.get(name);
		this[name] = value;
		if(this._watchCallbacks){
			this._watchCallbacks(name, oldValue, value);
		}
		return this; // dojo/Stateful
	},
	watch: function(/*String?*/name, /*Function*/callback){
		// summary:
		//		Watches a property for changes
		// name:
		//		Indicates the property to watch. This is optional (the callback may be the
		//		only parameter), and if omitted, all the properties will be watched
		// returns:
		//		An object handle for the watch. The unwatch method of this object
		//		can be used to discontinue watching this property:
		//		|	var watchHandle = obj.watch("foo", callback);
		//		|	watchHandle.unwatch(); // callback won't be called now
		// callback:
		//		The function to execute when the property changes. This will be called after
		//		the property has been changed. The callback will be called with the |this|
		//		set to the instance, the first argument as the name of the property, the
		//		second argument as the old value and the third argument as the new value.

		var callbacks = this._watchCallbacks;
		if(!callbacks){
			var self = this;
			callbacks = this._watchCallbacks = function(name, oldValue, value, ignoreCatchall){
				var notify = function(propertyCallbacks){
					if(propertyCallbacks){
						propertyCallbacks = propertyCallbacks.slice();
						for(var i = 0, l = propertyCallbacks.length; i < l; i++){
							propertyCallbacks[i].call(self, name, oldValue, value);
						}
					}
				};
				notify(callbacks['_' + name]);
				if(!ignoreCatchall){
					notify(callbacks["*"]); // the catch-all
				}
			}; // we use a function instead of an object so it will be ignored by JSON conversion
		}
		if(!callback && typeof name === "function"){
			callback = name;
			name = "*";
		}else{
			// prepend with dash to prevent name conflicts with function (like "name" property)
			name = '_' + name;
		}
		var propertyCallbacks = callbacks[name];
		if(typeof propertyCallbacks !== "object"){
			propertyCallbacks = callbacks[name] = [];
		}
		propertyCallbacks.push(callback);

		// TODO: Remove unwatch in 2.0
		var handle = {};
		handle.unwatch = handle.remove = function(){
			var index = array.indexOf(propertyCallbacks, callback);
			if(index > -1){
				propertyCallbacks.splice(index, 1);
			}
		};
		return handle; //Object
	}

});

});

},
'curam/cdsl/_base/_Connection':function(){
/*
 * Copyright 2014 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 13-Oct-2014  SC  [CR00446751] Support for preferences.
 * 08-Oct-2014  MV  [CR00446578] Increasing default timeout.
 */

define(['dojo/_base/declare',
        './MetadataRegistry',
        './PreferenceMap'
        ], function(
            declare, MetadataRegistry, PreferenceMap) {

  /**
   * @name curam.cdsl._base._Connection
   * @namespace Represents a connection to Curam server. This abstraction
   *    allows for variation in connection methods used for accessing the Curam
   *    server.
   */
  var _Connection = declare(null,
  /**
   * @lends curam._base._Connection.prototype
   */
  {
    /** Number of milliseconds to wait for server response by default. */
    _DEFAULT_REQUEST_TIMEOUT: 60000,

    /** Metadata related to this connection instance. */
    _metadata: null,

    /** Preferences related to this connection instance. */
    _preferences: null,

    constructor: function() {
      this._metadata = new MetadataRegistry();
      this._preferences = new PreferenceMap();
    },
    
    /**
     * Invokes a specific facade method on the server.
     * 
     * @param {curam.cdsl.request.FacadeMethodCall} methodCall
     *    The method call instance.
     * @param {Number} timeout Timeout value for the call.
     * @return {dojo.Promise} A promise for the result of invocation.
     */
    invoke: function(methodCall, timeout) {
      // set flags around metadata to be requested
      this._metadata.setFlags(methodCall);
    },
    
    updateMetadata: function(response) {
      this._metadata.update(response);
    },
    
    metadata: function() {
      return this._metadata;
    },
    
    /**
     * Provides access to the preferences associated with the connection.
     * 
     * @returns {PreferenceMap} The name value preference map.
     */
    preferences: function() {
      return this._preferences;
    }
  });
  
  return _Connection;
});

},
'curam/debug':function(){
/*
 * Copyright 2009-2011 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Curam Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(["curam/define",
        "curam/util/LocalConfig",
        "dojo/ready",
        "dojo/_base/lang",
        "curam/util/ResourceBundle"], function(define, localConfig, ready, lang, resBundle) {
  
  var debugBundle = new resBundle("curam.application.Debug");
  
  /*
   * Modification History
   * --------------------
   * 05-Mar-2014 MV [CR00421036] Move to using LocalConfig. 
   * 27-Feb-2014 MV [CR00419961] Add support for JavaScript debug configuration.
   * 12-Sep-2011  MV  [CR00286500] Fix stack overflow when logging window
   *    objects in IE.
   * 11-May-2011  MV  [CR00265902] Fixed log function invocation.
   * 26-Jan-2011  MV  [CR00244801] Fixed check for DEBUG flag.
   * 21-Jan-2011  DG  [CR00243540] Added note on new way to set DEBUG variable.
   *                    Added copyright. Tidied up formatting.
   * 26-Jun-2010  MV  [CR00204069] Improve to enable passing many number
   *                    of parameters. Enable easier usage by removing class
   *                    declaration and shortening the name.
   * 06-Nov-2009  SC  [CR00172239] Initial Version
   */
  /**
   * Provides a simple logging facility for debug tracing to the JavaScript
   * console.
   */
  define.singleton("curam.debug", {
    /**
     * A logging facility that can be turned on or off using the
     * "curam.trace.javascript" application property. Debug tracing is disabled
     * by default; use your application admin screens to enable it.
     * Any number of parameters can be passed. These are forwarded
     * to the <code>console.log.apply</code> function.
     */
    log: function() {
      //debugger;
      if (curam.debug.enabled()) {
        try {
          var a = arguments;
          if (!dojo.isIE) {
            console.log.apply(console, a);
          
          // the above generic call doesn't work in IE  so the following ugly
          // specific handling must be provided
          } else {
            var numArgs = a.length;
            var sa = curam.debug._serializeArgument;
            switch(numArgs) {
              case 1: console.log(arguments[0]);
              break;
              
              case 2: console.log(a[0], sa(a[1]));
              break;
              
              case 3: console.log(a[0], sa(a[1]), sa(a[2]));
              break;
              
              case 4: console.log(a[0], sa(a[1]), sa(a[2]), sa(a[3]));
              break;
              
              case 5: console.log(a[0], sa(a[1]), sa(a[2]), sa(a[3]), sa(a[4]));
              break;
              
              case 6: console.log(a[0], sa(a[1]), sa(a[2]), sa(a[3]), sa(a[4]),
                  sa(a[5]));
              break;
              
              default: console.log("[Incomplete message - " + (numArgs - 5)
                  + " message a truncated] " + a[0],
                  sa(a[1]), sa(a[2]), sa(a[3]), sa(a[4]), sa(a[5]));
            }
          }

        } catch (e) {
          console.log(e);
          // Some problem with the console. Do nothing.
        }
      }
    },
    
    getProperty : function(key, substValues) {
      return debugBundle.getProperty(key, substValues);
    },
    
    /**
     * Make a human readable version of the object to be logged.
     *
     * @param arg The object to be serialized.
     * @returns The human readable version of the object.
     */
    _serializeArgument: function(arg) {
      if (typeof arg != "undefined"
          && typeof arg.nodeType != "undefined"
          && typeof arg.cloneNode != "undefined") { // isNode 
        // DOM nodes can't be serialized using dojo.toJson(), so just return
        // default toString version
        return "" + arg;

      } else if (curam.debug._isWindow(arg)) {
        // window object causes stack overflow for dojo.toJson() so handling 
        // specifically here
        return arg.location.href;

      } else if (curam.debug._isArray(arg)
            && curam.debug._isWindow(arg[0])) { // is array of window objects
        // array of window objects causes stack overflow for dojo.toJson()
        // so handling specifically here
        return "[array of window objects, length " + arg.length + "]";

      } else {
        return dojo.toJson(arg);
      }
    },
    
    /**
     * Recognizes array objects.
     * 
     * @param arg The object to be checked.
     * @returns {Boolean} True if the argument is array, otherwise false.
     */
    _isArray: function(arg) {
      return typeof arg != "undefined"
          && (dojo.isArray(arg) || typeof arg.length != "undefined");
    },

    /**
     * Recognizes window object.
     *
     * @param arg The object to be checked.
     * @returns {Boolean} True if the argument is a window object,
     *    otherwise false.
     */
    _isWindow: function(arg) {
      // some of the significant properties might be undefined fo closed window
      // so handle this case separately
      var isClosed = typeof arg != "undefined"
        && typeof arg.closed != "undefined" && arg.closed;
      if (isClosed) {
        // it is a closed window
        return true;
      
      } else {
        return typeof arg != "undefined"
            && typeof arg.location != "undefined"
            && typeof arg.navigator != "undefined"
            && typeof arg.document != "undefined"
            && typeof arg.closed != "undefined";
      }
    },
    
    enabled: function() {
      return localConfig.readOption('jsTraceLog', 'false') == 'true';
    },
    
    /**
     * Performs setup of the debug/tracing infrastructure.
     * This is called from the application main page to setup debug.
     *
     * @param {Object} config Configuration data.
     */
    _setup: function(config) {
      localConfig.seedOption('jsTraceLog', config.trace, 'false');
      localConfig.seedOption('ajaxDebugMode', config.ajaxDebug, 'false');
      localConfig.seedOption('asyncProgressMonitor',
          config.asyncProgressMonitor, 'false');
    }
  });
  
  return curam.debug;
});

},
'curam/cdsl/store/CuramStore':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 26-Sep-2014  MV  [CR00445374] Implement generic data hookpoint.
 */

define(['dojo/_base/declare',
        'curam/cdsl/request/CuramService',
        'curam/cdsl/_base/FacadeMethodCall',
        "curam/cdsl/Struct",
        'curam/cdsl/store/IdentityApi',
        'dojo/store/util/QueryResults',
        'dojo/_base/lang',
        'curam/cdsl/_base/_Connection'
        /*=== 'dojo/store/api/Store ===*/
        ],
    function(declare, CuramService, FacadeMethodCall, Struct, IdentityApi,
        QueryResults, lang /*===, Store ===*/) {
  
  var METHOD_MAPPINGS = {
    query: 'listItems',
    get: 'read',
    put: 'modify',
    add: 'insert',
    remove: 'remove'
  };
  
  var DEFAULT_OPTIONS = {
    identityApi: new IdentityApi(),
    dataAdapter: null
  };
    
  var processOptions = function(options) {
    var o = lang.clone(DEFAULT_OPTIONS);
    
    /* TODO: REMOVE
     * this branch is here to support old API where the third parameter
     * to the constructor was the identityApi, not options. */
    if (options && options.getIdentity
        && options.parseIdentity
        && options.getIdentityPropertyNames) {
      
      o.identityApi = options;
      
    } else {
      o = lang.mixin(o, options);
    }
    
    return o;
  };
  
  var Store = null; // We implement the Store API, but not directly extending
                    // it in order to save resources. Noted here and above
                    // as commented out dependency for documentation purposes.
  
  /**
   * @name curam.cdsl.store.CuramStore
   * @namespace Store implementation that connects to Curam server and allows
   *  using specifically modeled Curam facades to provide data.
   */
  var CuramStore = declare(Store,
  /**
   * @lends curam.cdsl.store.CuramStore.prototype
   */
  {
    _service: null,
    _baseFacadeName: null,
    _identityApi: null,
    
    /**
     * Creates an instance of CuramStore.
     * 
     * @param {curam/cdsl/_base/_Connection} connection The connection object
     *  to be used. Use an instance of curam/cdsl/connection/CuramConnection
     *  class.
     * @param {String} baseFacadeName Name of the base facade that contains
     *  specific modeled methods to be used by the store.
     * @param {Object} [options] Object containing the options for this store
     *  instance. The accepted options are: "identityApi" An instance
     *  of identity API in case you want to base identity on a property other
     *  than the default "id". "dataAdapter" Object containing functions
     *  to be run on data as it is sent to and from the server. More information
     *  on this in curam/cdsl/request/FacadeMethodCall.
     */
    constructor: function(connection, baseFacadeName, opts) {
      var options = processOptions(opts);
      
      this._service = new CuramService(connection, {
        dataAdapter: options.dataAdapter
      });
      this._baseFacadeName = baseFacadeName;
      this._identityApi = options.identityApi;
    },
    
    /**
     * Implements the dojo/store get() method.
     * 
     * @param {Object} identity Object containing the identity properties.
     * @returns {dojo/Promise::curam/cdsl/Struct} Promise for a result struct.
     */
    get: function(identity) {
      var idStruct = new Struct(this._identityApi.parseIdentity(identity));
      var methodCall = new FacadeMethodCall(this._baseFacadeName,
          METHOD_MAPPINGS.get, [idStruct]);

      return this._service.call([methodCall]).then(function(data) {
        // unpack result from array
        return data[0];
      });
    },
    
    /**
     * Implements the dojo/store getIdentity() method.
     * 
     * @param {curam/cdsl/Struct} struct Object you want to get identity for.
     *    It must be one of the data objects returned by this API.
     * @returns {Number|String} The object identity.
     */
    getIdentity: function(struct) {
      return this._identityApi.getIdentity(struct);
    },
    
    /**
     * Implements the dojo/store query() method.
     * 
     * @param {Object} query The query object. It is expected to be a JavaScript
     *  object containing values to be mapped to the first input struct
     *  you modeled in your listItems() method in Curam facade.
     * @param {Object} options The standard dojo/store API options object for
     *  the query() method.
     * 
     * @returns {dojo/store/util/QueryResults} The query results, typically
     *  containing an array of curam/cdsl/Struct objects.
     */
    query: function(query, options) {
      var methodCall = new FacadeMethodCall(
          this._baseFacadeName, METHOD_MAPPINGS.query, [ new Struct(query) ]);

      if (options) {
        methodCall._setMetadata({ queryOptions: {
          offset: options.start,
          count: options.count,
          sort: options.sort
        }});
      }

      var promise = this._service.call([methodCall]).then(function(data) {
        // unpack results array and wrap with QueryResults API
        return data[0].dtls;
      });

      return new QueryResults(promise);
    },
    
    /**
     * Implements the dojo/store put() method.
     * 
     * @param {curam/cdsl/Struct} object Struct with the data to be updated
     *  on the server.
     * @param {Object} options The standard dojo/store API options object for
     *  the put() method. Please note the options are currently ingored by CDSL.
     * 
     * @returns {dojo/Promise::curam/cdsl/Struct} Promise for a result struct,
     *  which is the case of this method is expected to contain the identity
     *  of the updated object.
     */
    put: function(object, options) {
      if (options && typeof options.overwrite !== 'undefined'
        && !options.overwrite) {
        throw new Error(
            'The overwrite option is set to false, but adding new items '
            + 'via CuramStore.put() is not supported.');
      }
      return this._addOrPut(METHOD_MAPPINGS.put, object, options, 'putOptions');
    },
    
    /**
     * Implements the dojo/store add() method.
     * 
     * @param {curam/cdsl/Struct} object Struct with the data to be inserted
     *  on the server.
     * @param {Object} options The standard dojo/store API options object for
     *  the add() method. Please note the options are currently ingored by CDSL.
     * 
     * @returns {dojo/Promise::curam/cdsl/Struct} Promise for a result struct,
     *  which is the case of this method is expected to contain the identity
     *  of the inserted object.
     */
    add: function(object, options) {
      var opts = {};
      if (options) {
        opts = lang.mixin(opts, options);
      }
      opts.overwrite = false; // setting to satisfy dojo/store API spec
      return this._addOrPut(METHOD_MAPPINGS.add, object, opts, 'addOptions');
    },
    
    /**
     * Implementation of add() and put() methods.
     * 
     * @private
     * 
     * @param methodName
     * @param object
     * @param options
     * @param metadataPropertyName
     * @returns
     */
    _addOrPut: function(methodName, object, options, metadataPropertyName) {
      var struct = object;
      if (!struct.isInstanceOf || !struct.isInstanceOf(Struct)) {
        struct = new Struct(object);
      }
      
      var methodCall = new FacadeMethodCall(
          this._baseFacadeName, methodName, [ struct ]);

      if (options) {
        var metadata = {};
        metadata[metadataPropertyName] = {
          id: options.id ? options.id : null,
          before: options.before ? this.getIdentity(options.before) : null,
          parent: options.parent ? this.getIdentity(options.parent) : null,
          overwrite: false
        };

        methodCall._setMetadata(metadata);
      }

      return this._service.call([methodCall]).then(
          lang.hitch(this, function(data) {
            // return the identity value provided in options
            if (options && options.id) {
              return options.id;
            }
            // if updating existing object, use identity of provided struct
            if(options && options.overwrite) {
              return this.getIdentity(struct);
            }
            // otherwise the returned data contain the identity
            return this.getIdentity(data[0]);
          }));
    },
    
    /**
     * Implements the dojo/store remove() method.
     * 
     * @param {Object} identity Object containing the identity properties
     *  for the object to be removed.
     * 
     * @returns {dojo/Promise::curam/cdsl/Struct} Promise for a result struct,
     *  which is the case of this method is expected to contain the identity
     *  of the removed object.
     */
    remove: function(identity) {
      var idStruct = new Struct(this._identityApi.parseIdentity(identity)),
          methodCall = new FacadeMethodCall(this._baseFacadeName,
              METHOD_MAPPINGS.remove, [idStruct]);

      return this._service.call([methodCall]).then(function(data) {
        // success, no server error, return identity of deleted object
        return identity;
      });
    }
  });
  
  return CuramStore;
});

},
'curam/util/Constants':function(){
/*
 * Copyright 2009-2011 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(["curam/define"
        ], function() {
  
  /*
   * Modification History
   * --------------------
   * 05-Jul-2011  KW  [CR00275353] Initial version
   */

  /**
   * Maintains a list of parameter name constants. 
   */
  curam.define.singleton("curam.util.Constants", {
    RETURN_PAGE_PARAM: "__o3rpu"
  });
  
  return curam.util.Constants;
});

},
'curam/cdsl/connection/SimpleAccess':function(){
/*
 * Copyright 2014 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 06-Oct-2014  JY  [CR00446378] Initial version.
 */

  /**
   * @name curam.cdsl.connection.SimpleAccess
   * @namespace Simplified APIs to direct requesting the Curam cdsl layer. 
   */
define(["curam/cdsl/_base/_Connection",
        "curam/cdsl/store/CuramStore",
        "curam/cdsl/request/CuramService",
        "curam/cdsl/_base/FacadeMethodCall",
        "curam/cdsl/Struct",
        "dojo/_base/lang",
        "dojo/store/Observable",
        "dojo/store/Cache",
        "dojo/store/Memory"
        ], function(
            _Connection, 
            CuramStore,
            CuramService,
            FacadeMethodCall,
            Struct,
            lang,
            Observable,
            Cache,
            Memory) {
  
  //The connection object held here is a singleton. 
  var connection = null;
  
  /**
   * @lends curam.cdsl.connection.SimpleAccess.prototype
   */
  return {
    
    /**
     * Initialize the connection object. Once it is set, it can not be changed.
     * 
     * @param {curam/cdsl/_base/_Connection} connectionObj The connection object
     *  to be used. Use an instance of curam/cdsl/connection/CuramConnection
     *  class.
     * @return {curam/cdsl/_base/_Connection} An instance of the
     *  curam/cdsl/_base/_Connection class.
     */
    initConnection: function(connectionObj) {
      
      if (connectionObj == null) {
        throw new Error("The connection object should be provided.");
        
      } else if (!(connectionObj instanceof _Connection)){
        
        throw new Error("The wrong type of the connection object is provided.");
      }
      
      // Initializing the connection object.
      // Lazy loading.
      if (connection == null) {
        connection = connectionObj;
      }
      
      return connection;
    },
    
    /**
     * Builds an instance of curam/cdsl/store/CuramStore.
     * 
     * @param {String} facadeClassName Name of the facade class that contains
     *  specific modeled methods to be used by the store.
     * @param {curam/cdsl/store/IdentityApi} identityApi An instance of the 
	 *  IdentityApi.
     * @param {boolean} observable The indicator to make the store observable
     *  or not.
     * @param {boolean} cache The indicator to wrap the curam store with a 
     *  cache store.
     * @return {curam/cdsl/store/CuramStore} An instance of the CuramStore. 
     */
    buildStore: function(facadeClassName, identityApi, observable, cache) {                  

      if (connection == null) {
        throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
      }
      
      
      if(facadeClassName == null){
              throw new Error("Facade class name is missing.");
      }                     
      
      if(cache == null){
        cache = false;
      }
      
      if(observable == null){
        observable = false;
      }
                   
      
      var store = new CuramStore(connection, facadeClassName, identityApi);
      
      if (observable) {
        store = new Observable(store);
      }
      
      if (cache) {
        var cacheStore = new Memory();
        store = new Cache(store, cacheStore);
      }
      
      return store;
    },
    
    /**
     * Makes a request to the Curam facade layer for the specified server
     * interface method calls.
     * 
     * @param {String} facadeClassName Name of the server interface to call.
     * @param {String} facadeMethodName Name of the server interface method to call. 
     * @param {Object} facadeMethodParmas The object containing all the method
     *  parameters that required by the server interface method.
     * @returns {dojo/Promise::[curam/cdsl/Struct]} A promise for the array
     *  of Struct objects returned from the server call.
     */
    makeRequest: function(facadeClassName, facadeMethodName, facadeMethodParmas) {      
      
      if (connection == null) {
        throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
      }
      
      
      if(facadeClassName == null){
        throw new Error("Facade class name is missing.");
      }
      
      if(facadeMethodName == null){
              throw new Error("Facade method name is missing.");
      }
      
      var service = new CuramService(connection);
      
      var idStructs;
      
      if(facadeMethodParmas == null) {
        
        idStructs = [];
      } else {
        
        idStructs = [new Struct(facadeMethodParmas)];
      }

      var methodCall = new FacadeMethodCall(facadeClassName,
          facadeMethodName, idStructs);
      
      return service.call([methodCall]);
    }
  };
});
},
'curam/util/RuntimeContext':function(){
/*
 * Copyright 2011 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(['dojo/_base/declare'
        ], function(declare) {

/*
 * Modification History
 * --------------------
 * 02-Aug-2011  MV  [CR00283023] Added the contextObject() function.
 * 21-Jun-2011  KW  [CR00275353] Initial version.
 */

/**
 * @name curam.util.RuntimeContext
 * @namespace Provides access to the browser Window object.
 * 
   */
  var RuntimeContext = declare("curam.util.RuntimeContext", null, {

  /**
   * @private
   */
  _window: null,

  /**
   * comments to follow
   *
   * @constructor
   * @private
   */
  constructor: function(window) {
    this._window = window;
  },
  
  /**
   * Comments to follow
   * 
   */
  getHref: function(){
    return this._window.location.href;
  },
  
  /**
   * Returns the path name of the url from the window's location object.
   * @Returns {String} Url path name.
   */
  getPathName: function() {
    return this._window.location.pathName;
  },

  /**
   * Returns the underlying context object of this runtime context.
   * @returns The underlying context object of this runtime context.
   */
  contextObject: function() {
    return this._window;
  }
  });
  
  return RuntimeContext;
});

},
'dojo/NodeList-traverse':function(){
define(["./query", "./_base/lang", "./_base/array"], function(dquery, lang, array){

// module:
//		dojo/NodeList-traverse

/*=====
return function(){
	// summary:
	//		Adds chainable methods to dojo/query() / NodeList instances for traversing the DOM
};
=====*/

var NodeList = dquery.NodeList;

lang.extend(NodeList, {
	_buildArrayFromCallback: function(/*Function*/ callback){
		// summary:
		//		builds a new array of possibly differing size based on the input list.
		//		Since the returned array is likely of different size than the input array,
		//		the array's map function cannot be used.
		var ary = [];
		for(var i = 0; i < this.length; i++){
			var items = callback.call(this[i], this[i], ary);
			if(items){
				ary = ary.concat(items);
			}
		}
		return ary;	//Array
	},

	_getUniqueAsNodeList: function(/*Array*/ nodes){
		// summary:
		//		given a list of nodes, make sure only unique
		//		elements are returned as our NodeList object.
		//		Does not call _stash().
		var ary = [];
		//Using for loop for better speed.
		for(var i = 0, node; node = nodes[i]; i++){
			//Should be a faster way to do this. dojo/query has a private
			//_zip function that may be inspirational, but there are pathways
			//in query that force nozip?
			if(node.nodeType == 1 && array.indexOf(ary, node) == -1){
				ary.push(node);
			}
		}
		return this._wrap(ary, null, this._NodeListCtor);	 // dojo/NodeList
	},

	_getUniqueNodeListWithParent: function(/*Array*/ nodes, /*String*/ query){
		// summary:
		//		gets unique element nodes, filters them further
		//		with an optional query and then calls _stash to track parent NodeList.
		var ary = this._getUniqueAsNodeList(nodes);
		ary = (query ? dquery._filterResult(ary, query) : ary);
		return ary._stash(this);  // dojo/NodeList
	},

	_getRelatedUniqueNodes: function(/*String?*/ query, /*Function*/ callback){
		// summary:
		//		cycles over all the nodes and calls a callback
		//		to collect nodes for a possible inclusion in a result.
		//		The callback will get two args: callback(node, ary),
		//		where ary is the array being used to collect the nodes.
		return this._getUniqueNodeListWithParent(this._buildArrayFromCallback(callback), query);  // dojo/NodeList
	},

	children: function(/*String?*/ query){
		// summary:
		//		Returns all immediate child elements for nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the child elements.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		all immediate child elements for the nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		Some Text
		// 	|		<div class="blue">Blue One</div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".container").children();
		//	|	});
		//		returns the four divs that are children of the container div.
		//		Running this code:
		//	|	dojo.query(".container").children(".red");
		//		returns the two divs that have the class "red".
		return this._getRelatedUniqueNodes(query, function(node, ary){
			return lang._toArray(node.childNodes);
		}); // dojo/NodeList
	},

	closest: function(/*String*/ query, /*String|DOMNode?*/ root){
		// summary:
		//		Returns closest parent that matches query, including current node in this
		//		dojo/NodeList if it matches the query.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// root:
		//		If specified, query is relative to "root" rather than document body.
		// returns:
		//		the closest parent that matches the query, including the current
		//		node in this dojo/NodeList if it matches the query.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		//	|		<div class="red">Red One</div>
		//	|		Some Text
		//	|		<div class="blue">Blue One</div>
		//	|		<div class="red">Red Two</div>
		//	|		<div class="blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".red").closest(".container");
		//	|	});
		//		returns the div with class "container".
		return this._getRelatedUniqueNodes(null, function(node, ary){
			do{
				if(dquery._filterResult([node], query, root).length){
					return node;
				}
			}while(node != root && (node = node.parentNode) && node.nodeType == 1);
			return null; //To make rhino strict checking happy.
		}); // dojo/NodeList
	},

	parent: function(/*String?*/ query){
		// summary:
		//		Returns immediate parent elements for nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the parent elements.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		immediate parent elements for nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		<div class="blue first"><span class="text">Blue One</span></div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue"><span class="text">Blue Two</span></div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".text").parent();
		//	|	});
		//		returns the two divs with class "blue".
		//		Running this code:
		//	|		query(".text").parent(".first");
		//		returns the one div with class "blue" and "first".
		return this._getRelatedUniqueNodes(query, function(node, ary){
			return node.parentNode;
		}); // dojo/NodeList
	},

	parents: function(/*String?*/ query){
		// summary:
		//		Returns all parent elements for nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the child elements.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		all parent elements for nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		<div class="blue first"><span class="text">Blue One</span></div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue"><span class="text">Blue Two</span></div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".text").parents();
		//	|	});
		//		returns the two divs with class "blue", the div with class "container",
		// 	|	the body element and the html element.
		//		Running this code:
		//	|		query(".text").parents(".container");
		//		returns the one div with class "container".
		return this._getRelatedUniqueNodes(query, function(node, ary){
			var pary = [];
			while(node.parentNode){
				node = node.parentNode;
				pary.push(node);
			}
			return pary;
		}); // dojo/NodeList
	},

	siblings: function(/*String?*/ query){
		// summary:
		//		Returns all sibling elements for nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the sibling elements.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		all sibling elements for nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		Some Text
		// 	|		<div class="blue first">Blue One</div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".first").siblings();
		//	|	});
		//		returns the two divs with class "red" and the other div
		// 	|	with class "blue" that does not have "first".
		//		Running this code:
		//	|		query(".first").siblings(".red");
		//		returns the two div with class "red".
		return this._getRelatedUniqueNodes(query, function(node, ary){
			var pary = [];
			var nodes = (node.parentNode && node.parentNode.childNodes);
			for(var i = 0; i < nodes.length; i++){
				if(nodes[i] != node){
					pary.push(nodes[i]);
				}
			}
			return pary;
		}); // dojo/NodeList
	},

	next: function(/*String?*/ query){
		// summary:
		//		Returns the next element for nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the next elements.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		the next element for nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		Some Text
		// 	|		<div class="blue first">Blue One</div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue last">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".first").next();
		//	|	});
		//		returns the div with class "red" and has innerHTML of "Red Two".
		//		Running this code:
		//	|	dojo.query(".last").next(".red");
		//		does not return any elements.
		return this._getRelatedUniqueNodes(query, function(node, ary){
			var next = node.nextSibling;
			while(next && next.nodeType != 1){
				next = next.nextSibling;
			}
			return next;
		}); // dojo/NodeList
	},

	nextAll: function(/*String?*/ query){
		// summary:
		//		Returns all sibling elements that come after the nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the sibling elements.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		all sibling elements that come after the nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		Some Text
		// 	|		<div class="blue first">Blue One</div>
		// 	|		<div class="red next">Red Two</div>
		// 	|		<div class="blue next">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".first").nextAll();
		//	|	});
		//		returns the two divs with class of "next".
		//		Running this code:
		//	|		query(".first").nextAll(".red");
		//		returns the one div with class "red" and innerHTML "Red Two".
		return this._getRelatedUniqueNodes(query, function(node, ary){
			var pary = [];
			var next = node;
			while((next = next.nextSibling)){
				if(next.nodeType == 1){
					pary.push(next);
				}
			}
			return pary;
		}); // dojo/NodeList
	},

	prev: function(/*String?*/ query){
		// summary:
		//		Returns the previous element for nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the previous elements.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		the previous element for nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		Some Text
		// 	|		<div class="blue first">Blue One</div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".first").prev();
		//	|	});
		//		returns the div with class "red" and has innerHTML of "Red One".
		//		Running this code:
		//	|		query(".first").prev(".blue");
		//		does not return any elements.
		return this._getRelatedUniqueNodes(query, function(node, ary){
			var prev = node.previousSibling;
			while(prev && prev.nodeType != 1){
				prev = prev.previousSibling;
			}
			return prev;
		}); // dojo/NodeList
	},

	prevAll: function(/*String?*/ query){
		// summary:
		//		Returns all sibling elements that come before the nodes in this dojo/NodeList.
		//		Optionally takes a query to filter the sibling elements.
		// description:
		//		The returned nodes will be in reverse DOM order -- the first node in the list will
		//		be the node closest to the original node/NodeList.
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// query:
		//		a CSS selector.
		// returns:
		//		all sibling elements that come before the nodes in this dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red prev">Red One</div>
		// 	|		Some Text
		// 	|		<div class="blue prev">Blue One</div>
		// 	|		<div class="red second">Red Two</div>
		// 	|		<div class="blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".second").prevAll();
		//	|	});
		//		returns the two divs with class of "prev".
		//		Running this code:
		//	|		query(".first").prevAll(".red");
		//		returns the one div with class "red prev" and innerHTML "Red One".
		return this._getRelatedUniqueNodes(query, function(node, ary){
			var pary = [];
			var prev = node;
			while((prev = prev.previousSibling)){
				if(prev.nodeType == 1){
					pary.push(prev);
				}
			}
			return pary;
		}); // dojo/NodeList
	},

	andSelf: function(){
		// summary:
		//		Adds the nodes from the previous dojo/NodeList to the current dojo/NodeList.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red prev">Red One</div>
		// 	|		Some Text
		// 	|		<div class="blue prev">Blue One</div>
		// 	|		<div class="red second">Red Two</div>
		// 	|		<div class="blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".second").prevAll().andSelf();
		//	|	});
		//		returns the two divs with class of "prev", as well as the div with class "second".
		return this.concat(this._parent);	// dojo/NodeList
	},

	//Alternate methods for the :first/:last/:even/:odd pseudos.
	first: function(){
		// summary:
		//		Returns the first node in this dojo/NodeList as a dojo/NodeList.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// returns:
		//		the first node in this dojo/NodeList
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		<div class="blue first">Blue One</div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue last">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".blue").first();
		//	|	});
		//		returns the div with class "blue" and "first".
		return this._wrap(((this[0] && [this[0]]) || []), this); // dojo/NodeList
	},

	last: function(){
		// summary:
		//		Returns the last node in this dojo/NodeList as a dojo/NodeList.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// returns:
		//		the last node in this dojo/NodeList
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="red">Red One</div>
		// 	|		<div class="blue first">Blue One</div>
		// 	|		<div class="red">Red Two</div>
		// 	|		<div class="blue last">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|	query(".blue").last();
		//	|	});
		//		returns the last div with class "blue",
		return this._wrap((this.length ? [this[this.length - 1]] : []), this); // dojo/NodeList
	},

	even: function(){
		// summary:
		//		Returns the even nodes in this dojo/NodeList as a dojo/NodeList.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// returns:
		//		the even nodes in this dojo/NodeList
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="interior red">Red One</div>
		// 	|		<div class="interior blue">Blue One</div>
		// 	|		<div class="interior red">Red Two</div>
		// 	|		<div class="interior blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".interior").even();
		//	|	});
		//		returns the two divs with class "blue"
		return this.filter(function(item, i){
			return i % 2 != 0;
		}); // dojo/NodeList
	},

	odd: function(){
		// summary:
		//		Returns the odd nodes in this dojo/NodeList as a dojo/NodeList.
		// description:
		//		.end() can be used on the returned dojo/NodeList to get back to the
		//		original dojo/NodeList.
		// returns:
		//		the odd nodes in this dojo/NodeList
		// example:
		//		assume a DOM created by this markup:
		//	|	<div class="container">
		// 	|		<div class="interior red">Red One</div>
		// 	|		<div class="interior blue">Blue One</div>
		// 	|		<div class="interior red">Red Two</div>
		// 	|		<div class="interior blue">Blue Two</div>
		//	|	</div>
		//		Running this code:
		//	|	require(["dojo/query", "dojo/NodeList-traverse"
		//	|	], function(query){
		//	|		query(".interior").odd();
		//	|	});
		//		returns the two divs with class "red"
		return this.filter(function(item, i){
			return i % 2 == 0;
		}); // dojo/NodeList
	}
});

return NodeList;
});

},
'dijit/registry':function(){
define([
	"dojo/_base/array", // array.forEach array.map
	"dojo/_base/window", // win.body
	"./main"	// dijit._scopeName
], function(array, win, dijit){

	// module:
	//		dijit/registry

	var _widgetTypeCtr = {}, hash = {};

	var registry =  {
		// summary:
		//		Registry of existing widget on page, plus some utility methods.

		// length: Number
		//		Number of registered widgets
		length: 0,

		add: function(widget){
			// summary:
			//		Add a widget to the registry. If a duplicate ID is detected, a error is thrown.
			// widget: dijit/_WidgetBase
			//		Any dijit/_WidgetBase subclass.
			if(hash[widget.id]){
				throw new Error("Tried to register widget with id==" + widget.id + " but that id is already registered");
			}
			hash[widget.id] = widget;
			this.length++;
		},

		remove: function(/*String*/ id){
			// summary:
			//		Remove a widget from the registry. Does not destroy the widget; simply
			//		removes the reference.
			if(hash[id]){
				delete hash[id];
				this.length--;
			}
		},

		byId: function(/*String|Widget*/ id){
			// summary:
			//		Find a widget by it's id.
			//		If passed a widget then just returns the widget.
			return typeof id == "string" ? hash[id] : id;	// dijit/_WidgetBase
		},

		byNode: function(/*DOMNode*/ node){
			// summary:
			//		Returns the widget corresponding to the given DOMNode
			return hash[node.getAttribute("widgetId")]; // dijit/_WidgetBase
		},

		toArray: function(){
			// summary:
			//		Convert registry into a true Array
			//
			// example:
			//		Work with the widget .domNodes in a real Array
			//		|	array.map(registry.toArray(), function(w){ return w.domNode; });

			var ar = [];
			for(var id in hash){
				ar.push(hash[id]);
			}
			return ar;	// dijit/_WidgetBase[]
		},

		getUniqueId: function(/*String*/widgetType){
			// summary:
			//		Generates a unique id for a given widgetType

			var id;
			do{
				id = widgetType + "_" +
					(widgetType in _widgetTypeCtr ?
						++_widgetTypeCtr[widgetType] : _widgetTypeCtr[widgetType] = 0);
			}while(hash[id]);
			return dijit._scopeName == "dijit" ? id : dijit._scopeName + "_" + id; // String
		},

		findWidgets: function(root, skipNode){
			// summary:
			//		Search subtree under root returning widgets found.
			//		Doesn't search for nested widgets (ie, widgets inside other widgets).
			// root: DOMNode
			//		Node to search under.
			// skipNode: DOMNode
			//		If specified, don't search beneath this node (usually containerNode).

			var outAry = [];

			function getChildrenHelper(root){
				for(var node = root.firstChild; node; node = node.nextSibling){
					if(node.nodeType == 1){
						var widgetId = node.getAttribute("widgetId");
						if(widgetId){
							var widget = hash[widgetId];
							if(widget){	// may be null on page w/multiple dojo's loaded
								outAry.push(widget);
							}
						}else if(node !== skipNode){
							getChildrenHelper(node);
						}
					}
				}
			}

			getChildrenHelper(root);
			return outAry;
		},

		_destroyAll: function(){
			// summary:
			//		Code to destroy all widgets and do other cleanup on page unload

			// Clean up focus manager lingering references to widgets and nodes
			dijit._curFocus = null;
			dijit._prevFocus = null;
			dijit._activeStack = [];

			// Destroy all the widgets, top down
			array.forEach(registry.findWidgets(win.body()), function(widget){
				// Avoid double destroy of widgets like Menu that are attached to <body>
				// even though they are logically children of other widgets.
				if(!widget._destroyed){
					if(widget.destroyRecursive){
						widget.destroyRecursive();
					}else if(widget.destroy){
						widget.destroy();
					}
				}
			});
		},

		getEnclosingWidget: function(/*DOMNode*/ node){
			// summary:
			//		Returns the widget whose DOM tree contains the specified DOMNode, or null if
			//		the node is not contained within the DOM tree of any widget
			while(node){
				var id = node.nodeType == 1 && node.getAttribute("widgetId");
				if(id){
					return hash[id];
				}
				node = node.parentNode;
			}
			return null;
		},

		// In case someone needs to access hash.
		// Actually, this is accessed from WidgetSet back-compatibility code
		_hash: hash
	};

	dijit.registry = registry;

	return registry;
});

},
'dijit/_BidiMixin':function(){
define([], function(){

	// module:
	//		dijit/_BidiMixin

	// UCC - constants that will be used by bidi support.
	var bidi_const = {
		LRM : '\u200E',
		LRE : '\u202A',
		PDF : '\u202C',
		RLM : '\u200f',
		RLE : '\u202B'
	};

	return {
		// summary:
		//		When has("dojo-bidi") is true, _WidgetBase will mixin this class.   It enables support for the textdir
		//		property to control text direction independently from the GUI direction.
		// description:
		//		There's a special need for displaying BIDI text in rtl direction
		//		in ltr GUI, sometimes needed auto support.
		//		In creation of widget, if it's want to activate this class,
		//		the widget should define the "textDir".

		// textDir: String
		//		Bi-directional support,	the main variable which is responsible for the direction of the text.
		//		The text direction can be different than the GUI direction by using this parameter in creation
		//		of a widget.
		//
		//		Allowed values:
		//
		//		1. "ltr"
		//		2. "rtl"
		//		3. "auto" - contextual the direction of a text defined by first strong letter.
		//
		//		By default is as the page direction.
		textDir: "",

		getTextDir: function(/*String*/ text){
			// summary:
			//		Gets the right direction of text.
			// description:
			//		If textDir is ltr or rtl returns the value.
			//		If it's auto, calls to another function that responsible
			//		for checking the value, and defining the direction.
			// tags:
			//		protected.
			return this.textDir == "auto" ? this._checkContextual(text) : this.textDir;
		},

		_checkContextual: function(text){
			// summary:
			//		Finds the first strong (directional) character, return ltr if isLatin
			//		or rtl if isBidiChar.
			// tags:
			//		private.

			// look for strong (directional) characters
			var fdc = /[A-Za-z\u05d0-\u065f\u066a-\u06ef\u06fa-\u07ff\ufb1d-\ufdff\ufe70-\ufefc]/.exec(text);
			// if found return the direction that defined by the character, else return widgets dir as defult.
			return fdc ? ( fdc[0] <= 'z' ? "ltr" : "rtl" ) : this.dir ? this.dir : this.isLeftToRight() ? "ltr" : "rtl";
		},

		applyTextDir: function(/*DOMNode*/ element, /*String?*/ text){
			// summary:
			//		Set element.dir according to this.textDir, assuming this.textDir has a value.
			// element:
			//		The text element to be set. Should have dir property.
			// text:
			//		If specified, and this.textDir is "auto", for calculating the right transformation
			//		Otherwise text read from element.
			// description:
			//		If textDir is ltr or rtl returns the value.
			//		If it's auto, calls to another function that responsible
			//		for checking the value, and defining the direction.
			// tags:
			//		protected.

			if(this.textDir){
				var textDir = this.textDir;
				if(textDir == "auto"){
					// convert "auto" to either "ltr" or "rtl"
					if(typeof text === "undefined"){
						// text not specified, get text from element
						var tagName = element.tagName.toLowerCase();
						text = (tagName == "input" || tagName == "textarea") ? element.value :
							element.innerText || element.textContent || "";
					}
					textDir = this._checkContextual(text);
				}

				if(element.dir != textDir){
					// set element's dir to match textDir, but not when textDir is null and not when it already matches
					element.dir = textDir;
				}
			}
		},

		enforceTextDirWithUcc: function(option, text){
			// summary:
			//		Wraps by UCC (Unicode control characters) option's text according to this.textDir
			// option:
			//		The element (`<option>`) we wrapping the text for.
			// text:
			//		The text to be wrapped.
			// description:
			//		There's a dir problem with some HTML elements. For some elements (e.g. `<option>`, `<select>`)
			//		defining the dir in different direction then the GUI orientation, won't display correctly.
			//		FF 3.6 will change the alignment of the text in option - this doesn't follow the bidi standards (static text
			//		should be aligned following GUI direction). IE8 and Opera11.10 completely ignore dir setting for `<option>`.
			//		Therefore the only solution is to use UCC (Unicode  control characters) to display the text in correct orientation.
			//		This function saves the original text value for later restoration if needed, for example if the textDir will change etc.
			if(this.textDir){
				if(option){
					option.originalText = text;
				}
				var dir = this.textDir == "auto" ? this._checkContextual(text) : this.textDir;
				return (dir == "ltr" ? bidi_const.LRE : bidi_const.RLE ) + text + bidi_const.PDF;
			}
			return text;
		},

		restoreOriginalText: function(origObj){
			// summary:
			//		Restores the text of origObj, if needed, after enforceTextDirWithUcc, e.g. set("textDir", textDir).
			// origObj:
			//		The element (`<option>`) to restore.
			// description:
			//		Sets the text of origObj to origObj.originalText, which is the original text, without the UCCs.
			//		The function than removes the originalText from origObj!
			if(origObj.originalText){
				origObj.text = origObj.originalText;
				delete origObj.originalText;
			}
			return origObj;
		},

		_setTextDirAttr: function(/*String*/ textDir){
			// summary:
			//		Setter for textDir.
			// description:
			//		Users shouldn't call this function; they should be calling
			//		set('textDir', value)
			if(!this._created || this.textDir != textDir){
				this._set("textDir", textDir);
				var node = null;
				if(this.displayNode){
					node = this.displayNode;
					this.displayNode.align = this.dir == "rtl" ? "right" : "left";
				}else{
					node = this.textDirNode || this.focusNode || this.textbox;
				}
				if(node){
					this.applyTextDir(node);
				}
			}
		}
	};
});

},
'dijit/_BidiSupport':function(){
define(["dojo/has", "./_WidgetBase", "./_BidiMixin"], function(has, _WidgetBase, _BidiMixin){

	// module:
	//		dijit/_BidiSupport

	/*=====
	return function(){
		// summary:
		//		Deprecated module for enabling textdir support in the dijit widgets.   New code should just define
		//		has("dojo-bidi") to return true, rather than manually requiring this module.
	};
	=====*/

	_WidgetBase.extend(_BidiMixin);

	// Back-compat with version 1.8: just including _BidiSupport should trigger bidi support in all the widgets.
	// Although this statement doesn't do much because the other widgets have likely already been loaded.
	has.add("dojo-bidi", true);

	return _WidgetBase;
});

},
'curam/cdsl/_base/PreferenceMap':function(){
/*
 * Copyright 2014 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 13-Oct-2014  SC [CR00446751] Initial version.
 */

define(['dojo/_base/declare',
        'dojo/_base/lang'
        ], function(declare, lang) {

  /**
   * A map of name value pairs of user and global preferences.
   * 
   * @name curam.cdsl._base.PreferenceMap
   * @namespace 
   */
  var PreferenceMap = declare(null,

  /**
   * @lends curam.cdsl._base.PreferenceMap.prototype
   */
  {    
    _preferences : null,
    _preferenceNames : null,
    
    constructor: function() {
      this._preferences = {};
      this._preferenceNames = [];
    },
    
    /**
     * Return the value of a specific preference.
     * @param {String} name The name of the preference.
     * @returns {String} The preference value.
     */
    getPreference: function(name) {
      return this._preferences[name];
    },
    
    /**
     * Return an array of preference names.
     * @returns {Array} The list of preference names.
     */
    getPreferenceNames: function() {
      return this._preferenceNames;
    },

    /**
     * Return a list of the names of all preferences.
     * @param {String} name The name of the preference.
     * @param {String} value The value of the preference.
     */
    addPreference: function(name, value) {
      this._preferences[name] = value;
      this._preferenceNames[this._preferenceNames.length] = name;
    }
    
  });
  
  return PreferenceMap;
});
},
'dojo/store/Cache':function(){
define(["../_base/lang","../when" /*=====, "../_base/declare", "./api/Store" =====*/],
function(lang, when /*=====, declare, Store =====*/){

// module:
//		dojo/store/Cache

var Cache = function(masterStore, cachingStore, options){
	options = options || {};
	return lang.delegate(masterStore, {
		query: function(query, directives){
			var results = masterStore.query(query, directives);
			results.forEach(function(object){
				if(!options.isLoaded || options.isLoaded(object)){
					cachingStore.put(object);
				}
			});
			return results;
		},
		// look for a queryEngine in either store
		queryEngine: masterStore.queryEngine || cachingStore.queryEngine,
		get: function(id, directives){
			return when(cachingStore.get(id), function(result){
				return result || when(masterStore.get(id, directives), function(result){
					if(result){
						cachingStore.put(result, {id: id});
					}
					return result;
				});
			});
		},
		add: function(object, directives){
			return when(masterStore.add(object, directives), function(result){
				// now put result in cache
				cachingStore.add(result && typeof result == "object" ? result : object, directives);
				return result; // the result from the add should be dictated by the masterStore and be unaffected by the cachingStore
			});
		},
		put: function(object, directives){
			// first remove from the cache, so it is empty until we get a response from the master store
			cachingStore.remove((directives && directives.id) || this.getIdentity(object));
			return when(masterStore.put(object, directives), function(result){
				// now put result in cache
				cachingStore.put(result && typeof result == "object" ? result : object, directives);
				return result; // the result from the put should be dictated by the masterStore and be unaffected by the cachingStore
			});
		},
		remove: function(id, directives){
			return when(masterStore.remove(id, directives), function(result){
				return cachingStore.remove(id, directives);
			});
		},
		evict: function(id){
			return cachingStore.remove(id);
		}
	});
};
lang.setObject("dojo.store.Cache", Cache);

/*=====
var __CacheArgs = {
	// summary:
	//		These are additional options for how caching is handled.
	// isLoaded: Function?
	//		This is a function that will be called for each item in a query response to determine
	//		if it is cacheable. If isLoaded returns true, the item will be cached, otherwise it
	//		will not be cached. If isLoaded is not provided, all items will be cached.
};

Cache = declare(Store, {
	// summary:
	//		The Cache store wrapper takes a master store and a caching store,
	//		caches data from the master into the caching store for faster
	//		lookup. Normally one would use a memory store for the caching
	//		store and a server store like JsonRest for the master store.
	// example:
	//	|	var master = new Memory(data);
	//	|	var cacher = new Memory();
	//	|	var store = new Cache(master, cacher);
	//
	constructor: function(masterStore, cachingStore, options){
		// masterStore:
		//		This is the authoritative store, all uncached requests or non-safe requests will
		//		be made against this store.
		// cachingStore:
		//		This is the caching store that will be used to store responses for quick access.
		//		Typically this should be a local store.
		// options: __CacheArgs?
		//		These are additional options for how caching is handled.
	},
	query: function(query, directives){
		// summary:
		//		Query the underlying master store and cache any results.
		// query: Object|String
		//		The object or string containing query information. Dependent on the query engine used.
		// directives: dojo/store/api/Store.QueryOptions?
		//		An optional keyword arguments object with additional parameters describing the query.
		// returns: dojo/store/api/Store.QueryResults
		//		A QueryResults object that can be used to iterate over.
	},
	get: function(id, directives){
		// summary:
		//		Get the object with the specific id.
		// id: Number
		//		The identifier for the object in question.
		// directives: Object?
		//		Any additional parameters needed to describe how the get should be performed.
		// returns: dojo/store/api/Store.QueryResults
		//		A QueryResults object.
	},
	add: function(object, directives){
		// summary:
		//		Add the given object to the store.
		// object: Object
		//		The object to add to the store.
		// directives: dojo/store/api/Store.AddOptions?
		//		Any additional parameters needed to describe how the add should be performed.
		// returns: Number
		//		The new id for the object.
	},
	put: function(object, directives){
		// summary:
		//		Put the object into the store (similar to an HTTP PUT).
		// object: Object
		//		The object to put to the store.
		// directives: dojo/store/api/Store.PutDirectives?
		//		Any additional parameters needed to describe how the put should be performed.
		// returns: Number
		//		The new id for the object.
	},
	remove: function(id){
		// summary:
		//		Remove the object with the specific id.
		// id: Number
		//		The identifier for the object in question.
	},
	evict: function(id){
		// summary:
		//		Remove the object with the given id from the underlying caching store.
		// id: Number
		//		The identifier for the object in question.
	}
});
=====*/

return Cache;
});

},
'curam/cdsl/store/IdentityApi':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(['dojo/_base/declare',
        'dojo/json'
        ],
    function(declare, json) {
  
  /**
   * @name curam.cdsl.store.IdentityApi
   * @namespace API for determining identity of objects used in CuramStore.
   * 
   * By default this class determines identity by using the "id" attribute
   * of items.
   * You can easily create subclasses and override the getIdentityAttributes()
   * function to return an array of attributes that should be used
   * for determining identity for compound keys.
   * 
   * The class can be then passed into the CuramStore constructor
   * and it will be used instead of the default implementation.
   */
  var IdentityApi = declare(null,
  /**
   * @lends curam.cdsl.store.IdentityApi.prototype
   */
  {
    /**
     * Gets identity for the specified Struct.
     * 
     * @param {curam/cdsl/Struct} item The Struct to get identity for.
     * 
     * @returns {Number|String} Identity value.
     */
    getIdentity: function(item) {
      var idProp = this.getIdentityPropertyNames()[0];
      if (typeof item[idProp] === 'object') {
        throw new Error(
  'Complex identity attributes are not supported by this implementation.');
      }
      return item[idProp];
    },
    
    /**
     * Resolve identity back into it's constituent property names and values
     * and return these in an object.
     *
     * @param identity The identity to parse.
     * @returns {Object} An object populated with properties corresponding
     *  to identity key values.
     */
    parseIdentity: function(identity) {
      var result = {};
      result[this.getIdentityPropertyNames()[0]] = identity;
      return result;
    },
    
    /**
     * Gives names of the properties used in identity determination. 
     * 
     * @returns {Array} Array of identity property names.
     */
    getIdentityPropertyNames: function() {
      return ['id'];
    }
  });
  
  return IdentityApi;
});

},
'curam/cdsl/types/codetable/CodeTables':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 26-Sep-2014  MV  [CR00445374] Implement generic data hookpoint.
 */

define(['dojo/_base/declare',
        'dojo/_base/lang',
        'dojo/Deferred',
        'curam/cdsl/_base/FacadeMethodCall',
        'curam/cdsl/Struct'
        ], function(
            declare, lang, Deferred, FacadeMethodCall, Struct) {

  /**
   * @name curam.cdsl.types.codetable.CodeTables
   * @namespace The main entry point to using code tables in CDSL.
   */
  var CodeTables = declare(null,
  /**
   * @lends curam.cdsl.types.codetable.CodeTables.prototype
   */
  {
    _connection: null,
    
    /**
     * Instantiates the API.
     * 
     * @param {curam/cdsl/_base/_Connection} connection The connection object
     *  to be used. Use an instance of curam/cdsl/connection/CuramConnection
     *  class.
     */
    constructor: function(connection) {
      if (!connection) {
        throw new Error("Missing parameter.");
      }
      if (typeof connection !== "object") {
        throw new Error("Wrong parameter type: " + typeof connection);
      }

      this._connection = connection;
    },
    
    /**
     * Gets a code table by name. The code table will correspond to
     * the currently logged in user's default locale.
     * 
     * Please note the object returned from this method is not updated
     * when code table content changes. In those cases you will need to call
     * this function again to get an updated CodeTable instance. 
     * 
     * @param {String} name Name of the code table to return.
     * 
     * @returns The requested code table or null if such code table
     *    is not loaded.
     */
    getCodeTable: function(name) {
      return this._connection.metadata().codetables()[name];
    },
    
    /**
     * Loads code tables for one or more specified facade method calls.
     * 
     * @param {[curam/cdsl/_base/FacadeMethodCall]} callToGetCodetablesFor
     *    Aray of method calls to load codetables for.
     * 
     * @returns {dojo/Promise::curam/cdsl/types/codetable/CodeTables} Promise
     *    which resolves when the code tables have been loaded. The resolved
     *    value is this instance of the CodeTables API, so that you can continue
     *    with the getCodeTable() method call.
     */
    loadForFacades: function(callsToGetCodetablesFor) {
      var deferred = new Deferred();
      /* Using nested require so that we don't end up with circular module
       * references due to CuramService indirectly requiring CodeTables API.
       */
      require(['curam/cdsl/request/CuramService'], lang.hitch(this,
          function(CuramService) {
            var service = new CuramService(this._connection),
                getCtsMethod = new FacadeMethodCall(
                    "CuramService", "getCodetables",
                    this._getInputStructsForLoadingCodetables(
                        callsToGetCodetablesFor));
    
            service.call([getCtsMethod]).then(
              lang.hitch(this, function(data) {
                deferred.resolve(this);
              }), function(err) {
                deferred.reject(err);
              });
          }));
      
      return deferred;
    },
    
    /**
     * Turn an array of FacadeMethodCall instances into an array of input
     * Structs for the CuramService.getCodetables() call.
     * 
     * @private
     *  
     * @param arrayOfMethodCalls The method calls.
     * @returns {Array} Array of structs.
     */
    _getInputStructsForLoadingCodetables: function(arrayOfMethodCalls) {
      var ret = [];
      for (var i = 0; i < arrayOfMethodCalls.length; i++) {
        ret.push(new Struct({
          service: arrayOfMethodCalls[i].intf(),
          method: arrayOfMethodCalls[i].method()
        }));
      }
      
      return ret;
    }
  });
  
  return CodeTables;
});

},
'curam/cdsl/types/FrequencyPattern':function(){
define(["dojo/_base/declare",
        "dojo/_base/lang"
        ], function(
            declare) {

  /**
   * @name curam.cdsl.types.FrequencyPattern
   * @namespace Represents a Curam Frequency Pattern.
   */
  var FrequencyPattern = declare(null,
  /**
   * TODO
   */
  {
    _code: null,
    _description: null,
    
    constructor: function(code, description) {
      this._code = code;
      this._description = description;
    },
    
    getCode: function(){
      return this._code;
    },
    
    getDescription: function(){
      return this._description;
    }
  });
  
  return FrequencyPattern;
});

},
'curam/util/LocalConfig':function(){
/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2012,2021. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 04-Feb-2021  BOS [RTC267998]  Make the code more defensive to guard
 * against the local configuration being available for some reason.
 * 04-Mar-2014  MV  [CR00421036] Added implementation.
 * 27-Feb-2014  MV  [CR00419961] Initial version.
 */

/**
 * @name curam.util.LocalConfig
 * @namespace Provides support for accessing application properties
 *    from JavaScript and overriding their values locally.
 *      <p/>
 *    The basic idea is that desired options are determined at application
 *    init time (in the main app page) and they are "seeded" using this API.
 *    From then on JavaScript code can access the values of these options.
 *      <p/>
 *    It is then possible to override individual option values and the API
 *    will persist the overrides in local Web storage. This allows
 *    for individual users to control the behaviour of the application
 *    without the need to set options globally for all users in admin pages.
 *      <p/>
 *    Please note the option values and overrides are stored in the "top"
 *    browser window, even if the API is used from nested iframes.
 */
define([
        ], function() {
  
  // the code below ensures we store the values globally
  var globalName = function(name) {
        return 'curam_util_LocalConfig_' + name;
      },
      initGlobal = function(name, value) {
        var gName = globalName(name);
        
        // only initialize if it doesn't already exist!
        if (typeof top[gName] === 'undefined') {
          top[gName] = value;
        }
        
        return top[gName];
      },
      getGlobal = function(name) {
        return top && top !=null ? top[globalName(name)] : undefined;
      };
  
  initGlobal('seedValues', {}),
  initGlobal('overrides', {});
  
  var _checkIsString = function(value, valName) {
    if (typeof value !== 'undefined' && typeof value !== 'string') {
      throw new Error('Invalid ' + valName + ' type: ' + typeof value
          + '; expected string');
    }
  };
  
  var LocalConfig =
  /**
   * @lends curam.util.LocalConfig.prototype
   */
  {
    /**
     * Sets the "global" value for the option.
     * This can be later overriden to provide a local value. 
     *
     * @param name Name of the option to set.
     * @param value Value of the option to set. If this is undefined
     *  then default value will be used instead.
     * @param defaultValue Default value to be used if value is not specified.
     */
    seedOption: function(name, value, defaultValue) {
      _checkIsString(value, 'value');
      _checkIsString(defaultValue, 'defaultValue');
      
      // code below treats null as a valid value to be used
      getGlobal('seedValues')[name] =
          (typeof value !== 'undefined') ? value : defaultValue;
    },
    
    /**
     * Sets local override for the value of given option.
     * The override is persisted to local Web storage, if available.
     * 
     * @param name Name of the option to override.
     * @param value The local value to be used.
     */
    overrideOption: function(name, value) {
      _checkIsString(value, 'value');

      // persist the value, if possible
      if(typeof(Storage) !== "undefined") {
        localStorage[name] = value;

      // otherwise just store in memory - override will not be permanent
      } else {
        getGlobal('overrides')[name] = value;
      }
    },
    
    /**
     * Reads the value of the given option. It takes the values in the following
     * precedence order. The first that is found is returned.
     * <ul>
     * <li>override from local persistent Web storage</li>
     * <li>override from session memory</li>
     * <li>the global value</li>
     * <li>return the provided default value</li>
     * </ul>
     * 
     * @param name Name of the option to read.
     * @param defaultValue Default value to return if value is not set.
     * @returns Value of the option or provided default value.
     */
    readOption: function(name, defaultValue) {
      var seedValues = getGlobal('seedValues');
	  var overridesValues = getGlobal('overrides');
      _checkIsString(defaultValue, 'defaultValue');

      var finalValue = null;

      // use local persistent value, if possible and if available
      if (typeof(Storage) !== "undefined"
          && localStorage && typeof localStorage[name] !== 'undefined') {
        finalValue = localStorage[name];
    
      // otherwise fall back to local non-persistent override
      } else if (overridesValues && typeof overridesValues[name] !== 'undefined') {
        finalValue = overridesValues[name];
  
      // otherwise fall back to the seed value
      } else if (seedValues && typeof seedValues[name] !== 'undefined') {
        finalValue = seedValues[name];
  
      // otherwise fall back to the specified default value
      } else {
        finalValue = defaultValue;
      }

      return finalValue;
    },
    
    /**
     * Completely removes the option from configuration.
     * After using this method the readOption() will return the provided
     * default value.
     *
     * @param name Name of the option to clear.
     */
    clearOption: function(name) {
      if(typeof(Storage) !== "undefined") {
        localStorage.removeItem(name);
      }
      delete getGlobal('overrides')[name];
      delete getGlobal('seedValues')[name];
    }
  };
  
  return LocalConfig;
  
});

},
'curam/cdsl/_base/FacadeMethodCall':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 26-Sep-2014  MV  [CR00445374] Implement generic data hookpoint.
 */

define(['dojo/_base/declare',
        "dojo/_base/lang",
        'dojo/json',
        'dojo/_base/array'
        ], function(
            declare, lang, json, array) {

  /**
   * @name curam.cdsl.request.FacadeMethodCall
   * @namespace Represents a method of a Curam facade to be called. 
   */
  var FacadeMethodCall = declare(null,
  /**
   * @lends curam.cdsl.request.FacadeMethodCall.prototype
   */
  {
    _intf: null,

    _method: null,

    _structs: null,
    
    _metadata: null,
    
    _options: null,

    /**
     * 
     * Creates an instance.
     *
     * @param {String} intf Name of the server interface to call.
     * @param {String} method Name of the server interface method to call.
     * @param {[curam/cdsl/Struct]} structs Array of Struct object instances
     *     to pass as parameters to the server interface call.
     * @param {Object} options An object containing additional options.
     *     The following options are supported:<pre><code>
     *        {
     *          formatted: true|false,
     *          raw: true|false,
     *          dataAdapter: {
     *            onRequest: {
     *              onItem: function(path, val) { return val; },
     *              onStruct: function(data) {}
     *            },
     *            onResponse: {
     *              onItem: function(path, val) { return val; },
     *              onStruct: function(data) {}
     *            }
     *          }
     *        }</code></pre>
     *     The <code>onRequest</code> functions will be applied to raw data being sent
     *     TO the server, after the CDSL API has processed the data.
     *     The <code>onResponse</code> functions will be applied to raw data being received
     *     FROM the server, before the CDSL API has processed the data.
     *     All of the above functions are optional - you are free to only
     *     include the ones you are interested in using.
     *     <p/>
     *     The <code>onItem</code> function takes two arguments.
     *     The <code>path</codepre> argument is a fully qualified path to the data item
     *     within its Struct. For example:<pre><code>
     *       new Struct({
     *         id: 45  // path == 'id'
     *         nestedStruct: { name: "some name" }  // path == 'nestedStruct.name'
     *         dtls: [
     *           { id: 32 }, // path == 'dtls[0].id'
     *           { id: 33 }  // path == 'dtls[1].id'
     *         ]
     *       })</code></pre>
     *     The <code>val</code> argument contains the current value of the data
     *     item.
     *     The function is expected to return the new value for the data item.
     *     Please note you must always return a value, otherwise the data item
     *     will be undefined. By default you should return the current value.
     *     <p/>
     *     The <code>onStruct</code> function takes one argument which provides access
     *     to the complete contained data. You can examine or modify data
     *     as needed. No return value is expected.
     */
    constructor: function(intf, method, structs, options) {
      if (structs && !lang.isArray(structs)) {
        throw new Error("Unexpected type of the 'structs' argument.");
      }
      
      this._intf = intf;
      this._method = method;
      this._structs = structs ? structs : [];
      this._options = {};
      lang.mixin(this._options, {
        // default values below
        raw: true,
        formatted: false,
        sendCodetables: true,
        dataAdapter: null
      },
      // user specified values below
      options);
    },

    /**
     * @private
     * @param base
     * @returns {String}
     */
    url: function(base) {
      return base + '/' + this._intf + '/' + this._method;
    },
    
    /**
     * @private
     * @param metadata
     */
    _setMetadata: function(metadata) {
      this._metadata = metadata;
    },

    /**
     * @private
     * @returns
     */
    toJson: function() {
      var data = {
        service: this._intf,
        method: this._method,
        data: array.map(this._structs, lang.hitch(this, function(item) {
          item.setDataAdapter(this._options.dataAdapter);
          return item.getData();
        })),
        configOptions: {
          'response-type': this._responseType(),
          'send-codetables': this._sendCodetables()
        }
      };
      
      if (this._metadata && this._metadata.queryOptions) {
        data.queryOptions = this._metadata.queryOptions;
      }
      
      return json.stringify(data);
    },
    
    /**
     * Set or get the value of flag specifying whether server should return
     * formatted data.
     * 
     * The default value is false, which means do not send formatted data.
     *
     * @param {boolean} sendFormatted Specifies whether the server should
     *    return formatted data. If this parameter is not passed at all
     *    the function returns the current value.
     * @returns True if formatted data should be requested, otherwise false.
     *    When used as setter the function returns the old value of the flag.
     */
    formatted: function(sendFormatted) {
      return this._getOrSet(sendFormatted, this._options, 'formatted');
    },
    
    raw: function(sendRaw) {
      return this._getOrSet(sendRaw, this._options, 'raw');
    },
    
    /**
     * @private
     * @returns {String}
     */
    _responseType: function() {
      if (this.raw() && this.formatted()) {
        return 'both';

      } else if (this.raw()) {
        return 'raw';

      } else if (this.formatted()) {
        return 'formatted';
      }
      
      throw new Error(
          'Invalid response type: neither raw nor formatted was requested.');
    },
    
    /**
     * @private
     * @param sendCodetables
     * @returns
     */
    _sendCodetables: function(sendCodetables) {
      return this._getOrSet(sendCodetables, this._options, 'sendCodetables');
    },
    
    /**
     * @private 
     * @param value
     * @param options
     * @param property
     * @returns
     */
    _getOrSet: function(value, options, property) {
      if (typeof value === 'undefined') {
        return options[property];

      } else {
        var oldValue = options[property];
        options[property] = value;
        return oldValue;
      }
    },
    
    /**
     * private
     * @returns {String}
     */
    intf: function() {
      return this._intf;
    },

    /**
     * @private
     * @returns {String}
     */
    method: function() {
      return this._method;
    },
    
    dataAdapter: function(adapter) {
      if (!adapter) {
        return this._options.dataAdapter;
      }
      
      this._options.dataAdapter = adapter;
    }
  });
  
  return FacadeMethodCall;
});

},
'cm/_base/_dom':function(){
define(["dojo/dom", 
        "dojo/dom-style",
        "dojo/dom-class"], function(dom, domStyle, domClass) {
  
/*
  This file includes generic functions for use with the DOM.
*/

/*
 * Modification History
 * --------------------
 * 24-Mar-2010 BD  [CR00191575] Added exit function to getParentByType() when 
 *                              the document root is reached. Handles the 
 *                              iframe scenario.
 */

  var cm = dojo.global.cm || {};
  dojo.global.cm = cm;

  dojo.mixin(cm, {
    nextSibling: function(node, tagName) {
      //  summary:
            //            Returns the next sibling element matching tagName
      return cm._findSibling(node, tagName, true);
    },
    
    prevSibling: function(node, tagName) {
      //  summary:
            //            Returns the previous sibling element matching tagName
      return cm._findSibling(node, tagName, false);
    },
    
    getInput: function(name, multiple) {
      if(!dojo.isString(name)){
        return name;
      }
      var inputs = dojo.query("input[name='" + name + "'],select[name='" + name + "']");
      return multiple ? (inputs.length > 0 ? inputs : null) 
                                                                                  : (inputs.length > 0 ? inputs[0]:null);
    },
    
    getParentByClass: function(node, classStr) {
      // summary:
      //   Returns the first parent of the node that has the require class
      node = node.parentNode;
      while (node) {
        if(domClass.contains(node, classStr)){
          return node;
        }
        node = node.parentNode;
      }
      return null;
    },
    
    getParentByType: function(node, type) {
      // summary:
      //   Returns the first parent of the node that has the require class
      node = node.parentNode;
      type = type.toLowerCase();
      var docRoot = "html";
      while (node) {
        // Give up when you reach the root of the doc,
        // applies to iframes
        if(node.tagName.toLowerCase() == docRoot){
          break;
        }
        if(node.tagName.toLowerCase() == type){
          return node;
        }
        node = node.parentNode;
      }
      return null;
    },
  
    replaceClass: function(node, newCls, oldCls) {
      // summary:
      //   Replaces a single css class with another.
      //   node:   The node to operate on.
      //   newCls: The class to be added
      //   oldCls: The class to be removed
      domClass.remove(node, oldCls);
      domClass.add(node, newCls);
    },
    
    setClass: function(/* HTMLElement */node, /* string */classStr){
                  //      summary
                  //      Clobbers the existing list of classes for the node, replacing it with
                  //      the list given in the 2nd argument. Returns true or false
                  //      indicating success or failure.
                  node = dom.byId(node);
                  var cs = new String(classStr);
                  try{
                          if(typeof node.className == "string"){
                                  node.className = cs;
                          }else if(node.setAttribute){
                                  node.setAttribute("class", classStr);
                                  node.className = cs;
                          }else{
                                  return false;
                          }
                  }catch(e){
                          dojo.debug("dojo.html.setClass() failed", e);
                  }
                  return true;
          },
  
    _findSibling: function(node, tagName, forward) {
      
      if(!node) { return null; }
      if(tagName) { tagName = tagName.toLowerCase(); }
      var param = forward ? "nextSibling":"previousSibling";
            do {
                    node = node[param];
            } while(node && node.nodeType != 1);
  
            if(node && tagName && tagName != node.tagName.toLowerCase()) {
                    return cm[forward ? "nextSibling":"prevSibling"](node, tagName);
            }
            return node;  //      Element
    },
    
    getViewport: function(){
                  // summary: returns a viewport size (visible part of the window)
          
                  // FIXME: need more docs!!
                  var d = dojo.doc, dd = d.documentElement, w = window, b = dojo.body();
                  if(dojo.isMozilla){
                          return {w: dd.clientWidth, h: w.innerHeight};   // Object
                  }else if(!dojo.isOpera && w.innerWidth){
                          return {w: w.innerWidth, h: w.innerHeight};             // Object
                  }else if (!dojo.isOpera && dd && dd.clientWidth){
                          return {w: dd.clientWidth, h: dd.clientHeight}; // Object
                  }else if (b.clientWidth){
                          return {w: b.clientWidth, h: b.clientHeight};   // Object
                  }
                  return null;    // Object
          },
          
          toggleDisplay: function(node) {
            domStyle.set(node, "display", domStyle.get(node, "display") == "none" ? "": "none");
          },
          
          
          
          endsWith: function(/*string*/str, /*string*/end, /*boolean*/ignoreCase){
                  // summary:
                  //      Returns true if 'str' ends with 'end'
          
                  if(ignoreCase){
                          str = str.toLowerCase();
                          end = end.toLowerCase();
                  }
                  if((str.length - end.length) < 0){
                          return false; // boolean
                  }
                  return str.lastIndexOf(end) == str.length - end.length; // boolean
          },
          
          hide: function(n){
                  domStyle.set(n, "display", "none");
          },
          
          show: function(n){
                  domStyle.set(n, "display", "");
          }
  });
  
  return cm;
});

},
'curam/cdsl/types/codetable/CodeTable':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

define(['dojo/_base/declare',
        './CodeTableItem'
        ], function(
            declare, CodeTableItem) {

  /**
   * @name curam.cdsl.types.codetable.CodeTable
   * @namespace
   *    Note this class represents a code table for one specific locale.
   *    It does not have capability of holding codes for multiple locales.
   */
  var CodeTable = declare(null,
  /**
   * @lends curam.cdsl.types.codetable.CodeTable.prototype
   */
  {
    _name: null,

    _defaultCode: null,
    
    _codes: null,
    
    _items: null,
    
    /**
     * Not intended for direct instantiation by CDSL API users.
     * 
     * @private
     * 
     * @param name
     * @param defaultCode
     * @param codes
     */
    constructor: function(name, defaultCode, codes) {
      this._name = name;
      this._defaultCode = defaultCode;
      this._codes = this._parseCodesIntoObject(codes);
    },
    
    /**
     * Gets all items of this code table.
     * 
     * @returns {[curam/cdsl/types/codetable/CodeTableItem]} Array of items
     *  contained in this code table.
     */
    listItems: function() {
      this._initItems(this._codes, this._defaultCode);

      var ret = [];
      for (code in this._items) {
        ret.push(this._items[code]);
      }
      return ret;
    },
    
    /**
     * @private
     * 
     * @param codesArray
     * @returns {___anonymous1799_1800}
     */
    _parseCodesIntoObject: function(codesArray) {
      var ret = {};
      
      if (codesArray) {
        for (var i = 0; i < codesArray.length; i++) {
          var raw = codesArray[i];
          var code = raw.split(":")[0];
          var desc = raw.slice(code.length + 1);
          ret[code] = desc;
        }
      }
      
      return ret;
    },
    
    /**
     * @private
     * 
     * @param codes
     * @param defaultCode
     */
    _initItems: function(codes, defaultCode) {
      if (!this._items) {
        this._items = {};
        
        for (code in codes) {
          var cti = new CodeTableItem(code, codes[code]);
          cti.isDefault(code === defaultCode);
          this._items[code] = cti;
        }
      }
    },
    
    /**
     * Gets item for the specified code.
     * 
     * @param {String} code The code to get code table item for.
     * @returns {curam/cdsl/types/codetable/CodeTableItem} The requested code
     *  table item or undefined if no such item exists.
     */
    getItem: function(code) {
      this._initItems(this._codes, this._defaultCode);

      return this._items[code];
    }
  });
  
  return CodeTable;
});

},
'curam/cdsl/connection/CuramConnection':function(){
/*
 *  IBM Confidential
 *  
 *  OCO Source Materials
 *  
 *  Copyright IBM Corporation 2013,2020.
 *  
 *  The source code for this program is not published or otherwise divested
 *  of its trade secrets, irrespective of what has been deposited with the 
 *  US Copyright Office
 */

define(['dojo/_base/declare',
        'dojo/request/registry',
        'curam/cdsl/_base/_Connection',
        'curam/util'
        ], function(
            declare, request, _Connection, util) {

  /**
   * @name curam.cdsl.connection.CuramConnection
   * @namespace Allows invoking Curam facade methods via HTTP.
   */
  var CuramConnection = declare(_Connection,
  /**
   * @lends curam.cdsl.connection.CuramConnection.prototype
   */
  {
    _baseUrl: null,

    /**
     * Creates an instance of connection.
     * 
     * @param {String} baseUrl Base URL of Curam data service. Typically
     *    this will take the following form:
     *    http://&lt;host&gt;:&lt;port&gt;/Curam/dataservice 
     */
    constructor: function(baseUrl) {
      this._baseUrl = baseUrl;
    },

    /**
     * This method is not intended to be directly called by API clients.
     * @private
     */
    invoke: function(methodCall, timeout) {
      this.inherited(arguments);
      var topWindow = util.getTopmostWindow();
      return request(methodCall.url(this._baseUrl), {
        data: methodCall.toJson(),
        method: 'POST',
        headers: {"Content-Encoding": "UTF-8",
                  "csrfToken": topWindow.csrfToken},
        query: null,
        preventCache: true,
        timeout: timeout ? timeout : this._DEFAULT_REQUEST_TIMEOUT,
        handleAs: 'text'
      });
    }
  });
  
  return CuramConnection;
});

},
'dojo/store/util/QueryResults':function(){
define(["../../_base/array", "../../_base/lang", "../../when"
], function(array, lang, when){

// module:
//		dojo/store/util/QueryResults

var QueryResults = function(results){
	// summary:
	//		A function that wraps the results of a store query with additional
	//		methods.
	// description:
	//		QueryResults is a basic wrapper that allows for array-like iteration
	//		over any kind of returned data from a query.  While the simplest store
	//		will return a plain array of data, other stores may return deferreds or
	//		promises; this wrapper makes sure that *all* results can be treated
	//		the same.
	//
	//		Additional methods include `forEach`, `filter` and `map`.
	// results: Array|dojo/promise/Promise
	//		The result set as an array, or a promise for an array.
	// returns:
	//		An array-like object that can be used for iterating over.
	// example:
	//		Query a store and iterate over the results.
	//
	//	|	store.query({ prime: true }).forEach(function(item){
	//	|		//	do something
	//	|	});

	if(!results){
		return results;
	}

	var isPromise = !!results.then;
	// if it is a promise it may be frozen
	if(isPromise){
		results = lang.delegate(results);
	}
	function addIterativeMethod(method){
		// Always add the iterative methods so a QueryResults is
		// returned whether the environment is ES3 or ES5
		results[method] = function(){
			var args = arguments;
			var result = when(results, function(results){
				Array.prototype.unshift.call(args, results);
				return QueryResults(array[method].apply(array, args));
			});
			// forEach should only return the result of when()
			// when we're wrapping a promise
			if(method !== "forEach" || isPromise){
				return result;
			}
		};
	}

	addIterativeMethod("forEach");
	addIterativeMethod("filter");
	addIterativeMethod("map");
	if(results.total == null){
		results.total = when(results, function(results){
			return results.length;
		});
	}
	return results; // Object
};

lang.setObject("dojo.store.util.QueryResults", QueryResults);

return QueryResults;

});

},
'dijit/main':function(){
define([
	"dojo/_base/kernel"
], function(dojo){
	// module:
	//		dijit/main

/*=====
return {
	// summary:
	//		The dijit package main module.
	//		Deprecated.   Users should access individual modules (ex: dijit/registry) directly.
};
=====*/

	return dojo.dijit;
});

},
'curam/util':function(){
/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2012,2023. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 10-Jan-2023 BOS [SPM-126610] Moved updateInlineTabMenuState() and manageInlineTabMenuStates()
 * function to curam.util.TabActionsMenu.js
 * 09-Jan-2023 LS [SPM-1260841] Skip to Main Content Link not accessible by keyboard.
 * 03-Jan-2023 JD [SPM-125865] Updated span to h1 in dojo query in _findAppropriateDynamicTitle.
 * 19-Dec-2022 BOS [SPM-126480] Added updateInlineTabMenuState() function for inline tab actions.
 * 14-Dec-2022 CMC[SPM-105222] Updated file download logic in clickHandlerForListActionMenu(). 
 * 13-Oct-2022 FN [SPM125042] Added _addContextToWidgetForScreenReader to pick up cluster context in java widget render ListBodyRendererConcernMember.
 * 09-Jun-2022 CM [RTC277941] Remove code around the session storage localRefresh key.
 * 06-May-2022 LS  [RTC277951] Date picker for received date is displayed open blocking content when new evidence is opened.
 * 21-Apr-2022 GB  [RTC277797] Added a new parameter to insertAriaLiveLabelRecordSearchItem related to the heading id to be used. 
 * 06-Apr-2022 LS [RTC277567] Check Boxes - selecting checkbox on various screen  is causing the screen to align the focus to the top of the screen.
 * 30-Mar-2022 JD  [RTC270575] Updated URLs for search widget so params are ignored by secure URLs solution.
 * 29-Mar-2022 CM  [RTC277374] Updated setupPreferencesLink() and setupLocaleLink() to remove outdated css class 
 * and _curamDisable attribute.
 * 25-Mar-2022 CM  [RTC277060] Updated _doRedirectWindow() to allow application to be refreshed from an ActionPage,
 * when the LOCALE_REFRESH parameter is set to true.
 * 23-Mar-2022 CM  [RTC277294] Updated openLocaleNew() to allow dialog to open correctly.
 * 21-Mar-2022 CM  [RTC277294] Added openLocaleNew() function to open the new Language modal.
 * 09-Mar-2022 CM  [RTC276771] Updated _doRedirectWindow() function to add check for LOCALE_REFRESH, and 
 * refresh the entire application if parameter is found. 
 * 14-Oct-2021 LS  [RTC274183] Focus doesn't start in right place on the page
 * 07-Oct-2021 FN  [RTC255412] Added _addAccessibilityMarkupInAddressClustersWhenContextIsMissing function.
 * 06-Oct-2021 BOS [RTC273988] Updated the addCarbonModalButton() function to put the dataTestID into
 * object rather than separate argument.
 * 06-Oct-2021 GB  [RTC273983] Adding the new parameter to returnFocusToPopupActionAnchorElement function.
 * 29-Sep-2021  GB [RTC273619] Added data-testid to the carbon modal buttons.
 * 09-Sep-2021  FN  [RTC268445] Refactor toClipboard() since IE11 drop to make it work on supported modern browsers.
 * 16-Aug-2021  LS  [RTC272772] Move focus to tab warning or error modal when opened
 * 11-Aug-2021  SK [RTC272800] Progress spinner invocation has changed to the decoupled version.
 * 15-Jun-2021  SK [RTC272800] Memory leak prevention.
 * 03-Jun-2021  BD [RTC271240] Added returnFocusToPopupActionAnchorElement and addPlaceholderFocusClassToEventOrAnchorTag 
 * functions.
 * 18-May-2021  FN [RTC265093] Updated loadInformationalMsgs and setFocusOnField to set element attributes and focus
 * on container-messages-ul.
 * 13-May-2021  BD [RTC266250] Updated loadInformationalMsgs, setFocus and doSetFocus to return container-messages-ul or
 * error-messages element.
 * 04-May-2021  LS [RTC270554] Remove css-vars-ponyfill package to improve performance.
 * 26-Apr-2021  ZV [RTC270145] Update word dialog to Carbon.
 * 31-Mar-2021  FN [RTC265093] Updated loadInformationalMsgs() to explicitly focus on the correct page where the error message
 * details should be announced by the screen-reader. Also updated setFocusOnField() to fix Shift+Tab issue on error messages
 * in IE11 with screen reader.
 * 15-Mar-2021 BOS [RTC269160] Handling buttons configured as JSON object rather than HTML string.
 * 11-Mar-2021  FN [RTC265093] Updated loadInformationalMsgs() to explicitly focus on the correct page where the error message
 * details should be announced by the screen-reader.
 * 09-Mar-2021  JD [RTC268623] Updated loadInformationalMsgs() to keep focus on navigation tab instead of info
 * message if the navigation tab was the last element selected.
 * 18-Feb-2021  BD [RTC268079] Update method setFocus to set the tabindex and focus on the current iframe. Fix 
 * condition to check for Edge Chromium browser.
 * 05-Feb-2021  JD [RTC265373] Updated insertAriaLiveLabelRecordSearchItem so text is announced by screen reader
 * when search results are returned.
 * 28-Jan-2021  BD [RTC265376] Changed from label to span the element that contains the css class fileuploadButton.
 * 02-Dec-2020 BOS [RTC266982] Added functions to handle the addition of button to CuramDialog.
 * 11-Nov-2020  DM [RTC266277] Load css-vars-ponyfill to implement custom css properties in IE11.
 * 08-May-2020  JD [RTC259879] Adjusted timeouts when setting focus in IE11, added setFocusOnField() and _createHiddenInputField().
 * 01-Apr-2020  FN [RTC250370] Updated updateListControlReadings in order to get links in lists read out by a screen-reader.
 * 31-Mar-2020  FN [RTC257127] Updated _setBrowserTabTitle() to fix issue on embedded pages with in-page navigation
 *                             tabs throwing error message(s).
 * 05-Feb-2020  FN [RTC254720] Added logic in _findAppropriateDynamicTitle() to fix issue on pages with 
 *                             error messages where browser tab title was not updating correctly to reflect the 
 *                             presence of an error message.
 * 29-Jan-2020  JD [RTC257000] Fixed issue in IE11 where main content area is printed twice when context panel
 *                             is collapsed.
 * 17-Jan-2020  SH [RTC255274] Don't return the tabButton if in a modal in doSetFocus.
 * 08-Jan-2020  SH [RTC255274] More updates to setFocus() for focusing on CKEditor fields.
 * 04-Dec-2019  AA [RTC255177] Updated setBrowserTabTitle to handle the scenario where title is undefined and 
 *                             curam.util._browserTabTitleData does not contain staticTabTitle, separator 
 *                             and appNameFirst.
 * 03-Dec-2019  SH [RTC255274] Prevent focus being moved in setFocus if the user has already 
 *                             focused on a form element manually.
 * 18-Nov-2019  CMC[RTC239509] Updated printPage() function.
 * 07-Nov-2019  GG [RTC251143] Added lastOpenedTabButton variable to store the last opened tab to manage the focus 
 * on just opened tabs and updated doSetFocus to exclude hidden fields and use lastOpenedTabButton to set focus.
 * 17-Oct-2019  FN [RTC251289] Added aria-hidden attribute to input elements with the attribute 'hidden-button'.
 * 14-Oct-2019  FN [RTC250878] Minor change in the curam.debug.log when returning the application section title.
 * 10-Oct-2019  CM [RTC253438] Added getter and setter methods for new variable exitingIEGScript variable.
 * 04-Oct-2019  FN [RTC250878] Fixing the default application section title to be reflected
 *                             in the browser tab. This default changes upon the user used for logging.
 * 03-Oct-2019  AA [RTC251481] Added tabButtonClicked variable to store the tab button clicked in the navigation
 *                             tab. Added set and get functions. Changed the doSetFocus function to return
 *                             the tab button when the user is navigating through tabs.
 * 20-Sep-2019  AA [RTC250096] Added function to remove a property from the session storage. Updated setFocus to 
 *                             set the focus on the search button when the property curamDefaultActionId 
 *                             is defined.
 * 29-Aug-2019	FN [RTC249991] Additional updates to _findAppropriateDynamicTitle() to update the browser 
 * 							   title to a widget title when a session timeout closes.
 * 08-Aug-2019  CM [RTC248363] Updated updateListControlReadings function to check for controls 
 *                             on lists when sorting is triggered, as some links were missing 
 *                             aria-labels due to not having any controls from the initial loading.
 * 29-Jul-2019	FN [RTC249991] Updated _findAppropriateDynamicTitle() to update the browser title to
 * 							   a modal title when a pop-up window closes.
 * 22-Jul-2019  JD [RTC250498] Updated doSetFocus function to cater for multi-select fields and
 * 							   action pop-up links.
 * 01-Jul-2019  GG [RTC245742] Added extendXHR to update session timeout time into sessionStorage.
 * 18-Jun-2019  CMC[RTC248518] Removing 'Select' from aria labels on links within sortable lists.
 * 05-Jun-2019  SK [RTC248394] Updates accessibility readings gradually for IE only.
 * 28-May-2019  CM [RTC242923] Added fileUploadOpenFileBrowser() function.
 * 27-May-2019  SH [RTC240305] Decode url params before comparing them in isSameUrl().
 * 26-Apr-2019  SK [RTC241554] Added methods for list control readings set up and
 *                             updating.
 * 11-Feb-2019  CM [RTC240091] Fixing accessibility issue on date time widget field. 
 *                             Function addLayoutStylingOnDateTimeWidgetOnZoom() adds
 *                             css styling.
 * 17-Jan-2019  CM [RTC241236] Fixing accessibility issue where page title was set to 
 *                             undefined on a UIM page with in-page navigation. Function
 *                             getPageTitleOnContentPanel() updates the page title for this
 *                             edge case.
 * 30-NOV-2018  BD [RTC238774] Added method to check if the window contains 
 * 							   when the user clicks to print the page. The method 
 * 							   _prepareContentPrint() creates the wrapper to include
 * 							   the content of iframe and disable the iframe on the page. 
 * 							   After prints the page, the method _deletePrintVersion()
 *                             removes the wrapper and enables the iframe again,
 *                             returning to the original.
 * 24-Oct-2018	CMC[RTC225885] Fixing accessibility issue where initial focus was not  
 * 							   being placed correctly on the first editable page element.
 * 23-Oct-2018   SK [RTC237984] Modified the focus setter to correctly handle the
 *                             select Dijit widget and to return field for focusing
 *                             rather than doing the focus inside.
 * 25-Sep-2018  SK [RTC235825] Corrected the key handling to take into account the 
 *                             potentially different registry entry for the
 *                             Select based dropdown. 
* 25-Sep-2018  SK [RTC235825] Corrected the key handling to take into account the 
 *                             potentially different registry entry for the
 *                             Select based dropdown.
 * 23-Aug-2018  BD [RTC229425]  Included publish to alert when the page will be redirected 
 *                               to the logout page. 
 * 21-Aug-2018  SK [RTC230487]   Optimization of the table striping code.
 * 09-May-2018  BD [RTC225963]   Fixed accessibility issues related with alerts the search results.
 * 11-Apr-2018  BD [RTC210785]  Removed scrolltop for ios devices.
 * 27-Mar-2018  JD [RTC224593]  Fixed issue where the CSS of each row in a list
 * 								was set to 'even' after sorting the list.
 * 11-OCT-2017  JD [RTC206743]  Added GENERIC_ERROR_MODAL_MAP so parameters can be
 * 								passed to generic-modal-error.jspx without using
 * 								appending them to the url.
 * 12-07-2017   SK [RTC201190]  Moved clipboard related JS from the old JS file and
 *                              the custom tag.
 * 31-Mar-2017  GB [RTC182027]  Encoding URL for openGenericErrorModalDialog and 
 * 								removing the parameters from the request.
 * 27-Mar-2017  FG [191880]     Removed references to portlets.
 * 23-Feb-2017  BD [188234]     Added method setParentFocusByChild
 * 25-Oct-2016  BD [180045]     Update the method showModalDialogWithRef to accept the 
 *                        windows options as parameter.     
 * 04-Aug-2016  CD [143055]     Logic fixed on iframeTitleFallBack.
 * 25-Jan-2016  AZ [CR00475431] Check-box does not receive focus, if it is the 
 *                              first item on a page.
 * 05-Nov-2015  AB [CR00472177] Toggle buttons are not visible in Windows
 *                              high-contrast mode.
 * 07-Sep-2015  AZ [CR00466289] clickButton() function could not locate submit
 *                              buttons.
 * 27-Aug-2015  AB [CR00465809] Dynamic browser tab titles.
 * 20-Aug-2015  AZ [CR00465719] Added an index to submit button ID for unique IDs
 * 08-Apr-2015  SK [CR00460103] Added internal modal dialog showing function with
 *                              dialog reference.
 * 27-Feb-2015  AB [CR00458854] TEC-17714 Added code related to opening an Ok/Cancel
 *                              modal dialog with a browser-specific message when
 *                              attempting to download a file
 * 24-Feb-2015  AZ [CR00458302] Refactor isShiftTab() and focusHelpIconOnTab()
 *                              into curam/ModalDialog
 * 17-Feb-2015  AZ [CR00458206] Update the doSetFocus() function to cater for
 *                              scenario where there is a from with no editable
 *                              fields. (Port CR00450079 from TI_60)
 * 10-Feb-2015  AZ [CR00457893] Avoid opening a modal dialog when tab has
 *                              not yet loaded (Port CR00455284 from TI_60)
 * 30-Sep-2014  MV [CR00436657] Fixed informational display problem after
 *              closing modal.
 * 22-Sep-2014  SK [CR00445339] Added inspection layer integration.
 * 12-Sep-2014  MV [CR00444603] Replace dojox/storage with HTML5 local storage.
 * 06-Jun-2014  AS [CR00428142] TEC-17091. Skiplink should become visible when focused
 * 03-Nov-2014  JY [CR00448474] Make the context panel print configurable.
 * 06-Oct-2014  MV [CR00446285] Use cache when reloading page.
 * 06-Jun-2014  AS [CR00428142] TEC-17091. Skiplink should become visible when focused
 * 03-Jun-2014 BOS [CR00434187] Added the getCookie() function and updated
 *                    replaceSubmitButton() to support timeout warning dialog.
 * 15-Apr-2014  JY [CR00425261] Refactored the print function to allow printing
 *                              the context panel.
 * 20-Feb-2014  AS [CR00414442] Skipped arrow and validation div of filtering
 *                              select in doSetFocus and added a new method to
 *                              focus the help icon on tab navigation after end
 *                              of modal dialog.
 * 28-Sep-2013  BOS [CR00396277] Added tests for undefined selected tab.
 * 07-Jun-2013 NLH  [CR00385557] Added highContrastModeType() funtion.
 * 11-Mar-2013  SB  [CR00372052] Added iframeTitleFallBack() function.
 * 21-Feb-2013  SB  [CR00369658] Updated setupGenericKeyHandler() to
                                        handle year field in Date Selector correctly.
 * 20-Feb-2013  MV  [CR00367727] Prevent opening multiple dialogs at once
 *      by clicking on link in quick succession.
 * 14-Dec-2012  SB  [CR00352283] Added removeRoleRegion() function to remove
 *                                      aria role from multiselect.
 * 14-Dec-2012  JY  [CR00360602] Remove the hardcoded height for the actions
 *                               panel.
 * 09-Oct-2012  BOS [CR00346368] Localized debug messages to console.
 * 08-Nov-2012  SB  [CR00350381] Added focus setting code for error and
 *                  informational messages.
 * 23-Oct-2012  MV  [CR00347543] Refer to top level UIController.
 * 03-Oct-2012  SB  [CR00344085] Updated openGenericErrorModalDialog() function
 *      to include boolean check for error or warning modal.
 * 01-Oct-2012  MV  [CR00345339] Improve a way to determine topmost window.
 * 24-Sep-2012  MV  [CR00345119] Handle mailto: links properly across browsers.
 * 17-Sep-2012  MK  [CR00344397] Updated incorrect calls to method called
 *              hasClass to reference correct method called contains instead.
 * 17-Sep-2012  SB  [CR00341890] Added title attribute for page level action
 *                                  menu of the type submit.
 * 11-Sep-2012  MV  [CR00339639] Use local storage API from topmost window to
 *      avoid loading storage for every page. Move a function to this module.
 * 06-Sep-2012  AF  [CR00330559] Added skip link focus method.
 * 31-Aug-2012  MK  [CR00339638] Reverted connect and disconnect functions back
 *              to previous versions. Added in searchButtonStatus that was
 *              missed in the merge from TI_60.
 * 23-Aug-2012  BOS [CR00338361] Added the openGenericErrorModalDialog fucntion.
 * 23-Jul-2012  MV  [CR00336202] Handle gracefully when no tab is open on dialog
 *              submit. Migrate to take on Dojo 1.7.3
 * 26-Jun-2012  SB  [CR00332545] Added searchButtonStatus() function.
 * 28-May-2012  MV  [CR00326704] Fix click event handling for row action menus.
 * 30-Apr-2012  MK  [CR00319243] Updated condition in isSameUrl function to
 *      check if the base string is the same.
 * 24-Apr-2012  AF  [CR00317721] Updated online help URL in openHelpPage method.
 * 14-Mar-2012  SB  [CR00312247] Added openAbout() function
 * 07-Feb-2012  MV  [CR00301458] Code cleanup - added comments,
 *      removed unused code.
 * 13-Dec-2011  BOS [CR00299497] Updating the getTopmostWindow() function
 *                    to check that the Screen Context is defined.
 * 02-Dec-2011  BOS [CR00298234] Updating the getTopmostWindow() function
 *                 in order to support portlets.
 * 05-Oct-2011  PK  [CR00289859] Dojo 1.6.1 upgrade and IE9+ support.
 * 06-Sep-2011  MV  [CR00286500] Don't fail when Preferences anchor not found.
 * 05-Aug-2011  MV  [CR00283589] Remove incorrect code from connect().
 * 02-Aug-2011  MV  [CR00283023] Some refactoring to allow unit testing.
 * 01-Aug-2011  MV  [CR00283020] Refactored redirectWindow() to allow unit
 *      testing.
 * 29-Jul-2011  MV  [CR00269970] Define curam.util using dojo.mixin to avoid
 *      overwriting other class definitions in the same package. Add support
 *      for new UI refresh handling implementation.
 * 28-Jul-2011  MV  [] Handle the FORCE_REFRESH case fully
 *      in redirectWindow()
 * 05-Jul-2011  KW  [CR00275353] Added setRpu() function and refactored
 *                                openLocaleSelector().
 * 18-Jul-2011  KW  [CR00277581] Connect() now strips '#' from end of event URL
 * 13-Jun-2011  MV  [CR00269902] Avoid 404 error coming from bad RPU.
 * 25-May-2011  MV  [CR00267843] Add function for setting up the Preferences
 *    link.
 * 29-Apr-2011  SC  [CR00264826] Modified page load event to include context.
 * 27-Apr-2011  MV [CR00265188] Added support for opening pages in new tab from
 *    dialog opened in the INLINE_PAGE context.
 * 11-Apr-2011  AF [CR00262956] Modified setupGenericKeyHandler method to
 *                              support device independence.
 * 01-Apr-2011  KW [CR00262936] Altered 'alterScrollableListBottomBorder' to
 *                              run after page has loaded
 * 25-Mar-2011  SK [TEC-6335] Removed jsModals indicator as modals are always
 *     on for v6
 * 25-Feb-2011  MV [CR00254937] Honour the RPU set by LinkTag when redirecting
 *     content panel.
 * 25-Feb-2011  MV [CR00254380] Prepare for fix: Honour the RPU set by LinkTag
 *    when redirecting content panel.
 * 18-Feb-2011  MV [CR00247527] Do not block submit when text field has focus.
 * 16-Feb-2011  MV [CR00252701] Fix the isSameUrl() function to work for action
 *    pages that take no parameters.
 * 07-Feb-2011  SJ [CR00247527]  Fixed dropdown submit issue.
 * 07-Jan-2011  MV  [CR00251284] Add support for button load mask.
 * 31-Jan-2011  MK [CR00250297]  Update the clickButton function to handle both
 *    an object and an id as an argument.
 * 28-Jan-2011  MV [CR00245381]  Remove obsolete help settings for dialogs.
 * 26-Jan-2011  MV  [CR00244801] Added another type of refresh behaviour. Strip
 *    o3rpu from RPU value.
 * 25-Jan-2011  MV  [CR00244623] Refactored the function for firing onsubmit
 *    events to work with the latest tab infrastructure changes.
 * 25-Jan-2011  PK  [CR00244773] Filtered all CDEJ parameters from comparison
 *                    in isSameURL. Previously only __o3rpu was filtered.
 * 21-Jan-2011  DG  [CR00243540] Changed "console.log" to "curam.debug.log".
 * 21-Jan-2011  MV  [CR00243263] Add 1px to page height when in list row.
 *    Implement "force refresh" behaviour.
 * 20-Jan-2010  AF  [CR00243728] Added page toolbar button mouse effects.
 * 20-Jan-2010  MK  [CR00243648] Update getPageHeight function to take into
 *                    account the wizard progress bar.
 * 18-Jan-2010  AF  [CR00243204] Modified replaceSubmitButton function for the
 *                               agenda player.
 * 17-Jan-2010 MV [CR00242255] Remove the use of dijit.focus()
 * 16-Jan-2010  PK  [CR00242698] Changed file down load method for list row
 *                    menus so errors will be correctly reported.
 * 14-Jan-2011  MK  [CR00240138] Updated showModalDialog function to remove
 *                    contexts that were not needed in a modal.
 * 06-Jan-2011 KW [CR00240549] Added function to prevent overlapping of Action
 *                             set buttons of modals
 * 04-Jan-2011 MV [CR00240081] Fixes to the getPageHeight() function.
 * 15-Dec-2010 KW [CR00238785] Stopped the refresh event when submit button
 *                             clicked
 * 10-Dec-2010  AF  [CR00233054] Added button mouse event functions that will
 *                               add specific CSS class names when modal and
 *                               cluster buttons are clicked or rolled over.
 * 07-Dec-2010  MV  [CR00233442] Adjustments to the getPageHeight() function
 *    to bring the expandable list detail row more in line with
 *    the specification.
 * 08-Dec-2010  SJ  [CR00229344] Added the print functionality.
 * 03-Dec-2010  MV  [CR00232963] Optimize swapState() function.
 * 30-Nov-2010  MV  [CR00232623] Remove extra height when in-page navigation
 *    is present.
 * 24-Nov-2010  PK  [TEC-XXXX] Added NESTED_UIM context.
 * 23-Nov-2010  MV  [CR00232063] Remove page loading mask.
 * 18-Nov-2010 MV [CR00231387] Connect to DOM events with a function that will
 *    automatically disconnect on page unload.
 * 18-Nov-2010  SJ [CR00228391]Fixed the issue with OPEN_NEW attribute on
 *                   List Row Actions Menu links.
 * 01-Nov-2010  SD  [CR00225331] An extra parameter has been added to both
 *                     openModalDialog and showModalDialog functions for
 *                     UIMDialog API.
 * 27-Oct-2010  SK   [CR00224193] Changed the redirection of the window so that
       the absence of the content panel not caused failure.
 * 14-Oct-2010  MV [CR00223441] Move functions to different namespaces.
 *    Add getSuffixFromClass function.
 * 29-Sep-2010  MV  [CR00221605] Enable submitting by pressing Enter. Check
 *    for existence of dijit before accesing it.
 * 18-Sep-2010  PK  [CR00204622] Ensure when a page loads for the first time
 *                    in an expandable list, only the "expandedList.toggle"
 *                    event is processed.
 * 17-Sep-2010  MV  [CR00220607] Set page focus only when the whole page
 *                    is loaded. Use dijit.focus() instead of plain element
 *                    focus().
 * 14-Sep-2010  MV  [CR00220152] Add the getLastPathSegmentWithQueryString
 *                    function and also use it where appropriate in this file.
 * 10-Sep-2010  MV  [CR00219824] The focus setting function now indicates
 *                    the result via its return value.
 * 14-Sep-2010  PK  [CR00219843] Fixed expandable list sizing.
 * 08-Sep-2010  MV  [CR00219540] Add support for loading pages in the same
 *                    dialog from the list actions menu.
 * 27-Aug-2010  MV  [CR00217499] Added the makeQueryString() function. Replaced
 *                  the use of escape() with the correct function.
 * 05-Jul-2010 BD [CR00204119]  Introduced use of UIMController in place of
 *                              iframe for expandable lists to cater for
 *                              In Page Navigation tabs.
 * 28-Jul-2010  PK  [CR00211736] Updated due to re-factoring of
 *                    tab-app-controller.js.
 * 27-Jul-2010  MK  [CR00211743] Optional display the help icon on a modal
 *                    dialog.
 * 22-Jul-2010  MV  [CR00211225] Fix page height calculation for inline row
 *                    pages.
 * 22-Jul-2010  JY  [CR00210937] add 10px spacing at the top of the actions
 *                    panel.
 * 20-Jul-2010  MV  [CR00211031] doSetFocus(): handle pages with no HTML form.
 * 15-Jul-2010  MV  [CR00210541] Moved focus handling for modals to
 *                    ModalDialog.js
 * 12-Jul-2010  MV  [CR00210064] Added swapState() function.
 * 10-Jul-2010  OK  [CR00209714] Added setupRemovePageMask function.
 * 06-Jul-2010  MV  [CR00180694] Added toCommaSeparatedList() function.
 * 05-Jul-2010  SOS [CR00209386] Added try/catch to getPageHeight() for when
 *                    it's called in a hidden iframe.
 * 02-Jul-2010  PK  [CR00203531] Extra null check added to listRowFrameLoaded
 *                    method.
 * 26-Jun-2010  MV  [CR00204069] Added getPageHeight() function. Used Curam
 *                    debug logger throughout the file.
 * 18-Jun-2010  MV  [CR00203864] Remove the code for automatically resizing the
 *                    details panel.
 * 17-Jun-2010  MV  [CR00202490] Create the iframe for expandable lists on
 *                    demand only.
 * 15-Jun-2010  FG  [CR00202535] Added in some further functions required by the
 *                    application search functionality.
 * 01-Jun-2010  FG  [CR00200968] Added in some functions required by the
 *                    application search functionality.
 * 11-May-2010  SJ  [CR00198617] Implemented caching on expandable list row
 *                    level actions. As a fix CACHE_BUSTER,
 *                    CACHE_BUSTER_PARAM_NAME parameters are added and the same
 *                    are appended to the iframe source.
 * 11-May-2010  MV  [CR00196066] Added stripeTable() function.
 * 22-Apr-2010  AF  [CR00194043] Added 3 pixels to autoSizeDetailsPane's frame
 *                    height which removes an unwanted vertical scroll bar from
 *                    appearing on the details panel.
 * 23-Apr-2010  MV  [CR00194352] Avoid adding extra ampersand in
 *                    addUrlParameter() if there are no more parameters to add.
 * 07-Apr-2010  BD  [CR00191597] Renamed resizeDetailsPanel() function to
 *                    autoSizeDetailsPanel() to better reflect its function.
 *                    Refactored to handle new html structure.
 * 18-Mar-2010  PK  [CR00191211] Added toggleListDetailsRow.
 * 24-Feb-2010  MV  [CR00189738] Re-enable focus on the first editable field in
 *                    modals.
 * 23-Feb-2010  AF  [CR00189289] Added iframe title as a parameter in the iframe
 *                    upload publish event.
 * 16-Feb-2010  BD  [CR00183006] Add try/catch block to addContentWidthListener
 *                    function. Swallows an exception that does not effect the
 *                    application.
 * 08-Jan-2010  MV  [CR00182272] Added a localizable error message for the
 *                    language selector.
 * 11-Dec-2009  MV  [CR00173949] Remove the SrPopUp target from the modal
 *                    handler form.
 * 25-Nov-2009  MV  [CR00175955] Set focus to the first control when the
 *                    curam.modalDisplayed event happens.
 * 24-Nov-2009  MV  [CR00175837] Add new fireTabOpenedEvent function and a
 *                    missing require for curam.tab.
 * 20-Nov-2009  MV  [CR00175615] Fix the firePageSubmittedEvent function.
 * 20-Nov-2009  MV  [CR00175581] Fix the curam.tab.redirectContentPanel call.
 */

define(["dojo/dom",
        "dijit/registry",
        "dojo/dom-construct",
        "dojo/ready",
        "dojo/_base/window",
        "dojo/dom-style",
        "dojo/_base/array",
        "dojo/dom-class",
        "dojo/topic",
        "dojo/_base/event",
        "dojo/query",
        "dojo/Deferred",
        "dojo/has",
        "dojo/_base/unload",
        "dojo/dom-geometry",
        "dojo/_base/json",
        "dojo/dom-attr",
        "dojo/_base/lang",
        "dojo/on",
        "dijit/_BidiSupport",
        "curam/define",
        /* "dojox/storage", */
        "curam/debug",
        "curam/util/RuntimeContext",
        "curam/util/Constants",
        "dojo/_base/sniff",
        "cm/_base/_dom",
        "curam/util/ResourceBundle",
        "dojo/NodeList-traverse"

        ], function(dom, registry, domConstruct, ready, windowBase, style,
            array, domClass, topic, dojoEvent, query, Deferred, has, unload,
            geom, json, attr, lang, on, bidi, define, debug, runtimeCtx,
            Constants, sniff, _dom, resBundle) {

/**
 * @name      curam.util
 * @namespace Functions for generic utiltities across CDEJ.
 */
curam.define.singleton("curam.util",
/**
 * @lends curam.util.prototype
 */
{
  PREVENT_CACHE_FLAG: "o3pc",
  INFORMATIONAL_MSGS_STORAGE_ID: "__informationals__",
  ERROR_MESSAGES_CONTAINER: "error-messages-container",
  ERROR_MESSAGES_LIST: "error-messages",
  CACHE_BUSTER: 0,
  CACHE_BUSTER_PARAM_NAME: "o3nocache",
  PAGE_ID_PREFIX: "Curam_",
  msgLocaleSelectorActionPage: "$not-locaized$ Usage of the Language Selector is not permitted from an editable page that has previously been submitted.",
  GENERIC_ERROR_MODAL_MAP: {},
  wrappersMap : [],
  lastOpenedTabButton: false,
  tabButtonClicked: false,
  secureURLsExemptParamName: "suep",
  secureURLsExemptParamsPrefix: "spm",
  secureURLsHashTokenParam : "suhtp",
  
  /**
   * This function is used to record that the user is navigating 
   * through tabs so as to avoid the focus to move to the first editable field.
   * The tabButtonClicked stores the tabButton that will be used inside the
   * doSetFocus function to keep the focus on the tabButton.
   */
  setTabButtonClicked: function(tabButton){
          curam.util.getTopmostWindow().curam.util.tabButtonClicked = tabButton;
  },
  
  
  /**
   * Gets the tabButtonClicked and sets the variable to the default value.
   * 
   */
  getTabButtonClicked: function(){
          var tabButton = curam.util.getTopmostWindow().curam.util.tabButtonClicked;
          curam.util.getTopmostWindow().curam.util.tabButtonClicked = false;
          return tabButton;
  },

  /**
   * The lastOpenedTabButton stores the tabButton that will be used inside the
   * doSetFocus function to keep the focus on the tabButton if there are no editable 
   * fields.
   */
  setLastOpenedTabButton: function(tabButton){
    curam.util.getTopmostWindow().curam.util.lastOpenedTabButton = tabButton;
  },

  /**
   * Gets the lastOpenedTabButton and resets the variable to the default value.
   * 
   */
  getLastOpenedTabButton: function(){
        var tabButton = curam.util.getTopmostWindow().curam.util.lastOpenedTabButton;
        curam.util.getTopmostWindow().curam.util.lastOpenedTabButton = false;
        return tabButton;
  },
  
  insertCssText: function(cssString, styleNodeId) {
    var id = styleNodeId ? styleNodeId : "_runtime_stylesheet_";
      var styleSheetNode = dom.byId(id);
    var rmNode;

    if(styleSheetNode) {
      if(styleSheetNode.styleSheet) {
        cssString = styleSheetNode.styleSheet.cssText + cssString;
        rmNode = styleSheetNode;
        rmNode.setAttribute("id", "_nodeToRm");
      } else {
        styleSheetNode.appendChild(document.createTextNode(cssString));
        return;
      }
    }

    var pa = document.getElementsByTagName('head')[0];
      styleSheetNode = domConstruct.create("style", {
      type: "text/css",
      id: id
    });

    if(styleSheetNode.styleSheet) {
      styleSheetNode.styleSheet.cssText = cssString;
    }
    else{
      styleSheetNode.appendChild(document.createTextNode(cssString));
    }
    pa.appendChild(styleSheetNode);
    if(rmNode) {
      rmNode.parentNode.removeChild(rmNode);
    }
  },

  fireRefreshTreeEvent: function() {
      if (dojo.global.parent && dojo.global.parent.amIFrame) {
        var wpl = dojo.global.parent.loader;
    }
    if(wpl && wpl.dojo) {
      wpl.dojo.publish("refreshTree");
    }
  },

  /**
   * Invoked when a form is submitted on a page in any context.
   *
   * This event tracks submitting of pages anywhere in the application
   * to enable proper UI refresh handling.
   *
   * @param {String} context Specifies the context in which the submit
   *     happenned. The expected values are [main-content|dialog].
   */
  firePageSubmittedEvent: function(context) {
    require(["curam/tab"], function() {
      /*
       * This function is executed at onsubmit event and the call to
       * curam.tab.getContainerTab() below was failing in this scenario.
       * Using curam.tab.getSelectedTab() instead works fine.
       * Note that before refactoring to remove the use of getSelectedTab()
       * further changes will have to be made to make it work.
       */
      var sourceTab = curam.tab.getSelectedTab();
      if (sourceTab) {
        var tabWidgetId = curam.tab.getTabWidgetId(sourceTab);

        var topWin = curam.util.getTopmostWindow();
        var ctx = (context == "dialog")
            ? curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_DIALOG
            : curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN;
        topWin.curam.util.Refresh.getController(tabWidgetId).pageSubmitted(
                dojo.global.jsPageID, ctx);
        topWin.dojo.publish("/curam/main-content/page/submitted",
                [dojo.global.jsPageID, tabWidgetId]);

      } else {
        curam.debug.log("/curam/main-content/page/submitted: " // don't localize
            + debug.getProperty("curam.util.no.open")); // FIXME: localize
      }
    });
  },

  fireTabOpenedEvent: function(tabWidgetId) {
    // Publish the tab opened event
    curam.util.getTopmostWindow().dojo.publish("curam.tabOpened",
          [dojo.global.jsPageID, tabWidgetId]);
  },

  /**
   * Setup the submit event publisher for the main content panel.
   */
  setupSubmitEventPublisher: function() {
      ready(function() {
        var form = dom.byId('mainForm');
      if (form) {
        curam.util.connect(form, 'onsubmit', function() {
          
          // Display Progress spinner
            curam.util.getTopmostWindow().dojo.publish('/curam/progress/display',
          		  [curam.util.PAGE_ID_PREFIX + dojo.global.jsPageID]);
          curam.util.firePageSubmittedEvent("main-content");
        });
      }
    });
  },

  getScrollbar: function(){
    //  summary
    //  returns the width of a scrollbar.

    //  set up the test nodes.
      var scroll = domConstruct.create("div", {}, windowBase.body());

      style.set(scroll, {
      width: "100px",
      height: "100px",
      overflow: "scroll",
      position: "absolute",
      top: "-300px",
      left: "0px"
    });

      var test = domConstruct.create("div", {}, scroll);

      style.set(test, {
      width: "400px",
      height: "400px"
    });

    var width = scroll.offsetWidth - scroll.clientWidth;
      domConstruct.destroy(scroll);

    //  we return an object because we may add additional info in the future.
    return { width: width };  //  object
  },

  // Returns true if the page is in a modal dialog, false otherwise.
  isModalWindow: function() {
    // the window.curamModal flag is set in curam.dialog.initModal()
      return (dojo.global.curamModal === undefined) ? false : true;
  },

  // Returns true if the page is in a modal dialog and running or existing 
  // an IEG script, otherwise returns false.
  isExitingIEGScriptInModalWindow: function(exitingIEGScript) {
    // the exitingIEGScript variable is set in ieg-modal.initDialog()
      return (curam.util.getTopmostWindow().exitingIEGScript === undefined) ? false : true;
  },
  
  // Set a global variable to true for a modal dialog and running or existing 
  // an IEG script.
  setExitingIEGScriptInModalWindowVariable: function() {
	  curam.util.getTopmostWindow().exitingIEGScript = true;
  },

  /**
   * Gets the top most window. The topmost window window is returned 
   * starting with the current window object.
   */
  getTopmostWindow: function() {

    // check topmost window cache and cache it if it is not yet cached
    if (typeof (dojo.global._curamTopmostWindow) == "undefined") {
      var parentWin = dojo.global;
      if (parentWin.__extAppTopWin) {
        dojo.global._curamTopmostWindow = parentWin;
      } else {
        while (parentWin.parent != parentWin) {
          parentWin = parentWin.parent;
          if (parentWin.__extAppTopWin) {
            // found the top window of a public facing app
            break;
          }
        }
        dojo.global._curamTopmostWindow = parentWin;
      }
    }

    // report cases of incorrect topmost window
    if (dojo.global._curamTopmostWindow.location.href.indexOf(
        "AppController.do") < 0
        && typeof(dojo.global._curamTopmostWindow.__extAppTopWin)
           == "undefined") {
      curam.debug.log(debug.getProperty("curam.util.wrong.window")
         + dojo.global._curamTopmostWindow.location.href);
    }

    return dojo.global._curamTopmostWindow;
  },

  getUrlParamValue: function(url, paramName) {
    var qPos = url.indexOf("?");
    if(qPos < 0) {return null;}
    var paramStr = url.substring(qPos + 1, url.length);

    function getVal(delim) {
      var params = paramStr.split(delim);

      paramName += "=";
      for(var i = 0; i < params.length; i++) {
        if(params[i].indexOf(paramName) == 0){
          return params[i].split("=")[1];
        }
      }
    }

    return getVal("&") || getVal("");
  },

  addUrlParam: function(href, paramName, paramValue, prepend) {
    var hasQ = href.indexOf("?") > -1;
    var doPrepend = prepend ? prepend : 'undefined';

    if (!hasQ || (doPrepend == false)) {
      return href + (hasQ ? "&" : "?") + paramName + "=" + paramValue;

    } else {
      var parts = href.split("?");
      href = parts[0] + "?" + paramName + "=" + paramValue + (parts[1] != "" ? ("&" + parts[1]) : "");
      return href;
    }
  },

  replaceUrlParam: function(href, paramName, newValue) {
    href = curam.util.removeUrlParam(href, paramName);
    return curam.util.addUrlParam(href, paramName, newValue);
  },

  removeUrlParam: function(url, paramName, /*optional*/paramValue) {
    var qPos = url.indexOf("?");
    if(qPos < 0) {return url;}
    if(url.indexOf(paramName + "=") < 0){return url;}//shortcut

    var paramStr = url.substring(qPos + 1, url.length);
    var params = paramStr.split("&");
    var value;
    var paramParts, doRemove;

    for(var i = 0; i < params.length; i++) {
      if (params[i].indexOf(paramName+"=") == 0) {
        doRemove = false;
        if(paramValue) {
          paramParts = params[i].split("=");
          if (paramParts.length > 1) {
            if (paramParts[1] == paramValue){
              doRemove = true;
            }

          } else if ( paramValue == "") {
            doRemove = true;
          }

        } else {
          doRemove = true;
        }

        if(doRemove) {
          //remove the parameter from the array
          params.splice(i, 1);
          //in case the param is in the url more than once, keep checking
          i--;
        }
      }
    }
    return url.substring(0, qPos + 1) + params.join("&");
  },

  //Remove the hash symbol, and everything that follows it, from a url.
  stripHash: function(url) {
    var idx = url.indexOf("#");
    if(idx < 0){return url;}
    return url.substring(0, url);
  },

  /**
   * Compares the specified URLs.
   *
   * This ignores the order of parameters - identical parameters in different
   * orders will still return true.
   *
   * If the second href is omitted, it defaults to the location
   * of the specified runtime context.
   *
   * @param href1 First HREF.
   * @param href2 Second HREF.
   * @param rtc current runtime context.
   *
   * @return True if the two urls are the same, false otherwise.
   */
  isSameUrl: function(href1, href2, rtc) {
    if (!href2) {
      href2 = rtc.getHref();
    }
    if (href1.indexOf("#") == 0) {
      return true;
    }

    // Remove the # symbols from the comparison.
    var hashIdx = href1.indexOf('#');
    if (hashIdx > -1) {
      //If the first URL starts with a #, then it is automatically equal to the
      //second URL
      if (hashIdx == 0) {
        return true;
      }

      var urlParts1 = href1.split("#");
      var hashIdx2 = href2.indexOf("#");

      //If the second URL has a hash symbol, remove it and everything after it,
      //then do the comparison
      if (hashIdx2 > -1) {
        if (hashIdx2 == 0) {
          return true;
        }
        href2 = href2.split("#")[0];
      }
      return urlParts1[0] == href2;
    }

    var stripPageOrActionFromUrl = function(url) {
      var idx = url.lastIndexOf("Page.do");
      var len = 7;
      if (idx < 0) {
        idx = url.lastIndexOf("Action.do");
        len = 9;
      }
      if (idx < 0) {
        idx = url.lastIndexOf("Frame.do");
        len = 8;
      }
      if (idx > -1 && idx == url.length - len) {
        return url.substring(0, idx);
      }
      return url;
    };

    var rp = curam.util.removeUrlParam;

    var here = curam.util.stripHash(rp(href2,
                    curam.util.Constants.RETURN_PAGE_PARAM));
    var there = curam.util.stripHash(rp(href1,
                    curam.util.Constants.RETURN_PAGE_PARAM));
    var partsThere = there.split("?");
    var partsHere = here.split("?");

    //Remove the Action.do or Page.do from the url
    partsHere[0] = stripPageOrActionFromUrl(partsHere[0]);
    partsThere[0] = stripPageOrActionFromUrl(partsThere[0]);

      // This check to see if the page names are the same assumes that the
      // functions above to strip parameters and page or actions from the url
      // have been run first. So the end of each string is only the name of
      // the page.
    var baseEqual = (partsHere[0] == partsThere[0]
          || partsHere[0].match(partsThere[0]+"$")==partsThere[0]);

    if (!baseEqual) {
      return false;
    }

    if (partsHere.length == 1 && partsThere.length == 1 && baseEqual) {
      //If the base URL is equal, and the parameter string is exactly equal,
      //then don't bother checking the unordered parameters. Just return true,
      //because they're equal
      return true;

    } else {
      //Check the values of all of the parameters, ignoring order of url
      //parameters
      var paramsHere;
      var paramsThere;
      if (typeof partsHere[1] != "undefined" && partsHere[1] != "") {
        paramsHere = partsHere[1].split("&");

      } else {
        // if there aren't any parameters create an empty array
        paramsHere = new Array();
      }

      if (typeof partsThere[1] != "undefined" && partsThere[1] != "") {
        paramsThere = partsThere[1].split("&");

      } else {
        // if there aren't any parameters create an empty array
        paramsThere = new Array();
      }

      // don't include CDEJ parameters in the comparison
      curam.debug.log(
          "curam.util.isSameUrl: paramsHere "
          + debug.getProperty("curam.util.before")
          + paramsHere.length);
        paramsHere = array.filter(paramsHere, curam.util.isNotCDEJParam);
      curam.debug.log(
          "curam.util.isSameUrl: paramsHere "
          + debug.getProperty("curam.util.after")
          + paramsHere.length);

      curam.debug.log(
          "curam.util.isSameUrl: paramsHere "
          + debug.getProperty("curam.util.before")
          + paramsThere.length);
        paramsThere = array.filter(paramsThere, curam.util.isNotCDEJParam);
        curam.debug.log(
            "curam.util.isSameUrl: paramsHere "
            + debug.getProperty("curam.util.after")
          + paramsThere.length);

      if (paramsHere.length != paramsThere.length) {
        return false;
      }

      var paramMap = {};
      var param;
      for (var i = 0; i < paramsHere.length; i++) {
        param = paramsHere[i].split("=");
        // Decode the key and value before adding to the map.
        // This ensures a correct comparison in the following loop.
        param[0] = decodeURIComponent(param[0]);
        param[1] = decodeURIComponent(param[1]);
        paramMap[param[0]] = param[1];
      }
      for (var i = 0; i < paramsThere.length; i++) {
        param = paramsThere[i].split("=");
        // Decode the key and value before adding to the map.
        // This ensures a correct comparison in the next step.
        param[0] = decodeURIComponent(param[0]);
        param[1] = decodeURIComponent(param[1]);
        if (paramMap[param[0]] != param[1]) {
          curam.debug.log(debug.getProperty("curam.util.no.match",
              [param[0], param[1], paramMap[param[0]]]));
          return false;
        }
      }
    }

    //If the base url is the same, and all the parameters match, then
    //the urls are equal
    return true;
  },

  /**
   * Tests if the specified parameter name isn't a CDEJ parameter. It is used by
     * the array.filter used in the isSameUrl method.
   *
   * TODO: There is a similar method in PageRequest.js. Attempted to re-factor
   * but led to JavaScript errors in PageRequest.js when it was executed. Need
   * more time to work out dependency problem, for now duplicating the method.
   *
   * @return true if the specified parameter name is a CDEJ parameter, false
   *         otherwise.
   */
  isNotCDEJParam: function(paramName) {
    return !((paramName.charAt(0) == 'o' && paramName.charAt(1) == '3')
           || (paramName.charAt(0) == '_' && paramName.charAt(1) == '_'
               && paramName.charAt(2) == 'o' && paramName.charAt(3) == '3'));
  },

  //Sets one or more attributes on a DOM node. The map looks like:
  //{ type:'text', value:'This is text', style:'width:100px'}
  setAttributes: function(node, map) {
    for(var x in map) {
      node.setAttribute(x, map[x]);
    }
  },

  //This should be called if a pop up page has submitted a form, stating that
  //if this page is redirected to itself, rather than ignoring it, it should
  //refresh the browser.
  invalidatePage: function() {
    curam.PAGE_INVALIDATED = true;

      var parentWin = dojo.global.dialogArguments
          ? dojo.global.dialogArguments[0]:opener;

    if(parentWin && parentWin != dojo.global) {
      try {
        parentWin.curam.util.invalidatePage();

      } catch(e) {
        curam.debug.log(debug.getProperty("curam.util.error"), e);
      }
    }
  },

  /**
   * Sends the window to a new URL. This needs to be done differently depending
   * on whether or not the window is modal (IE only).
   * @param force
   *    If set to true, it does not matter if the href is the same as
   *    the current href or not, it will be refreshed.
   * @param ignoreFrame
   *    If true, then any other frames on the page are not refreshed.
   */
  redirectWindow: function(href, force, ignoreFrames) {
    var rtc = new curam.util.RuntimeContext(dojo.global);
    var redirectContentPanelInDifferentFrameRootContext =
      function(context, rootObject, href, forceLoad, justRefresh) {
        curam.util.getFrameRoot(context, rootObject)
            .curam.util.redirectContentPanel(href, forceLoad, justRefresh);
      };
      curam.util._doRedirectWindow(href, force, ignoreFrames,
          dojo.global.jsScreenContext, rtc, curam.util.publishRefreshEvent,
        redirectContentPanelInDifferentFrameRootContext);
  },

  _doRedirectWindow: function(href, force, ignoreFrames, screenContext, rtc,
      publishRefreshEvent, redirectContentPanelInDifferentFrameRootContext) {
    if (href && curam.util.isActionPage(href) && !curam.util.LOCALE_REFRESH) {
      // Avoid 404 error coming from bad RPU. This is temporary,
      // will be properly fixed by TEC-7123.
      curam.debug.log(debug.getProperty("curam.util.stopping"), href);
      return;
    }

    var rpl = curam.util.replaceUrlParam;
    //check if we are in the frameset context
    var inFrame = screenContext.hasContextBits('TREE')
                 || screenContext.hasContextBits('AGENDA')
                   || screenContext.hasContextBits('ORG_TREE');

    if (curam.util.LOCALE_REFRESH) {
     
      curam.util.publishRefreshEvent();
      // reload the entire application  
	  curam.util.getTopmostWindow().location.reload(); 
    
      return;
      
    } else if(curam.util.FORCE_REFRESH) {
      //If the FORCE_REFRESH parameter is set, in dialog.js, then ignore the
      //href parameter and just reload the page. This is done for the user
      //preferences dialog, so that it doesn't lose the __o3rpu parameter,
      //but can be used in other places too.
      href = rpl(rtc.getHref(), curam.util.PREVENT_CACHE_FLAG,
                 (new Date()).getTime());
      if(curam.util.isModalWindow() || inFrame) {
        publishRefreshEvent();
          dojo.global.location.href = href;

      } else {
        if (screenContext.hasContextBits('LIST_ROW_INLINE_PAGE')
            || screenContext.hasContextBits('NESTED_UIM')) {

          curam.util._handleInlinePageRefresh(href);

        } else {
          publishRefreshEvent();
          if (dojo.global.location !== curam.util.getTopmostWindow().location) {
            require(["curam/tab"], function() {
              redirectContentPanelInDifferentFrameRootContext(dojo.global,
                  curam.tab.getTabController().ROOT_OBJ, href, true, true);
            });
          }
        }
      }
      return;
    }

    var u = curam.util;
    //if the URL is identical, it's not a real redirect, so do nothing.
    //This solves the case of a Cancel button being clicked in a modal window.
    var rtc = new curam.util.RuntimeContext(dojo.global);
    if(!inFrame && !force && !curam.PAGE_INVALIDATED
        && u.isSameUrl(href, null, rtc)) {
      return;
    }

    //If in a modal dialog, then submit a form via a 'POST', as doing a normal
    //redirect
    if(curam.util.isModalWindow() || inFrame) {

      //make sure that the modal parameter is set, and that a timestamp is added
      //to prevent the resulting page from being cached.
      href = rpl(rpl(href, "o3frame", "modal"),
        curam.util.PREVENT_CACHE_FLAG, (new Date()).getTime());
        var form = domConstruct.create("form", {
        action:href,
        method:"POST"
      });

      //modals launched from Agenda Player in modal do not need artificial post
      if (!inFrame) {

          if(!dom.byId("o3ctx")) {
          // The o3ctx may exist on the url passed into this method already.
          // So, remove it and reset the form action.
          // This is a last-minute fix for an issue found during JDE 009
          // testing.
          form.action =
            curam.util.removeUrlParam(form.action, "o3ctx");
            var input1 = domConstruct.create("input", {
            type: "hidden", id: "o3ctx", name:"o3ctx",
            value: screenContext.getValue()
          }, form);
        }
          windowBase.body().appendChild(form);
        publishRefreshEvent();
        form.submit();
      }
      if(!ignoreFrames) {
        if (inFrame) {
          curam.util.redirectFrame(href);
        }
      }

    } else {
      //The base context case; no frameset, these are not supported in the tab
      //content panel. Just change the href
      //BEGIN,RTC 194820,267326 COF
   	  var launchWordEditOp=sessionStorage.getItem("launchWordEdit");
      //END,RTC 194820,267326 COF
      if (!launchWordEditOp && (screenContext.hasContextBits("LIST_ROW_INLINE_PAGE")
          || screenContext.hasContextBits("NESTED_UIM"))) {

        curam.util._handleInlinePageRefresh(href);

      } else {
    	//BEGIN,RTC 194820,267326 COF
	    if(launchWordEditOp){
           sessionStorage.removeItem("launchWordEdit");
   		}
         
      //END, RTC 194820,267326 COF
      publishRefreshEvent();
      if (dojo.global.location !== curam.util.getTopmostWindow().location) {
        if (screenContext.hasContextBits("EXTAPP")) {
          var topWindow = window.top;
          topWindow.dijit.byId("curam-app").updateMainContentIframe(href);
        } else {
          require(["curam/tab"], function() {
            curam.util.getFrameRoot(dojo.global,
              curam.tab.getTabController().ROOT_OBJ)
                .curam.util.redirectContentPanel(href, force);
          });
        }
      }
    }
  }
},

  /**
   * Closing modal dialog opened from expanded list row or nested UIM.
   * Either Redirect expanded row iframe or open the url in a new tab,
   * depending on whether the target page is mapped to some tab or not.
   */
  _handleInlinePageRefresh: function(href) {
    curam.debug.log(debug.getProperty("curam.util.closing.modal"), href);

    /*
     * The following code is based on assumption that inline pages are not
     * mapped to any tabs. If this is the case then the inline frame will
     * be refreshed. If the page is mapped to a tab then it will open in that
     * tab rather than in the inline frame.
     */
    var pageRequest = new curam.ui.PageRequest(href);
    require(["curam/tab"], function() {
      curam.tab.getTabController().checkPage(pageRequest, function(request) {
        // refresh the inline frame
        curam.util.publishRefreshEvent();
        // specifically pass false so that browser uses cached resources
        // where possible
        window.location.reload(false);
      });
    });
  },

  /**
   * @param url
   *    The URL to redirect to.
   * @param forceLoad
   *    Load the page even if the existing URL is the same.
   * @param justRefresh
   *    Do not change to a different URL, only refresh the existing page.
   */
  redirectContentPanel: function(url, forceLoad, justRefresh) {
    require(["curam/tab"], function() {
      // add the return page parameter
      var iframe = curam.tab.getContentPanelIframe();
      var newUrl = url;
      if (iframe != null) {
        var rpu = curam.util.Constants.RETURN_PAGE_PARAM;
        var o3rpuValue = null;
        if (url.indexOf(rpu + "=") >= 0) { // if the url has RPU param
          curam.debug.log("curam.util.redirectContentPanel: "
            + debug.getProperty("curam.util.rpu"));
          o3rpuValue = decodeURIComponent(curam.util.getUrlParamValue(url, rpu));
        }
        // or the specified URL has no __o3rpu parameter - just pass through

        if (o3rpuValue) {
          // strip the __o3rpu parameter form the RPU value
          o3rpuValue = curam.util.removeUrlParam(o3rpuValue, rpu);

          newUrl = curam.util.replaceUrlParam(url, rpu,
              encodeURIComponent(o3rpuValue));
        }
      }
      var uimPageRequest = new curam.ui.PageRequest(newUrl);
      if (forceLoad) {
        uimPageRequest.forceLoad = true;
      }
      if (justRefresh) {
        uimPageRequest.justRefresh = true;
      }
      curam.tab.getTabController().handlePageRequest(uimPageRequest);
    });
  },

  //Redirects a page in a frame, and refreshes all other frames.
  //If 'href' is not set, then the current window is not refreshed,
  //just the other frames.
  redirectFrame: function(href) {
      if (dojo.global.jsScreenContext.hasContextBits('AGENDA')) {
      var target = curam.util.getFrameRoot(dojo.global, "wizard").targetframe;
      target.curam.util.publishRefreshEvent();
      target.location.href = href;

      } else if (dojo.global.jsScreenContext.hasContextBits('ORG_TREE')) {//lazy tree
      var target = curam.util.getFrameRoot(dojo.global, "orgTreeRoot");
        /* FIXME: this code expects that curam.util and dojo are loaded
         * and avaialble in the target context. Instead it should call require()
         * to load the required module.
         */
      target.curam.util.publishRefreshEvent();
      target.dojo.publish("orgTree.refreshContent", [ href ]);

    } else { //tree frameset
      var treeRef = curam.util.getFrameRoot(dojo.global, "iegtree");
      var navigator = treeRef.navframe || treeRef.frames[0];
      var contents = treeRef.contentframe || treeRef.frames['contentframe'];
      contents.curam.util.publishRefreshEvent();
      if (curam.PAGE_INVALIDATED || navigator.curam.PAGE_INVALIDATED) {
        var newHref = curam.util.modifyUrlContext(href, 'ACTION');
        contents.location.href = newHref;

      } else {
        contents.location.href = href;
      }
    }

    //Return true, indicating that a redirect did take place.
    return true;
  },

  publishRefreshEvent: function() {
      topic.publish("/curam/page/refresh");
  },

  /**
   * Opens a basic error modal dialog using the href
   * <code>generic-modal-error.jspx</code>. The parameters are passed to a
   * Javascript object which can be retrieved in the JSP.
   *
   * @param windowOptions        The windows options to specifiy the width and
   *                             height of the dialog.
   * @param titleProp            The property key to be used when localizing
   *                             the text of the title on the dialog.
   * @param messageProp          The property key to be used when localizing
   *                             the message on the dialog.
   * @param messagePlaceholder1  The first placeholder to be within the message
   *                             on the dialog. THis will not be set if it is
   *                             undefined.
   * @param isErrorModal         The boolean value to indicate whether it is an
   *                             error modal or a warning modal.
   */
  openGenericErrorModalDialog: function(windowOptions, titleProp,
	      messageProp, messagePlaceholder1, isErrorModal) {
	  
	var topmostWin = curam.util.getTopmostWindow();
	
	// Add the GENERIC_ERROR_MODAL_MAP to the top most window so it can be
	// retrieved in the JSP.
	topmostWin.curam.util.GENERIC_ERROR_MODAL_MAP = {"windowsOptions": windowOptions,
          "titleInfo" : titleProp,
          "msg" : messageProp,
          "msgPlaceholder" : messagePlaceholder1,
          "errorModal" : isErrorModal,
          "hasCancelButton" : false};
	  
    var url ="generic-modal-error.jspx";
    // TODO: May also want to take into account whether window options are set
    // or not -- BOS
	curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton = true;
    curam.util.openModalDialog({href:encodeURI(url)}, windowOptions);

  },

  /**
   * Opens an Ok/Cancel modal dialog using the href
   * <code>generic-modal-error.jspx</code>. The parameters are passed to a
   * Javascript object which can be retrieved in the JSP. The Ok/Cancel buttons
   * pass 'confirm' or 'cancel' respectively to the "/curam/dialog/close"
   * event.
   *
   * @param windowOptions  The windows options to specifiy the width and
   *                       height of the dialog.
   * @param titleProp      The property key to be used when localizing
   *                       the text of the title on the dialog.
   * @param messageProp    The property key to be used when localizing
   *                       the message on the dialog.
   */
  openGenericErrorModalDialogYesNo: function(windowOptions, titleProp,
      messageProp) {
	
	// Parameters that are added to the URL for the JSP. 
	var sc = dojo.global.jsScreenContext;
    var topmostWin = curam.util.getTopmostWindow();    
    
    sc.addContextBits('MODAL');
        
	// Add the GENERIC_ERROR_MODAL_MAP to the top most window so it can be
	// retrieved in the JSP.
    topmostWin.curam.util.GENERIC_ERROR_MODAL_MAP = {"windowsOptions": windowOptions,
      "titleInfo" : titleProp,
      "msg" : messageProp,
      "msgPlaceholder" : "",
      "errorModal" : false,
      "hasCancelButton" : true};
            
    var url ="generic-modal-error.jspx?" + sc.toRequestString();

    // TODO: May also want to take into account whether window options are set
    // or not -- BOS
	      
	 curam.util.openModalDialog({href:encodeURI(url)}, windowOptions);
	 
  },

  /**
  * Appends placeholder-for-focus class to the element tag
  * and add the window to  to the PLACEHOLDER_WINDOW_LIST.
  * @param eventOrAnchorTag  The element to be added the placeholder-for-focus class.
  * @param window            The property window key to be used when returning the 
  *                          focus when the modal is closed.
  */
  addPlaceholderFocusClassToEventOrAnchorTag: function (eventOrAnchorTag, window){
	var topMostWindow = curam.util.getTopmostWindow();
	topMostWindow.curam.util.PLACEHOLDER_WINDOW_LIST = 
	  !topMostWindow.curam.util.PLACEHOLDER_WINDOW_LIST ? [] : topMostWindow.curam.util.PLACEHOLDER_WINDOW_LIST; 
	domClass.add(eventOrAnchorTag,"placeholder-for-focus");
	topMostWindow.curam.util.PLACEHOLDER_WINDOW_LIST.push(window);
  },

  /**
   * 
   * Returns the focus to a anchor element when the pop-up dialog is closed and
   * removes placeholder-for-focus appended to the anchor element class in the iframe content.
   */
   returnFocusToPopupActionAnchorElement: function(parentWindowRef) {
	 var focusList = parentWindowRef.curam.util.PLACEHOLDER_WINDOW_LIST;
	 if (focusList && focusList.length > 0 ){
	   var parentWindow = focusList.pop();
       var iFrameContent = parentWindow && parentWindow.document.activeElement;
	
       var anchorElementToFocusOn = iFrameContent && dojo.query(".placeholder-for-focus", iFrameContent); 
       if(anchorElementToFocusOn && anchorElementToFocusOn.length == 1){
         anchorElementToFocusOn[0].focus();
         domClass.remove(anchorElementToFocusOn[0],"placeholder-for-focus");
       }
       parentWindowRef.curam.util.PLACEHOLDER_WINDOW_LIST.splice(0, parentWindowRef.curam.util.PLACEHOLDER_WINDOW_LIST.length);
       parentWindowRef.curam.util.PLACEHOLDER_WINDOW_LIST = null;
     }
   },

  // Opens a modal dialog.
  // This is the public API function.
  // The function can take an anchor tag or an event as its first parameter
  // The uimToken parameter is only used in conjunction with the UIMDialog API.
  openModalDialog: function(
    eventOrAnchorTag, windowOptions, left, top, uimToken) {
	
	eventOrAnchorTag.event && 
	  curam.util.addPlaceholderFocusClassToEventOrAnchorTag(eventOrAnchorTag.event, eventOrAnchorTag.event.ownerDocument.defaultView.window);

    var href;
    if(!eventOrAnchorTag || !eventOrAnchorTag.href) {
      // it is an event
        eventOrAnchorTag = dojoEvent.fix(eventOrAnchorTag);

      var target = eventOrAnchorTag.target;
        while(target.tagName != "A" && target != windowBase.body()){
        target = target.parentNode;
      }

      href = target.href;

      //Mark the anchor tag as a modal dialog opener, so that other listeners on
      //it, e.g. the List Context Menus in /jscript/curam/listMenu.js, ignore
      //clicks on it.
      target._isModal = true;

        dojoEvent.stop(eventOrAnchorTag);

    } else {
      // it is an anchorTag
      href = eventOrAnchorTag.href;
      eventOrAnchorTag._isModal = true;
    }

        require(["curam/dialog"]);
    var opts = curam.dialog.parseWindowOptions(windowOptions);
    curam.util.showModalDialog(href, eventOrAnchorTag,
          opts['width'], opts['height'], left, top,  false, null, null, uimToken);
    return true;

  },

  // Shows a modal dialog.
  // Internal function, used from the public openModalDialog() function above
  // and also from pop-up-related code (omega3-util.js).

  // The uimToken parameter is only used in conjunction with the UIMDialog API.

  // @param realParent
  //            The parent window the request to open modal originated from.
  showModalDialog: function(url, eventOrAnchorTag,
      width, height, left, top, resizable, status, realParent, uimToken) {

    // handling nested modals ->
    // if called from within modal, redirect call to the parent window.
    var topmostWindow = curam.util.getTopmostWindow();
    if (dojo.global != topmostWindow) {
      curam.debug.log(
          "curam.util.showModalDialog: "
            + debug.getProperty("curam.util.redirecting.modal"));
      topmostWindow.curam.util.showModalDialog(url, eventOrAnchorTag,
          width, height, left, top, resizable, status, dojo.global, uimToken);
      return;
    }

    var rup = curam.util.replaceUrlParam;
    url = rup(url, "o3frame","modal");
    url = curam.util.modifyUrlContext(url, 'MODAL', 'TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM');
    url = rup(url, curam.util.PREVENT_CACHE_FLAG, (new Date()).getTime());
    curam.debug.log(debug.getProperty("curam.util.modal.url"), url);

    if (width) {
      width = typeof(width) == 'number' ? width : parseInt(width);
    }
    if (height) {
      height = typeof(height) == 'number' ? height : parseInt(height);
    }

    // Prevent multiple further requests for modal until this one is processed
    if (!curam.util._isModalCurrentlyOpening()) {
      // handle scenarios where user clicks on a modal link before main content
      // has finished loading
      if (url.indexOf('__o3rpu=about%3Ablank') >= 0 ) {
        alert(curam_util_showModalDialog_pageStillLoading);
        return;
      }

      curam.util._setModalCurrentlyOpening(true);

      if (jsScreenContext.hasContextBits("EXTAPP")) {
        require(["curam/ModalDialog"]);
        new curam.ModalDialog({href: url,
                               width: width,
                               height: height,
                               openNode: (eventOrAnchorTag && eventOrAnchorTag.target) ? eventOrAnchorTag.target : null,
                               parentWindow: realParent,
                               uimToken: uimToken});
      } else {
      	// Modal implemented in Carbon
        require(["curam/modal/CuramCarbonModal"]);
        new curam.modal.CuramCarbonModal({href: url,
                               width: width,
                               height: height,
                               openNode: (eventOrAnchorTag && eventOrAnchorTag.target) ? eventOrAnchorTag.target : null,
                               parentWindow: realParent,
                               uimToken: uimToken});
      }
      return true;
    }
  },
  // Shows a modal dialog and returns its reference.
  // Internal function, currently just used by the file edit widget.
  // @param realParent
  //            The parent window the request to open modal originated from.
  // @param windowsOptions
  //            The windows options to open a modal dialod.
  
  showModalDialogWithRef: function(modUrl, realParent, windowsOptions) {
    var topmostWindow = curam.util.getTopmostWindow();
    if (dojo.global != topmostWindow) {
      return topmostWindow.curam.util.showModalDialogWithRef(modUrl, dojo.global);
    }
    var rup = curam.util.replaceUrlParam;
    modUrl = curam.util.modifyUrlContext(modUrl, 'MODAL', 'TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM');
    modUrl = rup(modUrl, curam.util.PREVENT_CACHE_FLAG, (new Date()).getTime());
    if (!curam.util._isModalCurrentlyOpening()) {
      curam.util._setModalCurrentlyOpening(true);
      var dl;
      if (jsScreenContext.hasContextBits("EXTAPP")) {
        require(["curam/ModalDialog"]);
        if(windowsOptions){
            dl = new curam.ModalDialog({href: modUrl, width: windowsOptions.width, height: windowsOptions.height,  parentWindow: realParent});

        }else{
            dl = new curam.ModalDialog({href: modUrl,  parentWindow: realParent});
        }
      }else{
        require(["curam/modal/CuramCarbonModal"]);
        if(windowsOptions){
          dl = new curam.modal.CuramCarbonModal({href: modUrl, width: windowsOptions.width, height: windowsOptions.height,  parentWindow: realParent});
        }else{
          dl = new curam.modal.CuramCarbonModal({href: modUrl,  parentWindow: realParent});
        }
      }
      return dl;
    }
  },

  /**
   * Determine if a modal is being opened at this time.
   *
   * @returns True if modal open operation is in progress, otherwise false.
   */
  _isModalCurrentlyOpening: function() {
    return curam.util.getTopmostWindow().curam.util._modalOpenInProgress;
  },

  /**
   * Set the flag indicating if a modal is being opened at this time.
   *
   * @param isOpening {Boolean} Value for the flag, either true or false.
   */
  _setModalCurrentlyOpening: function(isOpening) {
    curam.util.getTopmostWindow().curam.util._modalOpenInProgress = isOpening;
  },

 setupPreferencesLink: function(href) {
      ready(function() {
        var prefsAnchor = query(".user-preferences")[0];
      if (prefsAnchor) {
        if (typeof(prefsAnchor._disconnectToken) == "undefined") {
          prefsAnchor._disconnectToken = curam.util.connect(prefsAnchor,
              "onclick", curam.util.openPreferences);
        }

        if (!href) {
            href = dojo.global.location.href;
        }

      } else {
        curam.debug.log(debug.getProperty("curam.util.no.setup"));
      }
    });
  },
  
  /**
   * Set up the link for the Language menu-item.
   *
   * @param href hyperlink
   *          
   */
  setupLocaleLink: function(href) {
      ready(function() {
        var localeAnchor = query(".user-locale")[0];
      if (localeAnchor) {
        if (typeof(localeAnchor._disconnectToken) == "undefined") {
        	localeAnchor._disconnectToken = curam.util.connect(localeAnchor,
              "onclick", curam.util.openLocaleNew);
        }
        if (!href) {
            href = dojo.global.location.href;
        }

      } else {
        curam.debug.log(debug.getProperty("curam.util.no.setup"));
      }
    });
  },
  
  openPreferences: function(event) {
      dojoEvent.stop(event);

    if (event.target._curamDisable) {
      // link disabled, do nothing
      return;
    }

    require(["curam/tab"], function() {
      curam.tab.getTabController().handleLinkClick(
          "user-prefs-editor.jspx", {dialogOptions:"width=605"});
    });
  },
  
  logout: function(event) {
          var topWin = curam.util.getTopmostWindow();
          topWin.dojo.publish("curam/redirect/logout");
          window.location.href = jsBaseURL + "/logout.jsp";
  },

  openAbout: function(event) {
      dojoEvent.stop(event);
      require(["curam/tab"], function() {
        curam.tab.getTabController().handleLinkClick(
            "about.jsp", {dialogOptions:"width=583,height=399"});
      });
  },

  addMinWidthCalendarCluster: function(id){

      var contentNode = dom.byId(id);
    var i = 0;

    function addWidth(evt){
        array.forEach(contentNode.childNodes, function(node){
          if(domClass.contains(node, "cluster")){
            style.set(node, "width", "97%");
          if(node.clientWidth < 700){
              style.set(node, "width", "700px");
          }
        }
      });
    }//end function addWidth

      if(has("ie") > 6){
        array.forEach(contentNode.childNodes, function(node){
          if(domClass.contains(node, "cluster")){
            style.set(node, "minWidth", "700px");
        }
      });
    } else {
        on(dojo.global, 'resize', addWidth);
        ready(addWidth);
    }
  },

  addPopupFieldListener: function(id){
      if(!has("ie") || has("ie") > 6){
      return;
    }
    if(!curam.util._popupFields) {
      function doResize(evt){
        var actionWidth=0;
        var j = 0;
        var x = 0;
        var arr = curam.util._popupFields;
          array.forEach(curam.util._popupFields, function(id){
            var fieldNode = dom.byId(id);
            query("> .popup-actions", fieldNode).forEach(function(node){
            actionWidth = node.clientWidth + 30;
          });

            query("> .desc", fieldNode).forEach(function(node){
              style.set(node, "width",
              Math.max(0, fieldNode.clientWidth - actionWidth) + "px");
          });
        });
      }// end doResize function
      curam.util._popupFields = [id];
        on(dojo.global, 'resize', doResize);
        ready(doResize);
    } else {
     curam.util._popupFields.push(id);
    }
  },

  /**
   * Sets the width and height (on IE6) of the main content area and sidebar
   * when the window is resized. The sidebar is not always included on a page.
   */
  addContentWidthListener: function(id) {
      if (has("ie") > 6) {
      // don't do if IE is 7 and higher
      return;
    }
      var setStyle = style.set;
      var hasClass = domClass.contains;

    function doResize(evt) {
      var i = 0;
        var contentNode = dom.byId("content");
      if (contentNode) {
        var width = contentNode.clientWidth;

        // Only set the height of the content if the footer is present,
        // and if the browser is Internet Explorer 6
          if (has("ie") == 6 && dom.byId("footer")) {
            var contentHeight = windowBase.body().clientHeight - 100;
          setStyle(contentNode, "height", contentHeight + "px");
            var sideNode = dom.byId("sidebar");
          if (sideNode) {
            setStyle(sideNode, "height", contentHeight + "px");
          }
        }

        try{
            query("> .page-title-bar", contentNode).forEach(function(node){
              var marginW = geom.getMarginSize(node).w
                  - geom.getContentBox(node).w;
              if (!has("ie")) {
              marginW +=1;
            }
            width =  contentNode.clientWidth - marginW;
              style.set(node, "width", width + "px");
          });
        }catch(e){
        // Do nothing. If the page-title-bar does not exist it won't need to be resized.
        }

          query("> .page-description", contentNode).style("width", width + "px");
          query("> .in-page-navigation", contentNode).style("width", width + "px");
      }
    }

    curam.util.subscribe("/clusterToggle", doResize);
    curam.util.connect(dojo.global, 'onresize', doResize);
      ready(doResize);
  },

  //depending on the final row and height of the visible scrollable area
  //we will have to add/remove the bottom border on the final row
  alterScrollableListBottomBorder: function(id, maxHeight){

    var visibleAreaHeight = maxHeight;
    var queryText = "#" + id + " table"; //to find the table contained in the div

    function alterBorder() {
        var scrollTable = query(queryText)[0];
      if (scrollTable.offsetHeight >= visibleAreaHeight) { //scrollbar visible & active
        //dont want a border on final row, if an odd row
          var lastRow = query(".odd-last-row", scrollTable)[0];
        if (typeof lastRow != "undefined") {
            domClass.add(lastRow, "no-bottom-border");
        }
      }
      else if (scrollTable.offsetHeight < visibleAreaHeight) { //scrollbar visible & inactive
        //we want a border on final row, if an even row
          var lastRow = query(".even-last-row", scrollTable)[0];
        if (typeof lastRow != "undefined") {
            domClass.add(lastRow, "add-bottom-border");
        }
      }
      else {
        curam.debug.log("curam.util.alterScrollableListBottomBorder: "
          + debug.getProperty("curam.util.code"));
      }
    }
    //added onLoad event to stop IE7 reading table heght before it is fully loaded
      ready(alterBorder);

  },

  //Set the width (on IE6) of the file upload button and its associated textfield,
  // and it will be auto resized when the window is resized.
  addFileUploadResizeListener:function(code){

    function fileUploadResize(evt){

        if(query(".widget")){

                query(".widget").forEach(function(widgetNode){
                var width = widgetNode.clientWidth;
                    if(query(".fileUpload", widgetNode)){

                        query(".fileUpload", widgetNode).forEach(function(fileUploadNode){
                        fileUploadWidth = width/30;
                        if(fileUploadWidth < 4){
                          fileUploadNode.size= 1;
                        }
                        else{
                          fileUploadNode.size= fileUploadWidth;
                        }
                      });
                  }
              });
          }
    }
      on(dojo.global, 'resize', fileUploadResize);
      ready(fileUploadResize);
  },


  //Opens a pop up dialog, non-modal, in the centre of the screen.
  openCenteredNonModalWindow: function(url, width, height, name) {
    // Fudge factors for window decoration space.
    width = Number(width);
    height = Number(height);
    var offsetLeft = (screen.width - width) / 2;
    var offsetTop = (screen.height - height) / 2;

    height = offsetTop < 0 ? screen.height : height;
    offsetTop = Math.max(0, offsetTop);

    width = offsetLeft < 0 ? screen.width : width;
    offsetLeft = Math.max(0, offsetLeft);

    var left = "left", top = "top";
      if(has("ff")) {
       left = "screenX", top = "screenY";
    }
    var defaultOptions = "location=no, menubar=no, status=no, toolbar=no, "
                         + "scrollbars=yes, resizable=no";

      var newWin = dojo.global.open(url, name || "name",
      'width=' + width + ', height=' + height + ', ' +
      left + '=' + offsetLeft + ',' + top + '=' + offsetTop + ',' +
            defaultOptions );
    // Enforce the size of the window.
    newWin.resizeTo(width, height);

    // Enforce the position of the window
    newWin.moveTo(offsetLeft, offsetTop);
    newWin.focus();
  },

  adjustTargetContext: function(win, href) {
      if (win && win.dojo.global.jsScreenContext) {
        var oldContext = win.dojo.global.jsScreenContext;
        oldContext.updateStates(dojo.global.jsScreenContext);
      return curam.util.replaceUrlParam(href, "o3ctx", oldContext.getValue());
    }
    return href;
    // TO DO: what context is needed to return here if the conditional
    // returns false?
    // Will be looked at in TEC-7946
  },

  modifyUrlContext: function(url, addBits, clearBits) {
    var newUrl = url;
    var ctx = new curam.util.ScreenContext();
    var valueInUrl = curam.util.getUrlParamValue(url, "o3ctx");
    if (valueInUrl) {
      ctx.setContext(valueInUrl);
    } else {
      ctx.clear();
    }
    if (addBits) {
      ctx.addContextBits(addBits);
    }
    if (clearBits) {
      ctx.clear(clearBits);
    }
    newUrl = curam.util.replaceUrlParam(url, "o3ctx", ctx.getValue());
    return newUrl;
  },

  updateCtx: function(initialValue) {
  var valueInUrl = curam.util.getUrlParamValue(initialValue, "o3ctx");
  if (!valueInUrl) {
    return initialValue;
  }
    return curam.util.modifyUrlContext(initialValue, null, 'MODAL');
  },

  getFrameRoot: function(thisWindow, rootObjectName) {
    var found = false;
    var topRef = thisWindow;
    if (topRef) {
      while (topRef != top && !topRef.rootObject) {
        topRef = topRef.parent;
      }
      if (topRef.rootObject) {
        found = (topRef.rootObject == rootObjectName);
      }
    }

    return found ? topRef : null;
  },

  //Saves HTML for informational messages locally on the clients machine.
  //This is used by modal windows to store informational messages before they
  //shut down. The parent page then loads these messages using
  //curam.util.loadInformationalMsgs, and clears the local
  //stored versions, so they are only loaded once.
  saveInformationalMsgs: function(callback) {
      try {
        localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID] =
              json.toJson({
              //Store the page ID, so these messages are only loaded back onto
              //the same page as the one on which they were saved.
                pageID: windowBase.body().id,

              //Store the entire set of messages, including the header, the
              //<ul> element and it's contents. This will be used in most cases.
                total: dom.byId(curam.util.ERROR_MESSAGES_CONTAINER).innerHTML,

              //Save just the <li> elements. If the parent page already has
              //informational messages, these are appended to the existing list.
              //This should not happen really, but is possible. I think...
              //Either way, this doesn't hurt.
                listItems: dom.byId(curam.util.ERROR_MESSAGES_LIST).innerHTML
        });

      callback();

      } catch (e) {
        curam.debug.log(debug.getProperty("curam.util.exception"), e);
      }
    },

  //Disables the loading of informational messages on the current page.
  //This prevents the messages being cleared for a modal dialog's parent page
  //if the loadInformationalMsgs function runs on the page after the messages
  //are saved using the saveInformationalMsgs function.
  disableInformationalLoad: function() {
    curam.util._informationalsDisabled = true;
  },

  redirectDirectUrl: function() {
      ready(function(){
        if (dojo.global.parent == dojo.global) {
        var url = document.location.href;
        var idx=url.lastIndexOf("/");
        if (idx > -1) {
          if (idx <= url.length) {
           url = url.substring(idx + 1);
         }
        }

        dojo.global.location = jsBaseURL + "/AppController.do?o3gtu=" + encodeURIComponent(url);
      }
    });
  },

  //Loads any informational messages from local storage, and puts them
  //on the page. If they exist, they are wiped out, so that they only show once.
  loadInformationalMsgs: function() {
    ready(function() {
      // no informational messages are to be displayed within the context panel
        if(dojo.global.jsScreenContext.hasContextBits('CONTEXT_PANEL')) {
          return;
        }

      if(curam.util._informationalsDisabled) {
        return;
      }

      var msgs = localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID];

        // If informational messages are stored locally, insert them into the
        // page
        if(msgs && msgs != "") {
          //Deserialize the messages from text to a JSON object
            msgs = json.fromJson(msgs);

          //Wipe out the messages so they are only loaded once
          //We could use the dojox.storage.remove method here, but it is not
          //supported by all storage implementations, so just make it an empty
          //string.
          localStorage.removeItem(curam.util.INFORMATIONAL_MSGS_STORAGE_ID);
            var div = dom.byId(curam.util.ERROR_MESSAGES_CONTAINER);
            var list = dom.byId(curam.util.ERROR_MESSAGES_LIST);

          //Only load the messages on the same page as they were saved.
          //The body of each page has it's ID set, which is the name of the
          //UIM file, more or less, and is therefore unique.
            if(msgs.pageID != windowBase.body().id) {
            return;
          }

          // If there are somehow messages already on the page, do not override
          // them, just append to them.
          if(list) {
            //Don't append duplicate informational messages.
              var tempUL = domConstruct.create("ul", {
              innerHTML: msgs.listItems
            });

            //Create an array of the LI elements already in the list.
            var currentLIs = [];
            for(var i = 0; i < list.childNodes.length; i++) {
              if(list.childNodes[i].tagName == "LI"){
                currentLIs.push(list.childNodes[i]);
              }
            }

            //Go through the existing messages to check for duplicates
            //If no duplicate informational message exists, then append the
            //message to the existing list of messages.
            var skip = false;
            var nodes = tempUL.childNodes;
            for(var i = 0; i < nodes.length; i++) {
              skip = false;
              for(var j = 0; j < currentLIs.length; j++) {
                if(nodes[i].innerHTML == currentLIs[j].innerHTML) {
                  skip = true;
                  break;
                }
              }
              if(!skip) {
                list.appendChild(nodes[i]);
                i--;
              }
            }
          } else if(div){
            div.innerHTML = msgs.total;
          }
        }
        
        var informationalMessage = dom.byId('container-messages-ul') ? dom.byId('container-messages-ul') : dom.byId('error-messages');
        if (informationalMessage && !dojo.global.jsScreenContext.hasContextBits("MODAL")) {
        
        // Keep focus on the tab if it was the last element selected, otherwise focus
        // on the informational message.
        if (curam.util.getTopmostWindow().curam.util.tabButtonClicked) {
          curam.util.getTopmostWindow().curam.util.getTabButtonClicked().focus();
          setTimeout(function() {
            // For accessibility purposes, the info message should still be announced
            // even when not focussed on. This triggers the screen reader to announce
            // the content
            informationalMessage.innerHTML = informationalMessage.innerHTML + " ";
          }, 500);
        } else {
            informationalMessage.focus();
          }
        }

        // A wrapper div element for an error message only exists for modals as defined in gen-jsp.
        // The explicit focus on this div is necessary for IE11 to fix an issue on an edge case
        // where both a non-modal page and a modal page have respectively error messages. This forces the
        // screen reader to announce the correct error message to the user.
        var informationalMessageOnModal = dom.byId('error-messages-container-wrapper');
        if (informationalMessageOnModal) {
          var modalErrorMessagesElement = query("#container-messages-ul", informationalMessageOnModal)[0];
          if (modalErrorMessagesElement){
            modalErrorMessagesElement.focus();
          }
        }
      });
  },
  /**
   * Sets the attribute tabindex for the currrent iframe and set the
   * focus. Created timeout to remove the attribute tabindex
   * after 10 miliseconds. Method called to allow JAWS sets forms mode 
   * on Edge Chromium browser.
   */
  _setFocusCurrentIframe: function(){
	var isEdge = /Edg/.test(navigator.userAgent);
	if(isEdge){
	  var currentIframe = window.frameElement;
      if(currentIframe){
	    currentIframe.setAttribute("tabindex", "0");
	    currentIframe.focus();
	    setTimeout(function(){
          currentIframe.removeAttribute("tabindex");
        },10);
      }

	}
  },

  /** 
   * sets focus to the input field with the biggest value
   * of tabindex property.
   */
	setFocus: function() {
		
    var setFocusTimeout;		   		
    if(window.document.getElementsByClassName("skeleton").length>0){			   
              setFocusTimeout=setTimeout(function (){curam.util.setFocus();},300);            
    }else{
	if(setFocusTimeout){
	  clearTimeout(setFocusTimeout);
     }
		   		
    var isFocusSetOn =curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton;
	if (isFocusSetOn) {
	 return;
	}
	curam.util._setFocusCurrentIframe();
    var isModal = 
        curam.util.getUrlParamValue(dojo.global.location.href, "o3frame") == "modal" || 
        (curam.util.getUrlParamValue(dojo.global.location.href, "o3modalprev") !== null && 
        curam.util.getUrlParamValue(dojo.global.location.href, "o3modalprev") !== undefined); //IE11
    if (!isModal) {
      // for pages in main content call setFocus here, modals will
      // do it differently in ModalDialog.js
        ready(function(){
          var errorMessage = dom.byId('container-messages-ul') ? dom.byId('container-messages-ul') : dom.byId('error-messages');
          var curamDefaultActionId = sessionStorage.getItem('curamDefaultActionId');
          var focusField = null;
          if (!errorMessage && curamDefaultActionId) {
            sessionStorage.removeItem('curamDefaultActionId');
            focusField =  dojo.query(".curam-default-action")[0].previousSibling;
          } else {
            focusField = curam.util.doSetFocus();
          }
          if (focusField) {
            // Call util.setFocusOnField() function where browser specific focus handling occurs.
            // For example, if IE11/Edge is the current browser, there will be a delay before
            // the focus is set. If Chrome is the current browser, the field receives focus immediately.
            curam.util.setFocusOnField(focusField, false);
          } else {
            window.focus();
          }
        });
   	 }
   }
  },
  
  /**
   * Browser specific focus handling occurs here. For example, if IE11/Edge is the current browser,
   * there will be a delay before the focus is set. If Chrome is the current browser, the field
   * receives focus immediately.
   * 
   *  @param focusField The field to focus on.
   *  @param isModal Flag indicating whether the page is a modal or not.
   *  @param localizedModalFrameTitle Localized modal frame title text that's
   *  used when the current page is open in a modal.
   */
  setFocusOnField: function(focusField, isModal, localizedModalFrameTitle) {
    
    // If the browser is not IE11 or Edge, focus on the field immediately.
	  if (has('IE') || has('trident')) {
      
      // Constants for setting focus timeout threshold.
      // Textareas require a longer delay as JAWs forms mode is not triggered consistently with anything less.
      // Text input fields/combo boxes require a different timeout depending on whether the field is
      // present in a modal or tabbed page in order to help trigger forms mode in JAWs.
      var textAreaFocusDelayThreshold = 1000;
      var inputFocusDelayThreshold = isModal ? 200 : 500;
      
      // Creating a visually hidden input and focussing on that initially before focussing on the
      // actual focusField helps the screenreader switch to forms mode for the appropriate fields in IE11.
      curam.util._createHiddenInputField(focusField);

      // Function to execute after timeout threshold has been reached.
      var fnFocus = function(ff){
        return function() {

          var currentNode = ff.ownerDocument.activeElement;
          // do nothing if already focused on a form element
          if (currentNode.tagName === 'INPUT' && !currentNode.classList.contains('hidden-focus-input') 
              || currentNode.tagName === 'TEXTAREA'
              || (currentNode.tagName === 'SPAN' && currentNode.className == 'fileUploadButton')
              || (currentNode.tagName === 'A' && currentNode.className == 'popup-action')
              || (currentNode.tagName === 'IFRAME' && currentNode.classList.contains('cke_wysiwyg_frame'))) {
            return;
          } else {
            ff.focus();
          }
        
        } 
      };
      
      if(isModal) {
	  
        // Only update the aria-label when setting focus within a modal.
        var ownAria = attr.get(focusField, 'aria-label');
        var localizedTitle = "";
        // Cases for combo boxes where a subcomponent value is dependent on its main component's (component) selected value.
        // Both components have ojbid attributes respectively set as "component" and "subComponent".
        // Added case for stand-alone combo box.
        var componentObjId = attr.get(focusField,"objid");
        if (componentObjId && componentObjId.indexOf("component") == 0
            || domClass.contains(focusField,"dijitReset dijitInputInner")){
          localizedTitle = focusField.title;
        } else {
          localizedTitle = localizedModalFrameTitle
                             || 'Modal Dialog';
        }

        // Prevent aria-label with localizedTitle from being added to error-messages as it prevents the screen reader from
        // announcing the contents of the error messages list.
        if (focusField && focusField.id !== "container-messages-ul") {
          attr.set(focusField, 'aria-label', localizedTitle);
        }
        
        var restorer = function(oldAria) {
          return function(e) {
            query('input|select[aria-label=' + localizedTitle + ']')
              .forEach(function(entry) {
              oldAria && attr.set(entry, 'aria-label', oldAria);
              !oldAria && attr.remove(entry, 'aria-label');
              });
          }
        }
        on(focusField, 'blur', restorer(ownAria));
      }
      
      // Only fields that require user interaction need a delay in setting focus.
      // Textareas require a longer delay as forms mode is not triggered consistently with anything less.
      // Text input fields, comboboxes and select elements require a shorter delay to help trigger forms mode.
      // Any other element that receives focus and does not require forms mode in order for the user to interact
      // with it, will receive focus without any delay.
      if (focusField.tagName === 'TEXTAREA') {
        setTimeout(fnFocus(focusField), textAreaFocusDelayThreshold);
      } else if (focusField.tagName === 'SELECT' || (focusField.tagName === 'INPUT' && attr.get(focusField, "type") === "text")) {
        setTimeout(fnFocus(focusField), inputFocusDelayThreshold);
      } else {
        focusField.focus();
      }
    } else {
      focusField.focus();
    }
  },
  
  /**
   * Creating a visually hidden input and focussing on that initially before focussing on the
   * actual focusField helps the screenreader switch to forms mode for the appropriate fields in IE11.
   * This is destroyed as soon as focus leaves the field.
   * 
   * @param focusField The field to focus on.
   */
  _createHiddenInputField: function(focusField) {
    
    var mainForm = focusField.ownerDocument.forms['mainForm'];
    // Only create the hidden input for fields that require forms mode to be automatically switched on.
    if (mainForm && (focusField.tagName === 'SELECT' || (focusField.tagName === 'INPUT' && attr.get(focusField, "type") === "text"))) {
      // Visually hide the input field.
      var hiddenInput = domConstruct.create("input", 
                        {"class": "hidden-focus-input", 
                         "style": "position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;", 
                         "type": "text", 
                         "aria-hidden": "true", 
                         "tabindex": "-1"});
      domConstruct.place(hiddenInput, mainForm, "before");
      hiddenInput.focus();
      on(hiddenInput, 'blur', function(){domConstruct.destroy(hiddenInput)});
    }
  },

  /**
   * Set focus on the first editable field in the page. If there are no editable
   * fields, the function does not do anything. In case the first editable field
   * is a FilteringSelect widget than it skips the arrow and validation container
   * and sets the focus on the input container of FilteringSelect widget.
   *
   * @returns {Object} the field to focus on, or boolean false if no such
   * field was found. 
   */
  doSetFocus: function() {
	try {
    //return the tab button if the user is navigating through tabs
	  var tabButton = curam.util.getTopmostWindow().curam.util.getTabButtonClicked();
	  if(tabButton != false && !curam.util.isModalWindow()){
	    return tabButton;
	  }

	  var errorMessage = dom.byId('container-messages-ul') ? dom.byId('container-messages-ul') :  dom.byId('error-messages');
	  if (errorMessage) {
	    return errorMessage;
      }
        
	  var form = document.forms[0];
	  if (!form) {
	    return false; //no editable fields - nothing to do
	  }
      
	  
	  var fields = form.querySelectorAll(
	  'button, output, input:not([type="image"]), select, object, textarea, fieldset, a.popup-action, span.fileUploadButton');
	  var focusField = false;
	  var l = fields.length, el;
	  for (var i = 0; i < l; i++) {
	    el = fields[i];
	    // exception: draw focus on the dijit select if that happens to
	    // be first editable field (and not overridden).
	    if (!focusField && /selectHook/.test(el.className)){
	      focusField = query(el).closest('table')[0];
	    }
        if (!focusField && !(el.style.visibility=="hidden") && (
	      /select-one|select-multiple|checkbox|radio|text/.test(el.type)
	        || el.tagName == "TEXTAREA" || /popup-action|fileUploadButton/.test(el.className))
	        && !/dijitArrowButtonInner|dijitValidationInner/.test(el.className)) {
	       focusField = el;
	    }
	    if (el.tabIndex == '1') {
	      // reset the tab index to prevent it having an effect on
	      // screen tabbing.
	      el.tabIndex = 0;
	      focusField = el;
	      break; // once we've found an override, no need to keep looping.
	    }
      }
      //if the is no form field to put the focus on, the focus should go on the last opened tab.
      lastOpenedTabButton = curam.util.getTopmostWindow().curam.util.getLastOpenedTabButton();
      if(!focusField && lastOpenedTabButton) {            
          return lastOpenedTabButton;       
      }
      var dataPickerInputFirst = focusField.classList.contains("bx--date-picker__input");

      if(dataPickerInputFirst){
			
			var modalBody = document.querySelector(".bx--uim-modal");
			
			if(modalBody){
			   focusField = modalBody;
            }      
       }
	  return focusField;
	} catch(e) {
	  debug.log(debug.getProperty("curam.util.error.focus"), e.message);
	  return false;
	}
	return false;
  },

  openLocaleSelector: function(event) {
      event = dojoEvent.fix(event);
    var target = event.target;
    while(target && target.tagName != "A") {
      target = target.parentNode;
    }
    var loc = target.href;
    var rpu = curam.util.getUrlParamValue(loc, "__o3rpu");
    // strip the __o3rpu parameter form the RPU value
    rpu = curam.util.removeUrlParam(rpu, "__o3rpu");
    var href="user-locale-selector.jspx" + "?__o3rpu=" + rpu;

      if (!curam.util.isActionPage(dojo.global.location.href)) {
      openModalDialog({href:href},"width=500,height=300",200,150);

    } else {
      alert(curam.util.msgLocaleSelectorActionPage);
    }

    return false;
  },
  
  /**
   * Opens the new Language selector modal.
   *
   * @param event 
   *          the click event on the Language menu-item. 
   */
  openLocaleNew: function(event) {
    dojoEvent.stop(event);

    if (event.target._curamDisable) {
      // link disabled, do nothing
      return;
    }

    require(["curam/tab"], function() {
      curam.tab.getTabController().handleLinkClick(
          "user-locale-selector.jspx", {dialogOptions:"width=300"});
    });
  },

  isActionPage: function(url) {
    var pageAndQuery = curam.util.getLastPathSegmentWithQueryString(url);
    var pageName = pageAndQuery.split("?")[0];
    return pageName.indexOf("Action.do") > -1;
  },

  closeLocaleSelector: function(event) {
      event = dojoEvent.fix(event);
      dojoEvent.stop(event);

      dojo.global.close();
    return false;
  },

  /**
   * Retrieves the remaining part of a class name which has the specified prefix.
   * E.g. for <theNode class="class1 class2 thePrefix-remaining-part-of-name" />
   * the call to curam.util.getSuffixFromClass(theNode, "thePrefix-")
   * will return "remaining-part-of-name".
   *
   * @param node
   *          The note to get the class from.
   * @param prefix
   *          The prefix to find the class by.
   * @returns
   *    If the class with the prefix is found it returns the remaining
   *    part of the class name. If the class is not found, returns null.
   *    If more than one class is found, it returns the first occurrence.
   */
  getSuffixFromClass: function(node, prefix) {
      var classes = attr.get(node, "class").split(" ");
      var namesFound = array.filter(classes, function(className) {
      return className.indexOf(prefix) == 0;
    });
    if (namesFound.length > 0) {
      return namesFound[0].split(prefix)[1];

    } else {
      return null;
    }
  },

  getCacheBusterParameter : function() {
    return this.CACHE_BUSTER_PARAM_NAME + "=" + new Date().getTime() + "_"
        + this.CACHE_BUSTER++;
  },

  /**
   * Add classes to table rows to allow striping in appearance.
   * Handles expandable and paginated lists in a specific way.
   */
  stripeTable: function(table, isExpandableList, lastVisibleRowIndex) {
    var tbody = table.tBodies[0];
    var mainRowStep = (isExpandableList ? 2 : 1);

    // for a list with one row do nothing
    if (tbody.rows.length < mainRowStep) {
      return;
    }

    var rows = tbody.rows;
    var oddRows = [], evenRows = [], isLast = false, lastRows = [], lastClass='';
    for (var i = 0, l = rows.length; i < l; i += mainRowStep) {
      //curam.debug.log("curam.util.stripeTable(%s, %s): i = %s", table, isExpandableList, i);
      var isEven = (i % (2 * mainRowStep) == 0), targetArray =  isEven ? evenRows : oddRows;
      domClass.remove(rows[i], ['odd-last-row','even-last-row']);
      targetArray.push(rows[i]);
      if (i == lastVisibleRowIndex) {
        lastRows.push(rows[i]);
        lastClass = isEven ? 'odd':'even';
        isLast = true;
      }
      if (isExpandableList && rows[i + 1]) {
        domClass.remove(rows[i+1], ['odd-last-row','even-last-row']);
    	targetArray.push(rows[i+1]);
    	isLast && lastRows.push(rows[i+1]);
      }
      isLast = false;
     }
     evenRows.forEach(function(evenRow){
       domClass.replace(evenRow, 'odd', 'even'); 
     });
     oddRows.forEach(function(oddRow){
       domClass.replace(oddRow, 'even', 'odd'); 
     });
     lastRows.forEach(function(lastRow){
       domClass.add(lastRow, lastClass+'-last-row');
     });
  },

  fillString: function(fillChar, count) {
    // summary:
    //  Creates a string of 'x' repeating characters
    var string = "";
    while (count > 0) {
      string += fillChar;
      count-=1;
    }
    return string;
 },

  updateHeader: function(qId, newHeader, answer, source) {
      var header = dom.byId('header_' + qId);
    header.firstChild.nextSibling.innerHTML = newHeader;
      answerCell = dom.byId('chosenAnswer_' + qId);
    answerCell.innerHTML = answer;
      sourceCell = dom.byId('chosenSource_' + qId);
    sourceCell.innerHTML = source;
  },

  search: function(textWidgetID, typeWidgetID){
     // summary:
     //              Invokes the required search page for an application search as
     //              specified by the associated configuration data. How this
     //              is done is described below:
     //
     // description:
     //              <ol>
     //                <li>
     //                  Retrieving the value of the business object select drop
     //                  down. This value includes a business object code and
     //                  also a page identifier.
     //                </li>
     //                <li>
     //                  Retrieving the value of the input text control which
     //                  specifies the search text to be used.
     //                </li>
     //                <li>
     //                  If the drop list of business objects has been
     //                  specified in the associated configuration data, the
     //                  page identifier specified by the selected option will
     //                  be used to construct a URL using the
     //                  <code>searchText</code> and <code>searchType</code>
     //                  page parameters and that page is invoked.
     //                </li>
     //                <li>
     //                  If the drop list of business objects has not been
     //                  specified in the associated configuration data, the
     //                  default search page specified will be used to construct
     //                  a URL using the <code>searchText</code> page parameter
     //                  only and then that page is invoked.
     //                </li>
     //              </ol>
     //              <P>
     //              The data used here is generated by a renderer so therefore
     //              no validations are performed and it is assumed that the
     //              data is in the correct format for parsing.
     // textWidgetID:
     //              The identifier of the text input control for the application
     //              search which is used to retrieve the text to be used in the
     //              application search. The value taken from this field is
     //              associated with the <code>searchText</code> page parameter
     //              of the search page being invoked.
     // typeWidgetID:
     //              The identifier of the drop down control for the application
     //              search which is used to retrieve the business object type
     //              to be used in the application search. The value taken from
     //              this field is associated with the <code>searchType</code>
     //              page parameter of the search page being invoked. Note that
     //              this is optional as the list of business objects to search
     //              for is an optional element of an application search.

    // TODO: Move this function into the application view JavaScript file when
    // it becomes available -- FG.

        // Retrieve the search text from the input control.
      var searchText = registry.byId(textWidgetID).get("value");
    // The search type, if specified, from the drop down list of business
    // objects. This is a combination of the type and the page identifier
    // so split these out here.
      var searchTypeWidget = registry.byId(typeWidgetID);
          if (searchTypeWidget == null) {
                  searchTypeWidget = dom.byId(typeWidgetID);
          }
          var currentlySelectedItem = null;
          if(searchTypeWidget != null) {
                  if (searchTypeWidget.tagName == null) {
                        currentlySelectedItem
                  = searchTypeWidget ? searchTypeWidget.get("value"): null;
                  } else {
                        if (searchTypeWidget.tagName == 'SELECT') {
                          var options = query('.multiple-search-banner select option');
                          array.forEach(options, function(elem){
                                  if (elem.selected) {
                                          currentlySelectedItem = elem.value;
                                  }
                          });
                        }
                  }
          }
    var searchType = "";
    var pageIDFromSearchOptions;
    var tokens;

    if(currentlySelectedItem){
      // The currently selected item consists of the search type and the page
      // identifier to use in the search.
      tokens = currentlySelectedItem.split("|");
      searchType = tokens[0];
      pageIDFromSearchOptions = tokens[1];
    }

    var defaultSearchPageID = curam.util.defaultSearchPageID;

    // If the select box has not been specified, then the page identifier is
    // the default page identifier. Construct the page to be invoked.
    var searchPageURL = "";
    
    // Should the secure URLs exempt param be appended?
	if (sessionStorage.getItem('appendSUEP') === "true") {
	
	    if (searchType==="") {
	      searchPageURL = defaultSearchPageID + "Page.do?searchText="
	                                              + encodeURIComponent(searchText) 
	                                              + "&" + curam.util.secureURLsExemptParamName 
	                                              + "=" + encodeURIComponent(curam.util.secureURLsExemptParamsPrefix + "_ST1");
	    } else {
	      searchPageURL = pageIDFromSearchOptions + "Page.do?searchText="
	                          + encodeURIComponent(searchText) + "&searchType="
	                            + encodeURIComponent(searchType) + "&" 
	                            + curam.util.secureURLsExemptParamName 
	                            + "=" + encodeURIComponent(curam.util.secureURLsExemptParamsPrefix 
	                            + "_ST1,"+ curam.util.secureURLsExemptParamsPrefix +"_ST2");
	    }
    } else {
	
	    if (searchType==="") {
	      searchPageURL = defaultSearchPageID + "Page.do?searchText="
	                                              + encodeURIComponent(searchText);
	    } else {
	      searchPageURL = pageIDFromSearchOptions + "Page.do?searchText="
	                          + encodeURIComponent(searchText) + "&searchType="
	                            + encodeURIComponent(searchType);
	    }
    }
    // Call the function that will load the search page.
    var searchPageRequest = new curam.ui.PageRequest(searchPageURL);
    require(["curam/tab"], function() {
      curam.tab.getTabController().handlePageRequest(searchPageRequest);
    });
  },

  updateDefaultSearchText: function(typeWidgetID, textWidgetID){
    // summary:
    //              Updates the search text input control in the application
    //              search widget with some initial text from the item selected
    //              in the associated list of search business objects.
    //
    // description:
    //              Retrieves the initial text from the item selected in the list
    //              of business objects drop down and sets the place holder
    //              attribute of the application search text input control with
    //              this initial text. This is called from the
    //              <code>onChange()</code> event of the application search drop
    //              down list.
    // typeWidgetID:
    //              The identifier of the drop down control for the application
    //              search which is used to retrieve the initial text that is
    //              set in the place holder attribute of the associated text
    //              input control each time the value of the drop down list
    //              is changed. This initial text acts as an aid to the user
    //              when performing the search as it offers them a hint as to
    //              what should be entered into the text area for the type of
    //              search being performed.
    // textWidgetID:
    //              The identifier of the text input control whose place
    //              holder attribute will be set with initial text to aid the
    //              user perform the search.

    // TODO: Move this function into the application view JavaScript file when
    // it becomes available -- FG.

      var searchTypeWidget = registry.byId(typeWidgetID);
      var textWidget = registry.byId(textWidgetID);
      var currentlySelectedItem = searchTypeWidget ? 
    		  searchTypeWidget.get("value"): null;
      // The initial text is the third token in the string.
      var str = currentlySelectedItem.split("|")[2];

      textWidget.set("placeHolder", str);
      topic.publish('curam/application-search/combobox-changed', 
    		  currentlySelectedItem);
  },

  updateSearchBtnState: function(textWidgetID, btnID){
    // summary:
    //              Enables or disables the search anchor for an application
    //              search depending on whether this is text specified for the
    //              search.
    //
    // description:
    //              The search anchor specified for an application search is
    //              disabled if no text has been specified in the text input
    //              control. This function is called on the
    //              <code>onKeyPress</code> of the text input control so that when
    //              a user enters some text, the search anchor is enabled to allow
    //              that user perform the search.
    // textWidgetID:
    //              The identifier of the text input control for the application
    //              search which is used to determine if that text box contains
    //              any text. If it does, then the search anchor is enabled,
    //              otherwise it is disabled.
    // btnID:
    //              The identifier of the search anchor for the application search
    //              and this is used to retrieve the control to enable or disable
    //              depending on whether there is text in the associated search
    //              text input control.

    // TODO: Move this function into the application view JavaScript file when
    // it becomes available -- FG.
      var widget = registry.byId(textWidgetID);
      var btn = dom.byId(btnID);
      var value = widget.get("value");

      if(!value || lang.trim(value).length < 1){
        domClass.add(btn, "dijitDisabled");
      } else {
        domClass.remove(btn, "dijitDisabled");
    }
  },

  furtherOptionsSearch: function() {

    // summary:
    //              Invokes the page specified for the further options link
    //              in an application search.
    //
    // description:
    //               The further options search page is invoked by retrieving
    //               the page identifier specified in the configuration
    //               data, constructing a page request for that URL and
    //               calling the function to handle that request.
    //               <P>
    //               Validations performed elsewhere should ensure that the
    //               data is specified in the correct fashion and hence there
    //               are no validations invoked here.

    // TODO: Move this function into the application view JavaScript file when
    // it becomes available -- FG.

    // Retrieve the page identifier for the further options link.
    var furtherOptionsPageURL = curam.util.furtherOptionsPageID + "Page.do";

    // Call the function that will load the search page.
    var furtherSearchOptionsPageRequest
        = new curam.ui.PageRequest(furtherOptionsPageURL);
    require(["curam/tab"], function() {
      curam.tab.getTabController().handlePageRequest(
          furtherSearchOptionsPageRequest);
    });
  },

  searchButtonStatus: function(btnID){
    // summary:
        //              Returns true if the button is not disabled
        //
        // description:
        //              The search anchor specified for an application search is
        //              disabled if no text has been specified in the text input
        //              control. This function is called on the
        //              <code>onKeyPress</code> of the text input control if this function returns true.
        // btnID:
        //              The identifier of the search anchor for the application search
        //              and this is used to retrieve the control to enable or disabled
        var btn = dom.byId(btnID);
        if(!domClass.contains(btn,"dijitDisabled")){
          return true;
        }
  },

  /**
  * Return the height of the page contents for the current width.
  * Note that if the width changes, the contents height might change as well.
  *
  * Note: this algorithm is dependent on the structure of the
  * DOM. Changes to the page layout will probably break this.
  *
  * @return page height.
  */
  getPageHeight: function() {
    var defaultHeight = 400;
    var resultingHeight = 0;

      if (query("frameset").length > 0) {
      /* Detect framesets and use default height for them.
      * Normally frameset windows should have fixed height specified
      * by the developer, but this is for the cases in which the developer
      * forgets to specify the height.
      */
      curam.debug.log(
        "curam.util.getPageHeight() "
          + debug.getProperty("curam.util.default.height"),
      defaultHeight);
      resultingHeight = defaultHeight;

    } else {
      // create function for determinning bottom coordinate of an element
      var bottom = function(node) {
        if (!node) {
          curam.debug.log(debug.getProperty("curam.util.node"));
          return 0;
        }

        // Use getMarginBoxSimple if it's available
          var mb = geom.getMarginSize(node);
          var pos = geom.position(node);

        return pos.y + mb.h;
      };

        if (dojo.global.jsScreenContext.hasContextBits('LIST_ROW_INLINE_PAGE')) {

        // if we are in list details row, just use the content div
        var contentDiv = query("div#content")[0];
        var contentBottom = bottom(contentDiv);
        curam.debug.log(debug.getProperty("curam.util.page.height"), contentBottom);
        resultingHeight = contentBottom;

      } else {
          var contentObj = dom.byId("content") || dom.byId("wizard-content");

        // find the bottom-most node
          var nodes = query("> *", contentObj).filter(function(n) {
          // leave out script and hidden nodes
          return n.tagName.indexOf("SCRIPT") < 0
            && style.get(n, "visibility") != "hidden"
            && style.get(n, "display") != "none";
        });
        var bottomNode = nodes[0];
        for (var i = 1; i < nodes.length; i++) {
          if(bottom(nodes[i]) >= bottom(bottomNode)) {
            bottomNode = nodes[i];
          }
        }
        // first count in the main contents height
        resultingHeight = bottom(bottomNode);
        curam.debug.log("curam.util.getPageHeight() "
            + debug.getProperty("curam.util.base.height"), resultingHeight);

        // count in modal dialog action set, if present
        var doesActionSetExist = query(".actions-panel", windowBase.body());

        if (doesActionSetExist.length > 0) {
          // Only one actions panel exists in one modal, we can get the height
          // of this panel dynamically.
          var actionsPanelHeight = geom.getMarginBox(doesActionSetExist[0]).h;

          curam.debug.log("curam.util.getPageHeight() "
              + debug.getProperty("curam.util.panel.height"));

          // Add the actions panel height to the total page height, here we
          // presume the actions panel is always positioned at the bottom of
          // the page.
          resultingHeight += actionsPanelHeight;

          // Then add additional 10px spacing at the top of the actions panel.
          resultingHeight += 10;
        }

        // in case we are in the details panel, count in the title bar height
          var detailsPanel = query("body.details");
        if (detailsPanel.length > 0) {
          curam.debug.log("curam.util.getPageHeight() "
              + debug.getProperty("curam.util.bar.height"));
            resultingHeight += 20;
        }
      }
    }

      curam.debug.log("curam.util.getPageHeight() "
          + debug.getProperty("curam.util.returning"), resultingHeight);
    return resultingHeight;
  },

  /**
  * Takes an array and from its elements it creates a comma separated
  * string of values, which is then returned.
  */
  toCommaSeparatedList: function(inputArray) {
    var result = "";
    for (var i = 0; i < inputArray.length; i++) {
      result += inputArray[i];
      if (i < inputArray.length -1) {
        result += ",";
      }
    }
    return result;
  },


  

  /**
  * Registers a handler for submitting a form when Enter key is pressed.
  *
  * Called from the PageTag - will be called on every page in any context,
  * main content, dialog, etc.
  */
  setupGenericKeyHandler: function() {
      ready(function() {
      // The handler is never explicitly deregistered - disappears when
      // the runtime context is destroyed (new page loaded)
      var f = function(event) {

        // On ESC key handling: When ESC key is pressed and in a modal,
        // the modal will close automatically.
        if (dojo.global.jsScreenContext.hasContextBits('MODAL')
            && event.keyCode == 27) {
          var ev = dojoEvent.fix(event);
          var dropdown = registry.byId(ev.target.id);
          var isDropdown =
            typeof dropdown != "undefined" && dropdown.baseClass == "dijitTextBox dijitComboBox";
          if (!isDropdown) {
            curam.dialog.closeModalDialog();
          }
        }

        // On ENTER key handle the event
        if (event.keyCode == 13) {
            var ev = dojoEvent.fix(event);

        // only submit form when certain input fields have focus. This allows
        // for normal keyboard selection (example: pressing enter on date
        // selector icon) to happen without the form submitting.

    var isText = ev.target.type == "text";
    var isRadio = ev.target.type == "radio";
    var isCheckbox = ev.target.type == "checkbox";
    var isMultiSelect = ev.target.type == "select-multiple";
    var isPassword = ev.target.type == "password";

    var combo = registry.byId(ev.target.id);
    // Added a check so that form is not submitted when "Enter" key
    // is pressend in open state of dropdown.
    if (typeof combo != "undefined") {
      var comboWidget = registry.byId(ev.target.id);
      if (!comboWidget) {
        comboWidget = registry.byNode(dom.byId("widget_" + ev.target.id));
      }
      if(comboWidget && comboWidget.enterKeyOnOpenDropDown) {
        comboWidget.enterKeyOnOpenDropDown = false;
        return false;
      }
    }
    
    var carbonAttachPoint = ev.target.getAttribute("data-carbon-attach-point");
    if (carbonAttachPoint && carbonAttachPoint === 'carbon-menu') {
    	return false;
    }

    var isCombo =
    typeof combo != "undefined" && combo.baseClass == "dijitComboBox";
    if ((!isText && !isRadio && !isCheckbox && !isMultiSelect
    && !isPassword) || isCombo ) {
      return true;
    }
    var defaultSubmitButton = null;
      var explicitDefaultBtnArray = query(".curam-default-action");
    // take the default button if set
    if (explicitDefaultBtnArray.length > 0) {
      defaultSubmitButton = explicitDefaultBtnArray[0];

    } else {
      // otherwise take the first found submit button
        var submitButtonsArr = query("input[type='submit']");
      if (submitButtonsArr.length > 0) {
        defaultSubmitButton = submitButtonsArr[0];
      }
    }
    // now click the button found
    if (defaultSubmitButton != null) {
        dojoEvent.stop(dojoEvent.fix(event));
      curam.util.clickButton(defaultSubmitButton);
      return false;
    }
      //Focus remains in the date selector on ENTER
      require(["curam/dateSelectorUtil"], function(selectorUtil) {
        var isInputyear = dom.byId("year");
        if (isInputyear) {
          dojo.stopEvent(dojo.fixEvent(event));

          //Enter key updates the calendar
          selectorUtil.updateCalendar();
        }
     });

     }

     // otherwise let the event continue
       return true;
     };

     // event must be onKeyUp, as the ESC key event is not fired during an
     // onKeyPress event.
     curam.util.connect(windowBase.body(), "onkeyup", f);
  });
  },

  /**
  * Returns true is key press event is triggered by the enter key.
  * Used by context panel toggle icon and list/cluster toggle icons.
  */
  enterKeyPress: function(event) {
    if(event.keyCode == 13) {
      return true;
    }
  },

  /**
  * Given a DOM node, boolean state and class names for true and false
  * alternatives, the function sets the appropriate classes on the node.
  */
  swapState: function(node, state, classTrue, classFalse) {
    if (state) {
        domClass.replace(node, classTrue, classFalse);

    } else {
        domClass.replace(node, classFalse, classTrue);
    }
  },

  /**
  * Creates a URL query string including the leading question mark
  * from the specified page parameters.
  * The function handles URL-encoding of the values so do NOT encode them.
  *
  * @param params Object in the following format:
  *                { param1Name:"value", param2Name:248 }
  */
  makeQueryString: function(params) {
    if (!params || params.length == 0) {
      return "";
    }

    var result = [];
    for (var paramName in params) {
      result.push(paramName + "=" + encodeURIComponent(params[paramName]));
    }

    return "?" + result.join("&");
  },

  /**
   * Handles the onClick event for file download anchor tags.
   *
   * Subscribes to the window close event, opens a yes/no dialog, then when publish happens
   * it checks the chosen option and if 'confirm' it calls the clickHandlerForListActionMenu
   * function. The url parameter is passed to clickHandlerForListActionMenu function.
   *
   * @param url
   *          The URL of the file download servet.
   */
  fileDownloadAnchorHandler: function(url) {
    // Subscribe downloadOnConfirm function to the window closing event
    var topmostWin = curam.util.getTopmostWindow();
    var unsToken = topmostWin.dojo.subscribe("/curam/dialog/close", function(id, option) {
      if(option === 'confirm') {
        curam.util.clickHandlerForListActionMenu(url, false, false);
      }
      topmostWin.dojo.unsubscribe(unsToken);
    });

    // Get the dialog window width and height from properties file
    var bundle = new resBundle("GenericModalError");
    var width = bundle.getProperty("file.download.warning.dialog.width");
    var height = bundle.getProperty("file.download.warning.dialog.height");
    if(!width) {
      width = 500; // Default value
    }
    if(!height) {
      height = 225; // Default value
    }

    // Show an ok/cancel dialog with browser-specific security steps
    var browserName = curam.util._getBrowserName();
    curam.util.openGenericErrorModalDialogYesNo(
        "width="+width+",height="+height,
        "file.download.warning.title",
        "file.download.warning."+browserName);

    return false; // Return false so that initial onclick event does not download the file
  },

  /**
   * Handles the onClick event for file download list action menu items.
   *
   * Subscribes to the window close event, opens a yes/no dialog, then when publish happens
   * it checks the chosen option and if 'confirm' it calls the clickHandlerForListActionMenu
   * function. The method parameters are all passed to clickHandlerForListActionMenu function.
   *
   * @param url
   *          The URL of the file download servet.
   */
  fileDownloadListActionHandler: function(url, sameDialog, newWindow, event) {
    // Subscribe downloadOnConfirm function to the window closing event
    var topmostWin = curam.util.getTopmostWindow();
    var unsToken = topmostWin.dojo.subscribe("/curam/dialog/close", function(id, option) {
      if(option === 'confirm') {
        curam.util.clickHandlerForListActionMenu(url, sameDialog, newWindow, event);
      }
      topmostWin.dojo.unsubscribe(unsToken);
    });

    // Get the dialog window width and height from properties file
    var bundle = new resBundle("GenericModalError");
    var width = bundle.getProperty("file.download.warning.dialog.width");
    var height = bundle.getProperty("file.download.warning.dialog.height");
    if(!width) {
      width = 500; // Default value
    }
    if(!height) {
      height = 225; // Default value
    }

    // Show an ok/cancel dialog with browser-specific security steps
    var browserName = curam.util._getBrowserName();
    curam.util.openGenericErrorModalDialogYesNo(
        "width="+width+",height="+height,
        "file.download.warning.title",
        "file.download.warning."+browserName);
  },

  /**
   * Gets the browser name. Returns a string, e.g. "ie8", "ie11", "firefox", etc.
   */
  _getBrowserName: function() {
    var tridentVersion = has("trident");
    var ffBrowserVersion = dojo.isFF;
    var chromeBrowserVersion = dojo.isChrome;
    var safariBrowserVersion = dojo.isSafari;
    var topmostWin = curam.util.getTopmostWindow();
    var isExternalApp = curam.util.ExpandableLists._isExternalApp(topmostWin);

    if (tridentVersion != undefined) {
      /*
       * We are assuming here that Microsoft will keep to the pattern of Trident
       * being 4 versions behind IE, i.e. IE8 = Trident 4, IE9 = Trident 5, etc.
       * If they change this pattern then we will need to modify the conditional
       * statements below.
       */
      var ieVersion = tridentVersion + 4;
      if(ieVersion < 8) {
        return "unknown.browser"; // IE7 and below are not supported
      } else {
        return "ie" + ieVersion;
      }
    } else if (ffBrowserVersion != undefined && isExternalApp) {
      return "firefox";
    } else if (chromeBrowserVersion != undefined) {
      return "chrome";
    } else if (safariBrowserVersion != undefined && isExternalApp) {
      return "safari";
    }
    return "unknown.browser"; // Unknown or unsupported browser
  },

 /**
  * Handles the onClick event for the list action menu items.
  *
  * @param url
  *          The URL of the required page.
  * @param sameDialog
  *          True if we re in a dialog and the page flow should stay
  *          in the same dialog.
  */
    clickHandlerForListActionMenu: function(url, sameDialog, newWindow, event) {
    // TODO: This handler was intended only for the list row actions menu but
    // is now being used for page level actions menus also, so re-name
    // appropriately. Also, *some* of the code in this method is duplicated
    // in the "clickHandler" method of UIMPageAdaptor.js. AS LONG AS THESE
    // METHODS ARE SEPARATE, ANY UPDATES TO THE LOGIC BELOW MUST BE ANALYZED
    // TO SEE IF THEY NEED TO BE APPLIED IN THE "UIMPageAdaptor" CLASS.
    if (sameDialog) {
      var href = curam.util.replaceUrlParam(url, "o3frame", "modal");
        var ctx = dojo.global.jsScreenContext;
      ctx.addContextBits('MODAL');
      href = curam.util.replaceUrlParam(href, "o3ctx", ctx.getValue());
      curam.util.redirectWindow(href);
      return;
    }

    // create a dummy anchor object
    var anchor = { href: url };

    require(["curam/ui/UIMPageAdaptor"]);
    if (curam.ui.UIMPageAdaptor.allowLinkToContinue(anchor)) {
      // In the case of a list action menu the click event is no longer coming
      // from an anchor element, instead it's a Diji MenuItem. Explicitly
      // setting window.location fakes the same behaviour as clicking the anchor
      // element. This will handle the sceanrios tested by allowLinkToContinue,
      // which are file downloads and the mailto link.
      // TODO: Can this method just return and let the event continue without
      // setting window.location....??? Seems to be other event handers
      // supressing this, so going with this approach for now.
      if (anchor.href.indexOf("/servlet/FileDownload")) {
        // When downloading a file, setting dojo.global.location will trigger the
        // addOnUnload event for each iframe with a addOnUnload attached. In this  
        // file downloading scenario we do not want these addOnUnload events to clear 
        // the contents of these iframes.
        sessionStorage.setItem("addOnUnloadTriggeredByFileDownload", "true");
        dojo.global.location = url;
        sessionStorage.removeItem("addOnUnloadTriggeredByFileDownload");
      } else {
        dojo.global.location = url; 
      }
      return;
    }

    // if we have an anchor, stop the click event and perform standard tab
    // processing (i.e. should it open in a new tab etc.)
    if (anchor != null) {
        if (event) {
          dojoEvent.fix(event);
          dojoEvent.stop(event);
      }

      if (!anchor.href || anchor.href.length == 0) {
        // the event has been stopped, just return if it has no href.
        // any onclick handlers attached to the link will have executed by now.
        return;
      }
      if (newWindow && !curam.util.isInternal(url)) {
          dojo.global.open(url);

      } else if (curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(anchor)) {
        var uimPageRequest = new curam.ui.PageRequest(anchor.href);
        if (dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")
          || dojo.global.jsScreenContext.hasContextBits("NESTED_UIM")) {
          uimPageRequest.pageHolder = dojo.global;
        }
        require(["curam/tab"], function() {
          var tabController = curam.tab.getTabController();
          if(tabController) {
            tabController.handlePageRequest(uimPageRequest);
          }
        });
      }
    }
    // Otherwise we let the event continue uninterrupted.
  },

  /**
     * Gets browser to action a mailto: link in a separate iframe.
     * This is to avoid issues with page unloading when mailto link is clicked
     * on some browsers.
     *
     * @param event The onclick DOM event.
     * @param url The URL of the link, including the mailto: protocol.
     */
    clickHandlerForMailtoLinks: function(event, url) {
      // stop anchor click from propagating and changing page
      dojo.stopEvent(event);

      // is there existing iframe to reuse?
      var iframe = dojo.query("#mailto_frame")[0];
      if (!iframe) {
        // no frame to reuse - create a new one
        iframe = dojo.io.iframe.create("mailto_frame", "");
      }

      // get browser to action the mailto: link in separate frame
      iframe.src = url;

      // return false as per convention for DOM click handlers
      return false;
    },

    /**
  * Decides whether or not the URL is internal.
  *
  * @param {String} url
  *            URL to extract data from.
  *
  * @returns true if the URL is internal false it it is external.
  */
  isInternal: function(url) {
    var path = url.split("?")[0];
    // Occurrence of Page.do determines whether or not it is internal URL.
    // It is assumed that, the external URL's would not have Page.do as it
    // fixed internal action call for curam.
    // TODO : Determining the URL is internal based on Page.do is a
    // brittle solution.Implement the improved solution.
    var occurrence = path.match("Page.do");
    if (occurrence!= null) {
      return true;
    }
    return false;
  },

  /**
  * Takes a URL and extracts its last path segment with the query string.
  *
  * @param {String} url
  *            URL to extract data from.
  *
  * @returns The last path segment of the URL with the query string,
  *      if it is part of the URL.
  */
  getLastPathSegmentWithQueryString: function(url) {
    var pathAndParams = url.split("?");
    var pathComponents = pathAndParams[0].split("/");
    return pathComponents[pathComponents.length - 1]
    + (pathAndParams[1] ? "?" + pathAndParams[1] : "");
  },

  /**
  * Replaces standard submit buttons with anchor tags when no images are used.
  * @param {String} buttonText
  *            The text to be displayed on submit button.
  */
  replaceSubmitButton: function(name, buttonText, isCustomSecondaryButton, newbuttonid, hasOnClickFunction) {
    if(curam.replacedButtons[name] == "true") {
      return;
    }

    /*
    * In the agenda player search for input elements where the name attribute
    * ends with SUM. This is required because an extra string of SUM is
    * appended onto the end of the name attribute value in the agenda player.
    * For all other submit buttons in the application we'll search for the
    * input fields the normal way to ensure we're not breaking any older
    * functionality.
    */
    var buttonId = '__o3btn.' + name;
    var inputList;
      if (dojo.global.jsScreenContext.hasContextBits('AGENDA')) {
        inputList = query("input[id='" + buttonId + "']");

    } else {
        inputList = query("input[name='" + buttonId + "']");
    }

    /*
    * You can supply 3 parameters to the forEach method of the dojo node list.
    * The current node, the index, and the node list itself.
    */
    inputList.forEach(function(replacedButton, index, theButtons) {
      // if there is a paramter passed in for button text then set the 'value'
      // of the second button (the button dipalyed to user) node to the button
      // text specified.
      // Note: This will replace any value set in the value attribute already!
      if (buttonText) {
        var buttonDisplayed = theButtons[1];
        buttonDisplayed.setAttribute("value",buttonText);
      }
      replacedButton.tabIndex = -1;
      var parentSpan = replacedButton.parentNode;
      var newLink;
      
      var isInternalModalFooter = curam.util.isInternalModal() && curam.util.isModalFooter(replacedButton);

      var buttonId = "btn-id-" + index;
      
      var cssClass = 'ac initially-hidden-widget ' + buttonId;
      if(domClass.contains(replacedButton, "first-action-control")) {
        cssClass += ' first-action-control';
      }
      
      if (isInternalModalFooter) {
    	 // for internal modal render the button using carbon
      	var submitButton = (isCustomSecondaryButton && !hasOnClickFunction) ?  undefined : theButtons[0];

    	// From a handcrafted JSP set the href to blank which will just close the dialog!
    	var buttonJson = isCustomSecondaryButton ?  {"href":"", "buttonid": newbuttonid} : {"buttonid": newbuttonid};
    	var rawDataTestID = replacedButton.getAttribute('data-rawtestid');
    	if (rawDataTestID) {
    	  buttonJson.dataTestId = rawDataTestID;
    	}
    	var isDefaulSubmitButton = domClass.contains(replacedButton, "curam-default-action") ? true : false;
      	curam.util.addCarbonModalButton(buttonJson, replacedButton.value, submitButton, isDefaulSubmitButton);
      } else {
		curam.util.setupWidgetLoadMask("a." + buttonId);
		var cssClass = 'ac initially-hidden-widget ' + buttonId;
	    if(domClass.contains(replacedButton, "first-action-control")) {
	      cssClass += ' first-action-control';
	    }
	
       var newLink = domConstruct.create("a", {
         "class": cssClass,
         href: "#"
       }, replacedButton, "before");

       var pageLevelMenu = dojo.query(".page-level-menu")[0];
       if(pageLevelMenu) {
         attr.set(newLink,"title",replacedButton.value);
       }

       /*
        * Adding filler span to create spacing between buttons. This is needed
        * because if spacing is added to anchor element using CSS, the spacing is
        * still clickable in IE.
        */
        domConstruct.create("span", {
          "class": "filler"
        }, newLink, "before");

        // Adding span elements inside anchor tag to display rounded corners.
        var left = domConstruct.create("span", {
          "class": "left-corner"
        }, newLink);

        var right = domConstruct.create("span", {
          "class": "right-corner"
        }, left);

        // Changed from using inner HTML.
        var middle = domConstruct.create("span", {
        "class": "middle"}, right );

        middle.appendChild(document.createTextNode(replacedButton.value));

        curam.util.addActionControlClass(newLink);
      }
	     

      if (newLink) {
	    on(newLink, "click", function(event) {
	      curam.util.clickButton(this._submitButton);
          dojoEvent.stop(event);
        });
	 
	    /*
        * Record the submit button on the link. This is for modal dialogs, so it
        * can distinguish a submit anchor tag from a normal anchor tag.
        *
        * NB: We have a temporary situation where we have 3 page-level-action
        * sets. 2 actions sets are the existing top and bottom ones, but they are
        * currently hidden. The third is the new button bar at the bottom of the
        * page. This is outside of the HTML form which means the "_submitButton"
        * variable was being set to a button that didn't have an associated form.
        * This caused errors in our modal dialog code. The temporary solution is
            * to set all _submitButtons to the first button found (theButtons[0])
            * which is guaranteed to be inside the form. The real solution
            * is to re-do the new button bar work so it is within the form.
        */
        newLink._submitButton = theButtons[0];
      }
	         

      domClass.add(replacedButton, 'hidden-button');
      attr.set(replacedButton, "aria-hidden", "true");
    
      /* 
       * TODO: ID of the button will be duplicated on the page, as the same 
       * ACTION_CONTROL can appear multiple times on the page. This ID is
       * connected to the ACTION_IDENTIFIER attribute, and causes knock-on
       * effects on buttons in various contexts.
       */
       attr.set(replacedButton, "id", replacedButton.id + "_" + index);
	         
	});

	curam.replacedButtons[name] = "true";
  },
	  
  /**
   * Indicates that the modal is in the internal application.
   */
  isInternalModal: function() {
    return !dojo.global.jsScreenContext.hasContextBits('EXTAPP') && dojo.global.jsScreenContext.hasContextBits('MODAL');
  },
  
  /**
   * Indicates that the modal is in the internal application and that the node is at the higest level in the modal
   * i.e not a hidden input on a form or wtihin a cluster on a modal.
   */
  isModalFooter: function(targetNode) {
	  if (targetNode) {
		var grandParentNode = targetNode.parentNode.parentNode;
		return grandParentNode && grandParentNode.id == "actions-panel";
	  }	
  },
  
  /**
   * Adds a button to modal footer.
   */
  addCarbonModalButton: function(buttonJson, buttonText, submitButton, isDefaultSubmitButton) {
    curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/addModalButton", [buttonJson, buttonText, submitButton, isDefaultSubmitButton, window]);
  },
	  

  /**
  * Adds a listener that will display the widget specified by query.
  *
  *  @param query
  *      Query of the widget to setup load mask for.
  */
  setupWidgetLoadMask: function(queryString) {
    curam.util.subscribe('/curam/page/loaded', function() {
        var widget = query(queryString)[0];
      if (widget) {
          style.set(widget, 'visibility', 'visible');

      } else {
        curam.debug.log("setupButtonLoadMask: "
          + debug.getProperty("curam.util.not.found") + "'" + queryString
         + "'" + debug.getProperty("curam.util.ignore.mask"));
      }
    });
  },

  /**
  * Optionaly replaces standard submit buttons within agenda player.
  */
  optReplaceSubmitButton: function(name) {
    if (curam.util.getFrameRoot(dojo.global,"wizard") == null) {
      curam.util.replaceSubmitButton(name);
      return;
    }
    var navigator = curam.util.getFrameRoot(dojo.global,"wizard").navframe.wizardNavigator;
    if (navigator.delegatesSubmit[jsPageID] != 'assumed') {
      curam.util.replaceSubmitButton(name);
    }
  },

  /**
  * Clicks a HTML button.
  *
  * @param buttonObj the button object or the id of the button object.
  */
  clickButton: function(buttonObj) {
      var theForm = dom.byId("mainForm");
    var hiddenButton;

    // if we have no argument, then return
    if (!buttonObj) {
      curam.debug.log("curam.util.clickButton: "
        + debug.getProperty("curam.util..no.arg"));
      return;
    }

    // if the argument is a string, then we assume it is the id of the
    // input element and search for it.
    if (typeof(buttonObj) == "string") {
      var buttonObjID = buttonObj;
      curam.debug.log("curam.util.clickButton: "
        + debug.getProperty("curam.util.searching")
        + debug.getProperty("curam.util.id.of") + "'" + buttonObjID + "'.");
        buttonObj = query("input[id='"  + buttonObjID + "']")[0];
        
        if (!buttonObj) {
          buttonObj = query("input[name='"  + buttonObjID + "']")[0];
        }
        

      // if we still have not found the input element, the return
      if (!buttonObj.form && !buttonObj.id) {
        curam.debug.log("curam.util.clickButton: "
            + debug.getProperty("curam.util.searched")
            + debug.getProperty("curam.util.id.of") + "'" + buttonObjID
            + debug.getProperty("curam.util.exiting"));
        return;
      }
    }

    // In the agenda player search for input elements where the name attribute
    // ends with SUM. This is required because an extra string of SUM is
    // appended onto the end of the name attribute value in the agenda player.
    // For all other submit buttons in the application we'll search for the
    // hidden buttons the normal way to ensure we're not breaking any older
    // functionality.
      if (dojo.global.jsScreenContext.hasContextBits('AGENDA')) {
      hiddenButton = buttonObj;

      } else {
        hiddenButton = query("input[id='" + buttonObj.id + "']", theForm)[0];
        if (!hiddenButton) {
          hiddenButton = query("input[name='" + buttonObj.name + "']", theForm)[0];
        }
    }
    try {
      // if the page is being submitted to self, publish event to enable
      // code to be run before refresh - used for example for saving list state
      // data to be restored on the page reload
        if (attr.get(theForm, "action").indexOf(jsPageID) == 0) {
        curam.util.publishRefreshEvent();
      }
      hiddenButton.click();

    } catch(e) {
      curam.debug.log(debug.getProperty("curam.util.exception.clicking"));
    }
  },

  /**
   * Handles click event on the page level print button.
   * Invokes the windows print function to print the context panel
   * and main content area.
   *
   * @param event the click event object.
   *
   */
  printPage:function(printContextPanelEnabled, event) {
    dojoEvent.stop(event);
	    
    var mainAreaWindow = dojo.window.get(event.currentTarget.ownerDocument);

    // Only printing the main content area when context
    // panel printing is disabled.
    if (printContextPanelEnabled === false) {
      curam.util._printMainAreaWindow(mainAreaWindow);    
      return false;
    }
    
    var mainAreaIframeNode = mainAreaWindow.frameElement;
    var searchNode = mainAreaIframeNode;

    // find the tab conent holder div which holds each tab.
    while(searchNode && !domClass.contains(searchNode, "tab-content-holder")) {
      searchNode = searchNode.parentNode;
    }

    var tabContentHolderNode = searchNode;

    var contextPanel = dojo.query(".detailsPanelFrame", tabContentHolderNode)[0];
    var hasContextPanel = contextPanel != undefined && contextPanel != null;

    if (hasContextPanel) {
      
      var isIE = has('trident') || has('ie');
      var isEdge = has('edge');
      
      // If a context panel is collapsed in IE11, it will not print due to display: none styling.
      // Instead, the main content area is printed twice. The below code addresses that issue.
      var contextPanelCollapsed = domClass.contains(contextPanel.parentNode, "collapsed");
      
      if (isIE && contextPanelCollapsed) {
        style.set(contextPanel.parentNode, "display", "block");
      }
      contextPanel.contentWindow.focus();
      contextPanel.contentWindow.print();
      
      if (isIE && contextPanelCollapsed) {
        style.set(contextPanel.parentNode, "display", "");
      }
      
      if (isIE || isEdge) {  
        // Give the print dialog time to display
	    setTimeout(function() {
	      if (isEdge) {
	        function printMainAreaWindow() {
	          curam.util._printMainAreaWindow(mainAreaWindow);
	          curam.util.getTopmostWindow().document.body.removeEventListener("mouseover", printMainAreaWindow, true);
	          return false;
	        }
	        // Edge will not trigger the onmouseover event on the browsers top most window until the
	        // context panel print dialog is closed.
	    	curam.util.getTopmostWindow().document.body.addEventListener("mouseover", printMainAreaWindow, true);
	      } else if (isIE) {
	        // Internet Explorer will pause timeout countdown when context panel print dialog is displayed, 
	        // once this dialog is closed the timer will continue and we can print the main content area.  
	        curam.util._printMainAreaWindow(mainAreaWindow);
	        return false;
	      }
	    }, 2000);
      } else {
    	  
        curam.util._printMainAreaWindow(mainAreaWindow);    
        return false;
      }
    } else {
      curam.util._printMainAreaWindow(mainAreaWindow);
      return false;
    }
  },
   
  /**
   * Prints the main Area window.
   */
  _printMainAreaWindow: function(mainAreaWindow){
    var hasDetailsRowExpanded = query(".list-details-row-toggle.expanded");
    if(hasDetailsRowExpanded.length > 0){
 	  curam.util._prepareContentPrint(mainAreaWindow);
 	  mainAreaWindow.focus();
      mainAreaWindow.print();
 	  curam.util._deletePrintVersion();
 	}else{
 	  mainAreaWindow.focus();
      mainAreaWindow.print();
    } 
  },
   
  /**
   * Prepare content to copy the body of iframe inside a wrapper and
   * disable the iframe when the user is printing the page.
   * This method reduce the problem of prints iframes nesting commom 
   * in scenario that involves the expandable list. 
   * In this version,the expandable list that contains context panel is excluded
   */
  _prepareContentPrint: function (baseElement){
    var iframes = Array.prototype.slice.call(baseElement.document.querySelectorAll('body iframe'));
    iframes.forEach(function(iframe) {
	  curam.util._prepareContentPrint(iframe.contentWindow);
	  var list = iframe.contentWindow.document.querySelectorAll('.title-exists');
	  var contextPanels = iframe.contentWindow.document.querySelectorAll('.title-exists div.context-panel-wrapper');		  
      if(list.length > 0 && contextPanels.length === 0){		  
	    var wrapper = document.createElement("div");
	    wrapper.setAttribute("class", "tempContentPanelFrameWrapper");
	    wrapper.innerHTML = list[0].innerHTML;
	    var parentOfIframe = iframe.parentNode;
	    parentOfIframe.parentNode.appendChild(wrapper);
	    parentOfIframe.style.display = 'none';			  
	    
	    curam.util.wrappersMap.push({tempDivWithIframeContent : wrapper, iframeParentElement: parentOfIframe}); 		    
	  } 		  
    });	  
  },
	  
  /**
   * Return the page to the original state. Removes the wrappers
   * and enables the iframes again after the user prints the page.
   */
  _deletePrintVersion: function() {
    if(curam.util.wrappersMap){
 	  curam.util.wrappersMap.forEach(function(wrappers){
 	    wrappers.tempDivWithIframeContent.parentNode.removeChild(wrappers.tempDivWithIframeContent);
 	    wrappers.iframeParentElement.style.display = 'block';
 	  });
 	
 	  curam.util.wrappersMap = [];
    }	
  },
   
  /**
  * Handles onmousedown event on the page toolbar buttons.
  * Adds a class name of selected to allow for CSS selected effect.
  *
  * @param event the click event object.
  *
  */
  addSelectedClass:function(event) {
      domClass.add(event.target,"selected");
  },

  /**
  * Handles onmouseup event on the page toolbar buttons.
  * Removes class name of selected..
  *
  * @param event the click event object.
  *
  */
  removeSelectedClass:function(event) {
      domClass.remove(event.target,"selected");
  },

  /**
  * Opens up the page level help page in new window.
  *
  * @param event the mouse click event.
  * @param event the help tag.
  *
  */
  openHelpPage: function(event, helpUrl){
      dojoEvent.stop(event);
    //opens up the constructed URL in new window.
      dojo.global.open(helpUrl);
  },

  /**
  * Connects the handler to the specified event on the specified object
  * and ensures it is disconnected when the page is unloaded.
  *
  * @param object
  *    The object to connect to.
  * @param eventName
  *    Name of the event to connect to.
  * @param handler
  *    The handler for the event.
  *
  * @return The disconnect token to be used with curam.util.disconnect()
  *    function.
  */
  connect: function(object, eventName, handler) {
    // wrap in another function to allow fixing event before passsing
    // to the handler
    var h = function(event) {
        handler(dojoEvent.fix(event));
    };

      if (has("ie") && has("ie") < 9) {
      object.attachEvent(eventName, h);

        unload.addOnWindowUnload(function() {
        object.detachEvent(eventName, h);
      });

      return { object: object, eventName: eventName, handler: h };

    } else {
        // The dojo/on API takes event names without the "on" prefix.
        var eventNameWithoutOn = eventName;
        if (eventName.indexOf("on") == 0) {
          eventNameWithoutOn = eventName.slice(2);
        }
        var dt = on(object, eventNameWithoutOn, h);

        unload.addOnWindowUnload(function() {
          dt.remove();
      });

      return dt;
    }
  },

  disconnect: function(token) {
      if (has("ie") && has("ie") < 9) {
      token.object.detachEvent(token.eventName, token.handler);
      } else {
        token.remove();
    }
  },

  /**
  * Subscribes the handler to the specified topic in the current runtime
  * context and ensures it is unsubscribed when the page is unloaded.
  *
  * @param topicName
  *    Name of the topic to subscribe to.
  * @param handler
  *    The handler for the topic.
  */
  subscribe: function(topicName, handler) {
      var st = topic.subscribe(topicName, handler);
      unload.addOnWindowUnload(function() {
        st.remove();
    });

    return st;
  },

  unsubscribe: function(token) {
      token.remove();
  },

  /**
  * Retrieves all action controls in action set.
  *
  * @param panelId
  *   ID of action set element.
  */
  addActionControlClickListener:function(panelId){
      var actionsPanel = dom.byId(panelId);
      var actionControlList = query(".ac", actionsPanel);
    if (actionControlList.length > 0) {
      for(var i = 0; i < actionControlList.length; i++) {
        var acNode = actionControlList[i];
        curam.util.addActionControlClass(acNode);
      }
    }
    this._addAccessibilityMarkupInAddressClustersWhenContextIsMissing();
  },

  /**
  * Check on each address cluster on a page for the presence of some context given whether
  * by a title or by the presence of a summary attribute in the uim page for the cluster.
  * If no given context exists on the HTML, one will be added automatically from the properties file.
  */
  _addAccessibilityMarkupInAddressClustersWhenContextIsMissing: function() {
    var clusterContentnDivWrapper = query(".bx--accordion__content");

    clusterContentnDivWrapper.forEach(function(cluster) {
      var addressCluster = query(".bx--address", cluster)[0];
      if (typeof(addressCluster) != 'undefined') {
        var bundle = new resBundle("util");
        var divAddressWrapper = addressCluster.parentElement.parentElement.parentElement;
        var divAddressMainWrapper = divAddressWrapper.parentElement.parentElement;

        // To keep it consistent with the behavior in other clusters, context should also be added automatically when
        // there is only a description present <p> on the cluster.
        var hasAddressHeader = query('h4, h3',divAddressWrapper).length == 1 ? true: false;

        var hasAlreadyAriaLabelValuePickedUpfromUIM = attr.get(divAddressMainWrapper, 'aria-label') !== null ? true: false;
        if (!hasAddressHeader && !hasAlreadyAriaLabelValuePickedUpfromUIM) {
          attr.set(divAddressMainWrapper, 'role', "group");
          attr.set(divAddressMainWrapper, 'aria-label', bundle.getProperty("curam.address.header"));
        }
      }
    });
  },

  /**
  * Adds a CSS class names to modal buttons that have been clicked.
  * This allows specific styling to be applied when buttons in a selected
  * state.
  *
  * @param acNode
  *    Modal action button node
  */
  addActionControlClass:function(acNode){
    curam.util.connect(acNode, "onmousedown",function(){
        domClass.add(acNode, "selected-button");
      curam.util.connect(acNode, "onmouseout",function(){
          domClass.remove(acNode, "selected-button");
      });
    });
  },

  /**
  * Gets all cluster level action sets contained in the content panel.
  *
  */
  getClusterActionSet:function(){
      var contentNode = dom.byId("content");
      var clusterActionSets = query(".blue-action-set", contentNode);
    if (clusterActionSets.length > 0){
      for (var i=0; i<clusterActionSets.length; i++) {
        curam.util.addActionControlClickListener(clusterActionSets[i]);
      }
    }
  },

  /**
  * Adjust Button spacing, if needed, to prevent them overlapping
  */
  adjustActionButtonWidth:function() {
      if (has("ie") == 8) {
        ready(function() {
          if (dojo.global.jsScreenContext.hasContextBits('MODAL')) {
            query(".action-set > a").forEach(function(node) {
            if(node.childNodes[0].offsetWidth > node.offsetWidth) {
                style.set(node, "width", node.childNodes[0].offsetWidth + "px");
                style.set(node, "display", "block");
                style.set(node, "display", "inline-block");
            }
          });
        }
      });
    }
  },

  /**
  * Sets the '__o3rpu' parameter for the URL. This parameter is an enocoded
  * value and holds the requesting URL and its parameters. This function may add
  * additonal parameters to the requesting URL prior to encoding it.
  *
  * @param {String} url
  *     The requested URL
  * @param {curam.util.RuntimeContext} rtc
  *     RunTimeContext Object - provides access to Window object.
  * @param {Array} [extraParaArray]
  *     An optional array of extra parametes to be added to the requesting URL.
  *     The expected array structure is
  *     [{key:"x1",value:"y1"},{key:"x2",value:"y2"},...]
  * @return The requested URL with the '__o3rpu' parameter appended.
  */
  setRpu:function(url, rtc, /*optional*/ extraParaArray){
    //Throw exception if null/blank values passed in
    if(!url||!rtc||!rtc.getHref()){
      throw {
        name:"Unexpected values",
        message:"This value not allowed for url or rtc"
      };
    }

    var o3rpuValue = curam.util.getLastPathSegmentWithQueryString(rtc.getHref());

    // strip the __o3rpu parameter from the RPU value
    o3rpuValue = curam.util.removeUrlParam(o3rpuValue, curam.util.Constants.RETURN_PAGE_PARAM);

    //Check for extra parameters passed in as {key:"",value:""} format
    if(extraParaArray){
      var i;
      for(i = 0; i < extraParaArray.length; i++) {
        if(!extraParaArray[i].key||!extraParaArray[i].value){
          throw {
            name:"undefined value error",
            message:"The object did not contain a valid key/value pair"
          };
        }
        o3rpuValue = curam.util.replaceUrlParam(o3rpuValue,
        extraParaArray[i].key, extraParaArray[i].value);
      }
    }
    var returnUrl = curam.util.replaceUrlParam(url, curam.util.Constants.RETURN_PAGE_PARAM, encodeURIComponent(o3rpuValue));
    curam.debug.log("curam.util.setRpu "
      + debug.getProperty("curam.util.added.rpu") + returnUrl);
    return returnUrl;
    },

    /**
     * Retrieves the base URL from the location.href property associated with
     * the current DOM Window object.
     * <p>
     * The base URL is the contents of the URL up as far, and including, the
     * application name.
     * <p>
     * For example, if the value of location.href was
     * "http://philippa:9080/TabTest/AppController.do" then the value returned
     * would be "http://philippa:9080/TabTest"
     *
     * @return the base URL or null if the URL cannot be retrieved.
     *
     */
    retrieveBaseURL: function() {
      //The regular expression will match a path of the form
      //[protocol]://[domain]/[name]. Where [protocol] can be any character
      //e.g. http. Where [domain] can be any character except for
      //the forward slash character e.g. www.curamsoftware.com. Where [name] can
      //be any character except for the forward slash character e.g. Curam.
      return dojo.global.location.href.match(".*://[^/]*/[^/]*");
    },

    removeRoleRegion:function(){
      var body = dojo.query("body")[0];
      attr.remove(body, "role");
    },

    /*
     * Function for iframe title fall back.
     * If PAGE_TITLE isn't specified in the UIM, this function takes care
     * of falling back to the navigation tab title or the application tab.
     */
    iframeTitleFallBack: function(){
      var currentiframe = curam.tab.getContainerTab(curam.tab.getContentPanelIframe());

      var iframe = dom.byId(curam.tab.getContentPanelIframe());
      var pageTitle = iframe.contentWindow.document.title;

      var currentAppTab = dojo.query("div.nowrapTabStrip.dijitTabContainerTop-tabs > div.dijitTabChecked.dijitChecked")[0];
      var currentAppTabName = dojo.query("span.tabLabel", currentAppTab)[0];

      var currentNavTab = dojo.query("div.nowrapTabStrip.dijitTabNoLayout > div.dijitTabChecked.dijitChecked", currentiframe.domNode)[0];
      var currentNavTabName = dojo.query("span.tabLabel", currentNavTab)[0];

      if(pageTitle=="undefined"){
          return this.getPageTitleOnContentPanel();
      } else if(pageTitle && pageTitle!=''){
        return pageTitle;
      } else if (currentNavTab){
          return currentNavTabName.innerHTML;
      } else {
          return currentAppTabName.innerHTML;
      }
    },
    
    /**
     * Function to return the page title from the content panel. 
     * This is for the edge case, where there is in-page-navigation
     * on a UIM page, and the current page title needs to be updated
     * after the content has been put into DOM.
     *
     * @return page title on content panel.
     */
    getPageTitleOnContentPanel: function() {
        
        // Page title on content panel
        var contentPanelPageTitle;
        var iframe = dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
        var iframeDoc;
        if(iframe && iframe.length == 1) {
          iframeDoc = iframe[0].contentWindow.document;
          windowBase.withDoc(iframeDoc, function() {
            var header = dojo.query("div.title h2 span:not(.hidden)");
            if(header && header.length == 1 && header[0].textContent) {
                contentPanelPageTitle = lang.trim(header[0].textContent);
            } 
          }, this);
        } 
        
        if(contentPanelPageTitle) {
         return contentPanelPageTitle;
        }else {
          return undefined;
        }
    },

    /**
     * Function to add a specific class to the last visible node (cluster, list
     * or action set) in the page content area. It only works specifically in
     * the external application.
     *
     * @return There's no return value of this function.
     */
    addClassToLastNodeInContentArea: function() {
      var divNodes = query("> div", "content");
      var divNodesCount = divNodes.length;

      if (divNodesCount == 0) {
        return "No need to add";
      }

      var lastNode = divNodes[--divNodesCount];

      while (domClass.contains(lastNode, "hidden-action-set") && lastNode) {
        lastNode = divNodes[--divNodesCount];
      }

      domClass.add(lastNode, "last-node");

    },

    /*
     * Function to check Highcontrast mode
     * If High contrast mode is turned on the method checks the class name
     * high-contrast on body element.
     */
    highContrastModeType: function(){
      var highContrastMode = dojo.query("body.high-contrast")[0];
      return highContrastMode;
    },

    /**
     * Function to check RTL (right-to-left) mode.
     * Checks for class name 'rtl' on body element to see if RTL mode is turned
     * on.
     */
    isRtlMode: function() {
      var rtlMode = dojo.query("body.rtl")[0];
      return rtlMode;
    },

    processBidiContextual: function (target){
      target.dir = bidi.prototype._checkContextual(target.value);
    },

    getCookie: function(name) {
      var dc=document.cookie;
      var prefix=name+"=";
      var begin=dc.indexOf("; "+prefix);
      if(begin==-1) {
        begin=dc.indexOf(prefix);
        if(begin!=0)
          return null;
        } else {
          begin+=2;
        }
        var end=document.cookie.indexOf(";",begin);
        if(end==-1) {
          end=dc.length;
        }
      return unescape(dc.substring(begin+prefix.length,end));
    },

    getHeadingTitleForScreenReader: function(landmarkTitle) {
      var topWin = curam.util.getTopmostWindow();
      var tabTitle = topWin.dojo.global._tabTitle;

      if (tabTitle) {
        curam.util.getHeadingTitle(tabTitle, landmarkTitle);
      } else {
        var tabTitleSubscription = topWin.dojo.subscribe("/curam/_tabTitle", function(tabTitleFromPublish) {
                  if(tabTitleFromPublish) {
            curam.util.getHeadingTitle(tabTitleFromPublish, landmarkTitle);
                  }
          topWin.dojo.unsubscribe(tabTitleSubscription);
        });
      }
    },

    getHeadingTitle: function(tabTitle, landmarkTitle) {
      var headingTitle = undefined;

      if (tabTitle && tabTitle.length > 0) {
        headingTitle = tabTitle;
      } else {
        headingTitle = landmarkTitle;
      }

      var pageTitleBar = dojo.query(".page-title-bar");
      var mainContentPageHeading = dojo.query("div h2", pageTitleBar[0]);

      // h2 heading
      if (mainContentPageHeading) {
        var childrenSpan = dojo.query("span",mainContentPageHeading[0]);
        var span = undefined;
        if (childrenSpan) {
          span = childrenSpan[0];
        }
        if (!span || (span && (span.innerHTML.length == 0))) {
          if (span) {
            attr.set(span, "class", "hidden");
            attr.set(span, "title", headingTitle);
            span.innerHTML= headingTitle;
          } else {
            span = domConstruct.create("span", {"class" : "hidden", "title": headingTitle}, mainContentPageHeading[0]);
            span.innerHTML = headingTitle;
          }
        }
      }
    },

    /**
     * Sets up the browser tab title data for later use by the
     * curam.util.setBrowserTabTitle method.
     * 
     * @param {String} tabTitle
     *     the static tab title taken from CDEJResource.properties
     *     'browser.tab.title' property
     * @param {String} separator
     *     the separator to use between the application title and context
     *     strings
     * @param {Boolean} appNameFirst
     *     <code>true</code> if the application name should come before the
     *     context, <code>false</code> otherwise
     */
    _setupBrowserTabTitle: function(staticTabTitle, separator, appNameFirst) {
      // Convert newlines to spaces as newlines are allowed in banner title but
      // not in browser tab title
      staticTabTitle = staticTabTitle.replace("\\n", " ");

      curam.util._browserTabTitleData.staticTabTitle = staticTabTitle;
      curam.util._browserTabTitleData.separator = separator;
      curam.util._browserTabTitleData.appNameFirst = appNameFirst;
    },

    /**
     * Stores the browser tab title data.
     */
    _browserTabTitleData: {},

    /**
     * Sets the browser tab title to the given value plus the static title, or,
     * if no value is given, attempt to find an appropriate title from the page
     * layout.
     * <p>
     * Example: "Register Person - IBM Curam Social Program Management"
     * <p>
     * The value is found using the following logic:
     * <ol>
     * <li>Page Title on Content Panel
     * <li>If no page title on CPanel, then get title from page navigation group
     * <li>If no page navigation group title then use title from navigation tab bar
     * <li>If no page title on context panel then use title on application tab bar
     * <li>If no title on application tab then use title on Section
     * </ol>
     *
     * @param {String} [title]
     *         Optional title to use for the page. If a title value is not
     *         given, the function attempts to select an appropriate title from
     *         the information on the page.
     *
     * @return No return value.
     */
    setBrowserTabTitle: function(/*optional*/title) {
      curam.debug.log("curam.util.setBrowserTabTitle(title = " + title + ") called");
      // If no title given then try to find an appropriate title on the page
      if(!title) {
        title = curam.util._findAppropriateDynamicTitle();
      }

      var staticTitle = curam.util._browserTabTitleData.staticTabTitle;
      var separator = curam.util._browserTabTitleData.separator;
      var appNameFirst = curam.util._browserTabTitleData.appNameFirst;

      if (!staticTitle && !separator && !appNameFirst && !title) {
        /**
         * When setBrowserTabTitle is called from a nested page, the curam.util._browserTabTitleData 
         * may not contain staticTabTitle, separator and appNameFirst. In this particolar scenario, 
         * set document.title as the page title.
         */
        
        var headTitle = document.querySelectorAll("head title")[0];
        if (headTitle) {
          document.title = headTitle.text;
        }
    }else if (!title) {
      // If no title given or found, then just use the static title on its own
      document.title = staticTitle; 
      } else {
        if(staticTitle) {
          if(appNameFirst) {
            // Display "Title - Context"
            document.title = staticTitle + separator + title;
          } else {
            // Display "Context - Title"
            document.title = title + separator + staticTitle;
          }
        }
      }
    },
    
    /**
     * Function that handles the toggle event for the Checkboxed Multi Select Renderer
     * 
     * @param e - The checkbox element. 
     * @param div - The Select div (row).
     */
    toggleCheckboxedSelectStyle: function(e, div){ 
      if(e.checked){
	    div.classList.remove('unchecked');
		div.classList.add('checked');
      }else{
	    div.classList.remove('checked'); 
		div.classList.add('unchecked');
      }
    },

    /**
     * Searches the DOM for an approriate string to use as the browser tab
     * title. The value is selected using the following logic:
     * <ol>
     * <li>When a popup window closes, get title from the modal
     * from which it was opened
     * <li>Page Title on Content Panel
     * <li>If no page title on CPanel, then get title from page navigation group
     * <li>If no page navigation group title then use title from navigation tab bar
     * <li>If no page title on context panel then use title on application tab bar
     * <li>If no title on application tab then use title on Section
     * </ol>
     *
     * @return a string to use for the browser tab title chosen by the above
     *         logic, or <code>undefined</code> if nothing could be found.
     */
    _findAppropriateDynamicTitle: function() {
      var i; // Index used for various loops
      
      // 1. When closing a modal or popup widget, relay modal and popup widget titles to browser tab
      var one;
      var numberOfActiveModals = dojo.query("iframe.curam-active-modal").length;
      if (numberOfActiveModals > 1) {
    	// If numberOfActiveModals > 1 we are closing a modal dialog or popup widget and falling back to
    	// to a modal dialog or popup widget.
        var iFrameContainingTheTitleWeWant = dojo.query("iframe.curam-active-modal")[0];
        if (iFrameContainingTheTitleWeWant) {
	      var iFrameContainingTheTitleWeWantDoc = iFrameContainingTheTitleWeWant.contentDocument;
	      if (iFrameContainingTheTitleWeWantDoc) {
	        var iFrameContainingTheTitleWeWantDocHeadTitle = 
	          iFrameContainingTheTitleWeWantDoc.head.getElementsByTagName("title")[0];
	        if (iFrameContainingTheTitleWeWantDocHeadTitle) {
	          if (iFrameContainingTheTitleWeWantDocHeadTitle.innerHTML != '') {
	            one = iFrameContainingTheTitleWeWant.contentDocument.head.getElementsByTagName("title")[0].innerHTML;
	  		  }  
	        }  
	      }
        }
      }
      
      if(one) {
        return one;
      }

      // 2. Page title on content panel
      var two;
      var iframe = dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
      var iframeDoc;
      if(iframe && iframe.length == 1) {
        iframeDoc = iframe[0].contentWindow.document;
        windowBase.withDoc(iframeDoc, function() {
          var header = dojo.query("div.title h2 span:not(.hidden)");
          var errorMessages = dom.byId("error-messages", iframeDoc);
          if(errorMessages) {
            two = iframeDoc.head.getElementsByTagName('title')[0].textContent;
          } else if(header && header.length == 1 && header[0].textContent) {
            two = lang.trim(header[0].textContent);
            curam.debug.log("(2) Page title for Content Panel = " + two);
          } else if(header && header.length > 1) { // Special case for sub-titles
            two = this._checkForSubTitles(header);
          } else {
            curam.debug.log("(2) Could not find page title for content panel: header = " + header);
          }
        }, this);
      } else {
        curam.debug.log("(2) Could not find iframeDoc for content panel: iframe = " + iframe);
      }

      if(two) {
        return two;
      }

      // 3. Selected navigation item title
      var three;
      var navItem = dojo.query("div.dijitVisible div.dijitVisible div.dijitVisible div.child-nav-items li.selected > div.link");
      if(navItem && navItem.length == 1 && navItem[0].textContent) {
        three = lang.trim(navItem[0].textContent);
        curam.debug.log("(3) Selected navigation item = " + three);
      } else {
        curam.debug.log("(3) Could not find selected navigation item: navItem = " + navItem);
      }

      if(three) {
        return three;
      }

      // 4. Navigation bar tab title
      var four;
      var allNavTabs = dojo.query("div.dijitVisible div.dijitVisible div.navigation-bar-tabs span.tabLabel");
      var selectedNavTab;
      for(i = 0; i < allNavTabs.length; i++) {
        if(allNavTabs[i].getAttribute("aria-selected") === "true") {
          selectedNavTab = allNavTabs[i];
        }
      }
      if(selectedNavTab && selectedNavTab.textContent) {
    	four = lang.trim(selectedNavTab.textContent);
        curam.debug.log("(4) Selected navigation bar tab = " + four);
      } else {
        curam.debug.log("(4) Could not find selected navigation bar tab: selectedNavTab = " + selectedNavTab);
      }

      if(four) {
        return four;
      }

      // 5. Application tab title bar
      var five;
      var appTabTitleBar = dojo.query("div.dijitVisible div.dijitVisible h1.detailsTitleText");
      if(appTabTitleBar && appTabTitleBar.length == 1 && appTabTitleBar[0].textContent) {
    	five = lang.trim(appTabTitleBar[0].textContent);
        curam.debug.log("(5) Selected application tab title bar = " + five);
      } else {
        curam.debug.log("(5) Could not find selected application tab title bar: appTabTitleBar = " + appTabTitleBar);
      }

      if(five) {
        return five;
      }

      // 6. Section title
      var six;
      var sections = dojo.query("div.dijitTabInnerDiv div.dijitTabContent div span.tabLabel");
      var selectedSection;
      for(i = 0; i < sections.length; i++) {
        if(sections[i].getAttribute("aria-selected") === "true") {
          selectedSection = sections[i];
          break;
        }
      }
      if(selectedSection && selectedSection.textContent) {
    	six = lang.trim(selectedSection.textContent);
        curam.debug.log("(6) Selected section title = " + six);
      } else {
        curam.debug.log("(6) Could not find selected section title: sections = " + sections);
      }

      if(six) {
        return six;
      }

      // 7. UIM pages in external application (for when curam.ModalDialog._modalClosedHandler gets called in that context.)
      var seven;
      iframe = dom.byId('curamUAIframe');
      if(iframe && iframe.contentWindow && iframe.contentWindow.document) {
        iframeDoc = iframe.contentWindow.document;
        windowBase.withDoc(iframeDoc, function() {
          var uimPageTitle = dojo.query('div.page-header > div.page-title-bar > div.title > h2 > span');
          if(uimPageTitle && uimPageTitle.length == 1 && uimPageTitle[0].textContent) {
        	seven = lang.trim(uimPageTitle[0].textContent);
            curam.debug.log("(7) UIM page title for external application page = " + seven);
          } else {
            curam.debug.log("(7) Could not find UIM page title for external application page: uimPageTitle = " + uimPageTitle);
          }
        }, this);
      }

      if(seven) {
        return seven;
      }

      return undefined;
    },

    /**
     * Checks the given array of elements consists of a valid header and
     * sub-titles. This is for when the PAGE_TITLE element contains multiple
     * CONNECT elements.
     * 
     * The first header only needs to have a textContent child. The second and
     * subsequent headers need a textContent child and to have the class
     * 'sub-title'.
     * 
     * @param {Array} header The array of HTML elements returned by dojo.query.
     * @return a string to use for the browser tab title created from the given
     *         header and sub-titles, or <code>undefined</code> if the array
     *         did not meet the criteria described above.
     */
    _checkForSubTitles: function(header) {
      var i; // Index used for various loops

      // Check first item has a textContet attribute 
      if(!header[0].textContent) {
        return undefined;
      }
      // Check all attributes except the first one have a "sub-title" class and .textContent attribute
      for(i = 1; i < header.length; i++) {
        var clazz = header[i].getAttribute('class'); 
        if(clazz.indexOf('sub-title') === -1 || !header[i].textContent) {
          curam.debug.log("(1) Failed to construct title from content panel page title. Not all header element spans had 'sub-title' class.");
          for(i = 0; i < header.length; i++) {
            curam.debug.log(header[i]);
          }
          return undefined;
        }
      }
      // All elements are valid sub-titles, so concatenate textContent values
      var ret = header[0].textContent;
      for(i = 1; i < header.length; i++) {
        ret += header[i].textContent;
      }
      return ret;
    },

    /**
     * This function adds the necessary context to a widget for accessibility concerns.
     *
     * The closest cluster above the current widget holding a title of type <h3> is used to give context to
     * the widget. If the cluster just above the widget does not have a title it looks for
     * the a cluster above and so on until a title is found.
     * If not tile is found, no accessibility feature is applied.
     *
     * @param {String} widgetId The id of the widget to add context to.
     */
    _addContextToWidgetForScreenReader: function(widgetId) {
      var found = false;
      var index = 0;
      var trainingDetailsListClusterWrapper = dojo.query(".training-details-list");

      if (trainingDetailsListClusterWrapper.length == 1) {
        var trainingDetailsListClusterWrapperParentElement = trainingDetailsListClusterWrapper[0].parentElement;
        var clusterChildrenList = dojo.query("div.bx--cluster", trainingDetailsListClusterWrapperParentElement);
        var currentIndexOfTheClusterInTheListForTheWidget = Array.prototype.indexOf.call(clusterChildrenList, trainingDetailsListClusterWrapper[0]);

        if (currentIndexOfTheClusterInTheListForTheWidget >= 0) {
          //We want to find the closest cluster with a title - hence starting from the index of the widget cluster and checking on the clusters above.
          for (var i = currentIndexOfTheClusterInTheListForTheWidget; i >= 0; i--) {
            if (dojo.query("h3", clusterChildrenList[i]).length == 1) {
              found = true;
              index = i;
              break;
            }
          }
        }

        if (found) {
          var closestClusterWithATitle = dojo.query("h3.bx--accordion__title", clusterChildrenList[index]);
          if (closestClusterWithATitle.length == 1) {
            //Define an id for the closest cluster element containing the title to be used as context for the widget.
            var idForTheClosestClusterWithATitle = closestClusterWithATitle[0].className + "_id";
            attr.set(closestClusterWithATitle[0], "id", idForTheClosestClusterWithATitle);
            //Set the widget itself with the appropriate accessibility features.
            var widgetDivWrapper = dojo.byId(widgetId).parentElement;
            attr.set(widgetDivWrapper, "aria-labelledby", idForTheClosestClusterWithATitle);
            attr.set(widgetDivWrapper, "role", "region");
          }
        }
      }
    },

    /**
     * Set the focus on the parent active iframe.
     * @param {window} [child] If specified, the dialog hierarchy will be used
     * to look up the parent window.
     */
    setParentFocusByChild : function(childID){
      var win = curam.util.UimDialog._getDialogFrameWindow(childID);

      if(win){
         var parentWindow = curam.dialog.getParentWindow(win); 
         if(parentWindow){
            parentWindow.focus();
         }
      }   
    },
    /**
     * Copies the value to system clipboard if the access
     * is configured; passes the value through the Word Integration channel if called
     * as part of Appeals Clause functionality. 
     */
    toClipboard: function(valueToCopy){
      try {
        navigator.clipboard.writeText(valueToCopy);
      } catch (err) {
        console.warn("Failed to copy into the clipboard.");
      }
      if (dojo.getObject('curam.dialog', false) != null) {
        var pw = curam.dialog.getParentWindow(window);
        pw && pw.dojo.publish('/curam/clip/selected', [valueToCopy]);
      }
      return false;
    },
    /**
     * Set to zero the scrollTop in ios devices.
     */
    removeTopScrollForIos: function(){
    	if(has("ios")){
    		window.document.body.scrollTop=0;
    	}
    },
    
    /**
     * Insert aria-live attribute in the span that contains the record of items. 
     * The span content is updated with text containing the amount of records 
     * returned so screen reader reads it when the screen is loaded.
     */
	insertAriaLiveLabelRecordSearchItem: function (numSearchResultsText){
        var span = dojo.query("[data-search-page]")[0];
        if(span) {
          span.setAttribute("aria-live", has('ios') ? "polite" : "assertive");
          // When DOM has loaded, update text after a short delay to trigger
          // screen reader to announce new text.
          setTimeout(function() {
            var currentSearchResultText = span.firstChild.nodeValue;
            var numSearchResultsTextOutput = currentSearchResultText + numSearchResultsText;
            span.innerHTML = numSearchResultsTextOutput;
          }, 10);
        }
    },
    
    /**
     * Remove a session property from the session storage.
     */
    removeSessionStorageProperty: function (propertyName){
        sessionStorage.removeItem(propertyName);
    },
    
    /**
     * This method is invoked from method render() from DateTimeEditRenderer.java
     * when a date time widget field is rendered onto a page. A css class style of 
     * "date-time-exists" is added to the <tr> element containing this widget.
     */
    addLayoutStylingOnDateTimeWidgetOnZoom: function (){
      
      var dateTimeTables = dojo.query("table.input-cluster td.field table.date-time");
      console.log("datetimetable from util.js: "+dateTimeTables);
      var dateTimeTableCount = dateTimeTables.length;
      
      if(dateTimeTables.length >0){
        for (var i=0;i<dateTimeTables.length;i++) {
          
          var dateTimeTable = dateTimeTables[i];
          var tableRow = dateTimeTable.parentNode.parentNode;
          tableRow.setAttribute("class", "date-time-exists");
        }
      }
    },
    
    /**
     * This method is used in FileUploadTag.java to cause the file
     * browser to open and allow the uploading of files to C?ram.
     * We only want to open the file browser on a 'space' or 'enter'
     * key press.
     */
    fileUploadOpenFileBrowser: function(e, elementId) {
    	if (e.keyCode == 32 || e.keyCode == 13) {
    		dom.byId(elementId).click();
    	}
    },
    
    /**
     * Invoked upon each page load to optionally set up lists
     * on a page so that the list selection controls are
     * initialized and updated with the correct text for
     * the screen readers.
     * Relies on the presence of the JavaScript objects
     * output for lists by the tag infrastructure and
     * JSP generator.
     */
    setupControlledLists: function() {
      var crtlPath = 'curam.listControls',
         togglerPath = 'curam.listTogglers';
      var crtlsStorage = getFrom(crtlPath),
            togglers = getFrom(togglerPath),
              lists = [];
      var batchQuery = crtlsStorage && query('*[data-control]'),
          togglerQuery = togglers && query('a[data-toggler]');
      if (crtlsStorage || togglers) { //no lists = no update 
        for (var listId in crtlsStorage) {
          batchQuery.filter(function(item) {
            return attr.get(item, 'data-control') == listId;
          }).forEach(function(listCrtl, ix) {
            var c = dom.byId(listCrtl),
              tr = query(listCrtl).closest('tr')[0];
              !tr.controls && (tr.controls = new Array());
              tr.controls.push(c);
              // do not add the same row twice
              if (!tr.visited) {
                tr.visited = true;
            	crtlsStorage[listId].push(tr);
              }
          });
          var cBuildUp = getFrom(crtlPath + '.' + listId);
          if (cBuildUp && cBuildUp.length && cBuildUp.length > 0) {
            lists.push(listId);
          } else {
            putTo(crtlPath + '.' + listId, false);//blanking out
          }
    	}
        if (togglerQuery && togglerQuery.length > 0) {
          for (var listId in togglers) {
            togglerQuery.filter(function(item) {
              return attr.get(item, 'data-toggler') == listId;
            }).forEach(function(listToggler) {
              var tr = query(listToggler).closest('tr')[0];
              tr.hasToggler = listToggler;
              tr.visited = true;
              togglers[listId].push(tr);
            });
            var tBuildUp = getFrom(togglerPath + '.' + listId);
            if (tBuildUp && tBuildUp.length && tBuildUp.length > 0) {
              (lists.indexOf(listId) == -1) && lists.push(listId);
            } else {
               putTo(togglerPath + '.' + listId, false); //blanking out
            }
          }
    	}
        lists.forEach(function(listId){
          var updateArray = getFrom(crtlPath + '.' + listId)
                             || getFrom(togglerPath + '.' + listId);
          cu.updateListControlReadings(listId, updateArray);      	
        });
      }
      dojo.subscribe("curam/sort/earlyAware", function(listId) {
        cu.suppressPaginationUpdate = listId;
      });
      dojo.subscribe('curam/update/readings/sort', function(listId, rows) {
    	if (!has('trident')) {
    	  cu.updateListActionReadings(listId);
          cu.updateListControlReadings(listId, rows);
          cu.suppressPaginationUpdate = false;
        } else {
      	  var pageBreak = cu.getPageBreak(listId),
	          limit = Math.ceil(rows.length/pageBreak);
	      cu.listRangeUpdate(0, limit, listId, rows, pageBreak);
        }
      });
      dojo.subscribe('curam/update/readings/pagination', function(listId, newPageSize) {
        putTo('curam.pageBreak.' + listId, newPageSize); // set for the subsequent sorts
      });
      dojo.subscribe('curam/update/pagination/rows', function(newRows, listId) {
        // this will cancel the gradual update process in IE only
    	cu.updateDeferred && !cu.updateDeferred.isResolved()
    	                     && cu.updateDeferred.cancel('Superseeded');
    	if (cu.suppressPaginationUpdate && cu.suppressPaginationUpdate == listId) {
          return; //do not attempt multiple updates
        }
        var hasTogglers = isHere('curam.listTogglers.' + listId),
                hasControls = isHere('curam.listControls.' + listId),
                  lms = getFrom('curam.listMenus.' + listId),
                    hasMenus = lms && (lms.length > 0);
        var needsStaticUpdate = hasControls || hasTogglers;
        if (!needsStaticUpdate && !hasMenus) return;
        if (needsStaticUpdate) {
          var nvRows = newRows.filter(function(aRow) {
            return (!aRow.visited || !aRow.done)
               && attr.has(aRow, 'data-lix');
          });
          hasTogglers && nvRows.forEach(function(aRow){
            var tgl = query('a[data-toggler]', aRow)[0];
            aRow.hasToggler = tgl;
            aRow.visited = true;
            curam.listTogglers[listId].push(aRow);
          });
          hasControls && nvRows.forEach(function(aRow){
            var crtlRefs = query('*[data-control]', aRow),
                  storage = new Array();
            crtlRefs.forEach(function(cRef){
              storage.push(dom.byId(cRef));
            });
            aRow.controls = storage;
            curam.listControls[listId].push(aRow);
            aRow.visited = true;
          });
          var updateArray = hasControls
        	            ? curam.listControls[listId]
                        : curam.listTogglers[listId];
          cu.updateListControlReadings(listId, updateArray);
        }
        hasMenus && cu.updateListActionReadings(listId);
      });
    },
    /**
     * Updates big list accessibility readings in parts equal to the
     * current page size, asynchronously and with delay to unlock
     * a page with list for interaction as quickly as possible.
     * Applies to IE11 only since this ancient browser is not capable
     * of reasonable updating otherwise.
     *
     * @param rangeNum the number of list range to be updated in this batch. 
     * @param limit the rage number limit to stop the updater.
     * @param listId the identifier of the list updatye applies to.
     * @param rows the full array containing row objects to be updated.
     * @param psz the current size of the paginated list.
     */
    listRangeUpdate: function(rangeNum, limit, listId, rows, psz) {
      if (rangeNum == limit) {
        cu.suppressPaginationUpdate = false;
        cu.updateDeferred = null;
        return;
      }
      var def = cu.updateDeferred = new Deferred(function(reason) {
        cu.suppressPaginationUpdate = false;
        cu.updateDeferred = null;
      });
      def.then(function(pNum) { cu.listRangeUpdate(pNum, limit, listId, rows, psz) },
    		  function(err) {/* swallow silently,it is cancellation*/});
      // additional delay for the invisible updates.
      var delay = (rangeNum === 0) ? 0 : 200;
      setTimeout(function() {
        var newNum = rangeNum+1, range = [rangeNum*psz, (newNum*psz)];
        cu.updateListActionReadings(listId, range);        
        cu.updateListControlReadings(listId, rows, range);       
        def.resolve(newNum);
      }, delay);
    },

    /**
     * Updates the screen reader texts for the list selection controls (links,
     * radio buttons, row toggle controls) depending on the current row number.
     * Relies on the additional markup output for the rows and lists.
     * 
     * @param listId optional list identifier to apply the update method to.
     * @param rowArray the array of list row objects to traverse.
     * @param suppressFocus indicates that no focus manipulation is needed
     *                      upon update completion.
     * @param range an optional array of two numbers specifying the part of
     * the rowArray to be updated
     *                      
     *                      
     */
    updateListControlReadings: function(listId, rowArray, range) {
      var c0, psz = cu.getPageBreak(listId),
        startAt = cu.getStartShift(listId, rowArray[0] || false),
        portion = rowArray;
      range && (portion = rowArray.slice(range[0], range[1]));
      for (var rix in portion) {
        var aRow = portion[rix],
            lixAttr = parseInt(attr.get(aRow, ROW_IDX)),
              lx = (lixAttr % psz) + startAt,
                crtls = aRow.controls;
        if(!crtls){ //check for controls
        	var crtlRefs = query('*[data-control]', aRow),
            storage = new Array();
      		crtlRefs.forEach(function(cRef){
        		storage.push(dom.byId(cRef));
      		});
      		aRow.controls = storage;
      		crtls = aRow.controls;
        }
        
        if (crtls) { /* a row can contain multiple controls in an array */
      	  for (var cix in crtls) {
      	    var crtl = crtls[cix],
      	      /* links have textContent, single selects do not */
      	      ttl = crtl.textContent || false,
      	        linkPrefixed = ttl ? ttl + ',' : '';  
      	    if (crtl.nodeName == 'A') {
              var imageElement = query("img",crtl)[0];
              if (imageElement && domClass.contains(crtl,"ac first-action-control external-link")) {
                var linkName = attr.get(imageElement,"alt");
                attr.set(crtl, READOUT,
                  linkName + "," + [listcontrol.reading.anchors, lx].join(' '));
              } else {
                attr.set(crtl, READOUT,
                  linkPrefixed + [listcontrol.reading.anchors, lx].join(' '));
              }
        	} else {
        	  attr.set(crtl, READOUT,
        	    linkPrefixed + [listcontrol.reading.selectors, lx].join(' '));
            }
      	  }
        }
        cu.updateToggler(aRow, lx);
        aRow.done = true;
      }
    },
    /**
     * Initializes the list action menus which are subject to the accessibility
     * readings update. Each such menu upon startup registers itself
     * in the dedicated array for quick access when updating when sorting or
     * paginating a list.
     * 
     * @param listId the identifier of the list being updated.
     */
    initListActionReadings: function(listId) {
      var actionsPath = 'curam.listMenus.' + listId; 
      putTo(actionsPath,[]);
      dojo.subscribe('curam/listmenu/started', function(widget, listId){
    	   var tr = query(widget.containerNode).closest('tr')[0],
    	     lix = parseInt(attr.get(tr, ROW_IDX)),
    	       lx = (lix % cu.getPageBreak(listId)) + cu.getStartShift(listId, tr);
    	widget.set({'belongsTo' : tr,
    		        'aria-labelledBy':'',
    		        'aria-label' : [listcontrol.reading.menus, lx].join(' ')});
    	getFrom(actionsPath).push(widget);
    	cu.updateToggler(tr, lx);
      });
    },
    /**
     * Updates toggle control in an expandable list row.
     * This can be initiated either dynamically when a list action menu
     * lazily initializes, or upon regular update where no dynamic controls
     * are present in a list.
     * 
     * @scanRow the row to be checked for toggle control.
     * @relLix the number to use for the toggling control accessibility reading.
     */
    updateToggler: function(scanRow, relLix) {
      scanRow.hasToggler
        && attr.set(scanRow.hasToggler, READOUT,
              [listcontrol.reading.togglers, relLix].join(' '));
    },
    /**
     * Called to update the list actions menu accessibility readings.
     * Used the initialized menu widget array where each of the them
     * stores the associated row reference.
     * 
     * @param listId the identifier of the list being updated.
     * @param range optional array specifying the range of list menus
     * to be updated; if absent the full range is subject to update.
     */
    updateListActionReadings: function(listId, range) {
      var menus = getFrom('curam.listMenus.' + listId),
        psz = cu.getPageBreak(listId), startAt = false,
          portion = menus;
      range && (portion = menus.slice(range[0], range[1]));
      for (var ix in portion) {
        var widget = portion[ix], tr = widget.belongsTo,
            lix = parseInt(attr.get(tr, ROW_IDX)),
              startAt = startAt || cu.getStartShift(listId, tr),
                 finalLix = (lix % psz) + startAt;
        widget.set(READOUT,
         	   [listcontrol.reading.menus, finalLix].join(' '));
        cu.updateToggler(tr, finalLix);
        tr.done = true;
      }
    },
    /**
     * Returns the current page size of a paginated list.
     * 1000 is returned for not paginated lists to exceed
     * any potential list page size.
     * This is used for the accessibility readings update.
     * 
     * @param listId the identifier of the list which page size is requested.
     */
    getPageBreak: function(listId) {
      if (!isHere('curam.list.isPaginated.'+ listId)) return 1000;
      /* NOTE: while the default size is what's used for pagination,
      threshold is what turns on pagination. Hence you can just apply
      the calculation where the threshold is actually reached. */
      if (getFrom('curam.shortlist.'+ listId)) return 1000;
      var psz = 
        getFrom('curam.pageBreak.'+ listId)
        || getFrom('curam.pagination.defaultPageSize') || 1000;
      return psz;
    },
    /**
     * Returns the starting row number to be read by the screen reader
     * for the tables with headers (2) and tables without headers (1).
     * 
     * @param listId the identifier of the list queried.
     * @param refRow a row used in header presence determination.
     */
    getStartShift: function(listId, refRow) {
      if (!refRow) return 2; // best guess default
      var hPath = 'curam.listHeaderStep.' + listId,
            hStep = getFrom(hPath);
      if (hStep) return hStep;
      putTo(hPath, 2);
	  // get header info. Readings correction for the present header is 2,
	  //but just 1 where no header is set on the list
	  var tableRef = query(refRow).closest('table');
	  if (tableRef.length == 0) return 2; //hidden row - no parent.
	  var headerRef = tableRef.children('thead')[0];
	  !headerRef && putTo(hPath, 1);
      return curam.listHeaderStep[listId];
    },
     /**
     * This method is invoked to intercept XMLHttpRequests to 
     * allow the update of sessionExpiry on sessionStorage for each ajax call.
     */
    extendXHR : function(){
        var protoTypeOpen = XMLHttpRequest.prototype.open;
        XMLHttpRequest.prototype.open = function() {
        
          this.addEventListener('load', function() {
            if (typeof(Storage) !== "undefined") {
                var sessionExp = this.getResponseHeader('sessionExpiry');
                sessionStorage.setItem("sessionExpiry", sessionExp);
            }
          });
          protoTypeOpen.apply(this, arguments);
        };
      },
    suppressPaginationUpdate: false,
    updateDeferred: null
  });
  // module level shortcuts. Used by the list control accessibility code above. 
  var cu = curam.util,
      getFrom = dojo.getObject,
      putTo = dojo.setObject,
      isHere = dojo.exists,
      READOUT = 'aria-label',
      ROW_IDX = 'data-lix';

  return curam.util;
});

},
'dojo/regexp':function(){
define(["./_base/kernel", "./_base/lang"], function(dojo, lang){

// module:
//		dojo/regexp

var regexp = {
	// summary:
	//		Regular expressions and Builder resources
};
lang.setObject("dojo.regexp", regexp);

regexp.escapeString = function(/*String*/str, /*String?*/except){
	// summary:
	//		Adds escape sequences for special characters in regular expressions
	// except:
	//		a String with special characters to be left unescaped

	return str.replace(/([\.$?*|{}\(\)\[\]\\\/\+\-^])/g, function(ch){
		if(except && except.indexOf(ch) != -1){
			return ch;
		}
		return "\\" + ch;
	}); // String
};

regexp.buildGroupRE = function(/*Object|Array*/arr, /*Function*/re, /*Boolean?*/nonCapture){
	// summary:
	//		Builds a regular expression that groups subexpressions
	// description:
	//		A utility function used by some of the RE generators. The
	//		subexpressions are constructed by the function, re, in the second
	//		parameter.  re builds one subexpression for each elem in the array
	//		a, in the first parameter. Returns a string for a regular
	//		expression that groups all the subexpressions.
	// arr:
	//		A single value or an array of values.
	// re:
	//		A function. Takes one parameter and converts it to a regular
	//		expression.
	// nonCapture:
	//		If true, uses non-capturing match, otherwise matches are retained
	//		by regular expression. Defaults to false

	// case 1: a is a single value.
	if(!(arr instanceof Array)){
		return re(arr); // String
	}

	// case 2: a is an array
	var b = [];
	for(var i = 0; i < arr.length; i++){
		// convert each elem to a RE
		b.push(re(arr[i]));
	}

	 // join the REs as alternatives in a RE group.
	return regexp.group(b.join("|"), nonCapture); // String
};

regexp.group = function(/*String*/expression, /*Boolean?*/nonCapture){
	// summary:
	//		adds group match to expression
	// nonCapture:
	//		If true, uses non-capturing match, otherwise matches are retained
	//		by regular expression.
	return "(" + (nonCapture ? "?:":"") + expression + ")"; // String
};

return regexp;
});

},
'dojo/date/locale':function(){
define([
	"../_base/lang",
	"../_base/array",
	"../date",
	/*===== "../_base/declare", =====*/
	"../cldr/supplemental",
	"../i18n",
	"../regexp",
	"../string",
	"../i18n!../cldr/nls/gregorian",
	"module"
], function(lang, array, date, /*===== declare, =====*/ supplemental, i18n, regexp, string, gregorian, module){

// module:
//		dojo/date/locale

var exports = {
	// summary:
	//		This modules defines dojo/date/locale, localization methods for Date.
};
lang.setObject(module.id.replace(/\//g, "."), exports);

// Localization methods for Date.   Honor local customs using locale-dependent dojo.cldr data.

// Load the bundles containing localization information for
// names and formats

//NOTE: Everything in this module assumes Gregorian calendars.
// Other calendars will be implemented in separate modules.

	// Format a pattern without literals
	function formatPattern(dateObject, bundle, options, pattern){
		return pattern.replace(/([a-z])\1*/ig, function(match){
			var s, pad,
				c = match.charAt(0),
				l = match.length,
				widthList = ["abbr", "wide", "narrow"];
			switch(c){
				case 'G':
					s = bundle[(l < 4) ? "eraAbbr" : "eraNames"][dateObject.getFullYear() < 0 ? 0 : 1];
					break;
				case 'y':
					s = dateObject.getFullYear();
					switch(l){
						case 1:
							break;
						case 2:
							if(!options.fullYear){
								s = String(s); s = s.substr(s.length - 2);
								break;
							}
							// fallthrough
						default:
							pad = true;
					}
					break;
				case 'Q':
				case 'q':
					s = Math.ceil((dateObject.getMonth()+1)/3);
//					switch(l){
//						case 1: case 2:
							pad = true;
//							break;
//						case 3: case 4: // unimplemented
//					}
					break;
				case 'M':
				case 'L':
					var m = dateObject.getMonth();
					if(l<3){
						s = m+1; pad = true;
					}else{
						var propM = [
							"months",
							c == 'L' ? "standAlone" : "format",
							widthList[l-3]
						].join("-");
						s = bundle[propM][m];
					}
					break;
				case 'w':
					var firstDay = 0;
					s = exports._getWeekOfYear(dateObject, firstDay); pad = true;
					break;
				case 'd':
					s = dateObject.getDate(); pad = true;
					break;
				case 'D':
					s = exports._getDayOfYear(dateObject); pad = true;
					break;
				case 'e':
				case 'c':
					var d = dateObject.getDay();
					if(l<2){
						s = (d - supplemental.getFirstDayOfWeek(options.locale) + 8) % 7
						break;
					}
					// fallthrough
				case 'E':
					d = dateObject.getDay();
					if(l<3){
						s = d+1; pad = true;
					}else{
						var propD = [
							"days",
							c == 'c' ? "standAlone" : "format",
							widthList[l-3]
						].join("-");
						s = bundle[propD][d];
					}
					break;
				case 'a':
					var timePeriod = dateObject.getHours() < 12 ? 'am' : 'pm';
					s = options[timePeriod] || bundle['dayPeriods-format-wide-' + timePeriod];
					break;
				case 'h':
				case 'H':
				case 'K':
				case 'k':
					var h = dateObject.getHours();
					// strange choices in the date format make it impossible to write this succinctly
					switch (c){
						case 'h': // 1-12
							s = (h % 12) || 12;
							break;
						case 'H': // 0-23
							s = h;
							break;
						case 'K': // 0-11
							s = (h % 12);
							break;
						case 'k': // 1-24
							s = h || 24;
							break;
					}
					pad = true;
					break;
				case 'm':
					s = dateObject.getMinutes(); pad = true;
					break;
				case 's':
					s = dateObject.getSeconds(); pad = true;
					break;
				case 'S':
					s = Math.round(dateObject.getMilliseconds() * Math.pow(10, l-3)); pad = true;
					break;
				case 'v': // FIXME: don't know what this is. seems to be same as z?
				case 'z':
					// We only have one timezone to offer; the one from the browser
					s = exports._getZone(dateObject, true, options);
					if(s){break;}
					l=4;
					// fallthrough... use GMT if tz not available
				case 'Z':
					var offset = exports._getZone(dateObject, false, options);
					var tz = [
						(offset<=0 ? "+" : "-"),
						string.pad(Math.floor(Math.abs(offset)/60), 2),
						string.pad(Math.abs(offset)% 60, 2)
					];
					if(l==4){
						tz.splice(0, 0, "GMT");
						tz.splice(3, 0, ":");
					}
					s = tz.join("");
					break;
//				case 'Y': case 'u': case 'W': case 'F': case 'g': case 'A':
//					console.log(match+" modifier unimplemented");
				default:
					throw new Error("dojo.date.locale.format: invalid pattern char: "+pattern);
			}
			if(pad){ s = string.pad(s, l); }
			return s;
		});
	}

/*=====
var __FormatOptions = exports.__FormatOptions = declare(null, {
	// selector: String
	//		choice of 'time','date' (default: date and time)
	// formatLength: String
	//		choice of long, short, medium or full (plus any custom additions).  Defaults to 'short'
	// datePattern:String
	//		override pattern with this string
	// timePattern:String
	//		override pattern with this string
	// am: String
	//		override strings for am in times
	// pm: String
	//		override strings for pm in times
	// locale: String
	//		override the locale used to determine formatting rules
	// fullYear: Boolean
	//		(format only) use 4 digit years whenever 2 digit years are called for
	// strict: Boolean
	//		(parse only) strict parsing, off by default
});
=====*/

exports._getZone = function(/*Date*/ dateObject, /*boolean*/ getName, /*__FormatOptions?*/ options){
	// summary:
	//		Returns the zone (or offset) for the given date and options.  This
	//		is broken out into a separate function so that it can be overridden
	//		by timezone-aware code.
	//
	// dateObject:
	//		the date and/or time being formatted.
	//
	// getName:
	//		Whether to return the timezone string (if true), or the offset (if false)
	//
	// options:
	//		The options being used for formatting
	if(getName){
		return date.getTimezoneName(dateObject);
	}else{
		return dateObject.getTimezoneOffset();
	}
};


exports.format = function(/*Date*/ dateObject, /*__FormatOptions?*/ options){
	// summary:
	//		Format a Date object as a String, using locale-specific settings.
	//
	// description:
	//		Create a string from a Date object using a known localized pattern.
	//		By default, this method formats both date and time from dateObject.
	//		Formatting patterns are chosen appropriate to the locale.  Different
	//		formatting lengths may be chosen, with "full" used by default.
	//		Custom patterns may be used or registered with translations using
	//		the dojo/date/locale.addCustomFormats() method.
	//		Formatting patterns are implemented using [the syntax described at
	//		unicode.org](http://www.unicode.org/reports/tr35/tr35-4.html#Date_Format_Patterns)
	//
	// dateObject:
	//		the date and/or time to be formatted.  If a time only is formatted,
	//		the values in the year, month, and day fields are irrelevant.  The
	//		opposite is true when formatting only dates.

	options = options || {};

	var locale = i18n.normalizeLocale(options.locale),
		formatLength = options.formatLength || 'short',
		bundle = exports._getGregorianBundle(locale),
		str = [],
		sauce = lang.hitch(this, formatPattern, dateObject, bundle, options);
	if(options.selector == "year"){
		return _processPattern(bundle["dateFormatItem-yyyy"] || "yyyy", sauce);
	}
	var pattern;
	if(options.selector != "date"){
		pattern = options.timePattern || bundle["timeFormat-"+formatLength];
		if(pattern){str.push(_processPattern(pattern, sauce));}
	}
	if(options.selector != "time"){
		pattern = options.datePattern || bundle["dateFormat-"+formatLength];
		if(pattern){str.push(_processPattern(pattern, sauce));}
	}

	return str.length == 1 ? str[0] : bundle["dateTimeFormat-"+formatLength].replace(/\'/g,'').replace(/\{(\d+)\}/g,
		function(match, key){ return str[key]; }); // String
};

exports.regexp = function(/*__FormatOptions?*/ options){
	// summary:
	//		Builds the regular needed to parse a localized date

	return exports._parseInfo(options).regexp; // String
};

exports._parseInfo = function(/*__FormatOptions?*/ options){
	options = options || {};
	var locale = i18n.normalizeLocale(options.locale),
		bundle = exports._getGregorianBundle(locale),
		formatLength = options.formatLength || 'short',
		datePattern = options.datePattern || bundle["dateFormat-" + formatLength],
		timePattern = options.timePattern || bundle["timeFormat-" + formatLength],
		pattern;
	if(options.selector == 'date'){
		pattern = datePattern;
	}else if(options.selector == 'time'){
		pattern = timePattern;
	}else{
		pattern = bundle["dateTimeFormat-"+formatLength].replace(/\{(\d+)\}/g,
			function(match, key){ return [timePattern, datePattern][key]; });
	}

	var tokens = [],
		re = _processPattern(pattern, lang.hitch(this, _buildDateTimeRE, tokens, bundle, options));
	return {regexp: re, tokens: tokens, bundle: bundle};
};

exports.parse = function(/*String*/ value, /*__FormatOptions?*/ options){
	// summary:
	//		Convert a properly formatted string to a primitive Date object,
	//		using locale-specific settings.
	//
	// description:
	//		Create a Date object from a string using a known localized pattern.
	//		By default, this method parses looking for both date and time in the string.
	//		Formatting patterns are chosen appropriate to the locale.  Different
	//		formatting lengths may be chosen, with "full" used by default.
	//		Custom patterns may be used or registered with translations using
	//		the dojo/date/locale.addCustomFormats() method.
	//
	//		Formatting patterns are implemented using [the syntax described at
	//		unicode.org](http://www.unicode.org/reports/tr35/tr35-4.html#Date_Format_Patterns)
	//		When two digit years are used, a century is chosen according to a sliding
	//		window of 80 years before and 20 years after present year, for both `yy` and `yyyy` patterns.
	//		year < 100CE requires strict mode.
	//
	// value:
	//		A string representation of a date

	// remove non-printing bidi control chars from input and pattern
	var controlChars = /[\u200E\u200F\u202A\u202E]/g,
		info = exports._parseInfo(options),
		tokens = info.tokens, bundle = info.bundle,
		re = new RegExp("^" + info.regexp.replace(controlChars, "") + "$",
			info.strict ? "" : "i"),
		match = re.exec(value && value.replace(controlChars, ""));

	if(!match){ return null; } // null

	var widthList = ['abbr', 'wide', 'narrow'],
		result = [1970,0,1,0,0,0,0], // will get converted to a Date at the end
		amPm = "",
		valid = array.every(match, function(v, i){
		if(!i){return true;}
		var token = tokens[i-1],
			l = token.length,
			c = token.charAt(0);
		switch(c){
			case 'y':
				if(l != 2 && options.strict){
					//interpret year literally, so '5' would be 5 A.D.
					result[0] = v;
				}else{
					if(v<100){
						v = Number(v);
						//choose century to apply, according to a sliding window
						//of 80 years before and 20 years after present year
						var year = '' + new Date().getFullYear(),
							century = year.substring(0, 2) * 100,
							cutoff = Math.min(Number(year.substring(2, 4)) + 20, 99);
						result[0] = (v < cutoff) ? century + v : century - 100 + v;
					}else{
						//we expected 2 digits and got more...
						if(options.strict){
							return false;
						}
						//interpret literally, so '150' would be 150 A.D.
						//also tolerate '1950', if 'yyyy' input passed to 'yy' format
						result[0] = v;
					}
				}
				break;
			case 'M':
			case 'L':
				if(l>2){
					var months = bundle['months-' +
							    (c == 'L' ? 'standAlone' : 'format') +
							    '-' + widthList[l-3]].concat();
					if(!options.strict){
						//Tolerate abbreviating period in month part
						//Case-insensitive comparison
						v = v.replace(".","").toLowerCase();
						months = array.map(months, function(s){ return s.replace(".","").toLowerCase(); } );
					}
					v = array.indexOf(months, v);
					if(v == -1){
//						console.log("dojo/date/locale.parse: Could not parse month name: '" + v + "'.");
						return false;
					}
				}else{
					v--;
				}
				result[1] = v;
				break;
			case 'E':
			case 'e':
			case 'c':
				var days = bundle['days-' +
						  (c == 'c' ? 'standAlone' : 'format') +
						  '-' + widthList[l-3]].concat();
				if(!options.strict){
					//Case-insensitive comparison
					v = v.toLowerCase();
					days = array.map(days, function(d){return d.toLowerCase();});
				}
				v = array.indexOf(days, v);
				if(v == -1){
//					console.log("dojo/date/locale.parse: Could not parse weekday name: '" + v + "'.");
					return false;
				}

				//TODO: not sure what to actually do with this input,
				//in terms of setting something on the Date obj...?
				//without more context, can't affect the actual date
				//TODO: just validate?
				break;
			case 'D':
				result[1] = 0;
				// fallthrough...
			case 'd':
				result[2] = v;
				break;
			case 'a': //am/pm
				var am = options.am || bundle['dayPeriods-format-wide-am'],
					pm = options.pm || bundle['dayPeriods-format-wide-pm'];
				if(!options.strict){
					var period = /\./g;
					v = v.replace(period,'').toLowerCase();
					am = am.replace(period,'').toLowerCase();
					pm = pm.replace(period,'').toLowerCase();
				}
				if(options.strict && v != am && v != pm){
//					console.log("dojo/date/locale.parse: Could not parse am/pm part.");
					return false;
				}

				// we might not have seen the hours field yet, so store the state and apply hour change later
				amPm = (v == pm) ? 'p' : (v == am) ? 'a' : '';
				break;
			case 'K': //hour (1-24)
				if(v == 24){ v = 0; }
				// fallthrough...
			case 'h': //hour (1-12)
			case 'H': //hour (0-23)
			case 'k': //hour (0-11)
				//TODO: strict bounds checking, padding
				if(v > 23){
//					console.log("dojo/date/locale.parse: Illegal hours value");
					return false;
				}

				//in the 12-hour case, adjusting for am/pm requires the 'a' part
				//which could come before or after the hour, so we will adjust later
				result[3] = v;
				break;
			case 'm': //minutes
				result[4] = v;
				break;
			case 's': //seconds
				result[5] = v;
				break;
			case 'S': //milliseconds
				result[6] = v;
//				break;
//			case 'w':
//TODO				var firstDay = 0;
//			default:
//TODO: throw?
//				console.log("dojo/date/locale.parse: unsupported pattern char=" + token.charAt(0));
		}
		return true;
	});

	var hours = +result[3];
	if(amPm === 'p' && hours < 12){
		result[3] = hours + 12; //e.g., 3pm -> 15
	}else if(amPm === 'a' && hours == 12){
		result[3] = 0; //12am -> 0
	}

	//TODO: implement a getWeekday() method in order to test
	//validity of input strings containing 'EEE' or 'EEEE'...

	var dateObject = new Date(result[0], result[1], result[2], result[3], result[4], result[5], result[6]); // Date
	if(options.strict){
		dateObject.setFullYear(result[0]);
	}

	// Check for overflow.  The Date() constructor normalizes things like April 32nd...
	//TODO: why isn't this done for times as well?
	var allTokens = tokens.join(""),
		dateToken = allTokens.indexOf('d') != -1,
		monthToken = allTokens.indexOf('M') != -1;

	if(!valid ||
		(monthToken && dateObject.getMonth() > result[1]) ||
		(dateToken && dateObject.getDate() > result[2])){
		return null;
	}

	// Check for underflow, due to DST shifts.  See #9366
	// This assumes a 1 hour dst shift correction at midnight
	// We could compare the timezone offset after the shift and add the difference instead.
	if((monthToken && dateObject.getMonth() < result[1]) ||
		(dateToken && dateObject.getDate() < result[2])){
		dateObject = date.add(dateObject, "hour", 1);
	}

	return dateObject; // Date
};

function _processPattern(pattern, applyPattern, applyLiteral, applyAll){
	//summary: Process a pattern with literals in it

	// Break up on single quotes, treat every other one as a literal, except '' which becomes '
	var identity = function(x){return x;};
	applyPattern = applyPattern || identity;
	applyLiteral = applyLiteral || identity;
	applyAll = applyAll || identity;

	//split on single quotes (which escape literals in date format strings)
	//but preserve escaped single quotes (e.g., o''clock)
	var chunks = pattern.match(/(''|[^'])+/g),
		literal = pattern.charAt(0) == "'";

	array.forEach(chunks, function(chunk, i){
		if(!chunk){
			chunks[i]='';
		}else{
			chunks[i]=(literal ? applyLiteral : applyPattern)(chunk.replace(/''/g, "'"));
			literal = !literal;
		}
	});
	return applyAll(chunks.join(''));
}

function _buildDateTimeRE(tokens, bundle, options, pattern){
	pattern = regexp.escapeString(pattern);
	if(!options.strict){ pattern = pattern.replace(" a", " ?a"); } // kludge to tolerate no space before am/pm
	return pattern.replace(/([a-z])\1*/ig, function(match){
		// Build a simple regexp.  Avoid captures, which would ruin the tokens list
		var s,
			c = match.charAt(0),
			l = match.length,
			p2 = '', p3 = '';
		if(options.strict){
			if(l > 1){ p2 = '0' + '{'+(l-1)+'}'; }
			if(l > 2){ p3 = '0' + '{'+(l-2)+'}'; }
		}else{
			p2 = '0?'; p3 = '0{0,2}';
		}
		switch(c){
			case 'y':
				s = '\\d{2,4}';
				break;
			case 'M':
			case 'L':
				s = (l>2) ? '\\S+?' : '1[0-2]|'+p2+'[1-9]';
				break;
			case 'D':
				s = '[12][0-9][0-9]|3[0-5][0-9]|36[0-6]|'+p2+'[1-9][0-9]|'+p3+'[1-9]';
				break;
			case 'd':
				s = '3[01]|[12]\\d|'+p2+'[1-9]';
				break;
			case 'w':
				s = '[1-4][0-9]|5[0-3]|'+p2+'[1-9]';
				break;
			case 'E':
			case 'e':
			case 'c':
				s = '.+?'; // match anything including spaces until the first pattern delimiter is found such as a comma or space
				break;
			case 'h': //hour (1-12)
				s = '1[0-2]|'+p2+'[1-9]';
				break;
			case 'k': //hour (0-11)
				s = '1[01]|'+p2+'\\d';
				break;
			case 'H': //hour (0-23)
				s = '1\\d|2[0-3]|'+p2+'\\d';
				break;
			case 'K': //hour (1-24)
				s = '1\\d|2[0-4]|'+p2+'[1-9]';
				break;
			case 'm':
			case 's':
				s = '[0-5]\\d';
				break;
			case 'S':
				s = '\\d{'+l+'}';
				break;
			case 'a':
				var am = options.am || bundle['dayPeriods-format-wide-am'],
					pm = options.pm || bundle['dayPeriods-format-wide-pm'];
					s = am + '|' + pm;
				if(!options.strict){
					if(am != am.toLowerCase()){ s += '|' + am.toLowerCase(); }
					if(pm != pm.toLowerCase()){ s += '|' + pm.toLowerCase(); }
					if(s.indexOf('.') != -1){ s += '|' + s.replace(/\./g, ""); }
				}
				s = s.replace(/\./g, "\\.");
				break;
			default:
			// case 'v':
			// case 'z':
			// case 'Z':
				s = ".*";
//				console.log("parse of date format, pattern=" + pattern);
		}

		if(tokens){ tokens.push(match); }

		return "(" + s + ")"; // add capture
	}).replace(/[\xa0 ]/g, "[\\s\\xa0]"); // normalize whitespace.  Need explicit handling of \xa0 for IE.
}

var _customFormats = [];
var _cachedGregorianBundles = {};
exports.addCustomFormats = function(/*String*/ packageName, /*String*/ bundleName){
	// summary:
	//		Add a reference to a bundle containing localized custom formats to be
	//		used by date/time formatting and parsing routines.
	//
	// description:
	//		The user may add custom localized formats where the bundle has properties following the
	//		same naming convention used by dojo.cldr: `dateFormat-xxxx` / `timeFormat-xxxx`
	//		The pattern string should match the format used by the CLDR.
	//		See dojo/date/locale.format() for details.
	//		The resources must be loaded by dojo.requireLocalization() prior to use

	_customFormats.push({pkg:packageName,name:bundleName});
	_cachedGregorianBundles = {};
};

exports._getGregorianBundle = function(/*String*/ locale){
	if(_cachedGregorianBundles[locale]){
		return _cachedGregorianBundles[locale];
	}
	var gregorian = {};
	array.forEach(_customFormats, function(desc){
		var bundle = i18n.getLocalization(desc.pkg, desc.name, locale);
		gregorian = lang.mixin(gregorian, bundle);
	}, this);
	return _cachedGregorianBundles[locale] = gregorian; /*Object*/
};

exports.addCustomFormats(module.id.replace(/\/date\/locale$/, ".cldr"),"gregorian");

exports.getNames = function(/*String*/ item, /*String*/ type, /*String?*/ context, /*String?*/ locale){
	// summary:
	//		Used to get localized strings from dojo.cldr for day or month names.
	//
	// item:
	//	'months' || 'days'
	// type:
	//	'wide' || 'abbr' || 'narrow' (e.g. "Monday", "Mon", or "M" respectively, in English)
	// context:
	//	'standAlone' || 'format' (default)
	// locale:
	//	override locale used to find the names

	var label,
		lookup = exports._getGregorianBundle(locale),
		props = [item, context, type];
	if(context == 'standAlone'){
		var key = props.join('-');
		label = lookup[key];
		// Fall back to 'format' flavor of name
		if(label[0] == 1){ label = undefined; } // kludge, in the absence of real aliasing support in dojo.cldr
	}
	props[1] = 'format';

	// return by copy so changes won't be made accidentally to the in-memory model
	return (label || lookup[props.join('-')]).concat(); /*Array*/
};

exports.isWeekend = function(/*Date?*/ dateObject, /*String?*/ locale){
	// summary:
	//	Determines if the date falls on a weekend, according to local custom.

	var weekend = supplemental.getWeekend(locale),
		day = (dateObject || new Date()).getDay();
	if(weekend.end < weekend.start){
		weekend.end += 7;
		if(day < weekend.start){ day += 7; }
	}
	return day >= weekend.start && day <= weekend.end; // Boolean
};

// These are used only by format and strftime.  Do they need to be public?  Which module should they go in?

exports._getDayOfYear = function(/*Date*/ dateObject){
	// summary:
	//		gets the day of the year as represented by dateObject
	return date.difference(new Date(dateObject.getFullYear(), 0, 1, dateObject.getHours()), dateObject) + 1; // Number
};

exports._getWeekOfYear = function(/*Date*/ dateObject, /*Number*/ firstDayOfWeek){
	if(arguments.length == 1){ firstDayOfWeek = 0; } // Sunday

	var firstDayOfYear = new Date(dateObject.getFullYear(), 0, 1).getDay(),
		adj = (firstDayOfYear - firstDayOfWeek + 7) % 7,
		week = Math.floor((exports._getDayOfYear(dateObject) + adj - 1) / 7);

	// if year starts on the specified day, start counting weeks at 1
	if(firstDayOfYear == firstDayOfWeek){ week++; }

	return week; // Number
};

return exports;
});

},
'dijit/Destroyable':function(){
define([
	"dojo/_base/array", // array.forEach array.map
	"dojo/aspect",
	"dojo/_base/declare"
], function(array, aspect, declare){

	// module:
	//		dijit/Destroyable

	return declare("dijit.Destroyable", null, {
		// summary:
		//		Mixin to track handles and release them when instance is destroyed.
		// description:
		//		Call this.own(...) on list of handles (returned from dojo/aspect, dojo/on,
		//		dojo/Stateful::watch, or any class (including widgets) with a destroyRecursive() or destroy() method.
		//		Then call destroy() later to destroy this instance and release the resources.

		destroy: function(/*Boolean*/ preserveDom){
			// summary:
			//		Destroy this class, releasing any resources registered via own().
			this._destroyed = true;
		},

		own: function(){
			// summary:
			//		Track specified handles and remove/destroy them when this instance is destroyed, unless they were
			//		already removed/destroyed manually.
			// tags:
			//		protected
			// returns:
			//		The array of specified handles, so you can do for example:
			//	|		var handle = this.own(on(...))[0];

			var cleanupMethods = [
				"destroyRecursive",
				"destroy",
				"remove"
			];

			array.forEach(arguments, function(handle){
				// When this.destroy() is called, destroy handle.  Since I'm using aspect.before(),
				// the handle will be destroyed before a subclass's destroy() method starts running, before it calls
				// this.inherited() or even if it doesn't call this.inherited() at all.  If that's an issue, make an
				// onDestroy() method and connect to that instead.
				var destroyMethodName;
				var odh = aspect.before(this, "destroy", function (preserveDom){
					handle[destroyMethodName](preserveDom);
				});

				// Callback for when handle is manually destroyed.
				var hdhs = [];
				function onManualDestroy(){
					odh.remove();
					array.forEach(hdhs, function(hdh){
						hdh.remove();
					});
				}

				// Setup listeners for manual destroy of handle.
				// Also computes destroyMethodName, used in listener above.
				if(handle.then){
					// Special path for Promises.  Detect when Promise is resolved, rejected, or
					// canceled (nb: cancelling a Promise causes it to be rejected).
					destroyMethodName = "cancel";
					handle.then(onManualDestroy, onManualDestroy);
				}else{
					// Path for other handles.  Just use AOP to detect when handle is manually destroyed.
					array.forEach(cleanupMethods, function(cleanupMethod){
						if(typeof handle[cleanupMethod] === "function"){
							if(!destroyMethodName){
								// Use first matching method name in above listener (prefer destroyRecursive() to destroy())
								destroyMethodName = cleanupMethod;
							}
							hdhs.push(aspect.after(handle, cleanupMethod, onManualDestroy, true));
						}
					});
				}
			}, this);

			return arguments;		// handle
		}
	});
});

},
'curam/cdsl/Struct':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 23-Sep-2014  MV  [CR00445374] Remove unneeded inherited call in constructor.
 */

define(["dojo/_base/declare",
        "dojo/_base/lang",
        "./_base/_StructBase"
        ], function(
            declare, lang, _StructBase) {

  /**
   * @name curam.cdsl.Struct
   * @namespace Represents a Curam struct. The instances of this class
   *  are expected as inputs and will be produced as outputs of the CDSL API.
   *  The properties you specify in constructor are the ones that will be used
   *  for mapping onto your modeled Curam structs. You are not allowed to add
   *  more properties dynamically after the Struct object instance has been
   *  created.
   */
  var Struct = declare(_StructBase,
  /**
   * @lends curam.cdsl.Struct.prototype
   */
  {
    /**
     * Creates an instance of Struct.
     * 
     * @param {Object} data Object containing values to be mapped to the struct
     *  properties. The values must adhere to the data types supported
     *  by CDSL API and they must map correctly to the modelled server side
     *  structs in the context where you will use this Struct instance.
     */
    constructor: function(data) {
      // fill this object with data passed into the constructor 
      lang.mixin(this, this._data);
    }
  });
  
  return Struct;
});

},
'curam/cdsl/_base/FacadeMethodResponse':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 21-Oct-2014  VT  [CR00446323] Implement CDSL - JSON Hijacking Prevention
 * 10-Oct-2014  MV  [CR00446578] Add support for development mode.
 * 07-Oct-2014  MV  [CR00446356] Fix getError() function.
 * 26-Sep-2014  MV  [CR00445374] Implement generic data hookpoint.
 */

define(['dojo/_base/declare',
        'curam/cdsl/Struct',
        'dojo/json',
        'dojo/string'
        ], function(
            declare, Struct, json, string) {
  
  /**
   * Prepend 'junk' to the JSON request to prevent <script > injection
   * (the junk will freeze the browser in an infinite loop if an attacker 
   *  attempts to execute JSON as JavaScript.)
   *
   */
  var jsonPrependJunk = "for(;;);";

  /**
   * Converts the list of errors from the server into a printable string.
   * 
   * @param errors The array of error objects to print.
   * @param nestLevel Nesting level of the given error list.
   * 
   * @return Printable string representing the error objects.
   */
  var errorToString = function(errors, nestLevel) {
    var ret = [],
        indent = nestLevel ? Array(nestLevel + 1).join('  ') : '';
    
    dojo.forEach(errors, function(e) {
      ret.push(string.substitute(
          '${indent}Type: ${type}\n'
          + '${indent}Message: ${msg}\n'
          + '${indent}Stack trace:\n'
          + '${indent}  ${stackTrace}', {
        type: e.type,
        msg: e.message,
        stackTrace: e.stackTrace,
        indent: indent 
      }));
      
      if (e.nestedError) {
        ret.push('\n-- nested error --');
        ret.push(errorToString([e.nestedError], nestLevel ? nestLevel + 1 : 1));
      }
    });
    
    return ret.join('\n');
  };
  
  /**
   * @name curam.cdsl.request.FacadeMethodResponse
   * @namespace Represents a response to a Curam facade method call. 
   */
  var FacadeMethodResponse = declare(null,
  /**
   * @lends curam.cdsl.request.FacadeMethodResponse.prototype
   */
  {
    _request: null,
    
    _data: null,
    
    _metadataRegistry: null,

    /**
     * Creates an instance.
     *
     * @param {String or Object} responseJsonStringOrObject The response data.
     */
    constructor: function(methodCall, responseJsonStringOrObject, metadataRegistry) {
      if (!methodCall || !responseJsonStringOrObject) {
        throw new Error('Missing parameter.');
      }

      if (typeof responseJsonStringOrObject == 'string') {
        this._data = json.parse(responseJsonStringOrObject.substr(
        jsonPrependJunk.length, responseJsonStringOrObject.length));
		
      } else if (typeof responseJsonStringOrObject == 'object') {
        this._data = responseJsonStringOrObject;
		
      } else {
        throw new Error('Wrong parameter type: ' + typeof methodCall +
        ', '
        + typeof responseJsonStringOrObject);
      }

      this._request = methodCall;
      this._metadataRegistry = metadataRegistry;
      
    },
    
    returnValue: function() {
      // the data property of the JSON contains the returned struct
      return new Struct(this._data.data, {
        bareInput: true,
        fixups: this._data.metadata && this._data.metadata.fixups
                    ? this._data.metadata.fixups
                    : null, 
        metadataRegistry: this._metadataRegistry,
        dataAdapter: this._request.dataAdapter()
      });
    },

    failed: function() {
      return this._data.code !== 0;
    },
    
    getError: function() {
      var errors = this._data.errors;
      if (errors) {
        var e = new Error('Server returned ' + errors.length
            + (errors.length == 1 ? ' error' : ' errors') + '.');
        e.errors = errors;
        
        // override toString() so that we get decent output in console
        e.toString = function() {
          return errorToString(errors);
        };
        
        return e;
      }
      
      return null;
    },
    
    hasCodetables: function() {
      return this._data.metadata
          && this._data.metadata.codetables
          && this._data.metadata.codetables.length > 0;
    },
    
    getCodetablesData: function() {
      return this._data.metadata.codetables;
    },
    
    devMode: function() {
      var dm = false;
      
      if (this._data.metadata && this._data.metadata.devMode) {
        dm = (this._data.metadata.devMode === true);
      }
      
      return dm;
    },
    
    request: function() {
      return this._request;
    }
  });
  
  return FacadeMethodResponse;
});

},
'curam/cdsl/_base/_StructBase':function(){
/*
 * Copyright 2013 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 23-Sep-2014  MV  [CR00445374] Implement generic data hookpoint.
 * 21-Jan-2014  MV  [CR00412812] Added mising support for handling
 *      of Date data type.
 */

define(['dojo/_base/declare',
        'dojo/_base/lang',
        'dojo/json',
        'curam/cdsl/types/FrequencyPattern'
        ], function(
            declare, lang, json, FrequencyPattern) {

  var DEFAULT_OPTIONS = {
    bareInput: false,
    fixups: null,
    metadataRegistry: null,
    dataAdapter: null
  };
  
  var processOptions = function(options) {
    var o = lang.clone(DEFAULT_OPTIONS);
    return lang.mixin(o, options);
  };
  
  var DEFAULT_DATA_ADAPTER = {
    onRequest: {
      onItem: function(path, data) { return data; },
      onStruct: function(structData) { },
    },
    
    onResponse: {
      onItem: function(path, data) { return data; },
      onStruct: function(structData) { },
    }
  };
  
  var concatToPath = function(frag, newPart) {
    if (frag) {
      return frag + "." + newPart;
    }
    
    return newPart;
  };
  

  /**
   * @name curam.cdsl._base._StructBase
   * @namespace Base class for Curam structs.
   *     This is provided so that the actual Struct class can inherit
   *     any methods and keep it's own object for storing the actual data
   *     properties. This is recommended by the dojo/store design:
   * http://dojotoolkit.org/reference-guide/1.9/dojo/store.html#returned-objects
   */
  var Struct = declare(null,
  /**
   * @lends curam._base._StructBase.prototype
   */
  {
    _data: null,
    
    _converter: null,
    
    _dataAdapter: null,

    /**
     * By default we just take the data parameter and this is meant
     * as the public interface for CDSL clients. In this mode we expect
     * to be getting data in typed form.
     * 
     * The other usages are governed by options, and these are for internal
     * CDSL usage.
     */
    constructor: function(data, opts) {
      if (!data) {
        throw new Error("Missing parameter.");
      }
      if (typeof data !== "object") {
        throw new Error("Wrong parameter type: " + typeof data);
      }
      
      var options = processOptions(opts);
      
      if (!options.bareInput) {
        this._data = this._typedToBare(data);

      } else {
        this.setDataAdapter(options.dataAdapter);

        this._data = this._bareToTyped(data);
        if (options.fixups) {
          this._applyFixUps(options.fixups, this._data,
              options.metadataRegistry);
        }
      }
    },
    
    _applyFixUps: function(fixups, data, metadataRegistry) {

      dojo.forEach(fixups, function(item, index) {
        var path = fixups[index].path;
        var type = fixups[index].type;
        
        this._processFixUp(data,
            path, this._getTransformFunction(type, metadataRegistry));            
       }, this);
    },
    
    _processFixUp: function(data, path, transform) {

      if(path.length == 1) {        
        data[path[0]] = transform(data[path[0]]);
        return;                 

      } else {
        if (lang.isArray(data[path[0]])) {
          dojo.forEach(data[path[0]], function(item, index){
            this._processFixUp(item, path.slice(1, path.length), transform);
          }, this);

        } else {
          this._processFixUp(data[path[0]], path.slice(1, path.length),
              transform);
        }
      }      
    },

    _getTransformFunction: function(type, metadataRegistry){
      
      // TODO add in other types, refactor out transform functions?
      // TODO cleaner with just a map of maps?
      if (type[0] === "frequencypattern") {
        return function(data) {
          return new FrequencyPattern(data.code, data.description);
        };

      } else if (type[0] === "datetime") {
        return function(data) {
          // TODO
          return new Date(data);
        };

      } else if (type[0] === "date") {
        return function(data) {
          // TODO
          return new Date(data);
        };

      } else if (type[0] === "time") {
        return function(data) {
          // TODO
          return new Date(data);
        };

      } else if (type[0] === "codetable") {
        
        if (type.length < 2) {
          throw new Error(
              "Missing codetable name, type specified is: " + type);
        }
        
        return function(data) {
          //TODO handle case where code does not exist
          var codetable = metadataRegistry.codetables()[type[1]];
          if (codetable){             
            return codetable.getItem(data);

          } else {
            throw new Error(
                "Codetable does not exist: codetable name=" + type[1]);
          }            
        };

      } else {
        throw new Error("Unsupported type: " + type);
      } 
    },    

    toJson: function() {
      return json.stringify(this.getData());
    },
    
    /**
     * Returns data in bare (untyped) form suitable for conversion to JSON.
     */
    getData: function() {
      // collect new data values - they might have been changed since
      // this instance was created
      for (var name in this._data) {
        this._data[name] = this[name];
      }
      
      return this._typedToBare(this._data);
    },

    /**
     * Takes data in the form adhering to the CDSL contract with regards
     * to JSON-serialized data types and converts them into the corresponding
     * JavaScript object representation.
     *
     * @param {Object} data Deserialized JSON data object.
     * @param {String} [path] Path to the data.
     * @returns JavaScript object ready to be used by this Struct API.
     */
    _bareToTyped: function(data, path) {
      if (lang.isObject(data)) {
        var retData = {};
        
        // apply client provided data transforms on a Struct level
        this._applyResponseStructAdapter(data);

        for (var prop in data) {
          if (lang.isArray(data[prop])) {
            retData[prop] = [];
            for (var i = 0; i < data[prop].length; i++) {
              retData[prop].push(this._bareToTyped(data[prop][i],
                  concatToPath(path, prop + "[" + i + "]")));
            }

          } else if (typeof data[prop] === 'object') {
            retData[prop] = this._bareToTyped(data[prop],
                concatToPath(path, prop));        

          } else {
            retData[prop] = data[prop];
          }
        
          // apply client provided data transforms on item level
          var fullPath = concatToPath(path, prop);
          retData[prop] = this._applyResponseDataAdapter(fullPath, retData[prop]);
        }
        
        return retData;
      }
      
      // apply client provided data transforms on item level
      return this._applyResponseDataAdapter(path, data);
    },

    /**
     * Converts typed (external) data into a form that conforms to the CDSL
     * contract with regards to JSON-serialized data types.
     *
     * @param {Object} data JavaScript objects representing the Struct data.
     * @param {String} [path] Path to the data.
     * @returns JavaScript object ready to be passed into JSON.stringify()
     *    and sent to CDSL servlet.
     */
    _typedToBare: function(data, path) {
      if (lang.isObject(data)) {
        var retData = {};
        for (var prop in data) {
          if (data.hasOwnProperty(prop)
              // strip out Dojo class related properties
              && '_data' !== prop
              && '_dataAdapter' !== prop
              && '_inherited' !== prop
              && '_converter' !== prop) {
            
            // handle arrays
            if (lang.isArray(data[prop])) {
              retData[prop] = [];
              for (var i = 0; i < data[prop].length; i++) {
                retData[prop].push(this._typedToBare(data[prop][i],
                    concatToPath(path, prop + "[" + i + "]")));
              }
            
            // handle codetables
            } else if (data[prop].getDescription && data[prop].getCode) {
              retData[prop] = data[prop].getCode();
              
            // handle Date
            } else if (data[prop].getTime) {
              retData[prop] = data[prop].getTime();

            } else if (typeof data[prop] === 'object') {
              retData[prop] = this._typedToBare(data[prop],
                  concatToPath(path, prop));        

            } else {
              retData[prop] = data[prop];
            }
            
            // apply client provided data transforms on item level
            var fullPath = concatToPath(path, prop);
            retData[prop] = this._applyRequestDataAdapter(fullPath, retData[prop]);
          }
        }

        this._applyRequestStructAdapter(retData);
        
        return retData;
      }
      
      // apply client provided data transforms on item level
      return this._applyRequestDataAdapter(path, data);
    },
    
    /**
     * Sets the data adapter to use for items of this struct.
     * 
     * @param {Object} adapter The adapter with appropriate callback functions.
     */
    setDataAdapter: function(adapter) {
      if (adapter) {
        var a = lang.clone(DEFAULT_DATA_ADAPTER);
        
        if (adapter.onRequest && adapter.onRequest.onItem) {
          a.onRequest.onItem = adapter.onRequest.onItem;
        }
        if (adapter.onRequest && adapter.onRequest.onStruct) {
          a.onRequest.onStruct = adapter.onRequest.onStruct;
        }
        if (adapter.onResponse && adapter.onResponse.onItem) {
          a.onResponse.onItem = adapter.onResponse.onItem;
        }
        if (adapter.onResponse && adapter.onResponse.onStruct) {
          a.onResponse.onStruct = adapter.onResponse.onStruct;
        }
        
        this._dataAdapter = a;
      
      } else {
        this._dataAdapter = null;
      }
    },
    
    _applyRequestDataAdapter: function(path, value) {
      if (this._dataAdapter) {
        return this._dataAdapter.onRequest.onItem(path, value);
      }
      
      return value;
    },

    _applyResponseDataAdapter: function(path, value) {
      if (this._dataAdapter) {
        return this._dataAdapter.onResponse.onItem(path, value);
      }
      
      return value;
    },
    
    _applyRequestStructAdapter: function(structData) {
      if (this._dataAdapter) {
        this._dataAdapter.onRequest.onStruct(structData);
      }
    },

    _applyResponseStructAdapter: function(structData) {
      if (this._dataAdapter) {
        this._dataAdapter.onResponse.onStruct(structData);
      }
    }
  });
  
  return Struct;
});

},
'curam/cdsl/util/Preferences':function(){
/*
 * Copyright 2014 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/*
 * Modification History
 * --------------------
 * 13-Oct-2014  SC [CR00446751] Initial version.
 */

define(['dojo/_base/declare',
        'dojo/_base/lang',
        'curam/cdsl/_base/FacadeMethodCall',
        'curam/cdsl/request/CuramService',
        'curam/cdsl/Struct'
        ], function(
            declare, lang, FacadeMethodCall, CuramService, Struct) {

  /**
   * Provides access to both global and user preferences. The loadPreferences()
   * function must be called before attempting to access any preferences. The
   * set of preferences are associated with each connection and only need to 
   * be loaded once.
   * 
   * @name curam.cdsl.util.Preferences
   * @namespace The main entry point to getting preferences in CDSL.
   */
  var Preferences = declare(null,
  /**
   * @lends curam.cdsl.util.Preferences.prototype
   */
  {
    _connection: null,
    
    /**
     * Instantiates the API.
     * 
     * @param {curam/cdsl/_base/_Connection} connection The connection object
     *  to be used. Use an instance of curam/cdsl/connection/CuramConnection
     *  class.
     */
    constructor: function(connection) {
      if (!connection) {
        throw new Error("Missing parameter.");
      }
      if (typeof connection !== "object") {
        throw new Error("Wrong parameter type: " + typeof connection);
      }

      this._connection = connection;
    },
        
    /**
     * Returns the value of the specified preference.
     * 
     * @param {String} name The name of the preference.
     * 
     * @returns {String} The preference value.
     */
    getPreference: function(name) {
      return this._connection.preferences().getPreference(name);
    },
    
    /**
     * Return the list of preference names.
     * 
     * @returns {Array} The list of preference names.
     */
    getPreferenceNames: function() {
      return this._connection.preferences().getPreferenceNames();
    },
    
    /**
     * Loads all preferences. This function returns a promise that will
     * indicate when the preferences are loaded.
     * 
     * @returns {dojo/Promise::curam/cdsl/util/Preferences} Promise
     *    which resolves when the preferences have been loaded.
     */
    loadPreferences: function() {
      var service = new CuramService(this._connection);
      var getPrefMethod = new FacadeMethodCall(
        "CuramService", "getPreferences");

      return service.call([getPrefMethod]).then(
        lang.hitch(this, function(data) {
          // Process through struct returned and add preferences
          var result = data[0].getData();
          for (prop in result) {
            this._connection.preferences().addPreference(prop, result[prop]);
          }                                    
          return this;
        }));      
      }  
  });
  
  return Preferences;
});

},
'*now':function(r){r(['dojo/i18n!*preload*dojo/nls/cdsl*["ar","ca","cs","da","de","el","en-gb","en-us","es-es","fi-fi","fr-fr","he-il","hu","it-it","ja-jp","ko-kr","nl-nl","nb","pl","pt-br","pt-pt","ru","sk","sl","sv","th","tr","zh-tw","zh-cn","ROOT"]']);}
}});
define("dojo/cdsl", [], 1);
