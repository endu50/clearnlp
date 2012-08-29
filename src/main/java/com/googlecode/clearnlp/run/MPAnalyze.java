/**
* Copyright (c) 2011, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package com.googlecode.clearnlp.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

import org.kohsuke.args4j.Option;
import org.w3c.dom.Element;

import com.googlecode.clearnlp.io.FileExtFilter;
import com.googlecode.clearnlp.morphology.AbstractMPAnalyzer;
import com.googlecode.clearnlp.pos.POSLib;
import com.googlecode.clearnlp.pos.POSNode;
import com.googlecode.clearnlp.reader.AbstractColumnReader;
import com.googlecode.clearnlp.reader.POSReader;
import com.googlecode.clearnlp.util.UTInput;
import com.googlecode.clearnlp.util.UTOutput;
import com.googlecode.clearnlp.util.UTXml;


/**
 * Morphological analyzer.
 * @since 1.0.0
 * @author Jinho D. Choi ({@code choijd@colorado.edu})
 */
public class MPAnalyze extends AbstractRun
{
	@Option(name="-c", usage="the configuration file (input; required)", required=true, metaVar="<filename>")
	protected String s_configFile;
	@Option(name="-i", usage="the input path (input; required)", required=true, metaVar="<filepath>")
	protected String s_inputPath;
	@Option(name="-ie", usage="the input file extension (default: .*)", required=false, metaVar="<regex>")
	protected String s_inputExt = ".*";
	@Option(name="-oe", usage="the output file extension (default: morph)", required=false, metaVar="<string>")
	protected String s_outputExt = "morph";
	
	public MPAnalyze() {}
	
	public MPAnalyze(String[] args)
	{
		initArgs(args);
		
		try
		{
			analyze(s_configFile, s_inputPath, s_inputExt, s_outputExt);
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	public void analyze(String configFile, String inputPath, String inputExt, String outputExt) throws Exception
	{
		Element          eConfig = UTXml.getDocumentElement(new FileInputStream(configFile));
		POSReader         reader = (POSReader)getReader(eConfig); 
		AbstractMPAnalyzer morph = getMPAnalyzer(eConfig);
		
		File f = new File(inputPath);
		
		if (f.isDirectory())
		{
			for (String inputFile : f.list(new FileExtFilter(inputExt)))
				analyze(reader, morph, inputPath+File.separator+inputFile, outputExt);	
		}
		else
			analyze(reader, morph, inputPath, outputExt);
	}
	
	public void analyze(POSReader reader, AbstractMPAnalyzer morph, String inputFile, String outputExt)
	{
		PrintStream fout = UTOutput.createPrintBufferedFileStream(inputFile+"."+outputExt);
		reader.open(UTInput.createBufferedFileReader(inputFile));
		POSNode[] nodes;
		
		System.out.println(inputFile);
		
		while ((nodes = reader.next()) != null)
		{
			morph.lemmatize(nodes);
			fout.println(POSLib.toString(nodes, true) + AbstractColumnReader.DELIM_SENTENCE);
		}
		
		fout.close();
	}
	
	public static void main(String[] args)
	{
		new MPAnalyze(args);
	}
}