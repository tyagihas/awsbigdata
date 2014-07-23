from boto.dynamodb2.table import Table

table = Table('bootcamplog')
item = table.get_item(user='10011',timestamp='1403173738269')
print item['latitude']
print item['longitude']

