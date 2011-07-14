#!/bin/bash

CURRENT_DATETIME=$(date "+%F-%H-%M-%S")

# Dump production database to an archive file
DATABASE_ARCHIVE_FILE_PATH=$BACKUP_PATH/epic_database.$CURRENT_DATETIME.archive
pg_dump -h cns-dbp -U epic_dumper --schema=public -Fc epic_web > $DATABASE_ARCHIVE_FILE_PATH

# Dump production data to an archive directory then tar+gzip it
DATA_ARCHIVE_PATH=$BACKUP_PATH/epic_data_$CURRENT_DATETIME
mkdir -p $DATA_ARCHIVE_PATH
rsync -avz apache@epic:/home/epic_website/epic_data/ $DATA_ARCHIVE_PATH
tar -cvzf $DATA_ARCHIVE_PATH.tar.gz $DATA_ARCHIVE_PATH
rm -rf $DATA_ARCHIVE_PATH