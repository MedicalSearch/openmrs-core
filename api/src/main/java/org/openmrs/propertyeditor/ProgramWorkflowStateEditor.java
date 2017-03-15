/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Allows for serializing/deserializing an object to a string so that Spring knows how to pass
 * an object back and forth through an html form or other medium. <br>
 * <br>
 * In version 1.9, added ability for this to also retrieve objects by uuid
 * 
 * @see ProgramWorkflowState
 */
public class ProgramWorkflowStateEditor extends PropertyEditorSupport {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public ProgramWorkflowStateEditor() {
	}
	
	/**
	 * @should set using id
	 * @should set using uuid
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		ProgramWorkflowService pws = Context.getProgramWorkflowService();
		if (StringUtils.hasText(text)) {
			try {
				setValue(pws.getStateByUuid(text));
			}
			catch (Exception ex) {
				ProgramWorkflowState s = pws.getStateByUuid(text);
				setValue(s);
				if (s == null) {
					log.error("Error setting text" + text, ex);
					throw new IllegalArgumentException("Program Workflow State not found: " + ex.getMessage());
				}
			}
		} else {
			setValue(null);
		}
	}
	
	@Override
	public String getAsText() {
		ProgramWorkflowState pws = (ProgramWorkflowState) getValue();
		if (pws == null) {
			return "";
		} else {
			return pws.getProgramWorkflowStateId().toString();
		}
	}
	
}
