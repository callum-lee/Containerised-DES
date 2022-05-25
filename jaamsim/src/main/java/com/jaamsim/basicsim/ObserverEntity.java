/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2020 JaamSim Software Inc.
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
package com.jaamsim.basicsim;

import java.util.ArrayList;

import com.jaamsim.input.InputErrorException;

public interface ObserverEntity {

	public static String ERR_WATCHLIST = "WatchList verification error.\n\n"
			+ "A state change has occured that was not triggered by an object in the WatchList.\n"
			+ "Re-run the model with the Event Viewer open and review the events that occurred\n"
			+ "at the same simulation time as this error. One of the objects associated with\n"
			+ "these events needs to be added to the WatchList input.";

	/**
	 * Performs the necessary updates when one or more of the subject entities being monitored
	 * has changed state.
	 * @param subj - subject entity that has changed state
	 */
	public void observerUpdate(SubjectEntity subj);

	/**
	 * Return a list of the subject entities watched by this observer.
	 * @return list of subject entities
	 */
	public ArrayList<SubjectEntity> getWatchList();

	/**
	 * Registers an observer with a list of subjects.
	 * @param obs - observer to be registered
	 * @param list - subjects being monitored by the observer
	 */
	public static void registerWithSubjects(ObserverEntity obs, ArrayList<SubjectEntity> list) {
		for (SubjectEntity subj : list) {
			subj.registerObserver(obs);
		}
	}

	/**
	 * Returns whether the specified observer entity is watching the specified subject or if any
	 * of its subjects are watching the specified subject, and so on recursively.
	 * @param obs - observer
	 * @param subj - subject
	 * @return true if the observer is watching the subject directly or indirectly
	 */
	public static boolean isObserverOf(ObserverEntity obs, SubjectEntity subj) {
		for (SubjectEntity ent : obs.getWatchList()) {
			if (ent == subj || (ent instanceof ObserverEntity
					&& isObserverOf((ObserverEntity) ent, subj)))
				return true;
		}
		return false;
	}

	/**
	 * Tests whether there is a closed loop in the chain of observers and subjects.
	 * @param obs - observer
	 */
	public static void validate(ObserverEntity obs) {
		if (!(obs instanceof SubjectEntity))
			return;
		SubjectEntity subj = (SubjectEntity) obs;
		if (isObserverOf(obs, subj))
			throw new InputErrorException("The chain of WatchList inputs cannot include "
					+ "this entity.");
	}

}
