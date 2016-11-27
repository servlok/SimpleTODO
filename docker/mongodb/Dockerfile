FROM mongo:latest
# Modify child mongo to use /data/db2 as dbpath (because /data/db wont persist the build)
RUN mkdir -p /data/mongodb \
    && echo "dbpath = /data/mongodb" > /etc/mongodb.conf \
    && chown -R mongodb:mongodb /data/mongodb

COPY create_db.js /data/mongodb/create_db.js

RUN mongod --fork --logpath /var/log/mongodb.log --dbpath /data/mongodb --smallfiles \
    && CREATE_FILES=/data/mongodb/create_db.js \
    && for f in $CREATE_FILES; do mongo 127.0.0.1:27017 $f; done \
    && mongod --dbpath /data/mongodb --shutdown \
    && chown -R mongodb /data/mongodb

# Make the new dir a VOLUME to persists it 
VOLUME /data/mongodb

