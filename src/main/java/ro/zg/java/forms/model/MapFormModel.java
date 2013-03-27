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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ro.zg.java.forms.ComponentConfig;
import ro.zg.java.forms.ComponentTypes;
import ro.zg.java.forms.FormComponent;
import ro.zg.java.forms.FormConfig;
import ro.zg.java.forms.FormElement;
import ro.zg.java.forms.FormLayoutConstraints;
import ro.zg.java.forms.event.FormUiEventListener;
import ro.zg.util.data.ProxyMap;

public class MapFormModel extends ObjectFormModel {
    private int currentIndex;
    /**
     * keeps the associations between the names allocated for the keys and values from the dataMap. This way when the
     * value is changed from the ui we are able to identify the exact object modified and update the dataMap
     */
    private Map<String, Object> referencesMap = new HashMap<String, Object>();

    private List availableKeysFromSource;

    private List keysSource;

    private List valuesSource;

    private Map dataMap;

    private FormElement keyFieldInfo;

    private FormElement valueFieldInfo;

    private FormComponent keyComponent;

    private FormComponent valueComponent;

    private FormConfig keyFormConfig;

    private FormConfig valueFormConfig;
    /**
     * Keeps a reference between a key and the actual source object it was derived from. </br> This can be useful if the
     * same object is used to derive the value or influences the value in some way
     */
    private Map<Object, Object> originalSourceData;

    public MapFormModel() {
    }

    private void initConstraints() {

	constraints = new FormLayoutConstraints();
	constraints.anchor = FormLayoutConstraints.NORTHWEST;
	constraints.weightx = 1.0;
	constraints.fill = FormLayoutConstraints.BOTH;
    }

    public void setDataObject(Object obj) {
	super.setDataObject(obj);
	// dataMap = new ProxyMap((Map) dataObject);
	if (formData != null) {
	    dataMap = new ProxyMap(formData.asMap());
	}
    }

    // public Object getDataObject() {
    // return dataObject;
    // }

    public void setFormConfig(FormConfig fc) {
	super.setFormConfig(fc);
	if (formConfig.getElements() != null) {
	    keyFieldInfo = formConfig.getElements().get("key");
	    valueFieldInfo = formConfig.getElements().get("value");
	    if (keyFieldInfo != null) {
		if (keyFieldInfo instanceof FormComponent) {
		    keyComponent = (FormComponent) keyFieldInfo;
		} else if (keyFieldInfo instanceof FormConfig) {
		    keyFormConfig = (FormConfig) keyFieldInfo;
		}
	    }
	    if (valueFieldInfo != null) {
		if (valueFieldInfo instanceof FormComponent) {
		    valueComponent = (FormComponent) valueFieldInfo;
		} else if (valueFieldInfo instanceof FormConfig) {
		    valueFormConfig = (FormConfig) valueFieldInfo;
		    valueFormConfig.setShowLabels(false);
		}
	    }
	}
    }

    // public void setAuxiliaryData(Map<String, Object> data) {
    // super.setAuxiliaryData(data);
    public void setDataContext(FormDataContext dataContext) {
	super.setDataContext(dataContext);
	updateKeysSource();
    }

    public void setDatasource(FormDataSource ds) {
	super.setDatasource(ds);
	updateKeysSource();
    }

    private void updateKeysSource() {
	if (keyFieldInfo instanceof FormComponent) {
	    FormComponent keyComponent = (FormComponent) keyFieldInfo;
	    if (keyFieldInfo != null && keyComponent.getSource() != null) {
		keysSource = (List) getSourceData(keyComponent);

		if (keysSource != null) {
		    // availableKeysFromSource = new ArrayList(keysSource);
		    deriveAvailableKeysFromSource();
		    if (dataMap != null) {
			/* if the keys from the map, are not included in the keyssource , clear the map */
			if (!availableKeysFromSource.containsAll(dataMap.keySet())) {
			    dataMap.clear();
			}
		    }
		}
	    }
	}
    }

    private void deriveAvailableKeysFromSource() {
	if (keyFieldInfo.getAuxiliarySourceProperty() == null) {
	    availableKeysFromSource = new ArrayList(keysSource);
	} else {
	    /* if the key is a property of the source object extract the possible values */
	    String nestedProp = keyFieldInfo.getAuxiliarySourceProperty();
	    availableKeysFromSource = new ArrayList();
	    originalSourceData = new HashMap<Object, Object>();
	    for (Object sourceObject : keysSource) {
		Object key = getValueForProperty(nestedProp, sourceObject);
		availableKeysFromSource.add(key);
		/* make a link between the key and the original object in order to use it later */
		originalSourceData.put(key, sourceObject);
	    }
	}
    }

