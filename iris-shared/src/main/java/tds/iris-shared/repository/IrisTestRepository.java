/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2014 American Institutes for Research
 *
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.iris.repository;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import TDS.Shared.Exceptions.ReturnStatusException;
import tds.student.sql.abstractions.ITestRepository;

@Component
@Scope ("prototype")
public class IrisTestRepository implements ITestRepository
{
  @Override
  public String getTrTestId (String testeeId, String testKey) throws ReturnStatusException {
    return String.format ("{Testeeid: %s,  TestKey: %s}", testeeId, testKey);
  }
}
