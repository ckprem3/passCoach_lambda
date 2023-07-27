package passcoach.checks;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DBChecks {
    public static final String LEAK = "leak";
    public static final String DICTIONARY = "dictionary";
    LambdaLogger logger;
    AmazonDynamoDB client;
    DynamoDB dynamoDB;


    public DBChecks(LambdaLogger logger) {
        logger.log("Init DB client");
        this.logger = logger;
        client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDB = new DynamoDB(client);
        logger.log("DB client done");
    }

    public Boolean checkLeaked(String pass) {
        return getItem(pass, LEAK);

    }

    public Boolean checkDictionary(String pass) {
        return getItem(pass, DICTIONARY);
    }

    private Boolean getItem(String pass, String tableName) {
        logger.log("Checking " + tableName + " table");
        Boolean found;
        try {
            Table table = dynamoDB.getTable(tableName);

            Item item = table.getItem("words", pass);
            found = item != null;
            logger.log("Check finished in DB, returning: " + found + " for: " + item);
        } catch (Exception e) {
            logger.log(String.valueOf(e));
            found = null;
        }
        return found;
    }

    public void save(String pass, String explodedResult) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("time", new AttributeValue(String.valueOf(new Date().getTime())));
        item.put("pass", new AttributeValue(pass));
        item.put("result", new AttributeValue(explodedResult));
        item.put("created_on", new AttributeValue(new Date().toString()));
        try {
            client.putItem("queries", item);
            logger.log("Saved to db:" + item);
        } catch (Exception e) {
            logger.log(String.valueOf(e));
        }
    }
}