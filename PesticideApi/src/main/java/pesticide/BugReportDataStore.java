package pesticide;

import java.util.List;

public interface BugReportDataStore
{
    public String store( BugReport report ) throws PersistenceException;
    public boolean exists( String uuid ) throws PersistenceException;
    public BugReport retrieve( String uuid ) throws PersistenceException;
    public List<BugReport> retrieveAll() throws PersistenceException;
    public boolean delete( String uuid ) throws PersistenceException;
    
    public boolean updateStatus( String uuid, Status value ) throws PersistenceException;
    public boolean updatePriority( String uuid, Priority value ) throws PersistenceException;
    public boolean updateDescription( String uuid, String value ) throws PersistenceException;
    public boolean updateAssignedTo( String uuid, String value ) throws PersistenceException;
}
