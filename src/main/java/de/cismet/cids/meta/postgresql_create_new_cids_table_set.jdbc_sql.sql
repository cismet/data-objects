-- nur einzeilige kommentare erlaubt

-- erster abschnitt cs_-Tabellen aus Dump erzeugt
--  pg_dump -d correct_cids_db -O -s  --table="public.cs*"
-------------------------------------------------

--
-- PostgreSQL database dump
--

SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- Name: cs_all_attr_mapping_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_all_attr_mapping_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cs_all_attr_mapping; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_all_attr_mapping (
    class_id integer NOT NULL,
    object_id integer NOT NULL,
    attr_class_id integer NOT NULL,
    attr_object_id integer NOT NULL,
    id integer DEFAULT nextval('cs_all_attr_mapping_sequence'::regclass) NOT NULL
);


--
-- Name: cs_attr; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_attr (
    id integer DEFAULT nextval(('cs_attr_sequence'::text)::regclass) NOT NULL,
    class_id integer NOT NULL,
    type_id integer NOT NULL,
    name character varying(100) NOT NULL,
    field_name character varying(50) NOT NULL,
    foreign_key boolean DEFAULT false NOT NULL,
    substitute boolean DEFAULT false NOT NULL,
    foreign_key_references_to integer,
    descr text,
    visible boolean DEFAULT true NOT NULL,
    indexed boolean DEFAULT false NOT NULL,
    isarray boolean DEFAULT false NOT NULL,
    array_key character varying(30),
    editor integer,
    tostring integer,
    complex_editor integer,
    optional boolean DEFAULT true NOT NULL,
    default_value character varying(100),
    from_string integer,
    pos integer DEFAULT 0,
    "precision" integer,
    scale integer
);


--
-- Name: cs_attr_string_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_attr_string_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_attr_string; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_attr_string (
    class_id integer NOT NULL,
    attr_id integer NOT NULL,
    object_id integer NOT NULL,
    string_val text NOT NULL,
    id integer DEFAULT nextval('cs_attr_string_sequence'::regclass) NOT NULL
);


--
-- Name: cs_cat_link_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_cat_link_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_cat_link; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_cat_link (
    id_from integer NOT NULL,
    id_to integer NOT NULL,
    org text,
    domain_to integer,
    id integer DEFAULT nextval('cs_cat_link_sequence'::regclass) NOT NULL
);


