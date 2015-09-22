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
package net.segoia.java.forms.impl;

import java.util.HashMap;
import java.util.Map;

import net.segoia.java.forms.model.FormDataContext;
import net.segoia.java.forms.model.FormDataSource;
import net.segoia.util.data.reflection.ReflectionUtility;

public class DefaultFormDataContext extends FormDataContext{
    /**
     * A map to hold local params
     */
    private Map<String,Object> localParams=new HashMap<String, Object>();
    
    public Object getSimpleValue(String name) {
	/* if a local param exists return in */
	if(localParams.containsKey(name)) {
	    return localParams.get(name);
	}
	/* otherwise search the datasource */
	else if(datasource != null) {
	    return datasource.getValue(name);
	}
	return null;
    }
    
    public Object getValue(String sourceName) {
	if (sourceName == null /*|| auxiliaryData == null*/) {
	    return null;
	}
	try {
	    int index = sourceName.indexOf(".");
	    if (index < 0) {
		return getSimpleValue(sourceName);
	    } else {
		Object mainObj = getSimpleValue(sourceName.substring(0, index));
		return getValueForProperty(sourceName.substring(index + 1), mainObj);
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }
    
    protected Object getValueForProperty(String propertyName, Object target) {
	if (target != null) {
	    try {
		return ReflectionUtility.getValueForField(target, propertyName);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	return null;
    }
    
    
    public void setValue(String name, Object value) {
	/* store the value to the local param map */
	localParams.put(name, value);
	
    }
}
