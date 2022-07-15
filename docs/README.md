Folder for design documents and any other documentation/notes.

To play the game: 
./compile.sh
./run.sh src/main/resources/board.xml src/main/resources/card.xml numPlayers     <- enter a number of players
UPDATED RUN: This may still work but the way we've been running it is by using the Run without Debugging 
command on VSCode and that way it has been working fine.

The game will prompt for how many players, and then each player for their name.

After all player names have been entered the game will start. The name of the player who's turn it is will be displayed.
The player has several options for their turn:


help - displays a list of all commands available with any prerequisite conditions to making a command. (This is for
the text version)

where am i - displays active player's current location (This is for
the text version)

move - displays neighboring Sets and then allows a player to enter a room they would like to move to

take role - displays all available roles and their required rank, then allows a player to type which role they want

act - rolls a die and displays if the act was successful or not, and pays the player accordingly

rehearse - adds a rehearsal chip to that player

upgrade rank - allows a player to upgrade their rank to a certain level with a specified currency 

end - ends turn unless player is working a role, in which case the player must act or rehearse first before ending their turn
