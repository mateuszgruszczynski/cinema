FROM jgoerzen/debian-base-apache-php
EXPOSE 80:80
RUN apt-get -qq update && apt-get -qq install php7.0-mysql
COPY www /var/www/html