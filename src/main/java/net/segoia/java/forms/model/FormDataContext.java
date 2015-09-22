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

public abstract class FormDataContext {
    /**
     * A {@link FormDataSource} to pull data from
     */
    protected FormDataSource datasource;

    public abstract Object getValue(String name);

    public abstract void setValue(String name, Object value);

    /**
     * @return the datasource
     */
    public FormDataSource getDatasource() {
        return datasource;
    }

    /**
     * @param datasource the datasource to set
     */
    public void setDatasource(FormDataSource datasource) {
        this.datasource = datasource;
    }
    
    

}
