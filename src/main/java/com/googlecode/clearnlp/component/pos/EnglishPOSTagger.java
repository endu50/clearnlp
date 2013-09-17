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
package com.googlecode.clearnlp.component.pos;

import java.util.Set;
import java.util.zip.ZipInputStream;

import com.googlecode.clearnlp.classification.feature.JointFtrXml;
import com.googlecode.clearnlp.classification.model.StringModel;
import com.googlecode.clearnlp.classification.train.StringTrainSpace;
import com.googlecode.clearnlp.constant.english.ENAux;
import com.googlecode.clearnlp.constituent.CTLibEn;
import com.googlecode.clearnlp.dependency.DEPNode;

/**
 * Part-of-speech tagger using document frequency cutoffs.
 * @since 1.3.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class EnglishPOSTagger extends AbstractPOSTagger
{
//	====================================== CONSTRUCTORS ======================================

	public EnglishPOSTagger() {}
	
	/** Constructs a part-of-speech tagger for collecting lexica. */
	public EnglishPOSTagger(JointFtrXml[] xmls, Set<String> sLsfs)
	{
		super(xmls, sLsfs);
	}
	
	/** Constructs a part-of-speech tagger for training. */
	public EnglishPOSTagger(JointFtrXml[] xmls, StringTrainSpace[] spaces, Object[] lexica)
	{
		super(xmls, spaces, lexica);
	}
	
	/** Constructs a part-of-speech tagger for developing. */
	public EnglishPOSTagger(JointFtrXml[] xmls, StringModel[] models, Object[] lexica)
	{
		super(xmls, models, lexica);
	}
	
	/** Constructs a part-of-speech tagger for bootsrapping. */
	public EnglishPOSTagger(JointFtrXml[] xmls, StringTrainSpace[] spaces, StringModel[] models, Object[] lexica)
	{
		super(xmls, spaces, models, lexica);
	}
	
	/** Constructs a part-of-speech tagger for decoding. */
	public EnglishPOSTagger(ZipInputStream in)
	{
		super(in);
	}
	
//	================================ APPLY RULES ================================
	
	@Override
	protected boolean applyRules()
	{
		if (s_lsfs.contains(d_tree.get(i_input).lowerSimplifiedForm)) return false;
		if (applyBe()) return true;
		
		return false;
	}
	
	private boolean applyBe()
	{
		DEPNode curr = d_tree.get(i_input);
		DEPNode p2 = d_tree.get(i_input-2);
		DEPNode p1 = d_tree.get(i_input-1);
		
		if (p2 != null)
		{
			if (p2.lowerSimplifiedForm.endsWith("name") && p1.lowerSimplifiedForm.equals(ENAux.IS))
			{
				curr.pos = CTLibEn.POS_NNP;
				return true;
			}
		}
		
		return false;
	}
}
