## Assignment
* Build a simple game of higher or lower using given movie database. For example http://www.higherlowergame.com/

* Game rules:
    * Player can choose which category they want to play with (vote average, popularity, run time, revenue)
    * Player is given 2 different movies with chosen category value revealed
    * Player has to choose if the value on the 2nd movie is higher or lower than the chosen category of the 1st one
    * If the player chose correctly, the game continues, the 2nd movie will replace the first one, and a new movie is loaded for comparison
    * Game ends when the player makes an incorrect guess
    * Movies with same category values shouldn't be matched together

* Resources:
    * For the task h2 database with table movie is given. Table contains data about 4798 movies.
    * Table columns are original_title, overview, popularity, release_date, revenue, runtime, tagline, title, vote_average, vote_count

* Main tasks:
    * Build a Java console application using Java 1.8 or newer (we recommend using Java 17) which allows to play a game of "higher or lower"
    * Build a normal game mode with the rules from above
    * Build a hard game mode where at least every second round is between 2 movies that are closely ranked. For example if previous movies was with vote_average 7.9 then 2nd movie should be between values 7.7 and 8.1.
    * Player can choose between different game modes after each game
    * Remember the current highest score achieved for both game modes
    * At least title should be given for every movie, but feel free to add also additional info like (overview, tagline, original title, release date, tagline)
    * Feel free to add any more functionalities you think would be cool to add

* For additional credit:
    * Instead of console application, build a simple UI for the game, using your preferred framework (We prefer Angular, but feel free to use any Javascript framework you like)

* Our expectations:
    * Your code is available on GitHub or any other VCS
    * Your code is clean and logically structured
    * Game logic is separated from other functionalities
    * Your code has tests where necessary
    * You are ready to explain your game & code
    * You are aware of the possible bugs in your game & code
