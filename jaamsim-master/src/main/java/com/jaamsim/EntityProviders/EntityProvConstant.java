/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2017-2018 JaamSim Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaamsim.EntityProviders;

import com.jaamsim.basicsim.Entity;

public class EntityProvConstant<T extends Entity> implements EntityProvider<T> {

	private final T ent;

	public EntityProvConstant(T ent) {
		this.ent = ent;
	}

	public Entity getEntity() {
		return ent;
	}

	@Override
	public T getNextEntity(double simTime) {
		return ent;
	}

	@Override
	public String toString() {
		return ent.getName();
	}

}
