package com.lsgllc.parser;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import sun.font.CreatedFontTracker;

import java.io.StringReader;

/**
 * Created By: sameloyiv
 * Date: 3/12/14
 * Time: 9:16 AM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public class JSqlTester {
    public static void main(String... args) throws Exception {
        new JSqlTester().run(new String []{"CREATE TABLE \"SARTS01D\".\"F0001\" (\n" +
                "  \"ISN\" DECIMAL(10, 0) NOT NULL\n" +
                ", \"F_AA\" VARGRAPHIC(17) DEFAULT ' '\n" +
                ", \"D_AA\" VARCHAR(17) FOR BIT DATA\n" +
                ", \"F_CB\" VARGRAPHIC(9) \n" +
                ", \"D_CB\" VARCHAR(9) FOR BIT DATA\n" +
                ", \"F_CE\" GRAPHIC(1) \n" +
                ", \"F_CF\" GRAPHIC(1) \n" +
                ", \"F_CG\" GRAPHIC(1) \n" +
                ", \"F_CJ\" VARGRAPHIC(19) \n" +
                ", \"F_CK\" GRAPHIC(2) \n" +
                ", \"F_CM\" VARGRAPHIC(5) \n" +
                ", \"F_CN\" GRAPHIC(4) \n" +
                ", \"F_CO\" GRAPHIC(4) \n" +
                ", \"F_CP\" DECIMAL(1, 0) \n" +
                ", \"F_CQ\" DECIMAL(1, 0) \n" +
                ", \"F_DA\" VARGRAPHIC(22) DEFAULT ' '\n" +
                ", \"D_DA\" VARCHAR(22) FOR BIT DATA\n" +
                ", \"F_EA\" VARGRAPHIC(17) \n" +
                ", \"F_EB\" DECIMAL(8, 0) \n" +
                ", \"F_FB\" GRAPHIC(1) \n" +
                ", \"F_FC\" DECIMAL(2, 0) \n" +
                ", \"F_FD\" DECIMAL(8, 0) \n" +
                ", \"F_FE\" GRAPHIC(2) \n" +
                ", \"F_FG\" DECIMAL(1, 0) \n" +
                ", \"F_GB\" VARGRAPHIC(24) \n" +
                ", \"F_GC\" VARGRAPHIC(11) \n" +
                ", \"F_GD\" GRAPHIC(2) \n" +
                ", \"F_GE\" DECIMAL(1, 0) \n" +
                ", \"F_HB\" GRAPHIC(2) \n" +
                ", \"F_HC\" VARGRAPHIC(8) \n" +
                ", \"F_HD\" GRAPHIC(4) \n" +
                ", \"F_HE\" DECIMAL(4, 0) \n" +
                ", \"F_HF\" GRAPHIC(3) \n" +
                ", \"F_HG\" DECIMAL(4, 2) \n" +
                ", \"F_HH\" GRAPHIC(4) \n" +
                ", \"F_HI\" DECIMAL(4, 0) \n" +
                ", \"F_HJ\" GRAPHIC(1) \n" +
                ", \"F_HK\" VARGRAPHIC(22) \n" +
                ", \"F_HL\" DECIMAL(2, 0) \n" +
                ", \"F_HM\" GRAPHIC(1) \n" +
                ", \"F_HN\" VARGRAPHIC(6) \n" +
                ", \"F_HO\" DECIMAL(10, 2) \n" +
                ", \"F_HP\" DECIMAL(8, 0) \n" +
                ", \"F_HQ\" DECIMAL(8, 2) \n" +
                ", \"F_HR\" VARGRAPHIC(6) \n" +
                ", \"F_HT\" DECIMAL(6, 0) \n" +
                ", \"F_HU\" DECIMAL(1, 0) \n" +
                ", \"F_IB\" DECIMAL(1, 0) \n" +
                ", \"F_IC\" DECIMAL(1, 0) \n" +
                ", \"F_ID\" DECIMAL(1, 0) \n" +
                ", \"F_IE\" DECIMAL(1, 0) \n" +
                ", \"F_IF\" DECIMAL(1, 0) \n" +
                ", \"F_IG\" DECIMAL(1, 0) \n" +
                ", \"F_IH\" DECIMAL(1, 0) \n" +
                ", \"F_II\" DECIMAL(1, 0) \n" +
                ", \"F_IJ\" DECIMAL(1, 0) \n" +
                ", \"F_IK\" DECIMAL(1, 0) \n" +
                ", \"F_IL\" DECIMAL(1, 0) \n" +
                ", \"F_IM\" DECIMAL(1, 0) \n" +
                ", \"F_IN\" DECIMAL(1, 0) \n" +
                ", \"F_IO\" DECIMAL(1, 0) \n" +
                ", \"F_IP\" DECIMAL(1, 0) \n" +
                ", \"F_IQ\" DECIMAL(1, 0) \n" +
                ", \"F_IR\" DECIMAL(1, 0) \n" +
                ", \"F_IT\" DECIMAL(1, 0) \n" +
                ", \"F_IU\" DECIMAL(1, 0) \n" +
                ", \"F_IV\" DECIMAL(1, 0) \n" +
                ", \"F_IW\" DECIMAL(1, 0) \n" +
                ", \"F_KA\" DECIMAL(8, 2) \n" +
                ", \"F_KB\" GRAPHIC(2) \n" +
                ", \"F_KC\" DECIMAL(8, 0) \n" +
                ", \"F_LA\" VARGRAPHIC(9) \n" +
                ", \"F_LB\" DECIMAL(8, 0) \n" +
                ", \"F_MC\" VARGRAPHIC(19) \n" +
                ", \"F_MD\" GRAPHIC(2) \n" +
                ", \"F_MF\" VARGRAPHIC(5) \n" +
                ", \"F_MG\" GRAPHIC(4) \n" +
                ", \"F_ND\" GRAPHIC(2) \n" +
                ", \"F_OA\" GRAPHIC(1) \n" +
                ", \"F_OB\" DECIMAL(3, 0) \n" +
                ", \"F_OC\" DECIMAL(2, 0) \n" +
                ", \"F_OD\" DECIMAL(1, 0) \n" +
                ", \"F_OE\" DECIMAL(1, 0) \n" +
                ", \"F_PA\" DECIMAL(8, 0) \n" +
                ", \"F_PB\" DECIMAL(1, 0) \n" +
                ", \"F_PC\" DECIMAL(1, 0) DEFAULT 0\n" +
                ", \"F_PD\" GRAPHIC(1) DEFAULT ' '\n" +
                ", \"F_PE\" GRAPHIC(1) DEFAULT ' '\n" +
                ", \"F_QA\" GRAPHIC(1) DEFAULT ' '\n" +
                ", \"F_QB\" GRAPHIC(1) DEFAULT ' '\n" +
                ", \"F_QC\" GRAPHIC(3) \n" +
                ", \"F_QD\" GRAPHIC(3) \n" +
                ", \"F_QE\" DECIMAL(8, 0) \n" +
                ", \"F_QF\" VARGRAPHIC(30) \n" +
                ", \"F_QG\" VARGRAPHIC(30) \n" +
                ", \"F_QH\" DECIMAL(1, 0) \n" +
                ", \"F_QI\" DECIMAL(1, 0) \n" +
                ", \"F_QJ\" DECIMAL(1, 0) \n" +
                ", \"F_RA\" VARGRAPHIC(17) \n" +
                ", \"D_RA\" VARCHAR(17) FOR BIT DATA\n" +
                ", \"F_RB\" VARGRAPHIC(22) \n" +
                ", \"D_RB\" VARCHAR(22) FOR BIT DATA\n" +
                ", \"F_RC\" GRAPHIC(2) \n" +
                ", \"F_RD\" DECIMAL(8, 0) \n" +
                ", \"F_RE\" VARGRAPHIC(6) \n" +
                ", \"F_RF\" DECIMAL(8, 0) \n" +
                ", \"F_RG\" DECIMAL(8, 0) \n" +
                ", \"F_RH\" DECIMAL(8, 0) \n" +
                ", \"F_RI\" GRAPHIC(2) \n" +
                ", \"F_RJ\" DECIMAL(8, 0) \n" +
                ", \"F_RK\" GRAPHIC(2) \n" +
                ", \"F_RL\" DECIMAL(8, 0) \n" +
                ", \"D_ZA\" VARCHAR(19) FOR BIT DATA\n" +
                ", CONSTRAINT \"F0001_PK\" PRIMARY KEY (\"ISN\")\n" +
                ")|"});
    }

    private void run(String[] args) throws JSQLParserException {
        if (args[0].isEmpty()) {
            return;
        }
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvdb2);
        sqlparser.sqltext = args[0];
        sqlparser.parse();
        TCreateTableSqlStatement createTable = (TCreateTableSqlStatement)sqlparser.sqlstatements.get(0);
        TTable table = createTable.tables.getTable(0);
        TObjectName o;
        System.out.println("Create statement, find out what clause a TObjectName belongs to:");
        for(int i=0;i<table.getCteColomnReferences().size();i++){
            o = table.getCteColomnReferences().getObjectName(i);
            System.out.println(o.toString()+"\t\t\tlocation:"+o.getColumnNameOnly());
        }







    }
    // CREATE TABLE
}
