# CS-360-Mobile-Architect-Programming

The purpose of this project was to create an android app that can read and write to a local database on the device to save and display data to the screen. To be less vague, a weight tracker app where you can enter weights each day to keep track of progress in weight loss or gaining fast and easy. The design was made simple, so anybody is able to quickly figure out all screens and know how to use it. You sign in or create an account which takes you to a screen with 3 buttons. One takes you to a screen that adds a weight, one takes you to a screen to add a goal weight, and another takes you to a  settings page. Once you add weights, they will show up on that screen with the 3 buttons in a scrollable list with the weight, the date it was entered and an 'x' button to delete the entry. Both the weights and dates can be edited on this page as well.

For this app, I have 1 class file that handles everything to do with the database. Then from all of the other pages and their respective class files, they will call the functions involving doing things like reading and writing from the database and update their UI according to the data they retrieve. It makes everything easy to follow and read. I did not get emulation working, but I was testing everything on my actual android device with every change to make sure all functionality worked without crashes. If it did crash the errors were easy to follow in the debug window. I had to make serveral code design changes when I ran into errors that I didn't realize were going to be an issue.

The biggest problem I cam across was when I was deleting things in the database. It was causing indexing issues if I just tried remvoing ui elements to the corresponding data. I ended up kind of fixing it, by just reloading the page when something is deleted. It makes a fresh table that is properly indexed for data retrieval when things are being edited in the database. A big thing Android Studio does is allow you to setup the app's ui simply by dragging an dropping and I think I did one better, but creating UI on the fly depending on what data it retrieves from the database. It needed a row of UI for each weight entry when I successfully pulled of by creating the layout first and everything that was added programmatically was automatically in that layout design created by hand.
