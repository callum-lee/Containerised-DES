/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2015 Ausenco Engineering Canada Inc.
 * Copyright (C) 2018-2022 JaamSim Software Inc.
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
package com.jaamsim.StringProviders;

import java.util.ArrayList;
import java.util.Collections;

import com.jaamsim.Samples.SampleConstant;
import com.jaamsim.Samples.SampleProvider;
import com.jaamsim.basicsim.Entity;
import com.jaamsim.basicsim.ErrorException;
import com.jaamsim.basicsim.JaamSimModel;
import com.jaamsim.input.Input;
import com.jaamsim.input.InputErrorException;
import com.jaamsim.input.KeywordIndex;
import com.jaamsim.input.Parser;
import com.jaamsim.units.DimensionlessUnit;
import com.jaamsim.units.Unit;
import com.jaamsim.units.UserSpecifiedUnit;

public class StringProvInput extends Input<StringProvider> {

	private Class<? extends Unit> unitType;

	public StringProvInput(String key, String cat, StringProvider def) {
		super(key, cat, def);
		unitType = null;
	}

	public void setUnitType(Class<? extends Unit> ut) {

		if (ut == unitType)
			return;

		this.setValid(false);
		unitType = ut;
	}

	@Override
	public void copyFrom(Entity thisEnt, Input<?> in) {
		super.copyFrom(thisEnt, in);

		// An expression input must be re-parsed to reset the entity referred to by "this"
		if (value instanceof StringProvExpression) {
			parseFrom(thisEnt, in);
		}
	}

	@Override
	public String applyConditioning(String str) {
		return Parser.addQuotesIfNeeded(str);
	}

	@Override
	public void parse(Entity thisEnt, KeywordIndex kw) throws InputErrorException {
		value = Input.parseStringProvider(kw, thisEnt, unitType);
		this.setValid(true);
	}

	@Override
	public String getValidInputDesc() {
		return Input.VALID_STRING_PROV;
	}

	@Override
	public ArrayList<String> getValidOptions(Entity ent) {
		ArrayList<String> list = new ArrayList<>();
		JaamSimModel simModel = ent.getJaamSimModel();
		for (Entity each : simModel.getClonesOfIterator(Entity.class, SampleProvider.class)) {
			SampleProvider sp = (SampleProvider)each;
			if (sp.getUnitType() == unitType)
				list.add(each.getName());
		}
		Collections.sort(list, Input.uiSortOrder);
		return list;
	}

	@Override
	public void getValueTokens(ArrayList<String> toks) {
		if (value == null || isDefault())
			return;

		// Preserve the exact text for a constant value input
		if (value instanceof StringProvSample && ((StringProvSample) value).getSampleProvider() instanceof SampleConstant) {
			super.getValueTokens(toks);
			return;
		}

		// All other inputs can be built from scratch
		toks.add(value.toString());
	}

	@Override
	public boolean removeReferences(Entity ent) {
		if (value == null)
			return false;

		if (value instanceof StringProvSample) {
			StringProvSample spsamp = (StringProvSample) value;
			if (spsamp.getSampleProvider() == ent) {
				this.reset();
				return true;
			}
		}
		return false;
	}

	@Override
	public void appendEntityReferences(ArrayList<Entity> list) {
		if (value instanceof Entity) {
			if (list.contains(value))
				return;
			list.add((Entity) value);
			return;
		}

		if (value instanceof StringProvExpression) {
			((StringProvExpression) value).appendEntityReferences(list);
			return;
		}
	}

	@Override
	public boolean useExpressionBuilder() {
		return true;
	}

	@Override
	public String getPresentValueString(JaamSimModel simModel, double simTime) {
		if (value == null)
			return "";

		StringBuilder sb = new StringBuilder();
		if (unitType == null || unitType == DimensionlessUnit.class
				|| unitType == UserSpecifiedUnit.class) {
			sb.append(value.getNextString(simTime));
		}
		else {
			String unitString = simModel.getDisplayedUnit(unitType);
			double sifactor = simModel.getDisplayedUnitFactor(unitType);
			sb.append(value.getNextString(simTime, sifactor));
			sb.append("[").append(unitString).append("]");
		}
		return sb.toString();
	}

	public String getNextString(double simTime) {
		return getNextString(simTime, 1.0d, false);
	}

	public String getNextString(double simTime, double siFactor) {
		return getNextString(simTime, siFactor, false);
	}

	public String getNextString(double simTime, double siFactor, boolean integerValue) {
		try {
			return value.getNextString(simTime, siFactor, integerValue);
		}
		catch (ErrorException e) {
			e.keyword = getKeyword();
			throw e;
		}
	}

	public String getNextString(double simTime, String fmt, double siFactor) {
		try {
			return value.getNextString(simTime, fmt, siFactor);
		}
		catch (ErrorException e) {
			e.keyword = getKeyword();
			throw e;
		}
	}

	public double getNextValue(double simTime) {
		try {
			return value.getNextValue(simTime);
		}
		catch (ErrorException e) {
			e.keyword = getKeyword();
			throw e;
		}
	}

}
