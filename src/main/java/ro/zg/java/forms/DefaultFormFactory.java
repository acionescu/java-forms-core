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

import ro.zg.java.forms.model.ObjectFormModel;

public class DefaultFormFactory implements FormFactory{
    private FormUiFactory uiFactory;
    
    public DefaultFormFactory(){
	
    }
    
    public DefaultFormFactory(FormUiFactory uifact){
	uiFactory = uifact;
    }
    
    public Form createForm(FormConfig config) {
	Form f = new Form();
	FormConfig newFormConfig = (FormConfig)config.clone();
	f.setUi(uiFactory.createFormUi());
	f.setFormFactory(this);
	ObjectFormModel model = newFormConfig.getModelFactory().createModel();
	f.setModel(model);
	model.setFormConfig(newFormConfig);
	
	return f;
    }
    public FormUiFactory getUiFactory() {
        return uiFactory;
    }
    public void setUiFactory(FormUiFactory uiFactory) {
        this.uiFactory = uiFactory;
    }
    
}
