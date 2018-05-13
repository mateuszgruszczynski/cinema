FROM java:8
WORKDIR /
COPY jar/cinema-http-mysql.jar cinema-http.jar
#COPY jar/cinema-http_1_4_16.jar cinema-http.jar
EXPOSE 9000:9000
#CMD ls -al
CMD echo "10.10.0.10" > /etc/hostname
CMD java -jar cinema-http.jar
