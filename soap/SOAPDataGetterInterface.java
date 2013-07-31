package soap;

public interface SOAPDataGetterInterface {

	public String getRecommended_Name(String enzyme) throws Exception;
	public String getSystematic_Name(String enzyme) throws Exception;
	public String getCAS_Registry_Number(String enzyme) throws Exception;
	public void getEnzyms() throws Exception;
	public void getAllOrganisms() throws Exception;
	public void getReferences(String enzyme);
	public void getMichealis_Mente_Values(String enzym);
	public void getTurnOver_number(String enzyme);
	public void getKi_Values(String enzym);
	public void getPiValue(String enzym);
	public void getPhrange(String enzym);
	public void getPhOptimum(String enzym);
	public void getTemperatureRange(String enzym);
	public void getTemperatureOptimum(String enzym);
	public void getSpecificActivity(String enzym);
	public void getParameters();
}
