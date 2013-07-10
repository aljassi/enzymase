package de.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import de.bachelorarbeit.database.DBCreator;
/**
 * This class get a text file and parse it, fill the database 
 * @author aassi
 *
 */
public class Parser {
	
	//global variables
	List<String> enzyms_List=new ArrayList<String>(),organisms_List=new ArrayList<String>();
	String currentEnzymID; 
	String currentEnzymName;
	String dbName = "parserdatabaseVer01";
	DBCreator db = new DBCreator(dbName);

	//contructors
	public Parser(){
		
	}
	public Parser(String db){
		this.dbName=db;
	}
	/**
	 * read ec number from file
	 * @param line
	 * @return ecnumber
	 */
	public String readEnzymID(String line) {
		//wenn line eine enzymid enthält gebe enzymid zurück sonst null
			String s;
			String []ecnummer=line.split(" ");
			
			s=ecnummer[0].substring(3, ecnummer[0].length());
			return s;
		
	
	}
	/**
	 * start parsing file
	 */
	public void parse() {
		InputStream    fis;
		BufferedReader br;
		String         line;
		

		HashMap<String,String> mappingIdToOrgasnimenName = new HashMap<String,String> ();
		try {
			// creating database
			db.createDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			// setting file path 
			fis = new FileInputStream("//media//PUBLIC//bachelorarbeit//brenda_download.txt");
			//fis = new FileInputStream("D://bachelorarbeit//brenda_download.txt");
		
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			// reading file
			while ((line = br.readLine()) != null) {
			    
				
				//String tmp =  readEnzymID(line);
				
				
				// if is new enzyme
				if (line.equals("///")){//&&!line.contains("(")
					//start a new tour
					currentEnzymID="";
					currentEnzymName="";
					//currentEnzymID =tmp;
					mappingIdToOrgasnimenName = new HashMap<String,String> ();
				}
				//get ID 
				if(line.startsWith("ID")){
				currentEnzymID=readEnzymID(line);
					
					
				}
				//get RN values
				if (line.startsWith("RN")&& !line.equals("RECOMMENDED_NAME")&& !line.contains("#")) {
					currentEnzymName=parseRnLine(line);
					if(!enzyms_List.contains(currentEnzymID)&&!currentEnzymID.equals("")){
						enzyms_List.add(currentEnzymID);
						db.insert_Enzyms_Table(currentEnzymID, cleanString(currentEnzymName));
						}
					
				}
				//get PR values
				if (line.startsWith("PR")&& !line.equals("PROTEIN")) {
					parsePrLine(line,mappingIdToOrgasnimenName);
					
				}
				
				//get PI values
				if (line.startsWith("PI")&& !line.equals("PI_VALUE")) {
					parsePiLine(line,mappingIdToOrgasnimenName);
				}
				
				//get KM values
				if (line.startsWith("KM")&& !line.equals("KM_VALUE")) {
					parseKmLine(line,mappingIdToOrgasnimenName);
				}
				
				//get KI values
				if (line.startsWith("KI")&& !line.equals("KI_VALUE")) {
					parseKiLine(line,mappingIdToOrgasnimenName);
				}
				//get TO values
				if (line.startsWith("TO")&& !line.equals("TEMPERATURE_OPTIMUM")) {
					parseToLine(line,mappingIdToOrgasnimenName);
				}
				
				//get Specific Activity values
				if (line.startsWith("SA")&& !line.equals("SPECIFIC_ACTIVITY")) {
					parseSaLine(line,mappingIdToOrgasnimenName);
				}
				
				
				//get IC50  values
				if (line.startsWith("IC50")&& !line.equals("IC50_VALUE")) {
					parseic50Line(line,mappingIdToOrgasnimenName);
				}
				//get TN values
				if (line.startsWith("TN")&& !line.equals("TURNOVER_NUMBER")) {
					parseTnLine(line,mappingIdToOrgasnimenName);
				}
				// get PHR  values
				if (line.startsWith("PHR")&& !line.equals("PH_RANGE")) {
					parsePhrLine(line,mappingIdToOrgasnimenName);
				}
				//get TR values
				if (line.startsWith("TR")&& !line.equals("TEMPERATURE_RANGE")) {
					parseTrLine(line,mappingIdToOrgasnimenName);
				}
				
				//get PHO values
				if (line.startsWith("PHO")&& !line.equals("PH_OPTIMUM")) {
					parsePhoLine(line,mappingIdToOrgasnimenName);
				}

				
			}
		} catch (IOException e) {
			// lesefehler
			e.printStackTrace();
		}
		 
	}
	public void parsePhoLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String phoValue = tokens[2].split(" ")[1];
		for (String id: proteinIDs) {
			// getting Min Max -Value
						String[] minmaxValue;
						Double minValue, maxValue;
						if (!phoValue.contains("e")&& !phoValue.contains("E")) {
							
							/*String occurrance="";
							for(){
								
							}*/
							if(phoValue.split("-")[0].length()>=1){ // for -9999
							minmaxValue = phoValue.split("-");

							if (minmaxValue.length == 2) {

								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[1]);
							} else {
								/*if(minmaxValue[0].split("(").length>0){
									minmaxValue[0]=minmaxValue[0].split("(")[0];
								}*/
								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[0]);
							}
							}else{
								//conserve negative number
								minValue = new Double(phoValue);
								maxValue = new Double(phoValue);
							}

						} else {
							if(phoValue.split("-").length==4){
								String []km=phoValue.split("-");
								String k1=km[0]+"-"+km[1];
								String k2=km[2]+"-"+km[3];
								minValue = new Double(k1);
								maxValue = new Double(k2);
								
							}else if(phoValue.split("-").length==3) {
								
								String []km=phoValue.split("-");
								if(km[0].contains("e")){ //4e-05-0.001
									String k1=km[0]+"-"+km[1];
									String k2=km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}else{ ///0.001-4e-05
									String k1=km[0];
									String k2=km[1]+"-"+km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}
								
							}else{
							minValue = new Double(phoValue);
							maxValue = new Double(phoValue);
							
							}
						}
						
			int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
						
			int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
			db.insert_Parameters_Table("phoptimum", enzymKey, organismKey, minValue, maxValue);			
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+"pho: "+minValue+ " + "+ maxValue);			
						
			
		}

	} 
	public void parsePhrLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String phrValue = tokens[2].split(" ")[1];
		for (String id: proteinIDs) {// getting Min Max -Value
			String[] minmaxValue;
			Double minValue, maxValue;
			if (!phrValue.contains("e")&& !phrValue.contains("E")) {
				
				/*String occurrance="";
				for(){
					
				}*/
				if(phrValue.split("-")[0].length()>=1){ // for -9999
				minmaxValue = phrValue.split("-");

				if (minmaxValue.length == 2) {

					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[1]);
				} else {
					/*if(minmaxValue[0].split("(").length>0){
						minmaxValue[0]=minmaxValue[0].split("(")[0];
					}*/
					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[0]);
				}
				}else{
					//conserve negative number
					minValue = new Double(phrValue);
					maxValue = new Double(phrValue);
				}

			} else {
				if(phrValue.split("-").length==4){
					String []km=phrValue.split("-");
					String k1=km[0]+"-"+km[1];
					String k2=km[2]+"-"+km[3];
					minValue = new Double(k1);
					maxValue = new Double(k2);
					
				}else if(phrValue.split("-").length==3) {
					
					String []km=phrValue.split("-");
					if(km[0].contains("e")){ //4e-05-0.001
						String k1=km[0]+"-"+km[1];
						String k2=km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}else{ ///0.001-4e-05
						String k1=km[0];
						String k2=km[1]+"-"+km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}
					
				}else{
				minValue = new Double(phrValue);
				maxValue = new Double(phrValue);
				
				}
			}
			int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
			
			int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
			db.insert_Parameters_Table("phrange", enzymKey, organismKey, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" phr: "+minValue+" + "+maxValue);
		}

	} 
	
	public void parsePiLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String piValue = tokens[2].split(" ")[1];
		for (String id: proteinIDs) {
			// getting Min Max -Value
						String[] minmaxValue;
						Double minValue, maxValue;
						if (!piValue.contains("e")&& !piValue.contains("E")) {
							
							/*String occurrance="";
							for(){
								
							}*/
							if(piValue.split("-")[0].length()>=1){ // for -9999
							minmaxValue = piValue.split("-");

							if (minmaxValue.length == 2) {

								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[1]);
							} else {
								/*if(minmaxValue[0].split("(").length>0){
									minmaxValue[0]=minmaxValue[0].split("(")[0];
								}*/
								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[0]);
							}
							}else{
								//conserve negative number
								
								minValue = new Double(piValue);
								maxValue = new Double(piValue);
							}

						} else {
							if(piValue.split("-").length==4){
								String []km=piValue.split("-");
								String k1=km[0]+"-"+km[1];
								String k2=km[2]+"-"+km[3];
								minValue = new Double(k1);
								maxValue = new Double(k2);
								
							}else if(piValue.split("-").length==3) {
								
								String []km=piValue.split("-");
								if(km[0].contains("e")){ //4e-05-0.001
									String k1=km[0]+"-"+km[1];
									String k2=km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}else{ ///0.001-4e-05
									String k1=km[0];
									String k2=km[1]+"-"+km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}
								
							}else{
							minValue = new Double(piValue);
							maxValue = new Double(piValue);
							
							}
						}
						int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
						
						int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
						db.insert_Parameters_Table("pivalue", enzymKey, organismKey, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" pi: "+minValue+ " + "+maxValue);
		}

	} 
	public void parseKiLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String kiValue = tokens[2].split(" ")[1];
		for (String id: proteinIDs) {
			// getting Min Max -Value
						String[] minmaxValue;
						Double minValue, maxValue;
						if(kiValue.contains("{")){
						String []my=kiValue.split("\\{");
						kiValue=my[0];
						}
						if (!kiValue.contains("e")&& !kiValue.contains("E")) {
							
							/*String occurrance="";
							for(){
								
							}*/
							if(kiValue.split("-")[0].length()>=1){ // for -9999
							minmaxValue = kiValue.split("-");

							if (minmaxValue.length == 2) {

								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[1]);
							} else {
								/*if(minmaxValue[0].split("(").length>0){
									minmaxValue[0]=minmaxValue[0].split("(")[0];
								}*/
								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[0]);
							}
							}else{
								//conserve negative number
								minValue = new Double(kiValue);
								maxValue = new Double(kiValue);
							}

						} else {
							if(kiValue.split("-").length==4){
								String []km=kiValue.split("-");
								String k1=km[0]+"-"+km[1];
								String k2=km[2]+"-"+km[3];
								minValue = new Double(k1);
								maxValue = new Double(k2);
								
							}else if(kiValue.split("-").length==3) {
								
								String []km=kiValue.split("-");
								if(km[0].contains("e")){ //4e-05-0.001
									String k1=km[0]+"-"+km[1];
									String k2=km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}else{ ///0.001-4e-05
									String k1=km[0];
									String k2=km[1]+"-"+km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}
								
							}else{
							minValue = new Double(kiValue);
							maxValue = new Double(kiValue);
							
							}
						}
						int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
						
						int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
						db.insert_SOAPValue("kivalue", enzymKey, organismKey,null, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" Ki: "+minValue+" + "+maxValue);
		}

	} 
	public void parseToLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String toValue = tokens[2].split(" ")[1];
		for (String id: proteinIDs) {
			// getting Min Max -Value
			String[] minmaxValue;
			Double minValue, maxValue;
			if (!toValue.contains("e")&& !toValue.contains("E")) {
				
				/*String occurrance="";
				for(){
					
				}*/
				if(toValue.split("-")[0].length()>=1){ // for -9999
				minmaxValue = toValue.split("-");

				if (minmaxValue.length == 2) {

					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[1]);
				} else {
					/*if(minmaxValue[0].split("(").length>0){
						minmaxValue[0]=minmaxValue[0].split("(")[0];
					}*/
					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[0]);
				}
				}else{
					//conserve negative number
					minValue = new Double(toValue);
					maxValue = new Double(toValue);
				}

			} else {
				if(toValue.split("-").length==4){
					String []km=toValue.split("-");
					String k1=km[0]+"-"+km[1];
					String k2=km[2]+"-"+km[3];
					minValue = new Double(k1);
					maxValue = new Double(k2);
					
				}else if(toValue.split("-").length==3) {
					
					String []km=toValue.split("-");
					if(km[0].contains("e")){ //4e-05-0.001
						String k1=km[0]+"-"+km[1];
						String k2=km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}else{ ///0.001-4e-05
						String k1=km[0];
						String k2=km[1]+"-"+km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}
					///home/aassi/Desktop/worspace_SOAP
				}else{
				minValue = new Double(toValue);
				maxValue = new Double(toValue);
				
				}
			}
			
			int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
			
			int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
			db.insert_Parameters_Table("temperatureoptimum", enzymKey, organismKey, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" To: "+minValue+" + "+maxValue);
		}

	} 
	public void parseTrLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String trValue = tokens[2].split(" ")[1];
		for (String id: proteinIDs) {
			// getting Min Max -Value
						String[] minmaxValue;
						Double minValue, maxValue;
						if (!trValue.contains("e")&& !trValue.contains("E")) {
							
							if(trValue.split("-")[0].length()>=1){ // for -9999
							minmaxValue = trValue.split("-");
							if (minmaxValue.length == 2) {

								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[1]);
							} else {
	
								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[0]);
							}
							}else{
								//conserve negative number
								
								if(trValue.split("-")[0].length()==0 ){ //-10-40&& trValue.split("-")[1].length()==1
									//String k1="";
									minValue = new Double("-"+trValue.split("-")[1]);
									maxValue = minValue;
								}else{
								minValue = new Double(trValue);
								maxValue = new Double(trValue);
								
								}
							}

						
						} else {

							minValue = new Double(trValue);
							maxValue = new Double(trValue);
						}
						int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
						
						int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
						db.insert_Parameters_Table("temperaturerange", enzymKey, organismKey, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" TR: "+minValue+ " + "+maxValue);
		}

	} 
	public void parseic50Line(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String ic50Value = tokens[2].split(" ")[1];
		
		if(ic50Value.contains("{")){
			String[]mystring=ic50Value.split("\\{");
			ic50Value=mystring[0];
		
		}
		for (String id: proteinIDs) {
			// getting Min Max -Value
						String[] minmaxValue;
						Double minValue, maxValue;
						
						if (!ic50Value.contains("e")&& !ic50Value.contains("E")) {
							
							/*String occurrance="";
							for(){
								
							}*/
							if(ic50Value.split("-")[0].length()>=1){ // for -9999
							minmaxValue = ic50Value.split("-");

							if (minmaxValue.length == 2) {

								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[1]);
							} else {
								/*if(minmaxValue[0].split("(").length>0){
									minmaxValue[0]=minmaxValue[0].split("(")[0];
								}*/
								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[0]);
							}
							}else{
								//conserve negative number
								minValue = new Double(ic50Value);
								maxValue = new Double(ic50Value);
							}

						} else {
							if(ic50Value.split("-").length==4){
								String []km=ic50Value.split("-");
								String k1=km[0]+"-"+km[1];
								String k2=km[2]+"-"+km[3];
								minValue = new Double(k1);
								maxValue = new Double(k2);
								
							}else if(ic50Value.split("-").length==3) {
								
								String []km=ic50Value.split("-");
								if(km[0].contains("e")){ //4e-05-0.001
									String k1=km[0]+"-"+km[1];
									String k2=km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}else{ ///0.001-4e-05
									String k1=km[0];
									String k2=km[1]+"-"+km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}
								
							}else{

								
								//System.out.println(ic50Value);
							minValue = new Double(ic50Value);
							maxValue = new Double(ic50Value);
							
							}
						}
						int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
						
						int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
						db.insert_Parameters_Table("ic50value", enzymKey, organismKey, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" IC50 is: "+minValue+" + "+maxValue);
		}

	} 
	public void parseTnLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String tnValue = tokens[2].split(" ")[1];
		for (String id: proteinIDs) {


			// getting Min Max -Value
			String[] minmaxValue;
			Double minValue, maxValue;
			if (!tnValue.contains("e")&& !tnValue.contains("E")) {
				
				/*String occurrance="";
				for(){
					
				}*/
				if(tnValue.split("-")[0].length()>=1){ // for -9999
				minmaxValue = tnValue.split("-");

				if (minmaxValue.length == 2) {

					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[1]);
				} else {
					/*if(minmaxValue[0].split("(").length>0){
						minmaxValue[0]=minmaxValue[0].split("(")[0];
					}*/
					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[0]);
				}
				}else{
					//conserve negative number
					
					minValue = new Double(tnValue);
					maxValue = new Double(tnValue);
				}

			} else {
				if(tnValue.split("-").length==4){
					String []km=tnValue.split("-");
					String k1=km[0]+"-"+km[1];
					String k2=km[2]+"-"+km[3];
					minValue = new Double(k1);
					maxValue = new Double(k2);
					
				}else if(tnValue.split("-").length==3) {
					
					String []km=tnValue.split("-");
					if(km[0].contains("e")){ //4e-05-0.001
						String k1=km[0]+"-"+km[1];
						String k2=km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}else{ ///0.001-4e-05
						String k1=km[0];
						String k2=km[1]+"-"+km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}
					
				}else{
				minValue = new Double(tnValue);
				maxValue = new Double(tnValue);
				
				}
			}
			int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
			
			int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
			db.insert_SOAPValue("turnover", enzymKey, organismKey,null, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" Tnvalue is: "+minValue+" + "+maxValue);
		}

	} 
	
	public String parseRnLine(String line) {
		//wenn zeile mit startwith anfängt true zurückgeben sonst false
		
		String recName = line.substring(3, line.length());
		
		return recName;
	}

	//parse organisms name
	public void parsePrLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {
		// to get organisms id
		String[] tokens = in.split(" ");
		//getting Organisms name
		String tokens_Name=in.split("#")[2].split("<")[0];
		String id = tokens[0].substring(4,tokens[0].length()-1);
		
		//System.out.println(id);
		
		String organismenName = tokens_Name.substring(0,tokens_Name.length()-1);
		//System.out.println(organismenName);
		
		mappingIdToOrgasnimenName.put(id,organismenName );
		

		if(!organisms_List.contains(organismenName)){
			organisms_List.add(organismenName);
			
			db.insert_Organisms_Table(cleanString(organismenName));
		}
		
	}

	
	public void parseKmLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {
		int count=0;
		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String kmValue = tokens[2].split("\\{")[0];
		//String substrate = tokens[2].split("\\{")[1].split("\\}")[0];

		for (String id : proteinIDs) {

			// getting Min Max -Value
			String[] minmaxValue;
			Double minValue, maxValue;
			if (!kmValue.contains("e")&& !kmValue.contains("E")) {
				
				/*String occurrance="";
				for(){
					
				}*/
				if(kmValue.split("-")[0].length()>1){ // for -9999
				minmaxValue = kmValue.split("-");

				if (minmaxValue.length == 2) {

					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[1]);
				} else {
					/*if(minmaxValue[0].split("(").length>0){
						minmaxValue[0]=minmaxValue[0].split("(")[0];
					}*/
					minValue = new Double(minmaxValue[0]);
					maxValue = new Double(minmaxValue[0]);
				}
				}else{
					//conserve negative number
					minValue = new Double(kmValue);
					maxValue = new Double(kmValue);
				}

			} else {
				if(kmValue.split("-").length==4){
					String []km=kmValue.split("-");
					String k1=km[0]+"-"+km[1];
					String k2=km[2]+"-"+km[3];
					minValue = new Double(k1);
					maxValue = new Double(k2);
					
				}else if(kmValue.split("-").length==3) {
					
					String []km=kmValue.split("-");
					if(km[0].contains("e")){ //4e-05-0.001
						String k1=km[0]+"-"+km[1];
						String k2=km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}else{ ///0.001-4e-05
						String k1=km[0];
						String k2=km[1]+"-"+km[2];
						minValue = new Double(k1);
						maxValue = new Double(k2);
					}
					
				}else{
				minValue = new Double(kmValue);
				maxValue = new Double(kmValue);
				
				}
			}
			int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
			
			int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
			db.insert_SOAPValue("kmvalue", enzymKey, organismKey,null, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+"  KM + "+minValue+" + "+ maxValue);
		}
	}
	public void parseSaLine(String in, HashMap<String,String> mappingIdToOrgasnimenName) {

		String[] tokens = in.split("#");
		String tmp = tokens[1];
		String[] proteinIDs = tmp.split(",");
		String saValue = tokens[2].split(" ")[1];
		if(saValue.contains("(")){
			saValue=saValue.split("(")[0];
		}
		
		for (String id: proteinIDs) {
			
			// getting Min Max -Value
						String[] minmaxValue;
						Double minValue, maxValue;
						if (!saValue.contains("e")&& !saValue.contains("E")) {
							
							/*String occurrance="";
							for(){
								
							}*/
							if(saValue.split("-")[0].length()>=1){ // for -9999
							minmaxValue = saValue.split("-");

							if (minmaxValue.length == 2) {

								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[1]);
							} else {
								
								minValue = new Double(minmaxValue[0]);
								maxValue = new Double(minmaxValue[0]);
							}
							}else{
								//conserve negative number
								
								minValue = new Double(saValue);
								maxValue = new Double(saValue);
							}

						} else {
							if(saValue.split("-").length==4){
								String []km=saValue.split("-");
								String k1=km[0]+"-"+km[1];
								String k2=km[2]+"-"+km[3];
								minValue = new Double(k1);
								maxValue = new Double(k2);
								
							}else if(saValue.split("-").length==3) {
								
								String []km=saValue.split("-");
								if(km[0].contains("e")){ //4e-05-0.001
									String k1=km[0]+"-"+km[1];
									String k2=km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}else{ ///0.001-4e-05
									String k1=km[0];
									String k2=km[1]+"-"+km[2];
									minValue = new Double(k1);
									maxValue = new Double(k2);
								}
								
							}else{
							minValue = new Double(saValue);
							maxValue = new Double(saValue);
							
							}
						}
						int enzymKey=enzyms_List.indexOf(currentEnzymID)+1;
						
						int organismKey=organisms_List.indexOf(mappingIdToOrgasnimenName.get(id))+1;
						db.insert_Parameters_Table("specificactivity", enzymKey, organismKey, minValue, maxValue);	
			//System.out.println(currentEnzymID+" + "+currentEnzymName+" + "+mappingIdToOrgasnimenName.get(id)+" SAvalue is: "+minValue+" + "+maxValue);
		}

	}
	
	//KM	#1,2,5,6,8,9,10,11,13,14,15,18,21,25,26, problem
	/**
	 * clean string for sql db
	 * @param s
	 * @return
	 */
	public String cleanString(String s){
		
		if(s.contains("'")){
			s=s.replaceAll("'", "''");
		}
		return s;
	}
	/**
	 * start enzymase with text file
	 * @param databasename
	 */
	public void enzymase_parser(String databasename){
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("LET'S GO");
		Parser p=new Parser(databasename);
		
		p.parse();
		System.out.println("PASSED");
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		//Queries
		//new DBCreator("parserdatabase").query();
		System.out.println("the programm took "+totalTime/60000+ " minutes for running.");
	}
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("LET'S GO");
		Parser p=new Parser();
		p.parse();
		System.out.println("PASSED");
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		//Queries
		//new DBCreator("parserdatabase").query();
		System.out.println("the programm took "+totalTime/60000+ " minutes for running.");
	}
}