/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2013 Ausenco Engineering Canada Inc.
 * Copyright (C) 2016-2022 JaamSim Software Inc.
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

import com.jaamsim.Samples.SampleConstant;
import com.jaamsim.Samples.SampleInput;
import com.jaamsim.input.IntegerInput;
import com.jaamsim.input.Keyword;
import com.jaamsim.rng.MRG1999a;
import com.jaamsim.units.Unit;
import com.jaamsim.units.UserSpecifiedUnit;

/**
 * Erlang Distribution.
 * Adapted from A.M. Law, "Simulation Modelling and Analysis, 4th Edition", page 449.
 */
public class ErlangDistribution extends Distribution {

	@Keyword(description = "The scale parameter for the Erlang distribution.",
	         exampleList = {"5.0", "InputValue1", "'2 * [InputValue1].Value'"})
	private final SampleInput meanInput;

	@Keyword(description = "The shape parameter for the Erlang distribution.  An integer value >= 1.  " +
			"Shape = 1 gives the Exponential distribution.  " +
			"For Shape > 10 it is better to use the Gamma distribution.",
	         exampleList = {"2"})
	private final IntegerInput shapeInput;

	private final MRG1999a rng = new MRG1999a();

	{
		minValueInput.setDefaultValue(new SampleConstant(0.0d));

		meanInput = new SampleInput("Mean", KEY_INPUTS, new SampleConstant(1.0d));
		meanInput.setUnitType(UserSpecifiedUnit.class);
		meanInput.setValidRange(0.0d, Double.POSITIVE_INFINITY);
		this.addInput(meanInput);

		shapeInput = new IntegerInput("Shape", KEY_INPUTS, 1);
		shapeInput.setValidRange( 1, Integer.MAX_VALUE);
		this.addInput(shapeInput);
	}

	public ErlangDistribution() {}

	@Override
	public void earlyInit() {
		super.earlyInit();
		rng.setSeedStream(getStreamNumber(), getSubstreamNumber());
	}

	@Override
	protected void setUnitType(Class<? extends Unit> specified) {
		super.setUnitType(specified);
		meanInput.setUnitType(specified);
	}

	@Override
	protected double getSample(double simTime) {

		// Calculate the product of k random values
		double u = 1.0;
		int k = shapeInput.getValue();
		for( int i=0; i<k; i++) {
			u *= rng.nextUniform();
		}

		// Inverse transform method
		double mean = meanInput.getNextSample(simTime);
		return (- mean / shapeInput.getValue() * Math.log( u ));
	}

	@Override
	protected double getMean(double simTime) {
		return meanInput.getNextSample(simTime);
	}

	@Override
	protected double getStandardDev(double simTime) {
		double mean = meanInput.getNextSample(simTime);
		return mean / Math.sqrt( shapeInput.getValue() );
	}

}
