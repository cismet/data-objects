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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.PermissionAwareEntity;
import de.cismet.cids.jpa.entity.permission.AbstractPermission;
import de.cismet.cids.jpa.entity.permission.ClassPermission;
import de.cismet.cids.jpa.entity.permission.Policy;

import de.cismet.tools.Equals;

/**
 * CidsClass objects are expensive so query them wisely.
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// this is only a bean class not real functionality at all
@SuppressWarnings({ "PMD.GodClass" })
public class CidsClass extends CommonEntity implements Serializable, PermissionAwareEntity {

    //~ Static fields/initializers ---------------------------------------------

    public static final CidsClass NO_CLASS;

    static {
        NO_CLASS = new CidsClass();
        NO_CLASS.setName("<keine Klasse>");      // TODO: I18N
        NO_CLASS.setTableName("<keine Klasse>"); // TODO: I18N
    }

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_class_sequence",
        sequenceName = "cs_class_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_class_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "descr")
    private String description;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "class_icon_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Icon classIcon;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "object_icon_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Icon objectIcon;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "primary_key_field")
    private String primaryKeyField;

    @Column(name = "indexed")
    private Boolean indexed;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "tostring",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private JavaClass toString;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "editor",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private JavaClass editor;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "renderer",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private JavaClass renderer;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "policy",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Policy policy;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "attribute_policy",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Policy attributePolicy;

    @Column(name = "array_link")
    private Boolean arrayLink;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "cidsClass", // NOPMD
        orphanRemoval = true
    )
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Attribute> attributes;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "cidsClass", // NOPMD
        orphanRemoval = true
    )
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ClassAttribute> classAttributes;

    @OneToOne(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "cidsClass", // NOPMD
        orphanRemoval = true
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Type type;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "cidsClass", // NOPMD
        orphanRemoval = true
    )
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ClassPermission> classPermissions;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsClass object.
     */
    public CidsClass() {
        // avoid npe if not null constraint is not set
        indexed = false;
        // avoid npe if not null constraint is not set
        arrayLink = false;
        attributes = new HashSet<Attribute>();
        classAttributes = new HashSet<ClassAttribute>();
        classPermissions = new HashSet<ClassPermission>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  description  DOCUMENT ME!
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Icon getClassIcon() {
        return classIcon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classIcon  DOCUMENT ME!
     */
    public void setClassIcon(final Icon classIcon) {
        this.classIcon = classIcon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Icon getObjectIcon() {
        return objectIcon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  objectIcon  DOCUMENT ME!
     */
    public void setObjectIcon(final Icon objectIcon) {
        this.objectIcon = objectIcon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tableName  DOCUMENT ME!
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getPrimaryKeyField() {
        return primaryKeyField;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  primaryKeyField  DOCUMENT ME!
     */
    public void setPrimaryKeyField(final String primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean isIndexed() {
        return indexed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  indexed  DOCUMENT ME!
     */
    public void setIndexed(final Boolean indexed) {
        this.indexed = indexed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JavaClass getToString() {
        return toString;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  toString  DOCUMENT ME!
     */
    public void setToString(final JavaClass toString) {
        this.toString = toString;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JavaClass getEditor() {
        return editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  editor  DOCUMENT ME!
     */
    public void setEditor(final JavaClass editor) {
        this.editor = editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JavaClass getRenderer() {
        return renderer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  renderer  DOCUMENT ME!
     */
    public void setRenderer(final JavaClass renderer) {
        this.renderer = renderer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean isArrayLink() {
        return arrayLink;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  arrayLink  DOCUMENT ME!
     */
    public void setArrayLink(final Boolean arrayLink) {
        this.arrayLink = arrayLink;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<ClassAttribute> getClassAttributes() {
        return classAttributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classAttributes  DOCUMENT ME!
     */
    public void setClassAttributes(final Set<ClassAttribute> classAttributes) {
        this.classAttributes = classAttributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attributes  DOCUMENT ME!
     */
    public void setAttributes(final Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Type getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type  DOCUMENT ME!
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<ClassPermission> getClassPermissions() {
        return classPermissions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classPermissions  DOCUMENT ME!
     */
    public void setClassPermissions(final Set<ClassPermission> classPermissions) {
        this.classPermissions = classPermissions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getTableName();
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

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Policy getPolicy() {
        return policy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  policy  DOCUMENT ME!
     */
    public void setPolicy(final Policy policy) {
        this.policy = policy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Policy getAttributePolicy() {
        return attributePolicy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attributePolicy  DOCUMENT ME!
     */
    public void setAttributePolicy(final Policy attributePolicy) {
        this.attributePolicy = attributePolicy;
    }

    // TODO: check whether it is possible to fully replace the getClassPermissions method with this one
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<? extends AbstractPermission> getPermissions() {
        return getClassPermissions();
    }

    @Override
    public boolean equals(final Object o) {
        if (!super.equals(o)) {
            return false;
        }

        return Equals.beanDeepEqual(this, o);
    }

    @Override
    @SuppressWarnings("PMD.ConfusingTernary")
    public int hashCode() {
        int hash = 3;
        hash = (67 * hash) + ((this.getId() != null) ? this.getId().hashCode() : 0);
        hash = (67 * hash) + ((this.getName() != null) ? this.getName().hashCode() : 0);
        hash = (67 * hash) + ((this.getDescription() != null) ? this.getDescription().hashCode() : 0);
        hash = (67 * hash) + ((this.getClassIcon() != null) ? this.getClassIcon().hashCode() : 0);
        hash = (67 * hash) + ((this.getObjectIcon() != null) ? this.getObjectIcon().hashCode() : 0);
        hash = (67 * hash) + ((this.getTableName() != null) ? this.getTableName().hashCode() : 0);
        hash = (67 * hash) + ((this.getPrimaryKeyField() != null) ? this.getPrimaryKeyField().hashCode() : 0);
        hash = (67 * hash) + ((this.isIndexed() != null) ? this.isIndexed().hashCode() : 0);
        hash = (67 * hash) + ((this.getToString() != null) ? this.getToString().hashCode() : 0);
        hash = (67 * hash) + ((this.getEditor() != null) ? this.getEditor().hashCode() : 0);
        hash = (67 * hash) + ((this.getRenderer() != null) ? this.getRenderer().hashCode() : 0);
        hash = (67 * hash) + ((this.getPolicy() != null) ? this.getPolicy().hashCode() : 0);
        hash = (67 * hash) + ((this.getAttributePolicy() != null) ? this.getAttributePolicy().hashCode() : 0);
        hash = (67 * hash) + ((this.isArrayLink() != null) ? this.isArrayLink().hashCode() : 0);
        hash = (67 * hash) + ((this.getAttributes() != null) ? this.getAttributes().hashCode() : 0);
        hash = (67 * hash) + ((this.getClassAttributes() != null) ? this.getClassAttributes().hashCode() : 0);
        hash = (67 * hash) + ((this.getType() != null) ? this.getType().hashCode() : 0);
        hash = (67 * hash) + ((this.getClassPermissions() != null) ? this.getClassPermissions().hashCode() : 0);

        return hash;
    }
}
