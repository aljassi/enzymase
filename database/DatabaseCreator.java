package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * this class create database and tables. the class manages the database
 * 
 * @author aassi
 * 
 */

public class DatabaseCreator extends MysqlQueries implements DBCreatorInterface,DBConnectionInterface,FillTableInterface{




	// DBConnector connector;
	// global variables
	//Queries query;
	String databaseName;
	Connection con = null;
	Statement s;

	// constructors
	public DatabaseCreator() {

	}

	public DatabaseCreator(String databaseName) {

		this.databaseName = databaseName;
	}

	/**
	 * this method drop a database if this exists
	 * 
	 * @return true if database is drop otherwise it return false
	 */
	public boolean dropDatabase() {
		try {
			con = getConnection();
			s = con.createStatement();
			// int p = s.executeUpdate(query.drop);
			// setting old foreign and unique key
			int set_unique_checks = s
					.executeUpdate("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;");
			int set_foreign_key = s
					.executeUpdate("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;");
			int drop = s.executeUpdate("DROP SCHEMA IF EXISTS "
					+ this.databaseName + ";");
			// execute the query
			int Result = s.executeUpdate("CREATE DATABASE IF NOT EXISTS "
					+ this.databaseName + ";");
			// createTables();
			System.out.println(this.databaseName + " is droped");
			System.out.println(" Creating  Tables have been droped....");
			return true;
		} catch (Exception e) {

			System.out.println("Database cannot be created!");
			return false;

		} finally {
			closeConnection();
		}
	}

