package parser;

import java.util.HashMap;

public interface ParserInterface {

	public void parsePhoLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public void parsePhrLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public void parsePiLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public void parseKiLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public void parseToLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public void parseTrLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public String parseRnLine(String line);

	public void parsePrLine(String in,
			HashMap<String,String> mappingIdToOrgasnimenName);

	public void parseic50Line(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public void parseKmLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public void parseSaLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);

	public String readEnzymID(String line);

	public void parseTnLine(String in,
			HashMap<String, String> mappingIdToOrgasnimenName);
}
