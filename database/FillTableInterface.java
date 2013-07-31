package database;

public interface FillTableInterface {
	public boolean insert_Enzyms_Table(String ecNumber, String enzymName);

	public boolean insert_Enzyms_Table(String ecNumber, String enzymName,
			String systematicName, String casRegistry);

	public boolean insert_Organisms_Table(String ornagnismName);

	public boolean insert_Reference_Table(int enzymId, int organismId,
			String reference, String autors, String title, String journal,
			String volume, String pages);

	public boolean insert_Parameters_Table(String table, int enzymKey,
			int organismKey, double minvalue, double maxvalue);

	public boolean insert_Parameters_Table(String table, int enzymKey,
			int organismKey, String ligand, double minvalue, double maxvalue);

	public boolean insert_SOAPValue(String table, int enzymKey,
			int organismKey, String ligand, double minvalue, double maxvalue);

}
