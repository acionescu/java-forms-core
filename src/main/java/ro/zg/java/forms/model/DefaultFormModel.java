/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.java.forms.model;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Map;

import ro.zg.java.forms.ComponentConfig;
import ro.zg.java.forms.ComponentTypes;
import ro.zg.java.forms.FormComponent;
import ro.zg.java.forms.FormConfig;
import ro.zg.java.forms.FormElement;
import ro.zg.java.forms.FormLayoutConstraints;
import ro.zg.java.forms.event.FormUiEventListener;
import ro.zg.java.forms.event.ValueChangedEvent;

public class DefaultFormModel extends ObjectFormModel {

    @Override
    public void construct() throws Exception {
//	if (dataObject == null && formConfig.getDataType() != null) {
//	    setDataObject(createObjectForDataType(formConfig.getDataType()));
//	}
	initFormData();
	Collection<FormElement> elements = formConfig.getElements().values();
	if (formConfig.getElements() == null) {
	    return;
	}
	initConstraints();
	setUpBorder();
	int count = 0;
	for (Map.Entry<String, FormElement> entry : formConfig.getElements().entrySet()) {
	    FormElement e = entry.getValue();
	    e.setId(entry.getKey());
	    e.setFieldName(entry.getKey());
	    // if( count >= (elements.size()-1) && formConfig.isLast()){
	    // constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	    // }
	    addComponentForField(e);
	    count++;
	}

    }

    private void initConstraints() {
	constraints = new FormLayoutConstraints();
	constraints.anchor = FormLayoutConstraints.NORTHWEST;
	constraints.weightx = 1.0;
	constraints.fill = FormLayoutConstraints.BOTH;
    }

    private void addComponentForField(FormElement fi) throws Exception {
	String fieldName = fi.getFieldName();
	Object value = null;

	boolean isSimpleField = fi instanceof FormComponent;
	FormComponent sfi = null;
	if (isSimpleField) {
	    sfi = (FormComponent) fi;
	    isSimpleField = sfi.getUiType() != null;
	}

	/* add label for this field */
	ComponentConfig config = null;
	if (formConfig.isShowLabels() && isSimpleField && !sfi.getUiType().equals(ComponentTypes.BUTTON)) {
	    String label = fi.getLabel();
	    if (label == null) {
		label = fi.getFieldName();
	    }
	    config = new ComponentConfig(ComponentTypes.LABEL, label, null);
	    if (isSimpleField) {
		// constraints.gridwidth = FormLayoutConstraints.RELATIVE;
		initConstraints();
	    } else {
		constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	    }
	    ui.addComponent(config, constraints);
	}

	// if (formConfig.isShowLabels()) {
	// constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	// }

	if (isSimpleField) {
	    if (fi.getActionName() != null) {
		addActionComponent(sfi);
	    } else if (fieldName != null) {
		if (sfi.isBoundToField()) {
		    value = getValueForProperty(fieldName);
		} else {
//		    value = getSourceData(fieldName);
		    value = dataContext.getValue(fieldName);
		}
		addElement(sfi, value);
	    }

	} else {
	    if (fi.isBoundToField()) {
		value = getValueForProperty(fieldName);
		 if (value == null && fi.getDataType() != null) {
		    value = createObjectForDataType(fi.getDataType());
		    setValueForProperty(fieldName, value);
		}
	    }
	    // ui.addSeparator(constraints);
	    addSubform((FormConfig) fi, value, null);
	}
	if (formConfig.isShowLabels()) {
	    addEndLine();
	}
    }

    private void addEndLine() {
	constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	constraints.weightx = 0.0;
	// ui.addVerticalSeparator(constraints);
	ui.addEmptySpace(constraints);
	constraints.weightx = 1.0;
    }

    class ValueChangedListener implements FormUiEventListener {
	private String propertyName;

	public ValueChangedListener(String name) {
	    propertyName = name;
	}

	public void onEvent(Object event) {
	    ValueChangedEvent vce = (ValueChangedEvent) event;
//	    try {
//		ReflectionUtility.setValueToField(dataObject, propertyName, vce.getValue());
//	    } catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
	    formData.setValue(propertyName, vce.getValue());
	}

    }

    @Override
    public void onNestedValueChanged(PropertyChangeEvent event) {
//	 System.out.println("Default: " + event.getOldValue() + "->" + event.getNewValue() + " for "
//	 + event.getPropertyName());
	String propName = event.getPropertyName();
	if (propName.contains("<dataObject>")) {
	    int index = propName.lastIndexOf(".");
	    if (index >= 0) {
		propName = propName.substring(0, index);
		/* if this is the first level property, update it */
		if (propName.indexOf(".") < 0) {
//		    setValueForProperty(propName, event.getNewValue());
		    FormData formData = (FormData)event.getNewValue();
		    if(formData != null) {
			setValueForProperty(propName, formData.asObject());
		    }
		    else {
			setValueForProperty(propName, null);
		    }
		}
	    }
	}

    }
}
