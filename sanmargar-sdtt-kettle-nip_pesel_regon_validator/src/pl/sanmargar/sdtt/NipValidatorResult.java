/*
 * Sanmargar Data Transformation Tools
 *
 * (C) Copyright 2015 Sanmargar Team sp z o. o. (http://sanmargar.pl/) and others.
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
package pl.sanmargar.sdtt;

/**
 * @author rafal.jastrzebski
 * @since 01-Jul-2015
 * 
 */
public class NipValidatorResult {

	public boolean isNipValid;
	public String USName;
	public String stdNIP;
	
	public NipValidatorResult() {
		super();
		this.isNipValid = false;
		this.USName = null;
		this.stdNIP = null;
	}
}