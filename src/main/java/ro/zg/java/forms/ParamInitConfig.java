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

public class ParamInitConfig {
    public static final String INIT_ALWAYS="always";
    public static final String INIT_WHEN_NULL="null";
    public static final String WRONG_TYPE="wrong-type";
    private String name;
    private Object value;
    private String source;
    private String sourceField;
    private boolean propertyFlagOn;
    private String initFlag = INIT_WHEN_NULL;
    
    public String getName() {
        return name;
    }
    public Object getValue() {
        return value;
    }
    public String getSource() {
        return source;
    }
    public String getSourceField() {
        return sourceField;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setSourceField(String field) {
        this.sourceField = field;
    }
    public boolean isPropertyFlagOn() {
        return propertyFlagOn;
    }
    public void setPropertyFlagOn(boolean propertyFlagOn) {
        this.propertyFlagOn = propertyFlagOn;
    }
    public String getInitFlag() {
        return initFlag;
    }
    public void setInitFlag(String initFlag) {
        this.initFlag = initFlag;
    }
    
}
