package com.amazonaws.services.dynamodb.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

public class Loader {

	private AmazonDynamoDBClient client;
	private String tableName;
	
	public Loader() {
		tableName = System.getProperty("kinesisapp.dynamodbtable");
		if (tableName.equals("")) 
			tableName = null;
		
		if (tableName != null) {
			// Assuming a dynamo table already exists
			client = new AmazonDynamoDBClient();
			/*
			try {
				client = new AmazonDynamoDBClient();

				ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
				.withReadCapacityUnits(1L)
				.withWriteCapacityUnits(1L);
				CreateTableRequest request = new CreateTableRequest()
				.withTableName(tableName)
				.withProvisionedThroughput(provisionedThroughput);

				ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
				attributeDefinitions.add(new AttributeDefinition().withAttributeName("user").withAttributeType("S"));
				request.setAttributeDefinitions(attributeDefinitions);

				ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
				tableKeySchema.add(new KeySchemaElement().withAttributeName("user").withKeyType(KeyType.HASH));
				request.setKeySchema(tableKeySchema);

				CreateTableResult result = client.createTable(request);
				System.out.println("Creating table : " + tableName);
			} catch(ResourceInUseException e) {
				System.out.println(tableName + " already exists.");
			}
			*/
		}
	}
	
	public void put(String user, long timestamp, double x, double y) {
		if (tableName != null) {
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			item.put("user", new AttributeValue().withS(user));
			item.put("timestamp", new AttributeValue().withS(Long.toString(timestamp)));
			item.put("latitude", new AttributeValue().withS(Double.toString(x)));
			item.put("longitude", new AttributeValue().withS(Double.toString(y)));

			PutItemRequest putItemRequest = new PutItemRequest()
			.withTableName(tableName)
			.withItem(item);
			PutItemResult result = client.putItem(putItemRequest);
		}
	}
}
