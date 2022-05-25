/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2019 JaamSim Software Inc.
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
package com.jaamsim.BasicObjects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jaamsim.input.ExpCollections;
import com.jaamsim.input.ExpError;
import com.jaamsim.input.ExpResType;
import com.jaamsim.input.ExpResult;
import com.jaamsim.units.DimensionlessUnit;

public class FileToHashMap extends FileToArray {

	public FileToHashMap() {}

	@Override
	protected ExpResult getValueForTokens(ArrayList<ArrayList<String>> tokens, double simTime) {
		LinkedHashMap<String, ExpResult> ret = new LinkedHashMap<>(tokens.size());

		// Process each record from the file
		for (ArrayList<String> strRecord : tokens) {
			ArrayList<ExpResult> record = getExpResultList(strRecord, this, simTime);
			if (record.size() < 1)
				error("Entry has no key: %s", strRecord);

			if (record.get(0).type != ExpResType.STRING)
				error("Key is not a string in record: %s", strRecord);

			// Add the entry to the hashmap
			String key = record.get(0).stringVal;
			List<ExpResult> list = record.subList(1, record.size());
			ExpResult colList = ExpCollections.wrapCollection(list, DimensionlessUnit.class);
			ret.put(key, colList);
		}
		return ExpCollections.wrapCollection(ret, DimensionlessUnit.class);
	}

	/**
	 * Sets the data for the FileToMatrix directly from a Java data structure, without the use
	 * of the DataFile input which can be left blank. The hashmap input can contain the following
	 * Java classes and their sub-classes: Double, Integer, String, Entity, List, Map, and Array.
	 * @param map - map whose values are a lists of Java objects containing the input data.
	 * @throws ExpError
	 */
	public void setValue(Map<String, ArrayList<Object>> map) throws ExpError {
		LinkedHashMap<String, ExpResult> temp = new LinkedHashMap<>(map.size());
		for (Map.Entry<String, ArrayList<Object>> entry : map.entrySet()) {
			String key = entry.getKey();
			ArrayList<ExpResult> resRow = getExpResultList(entry.getValue());
			ExpResult colRow = ExpCollections.wrapCollection(resRow, DimensionlessUnit.class);
			temp.put(key, colRow);
		}
		ExpResult val = ExpCollections.wrapCollection(temp, DimensionlessUnit.class);
		setValue(val);
	}

}
