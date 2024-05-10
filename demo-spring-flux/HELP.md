# Mongo db

## Using commands docker and Mongo DB in lesson

###  cd docker
###  docker container ls
###  docker exec -it fc584da95535 mongosh --username root --password root --authenticationDatabase admin

## inside mongo
###
### > use test_db
### > db.createCollection("usr")
### > db.usr.insert({name: "John", age: 25})
### > db.usr.find({age: {$gt: 25}})
### > db.usr.update({name: "Paul"}, {$set: {age: 25}})
### > db.usr.find({name: "Paul"})
### >  db.usr.deleteOne({name: "John"})

