
# For development

with java 11 Jdk, open each service and run them in your favourite Java IDE

## links

- [swagger api patient service](http://localhost:8080/patient-service/swagger-ui.html#/patient-rest) on port 8080
- [swagger api practitioner service](http://localhost:8081/practitioner-service/swagger-ui.html#/practitioner-rest) on port 8081
- [swagger api appointment service](http://localhost:8082/appointment-service/swagger-ui.html#/appointment-rest) on port 8082
- [swagger api medical record service](http://localhost:8083/medical-record-service/swagger-ui.html#/medical-record-rest) on port 8083
- [health gateway service](http://localhost:8084/health) on port 8084
- [admin eureka server service](http://localhost:8085/admin/#/) on port 8085

# For local pre-production / tests

## Building images

for each service build and run the image locally

```sh
cd patient-service
docker build -t practitioner-service:latest .
docker run -d -p 8080:8080 practitioner-service:latest

cd ..

cd practitioner-service
docker build -t patient-service:latest .
docker run -d -p 8081:8081 patient-service:latest

cd ..

cd appointment-service
docker build -t appointment-service:latest .
docker run -d -p 8082:8082 appointment-service:latest

cd ..

cd medical-record-service
docker build -t medical-record-service:latest .
docker run -d -p 8083:8083 medical-record-service:latest

cd ..

cd gateway-service
docker build -t gateway-service:latest .
docker run -d -p 8084:8084 gateway-service:latest

cd ..

cd eureka-server-service
docker build -t eureka-server-service:latest .
docker run -d -p 8085:8085 eureka-server-service:latest
```

# For cluster pre-production / production

Build image as above and push to your docker registry

```sh
docker tag patient-service:latest <your-registry>/patient-service:latest
docker push <your-registry>/patient-service:latest

docker tag practitioner-service:latest <your-registry>/practitioner-service:latest
docker push <your-registry>/practitioner-service:latest

docker tag appointment-service:latest <your-registry>/appointment-service:latest
docker push <your-registry>/appointment-service:latest

docker tag medical-record-service:latest <your-registry>/medical-record-service:latest
docker push <your-registry>/medical-record-service:latest

docker tag gateway-service:latest <your-registry>/gateway-service:latest
docker push <your-registry>/gateway-service:latest

docker tag eureka-server-service:latest <your-registry>/eureka-server-service:latest
docker push <your-registry>/eureka-server-service:latest
```

make sure to update the image in the kubernetes deployment files and apply them

```sh
kubectl apply -f patient-service/deployment.yaml
kubectl apply -f practitioner-service/deployment.yaml
kubectl apply -f appointment-service/deployment.yaml
kubectl apply -f medical-record-service/deployment.yaml
kubectl apply -f gateway-service/deployment.yaml
kubectl apply -f eureka-server-service/deployment.yaml
```

an your good !
