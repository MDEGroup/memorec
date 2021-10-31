package it.univaq.disim.memorec;

import java.util.HashMap;
import java.util.Map;

public abstract class SimilarityCalculator {

	protected DataReader reader = new DataReader();

	private String srcDir;
	private String simDir;
	private String subFolder;
	private Configuration configuration;

	private int trainingStartPos1;
	private int trainingEndPos1;
	private int trainingStartPos2;
	private int trainingEndPos2;
	private int testingStartPos;
	private int testingEndPos;


	public SimilarityCalculator(String srcDir) {
		this.srcDir = srcDir;
	}

	public SimilarityCalculator(String srcDir, String subFolder, Configuration conf, int trainingStartPos1,
			int trainingEndPos1, int trainingStartPos2, int trainingEndPos2, int testingStartPos, int testingEndPos) {
		this.srcDir = srcDir;
		this.subFolder = subFolder;
		this.configuration = conf;
		this.setSimDir(this.srcDir + this.subFolder + "/" + "Similarities" + "/");

		this.trainingStartPos1 = trainingStartPos1;
		this.trainingEndPos1 = trainingEndPos1;
		this.trainingStartPos2 = trainingStartPos2;
		this.trainingEndPos2 = trainingEndPos2;
		this.testingStartPos = testingStartPos;
		this.testingEndPos = testingEndPos;
	}
	
	public void computeSimilarity(String testingPro, Map<String, Map<String, Integer>> projects) {};
	public void computeProjectSimilarity() {
		Map<String, Map<String, Integer>> trainingProjects = new HashMap<>();
		Map<Integer, String> trainingProjectsID = new HashMap<>();

		// Read all training project IDs in trainingProjectsID
		if (trainingStartPos1 < trainingEndPos1)
			trainingProjectsID.putAll(reader.readProjectList(srcDir + "List.txt", trainingStartPos1, trainingEndPos1));

		if (trainingStartPos2 < trainingEndPos2)
			trainingProjectsID.putAll(reader.readProjectList(srcDir + "List.txt", trainingStartPos2, trainingEndPos2));

		// Read all training projects in trainingProjects
		for (String trainingID : trainingProjectsID.values())
			trainingProjects.putAll(reader.getProjectInvocations(srcDir, trainingID));

		// Read all testing project IDs in testingProjectsID
		Map<Integer, String> testingProjectsID = reader.readProjectList(srcDir + "List.txt", testingStartPos,
				testingEndPos);

		int numOfTestingInvocations = 0;
		boolean removeHalf = false;

		switch (configuration) {
			case C1_1:
				numOfTestingInvocations = 1;
				removeHalf = true;
			break;
			case C1_2:
				numOfTestingInvocations = 4;
				removeHalf = true;
			break;
			case C2_1:
				numOfTestingInvocations = 1;
				removeHalf = false;
			break;
			case C2_2:
				numOfTestingInvocations = 4;
				removeHalf = false;
			break;
		}

		for (Integer testingID : testingProjectsID.keySet()) {
			String testingProjectID = testingProjectsID.get(testingID);

			// Get half of all declarations and used for similarity computation
			Map<String, Map<String, Integer>> testingProject = reader.getTestingProjectInvocations(srcDir, subFolder,
					testingProjectID, numOfTestingInvocations, removeHalf);

			trainingProjects.putAll(testingProject);
			computeSimilarity(testingProjectID, trainingProjects);
			trainingProjects.remove(testingProjectID);
		}
	}

	public String getSimDir() {
		return simDir;
	}

	public void setSimDir(String simDir) {
		this.simDir = simDir;
	}
	
}
