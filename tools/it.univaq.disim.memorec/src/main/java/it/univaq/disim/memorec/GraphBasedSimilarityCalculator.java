package it.univaq.disim.memorec;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;

/**
 * Compute similarity between every testing project and all training projects
 * using cosine similarity with weight
 */
public class GraphBasedSimilarityCalculator extends SimilarityCalculator{
	
	private static final Logger log = LogManager.getFormatterLogger(GraphBasedSimilarityCalculator.class);

	
	public GraphBasedSimilarityCalculator(String srcDir, String subFolder, Configuration conf, int trainingStartPos1,
			int trainingEndPos1, int trainingStartPos2, int trainingEndPos2, int testingStartPos, int testingEndPos) {
		super(srcDir, subFolder, conf, trainingStartPos1, trainingEndPos1, trainingStartPos2, trainingEndPos2, testingStartPos,
				testingEndPos);
	}

	
	public GraphBasedSimilarityCalculator(String srcDir) {
		super(srcDir);
	}


	/**
	 * Compute the similarity between the project testingPro and all the projects in
	 * the supplied list and serialize the results.
	 */
	public void computeSimilarity(String testingPro, Map<String, Map<String, Integer>> projects) {
		Map<String, Integer> termFrequency = computeTermFrequency(projects);
		Map<String, Float> testingProjectVector = new HashMap<>();
		Map<String, Float> projectSimilarities = new HashMap<>();
		Map<String, Integer> terms = projects.get(testingPro);
		for (String term : terms.keySet()) {
			float tfIdf = computeTF_IDF(terms.get(term), projects.size(), termFrequency.get(term));
			testingProjectVector.put(term, tfIdf);
		}
		for (String trainingProject : projects.keySet()) {
			if (!trainingProject.equals(testingPro)) {
				Map<String, Float> trainingProjectVector = new HashMap<>();
				terms = projects.get(trainingProject);

				for (String term : terms.keySet()) {
					float tfIdf = computeTF_IDF(terms.get(term), projects.size(), termFrequency.get(term));
					trainingProjectVector.put(term, tfIdf);
				}

				float similarity = computeCosineSimilarity(testingProjectVector, trainingProjectVector);
				projectSimilarities.put(trainingProject, similarity);
			}
		}

		ValueComparator bvc = new ValueComparator(projectSimilarities);
		TreeMap<String, Float> sortedMap = new TreeMap<>(bvc);
		sortedMap.putAll(projectSimilarities);
		reader.writeSimilarityScores(getSimDir(), testingPro, sortedMap);
	}

	/**
	 * Compute the similarity between two vectors using Jaccard Similarity
	 */
	public float computeJaccardSimilarity(byte[] vector1, byte[] vector2) {
		int count = 0;
		int length = vector1.length;

		for (int i = 0; i < length; i++)
			if (vector1[i] == 1.0 && vector2[i] == 1.0)
				count++;

		return (float) count / (2 * length - count);
	}

	/**
	 * Compute the cosine similarity between two project vectors
	 */
	private float computeCosineSimilarity(Map<String, Float> v1, Map<String, Float> v2) {
		Set<String> both = Sets.intersection(v1.keySet(), v2.keySet());
		double scalar = 0, norm1 = 0, norm2 = 0;

		// Only perform cosine similarity on words that exist in both lists
		if (both.size() > 0) {
			for (Float f : v1.values())
				norm1 += f * f;

			for (Float f : v2.values())
				norm2 += f * f;

			for (String k : both)
				scalar += v1.get(k) * v2.get(k);

			if (scalar == 0)
				return 0f;
			else
				return (float) (scalar / Math.sqrt(norm1 * norm2));
		} else {
			return 0f;
		}
	}

	/**
	 * Compute a term-frequency map which stores, for every invocation, how many
	 * projects in the supplied list invoke it
	 * 
	 * java/util/ArrayList/ArrayList()=131 java/util/List/add(E)=129
	 * java/io/PrintStream/println(java.lang.String)=128
	 */
	private Map<String, Integer> computeTermFrequency(Map<String, Map<String, Integer>> projects) {
		Map<String, Integer> termFrequency = new HashMap<>();

		for (Map<String, Integer> terms : projects.values()) {
			for (String term : terms.keySet()) {
				termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
			}
		}

		return termFrequency;
	}

	/**
	 * Standard term-frequency inverse document frequency calculation
	 */
	private float computeTF_IDF(int count, int total, int freq) {
		return (float) (count * Math.log(total / freq));
	}
}
