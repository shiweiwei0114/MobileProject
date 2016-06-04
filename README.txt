README FILE

Product Name: ProterView
Type:     Android Mobile App
Authors: Weiwei Shi, Kyle Doan
Date: June 2, 2016 Version 1.1

----------------------------------------------------------------------------------------------------------------------
CONTENTS
I.		HOW TO START
II.		FEATURES OF THE APP
III.		SOFTWARE 
IV.             RESPONSES TO PEER-REVIEW


----------------------------------------------------------------------------------------------------------------------
I.		HOW TO START


-- Access the App --
Drag the .apk file to your emulator.


-- Login page credential as below --
-- We also encourage you to register an account with us by clicking "New to Proterview? Register here." -- 

EMAIL: proterview@uw.edu
PASSWORD:  visitorpassword


----------------------------------------------------------------------------------------------------------------------
II.		FEATURES OF THE APP (Implemented 100% of the use cases in our proposal)


1). Uses web services to retrieve or store data for the app.
    	-- We used web service to retrieve and store user information, question details and answer details.
	

2). Use Case 1 & 2: Sign-In and Registration with custom account
	-- The app allows the user to register and login with custom account.
        -- After the user register an account with us, it will sign-in the user automatically.
	-- This feature is fully functional with validation check and proper navigation. 
	-- Extra feature: password is encryted.       
	
3). Use Case 3: View Interview Questions
	-- The user who logged in can view a list of the interview questions and post date and time.
        -- The list of question is sorted by the post date and time. The most recent one will be always at the top of the list.
	-- When the user clicks on the question, it will go to the question detail page.
	-- The detail page includes the question, the user who posted, created time, and the company name who asked that question
        -- and other users' replies. 

4). Use Case 4: Submit Interview Questions
	-- The user can add/post interview questions by clicking the red button with the plus sign at the right corner.
	-- The user can put the question detail, which company asked that question and put tags for that question.
        -- "Tags" EditText is auto complete text.
	-- After clicking the "ADD QUESTION" button, the question will be seen by every user who has account with us.

5). Use Case 5: Reply A Question
	-- The user can contribute an answer to an interview question.
	-- After the user finishes editing his/her answer and hit "REPLY" button, the user can see his/her answer immediately.

6). Save the data to the device's storage.
	-- Utilize SharedPreferences and SQLite.
	-- We used SharedPreference to store sign-in information. Because this will help the user only need log in once, 
           -- and remove the log-in screen if the user don't hit log out.
	-- We used SQLite to store Questions and Tags information. Question and Tags table are stored for the purpose of the search by Tag.
           -- After download data from server, we drop the table and then create a new table exactly from the server to SQLite.
           -- So when we search question by tag, we just need to go to SQLite to query. No need to handle connection to server.

7). Content Sharing feature.
	-- In our app, if the user finds a cool question and wants to share with his/her friend, he/she can simply click on the 
	-- "Share" floating button located at the right corner in question detail page.

8). About Us Page.
	-- About us page has a short self-introduction about us. (The user is able to see our beautiful pictures there.)

9). Log Out feature: user is able to log out by click the "LOG OUT" button on the top right corner or the "Log Out" in the side bar.

10). Meeting Notes Link: https://docs.google.com/document/d/1TRqnvjLUGaeZ_K_BF21SZPOtfRbXC1MmFDhPZjubVck /edit?usp=sharing

	
----------------------------------------------------------------------------------------------------------------------
III.		 SOFTWARE 

For our app, we used Android Studio to build front-end and back-end code.
We store and retrieve data for the app using the web service by writing PHP scripts.


----------------------------------------------------------------------------------------------------------------------
IV.		 RESPONSES TO PEER-REVIEW 

About peer's feedbacks, we would like to provide the following responses:
1). The reason "About Us" button didn't work in Phase 1 because we haven't started to implement our navigation drawer(side bar) yet.
    The navigation drawer is fully implemented in the final version.

2). "Why do the interview questions need the data and time down to second?"
     Because we sort our questions by the user's post date and time. So the most recent one always shows on the top of the list.


----------------------------------------------------------------------------------------------------------------------

Enjoy!

The Proterview Team.
