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
package ro.zg.java.forms;


public class FormElement {
    private String id;
    private String fieldName;
    private String label;
    private String labelResourceId;
    private boolean editable = true;
    private String source;
    /**
     * Specifies the property name of the source object to be used to populate an element
     * This is useful for populating maps where the source is a list of objects
     * and the keys an values represent properties of those objects
     */
    private String auxiliarySourceProperty;
    private String sourceField;
    private String dataType;
    private String actionName;
    /** 
     * specifies that this element is actually the representation of the specified field name
     * This means that when the information in the ui changes the field is updated
     */
    private boolean boundToField = true;
    /**
     * this element will not be displayed if true
     */
    private boolean hidden = false;
    
    public String getId() {
        return id;
    }
    public String getFieldName() {
        return fieldName;
    }
    public boolean isEditable() {
        return editable;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    public String getLabel() {
        return label;
    }
    public String getLabelResourceId() {
        return labelResourceId;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setLabelResourceId(String labelResourceId) {
        this.labelResourceId = labelResourceId;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public boolean isBoundToField() {
        return boundToField;
    }
    public void setBoundToField(boolean boundToField) {
        this.boundToField = boundToField;
    }
    public String getSourceField() {
        return sourceField;
    }
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }
    public String getActionName() {
        return actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    
    public String getAuxiliarySourceProperty() {
        return auxiliarySourceProperty;
    }
    public void setAuxiliarySourceProperty(String nestedSourceProperty) {
        this.auxiliarySourceProperty = nestedSourceProperty;
    }
    
    
    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }
    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
	result = prime * result + ((auxiliarySourceProperty == null) ? 0 : auxiliarySourceProperty.hashCode());
	result = prime * result + (boundToField ? 1231 : 1237);
	result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
	result = prime * result + (editable ? 1231 : 1237);
	result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
	result = prime * result + (hidden ? 1231 : 1237);
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((label == null) ? 0 : label.hashCode());
	result = prime * result + ((labelResourceId == null) ? 0 : labelResourceId.hashCode());
	result = prime * result + ((source == null) ? 0 : source.hashCode());
	result = prime * result + ((sourceField == null) ? 0 : sourceField.hashCode());
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
	FormElement other = (FormElement) obj;
	if (actionName == null) {
	    if (other.actionName != null)
		return false;
	} else if (!actionName.equals(other.actionName))
	    return false;
	if (auxiliarySourceProperty == null) {
	    if (other.auxiliarySourceProperty != null)
		return false;
	} else if (!auxiliarySourceProperty.equals(other.auxiliarySourceProperty))
	    return false;
	if (boundToField != other.boundToField)
	    return false;
	if (dataType == null) {
	    if (other.dataType != null)
		return false;
	} else if (!dataType.equals(other.dataType))
	    return false;
	if (editable != other.editable)
	    return false;
	if (fieldName == null) {
	    if (other.fieldName != null)
		return false;
	} else if (!fieldName.equals(other.fieldName))
	    return false;
	if (hidden != other.hidden)
	    return false;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (label == null) {
	    if (other.label != null)
		return false;
	} else if (!label.equals(other.label))
	    return false;
	if (labelResourceId == null) {
	    if (other.labelResourceId != null)
		return false;
	} else if (!labelResourceId.equals(other.labelResourceId))
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
	return true;
    }
    
}
