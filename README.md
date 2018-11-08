GET

curl -X  GET http://10.3.32.167:8080/SmartCar/car/carid=xxxxxx -i

POST

curl -X POST http://10.3.32.167:8080/SmartCar/car/update/carid=xxxx -d '{"info":"2341234"}' -i -H "Content-Type: application/json

PUT

curl -X PUT http://10.3.32.167:8080/SmartCar/car/create -d '{"carid":"asdfsa2", "info":"2341234"}' -i -H "Content-Type: application/json"

DELETE

curl -X  DELETE http://10.3.32.167:8080/SmartCar/car/delete/carid=jras1233 -i
