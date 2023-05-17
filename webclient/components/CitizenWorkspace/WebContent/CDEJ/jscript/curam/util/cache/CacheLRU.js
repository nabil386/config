define(["dojo/_base/declare",
        "dojox/collections/Dictionary"],
        function(declare, Dictionary) {
	



var CacheItem = declare("curam/util/cache/CacheItem", null,
{	
	constPriority : {
		Low: 10,
	    Normal: 20,
		High: 30
	},
	
	constructor: function(keys, items, optionss)
    {
		if (keys == null)
		{ 
            throw new Error("Cache key cannot be null ");
		}
		//.expirationSliding	=	Number of seconds after last access before it expires
		//.expirationAbsolute	=	Javascript Date object indicating when the item is to expire
		//.priority 			=	Integer value indicating priority
		
		this.key = keys;
        this.value = items;
        if (optionss == null)
        {
            optionss = {};
        }
        if (optionss.priority == null)
        {
            optionss.priority = this.constPriority.Normal;
        }
        this.options = optionss;
        this.lastAccessed = new Date().getTime();
    },
    
    destroy : function()
	{
		try
		{
			this.inherited(arguments);
		}	
  		catch(err){
			console.error(err);
		  	//throw err;
		}
	}	
});
	
	
	
	
	return declare("curam/util/cache/CacheLRU",null, 
{
	maxSize : 20, // maxumim size of the cache
	activePurgeFrequency : null, // Time in millie seconds to pole purge 
	_dataColection : null,
	_tippingPoint : null, // % of cache full when it get's purged
	_purgePoint : null,   // The numner of items in the cache when purge invoked
	_purgingNow : false,  // flag to indacate purging is taking place
	constPriority : {
		Low: 10,
	    Normal: 20,
		High: 30
	},
	
	
    constructor: function(params)
 	{
 		try
 		{	
			dojo.mixin(this, params);
			
			
		    this._dataColection = new dojox.collections.Dictionary();
		    if (this.maxSize == null)
		    {
		        this.maxSize = -1;
		    }   
		    
			//console.debug("CacheLRU: "+this.maxSize);
		    
		    if (this.activePurgeFrequency == null)
		    {
		        this.activePurgeFrequency = -1;
		    }
		    this._tippingPoint = .75; 
		    this._purgePoint = Math.round(this.maxSize * this._tippingPoint);  
		    this._purgingNow = false;
		    
		    if(this.activePurgeFrequency > 0)
			{
			    this._timerPurge();
			}
			
			//dojo.addOnUnload(dojo.hitch(this,this.destroy));
 		}
 		catch(e)
 		{
 			console.error('There has been an issue with cache LRU"');
 			console.error(e);
 		}
	    
    },      
    

	/**
	 * addItem Method sets an item in the cache
	 * 
	 * @param key :
	 *            The key to store the data under
	 * @param data :
	 *            The data to be cached
	 * @param options :
	 *            Optional prameter that can be spesfied.
	 *            <ul>
	 *            <li>
	 * 
	 * expirationAbsolute : The date/time when cache entry will expire </li>
	 * <li> expirationSliding : A number representing the seconds since the last
	 * cache access after which the cache entry will expire
	 * 
	 * </li>
	 * <li> priority : Ranking of experation order of items in the cache. Note
	 * that placing a priority on an item does not guarantee it will remain in
	 * cache. It can still be purged if an expiration is hit, or if the cache is
	 * full.
	 * 
	 * </li>
	 * <li> callback : A function that gets called when the a cache item is
	 * purged from the cache. </li>
	 * </ul>
	 */
	addItem : function(key, data, options) 
	{
		if(this.maxSize < 1) {
			//We dont cache anything
			return;
		}

	
	    // add a new cache item to the cache
	    if (this._dataColection.contains(key) == true)
	    {
	        this._removeItem(key);
	    }
	    var newItem = new CacheItem(key, data, options);	    
	    this._dataColection.add(newItem.key,  newItem);
	    
	    // purge cache if full
	    if ((this.maxSize > 0) && (this._dataColection.count > this.maxSize)) {
	        this._purge();
	    }
	},
    
    
	/**
	 * getItem Retrieves an item from the cache, null is returned if the item
	 * does not exits or has expired
	 * 
	 * @param key :
	 *            The key which the item in the cache is cached under
	 */
	getItem : function(key) 
	{
	
	    // retrieve the item from the cache
	    var item = this._dataColection.item(key);
	    
	    if (item != null) 
	    {
	        if (!this._isExpired(item)) 
	        {
	            // if the item is not expired
	            // update its last accessed date
	            item.lastAccessed = new Date().getTime();
	        } 
	        else 
	        {
	            // if the item is expired, remove it from the cache
	            this._removeItem(key);
	            item = null;
	        }
	    }
	    
	    // return the item value (if it exists), or null
	    var returnVal = null;
	    if (item != null) 
	    {
	        returnVal = item.value;
	    } 
	    return returnVal;
	},
	
			
	/*
	 * clear : Removes all items from the cache
	 */
	clear : function() 
	{
		this._dataColection.forEach(
			function(data, index, array) 
			{
		   		var tmp = data.value;
	      		this._removeItem(tmp.key);
		    },
		this); 
	},
	
	/**
	 * Removes expired items from the cache 
	 */
	_purge : function() 
	{			
    	//console.info("timer purge _purge now ********");
	//	if(this._purgingNow == false)
		//{	 
		console.debug("purging cache");
		
			this._purgingNow = true;   
		    var tmparray = new Array();
		    
		    // Expire items that should be expired		    
		    this._dataColection.forEach( 
		    	function(data, index, array) 
		    	{	
		    		var tmp = data.value;		   	
			   		if (this._isExpired(tmp)) 
			   		{
			            this._removeItem(tmp.key);
			        } 
			        else 
			        {
			            tmparray.push(tmp);
			        }
			    },
			this);
						    
			if (tmparray.length > this._purgePoint) 
		    {		
		        // sort this array based on the last accessed date and cache priority
		        tmparray = tmparray.sort(
			        function(a, b) 
			        { 
			            if (a.options.priority != b.options.priority) 
			            {
			                return b.options.priority - a.options.priority;
			            } else 
			            {
			                return b.lastAccessed - a.lastAccessed;
			            }
			        }
				);
		        
		        // remove items from the end of the array
		        while (tmparray.length > this._purgePoint) 
		        {
		            var temp = tmparray.pop();
		            this._removeItem(temp.key);
		        }
		    }
		    this._purgingNow = false;
		//}
	},
	
	/**
	 * Remove item from cache, invoke callback if there
	 */
	_removeItem : function(key) {
	    var item = this._dataColection.item(key);
	    this._dataColection.remove(key);
	    
	    // if there is a callback function, call it at the end of execution
	    if (item.options.callback != null) 
	    {
	        var callback = dojo.hitch(this,function() 
	        {
	            item.options.callback(item.key, item.value);
	        });
	        setTimeout(callback, 0);
	    }
	},
	
	/**
	 * Retruns true if item is be expired
	 */
	_isExpired : function(item) 
	{
	    var now = new Date().getTime();
	    var expired = false;
	    if ((item.options.expirationAbsolute) && (item.options.expirationAbsolute < now)) 
	    {
	        // if the absolute expiration has passed, expire the item
	        expired = true;
	    } 
	    if ((expired == false) && (item.options.expirationSliding)) 
	    {
	        // if the sliding expiration has passed, expire the item
	        var lastAccess = item.lastAccessed + (item.options.expirationSliding * 1000);
	        if (lastAccess < now) {
	            expired = true;
	        }
	    }
	    return expired;
	},
    
    _timerPurge : function()
    {
    	//console.info("timer purge _timerPurge");
    	if(this._dataColection.count  > 0)
    	{
    		this._purge();
    	}
	    
	    this._timerID = setTimeout(dojo.hitch(this,
	    	function()
	    	{  
	    		this._timerPurgeSecond();   
	    	}
	    ), this.activePurgeFrequency );
    },
    
    _timerPurgeSecond : function()
    {
    	//console.info("timer purge _timerPurgeSecond");
    	if(this._dataColection.count  > 0)
    	{
    		this._purge();
    	}
	    
	    this._timerID = setTimeout(dojo.hitch(this,
	    	function()
	    	{  
	    		this._timerPurge();   
	    	}
	    ), this.activePurgeFrequency );
    },
    
    generateCacheOptions/*static*/ : function(slidingTimeout,absoluteTimeout, priority)
	{		
		//slidingTimeout	=	Time in seconds before it expires	
		var cacheOptions = new Object();
		//var now = new Date();
		//now.setTime(now.getTime()+timeout);	
		if(slidingTimeout) {
			cacheOptions.expirationSliding = slidingTimeout;
		}
		if(absoluteTimeout) {
			cacheOptions.expirationAbsolute = absoluteTimeout;
		}
		
		if(priority) {
			cacheOptions.priority = priority;
		} 		
		return cacheOptions;
	},
    
    destroy : function()
	{
		try
		{
			this.clear();
			delete this._dataColection;
			this.inherited(arguments);
		}	
  		catch(err){
			console.error(err);
		  	//throw err;
		}
	}
		
});

});

