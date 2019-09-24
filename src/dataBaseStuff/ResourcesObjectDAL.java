/**
 * 
 */
package dataBaseStuff;

import java.sql.ResultSet;

import classes.ResourceItem;
import classes.ResourceType;
import dataBaseStuff.*;
import helpers.*;
import querying.adapters.memory.InMemoryAdapter;

/**
 * @author authoro
 *
 */
public class ResourcesObjectDAL {

	private void Insert(ResourceItem inp) throws Exception {

		String Query = " INSERT INTO `droidForensics`.`ResourceItems`(`id`,`Title`,"
				+ "`Path`,`Number`,`Description`,`Type`) VALUES (";
		Query += "'" + inp.id + "',";
		Query += "'" + inp.Title + "',";
		Query += "'" + inp.Path + "',";
		Query += "'" + inp.Number + "',";
		Query += "'" + inp.Description + "',";
		Query += "'" + inp.Type + "');";

		DataBaseLayer db = new DataBaseLayer();
		db.runUpdateQuery(Query);
	}

	private void ReadAll() throws Exception {
		String query = "select * from `droidForensics`.`ResourceItems`;";
		DataBaseLayer db = new DataBaseLayer();
		ResultSet ret = db.RunSelectQuery(query);
		while (ret.next()) {
			itemFromDBRow(ret, true);
		}
	}

	private void ReadSome( String Criteria ) throws Exception{
		String query = "select * from `droidForensics`.`ResourceItems` where "+ Criteria +";";
		DataBaseLayer db = new DataBaseLayer();
		ResultSet ret = db.RunSelectQuery(query);
		while (ret.next()) {
			itemFromDBRow(ret, true);
		}	
	}
	
	private ResourceItem itemFromDBRow(ResultSet inp, boolean saveToMem) throws Exception {
		ResourceItem ret = new ResourceItem();
		ret.id = inp.getString("id");
		ret.Title = inp.getString("Title");
		ret.Path = inp.getString("Path");
		ret.Number = inp.getString("Number");
		ret.Description = inp.getString("Description");
		ret.Type = ResourceType.valueOf(inp.getString("Type"));

		if (saveToMem) {
			InMemoryAdapter mem = InMemoryAdapter.getSignleton();
			mem.addResourceItem(ret);
		}
		return ret;
	}

}
