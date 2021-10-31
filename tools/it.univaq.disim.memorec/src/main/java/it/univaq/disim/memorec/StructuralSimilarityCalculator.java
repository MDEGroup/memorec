package it.univaq.disim.memorec;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

/**
 * Compute similarity between every testing project and all training projects
 * using cosine similarity with weight
 */
public class StructuralSimilarityCalculator extends SimilarityCalculator {
	
	private static final Logger log = LogManager.getFormatterLogger(StructuralSimilarityCalculator.class);

	
	public StructuralSimilarityCalculator(String srcDir, String subFolder, Configuration conf, int trainingStartPos1,
			int trainingEndPos1, int trainingStartPos2, int trainingEndPos2, int testingStartPos, int testingEndPos) {
		super(srcDir, subFolder, conf, trainingStartPos1, trainingEndPos1, trainingStartPos2, trainingEndPos2, testingStartPos,
				testingEndPos);
		try {
			basePath = loadConfigurations("evaluation.properties");
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public String loadConfigurations(String propFile) throws FileNotFoundException, IOException {
		try (InputStream in = new FileInputStream(propFile)) {
			// Read sourceDirectory
			Properties prop = new Properties();
			prop.load(in);
			return prop.getProperty("structuralSimilaritiesDirectory");
		}
	}
	
	public StructuralSimilarityCalculator(String srcDir) {
		super(srcDir);
		try {
			basePath = loadConfigurations("evaluation.properties");
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private String basePath = "";
	/**
	 * Compute the similarity between the project testingPro and all the projects in
	 * the supplied list and serialize the results.
	 */
	@Override
	public void computeSimilarity(String testingPro, Map<String, Map<String, Integer>> projects) {
		Map<String, Float> projectSimilarities = new HashMap<>();
		try {
			Map<String, Float> similarityMap = readFromStructuralSimilarityFile(
					Paths.get(basePath, testingPro.replace(".txt", ".ecore")).toString());
			
			for (String trainingProject : projects.keySet()) {
				Float value = similarityMap.get(trainingProject.replace(".txt", ".ecore"));
				if(value == null) {
					Map<String, Float> tempSimMap = readFromStructuralSimilarityFile(
							Paths.get(basePath, trainingProject.replace(".txt", ".ecore")).toString());
					value = tempSimMap.get(testingPro.replace(".txt", ".ecore"));
				}
				if(value == null) value = 0.0f;
				projectSimilarities.put(trainingProject, value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ValueComparator bvc = new ValueComparator(projectSimilarities);
//		TreeMap<String, Float> sortedMap = new TreeMap<>(bvc);
//		sortedMap.putAll(projectSimilarities);
		final Map<String, Float> sortedByCount = projectSimilarities.entrySet()
                .stream().filter(z -> z.getValue()!=null)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		reader.writeSimilarityScores(getSimDir(), testingPro, sortedByCount);
	}

	private Map<String, Float> readFromStructuralSimilarityFile(String fileName) throws IOException {
		Map<String,Float> result = Maps.newHashMap();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(z ->  {
				String  [] s = z.split(":");
				result.put(s[0],Float.parseFloat(s[1]));
			});
			return result;
		}
	}

	
}
