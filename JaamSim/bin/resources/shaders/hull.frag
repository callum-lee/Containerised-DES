/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2012 Ausenco Engineering Canada Inc.
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
#version 120

uniform float C;
uniform float FC;
varying float interpZ;

//layout(location = 0) out vec4 output;
//out vec4 outColour;

void main()
{
    if (interpZ < 0)
        discard;

    if (gl_FrontFacing) {
      gl_FragColor.rgb = vec3(0, 0.5, 0);
    } else {
      gl_FragColor.rgb = vec3(0.5, 0, 0);
    }
    gl_FragColor.a = 0.5;

    gl_FragDepth = log(interpZ*C+1)*FC;

}
