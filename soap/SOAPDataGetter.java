package soap;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import database.DatabaseCreator;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;


//   https://github.com/aljassi/enzymase.git  :für Betreuer
/**
 * This class permit to get Data from Brenda database, parse the data and write
 * them in a Database
 * 
 * @author aassi
 * 
 */

public class SOAPDataGetter implements SOAPDataGetterInterface {
	// List intstances
	List<String> enzyms_List, organisms_List, substrates_List, inhibitors_List,
			listReference, listAutors, listTitel;
	// database name
	String dbName = "enzymaseVer26072013";
	String[] enzyms_array;
	DatabaseCreator db = new DatabaseCreator(dbName);

	/**
	 * start enzymase using soap
	 * 
	 * @param databaseName
	 */
	public void enzymase_Soap(String databaseName) {

		System.out.println("Enzymase is starting using Brenda SOAP Webservice");
		// define current time
		long startTime = System.currentTimeMillis();
		// defining database name for soap webservice
		this.dbName = databaseName;
		try {
			SOAPDataGetter s = new SOAPDataGetter();
			// starting getting element from Webservice
			s.getValues_SOAP();
		} catch (Exception e) {
			e.getCause();
			e.getStackTrace();
			e.getMessage();
		}
		// get current time when programm ends
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("the programm took " + totalTime / 60000
				+ " minutes for running.");

	}

	// DONT NEED IT ANYMORE
	public static void main(String[] args) throws Exception {
		// define current time
		long startTime = System.currentTimeMillis();

		try {
			SOAPDataGetter s = new SOAPDataGetter();
			// starting getting element from Webservice
			s.getValues_SOAP();
		} catch (Exception e) {
			e.getCause();
			e.getStackTrace();
			e.getMessage();
		}
		// get current time when programm ends
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("the programm took " + totalTime / 60000
				+ " minutes for running.");

	}

	/**
	 * getting Recommended Name from Brenda DB via SOAP
	 * 
	 * @param enzym
	 * @return return the recommended name for an enzym
	 * @throws Exception
	 * 
	 */
	@Override
	public String getRecommended_Name(String enzyme) throws Exception {
		// calling the webservice
		Service service = new Service();
		Call call = (Call) service.createCall();
		String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		// setting OperationNamen for getRecommendedName
		call.setOperationName(new QName("http://soapinterop.org/",
				"getRecommendedName"));
		String result = (String) call.invoke(new Object[] { enzyme });
		// none result from webservice
		if (result.equals("") || result.equals(" ")) {
			return "No entries in this field";
		}
		// Parsing String to get Recommended Name
		String[] name = result.split("#");
		String[] reconName = name[1].split("\\*");
		return reconName[reconName.length - 1];

	}

	/**
	 * Getting Systematic Name
	 * 
	 * @param enzym
	 * @return systematic name for an Enzyme
	 * @throws Exception
	 */
	@Override
	public String getSystematic_Name(String enzyme) throws Exception {
		// calling service
		Service service = new Service();
		Call call = (Call) service.createCall();
		String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		// call operation name for getting systematic name
		call.setOperationName(new QName("http://soapinterop.org/",
				"getSystematicName"));
		String result = (String) call.invoke(new Object[] { enzyme });
		// result from webservice is null
		if (result.equals("") || result.equals(" ")) {
			return "No entries in this field";
		}
		// Parsing String to get systematic Name
		String[] name = result.split("#");
		String[] systematic_name = name[1].split("\\*");

		return systematic_name[systematic_name.length - 1];
	}

	/**
	 * Getting CAS Registry Number from enzyme ec number
	 * 
	 * @param Enzym
	 * @return cas ergistry number for an Enzyme
	 * @throws Exception
	 */
	@Override
	public String getCAS_Registry_Number(String enzyme) throws Exception {
		// calling service
		Service service = new Service();
		Call call = (Call) service.createCall();
		String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		// calling operation name for getting systematic name
		call.setOperationName(new QName("http://soapinterop.org/",
				"getCasRegistryNumber"));
		String result = (String) call.invoke(new Object[] { enzyme });
		// Parsing String to get CAS Registry Value
		if (result.equals("") || result.equals(" ")) {
			return "No entries in this field";
		}
		String[] name = result.split("#");
		String[] casReg = name[1].split("\\*");
		return casReg[casReg.length - 1];
	}

