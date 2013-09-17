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
package com.googlecode.clearnlp.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.Option;

import com.googlecode.clearnlp.reader.AbstractColumnReader;
import com.googlecode.clearnlp.util.UTInput;


public class DEPEvaluate extends AbstractRun
{
	@Option(name="-g", usage="gold-standard file (required)", required=true, metaVar="<filename>")
	private String s_goldFile;
	@Option(name="-s", usage="system-generated file (required)", required=true, metaVar="<filename>")
	private String s_autoFile;
	@Option(name="-gh", usage="column index of gold head ID (required)", required=true, metaVar="<integer>")
	private int    i_goldHeadId;
	@Option(name="-gd", usage="column index of gold dependency label (required)", required=true, metaVar="<integer>")
	private int    i_goldDeprel;
	@Option(name="-sh", usage="column index of system head ID (required)", required=true, metaVar="<integer>")
	private int    i_autoHeadId;
	@Option(name="-sd", usage="column index of system dependency label (required)", required=true, metaVar="<integer>")
	private int    i_autoDeprel;
	
	private Map<String,int[]> m_labels;
	
	public DEPEvaluate() {}
	
	public DEPEvaluate(String[] args)
	{
		initArgs(args);
		run(s_goldFile, s_autoFile, i_goldHeadId-1, i_goldDeprel-1, i_autoHeadId-1, i_autoDeprel-1);
	}
	
	public void run(String goldFile, String autoFile, int goldHeadId, int goldDeprel, int autoHeadId, int autoDeprel)
	{
		BufferedReader fGold = UTInput.createBufferedFileReader(goldFile);
		BufferedReader fAuto = UTInput.createBufferedFileReader(autoFile);
		String[] gold, auto;
		int[] counts;
		String line;
		
		m_labels = new HashMap<String,int[]>();
		
		try
		{
			while ((line = fGold.readLine()) != null)
			{
				gold = line.split(AbstractColumnReader.DELIM_COLUMN);
				auto = fAuto.readLine().split(AbstractColumnReader.DELIM_COLUMN);
				
				line = line.trim();
				if (line.isEmpty())	 continue;
				counts = getCounts(gold[goldDeprel]);
				
				if (gold[goldDeprel].equals(auto[autoDeprel]))
				{
					counts[2]++;
					
					if (gold[goldHeadId].equals(auto[autoHeadId]))
						counts[0]++;
				}
				
				if (gold[goldHeadId].equals(auto[autoHeadId]))
					counts[1]++;
				
				counts[3]++;
			}
		}
		catch (IOException e) {e.printStackTrace();}
		
		print();
	}
	
	private void print()
	{
		String hline = "------------------------------------------------------------";
		
		System.out.println(hline);
		System.out.printf("%10s%10s%10s%10s%10s%10s\n", "Label", "Count", "Dist.", "LAS", "UAS", "LS");
		System.out.println(hline);
		
		int[] counts = getTotalCounts();
		int   total  = counts[3];
		
		printAccuracy("ALL", total, counts);
		System.out.println(hline);
		
		List<String> tags = new ArrayList<String>(m_labels.keySet());
		Collections.sort(tags);
		
		for (String tag : tags)
			printAccuracy(tag, total, m_labels.get(tag));
		System.out.println(hline);
	}
	
	private void printAccuracy(String label, int total, int[] counts)
	{
		int t = counts[3];
		System.out.printf("%10s%10d%10.2f%10.2f%10.2f%10.2f\n", label, t, 100d*t/total, 100d*counts[0]/t, 100d*counts[1]/t, 100d*counts[2]/t);
	}
	
	private int[] getCounts(String tag)
	{
		int[] counts;
		
		if (m_labels.containsKey(tag))
			counts = m_labels.get(tag);
		else
		{
			counts = new int[4];
			m_labels.put(tag, counts);
		}
		
		return counts;
	}
	
	private int[] getTotalCounts()
	{
		int[] gCounts = null, lCounts;
		int i;
		
		for (String tag : m_labels.keySet())
		{
			lCounts = m_labels.get(tag);
			
			if (gCounts == null)
				gCounts = Arrays.copyOf(lCounts, lCounts.length);
			else
			{
				for (i=0; i<gCounts.length; i++)
					gCounts[i] += lCounts[i];
			}
		}
		
		return gCounts;
	}

	static public void main(String[] args)
	{
		new DEPEvaluate(args);
	}
}
