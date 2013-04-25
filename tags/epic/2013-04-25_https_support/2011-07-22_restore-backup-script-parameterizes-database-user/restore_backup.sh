#!/bin/bash
# Wipes the current target database and "epic_data" folder
# and replaces them with the specified backup.
# Compare with backup_data.sh.

BACKUP_FILENAME=epic_backup_$BACKUP_DATETIME.tgz
BACKUP_FILE_PATH=$BACKUP_DIRECTORY/$BACKUP_FILENAME

# Determine DATABASE_HOST and EPIC_SETTINGS by examining EPIC_HOST
if [ "$EPIC_HOST" == "cns-epic-dev" ]; then
	EPIC_SETTINGS=dev_settings
	DATABASE_HOST=cns-dbdev
	DATABASE_USER=epic_appuser
elif [ "$EPIC_HOST" == "cns-epic-stage" ]; then
	EPIC_SETTINGS=staging_settings
	DATABASE_HOST=cns-dbs
	DATABASE_USER=epic_appuser
elif [ "$EPIC_HOST" == "epic" ]; then
	EPIC_SETTINGS=production_settings
	DATABASE_HOST=cns-dbp
	DATABASE_USER=epic_user
else
	echo "Restore stopped: unexpected EPIC_HOST '$EPIC_HOST'."
	exit
fi

execute_on_host() {
	SCRIPT=$1
	USER=$2
	HOST=$3
	
	scp $SCRIPT $USER@$HOST:/tmp/$SCRIPT
	ssh $USER@$HOST "chmod +x /tmp/$SCRIPT"
	ssh $USER@$HOST "/tmp/$SCRIPT"
}

# Unpack the database archive (already put on the target host), wipe the current database, and load the archive
restore_database_backup() {
	local SCRIPT=restore_database.sh

	# Notice that wipeout.py will be whichever revision is currently deployed to EPIC_HOST
	# TODO Put warnings in wipeout.py itself, too?
	echo "echo Unpacking the database archive from the backup.."                                              > $SCRIPT
	echo "DATABASE_ARCHIVE=epic_database_$BACKUP_DATETIME.archive"                                           >> $SCRIPT
	echo "tar -xzf /tmp/$BACKUP_FILENAME \$DATABASE_ARCHIVE"                                                 >> $SCRIPT
	echo "echo   Done."                                                                                      >> $SCRIPT	
	echo "echo Wiping current database using django.db commands.."                                           >> $SCRIPT
	echo "cd /home/epic_website/epic_code/epic"                                                              >> $SCRIPT
	echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code"                                 >> $SCRIPT
	echo "python2.6 manage.py shell --settings=$EPIC_SETTINGS < wipeout.py"                                  >> $SCRIPT
	echo "echo   Done."                                                                                      >> $SCRIPT	
	echo "echo Loading database backup.."                                                                    >> $SCRIPT
	echo "cd"                                                                                                >> $SCRIPT
	echo "pg_restore --no-acl --no-owner -h $DATABASE_HOST -U $DATABASE_USER -d epic_web \$DATABASE_ARCHIVE" >> $SCRIPT
	echo "echo   Done."                                                                                      >> $SCRIPT
	echo "echo Rebuilding index.."                                                                           >> $SCRIPT
	echo "cd /home/epic_website/epic_code/epic"                                                              >> $SCRIPT
	echo "echo 'y' | python2.6 manage.py rebuild_index --settings=$EPIC_SETTINGS "                           >> $SCRIPT
	echo "chmod -R 777 /tmp/whoosh"                                                                          >> $SCRIPT
	
	execute_on_host $SCRIPT apache $EPIC_HOST
}

# Unpack the data folder archive (already put on the target host), wipe the current data folder, and load the archive
restore_data_folder_backup() {
	local SCRIPT=restore_data_folder.sh

	echo "echo Unpacking data folder archive from the backup.."      > $SCRIPT
	echo "DATA_FOLDER_NAME=epic_data_$BACKUP_DATETIME"              >> $SCRIPT
	echo "DATA_FOLDER_ARCHIVE=\$DATA_FOLDER_NAME.tgz"               >> $SCRIPT
	echo "tar -xzf /tmp/$BACKUP_FILENAME \$DATA_FOLDER_ARCHIVE"     >> $SCRIPT
	echo "echo   Done."                                             >> $SCRIPT
	echo "echo Wiping current data folder.."                        >> $SCRIPT
	echo "rm -rf /home/epic_website/epic_data/*"                    >> $SCRIPT	
	echo "echo   Done."                                             >> $SCRIPT
	echo "echo Loading data folder backup.."                        >> $SCRIPT
	echo "tar -xzf \$DATA_FOLDER_ARCHIVE"                           >> $SCRIPT
	echo "cp -R \$DATA_FOLDER_NAME/* /home/epic_website/epic_data/" >> $SCRIPT
	echo "echo   Done."                                             >> $SCRIPT
	
	execute_on_host $SCRIPT apache $EPIC_HOST
}


if [ ! -e "$BACKUP_FILE_PATH" ]; then
	echo "Restore stopped: backup file '$BACKUP_FILE_PATH' does not exist."
	exit
elif [ ! -s "$BACKUP_FILE_PATH" ]; then
	echo "Restore stopped: backup file '$BACKUP_FILE_PATH' is empty."
	exit
elif [ ! -r "$BACKUP_FILE_PATH" ]; then
	echo "Restore stopped: backup file '$BACKUP_FILE_PATH' is not readable."
	exit
else
	# Send backup to EPIC_HOST:/tmp/
	scp $BACKUP_FILE_PATH apache@$EPIC_HOST:/tmp/$BACKUP_FILENAME

	restore_database_backup	
	restore_data_folder_backup
fi