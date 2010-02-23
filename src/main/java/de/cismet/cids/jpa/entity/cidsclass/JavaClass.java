package de.cismet.cids.jpa.entity.cidsclass;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity()
@Table(name = "cs_java_class")
public class JavaClass extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_java_class_sequence",
            sequenceName = "cs_java_class_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_java_class_sequence")
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "qualifier")
    private String qualifier;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "notice")
    private String notice;
   
    public JavaClass()
    {
        this.qualifier = null;
        this.type = null;
        this.notice = null;
    }
    
    public String getQualifier()
    {
        return qualifier;
    }
    
    public void setQualifier(final String qualifier)
    {
        this.qualifier = qualifier;
    }

    public String getType()
    {
        return type;
    }

    public void setType(final String type)
    {
        this.type = type;
    }

    public String getNotice()
    {
        return notice;
    }

    public void setNotice(final String notice)
    {
        this.notice = notice;
    }
    
    public String toString()
    {
        return getQualifier() + "(" + getId() + ")";
    }
    
    public static final class Type
    {
        private static final String REND =
                "de.cismet.cids.tools.metaobjectrenderer.MetaObjectRenderer";
        private static final String COMPLEX_ED =
                "Sirius.navigator.ui.attributes.editor.ComplexEditor";
        private static final String SIMPLE_ED =
                "Sirius.navigator.ui.attributes.editor.SimpleEditor";
        private static final String TO_S =
                "de.cismet.cids.tools.tostring.ToStringConverter";
        private static final String UNKN =
                "unknown";
        
        public static final Type RENDERER = new Type(REND);
                
        public static final Type COMPLEX_EDITOR = new Type(COMPLEX_ED);
                
        public static final Type SIMPLE_EDITOR = new Type(SIMPLE_ED);
                
        public static final Type TO_STRING = new Type(TO_S);
                
        public static final Type UNKNOWN = new Type(UNKN);
        
        private final String type;
        
        private Type(final String type)
        {
            this.type = type;
        }
        
        public String getType()
        {
            return type;
        }
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}