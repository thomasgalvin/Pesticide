package pesticide;

/**
 * Provides SQL with place holders for a prepared statement.
 */
public interface SqlProvider
{
    public String createBugReport();
    public String createTableBugReport();
    public String delete();
    public String exists();
    public String retrieve();
    public String retrieveAll();
    public String updateAssignedTo();
    public String updateDescription();
    public String updatePriority();
    public String updateStatus();
}