	/**
	 * Getting Enzyms and filling Enzyme in Enzyme Database
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void getEnzyms() throws Exception {
		// call service
		Service service = new Service();
		Call call = (Call) service.createCall();
		String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		// calling operation name for ec nummer
		call.setOperationName(new QName("http://soapinterop.org/",
				"getEcNumbersFromEcNumber"));
		String resultString = (String) call.invoke(new Object[] { "" });

		enzyms_array = resultString.split("!");

		// tranforming an array into an arraylist
		enzyms_List = new ArrayList<String>(Arrays.asList(enzyms_array));
		// System.out.println(enzyms_List.size());

		System.out.println();
		System.out.println("Filling Enzym Table with Enzym's parameters....");
		System.out.println();
		int count = 0;
		for (String s : enzyms_array) {
			System.out.println(s);
			String ec = "ecNumber*" + s;

			// getting Recommended Name

			String reconName = getRecommended_Name(ec);
			;

			// Getting Systematic Name
			String sysName = getSystematic_Name(ec);

			// Getting registry Number:CAS
			String cas = getCAS_Registry_Number(ec);

			// filling enzyms Table

			db.insert_Enzyms_Table(s, cleanString(reconName),
					cleanString(sysName), cleanString(cas));

		}

		System.out.println();
		System.out.println("Enzym's table has been filled");
		System.out.println();

	}

	/**
	 * Getting all Organisms and filling organisms table
	 * 
	 * @throws Exception
	 */
	@Override
	public void getAllOrganisms() throws Exception {

		Service service = new Service();
		Call call = (Call) service.createCall();
		String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
		call.setTargetEndpointAddress(new java.net.URL(endpoint));

		/* Get Organisms */

		call.setOperationName(new QName("http://soapinterop.org/",
				"getOrganismsFromOrganism")); // Execution of the FIRST SOAP
												// method
		String resultOrg = (String) call.invoke(new Object[] { "" });
		String[] organisms_array = resultOrg.split("!");
		// transform an array into an arrayList
		organisms_List = new ArrayList<String>(Arrays.asList(organisms_array));

		System.out.println("Filling Organisms Table...");
		// filling organism table

		for (String s : organisms_array) {

			db.insert_Organisms_Table(cleanString(s));

		}

		System.out.println("Organisms Table has been filled");

		System.out.println();

	}

