# ConverstationDisplayer

## Opening the app
When opening the app, the first screen shown is to input the username of 'self' in the conversation. The messages from this user will be right aligned and messages from other users are left aligned.
*Username validations* : Valid usernames consist of just 1 word. Checking if this username matches with the message's author is *case sensitive*.

## Messages List
* Messages from self are right aligned and messages from other authors are left aligned;
* It's possible to swipe down the list to refresh it;
* After the first successful data loading, the list is stored locally. On future occurrences, if there's no connection the list displayed is the one stored locally. Every time that a new data set is loaded, the previous one stored on persistence is replaced with the new one. Only one dataset is stored at a time, to avoid the growing of the database without any specific need (for now);
* When tapping the search icon, the search becomes active and the search action is triggered once the user clicks the search button on the keyboard. Supported filters
  * *before:yyyy-MM-dd* searches for messages before the specified date (exclusivé). If the user searches for *before:2016-07-04*, possible results will not include the 4th of July, only the 3rd and previous days;
  * *after:yyyy-MM-dd* searches for messages after the specified date (exclusivé). If the user searches for *after:2016-07-04*, possible results will not include the 4th of July, only the 5th and following days;
  * *from:username* searches for messages whose author is *username*. This search is case insensitive;
  * *free text search* every expression that doesn't fit in the previous tags is searched as a whole (case insensitive). If the user searches for *dummy search query* only the messages that include the full expression *dummy search query* (no matter the number of occurrences of the expression in the string we're looking into) will be highlighted;
  * **Note 1** : the search works as an intersection of the four possible tags. Only if a message matches all the search criterias is marked as a valid search result;
  * **Note 2** : there might only exist one occurrence of each tag per search query. A search with *from:user1 from:user2* is considered invalid;
  * **Note 3** : the search tags are localized. The default language is English but, if the device is in Portuguese, the valid search tags will be *antes*, *depois* and *de* for before, after and from respectively. Currently only portuguese is supported as an exception for English.

## Libraries used
* Retrofit 2 (and associated stack) for networking;
* Glide for image loading;
* JDeferred for promises support;
* Realm for local database wrapping;
* Only the libraries that don't belong to the compatibility pack were mentioned;
