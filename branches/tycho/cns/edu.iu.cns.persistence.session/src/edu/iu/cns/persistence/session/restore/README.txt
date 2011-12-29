Validators are a special kind of algorithm that the standard file loading machinery can handle.
Validators declare themselves as validators that can validate certain types of files.
A validator's job, so to speak, is essentially to act as the "entry point" for a file getting into
the tool. Of course, they can also do load-time validation on the file as necessary.

As a design decision, I wanted session files to be importable via the standard File > Load and
drag-and-drop operations. But also, since exporting a session (File > Export Session) is an
explicit operation, I wanted importing a session (File > Import Session) to be one as well. Thus is
the reason for having both a validator AND an actual algorithm version of the same thing (session
importing).