	/**
	 * getting References from Organisms and Enzyms save them in the reference
	 * table
	 * 
	 * @throws Exception
	 */
	@Override
	public void getReferences(String enzyme) {

		try {
			// preparing to get information from Brenda Database
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			// call method for reference
			call.setOperationName(new QName("http://soapinterop.org/",
					"getReference"));
			String resultString = (String) call.invoke(new Object[] { enzyme });
			// System.out.println(resultString);
			// for empty result from webservice
			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements : ec number, organism name, autor,
				// reference,
				// title, journal, volume, pages,
				for (String s : split) {

					String ecNumber = enzyme.split("\\*")[1];
					String reference = s.split("#")[1].split("\\*")[1];
					String autors = s.split("#")[2].split("\\*")[1];
					String title = s.split("#")[3].split("\\*")[1];
					String journal = s.split("#")[4].split("\\*")[1];
					String volume = s.split("#")[5].split("\\*")[1];
					String pages = s.split("#")[6].split("\\*")[1];
					String organism = s.split("#")[8].split("\\*")[1];

					// getting ID for element
					int organismKey = organisms_List.indexOf(organism) + 1;

					// int substrateKey=substrates_List.indexOf(substrate) +
					// 1;
					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;

					// inserting elements into reference database
					db.insert_Reference_Table(enzymKey, organismKey, reference,
							cleanString(autors), cleanString(title),
							cleanString(journal), volume, pages);
				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * getting all inhibitors and filling inhibitors table
	 * 
	 * @throws Exception
	 * @see obsolete
	 */
	public void getInhibitors() throws Exception {
		System.out.println("getting Inhibitors .......");
		inhibitors_List = new ArrayList<String>();
		System.out.println("filling Inhibitors Table");

		for (int e = 0; e < enzyms_List.size(); e++) {

			String enzyme = "ecNumber*" + enzyms_List.get(e);

			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName("http://soapinterop.org/",
					"getInhibitors"));
			String resultString = (String) call.invoke(new Object[] { enzyme });
			// for empty string
			String[] split = resultString.split("!");
			if (resultString.equals("") || resultString.equals(" ")) {
				continue;
			} else {

				for (String s : split) {
					String inhibitor = s.split("#")[1].split("\\*")[1];
					if (!inhibitors_List.contains(inhibitor)) {
						inhibitors_List.add(inhibitor);
						// db.insert_Inhibitor_Table(inhibitor);
					}
					// System.out.println(count+ " .......> " +substrate);

				}
			}

		}

		System.out.println("Inhibitors Table has been filled");
		// transform an array into an arrayList

		System.out.println();
	}

	/**
	 * getting all Substrate and filling substrate table
	 * 
	 * @throws Exception
	 * @see obsolete
	 */
	public void getSubstrate() throws Exception {
		// initializing lists for substrate
		substrates_List = new ArrayList<String>();
		for (int e = 0; e < enzyms_List.size(); e++) {

			String enzym = "ecNumber*" + enzyms_List.get(e);

			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName("http://soapinterop.org/",
					"getSubstrate"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);
			String[] split = resultString.split("!");

			if (resultString.equals("") || resultString.equals(" ")) {
				continue;

			} else {
				for (String s : split) {
					String substrate = s.split("#")[1].split("\\*")[1];
					if (!substrates_List.contains(substrate)) {
						substrates_List.add(substrate);
						// db.insert_Substrate_Table(substrate);
					}
					// System.out.println(count+ " .......> " +substrate);
				}
			}
			System.out.println(substrates_List.size());

			// db.insert_Substrate_Table(s);
		}

		System.out.println("Substrate Table has been filled");

		System.out.println();
	}

	/**
	 * getting all molecularweigth for couple(enzym-organism-reference)
	 * 
	 * @return Exception
	 */
	/*
	 * public void getMolecularWeigth()throws Exception{
	 * 
	 * 
	 * for (int e = 0; e < enzyms_List.size(); e++) {
	 * System.out.println("jacques " + enzyms_List.get(e)); String enzym =
	 * "ecNumber*" + enzyms_List.get(e); Service service = new Service(); Call
	 * call = (Call) service.createCall(); String endpoint =
	 * "http://www.brenda-enzymes.info/soap2/brenda_server.php";
	 * call.setTargetEndpointAddress(new java.net.URL(endpoint));
	 * 
	 * call.setOperationName(new QName("http://soapinterop.org/",
	 * "getReference")); String resultString = (String) call.invoke(new Object[]
	 * { enzym }); // System.out.println(resultString); String[] split =
	 * resultString.split("!");
	 * 
	 * if (resultString.equals("") || resultString.equals(" ")) { continue; }
	 * else {
	 * 
	 * for (String s : split) {
	 * 
	 * String molecularWeightMin = s.split("#")[1].split("\\*")[1]; String
	 * molecularWeightMax = s.split("#")[2].split("\\*")[1]; if
	 * (molecularWeightMax.equals("") || molecularWeightMax.equals(" ") ||
	 * molecularWeightMax == null) { molecularWeightMin = molecularWeightMax; }
	 * String organism = s.split("#")[3].split("\\*")[1]; String reference =
	 * s.split("#")[4].split("\\*")[1]; // get ID from Database: the id in the
	 * Database is // autoincremented that means we dont have to query // the db
	 * by getting the id for reference, organism and // enzym (it would take
	 * long time) // we just take the index+1 for enzym_List,organism_List and
	 * // ReferenceList int organismKey = organisms_List.indexOf(organism) + 1;
	 * int enzymKey = e+ 1; int referenceKey = Integer.parseInt(reference); int
	 * molMin = Integer.parseInt(molecularWeightMin); int molMax =
	 * Integer.parseInt(molecularWeightMax);
	 * //db.insert_MolecularWeight_Table(enzymKey, organismKey,referenceKey,
	 * molMin, molMax); } } }
	 * 
	 * }
	 */

	/**
	 * Getting all Michealis Mente Values (Min-Max) for
	 * couple(enzym-organism-substrate-reference) and updating the
	 * functionalParameter table
	 * 
	 * @throws Exception
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getMichealis_Mente_Values(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName("http://soapinterop.org/",
					"getKmValue"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(15,
							s.split("#")[2].length());

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}

					String ecNumber = enzym.split("\\*")[1];
					String substrate = s.split("#")[3].split("\\*")[1];
					String organism = s.split("#")[5].split("\\*")[1];
					// String reference = s.split("#")[7].split("\\*")[1];
					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;

					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double kmMin = Double.parseDouble(kmValueMin);
					double kmMax = Double.parseDouble(kmValueMax);

					// filling database
					db.insert_SOAPValue("kmvalue", enzymKey, organismKey,
							cleanString(substrate), kmMin, kmMax);
				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * Get all Turnover number via Brenda soap webservice and write them in
	 * database
	 * 
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getTurnOver_number(String enzyme) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"getTurnoverNumber"));
			String resultString = (String) call.invoke(new Object[] { enzyme });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(22,
							s.split("#")[2].length()); // turnoverNumberMaximum*

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzyme.split("\\*")[1];
					String substrate = s.split("#")[3].split("\\*")[1];
					String organism = s.split("#")[5].split("\\*")[1];

					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;

					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);

					// filling database
					db.insert_SOAPValue("turnover", enzymKey, organismKey,
							cleanString(substrate), valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * @see getTurnOver_number values
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getKi_Values(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"getKiValue"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(15,
							s.split("#")[2].length());// kiValueMaximum*

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzym.split("\\*")[1];
					String substrate = s.split("#")[3].split("\\*")[1];
					String organism = s.split("#")[5].split("\\*")[1];

					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;

					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);

					// filling database
					db.insert_SOAPValue("kivalue", enzymKey, organismKey,
							cleanString(substrate), valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * get Pi Values and insert them into Database
	 * 
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getPiValue(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName("http://soapinterop.org/",
					"getPiValue"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(15,
							s.split("#")[2].length());

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzym.split("\\*")[1];
					String organism = s.split("#")[5].split("\\*")[1];

					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;

					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);
					// filling pivale table
					db.insert_Parameters_Table("pivalue", enzymKey,
							organismKey, valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * get ph range values and insert them into Database
	 * 
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getPhrange(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName("http://soapinterop.org/",
					"getPhRange"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(15,
							s.split("#")[2].length());

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzym.split("\\*")[1];
					String organism = s.split("#")[4].split("\\*")[1];

					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;
					// int substrateKey=substrates_List.indexOf(substrate) +
					// 1;
					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);
					// fill phrange table
					db.insert_Parameters_Table("phrange", enzymKey,
							organismKey, valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * get ph optimum values and insert into database
	 * 
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getPhOptimum(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"getPhOptimum"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(17,
							s.split("#")[2].length());

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzym.split("\\*")[1];
					String organism = s.split("#")[4].split("\\*")[1];
					// String reference = s.split("#")[7].split("\\*")[1];
					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;
					// int substrateKey=substrates_List.indexOf(substrate) +
					// 1;
					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);

					db.insert_Parameters_Table("phoptimum", enzymKey,
							organismKey, valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * get Temperature range values
	 * 
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getTemperatureRange(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName("http://soapinterop.org/",
					"getTemperatureRange"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(24,
							s.split("#")[2].length());

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzym.split("\\*")[1];
					String organism = s.split("#")[4].split("\\*")[1];
					// String reference = s.split("#")[7].split("\\*")[1];
					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;
					// int substrateKey=substrates_List.indexOf(substrate) +
					// 1;
					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);

					db.insert_Parameters_Table("temperaturerange", enzymKey,
							organismKey, valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * get temperature optimum values
	 * 
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getTemperatureOptimum(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName("http://soapinterop.org/",
					"getTemperatureOptimum"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(26,
							s.split("#")[2].length());

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzym.split("\\*")[1];
					String organism = s.split("#")[4].split("\\*")[1];

					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;
					// int substrateKey=substrates_List.indexOf(substrate) +
					// 1;
					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);

					db.insert_Parameters_Table("temperatureoptimum", enzymKey,
							organismKey, valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * get specific activity values
	 * 
	 * @param enzym
	 * @see SOAPTR@getReferences
	 */
	@Override
	public void getSpecificActivity(String enzym) {
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			String endpoint = "http://www.brenda-enzymes.info/soap2/brenda_server.php";
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"getSpecificActivity"));
			String resultString = (String) call.invoke(new Object[] { enzym });
			// System.out.println(resultString);

			if (resultString.equals("") || resultString.equals(" ")) {
				return;
			} else {
				String[] split = resultString.split("!");

				// parsing all elements
				for (String s : split) {

					String kmValueMin = s.split("#")[1].split("\\*")[1];
					String kmValueMax = "";

					String p = s.split("#")[2].substring(24,
							s.split("#")[2].length());

					if (p.equals("") || p.equals(" ") || p == null) {
						kmValueMax = kmValueMin;
					} else {
						kmValueMax = s.split("#")[2].split("\\*")[1];
					}
					String ecNumber = enzym.split("\\*")[1];
					String organism = s.split("#")[4].split("\\*")[1];
					// String reference = s.split("#")[7].split("\\*")[1];
					// getting ID for element

					int organismKey = organisms_List.indexOf(organism) + 1;

					int enzymKey = enzyms_List.indexOf(ecNumber) + 1;
					// int referenceKey = Integer.parseInt(reference);
					double valueMin = Double.parseDouble(kmValueMin);
					double valueMax = Double.parseDouble(kmValueMax);

					// insert data into database
					db.insert_Parameters_Table("specificactivity", enzymKey,
							organismKey, valueMin, valueMax);

				}
			}

		} catch (Exception ex) {
			ex.getCause();
			ex.getStackTrace();
			ex.getMessage();
		}

	}

	/**
	 * clean string for sql db
	 * 
	 * @param s
	 * @return String without '
	 */
	public String cleanString(String s) {

		if (s.contains("'")) {
			s = s.replaceAll("'", "''");
		}
		return s;
	}

	/**
	 * Getting all parameters values(Min-Max) for
	 * couple(enzym-organism-substrate-reference) and updating the
	 * functionalParameter table for Inhibitors
	 * 
	 * @throws Exception
	 */
	@Override
	public void getParameters() {

		System.out.println("Filling parameters Tables");
		for (int e = 0; e < enzyms_List.size(); e++) {

			System.out.println("/****** " + enzyms_List.get(e) + " *****/");

			String enzym = "ecNumber*" + enzyms_List.get(e);
			/*
			 *  ********************KM Values
			 * ****************************************
			 */
			getMichealis_Mente_Values(enzym);

			/*
			 *  *******************TurnOver Numbers
			 * ************************************
			 */

			getTurnOver_number(enzym);

			/*
			 *  ************************** Ph Optimum
			 * ********************************
			 */
			getPhOptimum(enzym);

			/*
			 *  **************************** Ph Range
			 * ***********************************
			 */
			getPhrange(enzym);

			/*
			 *  ************************ Temperature Optimum
			 * ******************************
			 */

			getTemperatureOptimum(enzym);

			/*
			 *  *************************Temperature Range
			 * *******************************
			 */
			getTemperatureRange(enzym);

			/*
			 *  *****************************Specific Activity
			 * ****************************
			 */
			getSpecificActivity(enzym);

			/*
			 *  ******************************* pi Value
			 * ******************************
			 */

			getPiValue(enzym);

			/*
			 *  ********************************* Ki Value
			 * *********************************
			 */

			getKi_Values(enzym);

			/*
			 *  *******************************Reference**************************
			 * **********
			 */
			getReferences(enzym);

		}

	}

	/**
	 * Getting values from SOAP
	 * 
	 * @throws Exception
	 */
	public void getValues_SOAP() throws Exception {

		// creating database

		db.createDatabase();

		Service service = new Service();
		Call call = (Call) service.createCall();

		// get ecnumber
		getEnzyms();
		// get Organisms
		getAllOrganisms();

		// getting functional Parameters values
		getParameters();

	}

}
