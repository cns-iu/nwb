
#global logger

from java.sql import *

#for some reason problems importing
#from org.osgi.service.log import LogService



#global connection






def info(text):
    logger.log(3, text) #2 is LOG_WARNING. This is a hack due to import problems

def warn(text):
    logger.log(2, text) #2 is LOG_WARNING. This is a hack due to import problems

def error(text):
    logger.log(1, text) #1 is LOG_ERROR. This is a hack due to import problems

def create_table(statement, table_name, table_statement, cascade_constraints, simple_constraints):
    try:
        statement.execute(table_statement)
        for constraint_index in range(len(cascade_constraints)):
            try:
                statement.execute(cascade_constraints[constraint_index])
            except SQLException, cascade_exception:
                try:
                    statement.execute(simple_constraints[constraint_index])
                except SQLException, simple_exception:
                    info("Unable to set cascading or simple constraint on table. Table %s will lack referential integrity." % table_name)
                    cascade_exception.printStackTrace()
                    simple_exception.printStackTrace()
    except SQLException, table_exception:
        table_exception.printStackTrace()
        info("Problem creating table %s. Table probably already exists: %s" % (table_name, table_exception.getMessage()))
    

def create_database_structure():
    info("""Creating bibliometric tables (if they do not already exist).
    - nwb_journals
    """)
    statement = connection.createStatement()
    try:
        statement.execute("""CREATE TABLE nwb_journals (
                                journal_id int NOT NULL PRIMARY KEY,
                                journal_name VARCHAR(255) NOT NULL UNIQUE)""")
    except SQLException:
        info("Problem creating table nwb_journals. Table probably already exists.")
    statement.execute("INSERT INTO nwb_journals (journal_id, journal_name) VALUES (3, 'Falafel')")
    info("It worked? %s" % statement.getUpdateCount())
    statement.execute("SELECT journal_name FROM nwb_journals")
    results = statement.getResultSet()
    results.next()
    info(results.getString("journal_name"))
    

def initialize_records(data):
    current = {}
    records = []
    tag = ''
    for line in data: #needs to be preset by outside stuff
        if line.startswith('ER'):
            records.append(current)
            current = {}
        elif len(line.strip()) == 0: #empty line
            pass
        elif line.startswith(' '):
            current[tag] += line.lstrip()
        else:
            stuff = line.split(None, 1)
            if len(stuff) == 1:
                continue
            else:
                tag, value = stuff
            current[tag] = value
    return records



def normalize_records(records):
    new_records = {}
    
    for record in records:
        if 'UT' not in record:
            error("Malformed ISI file. Missing ISI's unique article identifier from record number %s" % records.index(record))
            return False
        if 'TC' in record:
            record['TC'] = int(record['TC'])
        else:
            record['TC'] = 0
            warn("Record %s missing Times Cited (TC) key. This is unusual." % record['UT'])
    
    for index_one in range(len(records)): # why did I use indices here?
        record_one = records[index_one]
        if record_one['UT'] in new_records:
            new_records[record_one['UT']]['TC'] = max(new_records[record_one['UT']]['TC'], record_one['TC'])
        else:
            new_records[record_one['UT']] = record_one
    return new_records.values()


def prepare_authors(record):
    authors = []
    author = {}
    
    return authors

def prepare_article(record):
    article = {}
    
    return article

def prepare_journal(record):
    journal = {}
    
    return journal

def insert_journal(journal):
    return 0

def insert_article(article, journal_id):
    return 0

def insert_author(author, article_id):
    return 0

def insert_data(article, journal, authors):
    journal_id = insert_journal(journal)
    article_id = insert_article(article, journal_id)
    for author in authors:
        insert_author(author, article_id)


def prepare_records(data):
    return normalize_records(initialize_records(data))

def insert_records(records):
    for record in records:
        article = prepare_article(record)
        journal = prepare_journal(record)
        authors = prepare_authors(record)
        
        insert_data(article, journal, authors)
        
info("In Python Script")
create_database_structure()
#insert_records(prepare_records(data))