--
-- Name: cs_cat_node; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_cat_node (
    id integer DEFAULT nextval(('cs_cat_node_sequence'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    descr integer DEFAULT 1,
    class_id integer,
    object_id integer,
    node_type character(1) DEFAULT 'N'::bpchar NOT NULL,
    is_root boolean DEFAULT false NOT NULL,
    org text,
    dynamic_children text,
    sql_sort boolean,
    policy integer,
    derive_permissions_from_class boolean DEFAULT true,
    iconfactory integer,
    icon character varying(512)
);


--
-- Name: cs_class; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_class (
    id integer DEFAULT nextval(('cs_class_sequence'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    descr text,
    class_icon_id integer NOT NULL,
    object_icon_id integer NOT NULL,
    table_name character varying(100) NOT NULL,
    primary_key_field character varying(100) DEFAULT 'ID'::character varying NOT NULL,
    indexed boolean DEFAULT false NOT NULL,
    tostring integer,
    editor integer,
    renderer integer,
    array_link boolean DEFAULT false NOT NULL,
    policy integer,
    attribute_policy integer
);


--
-- Name: cs_class_attr_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_class_attr_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_class_attr; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_class_attr (
    id integer DEFAULT nextval('cs_class_attr_sequence'::regclass) NOT NULL,
    class_id integer NOT NULL,
    type_id integer NOT NULL,
    attr_key character varying(100) NOT NULL,
    attr_value text
);


--
-- Name: cs_domain_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_domain_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_domain; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_domain (
    id integer DEFAULT nextval('cs_domain_sequence'::regclass) NOT NULL,
    name character varying(30)
);


--
-- Name: cs_icon_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_icon_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_icon; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_icon (
    id integer DEFAULT nextval('cs_icon_sequence'::regclass) NOT NULL,
    name character varying(32) NOT NULL,
    file_name character varying(100) DEFAULT 'default_icon.gif'::character varying NOT NULL
);


--
-- Name: cs_java_class_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_java_class_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_java_class; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_java_class (
    id integer DEFAULT nextval('cs_java_class_sequence'::regclass) NOT NULL,
    qualifier character varying(500),
    type character varying(500) DEFAULT 'unknown'::character varying NOT NULL,
    notice character varying(500)
);


--
-- Name: cs_locks_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_locks_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_locks; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_locks (
    class_id integer,
    object_id integer,
    user_string character varying(256),
    additional_info character varying(256),
    id integer DEFAULT nextval('cs_locks_sequence'::regclass) NOT NULL
);


--
-- Name: cs_method; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_method (
    id integer DEFAULT nextval(('cs_method_sequence'::text)::regclass) NOT NULL,
    descr text,
    mult boolean DEFAULT false NOT NULL,
    class_mult boolean DEFAULT false NOT NULL,
    plugin_id character varying(30) DEFAULT ''::character varying NOT NULL,
    method_id character varying(100) DEFAULT ''::character varying NOT NULL
);


--
-- Name: cs_method_class_assoc_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_method_class_assoc_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_method_class_assoc; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_method_class_assoc (
    class_id integer NOT NULL,
    method_id integer NOT NULL,
    id integer DEFAULT nextval('cs_method_class_assoc_sequence'::regclass) NOT NULL
);


--
-- Name: cs_permission_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_permission_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_permission; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_permission (
    id integer DEFAULT nextval('cs_permission_sequence'::regclass) NOT NULL,
    key character varying(10),
    description character varying(100)
);


--
-- Name: cs_policy_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_policy_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_policy; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_policy (
    id integer DEFAULT nextval('cs_policy_sequence'::regclass) NOT NULL,
    name character varying(20) NOT NULL
);


--
-- Name: cs_policy_rule_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_policy_rule_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_policy_rule; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_policy_rule (
    id integer DEFAULT nextval('cs_policy_rule_sequence'::regclass) NOT NULL,
    policy integer NOT NULL,
    permission integer NOT NULL,
    default_value boolean NOT NULL
);


--
-- Name: cs_query; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_query (
    id integer DEFAULT nextval(('cs_query_sequence'::text)::regclass) NOT NULL,
    name character varying(256) NOT NULL,
    descr text,
    statement text,
    result integer,
    is_update boolean DEFAULT false NOT NULL,
    is_union boolean DEFAULT false NOT NULL,
    is_root boolean DEFAULT false NOT NULL,
    is_batch boolean DEFAULT false NOT NULL,
    conjunction boolean DEFAULT false NOT NULL,
    is_search boolean DEFAULT false NOT NULL
);


--
-- Name: cs_query_class_assoc_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_query_class_assoc_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_query_class_assoc; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_query_class_assoc (
    class_id integer NOT NULL,
    query_id integer NOT NULL,
    id integer DEFAULT nextval('cs_query_class_assoc_sequence'::regclass) NOT NULL
);


--
-- Name: cs_query_link_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_query_link_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_query_link; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_query_link (
    id integer DEFAULT nextval('cs_query_link_sequence'::regclass) NOT NULL,
    id_from integer NOT NULL,
    id_to integer NOT NULL,
    domain_to integer
);


--
-- Name: cs_query_parameter; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_query_parameter (
    id integer DEFAULT nextval(('cs_query_parameter_sequence'::text)::regclass) NOT NULL,
    query_id integer NOT NULL,
    param_key character varying(100) NOT NULL,
    descr text,
    is_query_result boolean DEFAULT false NOT NULL,
    type_id integer,
    query_position integer DEFAULT 0
);


--
-- Name: cs_query_store_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_query_store_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_query_store; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_query_store (
    id integer DEFAULT nextval('cs_query_store_sequence'::regclass) NOT NULL,
    user_id integer NOT NULL,
    name character varying(100) NOT NULL,
    file_name character varying(100) NOT NULL
);


--
-- Name: cs_query_store_ug_assoc_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_query_store_ug_assoc_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_query_store_ug_assoc; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_query_store_ug_assoc (
    ug_id integer NOT NULL,
    query_store_id integer NOT NULL,
    permission integer,
    domain integer,
    id integer DEFAULT nextval('cs_query_store_ug_assoc_sequence'::regclass) NOT NULL
);


--
-- Name: cs_query_ug_assoc_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_query_ug_assoc_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_query_ug_assoc; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_query_ug_assoc (
    ug_id integer NOT NULL,
    query_id integer NOT NULL,
    permission integer,
    domain integer,
    id integer DEFAULT nextval('cs_query_ug_assoc_sequence'::regclass) NOT NULL
);


--
-- Name: cs_type; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_type (
    id integer DEFAULT nextval(('cs_type_sequence'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    class_id integer,
    complex_type boolean DEFAULT false NOT NULL,
    descr text,
    editor integer,
    renderer integer
);


--
-- Name: cs_ug_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_ug_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_ug; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_ug (
    id integer DEFAULT nextval('cs_ug_sequence'::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    descr text,
    domain integer NOT NULL
);


--
-- Name: cs_ug_attr_perm; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_ug_attr_perm (
    id integer DEFAULT nextval(('cs_ug_attr_perm_sequence'::text)::regclass) NOT NULL,
    ug_id integer NOT NULL,
    attr_id integer NOT NULL,
    permission integer
);


--
-- Name: cs_ug_cat_node_perm; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_ug_cat_node_perm (
    id integer DEFAULT nextval(('cs_ug_cat_node_perm_sequence'::text)::regclass) NOT NULL,
    ug_id integer NOT NULL,
    cat_node_id integer NOT NULL,
    permission integer
);


--
-- Name: cs_ug_class_perm_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_ug_class_perm_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_ug_class_perm; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_ug_class_perm (
    id integer DEFAULT nextval('cs_ug_class_perm_sequence'::regclass) NOT NULL,
    ug_id integer NOT NULL,
    class_id integer NOT NULL,
    permission integer
);


--
-- Name: cs_ug_membership_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_ug_membership_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_ug_membership; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_ug_membership (
    ug_id integer NOT NULL,
    usr_id integer NOT NULL,
    id integer DEFAULT nextval('cs_ug_membership_sequence'::regclass) NOT NULL
);


--
-- Name: cs_ug_method_perm; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_ug_method_perm (
    id integer DEFAULT nextval(('cs_ug_method_perm_sequence'::text)::regclass) NOT NULL,
    ug_id integer NOT NULL,
    method_id integer NOT NULL,
    permission integer
);


--
-- Name: cs_usr; Type: TABLE; Schema: public; Owner: -; Tablespace:
--

CREATE TABLE cs_usr (
    id integer DEFAULT nextval(('cs_usr_sequence'::text)::regclass) NOT NULL,
    login_name character varying(32) NOT NULL,
    password character varying(16),
    last_pwd_change timestamp without time zone NOT NULL,
    administrator boolean DEFAULT false NOT NULL
);


--
-- Name: cs_attr_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_attr_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_cat_node_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_cat_node_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_class_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_class_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_method_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_method_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_query_parameter_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_query_parameter_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_query_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_query_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_type_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_type_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_ug_attr_perm_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_ug_attr_perm_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_ug_cat_node_perm_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_ug_cat_node_perm_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_ug_method_perm_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_ug_method_perm_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cs_usr_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cs_usr_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: attr_perm_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_ug_attr_perm
    ADD CONSTRAINT attr_perm_pkey PRIMARY KEY (id);


--
-- Name: cat_node_perm_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_ug_cat_node_perm
    ADD CONSTRAINT cat_node_perm_pkey PRIMARY KEY (id);


--
-- Name: class_perm_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_ug_class_perm
    ADD CONSTRAINT class_perm_pkey PRIMARY KEY (id);


--
-- Name: cs_all_attr_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_all_attr_mapping
    ADD CONSTRAINT cs_all_attr_mapping_pkey PRIMARY KEY (id);


--
-- Name: cs_attr_string_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_attr_string
    ADD CONSTRAINT cs_attr_string_pkey PRIMARY KEY (id);


--
-- Name: cs_cat_link_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_cat_link
    ADD CONSTRAINT cs_cat_link_pkey PRIMARY KEY (id);


--
-- Name: cs_class_attr_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_class_attr
    ADD CONSTRAINT cs_class_attr_pkey PRIMARY KEY (id);


--
-- Name: cs_domain_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_domain
    ADD CONSTRAINT cs_domain_pkey PRIMARY KEY (id);


--
-- Name: cs_icon_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_icon
    ADD CONSTRAINT cs_icon_pkey PRIMARY KEY (id);


--
-- Name: cs_java_class_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_java_class
    ADD CONSTRAINT cs_java_class_pkey PRIMARY KEY (id);


--
-- Name: cs_locks_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_locks
    ADD CONSTRAINT cs_locks_pkey PRIMARY KEY (id);


--
-- Name: cs_method_class_assoc_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_method_class_assoc
    ADD CONSTRAINT cs_method_class_assoc_pkey PRIMARY KEY (id);


--
-- Name: cs_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_permission
    ADD CONSTRAINT cs_permission_pkey PRIMARY KEY (id);


--
-- Name: cs_policy_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_policy
    ADD CONSTRAINT cs_policy_pkey PRIMARY KEY (id);


--
-- Name: cs_policy_rule_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_policy_rule
    ADD CONSTRAINT cs_policy_rule_pkey PRIMARY KEY (id);


--
-- Name: cs_policy_rule_policy_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_policy_rule
    ADD CONSTRAINT cs_policy_rule_policy_key UNIQUE (policy, permission);


--
-- Name: cs_query_class_assoc_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query_class_assoc
    ADD CONSTRAINT cs_query_class_assoc_pkey PRIMARY KEY (id);


--
-- Name: cs_query_link_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query_link
    ADD CONSTRAINT cs_query_link_pkey PRIMARY KEY (id);


--
-- Name: cs_query_store_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query_store
    ADD CONSTRAINT cs_query_store_pkey PRIMARY KEY (id);


--
-- Name: cs_query_store_ug_assoc_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query_store_ug_assoc
    ADD CONSTRAINT cs_query_store_ug_assoc_pkey PRIMARY KEY (id);


--
-- Name: cs_query_ug_assoc_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query_ug_assoc
    ADD CONSTRAINT cs_query_ug_assoc_pkey PRIMARY KEY (id);


--
-- Name: cs_ug_membership_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_ug_membership
    ADD CONSTRAINT cs_ug_membership_pkey PRIMARY KEY (id);


--
-- Name: cs_ug_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_ug
    ADD CONSTRAINT cs_ug_pkey PRIMARY KEY (id);


--
-- Name: method_perm_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_ug_method_perm
    ADD CONSTRAINT method_perm_pkey PRIMARY KEY (id);


--
-- Name: x_cs_attr_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_attr
    ADD CONSTRAINT x_cs_attr_pkey PRIMARY KEY (id);


--
-- Name: x_cs_cat_node_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_cat_node
    ADD CONSTRAINT x_cs_cat_node_pkey PRIMARY KEY (id);


--
-- Name: x_cs_class_name_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_class
    ADD CONSTRAINT x_cs_class_name_key UNIQUE (name);


--
-- Name: x_cs_class_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_class
    ADD CONSTRAINT x_cs_class_pkey PRIMARY KEY (id);


--
-- Name: x_cs_method_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_method
    ADD CONSTRAINT x_cs_method_pkey PRIMARY KEY (id);


--
-- Name: x_cs_query_name_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query
    ADD CONSTRAINT x_cs_query_name_key UNIQUE (name);


--
-- Name: x_cs_query_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query_parameter
    ADD CONSTRAINT x_cs_query_parameter_pkey PRIMARY KEY (id);


--
-- Name: x_cs_query_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_query
    ADD CONSTRAINT x_cs_query_pkey PRIMARY KEY (id);


--
-- Name: x_cs_type_name_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_type
    ADD CONSTRAINT x_cs_type_name_key UNIQUE (name);


--
-- Name: x_cs_type_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_type
    ADD CONSTRAINT x_cs_type_pkey PRIMARY KEY (id);


--
-- Name: x_cs_usr_login_name_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_usr
    ADD CONSTRAINT x_cs_usr_login_name_key UNIQUE (login_name);


--
-- Name: x_cs_usr_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace:
--

ALTER TABLE ONLY cs_usr
    ADD CONSTRAINT x_cs_usr_pkey PRIMARY KEY (id);


--
-- Name: cl_idx; Type: INDEX; Schema: public; Owner: -; Tablespace:
--

CREATE INDEX cl_idx ON cs_cat_node USING btree (class_id);


--
-- Name: cs_all_attr_mapping_index1; Type: INDEX; Schema: public; Owner: -; Tablespace:
--

CREATE INDEX cs_all_attr_mapping_index1 ON cs_all_attr_mapping USING btree (class_id);


--
-- Name: cs_all_attr_mapping_index2; Type: INDEX; Schema: public; Owner: -; Tablespace:
--

CREATE INDEX cs_all_attr_mapping_index2 ON cs_all_attr_mapping USING btree (attr_class_id);


--
-- Name: cs_all_attr_mapping_index3; Type: INDEX; Schema: public; Owner: -; Tablespace:
--

CREATE INDEX cs_all_attr_mapping_index3 ON cs_all_attr_mapping USING btree (attr_object_id);


--
-- Name: i_cs_attr_string_aco_id; Type: INDEX; Schema: public; Owner: -; Tablespace:
--

CREATE INDEX i_cs_attr_string_aco_id ON cs_attr_string USING btree (attr_id, class_id, object_id);


--
-- Name: ob_idx; Type: INDEX; Schema: public; Owner: -; Tablespace:
--

CREATE INDEX ob_idx ON cs_cat_node USING btree (object_id);


--
-- Name: obj_cl_idx; Type: INDEX; Schema: public; Owner: -; Tablespace:
--

CREATE INDEX obj_cl_idx ON cs_cat_node USING btree (class_id, object_id);


--
-- PostgreSQL database dump complete
--



------------------------------------------------------------------------------



-- klasse für Georeferenzierung anlegen
--------------------------------------------------------------

create table geom
(
    id integer primary key

);


-- geofeld hinzufügen

SELECT AddGeometryColumn( 'public','geom','geo_field', -1, 'GEOMETRY', 2 );



-- Klassen händisch anlegen
insert into cs_icon ( name, file_name ) values ('Map', 'map.png');

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'cids_GEOMETRY', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'INTEGER', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'INT2', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'INT4', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'INT8', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'NUMERIC', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'CHAR', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'VARCHAR', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'TEXT', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'BOOL', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'FLOAT4', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'FLOAT8', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'DATE', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'TIMESTAMP', null, FALSE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'BPCHAR', null, FALSE, null, null, null);

insert into cs_class ( name, descr, class_icon_id, object_icon_id, table_name, primary_key_field, indexed, tostring, editor) values ('GEOM','Cids Geodatentyp',(select id from cs_icon where name = 'Map'),(select id from cs_icon where name = 'Map'),'GEOM','ID',TRUE,null,null);


insert into cs_attr ( class_id, type_id, name, field_name, foreign_key,pos,optional) values ( (select id from cs_class where name = 'GEOM'), (select id from cs_type where name = 'INTEGER'), 'ID', 'ID', FALSE,   0,FALSE);

insert into cs_attr ( class_id, type_id, name,  field_name,editor, pos,optional)values (  (select id from cs_class where name = 'GEOM'), (select id from cs_type where name = 'cids_GEOMETRY') , 'GEO_STRING', 'GEO_FIELD', (select id from cs_java_class where qualifier ='Sirius.navigator.ui.attributes.editor.metaobject.SimpleFromStringMetaAttributeEditor'),  1,TRUE);


-- anlegen von url und url_base

insert into cs_icon (name, file_name) values ('Url', 'url.png');

insert into cs_class ( name, descr, class_icon_id, object_icon_id, table_name, primary_key_field, indexed, tostring, editor) values( 'URL' , null, (select id from cs_icon where name ='Url'), (select id from cs_icon where name ='Url'), 'URL', 'ID',false, null,null);

insert into cs_class ( name, descr, class_icon_id, object_icon_id, table_name, primary_key_field, indexed, tostring, editor) values( 'URL_BASE', null , (select id from cs_icon where name ='Url'), (select id from cs_icon where name ='Url'), 'URL_BASE', 'ID',false, null,null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'URL_BASE', (select id from cs_class where name = 'URL_BASE'), TRUE, null, null, null);

insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'URL', (select id from cs_class where name = 'URL'), TRUE, null, null, null);

-- url_base attribute

insert into cs_attr ( class_id, type_id, name, field_name, foreign_key,pos,optional) values ( (select id from cs_class where name = 'URL_BASE'), (select id from cs_type where name = 'INTEGER'), 'ID', 'ID', FALSE,   0,FALSE);

insert into cs_attr ( class_id, type_id, name, field_name, foreign_key,pos,optional) values ( (select id from cs_class where name = 'URL_BASE'), (select id from cs_type where name = 'VARCHAR'), 'PROT_PREFIX', 'PROT_PREFIX', FALSE,   1,FALSE);

insert into cs_attr ( class_id, type_id, name, field_name, foreign_key,pos,optional) values ( (select id from cs_class where name = 'URL_BASE'), (select id from cs_type where name = 'TEXT'), 'PATH', 'PATH', FALSE,   3,FALSE);

insert into cs_attr ( class_id, type_id, name, field_name, foreign_key,pos,optional) values ( (select id from cs_class where name = 'URL_BASE'), (select id from cs_type where name = 'TEXT'), 'SERVER', 'SERVER', FALSE,   2,FALSE);

-- url attribute

insert into cs_attr ( class_id, type_id, name, field_name, foreign_key,pos,optional) values ( (select id from cs_class where name = 'URL'), (select id from cs_type where name = 'INTEGER'), 'ID', 'ID', FALSE,   0,FALSE);

insert into cs_attr ( class_id, type_id, name, field_name, foreign_key,pos,optional) values ( (select id from cs_class where name = 'URL'), (select id from cs_type where name = 'TEXT'), 'OBJECT_NAME', 'OBJECT_NAME', FALSE, 2, FALSE);

insert into cs_attr ( class_id, type_id, name, field_name, foreign_key, foreign_key_references_to, pos, optional) values ( (select id from cs_class where name = 'URL'), (select id from cs_type where name like 'URL_BASE'), 'URL_BASE_ID', 'URL_BASE_ID', TRUE, (select id from cs_class where name like 'URL_BASE'), 1,FALSE);



-- arraysymbol
 insert into cs_icon (name, file_name) values ('Array', 'array.png');
-- nativen Typ löschen
--select setVal('cs_class_sequence',(select max(id)+1 from cs_class));
delete from cs_type where name = 'geom';

-- Postgis gist index
CREATE INDEX geo_index ON geom USING GIST ( geo_field GIST_GEOMETRY_OPS ); 
--------------------------------------------------------------------

-- geom wieder einfügen, noch zu prüfen, ob erlaubt
insert into cs_type ( name, class_id, complex_type, descr, editor, renderer) values ( 'GEOM', (select id from cs_class where name = 'GEOM'), TRUE, null, null, null);


--- suche

insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (1, 'full_text_search', '1', 'Select distinct class_id,object_id from cs_attr_string where lower(string_val) like lower(''%?%'') and class_id in ?', 1, '0', '1', '1', '0', '0', '1');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (4, 'get_all_nodes', '1', 'select * from (cs_cat_node as n left outer  join URL as u on (n.descr=u.id)  )left outer join url_base b on( u.url_base_id =b.id)', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (5, 'get_all_classes', '1', 'select c.id, c.name ,descr,class_icon_id,object_icon_id,table_name,primary_key_field,indexed,toStringClass.qualifier as toStringQualifier,EditorClass.qualifier as EditorQualifier,RendererClass.qualifier as RendererQualifier,c.array_link,c.policy,c.attribute_policy  from cs_class as c  left outer join cs_java_class as toStringClass on (c.tostring =toStringClass.id) left outer join cs_java_class as EditorClass on (c.editor=EditorClass.id) left outer join cs_java_class as RendererClass on (c.renderer=RendererClass.id)', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (6, 'get_all_links', '1', 'select id_from,id_to,d.name as domain_to from cs_cat_link,cs_domain as d where domain_to = d.id', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (7, 'get_all_class_attributes', '1', 'select * from cs_class_attr', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (8, 'count_classes', '1', 'select count(*) from cs_class', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (9, 'count_nodes', '1', 'select count(*) from cs_cat_node', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (11, 'count_links', '1', 'select count(*) from cs_cat_link', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (12, 'get_all_class_method_ids', '1', 'select * from cs_method_class_assoc', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (13, 'get_all_methods', '1', 'select * from cs_method', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (14, 'count_methods', '1', 'select count(*) from cs_method', 1, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (15, 'get_all_images', '1', 'select * from cs_icon', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (16, 'count_images', '1', 'select count(*) from cs_icon', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (17, 'get_all_users', '1', 'select  id,login_name,password,last_pwd_change,administrator from cs_usr', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (18, 'get_all_usergroups', '1', 'select  id ,name, descr,  domain from cs_ug order by name', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (19, 'get_all_memberships', '1', 'select login_name, ug.name as ug , ''LOCAL'' as ugDomain  from cs_ug_membership as m,cs_usr as u ,cs_ug as ug  where u.id=m.usr_id and ug.id=m.ug_id', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (20, 'verify_user_password', '1', 'select count(*) from cs_usr where trim(lower(login_name)) = ''?'' AND trim(lower(password)) = ''?''', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (21, 'change_user_password', '1', 'update cs_usr set password = ''?'', last_pwd_change = ''now'' where lower(login_name) = ''?'' AND lower(password) = ''?'' ', 2, '1', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (22, 'get_all_class_permissions', '1', 'select
                        ug.name as ug_name, d.name  as domainname,cp.permission , p.key,cp.class_id,cp.ug_id  as ug_id
                    from
                        cs_ug_class_perm cp ,cs_permission p,cs_ug as ug , cs_domain as d
                    where
                        cp.permission=p.id and cp.ug_id = ug.id  and ug.domain=d.id', 1, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (23, 'get_all_class_attribute_permissions', '1', 'select ug_id,d.name,attr_id from cs_ug_attr_perm as p,cs_attr as a,cs_domain as d  WHERE attr_id = a.id and type_attr = ''C''  and d.id=p.domain', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (24, 'get_all_method_permissions', '1', 'select distinct m.method_id, plugin_id, u.id as ug_id , d.name as ls  from cs_ug_method_perm as p,cs_method  as m, cs_ug as u,cs_domain as d  where m.id =p.method_id and u.id=p.ug_id and u.domain=d.id', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (25, 'get_all_node_permissions', '1', 'select * from cs_ug_cat_node_perm', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (26, 'get_all_object_attribute_permissions', '1', 'select cs_ug.id,d.name,attr_id from cs_ug_attr_perm,cs_attr, cs_domain as d WHERE attr_id = id and d.id=cs_ug.domain', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (27, 'get_attribute_info', '1', 'select a.*,ts.qualifier as toStringString,e.qualifier as editor_class,s.qualifier as from_string_class , ce.qualifier as complexeditorclass  from cs_attr as a  left outer join cs_java_class as e on( editor = e.id )  left outer join cs_java_class as s on(from_string = s.id ) left outer join cs_java_class as ce on(complex_editor = ce.id ) left outer join cs_java_class as ts  on(a.tostring = ts.id ) order by a.class_id, a.pos', 2, '0', '0', '0', '0', '0', '0');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (45, 'GEOSEARCH', '1', 'select * from GEOSUCHE where class_id in ? and intersects(geo_field ,geometryfromtext(''SRID=-1;?))''', 1, '0', '1', '1', '0', '1', '1');
insert into public.cs_query (id, name, descr, statement, result, is_update, is_union, is_root, is_batch, conjunction, is_search) values (46, 'TEXTSEARCH', '1', 'select  distinct class_id,object_id,name  from TEXTSEARCH where lower(string_val) like lower(''%?%'') and class_id in ?', 1, '0', '1', '1', '0', '1', '1');

--- views die von der suche gebraucht werden

CREATE
    VIEW textsearch
    (
        class_id,
        object_id,
        name,
        string_val
    ) AS
SELECT DISTINCT
    x.class_id,
    x.object_id,
    c.name,
    x.string_val
FROM
    (cs_attr_string x
LEFT JOIN
    cs_cat_node c
    ON
    (
        (
            (
                x.class_id = c.class_id
            )
            AND
            (
                x.object_id = c.object_id
            )
        )
    )
    )
ORDER BY
    x.class_id,
    x.object_id,
    c.name,
    x.string_val;


CREATE
    VIEW geosuche
    (
        class_id,
        object_id,
        name,
        geo_field
    ) AS
SELECT DISTINCT
    x.class_id,
    x.object_id,
    c.name,
    x.geo_field
FROM
    (
        (
        SELECT DISTINCT
            cs_all_attr_mapping.class_id,
            cs_all_attr_mapping.object_id,
            geom.geo_field
        FROM
            geom,
            cs_all_attr_mapping
        WHERE
            (
                (
                    cs_all_attr_mapping.attr_class_id = 27
                )
                AND
                (
                    cs_all_attr_mapping.attr_object_id = geom.id
                )
            )
        ORDER BY
            cs_all_attr_mapping.class_id,
            cs_all_attr_mapping.object_id,
            geom.geo_field
        )
        x
    LEFT JOIN
        cs_cat_node c
        ON
        (
            (
                (
                    x.class_id = c.class_id
                )
                AND
                (
                    x.object_id = c.object_id
                )
            )
        )
    )
ORDER BY
    x.class_id,
    x.object_id,
    c.name,
    x.geo_field;


insert into public.cs_query_parameter (id, query_id, param_key, descr, is_query_result, type_id, query_position) values (58, 45, 'cs_classids', '1', '0', 1, 0);
insert into public.cs_query_parameter (id, query_id, param_key, descr, is_query_result, type_id, query_position) values (59, 45, 'polygon', '1', '0', 1, 1);
insert into public.cs_query_parameter (id, query_id, param_key, descr, is_query_result, type_id, query_position) values (60, 46, 'cs_classids', '1', '0', 1, 1);
insert into public.cs_query_parameter (id, query_id, param_key, descr, is_query_result, type_id, query_position) values (61, 46, 'text', '1', '0', 1, 0);

-- rechte
insert into public.cs_policy (id, name) values (0, 'STANDARD');
insert into public.cs_policy (id, name) values (1, 'WIKI');
insert into public.cs_policy (id, name) values (2, 'SECURE');

insert into public.cs_policy_rule (id, policy, permission, default_value) values (1, 0, 0, '1');
insert into public.cs_policy_rule (id, policy, permission, default_value) values (2, 0, 1, '0');
insert into public.cs_policy_rule (id, policy, permission, default_value) values (3, 1, 0, '1');
insert into public.cs_policy_rule (id, policy, permission, default_value) values (4, 1, 1, '1');
insert into public.cs_policy_rule (id, policy, permission, default_value) values (5, 2, 0, '0');
insert into public.cs_policy_rule (id, policy, permission, default_value) values (6, 2, 1, '0');

insert into public.cs_permission (id, key, description) values (0, 'read', 'Leserecht');
insert into public.cs_permission (id, key, description) values (1, 'write', 'Schreibrecht');


-- Inserts zum Anlegen eines Standardbenutzers admin und einer neuen Benutzergruppe Administratoren
insert into cs_domain (name) values('LOCAL');
insert into cs_ug (name, domain) values ('Administratoren', (Select id from cs_domain where name = 'LOCAL'));
insert into cs_usr(login_name,password,last_pwd_change,administrator) values('admin','cismet',(SELECT CURRENT_TIMESTAMP),True);
insert into cs_ug_membership (ug_id,usr_id) values ((Select id from cs_ug where name ='Administratoren'),(Select id from cs_usr where login_name ='admin'));
insert into cs_ug (name, domain) values ('Gäste', (Select id from cs_domain where name = 'LOCAL'));
insert into cs_usr(login_name,password,last_pwd_change,administrator) values('gast','cismet',(SELECT CURRENT_TIMESTAMP),false);
insert into cs_ug_membership (ug_id,usr_id) values ((Select id from cs_ug where name ='Gäste'),(Select id from cs_usr where login_name ='gast'));
