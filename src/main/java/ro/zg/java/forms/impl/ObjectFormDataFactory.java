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
package ro.zg.java.forms.impl;

import java.util.List;
import java.util.Map;

import ro.zg.java.forms.model.FormData;
import ro.zg.java.forms.model.FormDataFactory;
import ro.zg.java.forms.model.ObjectFormData;

public class ObjectFormDataFactory implements FormDataFactory {

    public FormData createFormDataFromList(List l) {
	return new ObjectFormData(l);
    }

    public FormData createFormDataFromMap(Map m) {
	return new ObjectFormData(m);
    }

    public FormData createFormDataFromObject(Object o) {
	return new ObjectFormData(o);
    }

    public FormData createFormDataForType(String type) {
	Object o;
	try {
	    o = Class.forName(type).newInstance();
	} catch (Exception e) {
	    throw new RuntimeException("Failed to create instance for class " + type);
	}
	return new ObjectFormData(o);

    }

}
