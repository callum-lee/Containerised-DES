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
package com.jaamsim.input;

public class ExpError extends Exception {
	public final String source;
	public final int pos;

	public ExpError(String source, int pos, String msg) {
		this(source, pos, msg, (Throwable) null);
	}

	public ExpError(String source, int pos, String msg, Throwable cause) {
		super(msg, cause);
		this.source = source;
		this.pos = pos;
	}

	public ExpError(String source, int pos, String fmt, Object... args) {
		this(source, pos, String.format(fmt, args));
	}

}
