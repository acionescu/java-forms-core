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

import java.util.List;
import java.util.Map;

import net.segoia.util.data.reflection.ReflectionUtility;

public class ObjectFormData implements FormData{
    private Object object;
    
    public ObjectFormData(Object o) {
	if(o==null) {
	    throw new IllegalArgumentException("object cannot be null");
	}
	this.object = o;
    }
    
    public Object getType() {
	return object.getClass();
    }

    public Object getValue(String name) {
	try {
	    return ReflectionUtility.getValueForField(object,name);
	} catch (Exception e) {
	   throw new RuntimeException(e);
	}
    }

    public void setValue(String name, Object value) {
	try {
	    ReflectionUtility.setValueToField(object, name, value);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	
    }

    public Object asObject() {
	return object;
    }

    public List asList() {
	if(object instanceof List) {
	    return (List)object;
	}
	throw new UnsupportedOperationException("Cannot cast object "+object +" to List");
    }

    public Map asMap() {
	if(object instanceof Map) {
	    return (Map)object;
	}
	throw new UnsupportedOperationException("Cannot cast object "+object +" to Map");
    }

}