	/**
	 * create the database and the table for functional parameter ,enzymes,
	 * organisms, and reference
	 * 
	 * @return true if database and table are successful created otherwise
	 *         return false
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public boolean createDatabase() throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		dropDatabase();
		try {
			// get connection
			con = getConnection();
			// create a statement
			s = con.createStatement();
			// create the database
			int Result = s.executeUpdate("CREATE DATABASE IF NOT EXISTS "
					+ this.databaseName + ";");
			System.out.println(this.databaseName + " is created");
			System.out.println(" Creating  Tables ....");
			/* Creating Tables */
			createTableEnzym();
			createTableorganism();
			createTableIc50value();
			createTableKivalue();
			createTableKmvalue();
			createTablePhoptimum();
			createTablePhrange();
			createTablePivalue();
			createTableSpecificactivity();
			createTableTurnover();
			createTableTempoptimum();
			createTableTemprange();
			createTableReference();
			System.out.println("All Tables have been created !");
			return true;
		} catch (Exception e) {
			System.out.println("Database cannot be created!");
			return false;
		} finally {
			closeConnection();
		}
	}

	/************************* CREATING TABLES ************************/
	/**
	 * create table for enzym
	 * 
	 * @return true if table successful created otherwise false
	 */
	@Override
	public boolean createTableEnzym() {

		try {
			// con = getConnection();
			s = con.createStatement();
			// select the current database
			useDatabase(this.databaseName);
			// create the database
			int r = s.executeUpdate(super.create_Enzym);
			System.out.println("Enzyms Table is created");
			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for Enzyms");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/* for Parser**** */

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTablePhoptimum() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int a = s.executeUpdate(super.create_pho);
			System.out.println("pho Table is created");
			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Tables");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTablePhrange() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int b = s.executeUpdate(super.create_phr);
			System.out.println("phr Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for phr");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableTempoptimum() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int c = s.executeUpdate(super.create_to);
			System.out.println("to Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for to");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableTemprange() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int d = s.executeUpdate(super.create_tr);
			System.out.println("tr Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for tr");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableSpecificactivity() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int e = s.executeUpdate(super.create_sa);
			System.out.println("sa Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for sa");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableTurnover() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int f = s.executeUpdate(super.create_tn);
			System.out.println("tn Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for tn");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableKmvalue() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int g = s.executeUpdate(super.create_km);
			System.out.println("km Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for km");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTablePivalue() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);
			int h = s.executeUpdate(super.create_pi);
			System.out.println("pi Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for pi");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableKivalue() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int i = s.executeUpdate(super.create_ki);
			System.out.println("ki Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for Ki");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableIc50value() {
		try {
			// con = getConnection();
			s = con.createStatement();

			useDatabase(this.databaseName);

			int j = s.executeUpdate(super.create_ic50);
			System.out.println("ic50 Table is created");

			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for ic50");
			return false;
		} catch (Exception e) {

			return false;
		}

	}

	/* **************parser end table*********************************** */
	/*
	 * public boolean createTableFunctionalParameter() {
	 * 
	 * try { s = con.createStatement(); useDatabase(this.databaseName); int r =
	 * s.executeUpdate(query.create_FunctionalParameters_Table);
	 * System.out.println(" Table for Parameters is created"); return true; }
	 * catch (SQLException e) {
	 * 
	 * System.out.println("Cannot create Table for Parameters"); return false; }
	 * catch (Exception e) {
	 * 
	 * return false; }
	 * 
	 * }
	 */
	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableorganism() {

		try {
			// con=getConnection();
			Statement s;
			s = con.createStatement();
			useDatabase(this.databaseName);
			System.out.println(this.databaseName);
			int k = s.executeUpdate(super.create_Organism);
			System.out.println(" Organisms Table created");
			return true;
		} catch (SQLException e) {
			System.out.println("Cannot create Table for Organisms");
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	/*
	 * public boolean createTableCofactor() {
	 * 
	 * try {
	 * 
	 * s = con.createStatement(); useDatabase(this.databaseName); int p =
	 * s.executeUpdate(query.create_Cofactor_Table);
	 * System.out.println("  Cofactor's Table created"); return true; } catch
	 * (SQLException e) {
	 * 
	 * System.out.println("Cannot create Table for Cofactor"); return false; }
	 * catch (Exception e) { return false; }
	 * 
	 * }
	 * 
	 * public boolean createTableSubstrate() {
	 * 
	 * try {
	 * 
	 * s = con.createStatement(); useDatabase(this.databaseName); int p =
	 * s.executeUpdate(query.create_Substrate_Table);
	 * System.out.println("  substrate's Table created"); return true; } catch
	 * (SQLException e) {
	 * 
	 * System.out.println("Cannot create Table for Substrate"); return false; }
	 * catch (Exception e) { return false; }
	 * 
	 * }
	 * 
	 * public boolean createTableInhibitor(){ try {
	 * 
	 * s = con.createStatement(); useDatabase(this.databaseName); int p =
	 * s.executeUpdate(query.create_Inhibitor_Table);
	 * System.out.println("  inhibitor's Table created"); return true; } catch
	 * (SQLException e) {
	 * 
	 * System.out.println("Cannot create Table for inhibitor"); return false; }
	 * catch (Exception e) { return false; }
	 * 
	 * }
	 * 
	 * public boolean createTableMolecularWeight(){ try {
	 * 
	 * s = con.createStatement(); useDatabase(this.databaseName); int p =
	 * s.executeUpdate(query.create_Molecular_Table);
	 * System.out.println("  Molecular weight's Table created"); return true; }
	 * catch (SQLException e) {
	 * 
	 * System.out.println("Cannot create Table for Molecular weight"); return
	 * false; } catch (Exception e) { return false; }
	 * 
	 * }
	 */
	/**
	 * 
	 * @see DBCreator@createTableEnzym
	 */
	@Override
	public boolean createTableReference() {
		try {

			s = con.createStatement();
			useDatabase(this.databaseName);
			int p = s.executeUpdate(super.create_Reference);
			System.out.println("  Reference's Table created");
			return true;
		} catch (SQLException e) {

			System.out.println("Cannot create Table for reference");
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	/***************** TABLES END ************************************************/

	/**
	 * permit to use a database
	 * 
	 * @param name
	 */

	public void useDatabase(String name) {

		try {

			s = con.createStatement();
			// use the current Database
			int Result = s.executeUpdate("USE " + name + ";");
		} catch (SQLException e) {
			System.out.println("Database '" + name + "' cannot be used");
		}

	}

	/**
	 * close a connection
	 */
	@Override
	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			e.getCause();
			e.getStackTrace();
			// System.out.println("Cannot close Connection");

		}
	}

	/**
	 * Create a connection
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager
				.getConnection("jdbc:mysql://localhost/?user=root&password=root");
		return con;
	}

	/* *
	 * GETTERS AND SETTERS
	 */

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/* ******************* Fill Database****************** */

	// public boolean insert_Enzyms_Table(String ecNumber, String
	// enzymName,String systematicName,String casRegistry) {
	/**
	 * fill enzyme table with data getting from text file
	 * 
	 * @param ecNumber
	 * @param enzymName
	 * @return true if table can be inserted
	 */

	public boolean insert_Enzyms_Table(String ecNumber, String enzymName) { // Parser
		try {
			con = getConnection();

			s = con.createStatement();
			useDatabase(this.databaseName);
			// int
			// result=s.executeUpdate("INSERT INTO enzyms SET ecNumber='"+ecNumber+"',SET enzymName='"+enzymName+"';");

			int result = s
					.executeUpdate("INSERT INTO enzyms (ecNumber,enzymName,systematicName,registryNumber) VALUES('"
							+ ecNumber + "','" + enzymName + "',NULL,NULL);");
			return true;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}

	}

	// for SOAP
	/**
	 * try to fill enzyme table with elements from SOAP
	 * 
	 * @param ecNumber
	 * @param enzymName
	 * @param systematicName
	 * @param casRegistry
	 * @return true if table can be filled otherwise it returns false
	 */
	public boolean insert_Enzyms_Table(String ecNumber, String enzymName,
			String systematicName, String casRegistry) { // for SOAP

		try {
			con = getConnection();

			s = con.createStatement();
			useDatabase(this.databaseName);

			int result = s
					.executeUpdate("INSERT INTO enzyms (ecNumber,enzymName,systematicName,registryNumber) VALUES('"
							+ ecNumber
							+ "','"
							+ enzymName
							+ "','"
							+ systematicName + "','" + casRegistry + "');");
			return true;
		} catch (Exception e) {

			// e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}

	}

	/**
	 * fill organisms table with organisms name
	 * 
	 * @param ornagnismName
	 * @return true if organism table can be filled with element
	 */
	public boolean insert_Organisms_Table(String ornagnismName) {
		try {
			con = getConnection();
			s = con.createStatement();
			useDatabase(this.databaseName);
			int result = s
					.executeUpdate("INSERT INTO organisms(organismName) VALUES('"
							+ ornagnismName + "');");
			return true;
		} catch (Exception e) {

			// e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}

	}

	/**
	 * getting all substrate und saving it in a database
	 * 
	 * @param substrate
	 * @return
	 */
	/*
	 * public boolean insert_Substrate_Table(String substrate) { try {
	 * con=getConnection(); s=con.createStatement();
	 * useDatabase(this.databaseName); int
	 * result=s.executeUpdate("INSERT INTO substrate(substrateName) VALUES('"
	 * +substrate+"');"); return true; } catch (Exception e) {
	 * 
	 * //e.printStackTrace(); return false; }finally{ closeConnection(); }
	 * 
	 * }
	 */
	/**
	 * try to fill reference table with parameters
	 * 
	 * @param enzymId
	 * @param organismId
	 * @param reference
	 * @param autors
	 * @param title
	 * @param journal
	 * @param volume
	 * @param pages
	 * @return true if filling is successful
	 */
	public boolean insert_Reference_Table(int enzymId, int organismId,
			String reference, String autors, String title, String journal,
			String volume, String pages) {
		try {
			con = getConnection();
			s = con.createStatement();
			useDatabase(this.databaseName);
			int result = s
					.executeUpdate("INSERT INTO literature(enzymKey,organismKey,ref,autors,title,journal,volume,page ) VALUES('"
							+ enzymId
							+ "','"
							+ organismId
							+ "','"
							+ reference
							+ "','"
							+ autors
							+ "','"
							+ title
							+ "','"
							+ journal
							+ "','" + volume + "','" + pages + "');");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}

	}

	/**
	 * getting all molecular weight and saving them in the Database
	 * 
	 * @param ecnumber
	 * @param organismkey
	 * @param referenceKey
	 * @param molecularWeight
	 * @return
	 */
	/*
	 * public boolean insert_MolecularWeight_Table(int ecnumber,int organismkey,
	 * int referenceKey,int molecularWeightMin,int molecularWeightMax) { try {
	 * con=getConnection(); s=con.createStatement();
	 * useDatabase(this.databaseName); int
	 * result=s.executeUpdate("INSERT INTO molecular(substrateName) VALUES('"
	 * +ecnumber
	 * +"','"+organismkey+"','"+referenceKey+"','"+molecularWeightMin+"','"
	 * +molecularWeightMax+"');"); return true; } catch (Exception e) {
	 * 
	 * //e.printStackTrace(); return false; }finally{ closeConnection(); }
	 * 
	 * }
	 */
	/**
	 * getting all inhibitor und saving it in a database
	 * 
	 * @param inhibitor
	 * @return
	 */
	/*
	 * public boolean insert_Inhibitor_Table(String inhibitor) { try {
	 * con=getConnection(); s=con.createStatement();
	 * useDatabase(this.databaseName); int
	 * result=s.executeUpdate("INSERT INTO inhibitor(inhibitorName) VALUES('"
	 * +inhibitor+"');"); return true; } catch (Exception e) {
	 * 
	 * //e.printStackTrace(); return false; }finally{ closeConnection(); }
	 * 
	 * }
	 */
	/**
	 * fill parameters table, kitvale,kmvalue,....
	 * 
	 * @param table
	 * @param enzymKey
	 * @param organismKey
	 * @param minvalue
	 * @param maxvalue
	 * @return true if table can be fill
	 */
	public boolean insert_Parameters_Table(String table, int enzymKey,
			int organismKey, double minvalue, double maxvalue) {
		try {
			con = getConnection();
			s = con.createStatement();
			useDatabase(this.databaseName);

			int result = s.executeUpdate("INSERT INTO " + table
					+ " (enzymKey,organismKey,minValue,mValue) VALUES ('"
					+ enzymKey + "','" + organismKey + "','" + minvalue
					+ "', '" + maxvalue + "')");
			return true;
		} catch (Exception e) {

			// e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	/**
	 * For Parser insert maxvalue and monvalue into a defined functional
	 * parameter table
	 * 
	 * @param table
	 * @param enzymKey
	 * @param organismKey
	 * @param ligand
	 * @param minvalue
	 * @param maxvalue
	 * @return
	 */
	public boolean insert_Parameters_Table(String table, int enzymKey,
			int organismKey, String ligand, double minvalue, double maxvalue) {
		try {
			con = getConnection();
			s = con.createStatement();
			useDatabase(this.databaseName);

			int result = s
					.executeUpdate("INSERT INTO "
							+ table
							+ " (enzymKey,organismKey,ligand,minValue,mValue) VALUES ('"
							+ enzymKey + "','" + organismKey + "',NULL,'"
							+ minvalue + "', '" + maxvalue + "')");
			return true;
		} catch (Exception e) {

			// e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	/**
	 * For SOAP will fill all functional parameters Table with value
	 * 
	 * @param table
	 * @param ecNumber
	 * @param ornagnismName
	 * @param value
	 * @return
	 * @throws Exception
	 */

	// for KM table, KI Table, Turnover (SOAP)
	public boolean insert_SOAPValue(String table, int enzymKey,
			int organismKey, String ligand, double minvalue, double maxvalue) {// SOAP

		try {
			con = getConnection();
			s = con.createStatement();
			useDatabase(this.databaseName);
			int result = s.executeUpdate("INSERT INTO " + table
					+ "(enzymKey,organismKey,ligand,minValue,mValue) VALUES('"
					+ enzymKey + "','" + organismKey + "','" + ligand + "','"
					+ minvalue + "','" + maxvalue + "');");
			return true;
		} catch (Exception e) {

			// e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	/*
	 * public boolean check_if_organism_exists_in_database(String organism){
	 * 
	 * try { con=getConnection(); s=con.createStatement();
	 * useDatabase(this.databaseName); ResultSet
	 * result=s.executeQuery("SELECT '"+organism+"' FROM parameters;");
	 * if(result!=null){ //insert_Organisms_Table(organism); return true; }
	 * return false; } catch (Exception e) {
	 * 
	 * //e.printStackTrace(); return false; }finally{ closeConnection(); } }
	 * public boolean check_if_parameter_exists_in_Organisms(String organism){
	 * 
	 * try { con=getConnection(); s=con.createStatement();
	 * useDatabase(this.databaseName); ResultSet
	 * result=s.executeQuery("SELECT '"+organism+"' FROM organisms;");
	 * if(result!=null){ //insert_Organisms_Table(organism); return true; }
	 * return false; } catch (Exception e) {
	 * 
	 * //e.printStackTrace(); return false; }finally{ closeConnection(); } }
	 * 
	 * 
	 * public boolean updateFunctionParameterTable(String enzymName, String
	 * organismName,String element,double value){ try { con=getConnection();
	 * s=con.createStatement(); useDatabase(this.databaseName); int
	 * result=s.executeUpdate
	 * ("UPDATE TABLE functionalparameters(SET "+element+"="
	 * +value+" WHERE enzymName="+
	 * enzymName+"AND organismName="+organismName+");"); return true; } catch
	 * (Exception e) {
	 * 
	 * //e.printStackTrace(); return false; }finally{ closeConnection(); }
	 * 
	 * }
	 */
	/************************** QUERY DB ******************************************/
	/**
	 * accept sql queries and display result as matrix in matlab
	 * 
	 * @param query
	 * @return an object[][] matrix of values when query successful
	 */
	public Object[][] query(String query) {

		try {
			con = getConnection();
			s = con.createStatement();
			useDatabase(this.databaseName);
			ResultSet result = s.executeQuery(query);
			Object[] fields;
			HashMap<Integer, Object[]> matrixHashmap = new HashMap<Integer, Object[]>();
			Object[][] matrix;
			int numColumns = 0, numRows = 0;
			// reading result element from query
			while (result.next()) {

				System.out.println();
				numColumns = result.getMetaData().getColumnCount();
				numRows = result.getRow();
				fields = new Object[numColumns];

				for (int i = 1; i <= numColumns; i++) {

					fields[i - 1] = result.getObject(i);

				}
				matrixHashmap.put(numRows - 1, fields);

			}
			// initializing the matrix
			matrix = new Object[numRows][numColumns];
			// filling Matrix
			for (Map.Entry<Integer, Object[]> e : matrixHashmap.entrySet()) {
				Object[] save = new Object[e.getValue().length];
				save = e.getValue();
				for (int i = 0; i < save.length; i++) {

					matrix[e.getKey()][i] = save[i];
				}
			}
			// returns result
			return matrix;

		} catch (Exception e) {
			// returns empty matrix if query not successful
			return new Object[5][5];
		} finally {
			// closing connection
			closeConnection();
		}

	}
	
	// other method--> check laptop

	/* **************end of filling Database ******************* */
}
