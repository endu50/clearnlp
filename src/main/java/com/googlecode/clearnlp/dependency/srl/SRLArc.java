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
package com.googlecode.clearnlp.dependency.srl;

import com.googlecode.clearnlp.constant.universal.STConstant;
import com.googlecode.clearnlp.constant.universal.STPunct;
import com.googlecode.clearnlp.dependency.DEPArc;
import com.googlecode.clearnlp.dependency.DEPLib;
import com.googlecode.clearnlp.dependency.DEPNode;
import com.googlecode.clearnlp.dependency.DEPTree;

/**
 * Dependency arc.
 * @since 1.0.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class SRLArc extends DEPArc
{
	static private String DELIM_FTAG = STPunct.EQUAL;
	private String functionTag = STConstant.EMPTY;
	
	public SRLArc()
	{
		super();
	}
	
	public SRLArc(DEPNode node, String label)
	{
		super(node, label);
	}
	
	public SRLArc(DEPNode node, String label, String functionTag)
	{
		super(node, label);
		this.functionTag = functionTag;
	}
	
	public SRLArc(DEPTree tree, String arc)
	{
		int idx = arc.indexOf(DEPLib.DELIM_HEADS_KEY);
		int nodeId = Integer.parseInt(arc.substring(0, idx));
		
		node  = tree.get(nodeId);
		label = arc.substring(idx+1);
		idx   = label.lastIndexOf(DELIM_FTAG);
		
		if (idx >= 0)
		{
			functionTag = label.substring(idx+1);
			label = label.substring(0, idx);
		}
	}
	
	public String getFunctionTag()
	{
		return functionTag;
	}
	
	@Override
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		
		build.append(node.id);
		build.append(DEPLib.DELIM_HEADS_KEY);
		build.append(label);
		
		if (!functionTag.isEmpty())
		{
			build.append(DELIM_FTAG);
			build.append(functionTag);
		}
		
		return build.toString();
	}
	
	@Override
	public int compareTo(DEPArc arc)
	{
		return label.compareTo(arc.getLabel());
	}	
}
