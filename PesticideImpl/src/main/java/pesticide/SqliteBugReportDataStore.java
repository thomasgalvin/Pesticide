package pesticide;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class SqliteBugReportDataStore implements BugReportDataStore
{
    private final String connectionUrl;
    private final SqlProvider sql;
    
    /**
     * Connects to or creates the database using the specified file.
     * 
     * All tables will be created if necessary.
     * 
     * @param database the location of the database file
     * @param sql the SQL provider
     * 
     * @throws PersistenceException if we are unable to connect to 
     * or create the database.
     */
    public SqliteBugReportDataStore( File database, SqlProvider sql ) throws PersistenceException{
        this.connectionUrl = "jdbc:sqlite:" + database.getAbsolutePath();
        this.sql = sql;
        loadDriver();
        createTables();
    }
    
    private static void loadDriver() throws PersistenceException {
        try {
            Class.forName( "org.sqlite.JDBC" );
        }
        catch( ClassNotFoundException ex ) {
            throw new PersistenceException( "SQLite JDBC driver not found", ex );
        }
    }
    
    private void createTables() throws PersistenceException {
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            connection.setAutoCommit( false );

            PreparedStatement statement = connection.prepareStatement( sql.createTableBugReport() );
            statement.executeUpdate();

            connection.commit();
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error creating database", ex );
        }
    }
    
    @Override
    public String store( BugReport report ) throws PersistenceException {
        if( report == null ) {
            throw new NullPointerException( "User cannot be null" );
        }

        boolean exists = false;
        
        if( StringUtils.isBlank( report.getUuid() ) ){
            report.setUuid( UUID.randomUUID().toString() );
        }
        else {
            exists = exists( report.getUuid() );
        }
        
        if( exists ){
            update( report );
        }
        else {
            create( report );
        }

        return report.getUuid();
    }
    
    private void create( BugReport report ) throws PersistenceException{
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            connection.setAutoCommit( false );
            
            String type = report.getType() != null ? report.getType().name() : null;
            String status = report.getStatus() != null ? report.getStatus().name() : null;
            String priority = report.getPriority()!= null ? report.getPriority().name() : null;
            
            PreparedStatement statement = connection.prepareStatement( sql.createBugReport() );
            statement.setString( 1, report.getUuid() );
            statement.setString( 2, report.getTitle() );
            statement.setLong( 3, report.getLastUpdated() );
            statement.setString( 4, report.getProject() );
            statement.setString( 5, report.getArea() );
            statement.setString( 6, report.getVersions() );
            statement.setString( 7, report.getFixedVersion() );
            statement.setString( 8, type );
            statement.setString( 9, status );
            statement.setString( 10, priority );
            statement.setString( 11, report.getDescription() );
            statement.setString( 12, report.getAssignedTo() );
            statement.setString( 13, report.getReportedBy() );
            
            statement.executeUpdate();
            connection.commit();
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error updating database", ex );
        }
    }
    
    @Override
    public BugReport retrieve( String uuid ) throws PersistenceException {
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            PreparedStatement statement = connection.prepareStatement( sql.retrieve() );
            statement.setString( 1, uuid );
            ResultSet results = statement.executeQuery();

            BugReport result = null;

            if( results.next() ) {
                result = unmarshall( results );
            }

            return result;
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error querying database", ex );
        }
    }
    
    @Override
    public List<BugReport> retrieveAll() throws PersistenceException{
        List<BugReport> result = new ArrayList();
        
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            PreparedStatement statement = connection.prepareStatement( sql.retrieveAll() );
            ResultSet results = statement.executeQuery();

            while( results.next() ) {
                BugReport report = unmarshall( results );
                result.add( report );
            }
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error querying database", ex );
        }
        
        return result;
    }
    
    private BugReport unmarshall( ResultSet results ) throws SQLException {
        BugReport report = new BugReport();
        report.setUuid( results.getString( "uuid" ) );
        report.setTitle( results.getString( "title" ) );
        report.setLastUpdated( results.getLong( "lastUpdated" ) );
        report.setProject( results.getString( "project" ) );
        report.setArea( results.getString( "area" ) );
        report.setVersions( results.getString( "versions" ) );
        report.setFixedVersion( results.getString( "fixedVersion" ) );
        report.setDescription( results.getString( "description" ) );
        report.setAssignedTo( results.getString( "assignedTo" ) );
        report.setReportedBy( results.getString( "reportedBy" ) );
        
        String type = results.getString( "type" ) ;
        if( !StringUtils.isBlank( type ) ){
            Type theType = Type.valueOf( type );
            report.setType( theType );
        }
        
        String status = results.getString( "status" ) ;
        if( !StringUtils.isBlank( status ) ){
            Status theStatus = Status.valueOf( status );
            report.setStatus( theStatus );
        }
        
        String priority = results.getString( "priority" ) ;
        if( !StringUtils.isBlank( priority ) ){
            Priority thePriority = Priority.valueOf( priority );
            report.setPriority( thePriority );
        }
        
        return report;
    }

    @Override
    public boolean delete( String uuid ) throws PersistenceException{
        if( StringUtils.isBlank( uuid ) ) {
            return false;
        }

        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            connection.setAutoCommit( false );
            
            PreparedStatement statement = connection.prepareStatement( sql.delete() );
            statement.setString( 1, uuid );
            int usersDeleted = statement.executeUpdate();
            
            connection.commit();
            return usersDeleted != 0;
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error deleting from database", ex );
        }
    }
    
    @Override
    public boolean exists( String uuid ) throws PersistenceException {
        if( StringUtils.isBlank( uuid ) ) {
            return false;
        }

        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            PreparedStatement statement = connection.prepareStatement( sql.exists() );
            statement.setString( 1, uuid );
            ResultSet results = statement.executeQuery();
            return results.next();
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error querying database", ex );
        }
    }
    
    private void update( BugReport report ) throws PersistenceException{
        delete( report.getUuid() );
        store( report );
    }
    
    @Override
    public boolean updateStatus( String uuid, Status value ) throws PersistenceException{
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            connection.setAutoCommit( false );

            String theStatus = value != null ? value.name() : null;            
            PreparedStatement statement = connection.prepareStatement( sql.updateStatus() );
            statement.setString( 1, theStatus );
            statement.setString( 2, uuid );
            
            int count = statement.executeUpdate();
            connection.commit();
            return count != 0;
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error updating database", ex );
        }
    }
    
    @Override
    public boolean updatePriority( String uuid, Priority value ) throws PersistenceException{
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            connection.setAutoCommit( false );

            String thePriority = value != null ? value.name() : null;            
            PreparedStatement statement = connection.prepareStatement( sql.updatePriority() );
            statement.setString( 1, thePriority );
            statement.setString( 2, uuid );
            
            int count = statement.executeUpdate();
            connection.commit();
            return count != 0;
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error updating database", ex );
        }
    }
    
    @Override
    public boolean updateDescription( String uuid, String value ) throws PersistenceException {
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            connection.setAutoCommit( false );

            PreparedStatement statement = connection.prepareStatement( sql.updateDescription() );
            statement.setString( 1, value );
            statement.setString( 2, uuid );
            
            int count = statement.executeUpdate();
            connection.commit();
            return count != 0;
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error updating database", ex );
        }
    }
    
    @Override
    public boolean updateAssignedTo( String uuid, String value ) throws PersistenceException {
        try ( Connection connection = DriverManager.getConnection( connectionUrl ) ) {
            connection.setAutoCommit( false );

            PreparedStatement statement = connection.prepareStatement( sql.updateAssignedTo() );
            statement.setString( 1, value );
            statement.setString( 2, uuid );
            
            int count = statement.executeUpdate();
            connection.commit();
            return count != 0;
        }
        catch( SQLException ex ) {
            throw new PersistenceException( "Error updating database", ex );
        }
    }
}
