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


public abstract class AbstractFormUi<O> implements FormUi{
    protected ComponentCreatorsRepository<O> componentCreatorsRepository;
    
    public AbstractFormUi(ComponentCreatorsRepository<O> cc){
	componentCreatorsRepository = cc;
    }

    /**
     * @return the componentCreatorsRepository
     */
    public ComponentCreatorsRepository<O> getComponentCreatorsRepository() {
        return componentCreatorsRepository;
    }

    /**
     * @param componentCreatorsRepository the componentCreatorsRepository to set
     */
    public void setComponentCreatorsRepository(ComponentCreatorsRepository<O> componentCreatorsRepository) {
        this.componentCreatorsRepository = componentCreatorsRepository;
    }
    
}
