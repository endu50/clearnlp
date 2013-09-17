/**
 * Copyright (c) 2009/09-2012/08, Regents of the University of Colorado
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Copyright 2012/09-2013/04, University of Massachusetts Amherst
 * Copyright 2013/05-Present, IPSoft Inc.
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
package com.googlecode.clearnlp.dependency;


/**
 * @since 1.3.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class DEPLabel
{
	static final String DELIM   = "_";
	
	public String arc;
	public String list;
	public String deprel;
	public double score;

	public DEPLabel() {}
	
	public DEPLabel(String label, double score)
	{
		set(label);
		this.score = score;
	}
	
	public DEPLabel(String label)
	{
		set(label);
	}
	
	public DEPLabel(String arc, String deprel)
	{
		this.arc    = arc;
		this.list   = "";
		this.deprel = deprel;
	}
	
	public void set(String label)
	{
		int idx = label.indexOf(DELIM);
		
		arc    = label.substring(0, idx);
		list   = label.substring(idx+1, idx = label.lastIndexOf(DELIM));
		deprel = label.substring(idx+1);
	}
	
	public boolean isArc(String label)
	{
		return arc.equals(label);
	}
	
	public boolean isList(String label)
	{
		return list.equals(label);
	}
	
	public boolean isDeprel(String label)
	{
		return deprel.equals(label);
	}
	
	public boolean isSame(DEPLabel label)
	{
		return isArc(label.arc) && isList(label.list) && isDeprel(label.deprel);
	}
	
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		
		build.append(arc);		build.append(DELIM);
		build.append(list);		build.append(DELIM);
		build.append(deprel);
		
		return build.toString();
	}
}
