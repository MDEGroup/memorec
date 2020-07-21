package it.univaq.disim.mmrec.dataextractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.Query;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;

public class Metrics {

	private static final String INPUT_FOLDER = "METAMODELS_CURATED/";
 
	public static void main(String[] args) throws IOException, ParserException {
		System.out.println("START");
		try (Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER))) {
			List<Path> pathStream = paths.filter(Files::isRegularFile).collect(Collectors.toList());
			int numMC = 0;
			int numSF = 0;
			int numPKG = 0;
			int numAttr = 0;
			int numRef = 0;
			for (Path path : pathStream) {
				EPackage pack = getRootEPackage(path.toString());
				numMC += getValue(pack, "EClass.allInstances()->size()");
				numSF += getValue(pack, "EStructuralFeature.allInstances()->size()");
				numAttr += getValue(pack, "EAttribute.allInstances()->size()");
				numRef += getValue(pack, "EReference.allInstances()->size()");	
				numPKG += pack.getESubpackages().size() +1;
			}
			System.out.println(numMC);
			System.out.println(numAttr);
			System.out.println(numSF);
			System.out.println(numRef);
			System.out.println(numPKG);
		}
	}
	

//			mmMetric.setNumberOfAbstractMetaClasses(
//					Math.round(getValue(pack, "EClass.allInstances()->select(e | e.abstract = true)->size()")));
//			mmMetric.setNumberOfConcreteMetaClasses(
//					mmMetric.getNumberOfMetaClasses() - mmMetric.getNumberOfAbstractMetaClasses());
//			mmMetric.setNumberOfConcreteCompletelyFeatureLessMetaClasses(Math
//					.round(getValue(pack, "EClass.allInstances()->select(e | e.eAllStructuralFeatures->size() = 0 and "
//							+ "e.abstract = false)->size()")));
//			mmMetric.setNumberOfConcreteCompletelyFeatureLessMetaClasses(Math.round(
//					getValue(pack, "EClass.allInstances()->select(e | e.eStructuralFeatures->size() = 0)->size()")));
//			mmMetric.setNumberOfAbstractImmediateFeatureLessMetaClass(
//					Math.round(getValue(pack, "EClass.allInstances()->select(e | e.eStructuralFeatures->size() = 0 and "
//							+ "e.abstract = true)->size()")));
//			mmMetric.setNumberOfConcreteImmediateFeatureLessMetaClass(
//					Math.round(getValue(pack, "EClass.allInstances()->select(e | e.eStructuralFeatures->size() = 0 and "
//							+ "e.abstract = false)->size()")));
//			mmMetric.setNumberOfLonelyMetaClass(Math.round(getValue(pack,
//					"EClass.allInstances()->select(e | EReference.allInstances()->select(k | k.eType = e)->size() = 0"
//							+ ")->size()")));
//			mmMetric.setNumberOfInheritedClass(
//					Math.round(getValue(pack, "EClass.allInstances()->select(e | e.eSuperTypes->size()<>0)->size()")));
//			mmMetric.setAttributeWithInherited(Math
//					.round(getValue(pack, "EClass.allInstances()->collect ( e | e.eAllAttributes->size())->sum()")));
//			mmMetric.setNumberOfReferencesContainment(
//					Math.round(getValue(pack, "EReference.allInstances()->select(x | x.containment = true)->size()")));
//			mmMetric.setNumberOfReferencesOpposite(Math.round(
//					getValue(pack, "EReference.allInstances()->select(e |  e.eOpposite.oclIsUndefined())->size()")));
//			mmMetric.setNumberOfCompletelyFeatureLessMetaClass(Math.round(
//					getValue(pack, "EClass.allInstances()->select(e | e.eAllStructuralFeatures->size() = 0)->size()")));
//			mmMetric.setNumberOfAbstractCompletelyFeatureLessMetaClass(Math.round(getValue(pack,
//					"EClass.allInstances()->select(e | e.eAllStructuralFeatures->size() = 0 and e.abstract = true)->size()")));
//			mmMetric.setAIF(getValue(pack, "EAttribute.allInstances()->size()") * 1.0 / getValue(pack,
//					"EClass.allInstances()->collect(e | e.eAllAttributes)->flatten()->asSequence()->size()"));

	
	private static EPackage getRootEPackage(String metamodel) {
//		String filename = String.format("%s.ecore", metamodel);
		
			
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
			ResourceSet rs = new ResourceSetImpl();
			final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
			rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
			Resource r = rs.getResource(URI.createFileURI(metamodel), true);
			
			return (EPackage) r.getContents().get(0);
		

	}

	private static int getValue(EPackage rootPackage, String expr) throws ParserException {
		OCL<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, Constraint, EClass, EObject> ocl;
		OCLHelper<EClassifier, ?, ?, Constraint> helper;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		helper = ocl.createOCLHelper();
		helper.setContext(EcorePackage.eINSTANCE.getEPackage());
		OCLExpression<EClassifier> expression = helper.createQuery(expr);
		Query<EClassifier, EClass, EObject> query = ocl.createQuery(expression);
		int success = (int) query.evaluate(rootPackage);
		return success;
	}

}
	