    @Override
    public void construct() throws Exception {
	initConstraints();
	setUpBorder();
	String keyLabel = "key";
	String valueLabel = "value";

	if (keyFieldInfo != null && keyFieldInfo.getLabel() != null) {
	    keyLabel = keyFieldInfo.getLabel();
	}
	if (valueFieldInfo != null && valueFieldInfo.getLabel() != null) {
	    valueLabel = valueFieldInfo.getLabel();
	}

	/* add key label */
	ComponentConfig cc = new ComponentConfig(ComponentTypes.LABEL, keyLabel, null);
	// constraints.gridwidth = FormLayoutConstraints.RELATIVE;
	ui.addComponent(cc, constraints);
	/* add value label */
	cc = new ComponentConfig(ComponentTypes.LABEL, valueLabel, null);
	constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	ui.addComponent(cc, constraints);
	currentIndex = 0;

	/* refresh available keys from source */
	getAvailableKeysFromSource();

	if (!formConfig.isEditable()) {
	    int availableKeysCount = availableKeysFromSource.size();
	    for (int i = 0; i < availableKeysCount; i++) {
		Object newKey = getAvailableKey(i);
		Object newValue = getAvailableValue(i);
		if (newKey != null && newValue != null) {
		    dataMap.put(newKey, newValue);
		}
	    }
	}

	for (Object key : dataMap.keySet()) {
	    initConstraints();
	    // constraints.gridwidth = 3;
	    Object value = dataMap.get(key);
	    String keyName = "key" + currentIndex;
	    String valueName = "value" + currentIndex;

	    /* add currentIndex to the auxiliaryData */
	    // auxiliaryData.put("_currentKeyIndex", currentIndex);
	    dataContext.setValue("_currentKeyIndex", currentIndex);

	    // constraints.gridwidth = FormLayoutConstraints.RELATIVE;
	    /* add the component for the key */
	    if (keyComponent != null) {
		List currentAvailableKeys = null;
		if (availableKeysFromSource != null) {
		    currentAvailableKeys = new ArrayList(availableKeysFromSource);
		    currentAvailableKeys.add(0, key);
		}
		// System.out.println("currentAvailableKeys="+currentAvailableKeys);
		ComponentConfig newc = getComponentConfig(keyName, keyComponent.getUiType(), key,
			currentAvailableKeys.toArray());
		addComponent(newc);
		referencesMap.put(keyName, key);
		/*
		 * if the case, set the original source used to derive the key as an auxiliary parameter with the name
		 * 'originalSourceKey'
		 */
		if (originalSourceData != null) {
		    // auxiliaryData.put("originalSourceKey", originalSourceData.get(key));
		    dataContext.setValue("originalSourceKey", originalSourceData.get(key));
		}
	    } else {
		keyFieldInfo.setFieldName(keyName);
		keyFormConfig.setId(keyName);
		addSubform(keyFormConfig, key, null);
	    }
	    if (!formConfig.isEditable()) {
		constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	    }
	    /* add the component for the value */
	    if (valueComponent != null) {
		List sourceValues = (List) getSourceData(valueFieldInfo);
		ComponentConfig valueCompConfig = getComponentConfig(valueName, valueComponent.getUiType(), value,
			sourceValues);
		// valueComponent.setFieldName(valueName);
		// addComponent(valueComponent, value);
		addComponent(valueCompConfig);
		referencesMap.put(valueName, value);
	    } else {
		valueFieldInfo.setFieldName(valueName);
		valueFormConfig.setId(valueName);
		addSubform(valueFormConfig, value, ui);
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
	    /* don't forget to increment the current index */
	    currentIndex++;
	}

	if (checkCanAdd()) {
	    constraints.gridwidth = FormLayoutConstraints.REMAINDER;
	    constraints.fill = FormLayoutConstraints.NONE;
	    addAddButton();
	}
    }

    private boolean checkCanAdd() {
	if (!formConfig.isEditable()) {
	    return false;
	}
	if (keyFieldInfo != null && valueFieldInfo != null) {
	    if (keyFieldInfo.getSource() != null && availableKeysFromSource != null) {
		return (availableKeysFromSource.size() > 0);
	    } else {
		return (keyFieldInfo.getDataType() != null && valueFieldInfo.getDataType() != null);
	    }
	}
	return false;
    }

    private List getAvailableKeysFromSource() {
	if (keysSource != null) {
	    // availableKeysFromSource = new ArrayList(keysSource);
	    deriveAvailableKeysFromSource();
	    /* remove from available keys the ones already in the map */
	    availableKeysFromSource.removeAll(dataMap.keySet());
	} else {
	    availableKeysFromSource = new ArrayList();
	}
	return availableKeysFromSource;

    }

    public void onValueChanged(String propertyName, Object value) {
	// System.out.println("prop change " + propertyName + " " + value);
	if (propertyName.startsWith("key")) {
	    Object key = referencesMap.get(propertyName);
	    if (key != null) {
		Object associatedValue = dataMap.get(key);
		/*
		 * if the key changed maybe the value is not appropriate anymore, so get the new list of values and if
		 * the current value is contained in the list keep it, otherwise the new value becomes the first element
		 * in the list
		 */

		/* first we need to set the current key source object(if there is one) on the context */
		if (originalSourceData != null) {
		    // auxiliaryData.put("originalSourceKey", originalSourceData.get(value));
		    dataContext.setValue("originalSourceKey", originalSourceData.get(value));
		}
		List valueSource = (List) getSourceData(valueFieldInfo);
		if (valueSource != null && valueSource.size() > 0) {
		    if (!valueSource.contains(associatedValue)) {
			associatedValue = valueSource.get(0);
		    }
		}

		Map newDataMap = new LinkedHashMap();
		for (Object ck : dataMap.keySet()) {
		    if (ck.equals(key)) {
			newDataMap.put(value, associatedValue);
		    } else {
			newDataMap.put(ck, dataMap.get(ck));
		    }
		}
		// System.out.println(newDataMap);
		dataMap.clear();
		dataMap.putAll(newDataMap);
	    }

	    // formConfigChangeSupport.firePropertyChange("formConfig", null, formConfig);
	    update();
	} else if (propertyName.startsWith("value")) {
	    String keyName = "key" + propertyName.replace("value", "");
	    Object key = referencesMap.get(keyName);
	    if (key != null) {
		if (value != null) {
		    dataMap.put(key, value);
		}
	    }
	}
    }

    // protected void onNestedFormConfigChanged(PropertyChangeEvent evt) {
    // update();
    // }

    private Object getAvailableKey(int index) {
	if (keysSource != null) {
	    if (availableKeysFromSource.size() > index) {
		return availableKeysFromSource.get(index);
	    }
	} else {
	    String keyNestedType = keyFieldInfo.getDataType();
	    if (keyNestedType != null) {
		return createObjectForDataType(keyNestedType);
	    }
	}
	return null;
    }

    private Object getAvailableValue(int index) {
	if (valuesSource != null && valuesSource.size() > index) {
	    return valuesSource.get(index);
	} else {
	    String valueNestedType = valueFieldInfo.getDataType();
	    if (valueNestedType != null) {
		return createObjectForDataType(valueNestedType);
	    }
	}
	return null;
    }

    private void addAddButton() {
	ComponentConfig cc = new ComponentConfig(ComponentTypes.BUTTON, "Add", null);
	cc.setUiEventListener(new FormUiEventListener() {

	    public void onEvent(Object event) {
		Object key = null;
		Object value = null;
		if (keysSource != null) {
		    // availableKeysFromSource = new ArrayList(keysSource);
		    deriveAvailableKeysFromSource();
		    /* remove from available keys the ones already in the map */
		    availableKeysFromSource.removeAll(dataMap.keySet());
		    if (availableKeysFromSource.size() > 0) {
			key = availableKeysFromSource.get(0);
		    }
		} else {
		    String keyNestedType = keyFieldInfo.getDataType();
		    if (keyNestedType != null) {
			try {
			    key = Class.forName(keyNestedType).newInstance();
			} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    }
		}
		if (key != null) {
		    if (originalSourceData != null) {
			// auxiliaryData.put("originalSourceKey", originalSourceData.get(key));
			dataContext.setValue("originalSourceKey", originalSourceData.get(key));
		    }
		    List valueSource = (List) getSourceData(valueFieldInfo);
		    if (valueSource != null && valueSource.size() > 0) {
			value = valueSource.get(0);
		    } else {
			String valueNestedType = valueFieldInfo.getDataType();
			if (valueNestedType != null) {
			    try {
				value = Class.forName(valueNestedType).newInstance();
			    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			}
		    }
		}

		if (key != null && value != null) {
		    dataMap.put(key, value);

		    // formConfigChangeSupport.firePropertyChange("formConfig", null, formConfig);
		    update();
		} else {
		    ActionEvent ae = (ActionEvent) event;
		    ui.removeComponent(ae.getSource());
		}
	    }
	});
	ui.addComponent(cc, constraints);
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
	    Object key = referencesMap.get("key" + index);
	    if (key != null) {
		dataMap.remove(key);
	    }

	    // formConfigChangeSupport.firePropertyChange("formConfig", null, formConfig);
	    update();
	}

    }

//    @Override
//    public PropertyChangeEvent onNestedValueChanged(PropertyChangeEvent event) {
//	String propertyName = event.getPropertyName();
//	
//	/* if the value has changed, make sure to modify the path with the actual key , 
//	 * in order for the property to be correctly identified */
//	if (propertyName.startsWith("value")) {
//	    String rest="";
//	    int keyIndexEnd = propertyName.length();
//	    int index = propertyName.indexOf(".");
//	    if( index > 0) {
//		rest = propertyName.substring(index);
//		keyIndexEnd=index;
//	    }
//	    String keyName = "key" + propertyName.substring(5,keyIndexEnd);
//	    Object key = referencesMap.get(keyName);
//	    if (key != null) {
//		return new PropertyChangeEvent(event.getSource(), key.toString()+rest,event.getOldValue(),event.getNewValue());
//	    }
//	}
//	
//	return event;
//    }
}
