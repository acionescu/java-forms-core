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

public class ConfigParam {
    private String name;
    private String value;
    private String source;
    private String sourceField;
    private Object reference;
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }
    /**
     * @return the sourceField
     */
    public String getSourceField() {
        return sourceField;
    }
    /**
     * @return the reference
     */
    public Object getReference() {
        return reference;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }
    /**
     * @param sourceField the sourceField to set
     */
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }
    /**
     * @param reference the reference to set
     */
    public void setReference(Object reference) {
        this.reference = reference;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((reference == null) ? 0 : reference.hashCode());
	result = prime * result + ((source == null) ? 0 : source.hashCode());
	result = prime * result + ((sourceField == null) ? 0 : sourceField.hashCode());
	result = prime * result + ((value == null) ? 0 : value.hashCode());
	return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ConfigParam other = (ConfigParam) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (reference == null) {
	    if (other.reference != null)
		return false;
	} else if (!reference.equals(other.reference))
	    return false;
	if (source == null) {
	    if (other.source != null)
		return false;
	} else if (!source.equals(other.source))
	    return false;
	if (sourceField == null) {
	    if (other.sourceField != null)
		return false;
	} else if (!sourceField.equals(other.sourceField))
	    return false;
	if (value == null) {
	    if (other.value != null)
		return false;
	} else if (!value.equals(other.value))
	    return false;
	return true;
    }
    
    
}
