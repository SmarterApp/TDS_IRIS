/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *       
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.irisshared.repository;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import TDS.Shared.Exceptions.ReturnStatusException;
import TDS.Shared.Messages.IMessageRepository;
import TDS.Shared.Messages.MessageDTO;

@Component
@Scope ("prototype")
public class MessageRepository implements IMessageRepository
{

  @Override
  public List<MessageDTO> getMessages (String language, String contextlist) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getMessage (String contextType, String context, String appKey, String language) throws ReturnStatusException {
    // TODO Auto-generated method stub
    return null;
  }

}
