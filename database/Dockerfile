FROM mysql:8.0.29

COPY ./init/01.sql /docker-entrypoint-initdb.d/01.sql
EXPOSE 3306
RUN chmod -R 777 /var/lib/mysql
