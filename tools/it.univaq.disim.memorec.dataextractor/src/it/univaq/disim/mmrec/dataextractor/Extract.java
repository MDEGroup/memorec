package it.univaq.disim.mmrec.dataextractor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.ocl.ParserException;

public class Extract {
	
	private static final String INPUT_FOLDER = "METAMODELS_RAW/";
	private static final String PACKAGE_CLASS_FOLDER_RQ1 = "pkg_cls_raw_RQ1/";
//	private static final String PACKAGE_CLASS_FOLDER_RQ2 = "pkg_cls_curated_RQ2/";
	private static final String CLASS_ATTRIBUTE_FOLDER_RQ1 = "cls_attr_raw_RQ1/";
//	private static final String CLASS_ATTRIBUTE_FOLDER_RQ2 = "cls_attr_curated_RQ2/";
	public static void main(String[] args) throws IOException, ParserException {
		System.out.println("START");
		try (Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER))) {
			List<Path> pathStream = paths.filter(Files::isRegularFile).collect(Collectors.toList());

			Map<Path, String> cls_attr_RQ1_Map = new HashMap<Path, String>();
			pathStream.forEach(z -> cls_attr_RQ1_Map.put(z, extractRQ1ClassAttribute(z.toString())));
			
			Map<Path, String> cls_attr_RQ2_Map = new HashMap<Path, String>();
			pathStream.forEach(z -> cls_attr_RQ2_Map.put(z, extractRQ2ClassAttribute(z.toString())));

			Map<Path, String> pkg_cls_RQ1_Map = new HashMap<Path, String>();
			pathStream.forEach(z -> pkg_cls_RQ1_Map.put(z, extractRQ1PackageClass(z.toString())));
			
			Map<Path, String> pkg_cls_RQ2_Map = new HashMap<Path, String>();
			pathStream.forEach(z -> pkg_cls_RQ2_Map.put(z, extractRQ2PackageClass(z.toString())));


			for (Entry<Path, String> entry : cls_attr_RQ1_Map.entrySet()) {
				Path path = Paths
						.get(entry.getKey().toString().replace(INPUT_FOLDER, CLASS_ATTRIBUTE_FOLDER_RQ1).replace(".ecore", ".txt"));
				try (BufferedWriter writer = Files.newBufferedWriter(path)) {
					writer.write(entry.getValue().trim());
				}
			}
			
//			for (Entry<Path, String> entry : cls_attr_RQ2_Map.entrySet()) {
//				Path path = Paths.get(
//						entry.getKey().toString().replace(INPUT_FOLDER, CLASS_ATTRIBUTE_FOLDER_RQ2).replace(".ecore", ".txt"));
//				try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//					writer.write(entry.getValue().trim());
//				}
//			}

			for (Entry<Path, String> entry : pkg_cls_RQ1_Map.entrySet()) {
				Path path = Paths
						.get(entry.getKey().toString().replace(INPUT_FOLDER, PACKAGE_CLASS_FOLDER_RQ1).replace(".ecore", ".txt"));
				try (BufferedWriter writer = Files.newBufferedWriter(path)) {
					writer.write(entry.getValue().trim());
				}
			}
			
//			for (Entry<Path, String> entry : pkg_cls_RQ2_Map.entrySet()) {
//				Path path = Paths
//						.get(entry.getKey().toString().replace(INPUT_FOLDER, PACKAGE_CLASS_FOLDER_RQ2).replace(".ecore", ".txt"));
//				try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//					writer.write(entry.getValue().trim());
//				}
//			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("STOP");
	}
	private static String extractRQ2PackageClass(String path) {
		String builder = "";
		URI fileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		try {
			Resource resource = resourceSet.getResource(fileURI, true);
			if (resource.isLoaded() && resource.getErrors() != null) {
				TreeIterator<EObject> eAllContents = resource.getAllContents();
				while (eAllContents.hasNext()) {
					EObject next = eAllContents.next();
					if (next instanceof EPackage) {
						EPackage ePackage = (EPackage) next;
						if (ePackage.getEClassifiers().size()>2)
							for(EClassifier ec : ePackage.getEClassifiers())
								builder = builder + "package#" + ec.getName() + System.lineSeparator();
					}
				}
			}
			return builder;

		} catch (Exception e) {
			System.err.println("ERROR1: " + path + " " + e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
	private static String extractRQ1PackageClass(String path) {
		String builder = "";
		URI fileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		try {
			Resource resource = resourceSet.getResource(fileURI, true);
			if (resource.isLoaded() && resource.getErrors() != null) {
				TreeIterator<EObject> eAllContents = resource.getAllContents();
				while (eAllContents.hasNext()) {
					EObject next = eAllContents.next();
					if (next instanceof EPackage) {
						EPackage ePackage = (EPackage) next;
						if (ePackage.getEClassifiers().size()>2)
							for(EClassifier ec : ePackage.getEClassifiers())
								builder = builder + ePackage.getName() + "#" + ec.getName() + System.lineSeparator();
					}
				}
			}
			return builder;

		} catch (Exception e) {
			System.err.println("ERROR1: " + path + " " + e.getMessage());
			e.printStackTrace();
			return "";
		}
	}

	private static String extractRQ2ClassAttribute(String path) {
		String builder = " ";
		URI fileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		try {
			Resource resource = resourceSet.getResource(fileURI, true);
			if (resource.isLoaded() && resource.getErrors() != null) {
				TreeIterator<EObject> eAllContents = resource.getAllContents();
				while (eAllContents.hasNext()) {
					EObject next = eAllContents.next();

					if (next instanceof EClass) {
						EClass eClass = (EClass) next;
						if (eClass.getEAllStructuralFeatures().size()>2)
							for (EStructuralFeature sf : eClass.getEAllStructuralFeatures()) {
								builder = builder + eClass.getName() + "#" + sf.getName() + System.lineSeparator();
							}
					}
				}
			}
			return builder;

		} catch (Exception e) {
			System.err.println("ERROR: " + path + " " + e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
	
	private static String extractRQ1ClassAttribute(String path) {
		String builder = " ";
		URI fileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		try {
			Resource resource = resourceSet.getResource(fileURI, true);
			if (resource.isLoaded() && resource.getErrors() != null) {
				TreeIterator<EObject> eAllContents = resource.getAllContents();
				while (eAllContents.hasNext()) {
					EObject next = eAllContents.next();
					if (next instanceof EClass) {
						EClass eClass = (EClass) next;
						if (eClass.getEStructuralFeatures().size()>2)
							for (EStructuralFeature sf : eClass.getEStructuralFeatures()) {
								builder = builder + eClass.getName() + "#" + sf.getName() + System.lineSeparator();
							}
					}
				}
			}
			return builder;

		} catch (Exception e) {
			System.err.println("ERROR: " + path + " " + e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
}
