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
package ro.zg.java.forms.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ro.zg.java.forms.ComponentConfig;
import ro.zg.java.forms.Form;
import ro.zg.java.forms.FormComponent;
import ro.zg.java.forms.FormConfig;
import ro.zg.java.forms.FormControlComponent;
import ro.zg.java.forms.FormElement;
import ro.zg.java.forms.FormLayoutConstraints;
import ro.zg.java.forms.FormUi;
import ro.zg.java.forms.ParamInitConfig;
import ro.zg.java.forms.event.FormUiEventListener;
import ro.zg.java.forms.event.ValueChangedEvent;
import ro.zg.java.forms.impl.DefaultFormDataContext;
import ro.zg.java.forms.impl.ObjectFormDataFactory;
import ro.zg.util.data.reflection.ReflectionUtility;

public abstract class ObjectFormModel implements Cloneable {
    // /**
    // * The object that this form represents
    // */
    // protected Object dataObject;

    protected FormDataContext dataContext = new DefaultFormDataContext();

    protected FormData formData;
    /**
     * The data used to populate components
     */
    // protected Map<String, Object> auxiliaryData = new HashMap<String, Object>();
    protected FormUi ui;
    protected Form owner;
    protected Map<String, Form> subforms = new HashMap<String, Form>();
    protected FormConfig formConfig;
    protected FormLayoutConstraints constraints = new FormLayoutConstraints();

    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected PropertyChangeSupport formConfigChangeSupport;
    protected ActionListener actionListener;
    private FormDataFactory formDataFactory = new ObjectFormDataFactory();

    public ObjectFormModel() {
	formConfigChangeSupport = new PropertyChangeSupport(this);
    }

