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
package com.googlecode.clearnlp.experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.clearnlp.coreference.Mention;
import com.googlecode.clearnlp.dependency.DEPNode;
import com.googlecode.clearnlp.dependency.DEPTree;
import com.googlecode.clearnlp.reader.JointReader;
import com.googlecode.clearnlp.util.UTInput;

public class CheckMentions
{
	public CheckMentions(String inDir)
	{
		JointReader reader = new JointReader(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		File file = new File(inDir);
		DEPTree tree;
		
		for (String filename : file.list())
		{
			if (!filename.endsWith("c"))	continue;
			reader.open(UTInput.createBufferedFileReader(inDir+File.separator+filename));
			System.out.println(filename);
			
			while ((tree = reader.next()) != null)
			{
				tree.setDependents();
				check(tree);
			}
			
			reader.close();
		}
	}

	public void check(DEPTree tree)
	{
		List<Mention> mentions = tree.getMentions();
		List<DEPNode> heads;
		
		for (Mention m : mentions)
		{
			heads = getHeads(tree, m.beginIndex, m.endIndex);

			if (heads.size() > 1)
				System.out.println(m.beginIndex+" "+m.endIndex+"\n"+tree.toStringDEP()+"\n");
		}
	}
	
	private List<DEPNode> getHeads(DEPTree tree, int bIdx, int eIdx)
	{
		List<DEPNode> heads = new ArrayList<DEPNode>();
		int i, headId;
		DEPNode node;
		
		for (i=bIdx; i<=eIdx; i++)
		{
			node = tree.get(i);
			headId = node.getHead().id;
			
			if (headId < bIdx || headId > eIdx)
				heads.add(node);
		}
		
		return heads;
	}
	
	static public void main(String[] args)
	{
		new CheckMentions(args[0]);
	}
}

