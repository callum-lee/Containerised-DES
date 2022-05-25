/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2010-2011 Ausenco Engineering Canada Inc.
 * Copyright (C) 2020-2021 JaamSim Software Inc.
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
package com.jaamsim.input;

import java.util.ArrayList;

import com.jaamsim.basicsim.Entity;
import com.jaamsim.basicsim.JaamSimModel;


public class BooleanInput extends Input<Boolean> {

	private static final ArrayList<String> validOptions;

	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";

	static {
		validOptions = new ArrayList<>();
		validOptions.add(TRUE);
		validOptions.add(FALSE);
	}

	/**
	 * Creates a new Boolean Input with the given keyword, category, units, and
	 * default value.
	 */
	public BooleanInput(String key, String cat, boolean def) {
		super(key, cat, Boolean.valueOf(def));
	}

	@Override
	public String applyConditioning(String str) {
		if (str.startsWith("t") || str.startsWith("T") || str.startsWith("1"))
			return TRUE;
		if (str.startsWith("f") || str.startsWith("F") || str.startsWith("0"))
			return FALSE;
		return str;
	}

	@Override
	public void parse(Entity thisEnt, KeywordIndex kw)
	throws InputErrorException {
		Input.assertCount(kw, 1);
		value = Boolean.valueOf(Input.parseBoolean(kw.getArg(0)));
	}

	@Override
	public String getValidInputDesc() {
		return Input.VALID_BOOLEAN;
	}

	@Override
	public ArrayList<String> getValidOptions(Entity ent) {
		return validOptions;
	}

	@Override
	public String getDefaultString(JaamSimModel simModel) {
		if (defValue == null)
			return "";

		if (defValue)
			return TRUE;

		return FALSE;
	}
}
