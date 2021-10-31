package it.univaq.disim.memorec.dataextractor;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

public class MetamodelComparator {
	private static final String basePath = "../../dataset/";
	private static final String INPUT_FOLDER = basePath + "METAMODELS_RAW/";

	public static void main(String[] args) {
		computeFiles();
	}

	public static Table<String, String, Double> computeDistanceMatrix() {
		Table<String, String, Double> distanceMatrix = HashBasedTable.create();
		try (Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER))) {
			List<Path> pathStream = paths.filter(Files::isRegularFile).collect(Collectors.toList());
			Path[] artifactsArray = new Path[pathStream.size()];
			artifactsArray = pathStream.toArray(artifactsArray);
			for (int i = 0; i < artifactsArray.length - 1; i++) {
				for (int j = i + 1; j < artifactsArray.length; j++) {
					double similarity = 0;
					List<Match> matches = compare(artifactsArray[i], artifactsArray[j]);
					similarity = similarity(matches);
					distanceMatrix.put(artifactsArray[i].getFileName().toString(),
							artifactsArray[j].getFileName().toString(), similarity);
				}
				if (i % 10 == 0)
					System.out.println("Computing " + i + " of " + artifactsArray.length + "similarity");
			}
			return distanceMatrix;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void computeFiles() {
		try (Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER))) {
			List<Path> pathStream = paths.filter(Files::isRegularFile).collect(Collectors.toList());
			Path[] artifactsArray = new Path[pathStream.size()];
			
			artifactsArray = pathStream.toArray(artifactsArray);
			for (int i = 0; i < artifactsArray.length - 1; i++) {
				String simOutputPath = "raw/sim/" + artifactsArray[i].getFileName().toString();
				String contOutputPath = "raw/cont/" + artifactsArray[i].getFileName().toString();
				try (BufferedWriter simWriter = Files.newBufferedWriter(Paths.get(simOutputPath));
						BufferedWriter contWriter = Files.newBufferedWriter(Paths.get(contOutputPath))) {
					for (int j = i + 1; j < artifactsArray.length; j++) {
						try {
						List<Match> matches = compare(artifactsArray[i], artifactsArray[j]);
						double similarity = similarity(matches);
						double containment = containment(matches);
						simWriter.write(artifactsArray[j].getFileName().toString() + ":" + similarity + "\n");
						contWriter.write(artifactsArray[j].getFileName().toString() + ":" + containment + "\n");
						} catch (Exception e) {
							simWriter.write(artifactsArray[j].getFileName().toString() + ":0\n");
							contWriter.write(artifactsArray[j].getFileName().toString() + ":0\n");
							System.err.println("ERROR");
						}
						
					}
				}
				if (i % 10 == 0)
					System.out.println("Computing " + i + " of " + artifactsArray.length + "similarity");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Match> compare(Path mm1Path, Path mm2Path) {
			URI uri1 = URI.createFileURI(mm1Path.toString());
			URI uri2 = URI.createFileURI(mm2Path.toString());
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
			ResourceSet resourceSet1 = new ResourceSetImpl();
			ResourceSet resourceSet2 = new ResourceSetImpl();
			resourceSet1.getResource(uri1, true);
			resourceSet2.getResource(uri2, true);
			IComparisonScope scope = new DefaultComparisonScope(resourceSet1, resourceSet2, null);
			Comparison comparison = EMFCompare.builder().build().compare(scope);
			return comparison.getMatches();
	}

	public static double similarity(List<Match> matchesDef) {
		int counterDef = 0;
		int total = 0;
		for (Match match : matchesDef) {
			List<Match> lm = Lists.newArrayList(match.getAllSubmatches());
			total += lm.size();
			for (Match match2 : lm) 
				if (match2.getLeft() != null && match2.getRight() != null)
					counterDef++;
			if (match.getLeft() != null && match.getRight() != null)
				counterDef++;
		}
		return (counterDef * 1.0) / total;
	}
	public static double containment(List<Match> matchesDef) {
		int counter = 0;
		int counterLeft = 0;
		int counterRight = 0;
		for (Match match : matchesDef) {
			List<Match> lm = Lists.newArrayList(match.getAllSubmatches());
			for (Match match2 : lm) {
				if (match2.getLeft() != null)
					counterLeft++;
				if (match2.getRight() != null)
					counterRight++;
				if (match2.getLeft() != null && match2.getRight() != null)
					counter++;
			}
			if (match.getLeft() != null && match.getRight() != null)
				counter++;
		}
		return (counter * 1.0) / ((counterLeft < counterRight) ? counterLeft : counterRight);
	}
}
