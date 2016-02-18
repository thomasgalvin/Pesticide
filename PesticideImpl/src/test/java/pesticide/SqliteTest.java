package pesticide;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class SqliteTest
{
    @Test
    public void doTest() throws Exception {
        File file = new File( "target/bugs.db" );
        SqliteBugReportDataStore dataStore = new SqliteBugReportDataStore( file, new SqliteProvider() );
        
        BugReport report = create();
        String uuid = dataStore.store( report );
        
        boolean exists = dataStore.exists( uuid );
        Assert.assertTrue( "UUID did not exist", exists );
        
        BugReport loaded = dataStore.retrieve( uuid );
        Assert.assertEquals( "Loaded bug report did not match original", report, loaded );
        
        report.setDescription( "new description" );
        dataStore.store( report );
        loaded = dataStore.retrieve( uuid );
        Assert.assertEquals( "Loaded bug report did not match updated", report, loaded );
        
        report.setStatus( Status.WontFix );
        dataStore.updateStatus( uuid, Status.WontFix );
        loaded = dataStore.retrieve( uuid );
        Assert.assertEquals( "Loaded bug report did not match updated status", report, loaded );
        
        report.setPriority( Priority.Medium );
        dataStore.updatePriority( uuid, Priority.Medium );
        loaded = dataStore.retrieve( uuid );
        Assert.assertEquals( "Loaded bug report did not match updated priority", report, loaded );
        
        report.setDescription( "even newer description" );
        dataStore.updateDescription( uuid, "even newer description");
        loaded = dataStore.retrieve( uuid );
        Assert.assertEquals( "Loaded bug report did not match updated description", report, loaded );
        
        String assignedTo = UUID.randomUUID().toString();
        report.setAssignedTo( assignedTo );
        dataStore.updateAssignedTo( uuid, assignedTo );
        loaded = dataStore.retrieve( uuid );
        Assert.assertEquals( "Loaded bug report did not match updated assignment", report, loaded );

        int expectedCount = 1;
        List<BugReport> reports = dataStore.retrieveAll();
        Assert.assertNotNull( "Bug report list was null", reports );
        Assert.assertEquals( "Wrong bug report count", expectedCount, reports.size() );
        
        for( int i = 0; i < 10; i++ ){
            expectedCount++;
            BugReport newReport = create();
            String newUuid = dataStore.store( newReport );
            loaded = dataStore.retrieve( newUuid );
            Assert.assertEquals( "Loaded bug report did not match updated assignment", newReport, loaded );
            
            reports = dataStore.retrieveAll();
            Assert.assertNotNull( "Bug report list was null", reports );
            Assert.assertEquals( "Wrong bug report count", expectedCount, reports.size() );
        }
        
        boolean deleted = dataStore.delete( uuid );
        Assert.assertTrue( "Delete returned false", deleted );
        
        exists = dataStore.exists( uuid );
        Assert.assertFalse( "UUID should not exist", exists );
    }
    
    private int count = 0;
    private BugReport create(){
        BugReport report = new BugReport();
        report.setArea( "area" + count);
        report.setAssignedTo( "assignedTo" + count );
        report.setDescription( "description" + count );
        report.setFixedVersion( "fixedVersion" + count );
        report.setLastUpdated( new Date().getTime() );
        report.setPriority( Priority.Critical );
        report.setProject( "project" + count );
        report.setReportedBy( "reportedBy" + count );
        report.setStatus( Status.New );
        report.setTitle( "title" + count );
        report.setType( Type.Error );
        report.setVersions( "versions" + count );
        return report;
    }
}
