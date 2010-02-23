
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- 
-- $Id: postgis7_4.sql,v 1.1 2008/03/05 12:40:58 cschmidt Exp $
--
-- PostGIS - Spatial Types for PostgreSQL
-- http://postgis.refractions.net
-- Copyright 2001-2003 Refractions Research Inc.
--
-- This is free software; you can redistribute and/or modify it under
-- the terms of hte GNU General Public Licence. See the COPYING file.
--  
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- $Log: postgis7_4.sql,v $
-- Revision 1.1  2008/03/05 12:40:58  cschmidt
-- no message
--
-- Revision 1.1  2008/02/25 10:51:46  cschmidt
-- no message
--
-- Revision 1.1  2008/01/24 10:24:56  cschmidt
-- Resource Package for CreateDomainServerProject
--
-- Revision 1.1  2007/12/20 15:09:08  cschmidt
-- Aktualisierte Skriptfiles ( 2 zusätzliche attribute cs_java_class )
--
-- Revision 1.2  2003/11/28 11:06:49  strk
-- Added WKB_recv function for binary WKB input
--
-- Revision 1.1  2003/11/11 10:38:23  strk
-- Postgresql 7.4 enabler scripts.
--
-- Revision 1.2  2003/07/01 18:30:55  pramsey
-- Added CVS revision headers.
--
--
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

BEGIN TRANSACTION;

-- You might have to define the PL/PgSQL language usually done with the
-- changelang script.

-- Here's some hokey code to test to see if PL/PgSQL is installed
-- if it is, you get a message "PL/PgSQL is installed" 
-- otherwise it will give a big error message.

(select 'PL/PgSQL is installed.' as message from pg_language where lanname='plpgsql') union (select 'You must install PL/PgSQL before running this SQL file,\nor you will get an error. To install PL/PgSQL run:\n\tcreatelang plpgsql <dbname>'::text as message) order by message limit 1;

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
--  HISTOGRAM2D

CREATE FUNCTION histogram2d_in(cstring)
	RETURNS histogram2d
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION histogram2d_out(histogram2d)
	RETURNS cstring
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE TYPE histogram2d (
	alignment = double,
	internallength = variable,
	input = histogram2d_in,
	output = histogram2d_out,
	storage = main
);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
--  BOX3D

CREATE FUNCTION box3d_in(cstring)
	RETURNS box3d 
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION box3d_out(box3d)
	RETURNS cstring
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE TYPE box3d (
	alignment = double,
	internallength = 48,
	input = box3d_in,
	output = box3d_out
);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
--  SPHEROID

CREATE FUNCTION spheroid_in(cstring)
	RETURNS spheroid 
	AS '$libdir/libpostgis.so.0.8','ellipsoid_in'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION spheroid_out(spheroid)
	RETURNS cstring
	AS '$libdir/libpostgis.so.0.8','ellipsoid_out'
	LANGUAGE 'C' WITH (isstrict);

CREATE TYPE spheroid (
	alignment = double,
	internallength = 65,
	input = spheroid_in,
	output = spheroid_out
);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
--  WKB

CREATE FUNCTION wkb_in(cstring)
	RETURNS wkb
	AS '$libdir/libpostgis.so.0.8','WKB_in'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION wkb_out(wkb)
	RETURNS cstring
	AS '$libdir/libpostgis.so.0.8','WKB_out'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION wkb_recv(internal)
	RETURNS wkb
	AS '$libdir/libpostgis.so.0.8','WKB_recv'
	LANGUAGE 'C' WITH (isstrict);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
--  CHIP

CREATE FUNCTION chip_in(cstring)
	RETURNS chip
	AS '$libdir/libpostgis.so.0.8','CHIP_in'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION chip_out(chip)
	RETURNS cstring
	AS '$libdir/libpostgis.so.0.8','CHIP_out'
	LANGUAGE 'C' WITH (isstrict);

CREATE TYPE chip (
	alignment = double,
	internallength = variable,
	input = chip_in,
	output = chip_out,
	storage = extended
);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
--  GEOMETRY

CREATE FUNCTION geometry_in(cstring)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_out(geometry)
	RETURNS cstring
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE TYPE geometry (
	alignment = double,
	internallength = variable,
	input = geometry_in,
	output = geometry_out,
	storage = main
);

--
-- GiST selectivity function
--

CREATE FUNCTION postgis_gist_sel (internal, oid, internal, int4)
	RETURNS float8
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C';



