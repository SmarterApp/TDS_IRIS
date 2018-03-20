/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2014 American Institutes for Research
 *
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.irisshared.repository;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import TDS.Shared.Exceptions.ReturnStatusException;
import tds.student.sql.abstractions.ITisRepository;

@Component
@Scope ("prototype")
public class IrisTisRepository implements ITisRepository
{

  @Override
  public void tisReply (UUID oppkey, Boolean success, String errorMessage) throws ReturnStatusException {
    // Do nothing.
  }

}
