package com.validation.processor;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder; 



public class Application {

	public static void main(String[] args) {

		// input and output files in XML format
//		File readFrom = new File("C:\\Users\\prash\\OneDrive\\Documents\\CTS\\ValidatingFiles\\xmls\\records.xml");
//		File writeTo = new File("C:\\Users\\prash\\OneDrive\\Documents\\CTS\\ValidatingFiles\\xmls\\invalid.xml");

		File inputFile = new File("resources/xmls/records.xml");
		File outputFile = new File("resources/xmls/invalid.xml");
//		System.out.println(inputFile.getAbsolutePath());

		Application app=new Application();
//		HashMap<String, Record> listOfRecords= app.toJavaObject(inputFile);
		List<Record> listOfRecords=app.toJavaObject(inputFile);

/*//		printing a hashmap entries
		for(Map.Entry<String, Record> entry: listOfRecords.entrySet()){
			System.out.println(entry.getKey()+"|"+ entry.getValue());
		}*/
		
		
		//printing arraylist of entire records
		for(Record r:listOfRecords){
			System.out.println(r.toString());
		}
		System.out.println("/////////");
		List<Record> print=app.validate(listOfRecords);
		System.out.println("Failed balances: ");
		for(Record e:print){
			System.out.println(e.toString());
		}
		writeXML write=new writeXML(outputFile);
		write.writeAsXml(print);
		
	}
	
	
	
//	public List<Record> XMLtoRecords(File inputfile) throws Exception{
//		
//		JAXBContext jaxb = JAXBContext.newInstance(Record.class);
//		Unmarshaller unmarshallr=jaxb.createUnmarshaller();
//		return (Record)unmarshallr.unmarshal(inputfile);
//	}
	
	
	
	
	// input xml to java objects
//		public HashMap<String, Record> toJavaObject(File inputxml) {
	public List<Record> toJavaObject(File inputxml) {
			
	        //XML elements names as final String literals
	        final String RECORD = "record";
	        final String REFERENCE = "reference";
	        final String ACCOUNTNUMBER = "accountNumber";
	        final String DESCRIPTION = "description";
	        final String STARTBALANCE = "startBalance";
	        final String MUTATION = "mutation";
	        final String ENDBALANCE = "endBalance";

//	        HashMap<String, Record> recordsList =  new HashMap<>();
	        List<Record> recordsList = new ArrayList<>();
	        
			try {
				SAXBuilder saxbuild = new SAXBuilder();
				Document doc = saxbuild.build(inputxml);

				//getting rootElement
				Element elements=doc.getRootElement();
				System.out.println("Rootelement: "+doc.getRootElement().getName());
				
				if(doc.getRootElement().getName().equals("records")){
					List<Element> recordsXml=elements.getChildren();
					
					for(Element recordElement: recordsXml){
						Record myRecord= new Record();
						myRecord.setReference(recordElement.getAttribute(REFERENCE).getValue());
						myRecord.setAccountNumber(recordElement.getChild(ACCOUNTNUMBER).getValue());
						myRecord.setDescription(recordElement.getChild(DESCRIPTION).getValue());
						
						double startbalance=Double.parseDouble(recordElement.getChild(STARTBALANCE).getValue());
						myRecord.setStartBalance(startbalance);
						
						double mutation=Double.parseDouble(recordElement.getChild(MUTATION).getValue());
						myRecord.setMutaion(mutation);
						
						double endBalance=Double.parseDouble(recordElement.getChild(ENDBALANCE).getValue());
						myRecord.setEndBalance(endBalance);
						
//						recordsList.put(recordElement.getAttribute(REFERENCE).getValue(), myRecord);
						recordsList.add(myRecord);
					}
//					System.out.println(recordsList);
				}
				else{
					System.out.println("Invalid xml..");
				}
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println("returning recordslist..");
			return recordsList;
		}
		
		
		public List<Record> validate(List<Record> flistOfRecords){

			List<Record> failedrecords=new ArrayList<>();
			Set<Record> failedreference=new HashSet<>();
			HashMap<String, Record> tocheck= new HashMap<>();
			
			
			try{
//				for(Map.Entry<String, Record> entry: flistOfRecords.entrySet()){
				for(Record currentRecord:flistOfRecords)
				{
					//checking end balance
					DecimalFormat df=new DecimalFormat("0.00");
					//entry.getValue().getStartBalance()+entry.getValue().getMutation()
					double vendbal=currentRecord.getStartBalance()+currentRecord.getMutation();
					vendbal=Double.parseDouble(df.format(vendbal));
					
					if(currentRecord.getEndBalance()!=vendbal || currentRecord.getEndBalance()<0){
						failedrecords.add(currentRecord);
					}
					
//					System.out.println("finding duplicates..");

					//tocheck.isEmpty()
					if(tocheck.containsKey(currentRecord.getReference())){
						failedreference.add(currentRecord);
					}else{
						tocheck.put(currentRecord.getReference(), currentRecord);
					}
					
				}
//				//checking unique references
//				failedreference.addAll(flistOfRecords.keySet());
//				for(String s:failedreference){
//					System.out.println(s.toString());				
//				}
				
				//printing failed balances
//				for(Record r:failedrecords){
//					System.out.println(r.getReference());
//				}
				
				//printing failed references
				System.out.println("Failed references");
				for(Record e:failedreference){
					System.out.println(e.toString());
				}
				failedrecords.addAll(failedreference);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return failedrecords;
		}
}
