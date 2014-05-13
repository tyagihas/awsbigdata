package com.amazonaws.services.dynamodb.loader;

import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

public class Loader {

	private AmazonDynamoDBClient client;
	
	public Loader() {
		String tableName = System.getProperty("kinesisapp.dynamodbtable");
		if (tableName != null) {
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
			tableKeySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
			request.setKeySchema(tableKeySchema);

			CreateTableResult result = client.createTable(request);
		}
	}
	
	public void put(String user, long timestamp, double x, double y) {
		
	}
}
