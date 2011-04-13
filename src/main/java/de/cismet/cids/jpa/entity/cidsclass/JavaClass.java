/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.cidsclass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.cismet.cids.jpa.entity.common.CommonEntity;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_java_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JavaClass extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_java_class_sequence",
        sequenceName = "cs_java_class_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_java_class_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "qualifier")
    private String qualifier;

    @Column(name = "type")
    private String type;

    @Column(name = "notice")
    private String notice;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  qualifier  DOCUMENT ME!
     */
    public void setQualifier(final String qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type  DOCUMENT ME!
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getNotice() {
        return notice;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  notice  DOCUMENT ME!
     */
    public void setNotice(final String notice) {
        this.notice = notice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getQualifier() + "(" + getId() + ")"; // NOI18N
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static final class Type {

        //~ Static fields/initializers -----------------------------------------

        private static final String REND = "de.cismet.cids.tools.metaobjectrenderer.MetaObjectRenderer"; // NOI18N
        private static final String COMPLEX_ED = "Sirius.navigator.ui.attributes.editor.ComplexEditor";  // NOI18N
        private static final String SIMPLE_ED = "Sirius.navigator.ui.attributes.editor.SimpleEditor";    // NOI18N
        private static final String TO_S = "de.cismet.cids.tools.tostring.ToStringConverter";            // NOI18N
        private static final String UNKN = "unknown";                                                    // NOI18N

        public static final Type RENDERER = new Type(REND);

        public static final Type COMPLEX_EDITOR = new Type(COMPLEX_ED);

        public static final Type SIMPLE_EDITOR = new Type(SIMPLE_ED);

        public static final Type TO_STRING = new Type(TO_S);

        public static final Type UNKNOWN = new Type(UNKN);

        //~ Instance fields ----------------------------------------------------

        private final transient String type;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Type object.
         *
         * @param  type  DOCUMENT ME!
         */
        private Type(final String type) {
            this.type = type;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getType() {
            return type;
        }
    }
}
