/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright(c) 2014 American Institutes for Research
 *
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.iris.repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import AIR.Common.Utilities.SpringApplicationContext;
import tds.blackbox.ContentRequestException;
import tds.iris.abstractions.repository.ContentException;
import tds.iris.abstractions.repository.IContentBuilder;
import tds.iris.abstractions.repository.IContentHelper;
import tds.iris.web.data.ContentRequest;
import tds.iris.web.data.ContentRequestEntity;
import tds.iris.web.data.ContentRequestItem;
import tds.itemrenderer.data.*;
import tds.itemrenderer.data.ITSTypes.ITSEntityType;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Component
@Scope("singleton")
public class ContentHelper implements IContentHelper {
    private static final Logger _logger = LoggerFactory.getLogger(ContentHelper.class);
    private IContentBuilder _contentBuilder;

    @PostConstruct
    public synchronized void init() throws ContentException {
        _contentBuilder = SpringApplicationContext.getBean("iContentBuilder", IContentBuilder.class);
        reloadContent();
    }

    private ItemRenderGroup getItemRenderGroup(List<ContentRequestItem> items,
                                               AccLookup accLookup,
                                               String passageId, boolean setPassage,
                                               boolean setResponse, boolean setDisabled) {
        String id = "Page-" + UUID.randomUUID().toString();

        AccProperties accProperties = new AccProperties(accLookup);
        ItemRenderGroup itemRenderGroup = new ItemRenderGroup(id, "default", accProperties.getLanguage());
        boolean didSetPassage = false;

        if(setPassage && StringUtils.isNotEmpty(passageId)){
            itemRenderGroup.setPassage(_contentBuilder.getITSDocumentAcc(passageId, accLookup));
            didSetPassage = true;
        }

        for (ContentRequestItem item : items) {
            IITSDocument document = _contentBuilder.getITSDocumentAcc(item.getId(), accLookup);
            if (document != null) {
                IItemRender itemRender = new ItemRender(document, (int) document.getItemKey());
                if (setResponse) {
                    itemRender.setResponse(item.getResponse());
                }

                itemRender.setDisabled(setDisabled);

                itemRenderGroup.add(itemRender);
                if (setPassage && !didSetPassage && document.getStimulusKey() > 0) {
                    // set to the first non-zero stimulus
                    String stimulusKey = getStimulusFormattedId(document.getBankKey(), document.getStimulusKey());
                    itemRenderGroup.setPassage(_contentBuilder.getITSDocumentAcc(stimulusKey, accLookup));
                }
            }
        }

        return itemRenderGroup;
    }

    public ItemRenderGroup loadTutorial(long bankKey, long itemKey, String language) {
        String id = "Page-" + UUID.randomUUID().toString();
        ItemRenderGroup itemRenderGroup = new ItemRenderGroup(id, "default", language);
        String itemId = getItemFormattedId(bankKey, itemKey);
        IITSDocument document = _contentBuilder.getITSDocument(itemId);

        if (document != null) {
            IItemRender itemRender = new ItemRender(document, (int) document.getItemKey());
            itemRenderGroup.add(itemRender);
        }
        return itemRenderGroup;
    }

    public ItemRenderGroup loadRenderGroupAcc(ContentRequest contentRequest, AccLookup accLookup) throws ContentException {
        String passageId = "";
        boolean setPassage = true;
        if(StringUtils.isNotEmpty(contentRequest.getLocationPath())){
            loadContent(contentRequest.getLocationPath());
        }

        if (contentRequest.getPassage() != null) {
            passageId = contentRequest.getPassage().getId();
             if (!contentRequest.getPassage().getAutoLoad()) {
                setPassage = false;
            }
        }

        return getItemRenderGroup(contentRequest.getItems(), accLookup, passageId, setPassage, true, false );
    }

    @Override
    public ItemRenderGroup loadRenderGroup(ContentRequest contentRequest) {
        //watch the directory for changes to the files
        AccLookup accLookup = new AccLookup();
        accLookup.add("Language", "ENU");
        return loadRenderGroupAcc(contentRequest, accLookup);
    }

    @Override
    //reload all of the content within the directory
    public boolean reloadContent() throws ContentException {
        _contentBuilder.init();
        return true;
    }

    public boolean loadContent(String locationPath) throws ContentException {
        _contentBuilder.loadFile(locationPath);
        return true;
    }

    //add the file that was created to the watched directory
    public void addFile(String fileName) {
        _contentBuilder.loadFile(fileName);
    }

    //remove the file that was deleted from the watched directory
    public void removeFile(String fileName) {
        _contentBuilder.removeFile(fileName);
    }

    public IITSDocument getITSDocument(String id) throws ContentRequestException{
        return _contentBuilder.getITSDocument(id);
    }
    public IITSDocument getITSDocument(String id, AccLookup accLookup) throws ContentRequestException{
        return _contentBuilder.getITSDocumentAcc(id, accLookup);
    }

    public String getItemFormattedId(long bankKey, long itemKey){
       return ItsItemIdUtil.getItsDocumentId(bankKey, itemKey, ITSEntityType.Item);

    }

    public String getStimulusFormattedId(long bankKey, long itemKey){
        return ItsItemIdUtil.getItsDocumentId(bankKey, itemKey, ITSEntityType.Passage);

    }

}