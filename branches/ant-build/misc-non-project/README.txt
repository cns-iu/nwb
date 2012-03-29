The Eclipse CheckStyle website is: http://eclipse-cs.sourceforge.net/
The Eclipse Checkstyle update site is: http://eclipse-cs.sf.net/update/

Once you install CheckStyle into your Eclipse, you can start using the CNS Checkstyle Configuration in Eclipse by going to Window -> Preferences -> CheckStyle, then select "New..." Change Type to External Configuration File, then set the Location to the CNS_Checkstyle_Config.xml you downloaded, then name it and hit "Ok", then select it in the list and click "Set as Default".

To check a project with CheckStyle in Eclipse, right-click the project, then click Checkstyle -> Activate Checkstyle, then right-click again and click Checkstyle -> Check Code with Checkstyle.

As part of every code review, a CheckStyle check should be performed, and all the CheckStyle errors should be corrected unless you have a good specific reason to ignore it. If there is any disagreement with CheckStyle's recommendations, they should be reconfigured by consensus with the development team, and then re-committed into the repository for everyone to share.


