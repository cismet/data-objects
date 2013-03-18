-- assumes initialised cids system
-- destroys cids system

DELETE FROM cs_config_attr_exempt;
DELETE FROM cs_config_attr_jt;
DELETE FROM cs_config_attr_key;
DELETE FROM cs_config_attr_value;

DELETE FROM cs_ug_attr_perm;
DELETE FROM cs_ug_cat_node_perm;
DELETE FROM cs_ug_class_perm;
DELETE FROM cs_query_ug_assoc;

DELETE FROM cs_ug;
DELETE FROM cs_usr;

ALTER SEQUENCE cs_config_attr_key_sequence   RESTART 1;
ALTER SEQUENCE cs_config_attr_value_sequence RESTART 1;
ALTER SEQUENCE cs_config_attr_jt_sequence    RESTART 1;
ALTER SEQUENCE cs_ug_attr_perm_sequence      RESTART 1;
ALTER SEQUENCE cs_ug_cat_node_perm_sequence  RESTART 1;
ALTER SEQUENCE cs_ug_class_perm_sequence     RESTART 1;
ALTER SEQUENCE cs_query_ug_assoc_sequence    RESTART 1;
ALTER SEQUENCE cs_ug_sequence                RESTART 1;
ALTER SEQUENCE cs_usr_sequence               RESTART 1;

INSERT INTO cs_ug ("name", descr, "domain", prio) VALUES ('test', '', 0, 1);
INSERT INTO cs_usr (login_name, password, last_pwd_change, administrator) VALUES ('t', 't', '2011-05-02 15:46:29.924', false);


-- add config attrs
INSERT INTO cs_config_attr_key (key) VALUES ('abc');
INSERT INTO cs_config_attr_key (key) VALUES ('lagis.app.wms');
INSERT INTO cs_config_attr_key (key) VALUES ('lagis.app.wfs');
INSERT INTO cs_config_attr_key (key) VALUES ('lagis.conf.main.pass');
INSERT INTO cs_config_attr_key (key) VALUES ('lagis.conf.main.uname');
INSERT INTO cs_config_attr_key (key) VALUES ('orphan1');
INSERT INTO cs_config_attr_key (key) VALUES ('orphankey2');
INSERT INTO cs_config_attr_key (key) VALUES ('orphankey3');
INSERT INTO cs_config_attr_key (key) VALUES ('orphankey4');

INSERT INTO cs_config_attr_value (value) VALUES ('alphabeth');
INSERT INTO cs_config_attr_value (value) VALUES ('alphabeth2');
INSERT INTO cs_config_attr_value (value) VALUES ('alphabeth3');
INSERT INTO cs_config_attr_value (value) VALUES ('wms1');
INSERT INTO cs_config_attr_value (value) VALUES ('wms2');
INSERT INTO cs_config_attr_value (value) VALUES ('wms3');
INSERT INTO cs_config_attr_value (value) VALUES ('orphanvalue1');
INSERT INTO cs_config_attr_value (value) VALUES ('wfs1');
INSERT INTO cs_config_attr_value (value) VALUES ('wfs2');
INSERT INTO cs_config_attr_value (value) VALUES ('wfs3');
INSERT INTO cs_config_attr_value (value) VALUES ('orphanvalue2');
INSERT INTO cs_config_attr_value (value) VALUES ('a');
INSERT INTO cs_config_attr_value (value) VALUES ('b');
INSERT INTO cs_config_attr_value (value) VALUES ('c');
INSERT INTO cs_config_attr_value (value) VALUES ('d');
INSERT INTO cs_config_attr_value (value) VALUES ('e');
INSERT INTO cs_config_attr_value (value) VALUES ('f');
INSERT INTO cs_config_attr_value (value) VALUES ('orphanvalue3');

INSERT INTO cs_config_attr_exempt (usr_id, ug_id, key_id) VALUES (1, 1, 2);
INSERT INTO cs_config_attr_exempt (usr_id, ug_id, key_id) VALUES (1, 1, 4);

INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 0, 1,  2);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 0, 1,  3);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 0, 2,  5);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 0, 2,  6);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 0, 3,  9);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 0, 3, 10);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 0, 4, 13);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 0, 4, 14);
INSERT INTO cs_config_attr_jt                (dom_id, key_id, val_id) VALUES       (0, 5, 15);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 0, 5, 16);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 0, 5, 17);

UPDATE cs_config_attr_jt SET type_id = 1;
-- end cfg attr 

-- add attr perm
INSERT INTO cs_ug_attr_perm (ug_id, attr_id, "permission", "domain") VALUES (1, 1, 1, 0);
-- end attr perm 

-- add cat node perm
INSERT INTO cs_ug_cat_node_perm (ug_id, "domain", cat_node_id, "permission") VALUES (1, 0, 252332826, 0);
INSERT INTO cs_ug_cat_node_perm (ug_id, "domain", cat_node_id, "permission") VALUES (1, 0, 252332827, 0);
INSERT INTO cs_ug_cat_node_perm (ug_id, "domain", cat_node_id, "permission") VALUES (1, 0, 252332827, 0);
INSERT INTO cs_ug_cat_node_perm (ug_id, "domain", cat_node_id, "permission") VALUES (1, 0, 281000950, 0);
INSERT INTO cs_ug_cat_node_perm (ug_id, "domain", cat_node_id, "permission") VALUES (1, 0, 281000952, 0);
INSERT INTO cs_ug_cat_node_perm (ug_id, "domain", cat_node_id, "permission") VALUES (1, 0, 231260856, 0);
-- end cat node perm 

-- add class perm
INSERT INTO cs_ug_class_perm (ug_id, class_id, "permission", "domain") VALUES (1, 1, 0, 0);
INSERT INTO cs_ug_class_perm (ug_id, class_id, "permission", "domain") VALUES (1, 1, 1, 0);
-- end class perm