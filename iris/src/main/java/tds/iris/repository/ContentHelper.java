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
import tds.iris.abstractions.repository.ContentException;
import tds.iris.abstractions.repository.IContentBuilder;
import tds.iris.abstractions.repository.IContentHelper;
import tds.iris.web.data.ContentRequest;
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
        ContentWatcher contentThread = SpringApplicationContext.getBean("contentWatcher", ContentWatcher.class);
        contentThread.start();

    }

    @Override
    public ItemRenderGroup loadRenderGroup(ContentRequest contentRequest) {
        //watch the directory for changes to the files


        String id = "Page-" + UUID.randomUUID().toString();
        ItemRenderGroup itemRenderGroup = new ItemRenderGroup(id, "default", "ENU");

        // load passage
        boolean reloadPassage = true;
        if (contentRequest.getPassage() != null) {
            String requestedPassageId = contentRequest.getPassage().getId();
            // we will not attempt to load a passage
            // if we already have a passage as part of the request or if we have
            // been explicitly asked not to load a passage
            if (!StringUtils.isEmpty(requestedPassageId)) {
                itemRenderGroup.setPassage(_contentBuilder.getITSDocument(requestedPassageId));
                reloadPassage = false;
            } else if (!contentRequest.getPassage().getAutoLoad()) {
                reloadPassage = false;
            }
        }

        if (contentRequest.getItems() != null) {
            long stimulusKey = 0;
            long bankKey = 0;

            for (ContentRequestItem item : contentRequest.getItems()) {
                IITSDocument document = _contentBuilder.getITSDocument(item.getId());
                if (document != null) {
                    IItemRender itemRender = new ItemRender(document, (int) document.getItemKey());
                    itemRender.setResponse(item.getResponse());
                    itemRenderGroup.add(itemRender);

                    if (stimulusKey == 0 && document.getStimulusKey() > 0) {
                        // set to the first non-zero stimulus
                        stimulusKey = document.getStimulusKey();
                        bankKey = document.getBankKey();
                    }
                }
            }

            if (reloadPassage && stimulusKey > 0)
                itemRenderGroup.setPassage(_contentBuilder.getITSDocument(ItsItemIdUtil.getItsDocumentId(bankKey, stimulusKey, ITSEntityType.Passage)));
        }
        return itemRenderGroup;
    }

    public ItemRenderGroup loadRenderGroupAcc(ContentRequest contentRequest, AccLookup accLookup) {
        String id = "Page-" + UUID.randomUUID().toString();
        AccProperties accProperties = new AccProperties(accLookup);
        ItemRenderGroup itemRenderGroup = new ItemRenderGroup(id, "default", accProperties.getLanguage());

        // load passage
        boolean reloadPassage = true;
        if (contentRequest.getPassage() != null) {
            String requestedPassageId = contentRequest.getPassage().getId();
            // we will not attempt to load a passage
            // if we already have a passage as part of the request or if we have
            // been explicitly asked not to load a passage
            if (!StringUtils.isEmpty(requestedPassageId)) {
                itemRenderGroup.setPassage(_contentBuilder.getITSDocumentAcc(requestedPassageId, accLookup));
                reloadPassage = false;
            } else if (!contentRequest.getPassage().getAutoLoad()) {
                reloadPassage = false;
            }
        }

        if (contentRequest.getItems() != null) {
            long stimulusKey = 0;
            long bankKey = 0;

            for (ContentRequestItem item : contentRequest.getItems()) {
                IITSDocument document = _contentBuilder.getITSDocumentAcc(item.getId(), accLookup);
                if (document != null) {
                    IItemRender itemRender = new ItemRender(document, (int) document.getItemKey());

                    itemRender.setDisabled(false);

                    itemRenderGroup.add(itemRender);


                    if (stimulusKey == 0 && document.getStimulusKey() > 0) {
                        // set to the first non-zero stimulus
                        stimulusKey = document.getStimulusKey();
                        bankKey = document.getBankKey();
                    }
                }
            }

            if (reloadPassage && stimulusKey > 0)
                itemRenderGroup.setPassage(_contentBuilder.getITSDocumentAcc(ItsItemIdUtil.getItsDocumentId(bankKey, stimulusKey, ITSEntityType.Passage), accLookup));
        }

        return itemRenderGroup;
    }

    @Override
    public boolean reloadContent() throws ContentException {
        _contentBuilder.init();
        return true;
    }
}