-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- 
-- $Id: postgis7_4.sql,v 1.1 2008/03/05 12:40:58 cschmidt Exp $
--
-- PostGIS - Spatial Types for PostgreSQL
-- http://postgis.refractions.net
-- Copyright 2001-2003 Refractions Research Inc.
--
-- This is free software; you can redistribute and/or modify it under
-- the terms of hte GNU General Public Licence. See the COPYING file.
--  
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- $Log: postgis7_4.sql,v $
-- Revision 1.1  2008/03/05 12:40:58  cschmidt
-- no message
--
-- Revision 1.1  2008/02/25 10:51:46  cschmidt
-- no message
--
-- Revision 1.1  2008/01/24 10:24:56  cschmidt
-- Resource Package for CreateDomainServerProject
--
-- Revision 1.1  2007/12/20 15:09:08  cschmidt
-- Aktualisierte Skriptfiles ( 2 zusätzliche attribute cs_java_class )
--
-- Revision 1.26  2003/12/23 09:00:12  strk
-- AddGeometryColumn, DropGeometryColum moved to version-specific scripts.
-- Schema support enabled for version 73 and 74.
--
-- Revision 1.25  2003/12/18 18:07:06  strk
-- Changed fix_geometry_columns() for PG >= 73 so to set f_table_schema to
-- the empty string if its value is not a valid pg namespace.
--
-- Revision 1.24  2003/12/12 10:08:24  strk
-- Added GEOSnoop function and some optional debugging output for
-- geos<->postgis converter (define DEBUG_CONVERTER at top postgis_geos.c)
--
-- Revision 1.23  2003/11/19 17:50:18  strk
-- missing function definition added (I forgot - sorry)
--
-- Revision 1.22  2003/11/19 15:23:27  strk
-- Fixed wrong COMMUTATOR specifications in '<','>','~=','@' operators,
-- added new '<=', '>=' operators
--
-- Revision 1.21  2003/11/13 13:14:49  strk
-- used quote_ident() calls in AddGeometryColumns as suggested by Bernhard Herzog
--
-- Revision 1.20  2003/11/12 20:55:18  strk
-- AddGeometryColumn column identifier case respect fix as suggested by Bernhard Herzog
--
-- Revision 1.19  2003/11/05 18:26:54  strk
-- Added fast collect() and geomunion() aggregates implementations
--
-- Revision 1.18  2003/10/28 11:18:27  strk
-- Added Algorithms section and simplify() enabler code
--
-- Revision 1.17  2003/10/23 08:06:54  strk
-- Added 'unite' aggregate.
--
-- Revision 1.16  2003/10/17 16:07:05  dblasby
-- made isEmpty() return true/false
--
-- Revision 1.15  2003/09/04 16:19:06  dblasby
-- removed truly_inside() function.
--
-- Revision 1.14  2003/08/08 18:19:20  dblasby
-- Conformance changes.
-- Removed junk from postgis_debug.c and added the first run of the long
-- transaction locking support.  (this will change - dont use it)
-- conformance tests were corrected
-- some dos cr/lf removed
-- empty geometries i.e. GEOMETRYCOLLECT(EMPTY) added (with indexing support)
-- pointN(<linestring>,1) now returns the first point (used to return 2nd)
--
-- Revision 1.13  2003/08/06 19:31:18  dblasby
-- Added the WKB parser.  Added all the functions like
-- PolyFromWKB(<WKB>,[<SRID>]).
--
-- Added all the functions like PolyFromText(<WKT>,[<SRID>])
--
-- Minor problem in GEOS library fixed.
--
-- Revision 1.12  2003/08/01 23:58:08  dblasby
-- added the functionality to convert GEOS->PostGIS geometries.  Added those geos
-- functions to postgis.
--
-- Revision 1.11  2003/07/01 18:30:55  pramsey
-- Added CVS revision headers.
--
--
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- SPATIAL_REF_SYS

CREATE TABLE spatial_ref_sys (
	 srid integer not null primary key,
	 auth_name varchar(256), 
	 auth_srid integer, 
	 srtext varchar(2048),
	 proj4text varchar(2048) 
);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- GEOMETRY_COLUMNS

CREATE TABLE geometry_columns (
	f_table_catalog varchar(256) not null,
	f_table_schema varchar(256) not null,
	f_table_name varchar(256) not null,
	f_geometry_column varchar(256) not null,
	coord_dimension integer not null,
	srid integer not null,
	type varchar(30) not null,
	attrelid oid,
	varattnum int,
	stats histogram2d,
	CONSTRAINT geometry_columns_pk primary key ( 
		f_table_catalog, 
		f_table_schema, 
		f_table_name, 
		f_geometry_column ) );

--
-- Workaround for old user defined variable length datatype 
-- default value bug. Should not be necessary > 7.2
-- 

UPDATE pg_type SET typdefault = NULL WHERE typname = 'wkb';
UPDATE pg_type SET typdefault = NULL WHERE typname = 'geometry';
UPDATE pg_type SET typdefault = NULL WHERE typname = 'histogram2d';

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- POSTGIS_VERSION()
-- AS 'SELECT \'0.8 USE_GEOS=1 USE_PROJ=1 USE_STATS=1\'::text AS version'
CREATE FUNCTION postgis_version() RETURNS text 
AS 'SELECT \'0.8 USE_GEOS=1 USE_PROJ=1 USE_STATS=1\'::text AS version' 
LANGUAGE 'sql';

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- FIND_SRID( <schema>, <table>, <geom col> )

CREATE FUNCTION find_srid(varchar,varchar,varchar) RETURNS int4 AS
'DECLARE
   schem text;
   tabl text;
   sr int4;
BEGIN
   IF $1 IS NULL THEN
      RAISE EXCEPTION ''find_srid() - schema is NULL!'';
   END IF;
   IF $2 IS NULL THEN
      RAISE EXCEPTION ''find_srid() - table name is NULL!'';
   END IF;
   IF $3 IS NULL THEN
      RAISE EXCEPTION ''find_srid() - column name is NULL!'';
   END IF;
   schem = $1;
   tabl = $2;
