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

import java.util.Map;

import ro.zg.java.forms.model.FormDataSource;

public class MapDataSource implements FormDataSource{
    private Map<String,Object> map;
    
    public MapDataSource(Map<String,Object> m) {
	this.map = m;
    }

    public Object getValue(String name) {
	return map.get(name);
    }

    public void init() {
	// TODO Auto-generated method stub
	
    }

}
