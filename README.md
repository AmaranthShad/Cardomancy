# **CARDOMANCY**

~ Do you have hundreds, thousands of trading cards? Maybe you made too many decks or configurations, 
and now you want all of them closer at hand? Then *Cardomancy* is for you!

**Users can register and create collections of their favorite TCGs!**
## Current TCGs:
  ~  Magic: The Gathering
##
~ Users can friend other users, send messages, and view each other's collections! 
Users can also send trade requests, and sort their own collections to ensure they meet standards!


~ Want a specific card, but you can't remember the name? No worries! Cardomancy offers 
a whole selection of search options and parameters including CMC, EDH Rank, Collector #'s, and more.

~ Want to know more about the Devs behind the curtain? Visit our About page to reach our personal LinkedIn's and GitHub's.

### To Run Cardomancy:
~ Visit https://scryfall.com/docs/api/bulk-data and download the most recent, generic bulk download of cards.(Large file, can be deleted after initial data base creation. *360mb*)
(We are hoping to phase this out by launching C. as a live site and adding an auto-loader)

~ Place the file in the root of the *java* folder.

~ Update the passwords in the *application properties* file to reflect your postgres config.

~ Run sh create.sh to set the database, and follow the prompts. Load time may vary, and may take up to 3-5 minutes.

~ Run the server

~ Open the *vue* folder in VSC, and run the npm install command.

~ Run the npm run serve command, toggle the site link, and you should be all set!

~ Register yourself, create, and have fun!
