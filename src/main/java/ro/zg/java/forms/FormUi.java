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


public interface FormUi {
    void addComponent(ComponentConfig config,FormLayoutConstraints constraints);
    void addSubform(FormUi ui, FormLayoutConstraints constraints);
    void addSeparator(FormLayoutConstraints constraints);
    void addVerticalSeparator(FormLayoutConstraints constraints);
    public Object getHolder();
    void validate();
    void clear();
    void removeComponent(Object component);
    void setBorderName(String name);
    void addEmptySpace(FormLayoutConstraints constraints);
    Object getLayout();
    void setLayout(Object l);
}
