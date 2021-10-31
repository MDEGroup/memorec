package org.focus.tests;



import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import it.univaq.disim.memorec.Configuration;
import it.univaq.disim.memorec.DataReader;
import it.univaq.disim.memorec.StructuralSimilarityCalculator;

public class MemoRecTest {
	
	public void testCompare() {
		String srcFocus = "/Users/juri/development/FOCUS/dataset/pkg_cls_curated_RQ2/";
		String srcMM = "/Users/juri/development/FOCUS/dataset/METAMODELS_CURATED";
		String mm1 = "001_001_001_01_BibTeX.ecore";
		String mm2 = "006_001_006_01_Publication.ecore";
		
		StructuralSimilarityCalculator ssc = new StructuralSimilarityCalculator(srcFocus, "round1", Configuration.C2_1, 1, 0, 56, 550, 1, 55);
		ssc.computeProjectSimilarity();
		
	}
	
	@Test
	public void testCompareProjects() {
		DataReader reader = new DataReader();
		
		Map<String, Map<String, Integer>> trainingProjects = new HashMap<>();
		Map<Integer, String> trainingProjectsID = new HashMap<>();
		
		String srcFocus = "/Users/juri/development/FOCUS/dataset/pkg_cls_curated_RQ2/";
		
		trainingProjectsID.putAll(reader.readProjectList(srcFocus + "List.txt", 55, 550));
		
		for (String trainingID : trainingProjectsID.values())
			trainingProjects.putAll(reader.getProjectInvocations(srcFocus, trainingID));
		
		Map<Integer, String> testingProjectsID = reader.readProjectList(srcFocus + "List.txt", 1, 54);
		
		StructuralSimilarityCalculator ssc = new StructuralSimilarityCalculator(srcFocus);
		ssc.setSimDir("testSimDir/");
		ssc.computeSimilarity(testingProjectsID.get(1), trainingProjects);
	}

}
