/**
 * java-forms-core - Support framework to generate java forms
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.java.forms.model;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import net.segoia.java.forms.ComponentConfig;
import net.segoia.java.forms.ComponentTypes;
import net.segoia.java.forms.FormComponent;
import net.segoia.java.forms.FormConfig;
import net.segoia.java.forms.FormElement;
import net.segoia.java.forms.FormLayoutConstraints;
import net.segoia.java.forms.event.FormUiEventListener;

public class ListFormModel extends ObjectFormModel {
    private int currentIndex;
    private FormElement nestedElement;
    private List<Object> sourceData;
    private List<Object> dataList;

    private void initConstraints() {
	constraints = new FormLayoutConstraints();
	constraints.anchor = FormLayoutConstraints.NORTHWEST;
	constraints.weightx = 1.0;
	constraints.fill = FormLayoutConstraints.BOTH;
    }

    public void setDataObject(Object obj) {
	super.setDataObject(obj);
	// dataList = (List)dataObject;
	if (formData != null) {
	    dataList = formData.asList();
	}
    }

    @Override
    public void construct() throws Exception {
	initConstraints();
	setUpBorder();
	/* add labels */

	if (formConfig.getElements() != null) {
	    nestedElement = formConfig.getElements().get("nestedElement");
	}
	if (nestedElement == null) {
	    return;
	}
	if (formConfig.getSource() != null || formConfig.getSourceField() != null) {
	    sourceData = (List) getSourceData(formConfig);
	}
	// if (dataObject == null) {
	if (formData == null) {
	    if (sourceData != null && !formConfig.isEditable()) {
		setDataObject(new ArrayList(sourceData));
	    } else {
		setDataObject(new ArrayList());
	    }
	}

	if (nestedElement instanceof FormConfig) {
	    handleNestedForm((FormConfig) nestedElement);
	} else {
	    handleNestedComponent((FormComponent) nestedElement);
	}

    }

    protected void handleNestedForm(FormConfig fc) throws Exception {
	List<FormElement> elements = new ArrayList(fc.getElements().values());

	if (!fc.isShowLabels()) {
	    for (int i = 0; i < elements.size(); i++) {
		FormElement fi = elements.get(i);
		String label = fi.getLabel();
		if (label == null) {
		    label = fi.getFieldName();
		}
		ComponentConfig cc = new ComponentConfig(ComponentTypes.LABEL, label, null);
		if (i == (elements.size() - 1)) {
		    constraints.gridwidth = FormLayoutConstraints.REMAINDER;
		}
		ui.addComponent(cc, constraints);
	    }
	    ui.addSeparator(constraints);
	} else {
	    constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	}

	for (int i = 0; i < dataList.size(); i++) {
	    currentIndex = i;
	    Object e = dataList.get(i);
	    fc.setFieldName(/* formConfig.getFieldName() + "." + */"" + currentIndex);
	    addSubform(fc, e, ui);

	    if (formConfig.isEditable()) {
		constraints.gridwidth = FormLayoutConstraints.RELATIVE;
	    }

	    if (formConfig.isEditable()) {
		constraints.gridwidth = FormLayoutConstraints.REMAINDER;
		constraints.fill = FormLayoutConstraints.NONE;
		addRemoveButton();
		constraints.fill = FormLayoutConstraints.BOTH;
	    } else {
		constraints.gridwidth = FormLayoutConstraints.REMAINDER;
		constraints.weightx = 0.0;
		ui.addVerticalSeparator(constraints);
		constraints.weightx = 1.0;
	    }
	}
	if (formConfig.isEditable()) {
	    constraints.fill = FormLayoutConstraints.NONE;
	    addAddButton();
	}
    }

    protected void handleNestedComponent(FormComponent fc) {

    }

    // private void addObject(Object o) throws Exception {
    // DefaultFormModel m = new DefaultFormModel();
    // m.setDataObject(o);
    // m.setAuxiliaryData(auxiliaryData);
    // m.setClassInfo(classInfo);
    // m.setUi(ui);
    // FormConfig fc = new FormConfig();
    // fc.setShowLabels(false);
    // m.setFormConfig(fc);
    // m.construct();
    // }

    public void onValueChanged(String propertyName, Object value) {

    }

    protected void onNestedFormConfigChanged(PropertyChangeEvent evt) {
	// update();
	super.onNestedFormConfigChanged(evt);
    }

    private void addAddButton() {
	ComponentConfig cc = new ComponentConfig(ComponentTypes.BUTTON, "Add", null);
	cc.setUiEventListener(new FormUiEventListener() {

	    public void onEvent(Object event) {
		Object newElement = null;
		if (sourceData != null && sourceData.size() > 0) {
		    int nextIndex = dataList.size();
		    if (sourceData.size() > nextIndex) {
			newElement = sourceData.get(nextIndex);
		    } else {
			newElement = sourceData.get(nextIndex % sourceData.size());
		    }
		} else {
		    newElement = createObjectForDataType(nestedElement.getDataType());
		}
		dataList.add(newElement);

		update();
		/*
		 * this was before update, changed because the size was not correct when broadcasted to the listeners
		 */
		propertyChangeSupport.firePropertyChange("" + (dataList.size() - 1), null, newElement);
	    }

	});
	// constraints.weightx = 0.2;
	ui.addComponent(cc, constraints);
	// constraints.weightx = 1.0;
    }

    private void addRemoveButton() {
	ComponentConfig cc = new ComponentConfig(ComponentTypes.BUTTON, "Remove", null);
	cc.setUiEventListener(new RemoveListener(currentIndex));

	ui.addComponent(cc, constraints);
    }

    class RemoveListener implements FormUiEventListener {
	private int index;

	public RemoveListener(int index) {
	    this.index = index;
	}

	public void onEvent(Object event) {
	    Object removedObject = dataList.remove(index);
	    propertyChangeSupport.firePropertyChange("" + index, removedObject, null);
	    update();
	}

    }

}
