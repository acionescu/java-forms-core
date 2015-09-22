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
package net.segoia.java.forms;

import java.util.LinkedHashMap;
import java.util.Map;

import net.segoia.java.forms.model.FormDataFactory;
import net.segoia.java.forms.model.FormModelFactory;

public class FormConfig extends FormElement implements Cloneable {
    private FormModelFactory modelFactory;
    private FormDataFactory formDataFactory;
    private Map<String, FormElement> elements = new LinkedHashMap<String, FormElement>();
    private Map<String, ParamInitConfig> fieldInitConfigs =new LinkedHashMap<String, ParamInitConfig>();
    private Map<String, ConfigParam> configParams = new LinkedHashMap<String, ConfigParam>();

    private boolean showLabels = true;
    private boolean isLast = false;

    public Object clone() {
	try {
	    FormConfig newfe = (FormConfig) super.clone();
	    if (elements != null) {
		newfe.setElements(new LinkedHashMap<String, FormElement>(elements));
	    }
	    if (fieldInitConfigs != null) {
		newfe.setFieldInitConfigs(new LinkedHashMap<String, ParamInitConfig>(fieldInitConfigs));
	    }
	    if(configParams != null){
		newfe.setConfigParams(new LinkedHashMap<String, ConfigParam>(configParams));
	    }
	    return newfe;
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public FormModelFactory getModelFactory() {
	return modelFactory;
    }

    public void setModelFactory(FormModelFactory modelFactory) {
	this.modelFactory = modelFactory;
    }

    public boolean isShowLabels() {
	return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
	this.showLabels = showLabels;
    }

    public Map<String, FormElement> getElements() {
	return elements;
    }

    public void setElements(Map<String, FormElement> elements) {
	this.elements = elements;
    }

    public Map<String, ParamInitConfig> getFieldInitConfigs() {
	return fieldInitConfigs;
    }

    public void setFieldInitConfigs(Map<String, ParamInitConfig> fieldInitConfigs) {
	this.fieldInitConfigs = fieldInitConfigs;
    }

    public boolean isLast() {
	return isLast;
    }

    public void setLast(boolean isLast) {
	this.isLast = isLast;
    }
    

    /**
     * @return the configParams
     */
    public Map<String, ConfigParam> getConfigParams() {
        return configParams;
    }

    /**
     * @param configParams the configParams to set
     */
    public void setConfigParams(Map<String, ConfigParam> configParams) {
        this.configParams = configParams;
    }
    

    /**
     * @return the formDataFactory
     */
    public FormDataFactory getFormDataFactory() {
        return formDataFactory;
    }

    /**
     * @param formDataFactory the formDataFactory to set
     */
    public void setFormDataFactory(FormDataFactory formDataFactory) {
        this.formDataFactory = formDataFactory;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((configParams == null) ? 0 : configParams.hashCode());
	result = prime * result + ((elements == null) ? 0 : elements.hashCode());
	result = prime * result + ((fieldInitConfigs == null) ? 0 : fieldInitConfigs.hashCode());
	result = prime * result + (isLast ? 1231 : 1237);
	result = prime * result + (showLabels ? 1231 : 1237);
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	FormConfig other = (FormConfig) obj;
	if (configParams == null) {
	    if (other.configParams != null)
		return false;
	} else if (!configParams.equals(other.configParams))
	    return false;
	if (elements == null) {
	    if (other.elements != null)
		return false;
	} else if (!elements.equals(other.elements))
	    return false;
	if (fieldInitConfigs == null) {
	    if (other.fieldInitConfigs != null)
		return false;
	} else if (!fieldInitConfigs.equals(other.fieldInitConfigs))
	    return false;
	if (isLast != other.isLast)
	    return false;
	if (showLabels != other.showLabels)
	    return false;
	return true;
    }

}
