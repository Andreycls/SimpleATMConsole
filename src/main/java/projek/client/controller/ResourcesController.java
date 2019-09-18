package projek.client.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.image.RescaleOp;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ResourcesController {

    public JSONObject getCompanyAccountNumber() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try (
                Reader reader = new FileReader(ResourcesController.class.getClassLoader().getResource("companyAccountNumber.json").getPath())) {
             jsonObject = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        if( jsonObject==null){
            jsonObject = new JSONObject();

        }
        return jsonObject;

    }
}
