package database;

public interface DBCreatorInterface {

	public boolean createTableEnzym();

	public boolean createTablePhoptimum();

	public boolean createTableReference();

	public boolean createTablePhrange();

	public boolean createTableTempoptimum();

	public boolean createTableTemprange();

	public boolean createTableSpecificactivity();

	public boolean createTableTurnover();

	public boolean createTableKmvalue();

	public boolean createTablePivalue();

	public boolean createTableKivalue();

	public boolean createTableIc50value();

	public boolean createTableorganism();
}
