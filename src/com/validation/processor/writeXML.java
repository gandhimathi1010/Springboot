package com.validation.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.*;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.*;

public class writeXML {

	File outputfile = null;

	public writeXML(File foutputFile) {
		this.outputfile = foutputFile;
	}

	public File writeAsXml(List<Record> frecords) {

		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

		try {

			XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(new FileOutputStream(outputfile),
					"UTF-8");

			XMLEventFactory eventFactory = XMLEventFactory.newInstance();

			XMLEvent end = eventFactory.createDTD("\n");

			// starting the XML document
			StartDocument startDocument = eventFactory.createStartDocument();
			xmlEventWriter.add(startDocument);
			xmlEventWriter.add(end);

			// creating rootElement
			StartElement configStartElement = eventFactory.createStartElement("", "", Record.ROOT);
			xmlEventWriter.add(configStartElement);
			xmlEventWriter.add(end);

			// converting list to map for mapping keys to values and helping to
			// create nodes
			Map<String, String> invalidValues = new HashMap<String, String>();

			// iterating through the invalid records
			for (Record rec : frecords) {

				// writing record element in xml
				StartElement configStartRecord = eventFactory.createStartElement("", "", Record.RECORD);
				xmlEventWriter.add(configStartRecord);

				invalidValues.put(rec.REFERENCE, rec.getReference());
				invalidValues.put(rec.DESCRIPTION, rec.getDescription());

				// Write the element nodes
				Set<String> elementNodes = invalidValues.keySet();

				for (String key : elementNodes) {

					if (key.equalsIgnoreCase(rec.REFERENCE)) {

						// creating reference attribute and adding to xml
						Attribute attr = eventFactory.createAttribute(rec.REFERENCE, invalidValues.get(key));
						xmlEventWriter.add(attr);
					}

					if (key.equalsIgnoreCase(rec.DESCRIPTION)) {

						// creating description child node
						createNode(xmlEventWriter, key, invalidValues.get(key));
					}

				}

				xmlEventWriter.add(eventFactory.createEndElement("", "", Record.RECORD));
				xmlEventWriter.add(end);
			}

			xmlEventWriter.add(eventFactory.createEndElement("", "", Record.ROOT));
			xmlEventWriter.add(end);

			xmlEventWriter.add(eventFactory.createEndDocument());
			xmlEventWriter.close();

		} catch (XMLStreamException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return outputfile;
	}

	private static void createNode(XMLEventWriter eventWriter, String element, String value) throws XMLStreamException {

		// creating nodes
		XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
		XMLEvent end = xmlEventFactory.createDTD("\n");
		XMLEvent tab = xmlEventFactory.createDTD("\t");

		// Create Start node
		StartElement sElement = xmlEventFactory.createStartElement("", "", element);
		eventWriter.add(tab);
		eventWriter.add(sElement);

		// Create Content
		Characters characters = xmlEventFactory.createCharacters(value);
		eventWriter.add(characters);

		// Create End node
		EndElement eElement = xmlEventFactory.createEndElement("", "", element);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

}
