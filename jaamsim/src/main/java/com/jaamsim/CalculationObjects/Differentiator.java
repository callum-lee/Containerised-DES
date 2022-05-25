/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2013 Ausenco Engineering Canada Inc.
 * Copyright (C) 2016-2019 JaamSim Software Inc.
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
package com.jaamsim.CalculationObjects;

import com.jaamsim.units.DimensionlessUnit;
import com.jaamsim.units.TimeUnit;
import com.jaamsim.units.Unit;

/**
 * The differentiator returns the derivative of the input signal with respect to time.
 * @author Harry King
 *
 */
public class Differentiator extends DoubleCalculation {

	protected Class<? extends Unit> outUnitType;  // Unit type for the output from this calculation

	public Differentiator() {}

	@Override
	protected void setUnitType(Class<? extends Unit> ut) {
		super.setUnitType(ut);

		outUnitType = Unit.getDivUnitType(ut, TimeUnit.class);
		if (outUnitType == null)
			outUnitType = DimensionlessUnit.class;
	}

	@Override
	public Class<? extends Unit> getUnitType() {
		return outUnitType;
	}

	@Override
	public Class<? extends Unit> getUserUnitType() {
		return outUnitType;
	}

	@Override
	protected double calculateValue(double simTime, double inputVal, double lastTime, double lastInputVal, double lastVal) {

		// Calculate the elapsed time
		double dt = simTime - lastTime;
		if (dt <= 0.0)
			return lastVal;

		// Calculate the derivative
		return (inputVal - lastInputVal)/dt;
	}
}
