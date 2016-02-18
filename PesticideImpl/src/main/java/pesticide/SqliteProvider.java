package pesticide;

/**
 * Provides SQL with place holders for a prepared statement. This version is
 * compatible with SQLite, but can be extended / overridden to provide 
 * support for other databases.
 */
public class SqliteProvider implements SqlProvider
{
    private static final String CREATE_TABLE = 
        "create table if not exists bugReports ("
      + "uuid text primary key,"
      + "title text,"
      + "lastUpdated integer,"
      
      + "project text,"
      + "area text,"
      + "versions text,"
      + "fixedVersion text,"
      
      + "type text,"
      + "status text,"
      + "priority text,"
        
      + "description text,"
      + "assignedTo text,"
      + "reportedBy text"
      + ");";
    @Override
    public String createTableBugReport(){
        return CREATE_TABLE;
    }
    
    private static final String CREATE = 
      "insert into bugReports "
    + "       (uuid, title, lastUpdated, project, area, versions, fixedVersion, type, status, priority, description, assignedTo, reportedBy)"
    + "values (?,    ?,     ?,           ?,       ?,    ?,        ?,            ?,    ?,      ?,        ?,           ?,          ?)";
    @Override
    public String createBugReport(){
        return CREATE;
    }
    
    private static final String EXISTS = "select uuid as uuid from bugReports where uuid = ?;";
    @Override
    public String exists(){
        return EXISTS;
    }
    
    private static final String DELETE = "delete from bugReports where uuid = ?;";
    @Override
    public String delete(){
        return DELETE;
    }
    
    private static final String RETRIEVE = 
        "select uuid as uuid,"
      + "       title as title,"
      + "       lastUpdated as lastUpdated,"
      + "       project as project,"
      + "       area as area,"
      + "       versions as versions,"
      + "       fixedVersion as fixedVersion,"
      + "       type as type,"
      + "       status as status,"
      + "       priority as priority,"
      + "       description as description,"
      + "       assignedTo as assignedTo,"
      + "       reportedBy as reportedBy"
      + " from bugReports where uuid = ?";
    @Override
    public String retrieve(){
        return RETRIEVE;
    }
    
    private static final String RETRIEVE_ALL = 
        "select uuid as uuid,"
      + "       title as title,"
      + "       lastUpdated as lastUpdated,"
      + "       project as project,"
      + "       area as area,"
      + "       versions as versions,"
      + "       fixedVersion as fixedVersion,"
      + "       type as type,"
      + "       status as status,"
      + "       priority as priority,"
      + "       description as description,"
      + "       assignedTo as assignedTo,"
      + "       reportedBy as reportedBy"
      + " from bugReports";
    @Override
    public String retrieveAll(){
        return RETRIEVE_ALL;
    }
    
    private static final String UPDATE_STATUS =  "update bugReports set status = ? where uuid = ?";
    @Override
    public String updateStatus(){
        return UPDATE_STATUS;
    }
    
    private static final String UPDATE_PRIORITY =  "update bugReports set priority = ? where uuid = ?";
    @Override
    public String updatePriority(){
        return UPDATE_PRIORITY;
    }
    
    private static final String UPDATE_DESCRIPTION =  "update bugReports set description = ? where uuid = ?";
    @Override
    public String updateDescription(){
        return UPDATE_DESCRIPTION;
    }
    
    private static final String UPDATE_ASSIGNED_TO =  "update bugReports set assignedTo = ? where uuid = ?";
    @Override
    public String updateAssignedTo(){
        return UPDATE_ASSIGNED_TO;
    }
}
