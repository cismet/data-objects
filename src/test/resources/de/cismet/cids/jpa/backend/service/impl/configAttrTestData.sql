DELETE FROM cs_config_attr_jt;
DELETE FROM cs_config_attr_key;
DELETE FROM cs_config_attr_value;

ALTER SEQUENCE cs_config_attr_key_sequence   RESTART 1;
ALTER SEQUENCE cs_config_attr_value_sequence RESTART 1;
ALTER SEQUENCE cs_config_attr_jt_sequence    RESTART 1;

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


INSERT INTO cs_config_attr_jt                (dom_id, key_id, val_id) VALUES       (1, 1,  1);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 1, 1,  2);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 1, 1,  3);

INSERT INTO cs_config_attr_jt                (dom_id, key_id, val_id) VALUES       (1, 2,  4);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 1, 2,  5);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 1, 2,  6);

INSERT INTO cs_config_attr_jt                (dom_id, key_id, val_id) VALUES       (1, 3,  8);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 1, 3,  9);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 1, 3, 10);

INSERT INTO cs_config_attr_jt                (dom_id, key_id, val_id) VALUES       (1, 4, 12);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 1, 4, 13);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 1, 4, 14);
INSERT INTO cs_config_attr_jt                (dom_id, key_id, val_id) VALUES       (1, 5, 15);
INSERT INTO cs_config_attr_jt         (ug_id, dom_id, key_id, val_id) VALUES    (1, 1, 5, 16);
INSERT INTO cs_config_attr_jt (usr_id, ug_id, dom_id, key_id, val_id) VALUES (1, 1, 1, 5, 17);

UPDATE cs_config_attr_jt SET type_id = 1;