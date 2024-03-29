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

import net.segoia.java.forms.event.FormUiEventListener;

public class ComponentConfig {
    /**
     * The data displayed by this component
     */
    private Object value;
    /**
     * The name of the field representing the data
     */
    private String name;
    /**
     * Component type @see {@link ComponentTypes}
     */
    private String type;
    /**
     * The source object to populate a complex component (e.g Combobox)
     */
    private Object source;
    
    private FormUiEventListener uiEventListener;
    
    
    public ComponentConfig(){
	
    }
    
    public ComponentConfig(String type,Object value,String name){
	this.type = type;
	this.value = value;
	this.name = name;
    }
    
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FormUiEventListener getUiEventListener() {
        return uiEventListener;
    }

    public void setUiEventListener(FormUiEventListener uiEventListener) {
        this.uiEventListener = uiEventListener;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    
}