-- if the table contains a . and the schema is empty
-- split the table into a schema and a table
-- otherwise drop through to default behavior
   IF ( schem = '''' and tabl LIKE ''%.%'' ) THEN
     schem = substr(tabl,1,strpos(tabl,''.'')-1);
     tabl = substr(tabl,length(schem)+2);
   ELSE
     schem = schem || ''%'';
   END IF;

   select SRID into sr from geometry_columns where f_table_schema like schem and f_table_name = tabl and f_geometry_column = $3;
   IF NOT FOUND THEN
       RAISE EXCEPTION ''find_srid() - couldnt find the corresponding SRID - is the geometry registered in the GEOMETRY_COLUMNS table?  Is there an uppercase/lowercase missmatch?'';
   END IF;
  return sr;
END;
'
LANGUAGE 'plpgsql' WITH (iscachable); 

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- GET_PROJ4_FROM_SRID( <srid> )

CREATE FUNCTION get_proj4_from_srid(integer) RETURNS text AS
'SELECT proj4text::text FROM spatial_ref_sys WHERE srid= $1' 
LANGUAGE 'sql' WITH (iscachable,isstrict);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- FIND_EXTENT( <table name>, <column name> )

CREATE FUNCTION find_extent(text,text) RETURNS box3d AS
'
DECLARE
	tablename alias for $1;
	columnname alias for $2;
	okay boolean;
 myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE ''SELECT extent("''||columnname||''") FROM "''||tablename||''"'' LOOP
		return myrec.extent;
	END LOOP; 
END;
'
LANGUAGE 'plpgsql' WITH (isstrict);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- TRANSFORM ( <geometry>, <srid> )
--
-- Test:
--
-- trans=# select * from spatial_ref_sys ;
--
--  srid |   auth_name   | auth_srid | srtext | proj4text 
-- ------+---------------+-----------+--------+--------------------------------------------------------------------------
--     1 | latlong WGS84 |         1 |        | +proj=longlat +datum=WGS84
--     2 | BC albers     |         2 |        | proj=aea ellps=GRS80 lon_0=-126 lat_0=45 lat_1=50 lat_2=58.5 x_0=1000000
--
-- select transform( 'SRID=1;POINT(-120.8 50.3)', 2);
--      -> 'SRID=2;POINT(1370033.37046971 600755.810968684)'
--

CREATE FUNCTION transform_geometry(geometry,text,text,int)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','transform_geom'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION transform(geometry,integer) RETURNS geometry AS
'BEGIN
 RETURN transform_geometry( $1 , get_proj4_from_srid(SRID( $1 ) ), get_proj4_from_srid( $2 ), $2 );
 END;'
LANGUAGE 'plpgsql' WITH (iscachable,isstrict);



-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- COMMON FUNCTIONS

CREATE FUNCTION srid(chip)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8','srid_chip'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION height(chip)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8','height_chip'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION factor(chip)
	RETURNS FLOAT4
	AS '$libdir/libpostgis.so.0.8','factor_chip'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION width(chip)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8','width_chip'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION datatype(chip)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8','datatype_chip'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION compression(chip)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8','compression_chip'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION setSRID(chip,int4)
	RETURNS chip
	AS '$libdir/libpostgis.so.0.8','setsrid_chip'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION setfactor(chip,float4)
	RETURNS chip
	AS '$libdir/libpostgis.so.0.8','setfactor_chip'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION geometry(CHIP)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','CHIP_to_geom'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION box3d(geometry)
	RETURNS box3d
	AS '$libdir/libpostgis.so.0.8','get_bbox_of_geometry'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION box(geometry)
	RETURNS BOX
	AS '$libdir/libpostgis.so.0.8','geometry2box'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION geometry(box3d)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','get_geometry_of_bbox'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION geometry(text)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_text'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION expand(box3d,float8)
	RETURNS box3d
	AS '$libdir/libpostgis.so.0.8','expand_bbox'
	LANGUAGE 'C' WITH (iscachable,isstrict);

--
-- Functions for converting to WKB
--

CREATE FUNCTION asbinary(geometry)
	RETURNS wkb
	AS '$libdir/libpostgis.so.0.8','asbinary_simple'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION asbinary(geometry,TEXT)
	RETURNS wkb
	AS '$libdir/libpostgis.so.0.8','asbinary_specify'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION bytea(wkb)
	RETURNS bytea
	AS '$libdir/libpostgis.so.0.8','WKBtoBYTEA'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
CREATE FUNCTION geometry(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','geometryfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
CREATE FUNCTION GeomFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','geometryfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION GeomFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','geometryfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
CREATE FUNCTION PointFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','PointfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION PointFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','PointfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
CREATE FUNCTION LineFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','LinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION LineFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','LinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);


CREATE FUNCTION LinestringFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','LinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION LinestringFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','LinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
CREATE FUNCTION PolyFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','PolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION PolyFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','PolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
CREATE FUNCTION PolygonFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','PolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION PolygonFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','PolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);


CREATE FUNCTION MPointFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPointfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MPointFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPointfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);


CREATE FUNCTION MultiPointFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPointfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MultiPointFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPointfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MultiLineFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MLinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MultiLineFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MLinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
	
CREATE FUNCTION MLineFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MLinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MLineFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MLinefromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MPolyFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MPolyFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
CREATE FUNCTION MultiPolyFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION MultiPolyFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','MPolyfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);


	
CREATE FUNCTION GeomCollFromWKB(wkb,int)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','GCfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);

CREATE FUNCTION GeomCollFromWKB(wkb)
	RETURNS GEOMETRY
	AS '$libdir/libpostgis.so.0.8','GCfromWKB_SRID'
	LANGUAGE 'C' WITH (iscachable,isstrict);
	
	
-- CREATE FUNCTION index_thing(geometry)
-- RETURNS BOOL
-- AS '$libdir/libpostgis.so.0.8'
-- LANGUAGE 'C' WITH (isstrict);

--
-- Debugging functions
--

CREATE FUNCTION npoints(geometry)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION nrings(geometry)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict) ;

CREATE FUNCTION mem_size(geometry)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);


CREATE FUNCTION summary(geometry)
	RETURNS text
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION translate(geometry,float8,float8,float8)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict) ;

CREATE FUNCTION dimension(geometry)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict) ;

CREATE FUNCTION geometrytype(geometry)
	RETURNS text
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION envelope(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION x(geometry)
	RETURNS float8
	AS '$libdir/libpostgis.so.0.8','x_point'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION y(geometry)
	RETURNS float8
	AS '$libdir/libpostgis.so.0.8','y_point'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION z(geometry)
	RETURNS float8
	AS '$libdir/libpostgis.so.0.8','z_point'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION numpoints(geometry)
	RETURNS integer
	AS '$libdir/libpostgis.so.0.8','numpoints_linestring'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION pointn(geometry,integer)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','pointn_linestring'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION exteriorring(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','exteriorring_polygon'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION numinteriorrings(geometry)
	RETURNS integer
	AS '$libdir/libpostgis.so.0.8','numinteriorrings_polygon'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION interiorringn(geometry,integer)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','interiorringn_polygon'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION numgeometries(geometry)
	RETURNS integer
	AS '$libdir/libpostgis.so.0.8','numgeometries_collection'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometryn(geometry,integer)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometryn_collection'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION max_distance(geometry,geometry)
	RETURNS float8
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION optimistic_overlap(geometry,geometry,FLOAT8)
	RETURNS BOOL
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION segmentize(geometry,FLOAT8)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION distance(geometry,geometry)
	RETURNS float8
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION astext(geometry)
	RETURNS TEXT
	AS '$libdir/libpostgis.so.0.8','astext_geometry'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION srid(geometry)
	RETURNS int4
	AS '$libdir/libpostgis.so.0.8','srid_geom'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometryfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text'
	LANGUAGE 'C' WITH (isstrict,iscachable);
	
	
CREATE FUNCTION geometryfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION geomfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION geomfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION polyfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_poly'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION polygonfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_poly'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION polygonfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_poly'
	LANGUAGE 'C' WITH (isstrict,iscachable);
	
CREATE FUNCTION mpolyfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoly'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION linefromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_line'
	LANGUAGE 'C' WITH (isstrict,iscachable);

	
CREATE FUNCTION mlinefromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mline'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION multilinestringfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mline'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION multilinestringfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mline'
	LANGUAGE 'C' WITH (isstrict,iscachable);
	
CREATE FUNCTION pointfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_point'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION mpointfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoint'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION multipointfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoint'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION multipointfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoint'
	LANGUAGE 'C' WITH (isstrict,iscachable);
	
CREATE FUNCTION geomcollfromtext(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_gc'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION setSRID(geometry,int4)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION polyfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_poly'
	LANGUAGE 'C' WITH (isstrict,iscachable);


CREATE FUNCTION mpolyfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoly'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION multipolygonfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoly'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION multipolygonfromtext(geometry,int)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoly'
	LANGUAGE 'C' WITH (isstrict,iscachable);
	
CREATE FUNCTION linefromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_line'
	LANGUAGE 'C' WITH (isstrict,iscachable);
	
CREATE FUNCTION linestringfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_line'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION linestringfromtext(geometry,int)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_line'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION mlinefromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mline'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION pointfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_point'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION mpointfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_mpoint'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION geomcollfromtext(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geometry_from_text_gc'
	LANGUAGE 'C' WITH (isstrict,iscachable);


CREATE FUNCTION isempty(geometry)
	RETURNS boolean
	AS '$libdir/libpostgis.so.0.8','isempty'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION issimple(geometry)
	RETURNS boolean
	AS '$libdir/libpostgis.so.0.8','issimple'
	LANGUAGE 'C' WITH (isstrict,iscachable);
	

CREATE FUNCTION equals(geometry,geometry)
	RETURNS boolean
	AS '$libdir/libpostgis.so.0.8','geomequals'
	LANGUAGE 'C' WITH (isstrict,iscachable);


--
-- Special spheroid functions
--

CREATE FUNCTION length_spheroid(geometry,spheroid)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','length_ellipsoid'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION length3d_spheroid(geometry,spheroid)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','length3d_ellipsoid'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION distance_spheroid(geometry,geometry,spheroid)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','distance_ellipsoid'
	LANGUAGE 'C' WITH (isstrict);

--
-- Generic operations
--

CREATE FUNCTION length3d(geometry)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION length(geometry)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','length2d'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION area2d(geometry)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION area(geometry)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','area2d'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION perimeter3d(geometry)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION perimeter(geometry)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','perimeter2d'
	LANGUAGE 'C' WITH (isstrict);

---CREATE FUNCTION truly_inside(geometry,geometry)
---	RETURNS bool
---	AS '$libdir/libpostgis.so.0.8'
---	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION point_inside_circle(geometry,float8,float8,float8)
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION startpoint(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION endpoint(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION isclosed(geometry)
	RETURNS boolean
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION centroid(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);
	
CREATE FUNCTION isring(geometry)
	RETURNS boolean
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION pointonsurface(geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C' WITH (isstrict);
	

--
-- BBox operations
--

CREATE FUNCTION xmin(box3d)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','box3d_xmin'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION ymin(box3d)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','box3d_ymin'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION zmin(box3d)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','box3d_zmin'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION xmax(box3d)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','box3d_xmax'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION ymax(box3d)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','box3d_ymax'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION zmax(box3d)
	RETURNS FLOAT8
	AS '$libdir/libpostgis.so.0.8','box3d_zmax'
	LANGUAGE 'C' WITH (isstrict,iscachable);

CREATE FUNCTION box3dtobox(box3d)
	RETURNS BOX
	AS '$libdir/libpostgis.so.0.8','box3dtobox'
	LANGUAGE 'C' WITH (isstrict,iscachable);

--
-- Aggregate functions
--

CREATE FUNCTION geom_accum (geometry[],geometry)
	RETURNS geometry[]
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C';

CREATE FUNCTION combine_bbox(box3d,geometry)
	RETURNS box3d
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C';

CREATE AGGREGATE extent(
	sfunc = combine_bbox,
	basetype = geometry,
	stype = box3d
	);

CREATE FUNCTION collector(geometry,geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C';

CREATE AGGREGATE memcollect(
	sfunc = collector,
	basetype = geometry,
	stype = geometry
	);

CREATE FUNCTION collect_garray (geometry[])
        RETURNS geometry
        AS '$libdir/libpostgis.so.0.8'
        LANGUAGE 'C';

CREATE AGGREGATE collect (
	sfunc = geom_accum,
	basetype = geometry,
	stype = geometry[],
	finalfunc = collect_garray
	);


--
-- Operator definitions
--

CREATE FUNCTION geometry_overleft(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_overright(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_left(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_right(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_contain(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_contained(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_overlap(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_same(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

--
-- Sorting functions
--

CREATE FUNCTION geometry_lt(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_le(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_gt(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_ge(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_eq(geometry, geometry) 
	RETURNS bool
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geometry_cmp(geometry, geometry) 
	RETURNS integer
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

--
-- Two dimensional to three dimensional forces
-- 

CREATE FUNCTION force_2d(geometry) 
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION force_3d(geometry) 
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

--
-- Force collection
--

CREATE FUNCTION force_collection(geometry) 
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C' WITH (isstrict);

-- 
-- Operator definitions
--

CREATE OPERATOR << (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_left,
   COMMUTATOR = '>>',
   RESTRICT = positionsel, JOIN = positionjoinsel
);

CREATE OPERATOR &< (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_overleft,
   COMMUTATOR = '&>',
   RESTRICT = positionsel, JOIN = positionjoinsel
);

CREATE OPERATOR && (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_overlap,
   COMMUTATOR = '&&',
   RESTRICT = postgis_gist_sel, JOIN = positionjoinsel
);

CREATE OPERATOR &> (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_overright,
   COMMUTATOR = '&<',
   RESTRICT = positionsel, JOIN = positionjoinsel
);

CREATE OPERATOR >> (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_right,
   COMMUTATOR = '<<',
   RESTRICT = positionsel, JOIN = positionjoinsel
);

CREATE OPERATOR ~= (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_same,
   COMMUTATOR = '~=', 
   RESTRICT = eqsel, JOIN = eqjoinsel
);

CREATE OPERATOR @ (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_contained,
   COMMUTATOR = '~',
   RESTRICT = contsel, JOIN = contjoinsel
);

CREATE OPERATOR ~ (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_contain,
   COMMUTATOR = '@',
   RESTRICT = contsel, JOIN = contjoinsel
);

--
-- Sorting operators for Btree
--

CREATE OPERATOR < (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_lt,
   COMMUTATOR = '>', NEGATOR = '>=',
   RESTRICT = contsel, JOIN = contjoinsel
);

CREATE OPERATOR <= (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_le,
   COMMUTATOR = '>=', NEGATOR = '>',
   RESTRICT = contsel, JOIN = contjoinsel
);

CREATE OPERATOR = (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_eq,
   COMMUTATOR = '=', -- we might implement a faster negator here
   RESTRICT = contsel, JOIN = contjoinsel
);

CREATE OPERATOR >= (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_ge,
   COMMUTATOR = '<=', NEGATOR = '<',
   RESTRICT = contsel, JOIN = contjoinsel
);
CREATE OPERATOR > (
   LEFTARG = GEOMETRY, RIGHTARG = GEOMETRY, PROCEDURE = geometry_gt,
   COMMUTATOR = '<', NEGATOR = '<=',
   RESTRICT = contsel, JOIN = contjoinsel
);

--
-- GEOS Functions
--


CREATE FUNCTION intersection(geometry,geometry)
   RETURNS geometry
   AS '$libdir/libpostgis.so.0.8','intersection'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION buffer(geometry,float8)
   RETURNS geometry
   AS '$libdir/libpostgis.so.0.8','buffer'
   LANGUAGE 'C' WITH (isstrict);
   
   CREATE FUNCTION convexhull(geometry)
      RETURNS geometry
      AS '$libdir/libpostgis.so.0.8','convexhull'
   LANGUAGE 'C' WITH (isstrict);
  
  
     CREATE FUNCTION difference(geometry,geometry)
        RETURNS geometry
        AS '$libdir/libpostgis.so.0.8','difference'
   LANGUAGE 'C' WITH (isstrict);
   
  CREATE FUNCTION boundary(geometry)
      RETURNS geometry
      AS '$libdir/libpostgis.so.0.8','boundary'
   LANGUAGE 'C' WITH (isstrict);

   CREATE FUNCTION symdifference(geometry,geometry)
        RETURNS geometry
        AS '$libdir/libpostgis.so.0.8','symdifference'
   LANGUAGE 'C' WITH (isstrict);


CREATE FUNCTION symmetricdifference(geometry,geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','symdifference'
	LANGUAGE 'C' WITH (isstrict);


CREATE FUNCTION GeomUnion(geometry,geometry)
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8','geomunion'
	LANGUAGE 'C' WITH (isstrict);

CREATE AGGREGATE MemGeomUnion (
	basetype = geometry,
	sfunc = geomunion,
	stype = geometry
	);

CREATE FUNCTION unite_garray (geometry[])
	RETURNS geometry
	AS '$libdir/libpostgis.so.0.8'
	LANGUAGE 'C'; 

CREATE AGGREGATE GeomUnion (
	sfunc = geom_accum,
	basetype = geometry,
	stype = geometry[],
	finalfunc = unite_garray
	);


CREATE FUNCTION relate(geometry,geometry)
   RETURNS text
   AS '$libdir/libpostgis.so.0.8','relate_full'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION relate(geometry,geometry,text)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8','relate_pattern'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION disjoint(geometry,geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION touches(geometry,geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION intersects(geometry,geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION crosses(geometry,geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION within(geometry,geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION contains(geometry,geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION overlaps(geometry,geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION isvalid(geometry)
   RETURNS boolean
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

CREATE FUNCTION geosnoop(geometry)
   RETURNS geometry
   AS '$libdir/libpostgis.so.0.8', 'GEOSnoop'
   LANGUAGE 'C' WITH (isstrict);
   

--
-- Algorithms
--

CREATE FUNCTION simplify(geometry, float8)
   RETURNS geometry
   AS '$libdir/libpostgis.so.0.8'
   LANGUAGE 'C' WITH (isstrict);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- 
-- $Id: postgis7_4.sql,v 1.1 2008/03/05 12:40:58 cschmidt Exp $
--
-- PostGIS - Spatial Types for PostgreSQL
-- http://postgis.refractions.net
-- Copyright 2001-2003 Refractions Research Inc.
--
-- This is free software; you can redistribute and/or modify it under
-- the terms of hte GNU General Public Licence. See the COPYING file.
--  
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- $Log: postgis7_4.sql,v $
-- Revision 1.1  2008/03/05 12:40:58  cschmidt
-- no message
--
-- Revision 1.1  2008/02/25 10:51:46  cschmidt
-- no message
--
-- Revision 1.1  2008/01/24 10:24:56  cschmidt
-- Resource Package for CreateDomainServerProject
--
-- Revision 1.1  2007/12/20 15:09:08  cschmidt
-- Aktualisierte Skriptfiles ( 2 zusätzliche attribute cs_java_class )
--
-- Revision 1.7  2003/12/30 10:40:21  strk
-- For all versions:
-- Updated fix_geometry_columns to use a more readable format in queries.
--
-- For PG >= 73:
-- Updated fix_geometry_columns() to consider schema when fixing attrelid and
-- varattnum, also changed empty value to 'public' string for records with
-- an invalid schema specification.
-- Updated DropGeometryColumn to actually issue the
-- ALTER TABLE DROP COLUMN query.
--
-- Revision 1.6  2003/12/23 09:00:12  strk
-- AddGeometryColumn, DropGeometryColum moved to version-specific scripts.
-- Schema support enabled for version 73 and 74.
--
-- Revision 1.5  2003/12/18 18:07:06  strk
-- Changed fix_geometry_columns() for PG >= 73 so to set f_table_schema to
-- the empty string if its value is not a valid pg namespace.
--
-- Revision 1.4  2003/11/28 11:25:31  strk
-- Added explicit geometry as text cast
--
-- Revision 1.3  2003/11/28 11:06:49  strk
-- Added WKB_recv function for binary WKB input
--
-- Revision 1.2  2003/11/19 15:29:21  strk
-- Added default btree operator class for PG7.4
--
-- Revision 1.1  2003/11/11 10:38:23  strk
-- Postgresql 7.4 enabler scripts.
--
-- Revision 1.4  2003/07/01 18:30:55  pramsey
-- Added CVS revision headers.
--
--
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
--  WKB

-- this has been moved here at _end from _start 
-- because we need the definition of function bytea
-- found in _common 
CREATE TYPE wkb (
	internallength = variable,
	input = wkb_in,
	output = wkb_out,
	storage = extended,
	send = bytea,
	receive = wkb_recv
);

--
-- 7.3 explicit casting definitions
--

CREATE CAST ( chip AS geometry ) WITH FUNCTION geometry(chip) AS IMPLICIT;
CREATE CAST ( geometry AS box3d ) WITH FUNCTION box3d(geometry) AS IMPLICIT;
CREATE CAST ( geometry AS box ) WITH FUNCTION box(geometry) AS IMPLICIT;
CREATE CAST ( box3d AS geometry ) WITH FUNCTION geometry(box3d) AS IMPLICIT;
CREATE CAST ( text AS geometry) WITH FUNCTION geometry(text) AS IMPLICIT;
CREATE CAST ( wkb AS bytea ) WITH FUNCTION bytea(wkb) AS IMPLICIT;
CREATE CAST ( box3d AS box ) WITH FUNCTION box3dtobox(box3d);
CREATE CAST ( geometry AS text ) WITH FUNCTION astext(geometry);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- UPDATE_GEOMETRY_STATS()

CREATE FUNCTION update_geometry_stats() 
RETURNS text
AS
'
BEGIN
	EXECUTE ''update geometry_columns set attrelid = (select pg_class.oid AS attrelid from pg_class,pg_attribute where relname =geometry_columns.f_table_name::name  and pg_attribute.attrelid = pg_class.oid and pg_attribute.attname = geometry_columns.f_geometry_column::name),  varattnum = (select pg_attribute.attnum from pg_class,pg_attribute where relname =geometry_columns.f_table_name::name and pg_attribute.attrelid = pg_class.oid and pg_attribute.attname = geometry_columns.f_geometry_column::name)'';
	execute ''update geometry_columns set stats = (build_histogram2d( create_histogram2d(find_extent(f_table_name,f_geometry_column),40 ),f_table_name::text, f_geometry_column::text))  '';	
	return ''done'';
END;
'
LANGUAGE 'plpgsql' ;

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- UPDATE_GEOMETRY_STATS( <table>, <column> )

CREATE FUNCTION update_geometry_stats(varchar,varchar) 
RETURNS text
AS
'
DECLARE
	tablename aliAS for $1;
	columnname aliAS for $2;

BEGIN
	EXECUTE ''update geometry_columns set attrelid = (select pg_class.oid AS attrelid from pg_class,pg_attribute where relname =geometry_columns.f_table_name::name  and pg_attribute.attrelid = pg_class.oid and pg_attribute.attname = geometry_columns.f_geometry_column::name),  varattnum = (select pg_attribute.attnum from pg_class,pg_attribute where relname =geometry_columns.f_table_name::name and pg_attribute.attrelid = pg_class.oid and pg_attribute.attname = geometry_columns.f_geometry_column::name)'';
	execute ''update geometry_columns set stats = (build_histogram2d( create_histogram2d(find_extent(''|| quote_literal(tablename) || '',''||quote_literal(columnname) ||''),40 ),''|| quote_literal(tablename) || ''::text,''||quote_literal(columnname) ||''::text )) WHERE f_table_name=''|| quote_literal(tablename) || ''and f_geometry_column=''||quote_literal(columnname) ;	
	return ''done'';
END;
'
LANGUAGE 'plpgsql' ;

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- CREATE_HISTOGRAM2D( <box>, <size> )
--
-- Returns a histgram with 0s in all the boxes.

CREATE FUNCTION create_histogram2d(box3d,int)
	RETURNS histogram2d
	AS '$libdir/libpostgis.so.0.8','create_histogram2d'
	LANGUAGE 'C'  with (isstrict);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- BUILD_HISTOGRAM2D( <histogram2d>, <tablename>, <columnname> )
--

CREATE FUNCTION build_histogram2d (histogram2d, text, text)
	RETURNS histogram2d
	AS '$libdir/libpostgis.so.0.8','build_histogram2d'
	LANGUAGE 'C'  with (isstrict);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- EXPLODE_HISTOGRAM2D( <histogram2d>, <tablename> )
--

CREATE FUNCTION explode_histogram2d (histogram2d, text)
	RETURNS histogram2d
	AS '$libdir/libpostgis.so.0.8','explode_histogram2d'
	LANGUAGE 'C'  with (isstrict);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- ESTIMATE_HISTOGRAM2D( <histogram2d>, <box> )
--

CREATE FUNCTION estimate_histogram2d(histogram2d,box)
	RETURNS float8
	AS '$libdir/libpostgis.so.0.8','estimate_histogram2d'
	LANGUAGE 'C'  with (isstrict);

CREATE FUNCTION postgisgistcostestimate(internal,internal,internal,internal,internal,internal,internal,internal)
	RETURNS opaque
	AS '$libdir/libpostgis.so.0.8','postgisgistcostestimate'
	LANGUAGE 'C'  with (isstrict);

--
-- 7.2 GiST support functions
--

CREATE FUNCTION ggeometry_consistent(internal,geometry,int4) 
	RETURNS bool 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION ggeometry_compress(internal) 
	RETURNS internal 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION gbox_penalty(internal,internal,internal) 
	RETURNS internal 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION gbox_picksplit(internal, internal) 
	RETURNS internal 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION gbox_union(bytea, internal) 
	RETURNS internal 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION gbox_same(box, box, internal) 
	RETURNS internal 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION rtree_decompress(internal) 
	RETURNS internal
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

--
-- 7.2 RTREE support functions
--

CREATE FUNCTION geometry_union(geometry,geometry) 
	RETURNS geometry 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION geometry_inter(geometry,geometry) 
	RETURNS geometry 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

CREATE FUNCTION geometry_size(geometry,internal) 
	RETURNS float4 
	AS '$libdir/libpostgis.so.0.8' 
	LANGUAGE 'C';

--
-- Create opclass index bindings
--

CREATE OPERATOR CLASS gist_geometry_ops
	DEFAULT FOR TYPE geometry USING gist AS
	OPERATOR	1	<< ,
	OPERATOR	2	&< ,
	OPERATOR	3	&& ,
	OPERATOR	4	&> ,
	OPERATOR	5	>> ,
	OPERATOR	6	~= ,
	OPERATOR	7	~ ,
	OPERATOR	8	@ ,
	FUNCTION	1	ggeometry_consistent (internal, geometry, int4),
	FUNCTION	2	gbox_union (bytea, internal),
	FUNCTION	3	ggeometry_compress (internal),
	FUNCTION	4	rtree_decompress (internal),
	FUNCTION	5	gbox_penalty (internal, internal, internal),
	FUNCTION	6	gbox_picksplit (internal, internal),
	FUNCTION	7	gbox_same (box, box, internal);

UPDATE pg_opclass 
	SET opckeytype = (select oid from pg_type where typname = 'box') 
	WHERE opcname = 'gist_geometry_ops';

CREATE OPERATOR CLASS btree_geometry_ops
	DEFAULT FOR TYPE geometry USING btree AS
	OPERATOR	1	< ,
	OPERATOR	2	<= ,
	OPERATOR	3	= ,
	OPERATOR	4	>= ,
	OPERATOR	5	> ,
	FUNCTION	1	geometry_cmp (geometry, geometry);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- FIX_GEOMETRY_COLUMNS()
--
-- Since 7.3 schema support has been added.
-- Previous postgis versions used to put the database name in
-- the schema column. This needs to be fixed, so we set to the
-- empty string any schema value not existing in pg_namespace
--

CREATE FUNCTION fix_geometry_columns() RETURNS text
AS 
'
BEGIN
	-- it would be better to find the correct schema name
	UPDATE geometry_columns SET f_table_schema = ''public''
		WHERE f_table_schema is NULL or f_table_schema NOT IN
		( SELECT nspname::varchar FROM pg_namespace );

	UPDATE geometry_columns SET attrelid = (
		SELECT c.oid 
		FROM pg_class c, pg_attribute a, pg_namespace n
		WHERE c.relname = geometry_columns.f_table_name::name
		AND a.attrelid = c.oid AND c.relnamespace = n.oid
		AND a.attname = geometry_columns.f_geometry_column::name
		AND n.nspname = geometry_columns.f_table_schema::name
		);
	
	UPDATE geometry_columns SET varattnum = (
		SELECT a.attnum
		FROM pg_class c, pg_attribute a, pg_namespace n
		WHERE n.nspname = geometry_columns.f_table_schema::name
		AND c.relname = geometry_columns.f_table_name::name
		AND a.attname = geometry_columns.f_geometry_column::name
		AND a.attrelid = c.oid AND c.relnamespace = n.oid
		);

	RETURN ''geometry_columns table is now linked to the system tables'';


END;
'
LANGUAGE 'plpgsql' ;

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- ADDGEOMETRYCOLUMN ( 
--      <catalog>, <schema>, <table name>, <column name>, 
--      <srid>, <type>, <dim> )
--
-- Type can be one of geometry, GEOMETRYCOLLECTION, POINT, MULTIPOINT, POLYGON,
-- MULTIPOLYGON, LINESTRING, or MULTILINESTRING.
--
-- Types (except geometry) are checked for consistency using a CHECK constraint
-- uses SQL ALTER TABLE command to add the geometry column to the table.
-- Addes a row to geometry_columns.
-- Addes a constraint on the table that all the geometries MUST have the same 
-- SRID. Checks the coord_dimension to make sure its between 0 and 3.
-- Should also check the precision grid (future expansion).
-- Calls fix_geometry_columns() at the end.
--
CREATE FUNCTION AddGeometryColumn(varchar,varchar,varchar,varchar,integer,varchar,integer)
	RETURNS text
	AS 
'
DECLARE
	catalog_name alias for $1;
	schema_name alias for $2;
	table_name alias for $3;
	column_name alias for $4;
	new_srid alias for $5;
	new_type alias for $6;
	new_dim alias for $7;

	rec RECORD;
	schema_ok bool;
	real_schema name;
BEGIN

	IF ( not ( (new_type =''GEOMETRY'') or
		   (new_type =''GEOMETRYCOLLECTION'') or
		   (new_type =''POINT'') or 
		   (new_type =''MULTIPOINT'') or
		   (new_type =''POLYGON'') or
		   (new_type =''MULTIPOLYGON'') or
		   (new_type =''LINESTRING'') or
		   (new_type =''MULTILINESTRING'')) )
	THEN
		RAISE EXCEPTION ''Invalid type name - valid ones are: 
			GEOMETRY, GEOMETRYCOLLECTION, POINT, 
			MULTIPOINT, POLYGON, MULTIPOLYGON, 
			LINESTRING, or MULTILINESTRING '';
		RETURN ''fail'';
	END IF;

	IF ( (new_dim >3) or (new_dim <0) ) THEN
		RAISE EXCEPTION ''invalid dimension'';
		RETURN ''fail'';
	END IF;

	IF ( schema_name != '''' ) THEN
		schema_ok = ''f'';
		FOR rec IN SELECT nspname FROM pg_namespace WHERE text(nspname) = schema_name LOOP
			schema_ok := ''t'';
		END LOOP;

		if ( schema_ok <> ''t'' ) THEN
			RAISE NOTICE ''Invalid schema name - using current_schema()'';
			SELECT current_schema() into real_schema;
		ELSE
			real_schema = schema_name;
		END IF;

	ELSE
		SELECT current_schema() into real_schema;
	END IF;

	EXECUTE ''ALTER TABLE '' || quote_ident(real_schema) ||
		''.'' || quote_ident(table_name) || 
		'' ADD COLUMN '' || quote_ident(column_name) || 
		'' geometry '';

	EXECUTE ''ALTER TABLE '' || quote_ident(real_schema) ||
		''.'' || quote_ident(table_name) || 
		'' ADD CHECK (SRID('' || quote_ident(column_name) ||
		'') = '' || new_srid || '')'' ;

	IF (not(new_type = ''GEOMETRY'')) THEN
		EXECUTE ''ALTER TABLE '' || quote_ident(real_schema) ||
			''.'' || quote_ident(table_name) || 
			'' ADD CHECK (geometrytype('' ||
			quote_ident(column_name) || '')='' ||
			quote_literal(new_type) || '' OR ('' ||
			quote_ident(column_name) || '') is null)'';
	END IF;

	EXECUTE ''INSERT INTO geometry_columns VALUES ('' ||
		quote_literal('''') || '','' ||
		quote_literal(real_schema) || '','' ||
		quote_literal(table_name) || '','' ||
		quote_literal(column_name) || '','' ||
		new_dim || '','' || new_srid || '','' ||
		quote_literal(new_type) || '')'';

	EXECUTE ''select fix_geometry_columns()'';
	--SELECT fix_geometry_columns();

	return ''Geometry column '' || column_name || '' added to table ''
		|| real_schema || ''.'' || table_name || '' WITH a SRID of '' || new_srid ||
		'' and type '' || new_type;
END;
' LANGUAGE 'plpgsql' WITH (isstrict);
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- ADDGEOMETRYCOLUMN ( <schema>, <table>, <column>, <srid>, <type>, <dim> )
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
CREATE FUNCTION AddGeometryColumn(varchar,varchar,varchar,integer,varchar,integer) RETURNS text AS '
DECLARE
	ret  text;
BEGIN
	SELECT AddGeometryColumn('''',$1,$2,$3,$4,$5,$6) into ret;
	RETURN ret;
END;
' LANGUAGE 'plpgsql' WITH (isstrict);

-- - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- DROPGEOMETRYCOLUMN( <schema name>, <table name>, <column name> )
--
-- There is no ALTER TABLE DROP COLUMN command in postgresql
-- There is no ALTER TABLE DROP CONSTRAINT command in postgresql
-- So, we:
--   1. remove the unwanted geom column reference from the 
--      geometry_columns table
--   2. update the table so that the geometry column is all NULLS
--      This is okay since the CHECK srid(geometry) = <srid> is not
--      checked if geometry is NULL (the isstrict attribute on srid())
--   3. add another constraint that the geometry column must be NULL
-- This, effectively kills the geometry column 
--   (a) its not in the geometry_column table
--   (b) it only has nulls in it
--   (c) you cannot add anything to the geom column because it must be NULL
-- 
-- This will screw up if you put a NOT NULL constraint on the geometry 
-- column, so the first thing we must do is remove this constraint (its a 
-- modification of the pg_attribute system table)
--
-- We also check to see if the table/column exists in the geometry_columns 
-- table 
CREATE FUNCTION DropGeometryColumn(varchar,varchar,varchar)
	RETURNS text
	AS 
'
DECLARE
	schema_name alias for $1;
	table_name alias for $2;
	column_name alias for $3;
	
	myrec RECORD;
	real_schema name;
	okay boolean;
	query text;

BEGIN


	-- Find, check or fix schema_name
	IF ( schema_name != '''' ) THEN
		okay = ''f'';

		FOR myrec IN SELECT nspname FROM pg_namespace WHERE text(nspname) = schema_name LOOP
			okay := ''t'';
		END LOOP;

		IF ( okay <> ''t'' ) THEN
			RAISE NOTICE ''Invalid schema name - using current_schema()'';
			SELECT current_schema() into real_schema;
		ELSE
			real_schema = schema_name;
		END IF;
	ELSE
		SELECT current_schema() into real_schema;
	END IF;

 	-- first we find out if the column is in the geometry_columns table
	okay = ''f'';
	FOR myrec IN SELECT * from geometry_columns where f_table_schema = text(real_schema) and f_table_name = table_name and f_geometry_column = column_name LOOP
		okay := ''t'';
	END LOOP; 
	IF (okay <> ''t'') THEN 
		RAISE EXCEPTION ''column not found in geometry_columns table'';
		RETURN ''f'';
	END IF;

	
	-- Remove ref from geometry_columns table
	EXECUTE ''delete from geometry_columns where f_table_schema = '' ||
		quote_literal(real_schema) || '' and f_table_name = '' ||
		quote_literal(table_name)  || '' and f_geometry_column = '' ||
		quote_literal(column_name);

	-- Remove table column
	EXECUTE ''ALTER TABLE '' || quote_ident(real_schema) || ''.'' ||
		quote_ident(table_name) || '' DROP COLUMN '' ||
		quote_ident(column_name);


	RETURN real_schema || ''.'' || table_name || ''.'' || column_name ||'' effectively removed.'';
	
END;
'
LANGUAGE 'plpgsql' WITH (isstrict);




END TRANSACTION;
