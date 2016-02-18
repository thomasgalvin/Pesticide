package pesticide;

import java.util.Objects;
import java.util.UUID;

public class BugReport implements Cloneable
{
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private long lastUpdated;
    
    private String project;
    private String area;
    private String versions;
    private String fixedVersion;
    
    private Type type;
    private Status status;
    private Priority priority;
    
    /*
     * A good bug report has three things:
     * 1. Steps to reproduce
     * 2. What you saw
     * 3. What you expected to see
     * 
     * The UI should gently prompt for all three.
     * 
     * See:
     * http://www.joelonsoftware.com/articles/fog0000000029.html
     */
    private String description;
    
    private String assignedTo;
    private String reportedBy;

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid( String uuid ) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated( long lastUpdated ) {
        this.lastUpdated = lastUpdated;
    }

    public String getProject() {
        return project;
    }

    public void setProject( String project ) {
        this.project = project;
    }

    public String getArea() {
        return area;
    }

    public void setArea( String area ) {
        this.area = area;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions( String versions ) {
        this.versions = versions;
    }

    public String getFixedVersion() {
        return fixedVersion;
    }

    public void setFixedVersion( String fixedVersion ) {
        this.fixedVersion = fixedVersion;
    }

    public Type getType() {
        return type;
    }

    public void setType( Type type ) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus( Status status ) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority( Priority priority ) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo( String assignedTo ) {
        this.assignedTo = assignedTo;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy( String reportedBy ) {
        this.reportedBy = reportedBy;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Equals and Hashcode">

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode( this.uuid );
        hash = 83 * hash + Objects.hashCode( this.title );
        hash = 83 * hash + (int) ( this.lastUpdated ^ ( this.lastUpdated >>> 32 ) );
        hash = 83 * hash + Objects.hashCode( this.project );
        hash = 83 * hash + Objects.hashCode( this.area );
        hash = 83 * hash + Objects.hashCode( this.versions );
        hash = 83 * hash + Objects.hashCode( this.fixedVersion );
        hash = 83 * hash + Objects.hashCode( this.type );
        hash = 83 * hash + Objects.hashCode( this.status );
        hash = 83 * hash + Objects.hashCode( this.priority );
        hash = 83 * hash + Objects.hashCode( this.description );
        hash = 83 * hash + Objects.hashCode( this.assignedTo );
        hash = 83 * hash + Objects.hashCode( this.reportedBy );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) {
            return true;
        }
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final BugReport other = (BugReport) obj;
        if( this.lastUpdated != other.lastUpdated ) {
            return false;
        }
        if( !Objects.equals( this.uuid, other.uuid ) ) {
            return false;
        }
        if( !Objects.equals( this.title, other.title ) ) {
            return false;
        }
        if( !Objects.equals( this.project, other.project ) ) {
            return false;
        }
        if( !Objects.equals( this.area, other.area ) ) {
            return false;
        }
        if( !Objects.equals( this.versions, other.versions ) ) {
            return false;
        }
        if( !Objects.equals( this.fixedVersion, other.fixedVersion ) ) {
            return false;
        }
        if( !Objects.equals( this.description, other.description ) ) {
            return false;
        }
        if( !Objects.equals( this.assignedTo, other.assignedTo ) ) {
            return false;
        }
        if( !Objects.equals( this.reportedBy, other.reportedBy ) ) {
            return false;
        }
        if( this.type != other.type ) {
            return false;
        }
        if( this.status != other.status ) {
            return false;
        }
        if( this.priority != other.priority ) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Clone">
    
    @Override
    public BugReport clone() {
        BugReport result = new BugReport();
        result.uuid = uuid;
        result.title = title;
        result.lastUpdated = lastUpdated;
        result.project = project;
        result.area = area;
        result.versions = versions;
        result.fixedVersion = fixedVersion;
        result.type = type;
        result.status = status;
        result.priority = priority;
        result.description = description;
        result.assignedTo = assignedTo;
        result.reportedBy = reportedBy;
        
        return result;
    }
    
    // </editor-fold>
    
}
