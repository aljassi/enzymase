package database;

/**
 * This class contains all necessary queries for generating tables in the
 * database.
 * 
 * @author aassi
 * 
 */

public abstract class MysqlQueries {

	/**
	 * Statement to create table for element in a database
	 */

	public static final String create_pho = "CREATE TABLE phoptimum(phoId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(phoId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_Enzym = "CREATE TABLE enzyms(enzymKey INTEGER NOT NULL AUTO_INCREMENT, ecNumber VARCHAR(25), enzymName VARCHAR(300), systematicName VARCHAR(400),registryNumber VARCHAR(50),PRIMARY KEY(enzymKey));",
			create_phr = "CREATE TABLE phrange(phrId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(phrId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_to = "CREATE TABLE temperatureoptimum(toId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(toId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_tr = "CREATE TABLE temperaturerange(trId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(trId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_sa = "CREATE TABLE specificactivity(saId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(saId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_ic50 = "CREATE TABLE ic50value(ic50Id INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(ic50Id), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_tn = "CREATE TABLE turnover(tnId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,ligand VARCHAR(300),minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(tnId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_km = "CREATE TABLE kmvalue(kmId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,ligand VARCHAR(300),minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(kmId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_ki = "CREATE TABLE kivalue(kiId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,ligand VARCHAR(300),minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(kiId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_pi = "CREATE TABLE pivalue(piId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,minValue DOUBLE,mValue DOUBLE,PRIMARY KEY(piId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);",
			create_Organism = "CREATE TABLE organisms(organismKey INTEGER NOT NULL AUTO_INCREMENT, organismName VARCHAR(150),PRIMARY KEY(organismKey));",
			create_Reference = "CREATE TABLE literature(reId INTEGER NOT NULL AUTO_INCREMENT,enzymKey INTEGER NOT NULL,organismKey INTEGER NOT NULL,ref VARCHAR(50),autors VARCHAR(1000),title VARCHAR(1000),journal VARCHAR(150),volume VARCHAR(50),page VARCHAR(50),PRIMARY KEY(reId), FOREIGN KEY(enzymKey) REFERENCES enzyms(enzymKey) ON DELETE CASCADE,FOREIGN KEY(organismKey) REFERENCES organisms(organismKey) ON DELETE CASCADE);";

	/**
	 * Statement for statistical demand
	 * 
	 * @see Matlab class
	 */
	public static final String select_Enym_Organism_KmValue = "SELECT ecNumber,organismName,avg(kmValueMin) FROM kmvalue INNER JOIN enzyms ON "
			+ "enzymKey=functionalparameters.enzymKey AND INNER JOIN organisms ON organismKey=functionalparameters.organismKey GROUP BY (ecNumber,organismName);",
			meistUntersuchtenEnzym = "SELECT ecNumber,enzymName, count(ecNumber) FROM functionalparameters INNER JOIN enzyms ON enzymKey=functionalparameters.enzymKey;";
	String s = "SELECT ecNumber as Enzym, organismName as Organism, avg( kmvalue.minValue ) as Mittelwert,count(ecNumber) as Anzahl"
			+ " FROM kmvalue  INNER JOIN organisms ON organisms.organismKey = kmvalue.organismKey"
			+ " JOIN enzyms ON enzyms.enzymKey = kmvalue.enzymKey GROUP BY ecNumber,organismName";

	// SELECT ecNumber, organisms.organismName,kmvalue.minValue,
	// avg(kmvalue.minValue )
	// FROM kmvalue join organisms on organisms.organismKey=kmvalue.organismKey
	// join enzyms on enzyms.enzymKey=kmvalue.enzymKey
	// GROUP BY (
	// ecNumber
	// )

	/**
	 * MDX Statement for OLAP representation
	 * 
	 * @see Matlab class
	 */

}
