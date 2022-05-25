/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2014 Ausenco Engineering Canada Inc.
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

import com.jaamsim.Graphics.DisplayEntity;
import com.jaamsim.Samples.SampleConstant;
import com.jaamsim.Samples.SampleInput;
import com.jaamsim.events.EventManager;
import com.jaamsim.input.InputAgent;
import com.jaamsim.input.IntegerInput;
import com.jaamsim.input.Keyword;
import com.jaamsim.input.Output;
import com.jaamsim.rng.MRG1999a;
import com.jaamsim.units.DimensionlessUnit;

public class BooleanSelector extends DisplayEntity implements RandomStreamUser {
	@Keyword(description = "Seed for the random number generator.  Must be an integer >= 0.",
	         exampleList = {"547"})
	private IntegerInput randomSeedInput;

	@Keyword(description = "The probability of the Selector returning true.",
	         exampleList = {"0.5", "InputValue1", "'2 * [InputValue1].Value'"})
	private SampleInput trueProbInput;

	private final MRG1999a rng = new MRG1999a();
	private boolean lastValue;

	{
		randomSeedInput = new IntegerInput("RandomSeed", KEY_INPUTS, -1);
		randomSeedInput.setValidRange(0, Integer.MAX_VALUE);
		randomSeedInput.setRequired(true);
		randomSeedInput.setDefaultText("None");
		this.addInput(randomSeedInput);

		trueProbInput = new SampleInput("TrueProbability", KEY_INPUTS, new SampleConstant(1.0d));
		trueProbInput.setUnitType(DimensionlessUnit.class);
		trueProbInput.setValidRange(0.0d, 1.0d);
		this.addInput(trueProbInput);
	}

	public BooleanSelector() {}

	@Override
	public void setInputsForDragAndDrop() {
		super.setInputsForDragAndDrop();

		// Find the largest seed used so far
		int seed = getSimulation().getLargestStreamNumber();

		// Set the random number seed next unused value
		InputAgent.applyIntegers(this, randomSeedInput.getKeyword(), seed + 1);
	}

	@Override
	public void earlyInit() {
		super.earlyInit();
		rng.setSeedStream(getStreamNumber(), getSimulation().getSubstreamNumber());
		lastValue = false;
	}

	@Override
	public int getStreamNumber() {
		return randomSeedInput.getValue();
	}

	@Override
	public String getStreamNumberKeyword() {
		return randomSeedInput.getKeyword();
	}

	public boolean getNextValue() {
		double samp = rng.nextUniform();
		double prob = trueProbInput.getNextSample(0);
		lastValue = samp < prob;
		return lastValue;
	}

	@Output(name = "Value",
	 description = "The last value sampled from the distribution. When used in an "
	             + "expression, this output returns a new sample every time the expression "
	             + "is evaluated.")
	public boolean getNextValue(double simTime) {

		// If we are not in a model context, do not perturb the distribution by sampling,
		// instead simply return the last sampled value
		if (!EventManager.hasCurrent()) {
			return lastValue;
		}

		// Select the next sample
		double samp = rng.nextUniform();
		double prob = trueProbInput.getNextSample(simTime);
		lastValue = samp < prob;
		return lastValue;
	}
}
