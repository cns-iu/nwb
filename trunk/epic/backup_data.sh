#!/bin/bash
# Backs up the production database and production "epic_data" folder.
# Compare with restore_backup.sh.

CURRENT_DATETIME=$(date "+%F-%H-%M-%S")

# Dump production database to an archive file
DATABASE_ARCHIVE=epic_database_$CURRENT_DATETIME.archive
pg_dump -h cns-dbp -U epic_dumper --schema=public -Fc epic_web > $DATABASE_ARCHIVE

# Dump production data to an archive directory then tar+gzip it
DATA_FOLDER_LOCAL_COPY=epic_data_$CURRENT_DATETIME
DATA_FOLDER_ARCHIVE=$DATA_FOLDER_LOCAL_COPY.tgz
mkdir -p $DATA_FOLDER_LOCAL_COPY
rsync -avz apache@epic:/home/epic_website/epic_data/ $DATA_FOLDER_LOCAL_COPY
tar -cvzf $DATA_FOLDER_ARCHIVE $DATA_FOLDER_LOCAL_COPY
rm -rf $DATA_FOLDER_LOCAL_COPY

# Package above two archives into one complete backup, then delete the source archives
BACKUP_FILE_PATH=$BACKUP_DIRECTORY/epic_backup_$CURRENT_DATETIME.tgz
tar -cvzf $BACKUP_FILE_PATH $DATABASE_ARCHIVE $DATA_FOLDER_ARCHIVE
rm $DATABASE_ARCHIVE
rm $DATA_FOLDER_ARCHIVE