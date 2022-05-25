/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2013 Ausenco Engineering Canada Inc.
 * Copyright (C) 2016 JaamSim Software Inc.
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
package com.jaamsim.ProbabilityDistributions;

import java.util.Arrays;

import com.jaamsim.datatypes.DoubleVector;
import com.jaamsim.input.InputErrorException;
import com.jaamsim.input.Keyword;
import com.jaamsim.input.Output;
import com.jaamsim.input.ValueListInput;
import com.jaamsim.rng.MRG1999a;
import com.jaamsim.units.DimensionlessUnit;
import com.jaamsim.units.Unit;
import com.jaamsim.units.UserSpecifiedUnit;

/**
 * DiscreteDistribution is a user-defined probability distribution that selects from a given list of specific values
 * based on a specified probability for each value.  No interpolation is performed between values.
 * @author Harry King
 *
 */
public class DiscreteDistribution extends Distribution {

	@Keyword(description = "The discrete values that can be returned by the distribution. "
	                     + "The values can be any positive or negative and can be listed in any "
	                     + "order. "
	                     + "No interpolation is performed between the values.",
	         exampleList = {"6.2 10.1", "3.5 4.5 6.5 km"})
	private final ValueListInput valueListInput;

	@Keyword(description = "The probabilities corresponding to the values in the 'ValueList' "
	                     + "input. "
	                     + "Must sum to 1.0.",
	         exampleList = {"0.3  0.7"})
	private final ValueListInput probabilityListInput;

	private final MRG1999a rng = new MRG1999a();
	private int[] sampleCount;  // number of times each index has been selected
	private double[] valueList;
	private double[] cumProbList;

	{
		valueListInput = new ValueListInput( "ValueList", KEY_INPUTS, null);
		valueListInput.setUnitType(UserSpecifiedUnit.class);
		valueListInput.setRequired(true);
		this.addInput( valueListInput);

		probabilityListInput = new ValueListInput( "ProbabilityList", KEY_INPUTS, null);
		probabilityListInput.setUnitType(DimensionlessUnit.class);
		probabilityListInput.setValidSum(1.0d, 0.001d);
		probabilityListInput.setValidRange(0.0d, 1.0d);
		probabilityListInput.setRequired(true);
		this.addInput( probabilityListInput);
	}

	public DiscreteDistribution() {
		sampleCount = new int[0];
		valueList = new double[0];
		cumProbList = new double[0];
	}

	@Override
	public void validate() {
		super.validate();

		// The number of entries in the ValueList and ProbabilityList inputs must match
		if( probabilityListInput.getValue().size() != valueListInput.getValue().size() ) {
			throw new InputErrorException( "The number of entries for ProbabilityList and ValueList must be equal" );
		}
	}

	@Override
	public void earlyInit() {
		super.earlyInit();
		rng.setSeedStream(getStreamNumber(), getSubstreamNumber());
		int n = probabilityListInput.getValue().size();
		sampleCount = new int[n];

		// Store the values and cumulative probabilities for binary searching
		valueList = new double[n];
		cumProbList = new double[n];
		double total = 0.0d;
		for (int i=0; i<n; i++) {
			valueList[i] = valueListInput.getValue().get(i);
			total += probabilityListInput.getValue().get(i);
			cumProbList[i] = total;
		}
		cumProbList[n-1] = 1.0d;
	}

	@Override
	protected void setUnitType(Class<? extends Unit> specified) {
		super.setUnitType(specified);
		valueListInput.setUnitType(specified);
	}

	@Override
	protected double getSample(double simTime) {

		double rand = rng.nextUniform();
		int index = -1;

		// Binary search the cumulative probabilities
		int k = Arrays.binarySearch(cumProbList, rand);
		if (k >= 0)
			index = k;
		else
			index = -k - 1;

		if (index < 0 || index >= valueList.length)
			error("Bad index returned from binary search.");

		sampleCount[index]++;
		return valueList[index];
	}

	@Override
	public double getMinValue() {
		if (probabilityListInput.getValue() == null || valueListInput.getValue() == null)
			return Double.NaN;
		double ret = Double.POSITIVE_INFINITY;
		for( int i = 0; i < probabilityListInput.getValue().size(); i++ ) {
			if( probabilityListInput.getValue().get(i) > 0.0 ) {
				if( valueListInput.getValue().get(i) < ret ) {
					ret = valueListInput.getValue().get(i);
				}
			}
		}
		return Math.max(ret, super.getMinValue());
	}

	@Override
	public double getMaxValue() {
		if (probabilityListInput.getValue() == null || valueListInput.getValue() == null)
			return Double.NaN;
		double ret = Double.NEGATIVE_INFINITY;
		for( int i = 0; i < probabilityListInput.getValue().size(); i++ ) {
			if( probabilityListInput.getValue().get(i) > 0.0 ) {
				if( valueListInput.getValue().get(i) > ret ) {
					ret = valueListInput.getValue().get(i);
				}
			}
		}
		return Math.min(ret, super.getMaxValue());
	}

	@Override
	protected double getMean(double simTime) {
		if (probabilityListInput.getValue() == null || valueListInput.getValue() == null)
			return Double.NaN;
		double ret = 0.0;
		for( int i=0; i<probabilityListInput.getValue().size(); i++) {
			ret += probabilityListInput.getValue().get(i) * valueListInput.getValue().get(i);
		}
		return ret;
	}

	@Override
	protected double getStandardDev(double simTime) {
		if (probabilityListInput.getValue() == null || valueListInput.getValue() == null)
			return Double.NaN;
		double sum = 0.0;
		for( int i=0; i<probabilityListInput.getValue().size(); i++) {
			double val = valueListInput.getValue().get(i);
			sum += probabilityListInput.getValue().get(i) * val * val;
		}
		double mean = getMean(simTime);
		return  Math.sqrt( sum - (mean * mean) );
	}

	@Output( name="SampleCount",
			 description="The number of samples selected for each value.")
	public DoubleVector getSampleCount(double simTime) {
		DoubleVector ret = new DoubleVector(sampleCount.length);
		for (int i=0; i<sampleCount.length; i++) {
			ret.add(sampleCount[i]);
		}
		return ret;
	}

}
