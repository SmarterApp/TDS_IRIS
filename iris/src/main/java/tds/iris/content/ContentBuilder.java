/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.iris.content;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import AIR.Common.Configuration.AppSettingsHelper;
import AIR.Common.Helpers.CaseInsensitiveMap;
import AIR.Common.Utilities.SpringApplicationContext;
import tds.blackbox.ContentRequestException;
import tds.iris.abstractions.repository.ContentException;
import tds.iris.abstractions.repository.IContentBuilder;
import tds.itempreview.ConfigBuilder;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.IrisITSDocument;
import tds.itemrenderer.data.ItsItemIdUtil;
import tds.itemrenderer.data.ITSTypes.ITSEntityType;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Component
@Scope("singleton")
public class ContentBuilder implements IContentBuilder {
    private static final Logger _logger = LoggerFactory.getLogger(ContentBuilder.class);
    private String _contentPath;
    private ConfigBuilder _directoryScanner = null;

    public synchronized void init() throws ContentException {
        try {
            // scan the local folder.
            _contentPath = AppSettingsHelper.get("iris.ContentPath");
            _directoryScanner = new ConfigBuilder(_contentPath);
            _directoryScanner.create();
        } catch (Exception exp) {
            _logger.error("Error loading IRiS content.", exp);
            throw new ContentException(exp);
        }

    }



    @Override
    public IITSDocument getITSDocument(String id) throws ContentRequestException {
        return _directoryScanner.getRenderableDocument(id);
    }

    @Override
    public IITSDocument getITSDocumentAcc(String id, AccLookup accLookup) {
        return _directoryScanner.getRenderableDocument(id, accLookup);
    }
}
