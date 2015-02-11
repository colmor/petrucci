# Petrucci Player

----
## About
The purpose of this project was to build and Android application which enables tablet users to browse [imslp.org](imslp.org) in an easier fashion. The website is full of public domain sheet music (~12,000 composers) and, although a great resource, isn't exactly a pleasure to browse.

This project is largely incomplete/poorly implemented, so I'll say what it does (most of the time) before I list the improvements that it needs.

1. Scrapes composers from imslp.org and displays them as a clickable list. The scraping was done using [JSoup](http://jsoup.org). The list can be shrunk down to more manageable chunks by typing into a search box.
2. Scrapes a list of pieces available for a given composer.
3. *Usually scrapes a list of available scores for a given piece*. This breaks. **A lot**. So far the page element responsible for this has eluded me.
4. Downloads scores to a local library where they are categorised by composer and allows the user to view them in an external PDF viewer.

####Improvements
1. Needs a sqlite database to store the retrieved list of composers. Otherwise the app has to scrape ~60 pages with 200 composers/p every time you want to find new scores.
2. The interface needs work. Navigation is currently an afterthought mixed with a really unrewarding puzzle. This shouldn't take a huge effort to overhaul, I *think*.
3. When viewing scores for a given piece not enough information is given about the scores (though this is presented in a mysterious manner on the website too).
4. There's definitely more.

----
