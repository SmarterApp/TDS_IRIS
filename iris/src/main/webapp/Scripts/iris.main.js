/*
This code implements the XDM API for use within item preview app.
*/

(function (XDM, CM) {

    var isIrisReady = false;
    var irisUrl = location.href.split("IrisPages")[0];
    Blackbox.getConfig().preventShowOnLoad = true;
    // we load one page in advance, but we don't want that to cause a cascade of page show/load
    Blackbox.getConfig().baseUrl =  irisUrl;
    ContentManager.Dialog.urlFrame =  irisUrl + "Pages/API/content/dialog";
    //This sets read only mode on the content manager disabling the answer entry areas.
    CM.setReadOnly(true);
    // Functions that are used by toolbar buttons
    //Calculator
    var calculatorBtn = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage) {
            Calculator.toggle();
        }
    };
    var comments = function(ev){
        var currentPage = ContentManager.getCurrentPage();
        var currentItem = ContentManager.getCurrentEntity();

        if (currentPage && TDS.Notes && currentItem) {
            var itemId = getItemId(currentItem);
            TDS.Notes.open({"id": itemId, "type": TDS.Notes.Types.TextArea});
        }
    }

    //Global Notes
    var globalNotesBtn = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage && TDS.Notes) {
            TDS.Notes.open();
        }
    };

    //Masking
    var showMask = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage) {
            Masking.toggle();
        }
    };

    var dictionaryBtn = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage) {
            Dictionary.toggle();
        }
    };

    // setup cross domain api 
    XDM.init(window);

    function getItemId(item) {
        return "I-" + item.bankKey + "-" + item.itemKey;
    }

    function getItemMap(requestedItems) {
        var distinctItemCount = 0;

        var itemMap = requestedItems.reduce(function (map, item) {
            ++distinctItemCount;
            map[getItemId(item)] = item;
            return map;
        }, {});

        if (requestedItems.length !== distinctItemCount) {
            throw new Error('One or more of the requested items appears multiple times in this request.');
        }

        return itemMap;
    }

    function getExistingPage(requestedItems) {

        var requestedItemCount = Object.keys(requestedItems).length,
            partialMatches = false,
            matchedPage = null,
            matchedItems = null;

        // go through each page to try matching items
        CM.getPages().forEach(function (page) {
            var items = page.getItems(),
                matches = [];

            // check this page for items which are in the current content request
            items.forEach(function (item) {
                var itemId = getItemId(item),
                    matchedItem = requestedItems[itemId];

                if (matchedItem) {
                    matches.push({
                        loaded: item,
                        requested: matchedItem
                    });
                }
            });

            if (matches.length === items.length && items.length === requestedItemCount) {
                // exact match, save the page and items
                matchedPage = page;
                matchedItems = matches;
            } else if (matches.length) {
                // only some items matched
                partialMatches = true;
            }
        });

        if (partialMatches) {
            throw new Error('One or more of the items requested have already been loaded. Make sure the content request is the same as the orginal (e.g. it can\'t contain different response or label values).');
        }

        return {
            page: matchedPage,
            itemPairs: matchedItems
        };
    }

    function isGlobalNotesEnabled(){
        return TDS.getAccommodationProperties().existsAndNotEquals('Global Notes', 'TDS_GN0');

    }

    function loadContent(xmlDoc) {
        if (typeof xmlDoc == 'string') {
            xmlDoc = Util.Xml.parseFromString(xmlDoc);
        }

        // create array of content json from the xml
        var deferred = $.Deferred();
        var contents = CM.Xml.create(xmlDoc);
        var content = contents[0];

        var itemMap = getItemMap(content.items);
        var result = getExistingPage(itemMap);

        //if the page is already loaded we want to force a reload because the accommodations may have changed.
        if (result.page) {
            // show the page
            TDS.Dialog.hideProgress();
            ContentManager.removePage(result.page);
            // If there is a word list loaded clear the cached words because they may have changed.
            if(window.WordListPanel){
                window.WordListPanel.clearCache();
            }
        }

        page = CM.createPage(content);

        page.render();
        page.once('loaded', function () {
            TDS.Dialog.hideProgress();
            CM.accessibilityEnabled = true;
            page.show();
            CM.accessibilityEnabled = false;
            deferred.resolve();
        });

        ContentManager.onItemEvent("comment", function(ev) {
            comments(ev);
        });

        if (TDS.getAccommodationProperties().hasMaskingEnabled()) {
            Blackbox.showButton('btnMask', showMask, true);
        }

        if(isGlobalNotesEnabled()){
            Blackbox.showButton('btnGlobalNotes', globalNotesBtn, true);
        }
        if (TDS.getAccommodationProperties().hasCalculator()) {
            Blackbox.showButton('btnCalculator', calculatorBtn, true);
        }

        if (TDS.getAccommodationProperties().isDictionaryEnabled()) {
            Blackbox.showButton('btnDictionary', dictionaryBtn, true);
        }

        if (TDS.getAccommodationProperties().showItemToolsMenu()) {
            $(".itemTools").addClass("toolsContainer");
        }
        /*
         If the print size is specified we need to set it because the previous
         If not set it to zero because this may not be the first item we are loading and the zoom level
         may have been set when we loaded an item earlier.
         */
        var printSize = CM.getAccProps().getPrintSize();
        if(printSize) {
            CM.getZoom().setLevel(printSize, true);
        } else {
            CM.getZoom().setLevel(0, true);
        }
        Blackbox.bindUIEvents();

        return deferred.promise();
    }

    var loadedDefaultAccommodations = false;

    function parseAccommodations(segmentId, position, label, segmentEl) {
        var types = [];

        $(segmentEl).find('accommodation').each(function () {
            var $this = $(this);

            types.push({
                name: $this.attr('type'),

                values: [{
                    name: $this.attr('name'),
                    code: $this.attr('code'),
                    selected: $this.attr('selected') === 'true',
                    isDefault: true
                }]
            });
        });

        // clone default accommodations
        var accs = Accommodations.Manager.getDefault().clone();

        // overwrite with segment-specific accommodations
        accs.importJson({
            id: segmentId,
            position: position,
            label: label,
            types: types
        });

        // se the first segment's accommodations as default
        if (!loadedDefaultAccommodations) {
            loadedDefaultAccommodations = true;
            Accommodations.Manager.setDefault(segmentId);
        }

        return accs;
    }

    function loadGroupedContent(xmlDoc) {
        if (CM.getPages().length > 0) {
            throw new Error("content has already been loaded; cannot load grouped content");
        }

        if (typeof xmlDoc == 'string') {
            xmlDoc = Util.Xml.parseFromString(xmlDoc);
        }

        $(xmlDoc).find('segment').each(function () {

            var segmentId = $(this).attr('id');

            // parse accommodations
            var accommodations = parseAccommodations(segmentId, 'position', 'label', this);
            Accommodations.Manager.add(accommodations);

            // parse content
            var contents = CM.Xml.create(this);
            for (var i = 0; i < contents.length; ++i) {
                var content = contents[i];
                var page = CM.createPage(content);

                // when a page is shown, we want to begin rendering the following page
                page.once('show', function () {
                    var pages = CM.getPages(),
                        nextPageIndex = pages.indexOf(this) + 1,
                        nextPage = pages[nextPageIndex];

                    if (nextPage) {
                        nextPage.render();
                    }
                });
            }
        });

        // render the first page, and notify the caller when it is ready
        var pages = CM.getPages(),
            pageCount = pages.length,
            firstPage = pages[0],
            deferred = $.Deferred();

        firstPage.once('loaded', function () {
            deferred.resolve();

            TDS.Dialog.hideProgress();
            firstPage.show();

            var navigability = getNavigability();
            sendNavUpdate(navigability);
        });

        firstPage.render();

        return deferred.promise();
    }

    //function that is passed to Blackbox.changeAccommodations to modify the accommodations
    //in our case we just want to clear out any accommodations that are set.
    function clearAccommodations(accoms) {
        accoms.clear()
    }

    //parses any accommodations from the token, and sets them on the Blackbox.
    function setAccommodations(token) {
        var parsed = JSON.parse(token);
        //Call changeAccommodations once to reset all accommodations to their default values
        Blackbox.changeAccommodations(clearAccommodations);
        if(parsed.hasOwnProperty('accommodations')) {
            Blackbox.setAccommodations(parsed['accommodations']);
            //Call changeAccommodations a second time to apply the new accommodations that were set
            //by setAccommodations
            Blackbox.changeAccommodations(function(accoms){})
        }
    }

    function setReadOnly(readOnly){
        if(readOnly !== null){
            CM.setReadOnly(Boolean(readOnly));                    
        }
    }

    function scrollToItem(scrollToDivId){
        if(scrollToDivId){
            var el = document.getElementById(scrollToDivId);
            if(el){
                 el.scrollIntoView();
             }
         }
    }

    function loadToken(vendorId, token, readOnly, scrollToDivId) {
       var deferred = $.Deferred();
       setReadOnly(readOnly);

       blackBoxReady.then(function(){
           loadContentPromise(vendorId, token)
               .then(function(value) {
                   scrollToItem(scrollToDivId);
                   deferred.resolve(value)
               })
               .catch(function(error){
                   var errorMsg = "error: " + error + " with loading token " + token;
                   console.log(errorMsg);
                   deferred.reject(errorMsg);
               }
           );
       });
       return deferred;
    }

    var loadContentPromise = function(vendorId, token){
        return new Promise(
            function(resolve, reject ) {
                Messages.set('TDS.WordList.illustration', 'Illustration', 'ENU');
                Messages.set('TDS.WordList.burmeseGlossary', 'Burmese Glossary', 'ENU');
                Messages.set('TDS.WordList.somaliGlossary', 'Somali Glossary', 'ENU');
                Messages.set('TDS.WordList.hmongGlossary', 'Hmong Glossary', 'ENU');
                TDS.Dialog.showProgress();
                setAccommodations(token);
                var url = irisUrl + '/Pages/API/content/load?id=' + vendorId;
                $.post(url, token, null, 'xml').then(function (data) {
                    loadContent(data).then(resolve);
                }).fail(function (xhr, status, error){
                    TDS.Dialog.hideProgress();
                    reject(error);
                });
            }
        );

    };

    function loadGroupedContentToken(vendorId, token) {
        var deferred = $.Deferred();
        blackBoxReady.then(function(){
            return loadGroupedContentTokenPromise(vendorId, token)
                .then(function(value) {
                    deferred.resolve(value);
                })
                .catch(function(error){
                    var errorMsg = "error: " + error + " with loading token " + token;
                    console.log(errorMsg);
                    deferred.reject(errorMsg);
                }
            );
        });
        return deferred;
    }

      var loadGroupedContentTokenPromise = function(vendorId, token){
        return new Promise(
            function(resolve, reject ) {
                TDS.Dialog.showProgress();
                setAccommodations(token);
                var url = location.href + '/Pages/API/content/loadContent?id=' + vendorId;
                $.post(url, token, null, 'json').then(function (data) {
                     loadGroupedContent(data).then(resolve);
                }).fail(function (xhr, status, error){
                    TDS.Dialog.hideProgress();
                    reject(error);
                });
            }
        );
    };

    var blackBoxReady = new Promise(
        function(resolve){
            if(isIrisReady){
                resolve(true);
            }else{
                Blackbox.events.on('ready', function () {
                    resolve(true);
                });
            }
        }
    );

    function setItemResponse(item, response) {
        if (item && item instanceof ContentItem) {
            itemResponseReady(item)
                .then(function(){
                    item.setResponse(response)
                })
                .catch(function(err){
                    console.error(err)
                });
        } else {
            throw new Error('invalid item; could not set response');
        }
    }

    function setItemLabel(item, label) {
        if (item && item instanceof ContentItem) {
            item.setQuestionLabel(label);
        } else {
            throw new Error('invalid item; could not set label');
        }
    }

    function setResponse(value) {
        var entity = CM.getCurrentPage().getActiveEntity();
        //Begin Hack: 1327 remove <p> from response with prev/next button
        while(value.search("&amp;")!=-1) // '&' comes as '&amp;amp;' in response
        	value = value.replace("&amp;", "&"); 
		value = $('<div/>').html(value).text();
		while(value.search("<p>")!=-1)
			value = value.replace ("<p>", "");
		while(value.search("</p>")!=-1)
			value = value.replace ("</p>", "</br>");
		//End Hack
        setItemResponse(entity, value);
    }

    function setResponses(itemResponses) {
        var items = CM.getCurrentPage().getItems();
        itemResponses.forEach(function (itemResponse) {
            var itemFromPosition, itemFromId;
            if (typeof itemResponse.position === 'number') {
                itemFromPosition = items[itemResponse.position - 1];
            }

            if (itemResponse.id) {
                itemFromId = items.filter(function (item) {
                    var itemId = getItemId(item);
                    return itemId === itemResponse.id;
                })[0];
            }

            if (itemFromPosition && itemFromId && itemFromPosition !== itemFromId) {
                throw new Error('item position and id do not match');
            }

            if (typeof itemResponse.response !== "undefined") {
                setItemResponse( itemFromPosition || itemFromId, itemResponse.response );
            }

            if (itemResponse.label) {
                setItemLabel(itemFromPosition || itemFromId, itemResponse.label);
            }
        });
    }

    //Checks if item response can be set otherwise waits for fired widget instanceReady event
    var itemResponseReady = function(item){
        return new Promise(
            function(resolve, reject){
                if(item && CKEDITOR){
                    if(item.isResponseAvailable()){
                        resolve(true);
                    }else{
                        CKEDITOR.on('instanceReady', function(){
                            resolve(true);});
                    }
                }else{
                    reject('item and/or editor cannot be undefined');
                }
            }
        );
    }

    function getResponse() {
        var entity = CM.getCurrentPage().getActiveEntity();
        if (entity instanceof ContentItem) {
            return entity.getResponse().value;
        }
        return null;
    }

    function getIndexOfCurrentPage() {
        var currentPage = CM.getCurrentPage(),
            pages = CM.getPages(),
            index = pages.indexOf(currentPage);
    }

    function getNavigability() {
        var n = {
            pages: null,
            index: 0,

            haveNextPage: false,
            haveNextPaginatedItem: false,

            havePrevPage: false,
            havePrevPaginatedItem: false,

            update: function () {
                this.haveNextPage = false;
                this.haveNextPaginatedItem = false;
                this.havePrevPage = false;
                this.havePrevPaginatedItem = false;

                var currentPage = CM.getCurrentPage();

                this.pages = this.pages || CM.getPages(),
                this.index = this.pages.indexOf(currentPage);

                this.haveNextPage = this.index < this.pages.length - 1;
                this.havePrevPage = this.index > 0;

                var pagination = currentPage.plugins.get('pagination');

                if (pagination) {
                    this.haveNextPaginatedItem = pagination.haveNext();
                    this.havePrevPaginatedItem = pagination.havePrev();
                }
            }
        };

        n.update();

        return n;
    }

    function go(direction) {
        var n = getNavigability();

        // too easy to take wrong branch if we use terse logic here,
        // so we'll just use the clear version

        if (direction === 'next' && n.haveNextPaginatedItem) {
            CM.requestNextPage()
        }
        else if (direction === 'prev' && n.havePrevPaginatedItem) {
            CM.requestPreviousPage();
        }
        else if (direction === 'next' && n.haveNextPage) {
            n.pages[++n.index].show();
        }
        else if (direction === 'prev' && n.havePrevPage) {
            n.pages[--n.index].show();
        }

        n.update();
        sendNavUpdate(n);
    }

    function showNext() {
        go('next');
    }

    function showPrev() {
        go('prev');
    }

    function sendNavUpdate(navigability) {
        var n = navigability;
        XDM(window.parent).post('IRiS:navUpdate',
            n.havePrevPage || n.havePrevPaginatedItem,
            n.haveNextPage || n.haveNextPaginatedItem
        );
    }


    XDM.addListener('IRiS:loadToken', loadToken);
    XDM.addListener('IRiS:loadContent', loadGroupedContentToken);
    XDM.addListener('IRiS:getResponse', getResponse);
    XDM.addListener('IRiS:setResponse', setResponse);
    XDM.addListener('IRiS:setResponses', setResponses);
    XDM.addListener('IRiS:setReadOnly', setReadOnly);
    XDM.addListener('IRiS:scrollToItem', scrollToItem);
    XDM.addListener('IRiS:showNext', showNext);
    XDM.addListener('IRiS:showPrev', showPrev);
    XDM.addListener('IRiS:TTS', TTS.Manager);

    Blackbox.events.on('ready', function () {
        Blackbox.fireEvent('IRiS:Ready')
        isIrisReady = true;
    });

})(window.Util.XDM, window.ContentManager);
