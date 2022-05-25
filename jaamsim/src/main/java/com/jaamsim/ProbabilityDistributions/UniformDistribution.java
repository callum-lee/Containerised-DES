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
import com.jaamsim.rng.MRG1999a;

/**
 * Uniform Distribution.
 * Adapted from A.M. Law, "Simulation Modelling and Analysis, 4th Edition", page 448.
 */
public class UniformDistribution extends Distribution {
	private final MRG1999a rng = new MRG1999a();

	{
		minValueInput.setDefaultValue(new SampleConstant(0.0d));
		maxValueInput.setDefaultValue(new SampleConstant(1.0d));
	}

	public UniformDistribution() {}

	@Override
	public void earlyInit() {
		super.earlyInit();
		rng.setSeedStream(getStreamNumber(), getSubstreamNumber());
	}

	@Override
	protected double getSample(double simTime) {

		// Select the sample from a uniform distribution between the min and max values
		double minVal = minValueInput.getNextSample(simTime);
		double maxVal = maxValueInput.getNextSample(simTime);
		return minVal + rng.nextUniform()*(maxVal - minVal);
	}

	@Override
	protected double getMean(double simTime) {
		double minVal = minValueInput.getNextSample(simTime);
		double maxVal = maxValueInput.getNextSample(simTime);
		return 0.5 *(minVal + maxVal);
	}

	@Override
	protected double getStandardDev(double simTime) {
		double minVal = minValueInput.getNextSample(simTime);
		double maxVal = maxValueInput.getNextSample(simTime);
		return 0.5*(maxVal - minVal) / Math.sqrt(3.0);
	}

}
