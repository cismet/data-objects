/*
 * Test.java
 *
 * Created on 14. Februar 2007, 16:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.diff.builder;

import de.cismet.diff.DiffAccessor;
import de.cismet.diff.container.PSQLStatement;
import de.cismet.diff.container.PSQLStatementGroup;
import de.cismet.diff.exception.ScriptGeneratorException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author Martin Scholl
 */
public class Test
{
  
  /** Creates a new instance of Test */
  public Test()
  {
    try
    {
      Properties p;
        p = new Properties();
        p.load(new BufferedInputStream(new FileInputStream(new File(
            this.getClass().getClassLoader().getResource(
            "de/cismet/diff/resource/runtime.properties").toURI()))));
      
      DiffAccessor da = new DiffAccessor(p);
      da.putDropAction("testtest");
      try
      {
        for(PSQLStatementGroup s : da.getStatementGroups())
        {
          if(s.isTransaction())
          {
            System.out.println(s.getDescription());
            if(s.getWarning() != null)
              System.out.println(s.getWarning());
            for(PSQLStatement psql : s.getPSQLStatements())
              System.out.println(psql.getStatement());
          }
          else
          {
            for(PSQLStatement psql : s.getPSQLStatements())
            {
              if(!psql.isPedantic())
              {
                if(s.getWarning() != null)
                  System.out.println(psql.getStatement() + " :: " + psql.getWarning());
                else
                  System.out.println(psql.getStatement());
              }
            }
          }
        }
      }
      catch (ScriptGeneratorException ex)
      {
        System.out.println(ex.getTable() + " :: " + ex.getColumn());
        ex.printStackTrace();
      }
    } catch (Exception ex)
    {
      ex.printStackTrace();
    } 
  }
  
  public static void main(String[] args)
  {
    //String[] sa = {"id", "server", "path"};
    //System.out.println(isDefaultValueValid("url_base", "prot_prefix", "12345678901234567890123456789012",sa));
    Test t = new Test();
  }
}
  
