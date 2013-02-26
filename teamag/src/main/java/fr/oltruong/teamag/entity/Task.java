package fr.oltruong.teamag.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table( name = "TM_TASK" )
@NamedQueries( { @NamedQuery( name = "findAllTasks", query = "SELECT t from Task t order by t.name, t.project" ),
    @NamedQuery( name = "findTaskByName", query = "SELECT t from Task t where (t.name=:fname and t.project=:fproject)" ) } )
@Entity
public class Task
{
    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false )
    private String name;

    private String project = "";

    private List<Member> members = new ArrayList<Member>( 1 );

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getProject()
    {
        return project;
    }

    public void setProject( String project )
    {
        this.project = project;
    }

    public List<Member> getMembers()
    {
        return members;
    }

    public void setMembers( List<Member> members )
    {
        this.members = members;
    }

    public void addMember( Member member )
    {
        if ( !this.members.contains( member ) )
        {
            this.members.add( member );
        }
    }

    public int compareTo( Task task )
    {

        return ( project + name ).compareTo( task.project + task.name );
    }

}