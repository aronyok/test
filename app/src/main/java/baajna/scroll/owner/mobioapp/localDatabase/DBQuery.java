/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baajna.scroll.owner.mobioapp.localDatabase;

import java.util.ArrayList;

/**
 *
 * @author Jewel
 */
public class DBQuery {

    //field data type
    public static final String INTEGER="integer";
    public static final String TEXT="text";

    //field data type with 
    public static final String INTEGER_PRI="integer primary key";
    public static final String INTEGER_PRI_AUTO="integer primary key autoincrement";


    //if get any error
    public static final String ERROR="error";
    //name of table
    private String tableName;

    private MField field;
    //list of table fields
    private ArrayList<MField> fields;

    private boolean isValidTable;

    private static ArrayList<String> tables;

    //for getting query
    private String selectedID,fromTbl,innerTbl,whereClause,orderBy,groupbBy;

    public DBQuery(){
        field=new MField();
        fields=new ArrayList<MField>();
        if(tables==null)
            tables=new ArrayList<String>();

    }
    public static DBQuery init(){
        return new DBQuery();
    }
    //*****************************************************************
    //startinig query func
    public  DBQuery select(String fileds){
        selectedID=fileds.trim();
        return this;
    }
    public  DBQuery from(String tableName){

        fromTbl=tableName.trim();
        return this;
    }
    public  DBQuery where(String where,String value){
        if(!where.trim().isEmpty()&&!value.trim().isEmpty())
            whereClause=where.trim()+"='"+value.trim()+"'";
        return this;
    }
    public  DBQuery groupBy(String value){
        if(!value.trim().isEmpty())
            groupbBy=value.trim();
        return this;
    }

    public  DBQuery orderBy(String where,String value){
        if(!where.trim().isEmpty()&&!value.trim().isEmpty())
            whereClause=where.trim()+"='"+value.trim()+"'";
        return this;
    }

    public String get(){
        String finalQuery=ERROR;
        if(selectedID!=null&&!selectedID.isEmpty()&&fromTbl!=null&&!fromTbl.isEmpty()){
            whereClause=(whereClause!=null&&!whereClause.isEmpty())?" where "+whereClause:"";
            finalQuery="select "+selectedID+" from "+fromTbl+whereClause;
        }


        return finalQuery;
    }
    //*****************************************************************
    //starting new table func
    public DBQuery newTable(String tableName){
        if(!tables.contains(tableName)){
            tables.add(tableName);
            this.tableName=tableName;
            isValidTable=true;

        }


        return this;
    }
    private boolean isFieldExists(String name){
        for(int i=0;i<fields.size();i++){
            if(fields.get(i).name.equalsIgnoreCase(name))  {
                return true;
            }

        }
        return false;
    }
    public DBQuery addField(String name,String type){

        if(!isFieldExists(name)){
            field=new MField();
            field.setName(name);
            field.setType(type);
            fields.add(field);
        }


        return this;
    }


    public String getTable(){
        if(!isValidTable)
            return ERROR;
        String a="";
        for(int i=0;i<fields.size();i++){
            a+=fields.get(i).getName()+" "+fields.get(i).getType()+((i<fields.size()-1)?",":"");
        }

        String query="create table "+tableName+" ("+a+")";
        return query;
    }

    class MField{
        private String name,type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }
}