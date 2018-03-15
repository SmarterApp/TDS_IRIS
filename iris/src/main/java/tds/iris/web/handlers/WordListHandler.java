/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2014 American Institutes for Research
 *
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/

package tds.iris.web.handlers;


import AIR.Common.Configuration.AppSettingsHelper;
import TDS.Shared.Exceptions.ReturnStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import tds.irisshared.content.ContentException;
import tds.irisshared.repository.IContentHelper;
import tds.irisshared.content.ContentBuilder;
import tds.itempreview.ConfigBuilder;
import tds.itemrenderer.handler.WordListHandlerBase;


@Controller
@Scope("prototype")
public class WordListHandler extends WordListHandlerBase
{
    private static final Logger     _logger = LoggerFactory.getLogger (ContentBuilder.class);

    @Autowired
    private IContentHelper _contentHelper;

    @Override
    protected String getItemPath (long bankKey, long itemKey) throws ReturnStatusException {
        try {
            String id = _contentHelper.getItemFormattedId(bankKey, itemKey);
            return _contentHelper.getITSDocument(id).getBaseUri();
        } catch (Exception exp) {
            _logger.error ("Error loading IRiS content.", exp);
            throw new ContentException(exp);
        }
    }
}