    public Object clone() {
	try {
	    return super.clone();
	} catch (CloneNotSupportedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    public void init() {
	// if (dataObject == null && formConfig.getDataType() != null) {
	// setDataObject(createObjectForDataType(formConfig.getDataType()));
	// }
	initFormData();

	Map<String, ParamInitConfig> fieldConfigs = formConfig.getFieldInitConfigs();
	if (fieldConfigs == null) {
	    return;
	}
	for (ParamInitConfig fic : fieldConfigs.values()) {
	    // System.out.println("try to init "+fic.getName()+" for "+formConfig.getId());
	    if (!checkShouldInitParam(fic)) {
		continue;
	    }

	    Object value = null;
	    if (fic.getValue() != null) {
		value = fic.getValue();
	    } else if (fic.getSource() != null) {
		// value = getSourceData(replaceFieldValueInSource(fic.getSource()));
		value = dataContext.getValue(replaceFieldValueInSource(fic.getSource()));
	    } else if (fic.getSourceField() != null) {
		value = getValueForProperty(fic.getSourceField());
	    }
	    /* initialize the dataObject */
	    if ("this".equals(fic.getName())) {
		if (value != null) {
		    setDataObject(value);
		} else {
		    setDataObject(createObjectForDataType(formConfig.getDataType()));
		}
		continue;
	    }

	    if (fic.isPropertyFlagOn()) {
		// System.out.println("init "+fic.getName()+" with "+value+" for "+formConfig.getId());
		setValueForProperty(fic.getName(), value);
	    } else {
		// auxiliaryData.put(fic.getName(), value);
		dataContext.setValue(fic.getName(), value);
	    }
	}
    }

    protected void initFormData() {
	if (formData == null) {
	    formData = getNewFormData();
	}
    }

    protected FormData getNewFormData() {
	FormDataFactory formDataFactory = formConfig.getFormDataFactory();
	if (formDataFactory == null) {
	    formDataFactory = this.formDataFactory;
	}
	if (formConfig.getDataType() != null) {
	    return formDataFactory.createFormDataForType(formConfig.getDataType());
	}
	return null;
    }

    protected FormData getFormDataForObject(Object o) {
	FormDataFactory formDataFactory = formConfig.getFormDataFactory();
	if (formDataFactory == null) {
	    formDataFactory = this.formDataFactory;
	}
	return formDataFactory.createFormDataFromObject(o);
    }

    private boolean checkShouldInitParam(ParamInitConfig pic) {
	String initFlag = pic.getInitFlag();
	if (ParamInitConfig.INIT_ALWAYS.equals(initFlag)) {
	    return true;
	}
	if (ParamInitConfig.WRONG_TYPE.equals(initFlag)) {
	    // try {
	    // return !dataObject.getClass().equals(Class.forName(formConfig.getDataType()));
	    return !formData.getType().equals(getNewFormData().getType());
	    // } catch (ClassNotFoundException e) {
	    // // TODO Auto-generated catch block
	    // e.printStackTrace();
	    // }

	}

	if (ParamInitConfig.INIT_WHEN_NULL.equals(initFlag)) {
	    if (pic.isPropertyFlagOn() && pic.getName() != null) {
		if (getValueForProperty(pic.getName()) != null) {
		    return false;
		}
	    }
	}
	return true;
    }

    public abstract void construct() throws Exception;

    protected void setUpBorder() {
	if (formConfig.isShowLabels()) {
	    String desc = getFormDescription();
	    if (desc != null) {
		ui.setBorderName(desc);
	    }
	}
    }

    protected String getFormDescription() {
	String desc = formConfig.getLabel();
	if (desc == null) {
	    desc = formConfig.getFieldName();
	}
	return desc;
    }

    protected void onValueChanged(String propertyName, Object value) {
	// try {
	// Object oldValue = getValueForProperty(propertyName);
	// ReflectionUtility.setValueToField(dataObject, propertyName, value);
	// propertyChangeSupport.firePropertyChange(propertyName, oldValue, value);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	setValueForProperty(propertyName, value);
    }

    protected String replaceFieldValueInSource(String target) {
	Pattern p = Pattern.compile("\\(.*\\)");
	Matcher m = p.matcher(target);
	String resp = target;
	while (m.find()) {
	    String group = m.group();
	    String prop = group.replace("(", "");
	    prop = prop.replace(")", "");
	    // System.out.println("replace prop "+prop);
	    // System.out.println(auxiliaryData);
	    Object value = getAnyValueForProperty(prop);
	    // System.out.println("returned : "+value);
	    if (value != null) {
		resp = resp.replace(group, value.toString());
	    }
	}
	return resp;
    }

    public abstract void onNestedValueChanged(PropertyChangeEvent event);

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
	propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
	propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public void addFormConfigChangeListener(PropertyChangeListener pcl) {
	formConfigChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removeFormConfigChangeListener(PropertyChangeListener pcl) {
	formConfigChangeSupport.removePropertyChangeListener(pcl);
    }

    public void refresh() {
	ui.clear();
	try {
	    construct();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	ui.validate();
    }

    protected void addElement(FormElement element, Object value) throws Exception {
	if (element.getClass().equals(FormComponent.class)) {
	    addComponent((FormComponent) element, value);
	} else if (element instanceof FormControlComponent) {
	    addControlComponent((FormControlComponent) element, value);
	}
    }

    // protected Object getSourceData(String sourceName) {
    // if (sourceName == null /*|| auxiliaryData == null*/) {
    // return null;
    // }
    // try {
    // int index = sourceName.indexOf(".");
    // if (index < 0) {
    // // return ReflectionUtility.getValueForField(auxiliaryData, sourceName);
    // return dataContext.getValue(sourceName);
    // } else {
    // Object mainObj = ReflectionUtility.getValueForField(auxiliaryData, sourceName.substring(0, index));
    // return getValueForProperty(sourceName.substring(index + 1), mainObj);
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return null;
    // }

    protected Object getSourceData(FormElement fe) {
	if (fe.getSourceField() != null) {
	    return getValueForProperty(fe.getSourceField());
	} else if (fe.getSource() != null /* && auxiliaryData != null */) {
	    // return getSourceData(replaceFieldValueInSource(fe.getSource()));
	    return dataContext.getValue(replaceFieldValueInSource(fe.getSource()));
	}
	return null;
    }

    protected ComponentConfig getComponentConfig(FormComponent fc) throws Exception {
	ComponentConfig cc = new ComponentConfig();
	cc.setType(fc.getUiType());
	cc.setName(fc.getFieldName());
	cc.setSource(getSourceData(fc));
	// Object value = ReflectionUtility.getValueForField(dataObject, fc.getFieldName());
	// cc.setValue(value);
	return cc;
    }

    protected ComponentConfig getComponentConfig(String name, String type, Object value, Object source) {
	ComponentConfig cc = new ComponentConfig();
	cc.setType(type);
	cc.setName(name);
	cc.setSource(source);
	cc.setValue(value);
	return cc;
    }

    protected void addComponent(ComponentConfig cc) throws Exception {

	cc.setUiEventListener(new ValueChangedListener(cc.getName()));
	ui.addComponent(cc, constraints);
    }

    protected void addComponent(FormComponent component, Object value) throws Exception {
	ComponentConfig cc = getComponentConfig(component);
	cc.setValue(value);
	cc.setUiEventListener(new ValueChangedListener(component.getFieldName()));
	ui.addComponent(cc, constraints);
    }

    protected void addActionComponent(FormComponent c) throws Exception {
	ComponentConfig cc = new ComponentConfig();
	cc.setType(c.getUiType());
	cc.setName(c.getActionName());
	cc.setValue(getLabel(c));
	cc.setUiEventListener(new FormActionEventListener(c.getActionName()));
	ui.addComponent(cc, constraints);
    }

    protected String getLabel(FormElement fe) {
	return fe.getLabel();
	// TODO: update to support resource bundle ids
    }

    protected void addSubform(FormConfig formConfig, Object dataObject, FormUi ui) throws Exception {
	FormConfig newFormConfig = syncWithControlComponents((FormConfig) formConfig.clone(), dataObject);
	String fieldName = newFormConfig.getFieldName();
	boolean formChanged = !newFormConfig.equals(formConfig);
	boolean isNewForm = !subforms.containsKey(newFormConfig.getId());
	Form form = owner.getFormFactory().createForm(newFormConfig);
	if (ui != null) {
	    form.setUi(ui);
	}
	form.setDataObject(dataObject);
	// form.setAuxiliaryData(auxiliaryData);
	form.setDataContext(dataContext);
	form.addPropertyChangeListener(new NestedFormPropertyChangeListener(fieldName));
	form.getModel().addFormConfigChangeListener(new NestedFormConfigChangeListener(fieldName));

	if (this.ui != form.getUi()) {
	    this.ui.addSubform(form.getUi(), constraints);
	}
	if (formChanged || isNewForm) {
	    form.initialize();
	} else {
	    form.construct();
	}
	subforms.put(fieldName, form);
    }

    protected void setUpNestedForm(Form nestedForm, Object dataObject, String fieldName) {
	nestedForm.setDataObject(dataObject);
	// nestedForm.setAuxiliaryData(auxiliaryData);
	nestedForm.setDataContext(dataContext);
	nestedForm.addPropertyChangeListener(new NestedFormPropertyChangeListener(fieldName));
	nestedForm.getModel().addFormConfigChangeListener(new NestedFormConfigChangeListener(fieldName));

    }

    protected FormConfig syncWithControlComponents(FormConfig formConfig, Object dataObject) {
	if (formConfig.getElements() == null || dataObject == null) {
	    return formConfig;
	}
	for (FormElement el : formConfig.getElements().values()) {
	    if (el instanceof FormControlComponent) {
		FormControlComponent cc = (FormControlComponent) el;
		if (cc.getFormMappings() != null) {
		    Object value = getValueForProperty(cc.getFieldName(), dataObject);
		    FormConfig newFormConfig = cc.getFormMappings().get(value);
		    // if (newFormConfig == null) {
		    // newFormConfig = cc.getFormMappings().get("*");
		    // }
		    if (newFormConfig != null) {
			// formConfig.setElements(newFormConfig.getElements());
			// formConfig.setFieldInitConfigs(newFormConfig.getFieldInitConfigs());
			// formConfig.setDataType(newFormConfig.getDataType());
			changeFormConfig(newFormConfig, formConfig);
		    }
		}
		break;
	    }
	}
	return formConfig;
    }

    protected void addControlComponent(FormControlComponent controlComponent, Object value) throws Exception {
	// if (controlComponent.getFormMappings() != null) {
	// FormConfig selectedFc = controlComponent.getFormMappings().get(value);
	// if (selectedFc != null && !formConfig.getElements().equals(selectedFc.getElements())) {
	// // setFormConfig(selectedFc);
	// formConfig.setElements(selectedFc.getElements());
	// refresh();
	// return;
	// }
	// }
	ComponentConfig cc = getComponentConfig(controlComponent);
	cc.setValue(value);
	cc.setUiEventListener(new FormControlEventListener(controlComponent));
	if (!controlComponent.isHidden()) {
	    ui.addComponent(cc, constraints);
	}

    }

    protected Object getValueForProperty(String propertyName) {
	FormElement fe = formConfig.getElements().get(propertyName);
	if (fe == null || fe.isBoundToField()) {
	    // return getValueForProperty(propertyName, dataObject);
	    return formData.getValue(propertyName);
	} else {
	    // return getSourceData(propertyName);
	    return dataContext.getValue(propertyName);
	}
    }

    protected Object getAnyValueForProperty(String propertyName) {
	Object value = null;
	/*
	 * first search for a property with the specified name on the data object, unless the property name starts with
	 * $ which means that is an auxiliary parameter
	 */
	if (!propertyName.startsWith("_")) {
	    // value = getValueForProperty(propertyName, dataObject);
	    value = formData.getValue(propertyName);
	}
	/* otherwise look for the property between the auxiliary parameters */
	if (value == null) {
	    // value = getSourceData(propertyName);
	    value = dataContext.getValue(propertyName);
	}
	return value;
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

    protected void setValueForProperty(String propertyName, Object value) {
	FormElement el = formConfig.getElements().get(propertyName);
	Object oldValue = null;
	// System.out.println("Setting value '"+value+"' to property '"+propertyName+"'");
	if (value != null) {
	    // System.out.println("Value "+value +" is not null. type "+value.getClass());
	    String stringValue = value.toString();
	    if (stringValue != null && "".equals(stringValue.trim())) {
		// System.out.println("changing value to null");
		value = null;
	    }
	}
	if (el == null || el.isBoundToField()) {
	    // if (dataObject != null) {
	    if (formData != null) {
		try {
		    oldValue = getValueForProperty(propertyName);
		    // ReflectionUtility.setValueToField(dataObject, propertyName, value);
		    formData.setValue(propertyName, value);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	} else {
	    try {
		oldValue = getValueForProperty(propertyName);
		// auxiliaryData.put(propertyName, value);
		dataContext.setValue(propertyName, value);
		// propertyChangeSupport.firePropertyChange(propertyName, oldValue, value);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	propertyChangeSupport.firePropertyChange(propertyName, oldValue, value);
    }

    protected Object createObjectForDataType(String dataType) {
	try {
	    return Class.forName(dataType).newInstance();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    protected void update() {
	refresh();
	formConfigChangeSupport.firePropertyChange(formConfig.getFieldName(), null, formConfig);
    }

    public Object getObjectForProperty(String propName) {
	if (propName.contains(".")) {
	    List<String> pl = new ArrayList(Arrays.asList(propName.split("\\.")));
	    return getObjecForPropertyPath(pl);
	}
	// return dataObject;
	return formData.asObject();
    }

    public Object getObjecForPropertyPath(List<String> pp) {
	if (pp.size() == 1) {
	    // return dataObject;
	    return formData.asObject();
	} else if (pp.size() > 1) {
	    Form f = subforms.get(pp.remove(0));
	    if (f != null) {
		return f.getModel().getObjecForPropertyPath(pp);
	    }
	}
	return null;
    }

    private void changeFormConfig(FormConfig source, FormConfig target) {
	target.setElements(source.getElements());
	target.setFieldInitConfigs(source.getFieldInitConfigs());
	target.setConfigParams(source.getConfigParams());
	target.setDataType(source.getDataType());
	target.setModelFactory(source.getModelFactory());
    }

    public FormConfig getFormConfig() {
	return formConfig;
    }

    public void setFormConfig(FormConfig formConfig) {
	this.formConfig = formConfig;
    }

    // public Map<String, Object> getAuxiliaryData() {
    // return auxiliaryData;
    // }

    // public void setAuxiliaryData(Map<String, Object> auxiliaryData) {
    // this.auxiliaryData = auxiliaryData;
    // }

    public Object getDataObject() {
	// return dataObject;
	return formData.asObject();
    }

    public FormUi getUi() {
	return ui;
    }

    public Form getOwner() {
	return owner;
    }

    public FormLayoutConstraints getConstraints() {
	return constraints;
    }

    public void setDataObject(Object dataObject) {
	// Object oldObject = this.dataObject;
	// this.dataObject = dataObject;
	FormData oldFormData = this.formData;
	this.formData = null;
	if (dataObject != null) {
	    formData = getFormDataForObject(dataObject);
	    if (propertyChangeSupport == null) {
		// propertyChangeSupport = new PropertyChangeSupport(dataObject);
		propertyChangeSupport = new PropertyChangeSupport(formData);
	    }
	}
	if (propertyChangeSupport != null) {
	    // propertyChangeSupport.firePropertyChange("<dataObject>", oldObject, dataObject);
	    propertyChangeSupport.firePropertyChange("<dataObject>", oldFormData, formData);

	}
    }

    public void setUi(FormUi ui) {
	this.ui = ui;
    }

    public void setOwner(Form owner) {
	this.owner = owner;
    }

    public void setConstraints(FormLayoutConstraints constraints) {
	this.constraints = constraints;
    }

    protected void onNestedFormConfigChanged(PropertyChangeEvent evt) {
	// System.out.println("nested form changed : "+evt.getPropertyName());
	formConfigChangeSupport.firePropertyChange(formConfig.getFieldName() + "." + evt.getPropertyName(), evt
		.getOldValue(), evt.getNewValue());
	// ui.validate();
	refresh();
    }

    public ActionListener getActionListener() {
	return actionListener;
    }

    public void setActionListener(ActionListener actionListener) {
	this.actionListener = actionListener;
    }

    /**
     * @return the formData
     */
    public FormData getFormData() {
	return formData;
    }

    /**
     * @param formData
     *            the formData to set
     */
    public void setFormData(FormData formData) {
	this.formData = formData;
    }

    /**
     * @return the dataContext
     */
    public FormDataContext getDataContext() {
	return dataContext;
    }

    /**
     * @param dataContext
     *            the dataContext to set
     */
    public void setDataContext(FormDataContext dataContext) {
	this.dataContext = dataContext;
    }

    public void setDatasource(FormDataSource ds) {
	this.dataContext.setDatasource(ds);
    }

    /**
     * @return the formDataFactory
     */
    public FormDataFactory getFormDataFactory() {
	return formDataFactory;
    }

    /**
     * @param formDataFactory
     *            the formDataFactory to set
     */
    public void setFormDataFactory(FormDataFactory formDataFactory) {
	this.formDataFactory = formDataFactory;
    }

    class ValueChangedListener implements FormUiEventListener {
	private String propertyName;

	public ValueChangedListener(String name) {
	    propertyName = name;
	}

	public void onEvent(Object event) {
	    ValueChangedEvent vce = (ValueChangedEvent) event;
	    Object oldValue = null;
	    // try {
	    // oldValue = ReflectionUtility.getValueForField(dataObject, propertyName);
	    // } catch (Exception e) {
	    // // TODO Auto-generated catch block
	    // e.printStackTrace();
	    // }
	    onValueChanged(propertyName, vce.getValue());
	    // changeSupport.firePropertyChange(propertyName, oldValue, vce.getValue());
	}
    }

    class FormActionEventListener implements FormUiEventListener {
	private String actionName;

	public FormActionEventListener(String actionName) {
	    this.actionName = actionName;
	}

	public void onEvent(Object event) {
	    if (actionListener != null) {
		ActionEvent ae = (ActionEvent) event;
		ActionEvent newEvent = new ActionEvent(ae.getSource(), ae.getID(), actionName, ae.getWhen(), ae
			.getModifiers());
		actionListener.actionPerformed(newEvent);
	    }

	}

    }

    class NestedFormPropertyChangeListener implements PropertyChangeListener {
	private String fieldName;

	public NestedFormPropertyChangeListener(String f) {
	    fieldName = f;
	}

	public void propertyChange(PropertyChangeEvent evt) {
	    String fullPropName = null;
	    if (fieldName != null) {
		fullPropName = fieldName + "." + evt.getPropertyName();
	    } else {
		fullPropName = evt.getPropertyName();
	    }
	    PropertyChangeEvent newEvent = new PropertyChangeEvent(evt.getSource(), fullPropName, evt.getOldValue(),
		    evt.getNewValue());
	    onNestedValueChanged(newEvent);
	    // propertyChangeSupport.firePropertyChange(newEvent);
	    propertyChangeSupport.firePropertyChange(fullPropName, evt.getOldValue(), evt.getNewValue());
	}
    }

    class FormControlEventListener implements FormUiEventListener {
	private FormControlComponent cc;

	public FormControlEventListener(FormControlComponent cc) {
	    this.cc = cc;
	}

	public void onEvent(Object event) {

	    ValueChangedEvent vce = (ValueChangedEvent) event;
	    String value = (String) vce.getValue();
	    onValueChanged(cc.getFieldName(), value);
	    if (cc.getFormMappings() != null && value != null) {
		FormConfig newFormConfig = cc.getFormMappings().get(value);
		// if(newFormConfig == null){
		// newFormConfig = cc.getFormMappings().get("*");
		// }
		if (newFormConfig != null) {
		    // formConfig.setElements(newFormConfig.getElements());
		    // formConfig.setFieldInitConfigs(newFormConfig.getFieldInitConfigs());
		    // formConfig.setDataType(newFormConfig.getDataType());
		    changeFormConfig(newFormConfig, formConfig);
		}
		init();
		// formConfigChangeSupport.firePropertyChange("formConfig", null, formConfig);
		update();
	    }
	}
    }

    class NestedFormConfigChangeListener implements PropertyChangeListener {
	private String fieldName;

	public NestedFormConfigChangeListener(String fn) {
	    fieldName = fn;
	}

	public void propertyChange(PropertyChangeEvent evt) {
	    onNestedFormConfigChanged(evt);
	}

    }
}
