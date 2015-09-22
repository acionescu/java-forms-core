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

import java.beans.PropertyChangeListener;
import java.util.Map;

import net.segoia.java.forms.model.FormDataContext;
import net.segoia.java.forms.model.FormDataSource;
import net.segoia.java.forms.model.ObjectFormModel;

public class Form {
    private String id;
    private FormUi ui;
    private ObjectFormModel model;
    private FormFactory formFactory;

    public Form() {

    }

    public void setDataObject(Object dataObject) {
	model.setDataObject(dataObject);
    }

//    public void setAuxiliaryData(Map<String,Object> ad){
//	model.setAuxiliaryData(ad);
//    }
    
    public void setFormDataSource(FormDataSource fds) {
	model.setDatasource(fds);
    }
    
    public FormDataSource getFormDataSource() {
	return model.getDataContext().getDatasource();
    }
    
    public FormDataContext getFormDataContext() {
	return model.getDataContext();
    }
    
    public void setDataContext(FormDataContext fdc) {
	model.setDataContext(fdc);
    }
    
    public void construct() throws Exception {
	model.construct();
    }
    
    public void initialize() throws Exception{
	model.init();
	model.construct();
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
	model.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
	model.removePropertyChangeListener(pcl);
    }

    public void setModel(ObjectFormModel model) {
	this.model = model;
	model.setUi(ui);
	model.setOwner(this);
    }

    public FormUi getUi() {
	return ui;
    }

    public void setUi(FormUi ui) {
	this.ui = ui;
	if (model != null) {
	    model.setUi(ui);
	}
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public ObjectFormModel getModel() {
	return model;
    }

    public FormFactory getFormFactory() {
	return formFactory;
    }

    public void setFormFactory(FormFactory formFactory) {
	this.formFactory = formFactory;
    }
    
}
