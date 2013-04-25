pg_dump -h cns-dbp -U epic_dumper --schema=public -Fc epic_web > epic_database.archive
rsync -avz cns-dbp:/home/epic_website/epic_data epic_data_backup